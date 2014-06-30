package cn.qtone.common.components.syspurview.core.module.dao;

import java.util.List;
import java.util.Map;

import cn.qtone.common.components.syspurview.core.module.domain.Module;
import cn.qtone.common.components.syspurview.core.module.domain.ModuleFunc;
import cn.qtone.common.components.syspurview.core.module.domain.ModuleSingleFunc;


/**
 * 模块管理设置的DAO接口。
 * 
 * @author 马必强
 *
 */
public interface IModuleDAO
{
	/**
	 * 获取指定系统中的所有一级模块（菜单和功能模块）
	 */
	List query(int sysId);
	
	/**
	 * 获取指定系统中指定名称（模糊查询）的所有模块.
	 */
	List query(int sysId, String moduleName);
	
	/**
	 * 根据指定的token获取其直接下属的模块（菜单和功能模块）
	 */
	List query(String token);
	
	/**
	 * 检查系统中已经存在于指定的列表中的token，以,连接返回
	 * @param tokens
	 * @return
	 */
	String checkTokenInThese(String tokens);
	
	/**
	 * 添加指定的模块.
	 * @param mod
	 */
	void addModule(Module mod);
	
	/**
	 * 获取一个子系统下的所有可用菜单.
	 * @param sysId
	 * @return
	 */
	List queryMenu(int sysId);
	
	/**
	 * 根据指定的token值获取模块信息.
	 * @param token
	 * @return
	 */
	Module getModule(String token);
	
	/**
	 * 根据ID获取模块的信息.
	 * @param moduleId
	 * @return
	 */
	Module getModule(int moduleId);
	
	/**
	 * 更新模块信息.
	 * @param mod
	 */
	void updateModule(Module mod);
	
	/**
	 * 根据指定的父菜单的token获取所有的子模块的信息.
	 * 返回结果采用Id-Token的键值对应。
	 * @param parentToken
	 * @return
	 */
	Map<Integer,String> getAllChilds(String parentToken);
	
	/**
	 * 更新指定token的模块的所有子模块的token前缀
	 * @param orginTokenPrefix 原始的token前缀
	 * @param newTokenPrefix 更新为新的前缀
	 * @param moduleId 当前更新的模块的ID
	 * @param sysId 模块所属的子系统ID
	 */
	void updateChildToken(String orginTokenPrefix, String newTokenPrefix, 
			int moduleId, int sysId);
	
	/**
	 * 获取指定token的模块，唯一
	 * @param pMId
	 * @return
	 */
	int remove(String mToken);
	
	/**
	 * 获取指定模块的功能模块信息.
	 * @param mId
	 * @return
	 */
	ModuleFunc getModuleFunc(int mId);
	
	/**
	 * 添加模块的功能，批处理添加
	 * @param moduleId
	 * @param funcs
	 */
	void batchAddModuleFunc(int moduleId, List funcs);
	
	/**
	 * 更新模块的功能，批处理更新
	 * @param moduleId
	 * @param funcs
	 */
	void batchUpdateModuleFunc(int moduleId, List funcs);
	
	/**
	 * 更新单个功能的功能.
	 * @param moduleId
	 * @param func
	 * @return
	 */
	int singleUpdateModuleFunc(int moduleId, ModuleSingleFunc func);
	
	/**
	 * 删除指定模块的指定功能.
	 * @param moduleId
	 * @param funcId
	 */
	void removeModuleFunc(int moduleId, int funcId);
}
