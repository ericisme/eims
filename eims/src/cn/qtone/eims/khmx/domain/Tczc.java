package cn.qtone.eims.khmx.domain;

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
	private Float bgje; //报关金额
	private String tcgsmc; //拖车公司名称
	private String gh; //柜号
	private String qyg; //起运港
	private Float tcf; //拖车费
	private Float ydtgf; //异地提柜费
	private Float gpf; //过磅费
	private Float ldzgf; //两地装柜费
	private Float ddf; //打单费
	private Float zgf; //重柜费
	private Float fyjehj; //费用金额合计
	private String zfrq; //支付日期
	private Float zfje; //支付金额
	private Float wfje; //未付金额
	
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
	public Float getBgje() {
		return bgje;
	}
	public void setBgje(Float bgje) {
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
	public Float getTcf() {
		return tcf;
	}
	public void setTcf(Float tcf) {
		this.tcf = tcf;
	}
	public Float getYdtgf() {
		return ydtgf;
	}
	public void setYdtgf(Float ydtgf) {
		this.ydtgf = ydtgf;
	}
	public Float getGpf() {
		return gpf;
	}
	public void setGpf(Float gpf) {
		this.gpf = gpf;
	}
	public Float getLdzgf() {
		return ldzgf;
	}
	public void setLdzgf(Float ldzgf) {
		this.ldzgf = ldzgf;
	}
	public Float getDdf() {
		return ddf;
	}
	public void setDdf(Float ddf) {
		this.ddf = ddf;
	}
	public Float getZgf() {
		return zgf;
	}
	public void setZgf(Float zgf) {
		this.zgf = zgf;
	}
	public Float getFyjehj() {
		return fyjehj;
	}
	public void setFyjehj(Float fyjehj) {
		this.fyjehj = fyjehj;
	}
	public String getZfrq() {
		return zfrq;
	}
	public void setZfrq(String zfrq) {
		this.zfrq = zfrq;
	}
	public Float getZfje() {
		return zfje;
	}
	public void setZfje(Float zfje) {
		this.zfje = zfje;
	}
	public Float getWfje() {
		return wfje;
	}
	public void setWfje(Float wfje) {
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
