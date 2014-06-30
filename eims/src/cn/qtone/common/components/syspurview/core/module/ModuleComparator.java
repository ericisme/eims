package cn.qtone.common.components.syspurview.core.module;

import java.util.Comparator;

import cn.qtone.common.components.syspurview.core.module.domain.Module;

/**
 * 模块的排序比较器.
 * 
 * @author 马必强
 *
 */
public class ModuleComparator implements Comparator
{
	public final static int CMP_FOR_TREE = 1; // 根据树形菜单生成排序
	
	public final static int CMP_BY_SEQ = 2; // 根据模块的序号排列，不管层次结构
	
	private int type = CMP_FOR_TREE;
	
	public ModuleComparator(int type)
	{
		this.type = type;
	}
	
	public int compare(Object arg0, Object arg1)
	{
		if (type == CMP_BY_SEQ) {
			return this.compareForNormal((Module)arg0, (Module)arg1);
		} else {
			return this.compareForTree((Module)arg0, (Module)arg1);
		}
	}
	
	/**
	 * type=CMP_FOR_TREE时的排序.
	 * 树形菜单的排序是先根据token的长度（长度越小则越是上层），然后再根据排序号，最
	 * 后则是根据其ID进行排序。
	 */
	private int compareForTree(Module mod1, Module mod2)
	{
		if (mod1.getMToken().length() < mod2.getMToken().length()) return -1;
		if (mod1.getMToken().length() > mod2.getMToken().length()) return 1;
		return compareForNormal(mod1, mod2);
	}
	
	/**
	 * type=CMP_BY_SEQ时的排序.
	 * 普通的排序则表示是属于同一级的排序，这时只需要根据其排序号和ID即可。
	 */
	private int compareForNormal(Module mod1, Module mod2)
	{
		if (mod1.getMSequence() < mod2.getMSequence()) return -1;
		if (mod1.getMSequence() > mod2.getMSequence()) return 1;
		if (mod1.getMId() < mod2.getMId()) return -1;
		else return 1;
	}
}
