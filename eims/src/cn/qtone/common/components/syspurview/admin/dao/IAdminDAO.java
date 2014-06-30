package cn.qtone.common.components.syspurview.admin.dao;

import java.util.List;


/**
 * 系统后台管理的DAO接口.
 * 
 * @author 马必强
 *
 */
public interface IAdminDAO
{
	/**
	 * 获取指定系统下的所有一级和二级模块列表.
	 * @param sysId
	 * @return
	 */
	public List getAllMenu(int sysId);
	
	/**
	 * 获取指定系统下指定菜单模块下的所有一级和二级模块列表.
	 * @param sysId
	 * @param pToken
	 * @return
	 */
	public List getAllMenu(int sysId, String pToken);
	
	/**
	 * 获取指定用户对指定系统下的所有可用一级和二级菜单列表.
	 * @param sysId
	 * @param roleId
	 * @return
	 */
	public List getMenu(int sysId, int[] roleId);
	
	/**
	 * 获取指定用户对指定系统下的指定菜单模块下的所有可用一级和二级模块列表.
	 * @param sysId
	 * @param roleId
	 * @param pToken
	 * @return
	 */
	public List getMenu(int sysId, int[] roleId, String pToken);
}
