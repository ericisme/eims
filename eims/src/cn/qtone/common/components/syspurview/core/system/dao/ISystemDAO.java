package cn.qtone.common.components.syspurview.core.system.dao;

import cn.qtone.common.components.syspurview.core.system.domain.SystemBean;
import cn.qtone.common.mvc.dao.Page;


/**
 * 子系统操作处理的接口DAO。
 * 
 * @author 马必强
 *
 */
public interface ISystemDAO
{
	/**
	 * 查询所有子系统.
	 * @return
	 */
	public Page query();
	
	/**
	 * 添加子系统
	 * @param sys
	 */
	public void addSystem(SystemBean sys);
	
	/**
	 * 获取指定ID的子系统信息.
	 * @param sysId
	 * @return
	 */
	public SystemBean getSystem(int sysId);
	
	/**
	 * 更新子系统信息
	 * @param sys
	 */
	public void updateSystem(SystemBean sys);
	
	/**
	 * 删除指定的子系统
	 * @param sysId
	 */
	public void remove(int[] sysId);
	
	/**
	 * 获取已经包含了模块的子系统的ID.
	 * @return
	 */
	public int[] getHaveModuleSystemId();
	
	/**
	 * 根据标记名称来确定子系统，用来检测系统标记的唯一性.
	 * @param flag
	 * @return
	 */
	public int getSystemIdByFlag(String flag);
}
