package cn.qtone.eims.fymx.gdzc.controller;


import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.mvc.view.spring.TextView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.eims.fymx.gdzc.domain.Gdzc;
import cn.qtone.eims.fymx.gdzc.service.GdzcService;
import cn.qtone.eims.util.EimsUtil;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;

/**
 * 固定资产
 * @author Administrator
 *
 */
public class GdzcController extends SimpleManageController<Gdzc, GdzcService> {

	private GdzcService service;

	/**
	 * 计提折旧 jtzj 单个或多个对象.
	 */
	public ModelAndView jtzj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] idStrs = request.getParameterValues(idName);
		if (log.isInfoEnabled()) {
			log.info("要删除的对象ID为：" + StringUtil.join(idStrs, ","));
		}
		//判断是否可以操作
		for(String idStr : idStrs){
			if(idStr.length() > 0){
				Gdzc gdzc = this.getDomainService().get(Integer.valueOf(idStr));
				if(gdzc.getZjyf()<=gdzc.getYzjyf()){
					return new ModelAndView(new TextView("操作不成功，选择的资产 "+gdzc.getSbxmmc()+" 已经折旧完了！"));
				}
			}
		}
		//计提折旧
		for(String idStr : idStrs){
			if(idStr.length() > 0){
				Gdzc o = this.getDomainService().get(Integer.valueOf(idStr));
				o.setYzjyf(o.getYzjyf()+1);//已折旧月份加1				
				//o.setYzjje(o.getYzjyf()*o.getMyzjje());//已折旧金额=已折旧月份*每月折旧金额				
				//o.setYe(o.getZje()-o.getYzjje());//余额（净值）=总金额-已折旧金额
				o.setYzjje(o.getMyzjje().multiply(new BigDecimal(o.getYzjyf())));//已折旧金额=已折旧月份*每月折旧金额				
				o.setYe(o.getZje().subtract(o.getYzjje()));//余额（净值）=总金额-已折旧金额
				getDomainService().saveOrUpdate(o);
			}
		}
		return new ModelAndView(new TextView("success"));
	}
	
	/**
	 * 通用的新增/修改对象的保存.
	 */
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Gdzc o = (Gdzc)getCommandObject(request, getDomainClass());
		
		
		//已折旧金额=已折旧月份*每月折旧金额
		//o.setYzjje(o.getYzjyf()*o.getMyzjje());
		o.setYzjje(o.getMyzjje().multiply(new BigDecimal(o.getYzjyf())));
		//余额（净值）=总金额-已折旧金额
		//o.setYe(o.getZje()-o.getYzjje());
		o.setYe(o.getZje().subtract(o.getYzjje()));
		
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
		//折旧状态
		String zjzt = ServletUtil.removeSpace(request, "zjzt");
		if(!("0".equals(zjzt))){
			if("1".equals(zjzt)){
				criteria.add(Restrictions.gtProperty("zjyf", "yzjyf"));
			}
			if("2".equals(zjzt)){
				criteria.add(Restrictions.leProperty("zjyf", "yzjyf"));
			}
		}
		
		
		Page page = getDomainService().pagedQuery(criteria, curPage, Page.DEFAULT_PAGE_SIZE);
		//TODO 完善page对象
		page.setPaginate(this.getAjaxPage(request, curPage, page, StringUtil.unCapitalize(domainClass.getSimpleName()) + "Jump"));
		map.put("page", page);
		
		
		//统计总数行，3项金额。
		Criteria criteria2 = getDomainService().createCriteria(domainClass);
		setSqlExpression(request, criteria2);	
		if(!("0".equals(zjzt))){
			if("1".equals(zjzt)){
				criteria2.add(Restrictions.gtProperty("zjyf", "yzjyf"));
			}
			if("2".equals(zjzt)){
				criteria2.add(Restrictions.leProperty("zjyf", "yzjyf"));
			}
		}
		map.put("sum_zje",    (BigDecimal)criteria2.setProjection(Projections.sum("zje")).uniqueResult());
		map.put("sum_myzjje", (BigDecimal)criteria2.setProjection(Projections.sum("myzjje")).uniqueResult());
		map.put("sum_yzjje",  (BigDecimal)criteria2.setProjection(Projections.sum("yzjje")).uniqueResult());
		map.put("sum_ye",     (BigDecimal)criteria2.setProjection(Projections.sum("ye")) .uniqueResult());
		//是否为最后一页
		map.put("ifLastPage",EimsUtil.ifLastPage(curPage, page));
		
		
		return new ModelAndView(getListPage(), map);
	}
	
	

	public GdzcService getService() {
		return service;
	}

	public void setService(GdzcService service) {
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
