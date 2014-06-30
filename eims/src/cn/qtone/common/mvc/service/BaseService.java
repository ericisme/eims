package cn.qtone.common.mvc.service;

import org.apache.log4j.Logger;


/**
 * 所有功能模块的业务类的基类，主要提供日志记录对象和dao的设置功能.
 * 
 * @author 马必强
 *
 */
public abstract class BaseService
{
	protected final static Logger log = Logger.getLogger(BaseService.class);
	
	protected Object dao; // 与该业务类对应的DAO

	public Object getDao()
	{
		return dao;
	}

	public void setDao(Object dao)
	{
		this.dao = dao;
	}
	
	/**
	 * 获取当前模块对应的特定DAO.
	 * @return
	 */
	public abstract Object getMyDAO();
	
	/**
	 * 获取业务逻辑返回的信息封装器.
	 * @return
	 */
	public ServiceMsg getServiceMsg()
	{
		return new ServiceMsg();
	}
}
