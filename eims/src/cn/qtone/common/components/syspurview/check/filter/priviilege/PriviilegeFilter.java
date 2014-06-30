package cn.qtone.common.components.syspurview.check.filter.priviilege;

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

import cn.qtone.common.components.syspurview.core.user.domain.AbstractUser;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.common.components.syspurview.login.domain.PurviewMap;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 资源的操作权限控制过滤器.
 * 
 * @author 马必强
 * 
 */
public class PriviilegeFilter implements Filter
{
	private static final Logger log = Logger.getLogger(PriviilegeFilter.class);

	public void destroy()
	{
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain fc) throws IOException, ServletException
	{
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpServletResponse httpResp = (HttpServletResponse) resp;
		String uri = httpReq.getRequestURI();
		String url = uri == null ? "" : uri + "?" + httpReq.getQueryString();
		// 如果不具有指定的前缀或是已经被指定为排除，则不进行登陆和权限验证
		if (!this.isRightPrefix(uri) || this.isExclude(url, false)) {
			if (log.isDebugEnabled()) log.debug("[权限过滤器]对地址{" + url + "}不进行权限验证！");
			fc.doFilter(req, resp);
			return;
		}

		// 凡是进行权限验证的必须先登陆，没有登陆的，提示先登陆
		if (!this.isLogin(httpReq)) {
			if (log.isDebugEnabled()) log.debug("[权限过滤器]该用户目前还没有登陆，定位到登陆页面！");
			this.reLogin(httpResp);
			return;
		}

		// 过滤不需要进行权限验证但是需要进行登陆验证的URL地址
		if (this.isExclude(uri, true)) {
			if (log.isDebugEnabled()) log.debug("[权限过滤器]忽略对地址{" + uri + "}的权限控制！");
			fc.doFilter(req, resp);
			return;
		}

		// 获取用户的session
		AbstractUser user = UserUtil.getUserBean(httpReq);
		String operate = this.getOperate(httpReq);
		if (operate == null || operate.trim().intern() == "") {
			operate = DefaultPropValue;
		}
		if (log.isInfoEnabled()) log.info("[权限过滤器]用户请求对资源[" + uri + "]进行["
				+ operate + "]操作！");
		if (this.isAllowd(user, uri, operate)) {
			fc.doFilter(req, resp);
		} else {
			NoPurviewView view = ViewFactory.getViewInstance(this.viewClass);
			view.render(httpReq, httpResp);
		}
	}

	/**
	 * 获取当前请求的URL地址中的操作名称.
	 * 
	 * @param httpReq
	 * @return
	 */
	private String getOperate(HttpServletRequest httpReq)
	{
		String operate = httpReq.getParameter(DefaultPropName);
		if (operate != null) return operate;
		if (this.propName == null || this.propName.length < 1) return null;
		for (int i = 0; i < this.propName.length; i++) {
			operate = httpReq.getParameter(this.propName[i]);
			if (operate != null) return operate;
		}
		return null;
	}

	public void init(FilterConfig cfg) throws ServletException
	{
		// 如果指定了配置文件则优先使用配置文件进行设置
		String location = StringUtils.trimToEmpty(cfg.getInitParameter("location"));
		String[] value = null;
		if (location.equals("")) value = this.initByParam(cfg);
		else value = this.initByFile(cfg, location);
		this.initConfigParam(value);

		if (log.isInfoEnabled()) {
			log.info("\n==========操作权限判断过滤器可用！==========\n"
					+ "进行权限过滤的URL前缀是：[" + StringUtil.join(this.prefix, ",") + "]\n"
					+ "动作参数名称列表是：[" + DefaultPropName + "," + StringUtil.join(propName, ",") + "]\n"
					+ "动作参数默认值是：[" + DefaultPropValue + "]\n"
					+ "没有权限的提示视图类是：[" + this.viewClass + "]\n"
					+ "不需要进行登陆验证和权限验证的URL地址：[" + StringUtil.join(excludeUrl, ",") + "]\n"
					+ "系统登陆地址是：[" + this.loginUrl + "]\n"
					+ "需要登陆但无须权限验证的URL地址是：[" + StringUtil.join(excludeButLoginUrl, ",") + "]\n");
		}
	}

	/**
	 * 根据配置参数（非属性文件）初始化参数来进行系统的必要配置.
	 * 
	 * @param cfg
	 * @throws ServletException
	 */
	private String[] initByParam(FilterConfig cfg) throws ServletException
	{
		if (log.isInfoEnabled()) log.info("根据过滤器参数初始化权限系统参数！");
		String[] value = new String[6];
		value[0] = StringUtils.trimToEmpty(cfg.getInitParameter("prefix"));
		value[1] = StringUtils.trimToEmpty(cfg.getInitParameter("exclude"));
		value[2] = StringUtils.trimToEmpty(cfg.getInitParameter("excludeButLogin"));
		value[3] = StringUtils.trimToEmpty(cfg.getInitParameter("propName"));
		value[4] = StringUtils.trimToEmpty(cfg.getInitParameter("viewClass"));
		value[5] = StringUtils.trimToEmpty(cfg.getInitParameter("loginUrl"));
		return value;
	}

	/**
	 * 根据文件来进行初始化系统参数.
	 * 
	 * @param cfg
	 * @return
	 * @throws ServletException
	 */
	private String[] initByFile(FilterConfig cfg, String file)
	{
		if (log.isInfoEnabled()) log.info("根据配置文件初始化权限系统参数！");
		String[] value = new String[6];
		FileInputStream fis = null;
		Properties p = new Properties();
		try {
			fis = new FileInputStream(cfg.getServletContext()
					.getRealPath("/") + file);
			p.load(fis);
			value[0] = StringUtils.trimToEmpty(p.getProperty("prefix"));
			value[1] = StringUtils.trimToEmpty(p.getProperty("exclude"));
			value[2] = StringUtils.trimToEmpty(p.getProperty("excludeButLogin"));
			value[3] = StringUtils.trimToEmpty(p.getProperty("propName"));
			value[4] = StringUtils.trimToEmpty(p.getProperty("viewClass"));
			value[5] = StringUtils.trimToEmpty(p.getProperty("loginUrl"));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				if (fis != null) fis.close();
			} catch (Exception ex) {}
		}
		return value;
	}

	/**
	 * 初始化系统参数
	 * 
	 * @param cfg
	 * @throws ServletException
	 */
	private void initConfigParam(String[] value)
	{
		// 要进行权限过滤的URL前缀
		if (value[0].intern() != "") prefix = value[0].split("\\s+");

		// 被排除的不需要进行登陆和权限验证的URL地址
		if (value[1].intern() != "") excludeUrl = value[1].split("\\s+");

		// 被排除不需要进行权限验证但需要进行登陆验证的URL地址
		if (value[2].intern() != "") excludeButLoginUrl = value[2]
				.split("\\s+");

		// 判断权限时URL地址中表示操作的参数名称，默认是action
		if (value[3].intern() != "") propName = value[3].split("\\s+");

		// 没有权限的提示类的类路径
		if (value[4].intern() != "") this.viewClass = value[4];

		// 登陆地址
		this.loginUrl = value[5];
	}

	/**
	 * 权限检测.
	 * 
	 * @param roleId
	 * @param resource
	 */
	public static boolean isAllowd(AbstractUser user, String uri, String operate)
	{
		if (user.isSuperManager()) return true;
		PurviewMap map = user.getPurviewMap();
		if (map == null) return false;
		return map.isAllowed(uri, operate);
	}

	/**
	 * 判断当前的URL地址是否需要进行权限过滤
	 * 
	 * @param uri
	 * @return
	 */
	private boolean isRightPrefix(String uri)
	{
		if (this.prefix == null) return false;
		for (String pfx : this.prefix) {
			if (uri.startsWith(pfx)) return true;
		}
		return false;
	}

	/**
	 * 判断用户是否已登陆系统
	 * 
	 * @param req
	 * @return
	 */
	private boolean isLogin(HttpServletRequest req)
	{
		return UserUtil.isLogin(req);
	}

	/**
	 * 提示用户需要登陆.
	 * 
	 * @param resp
	 * @throws IOException
	 */
	private void reLogin(HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		out.print("<script language='javascript'>");
		out.print("alert('您还没有登陆系统或帐号闲置时间过长，请重新登陆！');");
		out.print("parent.parent.location.href='" + this.loginUrl + "';");
		out.print("</script>");
		out.close();
		out = null;
	}

	/**
	 * 判断当前路径是否是被排除在外，不需要进行权限验证的路径. 如果设置的某一个路径最后
	 * 是以*结束的，那表示所有以这个路径开头的都是不需要进行权限验证的！
	 * 
	 * @param uri
	 * @return
	 */
	private boolean isExclude(String uri, boolean login)
	{
		String[] exclude = login ? this.excludeButLoginUrl : this.excludeUrl;
		if (exclude == null) return false;
		for (String exUrl : exclude) {
			if (exUrl.trim().length() <= 1) continue;
//			System.out.println(exUrl + " ------ " + uri );
			if (exUrl.endsWith("*")) {
				String tmp = exUrl.substring(0, exUrl.length() - 1);
				if (uri.startsWith(tmp)) return true;
			} else if (uri.equalsIgnoreCase(exUrl)) return true;
		}
		return false;
	}

	private String[] prefix; // 需要进行权限控制的URL前缀

	private String[] propName; // 用户设置的URL中代表操作的名称，系统将优先使用默认的名称

	private String viewClass; // 没有权限操作的视图提示类，实现了NoPurviewView接口的类

	private String[] excludeUrl; // 被排除的不需要验证登陆和权限的地址

	private String[] excludeButLoginUrl; // 被排除的不需要进行权限验证但需要登陆的URL地址

	private String loginUrl; // 登陆地址

	private static String DefaultPropName = "action"; // URL地址中代表操作默认名称

	private static String DefaultPropValue = "list"; // 默认的操作名称
}
