package cn.qtone.common.components.syspurview.check.aop.proxy;

import cn.qtone.common.components.syspurview.login.domain.PurviewMap;

/**
 * 基础的权限处理类.<br>
 * 
 * 该类主要来获取指定角色的权限，同时为其子类提供权限的校验判断.
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public abstract class BasePurviewHandler
{
	private PurviewMap userPurview; // 当前用户的权限集合
	
	public BasePurviewHandler(PurviewMap userPurview)
	{
		this.userPurview = userPurview;
	}
	
	/**
	 * 获取当前的角色ID.
	 */
	protected PurviewMap getUserPurview()
	{
		return this.userPurview;
	}
	
	/**
	 * 检查当前角色对指定的业务处理是否有指定的操作权限.
	 * @param clazz 实际的业务处理类的Class
	 * @param operate 操作标识字符串，比如add
	 * @return 返回true表示有操作权限，否则没有
	 */
	protected boolean havePurview(Class clazz, String operate)
	{
		if (this.userPurview == null) return false;
		return this.userPurview.isAllowed(clazz.getName(), operate);
	}
}
