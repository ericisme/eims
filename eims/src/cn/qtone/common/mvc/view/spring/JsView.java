package cn.qtone.common.mvc.view.spring;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.View;

/**
 * 使用JS返回时的视图解析类.<br>
 * 
 * 该视图解析类用来解析所有的JS直接返回的视图，比如添加成功的
 * 提示信息、更新成功的提示信息等。
 * 
 * @author 马必强
 * @version 1.0
 */
public class JsView implements View
{
	private static final String prefix = "<script language='javascript'>";
	
	private static final String suffix = "</script>";
	
	private String scriptTips; // js的弹出提示信息
	
	private String forward; // 转向的URL地址，如果没有指定则直接后退
	
	private int topLevel = 0; // 使用parent来进行location的层数
	
	public JsView(String scriptTips)
	{
		this.scriptTips = StringUtils.trimToEmpty(scriptTips);
	}
	
	public JsView(String scriptTips, String forward)
	{
		this.scriptTips = StringUtils.trimToEmpty(scriptTips);
		this.forward = StringUtils.trimToEmpty(forward);
	}
	
	public JsView(String scriptTips, String forward, int topLevel)
	{
		this.scriptTips = StringUtils.trimToEmpty(scriptTips);
		this.forward = StringUtils.trimToEmpty(forward);
		if (topLevel >= 1) this.topLevel = topLevel;
	}

	public String getContentType()
	{
		return "text/plain;charset=UTF-8";
	}

	public void render(Map arg0, HttpServletRequest arg1,
			HttpServletResponse arg2) throws Exception
	{
		StringBuffer script = new StringBuffer();
		script.append(prefix);
		if (this.scriptTips.intern() != "") {
			script.append("alert('" + this.scriptTips.replace("\'", "\\'") + "');");
		}
		if (this.forward != null && this.forward.intern() != "") {
			for (int i = 0; i < this.topLevel; i ++) {
				script.append("parent.");
			}
			script.append("window.location.href='" + this.forward + "';");
		} else {
			script.append("window.history.back();");
		}
		script.append(suffix);
		arg2.setContentType(this.getContentType());
		arg2.sendRedirect("/syspurview/login.do?action=logininit");
		/*
		PrintWriter out = arg2.getWriter();
		out.write(script.toString());
		out.close();
		*/
	}

}
