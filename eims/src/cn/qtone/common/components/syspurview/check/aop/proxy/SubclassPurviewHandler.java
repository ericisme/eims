package cn.qtone.common.components.syspurview.check.aop.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import cn.qtone.common.components.syspurview.check.aop.annotation.Check;
import cn.qtone.common.components.syspurview.check.aop.exception.PurviewVerifyException;
import cn.qtone.common.components.syspurview.core.user.domain.AbstractUser;

/**
 * 
 * @author 马必强
 * 
 */
public class SubclassPurviewHandler extends BasePurviewHandler implements
		MethodInterceptor
{
	private int[] roleId;
	
	public SubclassPurviewHandler(AbstractUser user)
	{
		super(user.getPurviewMap());
		this.roleId = user.getRoleId();
	}

	/**
	 * 业务类的方法调用处理.
	 */
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable
	{
		Check check = method.getAnnotation(Check.class);
		if (check != null && !havePurview(obj.getClass(), check.purview())) {
			throw new PurviewVerifyException(this.roleId, 
					obj.getClass().getSuperclass(), 
					method.getName(), check.purview());
		}
		return proxy.invokeSuper(obj, args);
	}

}
