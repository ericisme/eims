package cn.qtone.common.utils.auto.spring.domain;

import org.apache.commons.lang.StringUtils;

/**
 * 整个配置信息的基础信息.
 * 
 * @author 马必强
 *
 */
public class Configer
{
	private String projectPath; // 项目的物理路径
	
	private String basePackage; // 项目源码的基础包路径，如cn.qtone
	
	private String author; // 各模块的通用作者，如果子模块有指定则忽略
	
	private String version; // 各模块的通用版本号，如果子模块有指定则忽略
	
	private String beanFile; // 要输出到bean定义文件路径
	
	private String namingSpace; // 命名空间，用在bean定义文件中做ID前缀

	public String getNamingSpace()
	{
		return namingSpace == null ? "" : namingSpace;
	}

	public void setNamingSpace(String namingSpace)
	{
		this.namingSpace = StringUtils.trimToEmpty(namingSpace);
	}

	public String getAuthor()
	{
		return author == null ? "" : author;
	}

	public void setAuthor(String author)
	{
		this.author = StringUtils.trimToEmpty(author);
	}

	public String getBasePackage()
	{
		return basePackage;
	}

	public void setBasePackage(String basePackage)
	{
		this.basePackage = StringUtils.trimToEmpty(basePackage);
	}

	public String getBeanFile()
	{
		return beanFile == null ? "" : beanFile;
	}

	public void setBeanFile(String beanFile)
	{
		this.beanFile = StringUtils.trimToEmpty(beanFile);
	}

	public String getProjectPath()
	{
		return projectPath == null ? "" : projectPath;
	}

	public void setProjectPath(String projectPath)
	{
		this.projectPath = StringUtils.trimToEmpty(projectPath);
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
