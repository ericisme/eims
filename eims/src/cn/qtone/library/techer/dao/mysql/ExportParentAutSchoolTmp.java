package cn.qtone.library.techer.dao.mysql;

/**
 * @author 贺少辉
 * 导入家长的时候验证学校名称的有效性的临时对象.
 */
public class ExportParentAutSchoolTmp {
	
	private String agency_name;
	private String school_name;
	
	public String getAgency_name() {
		return agency_name;
	}
	public void setAgency_name(String agency_name) {
		this.agency_name = agency_name;
	}
	public String getSchool_name() {
		return school_name;
	}
	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}
}
