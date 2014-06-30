package cn.qtone.common.components.syspurview.cryptoguard.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import cn.qtone.common.components.syspurview.controlpanel.domain.UserCryptoguard;
import cn.qtone.common.mvc.dao.BaseDAO;

/**
 * 用户取回密码的DAO.
 * 
 * @author 马必强
 *
 */
public abstract class BaseGetPwdDAO extends BaseDAO implements IGetPwdDAO
{
	public UserCryptoguard getUserCryptoguard(String userName)
	{
		String sql = "SELECT b.userId,b.userMobile,b.userEmail,b.userQuestion,b.userAnswer,"
			+ "b.method FROM sys_user a LEFT JOIN sys_user_cryptoguard b ON a.userId=b.userId "
			+ "WHERE a.loginName=?";
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql, new Object[]{ userName });
		if (!rs.next()) return null;
		int userId = rs.getInt("userId");
		if (userId == 0) return null;
		UserCryptoguard user = new UserCryptoguard();
		user.setUserId(userId);
		user.setUserMobile(rs.getString("userMobile"));
		user.setUserEmail(rs.getString("userEmail"));
		user.setUserQuestion(rs.getString("userQuestion"));
		user.setUserAnswer(rs.getString("userAnswer"));
		user.setUseEmailMethod(rs.getInt("method") == 1);
		return user;
	}

	public void resetUserPassword(String loginName, String password)
	{
		String sql = "UPDATE sys_user SET loginPassword=? WHERE loginName=?";
		this.getJdbcTemplate().update(sql, new Object[] { password, loginName });
	}
	
}
