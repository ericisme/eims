package cn.qtone.common.components.syspurview.core.system;

import java.util.List;

import org.apache.log4j.Logger;

import cn.qtone.common.components.syspurview.core.system.dao.SystemMapper;
import cn.qtone.common.components.syspurview.core.system.domain.SystemBean;
import cn.qtone.common.mvc.dao.BaseDAO;

/**
 * 子系统信息的缓存.
 * 
 * @author 马必强
 *
 */
public class SystemCache extends BaseDAO
{
	private static final Logger log = Logger.getLogger(SystemCache.class);

	private static SystemCache sys = new SystemCache();
	
	private List sysList;
	
	protected SystemCache()
	{
		
	}
	
	public static SystemCache getInstance()
	{
		return sys;
	}
	
	/**
	 * 获取默认系统的系统唯一ID，通常是认为排第一个的就是默认系统
	 * @return
	 */
	public int getDefaultSystemId()
	{
		if (this.sysList == null) this.loadList();
		return ((SystemBean)this.sysList.get(0)).getSystemId();
	}
	
	/**
	 * 获取所有的子系统列表.
	 * @return
	 */
	public List getList()
	{
		if (this.sysList == null) this.loadList();
		return this.sysList;
	}
	
	/**
	 * 获取指定的子系统信息Bean.
	 * @param sysId
	 * @return
	 */
	public SystemBean getSystem(int sysId)
	{
		if (this.sysList == null) this.loadList();
		if (log.isInfoEnabled()) log.info("[子系统缓存]:获取子系统" + sysId);
		for (Object obj : this.sysList) {
			if (((SystemBean)obj).getSystemId() == sysId) {
				return (SystemBean)obj;
			}
		}
		return null;
	}
	
	/**
	 * 获取指定的子系统信息Bean.
	 * @param sysFlag
	 * @return
	 */
	public SystemBean getSystem(String sysFlag)
	{
		if (this.sysList == null) this.loadList();
		if (log.isInfoEnabled()) log.info("[子系统缓存]:获取子系统" + sysFlag);
		for (Object obj : this.sysList) {
			if (((SystemBean)obj).getSysFlag().equals(sysFlag)) {
				return (SystemBean)obj;
			}
		}
		return null;
	}
	
	/**
	 * 根据子系统标记来获取子系统的唯一ID，如果不存在则返回0
	 * @param sysFlag
	 * @return
	 */
	public int getSystemId(String sysFlag)
	{
		SystemBean bean = this.getSystem(sysFlag);
		return bean == null ? 0 : bean.getSystemId();
	}
	
	/**
	 * 重新加载子系统列表.
	 *
	 */
	public void reload()
	{
		this.loadList();
	}
	
	/**
	 * 加载子系统列表.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void loadList()
	{
		synchronized(SystemCache.class) {
			if (log.isInfoEnabled()) log.info("[子系统缓存]:加载子系统的列表~~~");
			String sql = "SELECT sysId,sysName,sysFlag,sysURL,sysTarget,sysSeq FROM "
				+ "sys_system ORDER BY sysSeq ASC,sysId ASC";
			this.sysList = this.getJdbcTemplate().query(sql, new SystemMapper());
		}
	}
}
