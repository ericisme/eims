package cn.qtone.crda.chargeback.dao;

/**
 * 修改扣费状态的条件对象.<br>
 * @author 贺少辉
 *
 */
public class UpdateChargeBackFeeStatusContionVO {

	private String year;
	private String month;
	private String lsBusiness;
	private String mobile;
	private int chargeback;
	private String chargebackTime;
	
	public UpdateChargeBackFeeStatusContionVO(String year, String month, String lsBusiness,String chargebackTime) {
		super();
		this.year = year;
		this.month = month;
		this.lsBusiness = lsBusiness;
		this.chargebackTime = chargebackTime;
	}

	public String getYear() {
		return year;
	}

	public String getMonth() {
		return month;
	}

	public String getLsBusiness() {
		return lsBusiness;
	}

	public String getMobile() {
		return mobile;
	}

	public int getChargeback() {
		return chargeback;
	}

	public void setChargeback(int chargeback) {
		this.chargeback = chargeback;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getChargebackTime() {
		return chargebackTime;
	}
}
