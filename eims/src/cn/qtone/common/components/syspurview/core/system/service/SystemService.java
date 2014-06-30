package cn.qtone.common.components.syspurview.core.system.service;

import org.apache.log4j.Logger;

import cn.qtone.common.components.syspurview.core.system.dao.ISystemDAO;
import cn.qtone.common.components.syspurview.core.system.domain.SystemBean;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.service.ServiceMsg;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 子系统的业务逻辑处理类。
 * 
 * @author 马必强
 *
 */
public class SystemService
{
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(SystemService.class);

	private ISystemDAO dao;
	
	/**
	 * 获取所有的子系统信息.
	 * @return
	 */
	public Page query()
	{
		return this.dao.query();
	}
	
	/**
	 * 执行子系统的添加操作.
	 * @param sys
	 * @return
	 */
	public ServiceMsg add(SystemBean sys)
	{
		ServiceMsg sms = new ServiceMsg();
		if (!checkSysInfo(sms, sys)) return sms;
		if (sys.getSysFlag().intern() == "") {
			sms.setMessage("子系统的唯一标记不能为空！");
			return sms;
		}
		if (this.dao.getSystemIdByFlag(sys.getSysFlag()) > 0) {
			sms.setMessage("当前子系统的标记已被其他子系统使用！");
			return sms;
		}
		this.dao.addSystem(sys);
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 更新子系统信息
	 * @param sys
	 * @return
	 */
	public ServiceMsg update(SystemBean sys)
	{
		ServiceMsg sms = new ServiceMsg();
		if (!checkSysInfo(sms, sys)) return sms;
		if (sys.getSystemId() <= 0) {
			sms.setMessage("子系统的ID非法，无法完成更新！");
			return sms;
		}
		this.dao.updateSystem(sys);
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 删除特定的子系统信息.
	 * @param sysIds
	 * @return
	 */
	public ServiceMsg remove(String[] sysIds)
	{
		ServiceMsg sms = new ServiceMsg();
		if (sysIds == null) {
			sms.setMessage("必须要指定要删除的子系统的唯一ID！");
			return sms;
		}
		int[] ids = StringUtil.parseInt(sysIds, -1);
		if (StringUtil.include(ids, -1)) {
			sms.setMessage("要删除的子系统的ID中含有非法ID！");
			return sms;
		}
		// 获取包含了模块的子系统
		int[] haveModuleId = this.dao.getHaveModuleSystemId();
		int[] delId = StringUtil.exclude(ids, haveModuleId);
		if (log.isInfoEnabled()) {
			log.info("最终要删除的子系统ID为：" + StringUtil.join(delId, ","));
		}
		if (delId.length > 0) {
			this.dao.remove(delId);
			sms.addObject("Refresh", true);
		}
		if (delId.length < ids.length) {
			sms.addObject("NoDel", StringUtil.join(StringUtil.exclude(ids, delId), ","));
		}
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 获取指定ID的子系统信息
	 * @param sysId
	 * @return
	 */
	public ServiceMsg getSystemBean(String sysId)
	{
		ServiceMsg sms = new ServiceMsg();
		int id = StringUtil.parseInt(sysId, -1);
		if (id <= -1) {
			sms.setMessage("非法的子系统ID，无法进行编辑！");
			return sms;
		}
		sms.addObject("system", this.dao.getSystem(id));
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 判断指定的标记是否已经存在
	 * @param flag
	 * @return
	 */
	public ServiceMsg isExist(String flag)
	{
		ServiceMsg sms = new ServiceMsg();
		if (StringUtil.trim(flag).intern() == "") {
			sms.setMessage("指定的标记不能为空！");
			return sms;
		}
		sms.addObject("sys", this.dao.getSystemIdByFlag(flag));
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 检查子系统的必要信息.添加和更新时的公用特性
	 * @param sms
	 * @return
	 */
	private boolean checkSysInfo(ServiceMsg sms, SystemBean sys)
	{
		if (sys.getSysName().intern() == "") {
			sms.setMessage("子系统的名称不能为空！");
			return false;
		} else if (sys.getSysSequence() < 1 || sys.getSysSequence() > 999) {
			sms.setMessage("子系统的排序序号必须在1到999之间！");
			return false;
		} else if (!SystemBean.isRightTarget(sys.getSysTarget())) {
			sms.setMessage("子系统的窗口特性标识错误！");
			return false;
		}
		return true;
	}

	public ISystemDAO getDao()
	{
		return dao;
	}

	public void setDao(ISystemDAO dao)
	{
		this.dao = dao;
	}
}
