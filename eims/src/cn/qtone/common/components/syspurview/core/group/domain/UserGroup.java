package cn.qtone.common.components.syspurview.core.group.domain;

import org.apache.commons.lang.StringUtils;

import cn.qtone.common.components.syspurview.core.system.SystemCache;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 用户组信息对象.
 * 
 * @author 马必强
 *
 */
public class UserGroup
{
	private int groupId; // 用户组的唯一ID
	
	private String groupName; // 用户组的名称
	
	private String groupAddTime; // 用户组的添加时间
	
	private String groupDetail; // 用户组的备注信息
	
	private int groupUserNum; // 用户组下的用户数
	
	private int[] groupRole; // 用户组下的角色ID列表
	
	private String loginSys; // 该用户组成员登陆的子系统的标记
	
	private int book_limit;//可借本数
	
	private int day_limit;//可借天数
	
	private double deposit;//压金
	
	private double fine;//罚金
	
	private static SystemCache sysCache = SystemCache.getInstance();

	public String getLoginSys()
	{
		return loginSys == null ? sysCache.getSystem(sysCache.getDefaultSystemId()).getSysFlag()
				: loginSys;
	}

	public void setLoginSys(String loginSys)
	{
		int sysId = sysCache.getSystemId(StringUtil.trim(loginSys));
		this.loginSys = sysId <= 0 ? sysCache.getSystem(sysCache.getDefaultSystemId()).getSysFlag()
				: StringUtil.trim(loginSys);
	}

	public int[] getGroupRole()
	{
		return groupRole;
	}

	public void setGroupRole(int[] groupRole)
	{
		this.groupRole = groupRole;
	}

	public String getGroupAddTime()
	{
		return groupAddTime;
	}

	public void setGroupAddTime(String groupAddTime)
	{
		this.groupAddTime = StringUtils.trimToEmpty(groupAddTime);
	}

	public String getGroupDetail()
	{
		return groupDetail;
	}

	public void setGroupDetail(String groupDetail)
	{
		this.groupDetail = StringUtils.trimToEmpty(groupDetail);
	}

	public int getGroupId()
	{
		return groupId;
	}

	public void setGroupId(int groupId)
	{
		this.groupId = groupId;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = StringUtils.trimToEmpty(groupName);
	}

	public int getGroupUserNum()
	{
		return groupUserNum;
	}

	public void setGroupUserNum(int groupUserNum)
	{
		this.groupUserNum = groupUserNum;
	}

	public int getBook_limit() {
		return book_limit;
	}

	public void setBook_limit(int book_limit) {
		this.book_limit = book_limit;
	}

	public int getDay_limit() {
		return day_limit;
	}

	public void setDay_limit(int day_limit) {
		this.day_limit = day_limit;
	}

	public double getDeposit() {
		return deposit;
	}

	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}

	public double getFine() {
		return fine;
	}

	public void setFine(double fine) {
		this.fine = fine;
	}
}
