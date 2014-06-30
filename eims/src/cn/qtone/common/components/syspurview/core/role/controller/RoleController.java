package cn.qtone.common.components.syspurview.core.role.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.components.syspurview.base.controller.BaseManageController;
import cn.qtone.common.components.syspurview.core.module.domain.TokenMaker;
import cn.qtone.common.components.syspurview.core.module.service.ModuleService;
import cn.qtone.common.components.syspurview.core.role.domain.Role;
import cn.qtone.common.components.syspurview.core.role.domain.RoleGrantModule;
import cn.qtone.common.components.syspurview.core.role.service.RoleService;
import cn.qtone.common.components.syspurview.core.system.SystemCache;
import cn.qtone.common.mvc.service.ServiceMsg;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 角色的控制器对象.
 * 负责对象的创建、列表等操作。
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public class RoleController extends BaseManageController
{
	/**
	 * 角色的添加页面！
	 */
	@Override
	public ModelAndView create(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		if (log.isInfoEnabled()) log.info("角色添加页面，返回数据模型和视图名称！");
		return new ModelAndView(this.getCreatePage());
	}

	/**
	 * 角色信息编辑页面.
	 */
	@Override
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		if (log.isInfoEnabled()) log.info("角色编辑页面返回！");
		String roleId = request.getParameter("id");

		RoleService service = this.getRoleService();
		ServiceMsg sms = service.getRole(roleId);
		if (log.isInfoEnabled()) {
			log.info("获取的角色为：" + StringUtil.reflectObj(sms.getObject("role")));
		}
		if (!sms.isSuccess()) {
			return new ModelAndView(new AjaxView(false, sms.getMessage()));
		} else {
			return new ModelAndView(this.getEditPage(), "role", sms.getObject("role"));
		}
	}

	/**
	 * 角色列表首页.
	 */
	@Override
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		if (log.isInfoEnabled()) log.info("获取角色列表！");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("configer", this.getConfiger());
		//new SimplePagePurview().setPageMap(request, map);
		return new ModelAndView(this.getIndexPage(), map);
	}

	/**
	 * 角色查询结果页面.
	 */
	@Override
	public ModelAndView query(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String roleName = request.getParameter("name");
		
		if (log.isInfoEnabled()) {
			log.info("角色列表查询！传递参数[RoleName=" + roleName + "]");
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("Roles", this.getRoleService().query(roleName));
		map.put("configer", this.getConfiger());
		
		return new ModelAndView(this.getListTplPage(), map);
	}

	/**
	 * 删除角色.
	 */
	@Override
	public ModelAndView remove(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String[] roleIds = request.getParameterValues("id");
		ServiceMsg sms = this.getRoleService().remove(roleIds);
		AjaxView view = new AjaxView(sms.isSuccess(), sms.getMessage());
		view.setProperty("refresh", sms.getObject("Refresh") != null);
		if (sms.getObject("NoDel") != null) {
			view.setProperty("NoDel", (String)sms.getObject("NoDel"));
		}
		return new ModelAndView(view);
	}

	/**
	 * 角色添加.
	 */
	@Override
	public ModelAndView save(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		Role role = (Role)this.getCommandObject(request, Role.class);
		if (log.isInfoEnabled()) {
			log.info("获取要添加的角色：" + StringUtil.reflectObj(role));
		}
		ServiceMsg sms = this.getRoleService().save(role);
		return new ModelAndView(new AjaxView(sms.isSuccess(), sms.getMessage()));
	}

	@Override
	public ModelAndView show(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		return null;
	}

	/**
	 * 更新角色
	 */
	@Override
	public ModelAndView update(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		Role role = (Role)this.getCommandObject(request, Role.class);
		if (log.isInfoEnabled()) log.info("更新的角色信息为：" + StringUtil.reflectObj(role));
		ServiceMsg sms = this.getRoleService().update(role);
		return new ModelAndView(new AjaxView(sms.isSuccess(), sms.getMessage()));
	}
	
	/**
	 * 显示对指定角色进行授权的授权首页.
	 */
	public ModelAndView grantIndex(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String roleId = StringUtil.trim(request.getParameter("roleId"));
		String roleName = StringUtil.trim(request.getParameter("roleName"));
		if (log.isInfoEnabled()) {
			log.info("显示对角色[id=" + roleId + ",name=" + roleName + "]授权的首页！");
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("roleId", roleId);
		map.put("roleName", roleName);
		map.put("systems", SystemCache.getInstance().getList());
		return new ModelAndView(this.getGrantIndex(), map);
	}
	
	/**
	 * 授权页面的左边菜单获取.
	 */
	public ModelAndView grantMenu(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String sysId = request.getParameter("sysId");
		String token = StringUtil.trim(request.getParameter("token"));
		
		// 调用模块管理的业务类来查询指定系统下指定菜单的所有模块
		ServiceMsg sms = this.getModuleService().query(sysId, null, token);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("showUp", token.equals("") ? false : true);
		map.put("noResult", sms.getObject("noResult"));
		map.put("Modules", sms.getObject("list"));
		map.put("pToken", TokenMaker.getParentToken(token));
		map.put("configer", this.getConfiger());
		
		return new ModelAndView(this.getGrantLeft(), map);
	}
	
	/**
	 * 编辑角色的权限，获取指定子系统和指定菜单下的所有模块和其功能.
	 */
	public ModelAndView editRolePurview(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		if (log.isInfoEnabled()) log.info("更新角色的权限[列表显示]~~~~");
		String sysId = request.getParameter("sysId");
		String roleId = request.getParameter("roleId");
		String token = request.getParameter("token");
		ServiceMsg sms = this.getRoleService().editRolePurview(sysId, roleId, token);
		if (!sms.isSuccess()) {
			return new ModelAndView(new AjaxView(false,sms.getMessage()));
		}
		return new ModelAndView(this.getGrantRight(), "modules", sms.getObject("list"));
	}
	
	/**
	 * 提交对指定角色的授权.
	 */
	public ModelAndView grantPurview(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		RoleGrantModule gm = (RoleGrantModule)this.getCommandObject(request, 
				RoleGrantModule.class);
		ServiceMsg sms = this.getRoleService().checkGrant(gm);
		if (!sms.isSuccess()) {
			return new ModelAndView(new AjaxView(false, sms.getMessage()));
		}
		
		// 获取每一个模块下的功能列表
		int[] moduleId = gm.getModuleId();
		for (int i = 0; i < moduleId.length; i ++) {
			gm.setModuleFunc(moduleId[i], StringUtil.parseInt(
					request.getParameterValues("funcId_" + moduleId[i]), 0));
		}
		
		// 执行角色的授权操作
		this.getRoleService().grantPurview(gm);
		return new ModelAndView(new AjaxView(true, "角色权限授权成功！"));
	}
	
	/**
	 * 删除指定角色的所有权限.
	 */
	public ModelAndView removeAllPurview(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String roleId = request.getParameter("roleId");
		ServiceMsg sms = this.getRoleService().removeAllPurview(roleId);
		return new ModelAndView(new AjaxView(sms.isSuccess(), sms.getMessage()));
	}
	
	/**
	 * 删除指定角色对指定模块的所有权限
	 */
	public ModelAndView removeModulePurview(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String[] moduleId = request.getParameterValues("moduleId");
		String roleId = request.getParameter("grantRoleId");
		ServiceMsg sms = this.getRoleService().removeModulePurview(roleId, moduleId);
		return new ModelAndView(new AjaxView(sms.isSuccess(), sms.getMessage()));
	}
	
	/**
	 * 获取角色的业务逻辑对象.
	 * @return
	 */
	private RoleService getRoleService()
	{
		return (RoleService)this.getServiceBean();
	}
	
	public String getGrantIndex()
	{
		return grantIndex;
	}

	public void setGrantIndex(String grantIndex)
	{
		this.grantIndex = grantIndex;
	}

	public String getGrantLeft()
	{
		return grantLeft;
	}

	public void setGrantLeft(String grantLeft)
	{
		this.grantLeft = grantLeft;
	}

	public String getGrantRight()
	{
		return grantRight;
	}

	public void setGrantRight(String grantRight)
	{
		this.grantRight = grantRight;
	}

	public ModuleService getModuleService()
	{
		return moduleService;
	}

	public void setModuleService(ModuleService moduleService)
	{
		this.moduleService = moduleService;
	}

	private String grantIndex; // 角色授权的首页
	
	private String grantLeft; // 角色授权的左侧菜单列表页面
	
	private String grantRight; // 角色授权的右侧功能列表页面
	
	private ModuleService moduleService; // 模块的业务逻辑处理类
}
