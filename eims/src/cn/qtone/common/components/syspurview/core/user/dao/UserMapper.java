package cn.qtone.common.components.syspurview.core.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.qtone.common.components.syspurview.core.user.domain.User;

/**
 * 用户数据库记录到对象的转换对象.
 * 
 * @author 马必强
 *
 */
public class UserMapper implements RowMapper
{
	public User mapRow(ResultSet rs, int arg1) throws SQLException
	{
		User user = new User();
		user.setUserId(rs.getInt("userId"));
		user.setGroupId(rs.getInt("groupId"));
		user.setGroupName(rs.getString("groupName"));
		user.setLoginName(rs.getString("loginName"));
		user.setLoginPassword(rs.getString("loginPassword"));
		user.setRealName(rs.getString("realName"));
		user.setMobile(rs.getString("mobile"));
		user.setEmail(rs.getString("email"));
		user.setAddTime(rs.getString("addTime"));
		user.setLastLoginTime(rs.getString("lastLoginTime"));
		user.setLastLoginIP(rs.getString("lastLoginIP"));
		user.setLoginTimes(rs.getInt("loginTimes"));
		user.setLock(rs.getInt("isLock") == 1);
		user.setUserType(rs.getString("user_type"));
		user.setTown_id(rs.getInt("town_id"));
		user.setAgency_id(rs.getInt("agency_id"));
		user.setGrade_id(rs.getInt("grade_id"));
		user.setSchool_id(rs.getInt("school_id"));
		user.setClass_id(rs.getInt("class_id"));
		user.setId_card(rs.getString("id_card"));
		user.setStatus(rs.getInt("status"));
		return user;
	}

}
