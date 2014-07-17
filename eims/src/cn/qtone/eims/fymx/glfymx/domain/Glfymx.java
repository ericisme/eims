package cn.qtone.eims.fymx.glfymx.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 管理费用明细
 * @author Administrator
 *
 */
@Entity
@Table(name = "eims_glfymx")
public class Glfymx {	
	
	private Integer id;	
	
	private String fyrq;	//费用日期（年-月-日）
	
	private BigDecimal gz;	//工资
	private BigDecimal sbf;	//社保费
	private BigDecimal zjf;	//折旧费
	private BigDecimal sdf;	//水电费
	private BigDecimal dhf;	//电话费
	private BigDecimal bgf;	//办公费
	private BigDecimal clf;	//差旅费
	private BigDecimal qyf;	//汽油费
	private BigDecimal twf;	//堤围费
	private BigDecimal kdf;	//快递费
	private BigDecimal yhs;	//印花税
	private BigDecimal zj;	//租金
	private BigDecimal zcf;	//租车费
	private BigDecimal jzf;	//建账费
	private BigDecimal qt;	//其他
	private BigDecimal hj;	//合计
	
	private Date lrsj;	    //录入时间

	
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFyrq() {
		return fyrq;
	}
	public void setFyrq(String fyrq) {
		this.fyrq = fyrq;
	}
	public BigDecimal getGz() {
		return gz;
	}
	public void setGz(BigDecimal gz) {
		this.gz = gz;
	}
	public BigDecimal getSbf() {
		return sbf;
	}
	public void setSbf(BigDecimal sbf) {
		this.sbf = sbf;
	}
	public BigDecimal getZjf() {
		return zjf;
	}
	public void setZjf(BigDecimal zjf) {
		this.zjf = zjf;
	}
	public BigDecimal getSdf() {
		return sdf;
	}
	public void setSdf(BigDecimal sdf) {
		this.sdf = sdf;
	}
	public BigDecimal getDhf() {
		return dhf;
	}
	public void setDhf(BigDecimal dhf) {
		this.dhf = dhf;
	}
	public BigDecimal getBgf() {
		return bgf;
	}
	public void setBgf(BigDecimal bgf) {
		this.bgf = bgf;
	}
	public BigDecimal getClf() {
		return clf;
	}
	public void setClf(BigDecimal clf) {
		this.clf = clf;
	}
	public BigDecimal getQyf() {
		return qyf;
	}
	public void setQyf(BigDecimal qyf) {
		this.qyf = qyf;
	}
	public BigDecimal getTwf() {
		return twf;
	}
	public void setTwf(BigDecimal twf) {
		this.twf = twf;
	}
	public BigDecimal getKdf() {
		return kdf;
	}
	public void setKdf(BigDecimal kdf) {
		this.kdf = kdf;
	}
	public BigDecimal getYhs() {
		return yhs;
	}
	public void setYhs(BigDecimal yhs) {
		this.yhs = yhs;
	}
	public BigDecimal getZj() {
		return zj;
	}
	public void setZj(BigDecimal zj) {
		this.zj = zj;
	}
	public BigDecimal getZcf() {
		return zcf;
	}
	public void setZcf(BigDecimal zcf) {
		this.zcf = zcf;
	}
	public BigDecimal getJzf() {
		return jzf;
	}
	public void setJzf(BigDecimal jzf) {
		this.jzf = jzf;
	}
	public BigDecimal getQt() {
		return qt;
	}
	public void setQt(BigDecimal qt) {
		this.qt = qt;
	}
	public BigDecimal getHj() {
		return hj;
	}
	public void setHj(BigDecimal hj) {
		this.hj = hj;
	}
	public Date getLrsj() {
		return lrsj;
	}
	public void setLrsj(Date lrsj) {
		this.lrsj = lrsj;
	}
		
}
