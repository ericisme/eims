package cn.qtone.common.components.syspurview.core.module.dao.oracle;

import java.util.List;

import cn.qtone.common.components.syspurview.core.module.dao.BaseModuleDAO;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 模块管理设置的oracle数据库实现类。
 * 
 * @author 马必强
 *
 */
public class OracleModuleDAO extends BaseModuleDAO
{
	/**
	 * 获取指定系统中指定名称（模糊查询）的所有模块.
	 */
	public List query(int sysId, String moduleName)
	{
		if (log.isInfoEnabled()) log.info("根据子系统和模块名称进行查询~~~");
		return this.exeQuery("WHERE sysId=" + sysId + " AND moduleName LIKE '%"
				+ StringUtil.toDBFilter(moduleName) + "%' ORDER BY moduleSeq");
	}
}
