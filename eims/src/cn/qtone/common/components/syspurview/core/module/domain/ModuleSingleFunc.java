package cn.qtone.common.components.syspurview.core.module.domain;

import org.apache.commons.lang.StringUtils;

/**
 * 模块的单个功能信息Bean.
 * 
 * @author 马必强
 *
 */
public class ModuleSingleFunc
{
	private int funcId; // 功能的唯一ID
	
	private String funcName; // 功能的中文名称
	
	private String funcOperate; // 功能的操作标识字符串，通常对应servlet中的action的值
	
	private String relOperate; // 相关连的操作标识字符串

	public String getFuncName()
	{
		return funcName;
	}

	public void setFuncName(String funcName)
	{
		this.funcName = StringUtils.trimToEmpty(funcName);
	}

	public String getFuncOperate()
	{
		return funcOperate;
	}

	public void setFuncOperate(String funcOperate)
	{
		this.funcOperate = StringUtils.trimToEmpty(funcOperate);
	}

	public String getRelOperate()
	{
		return relOperate;
	}

	public void setRelOperate(String relOperate)
	{
		this.relOperate = StringUtils.trimToEmpty(relOperate);
	}

	public int getFuncId()
	{
		return funcId;
	}

	public void setFuncId(int funcId)
	{
		this.funcId = funcId;
	}
}
