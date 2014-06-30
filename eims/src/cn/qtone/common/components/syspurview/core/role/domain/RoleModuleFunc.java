package cn.qtone.common.components.syspurview.core.role.domain;

import cn.qtone.common.components.syspurview.core.module.domain.ModuleSingleFunc;

/**
 * 角色对指定模块的单个功能操作权限Bean.
 * @author 马必强
 *
 */
public class RoleModuleFunc extends ModuleSingleFunc
{
	private boolean havePurview; // 对当前功能是否具有操作权限

	public boolean isHavePurview()
	{
		return havePurview;
	}

	public void setHavePurview(boolean havePurview)
	{
		this.havePurview = havePurview;
	}
}
