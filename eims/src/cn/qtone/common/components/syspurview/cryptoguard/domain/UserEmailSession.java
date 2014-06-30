package cn.qtone.common.components.syspurview.cryptoguard.domain;

/**
 * 密码取回使用电子邮件发送的相关信息.
 * 
 * @author 马必强
 *
 */
public class UserEmailSession
{
	private String userName;
	
	private String sessionId;
	
	private String sendTime;

	public String getSendTime()
	{
		return sendTime;
	}

	public void setSendTime(String sendTime)
	{
		this.sendTime = sendTime;
	}

	public String getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}
}
