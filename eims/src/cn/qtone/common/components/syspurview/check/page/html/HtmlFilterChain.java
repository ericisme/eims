package cn.qtone.common.components.syspurview.check.page.html;

import java.util.ArrayList;
import java.util.List;

import cn.qtone.common.components.syspurview.check.page.html.HtmlFilter.Filter;

/**
 * html页面代码的过滤器链，可以同时设置多个过滤器进行过滤.<P>
 * 
 * @author 马必强
 *
 */
public class HtmlFilterChain
{
	private List<HtmlFilter> filters;
	
	private List<Filter> fAttrs;
	
	public HtmlFilterChain()
	{
		this.filters = new ArrayList<HtmlFilter>();
		this.fAttrs = new ArrayList<Filter>();
	}
	
	/**
	 * 向过滤器链中添加一个过滤器.<P>
	 * 
	 * @param filter
	 * @pramat fAtrra 过滤的属性，不可用、隐藏或是删除.默认是删除
	 */
	public void addFilter(HtmlFilter filter, Filter fAttr)
	{
		if (filter == null) return;
		this.filters.add(filter);
		if (fAttr == null) fAttr = Filter.DELETE;
		this.fAttrs.add(fAttr);
	}
	
	/**
	 * 清除所有的过滤器设置.
	 *
	 */
	public void clearFilter()
	{
		this.filters.clear();
		this.fAttrs.clear();
	}
	
	/**
	 * 执行过滤操作.
	 * @return
	 */
	public String doFilter(String html)
	{
		String end = html;
		for (int i = 0; i < this.filters.size(); i ++) {
			end = this.filters.get(i).doFilter(end, this.fAttrs.get(i));
		}
		return end;
	}
}
