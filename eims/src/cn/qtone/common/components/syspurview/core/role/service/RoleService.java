package cn.qtone.common.components.syspurview.core.role.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import cn.qtone.common.components.syspurview.core.role.dao.IRoleDAO;
import cn.qtone.common.components.syspurview.core.role.domain.Role;
import cn.qtone.common.components.syspurview.core.role.domain.RoleGrantModule;
import cn.qtone.common.components.syspurview.core.system.SystemCache;
import cn.qtone.common.mvc.service.ServiceMsg;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 角色管理的业务逻辑处理类.
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public class RoleService
{
	private static final Logger log = Logger.getLogger(RoleService.class);

	private IRoleDAO dao; // 角色的DAO
	
	private TransactionTemplate tt;
	
	/**
	 * 添加角色信息到指定数据库.
	 * @param role
	 * @return
	 */
	public ServiceMsg save(Role role)
	{
		ServiceMsg sms = new ServiceMsg();
		if (log.isInfoEnabled()) log.info("调用业务逻辑处理角色添加！");
		if (role == null || role.getName().intern() == "") {
			sms.setMessage("添加角色必须指定角色的名称！");
			return sms;
		}
		sms.setSuccess(dao.save(role));
		sms.setMessage("角色信息保存成功！");
		return sms;
	}
	
	/**
	 * 列表查询角色.
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public List query(String roleName)
	{
		String tmp = StringUtils.trimToEmpty(roleName).intern();
		return tmp == "" ? this.dao.query() : this.dao.query(tmp);
	}
	
	/**
	 * 获取指定ID的角色Bean信息.
	 * @param roleId
	 * @return
	 */
	public ServiceMsg getRole(String roleId)
	{
		ServiceMsg sms = new ServiceMsg();
		int id = StringUtil.parseInt(roleId, 0);
		if (id <= 0) {
			sms.setMessage("非法的角色ID[" + roleId + "]!");
			return sms;
		}
		sms.addObject("role", dao.getRole(id));
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 更新指定角色的信息
	 * @param role
	 * @return
	 */
	public ServiceMsg update(Role role)
	{
		ServiceMsg sms = new ServiceMsg();
		if (role == null) {
			sms.setMessage("角色信息Bean不能为空！");
			return sms;
		}
		if (role.getRoleId() <=0 || role.getName().intern() == "") {
			sms.setMessage("必须指定角色的ID和角色的名称信息！");
			return sms;
		}
		sms.setSuccess(dao.update(role));
		sms.setMessage("角色信息更新成功！");
		return sms;
	}
	
	/**
	 * 删除角色.
	 * @param roleIds
	 * @return
	 */
	public ServiceMsg remove(String[] roleIds)
	{
		ServiceMsg sms = new ServiceMsg();
		if (roleIds == null || roleIds.length < 1) {
			sms.setMessage("必须指定要删除的角色ID！");
			return sms;
		}
		if (log.isInfoEnabled()) log.info("要删除的角色是：" + StringUtils.join(roleIds, ","));
		int[] ids = StringUtil.parseInt(roleIds, 0);
		int result = this.dao.remove(ids);
		if (result > 0) sms.addObject("Refresh", true);
		sms.setSuccess(true);
		sms.setMessage("角色删除成功！");
		return sms;
	}
	
	/**
	 * 编辑指定角色的权限.
	 * @param sysId
	 * @param roleId
	 * @return
	 */
	public ServiceMsg editRolePurview(String sysId, String roleId, String token)
	{
		ServiceMsg sms = new ServiceMsg();
		int deftSysId = SystemCache.getInstance().getDefaultSystemId();
		int sId = StringUtil.parseInt(sysId, deftSysId);
		int rId = StringUtil.parseInt(roleId, 0);
		if (rId <= 0) {
			sms.setMessage("必须指定角色的ID才能进行授权！");
			return sms;
		}
		String _token = StringUtils.trim(token).intern();
		if (_token == "") {
			sms.addObject("list", this.dao.getModuleList(rId, sId));
		} else {
			sms.addObject("list", this.dao.getModuleList(rId, sId, _token));
		}
		sms.addObject("systemId", sId);
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 检查对角色的授权的数据的合法性.
	 * @param gm
	 * @return
	 */
	public ServiceMsg checkGrant(RoleGrantModule gm)
	{
		ServiceMsg sms = new ServiceMsg();
		if (gm.getGrantRoleId() <= 0) {
			sms.setMessage("授权失败：必须指定要授权的角色！");
			return sms;
		} else if (gm.getModuleId() == null || gm.getModuleId().length < 1) {
			sms.setMessage("授权失败：必须要指定要授权的模块！");
			return sms;
		}
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 执行对角色的授权操作.在授予角色权限时应该先删除其之前对指定模块的权限，然后再
	 * 进行插入操作.所以这两个操作应该在一个事务中进行.
	 * @param gm
	 * @return
	 */
	public void grantPurview(RoleGrantModule gm)
	{
		final IRoleDAO fdao = this.dao;
		
		// 首先获取要删除的角色对指定模块的权限
		final int roleId = gm.getGrantRoleId();
		final int[] delModuleId = gm.getModuleId();
		
		// 获取要添加的模块ID和其功能ID
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < delModuleId.length; i ++) {
			int[] tmp = gm.getModuleFunc(delModuleId[i]);
			if (tmp == null || tmp.length < 1) continue;
			buf.append(delModuleId[i] + ",");
		}
		if (buf.length() > 1) buf.deleteCharAt(buf.length() - 1);
		final int[] saveModuleId = StringUtil.parseInt(buf.toString().split(","), 0);
		int[][] tmpFuncId = new int[saveModuleId.length][];
		for (int i = 0; i < saveModuleId.length; i ++) {
			tmpFuncId[i] = gm.getModuleFunc(saveModuleId[i]);
		}
		final int[][] funcId = tmpFuncId;
		
		if (log.isInfoEnabled()) {
			log.info("要被删除的模块ID为：" + StringUtil.join(delModuleId, ","));
			StringBuilder tmp = new StringBuilder();
			for (int i = 0; i < saveModuleId.length; i ++) {
				tmp.append("\n要添加的模块ID：" + saveModuleId[i]);
				tmp.append(" 其功能ID：" + StringUtil.join(funcId[i], ","));
			}
			log.info("添加信息：" + tmp);
		}
		
		// spring 的编程式事务
		tt.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus arg0)
			{
				fdao.removeRolePurview(roleId, delModuleId);
				// 如果有要添加的模块，则执行添加操作
				if (saveModuleId.length > 0) {
					fdao.addRolePurview(roleId, saveModuleId, funcId);
				}
				return null;
			}
		});
	}
	
	/**
	 * 设置事务处理的调用模板.
	 * @param tt
	 */
	public void setTransactionTpl(TransactionTemplate tt)
	{
		this.tt = tt;
	}
	
	/**
	 * 删除指定角色的所有权限.
	 * @param roleId
	 * @return
	 */
	public ServiceMsg removeAllPurview(String roleId)
	{
		ServiceMsg sms = new ServiceMsg();
		int rId = StringUtil.parseInt(roleId, 0);
		if (rId <= 0) {
			sms.setMessage("删除角色的权限必须指定角色的唯一ID！");
			return sms;
		}
		this.dao.removeRolePurview(rId);
		sms.setSuccess(true);
		sms.setMessage("角色所有权限已被成功删除！");
		return sms;
	}
	
	/**
	 * 删除指定角色对指定模块的所有权限.
	 * @param moduleId
	 * @return
	 */
	public ServiceMsg removeModulePurview(String roleId, String[] moduleId)
	{
		ServiceMsg sms = new ServiceMsg();
		int rId = StringUtil.parseInt(roleId, 0);
		if (rId <= 0) {
			sms.setMessage("指定的角色不存在或非法！");
			return sms;
		}
		int[] mId = StringUtil.parseInt(moduleId, 0);
		if (mId == null || mId.length < 1) {
			sms.setMessage("必须指定要删除权限的模块ID！");
			return sms;
		}
		this.dao.removeModulePurview(rId, mId);
		sms.setSuccess(true);
		sms.setMessage("已成功删除角色对当前选择模块的所有权限！");
		return sms;
	}

	public IRoleDAO getDao()
	{
		return dao;
	}

	public void setDao(IRoleDAO dao)
	{
		this.dao = dao;
	}
}
