package cn.qtone.common.components.syspurview.check.page.html.auto.tag;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Tag;

import cn.qtone.common.components.syspurview.check.page.html.simple.TagLeach;
import cn.qtone.common.components.syspurview.login.domain.PurviewMap;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 页面元素中的按钮元素的过滤器实现类.<P>
 * 
 * 按钮的自动过滤是根据按钮的value属性值和权限集合中当前访问的页面的权限
 * 对比判断来实现的。<P>
 * 
 * @author 马必强
 *
 */
public class AutoButtonLeach implements TagLeach
{
	/**
	 * 该方法如果返回true则表示该元素需要被过滤掉,即用户对该操作没有权限.
	 */
	public boolean leachTag(Tag tag, String uri, PurviewMap userPurview)
	{
		// 如果不是button则不进行过滤
		String type = StringUtils.trimToEmpty(tag.getAttribute("type"));
		if (!type.equalsIgnoreCase("button")) return false;
		
		// 获取button的value属性值
		String value = StringUtil.trim(tag.getAttribute("value"));
		
		return this.checkPurview(uri, userPurview, value);
	}
	
	protected boolean checkPurview(String uri, PurviewMap userPurview, String value)
	{
		Map<String,String>[] modPw = userPurview.getModulePurview(uri);
		if (modPw == null) return true;
		// 从用户不具有的模块权限集合中查找是否有该名称的操作，有则表示没有权限则
		// 不进行过滤，返回true,否则返回false
		Iterator it = modPw[1].values().iterator();
		while (it.hasNext()) {
			if (this.isSame(value, (String)it.next())) return true;
		}
		return false;
	}
	
	protected boolean isSame(String attrValue, String optName)
	{
		String tmp1 = StringUtil.join(StringUtil.trim(attrValue).split("\\s+"), "");
		String tmp2 = StringUtil.join(StringUtil.trim(optName).split("\\s+"), "");
		return tmp1.equalsIgnoreCase(tmp2);
	}

}
