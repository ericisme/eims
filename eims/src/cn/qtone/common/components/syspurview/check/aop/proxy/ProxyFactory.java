package cn.qtone.common.components.syspurview.check.aop.proxy;

import java.lang.reflect.Proxy;

import net.sf.cglib.proxy.Enhancer;

import org.apache.log4j.Logger;

import cn.qtone.common.components.syspurview.check.aop.annotation.InterfaceProxy;
import cn.qtone.common.components.syspurview.core.user.domain.AbstractUser;

/**
 * 代理类的类工厂.
 * 给每一个指定的业务处理类和其接口生成一个代理类。此处的代理类只代理
 * 每一个业务处理类的（公共）接口方法！
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public class ProxyFactory
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ProxyFactory.class);

	/**
	 * 保护构造方法，不允许直接实例化！
	 */
	protected ProxyFactory(){}
	
	/**
	 * 获取代理工厂的唯一实例.
	 * @return 代理类工厂对象的唯一实例
	 */
	public static ProxyFactory getInstance()
	{
		if (logger.isInfoEnabled()) {
			logger.info("getInstance() - 获取ProxyFactory的唯一实例！");
		}

		if (factory != null) return factory;
		synchronized (ProxyFactory.class) {
			if (factory != null) return factory;
			factory = new ProxyFactory();
		}
		
		return factory;
	}
	
	/**
	 * 获取指定class业务处理类的代理类.<br>
	 * 
	 * 该代理方法将使用两种方式来为被代理类生成其代理类：
	 * 1.在类名称上使用了@InterfaceProxy注解
	 *   在为该类型的业务类生成代理类时，系统将使用Proxy的动态代理方式。
	 *   注意使用该类型的代理类时，必须使用接口类型进行强制转换，否则其
	 *   代理类将不起作用！
	 *   
	 * 2.没有使用@InterfaceProxy注解
	 *   该类型的业务类，系统将为其使用CGLIB库来生成代理类！
	 * 
	 * @param boClass 业务处理类的Class
	 * @param user 当前用户信息Bean
	 * @return 该业务处理类的代理类
	 */
	public Object getProxy(Class<?> boClass, AbstractUser user)
	{
		if (logger.isInfoEnabled()) {
			logger.info("getProxy(Class<?>, int) - 获取类[" + boClass.getName() + 
					"]的代理类！");
		}

		if (boClass.getAnnotation(InterfaceProxy.class) != null) {
			return getInterfaceProxy(boClass, boClass.getInterfaces(),
					user);
		} else {
			return getProxyByCGLib(boClass, user);
		}
	}
	
	/**
	 * 如果直接指定业务处理类的接口则无须指定@InterfaceProxy注解！
	 * 
	 * @param boClass 业务处理类的Class
	 * @param boInterfaceClass 业务处理类的接口Class
	 * @param user 当前用户信息Bean
	 * @return 该业务处理类的代理类
	 */
	public Object getProxy(Class<?> boClass, Class<?> boInterfaceClass,
			AbstractUser user)
	{
		if (logger.isInfoEnabled()) {
			logger.info("getProxy(Class<?>, Class<?>, int) - 获取业务类[" + 
					boClass.getName() + "]的接口代理类(直接指明业务接口)！");
		}

		return getInterfaceProxy(boClass, new Class[] { boInterfaceClass }, 
				user);
	}
	
	/**
	 * 使用接口方式来获取代理类.
	 * @throws Throwable 
	 */
	protected Object getInterfaceProxy(Class boClass, Class[] boInterfaceClass,
			AbstractUser user)
	{
		if (logger.isInfoEnabled()) {
			logger.info("getInterfaceProxy(Class, Class[], int) - 获取业务类[" + 
					boClass.getName() + "]的接口代理类！");
		}

		InterfacePurviewHandler handler = null;
		Object bo = null;
		try {
			bo = boClass.newInstance();
		} catch (Throwable ex) {
			throw new RuntimeException("业务处理类实例化失败：" + ex.getMessage());
		}
		handler = new InterfacePurviewHandler(user, bo);
		return Proxy.newProxyInstance(boClass.getClassLoader(),
				boInterfaceClass, handler);
	}
	
	/**
	 * 使用CGLIB来为指定的业务类生成其代理类.
	 */
	protected Object getProxyByCGLib(Class boClass, AbstractUser user)
	{
		if (logger.isInfoEnabled()) {
			logger.info("getProxyByCGLib(Class, int) - 获取业务类[" + 
					boClass.getName() + "]的代理类（使用CGLIB库来生成）!");
		}

		SubclassPurviewHandler handler = new SubclassPurviewHandler(user);
		Enhancer en = new Enhancer();
		// en.setUseCache(true);
		en.setSuperclass(boClass);
		en.setCallback(handler);
		return en.create();
	}
	
	private static ProxyFactory factory;
}
