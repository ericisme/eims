package cn.qtone.qtoneframework.log;

import java.util.Collection;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 打印工具，利用反射机制打印出对像内部的属性
 * 
 * @author 林子龙
 */
public class PrintUtil {

	private Log log = LogFactory.getLog(getClass());

	/**
	 * 迭代＋反射打印对属性
	 */
	@SuppressWarnings("unchecked")
	public void toString(Collection collection) {
		for (Object obj : collection)
			toString(obj);
	}

	/**
	 * 迭代＋反射打印对像属性
	 */
	public void toString(Object... objs) {
		for (Object obj : objs)
			log.info(ToStringBuilder.reflectionToString(obj));
	}

}
