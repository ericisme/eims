package cn.qtone.common.components.syspurview.core.module.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.qtone.common.components.syspurview.core.module.domain.Module;

/**
 * 模块数据库记录到对象的转换mapper
 * @author 马必强
 *
 */
public class ModuleMapper implements RowMapper
{
	public Module mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		Module m = new Module();
		m.setMId(rs.getInt("moduleId"));
		m.setSysId(rs.getInt("sysId"));
		m.setMName(rs.getString("moduleName"));
		m.setMenu(rs.getInt("moduleType") == 1);
		m.setMStatus(rs.getInt("moduleStatus"));
		m.setMUrl(rs.getString("moduleURL"));
		m.setMToken(rs.getString("token"));
		m.setMSequence(rs.getInt("moduleSeq"));
		m.setMInfo(rs.getString("moduleInfo"));
		m.setSysModule(rs.getInt("sysModule") == 1);
		return m;
	}

}
