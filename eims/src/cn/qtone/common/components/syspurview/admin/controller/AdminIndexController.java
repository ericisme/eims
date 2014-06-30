package cn.qtone.common.components.syspurview.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.components.syspurview.admin.service.AdminService;
import cn.qtone.common.components.syspurview.base.PurviewConfiger;
import cn.qtone.common.components.syspurview.base.controller.BaseNormalController;
import cn.qtone.common.components.syspurview.core.module.domain.TokenMaker;
import cn.qtone.common.components.syspurview.core.system.SystemCache;
import cn.qtone.common.components.syspurview.core.user.domain.AbstractUser;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 后台首页的管理控制器。
 * 
 * @author 马必强
 *
 */
public class AdminIndexController extends BaseNormalController
{
	private String desktopIndex; // 使用桌面式管理的后台首页
	
	private String desktopLeftMenu; // 使用桌面式管理的左侧菜单页面
	
	private String taskbarPage; // 后台桌面管理的任务栏页面

	/**
	 * 初始化后台管理首页
	 */
	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		if (log.isInfoEnabled()) log.info("初始化后台管理首页！");
		PurviewConfiger configer = this.getConfiger();
		AbstractUser user = UserUtil.getUserBean(request);
		ModelAndView view = UserUtil.checkAccess(user, request);
		if (view != null) return view;
		// 总是使用桌面式的后台管理
		return this.desktop(configer, user);
	}
	
	/**
	 * 桌面后台管理的任务栏
	 */
	public ModelAndView taskbar(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		if (log.isInfoEnabled()) log.info("获取桌面后台管理的任务栏页面~~~");
		// 获取用户的风格
		AbstractUser user = UserUtil.getUserBean(request);
		Map<String,Object> map = this.getMap();
		map.put("user", user);
		map.put("configer", this.getConfiger());
		return new ModelAndView(this.getTaskbarPage(), map);
	}
	
	/**
	 * 获取当前登陆用户对指定系统的模块（菜单和功能模块）列表.
	 * 此列表按分级获取，即每次只获取两级
	 */
	public ModelAndView leftMenu(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		// 根据用户所属的用户组的登陆子系统设置来获取其权限菜单
		User user = (User)UserUtil.getUserBean(request);
		int sysId = SystemCache.getInstance().getSystemId(user.getLoginSys());
		
		// 首先获取子系统ID
//		int sysId = StringUtil.parseInt(request.getParameter("sysId"), 0);
		if (sysId <= 0) sysId = SystemCache.getInstance().getDefaultSystemId();
		
		// 再获取父菜单的token值，如果为空则表面是获取一级和二级模块
		String token = StringUtil.trim(request.getParameter("token"));
		List modules = getAdminService().getMenu(sysId, token, user);
		// 设置模型数据
		PurviewConfiger configer = this.getConfiger();
		Map<String,Object> map = this.getMap();
		map.put("configer", configer);
		map.put("Modules", modules);
		map.put("showUp", token.equals("") ? false : true);
		map.put("pToken", TokenMaker.getParentToken(token));
		map.put("sysId", sysId);
		map.put("user", user);
		return new ModelAndView(this.getDesktopLeftMenu(), map);
	}
	
	public ModelAndView topMenu(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		Map<String,Object> map = new HashMap<String,Object>();
		AbstractUser user = UserUtil.getUserBean(request);
		map.put("user", user);
		map.put("configer", this.getConfiger());
		return new ModelAndView("/syspurview/admin/desktop/default/top", map);
	}
	
	public ModelAndView mainMenu(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		Map<String,Object> map = new HashMap<String,Object>();
		AbstractUser user = UserUtil.getUserBean(request);
		map.put("user", user);
		return new ModelAndView("/syspurview/admin/desktop/default/main_home", map);
	}
	
	
	/**
	 * 桌面式后台管理的首页显示.
	 * @return
	 */
	protected ModelAndView desktop(PurviewConfiger configer, AbstractUser user)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("configer", configer);
		map.put("systems", SystemCache.getInstance().getList());
		map.put("user", user);
		return new ModelAndView(this.getDesktopIndex(), map);
	}
	
	private AdminService getAdminService()
	{
		return (AdminService)this.getServiceBean();
	}

	public String getDesktopIndex()
	{
		return desktopIndex;
	}

	public void setDesktopIndex(String desktopIndex)
	{
		this.desktopIndex = desktopIndex;
	}
	
	public String getTaskbarPage()
	{
		return taskbarPage;
	}

	public void setTaskbarPage(String taskbarPage)
	{
		this.taskbarPage = taskbarPage;
	}

	public String getDesktopLeftMenu()
	{
		return desktopLeftMenu;
	}

	public void setDesktopLeftMenu(String desktopLeftMenu)
	{
		this.desktopLeftMenu = desktopLeftMenu;
	}
}
