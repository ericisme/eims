package cn.qtone.eims.fymx.sds.domain;

import java.math.BigDecimal;
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
	private BigDecimal dybgje;	//当月报关金额
	private BigDecimal hl;		//汇率
	private BigDecimal by1;		//备用1
	private BigDecimal by2;		//备用2
	private BigDecimal je;		//金额
	
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
	public BigDecimal getDybgje() {
		return dybgje;
	}
	public void setDybgje(BigDecimal dybgje) {
		this.dybgje = dybgje;
	}
	public BigDecimal getHl() {
		return hl;
	}
	public void setHl(BigDecimal hl) {
		this.hl = hl;
	}
	public BigDecimal getBy1() {
		return by1;
	}
	public void setBy1(BigDecimal by1) {
		this.by1 = by1;
	}
	public BigDecimal getBy2() {
		return by2;
	}
	public void setBy2(BigDecimal by2) {
		this.by2 = by2;
	}
	public BigDecimal getJe() {
		return je;
	}
	public void setJe(BigDecimal je) {
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
