package cn.qtone.eims.fymx.glfymx.controller;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.eims.fymx.glfymx.domain.Glfymx;
import cn.qtone.eims.fymx.glfymx.service.GlfymxService;
import cn.qtone.eims.util.EimsUtil;

public class GlfymxController extends SimpleManageController<Glfymx, GlfymxService> {

	private GlfymxService service;

	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		int curPage = this.getCurrentPage(request); // 当前查询页数
		Criteria criteria = getDomainService().createCriteria(domainClass);
		setSqlExpression(request, criteria);		
		Page page = getDomainService().pagedQuery(criteria, curPage, Page.DEFAULT_PAGE_SIZE);
		//TODO 完善page对象
		page.setPaginate(this.getAjaxPage(request, curPage, page, StringUtil.unCapitalize(domainClass.getSimpleName()) + "Jump"));
		map.put("page", page);
		
		
		//统计总数行，12项金额。
		Criteria criteria2 = getDomainService().createCriteria(domainClass);
		setSqlExpression(request, criteria2);	
		map.put("sum_sbf", (BigDecimal)criteria2.setProjection(Projections.sum("sbf")).uniqueResult());
		map.put("sum_sdf", (BigDecimal)criteria2.setProjection(Projections.sum("sdf")).uniqueResult());
		map.put("sum_dhf", (BigDecimal)criteria2.setProjection(Projections.sum("dhf")).uniqueResult());
		map.put("sum_bgf", (BigDecimal)criteria2.setProjection(Projections.sum("bgf")).uniqueResult());
		map.put("sum_clf", (BigDecimal)criteria2.setProjection(Projections.sum("clf")).uniqueResult());
		map.put("sum_qyf", (BigDecimal)criteria2.setProjection(Projections.sum("qyf")).uniqueResult());
		map.put("sum_twf", (BigDecimal)criteria2.setProjection(Projections.sum("twf")).uniqueResult());
		map.put("sum_kdf", (BigDecimal)criteria2.setProjection(Projections.sum("kdf")).uniqueResult());
		map.put("sum_yhs", (BigDecimal)criteria2.setProjection(Projections.sum("yhs")).uniqueResult());
		map.put("sum_zj",  (BigDecimal)criteria2.setProjection(Projections.sum("zj")) .uniqueResult());
		map.put("sum_zcf", (BigDecimal)criteria2.setProjection(Projections.sum("zcf")).uniqueResult());
		map.put("sum_jzf", (BigDecimal)criteria2.setProjection(Projections.sum("jzf")).uniqueResult());
		map.put("sum_qt",  (BigDecimal)criteria2.setProjection(Projections.sum("qt")) .uniqueResult());
		//是否为最后一页
		map.put("ifLastPage",EimsUtil.ifLastPage(curPage, page));
		
		return new ModelAndView(getListPage(), map);
	}
	
	
	public GlfymxService getService() {
		return service;
	}
	public void setService(GlfymxService service) {
		this.service = service;
	}
	
	
}
