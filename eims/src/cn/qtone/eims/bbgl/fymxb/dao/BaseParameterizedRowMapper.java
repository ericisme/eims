package cn.qtone.eims.bbgl.fymxb.dao;

import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.mysql.jdbc.Blob;

/**
 * 通用BaseParameterizedRowMapper；自动根据列名获取数据值。<br>
 * 例子：List<WaitHandleBean> list = dao.getSimpleJdbcTemplate().query("select C.title,C.id main_id,A.id step_id,A.stepNo step_no,A.status,C.addtime add_time,jx_user.REAL_NAME,A.powerType,'3' type from cpj_steps A left join cpj_main C on C.id=A.mainId left join jx_user on C.addUserId=jx_user.USER_ID where A.status<>2 and C.status<>-1", new BaseParameterizedRowMapper<WaitHandleBean>(WaitHandleBean.class));
 * 
 * @param <T>      指定Bean的类型
 */
public class BaseParameterizedRowMapper<T> implements ParameterizedRowMapper<T> {
	private Class<T> cls;

	private Pattern pattern = Pattern.compile("([ -]+)|([\\_]+)");

	private Map<String, Property> properties;

	private List<String> columns;

	/**
	 * @param cls
	 *     指定Bean的类型
	 *     
	 */
	public BaseParameterizedRowMapper(Class<T> cls) {
		this.cls = cls;
		setPropertys();
	}

	public T mapRow(ResultSet rs, int arg1) throws SQLException {
		T bean = null;
		if (arg1 == 0) {
			setColumns(rs);
		}
		try {
			bean = cls.newInstance();
			Property property;
			String key;
			for (int i = 0; i < columns.size(); i++) {
				key = columns.get(i);
				property = properties.get(getName(key));
				if (property != null) {
					property.getMethod().invoke(bean,
							getObject(rs, property.getCls(), i + 1));
				}

			}
		} catch (Exception e) {
			throw new SQLException(e.toString());
		}
		return bean;
	}

	private void setColumns(ResultSet rs) throws SQLException {
		ResultSetMetaData metadata = rs.getMetaData();
		int columnCount = metadata.getColumnCount();
		columns = new LinkedList<String>();
		for (int i = 1; i <= columnCount; i++) {
			columns.add(metadata.getColumnLabel(i));
		}
	}

	private void setPropertys() {
		PropertyDescriptor[] propertyDescriptors = BeanUtils
				.getPropertyDescriptors(cls);
		if (propertyDescriptors != null) {
			properties = new HashMap<String, Property>();
			Method method;
			Property property;
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				method = propertyDescriptor.getWriteMethod();
				if (method != null) {
					property = new Property();
					property.setMethod(method);
					property.setCls(propertyDescriptor.getPropertyType());
					properties.put(getName(propertyDescriptor.getName()),
							property);
					property = null;
					method = null;
				}
			}
		}
		propertyDescriptors = null;
	}

	private String getName(String name) {
		return pattern.matcher(name).replaceAll("").toUpperCase();
	}

	@SuppressWarnings("unchecked")
	private Object getObject(ResultSet rs, Class cls, int columnIndex)
			throws SQLException {
		if (String.class.equals(cls)) {
			return rs.getString(columnIndex);

		} else if (Integer.class.equals(cls)
				|| cls.getSimpleName().equals("int")) {
			return rs.getInt(columnIndex);

		} else if (Float.class.equals(cls)
				|| cls.getSimpleName().equals("float")) {
			return rs.getFloat(columnIndex);

		} else if (Double.class.equals(cls)
				|| cls.getSimpleName().equals("int")) {
			return rs.getDouble(columnIndex);

		} else if (Long.class.equals(cls) || cls.getSimpleName().equals("long")) {
			return rs.getLong(columnIndex);

		} else if (Short.class.equals(cls)
				|| cls.getSimpleName().equals("short")) {
			return rs.getShort(columnIndex);

		} else if (Date.class.getSimpleName().equals(cls.getSimpleName())) {
			return rs.getDate(columnIndex);

		} else if (Time.class.equals(cls)) {
			return rs.getTime(columnIndex);

		} else if (Timestamp.class.equals(cls)) {
			return rs.getTimestamp(columnIndex);

		} else if (BigDecimal.class.equals(cls)) {
			return rs.getBigDecimal(columnIndex);

		} else if (Array.class.equals(cls)) {
			return rs.getArray(columnIndex);

		} else if (InputStream.class.equals(cls)) {
			return rs.getBinaryStream(columnIndex);

		} else if (Blob.class.equals(cls)) {
			return rs.getBlob(columnIndex);

		} else if (Clob.class.equals(cls)) {
			return rs.getClob(columnIndex);

		} else if (Reader.class.equals(cls)) {
			return rs.getCharacterStream(columnIndex);

		} else if (Byte.class.equals(cls) || cls.getSimpleName().equals("byte")) {
			return rs.getByte(columnIndex);

		} else if (Boolean.class.equals(cls)
				|| cls.getSimpleName().equals("boolean")) {
			return rs.getBoolean(columnIndex);

		} else if (URL.class.equals(cls)) {
			return rs.getURL(columnIndex);

		} else if (Ref.class.equals(cls)) {
			return rs.getRef(columnIndex);

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private class Property {
		private Class cls;
		private Method method;

		public Class getCls() {
			return cls;
		}

		public void setCls(Class cls) {
			this.cls = cls;
		}

		public Method getMethod() {
			return method;
		}

		public void setMethod(Method method) {
			this.method = method;
		}
	}

}
