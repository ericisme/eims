package cn.qtone.common.components.syspurview.login.service;

import java.util.Date;

import org.apache.log4j.Logger;

import cn.qtone.common.components.syspurview.controlpanel.dao.IControlPanelDAO;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.components.syspurview.login.dao.ILoginDAO;
import cn.qtone.common.components.syspurview.login.domain.PurviewMap;
import cn.qtone.common.mvc.service.ServiceMsg;
import cn.qtone.common.utils.base.DateUtil;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.common.utils.crypto.Encrypt;

/**
 * 登陆验证的业务处理类.
 * 
 * @author 马必强
 *
 */
public class LoginService
{
	private static final Logger log = Logger.getLogger(LoginService.class);

	private ILoginDAO dao;
	
	private IControlPanelDAO controlPanelDAO; // 控制面板DAO，主要用来获取用户的个性设置

	
	/**
	 * 用户登陆校验.
	 * @param userName
	 * @param userPwd
	 * @return
	 */
	public ServiceMsg login(String userName, String userPwd)
	{
		ServiceMsg sms = new ServiceMsg();
		
//		if(DateUtil.isAfter(new Date(), DateUtil.parseSimpleDateTime("2013-5-5 00:00:00"))){
//			sms.setMessage("[登陆失败]：用户名或密码不能为空！");
//			return sms;
//		}
		
		String uName = StringUtil.trim(userName);
		String uPwd = Encrypt.MD5(StringUtil.trim(userPwd)); // 加密密码
		if (uName.equals("") || uPwd.equals("")) {
			sms.setMessage("[登陆失败]：用户名或密码不能为空！");
			return sms;
		}
		User user = this.dao.getUserInfo(uName, uPwd);
		if (user == null) {
			sms.setMessage("[登陆失败]：用户名或密码错误！");
			return sms;
		} else if (user.isLock()) {
			sms.setMessage("[登陆失败]：当前帐号已被封锁，无法进行登陆。请联系管理员解决！");
			return sms;
		}
		return this.loginSuccess(sms, user);
	}
	
	/**
	 * 代理登陆.<P>
	 * 注意代理登陆时不需要检测帐号是否已被封锁，同时也不需要更新其登陆次数和最后登陆时间.
	 * 
	 * @param userId 当前的用户ID（系统管理员后超级管理员的ID）
	 * @param proxyUserId 要代理的用户的唯一ID
	 * @return
	 */
	public ServiceMsg proxyLogin(int userId, String userName, int proxyUserId)
	{
		ServiceMsg sms = new ServiceMsg();
		User user = this.dao.getProxyUser(userId, userName, proxyUserId);
		if (user == null) {
			sms.setMessage("[代理登陆失败]：指定的用户不存在！");
			return sms;
		}
		
		return this.loginSuccess(sms, user);
	}
	
	/**
	 * 代理登陆后用户的返回操作.
	 * @param userId
	 * @return
	 */
	public ServiceMsg proxyLoginBack(int userId)
	{
		if (log.isInfoEnabled()) log.info("用户[" + userId + "]执行代理回退操作！");
		ServiceMsg sms = new ServiceMsg();
		User user = this.dao.getUser(userId);
		if (user == null) {
			sms.setMessage("您的帐号已不存在，无法进行登陆！");
			return sms;
		}
		return this.loginSuccess(sms, user);
	}
	
	/**
	 * 登陆成功后的相关设置，如权限等信息.
	 * @param sms
	 * @param user
	 * @return
	 */
	private ServiceMsg loginSuccess(ServiceMsg sms, User user)
	{
		this.loadUserPurview(user);
		sms.setSuccess(true);
		sms.setMessage("登陆成功！");
		sms.addObject("user", user);
		return sms;
	}
	
	/**
	 * 更新用户的最后登陆时间和登陆地址.
	 * @param userId
	 * @param loginIP
	 */
	public void updateLastLogin(int userId, String loginIP)
	{
		if (log.isInfoEnabled()) log.info("更新用户[" + userId + "]的最后登陆地点和时间！");
		this.dao.updateLastLogin(userId, StringUtil.trim(loginIP));
	}
	
	/**
	 * 加载用户的角色权限.
	 * @param user
	 */
	protected void loadUserPurview(User user)
	{
		// 如果用户为超级管理员则不加载权限
		if (user.isSuperManager()) return;
		if (log.isInfoEnabled()) {
			log.info("加载用户[userId=" + user.getUserId() + ",name=" + user.getLoginName()
					+ "]所属用户组[" + user.getGroupId() + "]的权限！");
		}
		PurviewMap purview = this.dao.getRolePurview(user);
		user.setPurviewMap(purview);
	}

	public ILoginDAO getDao()
	{
		return dao;
	}

	public void setDao(ILoginDAO dao)
	{
		this.dao = dao;
	}
	
	public IControlPanelDAO getControlPanelDAO()
	{
		return controlPanelDAO;
	}

	public void setControlPanelDAO(IControlPanelDAO controlPanelDAO)
	{
		this.controlPanelDAO = controlPanelDAO;
	}

}
