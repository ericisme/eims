package cn.qtone.eims.kjpz.domain;

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
	
	private Float qnye=0f;//去年余额;
	
	private Float lsye=0f;//历史余额;
	
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

	public Float getQnye() {
		return qnye;
	}

	public void setQnye(Float qnye) {
		this.qnye = qnye;
	}

	public Float getLsye() {
		return lsye;
	}

	public void setLsye(Float lsye) {
		this.lsye = lsye;
	}


	
	
	
}
