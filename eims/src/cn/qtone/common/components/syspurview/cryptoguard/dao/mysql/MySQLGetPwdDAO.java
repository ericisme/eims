package cn.qtone.common.components.syspurview.cryptoguard.dao.mysql;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import cn.qtone.common.components.syspurview.cryptoguard.dao.BaseGetPwdDAO;
import cn.qtone.common.components.syspurview.cryptoguard.domain.UserEmailSession;

/**
 * 用户取回密码的mysql数据库DAO.
 * 
 * @author 马必强
 *
 */
public class MySQLGetPwdDAO extends BaseGetPwdDAO
{
	public void saveEmailSession(String userName, String sid)
	{
		String sql = "INSERT INTO sys_email_cryptoguard(userName,sessionId,"
			+ "sendTime) VALUES(?,?,now())";
		this.getJdbcTemplate().update(sql, new Object[] { userName, sid });
	}

	public UserEmailSession getUserEmailSession(String sid)
	{
		String sql = "SELECT userName,sessionId,sendTime FROM sys_email_cryptoguard "
			+ "WHERE sessionId=?";
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql, new Object[]{sid});
		if (!rs.next()) return null;
		UserEmailSession session = new UserEmailSession();
		session.setUserName(rs.getString("userName"));
		session.setSessionId(rs.getString("sessionId"));
		session.setSendTime(rs.getString("sendTime"));
		return session;
	}
}
