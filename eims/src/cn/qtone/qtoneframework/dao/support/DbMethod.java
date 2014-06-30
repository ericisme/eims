package cn.qtone.qtoneframework.dao.support;

/**
 * @author 林子龙
 * 
 * 封装各种数据库操作及函数的差异
 */
public interface DbMethod {

	/**
	 * 返回数据库的当前时间函数
	 * 
	 * @return
	 */
	public String getCurrentTime();

	/**
	 * 返回已设置好的分页
	 * 
	 * @param noPageSql
	 *            没有分页的sql
	 * @param startOfPage
	 *            开始的条数
	 * @param pageSize
	 *            第页的条数
	 * @return
	 */
	public String getPage(String noPageSql, Integer startOfPage,
			Integer pageSize);

	/**查找一条记录时使用
	 * @param noPageSql　没有分页的sql
	 * @return
	 */
	public String getOneId(String noPageSql,String fieldName);
	/**
	 * 把字符串时间转化为具体数据库的日期
	 * 
	 * @param data
	 * @return
	 */
	public String toDate(String data);

	/**
	 * 把字符串时间转化为具体数据库的时间
	 * 
	 * @param data
	 * @return
	 */
	public String toDateTime(String data);
	
	/**从数据库搜出的日期转换为字符串
	 * @param field
	 * @return
	 */
	public String transDateToString(String field);
	
	/**从数据库搜出的时间转换为字符串
	 * @param field
	 * @return
	 */
	public String transDateTimeToString(String field);
}
