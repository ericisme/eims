package cn.qtone.common.components.syspurview.admin.service;

import java.util.ArrayList;
import java.util.List;

import cn.qtone.common.components.syspurview.admin.dao.IAdminDAO;
import cn.qtone.common.components.syspurview.core.module.ModuleTree;
import cn.qtone.common.components.syspurview.core.module.service.ModuleService;
import cn.qtone.common.components.syspurview.core.user.domain.AbstractUser;

/**
 * 权限系统后台管理的业务处理类.
 * 
 * @author 马必强
 *
 */
public class AdminService
{
	private IAdminDAO dao; // 后台管理的DAO

	public IAdminDAO getDao()
	{
		return dao;
	}

	public void setDao(IAdminDAO dao)
	{
		this.dao = dao;
	}

	/**
	 * 获取指定系统下指定菜单下的二级模块列表.
	 */
	@SuppressWarnings("unchecked")
	public List getMenu(int sysId, String pToken, AbstractUser user)
	{
		List result = null;
		// 如果是超级管理员则获取所有可用模块列表,否则只获取具有权限的列表
		if (user.isSuperManager()) {
			if (pToken.equals("")) result = this.dao.getAllMenu(sysId);
			result = this.dao.getAllMenu(sysId, pToken);
		} else {
			if (pToken.equals("")) result = this.dao.getMenu(sysId, user.getRoleId());
			result = this.dao.getMenu(sysId, user.getRoleId(), pToken);
		}
		result = ModuleTree.getModuleTree(result, false);
		List cont = new ArrayList(); // 包含list,每一个list表示一个一级模块和其子模块
		if (result.size() < 1) return cont;
		List tmp = ModuleService.splitModuleList(result);
		for (Object obj : tmp) cont.add(obj);
		return cont;
	}
}
