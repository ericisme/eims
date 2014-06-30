package cn.qtone.common.components.syspurview.core.user.qvo;

public class QvoUser {

	private String qryName;
	private String qryGroupId;
	private String queryUserType;
	private int curPage;
	public int getCurPage() {
		return curPage;
	}
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	public String getQryName() {
		return qryName;
	}
	public void setQryName(String qryName) {
		this.qryName = qryName;
	}
	public String getQryGroupId() {
		return qryGroupId;
	}
	public void setQryGroupId(String qryGroupId) {
		this.qryGroupId = qryGroupId;
	}
	public String getQueryUserType() {
		return queryUserType;
	}
	public void setQueryUserType(String queryUserType) {
		this.queryUserType = queryUserType;
	}
}
