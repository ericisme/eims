package cn.qtone.library.student.domain;


public class ErrorStudent {
	private Integer line;
	private String realName;
	private String loginName;
	private String birthday;
	private String gender;
	private String id_card;
	private String errorReason;
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getId_card() {
		return id_card;
	}
	public void setId_card(String id_card) {
		this.id_card = id_card;
	}
	public String getErrorReason() {
		return errorReason;
	}
	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}
	public Integer getLine() {
		return line;
	}
	public void setLine(Integer line) {
		this.line = line;
	}
	

}
