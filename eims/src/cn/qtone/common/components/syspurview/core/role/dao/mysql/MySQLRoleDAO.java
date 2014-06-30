package cn.qtone.common.components.syspurview.core.role.dao.mysql;

import java.util.List;

import cn.qtone.common.components.syspurview.core.role.dao.BaseRoleDAO;
import cn.qtone.common.components.syspurview.core.role.dao.RoleMapper;
import cn.qtone.common.components.syspurview.core.role.domain.Role;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 角色管理的mysql操作DAO.
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public class MySQLRoleDAO extends BaseRoleDAO
{

	/**
	 * 根据角色ID查询角色信息Bean.
	 */
	public Role getRole(int roleId)
	{
		if (log.isInfoEnabled()) log.info("获取角色信息Bean[" + roleId + "]!");
		String sql = "SELECT roleId,roleName,addTime,roleDetail FROM sys_role "
			+ "WHERE roleId=" + roleId;
		return (Role)this.getJdbcTemplate().queryForObject(sql, 
				new RoleMapper());
	}

	/**
	 * 根据角色名称进行查询.
	 */
	public List query(String roleName)
	{
		if (log.isInfoEnabled()) log.info("根据角色名称查询角色[" + roleName + "]!");
		String sql = "SELECT roleId,roleName,addTime,roleDetail FROM sys_role " +
			"WHERE roleName LIKE '%" + StringUtil.toDBFilter(roleName) + "%'";
		return this.getJdbcTemplate().query(sql, new RoleMapper());
	}

	/**
	 * 查询，简单的列表查询
	 */
	public List query()
	{
		if (log.isInfoEnabled()) log.info("获取所有角色列表!");
		String sql = "SELECT roleId,roleName,addTime,roleDetail FROM sys_role";
		return this.getJdbcTemplate().query(sql, new RoleMapper());
	}

	/**
	 * 添加角色
	 */
	public boolean save(Role role)
	{
		if (log.isInfoEnabled()) log.info("执行角色的数据库插入！");
		String sql = "INSERT INTO sys_role(roleName,addTime,roleDetail) VALUES(" + 
			"?,now(),?)";
		this.getJdbcTemplate().update(sql, 
				new Object[] {role.getName(), role.getDetail()});
		return true;
	}
}
