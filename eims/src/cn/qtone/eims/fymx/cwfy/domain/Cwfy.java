package cn.qtone.eims.fymx.cwfy.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 财务费用明细或营业外支出
 * @author Administrator
 *
 */
@Entity
@Table(name = "eims_cwfy")
public class Cwfy {	
	
	private Integer id;	
	
	private String type; 	//类型，101=贷款利息，102=存款利息，103=银行手续费，104=汇兑损益，201=营业外支出
	private String fyrq;	//费用日期（年-月-日）
	private String dh;		//单号
	private String zy;		//摘要
	private Double je;		//金额
	private Date lrsj;	    //录入时间
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	
		
}
