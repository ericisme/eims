/**
 * 
 */
package cn.qtone.qtoneframework.dao.support;

/**
 * 封装从业务层到持久层的分页VO
 * 
 * @author 林子龙 
 */
public class PageVO {
	// 第几页
	private Integer startPage;

	private int pageSize = Page.DEFAULT_PAGE_SIZE;

	private SqlBuilder sqlBuilder = new DefaultSqlBuilder();

	/**
	 * 获取当前页第一条数据的位置
	 * 
	 * @return
	 */
	public int getStartOfPage() {
		return (startPage - 1) * pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public SqlBuilder getSqlBuilder() {
		return sqlBuilder;
	}

	public void setSqlBuilder(SqlBuilder sqlBuilder) {
		this.sqlBuilder = sqlBuilder;
	}

	public Integer getStartPage() {
		return startPage;
	}

	public void setStartPage(Integer startPage) {
		this.startPage = startPage;
	}
}
