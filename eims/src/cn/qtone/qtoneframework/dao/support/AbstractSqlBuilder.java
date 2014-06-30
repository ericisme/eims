/**
 * 
 */
package cn.qtone.qtoneframework.dao.support;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author 林子龙
 * 
 */
public abstract class AbstractSqlBuilder implements SqlBuilder {

	protected List<String> standarElements;

	protected Map<String, Order> orderElements = new LinkedHashMap<String,Order>();

	protected Map<Object[], Operator> whereElements = new LinkedHashMap<Object[], Operator>();

	protected Map<String, List<Integer>> whereInElements = new LinkedHashMap<String, List<Integer>>();

	/**
	 * oracle时间字段处理
	 * @author 张但 
	 */
	protected Map<Object[], Operator> whereDateElements = new LinkedHashMap<Object[], Operator>();
	
	public String getOrderSql() {
		init();
		if (orderElements.size() == 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		result.append(" ORDER BY ");
		for (String orderElement : orderElements.keySet()) {
			if (standarElements.contains(orderElement)) {
				result.append(orderElement + " "
						+ orderElements.get(orderElement) + ",");
			} else {
				throw new IllegalArgumentException("传入SqlBuilder需要排序的元素："
						+ orderElement + "和预设的不一至");
			}
		}
		return result.deleteCharAt(result.length() - 1).toString() + " ";
	}

	public void putOrderElement(LinkedHashMap<String, Order> map) {
		orderElements.putAll(map);
	}

	public void pubOrderElement(String element, Order order) {
		orderElements.put(element, order);
	}

	private void init() {
		if (null == standarElements) {
			standarElements = Collections
					.unmodifiableList(putStandarElements());
		}
	}

	/**
	 * 返回一个List集合，里面需要包含在排序中可能要用到的字段
	 * 
	 * @return
	 */
	abstract protected List<String> putStandarElements();

	public void putWhereElement(String name, Object value, Operator operator) {
		Object[] nameAndValue = new Object[]{name, value};
		whereElements.put(nameAndValue, operator);
	}

	public void putWhereInElement(String name, Integer... inElements){
		List<Integer> inElementList = new LinkedList<Integer>();
		for(Integer inElement : inElements){
			inElementList.add(inElement);
		}
		whereInElements.put(name, inElementList);
	}

	public void putWhereInElement(String name, List<Integer> inElements) {
		whereInElements.put(name, inElements);
	}


	/**oracle时间字段处理
	 * @author 张但
	 * @param name
	 * @param value
	 * @param operator
	 */
	public void putWhereDateElement(String name, String value, Operator operator) {
		String[] nameAndValue = new String[]{name, value};
		whereDateElements.put(nameAndValue, operator);
	}
}
