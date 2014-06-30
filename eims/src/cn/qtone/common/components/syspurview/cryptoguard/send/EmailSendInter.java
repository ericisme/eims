package cn.qtone.common.components.syspurview.cryptoguard.send;



/**
 * 密码保护的邮件信息发送接口。
 * 
 * @author 马必强
 *
 */
public interface EmailSendInter
{
	/**
	 * 邮件发送通知用户进行密码修改.
	 * @param user
	 * @param url 重置密码的连接地址，需要发送到用户邮箱
	 */
	public boolean sendEmail(String userName, String userEmail, String url);
	
	/**
	 * 获取email的session保存的天数.
	 * @return
	 */
	public int getSessionDays();
}
