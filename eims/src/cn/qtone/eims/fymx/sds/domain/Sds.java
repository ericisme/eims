package cn.qtone.eims.fymx.sds.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 所得税
 * @author Administrator
 *
 */
@Entity
@Table(name = "eims_sds")
public class Sds {		
	private Integer id;	
	
	private String fyrq;	//费用日期
	private Double dybgje;	//当月报关金额
	private Double hl;		//汇率
	private Double by1;		//备用1
	private Double by2;		//备用2
	private Double je;		//金额
	
	private Date lrsj;	    //录入时间
	
	private String dh;		//单号
	
	
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
	public Double getDybgje() {
		return dybgje;
	}
	public void setDybgje(Double dybgje) {
		this.dybgje = dybgje;
	}
	public Double getHl() {
		return hl;
	}
	public void setHl(Double hl) {
		this.hl = hl;
	}
	public Double getBy1() {
		return by1;
	}
	public void setBy1(Double by1) {
		this.by1 = by1;
	}
	public Double getBy2() {
		return by2;
	}
	public void setBy2(Double by2) {
		this.by2 = by2;
	}
	public Double getJe() {
		return je;
	}
	public void setJe(Double je) {
		this.je = je;
	}
	public Date getLrsj() {
		return lrsj;
	}
	public void setLrsj(Date lrsj) {
		this.lrsj = lrsj;
	}
	public String getDh() {
		return dh;
	}
	public void setDh(String dh) {
		this.dh = dh;
	}

		
}
