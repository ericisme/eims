package cn.qtone.common.components.syspurview.core.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.components.syspurview.base.controller.BaseManageController;
import cn.qtone.common.components.syspurview.core.system.SystemCache;
import cn.qtone.common.components.syspurview.core.system.domain.SystemBean;
import cn.qtone.common.components.syspurview.core.system.service.SystemService;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.service.ServiceMsg;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 子系统管理的中心控制器。
 * 
 * @author 马必强
 *
 */
public class SystemController extends BaseManageController
{
	/**
	 * 添加页面显示.
	 */
	@Override
	public ModelAndView create(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		if (log.isInfoEnabled()) log.info("返回子系统的添加页面~~~");
		int seq = SystemCache.getInstance().getList().size() + 1;
		return new ModelAndView(this.getCreatePage(), "seq", seq);
	}

	/**
	 * 显示子系统的编辑页面
	 */
	@Override
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String sysId = request.getParameter("sysId");
		if (log.isInfoEnabled()) log.info("编辑ID为" + sysId + "的子系统！");
		ServiceMsg sms = this.getService().getSystemBean(sysId);
		if (!sms.isSuccess()) {
			return new ModelAndView(new AjaxView(false, sms.getMessage()));
		} else {
			return new ModelAndView(this.getEditPage(), "system", sms.getObject("system"));
		}
	}

	/**
	 * 显示子系统首页.
	 */
	@Override
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		if (log.isInfoEnabled()) log.info("子系统管理首页显示~~~");
		return new ModelAndView(this.getIndexPage(), "configer", this.getConfiger());
	}

	/**
	 * 子系统查询列表获取，不分页，一个系统的子系统不会很多的
	 */
	@Override
	public ModelAndView query(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		if (log.isInfoEnabled()) log.info("子系统的列表获取~~");
		Page pag = this.getService().query();
		return new ModelAndView(this.getListTplPage(), "Systems", pag.getResult());
	}

	/**
	 * 删除指定的子系统
	 */
	@Override
	public ModelAndView remove(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String[] sysIds = request.getParameterValues("sysId");
		if (log.isInfoEnabled()) {
			log.info("要删除的子系统ID为：" + StringUtil.join(sysIds, ","));
		}
		ServiceMsg sms = this.getService().remove(sysIds);
		if (sms.isSuccess()) {
			AjaxView view = new AjaxView("子系统删除成功！");
			view.setProperty("NoDel", (String)sms.getObject("NoDel"));
			view.setProperty("refresh", sms.getObject("Refresh") != null);
			return new ModelAndView(view);
		} else {
			return new ModelAndView(new AjaxView(false, sms.getMessage()));
		}
	}
	
	/**
	 * 子系统的唯一标记检测.
	 *
	 */
	public ModelAndView checkFlag(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String flag = request.getParameter("flag");
		ServiceMsg sms = this.getService().isExist(flag);
		AjaxView view = new AjaxView(sms.isSuccess(), sms.getMessage());
		view.setProperty("exist", (Integer)sms.getObject("sys") != 0);
		return new ModelAndView(view);
	}

	/**
	 * 添加子系统
	 */
	@Override
	public ModelAndView save(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		SystemBean sys = (SystemBean)this.getCommandObject(request, SystemBean.class);
		if (log.isInfoEnabled()) {
			log.info("获取添加的子系统信息Bean：[" + StringUtil.reflectObj(sys) + "]");
		}
		ServiceMsg sms = this.getService().add(sys);
		if (sms.isSuccess()) {
			return new ModelAndView(new AjaxView("子系统添加成功！"));
		} else {
			return new ModelAndView(new AjaxView(false, sms.getMessage()));
		}
	}

	@Override
	public ModelAndView show(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		return null;
	}

	/**
	 * 刷新缓存。
	 */
	public ModelAndView refreshCache(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		SystemCache.getInstance().reload();
		return new ModelAndView(new AjaxView("子系统缓存刷新成功！"));
	}

	/**
	 * 执行子系统的信息更新
	 */
	@Override
	public ModelAndView update(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		SystemBean sys = (SystemBean)this.getCommandObject(request, SystemBean.class);
		if (log.isInfoEnabled()) {
			log.info("要更新的子系统信息为：" + StringUtil.reflectObj(sys));
		}
		ServiceMsg sms = this.getService().update(sys);
		if (sms.isSuccess()) {
			return new ModelAndView(new AjaxView("子系统信息更新成功！"));
		} else {
			return new ModelAndView(new AjaxView(false, sms.getMessage()));
		}
	}
	
	/**
	 * 获取子系统的业务处理bean.
	 * @return
	 */
	private SystemService getService()
	{
		return (SystemService)this.getServiceBean();
	}
}
