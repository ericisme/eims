/**
 * 
 */
package cn.qtone.common.simplemvc.controller;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.ModelAndView;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.simplemvc.dao.HibernateSimpleDao;
import cn.qtone.common.simplemvc.support.SqlExpression;
import cn.qtone.common.utils.base.BeanUtils;
import cn.qtone.common.utils.base.DateUtil;
import cn.qtone.common.utils.base.GenericsUtils;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 管理后台的控制器基类，负责单个域对象CRUD操作的基类，子类只需继承此类便拥有CRUD操作功能
 * 
 * InitializingBean spring
 * 提供的接口，只有一个方法afterPropertiesSet,spring在设置完继承这个接口的bean的所有合作者后，会调用afterPropertiesSet方法。
 * 
 * 目前提供index、list、add、edit、save、show、remove等基础方法，子类可以通过重载这几个方法实现特殊需求。
 * 
 * 
 * @author 贺少辉
 * @version 1.0
 *
 */
public class SimpleManageController<T, M extends HibernateSimpleDao<T>> extends SimpleController implements InitializingBean {
	/**
	 * Controller管理的域对象类型
	 */
	protected Class<T> domainClass;

	/**
	 * Controller管理的域对象主键类型.
	 */
	protected Class idClass;

	/**
	 * Controller管理的域对象主键名.
	 */
	protected String idName;

	/**
	 * Controller管理的域对象业务累.
	 */
	private M domainService;

	/**
	 * 编辑页面路径
	 */
	private String editPage;

	/**
	 * 查看页面路径
	 */
	private String showPage;

	/**
	 * 列表页面路径
	 */
	private String listPage;
	
	/**
	 * 首页
	 */
	private String indexPage;

	/**
	 * 通用的首页显示
	 */
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		return new ModelAndView(getIndexPage(), map);
	}
	
	/**
	 * 通用的列表查询方法.
	 * 多条件查询：
	 * 例 请求参数：
	 * msgTitle__like__string=测试   指msgTitle字段like “%测试%” 的限制；
	 * item.itemId__eq__int=5       指栏目ID即item.itemId=5的限制
	 * orders=mdfTime__desc          指按照mdfTime字段降序排序
	 * @see SqlExpression
	 */
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		int curPage = this.getCurrentPage(request); // 当前查询页数
		Criteria criteria = getDomainService().createCriteria(domainClass);
		setSqlExpression(request, criteria);
		Page page = getDomainService().pagedQuery(criteria, curPage, Page.DEFAULT_PAGE_SIZE);
		//TODO 完善page对象
		page.setPaginate(this.getAjaxPage(request, curPage, page, StringUtil.unCapitalize(domainClass.getSimpleName()) + "Jump"));
		map.put("page", page);
		return new ModelAndView(getListPage(), map);
	}

	protected void setSqlExpression(HttpServletRequest request, Criteria criteria) {
		String orders = request.getParameter("orders");
		if(orders != null && orders.length() > 0){
			for(String fieldAndType : orders.split(",")){
				if(SqlExpression.OrderType.ASC.toString().equalsIgnoreCase(fieldAndType.split("__")[1])){
					criteria.addOrder(Order.asc(fieldAndType.split("__")[0]));
				}else if(SqlExpression.OrderType.DESC.toString().equalsIgnoreCase(fieldAndType.split("__")[1])){
					criteria.addOrder(Order.desc(fieldAndType.split("__")[0]));
				}
				
			}
		}
		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String element = (String) params.nextElement();
			String[] nameAndType = element.split("__");
			if(nameAndType.length == 3){
				String v = request.getParameter(element);
				if(StringUtils.isBlank(v)){
					continue;
				}
				Object value = null;
				switch(SqlExpression.DataType.valueOf(nameAndType[2].toUpperCase())){
				case STRING:
					value = v;
					break;
				case INT:
					value = Integer.parseInt(v);
					break;
				case DATE:
					value = DateUtil.parseSimpleDateTime(v);
					break;
				default:
					break;
				}
				
				switch (SqlExpression.Symbol.valueOf(nameAndType[1].toUpperCase())) {
				case EQ:
					criteria.add(Expression.eq(nameAndType[0], value));
					break;
				case GT:
					criteria.add(Expression.gt(nameAndType[0], value));
					break;
				case GTE:
					criteria.add(Expression.ge(nameAndType[0], value));
					break;
				case LT:
					criteria.add(Expression.lt(nameAndType[0], value));
					break;
				case LTE:
					criteria.add(Expression.le(nameAndType[0], value));
					break;
				case LIKE:
					criteria.add(Expression.like(nameAndType[0], "%" + value + "%"));
					break;
				case LEFTLIKE:
					criteria.add(Expression.like(nameAndType[0], value + "%"));
					break;
				default:
					break;
				}
			}else if(nameAndType.length == 2){
				if(SqlExpression.OrderType.ASC.toString().equalsIgnoreCase(nameAndType[1])){
					criteria.addOrder(Order.asc(nameAndType[0]));
				}else if(SqlExpression.OrderType.DESC.toString().equalsIgnoreCase(nameAndType[1])){
					criteria.addOrder(Order.desc(nameAndType[0]));
				}
				
			}
		}
	}

	/**
	 * 通用的新增对象的界面.
	 */
	public ModelAndView create(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		map.put(getDomainName(), doNewDomain());
		return new ModelAndView(getEditPage(), map);
	}
	
	/**
	 * 通用的编辑对象的界面.
	 */
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		map.put(getDomainName(), doGetDomain(request));
		return new ModelAndView(getEditPage(), map);
	}

	/**
	 * 通用的只读显示对象细节的界面.
	 */
	public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		map.put(getDomainName(), doGetDomain(request));
		return new ModelAndView(getShowPage(), map);
	}

	/**
	 * 通用的新增/修改对象的保存.
	 */
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		T o = (T)getCommandObject(request, getDomainClass());
		if(isDomainIdBlank(request)){
			getDomainService().save(o);
		}else{
			getDomainService().saveOrUpdate(o);
		}
		AjaxView view = new AjaxView(true, "操作成功！");
		view.setProperty("refresh", true);
		return new ModelAndView(view);
	}

	/**
	 * 通用的删除单个或多个对象.
	 */
	public ModelAndView remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] idStrs = request.getParameterValues(idName);
		if (log.isInfoEnabled()) {
			log.info("要删除的对象ID为：" + StringUtil.join(idStrs, ","));
		}
		for(String idStr : idStrs){
			if(idStr.length() > 0){
				getDomainService().removeById((Serializable) ConvertUtils.convert(idStr, idClass));
			}
		}
		AjaxView view = new AjaxView(true, "操作成功！");
		view.setProperty("refresh", true);
		return new ModelAndView(view);
	}
	
	public String getIndexPage() {
		return indexPage;
	}

	public Class<T> getDomainClass() {
		return domainClass;
	}

	public M getDomainService() {
		return domainService;
	}

	/* 
	 * InitializingBean接口的方法，主要在bean组装完以后执行初始化动作。
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		domainClass = GenericsUtils.getSuperClassGenricType(getClass());

		// 根据M,反射获得符合M类型的service
		List<Field> fields = BeanUtils.getFieldsByType(this, GenericsUtils.getSuperClassGenricType(getClass(), 1));
		Assert.isTrue(fields.size() == 1,
				"subclass's has not only one entity manager property.");
		try {
			domainService = (M) BeanUtils.forceGetProperty(this, fields.get(0).getName());
			Assert.notNull(domainService,"subclass not inject manager to action sucessful.");
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}

		// 反射获得domain的主键类型
		try {
			idName = domainService.getIdName(domainClass);
			idClass = BeanUtils.getPropertyType(domainClass, idName);
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
	}

	/**
	 * 从request中获得Entity的id，并判断其有效性.
	 */
	protected Serializable getDomainId(HttpServletRequest request) {
		String idString = request.getParameter(idName);
		try {
			return (Serializable) ConvertUtils.convert(idString, idClass);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Wrong when get id from request");
		}
	}

	private boolean isDomainIdBlank(HttpServletRequest request) {
		String idString = request.getParameter(idName);
		if (StringUtils.isBlank(idString)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取域对象列表的函数.
	 */
	protected List<T> doListDomain() {
		return getDomainService().getAll();
	}

	/**
	 * 新建域对象的函数.
	 */
	protected T doNewDomain() {
		T object = null;
		try {
			object = getDomainClass().newInstance();
		} catch (Exception e) {
			log.error("Can't new Instance of domain.", e);
		}
		return object;
	}

	/**
	 * 从数据库获取域对象的函数.
	 */
	protected T doGetDomain(HttpServletRequest request) {
		Serializable id = getDomainId(request);
		return getDomainService().get(id);
	}

	/**
	 * 保存域对象的函数.
	 */
	protected void doSaveDomain(HttpServletRequest request, T object) {
		getDomainService().save(object);
	}

	/**
	 * 删除域对象的函数.
	 */
	protected void doDeleteEntity(HttpServletRequest request) {
		Serializable id = getDomainId(request);
		getDomainService().removeById(id);
	}

	/**
	 * 获取所管理的对象名. 首字母小写，如"user".
	 */
	protected String getDomainName() {
		return StringUtils.uncapitalize(ClassUtils
				.getShortName(getDomainClass()));
	}

	/**
	 * 获取所管理的对象列表名. 首字母小写，如"users".
	 */
	protected String getDomainListName() {
		return StringUtils.uncapitalize(ClassUtils.getShortName(getDomainClass())) + "s";
	}

	public String getEditPage() {
		return editPage;
	}

	public void setEditPage(String editPage) {
		this.editPage = editPage;
	}

	public String getListPage() {
		return listPage;
	}

	public void setListPage(String listPage) {
		this.listPage = listPage;
	}

	public String getShowPage() {
		return showPage;
	}

	public void setShowPage(String showPage) {
		this.showPage = showPage;
	}

	public void setIndexPage(String indexPage) {
		this.indexPage = indexPage;
	}

}
