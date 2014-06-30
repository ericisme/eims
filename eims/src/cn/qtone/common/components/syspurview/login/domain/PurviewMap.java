package cn.qtone.common.components.syspurview.login.domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 角色的权限存储对象.<br>
 * 
 * 角色的权限分为两种，一种是该角色具有的模块和其功能集合，另外一种是该角色
 * 具有的模块和其不具有的功能集合。而每个模块是采用其URL地址做为关键字检索。
 * 
 * 比如一个用户管理模块，其有功能：add  update query delete，而某一角色
 * 只有该模块的add和query权限，那么该角色的权限集合则表示为：
 * 
 * Map<String,Map[]> rolePurview = new HashMap<String,Map[]>(); // 角色的权限集合
 * Map<String,String> havePw = new HashMap<String,String>(); // 角色具有的权限集合
 * Map<String,String> noPw = new HashMap<String,String>(); // 角色不具有的权限集合
 * 
 * havePw.put("add", "添加"); 
 * havePw.put("query", "查询"); 
 * 
 * noPw.put("update", "更新");
 * noPw.put("delete", "删除"); 
 * 
 * rolePurview.put("/syspurview/user.do?action=list", new HashMap[]{havePw,noPw});
 * 
 * @author 马必强
 * @version 1.0
 * 
 */
public class PurviewMap implements java.io.Serializable
{
	private static final long serialVersionUID = -7147577906126569663L;

	private static Logger log = Logger.getLogger(PurviewMap.class);

	private Map<String, Map<String,String>[]> userPurview;
	
	public PurviewMap()
	{
		userPurview = new HashMap<String, Map<String,String>[]>();
	}
	
	/**
	 * 获取当前用户的所有具有权限的模块资源名称.
	 * 对过滤器而言就是URL，对AOP而言就是Class的名称
	 * @return
	 */
	public Iterator getResourceNames()
	{
		return this.userPurview.keySet().iterator();
	}

	/**
	 * 获取当前用户对指定模块的权限集合（具有的和不具有的权限集合）.
	 * 
	 * @param resourceName 模块的资源名称，对过滤器而言就是URL，对AOP而言就是Class的名称
	 * @return
	 */
	public Map<String,String>[] getModulePurview(String resourceName)
	{
		if (log.isDebugEnabled()) log.debug("[用户权限]获取模块[" + resourceName + "]的所有权限！");
		return this.userPurview.get(resourceName);
	}

	/**
	 * 添加一个模块的权限集合.
	 * 
	 * @param resourceName 模块的资源名称，对过滤器而言就是URL，对AOP而言就是Class的名称
	 * @param modPurview 当前资源的操作集合
	 */
	public void addModulePurview(String resourceName, Map<String,String>[] modPurview)
	{
		if (log.isDebugEnabled()) log.debug("[用户权限]添加模块[" + resourceName + "]的权限！");
		this.userPurview.put(resourceName, modPurview);
	}
	
	/**
	 * 判断当前用户对指定资源是否具有指定的操作权限.该方法只支持以URL为资源名称的权限判断。
	 * 
	 * 由于系统是根据URI地址来判断模块的，而权限则是使用URL来进行存储的，所以这里的
	 * 判断是根据指定的用户的所有模块权限中，是否有包含该URI地址的模块，然后再根据
	 * operate进行权限的操作判断。 
	 * 
	 * @param resourceName 资源名称（通常是功能模块的URL地址）
	 * @param operate 操作名称标识（比如add、update等）
	 * @return
	 */
	public boolean isAllowed(String resourceName, String operate)
	{
		Iterator it = userPurview.keySet().iterator();
		while (it.hasNext()) {
			String key = (String)it.next();
			// 如果模块的URL是以指定的URI开头，则表示访问的是当前模块
			if (key.startsWith(resourceName)) {
				// 获取当前模块的操作权限集合,索引为0表示当前角色具有的权限，为1表示当前角色没有的权限
				Map<String,String>[] tmap = userPurview.get(key);
				// 如果当前模块不为空并且有指定的操作权限，则返回true
				if (tmap[0] != null && tmap[0].get(operate) != null) return true;
			}
		}
		return false;
	}
}
