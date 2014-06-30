package cn.qtone.common.components.syspurview.check.page.html.simple;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.htmlparser.Tag;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.NodeVisitor;

import cn.qtone.common.components.syspurview.check.page.HtmlUtil;
import cn.qtone.common.components.syspurview.check.page.html.HtmlFilter.Filter;
import cn.qtone.common.components.syspurview.login.domain.PurviewMap;

/**
 * 页面元素访问对象，在此进行对元素的修改！<P>
 * 
 * @author 马必强
 *
 */
public class SimpleTagVisitor extends NodeVisitor
{
	protected static final Logger log = Logger.getLogger(SimpleTagVisitor.class);

	protected Class clazz; // 要访问的元素的class
	
	protected Filter filter; // 要过滤的模式，不可用、删除或是隐藏
	
	protected String uri; // 当前请求的URI
	
	protected PurviewMap userPurview; // 所有的角色的权限集合
	
	protected TagLeach tagLeach; // 元素的判断过滤器
	
	public SimpleTagVisitor(PurviewMap userPurview)
	{
		this.userPurview = userPurview;
	}
	
	public void setTagClass(Class clazz)
	{
		this.clazz = clazz;
	}
	
	public void setFilterMode(Filter filter)
	{
		this.filter = filter;
	}
	
	public void setModuleUri(String uri)
	{
		this.uri = StringUtils.trimToEmpty(uri);
	}
	
	public void setTagLeach(TagLeach tagLeach)
	{
		this.tagLeach = tagLeach;
	}
	
	/**
	 * 只访问标签元素,在该方法中设置只允许访问并设置提供的类型.<P>
	 * 
	 * 指定的标签中如果有提供HtmlUtil.TAG_OPT_ATTR_NAME属性，则表示该标签是要被过
	 * 滤的，那么系统会再从标签中尝试获取属性HtmlUtil.TAG_OPT_URI_ATTR_NAME属性，
	 * 来确定当前操作功能所属的模块，如果没有指定则默认是当前访问模块。
	 */
	@Override
	public void visitTag(Tag tag)
	{
		if (this.clazz == null) return;
		if (tag.getClass() != this.clazz) return;
		if (this.tagLeach != null && !this.tagLeach.leachTag(tag, null, null)) return;
		String operate = this.getOperate(tag);
		if (operate == null) return;
		String moduleUri = this.getModuleUri(tag);
		// 检测如果没有权限，则按照指定的方式进行过滤
		if (!havePurview(operate, moduleUri)) this.doTagFilter(tag);
		this.removeTagOptAttr(tag);
	}
	
	/**
	 * 删除过滤标签的操作标志属性和模块URI标志属性
	 * @param tag
	 */
	protected void removeTagOptAttr(Tag tag)
	{
		if (tag == null) return;
		tag.removeAttribute(HtmlUtil.TAG_OPT_ATTR_NAME);
		tag.removeAttribute(HtmlUtil.TAG_OPT_URI_ATTR_NAME);
	}
	
	/**
	 * 执行标签的过滤操作
	 * @param tag
	 */
	protected void doTagFilter(Tag tag)
	{
		if (log.isDebugEnabled()) log.debug("[页面过滤]过滤标签[" + tag.toHtml() + "]!");
		if (filter == Filter.DISABLLED) this.doDisabledFilter(tag);
		else if (filter == Filter.HIDDEN) this.doHiddenFilter(tag);
		else this.doDeleteFilter(tag);
	}
	
	/**
	 * 获取当前指定标签的操作属性.如果没有指定则返回null
	 * @param tag
	 * @return
	 */
	protected String getOperate(Tag tag)
	{
		String operate = tag.getAttribute(HtmlUtil.TAG_OPT_ATTR_NAME);
		if (operate == null) return null;
		return StringUtils.trimToEmpty(operate);
	}
	
	/**
	 * 获取当前操作的所属模块URI，如果没指定或为空串则默认是当前模块
	 * @param tag
	 * @return
	 */
	protected String getModuleUri(Tag tag)
	{
		String moduleUri = tag.getAttribute(HtmlUtil.TAG_OPT_URI_ATTR_NAME);
		if (moduleUri == null || moduleUri.trim().intern() == "") return this.uri;
		return StringUtils.trimToEmpty(moduleUri);
	}

	/**
	 * 检查当前用户是否对访问的资源具有指定的权限.
	 * @param operate
	 * @return
	 */
	protected boolean havePurview(String operate, String moduleUri)
	{
		if (this.userPurview == null) return false;
		return this.userPurview.isAllowed(moduleUri, operate);
	}
	
	/**
	 * 做使标签不可操作的过滤.
	 * @param tag
	 */
	protected void doDisabledFilter(Tag tag)
	{
		if (log.isDebugEnabled()) log.debug("[页面过滤]过滤标签为不可用！");
		String attr = tag.getAttribute("disabled");
		if (attr == null) {
			tag.setAttribute("disabled", "\"disabled\"");
		} else {
			tag.setAttribute("disabled", "disabled");
		}
	}
	
	/**
	 * 做隐藏标签的过滤.
	 * @param tag
	 */
	protected void doHiddenFilter(Tag tag)
	{
		if (log.isDebugEnabled()) log.debug("[页面过滤]过滤标签为隐藏！");
		String attr = tag.getAttribute("style");
		if (attr == null) {
			tag.setAttribute("style", "\"display:none;\"");
		} else {
			tag.setAttribute("style", attr + ";display:none;");
		}
	}
	
	/**
	 * 做删除标签的过滤.
	 * @param tag
	 */
	protected void doDeleteFilter(Tag tag)
	{
		if (log.isDebugEnabled()) log.debug("[页面过滤]过滤标签为删除！");
		NodeList list = tag.getParent().getChildren();
		list.remove(tag);
	}
}
