/**
 * 
 */
package cn.qtone.qtoneframework.dao.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页附助类，主要参考springside的core包中的分页实现。此类使用了jdk5中的泛型。
 * 类的提供者（执行new操作）生成Page时，需要写上类型信息。
 * 类的使用者调用getResult()方法时,得到的将是有类型信息的List对像，不需要转型就可以直接用。
 * 
 * @author 林子龙
 * 
 * 
 * @param <T>
 */

@SuppressWarnings("serial")
public class Page<T> implements Serializable {

	public final static int DEFAULT_PAGE_SIZE = 10;

	/**
	 * 每页的记录数
	 */
	private int pageSize = DEFAULT_PAGE_SIZE;

	/**
	 * 当前页第一条数据在List中的位置,从0开始
	 */
	private int start;

	/**
	 * 当前页中存放的记录,类型一般为List
	 */
	private List<T> data;

	/**
	 * 总记录数
	 */
	private int totalCount;

	/**
	 * 构造方法，只构造空页
	 */
	public Page() {
		this(0, 0, DEFAULT_PAGE_SIZE, new ArrayList<T>());
	}

	/**
	 * 默认构造方法
	 * 
	 * @param start
	 *            本页数据在数据库中的起始位置
	 * @param totalSize
	 *            数据库中总记录条数
	 * @param pageSize
	 *            本页容量
	 * @param data
	 *            本页包含的数据
	 */
	public Page(int start, int totalSize, int pageSize, List<T> data) {
		this.pageSize = pageSize;
		this.start = start;
		this.totalCount = totalSize;
		this.data = data;
	}

	/**
	 * 取数据库中包含的总记录数
	 */
	public long getTotalCount() {
		return this.totalCount;
	}

	/**
	 * 取总页数
	 */
	public long getTotalPageCount() {
		if (totalCount % pageSize == 0)
			return totalCount / pageSize;
		else
			return totalCount / pageSize + 1;
	}

	/**
	 * 取每页数据容量
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 当前页中的记录
	 */
	@SuppressWarnings("unchecked")
	public List<T> getResult() {
		return (List<T>) data;
	}

	/**
	 * 取当前页码,页码从1开始
	 */
	public long getCurrentPageNo() {
		return start / pageSize + 1;
	}

	/**
	 * 是否有下一页
	 */
	public boolean hasNextPage() {
		return this.getCurrentPageNo() < this.getTotalPageCount() - 1;
	}

	/**
	 * 是否有上一页
	 */
	public boolean hasPreviousPage() {
		return this.getCurrentPageNo() > 1;
	}

	/**
	 * 获取任一页第一条数据的位置，每页条数使用默认值
	 */
	protected static int getStartOfPage(int pageNo) {
		return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
	}

	/**
	 * 获取任一页第一条数据的位置,startIndex从0开始
	 */
	public static int getStartOfPage(int pageNo, int pageSize) {
		return (pageNo - 1) * pageSize;
	}
}
