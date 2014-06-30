package cn.qtone.eims.bbgl.khmxb.controller;

import java.io.OutputStream;
import java.util.ArrayList;
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

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.components.syspurview.base.controller.BaseManageController;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.utils.base.DateUtil;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.eims.khmx.domain.Fkzf;
import cn.qtone.eims.khmx.domain.Gjts;
import cn.qtone.eims.khmx.domain.Khqk;
import cn.qtone.eims.khmx.domain.Tczc;
import cn.qtone.eims.khmx.service.FkzfService;
import cn.qtone.eims.khmx.service.GjtsService;
import cn.qtone.eims.khmx.service.KhqkService;
import cn.qtone.eims.khmx.service.KhtsService;
import cn.qtone.eims.khmx.service.TczcService;
import cn.qtone.eims.util.EimsUtil;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;

public class KhmxbController extends BaseManageController{

	private String indexPage; 
	private String listPage;
	private String printPage;
	
	private KhtsService khtsService;
	private GjtsService gjtsService;
	private FkzfService fkzfService;
	private KhqkService khqkService;
	private TczcService tczcService;
	
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Object> map = this.getMapWithUser(request);
		return new ModelAndView(this.getIndexPage(), map);
	}
	
	@Override
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Object> map = this.getMapWithUser(request);	
		String bgdh = ServletUtil.removeSpace(request, "bgdh"); //报关单号
		String khmc = ServletUtil.removeSpace(request, "khmc"); //客户名称
		String ywy =  ServletUtil.removeSpace(request, "ywy"); //业务员
		String ksrq = ServletUtil.removeSpace(request, "ksrq"); //开始时间
		String jsrq = ServletUtil.removeSpace(request, "jsrq"); //结束时间
		String gsmc = ServletUtil.removeSpace(request, "gsmc"); //公司名称
		
		int curPage = this.getCurrentPage(request); // 当前查询页数		
		Page page = new Page();
		page.setPageSize(this.getListRows());
		khtsService.query(page,curPage, bgdh, khmc, ywy, ksrq, jsrq, gsmc);
		
		List<Map<String, Object>> results = (List<Map<String, Object>>) page.getResult();
		page.setResult(getList(results));
		
		page.setPaginate(this.getAjaxPage(request, curPage, page, "jump"));
		//if (log.isInfoEnabled()) log.info("分页代码：" +  page.getPaginate());
		map.put("page", page);
		
		boolean lastPage = EimsUtil.ifLastPage(curPage, page);
		//是否为最后一页
		map.put("ifLastPage",lastPage);	
		if(lastPage){
			List<Map<String, Object>> l = khtsService.findAll(bgdh, khmc, ywy, ksrq, jsrq, gsmc);
			List<Map<String, Object>> list = getList(l);
			map.put("hj", getHj(list));
		}
		return new ModelAndView(this.getListPage(), map);
	}
	
	/**
	 * 打印，根据报关单号
	 */
	public ModelAndView print(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		Map<String,Object> map = this.getMapWithUser(request);	
		
		//map.put("map", getList(request).get(0));
		return new ModelAndView(this.getPrintPage(), map);
	}
	
	/**
	 * 导出EXCEL
	 */
	public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		String bgdh = ServletUtil.removeSpace(request, "bgdh"); //报关单号
		String khmc = ServletUtil.removeSpace(request, "khmc"); //客户名称
		String ywy =  ServletUtil.removeSpace(request, "ywy"); //业务员
		String ksrq = ServletUtil.removeSpace(request, "ksrq"); //开始时间
		String jsrq = ServletUtil.removeSpace(request, "jsrq"); //结束时间
		String gsmc = ServletUtil.removeSpace(request, "gsmc"); //公司名称
		
		List<Map<String, Object>> results = khtsService.findAll(bgdh, khmc, ywy, ksrq, jsrq, gsmc);
		
		OutputStream os = response.getOutputStream();// 取得输出流 		
		String filename = new String("客户费用明细报表".getBytes("GB2312"), "ISO_8859_1");
		response.setHeader("Content-Disposition","attachment;filename=" + filename + ".xls");
		response.setContentType("application/msexcel");			
		WritableWorkbook wwb = Workbook.createWorkbook(os);		
		
		WritableSheet wsheet = wwb.createSheet("客户费用明细报表", 0); // sheet名称
		wsheet.getSettings().setDefaultColumnWidth(10);	
		
		wsheet.addCell(new Label(0,0,"客户名称", setCellFormat()));
		wsheet.mergeCells(0, 0, 0, 1);
		wsheet.addCell(new Label(1,0,"合作单位", setCellFormat()));
		wsheet.mergeCells(1, 0, 1, 1);
		wsheet.addCell(new Label(2,0,"报关日期", setCellFormat()));
		wsheet.mergeCells(2, 0, 2, 1);
		wsheet.addCell(new Label(3,0,"报关单号", setCellFormat()));
		wsheet.mergeCells(3, 0, 3, 1);
		wsheet.addCell(new Label(4,0,"报关金额", setCellFormat()));
		wsheet.mergeCells(4, 0, 4, 1);
		wsheet.addCell(new Label(5,0,"发票金额", setCellFormat()));
		wsheet.mergeCells(5, 0, 5, 1);
		wsheet.addCell(new Label(6,0,"收发票日期", setCellFormat()));
		wsheet.mergeCells(6, 0, 6, 1);
		wsheet.addCell(new Label(7,0,"退税金额", setCellFormat()));
		wsheet.mergeCells(7, 0, 7, 1);
		wsheet.addCell(new Label(8,0,"支付退税日期", setCellFormat()));
		wsheet.mergeCells(8, 0, 8, 1);
		wsheet.addCell(new Label(9,0,"收到国税退税日期", setCellFormat()));
		wsheet.mergeCells(9, 0, 9, 1);
		wsheet.addCell(new Label(10,0,"代理费", setCellFormat()));
		wsheet.mergeCells(10, 0, 10, 1);
		
		wsheet.addCell(new Label(11,0,"报关费", setCellFormat()));
		wsheet.mergeCells(11, 0, 12, 0);
		wsheet.addCell(new Label(13,0,"广建费", setCellFormat()));
		wsheet.mergeCells(13, 0, 14, 0);
		wsheet.addCell(new Label(15,0,"国检费", setCellFormat()));
		wsheet.mergeCells(15, 0, 16, 0);
		wsheet.addCell(new Label(17,0,"商检费", setCellFormat()));
		wsheet.mergeCells(17, 0, 18, 0);
		wsheet.addCell(new Label(19,0,"续页费", setCellFormat()));
		wsheet.mergeCells(19, 0, 20, 0);
		wsheet.addCell(new Label(21,0,"连柜费", setCellFormat()));
		wsheet.mergeCells(21, 0, 22, 0);
		wsheet.addCell(new Label(23,0,"拖车费", setCellFormat()));
		wsheet.mergeCells(23, 0, 24, 0);
		wsheet.addCell(new Label(25,0,"扫描费", setCellFormat()));
		wsheet.mergeCells(25, 0, 26, 0);
		wsheet.addCell(new Label(27,0,"查柜费", setCellFormat()));
		wsheet.mergeCells(27, 0, 28, 0);
		wsheet.addCell(new Label(29,0,"熏蒸费", setCellFormat()));
		wsheet.mergeCells(29, 0, 30, 0);
		wsheet.addCell(new Label(31,0,"加签", setCellFormat()));
		wsheet.mergeCells(31, 0, 32, 0);
		wsheet.addCell(new Label(33,0,"信用证费", setCellFormat()));
		wsheet.mergeCells(33, 0, 34, 0);
		wsheet.addCell(new Label(35,0,"产地证费", setCellFormat()));
		wsheet.mergeCells(35, 0, 36, 0);
		wsheet.addCell(new Label(37,0,"空白单证费", setCellFormat()));
		wsheet.mergeCells(37, 0, 38, 0);
		wsheet.addCell(new Label(39,0,"快递费", setCellFormat()));
		wsheet.mergeCells(39, 0, 40, 0);
		wsheet.addCell(new Label(41,0,"驳船费", setCellFormat()));
		wsheet.mergeCells(41, 0, 42, 0);
		wsheet.addCell(new Label(43,0,"封条费", setCellFormat()));
		wsheet.mergeCells(43, 0, 44, 0);
		wsheet.addCell(new Label(45,0,"仓单费", setCellFormat()));
		wsheet.mergeCells(45, 0, 46, 0);
		wsheet.addCell(new Label(47,0,"过磅费", setCellFormat()));
		wsheet.mergeCells(47, 0, 48, 0);
		wsheet.addCell(new Label(49,0,"换证凭条费", setCellFormat()));
		wsheet.mergeCells(49, 0, 50, 0);
		wsheet.addCell(new Label(51,0,"其他", setCellFormat()));
		wsheet.mergeCells(51, 0, 52, 0);
		wsheet.addCell(new Label(53,0,"合计", setCellFormat()));
		wsheet.mergeCells(53, 0, 54, 0);
		wsheet.addCell(new Label(55,0,"毛利", setCellFormat()));
		wsheet.mergeCells(55, 0, 55, 1);
		wsheet.addCell(new Label(56,0,"利润", setCellFormat()));
		wsheet.mergeCells(56, 0, 56, 1);
		wsheet.addCell(new Label(57,0,"业务员", setCellFormat()));
		wsheet.mergeCells(57, 0, 57, 1);
		wsheet.addCell(new Label(58,0,"代理费标准", setCellFormat()));
		wsheet.mergeCells(58, 0, 58, 1);
		
		wsheet.addCell(new Label(11,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(12,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(13,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(14,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(15,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(16,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(17,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(18,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(19,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(20,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(21,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(22,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(23,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(24,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(25,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(26,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(27,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(28,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(29,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(30,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(31,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(32,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(33,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(34,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(35,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(36,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(37,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(38,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(39,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(40,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(41,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(42,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(43,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(44,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(45,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(46,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(47,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(48,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(49,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(50,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(51,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(52,1,"成本", setCellFormat()));
		wsheet.addCell(new Label(53,1,"收入", setCellFormat()));
		wsheet.addCell(new Label(54,1,"成本", setCellFormat()));
		
		int i = 2;
		List<Map<String, Object>> list = getList(results);
		for(Map<String, Object> map : list){
			wsheet.addCell(new Label(0,i,(String)map.get("khmc"), setCellFormat()));
			wsheet.addCell(new Label(1,i,(String)map.get("gsmc"), setCellFormat()));
			wsheet.addCell(new Label(2,i,DateUtil.formatDate((Date)map.get("bgrq"), "yyyy-MM-dd"), setCellFormat()));
			wsheet.addCell(new Label(3,i,(String)map.get("bgdh"), setCellFormat()));
			wsheet.addCell(new Label(4,i,StringUtil.formatFloat((Float)map.get("bgje")), setCellFormat()));
			wsheet.addCell(new Label(5,i,StringUtil.formatFloat((Float)map.get("fpje")), setCellFormat()));
			wsheet.addCell(new Label(6,i,(String)map.get("sfprq"), setCellFormat()));
			wsheet.addCell(new Label(7,i,StringUtil.formatFloat((Float)map.get("tsje")), setCellFormat()));
			wsheet.addCell(new Label(8,i,(String)map.get("zftsrq"), setCellFormat()));
			wsheet.addCell(new Label(9,i,DateUtil.formatDate((Date)map.get("tsrq"), "yyyy-MM-dd"), setCellFormat()));
			wsheet.addCell(new Label(10,i,StringUtil.formatFloat((Float)map.get("sum_dlf")), setCellFormat()));
			
			wsheet.addCell(new Label(11,i,StringUtil.formatFloat((Float)map.get("sr_bgf")), setCellFormat()));
			wsheet.addCell(new Label(12,i,StringUtil.formatFloat((Float)map.get("cb_bgf")), setCellFormat()));
			wsheet.addCell(new Label(13,i,StringUtil.formatFloat((Float)map.get("sr_gjf")), setCellFormat()));
			wsheet.addCell(new Label(14,i,StringUtil.formatFloat((Float)map.get("cb_gjf")), setCellFormat()));
			wsheet.addCell(new Label(15,i,StringUtil.formatFloat((Float)map.get("sr_gj")), setCellFormat()));
			wsheet.addCell(new Label(16,i,StringUtil.formatFloat((Float)map.get("cb_gj")), setCellFormat()));
			wsheet.addCell(new Label(17,i,StringUtil.formatFloat((Float)map.get("sr_sjf")), setCellFormat()));
			wsheet.addCell(new Label(18,i,StringUtil.formatFloat((Float)map.get("cb_sjf")), setCellFormat()));
			wsheet.addCell(new Label(19,i,StringUtil.formatFloat((Float)map.get("sr_xyf")), setCellFormat()));
			wsheet.addCell(new Label(20,i,StringUtil.formatFloat((Float)map.get("cb_xyf")), setCellFormat()));
			wsheet.addCell(new Label(21,i,StringUtil.formatFloat((Float)map.get("sr_lgf")), setCellFormat()));
			wsheet.addCell(new Label(22,i,StringUtil.formatFloat((Float)map.get("cb_lgf")), setCellFormat()));
			wsheet.addCell(new Label(23,i,StringUtil.formatFloat((Float)map.get("sr_tcf")), setCellFormat()));
			wsheet.addCell(new Label(24,i,StringUtil.formatFloat((Float)map.get("cb_tcf")), setCellFormat()));
			wsheet.addCell(new Label(25,i,StringUtil.formatFloat((Float)map.get("sr_smf")), setCellFormat()));
			wsheet.addCell(new Label(26,i,StringUtil.formatFloat((Float)map.get("cb_smf")), setCellFormat()));
			wsheet.addCell(new Label(27,i,StringUtil.formatFloat((Float)map.get("sr_cgf")), setCellFormat()));
			wsheet.addCell(new Label(28,i,StringUtil.formatFloat((Float)map.get("cb_cgf")), setCellFormat()));
			wsheet.addCell(new Label(29,i,StringUtil.formatFloat((Float)map.get("sr_xzf")), setCellFormat()));
			wsheet.addCell(new Label(30,i,StringUtil.formatFloat((Float)map.get("cb_xzf")), setCellFormat()));
			wsheet.addCell(new Label(31,i,StringUtil.formatFloat((Float)map.get("sr_jq")), setCellFormat()));
			wsheet.addCell(new Label(32,i,StringUtil.formatFloat((Float)map.get("cb_jq")), setCellFormat()));
			wsheet.addCell(new Label(33,i,StringUtil.formatFloat((Float)map.get("sr_xyzf")), setCellFormat()));
			wsheet.addCell(new Label(34,i,StringUtil.formatFloat((Float)map.get("cb_xyzf")), setCellFormat()));
			wsheet.addCell(new Label(35,i,StringUtil.formatFloat((Float)map.get("sr_cdzf")), setCellFormat()));
			wsheet.addCell(new Label(36,i,StringUtil.formatFloat((Float)map.get("cb_cdzf")), setCellFormat()));
			wsheet.addCell(new Label(37,i,StringUtil.formatFloat((Float)map.get("sr_kbdzf")), setCellFormat()));
			wsheet.addCell(new Label(38,i,StringUtil.formatFloat((Float)map.get("cb_kbdzf")), setCellFormat()));
			wsheet.addCell(new Label(39,i,StringUtil.formatFloat((Float)map.get("sr_kdf")), setCellFormat()));
			wsheet.addCell(new Label(40,i,StringUtil.formatFloat((Float)map.get("cb_kdf")), setCellFormat()));
			wsheet.addCell(new Label(41,i,StringUtil.formatFloat((Float)map.get("sr_bcf")), setCellFormat()));
			wsheet.addCell(new Label(42,i,StringUtil.formatFloat((Float)map.get("cb_bcf")), setCellFormat()));
			wsheet.addCell(new Label(43,i,StringUtil.formatFloat((Float)map.get("sr_ftf")), setCellFormat()));
			wsheet.addCell(new Label(44,i,StringUtil.formatFloat((Float)map.get("cb_ftf")), setCellFormat()));
			wsheet.addCell(new Label(45,i,StringUtil.formatFloat((Float)map.get("sr_cdf")), setCellFormat()));
			wsheet.addCell(new Label(46,i,StringUtil.formatFloat((Float)map.get("cb_cdf")), setCellFormat()));
			wsheet.addCell(new Label(47,i,StringUtil.formatFloat((Float)map.get("sr_gpf")), setCellFormat()));
			wsheet.addCell(new Label(48,i,StringUtil.formatFloat((Float)map.get("cb_gpf")), setCellFormat()));
			wsheet.addCell(new Label(49,i,StringUtil.formatFloat((Float)map.get("sr_hzptf")), setCellFormat()));
			wsheet.addCell(new Label(50,i,StringUtil.formatFloat((Float)map.get("cb_hzptf")), setCellFormat()));
			wsheet.addCell(new Label(51,i,StringUtil.formatFloat((Float)map.get("sr_qt")), setCellFormat()));
			wsheet.addCell(new Label(52,i,StringUtil.formatFloat((Float)map.get("cb_qt")), setCellFormat()));
			wsheet.addCell(new Label(53,i,StringUtil.formatFloat((Float)map.get("sum_sr")), setCellFormat()));
			wsheet.addCell(new Label(54,i,StringUtil.formatFloat((Float)map.get("sum_cb")), setCellFormat()));
			
			wsheet.addCell(new Label(55,i,StringUtil.formatFloat((Float)map.get("ml")), setCellFormat()));
			wsheet.addCell(new Label(56,i,StringUtil.formatFloat((Float)map.get("lr")), setCellFormat()));
			wsheet.addCell(new Label(57,i,(String)map.get("ywy"), setCellFormat()));
			wsheet.addCell(new Label(58,i,(String)map.get("dlfbz"), setCellFormat()));
			
			i++;
		}
		
		Map<String, Object> map = getHj(list);
		wsheet.addCell(new Label(0,i,"合计", setCellFormat()));
		wsheet.mergeCells(0, i, 3, i);
		wsheet.addCell(new Label(4,i,StringUtil.formatFloat((Float)map.get("hj_bgje")), setCellFormat()));
		wsheet.addCell(new Label(5,i,StringUtil.formatFloat((Float)map.get("hj_fpje")), setCellFormat()));
		wsheet.addCell(new Label(6,i,"", setCellFormat()));
		wsheet.addCell(new Label(7,i,StringUtil.formatFloat((Float)map.get("hj_tsje")), setCellFormat()));
		wsheet.addCell(new Label(8,i,"", setCellFormat()));
		wsheet.addCell(new Label(9,i,"", setCellFormat()));
		wsheet.addCell(new Label(10,i,StringUtil.formatFloat((Float)map.get("hj_dlf")), setCellFormat()));
		wsheet.addCell(new Label(11,i,StringUtil.formatFloat((Float)map.get("hj_sr_bgf")), setCellFormat()));
		wsheet.addCell(new Label(12,i,StringUtil.formatFloat((Float)map.get("hj_cb_bgf")), setCellFormat()));
		wsheet.addCell(new Label(13,i,StringUtil.formatFloat((Float)map.get("hj_sr_gjf")), setCellFormat()));
		wsheet.addCell(new Label(14,i,StringUtil.formatFloat((Float)map.get("hj_cb_gjf")), setCellFormat()));
		wsheet.addCell(new Label(15,i,StringUtil.formatFloat((Float)map.get("hj_sr_gj")), setCellFormat()));
		wsheet.addCell(new Label(16,i,StringUtil.formatFloat((Float)map.get("hj_cb_gj")), setCellFormat()));
		wsheet.addCell(new Label(17,i,StringUtil.formatFloat((Float)map.get("hj_sr_sjf")), setCellFormat()));
		wsheet.addCell(new Label(18,i,StringUtil.formatFloat((Float)map.get("hj_cb_sjf")), setCellFormat()));
		wsheet.addCell(new Label(19,i,StringUtil.formatFloat((Float)map.get("hj_sr_xyf")), setCellFormat()));
		wsheet.addCell(new Label(20,i,StringUtil.formatFloat((Float)map.get("hj_cb_xyf")), setCellFormat()));
		wsheet.addCell(new Label(21,i,StringUtil.formatFloat((Float)map.get("hj_sr_lgf")), setCellFormat()));
		wsheet.addCell(new Label(22,i,StringUtil.formatFloat((Float)map.get("hj_cb_lgf")), setCellFormat()));
		wsheet.addCell(new Label(23,i,StringUtil.formatFloat((Float)map.get("hj_sr_tcf")), setCellFormat()));
		wsheet.addCell(new Label(24,i,StringUtil.formatFloat((Float)map.get("hj_cb_tcf")), setCellFormat()));
		wsheet.addCell(new Label(25,i,StringUtil.formatFloat((Float)map.get("hj_sr_smf")), setCellFormat()));
		wsheet.addCell(new Label(26,i,StringUtil.formatFloat((Float)map.get("hj_cb_smf")), setCellFormat()));
		wsheet.addCell(new Label(27,i,StringUtil.formatFloat((Float)map.get("hj_sr_cgf")), setCellFormat()));
		wsheet.addCell(new Label(28,i,StringUtil.formatFloat((Float)map.get("hj_cb_cgf")), setCellFormat()));
		wsheet.addCell(new Label(29,i,StringUtil.formatFloat((Float)map.get("hj_sr_xzf")), setCellFormat()));
		wsheet.addCell(new Label(30,i,StringUtil.formatFloat((Float)map.get("hj_cb_xzf")), setCellFormat()));
		wsheet.addCell(new Label(31,i,StringUtil.formatFloat((Float)map.get("hj_sr_jq")), setCellFormat()));
		wsheet.addCell(new Label(32,i,StringUtil.formatFloat((Float)map.get("hj_cb_jq")), setCellFormat()));
		wsheet.addCell(new Label(33,i,StringUtil.formatFloat((Float)map.get("hj_sr_xyzf")), setCellFormat()));
		wsheet.addCell(new Label(34,i,StringUtil.formatFloat((Float)map.get("hj_cb_xyzf")), setCellFormat()));
		wsheet.addCell(new Label(35,i,StringUtil.formatFloat((Float)map.get("hj_sr_cdzf")), setCellFormat()));
		wsheet.addCell(new Label(36,i,StringUtil.formatFloat((Float)map.get("hj_cb_cdzf")), setCellFormat()));
		wsheet.addCell(new Label(37,i,StringUtil.formatFloat((Float)map.get("hj_sr_kbdzf")), setCellFormat()));
		wsheet.addCell(new Label(38,i,StringUtil.formatFloat((Float)map.get("hj_cb_kbdzf")), setCellFormat()));
		wsheet.addCell(new Label(39,i,StringUtil.formatFloat((Float)map.get("hj_sr_kdf")), setCellFormat()));
		wsheet.addCell(new Label(40,i,StringUtil.formatFloat((Float)map.get("hj_cb_kdf")), setCellFormat()));
		wsheet.addCell(new Label(41,i,StringUtil.formatFloat((Float)map.get("hj_sr_bcf")), setCellFormat()));
		wsheet.addCell(new Label(42,i,StringUtil.formatFloat((Float)map.get("hj_cb_bcf")), setCellFormat()));
		wsheet.addCell(new Label(43,i,StringUtil.formatFloat((Float)map.get("hj_sr_ftf")), setCellFormat()));
		wsheet.addCell(new Label(44,i,StringUtil.formatFloat((Float)map.get("hj_cb_ftf")), setCellFormat()));
		wsheet.addCell(new Label(45,i,StringUtil.formatFloat((Float)map.get("hj_sr_cdf")), setCellFormat()));
		wsheet.addCell(new Label(46,i,StringUtil.formatFloat((Float)map.get("hj_cb_cdf")), setCellFormat()));
		wsheet.addCell(new Label(47,i,StringUtil.formatFloat((Float)map.get("hj_sr_gpf")), setCellFormat()));
		wsheet.addCell(new Label(48,i,StringUtil.formatFloat((Float)map.get("hj_cb_gpf")), setCellFormat()));
		wsheet.addCell(new Label(49,i,StringUtil.formatFloat((Float)map.get("hj_sr_hzptf")), setCellFormat()));
		wsheet.addCell(new Label(50,i,StringUtil.formatFloat((Float)map.get("hj_cb_hzptf")), setCellFormat()));
		wsheet.addCell(new Label(51,i,StringUtil.formatFloat((Float)map.get("hj_sr_qt")), setCellFormat()));
		wsheet.addCell(new Label(52,i,StringUtil.formatFloat((Float)map.get("hj_cb_qt")), setCellFormat()));
		wsheet.addCell(new Label(53,i,StringUtil.formatFloat((Float)map.get("hj_sum_sr")), setCellFormat()));
		wsheet.addCell(new Label(54,i,StringUtil.formatFloat((Float)map.get("hj_sum_cb")), setCellFormat()));
		wsheet.addCell(new Label(55,i,StringUtil.formatFloat((Float)map.get("hj_ml")), setCellFormat()));
		wsheet.addCell(new Label(56,i,StringUtil.formatFloat((Float)map.get("hj_lr")), setCellFormat()));
		wsheet.addCell(new Label(57,i,"", setCellFormat()));
		wsheet.addCell(new Label(58,i,"", setCellFormat()));
		
		wwb.write();
		wwb.close();		
		os.close(); 
		return null;
	}
	
	private List<Map<String, Object>> getList(List<Map<String, Object>> results){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			
		for(Map<String, Object> map : results){
			Criteria fkzfCriteria = fkzfService.createCriteria(Fkzf.class);
			Criteria khqkCriteria = khqkService.createCriteria(Khqk.class);
			Criteria tczcCriteria = tczcService.createCriteria(Tczc.class);
			
			Map<String, Object> m = new HashMap<String, Object>();
			String bgdh = (String)map.get("bgdh");
			m.put("dh",map.get("dh"));
			m.put("khmc", map.get("khmc"));
			m.put("bgrq", map.get("bgrq"));
			m.put("bgdh", bgdh);
			m.put("bgje", map.get("bgje"));
			m.put("fpje", map.get("fpje"));
			m.put("sfprq", map.get("sfprq"));
			m.put("tsje", map.get("tsje"));
			m.put("zftsrq", map.get("zftsrq"));
			m.put("ywy", map.get("ywy"));
			m.put("dlfbz", map.get("dlfbz"));
			
			Gjts gjts = gjtsService.findUniqueBy("bgdh", bgdh);
			m.put("tsrq", gjts!=null&&gjts.getTsrq()!=null&&!"null".equals(gjts.getTsrq())?gjts.getTsrq():null);
							
			khqkCriteria.add(Expression.eq("bgdh", bgdh));
			Float sum_dlf = 0f;
			float sr_bgf = 0;
			float sr_gjf = 0;
			float sr_sjf = 0;
			float sr_gj = 0;
			float sr_xyf = 0;
			float sr_lgf = 0;
			float sr_tcf = 0;
			float sr_smf = 0;
			float sr_cgf = 0;
			float sr_xzf = 0;
			float sr_jq = 0;
			float sr_qt = 0;
			float sr_xyzf = 0;
			float sr_cdzf = 0;
			float sr_kbdzf = 0;
			float sr_kdf = 0;
			float sr_bcf = 0;
			float sr_ftf = 0;
			float sr_cdf = 0;
			float sr_gpf = 0;
			float sr_hzptf = 0;
			float sum_sr = 0;
			if(khqkCriteria.list().size() > 0){
				Khqk khqk = (Khqk)khqkCriteria.list().get(0);
//				sum_dlf = (Float)khqkCriteria.setProjection(Projections.sum("dlf")).uniqueResult(); //代理
//				sr_bgf = (Float)khqkCriteria.setProjection(Projections.sum("bgf")).uniqueResult(); //报关费收入
//				sr_gjf = (Float)khqkCriteria.setProjection(Projections.sum("gjf")).uniqueResult(); //港建费收入
//				sr_sjf = (Float)khqkCriteria.setProjection(Projections.sum("sjf")).uniqueResult(); //商检费收入
//				sr_gj = (Float)khqkCriteria.setProjection(Projections.sum("gj")).uniqueResult(); //国检收入
//				sr_xyf = (Float)khqkCriteria.setProjection(Projections.sum("xyf")).uniqueResult(); //续页费收入
//				sr_lgf = (Float)khqkCriteria.setProjection(Projections.sum("lgf")).uniqueResult(); //连柜费收入
//				sr_tcf = (Float)khqkCriteria.setProjection(Projections.sum("tcf")).uniqueResult(); //拖车费收入
//				sr_smf = (Float)khqkCriteria.setProjection(Projections.sum("smf")).uniqueResult(); //扫苗费收入
//				sr_cgf = (Float)khqkCriteria.setProjection(Projections.sum("cgf")).uniqueResult(); //查柜费收入
//				sr_xzf = (Float)khqkCriteria.setProjection(Projections.sum("xzf")).uniqueResult(); //熏蒸费收入
//				sr_jq = (Float)khqkCriteria.setProjection(Projections.sum("jq")).uniqueResult(); //加签收入
//				sr_xyzf = (Float)khqkCriteria.setProjection(Projections.sum("xyzf")).uniqueResult(); //信用证费收入
//				sr_cdzf = (Float)khqkCriteria.setProjection(Projections.sum("cdzf")).uniqueResult(); //产地证费收入
//				sr_kbdzf = (Float)khqkCriteria.setProjection(Projections.sum("kbdzf")).uniqueResult(); //空白单证费收入
//				sr_kdf = (Float)khqkCriteria.setProjection(Projections.sum("kdf")).uniqueResult(); //快递费收入
//				sr_bcf = (Float)khqkCriteria.setProjection(Projections.sum("bcf")).uniqueResult(); //驳船费收入
//				sr_ftf = (Float)khqkCriteria.setProjection(Projections.sum("ftf")).uniqueResult(); //封条费收入
//				sr_cdf = (Float)khqkCriteria.setProjection(Projections.sum("cdf")).uniqueResult(); //仓单费收入
//				sr_gpf = (Float)khqkCriteria.setProjection(Projections.sum("gpf")).uniqueResult(); //过磅费收入
//				sr_hzptf = (Float)khqkCriteria.setProjection(Projections.sum("hzptf")).uniqueResult(); //换证凭条费收入
//				sr_qt = (Float)khqkCriteria.setProjection(Projections.sum("qt")).uniqueResult(); //其他收入
				sum_dlf = khqk.getDlf();
				sr_bgf = khqk.getBgf();
				sr_gjf = khqk.getGjf();
				sr_sjf = khqk.getSjf();
				sr_gj = khqk.getGj();
				sr_xyf = khqk.getXyf();
				sr_lgf = khqk.getLgf();
				sr_tcf = khqk.getTcf();
				sr_smf = khqk.getSmf();
				sr_cgf = khqk.getCgf();
				sr_xzf = khqk.getXzf();
				sr_jq = khqk.getJq();
				sr_xyzf = khqk.getXyzf();
				sr_cdzf = khqk.getCdzf();
				sr_kbdzf = khqk.getKbdzf();
				sr_kdf = khqk.getKdf();
				sr_bcf = khqk.getBcf();
				sr_ftf = khqk.getFtf();
				sr_cdf = khqk.getCdf();
				sr_gpf = khqk.getGpf();
				sr_hzptf = khqk.getHzptf();
				sr_qt = khqk.getQt();
				sum_sr = khqk.getHj();
			}
			m.put("sum_dlf", sum_dlf);			
			m.put("sr_bgf", sr_bgf);
			m.put("sr_gjf", sr_gjf);			
			m.put("sr_sjf", sr_sjf);		
			m.put("sr_gj", sr_gj);		
			m.put("sr_xyf", sr_xyf);			
			m.put("sr_lgf", sr_lgf);		
			m.put("sr_tcf", sr_tcf);			
			m.put("sr_smf", sr_smf);		
			m.put("sr_cgf", sr_cgf);		
			m.put("sr_xzf", sr_xzf);		
			m.put("sr_jq", sr_jq);	
			m.put("sr_xyzf", sr_xyzf);
			m.put("sr_cdzf", sr_cdzf);
			m.put("sr_kbdzf", sr_kbdzf);
			m.put("sr_kdf", sr_kdf);
			m.put("sr_bcf", sr_bcf);
			m.put("sr_ftf", sr_ftf);
			m.put("sr_cdf", sr_cdf);
			m.put("sr_gpf", sr_gpf);
			m.put("sr_hzptf", sr_hzptf);
			m.put("sr_qt", sr_qt);
			m.put("sum_sr", sum_sr); //收入总和
			
			float sum_cb = 0f;
			fkzfCriteria.add(Expression.eq("bgdh", bgdh));
			String gsmc = "";
			float cb_bgf = 0;
			float cb_gjf = 0;
			float cb_sjf = 0;
			float cb_gj = 0;
			float cb_xyf = 0;
			float cb_lgf = 0;
			float cb_smf = 0;
			float cb_cgf = 0;
			float cb_xzf = 0;
			float cb_jq = 0;
			float cb_qt = 0;
			float cb_xyzf = 0;
			float cb_cdzf = 0;
			float cb_kbdzf = 0;
			float cb_kdf = 0;
			float cb_bcf = 0;
			float cb_ftf = 0;
			float cb_cdf = 0;
			float cb_gpf = 0;
			float cb_hzptf = 0;
			if(fkzfCriteria.list().size() >0){
				Fkzf fkzf = (Fkzf)fkzfCriteria.list().get(0);
				gsmc = (String)map.get("gsmc");
//				cb_bgf = (Float)fkzfCriteria.setProjection(Projections.sum("bgf")).uniqueResult(); //报关费成本
//				cb_gjf = (Float)fkzfCriteria.setProjection(Projections.sum("gjf")).uniqueResult(); //港建费成本
//				cb_sjf = (Float)fkzfCriteria.setProjection(Projections.sum("sjf")).uniqueResult(); //商检费成本
//				cb_gj = (Float)fkzfCriteria.setProjection(Projections.sum("gj")).uniqueResult(); //国检成本
//				cb_xyf = (Float)fkzfCriteria.setProjection(Projections.sum("xyf")).uniqueResult(); //续页费成本
//				cb_lgf = (Float)fkzfCriteria.setProjection(Projections.sum("lgf")).uniqueResult(); //连柜费成本
//				cb_smf = (Float)fkzfCriteria.setProjection(Projections.sum("smf")).uniqueResult(); //扫苗费成本
//				cb_cgf = (Float)fkzfCriteria.setProjection(Projections.sum("cgf")).uniqueResult(); //查柜费成本
//				cb_xzf = (Float)fkzfCriteria.setProjection(Projections.sum("xzf")).uniqueResult(); //熏蒸费成本
//				cb_jq = (Float)fkzfCriteria.setProjection(Projections.sum("jq")).uniqueResult(); //加签成本
//				cb_xyzf = (Float)fkzfCriteria.setProjection(Projections.sum("xyzf")).uniqueResult(); //信用证费成本
//				cb_cdzf = (Float)fkzfCriteria.setProjection(Projections.sum("cdzf")).uniqueResult(); //产地证费成本
//				cb_kbdzf = (Float)fkzfCriteria.setProjection(Projections.sum("kbdzf")).uniqueResult(); //空白单证费成本
//				cb_kdf = (Float)fkzfCriteria.setProjection(Projections.sum("kdf")).uniqueResult(); //快递费成本
//				cb_bcf = (Float)fkzfCriteria.setProjection(Projections.sum("bcf")).uniqueResult(); //驳船费成本
//				cb_ftf = (Float)fkzfCriteria.setProjection(Projections.sum("ftf")).uniqueResult(); //封条成本
//				cb_cdf = (Float)fkzfCriteria.setProjection(Projections.sum("cdf")).uniqueResult(); //仓单费成本
//				cb_gpf = (Float)fkzfCriteria.setProjection(Projections.sum("gpf")).uniqueResult(); //过磅费成本
//				cb_hzptf = (Float)fkzfCriteria.setProjection(Projections.sum("hzptf")).uniqueResult(); //换证凭条费成本
//				cb_qt = (Float)fkzfCriteria.setProjection(Projections.sum("qt")).uniqueResult(); //其他成本
				cb_bgf = fkzf.getBgf();
				cb_gjf = fkzf.getGjf();
				cb_sjf = fkzf.getSjf();
				cb_gj = fkzf.getGj();
				cb_xyf = fkzf.getXyf();
				cb_lgf = fkzf.getLgf();
				cb_smf = fkzf.getSmf();
				cb_cgf = fkzf.getCgf();
				cb_xzf = fkzf.getXzf();
				cb_jq = fkzf.getJq();
				cb_xyzf = fkzf.getXyzf();
				cb_cdzf = fkzf.getCdzf();
				cb_kbdzf = fkzf.getKbdzf();
				cb_kdf = fkzf.getKdf();
				cb_bcf = fkzf.getBcf();
				cb_ftf = fkzf.getFtf();
				cb_cdf = fkzf.getCdf();
				cb_gpf = fkzf.getGpf();
				cb_hzptf = fkzf.getHzptf();
				cb_qt = fkzf.getQt();
				sum_cb = fkzf.getHj();
			}
			m.put("gsmc", gsmc);
			m.put("cb_bgf", cb_bgf);			
			m.put("cb_gjf", cb_gjf);			
			m.put("cb_sjf", cb_sjf);		
			m.put("cb_gj", cb_gj);
			m.put("cb_xyf", cb_xyf);
			m.put("cb_lgf", cb_lgf);
			m.put("cb_smf", cb_smf);
			m.put("cb_cgf", cb_cgf);
			m.put("cb_xzf", cb_xzf);
			m.put("cb_jq", cb_jq);
			m.put("cb_xyzf", cb_xyzf);
			m.put("cb_cdzf", cb_cdzf);
			m.put("cb_kbdzf", cb_kbdzf);
			m.put("cb_kdf", cb_kdf);
			m.put("cb_bcf", cb_bcf);
			m.put("cb_ftf", cb_ftf);
			m.put("cb_cdf", cb_cdf);
			m.put("cb_gpf", cb_gpf);
			m.put("cb_hzptf", cb_hzptf);
			m.put("cb_qt", cb_qt);
			
			//float cb_tcf = (Float)fkzfCriteria.setProjection(Projections.sum("tcf")).uniqueResult(); //拖车费成本
			tczcCriteria.add(Expression.eq("bgdh", bgdh));
			float cb_tcf = 0; //拖车费成本
			if(tczcCriteria.list().size() > 0){
				//cb_tcf = (Float)tczcCriteria.setProjection(Projections.sum("fyjehj")).uniqueResult();
				Tczc tczc = (Tczc)tczcCriteria.list().get(0);
				cb_tcf = tczc.getFyjehj();
				sum_cb += cb_tcf;
			}
			m.put("cb_tcf", cb_tcf);
			m.put("sum_cb", sum_cb); //成本总和
			
			float ml = sum_sr - sum_cb; //毛利=收入总和-成本总和
			m.put("ml", ml);
			m.put("lr", ml + sum_dlf); //利润=毛利+代理费
			
			list.add(m);
		}
		
		return list;
	}
	
	private Map<String, Object> getHj(List<Map<String, Object>> list){
		Map<String, Object> map = new HashMap<String, Object>();
		
		float hj_bgje = 0, hj_fpje = 0, hj_tsje = 0, hj_dlf = 0,hj_sr_bgf = 0, hj_cb_bgf = 0, hj_sr_gjf = 0, hj_cb_gjf = 0, hj_sr_gj = 0, hj_cb_gj = 0;
		float hj_sr_sjf = 0, hj_cb_sjf = 0, hj_sr_xyf = 0, hj_cb_xyf = 0, hj_sr_lgf = 0, hj_cb_lgf = 0, hj_sr_tcf = 0, hj_cb_tcf = 0, hj_sr_smf = 0;
		float hj_cb_smf = 0, hj_sr_cgf = 0, hj_cb_cgf = 0, hj_sr_xzf = 0, hj_cb_xzf = 0, hj_sr_jq = 0, hj_cb_jq = 0, hj_sr_xyzf = 0, hj_cb_xyzf = 0;
		float hj_sr_cdzf = 0, hj_cb_cdzf = 0, hj_sr_kbdzf = 0, hj_cb_kbdzf = 0, hj_sr_kdf = 0, hj_cb_kdf = 0, hj_sr_bcf = 0, hj_cb_bcf = 0, hj_sr_ftf = 0;
		float hj_cb_ftf = 0, hj_sr_cdf = 0, hj_cb_cdf = 0, hj_sr_gpf = 0, hj_cb_gpf = 0, hj_sr_hzptf = 0, hj_cb_hzptf = 0, hj_sr_qt = 0, hj_cb_qt = 0;
		float hj_sum_sr = 0, hj_sum_cb = 0, hj_ml = 0, hj_lr = 0;
		for(Map<String, Object> m : list){				
			hj_bgje += (Float)m.get("bgje");
			map.put("hj_bgje", hj_bgje);
			hj_fpje += (Float)m.get("fpje");
			map.put("hj_fpje", hj_fpje);
			hj_tsje += (Float)m.get("tsje");
			map.put("hj_tsje", hj_tsje);
			hj_dlf += (Float)m.get("sum_dlf");
			map.put("hj_dlf", hj_dlf);
			hj_sr_bgf += (Float)m.get("sr_bgf");
			map.put("hj_sr_bgf", hj_sr_bgf);
			hj_cb_bgf += (Float)m.get("cb_bgf");
			map.put("hj_cb_bgf", hj_cb_bgf);
			hj_sr_gjf += (Float)m.get("sr_gjf");
			map.put("hj_sr_gjf", hj_sr_gjf);
			hj_cb_gjf += (Float)m.get("cb_gjf");
			map.put("hj_cb_gjf", hj_cb_gjf);
			hj_sr_gj += (Float)m.get("sr_gj");
			map.put("hj_sr_gj", hj_sr_gj);
			hj_cb_gj += (Float)m.get("cb_gj");
			map.put("hj_cb_gj", hj_cb_gj);
			hj_sr_sjf += (Float)m.get("sr_sjf");
			map.put("hj_sr_sjf", hj_sr_sjf);
			hj_cb_sjf += (Float)m.get("cb_sjf");
			map.put("hj_cb_sjf", hj_cb_sjf);
			hj_sr_xyf += (Float)m.get("sr_xyf");
			map.put("hj_sr_xyf", hj_sr_xyf);
			hj_cb_xyf += (Float)m.get("cb_xyf");
			map.put("hj_cb_xyf", hj_cb_xyf);
			hj_sr_lgf += (Float)m.get("sr_lgf");
			map.put("hj_sr_lgf", hj_sr_lgf);
			hj_cb_lgf += (Float)m.get("cb_lgf");
			map.put("hj_cb_lgf", hj_cb_lgf);
			hj_sr_tcf += (Float)m.get("sr_tcf");
			map.put("hj_sr_tcf", hj_sr_tcf);
			hj_cb_tcf += (Float)m.get("cb_tcf");
			map.put("hj_cb_tcf", hj_cb_tcf);
			hj_sr_smf += (Float)m.get("sr_smf");
			map.put("hj_sr_smf", hj_sr_smf);
			hj_cb_smf += (Float)m.get("cb_smf");
			map.put("hj_cb_smf", hj_cb_smf);
			hj_sr_cgf += (Float)m.get("sr_cgf");
			map.put("hj_sr_cgf", hj_sr_cgf);
			hj_cb_cgf += (Float)m.get("cb_cgf");
			map.put("hj_cb_cgf", hj_cb_cgf);
			hj_sr_xzf += (Float)m.get("sr_xzf");
			map.put("hj_sr_xzf", hj_sr_xzf);
			hj_cb_xzf += (Float)m.get("cb_xzf");
			map.put("hj_cb_xzf", hj_cb_xzf);
			hj_sr_jq += (Float)m.get("sr_jq");
			map.put("hj_sr_jq", hj_sr_jq);
			hj_cb_jq += (Float)m.get("cb_jq");
			map.put("hj_cb_jq", hj_cb_jq);
			hj_sr_xyzf += (Float)m.get("sr_xyzf");
			map.put("hj_sr_xyzf", hj_sr_xyzf);
			hj_cb_xyzf += (Float)m.get("cb_xyzf");
			map.put("hj_cb_xyzf", hj_cb_xyzf);
			hj_sr_cdzf += (Float)m.get("sr_cdzf");
			map.put("hj_sr_cdzf", hj_sr_cdzf);
			hj_cb_cdzf += (Float)m.get("cb_cdzf");
			map.put("hj_cb_cdzf", hj_cb_cdzf);
			hj_sr_kbdzf += (Float)m.get("sr_kbdzf");
			map.put("hj_sr_kbdzf", hj_sr_kbdzf);
			hj_cb_kbdzf += (Float)m.get("cb_kbdzf");
			map.put("hj_cb_kbdzf", hj_cb_kbdzf);
			hj_sr_kdf += (Float)m.get("sr_kdf");
			map.put("hj_sr_kdf", hj_sr_kdf);
			hj_cb_kdf += (Float)m.get("cb_kdf");
			map.put("hj_cb_kdf", hj_cb_kdf);
			hj_sr_bcf += (Float)m.get("sr_bcf");
			map.put("hj_sr_bcf", hj_sr_bcf);
			hj_cb_bcf += (Float)m.get("cb_bcf");
			map.put("hj_cb_bcf", hj_cb_bcf);
			hj_sr_ftf += (Float)m.get("sr_ftf");
			map.put("hj_sr_ftf", hj_sr_ftf);
			hj_cb_ftf += (Float)m.get("cb_ftf");
			map.put("hj_cb_ftf", hj_cb_ftf);
			hj_sr_cdf += (Float)m.get("sr_cdf");
			map.put("hj_sr_cdf", hj_sr_cdf);
			hj_cb_cdf += (Float)m.get("cb_cdf");
			map.put("hj_cb_cdf", hj_cb_cdf);
			hj_sr_gpf += (Float)m.get("sr_gpf");
			map.put("hj_sr_gpf", hj_sr_gpf);
			hj_cb_gpf += (Float)m.get("cb_gpf");
			map.put("hj_cb_gpf", hj_cb_gpf);
			hj_sr_hzptf += (Float)m.get("sr_hzptf");
			map.put("hj_sr_hzptf", hj_sr_hzptf);
			hj_cb_hzptf += (Float)m.get("cb_hzptf");
			map.put("hj_cb_hzptf", hj_cb_hzptf);
			hj_sr_qt += (Float)m.get("sr_qt");
			map.put("hj_sr_qt", hj_sr_qt);
			hj_cb_qt += (Float)m.get("cb_qt");
			map.put("hj_cb_qt", hj_cb_qt);
			hj_sum_sr += (Float)m.get("sum_sr");
			map.put("hj_sum_sr", hj_sum_sr);
			hj_sum_cb +=(Float)m.get("sum_cb");
			map.put("hj_sum_cb", hj_sum_cb);
			hj_ml += (Float)m.get("ml");
			map.put("hj_ml", hj_ml);
			hj_lr += (Float)m.get("lr");
			map.put("hj_lr", hj_lr);
		}
		
		return map;
	}
	
	private WritableCellFormat setCellFormat() throws WriteException{
		WritableCellFormat cell = new WritableCellFormat(new WritableFont(WritableFont.createFont("宋体"),10,WritableFont.NO_BOLD));
		cell.setAlignment(jxl.format.Alignment.CENTRE);
		cell.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		
		return cell;
		
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
	public ModelAndView show(HttpServletRequest request,
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
	public ModelAndView update(HttpServletRequest request,
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

	public void setKhtsService(KhtsService khtsService) {
		this.khtsService = khtsService;
	}

	public void setGjtsService(GjtsService gjtsService) {
		this.gjtsService = gjtsService;
	}

	public void setFkzfService(FkzfService fkzfService) {
		this.fkzfService = fkzfService;
	}

	public void setKhqkService(KhqkService khqkService) {
		this.khqkService = khqkService;
	}

	public void setTczcService(TczcService tczcService) {
		this.tczcService = tczcService;
	}

}
