package cn.qtone.crda.chargeback.dao;

/**
 * 扣费记录批量修改操作.
 * @author 贺少辉.
 *
 */
public interface ChargeBackDao {

	/**
	 * 修改扣费状态
	 */
	void updateChargeBackFeeStatus(UpdateChargeBackFeeStatusContionVO vo);
	
	/**
	 * 修改订阅表中的扣费状态.
	 */
	void updateSubscribeChargeBack();
	
	/**
	 * 修改订阅表中的扣费次数.<br>
	 */
	void updateSubscribeChargebackTimes(int status);
}
