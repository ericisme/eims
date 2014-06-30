package cn.qtone.common.components.syspurview.core.system.domain;

import org.apache.commons.lang.StringUtils;

/**
 * 各系统的属性信息Bean。
 * 
 * @author 马必强
 *
 */
public class SystemBean
{
	private static String[] TARGET = new String[] {"default", "_blank"};
	
	private int systemId; // 子系统的唯一ID
	
	private String sysName; // 子系统的名称
	
	private String sysFlag; // 子系统的唯一标识
	
	private String sysUrl; // 子系统的连接地址
	
	private String sysTarget; // 子系统的目标地址
	
	private int sysSequence = 1; // 子系统的排序序号

	public String getSysFlag()
	{
		return sysFlag;
	}

	public void setSysFlag(String sysFlag)
	{
		this.sysFlag = StringUtils.trimToEmpty(sysFlag);
	}

	public String getSysName()
	{
		return sysName;
	}

	public void setSysName(String sysName)
	{
		this.sysName = StringUtils.trimToEmpty(sysName);
	}

	public int getSysSequence()
	{
		return sysSequence;
	}

	public void setSysSequence(int sysSequence)
	{
		this.sysSequence = sysSequence;
	}

	public String getSysTarget()
	{
		return sysTarget;
	}

	public void setSysTarget(String sysTarget)
	{
		this.sysTarget = StringUtils.trimToEmpty(sysTarget);
	}

	public int getSystemId()
	{
		return systemId;
	}

	public void setSystemId(int systemId)
	{
		this.systemId = systemId;
	}

	/**
	 * 如果连接地址为空则表示是默认地址
	 * @return
	 */
	public String getSysUrl()
	{
		return this.sysUrl == null ? "" : this.sysUrl;
	}

	public void setSysUrl(String sysUrl)
	{
		this.sysUrl = StringUtils.trimToEmpty(sysUrl);
	}
	
	/**
	 * 检查子系统的target是否符号要求.
	 * @param target
	 * @return
	 */
	public static boolean isRightTarget(String target)
	{
		for (String t : SystemBean.TARGET) {
			if (t.equalsIgnoreCase(target)) return true;
		}
		return false;
	}
}
