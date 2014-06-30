package cn.qtone.common.components.syspurview.core.role.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.qtone.common.components.syspurview.core.role.domain.Role;

/**
 * 角色的对象参数化.<br>
 * 可以将数据库中查询出来的结果集转换成指定的对象.
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public class RoleMapper implements RowMapper
{
	public Role mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		Role role = new Role();
		role.setRoleId(rs.getInt("roleId"));
		role.setName(rs.getString("roleName"));
		role.setAddTime(rs.getString("addTime"));
		role.setDetail(rs.getString("roleDetail"));
		return role;
	}

}
