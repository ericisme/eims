package cn.qtone.eims.bbgl.khmxb.controller;

import java.math.BigDecimal;


/**
 * 
 * 统计客户明细表Dto2
 * @author:eric 
 * @date:2014-07-03
 */
public class KhmxbDto {
	
	/**
	 * 客户名称
	 */
	private String khmc;//
	
	/**
	 * 合作单位
	 */	
	private String hzdw;//
	
	/**
	 * 报关日期
	 */	
	private String bgrq;//
	
	/**
	 * 报关单号
	 */	
	private String bgdh;//
	
	/**
	 * 报关金额
	 */	
	private BigDecimal bgje = new BigDecimal(0);//
	
	/**
	 * 发票金额
	 */	
	private BigDecimal fpje= new BigDecimal(0);//
	
	/**
	 * 收发票日期
	 */	
	private String sfprq;//
	
	/**
	 * 退税金额
	 */	
	private BigDecimal tsje= new BigDecimal(0);//
	
	/**
	 * 支付退税日期
	 */	
	private String zftsrq;//
	
	/**
	 * 收到国税退税日期
	 */	
	private String sdgstsrq;//
	
	/**
	 * 代理费
	 */	
	private BigDecimal dlf= new BigDecimal(0);//
	
	
	/**
	 * 报关费 收入
	 */	
	private BigDecimal bgf_sr= new BigDecimal(0);//
	
	/**
	 * 报关费 成本
	 */
	private BigDecimal bgf_cb= new BigDecimal(0);//
	
	/**
	 * 广建费 收入
	 */	
	private BigDecimal guangjf_sr= new BigDecimal(0);//
	/**
	 * 广建费 成本
	 */
	private BigDecimal guangjf_cb= new BigDecimal(0);//
	
	/**
	 * 国检费 收入
	 */	
	private BigDecimal goujf_sr= new BigDecimal(0);//
	/**
	 * 国检费 成本
	 */
	private BigDecimal goujf_cb= new BigDecimal(0);//
	
	/**
	 * 商检费 收入
	 */	
	private BigDecimal sjf_sr= new BigDecimal(0);//
	/**
	 * 商检费 成本
	 */
	private BigDecimal sjf_cb= new BigDecimal(0);//
	
	/**
	 * 续页费 收入
	 */	
	private BigDecimal xyf_sr= new BigDecimal(0);//
	/**
	 * 续页费 成本
	 */
	private BigDecimal xyf_cb= new BigDecimal(0);//
	
	/**
	 * 连柜费 收入
	 */	
	private BigDecimal lgf_sr= new BigDecimal(0);//
	/**
	 * 连柜费 成本
	 */
	private BigDecimal lgf_cb= new BigDecimal(0);//
	
	/**
	 * 拖车费 收入
	 */	
	private BigDecimal tcf_sr= new BigDecimal(0);//
	/**
	 * 拖车费 成本
	 */
	private BigDecimal tcf_cb= new BigDecimal(0);//
	
	/**
	 * 扫描费 收入
	 */	
	private BigDecimal smf_sr= new BigDecimal(0);//
	/**
	 * 扫描费 成本
	 */
	private BigDecimal smf_cb= new BigDecimal(0);//
	
	/**
	 * 查柜费 收入
	 */	
	private BigDecimal cgf_sr= new BigDecimal(0);//
	/**
	 * 查柜费 成本
	 */
	private BigDecimal cgf_cb= new BigDecimal(0);//
	
	/**
	 * 熏蒸费 收入
	 */	
	private BigDecimal xzf_sr= new BigDecimal(0);//
	/**
	 * 熏蒸费 成本
	 */
	private BigDecimal xzf_cb= new BigDecimal(0);//
	
	/**
	 * 加签 收入
	 */	
	private BigDecimal jq_sr= new BigDecimal(0);//
	/**
	 * 加签 成本
	 */
	private BigDecimal jq_cb= new BigDecimal(0);//
	
	/**
	 * 信用证费 收入
	 */	
	private BigDecimal xyzf_sr= new BigDecimal(0);//
	/**
	 * 信用证费 成本
	 */
	private BigDecimal xyzf_cb= new BigDecimal(0);//
	
	/**
	 * 产地证费 收入
	 */	
	private BigDecimal cdzf_sr= new BigDecimal(0);//
	/**
	 * 产地证费 成本
	 */
	private BigDecimal cdzf_cb= new BigDecimal(0);//
	
	/**
	 * 空白单证费 收入
	 */	
	private BigDecimal kbdzf_sr= new BigDecimal(0);//
	/**
	 * 空白单证费 成本
	 */
	private BigDecimal kbdzf_cb= new BigDecimal(0);//
	
	/**
	 * 快递费 收入
	 */	
	private BigDecimal kdf_sr= new BigDecimal(0);//
	/**
	 * 快递费 成本
	 */
	private BigDecimal kdf_cb= new BigDecimal(0);//
	
	/**
	 * 驳船费 收入
	 */	
	private BigDecimal bcf_sr= new BigDecimal(0);//
	/**
	 * 驳船费 成本
	 */
	private BigDecimal bcf_cb= new BigDecimal(0);//
	
	/**
	 * 封条费 收入
	 */	
	private BigDecimal ftf_sr= new BigDecimal(0);//
	/**
	 * 封条费 成本
	 */
	private BigDecimal ftf_cb= new BigDecimal(0);//
	
	/**
	 * 仓单费 收入
	 */	
	private BigDecimal cdf_sr= new BigDecimal(0);//
	/**
	 * 仓单费 成本
	 */
	private BigDecimal cdf_cb= new BigDecimal(0);//
	
	/**
	 * 过磅费 收入
	 */	
	private BigDecimal gpf_sr= new BigDecimal(0);//
	/**
	 * 过磅费 成本
	 */
	private BigDecimal gpf_cb= new BigDecimal(0);//
	
	/**
	 * 换证凭条费 收入
	 */	
	private BigDecimal hzptf_sr= new BigDecimal(0);//
	/**
	 * 换证凭条费 成本
	 */
	private BigDecimal hzptf_cb= new BigDecimal(0);//
	
	/**
	 * 其他 收入
	 */	
	private BigDecimal qt_sr= new BigDecimal(0);//
	/**
	 * 其他 成本
	 */
	private BigDecimal qt_cb= new BigDecimal(0);//
	
	
	/**
	 * 拖车费用 成本
	 */
	private BigDecimal tczc_fyje = new BigDecimal(0);	
	public BigDecimal getTczc_fyje() {return tczc_fyje;}
	public void setTczc_fyje(BigDecimal tczc_fyje) {this.tczc_fyje = tczc_fyje;}

	/**
	 * 合计 收入
	 */	
	private BigDecimal hj_sr= new BigDecimal(0);//
	/**
	 * 合计 成本
	 */
	private BigDecimal hj_cb= new BigDecimal(0);//
	
	/**
	 * 毛利
	 */
	private BigDecimal ml= new BigDecimal(0);//
	
	/**
	 * 利润
	 */
	private BigDecimal lr= new BigDecimal(0);//
	
	/**
	 * 业务员
	 */
	private String ywy;//
	
	/**
	 * 代理费标准
	 */
	private String dlfbz;//



	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	public String getHzdw() {
		return hzdw;
	}

	public void setHzdw(String hzdw) {
		this.hzdw = hzdw;
	}

	public String getBgrq() {
		return bgrq;
	}

	public void setBgrq(String bgrq) {
		this.bgrq = bgrq;
	}

	public String getBgdh() {
		return bgdh;
	}

	public void setBgdh(String bgdh) {
		this.bgdh = bgdh;
	}

	public BigDecimal getBgje() {
		return bgje;
	}

	public void setBgje(BigDecimal bgje) {
		this.bgje = bgje;
	}

	public BigDecimal getFpje() {
		return fpje;
	}

	public void setFpje(BigDecimal fpje) {
		this.fpje = fpje;
	}

	public String getSfprq() {
		return sfprq;
	}

	public void setSfprq(String sfprq) {
		this.sfprq = sfprq;
	}

	public BigDecimal getTsje() {
		return tsje;
	}

	public void setTsje(BigDecimal tsje) {
		this.tsje = tsje;
	}

	public String getZftsrq() {
		return zftsrq;
	}

	public void setZftsrq(String zftsrq) {
		this.zftsrq = zftsrq;
	}

	public String getSdgstsrq() {
		return sdgstsrq;
	}

	public void setSdgstsrq(String sdgstsrq) {
		this.sdgstsrq = sdgstsrq;
	}

	public BigDecimal getDlf() {
		return dlf;
	}

	public void setDlf(BigDecimal dlf) {
		this.dlf = dlf;
	}

	public BigDecimal getBgf_sr() {
		return bgf_sr;
	}

	public void setBgf_sr(BigDecimal bgf_sr) {
		this.bgf_sr = bgf_sr;
	}

	public BigDecimal getBgf_cb() {
		return bgf_cb;
	}

	public void setBgf_cb(BigDecimal bgf_cb) {
		this.bgf_cb = bgf_cb;
	}

	public BigDecimal getGuangjf_sr() {
		return guangjf_sr;
	}

	public void setGuangjf_sr(BigDecimal guangjf_sr) {
		this.guangjf_sr = guangjf_sr;
	}

	public BigDecimal getGuangjf_cb() {
		return guangjf_cb;
	}

	public void setGuangjf_cb(BigDecimal guangjf_cb) {
		this.guangjf_cb = guangjf_cb;
	}

	public BigDecimal getGoujf_sr() {
		return goujf_sr;
	}

	public void setGoujf_sr(BigDecimal goujf_sr) {
		this.goujf_sr = goujf_sr;
	}

	public BigDecimal getGoujf_cb() {
		return goujf_cb;
	}

	public void setGoujf_cb(BigDecimal goujf_cb) {
		this.goujf_cb = goujf_cb;
	}

	public BigDecimal getSjf_sr() {
		return sjf_sr;
	}

	public void setSjf_sr(BigDecimal sjf_sr) {
		this.sjf_sr = sjf_sr;
	}

	public BigDecimal getSjf_cb() {
		return sjf_cb;
	}

	public void setSjf_cb(BigDecimal sjf_cb) {
		this.sjf_cb = sjf_cb;
	}

	public BigDecimal getXyf_sr() {
		return xyf_sr;
	}

	public void setXyf_sr(BigDecimal xyf_sr) {
		this.xyf_sr = xyf_sr;
	}

	public BigDecimal getXyf_cb() {
		return xyf_cb;
	}

	public void setXyf_cb(BigDecimal xyf_cb) {
		this.xyf_cb = xyf_cb;
	}

	public BigDecimal getLgf_sr() {
		return lgf_sr;
	}

	public void setLgf_sr(BigDecimal lgf_sr) {
		this.lgf_sr = lgf_sr;
	}

	public BigDecimal getLgf_cb() {
		return lgf_cb;
	}

	public void setLgf_cb(BigDecimal lgf_cb) {
		this.lgf_cb = lgf_cb;
	}

	public BigDecimal getTcf_sr() {
		return tcf_sr;
	}

	public void setTcf_sr(BigDecimal tcf_sr) {
		this.tcf_sr = tcf_sr;
	}

	public BigDecimal getTcf_cb() {
		return tcf_cb;
	}

	public void setTcf_cb(BigDecimal tcf_cb) {
		this.tcf_cb = tcf_cb;
	}

	public BigDecimal getSmf_sr() {
		return smf_sr;
	}

	public void setSmf_sr(BigDecimal smf_sr) {
		this.smf_sr = smf_sr;
	}

	public BigDecimal getSmf_cb() {
		return smf_cb;
	}

	public void setSmf_cb(BigDecimal smf_cb) {
		this.smf_cb = smf_cb;
	}

	public BigDecimal getCgf_sr() {
		return cgf_sr;
	}

	public void setCgf_sr(BigDecimal cgf_sr) {
		this.cgf_sr = cgf_sr;
	}

	public BigDecimal getCgf_cb() {
		return cgf_cb;
	}

	public void setCgf_cb(BigDecimal cgf_cb) {
		this.cgf_cb = cgf_cb;
	}

	public BigDecimal getXzf_sr() {
		return xzf_sr;
	}

	public void setXzf_sr(BigDecimal xzf_sr) {
		this.xzf_sr = xzf_sr;
	}

	public BigDecimal getXzf_cb() {
		return xzf_cb;
	}

	public void setXzf_cb(BigDecimal xzf_cb) {
		this.xzf_cb = xzf_cb;
	}

	public BigDecimal getJq_sr() {
		return jq_sr;
	}

	public void setJq_sr(BigDecimal jq_sr) {
		this.jq_sr = jq_sr;
	}

	public BigDecimal getJq_cb() {
		return jq_cb;
	}

	public void setJq_cb(BigDecimal jq_cb) {
		this.jq_cb = jq_cb;
	}

	public BigDecimal getXyzf_sr() {
		return xyzf_sr;
	}

	public void setXyzf_sr(BigDecimal xyzf_sr) {
		this.xyzf_sr = xyzf_sr;
	}

	public BigDecimal getXyzf_cb() {
		return xyzf_cb;
	}

	public void setXyzf_cb(BigDecimal xyzf_cb) {
		this.xyzf_cb = xyzf_cb;
	}

	public BigDecimal getCdzf_sr() {
		return cdzf_sr;
	}

	public void setCdzf_sr(BigDecimal cdzf_sr) {
		this.cdzf_sr = cdzf_sr;
	}

	public BigDecimal getCdzf_cb() {
		return cdzf_cb;
	}

	public void setCdzf_cb(BigDecimal cdzf_cb) {
		this.cdzf_cb = cdzf_cb;
	}

	public BigDecimal getKbdzf_sr() {
		return kbdzf_sr;
	}

	public void setKbdzf_sr(BigDecimal kbdzf_sr) {
		this.kbdzf_sr = kbdzf_sr;
	}

	public BigDecimal getKbdzf_cb() {
		return kbdzf_cb;
	}

	public void setKbdzf_cb(BigDecimal kbdzf_cb) {
		this.kbdzf_cb = kbdzf_cb;
	}

	public BigDecimal getKdf_sr() {
		return kdf_sr;
	}

	public void setKdf_sr(BigDecimal kdf_sr) {
		this.kdf_sr = kdf_sr;
	}

	public BigDecimal getKdf_cb() {
		return kdf_cb;
	}

	public void setKdf_cb(BigDecimal kdf_cb) {
		this.kdf_cb = kdf_cb;
	}

	public BigDecimal getBcf_sr() {
		return bcf_sr;
	}

	public void setBcf_sr(BigDecimal bcf_sr) {
		this.bcf_sr = bcf_sr;
	}

	public BigDecimal getBcf_cb() {
		return bcf_cb;
	}

	public void setBcf_cb(BigDecimal bcf_cb) {
		this.bcf_cb = bcf_cb;
	}

	public BigDecimal getFtf_sr() {
		return ftf_sr;
	}

	public void setFtf_sr(BigDecimal ftf_sr) {
		this.ftf_sr = ftf_sr;
	}

	public BigDecimal getFtf_cb() {
		return ftf_cb;
	}

	public void setFtf_cb(BigDecimal ftf_cb) {
		this.ftf_cb = ftf_cb;
	}

	public BigDecimal getCdf_sr() {
		return cdf_sr;
	}

	public void setCdf_sr(BigDecimal cdf_sr) {
		this.cdf_sr = cdf_sr;
	}

	public BigDecimal getCdf_cb() {
		return cdf_cb;
	}

	public void setCdf_cb(BigDecimal cdf_cb) {
		this.cdf_cb = cdf_cb;
	}




	public BigDecimal getGpf_sr() {
		return gpf_sr;
	}

	public void setGpf_sr(BigDecimal gpf_sr) {
		this.gpf_sr = gpf_sr;
	}

	public BigDecimal getGpf_cb() {
		return gpf_cb;
	}

	public void setGpf_cb(BigDecimal gpf_cb) {
		this.gpf_cb = gpf_cb;
	}

	public BigDecimal getHzptf_sr() {
		return hzptf_sr;
	}

	public void setHzptf_sr(BigDecimal hzptf_sr) {
		this.hzptf_sr = hzptf_sr;
	}

	public BigDecimal getHzptf_cb() {
		return hzptf_cb;
	}

	public void setHzptf_cb(BigDecimal hzptf_cb) {
		this.hzptf_cb = hzptf_cb;
	}

	public BigDecimal getQt_sr() {
		return qt_sr;
	}

	public void setQt_sr(BigDecimal qt_sr) {
		this.qt_sr = qt_sr;
	}

	public BigDecimal getQt_cb() {
		return qt_cb;
	}

	public void setQt_cb(BigDecimal qt_cb) {
		this.qt_cb = qt_cb;
	}

	public BigDecimal getHj_sr() {
		return hj_sr;
	}

	public void setHj_sr(BigDecimal hj_sr) {
		this.hj_sr = hj_sr;
	}

	public BigDecimal getHj_cb() {
		return hj_cb;
	}

	public void setHj_cb(BigDecimal hj_cb) {
		this.hj_cb = hj_cb;
	}

	public BigDecimal getMl() {
		return ml;
	}

	public void setMl(BigDecimal ml) {
		this.ml = ml;
	}




	public BigDecimal getLr() {
		return lr;
	}

	public void setLr(BigDecimal lr) {
		this.lr = lr;
	}

	public String getYwy() {
		return ywy;
	}

	public void setYwy(String ywy) {
		this.ywy = ywy;
	}

	public String getDlfbz() {
		return dlfbz;
	}

	public void setDlfbz(String dlfbz) {
		this.dlfbz = dlfbz;
	}


	
	
	
	
	
}
