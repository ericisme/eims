package cn.qtone.eims.kjpz.domain;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 分录
 * @author Administrator
 *
 */
@Entity
@Table(name = "EIMS_FL")
public class Fl {

	private Integer id; // id
	
	private Pz pz;//所属凭证
	
	private String rq;//日期
	
	private String zy;//摘要
	
	private Kmgl kmgl;//所属科目 
	
	private BigDecimal jfje;//借方金额
	
	private BigDecimal dfje;//贷方金额 
	
	private Integer lx=1;//类型，1为凭证分录，2为期末余额分录(2013年期末)，3为历史余额分录

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="pz_id", nullable=true)
	public Pz getPz() {
		return pz;
	}

	public void setPz(Pz pz) {
		this.pz = pz;
	}

	public String getZy() {
		return zy;
	}

	public void setZy(String zy) {
		this.zy = zy;
	}

	public String getRq() {
		return rq;
	}

	public void setRq(String rq) {
		this.rq = rq;
	}

	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.ALL }, optional=false )
	@JoinColumn(name="kmgl_id", nullable=false)
	public Kmgl getKmgl() {
		return kmgl;
	}

	public void setKmgl(Kmgl kmgl) {
		this.kmgl = kmgl;
	}

	public BigDecimal getJfje() {
		return jfje;
	}

	public void setJfje(BigDecimal jfje) {
		this.jfje = jfje;
	}

	public BigDecimal getDfje() {
		return dfje;
	}

	public void setDfje(BigDecimal dfje) {
		this.dfje = dfje;
	}

	public Integer getLx() {
		return lx;
	}

	public void setLx(Integer lx) {
		this.lx = lx;
	}
	
	
}
