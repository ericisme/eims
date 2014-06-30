package cn.qtone.common.components.syspurview.cryptoguard.send.sms;

import org.apache.log4j.Logger;

import cn.qtone.common.components.syspurview.cryptoguard.send.SMSSendInter;

/**
 * 密码取回的短信发送接口实现.
 * 
 * @author 马必强
 *
 */
public class MySMSSender implements SMSSendInter
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MySMSSender.class);

	public boolean sendRandomCode(String mobile, String randomCode)
	{
		if (logger.isInfoEnabled()) {
			logger.info("短信发送随机验证码：code=" + randomCode);
		}
		return true;
	}
}
