/**
 * 
 */
package cn.qtone.qtoneframework.dao.support;

import java.util.List;
import java.util.Map;

/**
 * @author 林子龙
 * 
 */
public class DefaultSqlBuilder extends AbstractSqlBuilder {

	public static boolean oracle = true;

	@Override
	public String getOrderSql() {
		if (orderElements.size() == 0) {
			return " ";
		}
		StringBuilder result = new StringBuilder();
		result.append(" ORDER BY ");
		for (String key : orderElements.keySet()) {
			result.append(key + " " + orderElements.get(key) + ",");
		}
		return result.deleteCharAt(result.length() - 1).toString() + " ";
	}

	@Override
	protected List<String> putStandarElements() {
		return null;
	}

	public String getWhereSql() {
		if (whereElements.size() == 0 && getWhereInSql().equals("") && whereDateElements.size() == 0) {
			return " ";
		}
		StringBuilder result = new StringBuilder();
		result.append(" WHERE ");
		result.append(getWhereInSql().equals("") ? "" : getWhereInSql() + " AND ");
		dealWhereSql(whereElements, result);
		//处理时间字段
		dealDateWhereSql(whereDateElements, result);

		if (whereElements.size() == 0 && getWhereInSql().equals("") && whereDateElements.size() == 0) {
			return result.toString();
		}
		return result.substring(0, result.length() - 5) + " ";
	}

	private String getWhereInSql() {
		if (whereInElements.size() == 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		for (String key : whereInElements.keySet()) {
			StringBuilder temp = new StringBuilder();
			temp.append(" " + key + " in(");
			int count = 0;
			for (Integer element : whereInElements.get(key)) {
				temp.append(element + ",");
				count++;
			}
			if (count == 0)
				return "";
			result.append(temp.deleteCharAt(temp.length() - 1) + ") AND ");
		}
		return result.substring(0, result.length() - 5) + " ";
	}

	/**
	 * 判断值是否为数字，若为数字，则不用引号。若为字符串，则要引号，
	 * 
	 * @param sb
	 *            StringBuilder对像
	 * @param keyAndName
	 *            存放数据库列名和值的字符串数组
	 * @param operator
	 *            操作符
	 */
	private void dealIntAndString(StringBuilder sb, Object[] keyAndName, String operator) {
		try {
			Integer.parseInt(String.valueOf(keyAndName[1]));
			sb.append(keyAndName[0] + operator + keyAndName[1] + " AND ");
		} catch (NumberFormatException nfe) {
			sb.append(keyAndName[0] + operator + "'" + keyAndName[1] + "' AND ");
		}
	}

	/**
	 * 用于oracle数据库的时间格式处理
	 * @author 张但
	 * @param sb
	 * @param keyAndName
	 * @param operator
	 
	private void dealDateFormat(StringBuilder sb, String[] keyAndName, String operator) {
		sb.append(keyAndName[0] + operator + "to_date('" + keyAndName[1] + "','YYYY-MM-DD HH24:MI:SS') AND ");
	}
	*/
	
	/**
	 * 用于oracle数据库的时间格式处理
	 * @author 张但
	 */
	private void dealDateFormat(StringBuilder sb,Object[] keyAndName,
			String operator){
		if(((String)keyAndName[1]).length()<11 && ("<".equals(operator) || "<=".equals(operator))){
			sb.append(keyAndName[0] + operator + "to_date('" + keyAndName[1] + "','YYYY-MM-DD HH24:MI:SS')+1 AND ");
		}else{
			sb.append(keyAndName[0] + operator + "to_date('" + keyAndName[1] + "','YYYY-MM-DD HH24:MI:SS') AND ");	
		}
	}
	/**
	 * 时间字段的where子句
	 * @author 张但
	 * @param whereDateElements
	 * @param result
	 */
	private void dealDateWhereSql(Map<Object[], Operator> whereDateElements, StringBuilder result) {
		if (isOracle()) {
			for (Object[] keyAndName : whereDateElements.keySet()) {
				if (whereDateElements.get(keyAndName).equals(Operator.GT)) {
					dealDateFormat(result, keyAndName, ">");
				} else if (whereDateElements.get(keyAndName).equals(Operator.LT)) {
					dealDateFormat(result, keyAndName, "<");
				} else if (whereDateElements.get(keyAndName).equals(Operator.EQUAL)) {
					dealDateFormat(result, keyAndName, "=");
				} else if (whereDateElements.get(keyAndName).equals(Operator.LIKE)) {
					dealDateFormat(result, keyAndName, "=");
				} else if (whereDateElements.get(keyAndName).equals(Operator.ISNULL)) {
					result.append(keyAndName[0] + " is null AND ");
				} else if (whereDateElements.get(keyAndName).equals(Operator.NOT_NULL)) {
					result.append(keyAndName[0] + " is not null AND ");
				} else if (whereDateElements.get(keyAndName).equals(Operator.NOT_EQUAL)) {
					dealDateFormat(result, keyAndName, "!=");
				} else if (whereDateElements.get(keyAndName).equals(Operator.GTE)) {
					dealDateFormat(result, keyAndName, ">=");
				} else if (whereDateElements.get(keyAndName).equals(Operator.LTE)) {
					dealDateFormat(result, keyAndName, "<=");
				}
			}
		} else {
			dealWhereSql(whereDateElements, result);
		}
	}

	private void dealWhereSql(Map<Object[], Operator> whereElements, StringBuilder result) {
		for (Object[] keyAndName : whereElements.keySet()) {
			if (whereElements.get(keyAndName).equals(Operator.GT)) {
				dealIntAndString(result, keyAndName, ">");
			} else if (whereElements.get(keyAndName).equals(Operator.LT)) {
				dealIntAndString(result, keyAndName, "<");
			} else if (whereElements.get(keyAndName).equals(Operator.EQUAL)) {
				dealIntAndString(result, keyAndName, "=");
			} else if (whereElements.get(keyAndName).equals(Operator.LIKE)) {
				result.append(keyAndName[0] + " LIKE '%" + keyAndName[1] + "%' AND ");
			} else if (whereElements.get(keyAndName).equals(Operator.ISNULL)) {
				result.append(keyAndName[0] + " is null AND ");
			} else if (whereElements.get(keyAndName).equals(Operator.NOT_NULL)) {
				result.append(keyAndName[0] + " is not null AND ");
			} else if (whereElements.get(keyAndName).equals(Operator.NOT_EQUAL)) {
				dealIntAndString(result, keyAndName, "!=");
			} else if (whereElements.get(keyAndName).equals(Operator.GTE)) {
				dealIntAndString(result, keyAndName, ">=");
			} else if (whereElements.get(keyAndName).equals(Operator.LTE)) {
				dealIntAndString(result, keyAndName, "<=");
			}

		}
	}

	public boolean isOracle() {
		return oracle;
	}

	public void setOracle(boolean oracle) {
		DefaultSqlBuilder.oracle = oracle;
	}

}
