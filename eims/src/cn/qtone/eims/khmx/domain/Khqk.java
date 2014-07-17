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
 * 客户欠款明细
 * @author:周国辉
 * @date:2013-1-19
 */
@Entity
@Table(name = "EIMS_KHQK")
public class Khqk {

	private Integer id; // id
	private String bgdh; //报关单号
	private String dh; //单号
	private String khmc; //客户名称
	private Date bgrq; //报关日期	
	private BigDecimal bgje = new BigDecimal(0); //报关金额
	private BigDecimal dlf = new BigDecimal(0); //代理费
	private BigDecimal bgf = new BigDecimal(0); //报关费
	private BigDecimal gjf = new BigDecimal(0); //港建费
	private BigDecimal sjf = new BigDecimal(0); //商检费
	private BigDecimal gj = new BigDecimal(0); //国检
	private BigDecimal xyf = new BigDecimal(0); //续页费
	private BigDecimal lgf = new BigDecimal(0); //包柜费
	private BigDecimal tcf = new BigDecimal(0); //拖车费
	private BigDecimal smf = new BigDecimal(0); //扫描费
	private BigDecimal cgf = new BigDecimal(0); //查柜费
	private BigDecimal xzf = new BigDecimal(0); //熏蒸费
	private BigDecimal jq = new BigDecimal(0); //加签
	private BigDecimal qt = new BigDecimal(0); //其他
	private String skrq; //收款日期
	private BigDecimal ysje = new BigDecimal(0); //已收金额
	private BigDecimal wsje = new BigDecimal(0); //未收金额
	private BigDecimal hj = new BigDecimal(0); //合计
	
	private BigDecimal xyzf = new BigDecimal(0); //信用证费
	private BigDecimal cdzf = new BigDecimal(0); //产地证费
	private BigDecimal kbdzf = new BigDecimal(0); //空白单证费
	private BigDecimal kdf = new BigDecimal(0); //快递费
	private BigDecimal bcf = new BigDecimal(0); //驳船费
	private BigDecimal ftf = new BigDecimal(0); //封条费
	private BigDecimal cdf = new BigDecimal(0); //仓单费
	private BigDecimal gpf = new BigDecimal(0); //过磅费
	private BigDecimal hzptf = new BigDecimal(0); //换证凭条费
	
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

	public BigDecimal getDlf() {
		return dlf;
	}

	public void setDlf(BigDecimal dlf) {
		this.dlf = dlf;
	}

	public BigDecimal getBgf() {
		return bgf;
	}

	public void setBgf(BigDecimal bgf) {
		this.bgf = bgf;
	}

	public BigDecimal getGjf() {
		return gjf;
	}

	public void setGjf(BigDecimal gjf) {
		this.gjf = gjf;
	}

	public BigDecimal getSjf() {
		return sjf;
	}

	public void setSjf(BigDecimal sjf) {
		this.sjf = sjf;
	}

	public BigDecimal getXyf() {
		return xyf;
	}

	public void setXyf(BigDecimal xyf) {
		this.xyf = xyf;
	}

	public BigDecimal getLgf() {
		return lgf;
	}

	public void setLgf(BigDecimal lgf) {
		this.lgf = lgf;
	}

	public BigDecimal getTcf() {
		return tcf;
	}

	public void setTcf(BigDecimal tcf) {
		this.tcf = tcf;
	}

	public BigDecimal getSmf() {
		return smf;
	}

	public void setSmf(BigDecimal smf) {
		this.smf = smf;
	}

	public BigDecimal getCgf() {
		return cgf;
	}

	public void setCgf(BigDecimal cgf) {
		this.cgf = cgf;
	}

	public BigDecimal getXzf() {
		return xzf;
	}

	public void setXzf(BigDecimal xzf) {
		this.xzf = xzf;
	}

	public BigDecimal getJq() {
		return jq;
	}

	public void setJq(BigDecimal jq) {
		this.jq = jq;
	}

	public BigDecimal getQt() {
		return qt;
	}

	public void setQt(BigDecimal qt) {
		this.qt = qt;
	}

	public String getSkrq() {
		return skrq;
	}

	public void setSkrq(String skrq) {
		this.skrq = skrq;
	}

	public BigDecimal getYsje() {
		return ysje;
	}

	public void setYsje(BigDecimal ysje) {
		this.ysje = ysje;
	}

	public BigDecimal getWsje() {
		return wsje;
	}

	public void setWsje(BigDecimal wsje) {
		this.wsje = wsje;
	}

	public Date getLrsj() {
		return lrsj;
	}

	public void setLrsj(Date lrsj) {
		this.lrsj = lrsj;
	}

	public BigDecimal getGj() {
		return gj;
	}

	public void setGj(BigDecimal gj) {
		this.gj = gj;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	public BigDecimal getHj() {
		return hj;
	}

	public void setHj(BigDecimal hj) {
		this.hj = hj;
	}

	public BigDecimal getXyzf() {
		return xyzf;
	}

	public void setXyzf(BigDecimal xyzf) {
		this.xyzf = xyzf;
	}

	public BigDecimal getCdzf() {
		return cdzf;
	}

	public void setCdzf(BigDecimal cdzf) {
		this.cdzf = cdzf;
	}

	public BigDecimal getKbdzf() {
		return kbdzf;
	}

	public void setKbdzf(BigDecimal kbdzf) {
		this.kbdzf = kbdzf;
	}

	public BigDecimal getKdf() {
		return kdf;
	}

	public void setKdf(BigDecimal kdf) {
		this.kdf = kdf;
	}

	public BigDecimal getBcf() {
		return bcf;
	}

	public void setBcf(BigDecimal bcf) {
		this.bcf = bcf;
	}

	public BigDecimal getFtf() {
		return ftf;
	}

	public void setFtf(BigDecimal ftf) {
		this.ftf = ftf;
	}

	public BigDecimal getCdf() {
		return cdf;
	}

	public void setCdf(BigDecimal cdf) {
		this.cdf = cdf;
	}

	public BigDecimal getGpf() {
		return gpf;
	}

	public void setGpf(BigDecimal gpf) {
		this.gpf = gpf;
	}

	public BigDecimal getHzptf() {
		return hzptf;
	}

	public void setHzptf(BigDecimal hzptf) {
		this.hzptf = hzptf;
	}
	
}
