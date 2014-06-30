package cn.qtone.common.components.syspurview.base.dao;

import cn.qtone.common.components.syspurview.admin.dao.IAdminDAO;
import cn.qtone.common.components.syspurview.admin.dao.mysql.MySQLAdminDAO;
import cn.qtone.common.components.syspurview.controlpanel.dao.BaseControlPanelDAO;
import cn.qtone.common.components.syspurview.controlpanel.dao.IControlPanelDAO;
import cn.qtone.common.components.syspurview.core.group.dao.IUserGroupDAO;
import cn.qtone.common.components.syspurview.core.group.dao.mysql.MySQLUserGroupDAO;
import cn.qtone.common.components.syspurview.core.module.dao.IModuleDAO;
import cn.qtone.common.components.syspurview.core.module.dao.mysql.MySQLModuleDAO;
import cn.qtone.common.components.syspurview.core.role.dao.IRoleDAO;
import cn.qtone.common.components.syspurview.core.role.dao.mysql.MySQLRoleDAO;
import cn.qtone.common.components.syspurview.core.system.dao.ISystemDAO;
import cn.qtone.common.components.syspurview.core.system.dao.mysql.MySQLSystemDAO;
import cn.qtone.common.components.syspurview.core.user.dao.IUserDAO;
import cn.qtone.common.components.syspurview.core.user.dao.mysql.MySQLUserDAO;
import cn.qtone.common.components.syspurview.cryptoguard.dao.IGetPwdDAO;
import cn.qtone.common.components.syspurview.cryptoguard.dao.mysql.MySQLGetPwdDAO;
import cn.qtone.common.components.syspurview.login.dao.ILoginDAO;
import cn.qtone.common.components.syspurview.login.dao.mysql.MySQLLoginDAO;

/**
 * 权限系统MySQL数据库的DAO工厂.
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public class MySQLDAOFactory extends AbstractDAOFactory
{
	/**
	 * 获取角色的DAO.
	 * @return
	 */
	@Override
	public IRoleDAO getRoleDAO()
	{
		if (log.isInfoEnabled()) log.info("[mysql]工厂方法获取角色DAO！");
		return new MySQLRoleDAO();
	}

	/**
	 * 获取模块管理设置的DAO
	 */
	@Override
	public IModuleDAO getModuleDAO()
	{
		if (log.isInfoEnabled()) log.info("[mysql]工厂方法获取模块管理设置DAO！");
		return new MySQLModuleDAO();
	}

	/**
	 * 获取子系统管理操作的DAO
	 */
	@Override
	public ISystemDAO getSystemDAO()
	{
		if (log.isInfoEnabled()) log.info("[mysql]工厂方法获取子系统管理设置DAO！");
		return new MySQLSystemDAO();
	}

	/**
	 * 获取用户管理的DAO
	 */
	@Override
	public IUserDAO getUserDAO()
	{
		if (log.isInfoEnabled()) log.info("[mysql]工厂方法获取用户管理DAO！");
		return new MySQLUserDAO();
	}

	/**
	 * 获取登陆验证的DAO
	 */
	@Override
	public ILoginDAO getLoginDAO()
	{
		if (log.isInfoEnabled()) log.info("[mysql]工厂方法获取登陆验证DAO！");
		return new MySQLLoginDAO();
	}
	
	/**
	 * 获取后台管理的DAO
	 */
	@Override
	public IAdminDAO getAdminDAO()
	{
		if (log.isInfoEnabled()) log.info("[mysql]工厂方法获取后台管理DAO！");
		return new MySQLAdminDAO();
	}
	
	/**
	 * 获取控制面板的DAO
	 */
	@Override
	public IControlPanelDAO getControlPanelDAO()
	{
		if (log.isInfoEnabled()) log.info("[mysql]工厂方法获取控制面板DAO！");
		return new BaseControlPanelDAO();
	}

	/**
	 * 获取密码保护取回密码的DAO
	 */
	@Override
	public IGetPwdDAO getCryptoguardDAO()
	{
		if (log.isInfoEnabled()) log.info("[mysql]工厂方法获取密码取回DAO！");
		return new MySQLGetPwdDAO();
	}

	/**
	 * 获取用户组管理的DAO
	 */
	@Override
	public IUserGroupDAO getUserGroupDAO()
	{
		if (log.isInfoEnabled()) log.info("[mysql]工厂方法获取用户组管理的DAO！");
		return new MySQLUserGroupDAO();
	}
}
