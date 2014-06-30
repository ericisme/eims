package cn.qtone.crda.classstudent.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 成人档案袋
 * 
 * @author 王培森
 * @version 1.0
 */
@Entity
@Table(name = "crda_class_student")
public class ClassStudent{

	/**
 	 * 主键
 	 */ 
	private Integer id;

	private Integer class_id;
 
	private Integer member_id;

	private String unique_no;

	private Integer checked;
	
	private Integer is_valid;

	private Integer type;

	private String auth_context;
	
	private String lastvisitime;
	
	private Integer vist_count;
	
	private String cr_date;
	
	private Integer postion;
	
	private Integer is_squad;
	
	private String elected_date;
	
	private Integer last_month_visit_count;
	
	private Integer last_visit_count;
	
	private Integer class_create_event;
	
	private String truename;
	
	private String class_name;
	
	private Integer school_id;
	
	private String school_name;
	
	private String teacher_no;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId(){
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}

	public String getAuth_context() {
		return auth_context;
	}

	public void setAuth_context(String auth_context) {
		this.auth_context = auth_context;
	}

	public Integer getChecked() {
		return checked;
	}

	public void setChecked(Integer checked) {
		this.checked = checked;
	}

	public Integer getClass_create_event() {
		return class_create_event;
	}

	public void setClass_create_event(Integer class_create_event) {
		this.class_create_event = class_create_event;
	}

	public Integer getClass_id() {
		return class_id;
	}

	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}

	public String getCr_date() {
		return cr_date;
	}

	public void setCr_date(String cr_date) {
		this.cr_date = cr_date;
	}

	public String getElected_date() {
		return elected_date;
	}

	public void setElected_date(String elected_date) {
		this.elected_date = elected_date;
	}

	public Integer getIs_squad() {
		return is_squad;
	}

	public void setIs_squad(Integer is_squad) {
		this.is_squad = is_squad;
	}

	public Integer getLast_month_visit_count() {
		return last_month_visit_count;
	}

	public void setLast_month_visit_count(Integer last_month_visit_count) {
		this.last_month_visit_count = last_month_visit_count;
	}

	public Integer getLast_visit_count() {
		return last_visit_count;
	}

	public void setLast_visit_count(Integer last_visit_count) {
		this.last_visit_count = last_visit_count;
	}

	public String getLastvisitime() {
		return lastvisitime;
	}

	public void setLastvisitime(String lastvisitime) {
		this.lastvisitime = lastvisitime;
	}

	public Integer getMember_id() {
		return member_id;
	}

	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}

	public Integer getPostion() {
		return postion;
	}

	public void setPostion(Integer postion) {
		this.postion = postion;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getVist_count() {
		return vist_count;
	}

	public void setVist_count(Integer vist_count) {
		this.vist_count = vist_count;
	}

	@Transient
	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	@Transient
	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	@Transient
	public String getSchool_name() {
		return school_name;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}

	@Transient
	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	@Transient
	public String getUnique_no() {
		return unique_no;
	}

	public void setUnique_no(String unique_no) {
		this.unique_no = unique_no;
	}

	@Transient
	public Integer getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(Integer is_valid) {
		this.is_valid = is_valid;
	}
	
	/**
	 * 根据判断是否有效的文字值.
	 */
	public String containIsValidValue() {
		StringBuffer sb = new StringBuffer();
		if (this.is_valid == 0)
			sb.append("否");
		else if(this.is_valid == 1)
			sb.append("是");
		return sb.toString();
	}

	@Transient
	public String getTeacher_no() {
		return teacher_no;
	}

	public void setTeacher_no(String teacher_no) {
		this.teacher_no = teacher_no;
	}
}