package cn.qtone.library.techer.dao.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import cn.qtone.common.components.syspurview.core.user.domain.IUser;

public class TecherMapper implements ParameterizedRowMapper<IUser> {
 
	public IUser mapRow(ResultSet rs, int arg1) throws SQLException {
		IUser bean = new IUser();
		bean.setUserId(rs.getInt("userId"));
		bean.setRealName(rs.getString("realName"));
		//bean.setUnique_no(rs.getString("unique_no"));
		bean.setGender(rs.getInt("gender"));
		bean.setSchoolName(rs.getString("school_name"));
		bean.setClassName(rs.getString("class_name"));
		bean.setAgencyName(rs.getString("agency_name"));
		bean.setId_card(rs.getString("id_card"));
		bean.setBirthday(rs.getString("birthday"));
		bean.setLoginName(rs.getString("loginName"));
		//bean.setSchool_id(rs.getInt("school_id"));
		bean.setAgency_id(rs.getInt("agency_id"));
		bean.setGrade_id(rs.getInt("grade_id"));
		//bean.setClass_id(rs.getInt("class_id"));
		bean.setGroupId(rs.getInt("groupId"));
		bean.setUser_type(rs.getString("user_type"));
		bean.setPhone(rs.getString("phone"));
		bean.setMobile(rs.getString("mobile"));
		bean.setEmail(rs.getString("email"));
		return bean;
	}
}
