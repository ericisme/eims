package cn.qtone.common.components.syspurview.core.role.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 指定的角色的模块信息.
 * @author 马必强
 *
 */
public class RoleModule
{
	private int moduleId; // 模块的唯一名称
	
	private String moduleName; // 模块的名称
	
	private List<RoleModuleFunc> roleFuncList; // 角色对当前模块的操作功能列表
	
	public RoleModule()
	{
		this.roleFuncList = new ArrayList<RoleModuleFunc>();
	}

	public List<RoleModuleFunc> getRoleFuncList()
	{
		return roleFuncList;
	}

	public void addFunc(RoleModuleFunc func)
	{
		this.roleFuncList.add(func);
	}

	public int getModuleId()
	{
		return moduleId;
	}

	public void setModuleId(int moduleId)
	{
		this.moduleId = moduleId;
	}

	public String getModuleName()
	{
		return moduleName;
	}

	public void setModuleName(String moduleName)
	{
		this.moduleName = StringUtils.trimToEmpty(moduleName);
	}
}
