package cn.qtone.eims.sjhgl.gsjr.controller;


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
import cn.qtone.eims.sjhgl.gsjr.domain.Gsjr;
import cn.qtone.eims.sjhgl.gsjr.service.GsjrService;
import cn.qtone.eims.util.EimsUtil;
import cn.qtone.common.mvc.dao.Page;

/**
 * 公司借入
 * @author Administrator
 *
 */
public class GsjrController extends SimpleManageController<Gsjr, GsjrService> {

	private GsjrService service;

	/**
	 * 通用的新增/修改对象的保存.
	 */
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Gsjr o = (Gsjr)getCommandObject(request, getDomainClass());
		o.setJrje(0d);
		o.setHdje(0d);
		
		//余额=借入金额-还款金额
		o.setYe(o.getJrje()-o.getHdje());
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
		map.put("sum_jrje", (Double)criteria2.setProjection(Projections.sum("jrje")).uniqueResult());
		map.put("sum_hdje", (Double)criteria2.setProjection(Projections.sum("hdje")).uniqueResult());
		map.put("sum_ye",   (Double)criteria2.setProjection(Projections.sum("ye")) .uniqueResult());		
		
		
		//统计 借入余额
		Criteria criteria21 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria21);	
		Criteria criteria22 = getDomainService().createCriteria(domainClass);setSqlExpression(request, criteria22);
		Double lx_21_je = (Double) criteria21.add(Restrictions.eq("lx", 21)).setProjection(Projections.sum("je")).uniqueResult();
		Double lx_22_je = (Double) criteria22.add(Restrictions.eq("lx", 22)).setProjection(Projections.sum("je")).uniqueResult();
		lx_21_je = lx_21_je==null?0:lx_21_je;
		lx_22_je = lx_22_je==null?0:lx_22_je;
		//借入余额=借入-还贷
		Double total = lx_21_je	-lx_22_je;
		map.put("sum_je", total);
		
		
		
		
		
		
		//是否为最后一页
		map.put("ifLastPage",EimsUtil.ifLastPage(curPage, page));
		
		return new ModelAndView(getListPage(), map);
	}
	
	


	
	
	public GsjrService getService() {
		return service;
	}

	public void setService(GsjrService service) {
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
