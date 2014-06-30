package cn.qtone.eims.lb.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 损益表
 * @author:周国辉
 * @date:2013-1-27
 */
@Entity
@Table(name = "EIMS_SYB")
public class Syb {

	private Integer id; //id
	private Date ny; //年月
	private Float zyywlr; //主营业务利润
	private Float glfy; //管理费用
	private Float cwfy; //财务费用
	private Float jyfy; //经营费用
	private Float yywsr; //营业外收入
	private Float yywzc; //营业外支出
	private Float sds; //所得税
	private Float jlr; //净利润
	
	private Date lrsj;	    //录入时间
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Float getZyywlr() {
		return zyywlr;
	}
	public void setZyywlr(Float zyywlr) {
		this.zyywlr = zyywlr;
	}
	public Float getGlfy() {
		return glfy;
	}
	public void setGlfy(Float glfy) {
		this.glfy = glfy;
	}
	public Float getCwfy() {
		return cwfy;
	}
	public void setCwfy(Float cwfy) {
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
	public Float getYywzc() {
		return yywzc;
	}
	public void setYywzc(Float yywzc) {
		this.yywzc = yywzc;
	}
	public Float getSds() {
		return sds;
	}
	public void setSds(Float sds) {
		this.sds = sds;
	}
	public Float getJlr() {
		return jlr;
	}
	public void setJlr(Float jlr) {
		this.jlr = jlr;
	}
	public Date getLrsj() {
		return lrsj;
	}
	public void setLrsj(Date lrsj) {
		this.lrsj = lrsj;
	}
	public Date getNy() {
		return ny;
	}
	public void setNy(Date ny) {
		this.ny = ny;
	}
	
}
