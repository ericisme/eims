package cn.qtone.common.components.syspurview.check.aop.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 基础servlet，提供了简化的servlet处理方法，同时提供更多的实用 
 * 处理方法和数据获取方法.
 * 
 * @author 马必强
 * @version 1.0
 * 
 */
public abstract class BaseAbstractServlet extends HttpServlet
{
	private static ApplicationContext context; // spring的Bean获取Context
	
	private String contentType = "text/html;charset=GB2312";
	
	private String requestEncoding = "GB2312";
	
	protected Logger log = Logger.getLogger(BaseAbstractServlet.class);
	
	/**
	 * 从spring的bean定义文件中获取指定名称的Bean实例！
	 * @param beanName bean定义文件中的Bean名称
	 * @return
	 */
	protected final Object getBean(String beanName)
	{
		if (context == null) {
			context = WebApplicationContextUtils.
				getRequiredWebApplicationContext(this.getServletContext());
		}
		return context.getBean(beanName);
	}
	
	/**
	 * 获取指定Class的对象.<br>
	 * 注意此对象必须有无参构造方法！并且此对象必须是规范的JavaBean，系统
	 * 将使用set方式将参数设置到该Bean中。比如request中有参数name，那么系统
	 * 将调用指定对象的setName(String name)方法来设置该参数，如果setName具有
	 * 多个重载方法，那么系统将优先使用参数为字符串的方法。另外如果参数在进行类
	 * 型转换时出错，那么该参数将不被设置进该对象。
	 * 
	 * @param request 请求
	 * @param clazz 要设置的Bean对象的Class
	 * @return
	 * @throws Exception 
	 */
	protected final Object getCommandObject(HttpServletRequest request, Class clazz) 
			throws Exception
	{
		Map map = request.getParameterMap();
		Object obj = clazz.newInstance();
		BeanUtils.populate(obj, map);
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
	 * 获取当前WEB系统运行的物理路径.
	 */
	protected final String getRealPath()
	{
		return this.getServletContext().getRealPath("/");
	}

	/**
	 * 将指定的流输出到客户端.
	 */
	protected final void writeToClient(HttpServletResponse response, String html)
			throws IOException
	{
		response.setContentType(this.getContentType());
		PrintWriter out = response.getWriter();
		out.write(html);
		out.close();
		out = null;
	}
	
	/**
	 * 设置返回的内容类型！默认是:text/html;charset=gb2312
	 * @param contentType
	 */
	protected void setContentType(String contentType)
	{
		this.contentType = contentType;
	}
	
	/**
	 * 获取返回类容的类型！
	 * @return
	 */
	protected String getContentType()
	{
		return this.contentType;
	}
	
	/**
	 * 设置请求编码！默认是：GB2312
	 * @param encoding
	 */
	protected void setRequestEncoding(String encoding)
	{
		this.requestEncoding = encoding;
	}
	
	/**
	 * 获取servlet的请求编码！
	 * @return
	 */
	protected String getRequestEncoding()
	{
		return this.requestEncoding;
	}
	
	@Override
	public abstract void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException;
	
	@Override
	public abstract void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException;
}
