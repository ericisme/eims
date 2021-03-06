package cn.qtone.eims.khmx.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 客户退税
 * @author:周国辉
 * @date:2013-1-18
 */
@Entity
@Table(name = "EIMS_KHTS")
public class Khts {

	private Integer id; // id
	private String bgdh; //报关单号
	private String dh; //单号
	private String khmc; //客户名称
	private Date bgrq; //报关日期	
	private BigDecimal bgje = new BigDecimal(0); //报关金额
	private BigDecimal fpje = new BigDecimal(0); //发票金额
	private String sfprq; //收发票日期
	private BigDecimal tsje = new BigDecimal(0); //退税金额
	private String zftsrq; //支付退税日期
	private BigDecimal yfje = new BigDecimal(0); //已付金额
	private BigDecimal wfje = new BigDecimal(0); //未付金额
	private String fkdh; //付款单号
	private String ywy; //业务员
	private String dlfbz; //代理费标准
	
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

	public Date getBgrq() {
		return bgrq;
	}

	public void setBgrq(Date bgrq) {
		this.bgrq = bgrq;
	}

	public String getBgdh() {
		return bgdh;
	}

	public void setBgdh(String bgdh) {
		this.bgdh = bgdh;
	}

	public BigDecimal getBgje() {
		return bgje;
	}

	public void setBgje(BigDecimal bgje) {
		this.bgje = bgje;
	}

	public BigDecimal getFpje() {
		return fpje;
	}

	public void setFpje(BigDecimal fpje) {
		this.fpje = fpje;
	}

	public String getSfprq() {
		return sfprq;
	}

	public void setSfprq(String sfprq) {
		this.sfprq = sfprq;
	}

	public BigDecimal getTsje() {
		return tsje;
	}

	public void setTsje(BigDecimal tsje) {
		this.tsje = tsje;
	}

	public String getZftsrq() {
		return zftsrq;
	}

	public void setZftsrq(String zftsrq) {
		this.zftsrq = zftsrq;
	}

	public BigDecimal getYfje() {
		return yfje;
	}

	public void setYfje(BigDecimal yfje) {
		this.yfje = yfje;
	}

	public BigDecimal getWfje() {
		return wfje;
	}

	public void setWfje(BigDecimal wfje) {
		this.wfje = wfje;
	}

	public String getFkdh() {
		return fkdh;
	}

	public void setFkdh(String fkdh) {
		this.fkdh = fkdh;
	}

	public Date getLrsj() {
		return lrsj;
	}

	public void setLrsj(Date lrsj) {
		this.lrsj = lrsj;
	}

	public String getYwy() {
		return ywy;
	}

	public void setYwy(String ywy) {
		this.ywy = ywy;
	}

	public String getDlfbz() {
		return dlfbz;
	}

	public void setDlfbz(String dlfbz) {
		this.dlfbz = dlfbz;
	}
	
	
}
