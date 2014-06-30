package cn.qtone.common.utils.auto.spring.parser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import cn.qtone.common.utils.auto.spring.domain.Configer;
import cn.qtone.common.utils.auto.spring.domain.Module;

/**
 * JSON字符串的解析类.
 * 
 * @author 马必强
 *
 */
public class JSONParser implements ParserInter
{
	private String source; // 源字符串
	
	private Configer configer;
	
	private Module[] modules;
	
	public JSONParser(String source)
	{
		this.source = source;
		this.parse();
	}

	public Configer getConfiger()
	{
		return this.configer;
	}

	public Module[] getModules()
	{
		return this.modules;
	}
	
	protected void parse()
	{
		JSONObject json = JSONObject.fromString(this.source);
		// 配置对象生成
		Object obj = json.get("configer");
		if (obj.getClass() != JSONObject.class) {
			throw new RuntimeException("[配置信息错误]缺少基础配置对象configer！");
		}
		this.configer = (Configer)JSONObject.toBean((JSONObject)obj, Configer.class);
		
		//模块对象生成
		obj = json.get("modules");
		if (obj.getClass() != JSONArray.class) {
			throw new RuntimeException("[配置信息错误]缺少模块配置对象modules！");
		}
		JSONArray array = (JSONArray)obj;
		this.modules = new Module[array.length()];
		for (int i = 0; i < array.length(); i ++) {
			this.modules[i] = (Module)JSONObject.toBean(array.getJSONObject(i), Module.class);
		}
	}
}
