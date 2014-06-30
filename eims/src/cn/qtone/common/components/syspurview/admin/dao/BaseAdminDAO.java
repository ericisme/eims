package cn.qtone.common.components.syspurview.admin.dao;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import cn.qtone.common.components.syspurview.core.module.domain.TokenMaker;
import cn.qtone.common.mvc.dao.BaseDAO;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 后台管理的DAO接口的通用实现.
 * 
 * @author 马必强
 *
 */
public abstract class BaseAdminDAO extends BaseDAO implements IAdminDAO
{
	public List getAllMenu(int sysId)
	{
		if (log.isInfoEnabled()) log.info("获取子系统" + sysId + "下的所有一二级模块~");
		String sql = "SELECT moduleId,moduleName,moduleType,moduleStatus,moduleURL,"
			+ "token,moduleSeq FROM sys_module WHERE sysId=" + sysId
			+ " AND moduleStatus IN (0,1) AND LENGTH(TRIM(token))<=" + TokenMaker.TOKEN_LEN * 2;
		return this.getJdbcTemplate().query(sql, new LeftMenuMapper());
	}

	public List getAllMenu(int sysId, String pToken)
	{
		if (log.isInfoEnabled()) {
			log.info("获取子系统" + sysId + "下菜单" + pToken + "下的一二级模块！");
		}
		int len = pToken.length() + TokenMaker.TOKEN_LEN;
		String sql = "SELECT moduleId,moduleName,moduleType,moduleStatus,moduleURL,"
			+ "token,moduleSeq FROM sys_module WHERE sysId=" + sysId 
			+ " AND moduleStatus IN (0,1) AND token LIKE '" + pToken + "%' "
			+ "AND LENGTH(TRIM(token)) IN(" + len + ","  + (len + TokenMaker.TOKEN_LEN) + ")";
		return this.getJdbcTemplate().query(sql, new LeftMenuMapper());
	}
	
	public List getMenu(int sysId, int[] roleId)
	{
		String roleList = this.getRoleList(roleId);
		String sql = "SELECT token FROM sys_role_purview a LEFT JOIN sys_module b ON "
			+ "a.moduleId=b.moduleId AND b.sysId=" + sysId + " AND LENGTH(TRIM(b.token))<="
			+ TokenMaker.TOKEN_LEN * 2 + " WHERE roleId IN(" + roleList + ") GROUP BY token";
		if (log.isInfoEnabled()) {
			log.info("获取子系统" + sysId + "下角色" + roleList + "的一二级权限菜单！"+sql);
		}
		return this.getMenu(this.getTokenList(this.getJdbcTemplate().queryForRowSet(sql)));
	}

	public List getMenu(int sysId, int[] roleId, String pToken)
	{
		String roleList = this.getRoleList(roleId);
		int len = pToken.length() + TokenMaker.TOKEN_LEN;
		String sql = "SELECT token FROM sys_role_purview a LEFT JOIN sys_module b ON "
			+ "a.moduleId=b.moduleId AND b.sysId=" + sysId + " AND LENGTH(TRIM(b.token)) IN("
			+ len + ","  + (len + TokenMaker.TOKEN_LEN) + ") AND token LIKE '" + pToken + "%' "
			+ "WHERE roleId IN(" + roleList + ") GROUP BY token";
		if (log.isInfoEnabled()) {
			log.info("获取子系统" + sysId + "下角色" + roleList + "对菜单" + pToken + "下的一二级权限菜单！"+sql);
		}
		return this.getMenu(this.getTokenList(this.getJdbcTemplate().queryForRowSet(sql)));
	}
	
	/**
	 * 将指定的角色数组转换成符合数据库中的查询模式.
	 * @param roleId
	 * @return
	 */
	protected String getRoleList(int[] roleId)
	{
		String roleList = StringUtil.join(roleId, ",");
		if (roleList.equals("")) roleList = "0";
		return roleList;
	}
	
	/**
	 * 获取普通用户具有的权限的功能模块和其父菜单模块的token列表.
	 * @param rs
	 * @return
	 */
	protected String getTokenList(SqlRowSet rs)
	{
		StringBuilder buf = new StringBuilder("''");
		while (rs.next()) {
			String token = StringUtil.trim(rs.getString("token"));
			buf.append(",'" + token + "'");
			token = TokenMaker.getParentToken(token).intern();
			if (token != "") buf.append(",'" + token + "'");
		}
		if (log.isInfoEnabled()) log.info("获取的模块列表：" + buf);
		return buf.toString();
	}
	
	protected List getMenu(String tokenList)
	{
		String sql = "SELECT moduleId,moduleName,moduleType,moduleStatus,moduleURL,"
			+ "token,moduleSeq FROM sys_module WHERE token IN(" + tokenList + ") "
			+ "AND moduleStatus IN (0,1)";
		return this.getJdbcTemplate().query(sql, new LeftMenuMapper());
	}
}
