package cn.qtone.common.utils.base;


/**
 * 数据库操作的实用类.
 * 
 * @author 马必强
 *
 */
public class DBUtil
{
	public final static StringBuffer oraclePageQuery(StringBuffer sql, 
			String orderBy, int startIndex, int totals)
	{
		return oracleExePageQuery(sql, orderBy, startIndex, totals);
	}
	
	public final static StringBuffer oraclePageQuery(StringBuffer sql, 
			int startIndex, int totals)
	{
		return oracleExePageQuery(sql, null, startIndex, totals);
	}
	
	public final static String oraclePageQuery(String sql, String orderBy,
			int startIndex, int totals)
	{
		StringBuffer result = oracleExePageQuery(new StringBuffer(sql), 
				orderBy, startIndex, totals);
		return result == null ? null : result.toString();
	}
	
	public final static String oraclePageQuery(String sql, int startIndex,
			int totals)
	{
		StringBuffer result = oracleExePageQuery(new StringBuffer(sql), null, 
				startIndex, totals);
		return result == null ? null : result.toString();
	}
	
	public final static StringBuffer mysqlPageQuery(StringBuffer sql,
			String orderBy, int startIndex, int totals)
	{
		return mysqlExePageQuery(sql, orderBy, startIndex, totals);
	}
	
	public final static StringBuffer mysqlPageQuery(StringBuffer sql,
			int startIndex, int totals)
	{
		return mysqlExePageQuery(sql, null, startIndex, totals);
	}
	
	public final static String mysqlPageQuery(String sql, String orderBy, 
			int startIndex, int totals)
	{
		StringBuffer result = mysqlExePageQuery(new StringBuffer(sql), 
				orderBy, startIndex, totals);
		return result == null ? null : result.toString();
	}
	
	public final static String mysqlPageQuery(String sql, int startIndex, 
			int totals)
	{
		StringBuffer result = mysqlExePageQuery(new StringBuffer(sql), 
				null, startIndex, totals);
		return result == null ? null : result.toString();
	}
	
	/**
	 * 将oracle中的日期时间字段转换成日期时间型的字符串.
	 * @param field
	 * @return
	 */
	public final static String oracleDTToString(String field)
	{
		return "to_char(" + field + ",'yyyy-mm-dd hh24:Mi:ss')";
	}
	
	/**
	 * 将oracle中的日期字段转换成日期类型的字符串.
	 * @param field
	 * @return
	 */
	public final static String oracleDateToString(String field)
	{
		return "to_char(" + field + ",'yyyy-mm-dd')";
	}
	
	/**
	 * 将oracle中的时间字段转换成时间类型的字符串.
	 * @param field
	 * @return
	 */
	public final static String oracleTimeToString(String field)
	{
		return "to_char(" + field + ",'hh24:Mi:ss')";
	}
	
	/**
	 * 使用指定的格式来将oracle中的日期时间字段转换成字符串.
	 * @param field
	 * @param formate
	 * @return
	 */
	public final static String oracleDTToString(String field, String formate)
	{
		return "to_char(" + field + ",'" + formate + "')";
	}
	
	/**
	 * 将指定的日期时间字符串表示成oracle中的日期时间字段类型.
	 * @param dateStr
	 * @return
	 */
	public final static String stringToOracleDT(String dateStr)
	{
		return "to_date('" + dateStr + "','yyyy-mm-dd hh24:Mi:ss')";
	}
	
	/**
	 * 将指定的日期字符串表示成oracle中的日期字段类型.
	 * @param dateStr
	 * @return
	 */
	public final static String stringToOracleDate(String dateStr)
	{
		return "to_date('" + dateStr + "','yyyy-mm-dd')";
	}
	
	/**
	 * 将指定的时间字符串表示成oracle中的时间字段类型.
	 * @param dateStr
	 * @return
	 */
	public final static String stringToOracleTime(String dateStr)
	{
		return "to_date('" + dateStr + "','hh24:Mi:ss')";
	}

	/**
	 * oracle数据库的分页查询转换.
	 * @param sql 查询SQL语句
	 * @param orderBy 排序字段，类似于order by a.id,b.id
	 * @param startIndex 查询的开始位置，从1开始
	 * @param totals 要查询的结果数
	 * @return
	 */
	private static StringBuffer oracleExePageQuery(StringBuffer sql, String orderBy,
			int startIndex, int totals)
	{
		if (sql != null) {
			int fromIndex = sql.toString().toUpperCase().lastIndexOf("FROM");
			String order = StringUtil.trim(orderBy).toUpperCase().intern();
			if (order != "") {
				if (!order.startsWith("ORDER")) order = "ORDER BY " + order;
				sql.insert(fromIndex-1, ",row_number() OVER(" + order + ") rn");
			} else {
				sql.insert(fromIndex-1, ",ROWNUM as rn");
			}
			sql.insert(0, "SELECT * FROM (");
			sql.append(") WHERE rn BETWEEN ");
			sql.append(startIndex < 0 ? 0 : startIndex);
			sql.append(" AND ");
			sql.append(startIndex + totals - 1);
		}

		return sql;
	}
	
	public static StringBuffer addOracleQuery(StringBuffer sql, int startIndex, int totals){
		if (sql != null) {			
			StringBuffer sqlQuery = new StringBuffer();
			
			sqlQuery.append("SELECT * FROM (SELECT TMP.*,ROWNUM RN FROM(").append(sql).append(") TMP) WHERE RN BETWEEN ");
			sqlQuery.append(startIndex < 0 ? 0 : startIndex);
			sqlQuery.append(" AND ");
			sqlQuery.append(startIndex + totals - 1);
			
			return sqlQuery;
		}
		
		return sql;
	}
	
	/**
	 * mysql 数据库的分页查询转换
	 * @param sql 查询SQL语句
	 * @param orderBy 排序字段，类似于order by a.id,b.id
	 * @param startIndex 查询的开始位置，从1开始
	 * @param totals 要查询的结果数
	 * @return
	 */
	public static StringBuffer mysqlExePageQuery(StringBuffer sql, String orderBy, 
			int startIndex, int totals)
	{
		if (sql != null) {
//			if (orderBy != null) {
//				String order = orderBy.toUpperCase().trim();
//				if (!order.startsWith("ORDER")) order = "ORDER BY " + order;
//				sql.append(" " + order);
//			}
			sql.append(" LIMIT ");
			sql.append(startIndex < 1 ? 0 : (startIndex - 1));
			sql.append(",");
			sql.append(totals);
		}

		return sql;
	}
	
	public static void main(String[] args)
	{
		String sql = "SELECT a.id,a.name FROM tbl_a";
		StringUtil.debug(oraclePageQuery(sql, "order by a.id", 1, 30));
		StringUtil.debug(oraclePageQuery(sql, "a.name", 1, 30));
		StringUtil.debug(oraclePageQuery(sql, 1, 30));
		StringUtil.debug(mysqlPageQuery(sql, "order by a.id", 1, 30));
		StringUtil.debug(mysqlPageQuery(sql, "a.name", 1, 30));
		StringUtil.debug(mysqlPageQuery(sql, 1, 30));
	}
}
