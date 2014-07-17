package cn.qtone.eims.lb.domain;


/**
 * 
 * 损益表,报表用
 * @author:eric
 * @date:2014-07-02
 */
public class SybReport {

	private String ny;//年月
	
	private Float zyywlr; //主营业务利润
	private Double glfy; //管理费用
	private Double cwfy; //财务费用
	private Float jyfy; //经营费用
	private Float yywsr; //营业外收入
	private Double yywzc; //营业外支出
	private Double sds; //所得税
	private Float jlr; //净利润
	public String getNy() {
		return ny;
	}
	public void setNy(String ny) {
		this.ny = ny;
	}
	public Float getZyywlr() {
		return zyywlr;
	}
	public void setZyywlr(Float zyywlr) {
		this.zyywlr = zyywlr;
	}
	public Double getGlfy() {
		return glfy;
	}
	public void setGlfy(Double glfy) {
		this.glfy = glfy;
	}
	public Double getCwfy() {
		return cwfy;
	}
	public void setCwfy(Double cwfy) {
		this.cwfy = cwfy;
	}
	public Float getJyfy() {
		return jyfy;
	}
	public void setJyfy(Float jyfy) {
		this.jyfy = jyfy;
	}
	public Float getYywsr() {
		return yywsr;
	}
	public void setYywsr(Float yywsr) {
		this.yywsr = yywsr;
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
	public Float getJlr() {
		return jlr;
	}
	public void setJlr(Float jlr) {
		this.jlr = jlr;
	}




	

}
