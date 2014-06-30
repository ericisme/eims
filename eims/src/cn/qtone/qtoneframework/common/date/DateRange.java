/**
 * 
 */
package cn.qtone.qtoneframework.common.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 林子龙
 * 
 */
public class DateRange {
	
	public enum AvailableState {
		有效, 无效
	}

	private static String DATE_FORMAT = "yyyy-MM-dd";
	/**
	 * 开始时间
	 */
	private Date start;
	/**
	 * 结束时间
	 */
	private Date end;

	public Date getStart() {
		return start;
	}

	public AvailableState getAvailableState(Date date) {
		if (this.isBetweenRange(date)) {
			return AvailableState.有效;
		} else {
			return AvailableState.无效;
		}
	}

	/**
	 * @param date
	 * @return
	 */
	public boolean isBetweenRange(Date date) {
		if(null==start||null==end||date==null)
			return false;
	
		if (date.compareTo(start) >= 0 && date.compareTo(end) <= 0) {
			return true;
		} else {
			return false;
		}
	}

	public void setStart(String start, String... format) {
		if(start!=null)
		this.start = parseDate(start, format);
	};

	public void setEnd(String end, String... format) {
		if(end!=null)
		this.end = parseDate(end, format);
	}

	private Date parseDate(String start, String... format) {
		SimpleDateFormat sdf;
		if (format.length!=0) {
			sdf = new SimpleDateFormat(format[0]);
		} else {
			sdf = new SimpleDateFormat(DATE_FORMAT);
		}
		try {
			return sdf.parse(start);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

}
