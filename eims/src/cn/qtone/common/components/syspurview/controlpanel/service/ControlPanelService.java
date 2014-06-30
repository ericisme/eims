package cn.qtone.common.components.syspurview.controlpanel.service;

import cn.qtone.common.components.syspurview.controlpanel.dao.IControlPanelDAO;
import cn.qtone.common.components.syspurview.controlpanel.domain.UserCryptoguard;
import cn.qtone.common.components.syspurview.core.user.domain.AbstractUser;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.mvc.service.ServiceMsg;
import cn.qtone.common.utils.crypto.Encrypt;

/**
 * 控制面板的业务处理类.
 * 
 * @author 马必强
 *
 */
public class ControlPanelService
{
	private IControlPanelDAO dao;
	
	public IControlPanelDAO getDao()
	{
		return dao;
	}

	public void setDao(IControlPanelDAO dao)
	{
		this.dao = dao;
	}

	/**
	 * 更新用户的个人信息.
	 * @param user
	 * @return
	 */
	public ServiceMsg updatePersonalInfo(User user, User realUser)
	{
		ServiceMsg sms = new ServiceMsg();
		// 加密个人密码
		user.setLoginPassword(Encrypt.MD5(user.getLoginPassword()));
		System.out.println("------------------" + user.getLoginPassword());
		System.out.println("------------------" + realUser.getLoginPassword());
		if (!realUser.getLoginPassword().equals(user.getLoginPassword())) {
			sms.setMessage("[更新失败]用户当前帐号密码错误！");
			return sms;
		} else if (user.getRealName().intern() == "") {
			sms.setMessage("[更新失败]真实姓名不能为空！");
			return sms;
		} else if (user.getMobile().intern() == "") {
			sms.setMessage("[更新失败]用户手机号码不能为空！");
			return sms;
		} 
		this.dao.updateUserInfo(user);
		sms.setMessage("个人信息更新成功！");
		sms.setSuccess(true);
		// 更新成功后，修改session中用户的相关信息
		realUser.setRealName(user.getRealName());
		realUser.setMobile(user.getMobile());
		realUser.setEmail(user.getEmail());
		sms.addObject("userInfo", realUser);
		return sms;
	}
	
	/**
	 * 用户密码修改.
	 * @param oldPwd
	 * @param newPwd
	 * @param reNewPwd
	 * @param user
	 * @return
	 */
	public ServiceMsg updatePassowrd(String oldPwd, String newPwd, 
			String reNewPwd, AbstractUser user)
	{
		ServiceMsg sms = new ServiceMsg();
		if (!user.getLoginPassword().equals(Encrypt.MD5(oldPwd))) {
			sms.setMessage("[密码修改失败]原始密码错误！");
			return sms;
		} else if (!newPwd.equals(reNewPwd)) {
			sms.setMessage("[密码修改失败]新密码和确认密码不一致！");
			return sms;
		} else if (newPwd.intern() == "" || newPwd.length() < 6) {
			sms.setMessage("[密码修改失败]新密码不能为空，且长度不能少于6位！");
			return sms;
		}
		String encryptedPwd = Encrypt.MD5(newPwd);
		// 如果修改后的密码和原始密码不一样，则才进行数据库更新
		if (!user.getLoginPassword().equals(encryptedPwd)) {
			this.dao.updatePassword(user.getUserId(), encryptedPwd, newPwd);
			// 更新密码成功后，修改session中的用户密码
			user.setLoginPassword(encryptedPwd);
			sms.addObject("userInfo", user);
		}
		sms.setMessage("密码修改成功！");
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 获取用户的密码保护设置信息.
	 * @param userId
	 * @return
	 */
	public UserCryptoguard getUserCryptoguard(AbstractUser user)
	{
		UserCryptoguard guard = this.dao.getCryptoguard(user.getUserId());
		// 如果用户还没有设置密码保护，则默认使用用户信息中的手机号码和电子邮件地址
		if (guard.getUserId() == 0) {
			guard.setUserEmail(user.getEmail());
			guard.setUserMobile(user.getMobile());
		}
		return guard;
	}
	
	/**
	 * 更新用户的密码保护信息
	 * @param guard
	 * @return
	 */
	public ServiceMsg updateUserCryptoguard(UserCryptoguard guard, int userId)
	{
		ServiceMsg sms = new ServiceMsg();
		if (guard.getUserQuestion().intern() == "") {
			sms.setMessage("密码保护的问题不能为空！");
			return sms;
		} else if (guard.getUserAnswer().intern() == "") {
			sms.setMessage("密码保护问题的答案不能为空！");
			return sms;
		} else if (guard.isUseEmailMethod() && guard.getUserEmail().intern() == "") {
			sms.setMessage("使用Email取回密码必须指定Email地址！");
		} else if (!guard.isUseEmailMethod() && guard.getUserMobile().intern() == "") {
			sms.setMessage("使用手机取回密码必须指定手机号码！");
			return sms;
		}
		guard.setUserId(userId);
		this.dao.updateUserCryptoguard(guard);
		sms.setMessage("密码保护资料更新成功！");
		sms.setSuccess(true);
		return sms;
	}
}
