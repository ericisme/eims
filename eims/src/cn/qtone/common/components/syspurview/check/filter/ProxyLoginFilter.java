package cn.qtone.common.components.syspurview.check.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeList;

import cn.qtone.common.components.syspurview.check.filter.priviilege.PriviilegeFilter;
import cn.qtone.common.components.syspurview.check.filter.wrapper.CharArrayWrapper;
import cn.qtone.common.components.syspurview.core.user.domain.AbstractUser;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 对于使用代理登陆的用户要在页面上显示浮动条提示用户是在进行
 * 代理操作，同时也提供按钮便于其快速切回到原先的系统.<P>
 * 
 * @author 马必强
 *
 */
public class ProxyLoginFilter implements Filter
{
	private static final Logger log = Logger.getLogger(PriviilegeFilter.class);
	
	public void destroy()
	{
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain fc) throws IOException, ServletException
	{
		HttpServletRequest httpReq = (HttpServletRequest) req;
		// 如果用户没有登陆或是不是代理登陆则忽略
		AbstractUser user = UserUtil.getUserBean(httpReq);
		if (user == null || user.getProxyUserId() == -1) {
			fc.doFilter(req, resp);
			return;
		}
		// 如果不是指定的URL前缀则也忽略
		String uri = StringUtil.trim(httpReq.getRequestURI());
		String query = StringUtil.trim(httpReq.getQueryString()).intern();
		String url = uri + (query == "" ? "" : ("?" + query));
		if (log.isInfoEnabled()) log.info("[代理浮动条设置]当前请求的URL地址为:" + url);
		if (this.url == null || !this.isInclude(url)) {
			fc.doFilter(req, resp);
			return;
		}
		// 执行页面浮动条设置
		CharArrayWrapper wrapper = new CharArrayWrapper((HttpServletResponse)resp);
		fc.doFilter(req, wrapper);
		PrintWriter out = resp.getWriter();
		out.write(this.addFloatContent(wrapper.toString(), user));
		out.close();
		out = null;
	}

	public void init(FilterConfig cfg) throws ServletException
	{
		// 如果指定了配置文件则优先使用配置文件进行设置
		String location = StringUtils.trimToEmpty(cfg.getInitParameter("location"));
		String[] value = null;
		if (location.equals("")) value = this.initByParam(cfg);
		else value = this.initByFile(cfg, location);
		this.initConfigParam(value);
		// 初始化模板文件的绝对路径和模板内容
		this.floatLayerTplAbsPath = cfg.getServletContext().getRealPath("/") + this.floatLayerTpl;
		this.loadFloatLayerTpl();

		if (log.isInfoEnabled()) {
			log.info("\n==========代理登陆页面控制过滤器可用！==========\n"
					+ "进行浮动条设置的URL是：[" + StringUtil.join(this.url, ",")
					+ "]\n浮动条的模板文件路径是：[" + this.floatLayerTpl
					+ "]\n是否是调试模式：[" + this.isDebug + "]\n\n");
		}
	}
	
	/**
	 * 给指定的html代码加上浮动条的设置后返回.
	 * @param html
	 * @return
	 */
	private String addFloatContent(String html, AbstractUser user)
	{
		try {
			// 首先获取body节点
			Parser parser = Parser.createParser(html, "GB2312");
			NodeList list = parser.parse(new TagNameFilter("body"));
			if (list == null || list.size() != 1) {
				log.warn("[代理浮动条设置]******该页面不包含body节点或有多个body节点,无法解析!");
				return html;
			}
			// 添加浮动条设置
			Node body = list.elementAt(0);
			body.getChildren().add(new TextNode(this.replaceHoldPlace(user)));
			Node root = body;
			while (root.getParent() != null) {
				root = root.getParent();
			}
			return root.toHtml();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * 判断指定的URI地址是否在要设置浮动条的前缀URL里面.
	 * @param url
	 * @return
	 */
	private boolean isInclude(String url)
	{
		if (this.url == null || this.url.length < 1) return false;
		for (String exUrl : this.url) {
			if (exUrl.trim().length() <= 1) continue;
			if (exUrl.endsWith("*")) {
				String tmp = exUrl.substring(0, exUrl.length() - 1);
				if (url.startsWith(tmp)) return true;
			} else if (url.equalsIgnoreCase(exUrl)) return true;
		}
		return false;
	}

	/**
	 * 根据配置参数（非属性文件）初始化参数来进行系统的必要配置.
	 */
	private String[] initByParam(FilterConfig cfg) throws ServletException
	{
		if (log.isInfoEnabled()) log.info("[代理浮动条设置]根据过滤器参数初始化参数！");
		String[] value = new String[3];
		value[0] = StringUtils.trimToEmpty(cfg.getInitParameter("proxyLogin.prefix"));
		value[1] = StringUtils.trimToEmpty(cfg.getInitParameter("proxyLogin.floatLayerTpl"));
		value[2] = StringUtils.trimToEmpty(cfg.getInitParameter("proxyLogin.debug"));
		return value;
	}

	/**
	 * 根据文件来进行初始化系统参数.
	 */
	private String[] initByFile(FilterConfig cfg, String file)
	{
		if (log.isInfoEnabled()) log.info("[代理浮动条设置]根据配置文件初始化参数！");
		String[] value = new String[3];
		FileInputStream fis = null;
		Properties p = new Properties();
		try {
			fis = new FileInputStream(cfg.getServletContext()
					.getRealPath("/") + file);
			p.load(fis);
			value[0] = StringUtils.trimToEmpty(p.getProperty("proxyLogin.prefix"));
			value[1] = StringUtils.trimToEmpty(p.getProperty("proxyLogin.floatLayerTpl"));
			value[2] = StringUtils.trimToEmpty(p.getProperty("proxyLogin.debug"));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				if (fis != null) fis.close();
			} catch (Exception ex) {}
		}
		return value;
	}
	
	private void initConfigParam(String[] value)
	{
		// 要进行权限过滤的URL前缀
		if (value[0].intern() != "") this.url = value[0].split("\\s+");
		if (value[1].intern() != "") this.floatLayerTpl = value[1];
		if (value[2].equalsIgnoreCase("true")) this.isDebug = true;
	}
	
	/**
	 * 加载指定的模板文件，并读取其内容.<P>
	 * 注意,模板文件中有几处是需要进行替换的,相应的标签信息及其含义如下:
	 * %curUserName%     -   当前所代理用户的真实姓名
	 * %proxyUserName%   -   代理人的用户真实姓名
	 *
	 */
	private void loadFloatLayerTpl()
	{
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					new File(this.floatLayerTplAbsPath)), "UTF-8"));
			StringBuilder buf = new StringBuilder();
			String tmp = null;
			while((tmp = br.readLine()) != null) buf.append(tmp + "\r\n");
			this.tplContent = buf.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				if (br != null) br.close();
			} catch (Exception ex) {}
		}
	}
	
	/**
	 * 使用真实的信息替换模板文件中的占位符.
	 * @param user
	 * @return
	 */
	private String replaceHoldPlace(AbstractUser user)
	{
		if (this.isDebug) this.loadFloatLayerTpl(); // 如果是调试则重新加载模板
		String tmp = this.tplContent;
		tmp = tmp.replaceAll(holdPlace[0], user.getRealName());
		tmp = tmp.replaceAll(holdPlace[1], user.getProxyUserName());
		return tmp;
	}
	
	private String[] url; // 要进行设置浮动条的URL
	
	private String floatLayerTpl; // 浮动条模板文件的路径信息(配置文件中的相对路径)
	
	private String floatLayerTplAbsPath; // 模板文件的绝对路径信息，只初始化一次

	private String tplContent; // 模板文件的内容
	
	private boolean isDebug = false; // 是否是调试方式，是则每次读取模板文件内容
	
	private final static String[] holdPlace = { "%curUserName%", "%proxyUserName%" };
}
