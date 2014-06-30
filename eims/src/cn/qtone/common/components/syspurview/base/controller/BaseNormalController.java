package cn.qtone.common.components.syspurview.base.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.qtone.common.components.syspurview.base.PurviewConfiger;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.common.mvc.controller.spring.NormalController;

/**
 * 权限系统的一般控制器基类。
 * 
 * @author 马必强
 *
 */
public class BaseNormalController extends NormalController
{
	private PurviewConfiger configer; // 权限系统的配置对象

	public PurviewConfiger getConfiger()
	{
		return configer;
	}

	public void setConfiger(PurviewConfiger configer)
	{
		this.configer = configer;
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
}
