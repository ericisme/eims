package cn.qtone.library.xxptclass.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.components.syspurview.check.filter.priviilege.PriviilegeFilter;
import cn.qtone.common.components.syspurview.core.user.domain.AbstractUser;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.components.syspurview.core.user.service.UserService;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.mvc.view.spring.TextView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.library.XxptContants;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;
import cn.qtone.qtoneframework.web.view.poi.ExcelUtils;
import cn.qtone.library.city.domain.City;
import cn.qtone.library.city.service.CityService;
import cn.qtone.library.school.domain.School;
import cn.qtone.library.school.service.SchoolService;
import cn.qtone.library.student.dao.StudentQueryDao;
import cn.qtone.library.student.qvo.StudentQVO;
import cn.qtone.library.xxptclass.domain.ZfptClass;
import cn.qtone.library.xxptclass.service.XxptClassService;
import cn.qtone.library.agency.domain.Agency;
import cn.qtone.library.agency.service.AgencyService;

/**
 * 基础管理 - 班级管理控制器.<br>
 *
 * @author 贺少辉
 * @version 1.0
 */
public class XxptClassController extends SimpleManageController<ZfptClass, XxptClassService> {
	
	XxptClassService zfptClassService;
	SchoolService schoolService;
	CityService cityService;
	UserService userService;
	AgencyService agencyService;
	StudentQueryDao studentQueryDao;

	@Override
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		
		User user = (User) map.get("user");
		boolean all = PriviilegeFilter.isAllowd(user, "/library/zfptclass.do", "all");
		map.put("citySelectHtml", this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(user.getTown_id())));
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(String.valueOf(user.getTown_id()) , user.getAgency_id()));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(String.valueOf(user.getAgency_id()) , user.getSchool_id()));
		map.put("editGradeHtmlSelect", XxptContants.getGradeSelectHtml("1,2,3,4,5", "-1"));

		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/zfptclass.do", "area")){
			map.put("disableTown", "disabled");
			map.put("editGradeHtmlSelect", XxptContants.getGradeSelectHtml("1,2,3,4,5", "-1"));
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
	
	@SuppressWarnings("unchecked")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			Map<String, Object> map = this.getMapWithUser(request);
			int curPage = this.getCurrentPage(request); // 当前查询页数
			Criteria criteria = getDomainService().createCriteria(domainClass);
			//setSqlExpression(request, criteria);/*这个设置,允许在页面上写查询条件,在页面传过来的参数中已包括了school.id的查询条件*/			
			
			Integer queryCityId = ServletUtil.getIntParam(request, "queryCityId");
			Integer queryAgencyId=ServletUtil.getIntParam(request, "queryAgencyId");
			Integer querySchoolId=ServletUtil.getIntParam(request, "querySchoolId");
			Integer queryGradeId=ServletUtil.getIntParam(request, "queryGradeId");
			Integer queryClassNameId=ServletUtil.getIntParam(request, "queryClassNameId");
			
			if(queryClassNameId>0)
				criteria.add(Restrictions.eq("id", queryClassNameId));
			if(queryGradeId>0)
			criteria.add(Restrictions.eq("grade", queryGradeId));
			
			Criteria schoolCriteria= criteria.createCriteria("school");
			Criteria agencyCriteria = schoolCriteria.createCriteria("agency");
			
			User user = (User) map.get("user");
			boolean all = PriviilegeFilter.isAllowd(user, "/library/zfptclass.do", "all");
			//本市
			if(all){
				
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/zfptclass.do", "area")){
				queryCityId = user.getTown_id();
			//本校	
			}else{
				querySchoolId =user.getSchool_id();
			}
			if(querySchoolId>0)
				schoolCriteria.add(Restrictions.eq("id", querySchoolId));
			if(queryAgencyId>0)
				agencyCriteria.add(Restrictions.eq("id", queryAgencyId));
			if(queryCityId>0)
				agencyCriteria.add(Restrictions.eq("city.id", queryCityId));
			
			
			schoolCriteria.addOrder(Order.asc("id"));
			criteria.addOrder(Order.asc("ordergrade"));
			criteria.addOrder(Order.asc("orderclass"));
			Page page = getDomainService().pagedQuery(criteria, curPage, Page.DEFAULT_PAGE_SIZE);
			page.setPaginate(this.getAjaxPage(request, curPage, page, StringUtil.unCapitalize(domainClass.getSimpleName()) + "Jump"));
			map.put("page", page);
			
			return new ModelAndView(getListPage(), map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	@Override
	public ModelAndView create(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		
		User user = (User) map.get("user");
		boolean all = PriviilegeFilter.isAllowd(user, "/library/zfptclass.do", "all");
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/zfptclass.do", "area")){
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
		
		map.put("editClassNameHtmlSelect", XxptContants.getSystemClassCodeSelectHtml("-1"));
		map.put(getDomainName(), doNewDomain());
		return new ModelAndView(getEditPage(), map);
	}

	@Override
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		ZfptClass zc = doGetDomain(request);
		
		
		User user = (User) map.get("user");
		boolean all = PriviilegeFilter.isAllowd(user, "/library/zfptclass.do", "all");
		//本市
		if(all){
			
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/zfptclass.do", "area")){
			map.put("disableTown", "disabled");
		//本校	
		}else{
			map.put("disableTown", "disabled");
			map.put("disableAgency", "disabled");
			map.put("disableSchool", "disabled");
		}
		
		String citySelectHtml = this.cityService.getThirdCityHtmlSelect(
				XxptContants.ZSSCITYID, zc.getSchool().getAgency().getCity().getId()
			);
		map.put("citySelectHtml", citySelectHtml);
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(
				zc.getSchool().getAgency().getCity().getId().toString() , zc.getSchool().getAgency().getId()));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(
				zc.getSchool().getAgency().getId().toString() , zc.getSchool().getId()));
		
		map.put("editGradeHtmlSelect", this.zfptClassService.getGradeSelectHtml(
				zc.getSchool().getId(), zc.getOrdergrade()));

		map.put("editClassNameHtmlSelect", XxptContants.getSystemClassCodeSelectHtml(zc.getOrderclass().toString()));
		map.put(getDomainName(), zc);
		return new ModelAndView(getEditPage(), map);
	}


	@Override
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ZfptClass zfptClass = (ZfptClass)getCommandObject(request, getDomainClass());
		//zfptClass.setSchool(new School(ServletUtil.getIntParam(request, "editSchoolId")));
		zfptClass.setClass_code(ServletUtil.removeSpace(request, "editGradeId")+"_"+ServletUtil.removeSpace(request, "editClassNameId"));
		zfptClass.setClass_name(XxptContants.getGradeValue(ServletUtil.removeSpace(request, "editGradeId"))+"("+ServletUtil.removeSpace(request, "editClassNameId")+")班");
		zfptClass.setOrdergrade(ServletUtil.getIntParam(request, "editGradeId"));
		zfptClass.setGrade(ServletUtil.getIntParam(request, "editGradeId"));
        zfptClass.setOrderclass(ServletUtil.getIntParam(request, "editClassNameId"));
		Integer  editSchoolId= ServletUtil.getIntParam(request, "editSchoolId");
		
		User user = (User) UserUtil.getUserBean(request);
		boolean all = PriviilegeFilter.isAllowd(user, "/library/zfptclass.do", "all");
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/zfptclass.do", "area")){
		//本校	
		}else{
			editSchoolId = user.getSchool_id();
		}
		zfptClass.setSchool(new School(editSchoolId));
        
		//判断一下该班级编号是否存在
		if (getDomainService().queryZfptClassCodeBySchoolIdAndClassCode(zfptClass.getSchool().getId(),zfptClass.getGrade(),
				zfptClass.getOrderclass()).size() > 0) {
			AjaxView view = new AjaxView(true, "该班级编号已经存在,请重新选择！");
			view.setProperty("refresh", true);
			return new ModelAndView(view);
		}
		if(StringUtils.isBlank(ServletUtil.removeSpace(request, "id"))){
			getDomainService().save(zfptClass);
		}else{
			getDomainService().saveOrUpdate(zfptClass);
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
		for(String idStr : idStrs){
			if(idStr.length() > 0){
				StudentQVO qvo = new StudentQVO();
				qvo.setClass_id(ServletUtil.parseInt(idStr, -1));
				int count = studentQueryDao.queryStudentCount(qvo);
				if (count > 0) {
					
					AjaxView view = new AjaxView(true, "操作失败,该班级下包含学生,不能删除该班级！");
					view.setProperty("refresh", true);
					return new ModelAndView(view);
				}
				getDomainService().removeById((Serializable) ConvertUtils.convert(idStr, idClass));
			}
		}
		AjaxView view = new AjaxView(true, "操作成功！");
		view.setProperty("refresh", true);
		return new ModelAndView(view);
	}
	
	/**
	 * 导出班级数据
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView downloadClass(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Criteria criteria = getDomainService().createCriteria(domainClass);
		//ZfptClass zfptClass = (ZfptClass)getCommandObject(request, getDomainClass());
		//zfptClass = getZfptClass(request, zfptClass);
		//criteria = getQueryCondition(criteria, zfptClass);
		
		Integer queryCityId = ServletUtil.getIntParam(request, "queryCityId");
		Integer queryAgencyId=ServletUtil.getIntParam(request, "queryAgencyId");
		Integer querySchoolId=ServletUtil.getIntParam(request, "querySchoolId");
		Integer queryGradeId=ServletUtil.getIntParam(request, "queryGradeId");
		Integer queryClassNameId=ServletUtil.getIntParam(request, "queryClassNameId");
		
		criteria.add(Restrictions.eq("id", queryClassNameId));
		criteria.add(Restrictions.eq("grade", queryGradeId));
		
		Criteria schoolCriteria= criteria.createCriteria("school");
		Criteria agencyCriteria = schoolCriteria.createCriteria("agency");
		if(querySchoolId>0)
			schoolCriteria.add(Restrictions.eq("id", querySchoolId));
		
		if(queryAgencyId>0)
			agencyCriteria.add(Restrictions.eq("id", queryAgencyId));
		if(queryCityId>0)
			agencyCriteria.add(Restrictions.eq("city.id", queryCityId));
		
		
		criteria.addOrder(Order.desc("school.id"));
		criteria.addOrder(Order.asc("ordergrade"));
		criteria.addOrder(Order.asc("orderclass"));
		
		List<ZfptClass> list = (List<ZfptClass>) criteria.list();
		if (list.size() > 0) {
			HSSFWorkbook workbook = this.zfptClassService.getClassExport(list,
					request.getSession().getServletContext().getRealPath("/")
							+ "/template/class_export_template.xls");
			try {
				ExcelUtils.download(response, workbook, "班级数据表.xls");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			String msg ="<script>alert('根据所选条件,导出条目为0,请重新选择！');window.history.back();</script>";
			ServletOutputStream out=response.getOutputStream();
			out.print(msg);
			out.close();
		}
		return null;
	}
	
	/**
	 * 取页面返回值
	 * 
	 * @param request
	 * @param zfptClass
	 * @return
	 */
	private ZfptClass getZfptClass(HttpServletRequest request, ZfptClass zfptClass) {
		
		School school = new School();
		school.setId(ServletUtil.parseInt(ServletUtil.removeSpace(request, "querySchoolId"), -1));
		
		Agency agency = new Agency();
		agency.setId(ServletUtil.parseInt(ServletUtil.removeSpace(request, "queryAgencyId"), -1));
		
		City city = new City();
		city.setId(ServletUtil.parseInt(ServletUtil.removeSpace(request, "city_id"), -1));
		
		agency.setCity(city);
		school.setAgency(agency);
		zfptClass.setSchool(school);
		
		return zfptClass;
	}
	
	/**
	 * 取查询条件
	 * 
	 * @param criteria
	 * @param zfptClass
	 * @return
	 */
	/*private Criteria getQueryCondition(Criteria criteria, ZfptClass zfptClass){
		
		if (zfptClass.getSchool().getId() > 0) {
			
			criteria.createAlias("school","school");
			criteria.add(Restrictions.eq("school.id", zfptClass.getSchool().getId()));
		}
		else {
			
			if (zfptClass.getSchool().getAgency().getId() > 0) {
				
				Criteria criteria_school = criteria.createCriteria("school","school");
				criteria_school.createAlias("agency", "agency");
				criteria_school.add(Restrictions.eq("agency.id", zfptClass.getSchool().getAgency().getId()));
			}
			else{
				
				if (zfptClass.getSchool().getAgency().getCity().getId() > 0) {
					
					Criteria criteria_school = criteria.createCriteria("school","school");
					Criteria criteria_agency = criteria_school.createCriteria("agency", "agency");
					criteria_agency.createAlias("city", "city");
					criteria_agency.add(Restrictions.eq("city.id", zfptClass.getSchool().getAgency().getCity().getId()));
				}
			}
		}
		return criteria;
	}*/
	
	public ModelAndView getClassSelectHtmlByChangeSchoolOrGrade(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		AbstractUser user = UserUtil.getUserBean(request);
		if("1".equals(user.getUserType())){
			return new ModelAndView(new TextView(this.zfptClassService.getZfptClassSelectHtml(ServletUtil.removeSpace(request, "school_id"), ServletUtil.removeSpace(request, "grade_id"), "#",user.getUserId())));
		}else{
			return new ModelAndView(new TextView(this.zfptClassService.getZfptClassSelectHtml(ServletUtil.removeSpace(request, "school_id"), ServletUtil.removeSpace(request, "grade_id"), "#")));
		}
	}
	
	public ModelAndView getClassCodeSelectHtmlByChangeSchoolOrGrade(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		User user  = (User) UserUtil.getUserBean(request);
		return new ModelAndView(new TextView(this.zfptClassService.getClassSelectHtml(user,
				ServletUtil.getIntParam(request, "school_id"), ServletUtil.getIntParam(request, "orderGrade"), ServletUtil.removeSpace(request, "class_code"))));
	}
	
	public ModelAndView getClassCodeSelectHtmlByAdd(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		return new ModelAndView(new TextView(XxptContants.getSystemClassCodeSelectHtml(
				ServletUtil.removeSpace(request, "class_code").split("_")[1])));
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView getClassNameSelect(HttpServletRequest request, HttpServletResponse response){
		
		String htmlResult = "";
		User user  = (User) UserUtil.getUserBean(request);
		String student_index = ServletUtil.removeSpace(request, "student_index");
		if (student_index != null && !student_index.equals("")){
			htmlResult = this.zfptClassService.getClassNameSelectHtml(user,
					ServletUtil.getIntParam(request, "school_id"), ServletUtil.getIntParam(request, "orderGrade"), -1);
		}
		else
			htmlResult = XxptContants.getSystemClassCodeSelectHtml("-1");
		return new ModelAndView(new TextView(htmlResult));
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView getGradeSelectHtmlByChangeSchool(
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		String htmlResult = "";
		if (ServletUtil.getIntParam(request, "school_id") == -1)
			htmlResult = XxptContants.getGradeSelectHtml("1,2,3,4,5", "-1");
		else 
			htmlResult = XxptContants.getGradeSelectHtml(
					this.schoolService.get(
							ServletUtil.getIntParam(request, "school_id"))
							.getContainGrade(), "#");
		return new ModelAndView(new TextView(htmlResult));
	}
	
	public void setZfptClassService(XxptClassService zfptClassService) {
		this.zfptClassService = zfptClassService;
	}

	public void setCityService(CityService cityService) {
		this.cityService = cityService;
	}

	public void setSchoolService(SchoolService schoolService) {
		this.schoolService = schoolService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setAgencyService(AgencyService agencyService) {
		this.agencyService = agencyService;
	}

	public void setStudentQueryDao(StudentQueryDao studentQueryDao) {
		this.studentQueryDao = studentQueryDao;
	}
}


