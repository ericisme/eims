package cn.qtone.common.components.syspurview.check.page.freemarker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import cn.qtone.common.components.syspurview.check.page.HtmlUtil;
import cn.qtone.common.components.syspurview.core.user.domain.AbstractUser;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.common.components.syspurview.login.domain.PurviewMap;

/**
 * freemarker的页面级的权限操作集合.
 * 
 * @author 马必强
 *
 */
public class SimplePagePurview implements PagePurviewInter
{
	public Map getPageMap(HttpServletRequest req)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		this.setPageMap(req, map);
		return map;
	}

	public void setPageMap(HttpServletRequest req, Map<String,Object> map)
	{
		// 页面操作的权限集合.
		Map<String,Object> pagePurview = new HashMap<String,Object>();
		map.put(HtmlUtil.FM_MAP_KEY, pagePurview);
		
		// 用户的角色信息获取，如果不存在则直接返回
		AbstractUser user = UserUtil.getUserBean(req);
		if (user == null) return;
		int[] roleId = user.getRoleId();
		if (roleId == null || roleId.length < 1) return;
		
		// 获取当前操作页面上指定的角色ID对应的权限集合
		this.setPurviewMap(pagePurview, user.getPurviewMap(), req.getRequestURI());
	}

	/**
	 * 设置页面权限集合到整体的权限集合中去.
	 * @param pagePurview
	 * @param roleId
	 * @param uri
	 */
	private void setPurviewMap(Map<String,Object> pagePurview, PurviewMap purview, 
			String uri)
	{
		// 将用户所有模块和当前请求URI进行对比，确定其请求模块
		Iterator it = purview.getResourceNames();
		while (it.hasNext()) {
			String key = (String)it.next();
			if (!key.startsWith(uri)) continue;
			setSingleRolePurview(pagePurview, purview.getModulePurview(key));
		}
	}
	
	/**
	 * 设置用户对当前指定模块的权限到权限集合中去.
	 * @param pagePurview
	 * @param moduleMap
	 */
	private void setSingleRolePurview(Map<String,Object> pagePurview, 
			Map<String,String>[] moduleMap)
	{
		// 用户所具有的模块权限
		Map<String,String> tmp = moduleMap[0];
		Iterator it = tmp.keySet().iterator();
		while (it.hasNext()) {
			String key = StringUtils.trimToEmpty((String)it.next());
			pagePurview.put(key, true);
		}
		// 用户不具有的模块权限
		tmp = moduleMap[1];
		it = tmp.keySet().iterator();
		while (it.hasNext()) {
			String key = StringUtils.trimToEmpty((String)it.next());
			pagePurview.put(key, false);
		}
	}
}
