package cn.qtone.common.components.syspurview.core.group.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.components.syspurview.base.controller.BaseManageController;
import cn.qtone.common.components.syspurview.core.group.domain.UserGroup;
import cn.qtone.common.components.syspurview.core.group.service.UserGroupService;
import cn.qtone.common.components.syspurview.core.system.SystemCache;
import cn.qtone.common.mvc.service.ServiceMsg;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 用户组管理的控制器.
 * 
 * @author 马必强
 *
 */
public class UserGroupController extends BaseManageController
{
	/**
	 * 用户组的列表查询.
	 */
	@Override
	public ModelAndView query(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		return new ModelAndView(this.getListTplPage(), "groups", 
				this.getMyService().listAll());
	}

	/**
	 * 用户组的添加页面显示.
	 */
	@Override
	public ModelAndView create(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		Map<String,Object> map = this.getMap();
		map.put("roles", this.getMyService().getAllRole());
		map.put("subSys", SystemCache.getInstance().getList());
		return new ModelAndView(this.getCreatePage(), map);
	}

	/**
	 * 用户组的编辑页面显示.
	 */
	@Override
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String groupId = request.getParameter("groupId");
		if (log.isInfoEnabled()) log.info("编辑用户组的ID为：" + groupId);
		ServiceMsg sms = this.getMyService().getUserGroup(groupId);
		if (!sms.isSuccess()) {
			return new ModelAndView(new AjaxView(sms.isSuccess(), sms.getMessage()));
		}
		Map<String,Object> map = this.getMap();
		UserGroup ug = (UserGroup) sms.getObject("group");
		String deposit = String.valueOf(ug.getDeposit());
		//deposit = deposit.indexOf(".")>0&&deposit.subSequence(deposit.indexOf("."), deposit.length()).length()>3?deposit.substring(0,deposit.indexOf(".")+3):deposit;
		String fine = String.valueOf(ug.getFine());
		//fine = fine.indexOf(".")>0&&fine.subSequence(fine.indexOf("."), fine.length()).length()>3?fine.substring(0,fine.indexOf(".")+3):fine;
		map.put("deposit",deposit);
		map.put("fine",fine );
		map.put("group", ug);
		map.put("roles", this.getMyService().getAllRole());
		map.put("subSys", SystemCache.getInstance().getList());
		return new ModelAndView(this.getEditPage(), map);
	}

	/**
	 * 用户组的列表显示.
	 */
	@Override
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		return new ModelAndView(this.getIndexPage(), "configer", this.getConfiger());
	}

	/**
	 * 用户组的删除操作.
	 */
	@Override
	public ModelAndView remove(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String[] groupId = request.getParameterValues("groupId");
		ServiceMsg sms = this.getMyService().remove(groupId);
		AjaxView view = new AjaxView(sms.isSuccess(), sms.getMessage());
		if (sms.isSuccess()) view.setProperty("refresh", true);
		return new ModelAndView(view);
	}

	/**
	 * 用户组添加.
	 */
	@Override
	public ModelAndView save(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		UserGroup ug = (UserGroup)this.getCommandObject(request, UserGroup.class);
		if (log.isInfoEnabled()) {
			log.info("要添加的用户组信息为：" + StringUtil.reflectObj(ug));
		}
		ServiceMsg sms = this.getMyService().addUserGroup(ug);
		return new ModelAndView(new AjaxView(sms.isSuccess(), sms.getMessage()));
	}

	/**
	 * 查看指定用户组的详细情况.
	 */
	@Override
	public ModelAndView show(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String groupId = request.getParameter("groupId");
		ServiceMsg sms = this.getMyService().getGroupRole(groupId);
		AjaxView view = new AjaxView(sms.isSuccess(), sms.getMessage());
		if (!sms.isSuccess()) return new ModelAndView(view);
		view.setProperty("roles", sms.getObject("roles"));
		return new ModelAndView(view);
	}

	/**
	 * 用户组的修改提交.
	 */
	@Override
	public ModelAndView update(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		UserGroup ug = (UserGroup)this.getCommandObject(request, UserGroup.class);
		if (log.isInfoEnabled()) {
			log.info("要更新的用户组信息为：" + StringUtil.reflectObj(ug));
		}
		ServiceMsg sms = this.getMyService().updateUserGroup(ug);
		return new ModelAndView(new AjaxView(sms.isSuccess(), sms.getMessage()));
	}
	
	protected UserGroupService getMyService()
	{
		return (UserGroupService)this.getServiceBean();
	}
}
