package cn.qtone.common.components.syspurview.core.user.dao;

import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.components.syspurview.core.user.qvo.QvoUser;
import cn.qtone.common.mvc.dao.Page;


/**
 * 用户管理的数据库操作DAO接口。
 * 
 * @author 马必强
 *
 */
public interface IUserDAO
{
	/**
	 * 查询用户列表.
	 * @param page
	 */
	public void query(Page page);
	
	/**
	 * 根据用户名称查询用户列表.
	 * @param page
	 * @param name
	 */
	public void query(Page page, String name);
	
	/**
	 * 根据角色ID查询用户列表.
	 * @param page
	 * @param groupId
	 */
	public void query(Page page, int groupId);
	
	/**
	 * 根据用户名称和用户所属用户组来查询用户列表.
	 * @param page
	 * @param name
	 * @param groupId
	 */
	public void query(Page page,String name, int groupId);
	/**
	 * 根据用户名称和用户所属用户组来查询用户列表.
	 * @param page
	 * @param name
	 * @param groupId
	 */
	public void query(Page page,QvoUser qvo);
	
	/**
	 * 添加用户.
	 * @param user
	 */
	public void addUser(User user);
	
	/**
	 * 检测用户名是否唯一.若唯一则返回0，否则返回1
	 * @param loginName
	 * @return
	 */
	public int getUserCount(String loginName);
	
	/**
	 * 根据用户名称来获取用户的唯一ID
	 * @param loginName
	 * @return
	 */
	public int getUserIdByName(String loginName);
	
	/**
	 * 根据用户ID获取用户的信息.
	 * @param userId
	 * @return
	 */
	public User getUser(int userId);
	
	/**
	 * 更新用户信息.
	 * @param user
	 */
	public void updateUser(User user);
	
	/**
	 * 删除指定的用户
	 * @param userId
	 */
	public void removeUser(int[] userId);
	public void removeBorrow(int[] userId);
	
	/**
	 * 对指定的用户进行封锁操作
	 * @param userId
	 */
	public void lock(int[] userId);
	
	/**
	 * 对指定的用户进行解锁操作
	 * @param userId
	 */
	public void unlock(int[] userId);
	
	
	/**
	 * 修改密码
	 */
	public void updatePwd(int[] userId, String pwd, final String plainCode);
	
	/**
	 * 取得用戶鎮區ID列表
	 */
//	public Integer[] getUserTownIds(int userId);
}
