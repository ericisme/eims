package cn.qtone.common.simplemvc.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 针对单个域对象的dao接口，定义基本的操作接口
 * 
 * @author 张但
 *
 * @param <T>
 */
public interface SimpleDao<T> {
	
	T get(Serializable id);

	List<T> getAll();

	void save(Object o);

	void remove(Object o);

	void removeById(Serializable id);

	/**
	 * 获取Entity对象的主键名.
	 */
	String getIdName(Class clazz);
}
