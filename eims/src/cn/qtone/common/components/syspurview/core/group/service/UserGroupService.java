package cn.qtone.common.components.syspurview.core.group.service;

import java.util.List;

import cn.qtone.common.components.syspurview.core.group.dao.IUserGroupDAO;
import cn.qtone.common.components.syspurview.core.group.domain.UserGroup;
import cn.qtone.common.components.syspurview.core.role.dao.IRoleDAO;
import cn.qtone.common.mvc.service.ServiceMsg;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 用户组管理的业务逻辑处理类.
 * 
 * @author 马必强
 * 
 */
public class UserGroupService
{
	private IUserGroupDAO dao;
	
	private IRoleDAO roleDAO; // 角色管理的DAO

	public IRoleDAO getRoleDAO()
	{
		return roleDAO;
	}

	public void setRoleDAO(IRoleDAO roleDAO)
	{
		this.roleDAO = roleDAO;
	}

	public IUserGroupDAO getDao()
	{
		return dao;
	}

	public void setDao(IUserGroupDAO dao)
	{
		this.dao = dao;
	}

	/**
	 * 列表所有的用户组.
	 * 
	 * @return
	 */
	public List listAll()
	{
		return this.dao.listAll();
	}
	
	/**
	 * 获取系统中的所有角色列表.
	 * @return
	 */
	public List getAllRole()
	{
		return this.roleDAO.query();
	}
	
	/**
	 * 添加用户组.
	 * @param ug
	 * @return
	 */
	public ServiceMsg addUserGroup(UserGroup ug)
	{
		ServiceMsg sms = new ServiceMsg();
		if (!this.checkGroupInfo(ug, sms, true)) return sms;
		this.dao.addUserGroup(ug);
		int groupId = dao.getGroupId(ug);
		this.dao.addGroupRole(groupId, ug.getGroupRole());
		sms.setMessage("用户组信息添加成功！");
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 获取指定用户组的信息.
	 * @param groupId
	 * @return
	 */
	public ServiceMsg getUserGroup(String groupId)
	{
		ServiceMsg sms = new ServiceMsg();
		int gId = StringUtil.parseInt(groupId, 0);
		UserGroup ug = this.dao.getUserGroup(gId);
		if (ug == null) {
			sms.setMessage("指定的用户组不存在！");
			return sms;
		}
		sms.addObject("group", ug);
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 更新用户组的信息.
	 * @param ug
	 * @return
	 */
	public ServiceMsg updateUserGroup(UserGroup ug)
	{
		ServiceMsg sms = new ServiceMsg();
		if (!this.checkGroupInfo(ug, sms, false)) return sms;
		this.dao.updateUserGroup(ug);
		this.dao.removeGroupRole(ug.getGroupId());
		dao.addGroupRole(ug.getGroupId(), ug.getGroupRole());
		sms.setMessage("用户组信息更新成功！");
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 删除指定的用户组.
	 */
	public ServiceMsg remove(String[] groupId)
	{
		ServiceMsg sms = new ServiceMsg();
		int[] gId = StringUtil.parseInt(groupId, 0);
		if (gId.length < 1) {
			sms.setMessage("请指定要删除的用户组！");
			return sms;
		}
		this.dao.remove(gId);
		sms.setMessage("用户组删除成功！");
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 获取指定用户组包含的角色的列表信息.
	 * @param groupId
	 * @return
	 */
	public ServiceMsg getGroupRole(String groupId)
	{
		ServiceMsg sms = new ServiceMsg();
		int gId = StringUtil.parseInt(groupId, 0);
		if (gId <= 0) {
			sms.setMessage("请指定正确的用户组ID！");
			return sms;
		}
		List roles = this.dao.getGroupRole(gId);
		if (roles == null || roles.size() < 1) {
			sms.setMessage("该用户组暂时还不包含任何角色，请尽快设置！");
			return sms;
		}
		sms.addObject("roles", roles);
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 添加/更新时检查用户组的信息.
	 * @param ug
	 * @param sms
	 * @return
	 */
	private boolean checkGroupInfo(UserGroup ug, ServiceMsg sms, boolean add)
	{
		if ("".equals(ug.getGroupName())) {
			sms.setMessage("用户组的名称不能为空！");
			return false;
		} else if (ug.getGroupRole() == null || ug.getGroupRole().length < 1) {
			sms.setMessage("必须为用户组指定至少一个角色！");
			return false;
		} else if (!add && ug.getGroupId() < 1) {
			sms.setMessage("编辑用户组信息请指定用户组的唯一ID！");
			return false;
		}
		return true;
	}
}
