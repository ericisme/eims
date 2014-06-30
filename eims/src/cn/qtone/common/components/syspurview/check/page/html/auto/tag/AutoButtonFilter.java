package cn.qtone.common.components.syspurview.check.page.html.auto.tag;

import javax.servlet.http.HttpServletRequest;

import org.htmlparser.tags.InputTag;

import cn.qtone.common.components.syspurview.check.page.html.auto.AutoTagFilter;
import cn.qtone.common.components.syspurview.check.page.html.simple.TagLeach;

/**
 * 按钮元素的自动标签过滤器.<P>
 * 
 * 该类用来匹配页面中的按钮元素的value属性和功能的名称进行过滤，如果相同
 * 则执行该元素的过滤。
 * 
 * @author 马必强
 *
 */
public class AutoButtonFilter extends AutoTagFilter
{
	public AutoButtonFilter(HttpServletRequest req)
	{
		super(req);
	}
	
	@Override
	public TagLeach getTagLeach()
	{
		return new AutoButtonLeach();
	}
	
	@Override
	protected Class getTagClass()
	{
		return InputTag.class;
	}
}
