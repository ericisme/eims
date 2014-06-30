package cn.qtone.library.category.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.qtone.common.simplemvc.dao.HibernateSimpleDao;
import cn.qtone.library.category.domain.Category;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;

/**
 * 机构管理 - 机构管理
 *
 * @author 贺少辉
 * @version 1.0
 */
public class CategoryService extends HibernateSimpleDao<Category> {
	
	/**
	 * 根据系统参数类型返回该系统参数类型对应的下拉选择列表<br>
	 * @param param_type
	 */
	@SuppressWarnings("unchecked")
	public String getParamSelectHtml(String selected){
		StringBuffer sb = new StringBuffer();
		Criteria criteria = this.createCriteria(Category.class);
		 
	    for(Category agency:(ArrayList<Category>)criteria.list()){
	    	sb.append("<option value='");
			sb.append(agency.getId());
            sb.append("'");
			if(ServletUtil.parseInt(selected,-1)==agency.getId()){
				sb.append(" selected");
			}
            sb.append(">");
            //sb.append(agency.getCategory_name());
            sb.append("</option>");
	    }
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	public List<Category> queryCategoryParent(Criteria criteria){
		
		return criteria.list(); 
	}
	
	private String getCategoryOptionHtml(List<Category> list, int SelectedAgcyId){
		StringBuilder sb=new StringBuilder();
		sb.append("<option value='-1'>--请选择--</option>");
		if(list==null||list.size()==0)return sb.toString();
		for(Category agcy : list){
			if(agcy.getId()==null)continue;
			sb.append("<option value='");
			sb.append(agcy.getId());
			if(agcy.getId()==SelectedAgcyId)sb.append("' selected>");
			else sb.append("'>");
			sb.append(agcy.getName()+"</option>");
		}
		return sb.toString();
	}
	
	public String getAllCategoryOptionHtml(int selectId){
		Criteria criteria = this.createCriteria(Category.class);
		criteria.add(Restrictions.isNull("parent_id"));
		criteria.addOrder(Order.asc("id"));
		List<Category> list = criteria.list(); 
		StringBuilder sb=new StringBuilder();
		sb.append("<option value='-1'>一级分类</option>");
		if(list==null||list.size()==0)return sb.toString();
		for(Category agcy : list){
			if(agcy.getId()==null)continue;
			sb.append("<option value='");
			sb.append(agcy.getId());
			if(agcy.getId()==selectId)sb.append("' selected>");
			else sb.append("'>");
			sb.append(agcy.getCode());
			sb.append(" ");
			sb.append(agcy.getName());
			sb.append("</option>");
		}
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public Category queryCategoryByCategoryName(String CategoryName){
		Criteria criteria = this.createCriteria(Category.class);
		if(StringUtils.isNotBlank(CategoryName)){
		  criteria.add(Restrictions.like("agency_name", CategoryName));
		  criteria.add(Restrictions.eq("parent_id",1));
		}
		List<Category> list = (ArrayList<Category>)criteria.list();
		return list.size()>0?list.get(0):null; 
	}
	
}


