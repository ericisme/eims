package cn.qtone.common.base.share;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import cn.qtone.common.utils.base.StringUtil;

public class Paginate
{
	private String selfPath = null; // 文件名称

	private int totalPage = 0; // 总页数

	private int curPage; // 当前页

	private int totalRow = 0; // 总记录数

	private String roll = ""; // 翻页

	private int pagesize = 0; // 每页显示的记录数

	/**
	 * 定义构造器，初始化变量
	 * 
	 * @param totalRow
	 *            总行数
	 * @param pagesize
	 *            页大小
	 * @param curPage
	 *            当前页
	 * @author guyang
	 */

	public Paginate(int totalRow, int pagesize, int curPage)
	{
		this.pagesize = pagesize;
		this.totalRow = totalRow;
		if (totalRow / this.pagesize < 1) {
			this.totalPage = 1;
		} else {
			this.totalPage = totalRow / pagesize;
			if ((this.totalRow % pagesize) != 0) {
				this.totalPage += 1;
			}
		}
		this.curPage = curPage;
		if (this.curPage < 1) {
			this.curPage = 1;
		}
		if (this.curPage > this.totalPage) {
			this.curPage = this.totalPage;
		}
	}

	// 定义构造器，初始化变量
	public Paginate(ResultSet rsRow, int pagesize, int curPage)
	{
		this.pagesize = pagesize; // 1、一页数据条数
		try {
			if (rsRow != null) {
				if (rsRow.next()) {
					this.totalRow = rsRow.getInt(1);
					if (totalRow / pagesize < 1) {
						this.totalPage = 1;
					} else {
						this.totalPage = totalRow / pagesize; // 2、分页数
						if ((this.totalRow % pagesize) != 0) {
							this.totalPage += 1;
						}
					}
				}
				rsRow.close();
				rsRow = null;
			} else {
				this.totalRow = 0;
				this.totalPage = 1;
			}
			this.curPage = curPage; // 3、当前页
			if (this.curPage < 1) {
				this.curPage = 1;
			}
			if (this.curPage > this.totalPage) {
				this.curPage = this.totalPage;
			}
		} catch (SQLException e) {
		}
	}

	public int getTotalRow()
	{
		return this.totalRow;
	}

	public int getTotalPage()
	{
		return this.totalPage;
	}

	public int getCurPage()
	{
		return this.curPage;
	}

	public void setSelfPath(HttpServletRequest request)
	{
		this.selfPath = request.getRequestURI();
	}

	public void setSelfPathWithAction(HttpServletRequest request)
	{
		this.selfPath = request.getRequestURI();
		String action = StringUtil.trim(request.getParameter("action"));
		if (action.intern() != "") this.selfPath = this.selfPath + "?action=" + action + "&";
		else this.selfPath = this.selfPath + "?";
	}

	// 设置翻页
	public void setRoll(String perNum, HttpServletRequest request)
	{
		setBaseRoll(perNum, request);
		roll = roll + " 第" + curPage + "/" + totalPage + "页 共" + totalRow
				+ "条记录";
	}
	
	/**
	 * 直接使用form提交的方式设置翻页.使用此方法时要保证form表单中没有名称和ID
	 * 为url的元素！并且要置翻页代码于FORM表单内部！
	 * 
	 * add by 马必强
	 * @param request
	 */
	public void setRoll(HttpServletRequest request)
	{
		if (this.selfPath == null) {
			this.setSelfPath(request);
		}
		String slt = "document.getElementById('url')";
		String tmp = "javascript:" + slt + ".form.action='" + selfPath + "?page=";
		if (curPage != 1) {
			roll = "<a href=\"" + tmp + "1';" + slt + ".form.submit();\">首页</a>";
		}
		if (curPage > 1) {
			roll = roll + " <a href=\"" + tmp + (curPage - 1) + "';" + slt + ".form.submit();\">上一页</a>";
		}
		if (curPage < totalPage) {
			roll = roll + " <a href=\"" + tmp + (curPage + 1) + "';" + slt + ".form.submit();\">下一页</a>";
		}
		if (curPage != totalPage) {
			roll = roll + " <a href=\"" + tmp + totalPage + "';" + slt + ".form.submit();\">末页</a>";
		}
		roll = roll + "&nbsp;<select name=\"url\" id='url' onChange=\"javascript:this.form.action="
			+ "this.form.url.value;this.form.submit();\">";
		String urlOption = "";
		int start = 1, end = this.totalPage;
		if (this.totalPage > 31) {
			start = curPage - 30;
			if (start <= 0) {
				start = 1;
			}
			end = curPage + 30;
			if (end > totalPage) {
				end = totalPage;
			}
		}
		for (int i = start; i <= end; i++) {
			String selected = "";
			if (i == this.curPage) {
				selected = "selected";
			}
			urlOption = urlOption + "<option value=\"" + selfPath + "?page="
					+ i + "\" " + selected + ">" + i + "</option>";

		}
		urlOption = urlOption + "</select>";
		roll = roll + urlOption;
		roll = roll + " 第" + curPage + "/" + totalPage + "页 共" + totalRow
		+ "条记录";
	}
	
	/**
	 * 直接使用form提交的方式设置翻页.使用此方法时要保证form表单中没有名称和ID
	 * 为url的元素！并且要置翻页代码于FORM表单内部！同时该方法还附加了对特定的操作的
	 * 参数，一般如action的值
	 * 
	 * add by 马必强
	 * @param request
	 */
	public void setRollWithAction(HttpServletRequest request)
	{
		if (this.selfPath == null) {
			this.setSelfPathWithAction(request);
		}
		String slt = "document.getElementById('url')";
		String tmp = "javascript:" + slt + ".form.action='" + selfPath + "page=";
		if (curPage != 1) {
			roll = "<a href=\"" + tmp + "1';" + slt + ".form.submit();\">首页</a>";
		}
		if (curPage > 1) {
			roll = roll + " <a href=\"" + tmp + (curPage - 1) + "';" + slt + ".form.submit();\">上一页</a>";
		}
		if (curPage < totalPage) {
			roll = roll + " <a href=\"" + tmp + (curPage + 1) + "';" + slt + ".form.submit();\">下一页</a>";
		}
		if (curPage != totalPage) {
			roll = roll + " <a href=\"" + tmp + totalPage + "';" + slt + ".form.submit();\">末页</a>";
		}
		roll = roll + "&nbsp;<select name=\"url\" id='url' onChange=\"javascript:this.form.action="
			+ "this.form.url.value;this.form.submit();\">";
		String urlOption = "";
		int start = 1, end = this.totalPage;
		if (this.totalPage > 31) {
			start = curPage - 30;
			if (start <= 0) {
				start = 1;
			}
			end = curPage + 30;
			if (end > totalPage) {
				end = totalPage;
			}
		}
		for (int i = start; i <= end; i++) {
			String selected = "";
			if (i == this.curPage) {
				selected = "selected";
			}
			urlOption = urlOption + "<option value=\"" + selfPath + "page="
					+ i + "\" " + selected + ">" + i + "</option>";

		}
		urlOption = urlOption + "</select>";
		roll = roll + urlOption;
		roll = roll + " 第" + curPage + "/" + totalPage + "页 共" + totalRow
		+ "条记录";
	}

	// 得到翻页
	public String getRoll()
	{
		return roll;
	}

	/**
	 * 得到翻页 add by lzc on 2005-10-21
	 * 
	 * @return String
	 * @author lzc
	 */
	public String getFillRoll()
	{
		return roll;
	}

	/**
	 * 增加了输入框的翻页  add by 马必强
	 * 
	 * @param perNum
	 * @param request
	 */
	public void setRollWithInput(String perNum, HttpServletRequest request)
	{
		setBaseRoll(perNum, request);
		roll += "&nbsp;<input type=\"text\" name=\"gotopage\" size=\"2\" style=\"border: 1px solid #403F3F\" "
				+ "onKeyDown=\"if(event.keyCode==13) {window.location='"
				+ selfPath + "?page='+this.value+'" + perNum + "';}\">";

		roll = roll + " 第" + curPage + "/" + totalPage + "页 共" + totalRow
				+ "条记录";
	}
	
	private void setBaseRoll(String perNum, HttpServletRequest request)
	{
		if (this.selfPath == null) {
			this.setSelfPath(request);
		}
		if (curPage != 1) {
			roll = "<a href=" + selfPath + "?page=1" + perNum + ">首页</a>";
		}
		if (curPage > 1) {
			roll = roll + " <a href=" + selfPath + "?page=" + (curPage - 1)
					+ perNum + ">上一页</a>";
		}
		if (curPage < totalPage) {
			roll = roll + " <a href=" + selfPath + "?page=" + (curPage + 1)
					+ perNum + ">下一页</a>";
		}
		if (curPage != totalPage) {
			roll = roll + " <a href=" + selfPath + "?page=" + totalPage
					+ perNum + ">末页</a>";

			// ******************
		}
		roll = roll
				+ "&nbsp;<select name=\"url\" onChange=\"location.href=this.form.url.value\">";
		String urlOption = "";
		int start = 1, end = this.totalPage;
		if (this.totalPage > 31) {
			start = curPage - 30;
			if (start <= 0) {
				start = 1;
			}
			end = curPage + 30;
			if (end > totalPage) {
				end = totalPage;
			}
		}
		for (int i = start; i <= end; i++) {
			String selected = "";
			if (i == this.curPage) {
				selected = "selected";
			}
			urlOption = urlOption + "<option value=\"" + selfPath + "?page="
					+ i + perNum + "\" " + selected + ">" + i + "</option>";

		}
		urlOption = urlOption + "</select>";
		roll = roll + urlOption;
	}
}
