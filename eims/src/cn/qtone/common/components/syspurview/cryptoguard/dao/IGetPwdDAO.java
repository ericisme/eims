package cn.qtone.common.components.syspurview.cryptoguard.dao;

import cn.qtone.common.components.syspurview.controlpanel.domain.UserCryptoguard;
import cn.qtone.common.components.syspurview.cryptoguard.domain.UserEmailSession;

/**
 * 用户取回密码DAO的接口.
 * 
 * @author 马必强
 *
 */
public interface IGetPwdDAO
{
	/**
	 * 根据用户名获取用户的密码保护资料.
	 * @param userName
	 * @return
	 */
	public UserCryptoguard getUserCryptoguard(String userName);
	
	/**
	 * 用户的密码重置.
	 * @param loginName
	 * @param password
	 */
	public void resetUserPassword(String loginName, String password);
	
	/**
	 * 保存发送到用户电邮中的随机参数信息.
	 * @param userName
	 * @param sid
	 */
	public void saveEmailSession(String userName, String sid);
	
	/**
	 * 根据唯一的sessionID来获取用户名称（email方式取回密码时）
	 * @param sid
	 * @return
	 */
	public UserEmailSession getUserEmailSession(String sid);
}
