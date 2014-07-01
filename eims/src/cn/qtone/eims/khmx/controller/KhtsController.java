package cn.qtone.eims.khmx.controller;

import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
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
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

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
import cn.qtone.eims.khmx.domain.Khts;
import cn.qtone.eims.khmx.service.KhtsService;
import cn.qtone.eims.util.EimsUtil;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;

public class KhtsController extends SimpleManageController<Khts, KhtsService>{

	private KhtsService service;

	public ModelAndView findByBgdh(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//String id_str = request.getParameter("id");
		//Khts khts = service.get(Integer.parseInt(id_str));
		String bgdh = request.getParameter("bgdh");
		Khts khts = service.findUniqueBy("bgdh", bgdh);
		Map<String,Object> map = new HashMap<String,Object>();		
		map.put("id", khts.getId());		
		map.put("bgdh", khts.getBgdh());
		map.put("dh", khts.getDh());
		map.put("khmc", khts.getKhmc());
		map.put("bgrq", DateUtil.formatDate(khts.getBgrq(),"yyyy-MM-dd"));
		map.put("bgje", khts.getBgje());
		map.put("fpje", khts.getFpje());
		map.put("sfprq", khts.getSfprq());
		map.put("tsje", khts.getTsje());
		map.put("zftsrq", khts.getZftsrq());
		map.put("yfje", khts.getYfje());
		map.put("wfje", khts.getWfje());
		map.put("fkdh", khts.getFkdh());
		map.put("ywy", khts.getYwy());
		map.put("dlfbz", khts.getDlfbz());
		map.put("lrsj", DateUtil.formatDate(khts.getLrsj(),"yyyy-MM-dd"));		
		JSONObject khtsJson = JSONObject.fromObject(map);
		return new ModelAndView(new TextView(khtsJson.toString()));
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
			map.put("sum_bgje", (Float)criteria2.setProjection(Projections.sum("bgje")).uniqueResult());
			map.put("sum_fpje", (Float)criteria2.setProjection(Projections.sum("fpje")).uniqueResult());
			map.put("sum_tsje", (Float)criteria2.setProjection(Projections.sum("tsje")).uniqueResult());	
			map.put("sum_yfje", (Float)criteria2.setProjection(Projections.sum("yfje")).uniqueResult());	
			map.put("sum_wfje", (Float)criteria2.setProjection(Projections.sum("wfje")).uniqueResult());	
		}
		
		return new ModelAndView(getListPage(), map);
	}
	
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Khts entity = (Khts)getCommandObject(request, getDomainClass());
		entity.setBgrq(DateUtil.parseSimpleDate(request.getParameter("_bgrq"))); //转成为日期格式
		
		if(StringUtils.isBlank(request.getParameter("id"))){
			entity.setLrsj(new Date());
			
			getDomainService().save(entity);
		}else{
			Khts impl = service.get(getDomainId(request));
			entity.setLrsj(impl.getLrsj());
			service.clear();
			
			getDomainService().saveOrUpdate(entity);
		}
		AjaxView view = new AjaxView(true, "操作成功！");
		view.setProperty("refresh", true);
		return new ModelAndView(view);
	}
	public void setService(KhtsService service) {
		this.service = service;
	}
	
	/**
	 * 检查报关单号是否唯一(如果不存在则返回true)
	 */
	public ModelAndView checkBgdh(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bgdh = request.getParameter("bgdh");
		Integer id = StringUtil.parseInt(request.getParameter("id"), 0);
		String flag = "";
		
		Khts entity = service.findUniqueBy("bgdh", bgdh);
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
	 * 根据报关单号获取相应的值
	 */
	public ModelAndView getKhts(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String bgdh = request.getParameter("bgdh");
		Khts khts = service.findUniqueBy("bgdh", bgdh);
		String flag = "success";
		if(khts == null) flag = "fail";
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(khts != null){
			map.put("bgdh", khts.getBgdh());
			map.put("dh", khts.getDh());
			map.put("khmc", khts.getKhmc());
			map.put("bgrq", DateUtil.formatDate(khts.getBgrq(), "yyyy-MM-dd"));
			map.put("bgje", StringUtil.formatFloat(khts.getBgje()));
		}
				
		AjaxView view = new AjaxView(flag);
		view.setProperty("data", map);
		return new ModelAndView(view);
	}
	
	/**
	 * 导出EXCEL
	 */
	public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) throws Exception {
		OutputStream os = response.getOutputStream();// 取得输出流 		
		String filename = new String("客户退税明细表".getBytes("GB2312"), "ISO_8859_1");
		response.setHeader("Content-Disposition","attachment;filename=" + filename + ".xls");
		response.setContentType("application/msexcel");			
		WritableWorkbook wwb = Workbook.createWorkbook(os);		
		
		WritableSheet wsheet = wwb.createSheet("客户退税明细表", 0); // sheet名称
		wsheet.getSettings().setDefaultColumnWidth(10);	
		
		String[] titles = {"单号","客户名称","报关单号","报关日期","报关金额","发票金额","收发票日期","退税金额","支付退税日期","已付金额",
				"未付金额","付款单号"};
		wsheet.addCell(new Label(0,0,"客户退税明细表", setCellFormat())); //第一行
		wsheet.mergeCells(0, 0, titles.length, 0);
		wsheet.addCell(new Label(0,1,DateUtil.formatDate(new Date(), "yyyy-MM-dd"), setCellFormat())); //第二行
		wsheet.mergeCells(0, 1, titles.length, 1);
		for(int i=0;i<titles.length;i++){
			wsheet.addCell(new Label(i,2,titles[i], setCellFormat()));
		}
		
		Criteria criteria = getDomainService().createCriteria(domainClass);
		setSqlExpression(request, criteria);	
		List<Khts> list = criteria.list();
		int i = 3;
		for(Khts khts : list){
			wsheet.addCell(new Label(0,i,khts.getDh(), setCellFormat()));
			wsheet.addCell(new Label(1,i,khts.getKhmc(), setCellFormat()));
			wsheet.addCell(new Label(2,i,khts.getBgdh(), setCellFormat()));
			wsheet.addCell(new Label(3,i,DateUtil.formatDate(khts.getBgrq(), "yyyy-MM-dd"), setCellFormat()));
			wsheet.addCell(new Label(4,i,StringUtil.formatFloat(khts.getBgje()), setCellFormat()));
			wsheet.addCell(new Label(5,i,StringUtil.formatFloat(khts.getFpje()), setCellFormat()));
			wsheet.addCell(new Label(6,i,khts.getSfprq(), setCellFormat()));
			wsheet.addCell(new Label(7,i,StringUtil.formatFloat(khts.getTsje()), setCellFormat()));
			wsheet.addCell(new Label(8,i,khts.getZftsrq(), setCellFormat()));
			wsheet.addCell(new Label(9,i,StringUtil.formatFloat(khts.getYfje()), setCellFormat()));
			wsheet.addCell(new Label(10,i,StringUtil.formatFloat(khts.getWfje()), setCellFormat()));
			wsheet.addCell(new Label(11,i,khts.getFkdh(), setCellFormat()));		
			i++;
		}
		
		//统计总数行
		Float sum_bgje = (Float)criteria.setProjection(Projections.sum("bgje")).uniqueResult();
		Float sum_fpje = (Float)criteria.setProjection(Projections.sum("fpje")).uniqueResult();
		Float sum_tsje = (Float)criteria.setProjection(Projections.sum("tsje")).uniqueResult();
		Float sum_yfje = (Float)criteria.setProjection(Projections.sum("yfje")).uniqueResult();
		Float sum_wfje = (Float)criteria.setProjection(Projections.sum("wfje")).uniqueResult();
		wsheet.addCell(new Label(0,i,"合计", setCellFormat()));
		wsheet.mergeCells(0, i, 3, i);
		wsheet.addCell(new Label(4,i,StringUtil.formatFloat(sum_bgje), setCellFormat()));	
		wsheet.addCell(new Label(5,i,StringUtil.formatFloat(sum_fpje), setCellFormat()));	
		wsheet.addCell(new Label(6,i,"", setCellFormat()));
		wsheet.addCell(new Label(7,i,StringUtil.formatFloat(sum_tsje), setCellFormat()));	
		wsheet.addCell(new Label(8,i,"", setCellFormat()));
		wsheet.addCell(new Label(9,i,StringUtil.formatFloat(sum_yfje), setCellFormat()));	
		wsheet.addCell(new Label(10,i,StringUtil.formatFloat(sum_wfje), setCellFormat()));
		wsheet.addCell(new Label(11,i,"", setCellFormat()));
		
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
