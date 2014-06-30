package cn.qtone.common.components.syspurview.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.components.syspurview.base.controller.BaseNormalController;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.common.components.syspurview.login.service.LoginService;
import cn.qtone.common.mvc.service.ServiceMsg;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.mvc.view.spring.JsView;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 登陆控制器。
 * 
 * @author 马必强
 * 
 */
public class LoginController extends BaseNormalController
{
	private String loginPage; // 后台登陆页面的flt模板文件路径

	public void setLoginPage(String loginPage)
	{
		this.loginPage = loginPage;
	}

	/**
	 * 登陆初始化
	 */
	public ModelAndView logininit(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		if (log.isInfoEnabled()) log.info("用户登陆初始化登陆页面~~~~~~~~~");
		return new ModelAndView(this.loginPage, "configer", this.getConfiger());
	}

	/**
	 * 代理登陆，系统管理员使用单位管理员的帐号进行登陆的时候用到.<P>
	 * 需要在user中设置代理的用户ID（就是当前系统管理员的ID），然后日志
	 * 系统将记录代理人的ID进行记录，而非当前的用户！
	 */
	public ModelAndView proxyLogin(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		User user = (User)UserUtil.getUserBean(request);
		// 如果没有登陆或不是超级管理员或是系统管理员则不允许进行代理登陆
		if (user == null) return this.login(request, response);
		if (!user.isSuperManager()) return this.login(request, response);
		int proxyUserId = StringUtil.parseInt(request.getParameter("userId"), 0);
		if (proxyUserId <= 0) return this.login(request, response);
		
		// 代理登陆,如果用户信息中的代理用户ID不为空则表示已经代理了一次,则使用其代理ID为真实用户ID
		// 避免重复代理做跳板来进行操作
		int curUserId = user.getProxyUserId() != -1 ? user.getProxyUserId() : user.getUserId();
		String curUserName = user.getProxyUserName() != null ? user.getProxyUserName() : user.getRealName();
		ServiceMsg sms = this.getService().proxyLogin(curUserId, curUserName, proxyUserId);
		// 返回登陆结果
		return this.returnLoginResult(request, sms, true);
	}

	/**
	 * 代理登陆后的返回到个人操作平台的快捷方式.
	 */
	public ModelAndView proxyLoginBack(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		User user = (User)UserUtil.getUserBean(request);
		if (user == null) return logininit(request, response);
		if (log.isInfoEnabled()) log.info("用户[" + user.getProxyUserName() + "]返回到个人操作平台！");
		if (user.getProxyUserId() != -1) {
			ServiceMsg sms = this.getService().proxyLoginBack(user.getProxyUserId());
			// 登陆成功则直接跳转到指定页面,否则跳转到登陆页面
			if (!sms.isSuccess()) return logininit(request, response);
			user = (User)sms.getObject("user");
			UserUtil.setSession(request, user);
		}
		String location = UserUtil.getLoginIndexUrl(user);
		request.getRequestDispatcher(location).forward(request, response);
		return null;
	}

	/**
	 * 登陆
	 */
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		// 如果已经登陆了，则直接返回
		/*if (UserUtil.isLogin(request)) {
			return new ModelAndView(new AjaxView(true, "登陆成功！"));
		}*/
		
		// 否则进行登陆验证
		String userName = StringUtil.trim(request.getParameter("userName"));
		String userPwd = StringUtil.trim(request.getParameter("userPassword"));
		//测试使用
		//userPwd="123456";
		String randCode = StringUtil.trim(request.getParameter("randCode"));
		String sessionCode = (String)request.getSession().getAttribute("rand");
		if (log.isInfoEnabled()) {
			log.info("用户登陆系统检测[name=" + userName + ",pwd=" + userPwd
					+ ",randCode=" + randCode + "],session code=" 
					+ sessionCode);
		}
		
		// 验证码错误则直接返回
		//测试使用
		if (!randCode.equals(sessionCode)) {
			return new ModelAndView(new AjaxView(false, "验证码错误！"));
		}

		// 登陆校验
		ServiceMsg sms = this.getService().login(userName, userPwd);
		// 返回登陆结果
		return this.returnLoginResult(request, sms, false);
	}

	/**
	 * 登出,返回到当前主机的首页
	 */
	public ModelAndView loginout(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		if (log.isInfoEnabled()) log.info("用户退出系统~~~~~~~~~");
		JsView view = new JsView(null, "/", 3);
		UserUtil.removeSession(request);
		return new ModelAndView(view);
	}
	
	/**
	 * 返回直接登陆后代理登陆的结果.
	 * @param sms
	 * @return
	 */
	private ModelAndView returnLoginResult(HttpServletRequest request,
			ServiceMsg sms, boolean proxyLogin) throws Exception
	{
		AjaxView view = new AjaxView(sms.isSuccess(), sms.getMessage());
		if (sms.isSuccess()) {
			User user = (User) sms.getObject("user");
			UserUtil.setSession(request, user);
			
			if (!proxyLogin) this.getService().updateLastLogin(user.getUserId(), 
					request.getRemoteAddr());
			
			// 设置登陆成功后的转向地址
			view.setProperty("location", UserUtil.getLoginIndexUrl(user));
		}
		return new ModelAndView(view);
	}

	private LoginService getService()
	{
		return (LoginService) this.getServiceBean();
	}
}
