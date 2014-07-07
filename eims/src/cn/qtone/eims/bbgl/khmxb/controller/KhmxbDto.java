package cn.qtone.eims.bbgl.khmxb.controller;


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
	private Float bgje = 0F;//
	
	/**
	 * 发票金额
	 */	
	private Float fpje= 0F;//
	
	/**
	 * 收发票日期
	 */	
	private String sfprq;//
	
	/**
	 * 退税金额
	 */	
	private Float tsje= 0F;//
	
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
	private Float dlf= 0F;//
	
	
	/**
	 * 报关费 收入
	 */	
	private Float bgf_sr= 0F;//
	
	/**
	 * 报关费 成本
	 */
	private Float bgf_cb= 0F;//
	
	/**
	 * 广建费 收入
	 */	
	private Float guangjf_sr= 0F;//
	/**
	 * 广建费 成本
	 */
	private Float guangjf_cb= 0F;//
	
	/**
	 * 国检费 收入
	 */	
	private Float goujf_sr= 0F;//
	/**
	 * 国检费 成本
	 */
	private Float goujf_cb= 0F;//
	
	/**
	 * 商检费 收入
	 */	
	private Float sjf_sr= 0F;//
	/**
	 * 商检费 成本
	 */
	private Float sjf_cb= 0F;//
	
	/**
	 * 续页费 收入
	 */	
	private Float xyf_sr= 0F;//
	/**
	 * 续页费 成本
	 */
	private Float xyf_cb= 0F;//
	
	/**
	 * 连柜费 收入
	 */	
	private Float lgf_sr= 0F;//
	/**
	 * 连柜费 成本
	 */
	private Float lgf_cb= 0F;//
	
	/**
	 * 拖车费 收入
	 */	
	private Float tcf_sr= 0F;//
	/**
	 * 拖车费 成本
	 */
	private Float tcf_cb= 0F;//
	
	/**
	 * 扫描费 收入
	 */	
	private Float smf_sr= 0F;//
	/**
	 * 扫描费 成本
	 */
	private Float smf_cb= 0F;//
	
	/**
	 * 查柜费 收入
	 */	
	private Float cgf_sr= 0F;//
	/**
	 * 查柜费 成本
	 */
	private Float cgf_cb= 0F;//
	
	/**
	 * 熏蒸费 收入
	 */	
	private Float xzf_sr= 0F;//
	/**
	 * 熏蒸费 成本
	 */
	private Float xzf_cb= 0F;//
	
	/**
	 * 加签 收入
	 */	
	private Float jq_sr= 0F;//
	/**
	 * 加签 成本
	 */
	private Float jq_cb= 0F;//
	
	/**
	 * 信用证费 收入
	 */	
	private Float xyzf_sr= 0F;//
	/**
	 * 信用证费 成本
	 */
	private Float xyzf_cb= 0F;//
	
	/**
	 * 产地证费 收入
	 */	
	private Float cdzf_sr= 0F;//
	/**
	 * 产地证费 成本
	 */
	private Float cdzf_cb= 0F;//
	
	/**
	 * 空白单证费 收入
	 */	
	private Float kbdzf_sr= 0F;//
	/**
	 * 空白单证费 成本
	 */
	private Float kbdzf_cb= 0F;//
	
	/**
	 * 快递费 收入
	 */	
	private Float kdf_sr= 0F;//
	/**
	 * 快递费 成本
	 */
	private Float kdf_cb= 0F;//
	
	/**
	 * 驳船费 收入
	 */	
	private Float bcf_sr= 0F;//
	/**
	 * 驳船费 成本
	 */
	private Float bcf_cb= 0F;//
	
	/**
	 * 封条费 收入
	 */	
	private Float ftf_sr= 0F;//
	/**
	 * 封条费 成本
	 */
	private Float ftf_cb= 0F;//
	
	/**
	 * 仓单费 收入
	 */	
	private Float cdf_sr= 0F;//
	/**
	 * 仓单费 成本
	 */
	private Float cdf_cb= 0F;//
	
	/**
	 * 过磅费 收入
	 */	
	private Float gpf_sr= 0F;//
	/**
	 * 过磅费 成本
	 */
	private Float gpf_cb= 0F;//
	
	/**
	 * 换证凭条费 收入
	 */	
	private Float hzptf_sr= 0F;//
	/**
	 * 换证凭条费 成本
	 */
	private Float hzptf_cb= 0F;//
	
	/**
	 * 其他 收入
	 */	
	private Float qt_sr= 0F;//
	/**
	 * 其他 成本
	 */
	private Float qt_cb= 0F;//
	
	/**
	 * 合计 收入
	 */	
	private Float hj_sr= 0F;//
	/**
	 * 合计 成本
	 */
	private Float hj_cb= 0F;//
	
	/**
	 * 毛利
	 */
	private Float ml= 0F;//
	
	/**
	 * 利润
	 */
	private Float lr= 0F;//
	
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

	public Float getBgje() {
		return bgje;
	}

	public void setBgje(Float bgje) {
		this.bgje = bgje;
	}

	public Float getFpje() {
		return fpje;
	}

	public void setFpje(Float fpje) {
		this.fpje = fpje;
	}

	public String getSfprq() {
		return sfprq;
	}

	public void setSfprq(String sfprq) {
		this.sfprq = sfprq;
	}

	public Float getTsje() {
		return tsje;
	}

	public void setTsje(Float tsje) {
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

	public Float getDlf() {
		return dlf;
	}

	public void setDlf(Float dlf) {
		this.dlf = dlf;
	}

	public Float getBgf_sr() {
		return bgf_sr;
	}

	public void setBgf_sr(Float bgf_sr) {
		this.bgf_sr = bgf_sr;
	}

	public Float getBgf_cb() {
		return bgf_cb;
	}

	public void setBgf_cb(Float bgf_cb) {
		this.bgf_cb = bgf_cb;
	}

	public Float getGuangjf_sr() {
		return guangjf_sr;
	}

	public void setGuangjf_sr(Float guangjf_sr) {
		this.guangjf_sr = guangjf_sr;
	}

	public Float getGuangjf_cb() {
		return guangjf_cb;
	}

	public void setGuangjf_cb(Float guangjf_cb) {
		this.guangjf_cb = guangjf_cb;
	}

	public Float getGoujf_sr() {
		return goujf_sr;
	}

	public void setGoujf_sr(Float goujf_sr) {
		this.goujf_sr = goujf_sr;
	}

	public Float getGoujf_cb() {
		return goujf_cb;
	}

	public void setGoujf_cb(Float goujf_cb) {
		this.goujf_cb = goujf_cb;
	}

	public Float getSjf_sr() {
		return sjf_sr;
	}

	public void setSjf_sr(Float sjf_sr) {
		this.sjf_sr = sjf_sr;
	}

	public Float getSjf_cb() {
		return sjf_cb;
	}

	public void setSjf_cb(Float sjf_cb) {
		this.sjf_cb = sjf_cb;
	}

	public Float getXyf_sr() {
		return xyf_sr;
	}

	public void setXyf_sr(Float xyf_sr) {
		this.xyf_sr = xyf_sr;
	}

	public Float getXyf_cb() {
		return xyf_cb;
	}

	public void setXyf_cb(Float xyf_cb) {
		this.xyf_cb = xyf_cb;
	}

	public Float getLgf_sr() {
		return lgf_sr;
	}

	public void setLgf_sr(Float lgf_sr) {
		this.lgf_sr = lgf_sr;
	}

	public Float getLgf_cb() {
		return lgf_cb;
	}

	public void setLgf_cb(Float lgf_cb) {
		this.lgf_cb = lgf_cb;
	}

	public Float getTcf_sr() {
		return tcf_sr;
	}

	public void setTcf_sr(Float tcf_sr) {
		this.tcf_sr = tcf_sr;
	}

	public Float getTcf_cb() {
		return tcf_cb;
	}

	public void setTcf_cb(Float tcf_cb) {
		this.tcf_cb = tcf_cb;
	}

	public Float getSmf_sr() {
		return smf_sr;
	}

	public void setSmf_sr(Float smf_sr) {
		this.smf_sr = smf_sr;
	}

	public Float getSmf_cb() {
		return smf_cb;
	}

	public void setSmf_cb(Float smf_cb) {
		this.smf_cb = smf_cb;
	}

	public Float getCgf_sr() {
		return cgf_sr;
	}

	public void setCgf_sr(Float cgf_sr) {
		this.cgf_sr = cgf_sr;
	}

	public Float getCgf_cb() {
		return cgf_cb;
	}

	public void setCgf_cb(Float cgf_cb) {
		this.cgf_cb = cgf_cb;
	}

	public Float getXzf_sr() {
		return xzf_sr;
	}

	public void setXzf_sr(Float xzf_sr) {
		this.xzf_sr = xzf_sr;
	}

	public Float getXzf_cb() {
		return xzf_cb;
	}

	public void setXzf_cb(Float xzf_cb) {
		this.xzf_cb = xzf_cb;
	}

	public Float getJq_sr() {
		return jq_sr;
	}

	public void setJq_sr(Float jq_sr) {
		this.jq_sr = jq_sr;
	}

	public Float getJq_cb() {
		return jq_cb;
	}

	public void setJq_cb(Float jq_cb) {
		this.jq_cb = jq_cb;
	}

	public Float getXyzf_sr() {
		return xyzf_sr;
	}

	public void setXyzf_sr(Float xyzf_sr) {
		this.xyzf_sr = xyzf_sr;
	}

	public Float getXyzf_cb() {
		return xyzf_cb;
	}

	public void setXyzf_cb(Float xyzf_cb) {
		this.xyzf_cb = xyzf_cb;
	}

	public Float getCdzf_sr() {
		return cdzf_sr;
	}

	public void setCdzf_sr(Float cdzf_sr) {
		this.cdzf_sr = cdzf_sr;
	}

	public Float getCdzf_cb() {
		return cdzf_cb;
	}

	public void setCdzf_cb(Float cdzf_cb) {
		this.cdzf_cb = cdzf_cb;
	}

	public Float getKbdzf_sr() {
		return kbdzf_sr;
	}

	public void setKbdzf_sr(Float kbdzf_sr) {
		this.kbdzf_sr = kbdzf_sr;
	}

	public Float getKbdzf_cb() {
		return kbdzf_cb;
	}

	public void setKbdzf_cb(Float kbdzf_cb) {
		this.kbdzf_cb = kbdzf_cb;
	}

	public Float getKdf_sr() {
		return kdf_sr;
	}

	public void setKdf_sr(Float kdf_sr) {
		this.kdf_sr = kdf_sr;
	}

	public Float getKdf_cb() {
		return kdf_cb;
	}

	public void setKdf_cb(Float kdf_cb) {
		this.kdf_cb = kdf_cb;
	}

	public Float getBcf_sr() {
		return bcf_sr;
	}

	public void setBcf_sr(Float bcf_sr) {
		this.bcf_sr = bcf_sr;
	}

	public Float getBcf_cb() {
		return bcf_cb;
	}

	public void setBcf_cb(Float bcf_cb) {
		this.bcf_cb = bcf_cb;
	}

	public Float getFtf_sr() {
		return ftf_sr;
	}

	public void setFtf_sr(Float ftf_sr) {
		this.ftf_sr = ftf_sr;
	}

	public Float getFtf_cb() {
		return ftf_cb;
	}

	public void setFtf_cb(Float ftf_cb) {
		this.ftf_cb = ftf_cb;
	}

	public Float getCdf_sr() {
		return cdf_sr;
	}

	public void setCdf_sr(Float cdf_sr) {
		this.cdf_sr = cdf_sr;
	}

	public Float getCdf_cb() {
		return cdf_cb;
	}

	public void setCdf_cb(Float cdf_cb) {
		this.cdf_cb = cdf_cb;
	}




	public Float getGpf_sr() {
		return gpf_sr;
	}

	public void setGpf_sr(Float gpf_sr) {
		this.gpf_sr = gpf_sr;
	}

	public Float getGpf_cb() {
		return gpf_cb;
	}

	public void setGpf_cb(Float gpf_cb) {
		this.gpf_cb = gpf_cb;
	}

	public Float getHzptf_sr() {
		return hzptf_sr;
	}

	public void setHzptf_sr(Float hzptf_sr) {
		this.hzptf_sr = hzptf_sr;
	}

	public Float getHzptf_cb() {
		return hzptf_cb;
	}

	public void setHzptf_cb(Float hzptf_cb) {
		this.hzptf_cb = hzptf_cb;
	}

	public Float getQt_sr() {
		return qt_sr;
	}

	public void setQt_sr(Float qt_sr) {
		this.qt_sr = qt_sr;
	}

	public Float getQt_cb() {
		return qt_cb;
	}

	public void setQt_cb(Float qt_cb) {
		this.qt_cb = qt_cb;
	}

	public Float getHj_sr() {
		return hj_sr;
	}

	public void setHj_sr(Float hj_sr) {
		this.hj_sr = hj_sr;
	}

	public Float getHj_cb() {
		return hj_cb;
	}

	public void setHj_cb(Float hj_cb) {
		this.hj_cb = hj_cb;
	}

	public Float getMl() {
		return ml;
	}

	public void setMl(Float ml) {
		this.ml = ml;
	}




	public Float getLr() {
		return lr;
	}

	public void setLr(Float lr) {
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
