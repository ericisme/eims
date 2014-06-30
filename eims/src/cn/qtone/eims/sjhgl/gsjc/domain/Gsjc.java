package cn.qtone.eims.sjhgl.gsjc.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 公司借出
 * @author Administrator
 *
 */
@Entity
@Table(name = "eims_gsjc")
public class Gsjc {		
	private Integer id;	
	
	private String fyrq;	//借款日期
	private String dh;		//单号
	private String zy;		//摘要
	private String jhrmc;	//借款人名称
	
	private Double jcje;	//借出金额
	private Double ghje;	//归还金额
	private Double ye;		//余额
	
	private Integer lx;    //类型，11=借出,12=归还
	private Double je;	   //金额
	
	private Date lrsj;	    //录入时间
	
	@Transient
	private String lxChinese;    //类型，11=借出,12=归还
	@Transient
	public String getLxChinese() {if(lx==11)return "借出";if(lx==12)	return "归还";return "";}
	
	
	
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
	public String getDh() {
		return dh;
	}
	public void setDh(String dh) {
		this.dh = dh;
	}
	public String getZy() {
		return zy;
	}
	public void setZy(String zy) {
		this.zy = zy;
	}
	public String getJhrmc() {
		return jhrmc;
	}
	public void setJhrmc(String jhrmc) {
		this.jhrmc = jhrmc;
	}
	public Double getJcje() {
		return jcje;
	}
	public void setJcje(Double jcje) {
		this.jcje = jcje;
	}
	public Double getGhje() {
		return ghje;
	}
	public void setGhje(Double ghje) {
		this.ghje = ghje;
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
	public Integer getLx() {
		return lx;
	}
	public void setLx(Integer lx) {
		this.lx = lx;
	}
	public Double getJe() {
		return je;
	}
	public void setJe(Double je) {
		this.je = je;
	}
	
}
