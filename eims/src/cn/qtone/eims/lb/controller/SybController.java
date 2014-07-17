package cn.qtone.eims.lb.controller;

import java.io.OutputStream;
import java.util.ArrayList;
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
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.DateUtil;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.eims.bbgl.fymxb.dao.FymxbDao;
import cn.qtone.eims.fymx.cwfy.domain.Cwfy;
import cn.qtone.eims.fymx.sds.domain.Sds;
import cn.qtone.eims.fymx.yggz.domain.Yggz;
import cn.qtone.eims.khmx.domain.Fkzf;
import cn.qtone.eims.khmx.domain.Khqk;
import cn.qtone.eims.khmx.domain.Tczc;
import cn.qtone.eims.khmx.domain.Yywsr;
import cn.qtone.eims.lb.domain.Syb;
import cn.qtone.eims.lb.domain.SybReport;
import cn.qtone.eims.lb.service.SybService;
import cn.qtone.eims.util.EimsUtil;

public class SybController extends SimpleManageController<Syb, SybService>{

	private SybService service;
	private String reportPage;
	private FymxbDao fymxbDao;
	
	/**
	 * 报表html
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView report(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		String _ksrq_str = request.getParameter("_ksrq");
		String _jsrq_str = request.getParameter("_jsrq");	
		if(StringUtil.isNullAndBlank(_ksrq_str))
			_ksrq_str = "1900-01-01 00:00:00";
		if(StringUtil.isNullAndBlank(_jsrq_str))
			_jsrq_str = "2099-12-31 23:59:59";		
		
		List<Map<String, Object>> rq_list = fymxbDao.getRqListByKsnyAndJsny(_ksrq_str.substring(0,7), _jsrq_str.substring(0, 7));
		List<SybReport> sybReport_list = new ArrayList<SybReport>();
		SybReport sumSybReport = new SybReport();
		for(Map<String, Object> row : rq_list){
			String ny = (String)row.get("rq");
			SybReport sybReport = getSybReportByRq(ny+"-01 00:00:00", ny+"-"+getLastDayByYearAndMonth(ny)+" 23:59:59");
			sumSybReport.setZyywlr(nullToZero(sumSybReport.getZyywlr())+nullToZero(sybReport.getZyywlr()));
			sumSybReport.setGlfy(nullToZero(sumSybReport.getGlfy())+nullToZero(sybReport.getGlfy()));
			sumSybReport.setCwfy(nullToZero(sumSybReport.getCwfy())+nullToZero(sybReport.getGlfy()));
			sumSybReport.setJyfy(nullToZero(sumSybReport.getJyfy())+nullToZero(sybReport.getJyfy()));
			sumSybReport.setYywsr(nullToZero(sumSybReport.getYywsr())+nullToZero(sybReport.getYywsr()));
			sumSybReport.setYywzc(nullToZero(sumSybReport.getYywzc())+nullToZero(sybReport.getYywzc()));
			sumSybReport.setSds(nullToZero(sumSybReport.getSds())+nullToZero(sybReport.getSds()));
			sumSybReport.setJlr(nullToZero(sumSybReport.getJlr())+nullToZero(sybReport.getJlr()));
			sybReport.setNy(ny);
			sybReport_list.add(sybReport);	
		}
		
		//map.put("ksrq",_ksrq_str.substring(0, 10));
		//map.put("jsrq",_jsrq_str.substring(0, 10));
		//map.put("entity",getSybReportByRq(_ksrq_str, _jsrq_str));
		map.put("entity_list",sybReport_list);
		map.put("sumSybReport",sumSybReport);
		return new ModelAndView(getReportPage(), map);
	}
	
	
	/**
	 * 报表Xls
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView reportXls(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String _ksrq_str = request.getParameter("_ksrq");
		String _jsrq_str = request.getParameter("_jsrq");	
		if(StringUtil.isNullAndBlank(_ksrq_str))
			_ksrq_str = "1900-01-01 00:00:00";
		if(StringUtil.isNullAndBlank(_jsrq_str))
			_jsrq_str = "2099-12-31 23:59:59";		
		
		List<Map<String, Object>> rq_list = fymxbDao.getRqListByKsnyAndJsny(_ksrq_str.substring(0,7), _jsrq_str.substring(0, 7));
		List<SybReport> sybReport_list = new ArrayList<SybReport>();
		SybReport sumSybReport = new SybReport();
		for(Map<String, Object> row : rq_list){
			String ny = (String)row.get("rq");
			SybReport sybReport = getSybReportByRq(ny+"-01 00:00:00", ny+"-"+getLastDayByYearAndMonth(ny)+" 23:59:59");
			sumSybReport.setZyywlr(nullToZero(sumSybReport.getZyywlr())+nullToZero(sybReport.getZyywlr()));
			sumSybReport.setGlfy(nullToZero(sumSybReport.getGlfy())+nullToZero(sybReport.getGlfy()));
			sumSybReport.setCwfy(nullToZero(sumSybReport.getCwfy())+nullToZero(sybReport.getGlfy()));
			sumSybReport.setJyfy(nullToZero(sumSybReport.getJyfy())+nullToZero(sybReport.getJyfy()));
			sumSybReport.setYywsr(nullToZero(sumSybReport.getYywsr())+nullToZero(sybReport.getYywsr()));
			sumSybReport.setYywzc(nullToZero(sumSybReport.getYywzc())+nullToZero(sybReport.getYywzc()));
			sumSybReport.setSds(nullToZero(sumSybReport.getSds())+nullToZero(sybReport.getSds()));
			sumSybReport.setJlr(nullToZero(sumSybReport.getJlr())+nullToZero(sybReport.getJlr()));
			sybReport.setNy(ny);
			sybReport_list.add(sybReport);	
		}
		//SybReport sybReport = getSybReportByRq(_ksrq_str, _jsrq_str);
		
		OutputStream os = response.getOutputStream();// 取得输出流 		
		String filename = new String(("损益表"+"("+_ksrq_str.substring(0, 10)+"~"+_jsrq_str.substring(0, 10)+")").getBytes("GB2312"), "ISO_8859_1");
		response.setHeader("Content-Disposition","attachment;filename=" + filename + ".xls");
		response.setContentType("application/msexcel");			
		WritableWorkbook wwb = Workbook.createWorkbook(os);		
		
		WritableSheet wsheet = wwb.createSheet("损益表"+"("+_ksrq_str.substring(0, 10)+"~"+_jsrq_str.substring(0, 10)+")", 0); // sheet名称
		wsheet.getSettings().setDefaultColumnWidth(10);	
		
		String[] titles = {"日期","主营业务利润","管理费用","财务费用","经营费用","营业外收入","营业外支出","所得税","净利润"};
		wsheet.addCell(new Label(0,0,"损益表", setCellFormat())); //第一行
		wsheet.mergeCells(0, 0, titles.length, 0);
		wsheet.addCell(new Label(0,1,DateUtil.formatDate(new Date(), "yyyy-MM-dd"), setCellFormat())); //第二行
		wsheet.mergeCells(0, 1, titles.length, 1);
		for(int i=0;i<titles.length;i++){
			wsheet.addCell(new Label(i,2,titles[i], setCellFormat()));
		}		
		wsheet.setColumnView(0, 25);
		//List<SybReport> list = new ArrayList<SybReport>();
		//list.add(sybReport);
		int i = 3;
		for(SybReport syb : sybReport_list){
			wsheet.addCell(new Label(0,i,syb.getNy(), setCellFormat()));
			wsheet.addCell(new Label(1,i,StringUtil.formatFloat(syb.getZyywlr()), setCellFormat()));
			wsheet.addCell(new Label(2,i,StringUtil.formatDouble(syb.getGlfy()), setCellFormat()));
			wsheet.addCell(new Label(3,i,StringUtil.formatDouble(syb.getCwfy()), setCellFormat()));
			wsheet.addCell(new Label(4,i,StringUtil.formatFloat(syb.getJyfy()), setCellFormat()));
			wsheet.addCell(new Label(5,i,StringUtil.formatFloat(syb.getYywsr()), setCellFormat()));
			wsheet.addCell(new Label(6,i,StringUtil.formatDouble(syb.getYywzc()), setCellFormat()));
			wsheet.addCell(new Label(7,i,StringUtil.formatDouble(syb.getSds()), setCellFormat()));
			wsheet.addCell(new Label(8,i,StringUtil.formatFloat(syb.getJlr()), setCellFormat()));
			i++;
		}
		
		//sum
		wsheet.addCell(new Label(0,i,"合计", setCellFormat()));
		wsheet.addCell(new Label(1,i,StringUtil.formatFloat(sumSybReport.getZyywlr()), setCellFormat()));
		wsheet.addCell(new Label(2,i,StringUtil.formatDouble(sumSybReport.getGlfy()), setCellFormat()));
		wsheet.addCell(new Label(3,i,StringUtil.formatDouble(sumSybReport.getCwfy()), setCellFormat()));
		wsheet.addCell(new Label(4,i,StringUtil.formatFloat(sumSybReport.getJyfy()), setCellFormat()));
		wsheet.addCell(new Label(5,i,StringUtil.formatFloat(sumSybReport.getYywsr()), setCellFormat()));
		wsheet.addCell(new Label(6,i,StringUtil.formatDouble(sumSybReport.getYywzc()), setCellFormat()));
		wsheet.addCell(new Label(7,i,StringUtil.formatDouble(sumSybReport.getSds()), setCellFormat()));
		wsheet.addCell(new Label(8,i,StringUtil.formatFloat(sumSybReport.getJlr()), setCellFormat()));
		
		
		wwb.write();
		wwb.close();		
		os.close(); 
		return null;
	}
	 
	
	private SybReport getSybReportByRq(String _ksrq_str, String _jsrq_str){
		SybReport sybReport = new SybReport();
		System.out.println("_ksrq_str:"+_ksrq_str);
		System.out.println("_jsrq_str:"+_jsrq_str);
		//主营业务费用
		Float zyywlr = (Float) getDomainService().createCriteria(Khqk.class)
				.add(Expression.ge("bgrq", DateUtil.parseSimpleDateTime(_ksrq_str)))
				.add(Expression.le("bgrq", DateUtil.parseSimpleDateTime(_jsrq_str)))
				.setProjection(Projections.sum("hj")).uniqueResult();
		System.out.println("zyywlr:"+zyywlr);
		sybReport.setZyywlr(nullToZero(zyywlr));
		//管理费用
		Double glfymx = (Double) getDomainService().createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", _ksrq_str.substring(0, 10)))
				.add(Expression.le("fyrq", _jsrq_str.substring(0, 10)))
				.add(Expression.like("type", "30%"))
				.setProjection(Projections.sum("je")).uniqueResult();	
		Double yggz = (Double) getDomainService().createCriteria(Yggz.class)
				.add(Expression.ge("gzrq", _ksrq_str.substring(0, 10)))
				.add(Expression.le("gzrq", _jsrq_str.substring(0, 10)))
				.setProjection(Projections.sum("yfgz")).uniqueResult();	
		glfymx = nullToZero(glfymx);
		yggz = nullToZero(yggz);
		//System.out.println("glfymx:"+glfymx);		 
		//System.out.println("yggz:"+yggz);	
		System.out.println("glfy:"+Float.parseFloat(StringUtil.formatDouble((glfymx+yggz))));
		sybReport.setGlfy(glfymx+yggz);
		//财务费用
		Double cwfy101 = (Double) getDomainService().createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", _ksrq_str.substring(0, 10)))
				.add(Expression.le("fyrq", _jsrq_str.substring(0, 10)))
				.add(Expression.like("type", "101"))
				.setProjection(Projections.sum("je")).uniqueResult();	
		Double cwfy102 = (Double) getDomainService().createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", _ksrq_str.substring(0, 10)))
				.add(Expression.le("fyrq", _jsrq_str.substring(0, 10)))
				.add(Expression.like("type", "102"))
				.setProjection(Projections.sum("je")).uniqueResult();	
		Double cwfy103 = (Double) getDomainService().createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", _ksrq_str.substring(0, 10)))
				.add(Expression.le("fyrq", _jsrq_str.substring(0, 10)))
				.add(Expression.like("type", "103"))
				.setProjection(Projections.sum("je")).uniqueResult();	
		Double cwfy104 = (Double) getDomainService().createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", _ksrq_str.substring(0, 10)))
				.add(Expression.le("fyrq", _jsrq_str.substring(0, 10)))
				.add(Expression.like("type", "104"))
				.setProjection(Projections.sum("je")).uniqueResult();	
		cwfy101 = nullToZero(cwfy101);
		cwfy102 = nullToZero(cwfy102);
		cwfy103 = nullToZero(cwfy103);
		cwfy104 = nullToZero(cwfy104);
		System.out.println("cwfy:"+Float.parseFloat(StringUtil.formatDouble((cwfy101-cwfy102+cwfy103+cwfy104))));
		sybReport.setCwfy(cwfy101-cwfy102+cwfy103+cwfy104);
		//经营费用
		Float fkzf = (Float) getDomainService().createCriteria(Fkzf.class)
				.add(Expression.ge("bgrq", DateUtil.parseSimpleDateTime(_ksrq_str)))
				.add(Expression.le("bgrq", DateUtil.parseSimpleDateTime(_jsrq_str)))
				.setProjection(Projections.sum("hj")).uniqueResult();
		System.out.println("fkzf:"+fkzf);
		Float tczc = (Float) getDomainService().createCriteria(Tczc.class)
				.add(Expression.ge("bgrq", DateUtil.parseSimpleDateTime(_ksrq_str)))
				.add(Expression.le("bgrq", DateUtil.parseSimpleDateTime(_jsrq_str)))
				.setProjection(Projections.sum("fyjehj")).uniqueResult();
		fkzf = nullToZero(fkzf);
		tczc = nullToZero(tczc);
		System.out.println("fkzf:"+fkzf);
		System.out.println("tczc:"+tczc);
		sybReport.setJyfy(fkzf+tczc);
		//营业外收入
		Float yywsr = (Float) getDomainService().createCriteria(Yywsr.class)
				.add(Expression.ge("ny", DateUtil.parseSimpleDateTime(_ksrq_str)))
				.add(Expression.le("ny", DateUtil.parseSimpleDateTime(_jsrq_str)))
				.setProjection(Projections.sum("je")).uniqueResult();
		yywsr = nullToZero(yywsr);
		System.out.println("yywsr:"+yywsr);		
		sybReport.setYywsr(yywsr);
		//营业外支出
		Double yywzc = (Double) getDomainService().createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", _ksrq_str.substring(0, 10)))
				.add(Expression.le("fyrq", _jsrq_str.substring(0, 10)))
				.add(Expression.like("type", "20%"))
				.setProjection(Projections.sum("je")).uniqueResult();	
		yywzc = nullToZero(yywzc);
		System.out.println("yywzc:"+Float.parseFloat(StringUtil.formatDouble(yywzc)));
		sybReport.setYywzc(yywzc);
		//所得税
		Double sds = (Double) getDomainService().createCriteria(Sds.class)
				.add(Expression.ge("fyrq", _ksrq_str.substring(0, 10)))
				.add(Expression.le("fyrq", _jsrq_str.substring(0, 10)))
				.setProjection(Projections.sum("je")).uniqueResult();	
		sds = nullToZero(sds);
		System.out.println("sds:"+Float.parseFloat(StringUtil.formatDouble(sds)));
		sybReport.setSds(sds);
		//净利润
		//主营业务利润-管理费用-财务费用-经营费用+营业外收入-营业外支出-所得税=净利润
		sybReport.setJlr(sybReport.getZyywlr()-Float.parseFloat(StringUtil.formatDouble(sybReport.getGlfy()))-Float.parseFloat(StringUtil.formatDouble(sybReport.getCwfy()))-sybReport.getJyfy()+sybReport.getYywsr()-Float.parseFloat(StringUtil.formatDouble(sybReport.getYywzc()))-Float.parseFloat(StringUtil.formatDouble(sybReport.getSds())));
		//return
		return sybReport;
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
		
		boolean lastPage = EimsUtil.ifLastPage(curPage, page);
		//是否为最后一页
		map.put("ifLastPage",lastPage);	
		if(lastPage){
			//统计各数总和
			Criteria criteria2 = getDomainService().createCriteria(domainClass);
			setSqlExpression(request, criteria2);
			map.put("sum_zyywlr", (Float)criteria.setProjection(Projections.sum("zyywlr")).uniqueResult());
			map.put("sum_glfy", (Float)criteria.setProjection(Projections.sum("glfy")).uniqueResult());
			map.put("sum_cwfy", (Float)criteria.setProjection(Projections.sum("cwfy")).uniqueResult());		
			map.put("sum_jyfy", (Float)criteria.setProjection(Projections.sum("jyfy")).uniqueResult());	
			map.put("sum_yywsr", (Float)criteria.setProjection(Projections.sum("yywsr")).uniqueResult());	
			map.put("sum_yywzc", (Float)criteria.setProjection(Projections.sum("yywzc")).uniqueResult());	
			map.put("sum_sds", (Float)criteria.setProjection(Projections.sum("sds")).uniqueResult());	
			map.put("sum_jlr", (Float)criteria.setProjection(Projections.sum("jlr")).uniqueResult());
		}
		
		return new ModelAndView(getListPage(), map);
	}
	
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Syb entity = (Syb)getCommandObject(request, getDomainClass());
		
		//净利润=主营业务利润-经营费用-管理费用-财务费用+营业外收入-营业外支出-所得税
		float jlr = entity.getZyywlr() - entity.getJyfy() - entity.getGlfy() - entity.getCwfy() + entity.getYywsr() - entity.getYywzc() - entity.getSds();
		entity.setJlr(jlr);
		entity.setNy(DateUtil.parseSimpleDate(request.getParameter("_ny"))); //转成为日期格式
		
		if(StringUtils.isBlank(request.getParameter("id"))){
			entity.setLrsj(new Date());
			
			getDomainService().save(entity);
		}else{
			Syb impl = service.get(getDomainId(request));
			entity.setLrsj(impl.getLrsj());
			service.clear();
			
			getDomainService().saveOrUpdate(entity);
		}
		AjaxView view = new AjaxView(true, "操作成功！");
		view.setProperty("refresh", true);
		return new ModelAndView(view);
	}
	
	public void setService(SybService service) {
		this.service = service;
	}
	
	/**
	 * 导出EXCEL
	 */
	public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) throws Exception {
		OutputStream os = response.getOutputStream();// 取得输出流 		
		String filename = new String("损益表".getBytes("GB2312"), "ISO_8859_1");
		response.setHeader("Content-Disposition","attachment;filename=" + filename + ".xls");
		response.setContentType("application/msexcel");			
		WritableWorkbook wwb = Workbook.createWorkbook(os);		
		
		WritableSheet wsheet = wwb.createSheet("损益表", 0); // sheet名称
		wsheet.getSettings().setDefaultColumnWidth(10);	
		
		String[] titles = {"年月","主营业务利润","管理费用","财务费用","经营费用","营业外收入","营业外支出","所得税","净利润"};
		wsheet.addCell(new Label(0,0,"损益表", setCellFormat())); //第一行
		wsheet.mergeCells(0, 0, titles.length, 0);
		wsheet.addCell(new Label(0,1,DateUtil.formatDate(new Date(), "yyyy-MM-dd"), setCellFormat())); //第二行
		wsheet.mergeCells(0, 1, titles.length, 1);
		for(int i=0;i<titles.length;i++){
			wsheet.addCell(new Label(i,2,titles[i], setCellFormat()));
		}		
		Criteria criteria = getDomainService().createCriteria(domainClass);
		setSqlExpression(request, criteria);	
		List<Syb> list = criteria.list();
		int i = 3;
		for(Syb syb : list){
			wsheet.addCell(new Label(0,i,DateUtil.formatDate(syb.getNy(), "yyyy-MM-dd"), setCellFormat()));
			wsheet.addCell(new Label(1,i,StringUtil.formatFloat(syb.getZyywlr()), setCellFormat()));
			wsheet.addCell(new Label(2,i,StringUtil.formatFloat(syb.getGlfy()), setCellFormat()));
			wsheet.addCell(new Label(3,i,StringUtil.formatFloat(syb.getCwfy()), setCellFormat()));
			wsheet.addCell(new Label(4,i,StringUtil.formatFloat(syb.getJyfy()), setCellFormat()));
			wsheet.addCell(new Label(5,i,StringUtil.formatFloat(syb.getYywsr()), setCellFormat()));
			wsheet.addCell(new Label(6,i,StringUtil.formatFloat(syb.getYywzc()), setCellFormat()));
			wsheet.addCell(new Label(7,i,StringUtil.formatFloat(syb.getSds()), setCellFormat()));
			wsheet.addCell(new Label(8,i,StringUtil.formatFloat(syb.getJlr()), setCellFormat()));
			i++;
		}
		
		//统计各数总和(合计行)
		Float sum_zyywlr = (Float)criteria.setProjection(Projections.sum("zyywlr")).uniqueResult();
		Float sum_glfy = (Float)criteria.setProjection(Projections.sum("glfy")).uniqueResult();
		Float sum_cwfy = (Float)criteria.setProjection(Projections.sum("cwfy")).uniqueResult();
		Float sum_jyfy = (Float)criteria.setProjection(Projections.sum("jyfy")).uniqueResult();
		Float sum_yywsr = (Float)criteria.setProjection(Projections.sum("yywsr")).uniqueResult();
		Float sum_yywzc = (Float)criteria.setProjection(Projections.sum("yywzc")).uniqueResult();
		Float sum_sds = (Float)criteria.setProjection(Projections.sum("sds")).uniqueResult();
		Float sum_jlr = (Float)criteria.setProjection(Projections.sum("jlr")).uniqueResult();
		wsheet.addCell(new Label(0,i,"合计", setCellFormat()));
		wsheet.addCell(new Label(1,i,StringUtil.formatFloat(sum_zyywlr), setCellFormat()));
		wsheet.addCell(new Label(2,i,StringUtil.formatFloat(sum_glfy), setCellFormat()));
		wsheet.addCell(new Label(3,i,StringUtil.formatFloat(sum_cwfy), setCellFormat()));
		wsheet.addCell(new Label(4,i,StringUtil.formatFloat(sum_jyfy), setCellFormat()));
		wsheet.addCell(new Label(5,i,StringUtil.formatFloat(sum_yywsr), setCellFormat()));
		wsheet.addCell(new Label(6,i,StringUtil.formatFloat(sum_yywzc), setCellFormat()));
		wsheet.addCell(new Label(7,i,StringUtil.formatFloat(sum_sds), setCellFormat()));
		wsheet.addCell(new Label(8,i,StringUtil.formatFloat(sum_jlr), setCellFormat()));
		
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

	public String getReportPage() {
		return reportPage;
	}


	public void setReportPage(String reportPage) {
		this.reportPage = reportPage;
	}
	public Double nullToZero(Double d){
		if(d==null)
			return 0d;
		return d;
	}
	public Float nullToZero(Float f){
		if(f==null)
			return 0f;
		return f;
	}

	public FymxbDao getFymxbDao() {
		return fymxbDao;
	}

	public void setFymxbDao(FymxbDao fymxbDao) {
		this.fymxbDao = fymxbDao;
	}
	
	
	  public static void main(String args[]){
		  SybController t = new SybController();
		  t.getLastDayByYearAndMonth("2014-06");
		    int year=2014;
		    if((year%4==0&&year%100!=0)||year%400==0)
		    System.out.println("2009是闰年。");
		    else
		    System.out.println("2009是平年。");
		}
	
	public boolean isPingNian(int year){
	    if((year%4==0&&year%100!=0)||year%400==0)
	    	return false;
	    	//System.out.println("2009是闰年。");
	    else
	    	return true;
	    	//System.out.println("2009是平年。");
	}	
	public String getLastDayByYearAndMonth(String ny){
		String year = ny.substring(0,4);
		String month = ny.substring(5,7);
		if("01".equals(month)){
			return "31";
		}
		if("02".equals(month)){
			if(isPingNian(Integer.parseInt(year))){
				return "28";
			}else{
				return "29";
			}
			
		}
		if("03".equals(month)){
			return "31";
		}
		if("04".equals(month)){
			return "30";
		}
		if("05".equals(month)){
			return "31";
		}
		if("06".equals(month)){
			return "30";
		}
		if("07".equals(month)){
			return "31";
		}
		if("08".equals(month)){
			return "31";
		}
		if("09".equals(month)){
			return "30";
		}
		if("10".equals(month)){
			return "31";
		}
		if("11".equals(month)){
			return "30";
		}
		if("12".equals(month)){
			return "31";
		}
		return "";
	}
	
	
}
