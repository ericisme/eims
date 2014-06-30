package cn.qtone.common.components.syspurview.check.aop.annotation;

/**
 * 默认的操作权限操作代码.<br>
 * 
 * 该类一般是配合Check进行设计，比如:
 * @Check(purview=DefaultPurviewType.ADD);
 * public void add()
 * {
 *  	// 操作
 * }
 * 这样就表示在进入add方法前进行添加操作的权限检查！如果检查
 * 通过则进入add方法，否则跳转到没有权限操作的处理页面！
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public final class DefaultOperateType
{
	private static enum type {
		add, update, delete, query, list
	};
	
	/**
	 * 添加操作的默认名称(add).
	 */
	public final static String ADD = type.add.toString();
	
	/**
	 * 更新操作的默认名称(update).
	 */
	public final static String UPDATE = type.update.toString();
	
	/**
	 * 删除操作的默认名称(delete).
	 */
	public final static String DELETE = type.delete.toString();
	
	/**
	 * 查询操作的默认名称(query).
	 */
	public final static String QUERY = type.query.toString();
	
	/**
	 * 列表操作的默认名称(list).
	 */
	public final static String LIST = type.list.toString();
}
