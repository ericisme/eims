package cn.qtone.common.components.syspurview.cryptoguard.service;

import java.util.Date;

import org.apache.log4j.Logger;

import cn.qtone.common.components.syspurview.controlpanel.domain.UserCryptoguard;
import cn.qtone.common.components.syspurview.cryptoguard.dao.IGetPwdDAO;
import cn.qtone.common.components.syspurview.cryptoguard.domain.UserEmailSession;
import cn.qtone.common.components.syspurview.cryptoguard.send.EmailSendInter;
import cn.qtone.common.components.syspurview.cryptoguard.send.SMSSendInter;
import cn.qtone.common.mvc.service.ServiceMsg;
import cn.qtone.common.utils.base.DateUtil;
import cn.qtone.common.utils.base.RandomGraphic;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.common.utils.crypto.Encrypt;

/**
 * 用户取回密码的业务逻辑处理类.
 * 
 * @author 马必强
 *
 */
public class GetPwdService
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GetPwdService.class);

	public final static String EMAIL_SESSION_NAME = "ssid"; // 邮件重置密码时的随机码参数名称
	
	private IGetPwdDAO dao;
	
	private EmailSendInter emailSender; // 邮件取回密码的发送接口
	
	private SMSSendInter smsSender; // 短信取回密码的发送接口
	
	private String emailResetPwdUrl; // 邮件发送时重置密码的URL地址，其后会追加一大串随机码

	public String getEmailResetPwdUrl()
	{
		return emailResetPwdUrl;
	}

	public void setEmailResetPwdUrl(String resetPwdUrl)
	{
		this.emailResetPwdUrl = resetPwdUrl;
	}

	public EmailSendInter getEmailSender()
	{
		return emailSender;
	}

	public void setEmailSender(EmailSendInter email)
	{
		this.emailSender = email;
	}

	public SMSSendInter getSmsSender()
	{
		return smsSender;
	}

	public void setSmsSender(SMSSendInter sms)
	{
		this.smsSender = sms;
	}

	public IGetPwdDAO getDao()
	{
		return dao;
	}

	public void setDao(IGetPwdDAO dao)
	{
		this.dao = dao;
	}
	
	/**
	 * 检查指定的用户名是否设置了密码保护功能.
	 * @param userName
	 * @return
	 */
	public ServiceMsg isSetCryptoguard(String userName)
	{
		ServiceMsg sms = new ServiceMsg();
		UserCryptoguard user = this.dao.getUserCryptoguard(userName);
		if (user == null) {
			sms.setMessage("用户[" + userName + "]不存在或暂时还没有设置密码保护功能！");
			return sms;
		}
		sms.setSuccess(true);
		sms.addObject("user", user);
		return sms;
	}
	
	/**
	 * 检查用户的输入是否正确
	 * @param user
	 * @param email
	 * @param mobile
	 * @return
	 */
	public ServiceMsg checkInfoIsRight(UserCryptoguard user, String email, String mobile)
	{
		ServiceMsg sms = new ServiceMsg();
		if (user.isUseEmailMethod() && !user.getUserEmail().equals(email)) {
			sms.setMessage("电子邮件地址输入错误！");
			return sms;
		}
		if (!user.isUseEmailMethod() && !user.getUserMobile().equals(mobile)) {
			sms.setMessage("手机号码输入错误！");
			return sms;
		}
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 检查用户的问题答案输入，如果正确则执行邮件的发送或短信的发送
	 * @param user
	 * @param answer
	 * @return
	 */
	public ServiceMsg sendMailOrSMS(UserCryptoguard user, String answer, 
			String sessionId, String userName) throws Exception
	{
		ServiceMsg sms = new ServiceMsg();
		if (!user.getUserAnswer().equals(answer)) {
			sms.setMessage("密码保护问题回答错误！");
			return sms;
		}
		// 密码回答正确后再确定是采用邮件或短信进行密码取回
		if (user.isUseEmailMethod()) {
			if (this.emailSender == null) {
				sms.setMessage("系统管理员暂时禁用了邮件的密码取回功能！");
				return sms;
			}
			String sid = RandomGraphic.getInstance(30).randNumberAndAlpha()
				+ System.currentTimeMillis() + sessionId;
			boolean success = this.getEmailSender().sendEmail(userName, user.getUserEmail(), 
					this.getEmailResetPwdUrl() + "&" + EMAIL_SESSION_NAME + "=" + sid);
			if (!success) {
				sms.setMessage("邮件发送失败，请联系管理员解决！");
				return sms;
			}
			if (logger.isInfoEnabled()) {
				logger.info("email发送保存信息：name=" + userName + ",sessionId=" + sid);
			}
			// 保存session
			this.dao.saveEmailSession(userName, sid);
			sms.setMessage("密码已成功发送到指定的邮箱地址，请注意查收并及时重置密码！");
		} else {
			if (this.smsSender == null) {
				sms.setMessage("系统管理员暂时禁用了短信重置密码功能！");
				return sms;
			}
			String randomCode = RandomGraphic.getInstance(8).randNumber();
			boolean success = this.getSmsSender().sendRandomCode(user.getUserMobile(), randomCode);
			if (!success) {
				sms.setMessage("短信随机验证码发送失败，请联系管理员解决！");
				return sms;
			}
			sms.addObject("randomCode", randomCode);
		}
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 检查用户输入的随机码和系统发出的随机码是否一致.
	 * @param oldCode
	 * @param userCode
	 * @return
	 */
	public ServiceMsg checkRandomCodeIsRight(String oldCode, String userCode)
	{
		ServiceMsg sms = new ServiceMsg();
		if (!oldCode.equals(userCode)) {
			sms.setMessage("随机验证码输入错误！");
			return sms;
		}
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 当使用email地址取回时，通过地址中的ssid参数来获取当前的session.
	 * @param ssid
	 * @return
	 */
	public ServiceMsg getEmailSession(String ssid)
	{
		ServiceMsg sms = new ServiceMsg();
		UserEmailSession session = this.dao.getUserEmailSession(ssid);
		// session 不存在返回
		if (session == null) {
			sms.setMessage("指定session不存在或已过期，无法重置密码！");
			return sms;
		}
		
		// 获取email发送的session保存时间，默认是三天
		int days = this.emailSender.getSessionDays();
		// 获取session创建的日期
		Date create = DateUtil.parseDateTime(session.getSendTime());
		create = DateUtil.setDate(create, 0, 0, days);
		if (logger.isInfoEnabled()) {
			logger.info("Session=[" + StringUtil.reflectObj(session) + "]  isBefore:"
					+ DateUtil.isBefore(create, DateUtil.getNowDate()));
		}
		// session过期返回
		if (DateUtil.isBefore(create, DateUtil.getNowDate())) {
			sms.setMessage("指定session不存在或已过期，无法重置密码！");
			return sms;
		}
		sms.setSuccess(true);
		sms.addObject("session", session);
		return sms;
	}
	
	/**
	 * 用户密码重置.
	 * @param loginName
	 * @param pwd
	 */
	public void resetPassword(String loginName, String pwd)
	{
		String password = Encrypt.MD5(pwd); // MD5加密
		this.dao.resetUserPassword(loginName, password);
	}
}
