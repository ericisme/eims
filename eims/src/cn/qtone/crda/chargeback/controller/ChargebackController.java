package cn.qtone.crda.chargeback.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.springframework.web.servlet.ModelAndView;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.mvc.view.spring.TextView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.DateUtil;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.common.utils.upload.DownloadInter;
import cn.qtone.common.utils.upload.UploadFactory;
import cn.qtone.crda.CrdaContants;
import cn.qtone.crda.chargeback.dao.UpdateChargeBackFeeStatusContionVO;
import cn.qtone.crda.chargeback.domain.Chargeback;
import cn.qtone.crda.chargeback.qvo.ChargebackQVO;
import cn.qtone.crda.chargeback.service.ChargebackService;
import cn.qtone.crda.subscribe.domain.Subscribe;
import cn.qtone.crda.subscribe.service.SubscribeService;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;
import cn.qtone.qtoneframework.web.view.poi.ExcelUtils;

/**
 * 扣费记录 - 扣费记录
 *
 * @author 贺少辉
 * @version 1.0
 */
public class ChargebackController extends SimpleManageController<Chargeback, ChargebackService> {

	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		map.put("yearOptionHtmlSelect", CrdaContants.getYearOptions());
		map.put("monthOptionHtmlSelect", CrdaContants.getMonthOptions());
		return new ModelAndView(getIndexPage(), map);
	}

	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		int curPage = this.getCurrentPage(request); // 当前查询页数
		Criteria criteria = getDomainService().createCriteria(domainClass);
		setSqlExpression(request, criteria);
		Page page = getDomainService().pagedQuery(criteria, curPage, Page.DEFAULT_PAGE_SIZE);
		page.setPaginate(this.getAjaxPage(request, curPage, page, StringUtil.unCapitalize(domainClass.getSimpleName()) + "Jump"));
		map.put("page", page);
		return new ModelAndView(getListPage(), map);
	}

	/**
	 * 生成扣费记录
	 */
	public ModelAndView creatChargeback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ChargebackQVO qvo =  getChargebackQVOByRequest(request);
		//1:不能重复生成(如果当月已经生成了，则提示已经生成)
		if(this.chargebackService.queryChargebackByQVO(qvo).size()>0){
			AjaxView view = new AjaxView(true, "操作失败,本月扣费记录已经生成了");
			view.setProperty("refresh", true);
			return new ModelAndView(view);
		}
		createChargebck(qvo.getYear(),qvo.getMonth(),UserUtil.getUserBean(request).getRealName());
		AjaxView view = new AjaxView(true, "操作成功,本月扣费记录生成成功");
		view.setProperty("refresh", true);
		return new ModelAndView(view);
	}
	
	
	@SuppressWarnings("unchecked")
	public ModelAndView downloadChargeback(HttpServletRequest request, HttpServletResponse response) throws Exception {

		ChargebackQVO qvo =  getChargebackQVOByRequest(request);
		List<Chargeback> list = this.chargebackService.queryChargebackByQVO(qvo);
		if (list.size() > 0) {

			HSSFWorkbook workbook = this.chargebackService.getChargebackExport(list, request.getSession().getServletContext()
					.getRealPath("/")
					+ "/template/chargeback_export_template.xls",qvo.getYear()+"年"+qvo.getMonth());
			try {
				
				ExcelUtils.download(response, workbook, qvo.getYear()+"年"+qvo.getMonth()+"月扣费记录.xls");
			} 
			catch (Exception e) {
				
				e.printStackTrace();
			}
		} else {
			String msg = "<script>alert('导出失败,没有符合条件的记录！');window.history.back();</script>";
			ServletOutputStream out = response.getOutputStream();
			out.print(msg);
			out.close();
		}
		return null;
	}

	/**
	 * 导入移动扣费情况初始化
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView importChargebackInit(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> map = this.getMapWithUser(request);
		map.put("yearOptionHtmlSelect", CrdaContants.getYearOptions());
		map.put("monthOptionHtmlSelect", CrdaContants.getMonthOptions());
		return new ModelAndView("/crda/chargeback/chargeback_import", map);
	}

	/**
	 * 模板下载
	 */
	public ModelAndView download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		DownloadInter download = UploadFactory.getDownloadInstance(response);
		try {
			
			download.download(request.getSession().getServletContext().getRealPath("/")
					+ "/template/chargeback_import_template.xls", "UTF-8");
		} 
		catch (Exception ex) {
			
			ex.printStackTrace();
			return new ModelAndView(new TextView("未知原因导致文件下载失败！"));
		}
		return null;
	}

	/**
	 * 导入扣费情况的提交操作.
	 * 
	 */
	public ModelAndView importChargeBackSubmit(HttpServletRequest request, HttpServletResponse response) {
		UpdateChargeBackFeeStatusContionVO vo = new UpdateChargeBackFeeStatusContionVO(ServletUtil.removeSpace(request, "year"),ServletUtil.removeSpace(request, "month"),ServletUtil.removeSpace(request, "lsBusiness"),DateUtil.getDateTime());
		String chargeBackSucf = "";//扣费成功的
		String chargeBackfail = "";//扣费失败的
		try {
			
			for (Chargeback chargeBack : this.chargebackService.getImportChargeback(request.getSession().getServletContext()
					.getRealPath("/")
					+ ServletUtil.removeSpace(request, "importattach_path"))) {
				if (StringUtils.isEmpty(chargeBack.getMobile())||StringUtils.isEmpty(chargeBack.getMobile()))
					continue;
				if(chargeBack.getChargeback()==1){
					chargeBackSucf+=chargeBack.getMobile()+",";
				}
				if(chargeBack.getChargeback()==-1){
					chargeBackfail+=chargeBack.getMobile()+",";
				}
			}
			//保存扣费信息
			if(StringUtils.isNotBlank(chargeBackSucf)){
				vo.setChargeback(1);
				vo.setMobile(chargeBackSucf.substring(0,chargeBackSucf.length()-1));
				this.chargebackService.updateChargeBackFeeStatus(vo);
			}
			if(StringUtils.isNotBlank(chargeBackfail)){
				vo.setChargeback(-1);
				vo.setMobile(chargeBackfail.substring(0,chargeBackfail.length()-1));
				this.chargebackService.updateChargeBackFeeStatus(vo);
			}
			
			 //2 :修改订阅表中的扣费状态
			this.chargebackService.updateSubscribeChargeBack();
			//修改扣费次数 
			this.chargebackService.updateSubscribeChargebackTimes(1);
			this.chargebackService.updateSubscribeChargebackTimes(-1);
			AjaxView view = new AjaxView(true, "导入成功！");
			view.setProperty("refresh", true);
			return new ModelAndView(view);
		} 
		catch (Exception e) {
			
			e.printStackTrace();
			AjaxView view = new AjaxView(true, "导入失败！");
			view.setProperty("refresh", true);
			return new ModelAndView(view);
		}
	}
	
	/**
	 * 创建扣费记录
	 */
	private void createChargebck(String year,String month,String createUserName){
		//获取未取消的所有订阅记录
		for(Subscribe sb:this.subscribeService.querySubscribe()){
			Chargeback cb = new Chargeback();
			cb.setYear(year);
			cb.setMonth(month);
			cb.setChargeback(0);
			cb.setChargeFees(CrdaContants.CHARGEFEES);
			cb.setCreateUserNme(createUserName);
			cb.setMobile(sb.getMobile());
			cb.setStu_unique(sb.getStu_unique());
			cb.setExportStaus(0);
			cb.setLsBusiness(sb.getLsBusiness());
			cb.setSubscribeId(sb.getId());
			this.chargebackService.save(cb);
		}
	}
	
	private ChargebackQVO getChargebackQVOByRequest(HttpServletRequest request){
		ChargebackQVO qvo = new ChargebackQVO();
		qvo.setYear(ServletUtil.removeSpace(request, "year"));
		qvo.setMonth(ServletUtil.removeSpace(request, "month"));
		return qvo;
	}
	
	ChargebackService chargebackService;
	SubscribeService subscribeService;

	public void setChargebackService(ChargebackService chargebackService) {
		this.chargebackService = chargebackService;
	}

	public void setSubscribeService(SubscribeService subscribeService) {
		this.subscribeService = subscribeService;
	}
}