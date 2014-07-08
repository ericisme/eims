package cn.qtone.eims.kjpz.controller;

import cn.qtone.common.simplemvc.controller.SimpleManageController;
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



//	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Map<String, Object> map = this.getMapWithUser(request);
//		int curPage = this.getCurrentPage(request); // 当前查询页数
//		Criteria criteria = getDomainService().createCriteria(domainClass);
//		setSqlExpression(request, criteria);		
//		Page page = getDomainService().pagedQuery(criteria, curPage, Page.DEFAULT_PAGE_SIZE);
//		//TODO 完善page对象
//		page.setPaginate(this.getAjaxPage(request, curPage, page, StringUtil.unCapitalize(domainClass.getSimpleName()) + "Jump"));
//		map.put("page", page);		
//		
//		boolean lastPage = EimsUtil.ifLastPage(curPage, page);
//		//是否为最后一页
//		map.put("ifLastPage",lastPage);	
//		if(lastPage){
//			//统计总数行
//			Criteria criteria2 = getDomainService().createCriteria(domainClass);
//			setSqlExpression(request, criteria2);	
//			map.put("sum_tsje", (Float)criteria2.setProjection(Projections.sum("tsje")).uniqueResult());
//			map.put("sum_ysje", (Float)criteria2.setProjection(Projections.sum("ysje")).uniqueResult());
//			map.put("sum_wsje", (Float)criteria2.setProjection(Projections.sum("wsje")).uniqueResult());	
//		}
//		
//		return new ModelAndView(getListPage(), map);
//	}
//	
//	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Gjts entity = (Gjts)getCommandObject(request, getDomainClass());
////		float wsje = entity.getTsje() - entity.getYsje(); //未收金额=退税金额-已收金额
////		entity.setWsje(wsje);
//		entity.setTsrq(DateUtil.parseSimpleDate(request.getParameter("_tsrq"))); //转成为日期格式
//		
//		if(StringUtils.isBlank(request.getParameter("id"))){
//			entity.setLrsj(new Date());
//			
//			getDomainService().save(entity);
//		}else{
//			Gjts impl = service.get(getDomainId(request));
//			entity.setLrsj(impl.getLrsj());
//			service.clear();
//			
//			getDomainService().saveOrUpdate(entity);
//		}
//		AjaxView view = new AjaxView(true, "操作成功！");
//		view.setProperty("refresh", true);
//		return new ModelAndView(view);
//	}
//	
//	/**
//	 * 检查报关单号是否唯一(如果不存在则返回true)
//	 */
//	public ModelAndView checkBgdh(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String bgdh = request.getParameter("bgdh");
//		Integer id = StringUtil.parseInt(request.getParameter("id"), 0);
//		String flag = "";
//		
//		Gjts entity = service.findUniqueBy("bgdh", bgdh);
//		if(entity != null){
//			if(!entity.getId().equals(id))
//				flag = "false";
//			else
//				flag = "true";
//		}else{
//			flag = "true";
//		}
//		
//		return new ModelAndView(new TextView(flag));
//	}
//	
//	/**
//	 * 导出EXCEL
//	 */
//	public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		OutputStream os = response.getOutputStream();// 取得输出流 		
//		String filename = new String("国家退税明细表".getBytes("GB2312"), "ISO_8859_1");
//		response.setHeader("Content-Disposition","attachment;filename=" + filename + ".xls");
//		response.setContentType("application/msexcel");			
//		WritableWorkbook wwb = Workbook.createWorkbook(os);		
//		
//		WritableSheet wsheet = wwb.createSheet("国家退税明细表", 0); // sheet名称
//		wsheet.getSettings().setDefaultColumnWidth(10);	
//		
//		String[] titles = {"单号","客户名称","报关单号","增值税率","退税率","退税金额","收退税日期","已收金额","未收金额"};
//		wsheet.addCell(new Label(0,0,"国家退税明细表", setCellFormat())); //第一行
//		wsheet.mergeCells(0, 0, titles.length, 0);
//		wsheet.addCell(new Label(0,1,DateUtil.formatDate(new Date(), "yyyy-MM-dd"), setCellFormat())); //第二行
//		wsheet.mergeCells(0, 1, titles.length, 1);
//		for(int i=0;i<titles.length;i++){
//			wsheet.addCell(new Label(i,2,titles[i], setCellFormat()));
//		}
//		
//		Criteria criteria = getDomainService().createCriteria(domainClass);
//		setSqlExpression(request, criteria);
//		List<Gjts> list = criteria.list();
//		int i = 3;
//		for(Gjts gjts : list){
//			wsheet.addCell(new Label(0,i,gjts.getDh(), setCellFormat()));
//			wsheet.addCell(new Label(1,i,gjts.getKhmc(), setCellFormat()));
//			wsheet.addCell(new Label(2,i,gjts.getBgdh(), setCellFormat()));
//			wsheet.addCell(new Label(3,i,gjts.getZzsl(), setCellFormat()));
//			wsheet.addCell(new Label(4,i,gjts.getTsl(), setCellFormat()));
//			wsheet.addCell(new Label(5,i,StringUtil.formatFloat(gjts.getTsje()), setCellFormat()));
//			wsheet.addCell(new Label(6,i,DateUtil.formatDate(gjts.getTsrq(), "yyyy-MM-dd"), setCellFormat()));
//			wsheet.addCell(new Label(7,i,StringUtil.formatFloat(gjts.getYsje()), setCellFormat()));
//			wsheet.addCell(new Label(8,i,StringUtil.formatFloat(gjts.getWsje()), setCellFormat()));			
//			i++;
//		}
//		//统计总数行
//		Float sum_tsje = (Float)criteria.setProjection(Projections.sum("tsje")).uniqueResult();
//		Float sum_ysje = (Float)criteria.setProjection(Projections.sum("ysje")).uniqueResult();
//		Float sum_wsje = (Float)criteria.setProjection(Projections.sum("wsje")).uniqueResult();
//		wsheet.addCell(new Label(0,i,"合计", setCellFormat()));
//		wsheet.mergeCells(0, i, 4, i);
//		wsheet.addCell(new Label(5,i,StringUtil.formatFloat(sum_tsje), setCellFormat()));	
//		wsheet.addCell(new Label(6,i,"", setCellFormat()));
//		wsheet.addCell(new Label(7,i,StringUtil.formatFloat(sum_ysje), setCellFormat()));	
//		wsheet.addCell(new Label(8,i,StringUtil.formatFloat(sum_wsje), setCellFormat()));	
//		
//		wwb.write();
//		wwb.close();		
//		os.close(); 
//		return null;
//	}
//	
//	private WritableCellFormat setCellFormat() throws WriteException{
//		WritableCellFormat cell = new WritableCellFormat(new WritableFont(WritableFont.createFont("宋体"),10,WritableFont.NO_BOLD));
//		cell.setAlignment(jxl.format.Alignment.CENTRE);
//		cell.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
//		
//		return cell;
//	}
//	public void setService(GjtsService service) {
//		this.service = service;
//	}
	
	
}
