package cn.qtone.library.student.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.ContantsUtil;
import cn.qtone.common.components.syspurview.check.filter.priviilege.PriviilegeFilter;
import cn.qtone.common.components.syspurview.core.group.domain.UserGroup;
import cn.qtone.common.components.syspurview.core.user.domain.IUser;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.mvc.view.spring.TextView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.common.utils.crypto.Encrypt;
import cn.qtone.common.utils.upload.DownloadInter;
import cn.qtone.common.utils.upload.UploadFactory;
import cn.qtone.library.XxptContants;
import cn.qtone.library.agency.service.AgencyService;
import cn.qtone.library.city.service.CityService;
import cn.qtone.library.school.domain.School;
import cn.qtone.library.school.service.SchoolService;
import cn.qtone.library.student.domain.ErrorStudent;
import cn.qtone.library.student.service.StudentService;
import cn.qtone.library.xxptclass.domain.ZfptClass;
import cn.qtone.library.xxptclass.service.XxptClassService;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;
import cn.qtone.qtoneframework.web.view.poi.ExcelUtils;

/**
 * 学校管理 - 学生管理
 * 
 * @author 邝炳研
 * @version 1.0
 */
public class StudentController extends SimpleManageController<IUser, StudentService> {

	StudentService studentService;
	SchoolService schoolService;
	AgencyService agencyService;
	CityService cityService;
	XxptClassService zfptClassService;
	String importPage;
	String createPage;
	private String checkLoginPage;

	
	@Override
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String, Object> map = this.getMapWithUser(request);
		User user = (User) map.get("user");
		boolean all = PriviilegeFilter.isAllowd(user, "/library/student.do", "all");
		map.put("editGradeHtmlSelect", XxptContants.getGradeSelectHtml("1,2,3,4,5", "-1"));
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/student.do", "area")){
			map.put("disableTown", "disabled");
		//本校	
		}else{
			map.put("disableTown", "disabled");
			map.put("disableAgency", "disabled");
			map.put("disableSchool", "disabled");
			map.put("editGradeHtmlSelect", this.zfptClassService.getGradeSelectHtml(user.getSchool_id(), -1));
			map.put("areaSel", "none");
		}
		map.put("citySelectHtml", this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(user.getTown_id())));
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(String.valueOf(user.getTown_id()) , user.getAgency_id()));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(String.valueOf(user.getAgency_id()) , user.getSchool_id()));
		//map.put("editGradeHtmlSelect", XxptContants.getGradeSelectHtml("1,2,3,4,5", "-1"));
		//map.put("editParentMobileHtmlSelect", ContantsUtil.getSystemContantsSelectHtml(XxptContants.PARENTMOBILETYPE,"-1"));
		return new ModelAndView(getIndexPage(), map);
	}

	@Override
	public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> map = this.getMapWithUser(request);
		//StudentQVO qvo = StudentQVOUtil.getStudentQVO(request);
		//qvo.setId(ServletUtil.getIntParam(request, "userId"));
		IUser student  = this.studentService.get(ServletUtil.getIntParam(request, "userId"));
		map.put("student", student);
		UserGroup userGroup  = studentService.getUserGroup(student.getGroupId());
		if(userGroup!=null)
			map.put("userGroupName", userGroup.getGroupName());
		map.put("userType",XxptContants.MANAGE_USER_TYPE.get(student.getUser_type()));
		return new ModelAndView(this.getShowPage(), map);
	}

	@SuppressWarnings("unchecked")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> map = this.getMapWithUser(request);
			int curPage = this.getCurrentPage(request); // 当前查询页数
			Criteria criteria = getDomainService().createCriteria(domainClass);
			criteria.add(Restrictions.eq("status", 0));//正常用户
			criteria.add(Restrictions.eq("user_type", "4"));//学生用户
			
			Integer queryCityId = ServletUtil.getIntParam(request, "queryCityId");
			Integer queryAgencyId=ServletUtil.getIntParam(request, "queryAgencyId");
			Integer querySchoolId=ServletUtil.getIntParam(request, "querySchoolId");
			Integer queryGradeId=ServletUtil.getIntParam(request, "queryGradeId");
			Integer queryClassNameId=ServletUtil.getIntParam(request, "queryClassNameId");
			String studentName  =ServletUtil.removeSpace(request, "studentName");
			String queryLoginName  =ServletUtil.removeSpace(request, "queryLoginName");
			String queryIdCard  =ServletUtil.removeSpace(request, "queryIdCard");
			
			User user = (User) map.get("user");
			boolean all = PriviilegeFilter.isAllowd(user, "/library/student.do", "all");
			//本市
			if(all){
				
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/student.do", "area")){
				queryCityId = user.getTown_id();
				queryAgencyId = user.getAgency_id();
			//本校	
			}else{
				querySchoolId =  user.getSchool_id();
			}
			if(querySchoolId>0)
				criteria.add(Restrictions.eq("school.id", querySchoolId));
			else if(queryAgencyId>0){
				Criteria schoolCriteria =criteria.createCriteria("school");
				schoolCriteria.add(Restrictions.eq("agency.id", queryAgencyId));
			}else if(queryCityId>0){
				Criteria schoolCriteria =criteria.createCriteria("school");
				Criteria agencyCriteria = schoolCriteria.createCriteria("agency");
				agencyCriteria.add(Restrictions.eq("city.id", queryCityId));
			}
			
			if(queryGradeId>0)
				criteria.add(Restrictions.eq("grade_id", queryGradeId));
			if(queryClassNameId>0)
				criteria.add(Restrictions.eq("zfptClass.id", queryClassNameId));
			if(studentName!=null&&studentName.length()>0)
				criteria.add(Restrictions.like("realName", new StringBuilder("%").append(studentName).append("%").toString()));
			if(queryLoginName!=null&&queryLoginName.length()>0)
				criteria.add(Restrictions.like("loginName", new StringBuilder("%").append(queryLoginName).append("%").toString()));
			if(queryIdCard!=null&&queryIdCard.length()>0)
				criteria.add(Restrictions.like("id_card", new StringBuilder("%").append(queryIdCard).append("%").toString()));
			
			
			
			//criteria.addOrder(Order.asc("town_id"));
			//criteria.addOrder(Order.asc("agency_id"));
			criteria.addOrder(Order.asc("school.id"));
			criteria.addOrder(Order.asc("grade_id"));
			criteria.addOrder(Order.asc("grade_id"));
			criteria.addOrder(Order.asc("zfptClass.id"));
			Page page = getDomainService().pagedQuery(criteria, curPage, Page.DEFAULT_PAGE_SIZE+5);
			page.setPaginate(this.getAjaxPage(request, curPage, page, "student" + "Jump"));
			map.put("page", page);
			
			
			return new ModelAndView(this.getListPage(), map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		
	}

	@Override
	public ModelAndView create(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		User user = (User) map.get("user");
		boolean all = PriviilegeFilter.isAllowd(user, "/library/student.do", "all");
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/student.do", "area")){
			map.put("disableTown", "disabled");
		//本校	
		}else{
			map.put("disableTown", "disabled");
			map.put("disableAgency", "disabled");
			map.put("disableSchool", "disabled");
		}
		map.put("citySelectHtml", this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(user.getTown_id())));
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(String.valueOf(user.getTown_id()) , user.getAgency_id()));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(String.valueOf(user.getAgency_id()) , user.getSchool_id()));
		map.put("editGradeHtmlSelect", this.zfptClassService.getGradeSelectHtml(user.getSchool_id(), -1));
		map.put("editGenderHtmlSelect", ContantsUtil.getSystemContantsSelectHtml(XxptContants.GENDERTYPE, "1"));
		map.put(getDomainName(), doNewDomain());
		map.put("groupSelectHtml", this.studentService.getGroupSelectHtml("4"));
		map.put("userTypeOpiton", ContantsUtil.getSystemContantsSelectHtml(XxptContants.MANAGE_USER_TYPE, "4"));//学生用户类型
		return new ModelAndView(this.getCreatePage(), map);
	}

	@Override
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String, Object> map = this.getMapWithUser(request);
		IUser student = doGetDomain(request);
		User user = (User) map.get("user");
		boolean all = PriviilegeFilter.isAllowd(user, "/library/student.do", "all");
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/student.do", "area")){
			map.put("disableTown", "disabled");
		//本校	
		}else{
			map.put("disableTown", "disabled");
			map.put("disableAgency", "disabled");
			map.put("disableSchool", "disabled");
		}
		
		String citySelectHtml = this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(student.getTown_id()));//默认东区
		map.put("citySelectHtml", citySelectHtml);
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(String.valueOf(student.getTown_id()), student.getAgency_id()));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(student.getAgency_id().toString(),student.getSchool().getId()));
		map.put("editGradeHtmlSelect", this.zfptClassService.getGradeSelectHtml(student.getSchool().getId(), student.getGrade_id()));
		map.put("editGenderHtmlSelect", ContantsUtil.getSystemContantsSelectHtml(XxptContants.GENDERTYPE, student.getGender().toString()));
		map.put("editClassNameHtmlSelect", this.zfptClassService.getClassNameSelectHtml(user,student.getSchool().getId(), student.getGrade_id(), student.getZfptClass().getId()));
		map.put("student", student);
		map.put("groupSelectHtml", this.studentService.getGroupSelectHtml(String.valueOf(student.getGroupId())));
		map.put("userTypeOpiton", ContantsUtil.getSystemContantsSelectHtml(XxptContants.MANAGE_USER_TYPE, student.getUser_type()));
		return new ModelAndView(getEditPage(), map);
	}

	/**
	 * 取页面返回值
	 * 
	 * @param request
	 * @param student
	 * @return
	 */
	private IUser getStudentByRequest(HttpServletRequest request, IUser student) {
		if (ServletUtil.getIntParam(request, "id") >0)
			student=getDomainService().get(ServletUtil.getIntParam(request, "id"));
		Integer editCityId = ServletUtil.getIntParam(request, "editCityId");
		Integer editAgencyId = ServletUtil.getIntParam(request, "editAgencyId");
		Integer editSchoolId = ServletUtil.getIntParam(request, "editSchoolId");
		Integer editClassNameId = ServletUtil.getIntParam(request, "editClassNameId");
		
		if(editClassNameId>0){
			ZfptClass zfptClass = new ZfptClass();
			zfptClass.setId(editClassNameId);
			student.setZfptClass(zfptClass);
		}
		
		User user = (User) UserUtil.getUserBean(request);
		boolean all = PriviilegeFilter.isAllowd(user, "/library/student.do", "all");
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/student.do", "area")){
			editCityId = user.getTown_id();
		//本校	
		}else{
			editCityId = user.getTown_id();
			editAgencyId = user.getAgency_id();
			editSchoolId = user.getSchool_id();
		}
		student.setTown_id(editCityId);
		student.setAgency_id(editAgencyId);
		if (editSchoolId>0){
			School school = new School();
			school.setId(editSchoolId);
			student.setSchool(school);
		}
		
		student.setGrade_id(ServletUtil.getIntParam(request, "editGradeId"));
		student.setId_card(ServletUtil.removeSpace(request, "id_card"));
		student.setRealName(ServletUtil.removeSpace(request, "realName"));
		student.setGender(ServletUtil.getIntParam(request, "gender"));
		student.setLoginName(ServletUtil.removeSpace(request, "loginName"));
		//student.setUnique_no(ServletUtil.removeSpace(request, "unique_no"));
		student.setBirthday(ServletUtil.removeSpace(request, "birthday"));

		return student;
	}
	
	
	@Override
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		IUser student = (IUser) getCommandObject(request, getDomainClass());
		student = this.getStudentByRequest(request, student);
		student.setGroupId(4);
		student.setUser_type("4");
		student.setStatus(0);
		student.setIsLock(0);
		student.setIsSuper(0);
		if (ServletUtil.getIntParam(request, "id") == 0) {
			student.setPlainCode(ServletUtil.removeSpace(request, "loginPassword"));
			student.setLoginPassword(Encrypt.MD5(ServletUtil.removeSpace(request, "loginPassword")));
			getDomainService().save(student);
		}
		else {
			getDomainService().saveOrUpdate(student);
		}
		AjaxView view = new AjaxView(true, "操作成功！");
		view.setProperty("refresh", true);
		return new ModelAndView(view);
	}

	@Override
	public ModelAndView remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String[] idStrs = request.getParameterValues(idName);
		if (log.isInfoEnabled()) {
			log.info("要删除的对象ID为：" + StringUtil.join(idStrs, ","));
		}
		for (String idStr : idStrs) {
			if (idStr.length() > 0) {
				getDomainService().removeById((Serializable) ConvertUtils.convert(idStr, idClass));
				studentService.delBorrow(Integer.parseInt(idStr));//删除关联借书记录
			}
		}
		AjaxView view = new AjaxView(true, "操作成功！删除学生信息成功!");
		view.setProperty("refresh", true);
		return new ModelAndView(view);
	}

	/**
	 * 导入学生初始化
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView importStudentInit(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> map = this.getMapWithUser(request);
		//String agencySelectHtml = this.agencyService.getAgencyOptionHtmlFromCity("-1", -1);
		//map.put("agencySelectHtml", agencySelectHtml);

		//map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(ServletUtil.removeSpace(request,
		//		"agency_id"), -1));
		User user = (User) map.get("user");
		boolean all = PriviilegeFilter.isAllowd(user, "/library/student.do", "all");
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/student.do", "area")){
			map.put("disableTown", "disabled");
		//本校	
		}else{
			map.put("disableTown", "disabled");
			map.put("disableAgency", "disabled");
			map.put("disableSchool", "disabled");
		}
		map.put("citySelectHtml", this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(user.getTown_id())));
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(String.valueOf(user.getTown_id()) , user.getAgency_id()));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(String.valueOf(user.getAgency_id()) , user.getSchool_id()));
		map.put("editGradeHtmlSelect", this.zfptClassService.getGradeSelectHtml(user.getSchool_id(), -1));
		map.put("groupSelectHtml", this.studentService.getGroupSelectHtml("4"));
		return new ModelAndView(this.importPage, map);
	}

	/**
	 * 模板下载
	 */
	public ModelAndView download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		DownloadInter download = UploadFactory.getDownloadInstance(response);
		try {
			
			download.download(request.getSession().getServletContext().getRealPath("/")
					+ "/template/student_import_template.xls", "UTF-8");
		} 
		catch (Exception ex) {
			
			ex.printStackTrace();
			return new ModelAndView(new TextView("未知原因导致文件下载失败！"));
		}
		return null;
	}

	/**
	 * 导入学生后的提交操作.
	 * @throws IOException 
	 */
	public ModelAndView importStudentSubmit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<ErrorStudent> errorList = new ArrayList<ErrorStudent>();
		List<Integer> idList = new ArrayList<Integer>();
		ErrorStudent errorStudent = null;
		int line=2;
		try {
			for (IUser student : this.studentService.getImportStudentData(request.getSession().getServletContext()
					.getRealPath("/")+ ServletUtil.removeSpace(request, "importattach_path"))) {
				try{
					line++;
					if (student.getRealName() == null||student.getRealName().trim().length()==0){
						errorStudent = new ErrorStudent();
						errorStudent.setLine(line);
						errorStudent.setBirthday(student.getBirthday());
						errorStudent.setGender(student.getGender_str2());
						errorStudent.setId_card(student.getId_card());
						errorStudent.setLoginName(student.getLoginName());
						errorStudent.setRealName(student.getRealName());
						errorStudent.setErrorReason("请检查姓名是否有误！");
						errorList.add(errorStudent);
						continue;
					}
					if (student.getLoginName() == null || student.getLoginName().length()==0){
						errorStudent = new ErrorStudent();
						errorStudent.setLine(line);
						errorStudent.setBirthday(student.getBirthday());
						errorStudent.setGender(student.getGender_str2());
						errorStudent.setId_card(student.getId_card());
						errorStudent.setLoginName(student.getLoginName());
						errorStudent.setRealName(student.getRealName());
						errorStudent.setErrorReason("请检查学籍号是否有误！");
						errorList.add(errorStudent);
						continue;
					}
					if(this.studentService.checkLoginName(student.getLoginName())>0){//学籍号出现重复不进行保存
						errorStudent = new ErrorStudent();
						errorStudent.setLine(line);
						errorStudent.setBirthday(student.getBirthday());
						errorStudent.setGender(student.getGender_str2());
						errorStudent.setId_card(student.getId_card());
						errorStudent.setLoginName(student.getLoginName());
						errorStudent.setRealName(student.getRealName());
						errorStudent.setErrorReason("学籍号出现重复，请检查是否有误！");
						errorList.add(errorStudent);
						continue;
					}
					if(student.getId_card()!=null &&student.getId_card().length()>0){
						if(this.studentService.checkICCard(student.getId_card())>0){//IC卡出现重复不进行保存
							errorStudent = new ErrorStudent();
							errorStudent.setLine(line);
							errorStudent.setBirthday(student.getBirthday());
							errorStudent.setGender(student.getGender_str2());
							errorStudent.setId_card(student.getId_card());
							errorStudent.setLoginName(student.getLoginName());
							errorStudent.setRealName(student.getRealName());
							errorStudent.setErrorReason("IC卡号出现重复，请检查是否有误！");
							errorList.add(errorStudent);
							continue;
						}
					}
					
					Integer importClassId = ServletUtil.getIntParam(request, "importClassId");
					ZfptClass zfptClass = new ZfptClass();
					if(importClassId>0){
						zfptClass.setId(importClassId);
						student.setZfptClass(zfptClass);
					}
					student.setGrade_id(ServletUtil.getIntParam(request, "importGradeId"));
					//student.setAgency_id(ServletUtil.getIntParam(request, "importAgencyId"));
					//student.setTown_id(ServletUtil.getIntParam(request, "importCityId"));
					
					User user = (User) UserUtil.getUserBean(request);
					Integer editSchoolId = ServletUtil.getIntParam(request, "importSchoolId");
					boolean all = PriviilegeFilter.isAllowd(user, "/library/student.do", "all");
					//本市
					if(all){
						student.setTown_id(ServletUtil.getIntParam(request, "importCityId"));
						student.setAgency_id(ServletUtil.getIntParam(request, "importAgencyId"));
						
						
					//本镇区	
					}else if (PriviilegeFilter.isAllowd(user, "/library/student.do", "area")){
						student.setTown_id(user.getTown_id());
						student.setAgency_id(ServletUtil.getIntParam(request, "importAgencyId"));
					//本校	
					}else{
						student.setTown_id(user.getTown_id());
						student.setAgency_id(user.getAgency_id());
						editSchoolId =user.getSchool_id();
						
					}
					School school = null;
					if (editSchoolId>0)
						school = schoolService.get(editSchoolId);
					if(school!=null)
						student.setSchool(school);
					
					student.setGroupId(4);//学生用户组
					student.setUser_type("4");//学生用户类型
					student.setPlainCode("123456");//初始化用户密码
					student.setLoginPassword("e10adc3949ba59abbe56e057f20f883e");//初始化用户密码
					student.setGender(student.containGender());
					student.setStatus(0);
					student.setIsLock(0);
					student.setIsSuper(0);
					this.studentService.save(student);
					idList.add(student.getUserId());
				}catch(Exception ex){
					errorStudent = new ErrorStudent();
					errorStudent.setLine(line);
					errorStudent.setBirthday(student.getBirthday());
					errorStudent.setGender(student.getGender_str());
					errorStudent.setId_card(student.getId_card());
					errorStudent.setLoginName(student.getLoginName());
					errorStudent.setRealName(student.getRealName());
					//提示导入失败原因
					errorStudent.setErrorReason("");
					errorList.add(errorStudent);
					ex.printStackTrace();
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			ServletOutputStream out = response.getOutputStream();
			out.print("<script>alert('数据导入失败！');window.history.back();</script>");
			out.close();
			return  null;
		}
		//AjaxView view=null;
		String msg=null;;
		if(errorList.size()>0){
			if(idList.size()>0){
				StringBuilder idStr = new StringBuilder();
				for (Integer id:idList){
					idStr.append(id);
					idStr.append(",");
				}
				idStr.delete(idStr.length()-1, idStr.length());
				this.studentService.delBath(idStr.toString());//删除保存的。。
			}
			Map<String, Object> map = this.getMap();
			map.put("errorList", errorList);
			return new ModelAndView("/library/student/student_error_list", map);//显示错误提示页面
			
			/*
			//view = new AjaxView(true, "部分导入失败！请下载导入失败名单进行修改！");
			try {
			HSSFWorkbook workbook = this.studentService.getStudentErrorExport(errorList, request.getSession().getServletContext()
					.getRealPath("/")
					+ "/template/student_error_template.xls");
				ExcelUtils.download(response, workbook, "导入失败学生数据表.xls");
				msg = "请下载“导入失败学生数据表” 修改完继续上传";
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			*/
		}else
			//view = new AjaxView(true, "导入成功！");
			//view.setProperty("refresh", true);
			msg = "<script>alert('数据导入成功！');window.history.back();</script>";
		ServletOutputStream out = response.getOutputStream();
		out.print(msg);
		out.close();
		return  null;
	}

	/**
	 * 导出学生数据
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView downloadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			Map<String, Object> map = this.getMapWithUser(request);
			int curPage = this.getCurrentPage(request); // 当前查询页数
			Criteria criteria = getDomainService().createCriteria(domainClass);
			criteria.add(Restrictions.eq("status", 0));//正常用户
			criteria.add(Restrictions.eq("user_type", "4"));//学生用户
			
			Integer queryCityId = ServletUtil.getIntParam(request, "queryCityId");
			Integer queryAgencyId=ServletUtil.getIntParam(request, "queryAgencyId");
			Integer querySchoolId=ServletUtil.getIntParam(request, "querySchoolId");
			Integer queryGradeId=ServletUtil.getIntParam(request, "queryGradeId");
			Integer queryClassNameId=ServletUtil.getIntParam(request, "queryClassNameId");
			String studentName  =ServletUtil.removeSpace(request, "studentName");
			String queryLoginName  =ServletUtil.removeSpace(request, "queryLoginName");
			String queryIdCard  =ServletUtil.removeSpace(request, "queryIdCard");
			
			User user = (User) map.get("user");
			boolean all = PriviilegeFilter.isAllowd(user, "/library/student.do", "all");
			//本市
			if(all){
				
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/student.do", "area")){
				queryCityId = user.getTown_id();
				queryAgencyId = user.getAgency_id();
			//本校	
			}else{
				querySchoolId =  user.getSchool_id();
			}
			if(querySchoolId>0)
				criteria.add(Restrictions.eq("school.id", querySchoolId));
			else if(queryAgencyId>0){
				Criteria schoolCriteria =criteria.createCriteria("school");
				schoolCriteria.add(Restrictions.eq("agency.id", queryAgencyId));
			}else if(queryCityId>0){
				Criteria schoolCriteria =criteria.createCriteria("school");
				Criteria agencyCriteria = schoolCriteria.createCriteria("agency");
				agencyCriteria.add(Restrictions.eq("city.id", queryCityId));
			}
			
			if(queryGradeId>0)
				criteria.add(Restrictions.eq("grade_id", queryGradeId));
			if(queryClassNameId>0)
				criteria.add(Restrictions.eq("zfptClass.id", queryClassNameId));
			if(studentName!=null&&studentName.length()>0)
				criteria.add(Restrictions.like("realName", new StringBuilder("%").append(studentName).append("%").toString()));
			if(queryLoginName!=null&&queryLoginName.length()>0)
				criteria.add(Restrictions.like("loginName", new StringBuilder("%").append(queryLoginName).append("%").toString()));
			if(queryIdCard!=null&&queryIdCard.length()>0)
				criteria.add(Restrictions.like("id_card", new StringBuilder("%").append(queryIdCard).append("%").toString()));
			
			criteria.addOrder(Order.asc("school.id"));
			criteria.addOrder(Order.asc("grade_id"));
			criteria.addOrder(Order.asc("grade_id"));
			criteria.addOrder(Order.asc("zfptClass.id"));
			
			List<IUser> list = criteria.list();
			if (list.size() > 0) {

				HSSFWorkbook workbook = this.studentService.getStudentExport(list, request.getSession().getServletContext()
						.getRealPath("/")
						+ "/template/student_export_template.xls");
				try {
					
					ExcelUtils.download(response, workbook, "学生数据表.xls");
				} 
				catch (Exception e) {
					
					e.printStackTrace();
				}
			} else {
				
				String msg = "<script>alert('根据所选条件,导出条目为0,请重新选择！');window.history.back();</script>";
				ServletOutputStream out = response.getOutputStream();
				out.print(msg);
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	

	public void setStudentService(StudentService studentService) {
		this.studentService = studentService;
	}

	public void setAgencyService(AgencyService agencyService) {
		this.agencyService = agencyService;
	}

	public void setCityService(CityService cityService) {
		this.cityService = cityService;
	}

	public void setSchoolService(SchoolService schoolService) {
		this.schoolService = schoolService;
	}

	
	public void setImportPage(String importPage) {
		this.importPage = importPage;
	}

	public void setZfptClassService(XxptClassService zfptClassService) {
		this.zfptClassService = zfptClassService;
	}


	public String getCreatePage() {
		return createPage;
	}

	public void setCreatePage(String createPage) {
		this.createPage = createPage;
	}

	public String getCheckLoginPage() {
		return checkLoginPage;
	}

	public void setCheckLoginPage(String checkLoginPage) {
		this.checkLoginPage = checkLoginPage;
	}


}