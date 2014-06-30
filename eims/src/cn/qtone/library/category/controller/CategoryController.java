package cn.qtone.library.category.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.library.category.domain.Category;
import cn.qtone.library.category.service.CategoryService;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;

/**
 *
 * @author 邝炳研
 * @version 1.0
 */
public class CategoryController extends SimpleManageController<Category, CategoryService> {

	CategoryService categoryService;
	private String childrenPage;
	
	public String getChildrenPage() {
		return childrenPage;
	}
	public void setChildrenPage(String childrenPage) {
		this.childrenPage = childrenPage;
	}
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		return new ModelAndView(getIndexPage(), map);
	}
	public ModelAndView list(HttpServletRequest req,HttpServletResponse res){
		Map<String,Object> map=this.getMapWithUser(req);
		int currentPage=this.getCurrentPage(req, "page");
		Criteria criteria = categoryService.createCriteria(Category.class);
		String categoryName = ServletUtil.removeSpace(req, "categoryName"); 
		String categoryCode = ServletUtil.removeSpace(req, "categoryCode"); 
		if(categoryName!=null&&categoryName.length()>0)
			criteria.add(Restrictions.like("name", "%"+categoryName+"%"));
		if(categoryCode!=null&&categoryCode.length()>0)
			criteria.add(Restrictions.like("code", "%"+categoryCode+"%"));
		criteria.add(Restrictions.isNull("parent_id"));
		criteria.addOrder(Order.asc("code"));
		
		this.setSqlExpression(req, criteria);
		Page page=this.getDomainService().pagedQuery(criteria, currentPage,10 );
		page.setPaginate(this.getAjaxPage(req, currentPage, page,StringUtil.unCapitalize(this.getDomainName())+"Jump"));
		map.put("page",page);
		
		return new ModelAndView(this.getListPage(),map);
	}
	public ModelAndView childrenList(HttpServletRequest req,HttpServletResponse res){
		Map<String,Object> map=this.getMapWithUser(req);
		Criteria criteria = categoryService.createCriteria(Category.class);
		criteria.add(Restrictions.isNull("parent_id"));
		criteria.addOrder(Order.asc("id"));
		List<Category> list = categoryService.queryCategoryParent(criteria);
		map.put("list",list);
		return new ModelAndView(this.getChildrenPage(),map);
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView create(HttpServletRequest req, HttpServletResponse res){
		Map<String, Object> map = this.getMapWithUser(req);
		map.put(getDomainName(), doNewDomain());
		map.put("parentSelectHtml", this.categoryService.getAllCategoryOptionHtml(-1));
		return new ModelAndView(getEditPage(), map);
	}
	
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res){
		Map<String, Object> map = this.getMapWithUser(req);
		Category category=doGetDomain(req);
		map.put("category", category);
		map.put("parentSelectHtml", this.categoryService.getAllCategoryOptionHtml(category.getParent_id()==null?-1:category.getParent_id()));
		return new ModelAndView(getEditPage(), map);
	}
	
	public ModelAndView save(HttpServletRequest req, HttpServletResponse res){
		Category category=(Category)this.getCommandObject(req, Category.class);
		Integer parent_id=ServletUtil.getIntParamWithNoException(req, "parent_id");
		if(parent_id>=0){
			category.setParent_id(parent_id);
		}else
			category.setParent_id(null);
		if(!this.categoryService.isUnique(category,"name"))
			return new ModelAndView(new AjaxView(true, "此分类名已经存在,保存失败！"));
		if(StringUtils.isBlank(req.getParameter("id"))) 
			this.categoryService.save(category);
		else 
			this.categoryService.saveOrUpdate(category);
		return new ModelAndView(new AjaxView(true, "操作成功！"));
	}
	
	/**
	 * 删除机构
	 */
	public ModelAndView remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*String[] idStrs = request.getParameterValues(idName);
		if (log.isInfoEnabled()) {
			log.info("要删除的对象ID为：" + StringUtil.join(idStrs, ","));
		}
		int i=0;
		for(String idStr : idStrs){
			if(idStr.length() > 0){
				Integer id=(Integer)ConvertUtils.convert(idStr, idClass);
				
				getDomainService().removeById(id);
			}
		}*/
		System.out.println("id--->"+request.getParameter("id"));
		Integer id=Integer.parseInt(request.getParameter("id"));
		
		getDomainService().removeById(id);
		AjaxView view = new AjaxView(true, id>0?"分类有下属子分类,未删除!":"操作成功！");
		view.setProperty("refresh", true);
		return new ModelAndView(view);
	}
	
	

	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

}

