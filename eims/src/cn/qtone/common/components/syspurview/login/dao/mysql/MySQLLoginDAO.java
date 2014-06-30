package cn.qtone.common.components.syspurview.login.dao.mysql;

import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.components.syspurview.login.dao.BaseLoginDAO;

/**
 * 用户登陆系统的mysql数据库登陆DAO实现.
 * 
 * @author 马必强
 *
 */
public class MySQLLoginDAO extends BaseLoginDAO
{
	/**
	 * 根据用户名称来获取用户的信息.
	 */
	public User getUserInfo(String userName, String password)
	{
		if (log.isInfoEnabled()) log.info("[mysql]验证用户[" + userName + "," + password + "]登陆！");
		StringBuilder sql =
			new StringBuilder("SELECT userId,a.groupId,b.loginSys,loginName,loginPassword,realName,mobile,email,");
		sql.append(" addTime,lastLoginTime,lastLoginIP,loginTimes,isLock,isSuper,user_type,a.town_id ,a.agency_id,a.school_id,a.grade_id FROM sys_user a ");
		sql.append( " LEFT JOIN sys_group b ON a.groupId=b.groupId WHERE a.loginName=? AND a.loginPassword=? and a.status=0");
		if(log.isInfoEnabled()) log.info("用户登录："+sql);
		return this.getUser(sql.toString(), userName, password);
	}

	public void updateLastLogin(int userId, String loginIP)
	{
		String sql = "UPDATE sys_user SET lastLoginTime=now(),lastLoginIP=?,"
			+ "loginTimes=loginTimes+1 WHERE userId=?";
		this.getJdbcTemplate().update(sql, new Object[] {loginIP, userId});
	}
	
	/**
	 * 获取要代理的用户的信息.
	 * @param userId 当前用户的唯一ID（系统管理员或超级管理员）
	 * @param proxyUserId 要代理的用户的唯一ID
	 * @return
	 */
	public User getProxyUser(int userId, String userName, int proxyUserId)
	{
		if (log.isInfoEnabled()) log.info("[mysql]用户[userId=" + userId + "]代理用户[userId=" + proxyUserId + "]进行登陆！");
		String sql = "SELECT userId,a.groupId,b.loginSys,loginName,loginPassword,realName,mobile,email,"
			+ "addTime,lastLoginTime,lastLoginIP,loginTimes,isLock,isSuper,USER_TYPE,isManageAllTown  FROM sys_user a "
			+ "LEFT JOIN sys_group b ON a.groupId=b.groupId WHERE a.userId=" + proxyUserId;
		return this.getProxyUser(sql, userId, userName);
	}
	
	/**
	 * 根据用户的ID来获取用户信息.
	 */
	public User getUser(int userId)
	{
		if (log.isInfoEnabled()) log.info("[mysql]用户[userId=" + userId + "]进行代理登陆后的回退操作！");
		String sql = "SELECT userId,a.groupId,b.loginSys,loginName,loginPassword,realName,mobile,email,"
			+ "addTime,lastLoginTime,lastLoginIP,loginTimes,isLock,isSuper,USER_TYPE,isManageAllTown FROM sys_user a "
			+ "LEFT JOIN sys_group b ON a.groupId=b.groupId WHERE a.userId=" + userId;
		return this.getUserInfo(this.getJdbcTplForQuery().queryForRowSet(sql));
	}
}
