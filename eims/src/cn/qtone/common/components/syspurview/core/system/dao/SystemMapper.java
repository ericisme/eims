package cn.qtone.common.components.syspurview.core.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.qtone.common.components.syspurview.core.system.domain.SystemBean;

/**
 * 子系统的mapper.
 * 
 * @author 马必强
 *
 */
public class SystemMapper implements RowMapper
{
	public SystemBean mapRow(ResultSet rs, int rowNum) throws SQLException
	{
		SystemBean sys = new SystemBean();
		sys.setSystemId(rs.getInt("sysId"));
		sys.setSysFlag(rs.getString("sysFlag"));
		sys.setSysName(rs.getString("sysName"));
		sys.setSysSequence(rs.getInt("sysSeq"));
		sys.setSysTarget(rs.getString("sysTarget"));
		sys.setSysUrl(rs.getString("sysURL"));
		return sys;
	}

}
