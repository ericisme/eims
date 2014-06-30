package cn.qtone.common.components.syspurview.core.setting.domain;

/**
 * 权限系统的操作控制设置.
 * @author 马必强
 *
 */
public class OperateSetting
{
	private final static OperateSetting instance = new OperateSetting();
	
	private OperateSetting() {}
	
	/**
	 * 获取权限系统操作控制设置的实例.
	 * @return
	 */
	public static OperateSetting getInstance()
	{
		return instance;
	}
}
