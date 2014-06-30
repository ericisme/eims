package cn.qtone.common.mvc.controller.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通用的普通servlet,任何不需要身份验证的处理操作应该继承此类进行处理。
 * 
 * @author 马必强
 *
 */
public abstract class NormalServlet extends BaseAbstractServlet
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
