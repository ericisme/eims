package cn.qtone.common.components.syspurview.core.module.domain;

import java.util.ArrayList;
import java.util.List;

import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.common.utils.json.JSONRequestParser;

/**
 * 模块的所有功能的汇总.
 * 
 * 从数据库中查询出来的就使用list列表进行存储，从客户端进行一次性提交的
 * （包括添加和修改时）使用各个数组来存储，然后使用一个转换方法进行转换
 * 到列表的操作。
 * 
 * @author 马必强
 *
 */
public class ModuleFunc extends Module
{
	private List<ModuleSingleFunc> funcs;
	
	private String[] funcName; // 所有的功能名称
	
	private String[] funcOperate; // 所有的功能操作标识符
	
	private String[] relOperate; // 所有的相关联的操作标识符
	
	private int[] funcId; // 所有的功能ID
	
	public ModuleFunc()
	{
		this.funcs = new ArrayList<ModuleSingleFunc>();
	}
	
	public int[] getFuncId()
	{
		return funcId;
	}

	public void setFuncId(int[] funcId)
	{
		this.funcId = funcId;
	}

	public String[] getFuncName()
	{
		return funcName;
	}

	public void setFuncName(String[] funcName)
	{
		this.funcName = funcName;
	}

	public String[] getFuncOperate()
	{
		return funcOperate;
	}

	public void setFuncOperate(String[] funcOperate)
	{
		this.funcOperate = funcOperate;
	}

	public String[] getRelOperate()
	{
		return relOperate;
	}

	public void setRelOperate(String[] relOperate)
	{
		this.relOperate = relOperate;
	}
	
	public void addFunc(ModuleSingleFunc func)
	{
		this.funcs.add(func);
	}
	
	public List<ModuleSingleFunc> getFuncList()
	{
		return this.funcs;
	}
	
	/**
	 * 将客户端提交的各个数据组装成一个个ModuleSingleFunc对象，并
	 * 添加到list列表中去，返回状态status含义如下：
	 * 0  -  操作成功
	 * -1 -  至少有一项（名称、表示或ID）为null
	 * -2 -  至少有两个项的长度不一致
	 * @return status
	 */
	public int buildup()
	{
		if (this.funcId == null || this.funcName == null || 
				this.funcOperate == null || this.relOperate == null) return -1;
		if (!StringUtil.sameLength(this.funcName, this.funcOperate, 
				this.relOperate)) return -2;
		if (this.funcId.length != this.funcName.length) return -2;
		for (int i = 0; i < this.funcId.length; i ++) {
			ModuleSingleFunc func = new ModuleSingleFunc();
			func.setFuncId(this.funcId[i]);
			func.setFuncName(this.funcName[i]);
			func.setFuncOperate(this.funcOperate[i]);
			func.setRelOperate(this.relOperate[i]);
			this.addFunc(func);
		}
		return 0;
	}
	
	/**
	 * 设置JSON字符串参数.
	 * @param json
	 */
	public void setJson(String json)
	{
		JSONRequestParser parser = new JSONRequestParser(json);
		// 获取当前模块的唯一ID和模块的名称
		this.setMId(parser.getIntParameter("MId"));
		this.setMName(parser.getParameter("MName"));
		this.setFuncId(parser.getIntParameterValues("funcId"));
		this.setFuncName(parser.getParameterValues("funcName"));
		this.setFuncOperate(parser.getParameterValues("funcOperate"));
		this.setRelOperate(parser.getParameterValues("relOperate"));
	}
}
