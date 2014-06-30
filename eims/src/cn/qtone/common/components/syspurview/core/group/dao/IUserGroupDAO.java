package cn.qtone.common.components.syspurview.core.group.dao;

import java.util.List;

import cn.qtone.common.components.syspurview.core.group.domain.UserGroup;

/**
 * 用户组管理的DAO接口.
 * 
 * @author 马必强
 *
 */
public interface IUserGroupDAO
{
	/**
	 * 查询所有的用户组信息.
	 * @return
	 */
	public List listAll();
	
	/**
	 * 添加用户组.
	 * @param ug
	 */
	public void addUserGroup(UserGroup ug);
	
	/**
	 * 取得刚插入的用户组ID
	 */
	public int getGroupId(UserGroup ug);
	
	/**
	 * 获取指定的用户组信息.
	 * @param groupId
	 * @return
	 */
	public UserGroup getUserGroup(int groupId);
	
	/**
	 * 更新用户组信息.
	 * @param ug
	 */
	public void updateUserGroup(UserGroup ug);
	
	/**
	 * 删除指定的用户组.
	 * @param groupId
	 */
	public void remove(int[] groupId);
	
	/**
	 * 查看指定用户组的角色列表信息.
	 * @param groupId
	 * @return
	 */
	public List getGroupRole(int groupId);
	
	/**
	 * 新增用户组角色关系
	 */
	public void addGroupRole(int groupId, int[] roleIds);
	
	/**
	 * 按用户组ID删除用户组角色
	 */
	public void removeGroupRole(int groupId);
}
