package cn.qtone.common.components.syspurview.check.filter.page;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.qtone.common.components.syspurview.check.filter.wrapper.CharArrayWrapper;
import cn.qtone.common.components.syspurview.check.page.html.HtmlFilter;
import cn.qtone.common.components.syspurview.check.page.html.HtmlFilterChain;
import cn.qtone.common.components.syspurview.check.page.html.auto.tag.AutoButtonFilter;
import cn.qtone.common.components.syspurview.check.page.html.auto.tag.AutoSpanFilter;
import cn.qtone.common.components.syspurview.check.page.html.simple.tag.SimpleButtonFilter;
import cn.qtone.common.components.syspurview.check.page.html.simple.tag.SimpleSpanFilter;
import cn.qtone.common.components.syspurview.core.user.domain.AbstractUser;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 页面级别的权限控制处理过滤器.<P>
 * 
 * 此过滤器是根据当前用户的session和其请求的URL地址进行其访问功能的页面
 * 级的权限过滤，比如按钮的过滤等。<P>
 * 
 * 另外，页面过滤器只针对指定的请求类型进行页面过滤，通常的如text/html和
 * text/plain等类型，这样的类型才是具有html代码的，需要进行过滤。而对一
 * 些图片或是下载等处理是不需要进行页面过滤，故过滤掉此类的页面的过滤设置！<P>
 * 
 * @author 马必强
 *
 */
public class PagePurviewFilter implements Filter
{
	private static final Logger log = Logger.getLogger(PagePurviewFilter.class);
	
	private final static String[] CONTENT_TYPE = {
		"text/html", "text/plain", "text/"
	};

	public void destroy()
	{
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain fc) throws IOException, ServletException
	{
		// 获取用户的session，如果没有登陆则忽略过滤
		HttpServletRequest httpReq = (HttpServletRequest) req;
		AbstractUser user = UserUtil.getUserBean(httpReq);
		
		// 如果没有设置过滤元素和当前请求地址不满足前缀设置，则默认是不进行过滤
		if (user == null || user.isSuperManager() || this.filterElements == null || 
				this.filterElements.length < 1 || !this.doFilterThisURL(httpReq.getRequestURI())) {
			if (log.isInfoEnabled()) log.info("[页面过滤器]对请求地址{" + httpReq.getRequestURL() + "}不进行页面过滤！");
			fc.doFilter(req, resp);
		} else {
			HttpServletResponse httpResp = (HttpServletResponse) resp;
			// 使用过滤器链来进行过滤
			HtmlFilterChain hfc = new HtmlFilterChain();
			for (String elt : this.filterElements) {
				switch (Element.valueOf(elt)) {
					case button :
						hfc.addFilter(new SimpleButtonFilter(httpReq), this.filtLevel);
						break;
					case span : 
						hfc.addFilter(new SimpleSpanFilter(httpReq), this.filtLevel);
						break;
					case autoButton : 
						hfc.addFilter(new AutoButtonFilter(httpReq), this.filtLevel);
						break;
					case autoSpan : 
						hfc.addFilter(new AutoSpanFilter(httpReq), this.filtLevel);
						break;
				}
			}
			
			// 使用CharArrayWrapper包装器进行输出过滤
			CharArrayWrapper wrapper = new CharArrayWrapper(httpResp);
			fc.doFilter(req, wrapper);
			if (!this.isTextHtmlContentType(resp)) return;
			String result = hfc.doFilter(wrapper.toString()); // 页面过滤结果
			PrintWriter out = httpResp.getWriter();
			out.write(result);
			out.close();
		}
	}
	
	/**
	 * 只对指定的contentType类型的响应进行页面过滤.<P>
	 * 如果响应类型为空，则默认是text/html，也进行页面过滤！
	 * @param resp
	 * @return
	 */
	private boolean isTextHtmlContentType(ServletResponse resp)
	{
		String contentType = StringUtil.trim(resp.getContentType()).toLowerCase();
		if (log.isDebugEnabled()) log.debug("[页面过滤器]当前请求的响应类型为：" + contentType);
		if (contentType.equals("")) return true;
		for (String content : CONTENT_TYPE) {
			if (contentType.startsWith(content)) return true;
		}
		return false;
	}
	
	/**
	 * 判断是否对当前的URL地址进行过滤.
	 * @param uri
	 * @return
	 */
	private boolean doFilterThisURL(String uri)
	{
		if (this.prefix == null) return true;
		for (String prefix : this.prefix) {
			if (uri.startsWith(prefix)) return true;
		}
		return false;
	}

	public void init(FilterConfig cfg) throws ServletException
	{
		// 首先获取location来判断是否指定了文件，如果指定了则优先使用属性文件来配置
		String location = StringUtils.trimToEmpty(cfg.getInitParameter("location"));
		String[] value = null;
		if (location.equals("")) value = this.initByParam(cfg);
		else value = this.initByFile(cfg, location);
		
		this.initConfigParam(value);
		
		if (log.isInfoEnabled()) {
			log.info("\n==========页面权限过滤器可用！==========\n" 
					+ "要执行过滤的URL前缀是：[" + StringUtils.join(this.prefix, ",") + "]\n"
					+ "要过滤的页面元素有：[" + StringUtils.join(this.filterElements, ",") + "]\n"
					+ "过滤的级别是：[" + String.valueOf(this.filtLevel) + "]\n");
		}
	}

	/**
	 * 初始化系统参数
	 */
	private void initConfigParam(String[] value)
	{
		// 要执行页面权限过滤的URL前缀
		if (value[0].intern() != "") this.prefix = value[0].split("\\s+");
		
		// 要过滤的页面元素，目前是button和span,多个请使用空格分开
		if (value[1].intern() != "") this.filterElements = value[1].split("\\s+");
		
		// 获取过滤的级别
		if (value[2].equals("disabled")) this.filtLevel = HtmlFilter.Filter.DISABLLED;
		else if (value[2].equals("hidden")) this.filtLevel = HtmlFilter.Filter.HIDDEN;
		else if (value[2].equals("delete")) this.filtLevel = HtmlFilter.Filter.DELETE;
	}

	/**
	 * 根据配置参数（非属性文件）初始化参数来进行系统的必要配置.
	 * 
	 * @param cfg
	 * @throws ServletException
	 */
	private String[] initByParam(FilterConfig cfg) throws ServletException
	{
		if (log.isInfoEnabled()) log.info("根据过滤器参数初始化页面过滤器参数！");
		String[] value = new String[3];
		value[0] = StringUtils.trimToEmpty(cfg.getInitParameter("prefix"));
		value[1] = StringUtils.trimToEmpty(cfg.getInitParameter("elements"));
		value[2] = StringUtils.trimToEmpty(cfg.getInitParameter("level")).toLowerCase();
		return value;
	}

	/**
	 * 根据属性文件来进行初始化系统参数.
	 * 
	 * @param cfg
	 * @return
	 * @throws ServletException
	 */
	private String[] initByFile(FilterConfig cfg, String file)
	{
		if (log.isInfoEnabled()) log.info("根据配置文件初始化页面过滤器参数！");
		String[] value = new String[3];
		FileInputStream fis = null;
		Properties p = new Properties();
		try {
			fis = new FileInputStream(cfg.getServletContext()
					.getRealPath("/") + file);
			p.load(fis);
			value[0] = StringUtils.trimToEmpty(p.getProperty("prefix"));
			value[1] = StringUtils.trimToEmpty(p.getProperty("elements"));
			value[2] = StringUtils.trimToEmpty(p.getProperty("level")).toLowerCase();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				if (fis != null) fis.close();
			} catch (Exception ex) {}
		}
		return value;
	}
	
	private String[] prefix; // 要执行过滤的URL地址前缀
	
	private String[] filterElements; // 需要过滤的页面元素
	
	// 页面过滤的级别，不可用、隐藏还是删除，默认是删除
	private HtmlFilter.Filter filtLevel = HtmlFilter.Filter.DELETE; 
	
	private static enum Element {
		button, span, autoButton, autoSpan
	}; // 要过滤的页面元素类型
}
