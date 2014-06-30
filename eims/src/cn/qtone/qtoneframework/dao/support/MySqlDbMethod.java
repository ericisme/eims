package cn.qtone.qtoneframework.dao.support;

/**
 * @author 
 *
 */
public class MySqlDbMethod implements DbMethod {

	public String getCurrentTime() {
		return " CURRENT_TIMESTAMP ";
	}

	public String getPage(String noPageSql, Integer startOfPage,
			Integer pageSize) {
		return noPageSql + "LIMIT " + startOfPage + "," + pageSize;
	}

	public String toDate(String data) {
		return data;
	}

	public String toDateTime(String data) {
		return data;
	}

	public String transDateToString(String field) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOneId(String noPageSql, String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}

	public String transDateTimeToString(String field) {
		// TODO Auto-generated method stub
		return null;
	}
}
