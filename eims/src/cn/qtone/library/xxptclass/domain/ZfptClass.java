package cn.qtone.library.xxptclass.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.qtone.library.XxptContants;
import cn.qtone.library.school.domain.School;

/**
 * 基础管理 - 班级管理实体bean.<br>
 * 
 * @author 贺少辉
 * @version 1.0
 *
 */
@Entity
@Table(name = "library_class")
public class ZfptClass{

	/**
 	 * 主键
 	 */ 
	private Integer id;

	/**
 	 * 班级所属的学校
 	 */ 
	private School school;

	/**
 	 * 班级编号
 	 */ 
	private String class_code;
	
	/**
	 * 班级名称.
	 */
	private String class_name;
	
	/**
	 * 班级描述
	 */
	private String classDesc;
	  
    
    private Integer ordergrade;
    
    private Integer orderclass; 
    
    private Integer grade;
	  
	public Integer getOrdergrade() {
		return ordergrade;
	}

	public void setOrdergrade(Integer ordergrade) {
		this.ordergrade = ordergrade;
	}

	public Integer getOrderclass() {
		return orderclass;
	}

	public void setOrderclass(Integer orderclass) {
		this.orderclass = orderclass;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)  
	public Integer getId(){
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "school_id")
	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public String getClass_code(){
		return class_code;
	}
	
	public void setClass_code(String class_code){
		this.class_code = class_code;
	}
	
	/**
	 * 根据班级代码获取所在的年级值.
	 */
	public String gradeValue(){
		return XxptContants.getGradeValue(class_code.split("_")[0]);
	}

	public ZfptClass() {
		super();
	}

	public ZfptClass(Integer id) {
		super();
		this.id = id;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	
	public String getClassDesc() {
		return classDesc;
	}

	public void setClassDesc(String classDesc) {
		this.classDesc = classDesc;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}
}

