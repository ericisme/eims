package cn.qtone.eims.fymx.gdzc.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 固定资产
 * @author Administrator
 *
 */
@Entity
@Table(name = "eims_gdzc")
public class Gdzc {		
	private Integer id;	
	
	private String sbxmmc;	//设备项目名称
	private String dw;		//单位
	private Integer sl;		//数量
	private Double dj;		//单价
	private Double zje;		//总金额
	private Integer zjyf;	//折旧月份
	private Double myzjje;	//每月折旧金额
	private Integer yzjyf;	//已折旧月份
	private Double yzjje;	//已折旧金额
	private Double ye;		//余额
	
	private Date lrsj;	    //录入时间
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSbxmmc() {
		return sbxmmc;
	}
	public void setSbxmmc(String sbxmmc) {
		this.sbxmmc = sbxmmc;
	}
	public String getDw() {
		return dw;
	}
	public void setDw(String dw) {
		this.dw = dw;
	}
	public Integer getSl() {
		return sl;
	}
	public void setSl(Integer sl) {
		this.sl = sl;
	}
	public Double getDj() {
		return dj;
	}
	public void setDj(Double dj) {
		this.dj = dj;
	}
	public Double getZje() {
		return zje;
	}
	public void setZje(Double zje) {
		this.zje = zje;
	}
	public Integer getZjyf() {
		return zjyf;
	}
	public void setZjyf(Integer zjyf) {
		this.zjyf = zjyf;
	}
	public Double getMyzjje() {
		return myzjje;
	}
	public void setMyzjje(Double myzjje) {
		this.myzjje = myzjje;
	}
	public Integer getYzjyf() {
		return yzjyf;
	}
	public void setYzjyf(Integer yzjyf) {
		this.yzjyf = yzjyf;
	}
	public Double getYzjje() {
		return yzjje;
	}
	public void setYzjje(Double yzjje) {
		this.yzjje = yzjje;
	}
	public Double getYe() {
		return ye;
	}
	public void setYe(Double ye) {
		this.ye = ye;
	}
	public Date getLrsj() {
		return lrsj;
	}
	public void setLrsj(Date lrsj) {
		this.lrsj = lrsj;
	}
		
}
