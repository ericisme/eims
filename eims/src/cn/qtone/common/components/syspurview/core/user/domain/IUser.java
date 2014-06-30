package cn.qtone.common.components.syspurview.core.user.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.qtone.ContantsUtil;
import cn.qtone.library.XxptContants;
import cn.qtone.library.school.domain.School;
import cn.qtone.library.xxptclass.domain.ZfptClass;

@Entity
@Table(name = "sys_user")
public class IUser {
	
	private Integer userId; // 用户的唯一ID值
	private String loginName;//学籍号(继教号)
	private String loginPassword;
	private String plainCode;
	private String realName; // 真实姓名
	private Integer gender;//性别
	private String gender_str;//性别值
	private String birthday;//出生日期
	private String mobile; // 手机号码
	private String phone;//座机
	private String email; // 用户电邮
	private String addTime; // 用户添加时间
	private Integer isLock; // 帐号是否被停用，默认为false
	private Integer status;//0正常；1为毕业学生；2为教师调离；3为注销
	private String user_type;
	private Date lastLoginTime;
	private String lastLoginIP;
	private Integer loginTimes;
	private Integer isSuper;
	private Integer groupId; // 用户所属的用户组ID
	//private String groupName;

	private String id_card;//ic卡号
	
	private Integer town_id;
	private Integer agency_id;
	//private Integer school_id;
	private School school;
	private Integer grade_id;
	//private Integer class_id;
	private ZfptClass zfptClass;
	private String className;//班级名称
	private String schoolName;//学校名称
	private String agencyName;//机构名称
	
	public String uerTypeValue(){
		return  ContantsUtil.getSystemContantsMapValue(XxptContants.MANAGE_USER_TYPE, this.user_type);
	}
	
	@Transient
	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}




	public String getId_card(){
		return id_card;
	}
	
	public void setId_card(String id_card){
		this.id_card = id_card;
	}


	public String getBirthday(){
		return birthday;
	}
	
	public void setBirthday(String birthday){
		this.birthday = birthday;
	}
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
	public void setBirthdayStr(java.util.Date birthday){
		try {
			this.birthday = sdf.format(birthday);
		} catch (Exception e) {
			
		}
		
	}


	public Integer getGender(){
		return gender;
	}
	
	public void setGender(Integer gender){
		this.gender = gender;
	}

	
	/**
	 * 根据年级类型的数字值获取对应的文字值.
	 */
	public String containGenderValue() {
		StringBuffer sb = new StringBuffer();
		if(gender >= 0){
			String genderType = this.gender.toString();
			sb.append(XxptContants.GENDERTYPE.get(genderType)+",");
		}
		String s = sb.toString();
		if(s.lastIndexOf(",")>0){
			s = s.substring(0,s.length()-1);
		}
		return s;
	}
	
	/**
	 * 获取学生性别文本值
	 * @return
	 */
	public Integer containGender() {
		Integer gen = 1;
		if (gender_str != null && gender_str.indexOf("男") != -1) 
			gen = 1;
		else if (gender_str != null && gender_str.indexOf("女") != -1)
			gen = 0;
		return gen;
	}
	
	@Transient
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
    
	@Transient
	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	
	@Transient
	public String getGradeName() {
		return XxptContants.getGradeValue(this.getGrade_id()+"");
	}
	
	public Integer getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(Integer agency_id) {
		this.agency_id = agency_id;
	}

	public Integer getGrade_id() {
		return grade_id;
	}

	public void setGrade_id(Integer grade_id) {
		this.grade_id = grade_id;
	}


	@Transient
	public String getGender_str() {
		if (gender!=null&&gender==1)
			return "男";
		else
			return "女";
	}
	
	@Transient
	public String getGender_str2() {
		return gender_str;
	}

	public void setGender_str(String gender_str) {
		this.gender_str = gender_str;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}


	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getPlainCode() {
		return plainCode;
	}

	public void setPlainCode(String plainCode) {
		this.plainCode = plainCode;
	}

	public Integer getIsLock() {
		return isLock;
	}

	public void setIsLock(Integer isLock) {
		this.isLock = isLock;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginIP() {
		return lastLoginIP;
	}

	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}

	public Integer getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	public Integer getIsSuper() {
		return isSuper;
	}

	public void setIsSuper(Integer isSuper) {
		this.isSuper = isSuper;
	}

	public Integer getTown_id() {
		return town_id;
	}

	public void setTown_id(Integer town_id) {
		this.town_id = town_id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "school_id")
	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "class_id")
	public ZfptClass getZfptClass() {
		return zfptClass;
	}

	public void setZfptClass(ZfptClass zfptClass) {
		this.zfptClass = zfptClass;
	}


}
