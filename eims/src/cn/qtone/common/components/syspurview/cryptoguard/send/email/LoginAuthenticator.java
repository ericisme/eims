package cn.qtone.common.components.syspurview.cryptoguard.send.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 邮件服务器登陆验证类.
 * 
 * @author 马必强
 * 
 */
public class LoginAuthenticator extends Authenticator
{
	private String userName;

	private String userPwd;

	public LoginAuthenticator(String userName, String userPassword)
	{
		this.userName = userName;
		this.userPwd = userPassword;
	}

	public PasswordAuthentication getPasswordAuthentication()
	{
		return new PasswordAuthentication(userName, userPwd);
	}
}
