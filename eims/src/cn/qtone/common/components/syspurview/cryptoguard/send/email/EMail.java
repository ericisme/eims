package cn.qtone.common.components.syspurview.cryptoguard.send.email;

/**
 * 邮件发送的邮件信息实体.
 * 
 * @author 马必强
 *
 */
public class EMail
{
	private String subject; // 邮件标题
	
	private String mailFrom; // 邮件来源
	
	private String mailTo; // 邮件的接收对象
	
	private String content; // 邮件内容
	
	private String contentType = "text/html;charset=gb2312"; // 邮件类型

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public String getMailFrom()
	{
		return mailFrom;
	}

	public void setMailFrom(String mailFrom)
	{
		this.mailFrom = mailFrom;
	}

	public String getMailTo()
	{
		return mailTo;
	}

	public void setMailTo(String mailTo)
	{
		this.mailTo = mailTo;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}
}
