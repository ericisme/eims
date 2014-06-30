package cn.qtone.common.utils.json;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import cn.qtone.common.utils.base.StringUtil;

/**
 * 该类用来解析json字符串为对象，然后获取其属性值.
 * 主要用来获取从客户端传递过来的JSON对象，然后获取参数值.
 * 
 * @author 马必强
 *
 */
public class JSONRequestParser
{
	private String json;
	
	private JSONObject obj;
	
	public JSONRequestParser(String json)
	{
		this.json = StringUtil.trim(json);
		if (this.json.intern() == "") this.json = "{}";
		this.obj = JSONObject.fromString(this.json);
	}
	
	/**
	 * 返回指定属性的值，其特性和request.getParameter一样
	 * @param attrName
	 * @return
	 */
	public String getParameter(String attrName)
	{
		String name = StringUtil.trim(attrName).intern();
		if (name == "" || !this.obj.has(name)) return null;
		Object value = this.obj.get(name);
		// 如果为空对象，则返回null
		if (value.getClass() == JSONNull.class) return null;
		// 如果为字符串，则返回字符串值
		if (value.getClass() == String.class) return value.toString();
		// 如果是数组，则返回第一个值
		if (value.getClass() == JSONArray.class) {
			JSONArray ary = (JSONArray)value;
			if (ary.length() < 1) return null;
			return ary.getString(0);
		}
		return null;
	}
	
	/**
	 * 返回指定属性的数组.没有则返回null，其特性和request.getParameterValues一样
	 * @param attrName
	 * @return
	 */
	public String[] getParameterValues(String attrName)
	{
		String name = StringUtil.trim(attrName).intern();
		if (name == "" || !this.obj.has(name)) return null;
		Object value = this.obj.get(name);
		if (value.getClass() == String.class) return new String[]{value.toString()};
		if (value.getClass() == JSONArray.class) {
			JSONArray ary = (JSONArray)value;
			String[] result = new String[ary.length()];
			for (int i = 0; i < ary.length(); i ++) {
				result[i] = ary.getString(i);
			}
			return result;
		}
		return null;
	}
	
	/**
	 * 获取指定的属性，并将其转换成boolean值.
	 * 这里的boolean值的转换：
	 * 1.如果其string值是true，则是true，否则是false；
	 * 2.如果其string值是数字且大于等于1则返回true，否则返回false
	 * 3.如果不存在该属性则返回false
	 * 4.其他情况返回false
	 * @param attrName
	 * @return
	 */
	public boolean getBooleanParameter(String attrName)
	{
		String val = this.getParameter(attrName);
		if (val == null) return false;
		if (val.trim().intern() == "true") return true;
		int tmp1 = StringUtil.parseInt(val, -1), tmp2= StringUtil.parseInt(val, 0);
		if (tmp1 == tmp2) {
			if (tmp1 > 0) return true;
			else return false;
		}
		return false;
	}
	
	/**
	 * 获取指定参数值的整型值，如果不存在或解析错误则返回0
	 * @param attrName
	 * @return
	 */
	public int getIntParameter(String attrName)
	{
		String value = this.getParameter(attrName);
		return StringUtil.parseInt(value, 0);
	}
	
	/**
	 * 获取指定参数的（数组）值的整型值.
	 * @param attrName
	 * @return
	 */
	public int[] getIntParameterValues(String attrName)
	{
		String[] values = this.getParameterValues(attrName);
		if (values == null || values.length < 1) return null;
		return StringUtil.parseInt(values, 0);
	}
}
