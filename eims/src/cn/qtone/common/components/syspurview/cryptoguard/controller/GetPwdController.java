package cn.qtone.common.components.syspurview.cryptoguard.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.components.syspurview.base.controller.BaseNormalController;
import cn.qtone.common.components.syspurview.controlpanel.domain.UserCryptoguard;
import cn.qtone.common.components.syspurview.cryptoguard.domain.UserEmailSession;
import cn.qtone.common.components.syspurview.cryptoguard.service.GetPwdService;
import cn.qtone.common.mvc.service.ServiceMsg;
import cn.qtone.common.mvc.view.spring.JsView;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 用户密码保护取回密码的控制器.
 * 
 * @author 马必强
 *
 */
public class GetPwdController extends BaseNormalController
{
	 // 标识已访问了每一个页面
	private static String[] VISITED = new String[] {"visiteOne", 
		"visiteTwo", "visiteThree", "visiteFour", "visiteFive"};
	
	 // 用户输入的参数保存
	private static String[] PARAMETER = new String[] {"userName", 
		"userCryptoInfo", "randomCode", "emailSession"};
	
	private String stepOnePage; // 密码取回第一步页面
	
	private String stepTwoPage; // 密码取回第二页面
	
	private String stepThreePage; // 密码取回第三个页面
	
	private String tipsPage; // 信息提示页面
	
	private String smsResetPwdPage; // 密码重置页面.
	
	private String smsCodePage; // 手机随机码输入页面
	
	private String emailResetPwdPage; // 邮件重置密码页面
	
	public String getEmailResetPwdPage()
	{
		return emailResetPwdPage;
	}

	public void setEmailResetPwdPage(String emailResetPwdPage)
	{
		this.emailResetPwdPage = emailResetPwdPage;
	}

	public String getSmsCodePage()
	{
		return smsCodePage;
	}

	public void setSmsCodePage(String smsCodePage)
	{
		this.smsCodePage = smsCodePage;
	}

	public String getSmsResetPwdPage()
	{
		return smsResetPwdPage;
	}

	public void setSmsResetPwdPage(String resetPwdPage)
	{
		this.smsResetPwdPage = resetPwdPage;
	}

	public String getTipsPage()
	{
		return tipsPage;
	}

	public void setTipsPage(String tipsPage)
	{
		this.tipsPage = tipsPage;
	}

	public String getStepOnePage()
	{
		return stepOnePage;
	}

	public void setStepOnePage(String stepOnePage)
	{
		this.stepOnePage = stepOnePage;
	}

	public String getStepThreePage()
	{
		return stepThreePage;
	}

	public void setStepThreePage(String stepThreePage)
	{
		this.stepThreePage = stepThreePage;
	}

	public String getStepTwoPage()
	{
		return stepTwoPage;
	}

	public void setStepTwoPage(String stepTwoPage)
	{
		this.stepTwoPage = stepTwoPage;
	}

	/**
	 * 密码取回初始化页面。
	 */
	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		// 如果没有指定邮件发送和短信发送接口实现类，则表示不允许进行密码取回操作
		if (this.getPwdService().getEmailSender() == null && 
				this.getPwdService().getSmsSender() == null) {
			return new ModelAndView(this.getTipsPage(), "msg", "系统管理员已暂时禁止了密码的重置功能！");
		}
		// 设置第一个页面已被访问状态
		request.getSession().setAttribute(VISITED[0], true);
		if (log.isInfoEnabled()) log.info("[密码取回]初始化用户名称输入页面~");
		return new ModelAndView(this.getStepOnePage());
	}

	/**
	 * 密码取回的第二个页面，即email和手机号码的输入页面显示。
	 * 对使用手机方式取回密码的只显示手机输入，否则只显示email地址输入界面
	 */
	public ModelAndView initStep2(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String userName = StringUtils.trimToEmpty(request.getParameter("userName"));
		if (log.isInfoEnabled()) log.info("[密码取回]获取用户" + userName + "]的密码保护设置~");
		// 如果不是经过第一个页面过来的或是用户名为空，则直接返回到初始页面
		if (request.getSession().getAttribute(VISITED[0]) == null || 
				userName.equals("")) {
			return new ModelAndView(new JsView(null, request.getRequestURI() + "?action=init"));
		}
		
		// 检查该用户是否已经设置了密码保护功能.
		ServiceMsg sms = this.getPwdService().isSetCryptoguard(userName);
		if (!sms.isSuccess()) {
			return new ModelAndView(this.getTipsPage(), "msg", sms.getMessage());
		}
		
		// 设置第二个页面已被访问状态和用户的登陆名称
		HttpSession session = request.getSession();
		UserCryptoguard user = (UserCryptoguard)sms.getObject("user");
		if (log.isInfoEnabled()) log.info("[密码取回]用户密码保护信息：" + StringUtil.reflectObj(user));
		session.setAttribute(VISITED[1], true);
		session.setAttribute(PARAMETER[0], userName);
		session.setAttribute(PARAMETER[1], user);
		
		return new ModelAndView(this.getStepTwoPage(), "useEmailMethod", user.isUseEmailMethod());
	}

	/**
	 * 密码取回的第三个页面，即问题的回答页面。
	 */
	public ModelAndView initStep3(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		// 如果指定的属性不存在则直接返回到初始页面
		HttpSession session = request.getSession();
		UserCryptoguard user = (UserCryptoguard)session.getAttribute(PARAMETER[1]);
		if (session.getAttribute(VISITED[1]) == null || user == null ||
				session.getAttribute(PARAMETER[0]) == null) {
			return new ModelAndView(new JsView(null, request.getRequestURI() + "?action=init"));
		}
		
		// 检查用户输入的email地址和手机号码是否正确
		String email = StringUtils.trimToEmpty(request.getParameter("userEmail"));
		String mobile = StringUtils.trimToEmpty(request.getParameter("userMobile"));
		if (log.isInfoEnabled()) log.info("[密码取回]用户输入email=" + email + ",mobile=" + mobile);
		ServiceMsg sms = this.getPwdService().checkInfoIsRight(user, email, mobile);
		if (!sms.isSuccess()) {
			return new ModelAndView(this.getTipsPage(), "msg", sms.getMessage());
		}
		session.setAttribute(VISITED[2], true);
		return new ModelAndView(this.getStepThreePage(), "userQuestion", user.getUserQuestion());
	}

	/**
	 * 密码取回的问题答案检查，并发送邮件或短信通知。
	 */
	public ModelAndView checkAnswer(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		// 如果指定的属性不存在则直接返回到初始页面
		HttpSession session = request.getSession();
		String userName = (String)session.getAttribute(PARAMETER[0]);
		UserCryptoguard user = (UserCryptoguard)session.getAttribute(PARAMETER[1]);
		if (user == null || userName == null || session.getAttribute(VISITED[2]) == null) {
			return new ModelAndView(new JsView(null, request.getRequestURI() + "?action=init"));
		}
		
		// 获取用户的输入，即问题答案
		String answer = StringUtils.trimToEmpty(request.getParameter("answer"));
		if (log.isInfoEnabled()) {
			log.info("[密码取回]用户回答问题答案：" + answer + "\n请求URL：" + request.getRequestURL());
		}
		ServiceMsg sms = this.getPwdService().sendMailOrSMS(user, answer, session.getId(), userName);
		if (!sms.isSuccess()) {
			return new ModelAndView(this.getTipsPage(), "msg", sms.getMessage());
		}
		
		// 回答正确的操作,如果是短信取回则直接跳转到密码重置页面，否则提示邮件发送成功
		if (user.isUseEmailMethod()) {
			clearAttribute(session);
			return new ModelAndView(this.getTipsPage(), "msg", sms.getMessage());
		} else {
			session.setAttribute(VISITED[3], true);
			session.setAttribute(PARAMETER[2], sms.getObject("randomCode"));
			return new ModelAndView(this.getSmsCodePage());
		}
	}

	/**
	 * 短信重置密码的随机码输入页面显示。
	 */
	public ModelAndView initSMSRandomCode(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		// 如果指定的属性不存在则直接返回到初始页面
		HttpSession session = request.getSession();
		UserCryptoguard user = (UserCryptoguard)session.getAttribute(PARAMETER[1]);
		String randomCode = (String)session.getAttribute(PARAMETER[2]);
		if (user == null || randomCode == null || session.getAttribute(VISITED[3]) == null) {
			return new ModelAndView(new JsView(null, request.getRequestURI() + "?action=init"));
		}
		
		// 检查用户输入的随机码
		String code = StringUtils.trimToEmpty(request.getParameter("userCode"));
		ServiceMsg sms = this.getPwdService().checkRandomCodeIsRight(randomCode, code);
		if (!sms.isSuccess()) {
			return new ModelAndView(this.getTipsPage(), "msg", sms.getMessage());
		}
		session.setAttribute(VISITED[4], true);
		return new ModelAndView(this.getSmsResetPwdPage());
	}

	/**
	 * 短信密码重置页面。
	 */
	public ModelAndView smsResetPwd(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		// 如果指定的属性不存在则直接返回到初始页面
		HttpSession session = request.getSession();
		String userName = (String)session.getAttribute(PARAMETER[0]);
		UserCryptoguard user = (UserCryptoguard)session.getAttribute(PARAMETER[1]);
		if (user == null || userName == null || session.getAttribute(VISITED[4]) == null) {
			return new ModelAndView(new JsView(null, request.getRequestURI() + "?action=init"));
		}
		
		// 设置用户密码
		String pwd = StringUtils.trimToEmpty(request.getParameter("userPwd"));
		this.getPwdService().resetPassword(userName, pwd);
		this.clearAttribute(session);
		return new ModelAndView(this.getTipsPage(), "msg", "恭喜，重置密码成功！");
	}

	/**
	 * 邮件重置页面初始化页面，即密码输入页面。
	 */
	public ModelAndView emailResetPwdInit(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		// 获取连接参数-唯一的session值
		String sid = StringUtils.trimToEmpty(request.getParameter(
				GetPwdService.EMAIL_SESSION_NAME));
		if (log.isInfoEnabled()) log.info("用户session为：" + sid);
		ServiceMsg sms = this.getPwdService().getEmailSession(sid);
		if (!sms.isSuccess()) {
			return new ModelAndView(this.getTipsPage(), "msg", sms.getMessage());
		}
		request.getSession().setAttribute(PARAMETER[3], sms.getObject("session"));
		return new ModelAndView(this.getEmailResetPwdPage());
	}

	/**
	 * 邮件密码重置。
	 */
	public ModelAndView emailResetPwd(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		// 获取当前密码重置请求的session
		UserEmailSession session = (UserEmailSession)request.getSession().getAttribute(PARAMETER[3]);
		if (session == null) {
			return new ModelAndView(this.getTipsPage(), "msg", "非法访问！");
		}
		String pwd = StringUtils.trimToEmpty(request.getParameter("userPwd"));
		this.getPwdService().resetPassword(session.getUserName(), pwd);
		this.clearAttribute(request.getSession());
		return new ModelAndView(this.getTipsPage(), "msg", "恭喜，重置密码成功！");
	}
	
	
	/**
	 * 清除当前session中设置的属性.
	 * @param session
	 */
	private void clearAttribute(HttpSession session)
	{
		for (String tmp : VISITED) session.removeAttribute(tmp);
		for (String tmp : PARAMETER) session.removeAttribute(tmp);
	}
	
	private GetPwdService getPwdService()
	{
		return (GetPwdService)this.getServiceBean();
	}
}
