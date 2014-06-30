/**
 * 
 */
package cn.qtone.qtoneframework.test.junit;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.qtone.qtoneframework.log.PrintUtil;

/**
 * 主要针对Dao的整合测试，主要利用spring AbstractTransactionalDataSourceSpringContextTests
 * 测试后自动回滚的功能。此类只是实现loadContextLocations方法，及提供相关测试类。
 * 
 * @author 林子龙
 * 
 */
public abstract class AbstractAutoRollbackTestCase extends AbstractTransactionalDataSourceSpringContextTests {
	protected PrintUtil pu = new PrintUtil();

	@Override
	protected String[] getConfigLocations() {
		return new String[] { "resources/springoracle/*.xml", "resources/test/*.xml","resources/blog/*.xml","resources/gbpx/*.xml"};
	}
}
