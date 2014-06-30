package cn.qtone.crda.subscribe.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;
import cn.qtone.ContantsUtil;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.crda.CrdaContants;
import cn.qtone.crda.subscribe.domain.Subscribe;
import cn.qtone.crda.subscribe.service.SubscribeService;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;

/**
 * 订阅管理 - 订阅管理
 * 
 * @author 贺少辉
 * @version 1.0
 */
public class SubscribeController extends SimpleManageController<Subscribe, SubscribeService> {

	@Override
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		map
				.put("chargebackHtmlSelect", ContantsUtil.getSystemContantsSelectHtml(CrdaContants.SUBSCRIBECHARGEBACK,
						"#"));
		return new ModelAndView(getIndexPage(), map);
	}

	@Override
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		int curPage = this.getCurrentPage(request); // 当前查询页数
		Criteria criteria = getDomainService().createCriteria(domainClass);
		setSqlExpression(request, criteria);
		setQuerySubscribeTimeTime(criteria, request);
		Page page = getDomainService().pagedQuery(criteria, curPage, Page.DEFAULT_PAGE_SIZE);
		page.setPaginate(this.getAjaxPage(request, curPage, page, StringUtil.unCapitalize(domainClass.getSimpleName())
				+ "Jump"));
		map.put("page", page);
		return new ModelAndView(getListPage(), map);
	}


	private static void setQuerySubscribeTimeTime(Criteria criteria, HttpServletRequest request) {
		String qDateTimeSelectStart = ServletUtil.removeSpace(request, "QDateTimeSelectStart");
		String qDateTimeSelectEnd = ServletUtil.removeSpace(request, "QDateTimeSelectEnd");
		if (StringUtils.isNotBlank(qDateTimeSelectStart) && StringUtils.isNotBlank(qDateTimeSelectEnd)) {
			criteria.add(Restrictions.between("subscribeTime", qDateTimeSelectStart + " 00:00:00", qDateTimeSelectEnd
					+ " 23:59:59"));
		} else if (StringUtils.isNotBlank(qDateTimeSelectStart)) {
			criteria.add(Restrictions.ge("subscribeTime", qDateTimeSelectStart + " 00:00:00"));
		} else if (StringUtils.isNotBlank(qDateTimeSelectEnd)) {
			criteria.add(Restrictions.le("subscribeTime", qDateTimeSelectEnd + " 23:59:59"));
		}
	}
 
	public ModelAndView downloadChargebackIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		map.put("yearOptions", CrdaContants.getYearOptions());
		map.put("monthOptions", CrdaContants.getMonthOptions());
		return new ModelAndView("/crda/subscribe/downloadChargeback_index", map);
	}

	public ModelAndView downloadChargebackList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		int curPage = this.getCurrentPage(request); // 当前查询页数
		Criteria criteria = getDomainService().createCriteria(domainClass);
		setSqlExpression(request, criteria);
		Page page = getDomainService().pagedQuery(criteria, curPage, Page.DEFAULT_PAGE_SIZE);
		page.setPaginate(this.getAjaxPage(request, curPage, page, StringUtil.unCapitalize(domainClass.getSimpleName())
				+ "Jump"));
		map.put("page", page);
		return new ModelAndView("/crda/subscribe/downloadChargeback_list", map);
	}

	SubscribeService subscribeService;

	public void setSubscribeService(SubscribeService subscribeService) {
		this.subscribeService = subscribeService;
	}
}