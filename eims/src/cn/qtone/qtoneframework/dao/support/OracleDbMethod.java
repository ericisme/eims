package cn.qtone.qtoneframework.dao.support;

/**
 * DbMethod的ORACLE数据库实现类
 * 
 * @author 林子龙
 *
 */
public class OracleDbMethod implements DbMethod {

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.qtone.components.dao.support.DBMethod#getCurrentTime()
	 */
	public String getCurrentTime() {
		return "sysdate";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.qtone.components.dao.support.DBMethod#getPage(java.lang.Integer,
	 *      java.lang.Integer)
	 */
	public String getPage(String noPageSql, Integer startOfPage,
			Integer pageSize) {
		return "select * from ( select row_.*, rownum rownum_ from ( "
				+ noPageSql + " ) row_ where rownum <= " + (startOfPage
				+ pageSize) + ") where rownum_ > " + startOfPage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.qtone.components.dao.support.DBMethod#toDate(java.lang.String)
	 */
	public String toDate(String date) {
		return " to_date(" + date + ",'YYYY-MM-DD') ";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.qtone.components.dao.support.DBMethod#toDateTime(java.lang.String)
	 */
	public String toDateTime(String date) {
		return " to_date(" + date + ",'YYYY-MM-DD HH24:MI:SS') ";
	}

	/* (non-Javadoc)
	 * @see cn.qtone.components.dao.support.DBMethod#transDateToString(java.lang.String)
	 */
	public String transDateToString(String field) {
		return "to_char(" + field + ",'yyyy-mm-dd')";
	}

	/* (non-Javadoc)
	 * @see cn.qtone.components.dao.support.DBMethod#getOneId(java.lang.String)
	 */
	public String getOneId(String noPageSql,String fieldName) {
		return "select "+ fieldName+" from ( select row_.*, rownum rownum_ from ( "
		+ noPageSql + " ) row_ where rownum <= " + 1 + ") where rownum_ > " + 0;
	}

	public String transDateTimeToString(String field) {
		return "to_char(" + field + ",'yyyy-mm-dd hh24:mi:ss')";
	}

}
