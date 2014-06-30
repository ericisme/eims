package cn.qtone.qtoneframework.dao.support;

import org.apache.log4j.Logger;

/**
 * 实体分页VO,装饰实体,以提供查询条件
 * @author 李修宽
 */
public class QueryPageVO<T> extends PageVO {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(QueryPageVO.class);

	private T Entity;
	private String orderElement;//表格控件的排序元素
	private String orderType;  //表格控件的排序类型
	
	@SuppressWarnings("unchecked")
	public QueryPageVO(Class<T> cl){
		try {
			this.Entity=cl.newInstance();
		} catch (InstantiationException e) {
			logger.error("QueryPageVO(Class<T>)", e);
		} catch (IllegalAccessException e) {
			logger.error("QueryPageVO(Class<T>)", e);
		}
	}
	public QueryPageVO(T entity){
		this.Entity=entity;
	}
	public T getEntity() {
		return Entity;
	}
	public void setEntity(T entity) {
		Entity = entity;
	}
	public String getOrderElement() {
		return orderElement;
	}
	public void setOrderElement(String orderElement) {
		this.orderElement = orderElement;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
    public void clearSqlBuilder(){
    	this.setSqlBuilder(new DefaultSqlBuilder());
    }
}
