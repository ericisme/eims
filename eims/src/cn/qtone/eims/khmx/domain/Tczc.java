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
 * 拖车支出明细表
 * @author:周国辉
 * @date:2013-1-29
 */
@Entity
@Table(name = "EIMS_TCZC")
public class Tczc {

	private Integer id; // id
	private String bgdh; //报关单号
	private String dh; //单号
	private String khmc; //客户名称
	private Date bgrq; //报关日期	
	private BigDecimal bgje = new BigDecimal(0); //报关金额
	private String tcgsmc; //拖车公司名称
	private String gh; //柜号
	private String qyg; //起运港
	private BigDecimal tcf = new BigDecimal(0); //拖车费
	private BigDecimal ydtgf = new BigDecimal(0); //异地提柜费
	private BigDecimal gpf = new BigDecimal(0); //过磅费
	private BigDecimal ldzgf = new BigDecimal(0); //两地装柜费
	private BigDecimal ddf = new BigDecimal(0); //打单费
	private BigDecimal zgf = new BigDecimal(0); //重柜费
	private BigDecimal fyjehj = new BigDecimal(0); //费用金额合计
	private String zfrq; //支付日期
	private BigDecimal zfje = new BigDecimal(0); //支付金额
	private BigDecimal wfje = new BigDecimal(0); //未付金额
	
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
	public String getGh() {
		return gh;
	}
	public void setGh(String gh) {
		this.gh = gh;
	}
	public String getQyg() {
		return qyg;
	}
	public void setQyg(String qyg) {
		this.qyg = qyg;
	}
	public BigDecimal getTcf() {
		return tcf;
	}
	public void setTcf(BigDecimal tcf) {
		this.tcf = tcf;
	}
	public BigDecimal getYdtgf() {
		return ydtgf;
	}
	public void setYdtgf(BigDecimal ydtgf) {
		this.ydtgf = ydtgf;
	}
	public BigDecimal getGpf() {
		return gpf;
	}
	public void setGpf(BigDecimal gpf) {
		this.gpf = gpf;
	}
	public BigDecimal getLdzgf() {
		return ldzgf;
	}
	public void setLdzgf(BigDecimal ldzgf) {
		this.ldzgf = ldzgf;
	}
	public BigDecimal getDdf() {
		return ddf;
	}
	public void setDdf(BigDecimal ddf) {
		this.ddf = ddf;
	}
	public BigDecimal getZgf() {
		return zgf;
	}
	public void setZgf(BigDecimal zgf) {
		this.zgf = zgf;
	}
	public BigDecimal getFyjehj() {
		return fyjehj;
	}
	public void setFyjehj(BigDecimal fyjehj) {
		this.fyjehj = fyjehj;
	}
	public String getZfrq() {
		return zfrq;
	}
	public void setZfrq(String zfrq) {
		this.zfrq = zfrq;
	}
	public BigDecimal getZfje() {
		return zfje;
	}
	public void setZfje(BigDecimal zfje) {
		this.zfje = zfje;
	}
	public BigDecimal getWfje() {
		return wfje;
	}
	public void setWfje(BigDecimal wfje) {
		this.wfje = wfje;
	}
	public Date getLrsj() {
		return lrsj;
	}
	public void setLrsj(Date lrsj) {
		this.lrsj = lrsj;
	}
	public String getTcgsmc() {
		return tcgsmc;
	}
	public void setTcgsmc(String tcgsmc) {
		this.tcgsmc = tcgsmc;
	}
	
}
