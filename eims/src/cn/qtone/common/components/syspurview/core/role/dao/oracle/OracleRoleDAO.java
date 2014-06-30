package cn.qtone.common.components.syspurview.core.role.dao.oracle;

import java.util.List;

import cn.qtone.common.components.syspurview.core.role.dao.BaseRoleDAO;
import cn.qtone.common.components.syspurview.core.role.dao.RoleMapper;
import cn.qtone.common.components.syspurview.core.role.domain.Role;
import cn.qtone.common.utils.base.StringUtil;

/**
 * oracle数据库的角色管理DAO.
 * 
 * @author 马必强
 *
 */
public class OracleRoleDAO extends BaseRoleDAO
{
	/**
	 * 根据角色ID查询角色信息Bean.
	 */
	public Role getRole(int roleId)
	{
		if (log.isInfoEnabled()) log.info("(oracle)获取角色信息Bean[" + roleId + "]!");
		String sql = "SELECT roleId,roleName,to_char(addTime,'yyyy-mm-dd hh24:Mi:ss') "
			+ "AS addTime,roleDetail FROM sys_role WHERE roleId=" + roleId;
		return (Role)this.getJdbcTemplate().queryForObject(sql, 
				new RoleMapper());
	}

	/**
	 * 根据角色名称进行查询.
	 */
	public List query(String roleName)
	{
		if (log.isInfoEnabled()) log.info("(oracle)根据角色名称查询角色[" + roleName + "]!");
		String sql = "SELECT roleId,roleName,to_char(addTime,'yyyy-mm-dd hh24:Mi:ss') "
			+ "AS addTime,roleDetail FROM sys_role WHERE roleName LIKE '%" 
			+ StringUtil.toDBFilter(roleName) + "%'";
		return this.getJdbcTemplate().query(sql, new RoleMapper());
	}

	/**
	 * 查询，简单的列表查询
	 */
	public List query()
	{
		if (log.isInfoEnabled()) log.info("(oracle)获取所有角色列表!");
		String sql = "SELECT roleId,roleName,to_char(addTime,'yyyy-mm-dd hh24:Mi:ss') "
			+ "AS addTime,roleDetail FROM sys_role";
		return this.getJdbcTemplate().query(sql, new RoleMapper());
	}

	/**
	 * 添加角色
	 */
	public boolean save(Role role)
	{
		if (log.isInfoEnabled()) log.info("(oracle)执行角色的数据库插入！");
		String sql = "INSERT INTO sys_role(roleName,addTime,roleDetail) VALUES(" + 
			"?,sysdate,?)";
		this.getJdbcTemplate().update(sql, 
				new Object[] {role.getName(), role.getDetail()});
		return true;
	}
}
