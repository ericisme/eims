/**
 * 
 */
package cn.qtone.qtoneframework.dao.support;

import java.util.List;

/**
 * @author 林子龙
 * 
 */
public interface SqlBuilder {
	/**
	 * Sql语句中排序的上降序
	 * 
	 */
	public enum Order {
		ASC, DESC
	}

	/**
	 * Sql的Where句中的gt:>,lt:<,equal:=,like:like gte:>= lte:<=
	 * 
	 */
	public enum Operator {
		GT, LT, EQUAL, LIKE, ISNULL, NOT_EQUAL, GTE, LTE, NOT_NULL
	}

	/**
	 * 返回 ORDER BY语句，例如：ORDER BY userid asc,id desc
	 */
	public String getOrderSql();

	/**
	 * 返回Where子句，例如 WHERE userName="c" AND id=5,address LIKE %zs%
	 */
	public String getWhereSql();

	/**
	 * 把排序元素压入此对像中
	 * 
	 * @param element
	 *            排序元素
	 * @param order
	 *            升降序
	 */
	public void pubOrderElement(String element, Order order);

	/**
	 * @param name
	 *            数据库的字段
	 * @param value
	 * 			  数据库的字段值
	 * @param operator
	 *            操作符
	 */
	public void putWhereElement(String name, Object value, Operator operator);

	public void putWhereInElement(String name, List<Integer> list);

	public void putWhereInElement(String name, Integer... inElements);

	/**oracle时间字段处理
	 * @author 张但
	 * @param name
	 * @param value
	 * @param operator
	 */
	public void putWhereDateElement(String name, String value, Operator operator);

	/**
	 * 判断是否使用oracle数据库
	 * @return
	 */
	public boolean isOracle();
}
