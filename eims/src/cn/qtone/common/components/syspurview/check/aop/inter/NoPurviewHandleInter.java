package cn.qtone.common.components.syspurview.check.aop.inter;

/**
 * 当没有权限时的处理接口。
 * 
 * @author 马必强
 *
 */
public interface NoPurviewHandleInter
{
	/**
	 * aop方式检测没有权限时的处理方法。
	 *
	 * @param roleId 角色ID
	 * @param className 处理的业务类的类名称
	 * @param method 业务类的方法名称
	 * @param operate 操作权限字符串
	 */
	void doNoPurview(int roleId, String className, 
			String method, String operate);
}
