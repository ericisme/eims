package cn.qtone.common.test.junit;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * Dao测试基类,该类带OpenSessionInTest与事务默认回滚能力
 * 
 * @author 张但
 */
public abstract class DaoTestCase extends AbstractTransactionalDataSourceSpringContextTests {
	/**
	 * @see AbstractTransactionalDataSourceSpringContextTests#getConfigLocations()
	 */
	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[]{"classpath*:spring/allSpring.xml"};
	}
}
