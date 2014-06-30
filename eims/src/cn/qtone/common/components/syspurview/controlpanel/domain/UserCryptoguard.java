package cn.qtone.common.components.syspurview.controlpanel.domain;

import org.apache.commons.lang.StringUtils;

/**
 * 用户密码保护信息Bean.
 * 
 * @author 马必强
 *
 */
public class UserCryptoguard
{
	private int userId;
	
	private String userMobile;
	
	private String userEmail;
	
	private String userQuestion;
	
	private String userAnswer;
	
	private boolean useEmailMethod = true;

	public boolean isUseEmailMethod()
	{
		return useEmailMethod;
	}

	public void setUseEmailMethod(boolean useEmailMethod)
	{
		this.useEmailMethod = useEmailMethod;
	}

	public String getUserAnswer()
	{
		return userAnswer == null ? "" : userAnswer;
	}

	public void setUserAnswer(String userAnswer)
	{
		this.userAnswer = StringUtils.trimToEmpty(userAnswer);
	}

	public String getUserEmail()
	{
		return userEmail == null ? "" : userEmail;
	}

	public void setUserEmail(String userEmail)
	{
		this.userEmail = StringUtils.trimToEmpty(userEmail);
	}

	public int getUserId()
	{
		return userId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	public String getUserMobile()
	{
		return userMobile == null ? "" : userMobile;
	}

	public void setUserMobile(String userMobile)
	{
		this.userMobile = StringUtils.trimToEmpty(userMobile);
	}

	public String getUserQuestion()
	{
		return userQuestion == null ? "" : userQuestion;
	}

	public void setUserQuestion(String userQuestion)
	{
		this.userQuestion = StringUtils.trimToEmpty(userQuestion);
	}
}
