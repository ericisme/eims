package cn.qtone.common.utils.auto.spring.domain;

import org.apache.commons.lang.StringUtils;

/**
 * 模块的配置信息.
 * 
 * @author 马必强
 *
 */
public class Module
{
	private String moduleName; // 模块名称，使用英文，用来做类名称的前缀
	
	private String modulePackage; // 模块的包名称
	
	private String moduleDescribe; // 模块描述信息
	
	private String author; // 模块的作者，没指定则默认使用configer中的配置
	
	private String version; // 模块的版本，没指定则默认使用configer中的配置
	
	private String serviceProperty; // 控制器中的对业务类的引用set方法的属性名称,如果没指定则系统自己进行查找
	
	private String daoProperty; // 业务类中对dao类引用set方法的属性名称,如果没指定则系统自己进行查找

	public String getDaoProperty()
	{
		return daoProperty == null ? "" : daoProperty;
	}

	public void setDaoProperty(String daoProperty)
	{
		this.daoProperty = StringUtils.trimToEmpty(daoProperty);
	}

	public String getServiceProperty()
	{
		return serviceProperty == null ? "" : serviceProperty;
	}

	public void setServiceProperty(String serviceProperty)
	{
		this.serviceProperty = StringUtils.trimToEmpty(serviceProperty);
	}

	public String getAuthor()
	{
		return author == null ? "" : author;
	}

	public void setAuthor(String author)
	{
		this.author = StringUtils.trimToEmpty(author);
	}

	public String getModuleDescribe()
	{
		return moduleDescribe == null ? "" : moduleDescribe;
	}

	public void setModuleDescribe(String moduleDescribe)
	{
		this.moduleDescribe = StringUtils.trimToEmpty(moduleDescribe);
	}

	public String getModuleName()
	{
		return moduleName == null ? "" : moduleName;
	}

	public void setModuleName(String moduleName)
	{
		this.moduleName = StringUtils.trimToEmpty(moduleName);
	}

	public String getModulePackage()
	{
		return modulePackage == null ? "" : modulePackage;
	}

	public void setModulePackage(String modulePackage)
	{
		this.modulePackage = StringUtils.trimToEmpty(modulePackage);
	}

	public String getVersion()
	{
		return version == null ? "" : version;
	}

	public void setVersion(String version)
	{
		this.version = StringUtils.trimToEmpty(version);
	}
}
