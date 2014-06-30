package cn.qtone.common.components.syspurview.core.user.domain;

import org.apache.commons.lang.StringUtils;

import cn.qtone.ContantsUtil;
import cn.qtone.common.components.syspurview.core.system.SystemCache;
import cn.qtone.common.components.syspurview.login.domain.PurviewMap;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.library.XxptContants;

/**
 * 用户信息Bean. 包含了最基本的用户信息！
 * 
 * @author 马必强
 * 
 */
public abstract class AbstractUser
{
	private int userId; // 用户的唯一ID值
	private String schoolName;
	private int groupId; // 用户所属的用户组ID
	private String loginSys; // 当前用户所属用户组的登陆子系统的标记
	private int[] roleId; // 该用户所对应的用户组对应的角色ID列表
	private String groupName; // 用户所属用户组的组名称
	private String loginName; // 登陆名称
	private String realName; // 真实姓名
	private String loginPassword; // 登陆密码
	private String mobile; // 手机号码
	private String email; // 用户电邮
	private String addTime; // 用户添加时间
	private String lastLoginTime; // 用户最后登陆时间
	private String lastLoginIP; // 用户最后登陆IP地址
	private int loginTimes; // 登陆的总次数
	private boolean lock; // 帐号是否被停用，默认为false
	private boolean superManager; // 是否超级管理员，默认为false
	private PurviewMap purviewMap; // 用户的权限集合
	private int proxyUserId = -1; // 代理人的用户ID，使用代理登陆的时候用到

	private String proxyUserName; // 代理人的用户的真实姓名
	private Integer school_id;//学校ID
	private String userType;//0:系统管理 1:学校管理员
	
	
	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public int getUserId()
	{
		return userId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	

	public int getGroupId()
	{
		return groupId;
	}

	public void setGroupId(int groupId)
	{
		this.groupId = groupId;
	}

	

	public String getLoginSys()
	{
		return loginSys;
	}

	public void setLoginSys(String loginSys)
	{
		SystemCache sysCache = SystemCache.getInstance();
		int sysId = sysCache.getSystemId(StringUtil.trim(loginSys));
		this.loginSys = sysId <= 0 ? sysCache.getSystem(
				sysCache.getDefaultSystemId()).getSysFlag() : StringUtil
				.trim(loginSys);
	}

	

	public int[] getRoleId()
	{
		return roleId;
	}

	public void setRoleId(int[] roleId)
	{
		this.roleId = roleId;
	}

	

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = StringUtils.trimToEmpty(groupName);
	}

	

	public String getLoginName()
	{
		return loginName;
	}

	public void setLoginName(String loginName)
	{
		this.loginName = StringUtils.trimToEmpty(loginName);
	}

	

	public String getLoginPassword()
	{
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword)
	{
		this.loginPassword = StringUtils.trimToEmpty(loginPassword);
	}

	

	public String getRealName()
	{
		return realName;
	}

	public void setRealName(String realName)
	{
		this.realName = StringUtils.trimToEmpty(realName);
	}

	

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = StringUtils.trimToEmpty(mobile);
	}
	

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = StringUtils.trimToEmpty(email);
	}

	

	public String getAddTime()
	{
		return addTime;
	}

	public void setAddTime(String addTime)
	{
		this.addTime = StringUtils.trimToEmpty(addTime);
	}
	

	public String getLastLoginTime()
	{
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime)
	{
		this.lastLoginTime = StringUtils.trimToEmpty(lastLoginTime);
	}
	

	public String getLastLoginIP()
	{
		return lastLoginIP;
	}

	public void setLastLoginIP(String lastLoginIP)
	{
		this.lastLoginIP = StringUtils.trimToEmpty(lastLoginIP);
	}
	

	public int getLoginTimes()
	{
		return loginTimes;
	}

	public void setLoginTimes(int loginTimes)
	{
		this.loginTimes = loginTimes;
	}
	

	public boolean isLock()
	{
		return lock;
	}

	public void setLock(boolean lock)
	{
		this.lock = lock;
	}
	

	public boolean isSuperManager()
	{
		return superManager;
	}

	public void setSuperManager(boolean superManager)
	{
		this.superManager = superManager;
	}
	

	public PurviewMap getPurviewMap()
	{
		return purviewMap;
	}

	public void setPurviewMap(PurviewMap purviewMap)
	{
		this.purviewMap = purviewMap;
	}
	
	public int getProxyUserId()
	{
		return proxyUserId;
	}

	public void setProxyUserId(int proxyUserId)
	{
		this.proxyUserId = proxyUserId;
	}
	

	public String getProxyUserName()
	{
		return proxyUserName;
	}

	public void setProxyUserName(String proxyUserName)
	{
		this.proxyUserName = proxyUserName;
	}
	

	public Integer getSchool_id() {
		return school_id;
	}

	public void setSchool_id(Integer school_id) {
		this.school_id = school_id;
	}
	

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String uerTypeValue(){
		return  ContantsUtil.getSystemContantsMapValue(XxptContants.MANAGE_USER_TYPE, this.userType);
	}
	
}
