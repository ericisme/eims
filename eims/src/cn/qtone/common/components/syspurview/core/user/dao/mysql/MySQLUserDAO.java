package cn.qtone.common.components.syspurview.core.user.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.PreparedStatementSetter;
import cn.qtone.common.components.syspurview.core.user.dao.BaseUserDAO;
import cn.qtone.common.components.syspurview.core.user.dao.IUserDAO;
import cn.qtone.common.components.syspurview.core.user.dao.UserMapper;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.components.syspurview.core.user.qvo.QvoUser;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;

/**
 * 用户管理的mysql数据库实现DAO。
 * 
 * @author 马必强
 *
 */
public class MySQLUserDAO extends BaseUserDAO implements IUserDAO
{
	public void query(Page page)
	{
		if (log.isInfoEnabled()) {
			log.info("[mysql]查询用户列表,start=" + (page.getStartIndex() - 1)
					+ ",pageSize=" + page.getPageSize());
		}
		String sql = "SELECT userId,a.groupId,groupName,loginName,loginPassword,realName,man, a.town_id,a.agency_id,a.school_id,a.grade_id,a.class_id,a.id_card,a.status,"
			+ " mobile,email,birthday,addTime,lastLoginTime,lastLoginIP,isLock,loginTimes,a.USER_TYPE,c.DEPT_ID,c.DEPT_NAME "
			+ " FROM sys_user a LEFT JOIN sys_group b ON a.groupId=b.groupId "
			+ " LEFT JOIN t_dept c on a.DEPT_ID=c.DEPT_ID"
			+ " WHERE isSuper=0 LIMIT ?,?";
		if(log.isInfoEnabled()) log.info("用户查询："+sql);
		final Page p = page;
		List result = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setInt(1, p.getStartIndex() - 1);
				ps.setInt(2, p.getPageSize());
			}
		}, new UserMapper());
		page.setResult(result);
		page.setTotals(this.queryCount());
	}

	public void query(Page page, String name)
	{
		if (log.isInfoEnabled()) {
			log.info("[mysql]查询用户列表,start=" + (page.getStartIndex() - 1)
					+ ",pageSize=" + page.getPageSize() + ",name=" + name);
		}
		String qName = StringUtil.toDBFilter(name);
		String sql = "SELECT userId,a.groupId,groupName,loginName,loginPassword,realName,man, a.town_id,a.agency_id,a.school_id,a.grade_id,a.class_id,a.id_card,a.status,"
			+ "mobile,email,birthday,addTime,lastLoginTime,lastLoginIP,isLock,loginTimes,a.USER_TYPE,c.DEPT_ID,c.DEPT_NAME "
			+ "FROM sys_user a LEFT JOIN sys_group b ON a.groupId=b.groupId "
			+ " LEFT JOIN t_dept c on a.DEPT_ID=c.DEPT_ID "
			+ "WHERE isSuper=0 AND realName LIKE '%" + qName + "%' LIMIT ?,?";
		if(log.isInfoEnabled()) log.info("用户查询："+sql);
		final Page p = page;
		List result = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setInt(1, p.getStartIndex() - 1);
				ps.setInt(2, p.getPageSize());
			}
		}, new UserMapper());
		page.setResult(result);
		page.setTotals(this.queryCount(name));
	}

	public void query(Page page, int groupId)
	{
		if (log.isInfoEnabled()) {
			log.info("[mysql]查询用户列表,start=" + (page.getStartIndex() - 1)
					+ ",pageSize=" + page.getPageSize() + ",groupId=" + groupId);
		}
		String sql = "SELECT userId,a.groupId,groupName,loginName,loginPassword,realName,man, a.town_id,a.agency_id,a.school_id,a.grade_id,a.class_id,a.id_card,a.status,"
			+ "mobile,email,birthday,addTime,lastLoginTime,lastLoginIP,isLock,loginTimes,a.USER_TYPE,c.DEPT_ID,c.DEPT_NAME,isManageAllTown "
			+ "FROM sys_user a LEFT JOIN sys_group b ON a.groupId=b.groupId "
			+ " LEFT JOIN t_dept c on a.DEPT_ID=c.DEPT_ID "
			+ "WHERE isSuper=0 AND a.groupId=? LIMIT ?,?";
		final Page p = page;
		final int gId = groupId;
		List result = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setInt(1, gId);
				ps.setInt(2, p.getStartIndex() - 1);
				ps.setInt(3, p.getPageSize());
			}
		}, new UserMapper());
		page.setResult(result);
		page.setTotals(this.queryCount(gId));
	}

	public void query(Page page, String name, int groupId)
	{
		if (log.isInfoEnabled()) {
			log.info("[mysql]查询用户列表,start=" + (page.getStartIndex() - 1)
					+ ",pageSize=" + page.getPageSize() + ",groupId=" + groupId
					+ ",name=" + name);
		}
		String qName = StringUtil.toDBFilter(name);
		String sql = "SELECT userId,a.groupId,groupName,loginName,loginPassword,realName,man,a.town_id,a.agency_id,a.school_id,a.grade_id,a.class_id,a.id_card,a.status,"
			+ "mobile,email,birthday,addTime,lastLoginTime,lastLoginIP,isLock,loginTimes,a.USER_TYPE,c.DEPT_ID,c.DEPT_NAME,isManageAllTown "
			+ "FROM sys_user a LEFT JOIN sys_group b ON a.groupId=b.groupId "
			+ " LEFT JOIN t_dept c on a.DEPT_ID=c.DEPT_ID "
			+ "WHERE isSuper=0 AND a.groupId=? AND realName LIKE '%" + qName + "%' LIMIT ?,?";
		final Page p = page;
		final int gId = groupId;
		List result = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setInt(1, gId);
				ps.setInt(2, p.getStartIndex() - 1);
				ps.setInt(3, p.getPageSize());
			}
		}, new UserMapper());
		page.setResult(result);
		page.setTotals(this.queryCount(name, groupId));
	}

	public void addUser(User user)
	{
		try {
			
		
		if (log.isInfoEnabled()) {
			log.info("[mysql]添加用户信息：" + StringUtil.reflectObj(user));
		}
		String sql = "INSERT INTO sys_user(loginName,loginPassword,realName,"
			+ "mobile,email,addTime,isSuper,groupId,USER_TYPE,DEPT_ID,plainCode,town_id,agency_id,school_id,grade_id,class_id,id_card,status) " 
			+ " VALUES(?,?,?,?,?,now(),0,?,?,?,?,?,?,?,?,?,?,?)";
		final User u = user;
		
		this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setString(1, u.getLoginName());
				ps.setString(2, u.getLoginPassword());
				ps.setString(3, u.getRealName());
				ps.setString(4, u.getMobile());
				ps.setString(5, u.getEmail());
				ps.setInt(6, u.getGroupId());
				ps.setString(7,u.getUserType());
				ps.setInt(8,u.getParentId());
				ps.setString(9, u.getPlainCode());
				ps.setInt(10, u.getTown_id());
				ps.setInt(11, u.getAgency_id());
				ps.setInt(12, u.getSchool_id());
				ps.setInt(13, u.getGrade_id());
				ps.setInt(14, u.getClass_id());
				ps.setString(15, u.getId_card());
				ps.setInt(16, u.getStatus());
			}
		});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public User getUser(int userId)
	{
		if (log.isInfoEnabled()) log.info("获取用户[" + userId + "的详细信息！");
		String sql = "SELECT userId,a.groupId,groupName,loginName,loginPassword,realName, a.town_id,a.agency_id,a.school_id,a.grade_id,a.class_id,a.id_card,a.status,"
			+ " mobile,email,addTime,lastLoginTime,lastLoginIP,isLock,loginTimes,a.USER_TYPE,c.DEPT_ID,c.DEPT_NAME "
			+ " FROM sys_user a LEFT JOIN sys_group b ON a.groupId=b.groupId "
			+ " LEFT JOIN t_dept c ON a.DEPT_ID=c.DEPT_ID"
			+ " WHERE userId=" + userId;
		System.out.println("------>"+sql);
		return (User)this.getJdbcTemplate().queryForObject(sql, new UserMapper());
	}

	public void updateUser(User user)
	{
		if (log.isInfoEnabled()) {
			log.info("[mysql]更新用户信息:" + StringUtil.reflectObj(user));
		}
		String sql = "UPDATE sys_user SET realName=?,mobile=?,email=?,"
			+ "groupId=?,USER_TYPE=?,DEPT_ID=?,town_id=?,agency_id=?,school_id=?,grade_id=?,class_id=?,id_card=?,status=? WHERE userId=?";
		final User u = user;
		this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setString(1, u.getRealName());
				ps.setString(2, u.getMobile());
				ps.setString(3, u.getEmail());
				ps.setInt(4, u.getGroupId());
				ps.setString(5,u.getUserType());
				ps.setInt(6, u.getParentId());
				ps.setInt(7, u.getTown_id());
				ps.setInt(8, u.getAgency_id());
				ps.setInt(9, u.getSchool_id());
				ps.setInt(10, u.getGrade_id());
				ps.setInt(11, u.getClass_id());
				ps.setString(12, u.getId_card());
				ps.setInt(13, u.getStatus());
				ps.setInt(14, u.getUserId());
				
			}
		});
	}
 

	  public void query(Page page,QvoUser qvo)
		{
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT a.userId,a.groupId,a.loginTimes,groupName,loginName,loginPassword,realName, a.town_id,a.agency_id,a.school_id,a.grade_id,a.class_id,a.id_card,a.status,"
					+ "mobile,email,addTime as addTime,"
					+ "lastLoginTime as lastLoginTime,"
					+ "lastLoginIP,isLock,a.user_type FROM sys_user a LEFT JOIN sys_group b "
					+ " ON a.groupId=b.groupId ");
	        sql.append(" WHERE isSuper=0 ");
	        if(ServletUtil.notEqualF1(qvo.getQueryUserType())){
	        	sql.append(" and a.user_type like '"+qvo.getQueryUserType()+"'");
	        }
	        if(ServletUtil.notEqualF1(qvo.getQryGroupId())){
	        	sql.append(" AND a.groupId="+StringUtil.parseInt(qvo.getQryGroupId(), -1));
	        }
	        if(StringUtils.isNotBlank(qvo.getQryName())){
	        	sql.append(" AND a.realName LIKE '%"+ qvo.getQryName() + "%'");
	        }
	        sql.append(" order by groupId,addTime ");
			sql.append(" LIMIT "+(page.getStartIndex()==1?0:page.getStartIndex())+","+page.getPageSize());
			final int gId = StringUtil.parseInt(qvo.getQryGroupId(), -1);
			List result = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException
				{
				}
			}, new UserMapper());
			page.setResult(result);
			page.setTotals(this.queryCount(qvo));
		}

}
