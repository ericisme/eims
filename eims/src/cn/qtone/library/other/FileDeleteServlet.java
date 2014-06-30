package cn.qtone.library.other;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 
/**
 * mupload文件删除servlet.
 * @author 王培森.
 * @version 1.0.
 */
@SuppressWarnings("serial")
public class FileDeleteServlet extends HttpServlet
{
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String fileName = request.getParameter("filepath");
		if (fileName != null && !"null".equals(fileName)
				&& !"".equals(fileName)) {
			File file = new File(this.getServletContext().getRealPath("/")
					+ fileName);
			if (!file.delete()) {
				throw new RuntimeException("删除文件:"
						+ this.getServletContext().getRealPath("/") + fileName);
			}
		}
	}
}
