package cn.qtone.common.utils.auto.spring.parser;

import cn.qtone.common.utils.auto.spring.domain.Configer;
import cn.qtone.common.utils.auto.spring.domain.Module;

/**
 * 配置信息解析接口.
 * 
 * @author 马必强
 *
 */
public interface ParserInter
{
	/**
	 * 获取解析后的配置对象.
	 * @return
	 */
	public Configer getConfiger();
	
	/**
	 * 获取要生成的模块信息.
	 * @return
	 */
	public Module[] getModules();
}
