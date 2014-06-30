package cn.qtone.common.components.syspurview.check.page.html.auto;

import javax.servlet.http.HttpServletRequest;

import org.htmlparser.Node;

import cn.qtone.common.components.syspurview.check.page.html.simple.SimpleTagFilter;

/**
 * 自动过滤的标签过滤器基类.
 * 
 * 自动标签过滤器将从两个方面进行标签的权限过滤，第一层面是根据标签的特定
 * 属性进行获取，如果没有指定属性则再进行第二层面的过滤；第二层面的过滤是
 * 根据标签的固定属性和操作名称进行匹配过滤，比如对按钮的value属性和操作
 * 的中文名称进行匹配。通常的如添加、修改和删除等按钮就可以直接使用第二层
 * 面的过滤。
 * 
 * @author 马必强
 *
 */
public abstract class AutoTagFilter extends SimpleTagFilter
{
	public AutoTagFilter(HttpServletRequest req)
	{
		super(req);
	}
	
	@Override
	protected void visiteAndParse(Node body, Filter filter)
	{
		AutoTagVisitor tv = new AutoTagVisitor(this.userPurview);
		tv.setFilterMode(filter);
		tv.setModuleUri(uri);
		tv.setTagClass(this.getTagClass());
		tv.setTagLeach(this.getTagLeach());
		body.accept(tv);
	}
}
