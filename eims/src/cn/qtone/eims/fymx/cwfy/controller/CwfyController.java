package cn.qtone.eims.fymx.cwfy.controller;

/**
 * 财务费用明细或营业外支出
 */
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.eims.fymx.cwfy.domain.Cwfy;
import cn.qtone.eims.fymx.cwfy.service.CwfyService;
import cn.qtone.eims.util.EimsUtil;

public class CwfyController extends SimpleManageController<Cwfy, CwfyService> {

	private CwfyService service;

	public CwfyService getService() {
		return service;
	}

	public void setService(CwfyService service) {
		this.service = service;
	}

	
	/**
	 * 通用的首页显示
	 */
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);	
		
		String switcher = request.getParameter("switcher");
		map.put("switcher", switcher);//	switcher=1为	 财务费用,switcher=2为 营业外支出 ,switcher=3为 管理费用
		
		return new ModelAndView(getIndexPage(), map);
	}
	
	/**
	 * 通用的新增/修改对象的保存.
	 */
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Cwfy o = (Cwfy)getCommandObject(request, getDomainClass());
		o.setLrsj(new Date());
		if(o.getId()==null){
			getDomainService().save(o);
		}else{
			getDomainService().saveOrUpdate(o);
		}
		AjaxView view = new AjaxView(true, "操作成功！");
		view.setProperty("refresh", false);
		return new ModelAndView(view);
	}
	
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		int curPage = this.getCurrentPage(request); // 当前查询页数
		Criteria criteria = getDomainService().createCriteria(domainClass);
		setSqlExpression(request, criteria);		
		Page page = getDomainService().pagedQuery(criteria, curPage, Page.DEFAULT_PAGE_SIZE);
		//TODO 完善page对象
		page.setPaginate(this.getAjaxPage(request, curPage, page, StringUtil.unCapitalize(domainClass.getSimpleName()) + "Jump"));
		map.put("page", page);		
		
		
		//统计总数行,其中一项为扣减。
		Criteria criteria101 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria101);	
		Criteria criteria102 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria102);
		Criteria criteria103 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria103);
		Criteria criteria104 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria104);
		Criteria criteria201 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria201);
		Criteria criteria3001 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria3001);
		Criteria criteria3002 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria3002);
		Criteria criteria3003 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria3003);
		Criteria criteria3004 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria3004);
		Criteria criteria3005 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria3005);
		Criteria criteria3006 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria3006);
		Criteria criteria3007 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria3007);
		Criteria criteria3008 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria3008);
		Criteria criteria3009 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria3009);
		Criteria criteria3010 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria3010);
		Criteria criteria3011 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria3011);
		Criteria criteria3012 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria3012);
		Criteria criteria3013 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria3013);
		Criteria criteria3014 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria3014);
		
		BigDecimal type_101_je = (BigDecimal) criteria101.add(Restrictions.eq("type", "101")).setProjection(Projections.sum("je")).uniqueResult();
		BigDecimal type_102_je = (BigDecimal) criteria102.add(Restrictions.eq("type", "102")).setProjection(Projections.sum("je")).uniqueResult();
		BigDecimal type_103_je = (BigDecimal) criteria103.add(Restrictions.eq("type", "103")).setProjection(Projections.sum("je")).uniqueResult();
		BigDecimal type_104_je = (BigDecimal) criteria104.add(Restrictions.eq("type", "104")).setProjection(Projections.sum("je")).uniqueResult();
		BigDecimal type_201_je = (BigDecimal) criteria201.add(Restrictions.eq("type", "201")).setProjection(Projections.sum("je")).uniqueResult();
		BigDecimal type_3001_je = (BigDecimal) criteria3001.add(Restrictions.eq("type", "3001")).setProjection(Projections.sum("je")).uniqueResult();
		BigDecimal type_3002_je = (BigDecimal) criteria3002.add(Restrictions.eq("type", "3002")).setProjection(Projections.sum("je")).uniqueResult();
		BigDecimal type_3003_je = (BigDecimal) criteria3003.add(Restrictions.eq("type", "3003")).setProjection(Projections.sum("je")).uniqueResult();
		BigDecimal type_3004_je = (BigDecimal) criteria3004.add(Restrictions.eq("type", "3004")).setProjection(Projections.sum("je")).uniqueResult();
		BigDecimal type_3005_je = (BigDecimal) criteria3005.add(Restrictions.eq("type", "3005")).setProjection(Projections.sum("je")).uniqueResult();
		BigDecimal type_3006_je = (BigDecimal) criteria3006.add(Restrictions.eq("type", "3006")).setProjection(Projections.sum("je")).uniqueResult();
		BigDecimal type_3007_je = (BigDecimal) criteria3007.add(Restrictions.eq("type", "3007")).setProjection(Projections.sum("je")).uniqueResult();
		BigDecimal type_3008_je = (BigDecimal) criteria3008.add(Restrictions.eq("type", "3008")).setProjection(Projections.sum("je")).uniqueResult();
		BigDecimal type_3009_je = (BigDecimal) criteria3009.add(Restrictions.eq("type", "3009")).setProjection(Projections.sum("je")).uniqueResult();
		BigDecimal type_3010_je = (BigDecimal) criteria3010.add(Restrictions.eq("type", "3010")).setProjection(Projections.sum("je")).uniqueResult();
		BigDecimal type_3011_je = (BigDecimal) criteria3011.add(Restrictions.eq("type", "3011")).setProjection(Projections.sum("je")).uniqueResult();
		BigDecimal type_3012_je = (BigDecimal) criteria3012.add(Restrictions.eq("type", "3012")).setProjection(Projections.sum("je")).uniqueResult();
		BigDecimal type_3013_je = (BigDecimal) criteria3013.add(Restrictions.eq("type", "3013")).setProjection(Projections.sum("je")).uniqueResult();
		BigDecimal type_3014_je = (BigDecimal) criteria3014.add(Restrictions.eq("type", "3014")).setProjection(Projections.sum("je")).uniqueResult();
		
		type_101_je = type_101_je==null?(new BigDecimal(0)):type_101_je;
		type_102_je = type_102_je==null?(new BigDecimal(0)):type_102_je;
		type_103_je = type_103_je==null?(new BigDecimal(0)):type_103_je;
		type_104_je = type_104_je==null?(new BigDecimal(0)):type_104_je;
		type_201_je = type_201_je==null?(new BigDecimal(0)):type_201_je;		
		type_3001_je = type_3001_je==null?(new BigDecimal(0)):type_3001_je;
		type_3002_je = type_3002_je==null?(new BigDecimal(0)):type_3002_je;
		type_3003_je = type_3003_je==null?(new BigDecimal(0)):type_3003_je;
		type_3004_je = type_3004_je==null?(new BigDecimal(0)):type_3004_je;
		type_3005_je = type_3005_je==null?(new BigDecimal(0)):type_3005_je;
		type_3006_je = type_3006_je==null?(new BigDecimal(0)):type_3006_je;
		type_3007_je = type_3007_je==null?(new BigDecimal(0)):type_3007_je;
		type_3008_je = type_3008_je==null?(new BigDecimal(0)):type_3008_je;
		type_3009_je = type_3009_je==null?(new BigDecimal(0)):type_3009_je;
		type_3010_je = type_3010_je==null?(new BigDecimal(0)):type_3010_je;
		type_3011_je = type_3011_je==null?(new BigDecimal(0)):type_3011_je;
		type_3012_je = type_3012_je==null?(new BigDecimal(0)):type_3012_je;
		type_3013_je = type_3013_je==null?(new BigDecimal(0)):type_3013_je;
		type_3014_je = type_3014_je==null?(new BigDecimal(0)):type_3014_je;
		
//		BigDecimal total = type_101_je
//						-type_102_je
//						+type_103_je
//						+type_104_je
//						+type_201_je
//						+type_3001_je
//						+type_3002_je
//						+type_3003_je
//						+type_3004_je
//						+type_3005_je
//						+type_3006_je
//						+type_3007_je
//						+type_3008_je
//						+type_3009_je
//						+type_3010_je
//						+type_3011_je
//						+type_3012_je
//						+type_3013_je	
//						+type_3014_je
//						;		
		BigDecimal total = type_101_je
				.subtract(type_102_je)
				.add(type_103_je)
				.add(type_104_je)
				.add(type_201_je)
				.add(type_3001_je)
				.add(type_3002_je)
				.add(type_3003_je)
				.add(type_3004_je)
				.add(type_3005_je)
				.add(type_3006_je)
				.add(type_3007_je)
				.add(type_3008_je)
				.add(type_3009_je)
				.add(type_3010_je)
				.add(type_3011_je)
				.add(type_3012_je)
				.add(type_3013_je)	
				.add(type_3014_je)
				;	
		
		
		map.put("sum_je", total);
		//是否为最后一页
		map.put("ifLastPage",EimsUtil.ifLastPage(curPage, page));
		
		return new ModelAndView(getListPage(), map);
	}
	
	

	
	
}
