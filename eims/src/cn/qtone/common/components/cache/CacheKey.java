package cn.qtone.common.components.cache;

/**
 * 系统中所有用到memcahed缓存服务器的key生成类.<P>
 * 
 * @author 马必强
 *
 */
public class CacheKey
{
	private final static CacheKey instance = new CacheKey();
	
	/**
	 * 各种缓存对象的key值枚举变量，不包含自动生成的key.
	 */
	private static enum key {
		logsys_setting, logsys_infoList
	};
	
	private CacheKey() {}
	
	/**
	 * 获取关键字生成类的唯一实例.
	 * @return
	 */
	public static CacheKey getInstance()
	{
		return instance;
	}
	
	/**
	 * 获取日志系统的日志设置缓存key.
	 * @return
	 */
	public String getLogSettingKey()
	{
		return key.logsys_setting.toString();
	}
	
	/**
	 * 获取日志系统的日志列表缓存key.
	 * @return
	 */
	public String getLogInfoListKey()
	{
		return key.logsys_infoList.toString();
	}
}
