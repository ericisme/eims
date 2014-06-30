package cn.qtone.common.components.syspurview.check.page;


/**
 * HTML页面解析中的实用方法类，包括常量和组合等方法.<P>
 * 
 * @author 马必强
 *
 */
public final class HtmlUtil
{
	private HtmlUtil(){}
	
	/**
	 * 页面标签元素中表示权限的属性名称
	 */
	public final static String TAG_OPT_ATTR_NAME = "sys_opt";
	
	/**
	 * 页面标签元素中表示当前操作所属的模块的URI属性名称
	 */
	public final static String TAG_OPT_URI_ATTR_NAME = "sys_uri";
	
	/**
	 * 当使用自动过滤时如果设置了该属性则忽略对该元素的过滤.
	 */
	public final static String TAG_IGNORE_ATTR_NAME = "ignore";
	
	/**
	 * 使用freemarker进行页面权限判断时的map关键字
	 */
	public final static String FM_MAP_KEY = "sys_purview";
}
