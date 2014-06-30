package cn.qtone.common.components.syspurview.controlpanel.domain;

import org.apache.commons.lang.StringUtils;

/**
 * 用户的管理后台的个人风格设置.
 * 
 * @author 马必强
 *
 */
public class UserStyle implements java.io.Serializable
{
	private static final long serialVersionUID = -4875789685751478203L;

	private int userId; // 用户的个人唯一ID
	
	private String styleName; // 风格的中文名称
	
	private String layoutName; // 页面的布局风格名称
	
	private String cssName; // 页面的样式风格名称
	
	private String stylePicture; // 风格的缩略图路径信息
	
	public String getStyleKey()
	{
		return this.layoutName + "_" + this.cssName;
	}

	public String getCssName()
	{
		return cssName;
	}

	public void setCssName(String cssName)
	{
		this.cssName = StringUtils.trimToEmpty(cssName);
	}

	public String getLayoutName()
	{
		return layoutName;
	}

	public void setLayoutName(String layoutName)
	{
		this.layoutName = StringUtils.trimToEmpty(layoutName);
	}

	public String getStylePicture()
	{
		return stylePicture;
	}

	public void setStylePicture(String stylePicture)
	{
		this.stylePicture = StringUtils.trimToEmpty(stylePicture);
	}

	public int getUserId()
	{
		return userId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	public String getStyleName()
	{
		return styleName;
	}

	public void setStyleName(String styleName)
	{
		this.styleName = StringUtils.trimToEmpty(styleName);
	}
}
