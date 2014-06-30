package cn.qtone.common.components.syspurview.core.role.dao;

import java.util.List;

import cn.qtone.common.components.syspurview.core.role.domain.Role;

/**
 * 角色管理的DAO接口.
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public interface IRoleDAO
{
	/**
	 * 保存一个角色信息.
	 * @param role
	 * @return
	 */
	boolean save(Role role);
	
	/**
	 * 更新一个角色的信息.
	 * @param role
	 * @return
	 */
	boolean update(Role role);
	
	/**
	 * 删除指定的角色.
	 * @param roleId
	 * @return
	 */
	int remove(int[] roleId);
	
	/**
	 * 根据角色ID获取一个角色的信息.
	 * @param roleId
	 * @return
	 */
	Role getRole(int roleId);
	
	/**
	 * 根据角色名称来查询角色列表.模糊查询
	 * @param roleName
	 * @return
	 */
	List query(String roleName);
	
	/**
	 * 查询所有的角色信息.
	 * @return
	 */
	List query();
	
	/**
	 * 获取指定角色对指定子系统的功能模块权限列表.
	 * @param roleId
	 * @param sysId
	 * @return
	 */
	List getModuleList(int roleId, int sysId);
	
	/**
	 * 查询指定模块下的所有可授权的模块列表.
	 * @param roleId
	 * @param sysId
	 * @param token
	 * @return
	 */
	List getModuleList(int roleId, int sysId, String token);
	
	/**
	 * 删除指定角色对指定模块的权限.
	 * @param roleId
	 * @param moduleId
	 */
	void removeRolePurview(int roleId, int[] moduleId);
	
	/**
	 * 删除指定角色的全部权限.
	 * @param roleId
	 */
	void removeRolePurview(int roleId);
	
	/**
	 * 添加角色的权限.
	 */
	void addRolePurview(int roleId, int[] moduleId, int[][] funcId);
	
	/**
	 * 删除指定角色对指定模块的权限.
	 * @param roleId
	 * @param moduleId
	 */
	void removeModulePurview(int roleId, int[] moduleId);
}
