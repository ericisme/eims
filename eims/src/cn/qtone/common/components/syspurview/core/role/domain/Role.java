package cn.qtone.common.components.syspurview.core.role.domain;

import org.apache.commons.lang.StringUtils;

/**
 * 角色对象.
 * 
 * @author 马必强
 * @version 1.0
 * 
 */
public class Role
{
	private int roleId; // 角色唯一ID

	private String name; // 角色名称

	private String addTime; // 角色的添加时间

	private String detail; // 角色介绍或备注

	public String getAddTime()
	{
		return addTime;
	}

	public void setAddTime(String addTime)
	{
		this.addTime = StringUtils.trimToEmpty(addTime);
	}

	public String getDetail()
	{
		return detail;
	}

	public void setDetail(String detail)
	{
		this.detail = StringUtils.trimToEmpty(detail);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = StringUtils.trimToEmpty(name);
	}

	public int getRoleId()
	{
		return roleId;
	}

	public void setRoleId(int roleId)
	{
		this.roleId = roleId;
	}
}
