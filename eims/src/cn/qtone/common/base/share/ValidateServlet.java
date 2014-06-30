package cn.qtone.common.base.share;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.qtone.common.utils.base.RandomGraphic;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 图形随机验证码！ 认证码绑定Session名字：rand
 */
public class ValidateServlet extends HttpServlet
{
	public final static String ATTR_NAME = "rand";
	
	private static final long serialVersionUID = 4603635282836324648L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		response.setHeader("Pragma", "No-cache"); // 设置页面不缓存
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		// 岁机码类型 num-数字  char-字母  空或其他-字母和数字混合
		String type = StringUtil.trim(request.getParameter("type")).intern();
		// 随机码的长度，默认是6个
		int len = StringUtil.parseInt(request.getParameter("len"), 6);
		if (len <= 0) len = 6;
		getImage(request, response, type, len);
	}

	protected void getImage(HttpServletRequest req, HttpServletResponse res,
			String type, int len)
			throws IOException, ServletException
	{
		res.setContentType("image/jpg");
		try {
			RandomGraphic graphic = RandomGraphic.getInstance(len);
			String code = null;
			if (type == "num") {
				code = graphic.drawNumber(RandomGraphic.GRAPHIC_JPEG, 
						res.getOutputStream());
			} else if (type == "char") {
				code = graphic.drawAlpha(RandomGraphic.GRAPHIC_JPEG, 
						res.getOutputStream());
			} else {
				OutputStream os = res.getOutputStream();
				code = graphic.drawNumberAndAlpha(RandomGraphic.GRAPHIC_JPEG, os);
				if (os != null) os.close();
			}
			// 测试，修改code为000000
			//code = "000000";
			req.getSession().setAttribute(ATTR_NAME, code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
