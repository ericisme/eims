package cn.qtone.eims.bbgl.khmxb.controller;


/**
 * 
 * 统计客户明细表Dto
 * @author:eric
 * @date:2014-07-03
 */
public class KhmxbSumStrDto {
	
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
	private String bgje; //
	
	/**
	 * 发票金额
	 */	
	private String fpje;//
	
	/**
	 * 收发票日期
	 */	
	private String sfprq;//
	
	/**
	 * 退税金额
	 */	
	private String tsje;//
	
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
	private String dlf;//
	
	
	/**
	 * 报关费 收入
	 */	
	private String bgf_sr;//
	
	/**
	 * 报关费 成本
	 */
	private String bgf_cb;//
	
	/**
	 * 广建费 收入
	 */	
	private String guangjf_sr;//
	/**
	 * 广建费 成本
	 */
	private String guangjf_cb;//
	
	/**
	 * 国检费 收入
	 */	
	private String goujf_sr;//
	/**
	 * 国检费 成本
	 */
	private String goujf_cb;//
	
	/**
	 * 商检费 收入
	 */	
	private String sjf_sr;//
	/**
	 * 商检费 成本
	 */
	private String sjf_cb;//
	
	/**
	 * 续页费 收入
	 */	
	private String xyf_sr;//
	/**
	 * 续页费 成本
	 */
	private String xyf_cb;//
	
	/**
	 * 连柜费 收入
	 */	
	private String lgf_sr;//
	/**
	 * 连柜费 成本
	 */
	private String lgf_cb;//
	
	/**
	 * 拖车费 收入
	 */	
	private String tcf_sr;//
	/**
	 * 拖车费 成本
	 */
	private String tcf_cb;//
	
	/**
	 * 扫描费 收入
	 */	
	private String smf_sr;//
	/**
	 * 扫描费 成本
	 */
	private String smf_cb;//
	
	/**
	 * 查柜费 收入
	 */	
	private String cgf_sr;//
	/**
	 * 查柜费 成本
	 */
	private String cgf_cb;//
	
	/**
	 * 熏蒸费 收入
	 */	
	private String xzf_sr;//
	/**
	 * 熏蒸费 成本
	 */
	private String xzf_cb;//
	
	/**
	 * 加签 收入
	 */	
	private String jq_sr;//
	/**
	 * 加签 成本
	 */
	private String jq_cb;//
	
	/**
	 * 信用证费 收入
	 */	
	private String xyzf_sr;//
	/**
	 * 信用证费 成本
	 */
	private String xyzf_cb;//
	
	/**
	 * 产地证费 收入
	 */	
	private String cdzf_sr;//
	/**
	 * 产地证费 成本
	 */
	private String cdzf_cb;//
	
	/**
	 * 空白单证费 收入
	 */	
	private String kbdzf_sr;//
	/**
	 * 空白单证费 成本
	 */
	private String kbdzf_cb;//
	
	/**
	 * 快递费 收入
	 */	
	private String kdf_sr;//
	/**
	 * 快递费 成本
	 */
	private String kdf_cb;//
	
	/**
	 * 驳船费 收入
	 */	
	private String bcf_sr;//
	/**
	 * 驳船费 成本
	 */
	private String bcf_cb;//
	
	/**
	 * 封条费 收入
	 */	
	private String ftf_sr;//
	/**
	 * 封条费 成本
	 */
	private String ftf_cb;//
	
	/**
	 * 仓单费 收入
	 */	
	private String cdf_sr;//
	/**
	 * 仓单费 成本
	 */
	private String cdf_cb;//
	
	/**
	 * 过磅费 收入
	 */	
	private String gpf_sr;//
	/**
	 * 过磅费 成本
	 */
	private String gpf_cb;//
	
	/**
	 * 换证凭条费 收入
	 */	
	private String hzptf_sr;//
	/**
	 * 换证凭条费 成本
	 */
	private String hzptf_cb;//
	
	/**
	 * 其他 收入
	 */	
	private String qt_sr;//
	/**
	 * 其他 成本
	 */
	private String qt_cb;//
	
	/**
	 * 合计 收入
	 */	
	private String hj_sr;//
	/**
	 * 合计 成本
	 */
	private String hj_cb;//
	
	/**
	 * 毛利
	 */
	private String ml;//
	
	/**
	 * 利润
	 */
	private String lr;//
	
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

	public String getBgje() {
		return bgje;
	}

	public void setBgje(String bgje) {
		this.bgje = bgje;
	}

	public String getFpje() {
		return fpje;
	}

	public void setFpje(String fpje) {
		this.fpje = fpje;
	}

	public String getSfprq() {
		return sfprq;
	}

	public void setSfprq(String sfprq) {
		this.sfprq = sfprq;
	}

	public String getTsje() {
		return tsje;
	}

	public void setTsje(String tsje) {
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

	public String getDlf() {
		return dlf;
	}

	public void setDlf(String dlf) {
		this.dlf = dlf;
	}

	public String getBgf_sr() {
		return bgf_sr;
	}

	public void setBgf_sr(String bgf_sr) {
		this.bgf_sr = bgf_sr;
	}

	public String getBgf_cb() {
		return bgf_cb;
	}

	public void setBgf_cb(String bgf_cb) {
		this.bgf_cb = bgf_cb;
	}

	public String getGuangjf_sr() {
		return guangjf_sr;
	}

	public void setGuangjf_sr(String guangjf_sr) {
		this.guangjf_sr = guangjf_sr;
	}

	public String getGuangjf_cb() {
		return guangjf_cb;
	}

	public void setGuangjf_cb(String guangjf_cb) {
		this.guangjf_cb = guangjf_cb;
	}

	public String getGoujf_sr() {
		return goujf_sr;
	}

	public void setGoujf_sr(String goujf_sr) {
		this.goujf_sr = goujf_sr;
	}

	public String getGoujf_cb() {
		return goujf_cb;
	}

	public void setGoujf_cb(String goujf_cb) {
		this.goujf_cb = goujf_cb;
	}

	public String getSjf_sr() {
		return sjf_sr;
	}

	public void setSjf_sr(String sjf_sr) {
		this.sjf_sr = sjf_sr;
	}

	public String getSjf_cb() {
		return sjf_cb;
	}

	public void setSjf_cb(String sjf_cb) {
		this.sjf_cb = sjf_cb;
	}

	public String getXyf_sr() {
		return xyf_sr;
	}

	public void setXyf_sr(String xyf_sr) {
		this.xyf_sr = xyf_sr;
	}

	public String getXyf_cb() {
		return xyf_cb;
	}

	public void setXyf_cb(String xyf_cb) {
		this.xyf_cb = xyf_cb;
	}

	public String getLgf_sr() {
		return lgf_sr;
	}

	public void setLgf_sr(String lgf_sr) {
		this.lgf_sr = lgf_sr;
	}

	public String getLgf_cb() {
		return lgf_cb;
	}

	public void setLgf_cb(String lgf_cb) {
		this.lgf_cb = lgf_cb;
	}

	public String getTcf_sr() {
		return tcf_sr;
	}

	public void setTcf_sr(String tcf_sr) {
		this.tcf_sr = tcf_sr;
	}

	public String getTcf_cb() {
		return tcf_cb;
	}

	public void setTcf_cb(String tcf_cb) {
		this.tcf_cb = tcf_cb;
	}

	public String getSmf_sr() {
		return smf_sr;
	}

	public void setSmf_sr(String smf_sr) {
		this.smf_sr = smf_sr;
	}

	public String getSmf_cb() {
		return smf_cb;
	}

	public void setSmf_cb(String smf_cb) {
		this.smf_cb = smf_cb;
	}

	public String getCgf_sr() {
		return cgf_sr;
	}

	public void setCgf_sr(String cgf_sr) {
		this.cgf_sr = cgf_sr;
	}

	public String getCgf_cb() {
		return cgf_cb;
	}

	public void setCgf_cb(String cgf_cb) {
		this.cgf_cb = cgf_cb;
	}

	public String getXzf_sr() {
		return xzf_sr;
	}

	public void setXzf_sr(String xzf_sr) {
		this.xzf_sr = xzf_sr;
	}

	public String getXzf_cb() {
		return xzf_cb;
	}

	public void setXzf_cb(String xzf_cb) {
		this.xzf_cb = xzf_cb;
	}

	public String getJq_sr() {
		return jq_sr;
	}

	public void setJq_sr(String jq_sr) {
		this.jq_sr = jq_sr;
	}

	public String getJq_cb() {
		return jq_cb;
	}

	public void setJq_cb(String jq_cb) {
		this.jq_cb = jq_cb;
	}

	public String getXyzf_sr() {
		return xyzf_sr;
	}

	public void setXyzf_sr(String xyzf_sr) {
		this.xyzf_sr = xyzf_sr;
	}

	public String getXyzf_cb() {
		return xyzf_cb;
	}

	public void setXyzf_cb(String xyzf_cb) {
		this.xyzf_cb = xyzf_cb;
	}

	public String getCdzf_sr() {
		return cdzf_sr;
	}

	public void setCdzf_sr(String cdzf_sr) {
		this.cdzf_sr = cdzf_sr;
	}

	public String getCdzf_cb() {
		return cdzf_cb;
	}

	public void setCdzf_cb(String cdzf_cb) {
		this.cdzf_cb = cdzf_cb;
	}

	public String getKbdzf_sr() {
		return kbdzf_sr;
	}

	public void setKbdzf_sr(String kbdzf_sr) {
		this.kbdzf_sr = kbdzf_sr;
	}

	public String getKbdzf_cb() {
		return kbdzf_cb;
	}

	public void setKbdzf_cb(String kbdzf_cb) {
		this.kbdzf_cb = kbdzf_cb;
	}

	public String getKdf_sr() {
		return kdf_sr;
	}

	public void setKdf_sr(String kdf_sr) {
		this.kdf_sr = kdf_sr;
	}

	public String getKdf_cb() {
		return kdf_cb;
	}

	public void setKdf_cb(String kdf_cb) {
		this.kdf_cb = kdf_cb;
	}

	public String getBcf_sr() {
		return bcf_sr;
	}

	public void setBcf_sr(String bcf_sr) {
		this.bcf_sr = bcf_sr;
	}

	public String getBcf_cb() {
		return bcf_cb;
	}

	public void setBcf_cb(String bcf_cb) {
		this.bcf_cb = bcf_cb;
	}

	public String getFtf_sr() {
		return ftf_sr;
	}

	public void setFtf_sr(String ftf_sr) {
		this.ftf_sr = ftf_sr;
	}

	public String getFtf_cb() {
		return ftf_cb;
	}

	public void setFtf_cb(String ftf_cb) {
		this.ftf_cb = ftf_cb;
	}

	public String getCdf_sr() {
		return cdf_sr;
	}

	public void setCdf_sr(String cdf_sr) {
		this.cdf_sr = cdf_sr;
	}

	public String getCdf_cb() {
		return cdf_cb;
	}

	public void setCdf_cb(String cdf_cb) {
		this.cdf_cb = cdf_cb;
	}




	public String getGpf_sr() {
		return gpf_sr;
	}

	public void setGpf_sr(String gpf_sr) {
		this.gpf_sr = gpf_sr;
	}

	public String getGpf_cb() {
		return gpf_cb;
	}

	public void setGpf_cb(String gpf_cb) {
		this.gpf_cb = gpf_cb;
	}

	public String getHzptf_sr() {
		return hzptf_sr;
	}

	public void setHzptf_sr(String hzptf_sr) {
		this.hzptf_sr = hzptf_sr;
	}

	public String getHzptf_cb() {
		return hzptf_cb;
	}

	public void setHzptf_cb(String hzptf_cb) {
		this.hzptf_cb = hzptf_cb;
	}

	public String getQt_sr() {
		return qt_sr;
	}

	public void setQt_sr(String qt_sr) {
		this.qt_sr = qt_sr;
	}

	public String getQt_cb() {
		return qt_cb;
	}

	public void setQt_cb(String qt_cb) {
		this.qt_cb = qt_cb;
	}

	public String getHj_sr() {
		return hj_sr;
	}

	public void setHj_sr(String hj_sr) {
		this.hj_sr = hj_sr;
	}

	public String getHj_cb() {
		return hj_cb;
	}

	public void setHj_cb(String hj_cb) {
		this.hj_cb = hj_cb;
	}

	public String getMl() {
		return ml;
	}

	public void setMl(String ml) {
		this.ml = ml;
	}




	public String getLr() {
		return lr;
	}

	public void setLr(String lr) {
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
