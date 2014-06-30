package cn.qtone.common.components.syspurview.core.group.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import cn.qtone.common.components.syspurview.core.group.domain.UserGroup;
import cn.qtone.common.components.syspurview.core.role.domain.Role;
import cn.qtone.common.mvc.dao.BaseDAO;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 用户组DAO接口的抽象实现类.
 * 
 * @author 马必强
 *
 */
public abstract class BaseUserGroupDAO extends BaseDAO implements IUserGroupDAO
{
	/**
	 * 获取指定用户组的信息.
	 * @param sql
	 * @return
	 */
	public UserGroup getUserGroup(int groupId)
	{
		String sql = "SELECT groupId,groupName,groupDetail,groupRoles,loginSys,book_limit,day_limit,deposit,fine FROM sys_group "
			+ "WHERE groupId=" + groupId;
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		if (!rs.next()) return null;
		UserGroup ug = new UserGroup();
		ug.setGroupId(rs.getInt("groupId"));
		ug.setGroupName(rs.getString("groupName"));
		ug.setGroupDetail(rs.getString("groupDetail"));
		ug.setLoginSys(rs.getString("loginSys"));
		String roles = rs.getString("groupRoles");
		ug.setGroupRole(StringUtil.parseInt(roles.split(","), 0));
		ug.setBook_limit(rs.getInt("book_limit"));
		ug.setDay_limit(rs.getInt("day_limit"));
		ug.setDeposit(rs.getFloat("deposit"));
		ug.setFine(rs.getFloat("fine"));
		return ug;
	}
	
	/**
	 * 更新用户组信息
	 */
	public void updateUserGroup(UserGroup ug)
	{
		String sql = "UPDATE sys_group SET groupName=?,groupDetail=?,groupRoles=?,loginSys=? ,book_limit=?,day_limit=?,deposit=?,fine=? "
			+ "WHERE groupId=?";
		final UserGroup fug = ug;
		this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setString(1, fug.getGroupName());
				ps.setString(2, fug.getGroupDetail());
				ps.setString(3, StringUtil.join(fug.getGroupRole(), ","));
				ps.setString(4, fug.getLoginSys());
				ps.setInt(5, fug.getBook_limit());
				ps.setInt(6, fug.getDay_limit());
				ps.setDouble(7, fug.getDeposit());
				ps.setDouble(8, fug.getFine());
				ps.setInt(9, fug.getGroupId());
			}
		});
	}
	
	/**
	 * 删除指定的用户组.只允许删除没有用户的组！
	 * 系统默认的用户组：1-系统管理员 2-镇区管理员  是不允许被删除的！
	 * @param groupId
	 */
	public void remove(int[] groupId)
	{
		String sql = "DELETE FROM sys_group WHERE groupId=? AND groupUserNum=0 AND groupId>2";
		final int[] gId = groupId;
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			public int getBatchSize()
			{
				return gId.length;
			}

			public void setValues(PreparedStatement ps, int index) throws SQLException
			{
				ps.setInt(1, gId[index]);
			}
		});
	}
	
	/**
	 * 查看指定用户组的角色列表信息.
	 * @param groupId
	 * @return
	 */
	public List getGroupRole(int groupId)
	{
		String sql = "SELECT b.roleId,b.roleName FROM sys_group_role a LEFT JOIN sys_role b "
			+ "ON a.roleId=b.roleId WHERE a.groupId=" + groupId;
		return this.getJdbcTemplate().query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int index) throws SQLException
			{
				Role r = new Role();
				r.setRoleId(rs.getInt("roleId"));
				r.setName(rs.getString("roleName"));
				return r;
			}
			
		});
	}


	/* (non-Javadoc)
	 * @see cn.qtone.common.components.syspurview.core.group.dao.IUserGroupDAO#addGroupRole(int, int[])
	 */
	public void addGroupRole(int groupId, int[] roleIds) {
		String sql = "INSERT INTO sys_group_role(roleId,groupId) VALUES(?,?)";
		for (int roleId : roleIds) {
			getSimpleJdbcTemplate().update(sql, roleId, groupId);
		}
	}

	/* (non-Javadoc)
	 * @see cn.qtone.common.components.syspurview.core.group.dao.IUserGroupDAO#removeGroupRole(int)
	 */
	public void removeGroupRole(int groupId) {
		String sql = "delete from sys_group_role where groupId=?";
		getSimpleJdbcTemplate().update(sql, groupId);
	}
}
