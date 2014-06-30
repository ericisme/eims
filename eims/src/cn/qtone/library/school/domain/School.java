package cn.qtone.library.school.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;

import cn.qtone.library.XxptContants;
import cn.qtone.library.agency.domain.Agency;

/**
 * 基础管理 - 学校管理实体bean.<br>
 * 
 * @author 贺少辉
 * @version 1.0
 *
 */
@Entity
@Table(name = "library_school")
public class School{

	/**
 	 * 主键
 	 */ 
	private Integer id;

	/**
 	 * 学校名称
 	 */ 
	private String school_name;

	
	/**
	 * 学校包含的年级类型.
	 */
	private String containGrade;
	
	/**
	 * 学校描述
	 */
	private String schoolDesc;
	
	/**
 	 * 学校所属的机构
 	 */ 
	private Agency agency;
	
	private String school_no;//学校编码
	
	private String school_code;//学校代码
	

	public String getSchool_no() {
		return school_no;
	}

	public void setSchool_no(String school_no) {
		this.school_no = school_no;
	}

	public String getSchool_code() {
		return school_code;
	}

	public void setSchool_code(String school_code) {
		this.school_code = school_code;
	}

	public String getSchoolDesc() {
		return schoolDesc;
	}

	public void setSchoolDesc(String schoolDesc) {
		this.schoolDesc = schoolDesc;
	}

	public School(Integer id) {
		this.id = id;
	}

	public School() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)  
	public Integer getId(){
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}


	public String getSchool_name(){
		return school_name;
	}
	
	public void setSchool_name(String school_name){
		this.school_name = school_name;
	}

	public String getContainGrade() {
		return containGrade;
	}

	public void setContainGrade(String containGrade) {
		this.containGrade = containGrade;
	}

	/**
	 * 根据年级类型的数字值获取对应的文字值.
	 */
	public String containGradeValue() {
		StringBuffer sb = new StringBuffer();
		if(StringUtils.isNotBlank(containGrade)){
			for(String gradeType:containGrade.split(",")){
				sb.append(XxptContants.GRADETYPE.get(gradeType)+",");
			}
		}
		String s = sb.toString();
		if(s.lastIndexOf(",")>0){
			s = s.substring(0,s.length()-1);
		}
		return s;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "agency_id")
	public Agency getAgency() {
		return agency;
	}

	public void setAgency(Agency agency) {
		this.agency = agency;
	}

}