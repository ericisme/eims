package cn.qtone.eims.lb.controller;

import java.io.OutputStream;
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
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.DateUtil;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.eims.lb.domain.Syb;
import cn.qtone.eims.lb.service.SybService;
import cn.qtone.eims.util.EimsUtil;

public class SybController extends SimpleManageController<Syb, SybService>{

	private SybService service;

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
}
