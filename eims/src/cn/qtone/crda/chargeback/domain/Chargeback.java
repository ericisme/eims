package cn.qtone.crda.chargeback.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import cn.qtone.ContantsUtil;
import cn.qtone.crda.CrdaContants;

/**
 * 扣费记录 - 扣费记录.
 * 
 * @author 贺少辉
 * @version 1.0
 *
 */
@Entity
@Table(name = "crda_chargeback")
public class Chargeback{

	/**
 	 * 主键
 	 */ 
	private Integer id;

	/**
 	 * 学生唯一标识
 	 */ 
	private String stu_unique;

	/**
 	 * 生成时间(年份)
 	 */ 
	private String year;
	
	/**
 	 * 生成时间(月份)
 	 */ 
	private String month;

	/**
 	 * 扣费状态(1扣费成功,-1扣费失败,0未扣费)
 	 */ 
	private Integer chargeback;

	/**
 	 * 扣费金额
 	 */ 
	private Integer chargeFees;

	/**
 	 * 扣费时间
 	 */ 
	private String chargebackTime;

	/**
 	 * 手机号码
 	 */ 
	private String mobile;
	
	/**
	 * 创建人(当前系统的登陆用户)
	 */
	private String createUserNme;
	
	/**
	 * 属于哪个业务系统，方便以后扩展
	 */
	private String lsBusiness;
	
	/**
	 * 单订导出状态 0:未导出 1:已导出
	 */
	private Integer exportStaus;
	
	/**
	 * 订阅记录ID
	 */
	private Integer subscribeId;

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

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getChargeback(){
		return chargeback;
	}
	
	public void setChargeback(Integer chargeback){
		this.chargeback = chargeback;
	}

	public Integer getChargeFees() {
		return chargeFees;
	}

	public void setChargeFees(Integer chargeFees) {
		this.chargeFees = chargeFees;
	}

	public String getChargebackTime(){
		return chargebackTime;
	}
	
	public void setChargebackTime(String chargebackTime){
		this.chargebackTime = chargebackTime;
	}


	public String getMobile(){
		return mobile;
	}
	
	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getCreateUserNme() {
		return createUserNme;
	}

	public void setCreateUserNme(String createUserNme) {
		this.createUserNme = createUserNme;
	}
	
	public String subscribeChargebackValue(){
		   return ContantsUtil.getSystemContantsMapValue(CrdaContants.SUBSCRIBECHARGEBACK, this.chargeback+"");	
	}

	public String getLsBusiness() {
		return lsBusiness;
	}

	public void setLsBusiness(String lsBusiness) {
		this.lsBusiness = lsBusiness;
	}

	public Integer getExportStaus() {
		return exportStaus;
	}

	public void setExportStaus(Integer exportStaus) {
		this.exportStaus = exportStaus;
	}

	public Integer getSubscribeId() {
		return subscribeId;
	}

	public void setSubscribeId(Integer subscribeId) {
		this.subscribeId = subscribeId;
	}
}