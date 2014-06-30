package cn.qtone.common.components.syspurview.check.page.html.auto.tag;

import java.util.Map;

import org.htmlparser.Tag;

import cn.qtone.common.components.syspurview.login.domain.PurviewMap;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 页面元素中的span元素的过滤器实现类.<P>
 * 
 * span元素的自动过滤是根据span的innerText值和权限集合中当前访问的页面的权限
 * 对比判断来实现的。<P>
 * 
 * @author 马必强
 *
 */
public class AutoSpanLeach extends AutoButtonLeach
{
	/**
	 * 该方法如果返回true则表示该元素需要被过滤掉,即用户对该操作没有权限.
	 */
	@Override
	public boolean leachTag(Tag tag, String uri, PurviewMap userPurview)
	{
		Map<String,String>[] modPw = userPurview.getModulePurview(uri);
		if (modPw == null) return true;
		
		// 获取span的innerText值
		String value = StringUtil.trim(tag.toPlainTextString());
		
		return this.checkPurview(uri, userPurview, value);
	}
}
