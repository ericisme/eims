package cn.qtone.eims.lb.domain;

import java.math.BigDecimal;


/**
 * 
 * 损益表,报表用
 * @author:eric
 * @date:2014-07-02
 */
public class SybReport {

	private String ny;//年月
	
	private BigDecimal zyywlr; //主营业务利润
	private BigDecimal glfy; //管理费用
	private BigDecimal cwfy; //财务费用
	private BigDecimal jyfy; //经营费用
	private BigDecimal yywsr; //营业外收入
	private BigDecimal yywzc; //营业外支出
	private BigDecimal sds; //所得税
	private BigDecimal jlr = new BigDecimal(0); //净利润
	public String getNy() {
		return ny;
	}
	public void setNy(String ny) {
		this.ny = ny;
	}
	public BigDecimal getZyywlr() {
		return zyywlr;
	}
	public void setZyywlr(BigDecimal zyywlr) {
		this.zyywlr = zyywlr;
	}
	public BigDecimal getGlfy() {
		return glfy;
	}
	public void setGlfy(BigDecimal glfy) {
		this.glfy = glfy;
	}
	public BigDecimal getCwfy() {
		return cwfy;
	}
	public void setCwfy(BigDecimal cwfy) {
		this.cwfy = cwfy;
	}
	public BigDecimal getJyfy() {
		return jyfy;
	}
	public void setJyfy(BigDecimal jyfy) {
		this.jyfy = jyfy;
	}
	public BigDecimal getYywsr() {
		return yywsr;
	}
	public void setYywsr(BigDecimal yywsr) {
		this.yywsr = yywsr;
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
	public BigDecimal getJlr() {
		return jlr;
	}
	public void setJlr(BigDecimal jlr) {
		this.jlr = jlr;
	}




	

}
