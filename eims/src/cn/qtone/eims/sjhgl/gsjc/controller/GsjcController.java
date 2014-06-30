package cn.qtone.eims.sjhgl.gsjc.controller;


import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.eims.fymx.gdzc.domain.Gdzc;
import cn.qtone.eims.fymx.gdzc.service.GdzcService;
import cn.qtone.eims.fymx.glfymx.service.GlfymxService;
import cn.qtone.eims.fymx.yggz.domain.Yggz;
import cn.qtone.eims.sjhgl.gsjc.domain.Gsjc;
import cn.qtone.eims.sjhgl.gsjc.service.GsjcService;
import cn.qtone.eims.util.EimsUtil;
import cn.qtone.common.mvc.dao.Page;

/**
 * 公司借出
 * @author Administrator
 *
 */
public class GsjcController extends SimpleManageController<Gsjc, GsjcService> {

	private GsjcService service;

	/**
	 * 通用的新增/修改对象的保存.
	 */
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Gsjc o = (Gsjc)getCommandObject(request, getDomainClass());
		
		o.setJcje(0d);
		o.setGhje(0d);
		//余额=借出金额-归还金额
		o.setYe(o.getJcje()-o.getGhje());
		o.setLrsj(new Date());
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
		
		
		//统计总数行，3项金额。
		Criteria criteria2 = getDomainService().createCriteria(domainClass);
		setSqlExpression(request, criteria2);
		map.put("sum_jcje", (Double)criteria2.setProjection(Projections.sum("jcje")).uniqueResult());
		map.put("sum_ghje", (Double)criteria2.setProjection(Projections.sum("ghje")).uniqueResult());
		map.put("sum_ye",   (Double)criteria2.setProjection(Projections.sum("ye")) .uniqueResult());
		
		//统计 借出余额
		Criteria criteria11 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria11);	
		Criteria criteria12 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria12);
		Double lx_11_je = (Double) criteria11.add(Restrictions.eq("lx", 11)).setProjection(Projections.sum("je")).uniqueResult();
		Double lx_12_je = (Double) criteria12.add(Restrictions.eq("lx", 12)).setProjection(Projections.sum("je")).uniqueResult();
		lx_11_je = lx_11_je==null?0:lx_11_je;
		lx_12_je = lx_12_je==null?0:lx_12_je;
		//借出余额=借出-归还
		Double total = lx_11_je	-lx_12_je;
		map.put("sum_je", total);
		
		//是否为最后一页
		map.put("ifLastPage",EimsUtil.ifLastPage(curPage, page));
		
		
		return new ModelAndView(getListPage(), map);
	}
	
	


	
	
	public GsjcService getService() {
		return service;
	}

	public void setService(GsjcService service) {
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
