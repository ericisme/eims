package cn.qtone.common.components.syspurview.core.system.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import cn.qtone.common.components.syspurview.core.system.SystemCache;
import cn.qtone.common.components.syspurview.core.system.domain.SystemBean;
import cn.qtone.common.mvc.dao.BaseDAO;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 子系统的通用数据库操作抽象类。
 * 
 * @author 马必强
 * 
 */
public abstract class BaseSystemDAO extends BaseDAO implements ISystemDAO
{
	protected SystemCache cache; // 对子系统进行缓存
	
	/**
	 * 对象建立的时候进行子系统信息的缓存.
	 *
	 */
	public BaseSystemDAO()
	{
		cache = SystemCache.getInstance();
	}
	
	/**
	 * 查询所有的子系统信息.
	 * 
	 * @return
	 */
	public Page query()
	{
		if (log.isInfoEnabled()) log.info("dao获取所有子系统的信息~~");
		/*String sql = "SELECT sysId,sysName,sysFlag,sysURL,sysTarget,sysSeq FROM "
				+ "sys_system ORDER BY sysSeq ASC";
		List result = this.getJdbcTemplate().query(sql, new SystemMapper());*/
		Page pag = new Page();
		pag.setResult(cache.getList());
		return pag;
	}

	/**
	 * 添加子系统.
	 * 
	 * @param sys
	 */
	public void addSystem(SystemBean sys)
	{
		if (log.isInfoEnabled()) log.info("执行子系统的数据库添加操作~~~");
		String sql = "INSERT INTO sys_system(sysName,sysFlag,sysURL,sysTarget,"
				+ "sysSeq) VALUES(:sysName,:sysFlag,:sysUrl,:sysTarget,:sysSequence)";
		SqlParameterSource args = new BeanPropertySqlParameterSource(sys);
		this.getNamedJdbcTemplate().update(sql, args);
		// 添加成功后，重新加载列表
		cache.reload();
	}

	/**
	 * 获取特定子系统的信息.
	 */
	public SystemBean getSystem(int sysId)
	{
		if (log.isInfoEnabled()) log.info("获取id=" + sysId + "的子系统信息！");
		/*String sql = "SELECT sysId,sysName,sysFlag,sysURL,sysTarget,sysSeq FROM "
				+ "sys_system WHERE sysId=" + sysId;
		return (SystemBean) this.getJdbcTemplate().queryForObject(
				sql, new SystemMapper());*/
		return cache.getSystem(sysId); // 从缓存中获取
	}
	
	/**
	 * 更新子系统信息
	 */
	public void updateSystem(SystemBean sys)
	{
		String sql = "UPDATE sys_system SET sysName=:sysName,sysURL=:sysUrl,"
			+ "sysTarget=:sysTarget,sysSeq=:sysSequence WHERE sysId=:systemId";
		SqlParameterSource args = new BeanPropertySqlParameterSource(sys);
		this.getNamedJdbcTemplate().update(sql, args);
		// 更新缓存中的信息
		cache.reload();
	}
	
	/**
	 * 删除指定的系统
	 * @param sysId
	 */
	public void remove(int[] sysId)
	{
		String sql = "DELETE FROM sys_system WHERE sysId=?";
		final int[] removeId = sysId;
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException
			{
				ps.setInt(1, removeId[i]);
			}
			public int getBatchSize()
			{
				return removeId.length;
			}
		});
		// 更新缓存中的信息
		cache.reload();
	}
	
	/**
	 * 获取已经包含了模块的子系统的ID
	 */
	public int[] getHaveModuleSystemId()
	{
		String sql = "SELECT sysId FROM sys_module GROUP BY sysId";
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		StringBuffer id = new StringBuffer();
		while (rs.next()) {
			id.append(rs.getInt("sysId"));
			if (!rs.isLast()) id.append(",");
		}
		if (log.isInfoEnabled()) log.info("所有已包含模块的子系统为：" + id);
		return StringUtil.parseInt(id.toString().split(","), 0);
	}
	
	/**
	 * 根据标记名称来确定子系统，用来检测系统标记的唯一性.
	 * @param flag
	 * @return
	 */
	public int getSystemIdByFlag(String flag)
	{
		if (log.isInfoEnabled()) log.info("检测标记[" + flag + "]是否重复！");
		/*String sql = "SELECT sysId FROM sys_system WHERE sysFlag='" 
			+ StringUtil.toDBFilter(flag) + "'";
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		if (rs.next()) return rs.getInt("sysId");
		else return 0;*/
		return SystemCache.getInstance().getSystemId(flag);
	}
}
