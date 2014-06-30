package cn.qtone.common.components.syspurview.check.filter.priviilege;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 当用户访问没有指定权限的资源时系统提示用户的操作接口.
 * 
 * @author 马必强
 * 
 */
public interface NoPurviewView
{
	/**
	 * 没有权限操作时的调用方法.
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void render(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;
}
