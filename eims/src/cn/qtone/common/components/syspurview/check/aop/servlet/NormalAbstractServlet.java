package cn.qtone.common.components.syspurview.check.aop.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 不需要进行登陆和权限等身份控制的servlet.<br>
 * 如果某些servlet不需要进行身份验证，那么应该继承该类来执行操作.
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public abstract class NormalAbstractServlet extends BaseAbstractServlet
{
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
			request.setCharacterEncoding(this.getRequestEncoding());
			doRequest(request, response);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 请求处理，任何get或post请求都将会调用该方法！
	 * 该方法是抽象方法，任何子类必须实现该方法的实际处理。
	 */
	protected abstract void doRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception;
}
