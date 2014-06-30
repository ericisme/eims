package cn.qtone.common.components.syspurview.admin.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.qtone.common.components.syspurview.core.module.domain.Module;

/**
 * 后台管理的左侧菜单的对象转换.
 * 
 * @author 马必强
 *
 */
public class LeftMenuMapper implements RowMapper
{
	public Module mapRow(ResultSet rs, int arg1) throws SQLException
	{
		Module m = new Module();
		m.setMId(rs.getInt("moduleId"));
		m.setMName(rs.getString("moduleName"));
		m.setMenu(rs.getInt("moduleType") == 1);
		m.setMStatus(rs.getInt("moduleStatus"));
		m.setMUrl(rs.getString("moduleURL"));
		m.setMToken(rs.getString("token"));
		m.setMSequence(rs.getInt("moduleSeq"));
		return m;
	}

}
