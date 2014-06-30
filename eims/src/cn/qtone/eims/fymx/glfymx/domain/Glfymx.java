package cn.qtone.eims.fymx.glfymx.domain;

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
	
	private Double gz;	//工资
	private Double sbf;	//社保费
	private Double zjf;	//折旧费
	private Double sdf;	//水电费
	private Double dhf;	//电话费
	private Double bgf;	//办公费
	private Double clf;	//差旅费
	private Double qyf;	//汽油费
	private Double twf;	//堤围费
	private Double kdf;	//快递费
	private Double yhs;	//印花税
	private Double zj;	//租金
	private Double zcf;	//租车费
	private Double jzf;	//建账费
	private Double qt;	//其他
	private Double hj;	//合计
	
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
	public Double getGz() {
		return gz;
	}
	public void setGz(Double gz) {
		this.gz = gz;
	}
	public Double getSbf() {
		return sbf;
	}
	public void setSbf(Double sbf) {
		this.sbf = sbf;
	}
	public Double getZjf() {
		return zjf;
	}
	public void setZjf(Double zjf) {
		this.zjf = zjf;
	}
	public Double getSdf() {
		return sdf;
	}
	public void setSdf(Double sdf) {
		this.sdf = sdf;
	}
	public Double getDhf() {
		return dhf;
	}
	public void setDhf(Double dhf) {
		this.dhf = dhf;
	}
	public Double getBgf() {
		return bgf;
	}
	public void setBgf(Double bgf) {
		this.bgf = bgf;
	}
	public Double getClf() {
		return clf;
	}
	public void setClf(Double clf) {
		this.clf = clf;
	}
	public Double getQyf() {
		return qyf;
	}
	public void setQyf(Double qyf) {
		this.qyf = qyf;
	}
	public Double getTwf() {
		return twf;
	}
	public void setTwf(Double twf) {
		this.twf = twf;
	}
	public Double getKdf() {
		return kdf;
	}
	public void setKdf(Double kdf) {
		this.kdf = kdf;
	}
	public Double getYhs() {
		return yhs;
	}
	public void setYhs(Double yhs) {
		this.yhs = yhs;
	}
	public Double getZj() {
		return zj;
	}
	public void setZj(Double zj) {
		this.zj = zj;
	}
	public Double getZcf() {
		return zcf;
	}
	public void setZcf(Double zcf) {
		this.zcf = zcf;
	}
	public Double getJzf() {
		return jzf;
	}
	public void setJzf(Double jzf) {
		this.jzf = jzf;
	}
	public Double getQt() {
		return qt;
	}
	public void setQt(Double qt) {
		this.qt = qt;
	}
	public Double getHj() {
		return hj;
	}
	public void setHj(Double hj) {
		this.hj = hj;
	}
	public Date getLrsj() {
		return lrsj;
	}
	public void setLrsj(Date lrsj) {
		this.lrsj = lrsj;
	}
		
}
