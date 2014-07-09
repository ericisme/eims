package cn.qtone.eims.kjpz.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.simplemvc.support.SqlExpression;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.eims.kjpz.domain.Kmgl;
import cn.qtone.eims.kjpz.service.KmglService;

public class KmglController extends SimpleManageController<Kmgl, KmglService>{

	private KmglService kmglService;

	public KmglService getKmglService() {
		return kmglService;
	}

	public void setKmglService(KmglService kmglService) {
		this.kmglService = kmglService;
	}

	
	/**
	 * 通用的列表查询方法.
	 * 多条件查询：
	 * 例 请求参数：
	 * msgTitle__like__string=测试   指msgTitle字段like “%测试%” 的限制；
	 * item.itemId__eq__int=5       指栏目ID即item.itemId=5的限制
	 * orders=mdfTime__desc          指按照mdfTime字段降序排序
	 * @see SqlExpression
	 */
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		int curPage = this.getCurrentPage(request); // 当前查询页数
		Criteria criteria = getDomainService().createCriteria(domainClass);
		setSqlExpression(request, criteria);
		Page page = getDomainService().pagedQuery(criteria, curPage, 1000);
		//TODO 完善page对象
		page.setPaginate(this.getAjaxPage(request, curPage, page, StringUtil.unCapitalize(domainClass.getSimpleName()) + "Jump"));
		map.put("page", page);
		return new ModelAndView(getListPage(), map);
	}
	
}
