package cn.qtone.common.components.syspurview.core.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.qtone.common.components.syspurview.core.module.domain.Module;
import cn.qtone.common.components.syspurview.core.module.domain.TokenMaker;

/**
 * 模块（菜单和功能）的树形菜单的生成对象。
 * 
 * @author 马必强
 * 
 */
public class ModuleTree
{
	private static final String Space = "　"; // 层次结构部分的缩进

	private static final String TreeChar = "┣"; // 树形结构符号

	/**
	 * 返回给定列表的模块的树形结构list.
	 * 
	 * @param modules
	 * @param changeName 是否使用树型结构符来修改模块的名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Module> getModuleTree(List modules, boolean changeName)
	{
		if (modules == null || modules.size() < 1) return modules;

		// 先生成一个返回对象和对原始的列表进行排序
		List<Module> result = new ArrayList<Module>();
		Collections.sort(modules, new ModuleComparator(ModuleComparator.CMP_FOR_TREE));
		
		// 开始进行构建
		startSearch(modules, result, changeName);
		
		return result;
	}

	/**
	 * 开始进行树形菜单的构建。
	 * 
	 * @param modules
	 * @param result
	 * @param changeName 是否使用树型结构符来修改模块的名称
	 */
	protected static void startSearch(List modules, List<Module> result, 
			boolean changeName)
	{
		String startToken = null;
		for (Object obj : modules) {
			Module mod = (Module) obj;
			if (startToken == null) startToken = mod.getMToken();
			//if (mod.getMToken().length() != TokenMaker.TOKEN_LEN) continue;
			if (mod.getMToken().length() != startToken.length()) continue;
			result.add(mod);
			searchChild(modules, result, mod, changeName);
		}
	}

	/**
	 * 开始检索并生成树形的列表.
	 * 
	 * @param modules 原始的列表
	 * @param result 生成后的排序列表
	 * @param mod 要搜索其子菜单和模块的指定模块对象
	 */
	protected static void searchChild(List modules, List<Module> result,
			Module mod, boolean changeName)
	{
		for (Object obj : modules) {
			Module tmpMod = (Module) obj;
			if (!tmpMod.getMToken().startsWith(mod.getMToken())) continue;
			if (tmpMod.getMToken().length() != mod.getMToken().length()
					+ TokenMaker.TOKEN_LEN) continue;
			if (changeName) makeTreeModule(tmpMod);
			result.add(tmpMod);
			if (tmpMod.isMenu()) searchChild(modules, result, tmpMod, changeName);
		}
	}
	
	/**
	 * 将指定的模块使用树形菜单的形式表示出来，主要就是名称即moduleName中
	 * 加Space和TreeChar符号。
	 * @param mod
	 */
	protected static void makeTreeModule(Module mod)
	{
		int depth = mod.getMToken().length() / TokenMaker.TOKEN_LEN;
		mod.setMName(StringUtils.repeat(Space, depth -1) + TreeChar
				+ mod.getMName());
	}
}
