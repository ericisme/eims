package cn.qtone.library.agency.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.library.XxptContants;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;
import cn.qtone.qtoneframework.web.view.poi.ExcelUtils;
import cn.qtone.library.agency.domain.Agency;
import cn.qtone.library.agency.service.AgencyService;
import cn.qtone.library.city.domain.City;
import cn.qtone.library.city.service.CityService;
import cn.qtone.library.school.service.SchoolService;

/**
 * 机构管理 - 机构
 *
 * @author 贺少辉
 * @version 1.0
 */
public class AgencyController extends SimpleManageController<Agency, AgencyService> {

	AgencyService agencyService;
	CityService cityService;
	SchoolService schoolService;
	
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		String citySelectHtml = this.cityService.getThirdCityHtmlSelect(
				XxptContants.ZSSCITYID, "#");
		map.put("citySelectHtml", citySelectHtml);
		return new ModelAndView(getIndexPage(), map);
	}
	public ModelAndView list(HttpServletRequest req,HttpServletResponse res){
		Map<String,Object> map=this.getMapWithUser(req);
		int currentPage=this.getCurrentPage(req, "page");
		Criteria criteria=this.getDomainService().createCriteria(this.getDomainClass());
		criteria.addOrder(Order.asc("agency_order"));
		criteria.createAlias("city", "city")
					.addOrder(Order.asc("city.name"))
					.add(Restrictions.isNotNull("parent_id"));
		Integer cityId=ServletUtil.getIntParam(req, "cityId");
		
		if(cityId!=null&&cityId>0)criteria.add(Restrictions.eq("city.id", cityId));
		this.setSqlExpression(req, criteria);
		Page page=this.getDomainService().pagedQuery(criteria, currentPage, Page.DEFAULT_PAGE_SIZE);
		page.setPaginate(
				this.getAjaxPage(req, currentPage, page,StringUtil.unCapitalize(this.getDomainName())+"Jump")
				);
		map.put("page",page);
		return new ModelAndView(this.getListPage(),map);
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView create(HttpServletRequest req, HttpServletResponse res){
		Map<String, Object> map = this.getMapWithUser(req);
		map.put(getDomainName(), doNewDomain());
		String citySelectHtml=this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, -1);
		map.put("citySelectHtml", citySelectHtml);
		map.put("parentSelectHtml", this.agencyService.getAllAgencyOptionHtml(-1));
		map.put("typeSelectHtml", cn.qtone.common.utils.base.SelectUtil.getAgencyTypeSelectHtml(-1));
		return new ModelAndView(getEditPage(), map);
	}
	
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res){
		Map<String, Object> map = this.getMapWithUser(req);
		Agency agcy=doGetDomain(req);
		map.put(getDomainName(), agcy);
		City city=agcy==null?null:agcy.getCity();
		Integer cityId=city==null?-1:city.getId();
		String citySelectHtml=this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, cityId);
		map.put("citySelectHtml",citySelectHtml);
		map.put("parentSelectHtml", this.agencyService.getAllAgencyOptionHtml(agcy.getParent_id()==null?-1:agcy.getParent_id()));
		map.put("typeSelectHtml", cn.qtone.common.utils.base.SelectUtil.getAgencyTypeSelectHtml(agcy.getAgency_type()==null?-1:agcy.getAgency_type()));
		return new ModelAndView(getEditPage(), map);
	}
	
	public ModelAndView save(HttpServletRequest req, HttpServletResponse res){
		Agency agency=(Agency)this.getCommandObject(req, Agency.class);
		Integer cityId=ServletUtil.getIntParamWithNoException(req, "city_id");
		if(cityId>=0){
			City city=new City();
			city.setId(cityId);
			agency.setCity(city);
		}
		Integer parent_id=ServletUtil.getIntParamWithNoException(req, "parent_id");
		if(parent_id>=-1){
			agency.setParent_id(parent_id);
		}
		Integer agency_type=ServletUtil.getIntParamWithNoException(req, "agency_type");
		if(agency_type>=0){
			agency.setAgency_type(agency_type);
		}
		if(!this.agencyService.isUnique(agency,"agency_name"))
			return new ModelAndView(new AjaxView(true, "此机构名已经存在,保存失败！"));
		if(StringUtils.isBlank(req.getParameter("id"))) 
			this.agencyService.save(agency);
		else 
			this.agencyService.saveOrUpdate(agency);
		return new ModelAndView(new AjaxView(true, "操作成功！"));
	}
	
	/**
	 * 删除机构
	 */
	public ModelAndView remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] idStrs = request.getParameterValues(idName);
		if (log.isInfoEnabled()) {
			log.info("要删除的对象ID为：" + StringUtil.join(idStrs, ","));
		}
		int i=0;
		for(String idStr : idStrs){
			if(idStr.length() > 0){
				Integer id=(Integer)ConvertUtils.convert(idStr, idClass);
				if(this.schoolService.getSchoolsByAgencyId(id).size()>0){
					i++; continue;
				}
				getDomainService().removeById(id);
			}
		}
		AjaxView view = new AjaxView(true, i>0?i+"个机构有下属学校,未删除!":"操作成功！");
		view.setProperty("refresh", true);
		return new ModelAndView(view);
	}
	
	/**
	 * 导出机构数据
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView downloadAgency(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Criteria criteria=this.getDomainService().createCriteria(this.getDomainClass());
		this.setSqlExpression(request, criteria);
		List<Agency> list=criteria.list();
		if(list.size()<1||list.size()>65500){
			String msg=list.size()<1 ?
					"<script>alert('导出的记录数为空，请选择合适的查询条件！');window.history.back();</script>"
					: "<script>alert('导出的记录数超出限制，请选择合适的查询条件以便分批导出！');window.history.back();</script>";
			ServletOutputStream out=response.getOutputStream();
			out.print(msg);
			out.close(); return null;
		}
		HSSFWorkbook workbook = this.agencyService.getAgencyExport(list,
				request.getSession().getServletContext().getRealPath("/")
						+ "/template/agency_export_template.xls");
		try {
			ExcelUtils.download(response, workbook, "机构数据表.xls");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setAgencyService(AgencyService agencyService) {
		this.agencyService = agencyService;
	}

	public void setCityService(CityService cityService) {
		this.cityService = cityService;
	}

	public void setSchoolService(SchoolService schoolService) {
		this.schoolService = schoolService;
	}
	
}

