package cn.qtone.common.utils.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;

import cn.qtone.common.utils.base.StringUtil;

/**
 * 下拉列表的JSON对象转换.
 * 转换后的字符串是如下格式：
 * {"options" : [ {"value" : 值,"text" : 文本}, 
 * 				  {"value" : 值,"text" : 文本},
 * 				  ……
 * 				  {"value" : 值,"text" : 文本}
 *              ]
 * }
 * 前台将利用两个JS进行转换和生成：
 * BaseClass对象的json方法将返回的字符串转换成对象；
 * Select对象的create/add方法将把转换后的对象添加到指定的select上去。
 * 
 * @author 马必强
 *
 */
public class SelectJSON
{
	private List<String> values;
	
	private List<String> texts;
	
	protected SelectJSON()
	{
		values = new ArrayList<String>();
		texts = new ArrayList<String>();
	}
	
	/**
	 * 向SelectJSON实例中添加一个option.
	 */
	public SelectJSON addOption(String value, String text)
	{
		this.values.add(value);
		this.texts.add(text);
		return this;
	}
	
	@Override
	public String toString()
	{
		JSONArray options = new JSONArray();
		for (int i = 0; i < this.values.size(); i ++) {
			JSONObject option = new JSONObject();
			option.put("value", this.values.get(i));
			option.put("text", this.texts.get(i));
			options.put(option);
		}
		JSONObject result = new JSONObject();
		result.put("options", options.toString());
		return result.toString();
	}
	
	/**
	 * 获取一个SelectJSON的实例，然后就可以通过使用addOption来添加一个option了
	 * @return
	 */
	public static SelectJSON getInstance()
	{
		return new SelectJSON();
	}
	
	/**
	 * 从指定的map对象中获取要设置的值后组装成返回给前台的JSON对象字符串.
	 * 
	 * 存储在map中的key-value应该是option中的value和text，即如下的option：
	 * <option value="hello">你好</option>
	 * 应该使用如下的map对象：
	 * map.put("hello", "你好");
	 * 注意使用map对象相同key值的将会被后设置的给覆盖！
	 * 
	 * @param map
	 * @return
	 */
	public static String fromMap(Map map)
	{
		Iterator it = map.keySet().iterator();
		JSONArray options = new JSONArray();
		while (it.hasNext()) {
			JSONObject option = new JSONObject();
			String key = (String)it.next();
			option.put("value", key);
			option.put("text", map.get(key));
			options.put(option);
		}
		JSONObject result = new JSONObject();
		result.put("options", options.toString());
		return result.toString();
	}
	
	/**
	 * 从指定的list中获取要设置出去JSON对象.
	 * 
	 * 存放在list中的对象应该是满足JavaBean规范的POJO对象，同时还要指定该对象中的
	 * 哪个属性作为option的value，哪个属性作为option的text。比如有如下一个JavaBean
	 * ，其部分属性如下：
	 * ……
	 * public String getOptValue()
	 * {
	 *     return this.optValue;
	 * }
	 * public String getOptText()
	 * {
	 *     return this.optText;
	 * }
	 * ……
	 * 如果我们想用optValue作为option的value属性，而optText作为option的text值，那么
	 * 我们就应该传递参数分别是：optValue, optText.
	 * 
	 * @param list
	 * @param valueProp
	 * @param textProp
	 * @return
	 */
	public static String fromList(List list, String valueProp, String textProp)
	{
		JSONArray options = new JSONArray();
		try {
			for (Object obj : list) {
				JSONObject option = new JSONObject();
				String value = BeanUtils.getProperty(obj, valueProp);
				String text = BeanUtils.getProperty(obj, textProp);
				option.put("value", value);
				option.put("text", text);
				options.put(option);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		JSONObject result = new JSONObject();
		result.put("options", options.toString());
		return result.toString();
	}
	
	public static void main(String[] args)
	{
		// map 测试
		Map<String,String> tt = new HashMap<String,String>();
		tt.put("value1", "text1");
		tt.put("value2", "text2");
		tt.put("value3", "text3");
		StringUtil.debug(SelectJSON.fromMap(tt));
		
		// 实例测试
		String text = SelectJSON.getInstance()
			.addOption("value1", "text1")
			.addOption("value2", "text2")
			.addOption("value3", "text3").toString();
		StringUtil.debug(text);
	}
}
