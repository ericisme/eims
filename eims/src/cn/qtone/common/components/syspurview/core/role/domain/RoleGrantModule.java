package cn.qtone.common.components.syspurview.core.role.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * 提交角色被授予的模块的权限信息.
 * 
 * @author 马必强
 *
 */
public class RoleGrantModule
{
	private int grantRoleId; // 角色的唯一ID
	
	private int[] moduleId; // 当前提交的模块ID
	
	private Map<Object,int[]> moduleFuncs; // 设置每一个模块的功能列表
	
	public RoleGrantModule()
	{
		this.moduleFuncs = new HashMap<Object,int[]>();
	}

	public int[] getModuleId()
	{
		return moduleId;
	}

	public void setModuleId(int[] moduleId)
	{
		this.moduleId = moduleId;
	}

	public int getGrantRoleId()
	{
		return grantRoleId;
	}

	public void setGrantRoleId(int roleId)
	{
		this.grantRoleId = roleId;
	}
	
	public void setModuleFunc(int moduleId, int[] funcs)
	{
		if (funcs == null || funcs.length < 1) return;
		this.moduleFuncs.put(moduleId, funcs);
	}
	
	public int[] getModuleFunc(int moduleId)
	{
		return this.moduleFuncs.get(moduleId);
	}
}
