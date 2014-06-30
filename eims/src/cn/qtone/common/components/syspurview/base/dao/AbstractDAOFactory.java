package cn.qtone.common.components.syspurview.base.dao;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.qtone.common.components.syspurview.admin.dao.IAdminDAO;
import cn.qtone.common.components.syspurview.controlpanel.dao.IControlPanelDAO;
import cn.qtone.common.components.syspurview.core.group.dao.IUserGroupDAO;
import cn.qtone.common.components.syspurview.core.module.dao.IModuleDAO;
import cn.qtone.common.components.syspurview.core.role.dao.IRoleDAO;
import cn.qtone.common.components.syspurview.core.system.dao.ISystemDAO;
import cn.qtone.common.components.syspurview.core.user.dao.IUserDAO;
import cn.qtone.common.components.syspurview.cryptoguard.dao.IGetPwdDAO;
import cn.qtone.common.components.syspurview.login.dao.ILoginDAO;

/**
 * DAO的抽象工厂类.<br>
 * 该抽象类使用抽象工厂模式，用来获取不同数据库的工厂类，然后使用其获取
 * 指定的DAO。
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public abstract class AbstractDAOFactory
{
	/**
	 * Logger for this class
	 */
	protected static final Logger log = Logger.getLogger(MySQLDAOFactory.class);
	
	/**
	 * 获取角色的DAO.
	 */
	public abstract IRoleDAO getRoleDAO();
	
	/**
	 * 获取模块管理设置的DAO
	 * @return
	 */
	public abstract IModuleDAO getModuleDAO();
	
	/**
	 * 获取子系统操作管理的DAO
	 * @return
	 */
	public abstract ISystemDAO getSystemDAO();
	
	/**
	 * 获取用户操作管理的DAO
	 * @return
	 */
	public abstract IUserDAO getUserDAO();
	
	/**
	 * 获取登陆验证的DAO
	 * @return
	 */
	public abstract ILoginDAO getLoginDAO();
	
	/**
	 * 获取后台管理的DAO
	 * @return
	 */
	public abstract IAdminDAO getAdminDAO();
	
	/**
	 * 控制面板的DAO
	 * @return
	 */
	public abstract IControlPanelDAO getControlPanelDAO();
	
	/**
	 * 获取密码保护取回密码的DAO
	 * @return
	 */
	public abstract IGetPwdDAO getCryptoguardDAO();
	
	/**
	 * 获取用户组管理的DAO.
	 * @return
	 */
	public abstract IUserGroupDAO getUserGroupDAO();
	
	/**
	 * 获取不同数据库的DAO工厂.
	 * @param dbType
	 * @return
	 */
	public static AbstractDAOFactory getDAOFactory(String dbType)
	{
		if (log.isInfoEnabled()) log.info("获取DAO工厂类！数据库类型[" + dbType + "]");
		String type = StringUtils.trimToEmpty(dbType).toLowerCase().intern();
		if (type == "mysql") {
			return new MySQLDAOFactory();
		} 
		return new MySQLDAOFactory();
	}
}
