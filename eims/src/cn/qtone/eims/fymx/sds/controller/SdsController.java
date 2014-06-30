package cn.qtone.eims.fymx.sds.controller;


import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.eims.fymx.gdzc.domain.Gdzc;
import cn.qtone.eims.fymx.gdzc.service.GdzcService;
import cn.qtone.eims.fymx.glfymx.service.GlfymxService;
import cn.qtone.eims.fymx.sds.domain.Sds;
import cn.qtone.eims.fymx.sds.service.SdsService;
import cn.qtone.eims.fymx.yggz.domain.Yggz;
import cn.qtone.eims.util.EimsUtil;
import cn.qtone.common.mvc.dao.Page;

/**
 * 所得税
 * @author Administrator
 *
 */
public class SdsController extends SimpleManageController<Sds, SdsService> {

	private SdsService service;

	/**
	 * 通用的新增/修改对象的保存.
	 */
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Sds o = (Sds)getCommandObject(request, getDomainClass());
		o.setLrsj(new Date());
		
		
		/**
		 * 特殊需求，金额=当月报关金额*汇率*备用1*备用2%
		 */
		o.setJe(o.getDybgje()*o.getHl()*o.getBy1()*o.getBy2()/100);
		
		
		if(isDomainIdBlank(request)){
			getDomainService().save(o);
		}else{
			getDomainService().saveOrUpdate(o);
		}
		AjaxView view = new AjaxView(true, "操作成功！");
		view.setProperty("refresh", true);
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
		
		//统计总数行，2项金额。
		Criteria criteria2 = getDomainService().createCriteria(domainClass);
		setSqlExpression(request, criteria2);	
		map.put("sum_dybgje",   (Double)criteria2.setProjection(Projections.sum("dybgje")).uniqueResult());
		map.put("sum_je", 		(Double)criteria2.setProjection(Projections.sum("je")).uniqueResult());
		//是否为最后一页
		map.put("ifLastPage",EimsUtil.ifLastPage(curPage, page));
	
		return new ModelAndView(getListPage(), map);
	}
	
	


	
	
	public SdsService getService() {
		return service;
	}

	public void setService(SdsService service) {
		this.service = service;
	}

	private boolean isDomainIdBlank(HttpServletRequest request) {
		String idString = request.getParameter(idName);
		if (StringUtils.isBlank(idString)) {
			return true;
		}
		return false;
	}
}
