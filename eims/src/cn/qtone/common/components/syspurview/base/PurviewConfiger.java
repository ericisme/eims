package cn.qtone.common.components.syspurview.base;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 权限系统的公共配置对象.
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public class PurviewConfiger
{
	private static Logger log = Logger.getLogger(PurviewConfiger.class);
	
	private static PurviewConfiger configer;
	
	private String prefix; // 权限系统资源文件（如JS、CSS等）的前缀路径
	
	private String systemName; // 应用系统的名称
	
	private boolean moduleTree = true; // 打开模块列表查询的层次结构，默认是

	private PurviewConfiger(){}
	
	/**
	 * 获取权限系统配置对象的唯一实例.
	 * @return
	 */
	public static PurviewConfiger getInstance()
	{
		if (configer == null) configer = new PurviewConfiger();
		return configer;
	}

	public boolean isModuleTree()
	{
		return moduleTree;
	}

	/**
	 * 这里的设置是在查询指定系统的一级模块时是否打开其下属二级模块的列表.
	 * 当一级和二级模块比较多时建议关闭（设置为false），避免页面拉的很长。
	 * 当这个配置被关闭时模块列表将只显示同一层次的所有模块。
	 * @param moduleTree
	 */
	public void setModuleTree(boolean moduleTree)
	{
		this.moduleTree = moduleTree;
	}

	public String getSystemName()
	{
		return systemName;
	}

	public void setSystemName(String systemName)
	{
		this.systemName = StringUtils.trimToEmpty(systemName);
	}

	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = StringUtils.trimToEmpty(prefix);
		if (log.isInfoEnabled()) log.info("资源文件路径前缀：" + this.prefix);
	}
}
