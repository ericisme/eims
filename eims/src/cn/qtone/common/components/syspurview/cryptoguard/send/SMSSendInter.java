package cn.qtone.common.components.syspurview.cryptoguard.send;

/**
 * 密码取回的短信发送接口.
 * 
 * 短信重设密码分为两步，第一步是在正确回答问题后，系统会自动发送一随机码
 * 到用户设置的手机号码上；第二步是用户在收到随机码后在页面上输入收到的随
 * 机码，在系统验证正确后，即可进入密码重置页面，输入密码完成密码重新设置
 * 即可。
 * 
 * @author 马必强
 *
 */
public interface SMSSendInter
{
	/**
	 * 向用户的手机号码发送一指定的随机码。
	 * @param mobile
	 * @param randomCode
	 * @return
	 */
	public boolean sendRandomCode(String mobile, String randomCode);
}
