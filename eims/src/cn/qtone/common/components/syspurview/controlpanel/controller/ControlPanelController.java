package cn.qtone.common.components.syspurview.controlpanel.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.components.syspurview.base.controller.BaseNormalController;
import cn.qtone.common.components.syspurview.controlpanel.domain.UserCryptoguard;
import cn.qtone.common.components.syspurview.controlpanel.service.ControlPanelService;
import cn.qtone.common.components.syspurview.core.user.domain.AbstractUser;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.components.syspurview.cryptoguard.service.GetPwdService;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.common.mvc.service.ServiceMsg;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 用户控制面板控制器.
 * 
 * 用户控制面板主要用来做用户的个人资料修改、密码修改等。该模块需要登陆
 * 但无须进行权限控制。
 * 
 * @author 马必强
 *
 */
public class ControlPanelController extends BaseNormalController
{
	private String controlPanelIndex; // 控制面板首页
	
	private String editPersonalInfo; // 个人信息修改页面
	
	private String editPassword; // 个人密码修改页面
	
	private String styleSettingPage; // 个人的风格设置页面
	
	private String userCryptoguard; // 密码保护设置页面
	
	private GetPwdService getPwdService; // 密码取回的业务类，判断是否启用了邮件和短信取回密码功能
	
	public GetPwdService getGetPwdService()
	{
		return getPwdService;
	}

	public void setGetPwdService(GetPwdService getPwdService)
	{
		this.getPwdService = getPwdService;
	}

	public String getUserCryptoguard()
	{
		return userCryptoguard;
	}

	public void setUserCryptoguard(String userCryptoguard)
	{
		this.userCryptoguard = userCryptoguard;
	}

	public String getStyleSettingPage()
	{
		return styleSettingPage;
	}

	public void setStyleSettingPage(String styleSettingPage)
	{
		this.styleSettingPage = styleSettingPage;
	}

	public String getControlPanelIndex()
	{
		return controlPanelIndex;
	}

	public void setControlPanelIndex(String controlPanelIndex)
	{
		this.controlPanelIndex = controlPanelIndex;
	}

	public String getEditPassword()
	{
		return editPassword;
	}

	public void setEditPassword(String editPassword)
	{
		this.editPassword = editPassword;
	}

	public String getEditPersonalInfo()
	{
		return editPersonalInfo;
	}

	public void setEditPersonalInfo(String editPersonalInfo)
	{
		this.editPersonalInfo = editPersonalInfo;
	}

	/**
	 * 控制面板首页显示.
	 */
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		return new ModelAndView(this.getControlPanelIndex(), "configer", 
				this.getConfiger());
	}

	/**
	 * 个人资料修改页面显示.
	 */
	public ModelAndView editPersonalInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		User user = (User)UserUtil.getUserBean(request);
		if (log.isInfoEnabled()) {
			log.info("用户[" + user.getRealName() + "]进行个人资料修改~~");
		}
		return new ModelAndView(this.getEditPersonalInfo(), "user", user);
	}
	
	/**
	 * 个人信息更新操作.
	 */
	public ModelAndView updatePersonalInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		User user = (User)this.getCommandObject(request, User.class);
		User realUser = (User)UserUtil.getUserBean(request);
		user.setUserId(realUser.getUserId());
		if (log.isInfoEnabled()) {
			log.info("用户个人信息更新：[" + StringUtil.reflectObj(user) + "]");
		}
		
		ServiceMsg sms = this.getControlService().updatePersonalInfo(user, realUser);
		if (sms.isSuccess()) {
			UserUtil.removeSession(request);
			UserUtil.setSession(request, (User)sms.getObject("userInfo"));
		}
		return new ModelAndView(new AjaxView(sms.isSuccess(), sms.getMessage()));
	}

	/**
	 * 个人密码修改页面显示.
	 */
	public ModelAndView editPassword(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		if (log.isInfoEnabled()) log.info("用户进行个人密码修改~~");
		return new ModelAndView(this.getEditPassword());
	}

	/**
	 * 个人密码修改提交.
	 */
	public ModelAndView updatePassword(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String oldPwd = StringUtil.trim(request.getParameter("oldPassword"));
		String newPwd = StringUtil.trim(request.getParameter("loginPassword"));
		String reNewPwd = StringUtil.trim(request.getParameter("reLoginPassword"));
		AbstractUser user = UserUtil.getUserBean(request);
		if (log.isInfoEnabled()) {
			log.info("用户密码修改[nam=" + user.getRealName() + ",oldPwd=" 
					+ oldPwd + ",newPwd=" + newPwd + ",reNewPwd=" + reNewPwd);
		}
		
		ServiceMsg sms = this.getControlService().updatePassowrd(oldPwd, 
				newPwd, reNewPwd, user);
		if (sms.isSuccess() && sms.getObject("userInfo") != null) {
			// 密码更新成功后，同步session
			UserUtil.clusterSessionSyn(request, (User)sms.getObject("userInfo"));
		}
		return new ModelAndView(new AjaxView(sms.isSuccess(), sms.getMessage()));
	}

	/**
	 * 个人密码保护修改页面显示.
	 */
	public ModelAndView setCryptoguard(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		// 首先判断是否设置了密码保护的取回功能，没有则不允许进行设置
		if (this.getPwdService == null || (this.getPwdService.getEmailSender() == null &&
				this.getPwdService.getSmsSender() == null)) {
			return new ModelAndView(new AjaxView(false, "系统管理员已禁止了密码保护功能！"));
		}
		User user = (User)UserUtil.getUserBean(request);
		if (log.isInfoEnabled()) {
			log.info("用户[" + user.getRealName() + "]进行密码保护功能设置~~");
		}
		Map<String,Object> map = this.getMap();
		UserCryptoguard guard = this.getControlService().getUserCryptoguard(user);
		map.put("guard", guard);
		map.put("isSetted", guard.getUserId()>0); // 如果用户ID大于0则表示已经设置了密码保护
		map.put("disabledSms", this.getPwdService.getSmsSender() == null ? true : false);
		map.put("disabledEmail", this.getPwdService.getEmailSender() == null ? true : false);
		return new ModelAndView(this.getUserCryptoguard(), map);
	}

	/**
	 * 个人密码保护修改提交.
	 */
	public ModelAndView updateUserCryptoguard(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		User user = (User)UserUtil.getUserBean(request);
		UserCryptoguard guard = (UserCryptoguard)this.getCommandObject(request, 
				UserCryptoguard.class);
		if (log.isInfoEnabled()) {
			log.info("用户秘密保护信息：" + StringUtil.reflectObj(guard));
		}
		ServiceMsg sms = this.getControlService().updateUserCryptoguard(guard, user.getUserId());
		return new ModelAndView(new AjaxView(sms.isSuccess(), sms.getMessage()));
	}
	
	private ControlPanelService getControlService()
	{
		return (ControlPanelService)this.getServiceBean();
	}
}
