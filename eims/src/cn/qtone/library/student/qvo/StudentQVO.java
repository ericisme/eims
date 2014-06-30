package cn.qtone.library.student.qvo;

/**
 * @author 学生查询条件.
 * 
 */
public class StudentQVO {
	
	private Integer id;
	/**
	 * 机构ID
	 */
	private int agency_id;
	/**
	 * 学校ID
	 */
	private int school_id;
	/**
	 * 年级
	 */
	private int grade;
	/**
	 * 班级ID
	 */
	private Integer class_id;

	private int startPage;
	
	private String student_name;
	
	private String agency_name;
	
	private String school_name;
	
	private String grade_name;
	
	private String class_name;
	
	
	private int orderclass;

	private int pageSize;
	
	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(int agency_id) {
		this.agency_id = agency_id;
	}

	public int getSchool_id() {
		return school_id;
	}

	public void setSchool_id(int school_id) {
		this.school_id = school_id;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public Integer getClass_id() {
		return class_id;
	}

	public void setClass_id(Integer class_id) {
		this.class_id = class_id;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public String getGrade_name() {
		return grade_name;
	}

	public void setGrade_name(String grade_name) {
		this.grade_name = grade_name;
	}

	public String getSchool_name() {
		return school_name;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getOrderclass() {
		return orderclass;
	}

	public void setOrderclass(int orderclass) {
		this.orderclass = orderclass;
	}

	public String getAgency_name() {
		return agency_name;
	}

	public void setAgency_name(String agency_name) {
		this.agency_name = agency_name;
	}
}
