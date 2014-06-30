package cn.qtone.common.components.syspurview.check.page.html.auto.tag;

import javax.servlet.http.HttpServletRequest;

import org.htmlparser.tags.Span;

import cn.qtone.common.components.syspurview.check.page.html.auto.AutoTagFilter;
import cn.qtone.common.components.syspurview.check.page.html.simple.TagLeach;

/**
 * span元素的自动标签过滤器.<P>
 * 
 * 该类用来匹配页面中的span元素的innerText值和功能的名称进行过滤，如果相同
 * 则执行该元素的过滤。
 * 
 * @author 马必强
 *
 */
public class AutoSpanFilter extends AutoTagFilter
{
	public AutoSpanFilter(HttpServletRequest req)
	{
		super(req);
	}

	@Override
	protected Class getTagClass()
	{
		return Span.class;
	}

	@Override
	protected TagLeach getTagLeach()
	{
		return new AutoSpanLeach();
	}

}
