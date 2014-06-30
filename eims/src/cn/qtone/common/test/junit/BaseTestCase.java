package cn.qtone.common.test.junit;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import cn.qtone.qtoneframework.log.PrintUtil;

public class BaseTestCase extends TestCase {
	
	private SessionFactory sessionFactory;
	private Session session;

	protected AbstractApplicationContext ac;

	protected Log log;
	//可以以反射方式打印对像
	protected PrintUtil pu;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		log = LogFactory.getLog(this.getClass());
		pu = new PrintUtil();
		ac = new ClassPathXmlApplicationContext(new String[] { "spring/allSpringForTest.xml" });
		ac.registerShutdownHook();
		sessionFactory = (SessionFactory) getBean("sessionFactory"); 
		session = SessionFactoryUtils.getSession(sessionFactory, true); 
		Session s = sessionFactory.openSession(); 
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(s)); 
	}

	protected void tearDown() throws Exception {
		super.tearDown(); 
		SessionHolder holder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory); 
		Session s = holder.getSession(); 
		s.flush(); 
		TransactionSynchronizationManager.unbindResource(sessionFactory); 
		SessionFactoryUtils.closeSession(session);
	} 
	

		protected Object getBean(String beanName) { 
			return ac.getBean(beanName);
		} 


}
