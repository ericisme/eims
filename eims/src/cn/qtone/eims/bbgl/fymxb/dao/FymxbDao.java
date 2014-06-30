package cn.qtone.eims.bbgl.fymxb.dao;


import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;


import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cn.qtone.common.mvc.dao.BaseDAO;
import cn.qtone.eims.bbgl.fymxb.domain.Fymxb;
import cn.qtone.eims.bbgl.fymxb.util.JXLReadExcel2;
import cn.qtone.eims.fymx.cwfy.domain.Cwfy;
import cn.qtone.eims.fymx.gdzc.domain.Gdzc;
import cn.qtone.eims.fymx.glfymx.domain.Glfymx;
import cn.qtone.eims.fymx.glfymx.service.GlfymxService;
import cn.qtone.eims.fymx.sds.domain.Sds;
import cn.qtone.eims.fymx.yggz.domain.Yggz;

public class FymxbDao extends BaseDAO {

	public List<Fymxb> getFymxbTotalListGroupByMonth(String ksrq, String jsrq) {
		
		
		String sql = "select "+
"'Total' as rq, "+
"sum(gz.gz) as gz,   "+
"sum(sbf.je) as sbf, "+
"sum(zjf.je) as zjf, "+
"sum(sdf.je) as sdf, "+
" "+
"sum(dhf.je) as dhf, "+
"sum(bgf.je) as bgf, "+
"sum(clf.je) as clf, "+
"sum(qyf.je) as qyf, "+
"sum(twf.je) as twf, "+
"sum(kdf.je) as kdf, "+
"sum(yhs.je) as yhs, "+
"sum(zj.je) as zj, "+
"sum(zcf.je) as zcf, "+
"sum(jzf.je) as jzf, "+
"sum(qt.je) as qt, "+
"sum(dklx.je) as dklx, "+
"sum(cklx.je) as cklx, "+
"sum(yhsxf.je) as yhsxf, "+
"sum(hdsy.je) as hdsy, "+
"sum(yywzc.je) as yywzc, "+
"sum(sds.sds) as sds "+

" "+
" from "+
" "+
"( "+
"select rq.rq as rq "+
"from eims_rq rq "+
"where rq.rq>='" + ksrq + "' and rq.rq<='" + jsrq +"' "+
") rq "+
" "+
"left join  "+
"( "+
"select sum(yggz.yfgz) as gz,left(yggz.gzrq,7) as rq from eims_yggz yggz  "+
"where left(yggz.gzrq,7)>='" + ksrq + "' and left(yggz.gzrq,7)<='" + jsrq +"' "+
"GROUP BY left(yggz.gzrq,7) "+
") gz on rq.rq=gz.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3001'"+
"group by left(glfy.fyrq,7) "+
") sbf on rq.rq=sbf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3014' "+
"group by left(glfy.fyrq,7) "+
") zjf on rq.rq=zjf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3002' "+
"group by left(glfy.fyrq,7) "+
") sdf on rq.rq=sdf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3003' "+
"group by left(glfy.fyrq,7) "+
") dhf on rq.rq=dhf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3004' "+
"group by left(glfy.fyrq,7) "+
") bgf on rq.rq=bgf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3005' "+
"group by left(glfy.fyrq,7) "+
") clf on rq.rq=clf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3006' "+
"group by left(glfy.fyrq,7) "+
") qyf on rq.rq=qyf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3007' "+
"group by left(glfy.fyrq,7) "+
") twf on rq.rq=twf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3008' "+
"group by left(glfy.fyrq,7) "+
") kdf on rq.rq=kdf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3009' "+
"group by left(glfy.fyrq,7) "+
") yhs on rq.rq=yhs.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3010' "+
"group by left(glfy.fyrq,7) "+
") zj on rq.rq=zj.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3011' "+
"group by left(glfy.fyrq,7) "+
") zcf on rq.rq=zcf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3012' "+
"group by left(glfy.fyrq,7) "+
") jzf on rq.rq=jzf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3013' "+
"group by left(glfy.fyrq,7) "+
") qt on rq.rq=qt.rq "+
		
"		left join  " +
"		( "+
"		select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"		where type='101' "+
"		group by left(glfy.fyrq,7) "+
"		) dklx on rq.rq=dklx.rq "+
"		left join  "+
"		( "+
"		select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"		where type='102' "+
"		group by left(glfy.fyrq,7) "+
"		) cklx on rq.rq=cklx.rq "+
"		left join  "+
"		( "+
"		select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"		where type='103' "+
"		group by left(glfy.fyrq,7) "+
"		) yhsxf on rq.rq=yhsxf.rq "+
"		left join  "+
"		( "+
"		select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"		where type='104' "+
"		group by left(glfy.fyrq,7) "+
"		) hdsy on rq.rq=hdsy.rq "+
"		left join  "+
"		( "+
"		select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"		where type='201' "+
"		group by left(glfy.fyrq,7) "+
"		) yywzc on rq.rq=yywzc.rq "+
"		left join  "+
"		( "+
"		select sum(sds.je) as sds,left(sds.fyrq,7) as rq from eims_sds sds  "+
"		GROUP BY left(sds.fyrq,7) "+
"		) sds on rq.rq=sds.rq		 ";
		
		
		//System.out.println("sql:"+sql);
		
		
		//getSimpleJdbcTemplate().queryForList(" select * from eims_yggz ");
		
		List<Fymxb> list = this.getJdbcTemplate().query(sql, new BaseParameterizedRowMapper<Fymxb>(Fymxb.class));
			return list;
			//return null;
	}
	
	public List<Fymxb> getFymxbListGroupByMonth(String ksrq, String jsrq) {
		
		
		String sql = "select "+
"rq.rq as rq, "+
"gz.gz as gz,   "+
"sbf.je as sbf, "+
"zjf.je as zjf, "+
"sdf.je as sdf, "+
" "+
"dhf.je as dhf, "+
"bgf.je as bgf, "+
"clf.je as clf, "+
"qyf.je as qyf, "+
"twf.je as twf, "+
"kdf.je as kdf, "+
"yhs.je as yhs, "+
"zj.je as zj, "+
"zcf.je as zcf, "+
"jzf.je as jzf, "+
"qt.je as qt, "+
"dklx.je as dklx, "+
"cklx.je as cklx, "+
"yhsxf.je as yhsxf, "+
"hdsy.je as hdsy, "+
"yywzc.je as yywzc, "+
"sds.sds as sds "+

" "+
" from "+
" "+
"( "+
"select rq.rq as rq "+
"from eims_rq rq "+
"where rq.rq>='" + ksrq + "' and rq.rq<='" + jsrq +"' "+
") rq "+
" "+
"left join  "+
"( "+
"select sum(yggz.yfgz) as gz,left(yggz.gzrq,7) as rq from eims_yggz yggz  "+
"where left(yggz.gzrq,7)>='" + ksrq + "' and left(yggz.gzrq,7)<='" + jsrq +"' "+
"GROUP BY left(yggz.gzrq,7) "+
") gz on rq.rq=gz.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3001'"+
"group by left(glfy.fyrq,7) "+
") sbf on rq.rq=sbf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3014' "+
"group by left(glfy.fyrq,7) "+
") zjf on rq.rq=zjf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3002' "+
"group by left(glfy.fyrq,7) "+
") sdf on rq.rq=sdf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3003' "+
"group by left(glfy.fyrq,7) "+
") dhf on rq.rq=dhf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3004' "+
"group by left(glfy.fyrq,7) "+
") bgf on rq.rq=bgf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3005' "+
"group by left(glfy.fyrq,7) "+
") clf on rq.rq=clf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3006' "+
"group by left(glfy.fyrq,7) "+
") qyf on rq.rq=qyf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3007' "+
"group by left(glfy.fyrq,7) "+
") twf on rq.rq=twf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3008' "+
"group by left(glfy.fyrq,7) "+
") kdf on rq.rq=kdf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3009' "+
"group by left(glfy.fyrq,7) "+
") yhs on rq.rq=yhs.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3010' "+
"group by left(glfy.fyrq,7) "+
") zj on rq.rq=zj.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3011' "+
"group by left(glfy.fyrq,7) "+
") zcf on rq.rq=zcf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3012' "+
"group by left(glfy.fyrq,7) "+
") jzf on rq.rq=jzf.rq "+
" "+
"left join  "+
"( "+
"select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"where type='3013' "+
"group by left(glfy.fyrq,7) "+
") qt on rq.rq=qt.rq "+
		
"		left join  " +
"		( "+
"		select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"		where type='101' "+
"		group by left(glfy.fyrq,7) "+
"		) dklx on rq.rq=dklx.rq "+
"		left join  "+
"		( "+
"		select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"		where type='102' "+
"		group by left(glfy.fyrq,7) "+
"		) cklx on rq.rq=cklx.rq "+
"		left join  "+
"		( "+
"		select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"		where type='103' "+
"		group by left(glfy.fyrq,7) "+
"		) yhsxf on rq.rq=yhsxf.rq "+
"		left join  "+
"		( "+
"		select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"		where type='104' "+
"		group by left(glfy.fyrq,7) "+
"		) hdsy on rq.rq=hdsy.rq "+
"		left join  "+
"		( "+
"		select sum(glfy.je) as je,left(glfy.fyrq,7) as rq from eims_cwfy glfy "+
"		where type='201' "+
"		group by left(glfy.fyrq,7) "+
"		) yywzc on rq.rq=yywzc.rq "+
"		left join  "+
"		( "+
"		select sum(sds.je) as sds,left(sds.fyrq,7) as rq from eims_sds sds  "+
"		GROUP BY left(sds.fyrq,7) "+
"		) sds on rq.rq=sds.rq		 ";
		
		
		//System.out.println("sql:"+sql);
		
		
		//getSimpleJdbcTemplate().queryForList(" select * from eims_yggz ");
		
		List<Fymxb> list = this.getJdbcTemplate().query(sql, new BaseParameterizedRowMapper<Fymxb>(Fymxb.class));
			return list;
			//return null;
	}
	
	/**
	 * 打印xls报表                                                                                          1行
	 * xlsOrHtml 1为输入 xls文件，2为输出html文本。
	 * @throws Exception 
	 */
	public String getXlsBbByFymxbList(List<Fymxb> _f,OutputStream os) throws Exception{
		WritableWorkbook wwb = Workbook.createWorkbook(os);
		
		
		WritableSheet wsheet = wwb.createSheet("费用明细表", 0); // sheet名称
		
		wsheet.getSettings().setDefaultColumnWidth(10);	
		//设置列宽
//		wsheet.setColumnView(0, 20);wsheet.setColumnView(1, 15);
////		wsheet.setColumnView(2, 5);
//		wsheet.setColumnView(3, 10);wsheet.setColumnView(4, 20);
//		wsheet.setColumnView(5, 10);wsheet.setColumnView(6, 10);
//		wsheet.setColumnView(7, 15);wsheet.setColumnView(8, 40);
//		wsheet.setColumnView(9, 25);wsheet.setColumnView(10, 15);
//		wsheet.setColumnView(10, 20);
////			  wsheet.setColumnView(10, 5);wsheet.setColumnView(13, 5);wsheet.setColumnView(14, 5);wsheet.setColumnView(15, 5);wsheet.setColumnView(16, 5);
////			  wsheet.setColumnView(17, 5);wsheet.setColumnView(18, 5);wsheet.setColumnView(19, 5);
//		wsheet.setColumnView(20, 20);wsheet.setColumnView(21, 20);
////			  wsheet.setColumnView(22, 5);wsheet.setColumnView(23, 5);wsheet.setColumnView(24, 5);wsheet.setColumnView(25, 5);wsheet.setColumnView(26, 7);
//			  			  
		//红字蓝底
		WritableCellFormat totalx2Format = this.getWritableCellFormatTitleRed();
		//黑字蓝底
		//WritableCellFormat totalx2FormatBlack = this.getWritableCellFormatTitleBlack();
			  
		//字段行		
		//2010-7-18 ---------------------------------------------------------------------------------------------
		String[] title = {"开始日期","结束日期","工资","社保费","折旧费","水电费","电话费","办公费","差旅费","快递费","租金","堤围费","印花税","汽油费","租车费"};
		String[] title2 = {"建帐费","其他","贷款利息","存款利息(扣减)","银行手续费","汇兑损益","营业外支出","所得税","合计"};
		//2010-7-18 end ---------------------------------------------------------------------------------------------
//		for(int i=0;i<title.length;i++){
//			wsheet.addCell(new Label(i,0,title[i],totalx2Format));
//        }
		//第1行
		//wsheet.addCell(new Label(0,0,"开始日期",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		//wsheet.addCell(new Label(1,0,"结束日期",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(0,0,"年月",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		//wsheet.addCell(new Label(1,0,"月",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(1,0,"工资",		getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(2,0,"社保费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(3,0,"折旧费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(4,0,"水电费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(5,0,"电话费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(6,0,"办公费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(7,0,"差旅费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(8,0,"快递费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(9,0,"租金",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(10,0,"堤围费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(11,0,"印花税",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(12,0,"汽油费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(13,0,"租车费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(14,0,"建帐费",			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(15,0,"其他",				getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(16,0,"贷款利息",			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(17,0,"存款利息(扣减)",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(18,0,"银行手续费",		getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(19,0,"汇兑损益",			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(20,0,"营业外支出",		getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(21,0,"所得税",			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(22,0,"合计",				getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		
		//第2行
		int i=1;
		for(Fymxb f : _f){
		//wsheet.addCell(new Label(0,i,f.getKsrq(),					getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		//wsheet.addCell(new Label(1,i,f.getJsrq(),					getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(0,i,f.getRq(),					getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		//wsheet.addCell(new Label(1,i,f.getRq().substring(5,7),					getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(1,i,String.valueOf(f.getGz()),		getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(2,i,String.valueOf(f.getSbf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(3,i,String.valueOf(f.getZjf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(4,i,String.valueOf(f.getSdf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(5,i,String.valueOf(f.getDhf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(6,i,String.valueOf(f.getBgf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(7,i,String.valueOf(f.getClf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(8,i,String.valueOf(f.getKdf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(9,i,String.valueOf(f.getZj()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(10,i,String.valueOf(f.getTwf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(11,i,String.valueOf(f.getYhs()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(12,i,String.valueOf(f.getQyf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(13,i,String.valueOf(f.getZcf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));	

		wsheet.addCell(new Label(14,i,String.valueOf(f.getJzf()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(15,i,String.valueOf(f.getQt()),				getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(16,i,String.valueOf(f.getDklx()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(17,i,String.valueOf(f.getCklx()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(18,i,String.valueOf(f.getYhsxf()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(19,i,String.valueOf(f.getHdsy()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(20,i,String.valueOf(f.getYywzc()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(21,i,String.valueOf(f.getSds()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(22,i,String.valueOf(number2(f.getZbhj())),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		i++;
		}
		
		// 主体内容生成结束 
		wwb.write();
		wwb.close();		
		os.close(); 
		return JXLReadExcel2.getExcelInfo(wwb);
	};
	
	
	/**
	 * 打印xls报表                                                                                          1行
	 * xlsOrHtml 1为输入 xls文件，2为输出html文本。
	 * @throws Exception 
	 */
	public String getXlsBb(Fymxb f,OutputStream os) throws Exception{
		WritableWorkbook wwb = Workbook.createWorkbook(os);
		
		
		WritableSheet wsheet = wwb.createSheet("费用明细表", 0); // sheet名称
		
		wsheet.getSettings().setDefaultColumnWidth(10);	
		//设置列宽
//		wsheet.setColumnView(0, 20);wsheet.setColumnView(1, 15);
////		wsheet.setColumnView(2, 5);
//		wsheet.setColumnView(3, 10);wsheet.setColumnView(4, 20);
//		wsheet.setColumnView(5, 10);wsheet.setColumnView(6, 10);
//		wsheet.setColumnView(7, 15);wsheet.setColumnView(8, 40);
//		wsheet.setColumnView(9, 25);wsheet.setColumnView(10, 15);
//		wsheet.setColumnView(10, 20);
////			  wsheet.setColumnView(10, 5);wsheet.setColumnView(13, 5);wsheet.setColumnView(14, 5);wsheet.setColumnView(15, 5);wsheet.setColumnView(16, 5);
////			  wsheet.setColumnView(17, 5);wsheet.setColumnView(18, 5);wsheet.setColumnView(19, 5);
//		wsheet.setColumnView(20, 20);wsheet.setColumnView(21, 20);
////			  wsheet.setColumnView(22, 5);wsheet.setColumnView(23, 5);wsheet.setColumnView(24, 5);wsheet.setColumnView(25, 5);wsheet.setColumnView(26, 7);
//			  			  
		//红字蓝底
		WritableCellFormat totalx2Format = this.getWritableCellFormatTitleRed();
		//黑字蓝底
		//WritableCellFormat totalx2FormatBlack = this.getWritableCellFormatTitleBlack();
			  
		//字段行		
		//2010-7-18 ---------------------------------------------------------------------------------------------
		String[] title = {"开始日期","结束日期","工资","社保费","折旧费","水电费","电话费","办公费","差旅费","快递费","租金","堤围费","印花税","汽油费","租车费"};
		String[] title2 = {"建帐费","其他","贷款利息","存款利息(扣减)","银行手续费","汇兑损益","营业外支出","所得税","合计"};
		//2010-7-18 end ---------------------------------------------------------------------------------------------
//		for(int i=0;i<title.length;i++){
//			wsheet.addCell(new Label(i,0,title[i],totalx2Format));
//        }
		//第1行
		wsheet.addCell(new Label(0,0,"开始日期",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(1,0,"结束日期",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(2,0,"工资",		getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(3,0,"社保费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(4,0,"折旧费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(5,0,"水电费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(6,0,"电话费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(7,0,"办公费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(8,0,"差旅费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(9,0,"快递费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(10,0,"租金",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(11,0,"堤围费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(12,0,"印花税",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(13,0,"汽油费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(14,0,"租车费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		//第2行
		wsheet.addCell(new Label(0,1,f.getKsrq(),					getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(1,1,f.getJsrq(),					getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(2,1,String.valueOf(f.getGz()),		getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(3,1,String.valueOf(f.getSbf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(4,1,String.valueOf(f.getZjf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(5,1,String.valueOf(f.getSdf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(6,1,String.valueOf(f.getDhf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(7,1,String.valueOf(f.getBgf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(8,1,String.valueOf(f.getClf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(9,1,String.valueOf(f.getKdf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(10,1,String.valueOf(f.getZj()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(11,1,String.valueOf(f.getTwf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(12,1,String.valueOf(f.getYhs()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(13,1,String.valueOf(f.getQyf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(14,1,String.valueOf(f.getZcf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));	
		
		//第3行
		wsheet.addCell(new Label(15,0,"建帐费",			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(16,0,"其他",				getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(17,0,"贷款利息",			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(18,0,"存款利息(扣减)",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(19,0,"银行手续费",		getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(20,0,"汇兑损益",			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(21,0,"营业外支出",		getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(22,0,"所得税",			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(23,0,"合计",				getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		
		//第4行
		wsheet.addCell(new Label(15,1,String.valueOf(f.getJzf()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(16,1,String.valueOf(f.getQt()),				getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(17,1,String.valueOf(f.getDklx()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(18,1,String.valueOf(f.getCklx()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(19,1,String.valueOf(f.getYhsxf()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(20,1,String.valueOf(f.getHdsy()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(21,1,String.valueOf(f.getYywzc()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(22,1,String.valueOf(f.getSds()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(23,1,String.valueOf(number2(f.getZbhj())),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		
		
		// 主体内容生成结束 
		wwb.write();
		wwb.close();		
		os.close(); 
		return JXLReadExcel2.getExcelInfo(wwb);
	};
	
	
	/**
	 * 打印xls报表                                                                                          2行
	 * xlsOrHtml 1为输入 xls文件，2为输出html文本。
	 * @throws Exception 
	 */
	public String getXlsBb2(Fymxb f,OutputStream os) throws Exception{
		WritableWorkbook wwb = Workbook.createWorkbook(os);
		
		
		WritableSheet wsheet = wwb.createSheet("费用明细表", 0); // sheet名称
		
		wsheet.getSettings().setDefaultColumnWidth(10);	
		//设置列宽
//		wsheet.setColumnView(0, 20);wsheet.setColumnView(1, 15);
////		wsheet.setColumnView(2, 5);
//		wsheet.setColumnView(3, 10);wsheet.setColumnView(4, 20);
//		wsheet.setColumnView(5, 10);wsheet.setColumnView(6, 10);
//		wsheet.setColumnView(7, 15);wsheet.setColumnView(8, 40);
//		wsheet.setColumnView(9, 25);wsheet.setColumnView(10, 15);
//		wsheet.setColumnView(10, 20);
////			  wsheet.setColumnView(10, 5);wsheet.setColumnView(13, 5);wsheet.setColumnView(14, 5);wsheet.setColumnView(15, 5);wsheet.setColumnView(16, 5);
////			  wsheet.setColumnView(17, 5);wsheet.setColumnView(18, 5);wsheet.setColumnView(19, 5);
//		wsheet.setColumnView(20, 20);wsheet.setColumnView(21, 20);
////			  wsheet.setColumnView(22, 5);wsheet.setColumnView(23, 5);wsheet.setColumnView(24, 5);wsheet.setColumnView(25, 5);wsheet.setColumnView(26, 7);
//			  			  
		//红字蓝底
		WritableCellFormat totalx2Format = this.getWritableCellFormatTitleRed();
		//黑字蓝底
		//WritableCellFormat totalx2FormatBlack = this.getWritableCellFormatTitleBlack();
			  
		//字段行		
		//2010-7-18 ---------------------------------------------------------------------------------------------
		String[] title = {"开始日期","结束日期","工资","社保费","折旧费","水电费","电话费","办公费","差旅费","快递费","租金","堤围费","印花税","汽油费","租车费"};
		String[] title2 = {"建帐费","其他","贷款利息","存款利息(扣减)","银行手续费","汇兑损益","营业外支出","所得税","合计"};
		//2010-7-18 end ---------------------------------------------------------------------------------------------
//		for(int i=0;i<title.length;i++){
//			wsheet.addCell(new Label(i,0,title[i],totalx2Format));
//        }
		//第1行
		wsheet.addCell(new Label(0,0,"开始日期",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(1,0,"结束日期",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(2,0,"工资",		getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.LIGHT_ORANGE,10)));
		wsheet.addCell(new Label(3,0,"社保费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.LIGHT_BLUE,10)));
		wsheet.addCell(new Label(4,0,"折旧费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.LIGHT_BLUE,10)));
		wsheet.addCell(new Label(5,0,"水电费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.LIGHT_BLUE,10)));
		wsheet.addCell(new Label(6,0,"电话费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.LIGHT_BLUE,10)));
		wsheet.addCell(new Label(7,0,"办公费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.LIGHT_BLUE,10)));
		wsheet.addCell(new Label(8,0,"差旅费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.LIGHT_BLUE,10)));
		wsheet.addCell(new Label(9,0,"快递费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.LIGHT_BLUE,10)));
		wsheet.addCell(new Label(10,0,"租金",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.LIGHT_BLUE,10)));
		wsheet.addCell(new Label(11,0,"堤围费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.LIGHT_BLUE,10)));
		wsheet.addCell(new Label(12,0,"印花税",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.LIGHT_BLUE,10)));
		wsheet.addCell(new Label(13,0,"汽油费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.LIGHT_BLUE,10)));
		wsheet.addCell(new Label(14,0,"租车费",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.LIGHT_BLUE,10)));
		//第2行
		wsheet.addCell(new Label(0,1,f.getKsrq(),					getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(1,1,f.getJsrq(),					getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(2,1,String.valueOf(f.getGz()),		getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(3,1,String.valueOf(f.getSbf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(4,1,String.valueOf(f.getZjf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(5,1,String.valueOf(f.getSdf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(6,1,String.valueOf(f.getDhf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(7,1,String.valueOf(f.getBgf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(8,1,String.valueOf(f.getClf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(9,1,String.valueOf(f.getKdf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(10,1,String.valueOf(f.getZj()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(11,1,String.valueOf(f.getTwf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(12,1,String.valueOf(f.getYhs()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(13,1,String.valueOf(f.getQyf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(14,1,String.valueOf(f.getZcf()),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));	
		
		//第3行
		wsheet.addCell(new Label(0,2,"建帐费",			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.LIGHT_BLUE,10)));
		wsheet.addCell(new Label(1,2,"其他",				getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.LIGHT_BLUE,10)));
		wsheet.addCell(new Label(2,2,"贷款利息",			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.YELLOW,10)));
		wsheet.addCell(new Label(3,2,"存款利息(扣减)",	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.YELLOW,10)));
		wsheet.addCell(new Label(4,2,"银行手续费",		getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.YELLOW,10)));
		wsheet.addCell(new Label(5,2,"汇兑损益",			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.YELLOW,10)));
		wsheet.addCell(new Label(6,2,"营业外支出",		getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.LIGHT_BLUE,10)));
		wsheet.addCell(new Label(7,2,"所得税",			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.RED,10)));
		wsheet.addCell(new Label(8,2,"合计",				getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		
		//第4行
		wsheet.addCell(new Label(0,3,String.valueOf(f.getJzf()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(1,3,String.valueOf(f.getQt()),				getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(2,3,String.valueOf(f.getDklx()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(3,3,String.valueOf(f.getCklx()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(4,3,String.valueOf(f.getYhsxf()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(5,3,String.valueOf(f.getHdsy()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(6,3,String.valueOf(f.getYywzc()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(7,3,String.valueOf(f.getSds()),			getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		wsheet.addCell(new Label(8,3,String.valueOf(number2(f.getZbhj())),	getStyle(jxl.format.Colour.BLACK ,jxl.format.Colour.WHITE,10)));
		
		
		// 主体内容生成结束 
		wwb.write();
		wwb.close();		
		os.close(); 
		return JXLReadExcel2.getExcelInfo(wwb);
	};
	
	/**
	 * 用hibernate获得统计数据
	 * @param fyrq__gte__string
	 * @param fyrq__lte__string
	 * @param service
	 * @return
	 */
	public Fymxb getFymxbData(String fyrq__gte__string, String fyrq__lte__string, GlfymxService service){
		
		Fymxb f = new Fymxb();		
		//工资
		f.setGz(
				(Double)service.createCriteria(Yggz.class)
				.add(Expression.ge("gzrq", fyrq__gte__string))
				.add(Expression.le("gzrq", fyrq__lte__string))
				.setProjection(Projections.sum("yfgz"))
				.uniqueResult()
		);
		//社保费		
//		f.setSbf(
//				(Double)service.createCriteria(Yggz.class)
//				.add(Expression.ge("gzrq", fyrq__gte__string))
//				.add(Expression.le("gzrq", fyrq__lte__string))
//				.setProjection(Projections.sum("sbf"))
//				.uniqueResult()
//				
//		);	
		f.setSbf(
//				(Double)service.createCriteria(Glfymx.class)
//				.add(Expression.ge("fyrq", fyrq__gte__string))
//				.add(Expression.le("fyrq", fyrq__lte__string))
//				.setProjection(Projections.sum("sbf"))
//				.uniqueResult()
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "3001"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);
		//折旧费(特殊)		
//		int begin_year = Integer.parseInt(fyrq__gte__string.substring(0, 4));
//		int end_year = Integer.parseInt(fyrq__lte__string.substring(0, 4));		
//		int begin_month = Integer.parseInt(fyrq__gte__string.substring(5, 7));
//		int end_month = Integer.parseInt(fyrq__lte__string.substring(5, 7));		
//		int months = (end_year-begin_year)*12+(end_month-begin_month)+1;
//		try{
//		f.setZjf(
//				((Double)service.createCriteria(Gdzc.class).add(Restrictions.gtProperty("zjyf", "yzjyf"))
//				.setProjection(Projections.sum("myzjje"))
//				.uniqueResult())*months
//		);	
//		}catch(Exception e){
//			f.setZjf(0.00);
//		}
		f.setZjf(
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "3014"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);	
		//水电费
		f.setSdf(
//				(Double)service.createCriteria(Glfymx.class)
//				.add(Expression.ge("fyrq", fyrq__gte__string))
//				.add(Expression.le("fyrq", fyrq__lte__string))
//				.setProjection(Projections.sum("sdf"))
//				.uniqueResult()
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "3002"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);			
		//电话费
		f.setDhf(
//				(Double)service.createCriteria(Glfymx.class)
//				.add(Expression.ge("fyrq", fyrq__gte__string))
//				.add(Expression.le("fyrq", fyrq__lte__string))
//				.setProjection(Projections.sum("dhf"))
//				.uniqueResult()
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "3003"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);	
		//办公费
		f.setBgf(
//				(Double)service.createCriteria(Glfymx.class)
//				.add(Expression.ge("fyrq", fyrq__gte__string))
//				.add(Expression.le("fyrq", fyrq__lte__string))
//				.setProjection(Projections.sum("bgf"))
//				.uniqueResult()
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "3004"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);		
		//差旅费
		f.setClf(
//				(Double)service.createCriteria(Glfymx.class)
//				.add(Expression.ge("fyrq", fyrq__gte__string))
//				.add(Expression.le("fyrq", fyrq__lte__string))
//				.setProjection(Projections.sum("clf"))
//				.uniqueResult()
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "3005"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);
		//汽油费
		f.setQyf(
//				(Double)service.createCriteria(Glfymx.class)
//				.add(Expression.ge("fyrq", fyrq__gte__string))
//				.add(Expression.le("fyrq", fyrq__lte__string))
//				.setProjection(Projections.sum("qyf"))
//				.uniqueResult()
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "3006"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);	
		//堤围费
		f.setTwf(
//				(Double)service.createCriteria(Glfymx.class)
//				.add(Expression.ge("fyrq", fyrq__gte__string))
//				.add(Expression.le("fyrq", fyrq__lte__string))
//				.setProjection(Projections.sum("twf"))
//				.uniqueResult()
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "3007"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);	
		//快递费	
		f.setKdf(
//				(Double)service.createCriteria(Glfymx.class)
//				.add(Expression.ge("fyrq", fyrq__gte__string))
//				.add(Expression.le("fyrq", fyrq__lte__string))
//				.setProjection(Projections.sum("kdf"))
//				.uniqueResult()
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "3008"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);
		//印花税
		f.setYhs(
//				(Double)service.createCriteria(Glfymx.class)
//				.add(Expression.ge("fyrq", fyrq__gte__string))
//				.add(Expression.le("fyrq", fyrq__lte__string))
//				.setProjection(Projections.sum("yhs"))
//				.uniqueResult()
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "3009"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);			
		//租金
		f.setZj(
//				(Double)service.createCriteria(Glfymx.class)
//				.add(Expression.ge("fyrq", fyrq__gte__string))
//				.add(Expression.le("fyrq", fyrq__lte__string))
//				.setProjection(Projections.sum("zj"))
//				.uniqueResult()
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "3010"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);			
		//租车费
		f.setZcf(
//				(Double)service.createCriteria(Glfymx.class)
//				.add(Expression.ge("fyrq", fyrq__gte__string))
//				.add(Expression.le("fyrq", fyrq__lte__string))
//				.setProjection(Projections.sum("zcf"))
//				.uniqueResult()
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "3011"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);
		//建账费
		f.setJzf(
//				(Double)service.createCriteria(Glfymx.class)
//				.add(Expression.ge("fyrq", fyrq__gte__string))
//				.add(Expression.le("fyrq", fyrq__lte__string))
//				.setProjection(Projections.sum("jzf"))
//				.uniqueResult()
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "3012"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);
		//其他
		f.setQt(
//				(Double)service.createCriteria(Glfymx.class)
//				.add(Expression.ge("fyrq", fyrq__gte__string))
//				.add(Expression.le("fyrq", fyrq__lte__string))
//				.setProjection(Projections.sum("qt"))
//				.uniqueResult()
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "3013"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);				
		//贷款利息
		f.setDklx(
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "101"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);	
		//存款利息
		f.setCklx(
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "102"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);		
		//银行手续费
		f.setYhsxf(
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "103"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);	
		//汇兑损益
		f.setHdsy(
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "104"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);	
		//营业外支出
		f.setYywzc(
				(Double)service.createCriteria(Cwfy.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.add(Expression.eq("type", "201"))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);	
		//所得税
		f.setSds(
				(Double)service.createCriteria(Sds.class)
				.add(Expression.ge("fyrq", fyrq__gte__string))
				.add(Expression.le("fyrq", fyrq__lte__string))
				.setProjection(Projections.sum("je"))
				.uniqueResult()
		);			
		
		
		//管理费用合计
		f.setGlfyhj(f.getGlfyhj());
		//财务费用合计
		f.setCwfyhj(f.getCwfyhj());
		//总表合计
		f.setZbhj(number2(f.getZbhj()));

		
		//null to 0.00;
		if(f.gz==null)f.gz=0.00;
		if(f.sbf==null)f.sbf=0.00;
		if(f.zjf==null)f.zjf=0.00;
		if(f.sdf==null)f.sdf=0.00;
		if(f.dhf==null)f.dhf=0.00;
		if(f.bgf==null)f.bgf=0.00;
		if(f.clf==null)f.clf=0.00;
		if(f.kdf==null)f.kdf=0.00;
		if(f.zj==null)f.zj=0.00;
		if(f.twf==null)f.twf=0.00;
		if(f.yhs==null)f.yhs=0.00;
		if(f.qyf==null)f.qyf=0.00;
		if(f.zcf==null)f.zcf=0.00;
		if(f.jzf==null)f.jzf=0.00;
		if(f.qt==null)f.qt=0.00;
		if(f.glfyhj==null)f.glfyhj=0.00;
		if(f.dklx==null)f.dklx=0.00;
		if(f.cklx==null)f.cklx=0.00;
		if(f.yhsxf==null)f.yhsxf=0.00;
		if(f.hdsy==null)f.hdsy=0.00;
		if(f.cwfyhj==null)f.cwfyhj=0.00;
		if(f.yywzc==null)f.yywzc=0.00;
		if(f.sds==null)f.sds=0.00;
		if(f.zbhj==null)f.zbhj=0.00;
		
		
		return f;
	}
	
	
	
	
	//保留小数点后两位小数 
	public   double   number2(double   pDouble) 
	{ 
	    BigDecimal     bd=new     BigDecimal(pDouble); 
	    BigDecimal     bd1=bd.setScale(2,bd.ROUND_HALF_UP); 
	    pDouble=bd1.doubleValue(); 
	    long     ll   =   Double.doubleToLongBits(pDouble); 
	    
	    return   pDouble; 
	} 


	
	
	
	
	
	
	/**
	 * 粗红字，浅蓝底。
	 * @return
	 * @throws WriteException
	 */
	private WritableCellFormat getWritableCellFormatTitleRed() throws WriteException{
		/**
		   * 构造格式：ARIAL字体、10号、粗体、非斜体、无下划线、红色
		   */
		    WritableFont fmtx2TotalCaption = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD,
		        false,UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.RED); 
		    WritableCellFormat totalx2Format = new WritableCellFormat(fmtx2TotalCaption);
		    	//文字垂直居中对齐
		    	totalx2Format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
		    	//文字水平居中对齐 
		    	totalx2Format.setAlignment(jxl.format.Alignment.CENTRE);   
		    	//边框深蓝色
		    	totalx2Format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN,
		        jxl.format.Colour.DARK_BLUE);
		    	//设置底色为冰蓝
		    	totalx2Format.setBackground(jxl.format.Colour.ICE_BLUE);		    	
		    	return totalx2Format;
	}
	/**
	 * 粗黑字，浅蓝底。
	 * @return
	 * @throws WriteException
	 */
	private WritableCellFormat getWritableCellFormatTitleBlack() throws WriteException{
		/**
		   * 构造格式：ARIAL字体、10号、粗体、非斜体、无下划线、红色
		   */
		WritableFont fmtx2TotalCaption = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD,
															false,UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK); 
		WritableCellFormat totalx2Format = new WritableCellFormat(fmtx2TotalCaption);
		//文字垂直居中对齐
		totalx2Format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
		//文字水平居中对齐 
		totalx2Format.setAlignment(jxl.format.Alignment.CENTRE);   
		//边框深蓝色
		totalx2Format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN,
								jxl.format.Colour.DARK_BLUE);
		//设置底色为冰蓝
		totalx2Format.setBackground(jxl.format.Colour.ICE_BLUE);		    	
		return totalx2Format;
	}
	/**
	 * 粗黑字，白底，居中。
	 * @return
	 * @throws WriteException
	 */
	private WritableCellFormat getWritableCellFormatTitleWrite() throws WriteException{
		/**
		   * 构造格式：ARIAL字体、10号、粗体、非斜体、无下划线、红色
		   */
		    WritableFont fmtx2TotalCaption = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD,
		        false,UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK); 
		    WritableCellFormat totalx2Format = new WritableCellFormat(fmtx2TotalCaption);
		    	//文字垂直居中对齐
		    	totalx2Format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
		    	//文字水平居中对齐 
		    	totalx2Format.setAlignment(jxl.format.Alignment.CENTRE);   
		    	//边框深蓝色
//		    	totalx2Format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN,
//		        jxl.format.Colour.WHITE);
		    	//设置底色为冰蓝
//		    	totalx2Format.setBackground(jxl.format.Colour.ICE_BLUE);		    	
		    	return totalx2Format;
	}
	
	
	/**
	 * 黑字，白底，居中。
	 * @return
	 * @throws WriteException
	 */
	private WritableCellFormat getWritableCellFormatTitleWrite2() throws WriteException{
		/**
		   * 构造格式：ARIAL字体、10号、粗体、非斜体、无下划线、红色
		   */
		    WritableFont fmtx2TotalCaption = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD,
		        false,UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK); 
		    WritableCellFormat totalx2Format = new WritableCellFormat(fmtx2TotalCaption);
		    	//文字垂直居中对齐
		    	totalx2Format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
		    	//文字水平居中对齐 
		    	totalx2Format.setAlignment(jxl.format.Alignment.CENTRE);   
		    	//边框深蓝色
//		    	totalx2Format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN,
//		        jxl.format.Colour.WHITE);
		    	//设置底色为冰蓝
//		    	totalx2Format.setBackground(jxl.format.Colour.ICE_BLUE);		    	
		    	return totalx2Format;
	}
	
	
	
	private WritableCellFormat getStyle(jxl.format.Colour fontColour,jxl.format.Colour backColour,int fontSize) throws WriteException{
		/**
		   * 构造格式：ARIAL字体、10号、粗体、非斜体、无下划线、红色
		   */
		    WritableFont fmtx2TotalCaption = new WritableFont(WritableFont.ARIAL,fontSize,WritableFont.NO_BOLD,
		        false,UnderlineStyle.NO_UNDERLINE,fontColour); 
		    WritableCellFormat totalx2Format = new WritableCellFormat(fmtx2TotalCaption);
		    	//文字垂直居中对齐
		    	totalx2Format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
		    	//文字水平居中对齐 
		    	totalx2Format.setAlignment(jxl.format.Alignment.CENTRE);   
		    	//边框深蓝色
//		    	totalx2Format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN,
//		        jxl.format.Colour.WHITE);
		    	//设置底色为冰蓝
		    	totalx2Format.setBackground(backColour);		    	
		    	return totalx2Format;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 黑字，白底，居中。
	 */
	private WritableCellFormat balckBackWriteFont() throws WriteException{
		/**
		   * 构造格式：ARIAL字体、10号、粗体、非斜体、无下划线、红色
		   */
		    WritableFont fmtx2TotalCaption = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD,
		        false,UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK); 
		    WritableCellFormat totalx2Format = new WritableCellFormat(fmtx2TotalCaption);
		    	//文字垂直居中对齐
		    	totalx2Format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
		    	//文字水平居中对齐 
		    	totalx2Format.setAlignment(jxl.format.Alignment.CENTRE);   
		    	//边框深蓝色
//		    	totalx2Format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN,
//		        jxl.format.Colour.WHITE);
		    	//设置底色为冰蓝
//		    	totalx2Format.setBackground(jxl.format.Colour.ICE_BLUE);		    	
		    	return totalx2Format;
	}
	
	
}
