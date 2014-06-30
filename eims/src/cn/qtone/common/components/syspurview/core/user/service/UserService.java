package cn.qtone.common.components.syspurview.core.user.service;

import java.util.List;

import cn.qtone.common.components.syspurview.core.group.dao.IUserGroupDAO;
import cn.qtone.common.components.syspurview.core.user.dao.IUserDAO;
import cn.qtone.common.components.syspurview.core.user.domain.IUser;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.components.syspurview.core.user.qvo.QvoUser;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.service.ServiceMsg;
import cn.qtone.common.simplemvc.dao.HibernateSimpleDao;
import cn.qtone.common.utils.base.DateUtil;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.common.utils.crypto.Encrypt;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;

/**
 * 用户管理的业务逻辑处理类。
 * 
 * @author 马必强
 *
 */
public class UserService extends HibernateSimpleDao<IUser>
{
	private IUserDAO dao;
	
	private IUserGroupDAO groupDao; // 用户组管理的DAO，获取用户组的列表
	
	public IUserGroupDAO getGroupDao()
	{
		return groupDao;
	}

	public void setGroupDao(IUserGroupDAO groupDao)
	{
		this.groupDao = groupDao;
	}

	public IUserDAO getDao()
	{
		return dao;
	}

	public void setDao(IUserDAO dao)
	{
		this.dao = dao;
	}
	
	/**
	 * 执行对指定用户的指定操作.
	 * @param lock
	 * @param userId
	 */
	public ServiceMsg lock(String lock, String[] userId)
	{
		ServiceMsg sms = new ServiceMsg();
		boolean _lock = StringUtil.trim(lock).equals("true");
		int[] uId = StringUtil.parseInt(userId, 0);
		if (uId.length < 1) {
			sms.setMessage("必须指定要执行操作的用户ID！");
			return sms;
		}
		if (_lock) this.dao.lock(uId);
		else this.dao.unlock(uId);
		sms.setSuccess(true);
		sms.setMessage("执行对指定用户的" + (_lock ? "封锁" : "解锁") + "操作成功！");
		return sms;
	}
	
	/**
	 * 用户列表的查询操作.
	 * @param page
	 */
	public void query(Page page, int curPage, String qName, String qGroupId)
	{
		int start = page.getPageSize() * (curPage - 1) + 1;
		page.setStartIndex(start);
		int groupId = StringUtil.parseInt(qGroupId, 0);
		String name = StringUtil.trim(qName).intern();
		// 根据用户组ID和查询名称进行分别查询.
		if (name == "" && groupId <= 0) this.dao.query(page);
		else if (name == "" && groupId >= 1) this.dao.query(page, groupId);
		else if (name != "" && groupId <= 0) this.dao.query(page, name);
		else this.dao.query(page, name, groupId);
		
	}
	
	/**
	 * 用户列表的查询操作.
	 * @param page
	 */
	public void query(Page page,QvoUser qvo)
	{
		int start = page.getPageSize() * (qvo.getCurPage() - 1) + 1;
		page.setStartIndex(start);
		// 根据用户组ID和查询名称进行分别查询.
//		if (name == "" && groupId <= 0) this.dao.query(page);
//		else if (name == "" && groupId >= 1) this.dao.query(page, groupId);
//		else if (name != "" && groupId <= 0) this.dao.query(page, name);
//		else this.dao.query(page, name, groupId);
		this.dao.query(page, qvo);
		
	}
	
	/**
	 * 查询班主任
	 * @param page
	 */
	public List<User> queryListClassMater(QvoUser qvo)
	{
		Page page = new Page();
		page.setStartIndex(0);
		page.setPageSize(1000);
		// 根据用户组ID和查询名称进行分别查询.
//		if (name == "" && groupId <= 0) this.dao.query(page);
//		else if (name == "" && groupId >= 1) this.dao.query(page, groupId);
//		else if (name != "" && groupId <= 0) this.dao.query(page, name);
//		else this.dao.query(page, name, groupId);
		this.dao.query(page, qvo);
		return (List<User>) page.getResult();
		
	}
	/**
	 * 根据学校获取学校所有班主任用户的下拉选择框. <br />
	 * @param schoolId. <br />
	 * @param seclected
	 * @return
	 */
	public String getZfptClassSelectHtml(String schoolId,String seclected){
		QvoUser qvo = new QvoUser();
		qvo.setQueryUserType("1");
		return getZfptClassSelectHtmlByUserList(queryListClassMater(qvo),seclected);
	}
	
	//将集合对象整理成下拉选择框的形式.
	private  String getZfptClassSelectHtmlByUserList(List<User> list,String selected){
		StringBuffer sb = new StringBuffer();
		for(User user:list){
			sb.append("<option value='");
			sb.append(user.getUserId());
            sb.append("'");
			if(ServletUtil.parseInt(selected,-1)==user.getUserId()){
				sb.append(" selected");
			}
            sb.append(">");
            sb.append(user.getRealName());
            sb.append("</option>");
		}
		return sb.toString();
	}
	/**
	 * 获取指定用户ID的信息.
	 * @param userId
	 * @return
	 */
	public ServiceMsg getUser(int userId)
	{
		ServiceMsg sms = new ServiceMsg();
		if (userId <= 0) {
			sms.setMessage("请指定正确的用户ID！");
			return sms;
		}
		User user = this.dao.getUser(userId);
		if (user == null) {
			sms.setMessage("指定的用户找不到！");
			return sms;
		}
		sms.setSuccess(true);
		sms.addObject("user", user);
		return sms;
	}
	
	public User getUserBean(int userId){
		return this.dao.getUser(userId);
	}
	
	/**
	 * 获取所有的用户组列表.
	 * @return
	 */
	public List getGroupList()
	{
		return this.groupDao.listAll();
	}
	
	public ServiceMsg removeUser(String[] userIds)
	{
		ServiceMsg sms = new ServiceMsg();
		int[] userId = StringUtil.parseInt(userIds, 0);
		if (userId.length < 1) {
			sms.setMessage("请指定要删除的用户ID！");
			return sms;
		}
		this.dao.removeUser(userId);
		this.dao.removeBorrow(userId);
		sms.setSuccess(true);
		sms.setMessage("用户删除成功！");
		return sms;
	}
	
	/**
	 * 添加用户.
	 * @param user
	 * @return
	 */
	public ServiceMsg addUser(User user)
	{
		ServiceMsg sms = new ServiceMsg();
		if (!this.checkUserInfo(sms, user, true)) return sms;
		// MD5算法加密用户密码
		user.setPlainCode(user.getLoginPassword());
		user.setLoginPassword(Encrypt.MD5(user.getLoginPassword()));
		this.dao.addUser(user);
		sms.setSuccess(true);
		sms.setMessage("用户信息添加成功！");
		return sms;
	}
	
	/**
	 * 更新用户信息.
	 * @param user
	 * @return
	 */
	public ServiceMsg updateUser(User user)
	{
		ServiceMsg sms = new ServiceMsg();
		if (!this.checkUserInfo(sms, user, false)) return sms;
		this.dao.updateUser(user);
		sms.setSuccess(true);
		sms.setMessage("用户信息更新成功！");
		return sms;
	}
	
	/**
	 * 检查用户名称的唯一性.
	 * @param loginName
	 * @return
	 */
	public ServiceMsg checkLoginName(String loginName)
	{
		ServiceMsg sms = new ServiceMsg();
		String name = StringUtil.trim(loginName);
		if (name.equals("")) {
			sms.setMessage("用户名称不能为空！");
			return sms;
		}
		int userId = this.dao.getUserIdByName(name);
		if (userId > 0) {
			sms.setMessage("当前用户名称已被使用，请使用新的用户名称，比如" + name + "123!");
			return sms;
		}
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 校验用户的信息是否正确.
	 * @param sms
	 * @param user
	 * @return
	 */
	private boolean checkUserInfo(ServiceMsg sms, User user, boolean add)
	{
		if (user.getRealName().equals("")) {
			sms.setMessage("用户的真实姓名不能为空！");
			return false;
		}else if (user.getGroupId() <= 0) {
			sms.setMessage("请为用户指定用户组！");
			return false;
		}
		if (add) {
			if (user.getLoginPassword().equals("")) {
				sms.setMessage("用户的登陆密码不能为空！");
				return false;
			} else if (user.getLoginName().equals("")) {
				sms.setMessage("用户的登陆名称不能为空！");
				return false;
			}
		} else {
			if (user.getUserId() <= 0) {
				sms.setMessage("必须指定要编辑的用户的唯一ID！");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 更改用户密码
	 * @param userId
	 * @return
	 */
	public ServiceMsg updatePwd(int[] userId)
	{
		ServiceMsg sms = new ServiceMsg();
		if(userId.length <= 0){
			sms.setMessage("请选择要进行密码重置的用户！");
			return sms;
		}
		if (StringUtil.include(userId, -1)) {
			sms.setMessage("指定的用户ID中包含非法的Id值！");
			return sms;
		}
		String date = "123456";//this.getDate();
		String pwd = Encrypt.MD5(date);
		this.dao.updatePwd(userId, pwd,date);
		sms.setSuccess(true);
		sms.setMessage("用户密码重置成功！新密码为：" + date);
		return sms;
	}
	
	private String getDate(){
		String date = "";
		date += DateUtil.getYear();
		date += DateUtil.getMonth();
		date += DateUtil.getDay();
		return date;
	}
}
