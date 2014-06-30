package cn.qtone.common.components.syspurview.check.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import cn.qtone.common.components.syspurview.check.aop.annotation.Check;
import cn.qtone.common.components.syspurview.check.aop.exception.PurviewVerifyException;
import cn.qtone.common.components.syspurview.core.user.domain.AbstractUser;

/**
 * 业务代理类的权限检查处理器.<br>
 * 
 * 使用接口代理类的权限检查处理类，权限检查将根据当前用户所属的
 * 角色的权限，并结合方法的标注来进行判断该角色是否有指定的操作
 * 权限，有则继续执行，否则跳出执行并返回出错页面！
 * 
 * @author 马必强
 * @version 1.0
 * 
 */
public class InterfacePurviewHandler extends BasePurviewHandler implements
		InvocationHandler
{
	private Object bo; // 实际的业务处理类
	
	private int[] roleId;

	public InterfacePurviewHandler(AbstractUser user, Object bo)
	{
		super(user.getPurviewMap());
		this.bo = bo;
		this.roleId = user.getRoleId();
	}

	/**
	 * 业务类的方法调用处理.
	 */
	public Object invoke(Object obj, Method method, Object[] args)
			throws Throwable
	{
		Check check = method.getAnnotation(Check.class);
		// 如果要检查权限，并且检测到没有权限则调用错误处理类进行
		if (check != null && !havePurview(bo.getClass(), check.purview())) {
			throw new PurviewVerifyException(this.roleId, bo.getClass(), 
					method.getName(), check.purview());
		}
		return method.invoke(bo, args);
	}

}
