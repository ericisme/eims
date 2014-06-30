package cn.qtone.common.mvc.controller.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

/**
 * 后台管理的mvc模型控制器基类，定义了后台管理的各个基本方法.
 * 
 * @author 马必强
 *
 */
public abstract class ManageController extends NormalController
{
	private String createPage; // 添加页面
	
	private String editPage; // 编辑页面
	
	private String showPage; // 信息查看页面
	
	private String listTplPage; // 列表查询模板页面
	
	private int listRows = 20; // 列表页面每也显示的行数，默认是20行

	public String getCreatePage()
	{
		return createPage;
	}

	public void setCreatePage(String createPage)
	{
		this.createPage = createPage;
	}

	public String getEditPage()
	{
		return editPage;
	}

	public void setEditPage(String editPage)
	{
		this.editPage = editPage;
	}

	public int getListRows()
	{
		return listRows;
	}

	public void setListRows(int listRows)
	{
		this.listRows = listRows;
	}

	public String getListTplPage()
	{
		return listTplPage;
	}

	public void setListTplPage(String listTplPage)
	{
		this.listTplPage = listTplPage;
	}

	public String getShowPage()
	{
		return showPage;
	}

	public void setShowPage(String showPage)
	{
		this.showPage = showPage;
	}
	
	/**
	 * 通用的列表查询方法.
	 */
	public abstract ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	/**
	 * 通用的新增对象的输入界面.
	 */
	public abstract ModelAndView create(HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	/**
	 * 通用的编辑对象的修改界面.
	 */
	public abstract ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	/**
	 * 通用的只读显示对象细节的界面.
	 */
	public abstract ModelAndView show(HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	/**
	 * 通用的新增对象的保存.
	 */
	public abstract ModelAndView save(HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	/**
	 * 通用的修改对象的保存.
	 */
	public abstract ModelAndView update(HttpServletRequest request,
			HttpServletResponse response) throws Exception;
	
	/**
	 * 通用的删除单个或多个对象.
	 */
	public abstract ModelAndView remove(HttpServletRequest request,
			HttpServletResponse response) throws Exception;
}
