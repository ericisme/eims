package cn.qtone.crda.classstudent.dao.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import cn.qtone.crda.classstudent.domain.ClassStudent;

public class ClassStudentMapper implements ParameterizedRowMapper<ClassStudent> {
 
	public ClassStudent mapRow(ResultSet rs, int arg1) throws SQLException {
		ClassStudent bean = new ClassStudent();
		bean.setTeacher_no(rs.getString("teacher_no"));
		bean.setUnique_no(rs.getString("unique_no"));
		bean.setTruename(rs.getString("truename"));
		bean.setClass_name(rs.getString("class_name"));
		bean.setSchool_name(rs.getString("school_name"));
		bean.setIs_valid(rs.getInt("is_valid"));
		return bean;
	}
}
