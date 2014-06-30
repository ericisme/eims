package cn.qtone.common.mvc.dao;

import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * 抽象DAO类，封装了DataSource的获取和设置.同时为子类提供了
 * JdbcTemplate等对象，全局的模板对象方便了各模块DAO的数据库
 * 操作。
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public abstract class BaseDAO
{
	protected static final Logger log = Logger.getLogger(BaseDAO.class);
	
	private DataSource masterDataSource, slaveDataSource; // 主数据源和从数据源
	
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	private JdbcTemplate jdbcTemplate;
	
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	
	private SimpleJdbcTemplate simpleTplForQuery;
	
	private JdbcTemplate jdbcTplForQuery;
	
	private NamedParameterJdbcTemplate namedTplForQuery;
	
	/**
	 * 使用spring提供的SimpleJdbcTemplate进行数据库查询操作.
	 * 如果不想使用该类，可以在自己的dao实现层使用DataSource构造
	 * 另外一个查询对象。
	 * @return
	 */
	protected SimpleJdbcTemplate getSimpleJdbcTemplate()
	{
		return this.simpleJdbcTemplate;
	}
	
	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate)
	{
		if (this.simpleJdbcTemplate != null) return;
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}
	
	/**
	 * 获取专门用来进行查询的SimpleJdbcTemplate
	 * @return
	 */
	protected SimpleJdbcTemplate getSimpleJdbcTemplateForQuery()
	{
		return this.simpleTplForQuery;
	}
	
	public void setSimpleTplForQuery(SimpleJdbcTemplate simpleTplForQuery)
	{
		if (this.simpleTplForQuery != null) return;
		this.simpleTplForQuery = simpleTplForQuery;
	}
	
	/**
	 * 使用JdbcTemplate来执行数据库的操作.
	 * @return
	 */
	protected JdbcTemplate getJdbcTemplate()
	{
		return this.jdbcTemplate;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		if (this.jdbcTemplate != null) return;
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * 获取专门用来进行查询的JdbcTemplate.
	 * @return
	 */
	protected JdbcTemplate getJdbcTplForQuery()
	{
		return this.jdbcTplForQuery;
	}
	
	public void setJdbcTplForQuery(JdbcTemplate jdbcTemplateForQuery)
	{
		if (this.jdbcTplForQuery != null) return;
		this.jdbcTplForQuery = jdbcTemplateForQuery;
	}
	
	
	/**
	 * 使用spring2.0的NamedParameterJdbcTemplate进行数据库操作.
	 * @return
	 */
	protected NamedParameterJdbcTemplate getNamedJdbcTemplate()
	{
		return this.namedJdbcTemplate;
	}
	
	public void setNamedJdbcTemplate(NamedParameterJdbcTemplate template)
	{
		if (this.namedJdbcTemplate != null) return;
		this.namedJdbcTemplate = template;
	}
	
	/**
	 * 获取专门用来查询的NamedParameterJdbcTemplate.
	 * @return
	 */
	protected NamedParameterJdbcTemplate getNamedTemplateForQuery()
	{
		return this.namedTplForQuery;
	}
	
	public void setNamedTplForQuery(NamedParameterJdbcTemplate namedTplForQuery)
	{
		if (this.namedTplForQuery != null) return;
		this.namedTplForQuery = namedTplForQuery;
	}
	
	/**
	 * 设置更新数据源(主数据库数据源).
	 * @param dataSource
	 */
	public void setMasterDataSource(DataSource dataSource)
	{
		if (this.masterDataSource != null) return;
		this.masterDataSource = dataSource;
	}
	
	/**
	 * 设置查询数据源(从数据库数据源).
	 * @param dataSource
	 */
	public void setSlaveDataSource(DataSource dataSource)
	{
		if (this.slaveDataSource != null) return;
		this.slaveDataSource = dataSource;
	}
}
