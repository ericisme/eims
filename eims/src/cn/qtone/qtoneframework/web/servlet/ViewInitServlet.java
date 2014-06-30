package cn.qtone.qtoneframework.web.servlet;

import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.qtone.qtoneframework.web.view.liboys.JspTemplateUtil;
import cn.qtone.qtoneframework.web.view.velocity.VelocityHelper;

public class ViewInitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	Log log = LogFactory.getLog(this.getClass());

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
//			VelocityHelper上下文初始化
			VelocityHelper.setCONTEXT_PATH(strRealPath);
			log.info("VelocityHelper contextPath: " + strRealPath);		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}