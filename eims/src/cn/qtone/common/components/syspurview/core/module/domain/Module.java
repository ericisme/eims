package cn.qtone.common.components.syspurview.core.module.domain;

import org.apache.commons.lang.StringUtils;

/**
 * 模块的信息Bean。
 * 
 * @author 马必强
 *
 */
public class Module
{
	public static final int STAT_DISABLED = -1; // 模块状态不可用

	public static final int STAT_NORMAL = 0; // 模块状态正常

	public static final int STAT_PUBLIC = 1; // 模块状态公用
	
	public static final int STAT_HIDE = 2;//模块状态隐藏
	
	private int mId; // 模块的唯一ID
	
	private int sysId = 1; // 所属系统ID
	
	private String mName; // 模块的名称
	
	private boolean menu = true; // 是否是菜单模块，默认是
	
	private int mStatus = 0; // 模块状态 -1-不可用 0-正常 1-无须授权
	
	private String mUrl; // 模块的URL地址
	
	private String mToken; // 模块的唯一TOKEN
	
	private int mSequence = 1; // 模块的排序序号
	
	private String mInfo; // 模块的介绍说明
	
	private boolean sysModule; // 是否是系统模块，默认不是

	public boolean isSysModule()
	{
		return sysModule;
	}

	public void setSysModule(boolean sysModule)
	{
		this.sysModule = sysModule;
	}

	public boolean isMenu()
	{
		return menu;
	}

	public void setMenu(boolean menu)
	{
		this.menu = menu;
	}

	public int getMId()
	{
		return mId;
	}

	public void setMId(int id)
	{
		mId = id;
	}

	public String getMInfo()
	{
		return mInfo;
	}

	public void setMInfo(String info)
	{
		mInfo = StringUtils.trimToEmpty(info);
	}

	public String getMName()
	{
		return mName;
	}

	public void setMName(String name)
	{
		mName = StringUtils.trimToEmpty(name);
	}

	public int getMSequence()
	{
		return mSequence;
	}

	public void setMSequence(int sequence)
	{
		mSequence = sequence;
	}

	public int getMStatus()
	{
		return mStatus;
	}

	public void setMStatus(int status)
	{
		if (status < Module.STAT_DISABLED || status > Module.STAT_HIDE) {
			mStatus = Module.STAT_NORMAL;
		} else {
			mStatus = status;
		}
	}

	public String getMToken()
	{
		return mToken;
	}

	public void setMToken(String token)
	{
		mToken = StringUtils.trimToEmpty(token);
	}

	public String getMUrl()
	{
		return mUrl;
	}

	public void setMUrl(String url)
	{
		mUrl = StringUtils.trimToEmpty(url);
	}

	public int getSysId()
	{
		return sysId;
	}

	public void setSysId(int sysId)
	{
		this.sysId = sysId;
	}
}
