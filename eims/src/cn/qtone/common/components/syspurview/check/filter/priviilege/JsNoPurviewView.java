package cn.qtone.common.components.syspurview.check.filter.priviilege;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 使用JS提示客户端没有操作权限，这也是系统默认的提示方式.
 * 
 * @author 马必强
 *
 */
public class JsNoPurviewView implements NoPurviewView
{

	public void render(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>");
		out.print("alert('对不起，您没有权限执行该操作！');");
		//out.print("window.history.back();");
		out.print("</script>");
		out.close();
		out = null;
	}

}
