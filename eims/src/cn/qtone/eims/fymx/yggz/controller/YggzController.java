package cn.qtone.eims.fymx.yggz.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.mvc.view.spring.TextView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.eims.fymx.yggz.domain.Yggz;
import cn.qtone.eims.fymx.yggz.service.YggzService;
import cn.qtone.eims.util.EimsUtil;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;

public class YggzController extends SimpleManageController<Yggz, YggzService> {

	private YggzService service;

	/**
	 * 通用的新增/修改对象的保存.
	 */
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Yggz o = (Yggz)getCommandObject(request, getDomainClass());
		//应付工资=基本工资+提成+补贴
		o.setYfgz(o.getJbgz()+o.getTc()+o.getBt());
		//实发工资=应付工资-社保费-扣回工资
		o.setSfgz(o.getYfgz()-o.getSbf()-o.getKhgz());
		
		o.setLrsj(new Date());
		System.out.println("--------------------------");
		System.out.println(new Date());
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

	
		//统计总数行，6项金额。
		Criteria criteria2 = getDomainService().createCriteria(domainClass);
		setSqlExpression(request, criteria2);		
		map.put("sum_jbgz", (Double)criteria2.setProjection(Projections.sum("jbgz")).uniqueResult());
		map.put("sum_tc",   (Double)criteria2.setProjection(Projections.sum("tc")).uniqueResult());
		map.put("sum_bt",   (Double)criteria2.setProjection(Projections.sum("bt")).uniqueResult());
		map.put("sum_yfgz", (Double)criteria2.setProjection(Projections.sum("yfgz")).uniqueResult());
		map.put("sum_sbf",  (Double)criteria2.setProjection(Projections.sum("sbf")).uniqueResult());
		map.put("sum_khgz", (Double)criteria2.setProjection(Projections.sum("khgz")).uniqueResult());
		map.put("sum_sfgz", (Double)criteria2.setProjection(Projections.sum("sfgz")).uniqueResult());		
		//是否为最后一页
		map.put("ifLastPage",EimsUtil.ifLastPage(curPage, page));
		
		return new ModelAndView(getListPage(), map);
	}
	
	
	//为text input提供 distinct选项
	public ModelAndView setDistinctValue(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		
		String spanId = ServletUtil.removeSpace(request, "spanId");
		String inputId = ServletUtil.removeSpace(request, "inputId");
		String inputValue = ServletUtil.removeSpace(request, "inputValue");		
		String mouseX = ServletUtil.removeSpace(request, "mouseX");
		String mouseY = ServletUtil.removeSpace(request, "mouseY");		
		String domainClassName = ServletUtil.removeSpace(request, "domainClassName");
		String propertyName = ServletUtil.removeSpace(request, "propertyName");	
		
		Criteria criteria = getDomainService().createCriteria(Class.forName(domainClassName));
		criteria.add(Restrictions.like(propertyName, "%"+inputValue+"%"));
		criteria.setProjection(Projections.distinct(Projections.property(propertyName)));
		
		String innerHtml= "<div style=\"z-index: 9998; position: absolute; top: "+mouseY+"px; left: "+mouseX+"px;width:150px;height:180px;overflow:auto;\" class=\"body_bg\">";
		innerHtml+="<table ><tr><td><input type=\"button\" onclick=\"document.getElementById('"+spanId+"').style.display='none';\" value=\"关闭\"/></td></tr></table>";
		innerHtml+="<table bgcolor=\"white\" >";		
		for(String ygxm : (List<String> )criteria.list()){
			innerHtml+="<tr><td><a href=\"#\" onclick=\"document.getElementById('"+inputId+"').value='"+ygxm+"';document.getElementById('"+spanId+"').style.display='none';\">"+ygxm+"</a></td></tr>";			
		}					
		innerHtml+="</table>";				
		innerHtml+="</div>";		
		
		return new ModelAndView(new TextView(innerHtml));
	}
	
	
	
	public YggzService getService() {
		return service;
	}
	public void setService(YggzService service) {
		this.service = service;
	}
}
