package cn.qtone.common.base.spring;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 基于spring的IOC控制的DBControl,可以提供使用非spring特性的
 * 应用程序的方便数据库操作功能.
 * 
 * @author 马必强
 *
 */
public class DBControl
{
	private static DBControl dbInstance;
	
	private DataSource dataSource; // 数据源
	
	private JdbcTemplate jdbcTpl;

	/**
	 * 只允许设置一次JdbcTemplate.
	 * @param jdbcTpl
	 */
	public void setJdbcTpl(JdbcTemplate jdbcTpl)
	{
		if (this.jdbcTpl != null) return;
		if (jdbcTpl == null) return;
		this.jdbcTpl = jdbcTpl;
	}

	/**
	 * DBControl的唯一工厂方法，获取唯一实例（线程安全的）.
	 * @param ds
	 * @return
	 */
	public static DBControl getInstance(DataSource ds)
	{
		if (dbInstance != null) return dbInstance;
		dbInstance = new DBControl(ds);
		return dbInstance;
	}
	
	/**
	 * DBControl的保护型构造方法.
	 * @param ds
	 */
	protected DBControl(DataSource ds)
	{
		this.dataSource = ds;
		this.jdbcTpl = new JdbcTemplate(ds);
	}
	
	/**
	 * 执行单个SQL语句，典型的如DDL语句.
	 * @param sql
	 */
	public void execute(String sql)
	{
		this.jdbcTpl.execute(sql);
	}
	
	/**
	 * 执行查询，其查询结果集合为一整型.
	 * @param sql
	 * @return
	 */
	public int queryForInt(String sql)
	{
		return this.jdbcTpl.queryForInt(sql);
	}
	
	/**
	 * 获取当前数据库一个可用连接.
	 * @return
	 */
	public Connection getConnection()
	{
		try {
			return this.dataSource.getConnection();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
}
