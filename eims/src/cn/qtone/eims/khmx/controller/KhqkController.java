package cn.qtone.eims.khmx.controller;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.mvc.view.spring.TextView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.DateUtil;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.eims.khmx.domain.Khqk;
import cn.qtone.eims.khmx.service.KhqkService;
import cn.qtone.eims.util.EimsUtil;

public class KhqkController extends SimpleManageController<Khqk, KhqkService>{

	private KhqkService service;

	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		int curPage = this.getCurrentPage(request); // 当前查询页数
		Criteria criteria = getDomainService().createCriteria(domainClass);
		setSqlExpression(request, criteria);		
		Page page = getDomainService().pagedQuery(criteria, curPage, Page.DEFAULT_PAGE_SIZE);
		//TODO 完善page对象
		page.setPaginate(this.getAjaxPage(request, curPage, page, StringUtil.unCapitalize(domainClass.getSimpleName()) + "Jump"));
		map.put("page", page);		
		
		boolean lastPage = EimsUtil.ifLastPage(curPage, page);
		//是否为最后一页
		map.put("ifLastPage",lastPage);	
		if(lastPage){
			//统计总数行
			Criteria criteria2 = getDomainService().createCriteria(domainClass);
			setSqlExpression(request, criteria2);		
			map.put("sum_bgje", (BigDecimal)criteria2.setProjection(Projections.sum("bgje")).uniqueResult());
			map.put("sum_ysje", (BigDecimal)criteria2.setProjection(Projections.sum("ysje")).uniqueResult());
			map.put("sum_wsje", (BigDecimal)criteria2.setProjection(Projections.sum("wsje")).uniqueResult());		
			map.put("sum_hj", (BigDecimal)criteria2.setProjection(Projections.sum("hj")).uniqueResult());		
		}
		
		return new ModelAndView(getListPage(), map);
	}
	
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Khqk entity = (Khqk)getCommandObject(request, getDomainClass());
		entity.setBgrq(DateUtil.parseSimpleDate(request.getParameter("_bgrq"))); //转成为日期格式
		
		if(StringUtils.isBlank(request.getParameter("id"))){
			entity.setLrsj(new Date());
			
			getDomainService().save(entity);
		}else{
			Khqk impl = service.get(getDomainId(request));
			entity.setLrsj(impl.getLrsj());
			service.clear();
			
			getDomainService().saveOrUpdate(entity);
		}
		AjaxView view = new AjaxView(true, "操作成功！");
		view.setProperty("refresh", true);
		return new ModelAndView(view);
	}
	
	/**
	 * 检查报关单号是否唯一(如果不存在则返回true)
	 */
	public ModelAndView checkBgdh(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bgdh = request.getParameter("bgdh");
		Integer id = StringUtil.parseInt(request.getParameter("id"), 0);
		String flag = "";
		
		Khqk entity = service.findUniqueBy("bgdh", bgdh);
		if(entity != null){
			if(!entity.getId().equals(id))
				flag = "false";
			else
				flag = "true";
		}else{
			flag = "true";
		}
		
		return new ModelAndView(new TextView(flag));
	}
	
	/**
	 * 导出EXCEL
	 */
	public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) throws Exception {
		OutputStream os = response.getOutputStream();// 取得输出流 		
		String filename = new String("应收客户帐款表".getBytes("GB2312"), "ISO_8859_1");
		response.setHeader("Content-Disposition","attachment;filename=" + filename + ".xls");
		response.setContentType("application/msexcel");			
		WritableWorkbook wwb = Workbook.createWorkbook(os);		
		
		WritableSheet wsheet = wwb.createSheet("应收客户帐款表", 0); // sheet名称
		wsheet.getSettings().setDefaultColumnWidth(10);	
		
		String[] titles = {"单号","客户名称","报关单号","报关日期","报关金额","代理费","报关费","港建费","国检","商检费","续页费","连柜费","拖车费","扫描费",
				"查柜费","熏蒸费","加签","信用证费","产地证费","空白单证费","快递费","驳船费","封条费","仓单费","过磅费","换证凭条费","其他","收款日期",
				"已收金额","未收金额"};
		wsheet.addCell(new Label(0,0,"客户欠款明细表", setCellFormat())); //第一行
		wsheet.mergeCells(0, 0, titles.length, 0);
		wsheet.addCell(new Label(0,1,DateUtil.formatDate(new Date(), "yyyy-MM-dd"), setCellFormat())); //第二行
		wsheet.mergeCells(0, 1, titles.length, 1);
		for(int i=0;i<titles.length;i++){
			wsheet.addCell(new Label(i,2,titles[i], setCellFormat()));
		}
		
		Criteria criteria = getDomainService().createCriteria(domainClass);
		setSqlExpression(request, criteria);		
		List<Khqk> list = criteria.list();
		int i = 3;
		for(Khqk khqk : list){
			wsheet.addCell(new Label(0,i,khqk.getDh(), setCellFormat()));
			wsheet.addCell(new Label(2,i,khqk.getKhmc(), setCellFormat()));
			wsheet.addCell(new Label(3,i,khqk.getBgdh(), setCellFormat()));
			wsheet.addCell(new Label(4,i,DateUtil.formatDate(khqk.getBgrq(), "yyyy-MM-dd"), setCellFormat()));			
			wsheet.addCell(new Label(5,i,StringUtil.formatBigDecimal(khqk.getBgje()), setCellFormat()));
			wsheet.addCell(new Label(6,i,StringUtil.formatBigDecimal(khqk.getBgf()), setCellFormat()));
			wsheet.addCell(new Label(7,i,StringUtil.formatBigDecimal(khqk.getGjf()), setCellFormat()));
			wsheet.addCell(new Label(8,i,StringUtil.formatBigDecimal(khqk.getGj()), setCellFormat()));
			wsheet.addCell(new Label(9,i,StringUtil.formatBigDecimal(khqk.getSjf()), setCellFormat()));
			wsheet.addCell(new Label(10,i,StringUtil.formatBigDecimal(khqk.getXyf()), setCellFormat()));
			wsheet.addCell(new Label(11,i,StringUtil.formatBigDecimal(khqk.getLgf()), setCellFormat()));
			wsheet.addCell(new Label(11,i,StringUtil.formatBigDecimal(khqk.getTcf()), setCellFormat()));
			wsheet.addCell(new Label(13,i,StringUtil.formatBigDecimal(khqk.getSmf()), setCellFormat()));
			wsheet.addCell(new Label(14,i,StringUtil.formatBigDecimal(khqk.getCgf()), setCellFormat()));
			wsheet.addCell(new Label(15,i,StringUtil.formatBigDecimal(khqk.getXzf()), setCellFormat()));
			wsheet.addCell(new Label(16,i,StringUtil.formatBigDecimal(khqk.getJq()), setCellFormat()));
			wsheet.addCell(new Label(17,i,StringUtil.formatBigDecimal(khqk.getXyzf()), setCellFormat()));
			wsheet.addCell(new Label(18,i,StringUtil.formatBigDecimal(khqk.getCdzf()), setCellFormat()));
			wsheet.addCell(new Label(19,i,StringUtil.formatBigDecimal(khqk.getKbdzf()), setCellFormat()));
			wsheet.addCell(new Label(20,i,StringUtil.formatBigDecimal(khqk.getKdf()), setCellFormat()));
			wsheet.addCell(new Label(21,i,StringUtil.formatBigDecimal(khqk.getBcf()), setCellFormat()));
			wsheet.addCell(new Label(22,i,StringUtil.formatBigDecimal(khqk.getFtf()), setCellFormat()));
			wsheet.addCell(new Label(23,i,StringUtil.formatBigDecimal(khqk.getCdf()), setCellFormat()));
			wsheet.addCell(new Label(24,i,StringUtil.formatBigDecimal(khqk.getGpf()), setCellFormat()));
			wsheet.addCell(new Label(25,i,StringUtil.formatBigDecimal(khqk.getHzptf()), setCellFormat()));		
			wsheet.addCell(new Label(26,i,StringUtil.formatBigDecimal(khqk.getQt()), setCellFormat()));			
			wsheet.addCell(new Label(27,i,khqk.getSkrq(), setCellFormat()));
			wsheet.addCell(new Label(28,i,StringUtil.formatBigDecimal(khqk.getYsje()), setCellFormat()));
			wsheet.addCell(new Label(29,i,StringUtil.formatBigDecimal(khqk.getWsje()), setCellFormat()));
			
			i++;
		}
		
		//统计各数总和
		BigDecimal sum_bgje = (BigDecimal)criteria.setProjection(Projections.sum("bgje")).uniqueResult();
		BigDecimal sum_bgf = (BigDecimal)criteria.setProjection(Projections.sum("bgf")).uniqueResult();
		BigDecimal sum_gjf = (BigDecimal)criteria.setProjection(Projections.sum("gjf")).uniqueResult();
		BigDecimal sum_gj = (BigDecimal)criteria.setProjection(Projections.sum("gj")).uniqueResult();
		BigDecimal sum_sjf = (BigDecimal)criteria.setProjection(Projections.sum("sjf")).uniqueResult();
		BigDecimal sum_xyf = (BigDecimal)criteria.setProjection(Projections.sum("xyf")).uniqueResult();
		BigDecimal sum_lgf = (BigDecimal)criteria.setProjection(Projections.sum("lgf")).uniqueResult();
		BigDecimal sum_tcf = (BigDecimal)criteria.setProjection(Projections.sum("tcf")).uniqueResult();
		BigDecimal sum_smf = (BigDecimal)criteria.setProjection(Projections.sum("smf")).uniqueResult();
		BigDecimal sum_cgf = (BigDecimal)criteria.setProjection(Projections.sum("cgf")).uniqueResult();
		BigDecimal sum_xzf = (BigDecimal)criteria.setProjection(Projections.sum("xzf")).uniqueResult();
		BigDecimal sum_jq = (BigDecimal)criteria.setProjection(Projections.sum("jq")).uniqueResult();
		BigDecimal sum_xyzf = (BigDecimal)criteria.setProjection(Projections.sum("xyzf")).uniqueResult();
		BigDecimal sum_cdzf = (BigDecimal)criteria.setProjection(Projections.sum("cdzf")).uniqueResult();
		BigDecimal sum_kbdzf = (BigDecimal)criteria.setProjection(Projections.sum("kbdzf")).uniqueResult();
		BigDecimal sum_kdf = (BigDecimal)criteria.setProjection(Projections.sum("kdf")).uniqueResult();
		BigDecimal sum_bcf = (BigDecimal)criteria.setProjection(Projections.sum("bcf")).uniqueResult();
		BigDecimal sum_ftf = (BigDecimal)criteria.setProjection(Projections.sum("ftf")).uniqueResult();
		BigDecimal sum_cdf = (BigDecimal)criteria.setProjection(Projections.sum("cdf")).uniqueResult();
		BigDecimal sum_gpf = (BigDecimal)criteria.setProjection(Projections.sum("gpf")).uniqueResult();
		BigDecimal sum_hzptf = (BigDecimal)criteria.setProjection(Projections.sum("hzptf")).uniqueResult();
		BigDecimal sum_qt = (BigDecimal)criteria.setProjection(Projections.sum("qt")).uniqueResult();
		BigDecimal sum_ysje = (BigDecimal)criteria.setProjection(Projections.sum("ysje")).uniqueResult();
		BigDecimal sum_wsje = (BigDecimal)criteria.setProjection(Projections.sum("wsje")).uniqueResult();
		wsheet.addCell(new Label(0,i,"合计", setCellFormat()));
		wsheet.mergeCells(0, i, 4, i);
		wsheet.addCell(new Label(5,i,StringUtil.formatBigDecimal(sum_bgje), setCellFormat()));
		wsheet.addCell(new Label(6,i,StringUtil.formatBigDecimal(sum_bgf), setCellFormat()));
		wsheet.addCell(new Label(7,i,StringUtil.formatBigDecimal(sum_gjf), setCellFormat()));
		wsheet.addCell(new Label(8,i,StringUtil.formatBigDecimal(sum_gj), setCellFormat()));
		wsheet.addCell(new Label(9,i,StringUtil.formatBigDecimal(sum_sjf), setCellFormat()));
		wsheet.addCell(new Label(10,i,StringUtil.formatBigDecimal(sum_xyf), setCellFormat()));
		wsheet.addCell(new Label(11,i,StringUtil.formatBigDecimal(sum_lgf), setCellFormat()));
		wsheet.addCell(new Label(12,i,StringUtil.formatBigDecimal(sum_tcf), setCellFormat()));
		wsheet.addCell(new Label(13,i,StringUtil.formatBigDecimal(sum_smf), setCellFormat()));
		wsheet.addCell(new Label(14,i,StringUtil.formatBigDecimal(sum_cgf), setCellFormat()));
		wsheet.addCell(new Label(15,i,StringUtil.formatBigDecimal(sum_xzf), setCellFormat()));
		wsheet.addCell(new Label(16,i,StringUtil.formatBigDecimal(sum_jq), setCellFormat()));
		wsheet.addCell(new Label(17,i,StringUtil.formatBigDecimal(sum_xyzf), setCellFormat()));
		wsheet.addCell(new Label(18,i,StringUtil.formatBigDecimal(sum_cdzf), setCellFormat()));
		wsheet.addCell(new Label(19,i,StringUtil.formatBigDecimal(sum_kbdzf), setCellFormat()));
		wsheet.addCell(new Label(20,i,StringUtil.formatBigDecimal(sum_kdf), setCellFormat()));
		wsheet.addCell(new Label(21,i,StringUtil.formatBigDecimal(sum_bcf), setCellFormat()));
		wsheet.addCell(new Label(22,i,StringUtil.formatBigDecimal(sum_ftf), setCellFormat()));
		wsheet.addCell(new Label(23,i,StringUtil.formatBigDecimal(sum_cdf), setCellFormat()));
		wsheet.addCell(new Label(24,i,StringUtil.formatBigDecimal(sum_gpf), setCellFormat()));
		wsheet.addCell(new Label(25,i,StringUtil.formatBigDecimal(sum_hzptf), setCellFormat()));
		wsheet.addCell(new Label(26,i,StringUtil.formatBigDecimal(sum_qt), setCellFormat()));
		wsheet.addCell(new Label(27,i,"", setCellFormat()));
		wsheet.addCell(new Label(28,i,StringUtil.formatBigDecimal(sum_ysje), setCellFormat()));
		wsheet.addCell(new Label(29,i,StringUtil.formatBigDecimal(sum_wsje), setCellFormat()));
		
		wwb.write();
		wwb.close();		
		os.close(); 
		return null;
	}
	
	private WritableCellFormat setCellFormat() throws WriteException{
		WritableCellFormat cell = new WritableCellFormat(new WritableFont(WritableFont.createFont("宋体"),10,WritableFont.NO_BOLD));
		cell.setAlignment(jxl.format.Alignment.CENTRE);
		cell.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		
		return cell;
	}
	public void setService(KhqkService service) {
		this.service = service;
	}
	
}
