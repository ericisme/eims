package cn.qtone.eims.fymx.yggz.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 员工工资
 * @author Administrator
 *
 */
@Entity
@Table(name = "eims_yggz")
public class Yggz {	
	
	private Integer id;	
	private String ygbh;	//员工编号
	private String ygxm;	//员工姓名
	private String xb;		//性别（直接保存中文）
	private BigDecimal jbgz;	//基本工资
	private BigDecimal tc;		//提成
	private BigDecimal yfgz;	//应付工资， 应付工资=基本工资+提成+补贴
	private BigDecimal sbf;		//社保费
	private BigDecimal khgz;	//扣回工资
	private BigDecimal sfgz;	//实发工资，实发工资=应付工资-社保费-扣回工资
	private String gzrq;	//工资日期（年-月-日）
	private Date lrsj;	    //录入时间
	
	private BigDecimal bt;		//补贴
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getYgbh() {
		return ygbh;
	}
	public void setYgbh(String ygbh) {
		this.ygbh = ygbh;
	}
	public String getYgxm() {
		return ygxm;
	}
	public void setYgxm(String ygxm) {
		this.ygxm = ygxm;
	}
	public String getXb() {
		return xb;
	}
	public void setXb(String xb) {
		this.xb = xb;
	}
	public BigDecimal getJbgz() {
		return jbgz;
	}
	public void setJbgz(BigDecimal jbgz) {
		this.jbgz = jbgz;
	}
	public BigDecimal getTc() {
		return tc;
	}
	public void setTc(BigDecimal tc) {
		this.tc = tc;
	}
	public BigDecimal getYfgz() {
		return yfgz;
	}
	public void setYfgz(BigDecimal yfgz) {
		this.yfgz = yfgz;
	}
	public BigDecimal getSbf() {
		return sbf;
	}
	public void setSbf(BigDecimal sbf) {
		this.sbf = sbf;
	}
	public BigDecimal getKhgz() {
		return khgz;
	}
	public void setKhgz(BigDecimal khgz) {
		this.khgz = khgz;
	}
	public BigDecimal getSfgz() {
		return sfgz;
	}
	public void setSfgz(BigDecimal sfgz) {
		this.sfgz = sfgz;
	}
	public String getGzrq() {
		return gzrq;
	}
	public void setGzrq(String gzrq) {
		this.gzrq = gzrq;
	}
	public Date getLrsj() {
		return lrsj;
	}
	public void setLrsj(Date lrsj) {
		this.lrsj = lrsj;
	}
	public BigDecimal getBt() {
		return bt;
	}
	public void setBt(BigDecimal bt) {
		this.bt = bt;
	}


	

	
}
