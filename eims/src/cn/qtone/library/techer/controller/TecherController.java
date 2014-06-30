package cn.qtone.library.techer.controller;

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
import cn.qtone.library.techer.domain.ErrorTecher;
import cn.qtone.library.techer.service.TecherService;
import cn.qtone.library.xxptclass.domain.ZfptClass;
import cn.qtone.library.xxptclass.service.XxptClassService;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;
import cn.qtone.qtoneframework.web.view.poi.ExcelUtils;

/**
 * 学校管理 - 教师管理
 * 
 * @author 
 * @version 1.0
 */
public class TecherController extends SimpleManageController<IUser, TecherService> {

	TecherService techerService;
	SchoolService schoolService;
	AgencyService agencyService;
	CityService cityService;
	XxptClassService zfptClassService;
	String importPage;
	String createPage;
	private String checkLoginPage;

	public String getCheckLoginPage() {
		return checkLoginPage;
	}

	public void setCheckLoginPage(String checkLoginPage) {
		this.checkLoginPage = checkLoginPage;
	}

	@Override
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String, Object> map = this.getMapWithUser(request);
		User user = (User) map.get("user");
		boolean all = PriviilegeFilter.isAllowd(user, "/library/techer.do", "all");
		map.put("citySelectHtml", this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(user.getTown_id())));
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(String.valueOf(user.getTown_id()) , user.getAgency_id()));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(String.valueOf(user.getAgency_id()) , user.getSchool_id()));
		map.put("editGradeHtmlSelect", XxptContants.getGradeSelectHtml("1,2,3,4,5", "-1"));
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/techer.do", "area")){
			map.put("disableTown", "disabled");
		//本校	
		}else{
			map.put("disableTown", "disabled");
			map.put("disableAgency", "disabled");
			map.put("disableSchool", "disabled");
			map.put("editGradeHtmlSelect", this.zfptClassService.getGradeSelectHtml(user.getSchool_id(), -1));
			map.put("areaSel", "none");
		}
		return new ModelAndView(getIndexPage(), map);
	}

	@Override
	public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> map = this.getMapWithUser(request);
		//TecherQVO qvo = TecherQVOUtil.getTecherQVO(request);
		//qvo.setId(ServletUtil.getIntParam(request, "userId"));
		IUser techer = this.techerService.get(ServletUtil.getIntParam(request, "userId"));
		map.put("techer", techer);
		UserGroup userGroup  = techerService.getUserGroup(techer.getGroupId());
		if(userGroup!=null)
			map.put("userGroupName", userGroup.getGroupName());
		map.put("userType",XxptContants.MANAGE_USER_TYPE.get(techer.getUser_type()));
		return new ModelAndView(this.getShowPage(), map);
	}

	@SuppressWarnings("unchecked")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> map = this.getMapWithUser(request);
			int curPage = this.getCurrentPage(request); // 当前查询页数
			Criteria criteria = getDomainService().createCriteria(domainClass);
			criteria.add(Restrictions.eq("status", 0));//正常用户
			criteria.add(Restrictions.eq("user_type", "5"));//学生用户
			
			Integer queryCityId = ServletUtil.getIntParam(request, "queryCityId");
			Integer queryAgencyId=ServletUtil.getIntParam(request, "queryAgencyId");
			Integer querySchoolId=ServletUtil.getIntParam(request, "querySchoolId");
			String techerName  =ServletUtil.removeSpace(request, "techerName");
			String queryloginName  =ServletUtil.removeSpace(request, "queryloginName");
			String idCard  =ServletUtil.removeSpace(request, "idCard");
			
			
			
			User user = (User) map.get("user");
			boolean all = PriviilegeFilter.isAllowd(user, "/library/techer.do", "all");
			//本市
			if(all){
				
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/techer.do", "area")){
				queryCityId =  user.getTown_id();
			//本校	
			}else{
				querySchoolId = user.getSchool_id();
			}
			
			
			
			if(querySchoolId>0)
				criteria.add(Restrictions.eq("school.id", querySchoolId));
			else if(queryAgencyId>0)
				criteria.add(Restrictions.eq("agency_id", queryAgencyId));
			else if(queryCityId>0)
				criteria.add(Restrictions.eq("town_id", queryCityId));
			
			if(techerName!=null&&techerName.length()>0)
				criteria.add(Restrictions.like("realName", new StringBuilder("%").append(techerName).append("%").toString()));
			if(queryloginName!=null&&queryloginName.length()>0)
				criteria.add(Restrictions.like("loginName", new StringBuilder("%").append(queryloginName).append("%").toString()));
			if(idCard!=null&&idCard.length()>0)
				criteria.add(Restrictions.like("id_card", new StringBuilder("%").append(idCard).append("%").toString()));
			
			
			criteria.addOrder(Order.asc("town_id"));
			criteria.addOrder(Order.asc("agency_id"));
			criteria.addOrder(Order.asc("school.id"));
			criteria.addOrder(Order.asc("grade_id"));
			criteria.addOrder(Order.asc("zfptClass.id"));
			Page page = getDomainService().pagedQuery(criteria, curPage, Page.DEFAULT_PAGE_SIZE);
			page.setPaginate(this.getAjaxPage(request, curPage, page, "techerJump"));
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
		boolean all = PriviilegeFilter.isAllowd(user, "/library/techer.do", "all");
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/techer.do", "area")){
			map.put("disableTown", "disabled");
		//本校	
		}else{
			map.put("disableTown", "disabled");
			map.put("disableAgency", "disabled");
			map.put("disableSchool", "disabled");
		}
		map.put("citySelectHtml", this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(user.getTown_id())));
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(String.valueOf(user.getTown_id()) , user.getAgency_id()));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(String.valueOf(user.getAgency_id()) , user.getSchool_id()==null?-1:user.getSchool_id()));
		map.put("editGradeHtmlSelect", this.zfptClassService.getGradeSelectHtml(user.getSchool_id()==null?-1:user.getSchool_id(), -1));
		map.put("editGenderHtmlSelect", ContantsUtil.getSystemContantsSelectHtml(XxptContants.GENDERTYPE, "1"));
		//map.put("relationSelectHtml", this.xxptParamService.getParamSelectHtml(XxptContants.XXPT_PARAM_TYPE_ENUM.JZGX,"5"));
		map.put(getDomainName(), doNewDomain());
		map.put("groupSelectHtml", this.techerService.getGroupSelectHtml("5"));//教师用户组
		map.put("userTypeOpiton", ContantsUtil.getSystemContantsSelectHtml(XxptContants.MANAGE_USER_TYPE, "5"));//教师用户类型
		
		return new ModelAndView(this.getCreatePage(), map);
	}

	@Override
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			Map<String, Object> map = this.getMapWithUser(request);
			IUser techer = doGetDomain(request);
			User user = (User) map.get("user");
			boolean all = PriviilegeFilter.isAllowd(user, "/library/techer.do", "all");
			//本市
			if(all){
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/techer.do", "area")){
				map.put("disableTown", "disabled");
			//本校	
			}else{
				map.put("disableTown", "disabled");
				map.put("disableAgency", "disabled");
				map.put("disableSchool", "disabled");
			}
			
			String citySelectHtml = this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(techer.getTown_id()));//默认东区
			map.put("citySelectHtml", citySelectHtml);
			map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(ServletUtil.removeSpace(request,"city_id"), techer.getAgency_id()));
			map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(String.valueOf(techer.getAgency_id()),techer.getSchool()!=null?techer.getSchool().getId():-1));
			map.put("editGradeHtmlSelect", this.zfptClassService.getGradeSelectHtml(techer.getSchool()==null?-1:techer.getSchool().getId(), techer.getGrade_id()==null?-1:techer.getGrade_id()));
			map.put("editGenderHtmlSelect", ContantsUtil.getSystemContantsSelectHtml(XxptContants.GENDERTYPE, techer.getGender().toString()));
			map.put("editClassNameHtmlSelect", this.zfptClassService.getClassNameSelectHtml(user,techer.getSchool()==null?-1:techer.getSchool().getId(), techer.getGrade_id()==null?-1:techer.getGrade_id(), techer.getZfptClass()==null?-1:techer.getZfptClass().getId()));
			map.put("techer", techer);
			map.put("userGroups", techerService.getGroupList());
			map.put("userTypeOpiton", ContantsUtil.getSystemContantsSelectHtml(XxptContants.MANAGE_USER_TYPE, techer.getUser_type()));
			return new ModelAndView(getEditPage(), map);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	

	
	/**
	 * 取页面返回值
	 * 
	 * @param request
	 * @param techer
	 * @return
	 */
	private IUser getTecherByRequest(HttpServletRequest request, IUser techer) {
		if (ServletUtil.getIntParam(request, "id") >0)
			techer=getDomainService().get(ServletUtil.getIntParam(request, "id"));
		Integer editCityId = ServletUtil.getIntParam(request, "editCityId");
		Integer editAgencyId = ServletUtil.getIntParam(request, "editAgencyId");
		Integer editSchoolId = ServletUtil.getIntParam(request, "editSchoolId");
		Integer editGradeId = ServletUtil.getIntParam(request, "editGradeId");
		Integer editClassNameId = ServletUtil.getIntParam(request, "editClassNameId");
		if(editClassNameId>0){
			ZfptClass zfptClass  = new ZfptClass();
			zfptClass.setId(editClassNameId);
			techer.setZfptClass(zfptClass);
		}
		techer.setGroupId(ServletUtil.getIntParam(request, "groupId"));
		
		User user = (User) UserUtil.getUserBean(request);
		boolean all = PriviilegeFilter.isAllowd(user, "/library/techer.do", "all");
		//本市
		if(all){
			
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/techer.do", "area")){
			editCityId = user.getTown_id();
		//本校	
		}else{
			editCityId = user.getTown_id();
			editAgencyId = user.getAgency_id();
			editSchoolId = user.getSchool_id();
		}
		if(editCityId>0)
			techer.setTown_id(editCityId);
		if(editAgencyId>0)
			techer.setAgency_id(editAgencyId);
		
		if(editSchoolId>0){
			School school = new School();
			school.setId(editSchoolId);
			techer.setSchool(school);
		}
		if(editGradeId>0)
			techer.setGrade_id(editGradeId);
		techer.setId_card(ServletUtil.removeSpace(request, "id_card"));
		techer.setRealName(ServletUtil.removeSpace(request, "realName"));
		techer.setGender(ServletUtil.getIntParam(request, "gender"));
		techer.setLoginName(ServletUtil.removeSpace(request, "loginName"));
		//techer.setUnique_no(ServletUtil.removeSpace(request, "unique_no"));
		techer.setBirthday(ServletUtil.removeSpace(request, "birthday"));
		techer.setMobile(ServletUtil.removeSpace(request, "mobile"));
		techer.setPhone(ServletUtil.removeSpace(request, "phone"));
		techer.setEmail(ServletUtil.removeSpace(request, "email"));
		return techer;
	}
	
	@Override
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		IUser techer = (IUser) getCommandObject(request, getDomainClass());
		techer = this.getTecherByRequest(request, techer);
		techer.setGroupId(5);
		techer.setUser_type("5");
		techer.setStatus(0);
		techer.setIsLock(0);
		techer.setIsSuper(0);
		if (ServletUtil.getIntParam(request, "id") == 0) {
			techer.setPlainCode(ServletUtil.removeSpace(request, "loginPassword"));
			techer.setLoginPassword(Encrypt.MD5(ServletUtil.removeSpace(request, "loginPassword")));
			getDomainService().save(techer);
		}else {
			getDomainService().saveOrUpdate(techer);
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
				techerService.delBorrow(Integer.parseInt(idStr));
			}
		}
		AjaxView view = new AjaxView(true, "操作成功！删除教师信息!");
		view.setProperty("refresh", true);
		return new ModelAndView(view);
	}

	/**
	 * 导入教师初始化
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView importTecherInit(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> map = this.getMapWithUser(request);
		User user = (User) map.get("user");
		boolean all = PriviilegeFilter.isAllowd(user, "/library/techer.do", "all");
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/techer.do", "area")){
			map.put("disableTown", "disabled");
		//本校	
		}else{
			map.put("disableTown", "disabled");
			map.put("disableAgency", "disabled");
			map.put("disableSchool", "disabled");
		}
		map.put("citySelectHtml", this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(user.getTown_id())));
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(String.valueOf(user.getTown_id()) , user.getAgency_id()));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(String.valueOf(user.getAgency_id()) , user.getSchool_id()==null?-1: user.getSchool_id()));
		map.put("editGradeHtmlSelect", this.zfptClassService.getGradeSelectHtml(user.getSchool_id(), -1));
		map.put("groupSelectHtml", this.techerService.getGroupSelectHtml("5"));

		return new ModelAndView(this.importPage, map);
	}

	/**
	 * 模板下载
	 */
	public ModelAndView download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		DownloadInter download = UploadFactory.getDownloadInstance(response);
		try {
			
			download.download(request.getSession().getServletContext().getRealPath("/")
					+ "/template/techer_import_template.xls", "UTF-8");
		} 
		catch (Exception ex) {
			
			ex.printStackTrace();
			return new ModelAndView(new TextView("未知原因导致文件下载失败！"));
		}
		return null;
	}

	/**
	 * 导入教师后的提交操作.
	 */
	public ModelAndView importTecherSubmit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<ErrorTecher> errorList = new ArrayList<ErrorTecher>();
		ErrorTecher errorTecher = null;
		Integer editClassNameId = 0;
		ZfptClass zfptClass  = null;
		Integer editSchoolId = 0;
		School school  = null;
		List<Integer> idList = new ArrayList<Integer>();
		int line=2;
		try {
			for (IUser techer : this.techerService.getImportTecherData(request.getSession().getServletContext()
					.getRealPath("/")+ ServletUtil.removeSpace(request, "importattach_path"))) {
				try{
					line++;
					if (techer.getRealName() == null||"".equals(techer.getRealName())){
						errorTecher = new ErrorTecher();
						errorTecher.setLine(line);
						errorTecher.setBirthday(techer.getBirthday());
						errorTecher.setGender(techer.getGender_str2());
						errorTecher.setId_card(techer.getId_card());
						errorTecher.setLoginName(techer.getLoginName());
						errorTecher.setRealName(techer.getRealName());
						errorTecher.setPhone(techer.getPhone());
						errorTecher.setMobile(techer.getMobile());
						errorTecher.setEmail(techer.getEmail());
						errorTecher.setErrorReason("请填写教师姓名！");
						errorList.add(errorTecher);
						continue;
					}
					if (techer.getLoginName() == null||"".equals(techer.getLoginName())){
						errorTecher = new ErrorTecher();
						errorTecher.setLine(line);
						errorTecher.setBirthday(techer.getBirthday());
						errorTecher.setGender(techer.getGender_str2());
						errorTecher.setId_card(techer.getId_card());
						errorTecher.setLoginName(techer.getLoginName());
						errorTecher.setRealName(techer.getRealName());
						errorTecher.setPhone(techer.getPhone());
						errorTecher.setMobile(techer.getMobile());
						errorTecher.setEmail(techer.getEmail());
						errorTecher.setErrorReason("请填写教师继教号！");
						errorList.add(errorTecher);
						continue;
					}
					if(this.techerService.checkLoginName(techer.getLoginName())>0){//继教号出现重复不进行保存
						errorTecher = new ErrorTecher();
						errorTecher.setLine(line);
						errorTecher.setBirthday(techer.getBirthday());
						errorTecher.setGender(techer.getGender_str2());
						errorTecher.setId_card(techer.getId_card());
						errorTecher.setLoginName(techer.getLoginName());
						errorTecher.setRealName(techer.getRealName());
						errorTecher.setPhone(techer.getPhone());
						errorTecher.setMobile(techer.getMobile());
						errorTecher.setEmail(techer.getEmail());
						errorTecher.setErrorReason("继教号出现重复，请检查是否有误！");
						errorList.add(errorTecher);
						continue;
					}
					if(techer.getId_card()!=null&&techer.getId_card().length()>0){
						if(this.techerService.checkICCard(techer.getId_card())>0){//IC卡号出现重复不进行保存
							errorTecher = new ErrorTecher();
							errorTecher.setLine(line);
							errorTecher.setBirthday(techer.getBirthday());
							errorTecher.setGender(techer.getGender_str2());
							errorTecher.setId_card(techer.getId_card());
							errorTecher.setLoginName(techer.getLoginName());
							errorTecher.setRealName(techer.getRealName());
							errorTecher.setPhone(techer.getPhone());
							errorTecher.setMobile(techer.getMobile());
							errorTecher.setEmail(techer.getEmail());
							errorTecher.setErrorReason("IC卡号出现重复，请检查是否有误！");
							errorList.add(errorTecher);
							continue;
						}
					}
					
					editClassNameId = ServletUtil.getIntParam(request, "importClassId");
					if(editClassNameId>0){
						zfptClass = zfptClassService.get(editClassNameId);
						if(zfptClass !=null )
							techer.setZfptClass(zfptClass);
					}
					User user = (User) UserUtil.getUserBean(request);
					boolean all = PriviilegeFilter.isAllowd(user, "/library/techer.do", "all");
					//本市
					if(all){
						techer.setTown_id(ServletUtil.getIntParam(request, "importCityId"));
						techer.setAgency_id(ServletUtil.getIntParam(request, "importAgencyId"));
						editSchoolId = ServletUtil.getIntParam(request, "importSchoolId");
						if (editSchoolId>0)
							school = schoolService.get(editSchoolId);
						if(school!=null)
							techer.setSchool(school);
					//本镇区	
					}else if (PriviilegeFilter.isAllowd(user, "/library/techer.do", "area")){
						techer.setTown_id(user.getTown_id());
						techer.setAgency_id(ServletUtil.getIntParam(request, "importAgencyId"));
						editSchoolId = ServletUtil.getIntParam(request, "importSchoolId");
						if (editSchoolId>0)
							school = schoolService.get(editSchoolId);
						if(school!=null)
							techer.setSchool(school);
					//本校	
					}else{
						techer.setTown_id(user.getTown_id());
						techer.setAgency_id(user.getAgency_id());
						school = schoolService.get(user.getSchool_id());
						techer.setSchool(school);
					}
					
					techer.setGroupId(5);//教师用户组
					techer.setUser_type("5");//教师用户类型
					techer.setPlainCode("123456");//初始化用户密码
					techer.setLoginPassword("e10adc3949ba59abbe56e057f20f883e");//初始化用户密码123456
					techer.setGender(techer.containGender());
					techer.setStatus(0);
					techer.setIsLock(0);
					techer.setIsSuper(0);
					this.techerService.save(techer);
					idList.add(techer.getUserId());
				}catch(Exception ex){
					errorTecher = new ErrorTecher();
					errorTecher.setLine(line);
					errorTecher.setBirthday(techer.getBirthday());
					errorTecher.setGender(techer.getGender_str());
					errorTecher.setId_card(techer.getId_card());
					errorTecher.setLoginName(techer.getLoginName());
					errorTecher.setRealName(techer.getRealName());
					errorTecher.setPhone(techer.getPhone());
					errorTecher.setMobile(techer.getMobile());
					errorTecher.setEmail(techer.getEmail());
					//提示导入失败原因
					errorTecher.setErrorReason("无法保存，请检查是否有误！");
					errorList.add(errorTecher);
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
				this.techerService.delBath(idStr.toString());//删除保存的。。
			}
			Map<String, Object> map = this.getMap();
			map.put("errorList", errorList);
			return new ModelAndView("/library/techer/teacher_error_list", map);//显示错误提示页面
			
			/*
			//view = new AjaxView(true, "部分导入失败！请下载导入失败名单进行修改！");
			try {
			HSSFWorkbook workbook = this.techerService.getTecherErrorExport(errorList, request.getSession().getServletContext()
					.getRealPath("/")
					+ "/template/techer_error_template.xls");
				ExcelUtils.download(response, workbook, "导入失败教师数据表.xls");
				msg = "请下载“导入失败教师数据表” 修改完继续上传";
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
	 * 导出教师数据
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView downloadTecher(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			Map<String, Object> map = this.getMapWithUser(request);
			int curPage = this.getCurrentPage(request); // 当前查询页数
			Criteria criteria = getDomainService().createCriteria(domainClass);
			criteria.add(Restrictions.eq("status", 0));//正常用户
			criteria.add(Restrictions.eq("user_type", "5"));//学生用户
			
			Integer queryCityId = ServletUtil.getIntParam(request, "queryCityId");
			Integer queryAgencyId=ServletUtil.getIntParam(request, "queryAgencyId");
			Integer querySchoolId=ServletUtil.getIntParam(request, "querySchoolId");
			String techerName  =ServletUtil.removeSpace(request, "techerName");
			String queryloginName  =ServletUtil.removeSpace(request, "queryloginName");
			String idCard  =ServletUtil.removeSpace(request, "idCard");
			
			
			
			User user = (User) map.get("user");
			boolean all = PriviilegeFilter.isAllowd(user, "/library/techer.do", "all");
			//本市
			if(all){
				
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/techer.do", "area")){
				queryCityId =  user.getTown_id();
			//本校	
			}else{
				querySchoolId = user.getSchool_id();
			}
			
			
			
			if(querySchoolId>0)
				criteria.add(Restrictions.eq("school.id", querySchoolId));
			else if(queryAgencyId>0)
				criteria.add(Restrictions.eq("agency_id", queryAgencyId));
			else if(queryCityId>0)
				criteria.add(Restrictions.eq("town_id", queryCityId));
			
			if(techerName!=null&&techerName.length()>0)
				criteria.add(Restrictions.like("realName", new StringBuilder("%").append(techerName).append("%").toString()));
			if(queryloginName!=null&&queryloginName.length()>0)
				criteria.add(Restrictions.like("loginName", new StringBuilder("%").append(queryloginName).append("%").toString()));
			if(idCard!=null&&idCard.length()>0)
				criteria.add(Restrictions.like("id_card", new StringBuilder("%").append(idCard).append("%").toString()));
			
			
			//criteria.addOrder(Order.asc("town_id"));
			//criteria.addOrder(Order.asc("agency_id"));
			criteria.addOrder(Order.asc("school.id"));
			criteria.addOrder(Order.asc("grade_id"));
			List<IUser> list = criteria.list();
			if (list.size() > 0) {
				HSSFWorkbook workbook = this.techerService.getTecherExport(list, request.getSession().getServletContext()
						.getRealPath("/")
						+ "/template/techer_export_template.xls");
				try {
					ExcelUtils.download(response, workbook, "导出教师数据表.xls");
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
	

	public void setTecherService(TecherService techerService) {
		this.techerService = techerService;
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


}