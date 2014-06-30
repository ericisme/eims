package cn.qtone.qtoneframework.common.utils.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * Html分析工具类
 * 
 * @author 张但
 *
 */
public class HtmlParseUtil {

	/**
	 * 过滤HTML标签，返回文本
	 */
	public static String getTextContent(String htmlSource) {
		if(StringUtils.isBlank(htmlSource)){
			return "";
		}
		String textStr = "";
		String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
		String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
		try {
			Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			Matcher m_script = p_script.matcher(htmlSource);
			htmlSource = m_script.replaceAll(""); // 过滤script标签

			Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			Matcher m_style = p_style.matcher(htmlSource);
			htmlSource = m_style.replaceAll(""); // 过滤style标签

			Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			Matcher m_html = p_html.matcher(htmlSource);
			htmlSource = m_html.replaceAll(""); // 过滤html标签
			textStr = htmlSource;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return textStr;// 返回文本字符串
	}
	
	public static void main(String[] args) {
		System.out.println(getTextContent("<html><body><table><tr><td>sadfasdfasdfas</td><td>22222222</td></tr></table></body></html>"));
	}
}
