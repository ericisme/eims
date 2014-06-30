package cn.qtone.common.components.syspurview.core.user.controller;

import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import cn.qtone.ContantsUtil;
import cn.qtone.common.components.syspurview.base.controller.BaseManageController;
import cn.qtone.common.components.syspurview.core.user.domain.AbstractUser;
import cn.qtone.common.components.syspurview.core.user.domain.IUser;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.components.syspurview.core.user.qvo.QvoUser;
import cn.qtone.common.components.syspurview.core.user.service.UserService;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.service.ServiceMsg;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.mvc.view.spring.TextView;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.common.utils.crypto.Encrypt;
import cn.qtone.library.XxptContants;
import cn.qtone.library.agency.service.AgencyService;
import cn.qtone.library.city.service.CityService;
import cn.qtone.library.school.domain.School;
import cn.qtone.library.school.service.SchoolService;
import cn.qtone.library.xxptclass.domain.ZfptClass;
import cn.qtone.library.xxptclass.service.XxptClassService;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;

/**
 * 用户管理的中心控制器。
 * 
 * @author 
 * 
 */
public class UserController extends BaseManageController {
	XxptClassService zfptClassService; 
	SchoolService schoolService;
	CityService cityService;
	AgencyService agencyService;

	private String yhxzPage;
	private String yhlbPage;

	public String getYhlbPage() {
		return yhlbPage;
	}

	public void setYhlbPage(String yhlbPage) {
		this.yhlbPage = yhlbPage;
	}

	public String getYhxzPage() {
		return yhxzPage;
	}

	public void setYhxzPage(String yhxzPage) {
		this.yhxzPage = yhxzPage;
	}

	/**
	 * 用户添加页面显示.
	 */
	@Override
	public ModelAndView create(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMap();
		map.put("userGroups", this.getUserService().getGroupList());
		map.put("userTypeOpiton", ContantsUtil.getSystemContantsSelectHtml(XxptContants.MANAGE_USER_TYPE, "#"));
		
		String citySelectHtml = this.cityService.getThirdCityHtmlSelect(
				XxptContants.ZSSCITYID, "#"
			);
		map.put("citySelectHtml", citySelectHtml);
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(ServletUtil.removeSpace(request, "city_id") , -1));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(
				"-1" , -1));
		
		map.put("editGradeHtmlSelect", this.zfptClassService.getGradeSelectHtml(
				-1, -1));
		map.put("editClassNameHtmlSelect", XxptContants.getSystemClassCodeSelectHtml("-1"));
		map.put("userStatusOpiton", XxptContants.getUserStatus("0"));
		return new ModelAndView(this.getCreatePage(), map);
	}

	/**
	 * 用户编辑页面显示.
	 */
	@Override
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int userId = StringUtil.parseInt(request.getParameter("userId"), 0);
		// 用户信息
		ServiceMsg sms = this.getUserService().getUser(userId);
		if (!sms.isSuccess()) {
			return new ModelAndView(new AjaxView(false, sms.getMessage()));
		}
		Map<String, Object> map = this.getMap();
		User user = (User) sms.getObject("user");
		map.put("user", user);
		map.put("userGroups", this.getUserService().getGroupList());
		map.put("userTypeOpiton", ContantsUtil.getSystemContantsSelectHtml(XxptContants.MANAGE_USER_TYPE, user.getUserType()));
		
		String citySelectHtml = this.cityService.getThirdCityHtmlSelect(
				XxptContants.ZSSCITYID, user.getTown_id().toString()
			);
		map.put("citySelectHtml", citySelectHtml);
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(ServletUtil.removeSpace(request, "city_id") , user.getAgency_id()));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(
				user.getAgency_id().toString() , user.getSchool_id()));
		
		map.put("editGradeHtmlSelect", this.zfptClassService.getGradeSelectHtml(
				user.getSchool_id(), user.getGrade_id()));
		map.put("editClassNameHtmlSelect", this.zfptClassService.getClassNameSelectHtml(user,user.getSchool_id(), user.getGrade_id(), user.getClass_id()));
		map.put("userStatusOpiton", XxptContants.getUserStatus(user.getStatus().toString()));
		return new ModelAndView(this.getEditPage(), map);
	}

	/**
	 * 用户列表查看,列表首页显示。
	 */
	@Override
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMap();
		map.put("userGroups", this.getUserService().getGroupList());
		map.put("userTypeOpiton", ContantsUtil.getSystemContantsSelectHtml(XxptContants.MANAGE_USER_TYPE, "#"));
		map.put("configer", this.getConfiger());
		return new ModelAndView(this.getIndexPage(), map);
	}

	/**
	 * 用户的列表查询.
	 */
	@Override
	public ModelAndView query(HttpServletRequest request, HttpServletResponse response) throws Exception {
		QvoUser qvo = new QvoUser();
		qvo.setCurPage(this.getCurrentPage(request));
		qvo.setQryName(ServletUtil.removeSpace(request, "qryName"));
		qvo.setQryGroupId(ServletUtil.removeSpace(request, "qryGroupId"));
		AbstractUser user = (AbstractUser) UserUtil.getUserBean(request);
		if ("1".equals(user.getUserType())) {
			qvo.setQueryUserType("1");
		} else {
			qvo.setQueryUserType(ServletUtil.removeSpace(request, "queryUserType"));
		}
		if (log.isInfoEnabled()) {
			log.info("用户列表获取查询：qryName=" + qvo.getQryName() + ",qryGroupId=" + qvo.getQryGroupId() + ",curPage="
					+ qvo.getCurPage() + ",UserType=" + qvo.getQueryUserType());
		}

		Page page = new Page();
		page.setPageSize(this.getListRows());
		this.getUserService().query(page, qvo);
		page.setPaginate(this.getAjaxPage(request, qvo.getCurPage(), page, "userJump"));

		Map<String, Object> map = this.getMap();
		map.put("page", page);
		map.put("configer", this.getConfiger());
		return new ModelAndView(this.getListTplPage(), map);
	}

	/**
	 * 用户信息删除.
	 */
	@Override
	public ModelAndView remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] userId = request.getParameterValues("userId");
		if (log.isInfoEnabled()) {
			log.info("要删除的用户ID为：" + StringUtil.join(userId, ","));
		}
		ServiceMsg sms = this.getUserService().removeUser(userId);
		AjaxView view = new AjaxView(sms.isSuccess(), sms.getMessage());
		if (sms.isSuccess())
			view.setProperty("refresh", true);
		return new ModelAndView(view);
	}

	/**
	 * 用户添加到数据库操作.
	 */
	@Override
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			
		
		//User user = (User) this.getCommandObject(request, User.class);
		
		IUser iuser= new IUser();
		int editCityId = Integer.parseInt(request.getParameter("editCityId"));
		if(editCityId>0)
			iuser.setTown_id(editCityId);
		
		int  editAgencyId= Integer.parseInt(request.getParameter("editAgencyId"));
		if(editAgencyId>0)
			iuser.setAgency_id(editAgencyId);
		
		int  editSchoolId= Integer.parseInt(request.getParameter("editSchoolId"));
		if(editSchoolId>0){
			School school = new School();
			school.setId(editSchoolId);
			iuser.setSchool(school);
		}
			
		int  editGradeId= Integer.parseInt(request.getParameter("editGradeId"));
		if(editGradeId>0)
			iuser.setGrade_id(editGradeId);
		
		int  editClassNameId= Integer.parseInt(request.getParameter("editClassNameId"));
		if(editClassNameId>0){
			ZfptClass zfptClass = new ZfptClass();
			zfptClass.setId(editClassNameId);
			iuser.setZfptClass(zfptClass);
		}
			
		iuser.setId_card(request.getParameter("id_card"));
		iuser.setStatus(Integer.parseInt(request.getParameter("status")));
		iuser.setAddTime(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
		iuser.setIsLock(0);
		iuser.setIsSuper(0);
		iuser.setUser_type(request.getParameter("userType"));
		iuser.setGroupId(ServletUtil.getIntParam(request,"groupId"));
		iuser.setMobile(request.getParameter("mobile"));
		iuser.setPlainCode(request.getParameter("loginPassword"));
		iuser.setRealName(request.getParameter("realName"));
		iuser.setEmail(request.getParameter("email"));
		iuser.setLoginName(request.getParameter("loginName"));
		iuser.setLoginPassword(Encrypt.MD5(iuser.getPlainCode()));
		
		this.getUserService().getHibernateTemplate().save(iuser);
		//erviceMsg sms = this.getUserService().addUser(user);
		return new ModelAndView(new AjaxView(true,"用户信息添加成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView(new AjaxView(false,"用户信息添加失败！"));
		}
	}

	/**
	 * 用户详细情况查看.
	 */
	@Override
	public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int userId = StringUtil.parseInt(request.getParameter("userId"), 0);
		ServiceMsg sms = this.getUserService().getUser(userId);
		AjaxView view = new AjaxView(sms.isSuccess(), sms.getMessage());
		if (sms.isSuccess())
			view.setProperty("user", sms.getObject("user"));
		return new ModelAndView(view);
	}

	/**
	 * 用户帐号的封锁和解锁操作.
	 */
	public ModelAndView lock(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String lock = request.getParameter("lock");
		String[] userId = request.getParameterValues("userId");
		if (log.isInfoEnabled()) {
			log.info("执行用户[" + StringUtil.join(userId, ",") + "]的[lock=" + lock + "]操作！");
		}
		ServiceMsg sms = this.getUserService().lock(lock, userId);
		return new ModelAndView(new AjaxView(sms.isSuccess(), sms.getMessage()));
	}

	/**
	 * 检查指定的用户名是否已被占用.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView checkUserName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String name = request.getParameter("loginName");
		ServiceMsg sms = this.getUserService().checkLoginName(name);
		return new ModelAndView(new AjaxView(sms.isSuccess(), sms.getMessage()));
	}

	/**
	 * 用户信息的修改保存.
	 */
	@Override
	public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = (User) this.getCommandObject(request, User.class);
		user.setTown_id(Integer.parseInt(request.getParameter("editCityId")));
		user.setAgency_id(Integer.parseInt(request.getParameter("editAgencyId")));
		user.setSchool_id(Integer.parseInt(request.getParameter("editSchoolId")));
		user.setGrade_id(Integer.parseInt(request.getParameter("editGradeId")));
		user.setClass_id(Integer.parseInt(request.getParameter("editClassNameId")));
		user.setId_card(request.getParameter("id_card"));
		user.setStatus(Integer.parseInt(request.getParameter("status")));
		if (log.isInfoEnabled()) {
			log.info("要更新的用户信息为：" + StringUtil.reflectObj(user));
		}
		ServiceMsg sms = this.getUserService().updateUser(user);
		return new ModelAndView(new AjaxView(sms.isSuccess(), sms.getMessage()));
	}

	/**
	 * 密码重置
	 */
	public ModelAndView updatePwd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int[] userId = StringUtil.parseInt(request.getParameterValues("userId"), -1);
		if (log.isInfoEnabled()) {
			log.info("要更新密码的用户为：" + userId);
		}
		ServiceMsg sms = this.getUserService().updatePwd(userId);
		return new ModelAndView(new AjaxView(sms.isSuccess(), sms.getMessage()));
	}

	
	public ModelAndView getClassMasterHtml(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		return new ModelAndView(new TextView(this.getUserService().getZfptClassSelectHtml(ServletUtil.removeSpace(request, "school_id"), "#")));
	}
	
	
	private UserService getUserService() {
		return (UserService) this.getServiceBean();
	}

	public void setSchoolService(SchoolService schoolService) {
		this.schoolService = schoolService;
	}

	public void setCityService(CityService cityService) {
		this.cityService = cityService;
	}
	public void setAgencyService(AgencyService agencyService) {
		this.agencyService = agencyService;
	}

	public void setZfptClassService(XxptClassService zfptClassService) {
		this.zfptClassService = zfptClassService;
	}

}
