package cn.qtone.eims.khmx.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 付款支付
 * @author:周国辉
 * @date:2013-1-19
 */
@Entity
@Table(name = "EIMS_FKZF")
public class Fkzf {

	private Integer id; // id
	private String bgdh; //报关单号
	private String dh; //单号
	private String gsmc; //合作单位
	private String khmc; //客户名称
	private Date bgrq; //报关日期	
	private Float bgje = 0f; //报关金额
	private Float bgf = 0f; //报关费
	private Float gjf = 0f; //港建费
	private Float gj = 0f; //国检
	private Float sjf = 0f; //商检费
	private Float xyf = 0f; //续页费
	private Float lgf = 0f; //包柜费
	private Float tcf = 0f; //拖车费(改成拖车支出明细表的合计金额)
	private Float smf = 0f; //扫描费
	private Float cgf = 0f; //查柜费
	private Float xzf = 0f; //熏蒸费
	private Float jq = 0f; //加签
	private Float qt = 0f; //其他
	private String zfrq; //支付日期
	private Float zfje = 0f; //支付金额
	private Float wfje = 0f; //未付金额
	private Float hj = 0f; //合计
	private String bz; //备注
	
	private Float xyzf = 0f; //信用证费
	private Float cdzf = 0f; //产地证费
	private Float kbdzf = 0f; //空白单证费
	private Float kdf = 0f; //快递费
	private Float bcf = 0f; //驳船费
	private Float ftf = 0f; //封条费
	private Float cdf = 0f; //仓单费
	private Float gpf = 0f; //过磅费
	private Float hzptf = 0f; //换证凭条费
	
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

	public String getGsmc() {
		return gsmc;
	}

	public void setGsmc(String gsmc) {
		this.gsmc = gsmc;
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

	public Float getBgf() {
		return bgf;
	}

	public void setBgf(Float bgf) {
		this.bgf = bgf;
	}

	public Float getGjf() {
		return gjf;
	}

	public void setGjf(Float gjf) {
		this.gjf = gjf;
	}

	public Float getSjf() {
		return sjf;
	}

	public void setSjf(Float sjf) {
		this.sjf = sjf;
	}

	public Float getXyf() {
		return xyf;
	}

	public void setXyf(Float xyf) {
		this.xyf = xyf;
	}

	public Float getLgf() {
		return lgf;
	}

	public void setLgf(Float lgf) {
		this.lgf = lgf;
	}

	public Float getTcf() {
		return tcf;
	}

	public void setTcf(Float tcf) {
		this.tcf = tcf;
	}

	public Float getSmf() {
		return smf;
	}

	public void setSmf(Float smf) {
		this.smf = smf;
	}

	public Float getCgf() {
		return cgf;
	}

	public void setCgf(Float cgf) {
		this.cgf = cgf;
	}

	public Float getXzf() {
		return xzf;
	}

	public void setXzf(Float xzf) {
		this.xzf = xzf;
	}

	public Float getJq() {
		return jq;
	}

	public void setJq(Float jq) {
		this.jq = jq;
	}

	public Float getQt() {
		return qt;
	}

	public void setQt(Float qt) {
		this.qt = qt;
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

	public Float getGj() {
		return gj;
	}

	public void setGj(Float gj) {
		this.gj = gj;
	}

	public Float getHj() {
		return hj;
	}

	public void setHj(Float hj) {
		this.hj = hj;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public Float getXyzf() {
		return xyzf;
	}

	public void setXyzf(Float xyzf) {
		this.xyzf = xyzf;
	}

	public Float getCdzf() {
		return cdzf;
	}

	public void setCdzf(Float cdzf) {
		this.cdzf = cdzf;
	}

	public Float getKbdzf() {
		return kbdzf;
	}

	public void setKbdzf(Float kbdzf) {
		this.kbdzf = kbdzf;
	}

	public Float getKdf() {
		return kdf;
	}

	public void setKdf(Float kdf) {
		this.kdf = kdf;
	}

	public Float getBcf() {
		return bcf;
	}

	public void setBcf(Float bcf) {
		this.bcf = bcf;
	}

	public Float getFtf() {
		return ftf;
	}

	public void setFtf(Float ftf) {
		this.ftf = ftf;
	}

	public Float getCdf() {
		return cdf;
	}

	public void setCdf(Float cdf) {
		this.cdf = cdf;
	}

	public Float getGpf() {
		return gpf;
	}

	public void setGpf(Float gpf) {
		this.gpf = gpf;
	}

	public Float getHzptf() {
		return hzptf;
	}

	public void setHzptf(Float hzptf) {
		this.hzptf = hzptf;
	}
	
}
