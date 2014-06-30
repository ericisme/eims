package cn.qtone.qtoneframework.dao.spring.jdbc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import cn.qtone.qtoneframework.dao.support.DbMethod;


/**
 * 持久层抽象基类，注入了spring的JdbcTemplate、NamedParameterJdbcTemplate、SimpleJdbcTemplate，
 * 继承了这个类可方便的调用JdbcTemplate等spring的JDBC模板进行数据库操作。
 * 
 * @author 张但
 */
public abstract class SpringJdbcDaoSupport {

	private JdbcTemplate jdbcTemplate;

	private NamedParameterJdbcTemplate namedJdbcTemplate;

	private SimpleJdbcTemplate simpleJdbcTemplate;

	protected DbMethod qtfDbMethod;
	
	protected Log log;
	
	public SpringJdbcDaoSupport(){
		log = LogFactory.getLog(this.getClass());
	}
	
	/**
	 * @return JdbcTemplate。 JDBC的core包核心类，它完成了资源的创建和释放工作，简化了JDBC的使用。
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * @return NamedParameterJdbcTemplate。 封装了JdbcTemplate，在SQL语句中增加了命名参数的支持。
	 */
	public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
		return namedJdbcTemplate;
	}

	public void setNamedJdbcTemplate(NamedParameterJdbcTemplate namedJdbcTemplate) {
		this.namedJdbcTemplate = namedJdbcTemplate;
	}

	/**
	 * @return SimpleJdbcTemplate。 JdbcTemplate的包装类，利用了JAVA5的一些特性，如泛型。
	 */
	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}
	
	/**
	 * @return DbMethod。 封装各种数据库操作及函数的差异
	 */
	public DbMethod getQtfDbMethod() {
		return qtfDbMethod;
	}

	public void setQtfDbMethod(DbMethod qtfDbMethod) {
		this.qtfDbMethod = qtfDbMethod;
	}

	
}
