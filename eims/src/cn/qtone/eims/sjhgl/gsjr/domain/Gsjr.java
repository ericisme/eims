package cn.qtone.eims.sjhgl.gsjr.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 公司借入
 * @author Administrator
 *
 */
@Entity
@Table(name = "eims_gsjr")
public class Gsjr {		
	private Integer id;	
	
	private String fyrq;	//借款日期
	private String dh;		//单号
	private String zy;		//摘要
	private String jhrmc;	//借款人名称
	
	private Double jrje;	//借入金额
	private Double hdje;	//还贷金额
	private Double ye;		//余额
	
	
	private Integer lx;    //类型，21=借入,22=还贷
	private Double je;	   //金额
	
	private Date lrsj;	    //录入时间
	
	
	@Transient
	private String lxChinese;    //类型，21=借入,22=还贷
	@Transient
	public String getLxChinese() {if(lx==21)return "借入";if(lx==22)	return "还贷";return "";}
	
	
	
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
	public Double getJrje() {
		return jrje;
	}
	public void setJrje(Double jrje) {
		this.jrje = jrje;
	}
	public Double getHdje() {
		return hdje;
	}
	public void setHdje(Double hdje) {
		this.hdje = hdje;
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
