package cn.qtone.common.components.syspurview.core.role.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import cn.qtone.common.components.syspurview.core.role.domain.Role;
import cn.qtone.common.components.syspurview.core.role.domain.RoleModule;
import cn.qtone.common.components.syspurview.core.role.domain.RoleModuleFunc;
import cn.qtone.common.mvc.dao.BaseDAO;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 角色管理的DAO.
 * 
 * @author 马必强
 *
 */
public abstract class BaseRoleDAO extends BaseDAO implements IRoleDAO
{
	/**
	 * 只使用角色ID进行删除.
	 */
	public int remove(int[] roleId)
	{
		if (log.isInfoEnabled()) log.info("删除角色：[" + StringUtil.join(roleId,",") + "]");
		String sql = "DELETE FROM sys_role WHERE roleId IN(" + StringUtil.join(roleId, ",") + ")";
		return this.getJdbcTemplate().update(sql);
	}

	/**
	 * 修改角色名称.
	 */
	public boolean update(Role role)
	{
		if (log.isInfoEnabled()) {
			log.info("更新角色信息：" + StringUtil.reflectObj(role));
		}
		String sql = "UPDATE sys_role SET roleName=:name,roleDetail=:detail "
			+ "WHERE roleId=:roleId";
		SqlParameterSource args = new BeanPropertySqlParameterSource(role);
		this.getNamedJdbcTemplate().update(sql, args);
		return true;
	}
	
	/**
	 * 删除指定角色对指定模块的权限.
	 * @param roleId
	 * @param moduleId
	 */
	public void removeRolePurview(int roleId, int[] moduleId)
	{
		if (log.isInfoEnabled()) {
			log.info("删除角色[" + roleId + "]对模块[" + StringUtil.join(moduleId, ",")
					+ "]的权限！");
		}
		final int rId = roleId;
		final int[] mId = moduleId;
		String sql = "DELETE FROM sys_role_purview WHERE roleId=? AND moduleId=?";
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			public int getBatchSize()
			{
				return mId.length;
			}

			public void setValues(PreparedStatement ps, int index) throws SQLException
			{
				ps.setInt(1, rId);
				ps.setInt(2, mId[index]);
			}
		});
	}
	
	/**
	 * 删除指定角色的全部权限.
	 * @param roleId
	 */
	public void removeRolePurview(int roleId)
	{
		if (log.isInfoEnabled()) log.info("删除角色[" + roleId + "]的所有权限！");
		String sql = "DELETE FROM sys_role_purview WHERE roleId=" + roleId;
		this.getJdbcTemplate().update(sql);
	}
	
	/**
	 * 添加角色的权限.
	 */
	public void addRolePurview(int roleId, int[] moduleId, int[][] funcId)
	{
		if (log.isInfoEnabled()) log.info("给角色添加模块权限~~~");
		String sql = "INSERT INTO sys_role_purview(roleId,moduleId,funcId) "
			+ "VALUES(?,?,?)";
		final int rId = roleId;
		final int[] mId = moduleId;
		final int[][] fId = funcId;
		
		// 使用spring的预处理回调方法执行插入，其ps会由spring来管理并关闭
		this.getJdbcTemplate().execute(sql, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException
			{
				for (int i = 0; i < mId.length; i ++) {
					for (int j = 0; j < fId[i].length; j ++) {
						ps.setInt(1, rId);
						ps.setInt(2, mId[i]);
						ps.setInt(3, fId[i][j]);
						ps.addBatch();
					}
				}
				return ps.executeBatch();
			}
			
		});
	}
	
	/**
	 * 获取指定角色对指定子系统的功能模块权限列表.只查询状态为正常状态的功能
	 * 模块，菜单模块不在授权之列。
	 * @param roleId
	 * @param sysId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getModuleList(int roleId, int sysId)
	{
		if (log.isInfoEnabled()) {
			log.info("获取角色[" + roleId + "]对子系统[" + sysId + "]各模块的权限列表！");
		}
		String sql = "SELECT a.moduleId,a.moduleName,b.funcId,b.funcName,"
			+ "c.funcId roleFuncId FROM sys_module a LEFT JOIN sys_module_func b "
			+ "ON a.moduleId=b.moduleId LEFT JOIN sys_role_purview c "
			+ "ON a.moduleId=c.moduleId AND b.funcId=c.funcId AND c.roleId=" + roleId 
			+ " WHERE a.sysId=" + sysId + " AND a.moduleType=2 AND a.moduleStatus IN (0,2) "
			+ "ORDER BY a.moduleSeq ASC,b.funcId ASC";
		return this.exeModuleListQuery(sql);
	}
	
	/**
	 * 查询指定模块下的所有可授权的模块列表.
	 * @param roleId
	 * @param sysId
	 * @param token
	 * @return
	 */
	public List getModuleList(int roleId, int sysId, String token)
	{
		if (log.isInfoEnabled()) {
			log.info("获取角色[" + roleId + "]对子系统[" + sysId + "]下以["
					+ token + "]开头的所有功能模块的权限列表！");
		}
		String sql = "SELECT a.moduleId,a.moduleName,b.funcId,b.funcName,"
			+ "c.funcId roleFuncId FROM sys_module a LEFT JOIN sys_module_func b "
			+ "ON a.moduleId=b.moduleId LEFT JOIN sys_role_purview c "
			+ "ON a.moduleId=c.moduleId AND b.funcId=c.funcId AND c.roleId=" + roleId 
			+ " WHERE a.sysId=" + sysId + " AND a.moduleType=2 AND a.moduleStatus IN (0,2) "
			+ "AND a.token LIKE '" + token + "%' ORDER BY a.moduleSeq ASC,b.funcId ASC";
		return this.exeModuleListQuery(sql);
	}
	
	/**
	 * 执行授权时模块和其功能的列表查询操作.
	 * @param sql
	 * @return
	 */
	private List exeModuleListQuery(String sql)
	{
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		List<RoleModule> result = new ArrayList<RoleModule>(); // 每一个模块作为一个list存储
		Map<Object,Object> tmp = new HashMap<Object,Object>();
		while (rs.next()) {
			// 首先获取模块的列表存储，没有则新建
			RoleModule mod = (RoleModule)tmp.get(rs.getInt("moduleId"));
			if (mod == null) {
				mod = new RoleModule();
				mod.setModuleId(rs.getInt("moduleId"));
				mod.setModuleName(rs.getString("moduleName"));
				tmp.put(rs.getInt("moduleId"), mod);
				result.add(mod);
			}
			// 如果当前模块的funcId为0则表示该模块还没有功能
			if (rs.getInt("funcId") == 0) continue;
			RoleModuleFunc func = new RoleModuleFunc();
			func.setFuncId(rs.getInt("funcId"));
			func.setFuncName(rs.getString("funcName"));
			// 如果查询到的roleFuncId不为空则表示该角色有对该模块的权限
			func.setHavePurview(rs.getInt("roleFuncId") != 0);
			mod.addFunc(func);
		}
		return result;
	}
	
	/**
	 * 删除指定角色对指定模块的权限.
	 */
	public void removeModulePurview(int roleId, int[] moduleId)
	{
		if (log.isInfoEnabled()) log.info("删除角色[" + roleId + "]对模块["
				+ StringUtil.join(moduleId, ",") + "]的所有权限！");
		String sql = "DELETE FROM sys_role_purview WHERE roleId=" + roleId
			+ " AND moduleId IN(" + StringUtil.join(moduleId, ",") + ")";
		this.getJdbcTemplate().update(sql);
	}
}
