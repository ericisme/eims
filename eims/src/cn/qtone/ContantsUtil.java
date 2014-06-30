package cn.qtone;

import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;

public class ContantsUtil {

	/**
	 * 获取该系统常量类的初始化MAP的值.<br />
	 * @param map
	 * @param key
	 * @return
	 */
	public static String getSystemContantsMapValue(Map<String,String> map,String key){
		return  map.get(key);
	}

	/**
	 * 取得系统常量类的MAP的下拉选择框形式. <br />
	 * only option.<br />
	 * @param checked
	 * @return
	 */
	public static String getSystemContantsSelectHtml(Map<String,String> map,String selected){
		StringBuffer sb = new StringBuffer();
		Iterator<String> gradeKey =  (Iterator<String>) map.keySet().iterator();
		while(gradeKey.hasNext()){
			String key = gradeKey.next();
			sb.append("<option  value='");
			sb.append(key);
			sb.append("'");
			sb.append(selected.equals(key)?" selected":"");
			sb.append(">");
			sb.append(map.get(key));
			sb.append("</option>");
		}
		return sb.toString();
	}
	/**
	 * 取得系统常量类的MAP的单选的框形式. <br />
	 * @param map
	 * @param radioName
	 * @param checked
	 * @return
	 */
	public static String getSystemContantsRadioHtml(Map<String,String> map,String radioName,String checked){
		StringBuffer sb = new StringBuffer();
		Iterator<String> gradeKey =  (Iterator<String>) map.keySet().iterator();
		while(gradeKey.hasNext()){
			String key = gradeKey.next();
			sb.append("<input type=\"radio\" name=\"");
			sb.append(radioName);
			sb.append("\" value=\"");
			sb.append(key);
			sb.append("\" ");
			sb.append((checked.equals(key)?"checked":""));
			sb.append(">");
			sb.append(map.get(key));
			sb.append("</input>");
		}
		return sb.toString();
	}
	
	public static void setQueryTime(Criteria criteria,HttpServletRequest request){
		String qDateTimeSelectStart=ServletUtil.removeSpace(request, "QDateTimeSelectStart");
		String qDateTimeSelectEnd=ServletUtil.removeSpace(request, "QDateTimeSelectEnd");
		if(StringUtils.isNotBlank(qDateTimeSelectStart)&&StringUtils.isNotBlank(qDateTimeSelectEnd)){
			criteria.add(Restrictions.between("send_time",qDateTimeSelectStart+" 00:00:00", qDateTimeSelectEnd+" 23:59:59"));
		}else if(StringUtils.isNotBlank(qDateTimeSelectStart)){
			criteria.add(Restrictions.ge("send_time", qDateTimeSelectStart+" 00:00:00"));			
		}else if(StringUtils.isNotBlank(qDateTimeSelectEnd)){
			criteria.add(Restrictions.le("send_time", qDateTimeSelectEnd+" 23:59:59"));
		}
	}
	
	public static enum CHARGEBACK_LS_BUSINESS{
		CRDA/*成人档案系统*/
	}
}
