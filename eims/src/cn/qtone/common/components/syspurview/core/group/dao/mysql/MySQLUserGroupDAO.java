package cn.qtone.common.components.syspurview.core.group.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;

import cn.qtone.common.components.syspurview.core.group.dao.BaseUserGroupDAO;
import cn.qtone.common.components.syspurview.core.group.dao.UserGroupMapper;
import cn.qtone.common.components.syspurview.core.group.domain.UserGroup;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 用户组管理DAO的mysql数据库实现.
 * 
 * @author 马必强
 *
 */
public class MySQLUserGroupDAO extends BaseUserGroupDAO
{
	/**
	 * 查询所有的用户组列表信息.
	 */
	public List listAll()
	{
		String sql = "SELECT groupId,groupName,groupAddTime,groupDetail,groupUserNum ,book_limit,day_limit,deposit,fine FROM "
			+ "sys_group";
		return this.getJdbcTemplate().query(sql, new UserGroupMapper());
	}

	/**
	 * 添加用户组信息.
	 */
	public void addUserGroup(UserGroup ug)
	{
		String sql = "INSERT INTO sys_group(groupName,groupDetail,groupRoles,groupAddTime,loginSys,book_limit,day_limit,deposit,fine) "
			+ "VALUES(?,?,?,now(),?)";
		final UserGroup fug = ug;
		this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setString(1, fug.getGroupName());
				ps.setString(2, fug.getGroupDetail());
				ps.setString(3, StringUtil.join(fug.getGroupRole(),","));
				ps.setString(4, fug.getLoginSys());
				ps.setInt(5, fug.getBook_limit());
				ps.setInt(6, fug.getDay_limit());
				ps.setDouble(7, fug.getDeposit());
				ps.setDouble(8, fug.getFine());
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see cn.qtone.common.components.syspurview.core.group.dao.IUserGroupDAO#getGroupId(cn.qtone.common.components.syspurview.core.group.domain.UserGroup)
	 */
	public int getGroupId(UserGroup ug) {
		String sql = "select groupId from sys_group where groupName=? and loginSys=? order by groupId desc limit 0,1";
		return getSimpleJdbcTemplate().queryForInt(sql, ug.getGroupName(), ug.getLoginSys());
	}
}
