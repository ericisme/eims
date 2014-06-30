package cn.qtone.common.components.cache;

/**
 * 当使用get方法没有取得指定key的值时调用的模板接口.
 * @author 马必强
 *
 */
public interface NotFoundInvoke
{
	/**
	 * 执行方法获取指定的对象值并返回.
	 * @return
	 */
	public Object getValue();
}
