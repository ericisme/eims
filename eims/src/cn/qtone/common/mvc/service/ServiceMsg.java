package cn.qtone.common.mvc.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 业务处理类返回的信息bean，用来封装状态和消息。
 * 
 * @author 马必强
 *
 */
public class ServiceMsg
{
	private boolean success;
	
	private String message;
	
	private Map<String,Object> map;
	
	public ServiceMsg()
	{
		this.map = new HashMap<String,Object>();
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = StringUtils.trimToEmpty(message);
	}

	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}
	
	public void addObject(String key, Object obj)
	{
		this.map.put(key, obj);
	}
	
	public Object getObject(String key)
	{
		return this.map.get(key);
	}
}
