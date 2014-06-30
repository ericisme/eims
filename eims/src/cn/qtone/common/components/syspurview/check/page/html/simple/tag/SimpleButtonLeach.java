package cn.qtone.common.components.syspurview.check.page.html.simple.tag;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Tag;

import cn.qtone.common.components.syspurview.check.page.html.simple.TagLeach;
import cn.qtone.common.components.syspurview.login.domain.PurviewMap;

/**
 * 按钮的元素判断过滤器.即所有是input元素的只有type为button才进行过滤.
 * 
 * @author 马必强
 *
 */
public class SimpleButtonLeach implements TagLeach
{
	public boolean leachTag(Tag tag, String uri, PurviewMap userPurview)
	{
		String type = StringUtils.trimToEmpty(tag.getAttribute("type"));
		return type.equalsIgnoreCase("button");
	}
}
