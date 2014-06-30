package cn.qtone.common.components.syspurview.core.user.domain;

import cn.qtone.common.components.syspurview.controlpanel.domain.UserStyle;

/**
 * 具体的用户信息实体.如果系统需要扩展的话在此进行属性的扩展即可!
 * 
 * @author 马必强
 *
 */
public class User extends AbstractUser
{
	private int parentId;//所属部门deptId
	private String deptName="";
	private String id_number;//用户身份证号
	
	private UserStyle userStyle; // 用户的风格信息

	private String plainCode; //密码用明码保存
	
	private Integer town_id;
	private Integer agency_id;
	private Integer school_id;
	private Integer grade_id;
	private Integer class_id;
	private String id_card;
	private Integer status;
	
	public String getId_card() {
		return id_card;
	}


	public void setId_card(String id_card) {
		this.id_card = id_card;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public UserStyle getUserStyle() {
		return userStyle;
	}


	public void setUserStyle(UserStyle userStyle) {
		this.userStyle = userStyle;
	}


	public String getDeptName() {
		return deptName;
	}


	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}


	public int getParentId() {
		return parentId;
	}


	public void setParentId(int parentId) {
		this.parentId = parentId;
	}


	public String getId_number() {
		return id_number;
	}


	public void setId_number(String id_number) {
		this.id_number = id_number;
	}


	public String getPlainCode() {
		return plainCode;
	}


	public void setPlainCode(String plainCode) {
		this.plainCode = plainCode;
	}


	public Integer getTown_id() {
		return town_id;
	}


	public void setTown_id(Integer town_id) {
		this.town_id = town_id;
	}


	public Integer getAgency_id() {
		return agency_id;
	}


	public void setAgency_id(Integer agency_id) {
		this.agency_id = agency_id;
	}


	public Integer getSchool_id() {
		return school_id;
	}


	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}


	public Integer getGrade_id() {
		return grade_id;
	}


	public void setGrade_id(Integer grade_id) {
		this.grade_id = grade_id;
	}


	public Integer getClass_id() {
		return class_id;
	}


	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}
	
}
