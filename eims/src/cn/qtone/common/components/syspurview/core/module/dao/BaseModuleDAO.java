package cn.qtone.common.components.syspurview.core.module.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import cn.qtone.common.components.syspurview.core.module.domain.Module;
import cn.qtone.common.components.syspurview.core.module.domain.ModuleFunc;
import cn.qtone.common.components.syspurview.core.module.domain.ModuleSingleFunc;
import cn.qtone.common.components.syspurview.core.module.domain.TokenMaker;
import cn.qtone.common.mvc.dao.BaseDAO;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 模块DAO的不同数据库的公用DAO.
 * 
 * @author 马必强
 *
 */
public abstract class BaseModuleDAO extends BaseDAO implements IModuleDAO
{
	/**
	 * 获取指定系统中的所有一级和二级模块（菜单和功能模块）.
	 */
	public List query(int sysId)
	{
		if (log.isInfoEnabled()) log.info("根据子系统查询模块~~");
		return this.exeQuery("WHERE sysId=" + sysId + " AND LENGTH(TRIM(token))<="
				+ TokenMaker.TOKEN_LEN * 2);
	}
	
	/**
	 * 根据指定的token获取其直接下属的一二级模块（菜单和功能模块）
	 */
	public List query(String token)
	{
		if (log.isInfoEnabled()) log.info("根据模块的唯一token进行查询~~~");
		int len = token.length() + TokenMaker.TOKEN_LEN;
		return this.exeQuery("WHERE token LIKE '" + token + "%' AND LENGTH(TRIM(token)) "
				+ "IN(" + len + "," + (len + TokenMaker.TOKEN_LEN) + ")");
	}
	
	/**
	 * 检查系统中已经存在于指定的列表中的token，并且以,连接返回
	 */
	public String checkTokenInThese(String tokens)
	{
		if (log.isInfoEnabled()) log.info("检查系统中是否已存在token[" + tokens + "]");
		String sql = "SELECT token FROM sys_module WHERE token IN(" + tokens + ")";
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		StringBuilder buf = new StringBuilder();
		while (rs.next()) {
			buf.append(StringUtils.trim(rs.getString("token")));
			if (!rs.isLast()) buf.append(",");
		}
		return buf.toString();
	}
	
	/**
	 * 添加指定的模块.
	 * @param mod
	 */
	public void addModule(Module mod)
	{
		if (log.isInfoEnabled()) {
			log.info("要添加的模块信息：[" + StringUtil.reflectObj(mod) + "]");
		}
		final Module m = mod;
		String sql = "INSERT INTO sys_module(sysId,moduleName,moduleType,moduleStatus,"
			+ "moduleURL,token,moduleSeq,moduleInfo) VALUES(?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement stat) throws SQLException
			{
				stat.setInt(1, m.getSysId());
				stat.setString(2, m.getMName());
				stat.setInt(3, m.isMenu() ? 1 : 2);
				stat.setInt(4, m.getMStatus());
				stat.setString(5, m.getMUrl());
				stat.setString(6, m.getMToken());
				stat.setInt(7, m.getMSequence());
				stat.setString(8, m.getMInfo());
			}
		});
	}
	
	/**
	 * 获取一个子系统下的所有可用菜单.
	 * @param sysId
	 * @return
	 */
	public List queryMenu(int sysId)
	{
		if (log.isInfoEnabled()) log.info("获取子系统[" + sysId + "]下的所有菜单！");
		/*return this.exeQuery("WHERE sysId=" + sysId + " AND moduleType=1 AND "
				+ "moduleStatus>=0");*/
		return this.exeQuery("WHERE sysId=" + sysId + " AND moduleType=1");
	}
	
	/**
	 * 根据指定的token值获取模块信息.
	 * @param token
	 * @return
	 */
	public Module getModule(String token)
	{
		String sql = "SELECT moduleId,sysId,moduleName,moduleType,moduleStatus,"
			+ "moduleURL,token,moduleSeq,moduleInfo,sysModule FROM sys_module "
			+ "WHERE token='" + StringUtil.toDBFilter(token) + "'";
		if (log.isInfoEnabled()) log.info("获取token为[" + token + "]的模块信息："+sql);
		return (Module)this.getJdbcTemplate().queryForObject(sql, new ModuleMapper());
	}
	
	/**
	 * 根据ID获取模块的信息.
	 * @param moduleId
	 * @return
	 */
	public Module getModule(int moduleId)
	{
		String sql = "SELECT moduleId,sysId,moduleName,moduleType,moduleStatus,"
			+ "moduleURL,token,moduleSeq,moduleInfo,sysModule FROM sys_module "
			+ "WHERE moduleId=" + moduleId;
		if (log.isInfoEnabled()) log.info("获取Id为[" + moduleId + "]的模块信息:"+sql);
		return (Module)this.getJdbcTemplate().queryForObject(sql, new ModuleMapper());
	}
	
	/**
	 * 更新模块信息.
	 * @param mod
	 */
	public void updateModule(Module mod)
	{
		String sql = "UPDATE sys_module SET moduleName=?,moduleType=?,moduleStatus=?,"
			+ "moduleURL=?,token=?,moduleSeq=?,moduleInfo=?,sysId=? WHERE moduleId=?";
		final Module m = mod;
		this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement stat) throws SQLException
			{
				stat.setString(1, m.getMName());
				stat.setInt(2, m.isMenu() ? 1 : 2);
				stat.setInt(3, m.getMStatus());
				stat.setString(4, m.getMUrl());
				stat.setString(5, m.getMToken());
				stat.setInt(6, m.getMSequence());
				stat.setString(7, m.getMInfo());
				stat.setInt(8, m.getSysId());
				stat.setInt(9, m.getMId());
			}
		});
	}
	
	/**
	 * 更新指定token的模块的所有子模块的token前缀和sysId值.
	 * @param orginTokenPrefix 原始的token前缀
	 * @param newTokenPrefix 更新为新的前缀
	 * @param moduleId 当前更新的模块的ID
	 * @param sysId 模块所属的子系统ID
	 */
	public void updateChildToken(String orginTokenPrefix, String newTokenPrefix, 
			int moduleId, int sysId)
	{
		if (log.isInfoEnabled()) {
			log.info("更新token前缀为[" + orginTokenPrefix + "]的所有模块的前缀为["
					+ newTokenPrefix + "]");
		}
		String sql = "UPDATE sys_module SET token=REPLACE(token,?,?) "
			+ "WHERE token LIKE '" + orginTokenPrefix + "%' AND sysId=? AND moduleId!="
			+ moduleId;
		final String oldToken = orginTokenPrefix, newToken = newTokenPrefix;
		final int sId = sysId;
		this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setString(1, oldToken);
				ps.setString(2, newToken);
				ps.setInt(3, sId);
			}
		});
	}
	
	/**
	 * 根据指定的父菜单的token获取所有的子模块的信息.
	 * 返回结果采用Id-Token的键值对应。
	 * @param parentToken
	 * @return
	 */
	public Map<Integer,String> getAllChilds(String parentToken)
	{
		String sql = "SELECT moduleId,token FROM sys_module WHERE token LIKE '"
			+ parentToken + "%'";
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		Map<Integer,String> map = new HashMap<Integer,String>();
		while (rs.next()) {
			map.put(rs.getInt("moduleId"), rs.getString("token"));
		}
		return map;
	}
	
	/**
	 * 删除指定token的模块，如果该模块是菜单模块且有子模块则不允许删除
	 * @param pMId
	 * @return -1表不存在指定的模块 0-表示有子模块不允许删除 大于等于1-删除成功
	 */
	public int remove(String mToken)
	{
		String sql = "SELECT count(1) FROM sys_module WHERE token LIKE '"
			+ mToken + "%'";
		int num = this.getJdbcTemplate().queryForInt(sql);
		// 如果返回的结果集大于1则表示该模块为菜单模块且存在子模块，不允许删除
		if (num == 0) return -1;
		else if (num > 1) return 0;
		// 删除该模块(模块的功能由触发器进行删除)
		sql = "DELETE FROM sys_module WHERE token='" + mToken + "' AND sysModule=0";
		return this.getJdbcTemplate().update(sql);
	}
	
	/**
	 * 获取指定模块的功能模块信息.如果返回null则表示该模块不存在或存在但不是功能模块.
	 * @param mId
	 * @return
	 */
	public ModuleFunc getModuleFunc(int mId)
	{
		if (log.isInfoEnabled()) log.info("获取模块[" + mId + "]的功能模块信息~~");
		String sql = "SELECT a.moduleName,funcId,funcName,funcFlag,relFuncFlag "
			+ "FROM sys_module a LEFT JOIN sys_module_func b ON a.moduleId=b.moduleId "
			+ "WHERE a.moduleType=2 AND a.moduleId=" + mId;
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		if (!rs.next()) return null;
		ModuleFunc func = new ModuleFunc();
		func.setMId(mId);
		func.setMName(rs.getString("moduleName"));
		do {
			if (log.isInfoEnabled()) log.info("功能ID：" + rs.getInt("funcId"));
			if (rs.getInt("funcId") == 0) continue;
			ModuleSingleFunc sf = new ModuleSingleFunc();
			sf.setFuncId(rs.getInt("funcId"));
			sf.setFuncName(rs.getString("funcName"));
			sf.setFuncOperate(rs.getString("funcFlag"));
			sf.setRelOperate(rs.getString("relFuncFlag"));
			func.addFunc(sf);
		} while(rs.next());
		return func;
	}
	
	/**
	 * 删除指定模块的指定功能.
	 * @param moduleId
	 * @param funcId
	 */
	public void removeModuleFunc(int moduleId, int funcId)
	{
		if (log.isInfoEnabled()) {
			log.info("删除模块[" + moduleId + "]的功能[" + funcId + "]!");
		}
		String sql = "DELETE FROM sys_module_func WHERE moduleId=" + moduleId
			+ " AND funcId=" + funcId;
		this.getJdbcTemplate().update(sql);
	}
	
	/**
	 * 添加模块的功能，批处理添加
	 * @param moduleId
	 * @param funcs
	 */
	public void batchAddModuleFunc(int moduleId, List funcs)
	{
		if (log.isInfoEnabled()) log.info("添加模块" + moduleId + "的功能项~~~~~~");
		String sql = "INSERT INTO sys_module_func(moduleId,funcName,funcFlag,"
			+ "relFuncFlag) VALUES(?,?,?,?)";
		final List addFunc = funcs;
		final int mId = moduleId;
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter(){
			public int getBatchSize()
			{
				return addFunc.size();
			}
			public void setValues(PreparedStatement ps, int i) throws SQLException
			{
				ModuleSingleFunc sf = (ModuleSingleFunc)addFunc.get(i);
				ps.setInt(1, mId);
				ps.setString(2, sf.getFuncName());
				ps.setString(3, sf.getFuncOperate());
				ps.setString(4, sf.getRelOperate());
			}
		});
	}
	
	/**
	 * 更新模块的功能，批处理更新
	 * @param moduleId
	 * @param funcs
	 */
	public void batchUpdateModuleFunc(int moduleId, List funcs)
	{
		if (log.isInfoEnabled()) log.info("添加模块" + moduleId + "的功能项~~~~~~");
		String sql = "UPDATE sys_module_func SET funcName=?,funcFlag=?,"
			+ "relFuncFlag=? WHERE moduleId=? AND funcId=?";
		final List updateFunc = funcs;
		final int mId = moduleId;
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter(){
			public int getBatchSize()
			{
				return updateFunc.size();
			}
			public void setValues(PreparedStatement ps, int i) throws SQLException
			{
				ModuleSingleFunc sf = (ModuleSingleFunc)updateFunc.get(i);
				ps.setString(1, sf.getFuncName());
				ps.setString(2, sf.getFuncOperate());
				ps.setString(3, sf.getRelOperate());
				ps.setInt(4, mId);
				ps.setInt(5, sf.getFuncId());
			}
		});
	}
	
	/**
	 * 更新指定模块的单个功能的信息，同时返回当前更新的功能的唯一ID.
	 * @param moduleId
	 * @param func
	 * @return
	 */
	public int singleUpdateModuleFunc(int moduleId, ModuleSingleFunc func)
	{
		final ModuleSingleFunc f = func;
		final int mId = moduleId;
		int result = 0;
		if (func.getFuncId() <= 0) result = this.insertSingleFunc(f, mId);
		else result = this.updateSingleFunc(f, mId);
		return result;
	}
	
	/**
	 * 插入模块的单个功能.
	 * @param f
	 * @param mId
	 * @return
	 */
	private int insertSingleFunc(final ModuleSingleFunc f, final int mId)
	{
		synchronized(this.getClass()) {
			// 执行插入操作
			String sql = "INSERT INTO sys_module_func(moduleId,funcName,"
				+ "funcFlag,relFuncFlag) VALUES(?,?,?,?)";
			this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException
				{
					ps.setInt(1, mId);
					ps.setString(2, f.getFuncName());
					ps.setString(3, f.getFuncOperate());
					ps.setString(4, f.getRelOperate());
				}
			});
			// 获取刚插入的功能ID
			sql = "SELECT funcId FROM sys_module_func WHERE moduleId=? AND "
				+ "funcFlag=? ORDER BY funcId DESC";
			SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql, 
					new Object[] {mId, f.getFuncOperate()} );
			if (rs.next()) return rs.getInt("funcId");
			return 0;
		}
	}
	
	/**
	 * 更新指定模块指定的单个功能的信息.
	 * @param f
	 * @param mId
	 * @return
	 */
	private int updateSingleFunc(final ModuleSingleFunc f, final int mId)
	{
		String sql = "UPDATE sys_module_func SET funcName=?,funcFlag=?,"
			+ "relFuncFlag=? WHERE moduleId=? AND funcId=?";
		this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException
			{
				ps.setString(1, f.getFuncName());
				ps.setString(2, f.getFuncOperate());
				ps.setString(3, f.getRelOperate());
				ps.setInt(4, mId);
				ps.setInt(5, f.getFuncId());
			}
		});
		return f.getFuncId();
	}
	
	/**
	 * 调用子类的查询操作
	 * @param page
	 * @param where
	 */
	protected List exeQuery(String where)
	{
		String sql = "SELECT moduleId,sysId,moduleName,moduleType,moduleStatus,"
			+ "moduleURL,token,moduleSeq,moduleInfo,sysModule FROM sys_module "
			+ where;
		if (log.isInfoEnabled()) log.info("查询SQL语句：" + sql);
		return this.getJdbcTemplate().query(sql, new ModuleMapper());
	}
}
