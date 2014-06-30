package cn.qtone.common.components.syspurview.check.page.html.simple;

import org.htmlparser.Tag;

import cn.qtone.common.components.syspurview.login.domain.PurviewMap;

/**
 * 对于特定元素的是否进行过滤的判断接口，需要进行判断的元素必须实现该接
 * 口，同时要在其类中提供对该接口实现类的简便访问方法.如果某一元素没有
 * 提供对该接口的实现，那么系统将默认满足条件的元素将全部被过滤！
 * <P>
 * 这里主要是对同类元素不同类型进行判断，如input元素有button、text之分
 * 等，这就需要每一个特定的元素过滤器进行设置和判断了。
 * 
 * @author 马必强
 *
 */
public interface TagLeach
{
	/**
	 * 判断指定的元素是否需要过滤，返回true则表示是，否则不进行过滤！<P>
	 * 
	 * @param tag
	 * @return
	 */
	public boolean leachTag(Tag tag, String uri, PurviewMap userPurview);
}
