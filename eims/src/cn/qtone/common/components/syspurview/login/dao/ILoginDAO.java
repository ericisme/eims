package cn.qtone.common.components.syspurview.login.dao;

import cn.qtone.common.components.syspurview.core.user.domain.AbstractUser;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.components.syspurview.login.domain.PurviewMap;

/**
 * 用户登陆的DAO接口.
 * 
 * @author 马必强
 *
 */
public interface ILoginDAO
{
	/**
	 * 根据用户名称获取用户信息，包括角色信息。并验证其密码的正确性！
	 * @param userName
	 * @return
	 */
	public User getUserInfo(String userName, String password);
	
	/**
	 * 获取指定用户的权限.
	 * @param groupId
	 * @return
	 */
	public PurviewMap getRolePurview(AbstractUser user);
	
	/**
	 * 更新用户的最后登陆时间和登陆IP.
	 * @param loginIP
	 */
	public void updateLastLogin(int userId, String loginIP);
	
	/**
	 * 获取代理登陆的用户信息.
	 * @param userId
	 * @param proxyUserId
	 * @return
	 */
	public User getProxyUser(int userId, String userName, int proxyUserId);
	
	/**
	 * 根据ID来获取用户信息.
	 * @param userId
	 * @return
	 */
	public User getUser(int userId);
}
