package cn.qtone.common.mvc.dao;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * DAO查询的页对象.<br>
 * 包括设置获取的开始位置、每页的大小、总的记录数和查询的列表结果。
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public class Page
{
	public static int DEFAULT_PAGE_SIZE = 20;
	
	public static int DEFAULT_STUDENT_SIZE = 20;
	
	private int startIndex; // 对象获取的开始位置，应该总是从1开始
	
	private int pageSize = DEFAULT_PAGE_SIZE; // 每页获取的页大小，默认是15
	
	private int totals; // 总的记录数
	
	private List<?> result; // 查询的结果
	
	private String paginate; // 分页代码
	
	private List<?> assistBusiness;//辅助

	
	
	public String getPaginate()
	{
		return paginate;
	}

	public void setPaginate(String paginate)
	{
		this.paginate = StringUtils.trimToEmpty(paginate);
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public List<?> getResult()
	{
		return result;
	}

	public void setResult(List<?> result)
	{
		this.result = result;
	}

	public int getStartIndex()
	{
		return startIndex;
	}

	/**
	 * 查询的开始位置总是从1开始.
	 * @param startIndex
	 */
	public void setStartIndex(int startIndex)
	{
		this.startIndex = startIndex;
		if (this.startIndex <= 0) this.startIndex = 1;
	}

	public int getTotals()
	{
		return totals;
	}

	public void setTotals(int totals)
	{
		this.totals = totals;
	}

	public List<?> getAssistBusiness() {
		return assistBusiness;
	}

	public void setAssistBusiness(List<?> assistBusiness) {
		this.assistBusiness = assistBusiness;
	}
	
	//add by zhangdan
	/**
	 * 构造方法，只构造空页.
	 */
	public Page() {
		this(0, 0, DEFAULT_PAGE_SIZE, new ArrayList());
	}
	
	/**
	 * 默认构造方法.
	 *
	 * @param startIndex	 本页数据在数据库中的起始位置
	 * @param totals 数据库中总记录条数
	 * @param pageSize  本页容量
	 * @param result	  本页包含的数据
	 * by zhangdan
	 */
	public Page(int startIndex, int totals, int pageSize, List<?> result) {
		this.pageSize = pageSize;
		this.startIndex = startIndex;
		this.totals = totals;
		this.result = result;
	}
	
	/**
	 * 获取任一页第一条数据在数据集的位置.
	 *
	 * @param pageNo   从1开始的页号
	 * @param pageSize 每页记录条数
	 * @return 该页第一条数据
	 */
	public static int getStartOfPage(int pageNo, int pageSize) {
		return (pageNo - 1) * pageSize;
	}
}
