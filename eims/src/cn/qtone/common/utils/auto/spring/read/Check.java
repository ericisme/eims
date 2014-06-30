package cn.qtone.common.utils.auto.spring.read;

import java.io.File;

import cn.qtone.common.utils.auto.spring.domain.Configer;
import cn.qtone.common.utils.auto.spring.domain.Module;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 信息完整性检测.
 * 
 * @author 马必强
 *
 */
public class Check
{
	/**
	 * 检查所有信息的完整性.
	 * @param configer
	 * @param modules
	 */
	public static void checkAll(Configer configer, Module[] modules)
	{
		checkConfiger(configer);
		checkPath(configer);
		checkModule(modules);
		checkModulePath(configer, modules);
		checkTplFile(configer);
	}
	
	/**
	 * 检测配置对象的完整性.
	 * @param configer
	 */
	protected static void checkConfiger(Configer configer)
	{
		StringUtil.debug("检查配置信息的完整性……");
		String msg = null;
		if (configer == null) msg = "配置对象configer不能为空！";
		else if (configer.getProjectPath().intern() == "") msg = "项目的根路径不能为空！";
		else if (configer.getBasePackage().intern() == "") msg = "基础包名称不能为空！";
		else if (configer.getBeanFile().intern() == "") msg = "请指定输出的Bean定义文件！";
		if (msg != null) throw new RuntimeException(msg);
		StringUtil.debug("检查配置信息的完整性检测成功！");
	}
	
	/**
	 * 检测模块的配置信息的完整性.
	 * @param modules
	 */
	protected static void checkModule(Module[] modules)
	{
		StringUtil.debug("检查各模块的信息完整性……");
		String msg = null;
		if (modules == null) msg = "模块配置对象modules不能为空！";
		if (modules.length < 1) return;
		for (Module mod : modules) {
			if (mod == null) {
				msg = "模块域对象不能为空！";break;
			} else if (StringUtil.trim(mod.getModuleName()).intern() == "") {
				msg = "模块的名称不能为空！";break;
			} else if (StringUtil.trim(mod.getModulePackage()).intern() == "") {
				msg = "模块的包名称不能为空！";break;
			}
		}
		if (msg != null) throw new RuntimeException(msg);
		StringUtil.debug("检查各模块的信息完整性成功！");
	}
	
	/**
	 * 检查配置文件配置的路径（如项目路径、src目录）等信息是否正确.
	 * @param configer
	 */
	protected static void checkPath(Configer configer)
	{
		StringUtil.debug("检查配置信息中的路径信息的正确性……");
		try {
			File file = new File(configer.getProjectPath());
			if (!file.exists() || !file.isDirectory()) {
				throw new RuntimeException("指定的项目路径不存在！");
			}
			// src目录检测
			file = new File(configer.getProjectPath() + "/src");
			if (!file.exists() || !file.isDirectory()) {
				throw new RuntimeException("指定项目的源代码src路径不存在！");
			}
			// bean定义文件检测,存在则报错
			file = new File(configer.getProjectPath() + "/" + configer.getBeanFile());
			if (file.exists() && file.isFile()) {
				throw new RuntimeException("指定的Bean定义文件已存在！");
			}
		} catch (Exception ex) {
			throw new RuntimeException("[系统错误]" + ex.getMessage());
		}
		StringUtil.debug("检查配置信息中的路径信息的正确性成功！");
	}
	
	/**
	 * 检查模块的包路径是否已存在或是否有重复的，有则提示错误.
	 * @param configer
	 * @param modules
	 */
	protected static void checkModulePath(Configer configer, Module[] modules)
	{
		StringUtil.debug("检查各模块的包名称是否重复……");
		// 首先检查内部是否有重复的包名称
		for (int i = 0; i < modules.length; i ++) {
			for (int j = i + 1; j < modules.length; j ++) {
				if (modules[i].getModulePackage().equalsIgnoreCase(modules[j].getModulePackage())) {
					throw new RuntimeException("[系统错误]模块[" + modules[i].getModuleName() + 
							"]和[" + modules[j].getModuleName() + "]的包路径重复！");
				}
			}
		}
		StringUtil.debug("检查各模块的包名称是否重复成功！");

		StringUtil.debug("检查各模块的包路径的正确性……");
		// 再检测实际的路径是否已存在
		String base = configer.getProjectPath() + "/src/" + configer.getBasePackage().replaceAll("[.]", "/");
		for (Module mod : modules) {
			File file = new File(base + "/" + mod.getModulePackage().replaceAll("[.]", "/"));
			if (file.exists() && file.isDirectory()) {
				throw new RuntimeException("[系统错误]包路径[" + file.getAbsolutePath() + "]已存在！");
			}
		}
		StringUtil.debug("检查各模块的包路径的正确性成功！");
	}
	
	/**
	 * 检查模板是否都存在.
	 * @param configer
	 */
	protected static void checkTplFile(Configer configer)
	{
		StringUtil.debug("检查模板信息的完整性……");
		String base = configer.getProjectPath() + "/src/springBuilder/";
		String[] tpl = {"ControllerTpl.ftl", "ServiceTpl.ftl", "DAOInterfaceTpl.ftl", "DAOTpl.ftl"};
		for (String name : tpl) {
			File file = new File(base + name);
			if (!file.exists() || !file.isFile()) {
				throw new RuntimeException("[系统错误]模板文件[" + file.getAbsolutePath() + "]不存在！");
			}
		}
		StringUtil.debug("检查模板信息的完整性成功！");
	}
}
