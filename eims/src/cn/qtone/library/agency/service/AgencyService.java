package cn.qtone.library.agency.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.qtone.common.simplemvc.dao.HibernateSimpleDao;
import cn.qtone.library.agency.domain.Agency;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;
import cn.qtone.qtoneframework.web.view.poi.ExcelUtils;

/**
 * 机构管理 - 机构管理
 *
 * @author 贺少辉
 * @version 1.0
 */
public class AgencyService extends HibernateSimpleDao<Agency> {
	
	/**
	 * 把机构列表用Html的&lt;option&gt;标签表示
	 */
	@SuppressWarnings("unchecked")
	public String getAgencyOptionHtmlFromCity(String city_id , int SelectedAgcyId){
		return getAgencyOptionHtml(queryAgencyByCity(city_id),SelectedAgcyId);
	}
	/**
	 * 根据系统参数类型返回该系统参数类型对应的下拉选择列表<br>
	 * @param param_type
	 */
	@SuppressWarnings("unchecked")
	public String getParamSelectHtml(String selected){
		StringBuffer sb = new StringBuffer();
		Criteria criteria = this.createCriteria(Agency.class);
		 
	    for(Agency agency:(ArrayList<Agency>)criteria.list()){
	    	sb.append("<option value='");
			sb.append(agency.getId());
            sb.append("'");
			if(ServletUtil.parseInt(selected,-1)==agency.getId()){
				sb.append(" selected");
			}
            sb.append(">");
            sb.append(agency.getAgency_name());
            sb.append("</option>");
	    }
		return sb.toString();
	}
	@SuppressWarnings("unchecked")
	private List<Agency> queryAgencyByCity(String city_id){
		Criteria criteria = this.createCriteria(Agency.class);
		if(!"-1".equals(city_id)&&StringUtils.isNotBlank(city_id)){
		  criteria.add(Restrictions.eq("city.id", ServletUtil.parseInt(city_id, -1)));
		}
		criteria.addOrder(Order.asc("agency_order"));
		criteria.addOrder(Order.desc("id"));
		return (ArrayList<Agency>)criteria.list(); 
	}
	
	private String getAgencyOptionHtml(List<Agency> list, int SelectedAgcyId){
		StringBuilder sb=new StringBuilder();
		sb.append("<option value='-1'>--请选择--</option>");
		if(list==null||list.size()==0)return sb.toString();
		for(Agency agcy : list){
			if(agcy.getParent_id()==null)continue;
			sb.append("<option value='");
			sb.append(agcy.getId());
			if(agcy.getId()==SelectedAgcyId)sb.append("' selected>");
			else sb.append("'>");
			sb.append(agcy.getAgency_name()+"</option>");
		}
		return sb.toString();
	}
	
	public String getAllAgencyOptionHtml(int id){
		return getAgencyOptionHtml(this.getAll(),id);
	}
	
	@SuppressWarnings("unchecked")
	public Agency queryAgencyByAgencyName(String AgencyName){
		Criteria criteria = this.createCriteria(Agency.class);
		if(StringUtils.isNotBlank(AgencyName)){
		  criteria.add(Restrictions.like("agency_name", AgencyName));
		  criteria.add(Restrictions.eq("parent_id",1));
		}
		List<Agency> list = (ArrayList<Agency>)criteria.list();
		return list.size()>0?list.get(0):null; 
	}
	
	/**
	 * Excel数据导出.
	 */
	public HSSFWorkbook getAgencyExport(List<Agency> list,String templatePath) throws FileNotFoundException, IOException {
		HSSFWorkbook wb = ExcelUtils.loadWorkbook(templatePath);
		HSSFSheet sheet = wb.getSheetAt(0);
		ExcelUtils.setValue(sheet, 0, 0,"机构列表");
		int row = 2;
        for(Agency agcy:list){
        	Integer parentId=agcy.getParent_id();
        	if(parentId==null)continue;
        	ExcelUtils.setValue(sheet, row, 0, agcy.getAgency_name());
        	ExcelUtils.setValue(sheet, row, 1, agcy.getAgency_shortname()==null?"":agcy.getAgency_shortname());
        	ExcelUtils.setValue(sheet, row, 2, agcy.getCity()==null?"":agcy.getCity().getName());
        	ExcelUtils.setValue(sheet, row, 3, agcy.getAgency_desc()==null?"":agcy.getAgency_desc());
        	row++;
        }
		return wb;
	}
}


