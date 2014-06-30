package cn.qtone.eims.khmx.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 营业外收入
 * @author:周国辉
 * @date:2013-1-23
 */
@Entity
@Table(name = "EIMS_YYWSR")
public class Yywsr {

	private Integer id; // id
	private Date ny; //年月
	private String dh; //单号
	private String zy; //摘要
	private Float je; //金额
	
	private Date lrsj;	    //录入时间
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getNy() {
		return ny;
	}
	public void setNy(Date ny) {
		this.ny = ny;
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
	public Float getJe() {
		return je;
	}
	public void setJe(Float je) {
		this.je = je;
	}
	public Date getLrsj() {
		return lrsj;
	}
	public void setLrsj(Date lrsj) {
		this.lrsj = lrsj;
	}
	
	
}
