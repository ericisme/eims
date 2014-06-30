package cn.qtone.common.components.syspurview.check.page.html.simple;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import cn.qtone.common.components.syspurview.check.page.html.HtmlFilter;
import cn.qtone.common.components.syspurview.core.user.domain.AbstractUser;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.common.components.syspurview.login.domain.PurviewMap;

/**
 * 将html代码中指定的标签进行过滤的简单实现类。
 * 如果需要对特定的标签进行过滤，那么则可以继承此类来实现。
 * 
 * @author 马必强
 *
 */
public abstract class SimpleTagFilter implements HtmlFilter
{
	protected static final Logger logger = Logger.getLogger(SimpleTagFilter.class);

	protected PurviewMap userPurview; // 当前用户的权限集合
	
	protected String uri; // 当前请求的URI

	/**
	 * 构造器，必须指定当前的用户信息Bean.
	 * @param req 当前请求request
	 */
	public SimpleTagFilter(HttpServletRequest req)
	{
		this.uri = req.getRequestURI();
		AbstractUser user = UserUtil.getUserBean(req);
		if (user != null) this.userPurview = user.getPurviewMap();
		if (logger.isDebugEnabled()) logger.debug("[页面过滤]当前请求URI为[" + this.uri + "]");
	}
	
	/**
	 * 使用按钮进行过滤页面中指定的角色没有权限操作的部分.<br>
	 * 
	 * 系统将首先获取body体内的所有指定的标签，同时检测其是否包含sys_opt
	 * 属性，如果是则认为这是需要进行权限过滤的标签，那么系统会根据当前用户的角色
	 * 去查询是否具有该属性所标识别的权限（注意sys_opt的值必须和该模块设置的
	 * 功能字符串要一致），然后角色是否进行过滤以及进行何种过滤。
	 * 
	 * 例如以input标签为例，其页面布局如下：
	 * <html>
	 * ……
	 * <body>
	 * ……
	 * <input sys_opt="add" ……/>
	 * ……
	 * </body>
	 * </html>
	 * 
	 * 注意的是，body必须有父节点，否则其前后的信息都将不可见！对于正常的html规范来
	 * 说这不是问题，但是对于编写非规范的html代码就需要注意了！
	 * 
	 * 如果不指定过滤的属性（级别），系统默认是删除！
	 */
	public String doFilter(String html, Filter filter)
	{
		// 如果角色为空或是指定的替换标签为空，那么则直接返回原始字符串
		if (this.userPurview == null) return html;
		try {
			Node body = this.getBodyNode(html);
			if (body == null) {
				logger.warn("******当前请求页面不含body标签！如果要进行页面级的标签过滤,"
						+ "请务必保证页面中包含层次结构的html和body标签！");
				return html;
			}
			this.visiteAndParse(body, filter == null ? Filter.DELETE : filter);
			return getFinalString(body);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * 获取html代码中的body节点.
	 * @throws ParserException 
	 */
	protected Node getBodyNode(String html) throws ParserException
	{
		Parser parser = Parser.createParser(html, "GB2312");
		NodeList list = parser.parse(new TagNameFilter("body"));
		if (list == null || list.size() < 1) return null;
		return list.elementAt(0);
	}
	
	/**
	 * 使用visitor模式来遍历body区域内的所有标签，并对指定的标签做替换操作.
	 * @param body
	 */
	protected void visiteAndParse(Node body, Filter filter)
	{
		SimpleTagVisitor tv = new SimpleTagVisitor(this.userPurview);
		tv.setFilterMode(filter);
		tv.setModuleUri(uri);
		tv.setTagClass(this.getTagClass());
		tv.setTagLeach(this.getTagLeach());
		body.accept(tv);
	}
	
	/**
	 * 获取特定标签过滤器的class.
	 * 不同的子类要实现该方法，以提供其本身的class，否则系统将不做任何过滤操作。
	 * @return
	 */
	protected abstract Class getTagClass();
	
	/**
	 * 提供针对特定元素的进一步判断的过滤器.如果没有指定则默认全部进行过滤！
	 * @param tag
	 * @return
	 */
	protected abstract TagLeach getTagLeach();
	
	/**
	 * 获取最终过滤和解析后的字符串.
	 * @param body
	 * @return
	 */
	private String getFinalString(Node body)
	{
		if (logger.isDebugEnabled()) logger.debug("[页面过滤]获取当前html的最终解析结果！");
		Node root = body;
		while (root.getParent() != null) {
			root = root.getParent();
		}
		return root.toHtml();
	}
}
