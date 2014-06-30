package cn.qtone.crda.classstudent.qvo;

/**
 * 成人档案袋
 * 
 * @author 王培森
 * @version 1.0
 */
public class ClassStudentQVO{

	/**
 	 * 主键
 	 */ 
	private Integer id;

	private Integer class_id;
 
	private Integer menber_id;

	private String unique_no;

	private Integer school_id;
	
	private String class_name;
	
	private String school_name;
	
	private Integer agency_id;
	
	private Integer is_valid;
	
	private int startPage;
	
	private int pageSize;
	
	public Integer getId(){
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}

	public Integer getClass_id() {
		return class_id;
	}

	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}

	public Integer getMenber_id() {
		return menber_id;
	}

	public void setMenber_id(Integer menber_id) {
		this.menber_id = menber_id;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}

	public String getUnique_no() {
		return unique_no;
	}

	public void setUnique_no(String unique_no) {
		this.unique_no = unique_no;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public String getSchool_name() {
		return school_name;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}

	public Integer getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(Integer agency_id) {
		this.agency_id = agency_id;
	}

	public Integer getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(Integer is_valid) {
		this.is_valid = is_valid;
	}

}