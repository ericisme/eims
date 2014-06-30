package cn.qtone.qtoneframework.web.view.liboys;
import liboys.jsp.Template;

/**对liboys.Template的简单封装
 * @author 陈倩
 *
 */
public class JspTemplateUtil {
	private static String DEFAULT_TAG = "html";

	private static final String LEFT_MARK = "{";// 标签的左符号

	private static final String RIGHT_MARK = "}";// 标签的右符号

	private static String CONTEXTPATH;// 初始化时的上下文路径

	private static String CYCLE_REPLACE_TAG = "list";// 默认循环标签

	/**
	 * 设置上下文路径
	 * @param contextPath
	 * 
	 * @author 陈倩
	 */
	public static void setContextPath(String contextPath){
		if (CONTEXTPATH != null) {
			throw new RuntimeException("上下文路径不能重新设置");
		} else {
			CONTEXTPATH = contextPath;
		}
	}
	
	/**
	 * 返回初始化的template对像
	 * 
	 * @param filePath
	 *            相对于上下文的的绝对路径
	 * @return 已初始化的Template实例
	 * 
	 * @author 陈倩
	 */
	public static Template getDefaultTemplate(String filePath) {
		Template mytpl = new Template(CONTEXTPATH, LEFT_MARK, RIGHT_MARK);
		mytpl.setTpl(DEFAULT_TAG, filePath);
		return mytpl;
	}

	/**
	 * 根据是否是只读操作进行标签替换
	 * 
	 * @param template
	 *            模版对象
	 * @param isReadOnly
	 * 
	 * @author 陈倩
	 */
	public static void setStyle(Template template, boolean isReadOnly) {
		if (isReadOnly) {
			template.setParam("disable", "disabled=disabled");
			template.setParam("display", "style=display:none");
			template.setParam("readonly", "readonly=readonly");
		} else {
			template.setParam("disable", "");
			template.setParam("display", "");
			template.setParam("readonly", "");
		}
	}

	/**
	 * 解析并释放Template资源同时返回解析结果
	 * 
	 * @param template
	 *            模版对象
	 * 
	 * @author 陈倩
	 */
	public static String clearTemplateAndGetParseResult(Template template) {
		template.parse(DEFAULT_TAG);
		String result = template.getText(DEFAULT_TAG);
		template.clear();
		return result;
	}

	/**
	 * 解析并释放Template资源同时返回解析结果
	 * 解析返回结果是否0行:如果是,先设置只读操作,再去掉所有没有替换的标签;如果不是,先屏蔽只读操作,再去掉所有没有替换的标签
	 * 
	 * @param template
	 * @param row
	 * 
	 * @author 陈倩
	 */
	public static String clearTemplateAndGetParseResult(Template template,int row) {
		if (0 == row) {
			setStyle(template, true);
		} else {
			setStyle(template, false);
		}
		return removeAllTag(template);
	}

	/**
	 * 去掉所有的由默认的符号开始和结尾的所有标签.
	 * 
	 * @param template
	 *            模版对象
	 * 
	 * @author 陈倩
	 */
	public static String removeAllTag(Template template) {
		return clearTemplateAndGetParseResult(template).replaceAll(
				"\\" + LEFT_MARK + "[^\\" + RIGHT_MARK + "]*\\" + RIGHT_MARK,
				"");
	}
	
	/**
	 * 去掉默认循环标签之间的内容
	 * @param result 
	 * 
	 * @author 陈倩
	 */
	public static String clearSpecificTag(Template template) {
		return clearTemplateAndGetParseResult(template).replaceAll("\\" + LEFT_MARK + CYCLE_REPLACE_TAG + "\\"
				+ RIGHT_MARK + "[\\s|\\S]*\\" + LEFT_MARK + CYCLE_REPLACE_TAG
				+ "\\" + LEFT_MARK, "");
	}
	
	/**
	 * 去掉指定循环标签之间的内容
	 * @param result 
	 * 
	 * @author 陈倩
	 */
	public static String clearSpecificTag(String result,String tag) {
		return result.replaceAll("\\" + LEFT_MARK + CYCLE_REPLACE_TAG + "\\"
				+ RIGHT_MARK + "[\\s|\\S]*\\" + LEFT_MARK + CYCLE_REPLACE_TAG
				+ "\\" + LEFT_MARK, "");
	}
	
	
}
