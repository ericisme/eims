package cn.qtone.common.components.syspurview.check.page.freemarker;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 当使用freemarker进行页面解析时的页面操作权限的过滤接口.
 * 
 * @author 马必强
 *
 */
public interface PagePurviewInter
{
	/**
	 * 获取当前用户对请求页面的页面级的权限map集合.<br>
	 * 
	 * 该map返回的数据结构如下：
	 * 当前页面的功能权限集合（有的功能）
	 * Map<String,Object> p = new HashMap<String,Object>();
	 * p.put("add", true);
	 * p.put("edit", true);
	 * p.put("delete", false);
	 * 最终存放并返回的结果
	 * map.put("sys_purview", p);
	 * 
	 * 那么在ftl文件中使用的时候就应该是：
	 * <#if sys_purview.add?default(false)>
	 * 	<input type="button" style="display:" …… />
	 * <#else>
	 * 	<input type="button" style="display:none" …… />
	 * </#if>
	 * 
	 * 当然你也可以直接把else这一段去掉，对应就是不显示add按钮了，如果没有
	 * 权限的话，另外也可以考虑使用灰色来屏蔽按钮操作等。注意权限集合中存放了
	 * 当前模块的所有操作权限，即设置到该模块的所有功能，如果用户对某一功能
	 * 没有权限的话，那么系统就会返回false。
	 * 
	 * @param req
	 * @return
	 */
	Map getPageMap(HttpServletRequest req);
	
	/**
	 * 设置当前用户对请求页面的页面级的权限map集合.<br>
	 * 
	 * 这个和上面的getPurviewMap方法一样，不同的是这个需要指定已有的map，而上
	 * 一个会自动新生成一个map并返回。
	 * 
	 * @param req
	 * @param map
	 */
	void setPageMap(HttpServletRequest req, Map<String,Object> map);
}
