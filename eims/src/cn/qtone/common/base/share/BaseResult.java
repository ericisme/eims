package cn.qtone.common.base.share;

import java.util.HashMap;

public class BaseResult
{
	public final static int WRONG = 0;
	
	public final static int RIGHT = 1;
	
	private String exception = null;

	private String message = null;

	private int state = -1;

	private String page = null;

	private HashMap<String,Object> map = new HashMap<String,Object>();

	public BaseResult()
	{
	}

	public void setException(Exception e)
	{
		exception = e.getMessage();
	}

	public String getException()
	{
		return exception;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getMessage()
	{
		return message;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public int getState()
	{
		return state;
	}

	public void setPage(String page)
	{
		this.page = page;
	}

	public String getPage()
	{
		return page;
	}

	public void putValue(String key, Object value)
	{
		map.put(key, value);
	}

	public Object getValue(String key)
	{
		return map.get(key);
	}

	public int size()
	{
		return map.size();
	}

	public void clear()
	{
		map.clear();
	}

	public void remove(String key)
	{
		map.remove(key);
	}
}
