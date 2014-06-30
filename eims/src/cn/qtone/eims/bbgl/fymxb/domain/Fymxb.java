package cn.qtone.eims.bbgl.fymxb.domain;

public class Fymxb {

	public String ksrq;	//开始日期（年-月-日）
	public String jsrq;	//结束日期（年-月-日）
	public String rq; 
	
	/**
	 * 管理费用
	 */
	public Double gz;			//工资
	public Double sbf;			//社保费
	
	public Double zjf;			//折旧费
	
	public Double sdf;			//水电费
	public Double dhf;			//电话费
	public Double bgf;			//办公费
	public Double clf;			//差旅费
	public Double kdf;			//快递费	
	public Double zj;			//租金
	public Double twf;			//堤围费
	public Double yhs;			//印花税	
	public Double qyf;			//汽油费
	public Double zcf;			//租车费
	public Double jzf;			//建账费
	public Double qt;			//其他
	
	public Double glfyhj;		//管理费用合计
	

	/**
	 * 财务费用
	 */
	public Double dklx;		//贷款利息
	public Double cklx;		//存款利息（扣减）
	public Double yhsxf;	//银行手续费
	public Double hdsy;		//汇兑损益
	
	public Double cwfyhj;	//账务费用合计
	
	
	/**
	 * 营业外支出
	 */
	public Double yywzc;	//营业外支出
	
	
	/**
	 * 所得税
	 */
	public Double sds;		//所得税
	
	
	
	public Double zbhj;		//总表合计


	
	
	
	/**
	 * 获得  管理费用合计
	 * @return
	 */
	public Double getGlfyhj() {		
		return 		
		(this.gz==null?0:this.gz)+	    	//工资
		(this.sbf==null?0:this.sbf)+	    //社保费
		
		(this.zjf==null?0:this.zjf)+     	//折旧费
		
		(this.sdf==null?0:this.sdf)+		//水电费
		(this.dhf==null?0:this.dhf)+		//电话费
		(this.bgf==null?0:this.bgf)+		///办公费
		(this.clf==null?0:this.clf)+		//差旅费	
		(this.kdf==null?0:this.kdf)+		//快递费
		(this.zj ==null?0:this.zj) +		//租金
		(this.twf==null?0:this.twf)+		//堤围费
		(this.yhs==null?0:this.yhs)+		//印花税
		(this.qyf==null?0:this.qyf)+		//汽油费
		(this.zcf==null?0:this.zcf)+		//租车费
		(this.jzf==null?0:this.jzf)+		//建账费
		(this.qt ==null?0:this.qt);			//其他
	}	
	/**
	 * 获得  账务费用合计
	 * @return
	 */
	public Double getCwfyhj() {		
		return 
			(this.dklx==null?0:this.dklx)-		//贷款利息
			(this.cklx==null?0:this.cklx)+		//存款利息（扣减）
			(this.yhsxf==null?0:this.yhsxf)+	//银行手续费
			(this.hdsy==null?0:this.hdsy);		//汇兑损益
	}
	/**
	 * 获得 总表合计
	 * @return
	 */
	public Double getZbhj() {
		return 
		this.getGlfyhj()+					//获得  管理费用合计
		this.getCwfyhj()+					//获得  账务费用合计
		(this.yywzc==null?0:this.yywzc)+	//营业外支出
		(this.sds==null?0:this.sds);		//所得税
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


	public Double getGz() {
		return gz;
	}


	public void setGz(Double gz) {
		this.gz = gz;
	}


	public Double getSbf() {
		return sbf;
	}


	public void setSbf(Double sbf) {
		this.sbf = sbf;
	}


	public Double getZjf() {
		return zjf;
	}


	public void setZjf(Double zjf) {
		this.zjf = zjf;
	}


	public Double getSdf() {
		return sdf;
	}


	public void setSdf(Double sdf) {
		this.sdf = sdf;
	}


	public Double getDhf() {
		return dhf;
	}


	public void setDhf(Double dhf) {
		this.dhf = dhf;
	}


	public Double getBgf() {
		return bgf;
	}


	public void setBgf(Double bgf) {
		this.bgf = bgf;
	}


	public Double getClf() {
		return clf;
	}


	public void setClf(Double clf) {
		this.clf = clf;
	}


	public Double getKdf() {
		return kdf;
	}


	public void setKdf(Double kdf) {
		this.kdf = kdf;
	}


	public Double getZj() {
		return zj;
	}


	public void setZj(Double zj) {
		this.zj = zj;
	}


	public Double getTwf() {
		return twf;
	}


	public void setTwf(Double twf) {
		this.twf = twf;
	}


	public Double getYhs() {
		return yhs;
	}


	public void setYhs(Double yhs) {
		this.yhs = yhs;
	}


	public Double getQyf() {
		return qyf;
	}


	public void setQyf(Double qyf) {
		this.qyf = qyf;
	}


	public Double getZcf() {
		return zcf;
	}


	public void setZcf(Double zcf) {
		this.zcf = zcf;
	}


	public Double getJzf() {
		return jzf;
	}


	public void setJzf(Double jzf) {
		this.jzf = jzf;
	}


	public Double getQt() {
		return qt;
	}


	public void setQt(Double qt) {
		this.qt = qt;
	}





	public void setGlfyhj(Double glfyhj) {
		this.glfyhj = glfyhj;
	}


	public Double getDklx() {
		return dklx;
	}


	public void setDklx(Double dklx) {
		this.dklx = dklx;
	}


	public Double getCklx() {
		return cklx;
	}


	public void setCklx(Double cklx) {
		this.cklx = cklx;
	}


	public Double getYhsxf() {
		return yhsxf;
	}


	public void setYhsxf(Double yhsxf) {
		this.yhsxf = yhsxf;
	}


	public Double getHdsy() {
		return hdsy;
	}


	public void setHdsy(Double hdsy) {
		this.hdsy = hdsy;
	}


	public Double getYywzc() {
		return yywzc;
	}


	public void setYywzc(Double yywzc) {
		this.yywzc = yywzc;
	}


	public Double getSds() {
		return sds;
	}


	public void setSds(Double sds) {
		this.sds = sds;
	}





	public void setZbhj(Double zbhj) {
		this.zbhj = zbhj;
	}

	public void setCwfyhj(Double cwfyhj) {
		this.cwfyhj = cwfyhj;
	}
	public String getRq() {
		return rq;
	}
	public void setRq(String rq) {
		this.rq = rq;
	}
	
	
	
	
	
	
}
