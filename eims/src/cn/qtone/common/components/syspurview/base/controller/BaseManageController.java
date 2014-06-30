package cn.qtone.common.components.syspurview.base.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.components.syspurview.base.PurviewConfiger;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.common.mvc.controller.spring.ManageController;

/**
 * 后台管理通用的Controller.
 * 该类定义了通用的操作方法，包括list、add等.
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public abstract class BaseManageController extends ManageController
{
	private String indexPage; // 列表首页页面
	
	private PurviewConfiger configer; // 权限系统的配置对象

	public PurviewConfiger getConfiger()
	{
		return configer;
	}

	public void setConfiger(PurviewConfiger configer)
	{
		this.configer = configer;
	}

	protected String getIndexPage()
	{
		return indexPage;
	}

	public void setIndexPage(String indexPage)
	{
		this.indexPage = indexPage;
		if (log.isInfoEnabled()) log.info("列表首页页面名称：" + this.indexPage);
	}
	
	/**
	 * 获取包含了用户信息Bean的map对象，key为user
	 * @param request
	 * @return
	 */
	public Map<String,Object> getMapWithUser(HttpServletRequest request)
	{
		Map<String,Object> map = this.getMap();
		map.put("user", UserUtil.getUserBean(request));
		return map;
	}
	
	/**
	 * 通用的查询.
	 */
	public abstract ModelAndView query(HttpServletRequest request,
			HttpServletResponse response) throws Exception;

}
