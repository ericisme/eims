package cn.qtone.common.components.syspurview.core.module.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.components.syspurview.base.controller.BaseManageController;
import cn.qtone.common.components.syspurview.core.module.domain.Module;
import cn.qtone.common.components.syspurview.core.module.domain.ModuleFunc;
import cn.qtone.common.components.syspurview.core.module.domain.TokenMaker;
import cn.qtone.common.components.syspurview.core.module.service.ModuleService;
import cn.qtone.common.components.syspurview.core.system.SystemCache;
import cn.qtone.common.mvc.service.ServiceMsg;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 模块管理设置的中心控制器。
 * 
 * @author 马必强
 *
 */
public class ModuleController extends BaseManageController
{
	private String funcIndex; // 模块的功能管理首页
	
	/**
	 * 模块添加页面.
	 */
	@Override
	public ModelAndView create(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		if (log.isInfoEnabled()) log.info("模块添加页面显示~~~");
		SystemCache cache = SystemCache.getInstance();
		
		// 首先获取是否指定了父菜单,如果没有指定则new一个空的module对象
		Module mod = this.getService().getModuleByToken(request.getParameter("ptoken"));
		int sysId = mod == null ? cache.getDefaultSystemId() : mod.getSysId();
		if (mod == null) {
			mod = new Module();
			mod.setMToken(null);
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("systems", cache.getList());
		map.put("menus", this.getService().getAllMenu(sysId));
		// 当前被选中的菜单（对单个菜单进行模块添加时）
		map.put("pMod", mod);
		return new ModelAndView(this.getCreatePage(), map);
	}

	/**
	 * 模块信息编辑
	 */
	@Override
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String mId = request.getParameter("mId");
		if (log.isInfoEnabled()) log.info("要编辑的模块是：" + mId);
		ServiceMsg sms = this.getService().getModule(mId);
		if (sms.isSuccess()) {
			Map<String,Object> map = new HashMap<String,Object>();
			SystemCache cache = SystemCache.getInstance();
			map.put("systems", cache.getList());
			Module mod = (Module)sms.getObject("Mod");
			map.put("mod", mod);
			map.put("parentToken", TokenMaker.getParentToken(mod));
			map.put("menus", this.getService().getAllMenu(mod.getSysId(), 
					mod.getMToken()));
			return new ModelAndView(this.getEditPage(), map);
		} else {
			return new ModelAndView(new AjaxView(false, sms.getMessage()));
		}
	}

	/**
	 * 模块列表管理的首页.
	 */
	@Override
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		if (log.isInfoEnabled()) log.info("模块管理首页显示获取~~~");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("configer", this.getConfiger());
		map.put("systems", SystemCache.getInstance().getList());
		return new ModelAndView(this.getIndexPage(), map);
	}

	/**
	 * 列表查询
	 */
	@Override
	public ModelAndView query(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		// 获取查询并执行查询
		String sysId = StringUtil.trim(request.getParameter("sysId"));
		String mName = request.getParameter("moduleName");
		String token = StringUtil.trim(request.getParameter("token"));
		if (log.isInfoEnabled()) {
			log.info("模块列表查询，当前请求参数为[sysId=" + sysId + ",moduleName="
					+ mName + ",token=" + token + "]");
		}
		ServiceMsg sms = this.getService().query(sysId, mName, token);
		
		// 如果token不为空则查询其名称
		String qMName = null;
		if (token.intern() != "") {
			Module mod = this.getService().getModuleByToken(token);
			if (mod != null) qMName = mod.getMName();
		}
		if (qMName == null) qMName = "";
		
		// 设置返回结果
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("configer", this.getConfiger());
		map.put("byName", sms.getObject("queryByName"));
		map.put("noResult", sms.getObject("noResult"));
		map.put("Modules", sms.getObject("list"));
		map.put("pToken", TokenMaker.getParentToken(token));
		map.put("showUp", token.equals("") ? false : true);
		map.put("qModuleName", qMName);
		return new ModelAndView(this.getListTplPage(), map);
	}

	/**
	 * 删除模块.
	 */
	@Override
	public ModelAndView remove(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String mToken = request.getParameter("mToken");
		if (log.isInfoEnabled()) log.info("要删除的模块Token为：" + mToken);
		ServiceMsg sms = this.getService().removeModule(mToken);
		AjaxView view = new AjaxView(sms.isSuccess(), sms.getMessage());
		view.setProperty("refresh", sms.getObject("Refresh") != null);
		return new ModelAndView(view);
	}

	/**
	 * 模块添加.
	 */
	@Override
	public ModelAndView save(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		Module mod = (Module)this.getCommandObject(request, Module.class);
		if (log.isInfoEnabled()) {
			log.info("模块添加的参数是：" + StringUtil.reflectObj(mod));
		}
		String parentToken = request.getParameter("parentMenu");
		ServiceMsg sms = this.getService().addModule(mod, parentToken);
		return new ModelAndView(new AjaxView(sms.isSuccess(), sms.getMessage()));
	}

	@Override
	public ModelAndView show(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		return null;
	}

	/**
	 * 更新模块信息
	 */
	@Override
	public ModelAndView update(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		Module mod = (Module)this.getCommandObject(request, Module.class);
		if (log.isInfoEnabled()) {
			log.info("要更新的模块信息为：" + StringUtil.reflectObj(mod));
		}
		String parentToken = request.getParameter("parentMenu");
		ServiceMsg sms = this.getService().updateModule(mod, parentToken);
		return new ModelAndView(new AjaxView(sms.isSuccess(), sms.getMessage()));
	}
	
	/**
	 * 根据指定的子系统ID来获取其下可用的菜单模块.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getMenu(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String sysId = request.getParameter("sysId");
		ServiceMsg sms = this.getService().menuTreeToJSON(sysId);
		if (!sms.isSuccess()) {
			return new ModelAndView(new AjaxView(false, sms.getMessage()));
		}
		AjaxView view = new AjaxView(true, "子系统菜单获取成功！");
		view.setProperty("select", sms.getObject("sltJson"));
		return new ModelAndView(view);
	}
	
	/**
	 * 获取指定的功能模块的所有功能并显示.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView moduleFunc(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String mId = request.getParameter("mId");
		ServiceMsg sms = this.getService().getModuleFunc(mId);
		if (!sms.isSuccess()) {
			return new ModelAndView(new AjaxView(false, sms.getMessage()));
		}
		return new ModelAndView(this.funcIndex, "modFunc", sms.getObject("MF"));
	}
	
	/**
	 * 添加或更新指定模块的功能项（一个或多个）.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateFunc(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		ModuleFunc func = null;
		// 是否是添加单个功能，这样会返回功能ID
		String single = request.getParameter("single");
		if (StringUtil.trim(single).equals("true")) {
			func = (ModuleFunc)this.getCommandObject(request, ModuleFunc.class);
		} else {
			String json = request.getParameter("json");
			if (log.isInfoEnabled()) log.info("获取JSON参数：" + json);
			func = new ModuleFunc();
			func.setJson(json);
		}
		ServiceMsg sms = this.getService().updateModuleFunc(func, single);
		AjaxView view = new AjaxView(sms.isSuccess(), sms.getMessage());
		// 单个模块功能添加和更新时的返回其功能唯一ID
		if (sms.getObject("FuncId") != null) {
			view.setProperty("FuncId", sms.getObject("FuncId"));
		}
		return new ModelAndView(view);
	}
	
	/**
	 * 删除指定模块的指定功能（单个功能）.
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView removeFunc(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		// 获取模块ID和功能ID
		String mId = request.getParameter("mId");
		String fId = request.getParameter("funcId");
		ServiceMsg sms = this.getService().removeModuelFunc(mId, fId);
		return new ModelAndView(new AjaxView(sms.isSuccess(), sms.getMessage()));
	}
	
	/**
	 * 获取模块管理的业务处理类
	 * @return
	 */
	private ModuleService getService()
	{
		return (ModuleService)this.getServiceBean();
	}
	
	/**
	 * 设置模块（功能模块）的功能管理页面
	 * @param page
	 */
	public void setModuleFuncIndex(String page)
	{
		this.funcIndex = page;
	}
}
