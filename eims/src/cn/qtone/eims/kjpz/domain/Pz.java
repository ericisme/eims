package cn.qtone.eims.kjpz.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * 凭证
 * @author eric
 *
 */
@Entity
@Table(name = "EIMS_PZ")
public class Pz {

	private Integer id; // id
	
	private String rq;//日期
	
	private String pzz="记"; //凭证字
	
	private Integer pzh;//凭证号
	
	private Integer fdj=1;//附单据
	
	private BigDecimal hj_jfje;//合计_借方金额
	
	private BigDecimal hj_dfje;//合计_贷方金额 
	
	private String shr;//审核人
	
	private String gzr;//过账人
	
	private String zdr;//制单人

	/**
	 * 包含分录
	 */
	private List<Fl> flList = new ArrayList<Fl>();
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRq() {
		return rq;
	}

	public void setRq(String rq) {
		this.rq = rq;
	}

	public String getPzz() {
		return pzz;
	}

	public void setPzz(String pzz) {
		this.pzz = pzz;
	}

	public Integer getPzh() {
		return pzh;
	}

	public void setPzh(Integer pzh) {
		this.pzh = pzh;
	}

	public Integer getFdj() {
		return fdj;
	}

	public void setFdj(Integer fdj) {
		this.fdj = fdj;
	}

	public BigDecimal getHj_jfje() {
		return hj_jfje;
	}

	public void setHj_jfje(BigDecimal hj_jfje) {
		this.hj_jfje = hj_jfje;
	}

	public BigDecimal getHj_dfje() {
		return hj_dfje;
	}

	public void setHj_dfje(BigDecimal hj_dfje) {
		this.hj_dfje = hj_dfje;
	}

	public String getShr() {
		return shr;
	}

	public void setShr(String shr) {
		this.shr = shr;
	}

	public String getGzr() {
		return gzr;
	}

	public void setGzr(String gzr) {
		this.gzr = gzr;
	}

	public String getZdr() {
		return zdr;
	}

	public void setZdr(String zdr) {
		this.zdr = zdr;
	}
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="pz_id")
	public List<Fl> getFlList() {
		return flList;
	}

	public void setFlList(List<Fl> flList) {
		this.flList = flList;
	}
	
	
	
	
}
