package cn.qtone.library.school.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.mvc.view.spring.TextView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.common.utils.upload.DownloadInter;
import cn.qtone.common.utils.upload.UploadFactory;
import cn.qtone.library.XxptContants;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;
import cn.qtone.qtoneframework.web.view.poi.ExcelUtils;
import cn.qtone.library.agency.domain.Agency;
import cn.qtone.library.agency.service.AgencyService;
import cn.qtone.library.city.domain.City;
import cn.qtone.library.city.service.CityService;
import cn.qtone.library.school.domain.School;
import cn.qtone.library.school.service.SchoolService;
import cn.qtone.library.xxptclass.service.XxptClassService;

/**
 * 基础管理 - 学校管理控制器.<br>
 * 
 * @author 贺少辉
 * @version 1.0
 */
public class SchoolController extends
		SimpleManageController<School, SchoolService> {

	XxptClassService classService;
	SchoolService schoolService;
	AgencyService agencyService;
	CityService cityService;
	String importPage;

	@SuppressWarnings("unchecked")
	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = this.getMapWithUser(request);
		String citySelectHtml = this.cityService.getThirdCityHtmlSelect(
				XxptContants.ZSSCITYID, "3626");
		map.put("citySelectHtml", citySelectHtml);
		map.put("agencySelectHtml", this.agencyService
				.getAgencyOptionHtmlFromCity(ServletUtil.removeSpace(request,
						"city_id"), 18));
		return new ModelAndView(getIndexPage(), map);
	}

	@SuppressWarnings("unchecked")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = this.getMapWithUser(request);
		int currentPage = this.getCurrentPage(request, "page");
		Integer agencyId = ServletUtil.getIntParam(request, "agencyId");
		String schoolName = ServletUtil.removeSpace(request, "schoolName");
		Criteria criteria = this.schoolService.createCriteria();
		criteria.createAlias("agency", "agency");
		if (agencyId == null || agencyId == -1) {
			Integer cityId = ServletUtil.getIntParam(request, "cityId");
			if (cityId != null && cityId != -1) {
				List<Agency> agcyList = agencyService.createCriteria(
						Restrictions.eq("city.id", cityId)).list();
				if (agcyList.size() != 0)
					criteria.add(Restrictions.in("agency", agcyList));
				else criteria.add(Restrictions.eq("id", null));
			}
		} else {
			criteria.add(Restrictions.eq("agency.id", agencyId));
		}
		criteria.add(Restrictions.like("school_name", schoolName,
				MatchMode.ANYWHERE));
		criteria.addOrder(Order.asc("agency.agency_order"));
		criteria.addOrder(Order.asc("agency.id"));
		Page page = this.getDomainService().pagedQuery(criteria, currentPage,
				Page.DEFAULT_PAGE_SIZE);
		page.setPaginate(this.getAjaxPage(request, currentPage, page,
				StringUtil.unCapitalize(this.getDomainName()) + "Jump"));
		map.put("page", page);
		return new ModelAndView(this.getListPage(), map);
	}

	public ModelAndView create(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = this.getMapWithUser(request);
		map.put(getDomainName(), doNewDomain());
		String citySelectHtml = this.cityService.getThirdCityHtmlSelect(
				XxptContants.ZSSCITYID, -1);
		map.put("citySelectHtml", citySelectHtml);
		String agencySelectHtml = this.agencyService
				.getAgencyOptionHtmlFromCity("-1", -1);
		map.put("agencySelectHtml", agencySelectHtml);
		map.put("gradeCheckBox", XxptContants.getGradeTypeCheckBoxHtml(
				"containGrade", "#"));
		return new ModelAndView(getEditPage(), map);
	}

	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res) {
		Map<String, Object> map = this.getMapWithUser(req);
		School schl = doGetDomain(req);
		map.put(getDomainName(), schl);
		Agency agcy = schl == null ? null : schl.getAgency();
		Integer agcyId = agcy == null ? null : agcy.getId();
		City city = agcy == null ? null : agcy.getCity();
		Integer cityId = city == null ? -1 : city.getId();
		String citySelectHtml = this.cityService.getThirdCityHtmlSelect(
				XxptContants.ZSSCITYID, cityId);
		map.put("citySelectHtml", citySelectHtml);
		String agencySelectHtml = this.agencyService
				.getAgencyOptionHtmlFromCity(cityId == null ? "-1" : cityId
						.toString(), agcyId == null ? -1 : agcyId);
		map.put("agencySelectHtml", agencySelectHtml);
		map.put("gradeCheckBox", XxptContants.getGradeTypeCheckBoxHtml(
				"containGrade", schl.getContainGrade()));
		return new ModelAndView(getEditPage(), map);
	}

	public ModelAndView save(HttpServletRequest req, HttpServletResponse res) {
		School schl = (School) this.getCommandObject(req, School.class);
		Integer agcyId = ServletUtil.getIntParamWithNoException(req,
				"editAgencySelect");
		Agency agcy=this.agencyService.get(agcyId);
		Integer cityId=agcy.getCity().getId();
		if(!this.schoolService.isUniqueInTheSameCity(schl,cityId)) 
			return new ModelAndView(new AjaxView(true, "同一镇区下已有同名学校存在,保存失败！"));
		schl.setAgency(agcy);
		schl.setContainGrade(StringUtils.join(ServletUtil.removeMultiSpace(req,
				"containGrade"), ","));
		if (StringUtils.isBlank(req.getParameter("id"))){
			schl.setSchool_no(String.valueOf(schoolService.getMaxSchoolNO()+1));
			this.cityService.save(schl);
		}else{
			if(schl.getSchool_no()==null||schl.getSchool_no().length()==0)
				schl.setSchool_no(String.valueOf(schoolService.getMaxSchoolNO()+1));
			this.cityService.saveOrUpdate(schl);
		}
			
		return new ModelAndView(new AjaxView(true, "操作成功！"));
	}

	/**
	 * 删除机构
	 */
	public ModelAndView remove(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String[] idStrs = request.getParameterValues(idName);
		if (log.isInfoEnabled()) {
			log.info("要删除的对象ID为：" + StringUtil.join(idStrs, ","));
		}
		int i = 0;
		for (String idStr : idStrs) {
			if (idStr.length() > 0) {
				Integer id = (Integer) ConvertUtils.convert(idStr, idClass);
				if (this.classService.queryZfptClassBySchoolOrGrade(
						id.toString(), "-1").size() > 0) {
					i++;
					continue;
				}
				getDomainService().removeById(id);
			}
		}
		AjaxView view = new AjaxView(true, i > 0 ? i + "个学校有下属班级,未删除!"
				: "操作成功！");
		view.setProperty("refresh", true);
		return new ModelAndView(view);
	}

	/**
	 * 导入学校初始化
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView importSchoolInit(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> map = this.getMapWithUser(request);
		String agencySelectHtml = this.agencyService
				.getAgencyOptionHtmlFromCity("-1", -1);
		map.put("agencySelectHtml", agencySelectHtml);
		return new ModelAndView(this.importPage, map);
	}

	/**
	 * 模板下载
	 */
	public ModelAndView download(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DownloadInter download = UploadFactory.getDownloadInstance(response);
		try {
			download.download(request.getSession().getServletContext()
					.getRealPath("/")
					+ "/template/school_import_template.xls", "UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView(new TextView("未知原因导致文件下载失败！"));
		}
		return null;
	}

	/**
	 * 导入学校后的提交操作.
	 */
	public ModelAndView importSchoolSubmit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Agency agcy = this.agencyService.get( ServletUtil.getIntParam(request, "importAgencyId"));
		String filePath = request.getSession().getServletContext().getRealPath("/")
				+ ServletUtil.removeSpace(request, "importattach_path");
		List<School> schlList = this.schoolService.getImportStudentData(filePath);
		String[] gradeTypes = new String[] { "1", "2", "3", "4", "5" };
		StringBuilder sb = new StringBuilder();
		for (School schl : schlList) {
			String name=schl.getSchool_name();
			if("".equals(StringUtil.trim(name)))
				return new ModelAndView(new AjaxView(true,"导入失败，学校名称不能为空！"));
			String containGrade = schl.getContainGrade();
			if (containGrade == null)
				return new ModelAndView(new AjaxView(true,"导入失败，导入的所有记录都必须包含年级类型！"));
			String[] grades = containGrade.replaceAll("，", ",").split(",");
			for (String s : grades) {
				if (Arrays.binarySearch(gradeTypes, s) >= 0)sb.append(s + ",");
			}
			String temp = sb.substring(0, sb.length()).toString();
			if (temp.length() < 1)
				return new ModelAndView(new AjaxView(true,"导入失败，导入的所有记录都必须包含年级类型！"));
			schl.setContainGrade(temp);
			sb.delete(0, sb.length());
		}
		if (!this.schoolService.isUniqueInTheSameCity(schlList, agcy.getCity().getId())) {
			return new ModelAndView(new AjaxView(true, "导入失败，学校名称可能同名！"));
		}
		for (School schl : schlList) {
			schl.setSchool_no(String.valueOf(schoolService.getMaxSchoolNO()+1));
			schl.setAgency(agcy);
			this.schoolService.save(schl);
		}
		return new ModelAndView(new AjaxView(true, "导入成功！"));
	}

	/**
	 * 导出学校数据
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView downloadSchool(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Integer agencyId = ServletUtil.getIntParam(request, "agencyId");
		String schoolName = ServletUtil.removeSpace(request, "schoolName");
		Criteria criteria = this.schoolService.createCriteria();
		criteria.createAlias("agency", "agency");
		if (agencyId == null || agencyId == -1) {
			Integer cityId = ServletUtil.getIntParam(request, "cityId");
			if (cityId != null && cityId != -1) {
				List<Agency> agcyList = agencyService.createCriteria(
						Restrictions.eq("city.id", cityId)).list();
				if (agcyList.size() != 0)
					criteria.add(Restrictions.in("agency", agcyList));
				else criteria.add(Restrictions.eq("id", null));
			}
		} else {
			criteria.add(Restrictions.eq("agency.id", agencyId));
		}
		criteria.add(Restrictions.like("school_name", schoolName,
				MatchMode.ANYWHERE));
		criteria.addOrder(Order.asc("agency.id"));
		List<School> list = new ArrayList<School>();
		StringBuilder strBuf=new StringBuilder();	
		for(School schl : (List<School>)criteria.list()){
			String containGrade=schl.getContainGrade();
			if(containGrade!=null&&containGrade.length()>0){
				String[] gradeTypes=containGrade.split(",");
				for(String gradeType : gradeTypes){
					strBuf.append(XxptContants.GRADETYPE.get(gradeType)+",");
				}
				strBuf.deleteCharAt(strBuf.length()-1);
			}
			School school=new School();
			school.setAgency(schl.getAgency());
			school.setSchool_name(schl.getSchool_name());
			school.setSchoolDesc(schl.getSchoolDesc());
			school.setContainGrade(strBuf.toString());
			school.setSchool_no(schl.getSchool_no());
			school.setSchool_code(schl.getSchool_code()); 
			list.add(school);
			strBuf.delete(0, strBuf.length());
		}
		if(list.size()<1||list.size()>65500){
			String msg=list.size()<1 ?
					"<script>alert('导出的记录数为空，请选择合适的查询条件！');window.history.back();</script>"
					: "<script>alert('导出的记录数超出限制，请选择合适的查询条件以便分批导出！');window.history.back();</script>";
			ServletOutputStream out=response.getOutputStream();
			out.print(msg);
			out.close(); return null;
		}
		HSSFWorkbook workbook = this.schoolService.getStudentExport(list,
				request.getSession().getServletContext().getRealPath("/")
						+ "/template/school_export_template.xls");
		try {
			ExcelUtils.download(response, workbook, "学校数据表.xls");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 请求机构列表
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView getAgencySelect(HttpServletRequest req,
			HttpServletResponse res) {
		String htmlResult = this.agencyService.getAgencyOptionHtmlFromCity(
				ServletUtil.removeSpace(req, "city_id"), -1);
		
		System.out.println("##############################################################");
		System.out.println("##############################################################");
		System.out.println(htmlResult);
		System.out.println("##############################################################");
		System.out.println("##############################################################");
		
		return new ModelAndView(new TextView(htmlResult));
	}
	
	/**
	 * 请求学校列表
	 */
	public ModelAndView getSchoolSelect(HttpServletRequest req, HttpServletResponse res){
		
		String htmlResult = this.schoolService.getSchoolOptionHtmlByAgency(
				ServletUtil.removeSpace(req, "agency_id") , -1);
		return new ModelAndView(new TextView(htmlResult));
	}

	/**
	 * 请求年级列表
	 * @return
	 */
	public ModelAndView getGradeSelectHtmlByChangeSchool(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if(ServletUtil.getIntParam(request, "school_id") == -1)
			return new ModelAndView(
					new TextView(XxptContants.getGradeSelectHtml("1,2,3,4,5", "#")));
		else
			return new ModelAndView(new TextView(XxptContants.getGradeSelectHtml(
					this.schoolService.get(
							ServletUtil.getIntParam(request, "school_id"))
							.getContainGrade(), "#")));
	}
	
	public void setClassService(XxptClassService classService) {
		this.classService = classService;
	}

	public void setSchoolService(SchoolService schoolService) {
		this.schoolService = schoolService;
	}

	public void setCityService(CityService cityService) {
		this.cityService = cityService;
	}

	public void setAgencyService(AgencyService agencyService) {
		this.agencyService = agencyService;
	}

	public void setImportPage(String importPage) {
		this.importPage = importPage;
	}

}