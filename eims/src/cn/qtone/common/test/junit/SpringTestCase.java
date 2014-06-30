package cn.qtone.common.test.junit;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import cn.qtone.qtoneframework.log.PrintUtil;


/**
 * Spring环境下的测试基类，该类带Spring的IOC能力,子类只要设置Setter即可获得依赖注入。
 * 设置AUTOWIRE_BY_NAME，因为Spring的测试基类默认为BY_TYPE,在有多个相同类型的Bean时冲突.
 *
 * @author 张但
 *
 */
public abstract class SpringTestCase extends AbstractDependencyInjectionSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[]{"classpath*:spring/allSpringForTest.xml"};
	}
	
	protected Log log = LogFactory.getLog(this.getClass());
	protected PrintUtil pu = new PrintUtil();
}
