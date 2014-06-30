package cn.qtone.common.components.syspurview.login;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.components.syspurview.core.system.SystemCache;
import cn.qtone.common.components.syspurview.core.system.domain.SystemBean;
import cn.qtone.common.components.syspurview.core.user.domain.AbstractUser;
import cn.qtone.common.mvc.view.spring.JsView;

/**
 * 用户信息获取的实用类.
 * 
 * @author 马必强
 * 
 */
public class UserUtil
{
	private final static Logger log = Logger.getLogger(UserUtil.class);
	
	/**
	 * 用户的session名称，存放在request中的关键字。
	 */
	public enum SessionName
	{
		AdminUser
	};

	/**
	 * 获取用户信息Bean.
	 * 
	 * @param req
	 * @return
	 */
	public static AbstractUser getUserBean(HttpServletRequest req)
	{
		return getSession(req);
	}

	/**
	 * 判断用户是否已经登陆.
	 * 
	 * @param req
	 * @return
	 */
	public static boolean isLogin(HttpServletRequest req)
	{
		return getSession(req) != null;
	}

	/**
	 * 设置用户的session
	 * @param req
	 * @param user
	 * @return
	 */
	public static boolean setSession(HttpServletRequest req, AbstractUser user)
	{
		if (user == null) return false;
		req.getSession().setAttribute(String.valueOf(SessionName.AdminUser),
				user);
		return true;
	}
	
	/**
	 * 同步各node的session.
	 * @param req
	 */
	public static void clusterSessionSyn(HttpServletRequest req, AbstractUser user)
	{
		setSession(req, user);
	}
	
	/**
	 * 用户退出时移除其session
	 * @param req
	 * @return
	 */
	public static void removeSession(HttpServletRequest req)
	{
		req.getSession().removeAttribute(String.valueOf(SessionName.AdminUser));
	}
	
	/**
	 * 获取用户登陆成功后转向的子系统的首页地址.
	 * @param user
	 * @return
	 */
	public static String getLoginIndexUrl(AbstractUser user)
	{
		String loginSys = user.getLoginSys();
		SystemCache sysCache = SystemCache.getInstance();
		SystemBean sys = sysCache.getSystem(loginSys);
		if (sys == null) sys = sysCache.getSystem(sysCache.getDefaultSystemId());
		return sys.getSysUrl();
	}
	
	/**
	 * 检查并判断当前用户是否可以登陆到某一子系统的首页地址.通常用户只能登陆到其所在用户组
	 * 可以访问的子系统，如果访问其他子系统则系统会提示。通常该方法用在需要进行控制的子系统
	 * 的入口方法中！
	 * @param user
	 * @param request
	 * @return
	 */
	public static ModelAndView checkAccess(AbstractUser user, HttpServletRequest request)
	{
		String sysUrl = UserUtil.getLoginIndexUrl(user);
		String currentUrl = request.getRequestURI() + (request.getQueryString() == null ? 
				"" :  ("?" + request.getQueryString()));
		if (log.isInfoEnabled()) log.info("[登陆子系统检测]应该登陆的子系统为：" + sysUrl 
				+ ",当前访问的地址为：" + currentUrl);
		if (!sysUrl.equalsIgnoreCase(currentUrl)) {
			return new ModelAndView(new JsView("您目前不能访问系统！", 
					sysUrl, 3));
		}
		return null;
	}

	/**
	 * 获取当前用户的session.
	 * 
	 * @param req
	 * @return
	 */
	private static AbstractUser getSession(HttpServletRequest req)
	{
		return (AbstractUser) req.getSession().getAttribute(
				String.valueOf(SessionName.AdminUser));
	}
}
