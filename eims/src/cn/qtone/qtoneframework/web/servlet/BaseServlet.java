package cn.qtone.qtoneframework.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.qtone.qtoneframework.log.PrintUtil;
import cn.qtone.qtoneframework.web.view.ErrorView;

/**
 * 此类初始化了spring上下文，继承此类的子类可以直接用getBean()方法
 * 取出已经在spring容器中注册的bean。
 * 
 * @author 林子龙
 */
public abstract class BaseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static ApplicationContext ctx = null;

	protected String contextPath;

	protected Log log;
	
	public static PrintUtil pu;

	private ErrorView errorView;
	
	/**
	 * @param name
	 *            spring配置文件中对应bean的id
	 * @return 此Id的对像
	 */
	public Object getBean(String name) {
		if (ctx == null) {
			ctx = WebApplicationContextUtils
					.getRequiredWebApplicationContext(this.getServletContext());
		}
		return ctx.getBean(name);
	}

	@Override
	public void init() throws ServletException {
		super.init();
		contextPath = getServletContext().getRealPath("/")+"/";
		log = LogFactory.getLog(getClass());
		pu=new PrintUtil();
		errorView = (ErrorView) getBean("qtfErrorView");
	}

	public ErrorView getErrorView() {
		return errorView;
	}
}
