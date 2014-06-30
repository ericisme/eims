package cn.qtone.library.city.service;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import cn.qtone.common.simplemvc.dao.HibernateSimpleDao;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;
import cn.qtone.library.XxptContants;
import cn.qtone.library.city.domain.City;
/**
 * 城市、镇区业务类.<br>
 * 
 * @author 贺少辉
 * @version 1.0
 *
 */
public class CityService extends HibernateSimpleDao<City>{

	/**
	 * 获取一级城市[例如:广东省,北京市等]的下拉选择框.<br />
	 * @param selectedId 默认选择项的ID<br />
	 * only option
	 */
	public String getFirstCityHtmlSelect(String selectedId){
		StringBuffer sb = new StringBuffer();
        //List<City> list = queryCityListByParentCityId("0","0");
		return sb.toString();
	}
	
	/**
	 * 获取二级城市[例如:中山市,广州市等]的下拉选择框.<br />
	 * @param firstId 一级城市ID<br />
	 * @param selectedId 默认选择项的ID<br /> 
	 * only  option
	 */
	public String getSecondCityHtmlSelect(String firstCityId,String selectId){
		StringBuffer sb = new StringBuffer();
        //List<City> list = queryCityListByParentCityId(firstCityId,"1");
		return sb.toString();
	}
	
	/**
	 * 获取三级城市[例如:石岐区,东区等]的下拉选择框.<br />
	 * @param secondId 一级城市ID<br />
	 * @param selectedId 默认选择项的ID<br />
	 * only  option
	 */
	public String getThirdCityHtmlSelect(String secondCityId,String selectedId){
		return getCityHtmlSelectByCityList(queryCityListByParentCityId(secondCityId,new Integer[]{2,3}),selectedId);
	}
	
	//对城市列表进行下拉选择框的封装.
	private String getCityHtmlSelectByCityList(List<City> list,String selectedId){
		StringBuffer sb = new StringBuffer();
		sb.append("<option value=\"-1\">--请选择--</option>");
		for(City city:list){
			sb.append("<option value='");
			sb.append(city.getId());
            sb.append("'");
			if(ServletUtil.parseInt(selectedId,-1)==city.getId()){
				sb.append(" selected");
			}
            sb.append(">");
            sb.append(city.getName());
            sb.append("</option>");
		}
		return sb.toString();
	}
	
	
	/*
	public String getZSCityHtmlSelect(Integer selectedId,boolean disabled){
		Criteria criteria = this.createCriteria(City.class);
		criteria.add(Restrictions.eq("parentId", ServletUtil.parseInt(XxptContants.ZSSCITYID, -1)));
		criteria.add(Restrictions.in("levelNo", new Integer[]{2,3}));
		List<City> list =  criteria.list();
		StringBuffer sb = new StringBuffer();
		sb.append("<option value=\"-1\">--请选择--</option>");
		for(City city:list){
			sb.append("<option value='");
			sb.append(city.getId());
            sb.append("'");
			if(city.getId().equals(selectedId)){
				sb.append(" selected");
			}
            sb.append(">");
            sb.append(city.getName());
            sb.append("</option>");
		}
		return sb.toString();
	}*/
	
	/**
	 * 获取三级城市[例如:石岐区,东区等]的下拉选择框.<br />
	 * @param secondId 一级城市ID<br />
	 * @param selectedId 默认选择项的ID<br />
	 * only  option
	 */
	public String getThirdCityHtmlSelect(String secondCityId,Integer selectedId){
		return getCityHtmlSelectByCityList(queryCityListByParentCityId(secondCityId,new Integer[]{2,3}),selectedId);
	}
	
	//对城市列表进行下拉选择框的封装.
	private String getCityHtmlSelectByCityList(List<City> list,Integer selectedId){
		StringBuffer sb = new StringBuffer();
		sb.append("<option value=\"-1\">--请选择--</option>");
		for(City city:list){
			sb.append("<option value='");
			sb.append(city.getId());
            sb.append("'");
			if(city.getId().equals(selectedId)){
				sb.append(" selected");
			}
            sb.append(">");
            sb.append(city.getName());
            sb.append("</option>");
		}
		return sb.toString();
	}
	
	public static String getCityHtmlSelectByCity(City city){
		StringBuffer sb = new StringBuffer();
			sb.append("<option value='");
			sb.append(city.getId());
            sb.append("'>");
            sb.append(city.getName());
            sb.append("</option>");
		return sb.toString();
	}
	
	 //根据父级城市和要获取城市的级别取得城市的列表.
	@SuppressWarnings("unchecked")
	private List<City> queryCityListByParentCityId(String parentId,Integer[] levelNo){
		Criteria criteria = this.createCriteria(City.class);
		criteria.add(Restrictions.eq("parentId", ServletUtil.parseInt(parentId, -1)));
		criteria.add(Restrictions.in("levelNo", levelNo));
		return criteria.list();
	}
}
