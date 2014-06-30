package cn.qtone.common.components.syspurview.controlpanel.dao;

import cn.qtone.common.components.syspurview.controlpanel.domain.UserCryptoguard;
import cn.qtone.common.components.syspurview.core.user.domain.User;


/**
 * 控制面板的DAO接口.
 * 
 * @author 马必强
 *
 */
public interface IControlPanelDAO
{
	/**
	 * 更新用户的个人信息
	 * @param user
	 */
	public void updateUserInfo(User user);
	
	/**
	 * 用户密码修改
	 */
	public void updatePassword(int userId, String newPwd,String plainCode);
	
	/**
	 * 获取用户的密码保护设置信息
	 * @param userId
	 * @return
	 */
	public UserCryptoguard getCryptoguard(int userId);
	
	/**
	 * 更新用户的密码保护资料
	 * @param guard
	 */
	public void updateUserCryptoguard(UserCryptoguard guard);
}
