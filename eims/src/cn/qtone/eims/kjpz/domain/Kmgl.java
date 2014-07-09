package cn.qtone.eims.kjpz.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 会计科目管理，只有一级
 * @author eric
 *
 */
@Entity
@Table(name = "EIMS_KMGL")
public class Kmgl {

	private Integer id; // id
	
	private String kmdh;//科目代号
	
	private String kmmc;//科目名称
	
	private Integer zt=1;//状态
	
	private BigDecimal qnye_jf= new BigDecimal(0f);//去年余额,借方;
	private BigDecimal qnye_df= new BigDecimal(0f);//去年余额,贷方;
	
	private BigDecimal lsye_jf= new BigDecimal(0f);//历史余额,借方;
	private BigDecimal lsye_df= new BigDecimal(0f);//历史余额,贷方;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKmdh() {
		return kmdh;
	}

	public void setKmdh(String kmdh) {
		this.kmdh = kmdh;
	}

	public String getKmmc() {
		return kmmc;
	}

	public void setKmmc(String kmmc) {
		this.kmmc = kmmc;
	}

	public Integer getZt() {
		return zt;
	}

	public void setZt(Integer zt) {
		this.zt = zt;
	}

	public BigDecimal getQnye_jf() {
		return qnye_jf;
	}

	public void setQnye_jf(BigDecimal qnye_jf) {
		this.qnye_jf = qnye_jf;
	}

	public BigDecimal getQnye_df() {
		return qnye_df;
	}

	public void setQnye_df(BigDecimal qnye_df) {
		this.qnye_df = qnye_df;
	}

	public BigDecimal getLsye_jf() {
		return lsye_jf;
	}

	public void setLsye_jf(BigDecimal lsye_jf) {
		this.lsye_jf = lsye_jf;
	}

	public BigDecimal getLsye_df() {
		return lsye_df;
	}

	public void setLsye_df(BigDecimal lsye_df) {
		this.lsye_df = lsye_df;
	}






	
	
	
}
