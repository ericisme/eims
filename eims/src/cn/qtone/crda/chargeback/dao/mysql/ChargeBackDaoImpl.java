package cn.qtone.crda.chargeback.dao.mysql;

import cn.qtone.common.mvc.dao.BaseDAO;
import cn.qtone.crda.chargeback.dao.ChargeBackDao;
import cn.qtone.crda.chargeback.dao.UpdateChargeBackFeeStatusContionVO;

public class ChargeBackDaoImpl extends BaseDAO implements ChargeBackDao {

	public void updateChargeBackFeeStatus(UpdateChargeBackFeeStatusContionVO vo) {
		this.getSimpleJdbcTemplate().update("update crda_chargeback set chargeback=?,chargebackTime=? " +
				" where year=? and month=? and mobile in(?)", vo.getChargeback(),vo.getChargebackTime(),vo.getYear(),vo.getMonth(),vo.getMobile());
	}

	public void updateSubscribeChargeBack() {
		this.getSimpleJdbcTemplate().update("update crda_subscribe sub set sub.chargeback=(select charge.chargeback from crda_chargeback charge where charge.subscribeId=sub.id )");
	}
	
	public void updateSubscribeChargebackTimes(int status) {
		if(status==-1){
			  this.getSimpleJdbcTemplate().update("update crda_subscribe sub set sub.chargebackTimes=chargebackTimes+1 where chargeback=-1 and subscribeStatus=1");	
		}
		if(status==1){
			  this.getSimpleJdbcTemplate().update("update crda_subscribe sub set sub.chargebackTimes=0 where chargeback=1 and subscribeStatus=1");	
		}
	}
}
