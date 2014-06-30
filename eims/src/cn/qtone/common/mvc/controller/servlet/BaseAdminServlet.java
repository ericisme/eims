package cn.qtone.common.mvc.controller.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * 通用的普通后台管理基类servlet，实现了action到方法的切换.
 * 
 * @author 马必强
 *
 */
public abstract class BaseAdminServlet extends NormalServlet
{
	private String actionName = "action"; // 表示动作的参数名称
	
	// 基本的操作，如添加、删除等
	protected enum ActionName {
		list, add, save, edit, update, remove
	}
	
	public String getActionName()
	{
		return actionName;
	}

	public void setActionName(String actionName)
	{
		this.actionName = actionName;
	}

	@Override
	protected void doRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String action = StringUtils.trimToEmpty(request.getParameter(this.getActionName())).toLowerCase();
		if (ActionName.list.toString().equals(action)) {
			this.list(request, response);
		} else if (ActionName.add.toString().equals(action)) {
			this.add(request, response);
		} else if (ActionName.save.toString().equals(action)) {
			this.save(request, response);
		} else if (ActionName.edit.toString().equals(action)) {
			this.edit(request, response);
		} else if (ActionName.update.toString().equals(action)) {
			this.update(request, response);
		} else if (ActionName.remove.toString().equals(action)) {
			this.remove(request, response);
		} else {
			this.doAction(request, response);
		}
	}

	/**
	 * 后台管理的列表显示操作.
	 */
	protected abstract void list(HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	/**
	 * 后台管理的添加页面显示操作.
	 */
	protected abstract void add(HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	/**
	 * 后台管理的添加到数据库的操作.
	 */
	protected abstract void save(HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	/**
	 * 后台管理的编辑页面显示操作.
	 */
	protected abstract void edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	/**
	 * 后台管理的实际更新操作，更新到数据库.
	 */
	protected abstract void update(HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	/**
	 * 后台管理的对象删除（一个或多个）操作.
	 */
	protected abstract void remove(HttpServletRequest request,
			HttpServletResponse response) throws Exception;

	/**
	 * 后台管理的其他操作.
	 */
	protected abstract void doAction(HttpServletRequest request,
			HttpServletResponse response) throws Exception;
}
