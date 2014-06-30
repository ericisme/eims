package cn.qtone.common.simplemvc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.qtone.common.base.share.AJAXPaginate;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.common.mvc.dao.Page;

/**
 * 简单mvc模型中的通用基本控制器基类.<br>
 * 
 * @author 贺少辉
 * @version 1.0
 *
 */
public abstract class SimpleController extends MultiActionController
{
	protected static Logger log = Logger.getLogger(SimpleController.class);
	
	private Object serviceBean; // 业务逻辑对象

	public Object getServiceBean()
	{
		return serviceBean;
	}

	public void setServiceBean(Object serviceBean)
	{
		this.serviceBean = serviceBean;
	}

	/**
	 * 根据request获取指定对象的实例.
	 */
	protected Object getCommandObject(HttpServletRequest request, Class clazz)
	{
		if (log.isInfoEnabled()) {
			log.info("转换request请求参数为对象[" + clazz.getName() + "]");
		}
		Map parameters = request.getParameterMap();
		Object obj = null;
		try {
			obj = clazz.newInstance();
			BeanUtils.populate(obj, parameters);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 获取当前页数.
	 * @param request
	 * @return
	 */
	protected int getCurrentPage(HttpServletRequest request, String pageName)
	{
		try {
			return Integer.parseInt(request.getParameter(pageName));
		} catch (NumberFormatException ex) {
			return 1;
		}
	}
	
	/**
	 * 获取当前的页数，使用默认的页名称page.
	 * @param request
	 * @return
	 */
	protected int getCurrentPage(HttpServletRequest request)
	{
		return getCurrentPage(request, "page");
	}
	
	/**
	 * 获取数据模型存储的map对象.
	 * @return
	 */
	protected Map<String,Object> getMap()
	{
		return new HashMap<String,Object>();
	}
	
	/**
	 * 获取包含了用户信息Bean的map对象，key为user
	 * @param request
	 * @return
	 */
	protected Map<String,Object> getMapWithUser(HttpServletRequest request)
	{
		Map<String,Object> map = this.getMap();
		map.put("user", UserUtil.getUserBean(request));
		return map;
	}
	
	/**
	 * 获取ajax形式的分页代码.分页方法为默认的jump
	 * @param request
	 * @return
	 */
	protected String getAjaxPage(HttpServletRequest request, int curPage, 
			Page page)
	{
		AJAXPaginate html = new AJAXPaginate(page.getTotals(), curPage, 
				page.getPageSize());
		html.setUrl(request);
		return html.getRoll();
	}
	
	/**
	 * 获取ajax形式的分页代码.使用自定义的分页方法来代替默认的jump
	 * @param request
	 * @return
	 */
	protected String getAjaxPage(HttpServletRequest request, int curPage, 
			Page page, String funcName)
	{
		AJAXPaginate html = new AJAXPaginate(page.getTotals(), curPage, 
				page.getPageSize());
		html.setUrl(request, funcName);
		return html.getRoll();
	}
}