package cn.qtone.common.components.syspurview.check.page.html.simple.tag;

import javax.servlet.http.HttpServletRequest;

import org.htmlparser.tags.InputTag;

import cn.qtone.common.components.syspurview.check.page.html.simple.SimpleTagFilter;
import cn.qtone.common.components.syspurview.check.page.html.simple.TagLeach;

/**
 * 按照按钮（对所有的input标签）进行过滤时的特定过滤类.<P>
 * 
 * @author 马必强
 *
 */
public class SimpleButtonFilter extends SimpleTagFilter
{
	public SimpleButtonFilter(HttpServletRequest req)
	{
		super(req);
	}
	
	/**
	 * 获取input标签过滤器的class.
	 * @return
	 */
	@Override
	protected Class getTagClass()
	{
		return InputTag.class;
	}
	
	/**
	 * 判断InputTag标签是否是button，即按钮
	 */
	@Override
	protected TagLeach getTagLeach()
	{
		return new SimpleButtonLeach();
	}
}
