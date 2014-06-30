package cn.qtone.crda.subscribe.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import cn.qtone.ContantsUtil;
import cn.qtone.crda.CrdaContants;

/**
 * 订阅管理 - 订阅管理.
 * 
 * @author 贺少辉
 * @version 1.0
 *
 */
@Entity
@Table(name = "crda_subscribe")
public class Subscribe{

	/**
 	 * 主键
 	 */ 
	private Integer id;

	/**
 	 * 学生唯一标识
 	 */ 
	private String stu_unique;

	/**
 	 * 订阅时间
 	 */ 
	private String subscribeTime;

	/**
 	 * 取消时间
 	 */ 
	private String cancelTime;

	/**
 	 * 扣费状态
 	 */ 
	private Integer chargeback;

	/**
 	 * 扣费次数(如果3次扣费失败则取消订阅)
 	 */ 
	private Integer chargebackTimes;

	/**
 	 * 扣费金额
 	 */ 
	private Float chargeFees;

	/**
 	 * 手机号码
 	 */ 
	private String mobile;
	
	/**
	 * 订单状态(0,取消 1正常)
	 */
	private Integer subscribeStatus; 
	
	/**
	 * 属于哪个业务系统，方便以后扩展
	 */
	private String lsBusiness;
	
	/**
	 * 生成扣费记录 0:未生成 1:已生成
	 */
	private Integer toChargeBack;

	public Integer getSubscribeStatus() {
		return subscribeStatus;
	}

	public void setSubscribeStatus(Integer subscribeStatus) {
		this.subscribeStatus = subscribeStatus;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId(){
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}


	public String getStu_unique(){
		return stu_unique;
	}
	
	public void setStu_unique(String stu_unique){
		this.stu_unique = stu_unique;
	}


	public String getSubscribeTime(){
		return subscribeTime;
	}
	
	public void setSubscribeTime(String subscribeTime){
		this.subscribeTime = subscribeTime;
	}


	public String getCancelTime(){
		return cancelTime;
	}
	
	public void setCancelTime(String cancelTime){
		this.cancelTime = cancelTime;
	}


	public Integer getChargeback(){
		return chargeback;
	}
	
	public void setChargeback(Integer chargeback){
		this.chargeback = chargeback;
	}


	public Integer getChargebackTimes(){
		return chargebackTimes;
	}
	
	public void setChargebackTimes(Integer chargebackTimes){
		this.chargebackTimes = chargebackTimes;
	}


	public Float getChargeFees(){
		return chargeFees;
	}
	
	public void setChargeFees(Float chargeFees){
		this.chargeFees = chargeFees;
	}


	public String getMobile(){
		return mobile;
	}
	
	public void setMobile(String mobile){
		this.mobile = mobile;
	}
	
	public String subscribeChargebackValue(){
	   return ContantsUtil.getSystemContantsMapValue(CrdaContants.SUBSCRIBECHARGEBACK, this.chargeback+"");	
	}
	public String SubscribeStatusValue(){
		return ContantsUtil.getSystemContantsMapValue(CrdaContants.SUBSCRIBESTATUS, this.subscribeStatus+"");	
	}

	public String getLsBusiness() {
		return lsBusiness;
	}

	public void setLsBusiness(String lsBusiness) {
		this.lsBusiness = lsBusiness;
	}

	public Integer getToChargeBack() {
		return toChargeBack;
	}

	public void setToChargeBack(Integer toChargeBack) {
		this.toChargeBack = toChargeBack;
	}
}