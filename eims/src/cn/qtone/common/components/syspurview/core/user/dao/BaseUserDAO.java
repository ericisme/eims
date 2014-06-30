package cn.qtone.common.components.syspurview.core.user.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import cn.qtone.common.components.syspurview.core.user.qvo.QvoUser;
import cn.qtone.common.mvc.dao.BaseDAO;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;

/**
 * 用户管理的通用数据库操作抽象DAO。
 * 
 * @author 马必强
 *
 */
public abstract class BaseUserDAO extends BaseDAO
{
	public int getUserCount(String loginName)
	{
		if (log.isInfoEnabled()) log.info("检查用户名[" + loginName + "]是否重复！");
		String sql = "SELECT COUNT(1) FROM sys_user WHERE loingName=?";
		return this.getJdbcTemplate().queryForInt(sql, new Object[] {loginName});
	}
	
	public int getUserIdByName(String loginName)
	{
		if (log.isInfoEnabled()) log.info("根据用户名[" + loginName + "]获取用户唯一ID！");
		String sql = "SELECT userId FROM sys_user WHERE loginName=?";
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql, new Object[]{loginName});
		if (rs.next()) return rs.getInt("userId");
		return 0;
	}
	
	public void removeUser(int[] userId)
	{
		if (log.isInfoEnabled()) {
			log.info("要被删除的用户为：" + StringUtil.join(userId, ","));
		}
		String sql = "DELETE FROM sys_user WHERE userId=?";
		final int[] uId = userId;
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			public int getBatchSize()
			{
				return uId.length;
			}
			public void setValues(PreparedStatement ps, int index) throws SQLException
			{
				ps.setInt(1, uId[index]);
			}
		});
	}
	
	public void removeBorrow(int[] userId)
	{
		if (log.isInfoEnabled()) {
			log.info("要被删除的用户为：" + StringUtil.join(userId, ","));
		}
		String sql = "DELETE FROM library_borrow WHERE user_id=?";
		final int[] uId = userId;
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			public int getBatchSize()
			{
				return uId.length;
			}
			public void setValues(PreparedStatement ps, int index) throws SQLException
			{
				ps.setInt(1, uId[index]);
			}
		});
	}
	
	public void lock(int[] userId)
	{
		this.doLock(1, userId);
	}
	
	public void unlock(int[] userId)
	{
		this.doLock(0, userId);
	}
	
	protected void doLock(int lockValue, int[] userId)
	{
		String sql = "UPDATE sys_user SET isLock=? WHERE userId=?";
		final int lock = lockValue;
		final int[] uId = userId;
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			public int getBatchSize()
			{
				return uId.length;
			}
			public void setValues(PreparedStatement ps, int index) throws SQLException
			{
				ps.setInt(1, lock);
				ps.setInt(2, uId[index]);
			}
		});
	}
	
	/**
	 * 查询用户的总数.
	 *
	 */
	protected int queryCount()
	{
		String sql = "SELECT count(1) FROM sys_user WHERE isSuper=0 ";
		if(log.isInfoEnabled()) log.info("用户查询："+sql);
		return this.getJdbcTemplate().queryForInt(sql);
	}
	
	/**
	 * 根据用户名称来查询用户总数.
	 * @param name
	 * @return
	 */
	protected int queryCount(String name)
	{
		String qName = StringUtil.toDBFilter(name);
		String sql = "SELECT count(1) FROM sys_user WHERE realName LIKE'%"
			+ qName + "%' AND isSuper=0 ";
		return this.getJdbcTemplate().queryForInt(sql);
	}
	
	/**
	 * 根据用户组来查询用户总数
	 * @param groupId
	 * @return
	 */
	protected int queryCount(int groupId)
	{
		String sql = "SELECT count(1) FROM sys_user WHERE groupId=" + groupId
			+ "  AND isSuper=0 ";
		return this.getJdbcTemplate().queryForInt(sql);
	}
	
	/**
	 * 根据用户组ID和用户名称来查询用户总数
	 * @param name
	 * @param groupId
	 * @return
	 */
	protected int queryCount(String name, int groupId)
	{
		String qName = StringUtil.toDBFilter(name);
		String sql = "SELECT count(1) FROM sys_user WHERE realName LIKE '%"
			+ qName + "%' AND groupId=" + groupId + " AND isSuper=0 ";
		return this.getJdbcTemplate().queryForInt(sql);
	}
	
	protected int queryCount(QvoUser qvo)
	{
		 StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(1) FROM sys_user a LEFT JOIN sys_group b "
				+ "ON a.groupId=b.groupId WHERE isSuper=0  ");
		   if(ServletUtil.notEqualF1(qvo.getQueryUserType())){
	        	sql.append(" and a.user_type like '"+qvo.getQueryUserType()+"'");
	        }
	        if(ServletUtil.notEqualF1(qvo.getQryGroupId())){
	        	sql.append(" AND a.groupId="+StringUtil.parseInt(qvo.getQryGroupId(), -1));
	        }
	        if(StringUtils.isNotBlank(qvo.getQryName())){
	        	sql.append(" AND a.realName LIKE '%"+ qvo.getQryName() + "%'");
	        }
		return this.getJdbcTemplate().queryForInt(sql.toString());
	}
	
	public void updatePwd(int[] userId, String pwd,final String plainCode){
		String sql = "UPDATE sys_user set loginPassword = ?,plainCode=? where userId = ?";
		final String p = pwd;
		final int[] uId = userId;
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			public int getBatchSize()
			{
				return uId.length;
			}
			public void setValues(PreparedStatement ps, int index) throws SQLException
			{
				ps.setString(1, p);
				ps.setInt(3, uId[index]);
				ps.setString(2, plainCode);
			}
		});
	}
	

	/* (non-Javadoc)
	 * @see cn.qtone.common.components.syspurview.core.user.dao.IUserDAO#getUserTownIds(int)
	 */
	/*public Integer[] getUserTownIds(int userId) {
		String sql = "select townId from qt_usertown where userId=?";
		List<Integer> towns = getSimpleJdbcTemplate().query(sql, new ParameterizedRowMapper<Integer>(){
			public Integer mapRow(ResultSet arg0, int arg1) throws SQLException {
				return arg0.getInt("townId");
			}
		}, userId);
		Integer[] townIds = new Integer[towns.size()];
		for(int i = 0;i<towns.size();i++){
			townIds[i] = towns.get(i);
		}
		return townIds;
	}*/
}
