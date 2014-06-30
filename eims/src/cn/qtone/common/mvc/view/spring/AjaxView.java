package cn.qtone.common.mvc.view.spring;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.View;

/**
 * 使用AJAX的执行操作视图解析类.
 * 
 * @author 马必强
 *
 */
public class AjaxView implements View
{
	private static final Logger logger = Logger.getLogger(AjaxView.class);

	private String msg;
	
	private boolean success = true; // 默认是执行成功
	
	private Map<String,Object> map = new HashMap<String,Object>(); // 设置的属性
	
	public AjaxView(String msg)
	{
		this.msg = StringUtils.trimToEmpty(msg);
	}
	
	public AjaxView(boolean success, String msg)
	{
		this.msg = StringUtils.trimToEmpty(msg);
		this.success = success;
	}
	
	public String getContentType()
	{
		return "text/plain;charset=UTF-8";
	}
	
	/**
	 * 设置返回到客户端的json对象的属性，success和message属性不允许设置。
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, Object value)
	{
		String tmp = StringUtils.trimToEmpty(key).intern();
		if (tmp == "sucess" || tmp == "message") return;
		this.map.put(tmp, value);
	}

	/**
	 * 使用json返回数据供前台js进行解析.
	 */
	public void render(Map arg0, HttpServletRequest arg1,
			HttpServletResponse arg2) throws Exception
	{
		PrintWriter out = arg2.getWriter();
		out.write(this.getJSONString());
		out.close();
		out = null;
	}
	
	/**
	 * 获取每个ajax操作的json对象，格式为：
	 * ["success":true,"message":"成功！"]
	 * @return
	 */
	protected String getJSONString()
	{
		JSONObject obj = JSONObject.fromMap(this.map);
		obj.put("success", success);
		obj.put("message", msg);
		if (logger.isInfoEnabled()) logger.info("ajax返回：" + obj);
		return obj.toString();
	}
}
