package cn.qtone.eims.bbgl.fymxb.domain;

import java.math.BigDecimal;

public class Fymxb {

	public String ksrq;	//开始日期（年-月-日）
	public String jsrq;	//结束日期（年-月-日）
	public String rq; 
	
	/**
	 * 管理费用
	 */
	public BigDecimal gz=new BigDecimal(0);			//工资
	public BigDecimal sbf=new BigDecimal(0);			//社保费
	
	public BigDecimal zjf=new BigDecimal(0);			//折旧费
	
	public BigDecimal sdf=new BigDecimal(0);			//水电费
	public BigDecimal dhf=new BigDecimal(0);			//电话费
	public BigDecimal bgf=new BigDecimal(0);			//办公费
	public BigDecimal clf=new BigDecimal(0);			//差旅费
	public BigDecimal kdf=new BigDecimal(0);			//快递费	
	public BigDecimal zj=new BigDecimal(0);			//租金
	public BigDecimal twf=new BigDecimal(0);			//堤围费
	public BigDecimal yhs=new BigDecimal(0);			//印花税	
	public BigDecimal qyf=new BigDecimal(0);			//汽油费
	public BigDecimal zcf=new BigDecimal(0);			//租车费
	public BigDecimal jzf=new BigDecimal(0);			//建账费
	public BigDecimal qt=new BigDecimal(0);			//其他
	
	public BigDecimal glfyhj;		//管理费用合计
	

	/**
	 * 财务费用
	 */
	public BigDecimal dklx;		//贷款利息
	public BigDecimal cklx;		//存款利息（扣减）
	public BigDecimal yhsxf;	//银行手续费
	public BigDecimal hdsy;		//汇兑损益
	
	public BigDecimal cwfyhj;	//账务费用合计
	
	
	/**
	 * 营业外支出
	 */
	public BigDecimal yywzc;	//营业外支出
	
	
	/**
	 * 所得税
	 */
	public BigDecimal sds;		//所得税
	
	
	
	public BigDecimal zbhj;		//总表合计


	
	
	
//	/**
//	 * 获得  管理费用合计
//	 * @return
//	 */
//	public BigDecimal getGlfyhj() {		
//		return 		
//		(this.gz==null?0:this.gz)+	    	//工资
//		(this.sbf==null?0:this.sbf)+	    //社保费
//		
//		(this.zjf==null?0:this.zjf)+     	//折旧费
//		
//		(this.sdf==null?0:this.sdf)+		//水电费
//		(this.dhf==null?0:this.dhf)+		//电话费
//		(this.bgf==null?0:this.bgf)+		///办公费
//		(this.clf==null?0:this.clf)+		//差旅费	
//		(this.kdf==null?0:this.kdf)+		//快递费
//		(this.zj ==null?0:this.zj) +		//租金
//		(this.twf==null?0:this.twf)+		//堤围费
//		(this.yhs==null?0:this.yhs)+		//印花税
//		(this.qyf==null?0:this.qyf)+		//汽油费
//		(this.zcf==null?0:this.zcf)+		//租车费
//		(this.jzf==null?0:this.jzf)+		//建账费
//		(this.qt ==null?0:this.qt);			//其他
//	}	
	/**
	 * 获得  管理费用合计
	 * @return
	 */
	public BigDecimal getGlfyhj() {		
		return 		
		(this.gz==null?(new BigDecimal(0)):this.gz).add(	    	//工资
		(this.sbf==null?(new BigDecimal(0)):this.sbf)).add(  //社保费
		
		(this.zjf==null?(new BigDecimal(0)):this.zjf)).add(     	//折旧费
		
		(this.sdf==null?(new BigDecimal(0)):this.sdf)).add(		//水电费
		(this.dhf==null?(new BigDecimal(0)):this.dhf)).add(	//电话费
		(this.bgf==null?(new BigDecimal(0)):this.bgf)).add(		///办公费
		(this.clf==null?(new BigDecimal(0)):this.clf)).add(		//差旅费	
		(this.kdf==null?(new BigDecimal(0)):this.kdf)).add(	//快递费
		(this.zj ==null?(new BigDecimal(0)):this.zj) ).add(		//租金
		(this.twf==null?(new BigDecimal(0)):this.twf)).add(		//堤围费
		(this.yhs==null?(new BigDecimal(0)):this.yhs)).add(		//印花税
		(this.qyf==null?(new BigDecimal(0)):this.qyf)).add(		//汽油费
		(this.zcf==null?(new BigDecimal(0)):this.zcf)).add(		//租车费
		(this.jzf==null?(new BigDecimal(0)):this.jzf)).add(		//建账费
		(this.qt ==null?(new BigDecimal(0)):this.qt));			//其他
	}	
	
	
//	/**
//	 * 获得  账务费用合计
//	 * @return
//	 */
//	public BigDecimal getCwfyhj() {		
//		return 
//			(this.dklx==null?0:this.dklx)-		//贷款利息
//			(this.cklx==null?0:this.cklx)+		//存款利息（扣减）
//			(this.yhsxf==null?0:this.yhsxf)+	//银行手续费
//			(this.hdsy==null?0:this.hdsy);		//汇兑损益
//	}
	/**
	 * 获得  账务费用合计
	 * @return
	 */
	public BigDecimal getCwfyhj() {		
		return 
			(this.dklx==null?(new BigDecimal(0)):this.dklx).subtract(		//贷款利息
			(this.cklx==null?(new BigDecimal(0)):this.cklx)).add(//存款利息（扣减）
			(this.yhsxf==null?(new BigDecimal(0)):this.yhsxf)).add(	//银行手续费
			(this.hdsy==null?(new BigDecimal(0)):this.hdsy));		//汇兑损益
	}	
	
//	/**
//	 * 获得 总表合计
//	 * @return
//	 */
//	public BigDecimal getZbhj() {
//		return 
//		this.getGlfyhj()+					//获得  管理费用合计
//		this.getCwfyhj()+					//获得  账务费用合计
//		(this.yywzc==null?0:this.yywzc)+	//营业外支出
//		(this.sds==null?0:this.sds);		//所得税
//	}
	/**
	 * 获得 总表合计
	 * @return
	 */
	public BigDecimal getZbhj() {
		return 
		this.getGlfyhj().add(					//获得  管理费用合计
		this.getCwfyhj()).add(					//获得  账务费用合计
		(this.yywzc==null?(new BigDecimal(0)):this.yywzc)).add(	//营业外支出
		(this.sds==null?(new BigDecimal(0)):this.sds));		//所得税
	}	
	
	
	
	public String getKsrq() {
		return ksrq;
	}


	public void setKsrq(String ksrq) {
		this.ksrq = ksrq;
	}


	public String getJsrq() {
		return jsrq;
	}


	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}


	public BigDecimal getGz() {
		return gz;
	}


	public void setGz(BigDecimal gz) {
		this.gz = gz;
	}


	public BigDecimal getSbf() {
		return sbf;
	}


	public void setSbf(BigDecimal sbf) {
		this.sbf = sbf;
	}


	public BigDecimal getZjf() {
		return zjf;
	}


	public void setZjf(BigDecimal zjf) {
		this.zjf = zjf;
	}


	public BigDecimal getSdf() {
		return sdf;
	}


	public void setSdf(BigDecimal sdf) {
		this.sdf = sdf;
	}


	public BigDecimal getDhf() {
		return dhf;
	}


	public void setDhf(BigDecimal dhf) {
		this.dhf = dhf;
	}


	public BigDecimal getBgf() {
		return bgf;
	}


	public void setBgf(BigDecimal bgf) {
		this.bgf = bgf;
	}


	public BigDecimal getClf() {
		return clf;
	}


	public void setClf(BigDecimal clf) {
		this.clf = clf;
	}


	public BigDecimal getKdf() {
		return kdf;
	}


	public void setKdf(BigDecimal kdf) {
		this.kdf = kdf;
	}


	public BigDecimal getZj() {
		return zj;
	}


	public void setZj(BigDecimal zj) {
		this.zj = zj;
	}


	public BigDecimal getTwf() {
		return twf;
	}


	public void setTwf(BigDecimal twf) {
		this.twf = twf;
	}


	public BigDecimal getYhs() {
		return yhs;
	}


	public void setYhs(BigDecimal yhs) {
		this.yhs = yhs;
	}


	public BigDecimal getQyf() {
		return qyf;
	}


	public void setQyf(BigDecimal qyf) {
		this.qyf = qyf;
	}


	public BigDecimal getZcf() {
		return zcf;
	}


	public void setZcf(BigDecimal zcf) {
		this.zcf = zcf;
	}


	public BigDecimal getJzf() {
		return jzf;
	}


	public void setJzf(BigDecimal jzf) {
		this.jzf = jzf;
	}


	public BigDecimal getQt() {
		return qt;
	}


	public void setQt(BigDecimal qt) {
		this.qt = qt;
	}





	public void setGlfyhj(BigDecimal glfyhj) {
		this.glfyhj = glfyhj;
	}


	public BigDecimal getDklx() {
		return dklx;
	}


	public void setDklx(BigDecimal dklx) {
		this.dklx = dklx;
	}


	public BigDecimal getCklx() {
		return cklx;
	}


	public void setCklx(BigDecimal cklx) {
		this.cklx = cklx;
	}


	public BigDecimal getYhsxf() {
		return yhsxf;
	}


	public void setYhsxf(BigDecimal yhsxf) {
		this.yhsxf = yhsxf;
	}


	public BigDecimal getHdsy() {
		return hdsy;
	}


	public void setHdsy(BigDecimal hdsy) {
		this.hdsy = hdsy;
	}


	public BigDecimal getYywzc() {
		return yywzc;
	}


	public void setYywzc(BigDecimal yywzc) {
		this.yywzc = yywzc;
	}


	public BigDecimal getSds() {
		return sds;
	}


	public void setSds(BigDecimal sds) {
		this.sds = sds;
	}





	public void setZbhj(BigDecimal zbhj) {
		this.zbhj = zbhj;
	}

	public void setCwfyhj(BigDecimal cwfyhj) {
		this.cwfyhj = cwfyhj;
	}
	public String getRq() {
		return rq;
	}
	public void setRq(String rq) {
		this.rq = rq;
	}
	
	
	
	
	
	
}
