package cn.qtone.qtoneframework.common.utils.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * spring应用上下文工具类
 * 
 * @author 张但
 *
 */
public class SpringAppContextUtil {

	/**
	 * 通过bean的名称取得bean实例
	 * @param request
	 * @param name
	 * @return
	 */
	public static Object  getBean(HttpServletRequest request, String name) {
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
		return ctx.getBean(name);
	}
}
