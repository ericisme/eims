/**
 * 
 */
package cn.qtone.qtoneframework.web.view.velocity;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * 对Velocity的封装
 * 
 * @author 林子龙
 * 
 */
public class VelocityHelper {
	private static Log log = LogFactory.getLog(VelocityHelper.class);

	private static Boolean hasInit = false;

	private static String CONTEXT_PATH;// 上下文路径
	
	/**
	 * 保证在多线程环境下只执行一次 double checked locking
	 */
	public void init() {
		if (hasInit == false) {
			synchronized (this) {
				if (hasInit == false) {
					initVelocity();
					hasInit = true;
				}
			}
		}
	}

	/**
	 * 初始化velocity
	 */
	public void initVelocity() {
		try {
			Properties p = new Properties();
			p.setProperty("input.encoding", "utf-8");
			p.setProperty("output.encoding", "utf-8");
//			String contextPath = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
//			p.setProperty("file.resource.loader.path", contextPath);
			p.setProperty("file.resource.loader.path", CONTEXT_PATH);
			Velocity.init(p);
			log.info("初始化成功");
		} catch (Exception e) {
			throw new RuntimeException("Veloctity初始化失败", e);
		}
	}

	/**
	 * @param vmPath,vm文件相对于classpath的路径
	 */
	public VelocityHelper(String vmPath) {
		this.vmPath = vmPath;
	}
	
	/**
	 * @param vmPath 页面路径
	 * @param map 参数和值
	 */
	public VelocityHelper(String vmPath, Map<String, Object> map){
		this.vmPath = vmPath;
		for(String key : map.keySet()){
			names.add(key);
			values.add(map.get(key));
		}
	}

	// vm文件的路径
	private String vmPath;

	// 对像名字
	private List<String> names = new LinkedList<String>();

	// 对像
	private List<Object> values = new LinkedList<Object>();

	/**
	 * @param name,传入所有被解释对像的名字
	 */
	public void putNames(String... names) {
		for(String name : names){
			this.names.add(name);
		}
	}

	/**
	 * @param values
	 *            传入所有被解释的对像
	 */
	public void putValues(Object... values) {
		for(Object value : values){
			this.values.add(value);
		}
	}

	/**
	 * 解释后，输出结果
	 */
	public String getParsedResult() {
		init();
		if (this.values.size() != this.names.size()) {
			throw new RuntimeException("传入的参数名的数量与值的数量不一致。");
		}
		StringWriter result = new StringWriter();
		VelocityContext context = new VelocityContext();
		for (int i = 0; i < names.size(); i++) {
			context.put(names.get(i), values.get(i));

		}
		Template tpl;
		try {
			tpl = Velocity.getTemplate(vmPath);
			tpl.merge(context, result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		String resultString= result.toString();
	//	log.info("标志位置"+resultString.indexOf("xml version=\"1.0\" encoding"));
//		if(resultString.indexOf("xml version=\"1.0\" encoding")!=-1){
//			XtableHelper xtableHelper=new XtableHelper();
//			return xtableHelper.escape(resultString);
//		}
		return resultString;
	}

	/**
	 * 设置上下文路径
	 * 
	 * @param context_path
	 */
	public static void setCONTEXT_PATH(String context_path) {
		if (CONTEXT_PATH != null) {
			throw new RuntimeException("上下文路径不能重新设置");
		} else {
			CONTEXT_PATH = context_path;
			log.info("设置contextPath:" + CONTEXT_PATH);
		}		
	}

	/**
	 * @return the cONTEXT_PATH
	 */
	public static String getCONTEXT_PATH() {
		if (CONTEXT_PATH == null) {
			throw new RuntimeException("上下文路径没有初始化");
		} 
		return CONTEXT_PATH;
	}
}
