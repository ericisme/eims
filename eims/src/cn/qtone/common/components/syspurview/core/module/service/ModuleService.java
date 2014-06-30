package cn.qtone.common.components.syspurview.core.module.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import cn.qtone.common.components.syspurview.core.module.ModuleTree;
import cn.qtone.common.components.syspurview.core.module.dao.IModuleDAO;
import cn.qtone.common.components.syspurview.core.module.domain.Module;
import cn.qtone.common.components.syspurview.core.module.domain.ModuleFunc;
import cn.qtone.common.components.syspurview.core.module.domain.ModuleSingleFunc;
import cn.qtone.common.components.syspurview.core.module.domain.TokenMaker;
import cn.qtone.common.components.syspurview.core.system.SystemCache;
import cn.qtone.common.mvc.service.ServiceMsg;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.common.utils.json.SelectJSON;

/**
 * 模块的业务逻辑处理类。
 * 
 * @author 马必强
 *
 */
public class ModuleService
{
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(ModuleService.class);

	private IModuleDAO dao;
	
	private TransactionTemplate tt;
	
	/**
	 * 根据指定的参数做列表查询.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ServiceMsg query(String sysId, String mName, String token)
	{
		ServiceMsg sms = new ServiceMsg();
		
		// 获取参数,默认子系统的ID为1
		SystemCache cache = SystemCache.getInstance();
		int _sysId = StringUtil.parseInt(sysId, cache.getDefaultSystemId());
		String _mName = StringUtil.trim(mName).intern();
		String _token = StringUtil.trim(token).intern();
		
		// 执行查询
		List result = null;
		boolean limitChild = true;
		if (_mName == "" && _token == "") {
			result = this.dao.query(_sysId);
		} else if (_mName != "" && _token == "") {
			result = this.dao.query(_sysId, _mName);
			limitChild = false;
		} else {
			result = this.dao.query(token);
		}
		if (log.isInfoEnabled()) {
			log.info("查询结果数是：" + result.size());
		}
		sms.addObject("queryByName", !limitChild);
		sms.addObject("noResult", result.size() < 1);
		List cont = new ArrayList(); // 包含list,每一个list表示一个一级模块和其子模块
		if (result.size() < 1) {
			cont.add(result);
			sms.addObject("list", cont);
			return sms;
		}
		if (!limitChild) {
			cont.add(result);
		} else {
			List tmp = splitModuleList(ModuleTree.getModuleTree(result, false));
			for (Object obj : tmp) cont.add(obj);
		}

		sms.addObject("list", cont);
		return sms;
	}
	
	/**
	 * 将指定的模块列表中（只有一级和二级）按照第一级进行分割形成单独的列表返回.
	 * @param result
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List splitModuleList(List result)
	{
		String token = ((Module)result.get(0)).getMToken();
		int start = 0;
		List<List> array = new ArrayList<List>();
		Module mod = null;
		for (int i = 0; i < result.size(); i ++) {
			mod = (Module)result.get(i);
			if (mod.getMToken().startsWith(token)) continue;
			array.add(result.subList(start, i));
			token = mod.getMToken();
			start = i;
		}
		// 如果token和最后一个module的token一致则表示最后一个是一级模块
		if (token.equals(mod.getMToken())) {
			List<Module> tmp = new ArrayList<Module>();
			tmp.add(mod);
			array.add(tmp);
		} else {
			// 如果array的size小于1则表示当前list中只有一个一级模块
			if (array.size() < 1) array.add(result);
			else {
				for (int i = 0; i < result.size(); i ++) {
					Module tmp = (Module)result.get(i);
					if (tmp.getMToken().equals(token)) {
						array.add(result.subList(i, result.size()));
						break;
					}
				}
				//array.get(array.size() - 1).add(mod);
			}
		}
		return array;
	}
	
	/**
	 * 根据指定的token来获取模块信息.
	 * @param token
	 * @return
	 */
	public Module getModuleByToken(String token)
	{
		String mToken = StringUtil.trim(token);
		if (mToken.intern() == "") return null;
		return this.dao.getModule(mToken);
	}
	
	/**
	 * 获取一个指定的子系统下的所有菜单模块(可用和不可用都获取），添加页面和编辑页面用到.
	 * @param sysId
	 * @return
	 */
	public List getAllMenu(int sysId)
	{
		List orginList = this.dao.queryMenu(sysId);
		if (log.isInfoEnabled()) log.info("菜单总数为：" + orginList.size());
		return ModuleTree.getModuleTree(orginList, true);
	}
	
	/**
	 * 获取一个指定子系统下的所有菜单模块(可用和不可用都获取），并过滤掉其token具.
	 * 有指定前缀的模块这个在编辑菜单模块时用到，即菜单模块不能选择其父菜单为其本
	 * 身和其子菜单模块，对于功能模块的编辑就是所有的菜单模块了。
	 * @param sysId
	 * @param filterToken
	 * @return
	 */
	public List getAllMenu(int sysId, String filterToken)
	{
		List orginList = this.dao.queryMenu(sysId);
		List<Module> newList = new ArrayList<Module>();
		for (Object obj : orginList) {
			Module mod = (Module)obj;
			if (mod.getMToken().startsWith(filterToken)) continue;
			newList.add(mod);
		}
		return ModuleTree.getModuleTree(newList, true);
	}
	
	/**
	 * 将菜单模块的树形结构列表转换成JSON对象形式返回给客户端.
	 * @param list
	 * @return
	 */
	public ServiceMsg menuTreeToJSON(String sysId)
	{
		ServiceMsg sms = new ServiceMsg();
		int sId = StringUtil.parseInt(sysId, -1);
		if (sId == -1) {
			sms.setMessage("指定的子系统ID[" + sysId + "]非法，无法获取菜单！");
			return sms;
		}
		List result = this.getAllMenu(sId); // 获取所有可用菜单
		sms.addObject("sltJson", SelectJSON.fromList(result, "MToken", "MName"));
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 获取指定的模块信息.
	 * @param moduleId
	 * @return
	 */
	public ServiceMsg getModule(String moduleId)
	{
		ServiceMsg sms = new ServiceMsg();
		int mId = StringUtil.parseInt(moduleId, -1);
		if (mId == -1) {
			sms.setMessage("指定的模块ID非法，无法获取模块信息！");
			return sms;
		}
		Module mod = this.dao.getModule(mId);
		if(log.isInfoEnabled()) log.info("模块信息："+StringUtil.reflectObj(mod));
		if (mod == null) {
			sms.setMessage("指定的模块信息不存在，无法进行编辑！");
			return sms;
		}
		sms.addObject("Mod", mod);
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 更新模块信息.
	 * 模块的更新操作涉及到模块本身的信息更新，如果模块为菜单的话，那么还要更新
	 * 其子模块的所有token信息，这就涉及到事务的管理，要么都更新成功，要么都回退。
	 * 这里的更新使用spring的编程式事务，可以很方便的将多个更新操作做成一个事务
	 * 进行管理。
	 * 
	 * @param mod
	 * @return
	 */
	public ServiceMsg updateModule(Module mod, String parentToken)
	{
		ServiceMsg sms = new ServiceMsg();
		if (!this.checkPassed(sms, mod)) return sms;
		
		// 检查指定的父菜单的token是否存在，如果有指定父菜单的话
		String pToken = StringUtils.trimToEmpty(parentToken).intern();
		if (!checkParentMenu(sms, pToken, mod)) return sms;
		
		// 更新前获取当前要更新的模块的唯一token值
		Module curMod = this.dao.getModule(mod.getMId());
		if (curMod == null) {
			sms.setMessage("指定要更新的模块不存在，无法进行更新！");
			return sms;
		}
		final String orginToken = curMod.getMToken();
		
		// 如果要更新的模块是功能模块或是菜单模块但其token没有改变，则只需要更新其信息即可
		// 否则还需要更新其子模块（菜单和功能模块）的token前缀
		mod.setMToken(pToken + TokenMaker.getSelfToken(curMod));
		if (!curMod.isMenu() || pToken.equals(TokenMaker.getParentToken(curMod))) {
			if (log.isInfoEnabled()) log.info("直接更新模块的信息~~~");
			//mod.setMToken(curMod.getMToken());
			this.dao.updateModule(mod);
		} else {
			if (log.isInfoEnabled()) log.info("更新模块信息和子模块的token前缀~~");
			// 更新模块信息和其子模块的信息时需要保持一致，使用spring的事务管理特性
			final IModuleDAO fdao = this.dao;
			final Module updateMod = mod;
			//updateMod.setMToken(pToken + TokenMaker.getSelfToken(curMod));
			tt.execute(new TransactionCallback() {
				public Object doInTransaction(TransactionStatus arg0)
				{
					fdao.updateModule(updateMod);
					fdao.updateChildToken(orginToken, updateMod.getMToken(), 
							updateMod.getMId(), updateMod.getSysId());
					return null;
				}
			});
		}
		
		// 如果是菜单模块则要更新其下属的所有模块（菜单和功能）的token值
		sms.setSuccess(true);
		sms.setMessage("模块信息更新成功！");
		return sms;
	}
	
	/**
	 * 设置事务处理的调用模板.
	 * @param tt
	 */
	public void setTransactionTpl(TransactionTemplate tt)
	{
		this.tt = tt;
	}
	
	/**
	 * 删除指定的模块.
	 * @param mToken
	 * @return
	 */
	public ServiceMsg removeModule(String mToken)
	{
		ServiceMsg sms = new ServiceMsg();
		mToken = StringUtils.trimToEmpty(mToken);
		if (mToken.intern() == "") {
			sms.setMessage("要删除的模块必须指定其唯一键值！");
			return sms;
		}
		int result = this.dao.remove(mToken);
		if (result == -1) sms.setMessage("指定的模块不存在，无法进行删除！");
		else if (result == 0) sms.setMessage("当前菜单模块含有子模块，不允许删除！");
		else {
			sms.setSuccess(true);
			sms.addObject("Refresh", true);
			sms.setMessage("模块删除成功！");
		}
		return sms;
	}
	
	/**
	 * 获取指定模块的所有功能信息和模块的信息
	 * @param moduleId
	 * @return
	 */
	public ServiceMsg getModuleFunc(String moduleId)
	{
		ServiceMsg sms = new ServiceMsg();
		int mId = StringUtil.parseInt(moduleId, -1);
		if (mId == -1) {
			sms.setMessage("指定的模块ID非法，无法获取其功能信息！");
			return sms;
		}
		ModuleFunc func = this.dao.getModuleFunc(mId);
		if (func == null) {
			sms.setMessage("指定的模块不存在或存在但不是功能模块！");
			return sms;
		}
		if (log.isInfoEnabled()) {
			log.info("模块的功能总数为：" + func.getFuncList().size());
		}
		sms.addObject("MF", func);
		sms.setSuccess(true);
		sms.setMessage("模块信息和功能信息获取成功！");
		return sms;
	}
	
	/**
	 * 删除指定模块的指定功能.
	 * @param moduleId
	 * @param funcId
	 * @return
	 */
	public ServiceMsg removeModuelFunc(String moduleId, String funcId)
	{
		ServiceMsg sms = new ServiceMsg();
		int mId = StringUtil.parseInt(moduleId, -1);
		int fId = StringUtil.parseInt(funcId, -1);
		if (mId == -1 || fId == -1) {
			sms.setMessage("删除失败，必须指定要删除的模块ID和功能ID！");
			return sms;
		}
		this.dao.removeModuleFunc(mId, fId);
		sms.setSuccess(true);
		sms.setMessage("模块功能删除成功！");
		return sms;
	}
	
	/**
	 * 添加或更新模块的指定功能.
	 * @param func
	 * @return
	 */
	public ServiceMsg updateModuleFunc(ModuleFunc func, String single)
	{
		ServiceMsg sms = new ServiceMsg();
		
		int buildResult = func.buildup(); // 重组为每个功能对应一个对象
		if (log.isInfoEnabled()) log.info("重组后的结果返回：" + buildResult);
		if (buildResult == -1) {
			sms.setMessage("指定的功能名称、操作标识不能为空！");
			return sms;
		} else if (buildResult == -2) {
			sms.setMessage("功能名称、操作标识不对应！");
			return sms;
		}
		
		if (func.getMId() <= 0 || this.dao.getModule(func.getMId()) == null) {
			sms.setMessage("指定的模块" + func.getMName() + "不存在，无法完成更新！");
			return sms;
		}
		List<ModuleSingleFunc> list = func.getFuncList();
		if (log.isInfoEnabled()) log.info("要添加或修改的功能项数为：" + list.size());
		if (list.size() < 1) {
			sms.setMessage("必须至少指定一个功能进行更新！");
			return sms;
		}
		
		if ("true".equals(StringUtil.trim(single)) && list.size() == 1) {
			int funcId = this.updateSingleFunc(func.getMId(), list.get(0));
			sms.addObject("FuncId", funcId);
		} else {
			this.batchUpdateFunc(func.getMId(), list);
		}
		
		sms.setSuccess(true);
		sms.setMessage("模块功能更新成功！");
		return sms;
	}
	
	/**
	 * 批量添加和更新指定模块的功能
	 * @param list
	 */
	private void batchUpdateFunc(int moduleId, List<ModuleSingleFunc> list)
	{
		if (log.isInfoEnabled()) log.info("批量添加和更新模块功能~~~");
		// 将功能分成需要添加和需要更新的两个部分
		List[] result = this.splitFunc(list);
		if (result[0].size() >= 1) {
			if (log.isInfoEnabled()) log.info("批量添加的功能数：" + result[0].size());
			this.dao.batchAddModuleFunc(moduleId, result[0]);
		}
		if (result[1].size() >= 1) {
			if (log.isInfoEnabled()) log.info("批量更新的功能数：" + result[0].size());
			this.dao.batchUpdateModuleFunc(moduleId, result[1]);
		}
	}
	
	/**
	 * 添加或更新单个功能项
	 * @param moduleId
	 * @param func
	 */
	private int updateSingleFunc(int moduleId, ModuleSingleFunc func)
	{
		if (log.isInfoEnabled()) log.info("添加或更新模块的单个功能信息~~");
		return this.dao.singleUpdateModuleFunc(moduleId, func);
	}
	
	/**
	 * 将指定的功能分解成需要添加和需要更新的部分
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<ModuleSingleFunc>[] splitFunc(List<ModuleSingleFunc> list)
	{
		List[] result = new ArrayList[2];
		result[0] = new ArrayList<ModuleSingleFunc>(); // 需要添加的
		result[1] = new ArrayList<ModuleSingleFunc>(); // 需要更新的
		for (ModuleSingleFunc func : list) {
			if (func == null) continue;
			if (func.getFuncId() <= 0) result[0].add(func);
			else result[1].add(func);
		}
		return result;
	}
	
	/**
	 * 添加模块.
	 * @param mod
	 * @return
	 */
	public ServiceMsg addModule(Module mod, String parentToken)
	{
		ServiceMsg sms = new ServiceMsg();
		if (!this.checkPassed(sms, mod)) return sms;
		// 检查指定的父菜单的token是否存在，如果有指定父菜单的话
		String pToken = StringUtils.trimToEmpty(parentToken).intern();
		if (!checkParentMenu(sms, pToken, mod)) return sms;
		mod.setMToken(this.getModuleToken(pToken));
		this.dao.addModule(mod);
		sms.setMessage("模块添加成功！");
		sms.setSuccess(true);
		return sms;
	}
	
	/**
	 * 检查指定模块的父菜单模块是否存在.若存在则返回其所属子系统ID，否则返回-1
	 * @param sms
	 * @param pToken
	 * @return
	 */
	private boolean checkParentMenu(ServiceMsg sms, String pToken, Module mod)
	{
		if (pToken.intern() != "") {
			Module parent = this.dao.getModule(pToken);
			if (parent == null || !parent.isMenu()) {
				sms.setMessage("指定的父菜单模块不存在或其不是菜单模块！");
				return false;
			}
			mod.setSysId(parent.getSysId());
		}
		return true;
	}
	
	/**
	 * 服务器端的数据校验.
	 * @return
	 */
	private boolean checkPassed(ServiceMsg sms, Module mod)
	{
		if (mod.getMName().intern() == "") {
			sms.setMessage("模块的名称不能为空！");
			return false;
		} else if (mod.getMSequence() <= 0 || mod.getMSequence() > 999) {
			sms.setMessage("模块的排序序号只能在1到999之间！");
			return false;
		} else if (mod.getMStatus() < -1 || mod.getMStatus() > 2) {
			sms.setMessage("模块的状态标识错误！");
			return false;
		} else if (!mod.isMenu() && mod.getMUrl().intern() == "") {
			sms.setMessage("功能模块必须指定其链接地址！");
			return false;
		}
		mod.setSysModule(false); // 前台添加的不允许是系统固定模块
		// 菜单模块不允许设置模块的URL地址
		if (mod.isMenu()) mod.setMUrl(null);
		return true;
	}
	
	/**
	 * 生成当前模块的token.
	 * @return
	 */
	private String getModuleToken(String pToken)
	{
		String token = null; // 最终生成的token值
		int startNums = 3; // 第一次生成6个，如果还有重复，则每次追加3个
		while (token == null) {
			String[] tokens = TokenMaker.getTokens(pToken, startNums + 3);
			String tmp = StringUtil.join(StringUtil.arround(tokens, "'"), ",");
			String exists = this.dao.checkTokenInThese(tmp);
			String[] result = StringUtil.exclude(tokens, exists.split(","));
			if (result.length < 1) continue;
			token = result[0];
		}
		return token;
	}

	public IModuleDAO getDao()
	{
		return dao;
	}

	public void setDao(IModuleDAO dao)
	{
		this.dao = dao;
	}
}
