/**
 * 
 */
package cn.qtone.common.simplemvc.dao;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import cn.qtone.common.utils.base.GenericsUtils;

/**
 * 单个域对象CRUD操作的基类，只需继承此类就拥有已经实现的基本的CRUD方法.<br>
 * 
 * 
 * @author 贺少辉
 * @version 1.0
 *
 */
@SuppressWarnings("unchecked")
public class HibernateSimpleDao<T> extends HibernateBaseDao implements SimpleDao<T> {

	protected Class<T> entityClass;// DAO所管理的Entity类型.

	/**
	 * 在构造函数中将泛型T.class赋给entityClass.
	 */
	public HibernateSimpleDao() {
		entityClass = GenericsUtils.getSuperClassGenricType(getClass());
	}

	/**
	 * 取得entityClass.JDK1.4不支持泛型的子类可以抛开Class<T> entityClass,重载此函数达到相同效果。
	 */
	protected Class<T> getEntityClass() {
		return entityClass;
	}

	/**
	 * 根据ID获取对象.
	 */
	public T get(Serializable id) {
		return get(getEntityClass(), id);
	}

	/**
	 * 获取全部对象
	 */
	public List<T> getAll() {
		return getAll(getEntityClass());
	}

	/**
	 * 获取全部对象,带排序参数.
	 */
	public List<T> getAll(String orderBy, boolean isAsc) {
		return getAll(getEntityClass(), orderBy, isAsc);
	}

	/**
	 * 根据ID移除对象.
	 */
	public void removeById(Serializable id) {
		removeById(getEntityClass(), id);
	}

	/**
	 * 取得Entity的Criteria.
	 */
	public Criteria createCriteria(Criterion... criterions) {
		return createCriteria(getEntityClass(), criterions);
	}

	/**
	 * 取得Entity的Criteria,带排序参数.
	 */
	public Criteria createCriteria(String orderBy, boolean isAsc, Criterion... criterions) {
		return createCriteria(getEntityClass(), orderBy, isAsc, criterions);
	}

	/**
	 * 根据属性名和属性值查询对象.
	 *
	 * @return 符合条件的对象列表
	 */
	public List<T> findBy(String propertyName, Object value) {
		return findBy(getEntityClass(), propertyName, value);
	}

	/**
	 * 根据属性名和属性值查询对象,带排序参数.
	 *
	 * @return 符合条件的对象列表
	 */
	public List<T> findBy(String propertyName, Object value, String orderBy, boolean isAsc) {
		return findBy(getEntityClass(), propertyName, value, orderBy, isAsc);
	}

	/**
	 * 根据属性名和属性值查询单个对象.
	 *
	 * @return 符合条件的唯一对象 or null
	 */
	public T findUniqueBy(String propertyName, Object value) {
		return findUniqueBy(getEntityClass(), propertyName, value);
	}

	/**
	 * 判断对象某些属性的值在数据库中唯一.
	 *
	 * @param uniquePropertyNames 在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
	 */
	public boolean isUnique(Object entity, String uniquePropertyNames) {
		return isUnique(getEntityClass(), entity, uniquePropertyNames);
	}
	
	/**
	 * 根据主键,返回任意对象
	 * @param clazz
	 * @param id
	 * @return
	 */
	public Object getObject(Class clazz,Serializable id) {
		return get(clazz, id);
	}

}
