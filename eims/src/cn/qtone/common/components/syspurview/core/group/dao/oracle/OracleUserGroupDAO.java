package cn.qtone.common.components.syspurview.core.group.dao.oracle;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;

import cn.qtone.common.components.syspurview.core.group.dao.BaseUserGroupDAO;
import cn.qtone.common.components.syspurview.core.group.dao.UserGroupMapper;
import cn.qtone.common.components.syspurview.core.group.domain.UserGroup;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 用户组管理DAO的oracle数据库实现.
 * 
 * @author 马必强
 *
 */
public class OracleUserGroupDAO extends BaseUserGroupDAO
{
	/**
	 * 查询所有的用户组列表信息.
	 */
	public List listAll()
	{
		String sql = "SELECT groupId,groupName,to_char(groupAddTime,'yyyy-mm-dd hh24:Mi:ss') "
			+ "as groupAddTime,groupDetail,groupUserNum FROM sys_group";
		return this.getJdbcTemplate().query(sql, new UserGroupMapper());
	}

	/**
	 * 添加用户组信息.
	 */
	public void addUserGroup(UserGroup ug)
	{
		String sql = "INSERT INTO sys_group(groupName,groupDetail,groupRoles,groupAddTime,loginSys) "
			+ "VALUES(?,?,?,sysdate,?)";
		final UserGroup fug = ug;
		this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setString(1, fug.getGroupName());
				ps.setString(2, fug.getGroupDetail());
				ps.setString(3, StringUtil.join(fug.getGroupRole(),","));
				ps.setString(4, fug.getLoginSys());
			}
		});
	}

	public int getGroupId(UserGroup ug) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
}
