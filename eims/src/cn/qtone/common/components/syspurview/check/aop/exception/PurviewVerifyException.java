package cn.qtone.common.components.syspurview.check.aop.exception;

import cn.qtone.common.utils.base.StringUtil;

/**
 * 权限校验异常.<br>
 * 
 * 该异常的抛出表明了指定的角色对指定的资源并没有指定的操作权限.
 * 用户在进行业务处理类的操作过程中，可以对此异常进行捕获，或者
 * 放任其抛出到BaseServlet进行捕获，这样系统会使用指定的Bean来
 * 处理没有权限的操作。
 * 
 * 另外这里必须继承未经检查的异常类，原因是在使用接口代理的时候如果
 * 抛出经过检查的异常类，那么在invoke中将不是由其本身抛出，而是其代
 * 理实例来进行抛出，所以这样的异常在最上层是无法进行捕获的！
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public class PurviewVerifyException extends RuntimeException
{
	private static final long serialVersionUID = 2127054156478264669L;
	
	private String className, methodName, operate;
	
	private int[] roleId;
	
	public PurviewVerifyException(int[] roleId, Class clazz, String methodName, 
			String operate)
	{
		this("角色[" + StringUtil.join(roleId, ",") + "]对资源[" + clazz.getName() 
				+ "." + methodName + "(...)没有[" + operate + "]权限！");
		this.roleId = roleId;
		this.className = clazz.getName();
		this.methodName = methodName;
		this.operate = operate;
	}
	
	public PurviewVerifyException(String message)
	{
		super(message);
	}
	
	/**
	 * 返回没有权限操作的角色ID.
	 * @return
	 */
	public int[] getRoleId()
	{
		return this.roleId;
	}
	
	/**
	 * 返回资源的名称（类名+方法名）.
	 * @return
	 */
	public String getResourceName()
	{
		return this.className + "." + this.methodName + "(...)";
	}
	
	/**
	 * 返回操作标识符（如add、update等）.
	 * @return
	 */
	public String getOperate()
	{
		return this.operate;
	}
}
