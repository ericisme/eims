package cn.qtone.common.mvc.controller.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import cn.qtone.common.base.share.AJAXPaginate;
import cn.qtone.common.mvc.dao.Page;

/**
 * 普通的servelt管理基类，提供了通用的处理方法.
 * 
 * @author 马必强
 *
 */
public abstract class BaseAbstractServlet extends HttpServlet
{
	protected static Logger log = Logger.getLogger(BaseAbstractServlet.class);
	
	private String contentType = "text/html;charset=GB2312";
	
	private String requestEncoding = "GB2312";
	
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
