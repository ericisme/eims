package cn.qtone.common.components.syspurview.check.aop.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.qtone.common.components.syspurview.check.aop.exception.PurviewVerifyException;
import cn.qtone.common.components.syspurview.check.aop.proxy.ProxyFactory;
import cn.qtone.common.components.syspurview.core.user.domain.AbstractUser;
import cn.qtone.common.components.syspurview.login.UserUtil;

/**
 * 后台需要身份认证的servlet.<br>
 * 所有需要进行身份认证和权限判断的都应该继承该类来执行操作。
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public abstract class AdminAbstractServlet extends BaseAbstractServlet
{
	private PurviewVerifyException pve;
	
	@Override
	public final void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doPost(request, response);
	}
	
	@Override
	public final void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		try {
			if (!this.isLogin(request)) {
				this.doNoLogin(request, response);
				return;
			}
			request.setCharacterEncoding(this.getRequestEncoding());
			doRequest(request, response);
		} catch (Exception ex) {
			if (ex instanceof PurviewVerifyException) {
				if (log.isDebugEnabled()) {
					log.debug(ex.getMessage());
				}
				try {
					doNoPurview(request, response, (PurviewVerifyException)ex);
				} catch (Exception ex1) {
					throw new RuntimeException(ex1);
				}
			} else {
				throw new RuntimeException(ex);
			}
		}
	}
	
	/**
	 * 没有权限操作时调用的方法，设置了权限校验错误的异常类！<br>
	 * 那么在子类的doNoPurview()中可以直接获取异常信息（校验不通过）
	 */
	protected final void doNoPurview(HttpServletRequest request,
			HttpServletResponse response, PurviewVerifyException pve)
			throws Exception
	{
		this.pve = pve;
		doNoPurview(request, response);
	}
	
	/**
	 * 返回没有权限操作的角色的唯一ID.
	 * @return
	 */
	protected int[] getRoleId()
	{
		return this.pve == null ? new int[0] : this.pve.getRoleId();
	}
	
	/**
	 * 返回没有权限操作的资源名称（类名称+方法名称）.
	 * @return
	 */
	protected String getResourceName()
	{
		return this.pve == null ? null : this.pve.getResourceName();
	}
	
	/**
	 * 返回没有权限操作的操作字符串表示.
	 * @return
	 */
	protected String getOperate()
	{
		return this.pve == null ? null : this.pve.getOperate();
	}
	
	/**
	 * 返回没有权限操作的异常提示信息！
	 * @return
	 */
	protected String getMessage()
	{
		return this.pve == null ? null : this.pve.getMessage();
	}

	/**
	 * 获取指定Class的业务处理类的代理类的实例.
	 * 使用权限系统本身的动态代理方式来实现业务类的代理。
	 */
	protected final Object getBusinessObject(Class clazz, AbstractUser userBean)
	{
		return ProxyFactory.getInstance().getProxy(clazz, userBean);
	}
	
	/**
	 * 获取指定的业务处理类的代理类的实例.
	 * 使用此方法获取，必须显示的指定业务处理类实现的业务接口！
	 */
	protected final Object getBusinessObject(Class clazz, Class boInterfaceClass, 
			AbstractUser userBean)
	{
		return ProxyFactory.getInstance().getProxy(clazz, boInterfaceClass,
				userBean);
	}

	/**
	 * 返回用户的信息Bean.
	 */
	protected final AbstractUser getUserSession(HttpServletRequest request)
	{
		return UserUtil.getUserBean(request);
	}
	
	/**
	 * 判断用户是否已经登陆.
	 * @return 返回true表示已登陆，false则表示没有登陆
	 */
	private boolean isLogin(HttpServletRequest request)
	{
		return this.getUserSession(request) != null;
	}

	/**
	 * 请求处理，任何get或post请求都将会调用该方法！
	 * 该方法是抽象方法，任何子类必须实现该方法的实际处理。
	 */
	protected abstract void doRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	/**
	 * 当用户没有操作权限时的处理方法！这个也是子类必须实现的方法。
	 */
	protected abstract void doNoPurview(HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	/**
	 * 用户没有登陆时的处理方法.
	 * 权限系统默认所有继承该类的servlet都是需要认证才能进行访问，这样在每个
	 * servlet真正进行处理前将会进行身份验证，那么继承该类的servlet也需要
	 * 实现用户没有登陆时的处理方法！
	 */
	protected abstract void doNoLogin(HttpServletRequest request,
			HttpServletResponse response) throws Exception;
}
