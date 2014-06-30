package cn.qtone.common.components.syspurview.login.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import cn.qtone.common.components.syspurview.core.user.domain.AbstractUser;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.components.syspurview.login.domain.PurviewMap;
import cn.qtone.common.mvc.dao.BaseDAO;
import cn.qtone.common.utils.base.CollectionUtil;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 登陆验证DAO的不同数据库的公用设置.
 * 
 * @author 马必强
 *
 */
public abstract class BaseLoginDAO extends BaseDAO implements ILoginDAO
{
	/**
	 * 将查询结果转换成User对象返回。
	 * @param rs
	 * @return
	 */
	protected User getUser(String sql, String userName, String password)
	{
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql, 
				new Object[]{userName, password});
		return this.getUserInfo(rs);
	}
	
	/**
	 * 代理登陆时候获取用户的基本信息，包括代理人的ID.
	 * @param sql
	 * @param proxyUserId 当前用户的唯一ID（即代理人的ID，系统或超级管理员的ID）
	 * @return
	 */
	protected User getProxyUser(String sql, int proxyUserId, String proxyUserName)
	{
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		User user = this.getUserInfo(rs);
		if (user == null) return null;
		user.setProxyUserId(proxyUserId);
		user.setProxyUserName(proxyUserName);
		return user;
	}
	
	protected User getUserInfo(SqlRowSet rs)
	{
		if (!rs.next()) return null;
		User user = new User();
		user.setUserId(rs.getInt("userId"));
		user.setGroupId(rs.getInt("groupId"));
		user.setLoginSys(rs.getString("loginSys"));
		user.setLoginName(rs.getString("loginName"));
		user.setLoginPassword(rs.getString("loginPassword"));
		user.setRealName(rs.getString("realName"));
		user.setMobile(rs.getString("mobile"));
		user.setEmail(rs.getString("email"));
		user.setAddTime(rs.getString("addTime"));
		user.setLastLoginTime(rs.getString("lastLoginTime"));
		user.setLastLoginIP(rs.getString("lastLoginIP"));
		user.setLoginTimes(rs.getInt("loginTimes"));
		user.setLock(rs.getInt("isLock") == 1);
		user.setSuperManager(rs.getInt("isSuper") == 1);
		user.setUserType(rs.getString("USER_TYPE"));
		user.setTown_id(rs.getInt("town_id"));
		user.setAgency_id(rs.getInt("agency_id"));
		user.setSchool_id(rs.getInt("school_id"));
		user.setGrade_id(rs.getInt("grade_id"));
		return user;
	}
	
	/**
	 * 获取普通用户的模块权限.对于普通用户的模块操作权限是查询其所属角色全部的功能模块的所有功能，
	 * 这些功能当中包含了用户具有的权限和不具有的权限，这样是为了在进行页面级的按钮自动匹配过滤时
	 * 用到.<br>
	 * 用户的权限分两部进行查询，第一步是查询出该用户所属角色的所有可操作的模块ID和功能ID，然后
	 * 再查询模块和模块功能表查询出这些模块的所有功能.
	 */
	@SuppressWarnings("unchecked")
	public PurviewMap getRolePurview(AbstractUser user)
	{
		// 首先获取该用户组所对应的角色ID
		String sql = "SELECT roleId FROM sys_group_role WHERE groupId=" + user.getGroupId();
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		List<Integer> roles = new ArrayList<Integer>();
		while (rs.next()) roles.add(rs.getInt("roleId"));
		user.setRoleId(CollectionUtil.listToIntArray(roles));
		
		// 再根据角色进行查询权限
		List[] rolePurview = this.getRoleModuleAndFunc(user.getRoleId());
		return this.loadRolePurview(rolePurview[0], rolePurview[1]);
	}
	
	/**
	 * step1 - 获取指定角色具有的所有模块和功能ID列表.
	 * @param userPurview
	 */
	protected List[] getRoleModuleAndFunc(int[] roleId)
	{
		List<Integer> moduleId = new ArrayList<Integer>(), funcId = new ArrayList<Integer>();
		String sql = "SELECT moduleId,funcId FROM sys_role_purview WHERE roleId IN("
			+ StringUtil.join(roleId, ",") + ")";
		if(log.isInfoEnabled()) log.info("权限："+sql);
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		while (rs.next()) {
			int tmp = rs.getInt("moduleId");
			if (!moduleId.contains(tmp)) moduleId.add(tmp);
			tmp = rs.getInt("funcId");
			if (!funcId.contains(tmp)) funcId.add(tmp);
		}
		return new List[]{moduleId, funcId};
	}
	
	/**
	 * step2 - 加载指定模块的模块信息和其所有功能信息.
	 * @param modIdList
	 * @param funcIdList
	 * @return
	 */
	protected PurviewMap loadRolePurview(List<Integer> modIdList, List<Integer> funcIdList)
	{
		PurviewMap userPurview = new PurviewMap(); // 用户的权限集合
		
		// 如果模块的ID列表为空，则直接返回空权限集合
		if (modIdList.isEmpty()) return userPurview;
		
		int[] modId = CollectionUtil.listToIntArray(modIdList);

		String sql = "SELECT moduleURL,funcName,funcFlag,relFuncFlag,funcId FROM sys_module a "
			+ "INNER JOIN sys_module_func b ON a.moduleId=b.moduleId WHERE moduleStatus>=0 "
			+ "AND moduleType=2 AND a.moduleId IN(" + StringUtil.join(modId, ",") + ")";
		if (log.isInfoEnabled()) log.info("查询当前用户的权限集合！\n " + sql);
		SqlRowSet rs = this.getJdbcTemplate().queryForRowSet(sql);
		Map<String,String> modTmp = null;
		while (rs.next()) {
			String url = StringUtil.trim(rs.getString("moduleURL")); // 模块的唯一URL
			Map<String,String>[] modMap = this.getModulePurview(userPurview, url);
			
			// 如果funcIdList中不包含当前记录的funcId，则表示没有该功能的权限,否则是有权限
			if (!funcIdList.contains(rs.getInt("funcId"))) modTmp = modMap[1];
			else modTmp = modMap[0];
			
			modTmp.put(StringUtil.trim(rs.getString("funcFlag")), 
					StringUtil.trim(rs.getString("funcName")));
			this.setRelFunc(modTmp, StringUtil.trim(rs.getString("relFuncFlag")));
		}
		return userPurview;
	}
	
	/**
	 * 设置相关功能操作权限
	 * @param moduleMap
	 * @param relFunc
	 */
	protected void setRelFunc(Map<String,String> moduleMap, String relFunc)
	{
		if (relFunc.equals("")) return;
		String[] funcs = relFunc.split("\\s+"); // 以空格隔开的相关操作
		for (int i = 0; i < funcs.length; i ++) {
			moduleMap.put(funcs[i], ""); // 相关功能的名称为空
		}
	}
	
	/**
	 * 在用户的权限集合中获取对当前指定模块的权限列表
	 * @param userPurview
	 * @param url
	 */
	@SuppressWarnings("unchecked")
	protected Map<String,String>[] getModulePurview(PurviewMap userPurview, String url)
	{
		Map<String,String>[] moduleMap = userPurview.getModulePurview(url);
		if (moduleMap != null) return moduleMap;
		moduleMap = new Map[2];
		moduleMap[0] = new HashMap<String,String>();
		moduleMap[1] = new HashMap<String,String>();
		userPurview.addModulePurview(url, moduleMap);
		return moduleMap;
	}
}
