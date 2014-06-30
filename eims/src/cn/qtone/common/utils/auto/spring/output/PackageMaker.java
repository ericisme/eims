package cn.qtone.common.utils.auto.spring.output;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import cn.qtone.common.utils.auto.spring.domain.Configer;
import cn.qtone.common.utils.auto.spring.domain.Module;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 各模块的包生成器.
 * 
 * @author 马必强
 *
 */
public class PackageMaker
{
	private Configer configer;
	
	public PackageMaker(Configer configer)
	{
		this.configer = configer;
	}
	
	/**
	 * 生成各模块的包路径.
	 * @return
	 */
	public void createPackage(Module[] modules)
	{
		this.generate(configer.getBasePackage());
		for (Module mod : modules) {
			String base = configer.getBasePackage() + "." + mod.getModulePackage();
			this.generate(base + ".controller");
			this.generate(base + ".service");
			this.generate(base + ".dao");
		}
	}
	
	/**
	 * 创建指定路径的包路径.
	 * @param path
	 */
	protected void generate(String path)
	{
		try {
			String _path = StringUtils.trimToEmpty(path).replaceAll("[.]", "/");
			File f = new File(configer.getProjectPath() + "/src/" + _path);
			StringUtil.debug("创建包：" + _path);
			f.mkdirs();
			/*if (!f.exists()) f.mkdirs();
			else if (f.isDirectory()) throw new RuntimeException("包路径" + path + "已存在！");*/
		} catch (Exception ex) {
			throw new RuntimeException("[系统错误]" + ex.getMessage());
		}
	}
}
