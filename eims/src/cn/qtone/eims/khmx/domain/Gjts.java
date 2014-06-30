package cn.qtone.eims.khmx.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 国家退税明细
 * @author:周国辉
 * @date:2013-1-18
 */
@Entity
@Table(name = "EIMS_GJTS")
public class Gjts {

	private Integer id; // id
	private String dh; // 单号
	private String khmc; //客户名称
	private String bgdh; //报关单号
	private String zzsl; //增值税率
	private String tsl; //退税率
	private Float tsje; //退税金额
	private Date tsrq; //收退税日期
	private Float ysje; //已收金额
	private Float wsje; //未收金额
	
	private Date lrsj;	    //录入时间

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDh() {
		return dh;
	}

	public void setDh(String dh) {
		this.dh = dh;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	public String getBgdh() {
		return bgdh;
	}

	public void setBgdh(String bgdh) {
		this.bgdh = bgdh;
	}

	public String getZzsl() {
		return zzsl;
	}

	public void setZzsl(String zzsl) {
		this.zzsl = zzsl;
	}

	public String getTsl() {
		return tsl;
	}

	public void setTsl(String tsl) {
		this.tsl = tsl;
	}

	public Float getTsje() {
		return tsje;
	}

	public void setTsje(Float tsje) {
		this.tsje = tsje;
	}

	public Date getTsrq() {
		return tsrq;
	}

	public void setTsrq(Date tsrq) {
		this.tsrq = tsrq;
	}

	public Float getYsje() {
		return ysje;
	}

	public void setYsje(Float ysje) {
		this.ysje = ysje;
	}

	public Float getWsje() {
		return wsje;
	}

	public void setWsje(Float wsje) {
		this.wsje = wsje;
	}

	public Date getLrsj() {
		return lrsj;
	}

	public void setLrsj(Date lrsj) {
		this.lrsj = lrsj;
	}
	
}
