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
import cn.qtone.eims.khmx.domain.Tczc;
import cn.qtone.eims.khmx.service.TczcService;
import cn.qtone.eims.util.EimsUtil;

public class TczcController extends SimpleManageController<Tczc, TczcService>{

	private TczcService service;
	
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
			//统计各数总和
			Criteria criteria2 = getDomainService().createCriteria(domainClass);
			setSqlExpression(request, criteria2);
			map.put("sum_bgje", (BigDecimal)criteria2.setProjection(Projections.sum("bgje")).uniqueResult());
			map.put("sum_tcf", (BigDecimal)criteria2.setProjection(Projections.sum("tcf")).uniqueResult());
			map.put("sum_ydtgf", (BigDecimal)criteria2.setProjection(Projections.sum("ydtgf")).uniqueResult());
			map.put("sum_gpf", (BigDecimal)criteria2.setProjection(Projections.sum("gpf")).uniqueResult());	
			map.put("sum_ldzgf", (BigDecimal)criteria2.setProjection(Projections.sum("ldzgf")).uniqueResult());
			map.put("sum_ddf", (BigDecimal)criteria2.setProjection(Projections.sum("ddf")).uniqueResult());
			map.put("sum_zgf", (BigDecimal)criteria2.setProjection(Projections.sum("zgf")).uniqueResult());
			map.put("sum_fyjehj", (BigDecimal)criteria2.setProjection(Projections.sum("fyjehj")).uniqueResult());
			map.put("sum_zfje", (BigDecimal)criteria2.setProjection(Projections.sum("zfje")).uniqueResult());
			map.put("sum_wfje", (BigDecimal)criteria2.setProjection(Projections.sum("wfje")).uniqueResult());
		}
		
		return new ModelAndView(getListPage(), map);
	}
	
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Tczc entity = (Tczc)getCommandObject(request, getDomainClass());
		//BigDecimal fyjehj = entity.getTcf() + entity.getYdtgf() + entity.getGpf() + entity.getLdzgf() + entity.getDdf() + entity.getZgf();
		BigDecimal fyjehj = entity.getTcf().add(entity.getYdtgf()).add(entity.getGpf()).add(entity.getLdzgf()).add(entity.getDdf()).add(entity.getZgf());
		entity.setFyjehj(fyjehj);
		entity.setBgrq(DateUtil.parseSimpleDate(request.getParameter("_bgrq"))); //转成为日期格式
		
		if(StringUtils.isBlank(request.getParameter("id"))){
			entity.setLrsj(new Date());
			
			getDomainService().save(entity);
		}else{
			Tczc impl = service.get(getDomainId(request));
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
		
		Tczc entity = service.findUniqueBy("bgdh", bgdh);
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
		String filename = new String("拖车明细表".getBytes("GB2312"), "ISO_8859_1");
		response.setHeader("Content-Disposition","attachment;filename=" + filename + ".xls");
		response.setContentType("application/msexcel");			
		WritableWorkbook wwb = Workbook.createWorkbook(os);		
		
		WritableSheet wsheet = wwb.createSheet("拖车明细表", 0); // sheet名称
		wsheet.getSettings().setDefaultColumnWidth(10);	
		
		String[] titles = {"单号","客户名称","报关单号","报关日期","报关金额","柜号","起运港","拖车费","异地提柜费","过磅费","两地装柜费","打单费",
				"重柜费","费用金额合计","支付日期","支付金额","未付金额"};
		wsheet.addCell(new Label(0,0,"拖车明细表", setCellFormat())); //第一行
		wsheet.mergeCells(0, 0, titles.length, 0);
		wsheet.addCell(new Label(0,1,DateUtil.formatDate(new Date(), "yyyy-MM-dd"), setCellFormat())); //第二行
		wsheet.mergeCells(0, 1, titles.length, 1);
		for(int i=0;i<titles.length;i++){
			wsheet.addCell(new Label(i,2,titles[i], setCellFormat()));
		}
		
		Criteria criteria = getDomainService().createCriteria(domainClass);
		setSqlExpression(request, criteria);
		List<Tczc> list = criteria.list();
		int i = 3;
		for(Tczc tczc : list){
			wsheet.addCell(new Label(0,i,tczc.getDh(), setCellFormat()));
			wsheet.addCell(new Label(1,i,tczc.getKhmc(), setCellFormat()));
			wsheet.addCell(new Label(2,i,tczc.getBgdh(), setCellFormat()));
			wsheet.addCell(new Label(3,i,DateUtil.formatDate(tczc.getBgrq(), "yyyy-MM-dd"), setCellFormat()));
			wsheet.addCell(new Label(4,i,StringUtil.formatBigDecimal(tczc.getBgje()), setCellFormat()));
			wsheet.addCell(new Label(5,i,tczc.getGh(), setCellFormat()));
			wsheet.addCell(new Label(6,i,tczc.getQyg(), setCellFormat()));
			wsheet.addCell(new Label(7,i,StringUtil.formatBigDecimal(tczc.getTcf()), setCellFormat()));
			wsheet.addCell(new Label(8,i,StringUtil.formatBigDecimal(tczc.getYdtgf()), setCellFormat()));
			wsheet.addCell(new Label(9,i,StringUtil.formatBigDecimal(tczc.getGpf()), setCellFormat()));
			wsheet.addCell(new Label(10,i,StringUtil.formatBigDecimal(tczc.getLdzgf()), setCellFormat()));
			wsheet.addCell(new Label(11,i,StringUtil.formatBigDecimal(tczc.getDdf()), setCellFormat()));
			wsheet.addCell(new Label(12,i,StringUtil.formatBigDecimal(tczc.getZgf()), setCellFormat()));
			wsheet.addCell(new Label(13,i,StringUtil.formatBigDecimal(tczc.getFyjehj()), setCellFormat()));
			wsheet.addCell(new Label(14,i,tczc.getZfrq(), setCellFormat()));
			wsheet.addCell(new Label(15,i,StringUtil.formatBigDecimal(tczc.getZfje()), setCellFormat()));
			wsheet.addCell(new Label(16,i,StringUtil.formatBigDecimal(tczc.getWfje()), setCellFormat()));			
			i++;
		}
		
		//统计各数总和
		BigDecimal sum_bgje = (BigDecimal)criteria.setProjection(Projections.sum("bgje")).uniqueResult();
		BigDecimal sum_tcf = (BigDecimal)criteria.setProjection(Projections.sum("tcf")).uniqueResult();
		BigDecimal sum_ydtgf = (BigDecimal)criteria.setProjection(Projections.sum("ydtgf")).uniqueResult();
		BigDecimal sum_gpf = (BigDecimal)criteria.setProjection(Projections.sum("gpf")).uniqueResult();
		BigDecimal sum_ldzgf = (BigDecimal)criteria.setProjection(Projections.sum("ldzgf")).uniqueResult();
		BigDecimal sum_ddf = (BigDecimal)criteria.setProjection(Projections.sum("ddf")).uniqueResult();
		BigDecimal sum_zgf = (BigDecimal)criteria.setProjection(Projections.sum("zgf")).uniqueResult();
		BigDecimal sum_fyjehj = (BigDecimal)criteria.setProjection(Projections.sum("fyjehj")).uniqueResult();
		BigDecimal sum_zfje = (BigDecimal)criteria.setProjection(Projections.sum("zfje")).uniqueResult();
		BigDecimal sum_wfje = (BigDecimal)criteria.setProjection(Projections.sum("wfje")).uniqueResult();
		wsheet.addCell(new Label(0,i,"合计", setCellFormat()));
		wsheet.mergeCells(0, i, 3, i);
		wsheet.addCell(new Label(4,i,StringUtil.formatBigDecimal(sum_bgje), setCellFormat()));
		wsheet.addCell(new Label(5,i,"", setCellFormat()));
		wsheet.addCell(new Label(6,i,"", setCellFormat()));
		wsheet.addCell(new Label(7,i,StringUtil.formatBigDecimal(sum_tcf), setCellFormat()));
		wsheet.addCell(new Label(8,i,StringUtil.formatBigDecimal(sum_ydtgf), setCellFormat()));
		wsheet.addCell(new Label(9,i,StringUtil.formatBigDecimal(sum_gpf), setCellFormat()));
		wsheet.addCell(new Label(10,i,StringUtil.formatBigDecimal(sum_ldzgf), setCellFormat()));
		wsheet.addCell(new Label(11,i,StringUtil.formatBigDecimal(sum_ddf), setCellFormat()));
		wsheet.addCell(new Label(12,i,StringUtil.formatBigDecimal(sum_zgf), setCellFormat()));
		wsheet.addCell(new Label(13,i,StringUtil.formatBigDecimal(sum_fyjehj), setCellFormat()));
		wsheet.addCell(new Label(14,i,"", setCellFormat()));
		wsheet.addCell(new Label(15,i,StringUtil.formatBigDecimal(sum_zfje), setCellFormat()));
		wsheet.addCell(new Label(16,i,StringUtil.formatBigDecimal(sum_wfje), setCellFormat()));
		
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
	
	public void setService(TczcService service) {
		this.service = service;
	}
	
}
