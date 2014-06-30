package cn.qtone.common.components.syspurview.cryptoguard.send.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import cn.qtone.common.components.syspurview.cryptoguard.send.EmailSendInter;
import cn.qtone.common.utils.base.DateUtil;

/**
 * 邮件发送接口实现.
 * 使用基于gmail的邮件发送服务器，要提供SSL的发送方式！端口号为465
 * 
 * @author 马必强
 * 
 */
public class MyEmailSender implements EmailSendInter
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MyEmailSender.class);
	
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	
	private String host; // 邮件发送主机
	
	private String userName; // 主机登陆用户名
	
	private String userPassword; // 主机登陆密码
	
	private String sender; // 邮件发送者
	
	private int sessionDays; // 邮件sessioin保存天数

	public boolean sendEmail(String userName, String userEmail, String url)
	{
		if (logger.isInfoEnabled()) logger.info("邮件发送：url=" + url);
		
		// 邮件初始化
		EMail email = this.initEMail(userName, userEmail, url);
		
		// 发送初始化
		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", this.host);
		properties.put("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		properties.setProperty("mail.smtp.socketFactory.fallback", "false");
		properties.setProperty("mail.smtp.port", "465");
		properties.setProperty("mail.smtp.socketFactory.port", "465");
		LoginAuthenticator loginAuth = new LoginAuthenticator(this.userName, this.userPassword);
		if (logger.isInfoEnabled()) logger.info("邮件服务器登陆：user=" + userName + ", pwd=" + userPassword);
		
		// 邮件发送
		try {
			Session mailSession = Session.getInstance(properties, loginAuth);
			if (logger.isInfoEnabled()) mailSession.setDebug(true);
			MimeMessage msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(email.getMailFrom())); // 邮件发送人地址
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getMailTo())); // 接收人地址
			msg.setSubject(email.getContent()); // 邮件主题
			msg.setContent(email.getContent(), email.getContentType()); // 邮件内容
			msg.setSentDate(DateUtil.getNowDate()); // 发送时间
			Transport.send(msg); // 发送邮件
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 初始化邮件相关信息.
	 * @param userName
	 * @param userEmail
	 * @param url
	 * @return
	 */
	private EMail initEMail(String userName, String userEmail, String url)
	{
		EMail mail = new EMail();
		mail.setMailFrom(this.sender);
		mail.setMailTo(userEmail);
		mail.setSubject(this.getSubject(userName));
		mail.setContent(this.getContent(userName, url));
		return mail;
	}
	
	/**
	 * 返回邮件发送的主题.
	 * @param userName
	 * @return
	 */
	protected String getSubject(String userName)
	{
		return "系统邮件发送-密码重置！";
	}
	
	/**
	 * 邮件发送的内容.
	 * @param userName
	 * @return
	 */
	protected String getContent(String userName, String url)
	{
		return userName + "：<br/>    您好！您的密码重置申请已被确认，请于XX天前到地址["
			+ url + "]进行密码重置！请勿回复此邮件！";
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public String getSender()
	{
		return sender;
	}

	public void setSender(String sender)
	{
		this.sender = sender;
	}

	public int getSessionDays()
	{
		return sessionDays;
	}

	public void setSessionDays(int sessionDays)
	{
		this.sessionDays = sessionDays;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getUserPassword()
	{
		return userPassword;
	}

	public void setUserPassword(String userPassword)
	{
		this.userPassword = userPassword;
	}
}
