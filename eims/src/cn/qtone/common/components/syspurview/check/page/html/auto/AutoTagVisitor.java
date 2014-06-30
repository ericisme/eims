package cn.qtone.common.components.syspurview.check.page.html.auto;

import org.htmlparser.Tag;

import cn.qtone.common.components.syspurview.check.page.HtmlUtil;
import cn.qtone.common.components.syspurview.check.page.html.simple.SimpleTagVisitor;
import cn.qtone.common.components.syspurview.login.domain.PurviewMap;

/**
 * 页面元素的自动过滤访问器.<P>
 * 
 * 自动过滤是根据操作的中文名称或英文名称和页面上指定的元素的指定属性进行匹配，
 * 如果能够匹配上则执行过滤，否则忽略。<P>
 * 
 * 针对自动过滤中的如果指定的元素有设置属性ignore，则忽略对该元素的过滤！属性
 * 的设置可以这样设置：ignore 或 ignore="true"
 * 
 * @author 马必强
 *
 */
public class AutoTagVisitor extends SimpleTagVisitor
{
	public AutoTagVisitor(PurviewMap userPurview)
	{
		super(userPurview);
	}

	/**
	 * 执行页面元素的访问并且执行过滤操作.该方法提供页面元素的自动过滤,
	 * 使用名称来进行匹配，用户可以不用在页面中加任何标识符属性等。
	 */
	@Override
	public void visitTag(Tag tag)
	{
		if (this.clazz == null) return;
		if (tag.getClass() != this.clazz) return;
		
		// 如果有指定忽略属性则忽略对该元素的过滤
		String attr = tag.getAttribute(HtmlUtil.TAG_IGNORE_ATTR_NAME);
		if (attr != null && attr.equalsIgnoreCase("true")) {
			if (log.isDebugEnabled()) log.debug("[页面过滤]标签{" + tag.toHtml()
					+ "}设置了Ignore属性，忽略对该元素的过滤！");
			return;
		}
		
		// 首先根据特定属性进行判断和过滤,如果没有指定属性则再根据标签的
		// 指定属性进行自动匹配过滤
		if (this.filtBySpecialAttribute(tag)) return;
		
		// 如果没有指定tagleach或者指定了但返回表示也需要进行过滤则进行过滤
		if (this.tagLeach == null || this.tagLeach.leachTag(tag, uri, userPurview)) {
			this.doTagFilter(tag);
		}
		this.removeTagOptAttr(tag);
	}
	
	/**
	 * 判断是否已经根据特定的操作属性来进行了标签过滤.
	 * @param tag
	 * @return
	 */
	protected boolean filtBySpecialAttribute(Tag tag)
	{
		String operate = this.getOperate(tag);
		if (operate == null) return false;
		String moduleUri = this.getModuleUri(tag);
		if (!this.havePurview(operate, moduleUri)) this.doTagFilter(tag);
		this.removeTagOptAttr(tag);
		return true;
	}
}
