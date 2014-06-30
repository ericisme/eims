package cn.qtone.common.components.syspurview.controlpanel.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import cn.qtone.common.components.syspurview.controlpanel.domain.UserCryptoguard;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.mvc.dao.BaseDAO;

/**
 * 控制面板的通用数据库DAO实现.
 * 
 * @author 马必强
 *
 */
public class BaseControlPanelDAO extends BaseDAO implements IControlPanelDAO
{
	public void updateUserInfo(User user)
	{
		String sql = "UPDATE sys_user SET realName=?,mobile=?,email=? "
			+ "WHERE userId=?";
		final User u = user;
		this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setString(1, u.getRealName());
				ps.setString(3, u.getMobile());
				ps.setString(4, u.getEmail());
				ps.setInt(5, u.getUserId());
			}
		});
	}

	public void updatePassword(int userId, String newPwd,String plainCode)
	{
		String sql ="UPDATE sys_user SET loginPassword=?,plainCode=? WHERE userId=?";
		this.getJdbcTemplate().update(sql, new Object[] {newPwd, plainCode, userId});
	}
	
	public UserCryptoguard getCryptoguard(int userId)
	{
		String sql = "SELECT userId,userMobile,userEmail,userQuestion,userAnswer,method "
			+ "FROM sys_user_cryptoguard WHERE userId=" + userId;
		UserCryptoguard guard = new UserCryptoguard();
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		if (!rs.next()) return guard;
		guard.setUserId(userId);
		guard.setUserQuestion(rs.getString("userQuestion"));
		guard.setUserAnswer(rs.getString("userAnswer"));
		guard.setUserMobile(rs.getString("userMobile"));
		guard.setUserEmail(rs.getString("userEmail"));
		guard.setUseEmailMethod(rs.getInt("method") == 1);
		return guard;
	}

	public void updateUserCryptoguard(UserCryptoguard guard)
	{
		// 先删除之前的记录
		String sql = "DELETE FROM sys_user_cryptoguard WHERE userId=?";
		this.getJdbcTemplate().update(sql, new Object[] { guard.getUserId() });
		// 再执行插入操作
		sql = "INSERT INTO sys_user_cryptoguard(userId,userQuestion,userAnswer,userMobile,"
			+ "userEmail,method) VALUES(?,?,?,?,?,?)";
		final UserCryptoguard fGuard = guard;
		this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setInt(1, fGuard.getUserId());
				ps.setString(2, fGuard.getUserQuestion());
				ps.setString(3, fGuard.getUserAnswer());
				ps.setString(4, fGuard.getUserMobile());
				ps.setString(5, fGuard.getUserEmail());
				ps.setInt(6, fGuard.isUseEmailMethod() ? 1 : 2);
			}
		});
	}
}
