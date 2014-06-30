/**
 * 
 */
package cn.qtone.qtoneframework.web.view;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * @author 林子龙
 * 
 */
public class HtmlUtil {

	public static String getOption(Object name, Object value) {
		return "<option value=" + value + ">" + name + "</option>";
	}
	
	public static String getCheckOption(Object name,Object value){
		return "<option value="+value+" selected=selected>"+name+"</option>";
	}

	/**
	 * @param link
	 *            链接
	 * @param name
	 *            链接名称
	 * @return 转义后的链接
	 */
	public static String toLink(String link, String name) {
		return StringEscapeUtils.escapeXml("<a href=" + link + ">" + StringEscapeUtils.unescapeHtml(name) + "</a>");
	}
	
	public static String toAlertLink(String link,String name,String msg){
		return StringEscapeUtils.escapeHtml("<a href="+link+" onClick=\"return confirm('"+msg+"')\" >"+StringEscapeUtils.unescapeHtml(name)+"</a>");
	}
	
	public static String toHtmlLink(String link, String name){
		return "<a href=\"" + link + "\">" + name + "</a>";
	}
   
	public static String toHtmlAlertLink(String link, String name, String msg){
		return "<a href="+link+" onClick=\"return confirm('"+msg+"')\">" + name + "</a>";
	}
	
	public static String toHtmlLink(String link, String name,String propertyKeyValue){
	    return StringEscapeUtils.escapeXml("<a href=" + link + " "+propertyKeyValue+"  >" + name + "</a>");
    }
	/**
	 * @param link
	 *            图片链接
	 * @return 转义后的图片html标记
	 */
	public static String getImg(String link) {
		return StringEscapeUtils.escapeHtml("<img src=" + link + " border=0>");
	}
	public static String getHtmlImg(String link) {
		return "<img src=" + link + " border=0>";
	}
	
	public static String changeTextColor(String content, String color) {
		return StringEscapeUtils.escapeHtml("<span  style=color:" + color + ">" + StringEscapeUtils.unescapeHtml(content) + "</span>");
	}
	
	public static String toBold(String content){
		return StringEscapeUtils.escapeHtml("<strong>"+content+"</strong>");
	}
	
	public static String nextLine(){
		return StringEscapeUtils.escapeHtml("<br>");
	}
	
	public static String getRadioButton(String name,Object value,boolean checked){
		String check=checked?" checked":" ";
		return "<input name=qtonedefault type=radio value="+value+" "+check+"/>"+name;
	}

}