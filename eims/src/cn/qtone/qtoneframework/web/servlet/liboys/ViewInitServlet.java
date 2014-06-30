package cn.qtone.qtoneframework.web.servlet.liboys;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;

import cn.qtone.qtoneframework.web.view.liboys.JspTemplateUtil;
import cn.qtone.qtoneframework.web.view.velocity.VelocityHelper;

public class ViewInitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected Log log;

	/**
	 * 初始化Liboys模版替换的上下文路径
	 * 
	 * @author 陈倩
	 */
	public void init() {
		try {
			super.init();
			String strRealPath = this.getServletContext().getRealPath("/");
			JspTemplateUtil.setContextPath(strRealPath);
			
			//VelocityHelper上下文初始化
			VelocityHelper.setCONTEXT_PATH(strRealPath);
			log.info("VelocityHelper contextPath: " + strRealPath);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}