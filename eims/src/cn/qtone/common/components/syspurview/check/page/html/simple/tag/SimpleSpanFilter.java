package cn.qtone.common.components.syspurview.check.page.html.simple.tag;

import javax.servlet.http.HttpServletRequest;

import org.htmlparser.tags.Span;

import cn.qtone.common.components.syspurview.check.page.html.simple.SimpleTagFilter;
import cn.qtone.common.components.syspurview.check.page.html.simple.TagLeach;

/**
 * 对页面中的span标签进行过滤.
 * @author 马必强
 *
 */
public class SimpleSpanFilter extends SimpleTagFilter
{
	public SimpleSpanFilter(HttpServletRequest req)
	{
		super(req);
	}
	
	/**
	 * 获取span标签过滤器的class.
	 * @return
	 */
	@Override
	protected Class getTagClass()
	{
		return Span.class;
	}

	@Override
	protected TagLeach getTagLeach()
	{
		return null;
	}
}
