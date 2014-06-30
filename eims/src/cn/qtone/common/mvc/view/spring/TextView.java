package cn.qtone.common.mvc.view.spring;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.View;

/**
 * 使用spring的ModelAndView向客户端返回普通的文本字符串.
 * 
 * @author 马必强
 * 
 */
public class TextView implements View
{
	private String tips;

	public TextView(String text)
	{
		this.tips = StringUtils.trimToEmpty(text);
	}

	public String getContentType()
	{
		return "text/plain;charset=UTF-8";
	}

	public void render(Map arg0, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		response.setContentType(this.getContentType());
		PrintWriter out = response.getWriter();
		out.write(this.tips);
		out.close();
	}
}
