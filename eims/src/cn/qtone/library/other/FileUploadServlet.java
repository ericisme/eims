package cn.qtone.library.other;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.qtone.common.utils.fileupload.FileUploadFactory;
import cn.qtone.common.utils.fileupload.FileUploadInter;

 
/**
 * mupload文件上传servlet.
 * @author 贺少辉.
 * @version 1.0.
 */
@SuppressWarnings("serial")
public class FileUploadServlet extends HttpServlet
{
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		response.setCharacterEncoding("UTF-8");   
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		UploadPath c = new UploadPath();
		String path = c.getUploadPath();
		FileUploadInter upload = FileUploadFactory.getUploadInstance(request, path);
		upload.upload();
		String uploadFlag =upload.getParameter("uploadFlag");
		String url = upload.getFilePath();
		String filename = upload.getOriginalName();
		//filename=new String(filename.getBytes("gbk"),"utf-8");
		filename = filename.substring(filename.lastIndexOf("\\")+1,filename.length());
		String ems = "";
		if("schoolUpload".equals(uploadFlag) 
				|| "studentUpload".equals(uploadFlag)
				||"msgSendingUpload".equals(uploadFlag)
				|| "parentUpload".equals(uploadFlag)){
			ems = "<script>window.history.back();parent.document.getElementById('importName').value='"+filename
			+"';parent.document.getElementById('importattach_path').value='"+url
			+"';parent.document.getElementById('F1').click()</script>";
		}
		PrintWriter out = response.getWriter();
		out.print(ems);
		out.flush();
		out.close();
		out = null;
			
	}
}
