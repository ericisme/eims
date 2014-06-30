package cn.qtone.common.components.syspurview.core.group.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.qtone.common.components.syspurview.core.group.domain.UserGroup;

/**
 * 用户组信息的包装器.
 * 
 * @author 马必强
 *
 */
public class UserGroupMapper implements RowMapper
{
	public UserGroup mapRow(ResultSet rs, int index) throws SQLException
	{
		UserGroup ug = new UserGroup();
		ug.setGroupId(rs.getInt("groupId"));
		ug.setGroupName(rs.getString("groupName"));
		ug.setGroupAddTime(rs.getString("groupAddTime"));
		ug.setGroupDetail(rs.getString("groupDetail"));
		ug.setGroupUserNum(rs.getInt("groupUserNum"));
		ug.setBook_limit(rs.getInt("book_limit"));
		ug.setDay_limit(rs.getInt("day_limit"));
		ug.setDeposit(rs.getDouble("deposit"));
		ug.setFine(rs.getDouble("fine"));
		
		
		return ug;
	}

}
