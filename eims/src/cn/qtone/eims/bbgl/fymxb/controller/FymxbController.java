package cn.qtone.eims.bbgl.fymxb.controller;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;


import cn.qtone.common.components.syspurview.base.controller.BaseManageController;
import cn.qtone.eims.bbgl.fymxb.dao.FymxbDao;
import cn.qtone.eims.bbgl.fymxb.domain.Fymxb;
import cn.qtone.eims.fymx.glfymx.service.GlfymxService;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;

public class FymxbController extends BaseManageController {

	
	private GlfymxService service;
	private FymxbDao dao;
	
	private String listPage;
	private String indexPage; 
	private String printPage;
	
	/**
	 * 打印，根据单号
	 */
	public ModelAndView print(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		Map<String,Object> map = this.getMapWithUser(request);		
		String fyrq__gte__string = ServletUtil.removeSpace(request, "fyrq__gte__string");
		String fyrq__lte__string = ServletUtil.removeSpace(request, "fyrq__lte__string");			
		//获得统计数据		
		Fymxb fymxb =this.getDao().getFymxbData(fyrq__gte__string, fyrq__lte__string, this.getService());
		fymxb.setKsrq(fyrq__gte__string);
		fymxb.setJsrq(fyrq__lte__string);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String xlsHtml = this.getDao().getXlsBb(fymxb,baos);	
		
		List<Fymxb> _fymxb = this.getDao().getFymxbListGroupByMonth(fyrq__gte__string.substring(0,7), fyrq__lte__string.substring(0,7));
		
		List<Fymxb> _fymxb_total = this.getDao().getFymxbTotalListGroupByMonth(fyrq__gte__string.substring(0,7), fyrq__lte__string.substring(0,7));
		_fymxb.addAll(_fymxb_total);
		
		xlsHtml = this.getDao().getXlsBbByFymxbList(_fymxb,baos);
		
		
		
		map.put("fymxb", fymxb);	
		map.put("xlsHtml", xlsHtml);
		return new ModelAndView(this.getPrintPage(), map);
	}
	
	//index methord
	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> map = this.getMapWithUser(request);
		return new ModelAndView(this.getIndexPage(), map);
	}
	
	
	//输出xls
	public ModelAndView exportXls(HttpServletRequest request,
			HttpServletResponse response) throws Exception {		
		Map<String,Object> map = this.getMapWithUser(request);		
		String fyrq__gte__string = ServletUtil.removeSpace(request, "fyrq__gte__string");
		String fyrq__lte__string = ServletUtil.removeSpace(request, "fyrq__lte__string");				
		//获得统计数据		
		Fymxb fymxb =this.getDao().getFymxbData(fyrq__gte__string, fyrq__lte__string, this.getService());
		fymxb.setKsrq(fyrq__gte__string);
		fymxb.setJsrq(fyrq__lte__string);				
		//输出xls下载文件
		OutputStream os = response.getOutputStream();// 取得输出流 		
		String filename = new String("费用明细报表".getBytes("GB2312"), "ISO_8859_1");
		response.setHeader("Content-Disposition","attachment;filename=" + filename + ".xls");
		response.setContentType("application/msexcel");			
		//this.getDao().getXlsBb(fymxb,os);	
		List<Fymxb> _fymxb = this.getDao().getFymxbListGroupByMonth(fyrq__gte__string.substring(0,7), fyrq__lte__string.substring(0,7));
		
		List<Fymxb> _fymxb_total = this.getDao().getFymxbTotalListGroupByMonth(fyrq__gte__string.substring(0,7), fyrq__lte__string.substring(0,7));
		_fymxb.addAll(_fymxb_total);
		
		this.getDao().getXlsBbByFymxbList(_fymxb,os);
		return null;
	}
	
	
	
	@Override
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {		
		Map<String,Object> map = this.getMapWithUser(request);		
		String fyrq__gte__string = ServletUtil.removeSpace(request, "fyrq__gte__string");
		String fyrq__lte__string = ServletUtil.removeSpace(request, "fyrq__lte__string");			
		//获得统计数据		
		Fymxb fymxb =this.getDao().getFymxbData(fyrq__gte__string, fyrq__lte__string, this.getService());
		fymxb.setKsrq(fyrq__gte__string);
		fymxb.setJsrq(fyrq__lte__string);
		
		System.out.println("fyrq__gte__string.substring(0,7):"+fyrq__gte__string.substring(0,7));
		System.out.println("fyrq__lte__string.substring(0,7):"+fyrq__lte__string.substring(0,7));
		
		List<Fymxb> _fymxb = this.getDao().getFymxbListGroupByMonth(fyrq__gte__string.substring(0,7), fyrq__lte__string.substring(0,7));
		
		List<Fymxb> _fymxb_total = this.getDao().getFymxbTotalListGroupByMonth(fyrq__gte__string.substring(0,7), fyrq__lte__string.substring(0,7));
		_fymxb.addAll(_fymxb_total);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();		
		//String xlsHtml = this.getDao().getXlsBb(fymxb,baos);	
		String xlsHtml = this.getDao().getXlsBbByFymxbList(_fymxb,baos);
		
		
		map.put("fymxb", fymxb);	
		map.put("xlsHtml", xlsHtml);
		return new ModelAndView(this.getListPage(), map);
	}	
	
	
	
	@Override
	public ModelAndView query(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelAndView create(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public ModelAndView remove(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelAndView save(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelAndView show(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelAndView update(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	public GlfymxService getService() {
		return service;
	}

	public void setService(GlfymxService service) {
		this.service = service;
	}

	public FymxbDao getDao() {
		return dao;
	}

	public void setDao(FymxbDao dao) {
		this.dao = dao;
	}

	public String getListPage() {
		return listPage;
	}

	public void setListPage(String listPage) {
		this.listPage = listPage;
	}

	public String getIndexPage() {
		return indexPage;
	}

	public void setIndexPage(String indexPage) {
		this.indexPage = indexPage;
	}


	public String getPrintPage() {
		return printPage;
	}


	public void setPrintPage(String printPage) {
		this.printPage = printPage;
	}





	
	
}
