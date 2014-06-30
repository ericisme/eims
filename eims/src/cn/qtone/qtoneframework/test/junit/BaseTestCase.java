/**
 * 
 */
package cn.qtone.qtoneframework.test.junit;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.qtone.qtoneframework.log.PrintUtil;

/**
 * @author 林子龙
 * 
 */
public abstract class BaseTestCase extends TestCase {
	protected static ApplicationContext ac;

	protected Log log = LogFactory.getLog(getClass());

	// 可以以反射方式打印对像
	protected PrintUtil pu = new PrintUtil();

	@Override
	protected void setUp() {
		Long start = System.currentTimeMillis();
		ac = new ClassPathXmlApplicationContext(getConfigLocation());
		Long end = System.currentTimeMillis();
		log.info("Spring容器初始化时间为：" + (end - start) + "毫秒。");
	}

	protected  String[] getConfigLocation(){
		return new String[]{"resources/springoracle/*.xml"};
	};
}
