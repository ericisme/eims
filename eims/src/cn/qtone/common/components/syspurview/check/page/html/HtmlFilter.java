package cn.qtone.common.components.syspurview.check.page.html;

/**
 * html代码的过滤器接口定义.<P>
 * 
 * 使用该接口可以将按钮的权限控制进行过滤，比如显示或隐藏，或是直接去掉等。
 * 
 * @author 马必强
 *
 */
public interface HtmlFilter
{
	/**
	 * 要进行过滤的级别，分别是不可用、隐藏和删除.
	 */
	public enum Filter {
		DISABLLED, HIDDEN, DELETE
	};
	
	/**
	 * 主要的执行方法，通过指定属性将指定的html中的代码进行过滤。<P>
	 * 
	 * @param html 要过滤的完整html代码
	 * @param filter 对其中的元素执行的过滤级别，不可用、隐藏、删除
	 * @return 过滤后的html代码
	 */
	String doFilter(String html, Filter filter);
}
