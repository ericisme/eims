package cn.qtone.eims.fymx.yggz.domain;

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
	private Double jbgz;	//基本工资
	private Double tc;		//提成
	private Double yfgz;	//应付工资， 应付工资=基本工资+提成+补贴
	private Double sbf;		//社保费
	private Double khgz;	//扣回工资
	private Double sfgz;	//实发工资，实发工资=应付工资-社保费-扣回工资
	private String gzrq;	//工资日期（年-月-日）
	private Date lrsj;	    //录入时间
	
	private Double bt;		//补贴
	
	
	
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
	public Double getJbgz() {
		return jbgz;
	}
	public void setJbgz(Double jbgz) {
		this.jbgz = jbgz;
	}
	public Double getTc() {
		return tc;
	}
	public void setTc(Double tc) {
		this.tc = tc;
	}
	public Double getYfgz() {
		return yfgz;
	}
	public void setYfgz(Double yfgz) {
		this.yfgz = yfgz;
	}
	public Double getSbf() {
		return sbf;
	}
	public void setSbf(Double sbf) {
		this.sbf = sbf;
	}
	public Double getKhgz() {
		return khgz;
	}
	public void setKhgz(Double khgz) {
		this.khgz = khgz;
	}
	public Double getSfgz() {
		return sfgz;
	}
	public void setSfgz(Double sfgz) {
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
	public Double getBt() {
		return bt;
	}
	public void setBt(Double bt) {
		this.bt = bt;
	}


	

	
}
