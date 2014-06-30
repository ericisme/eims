package cn.qtone.common.components.cache;

import java.util.Date;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

import cn.qtone.common.utils.base.DateUtil;
import cn.qtone.common.utils.base.StringUtil;

/**
 * 使用memcached的缓存实用类.<P>
 * 注意，所有需要缓存的对象必须实现java.io.Serializable接口！
 * 
 * @author 马必强
 *
 */
public class MemCached
{
	// 创建全局的唯一实例
	private static MemCachedClient mcc = new MemCachedClient();
	
	private final static MemCached memCached = new MemCached();
	
	// 获取socke连接池的实例对象
	private static SockIOPool pool;
	
	// 初始化与缓存服务器的连接池
	protected static void init(Map<String,String> config)
	{
		// 设置服务器信息和其权重,如果没有指定服务器列表，则表示不使用缓存
		String tmp = StringUtil.trim(config.get("memcached.servers"));
		if (tmp.equals("")) {
			mcc = null;
			return;
		}
		
		pool = SockIOPool.getInstance();
		pool.setServers(StringUtil.trim(tmp.split(",")));
		tmp = StringUtil.trim(config.get("memcached.weights"));
		String[] array = StringUtil.trim(tmp.split(","));
		Integer[] weights = new Integer[array.length];
		for (int i = 0; i < weights.length; i ++) weights[i] = StringUtil.parseInt(array[i], 3);
		pool.setWeights(weights);

		// 设置初始连接数、最小和最大连接数以及最大处理时间
		tmp = StringUtil.trim(config.get("memcached.initConnections")).intern();
		if (tmp != "") pool.setInitConn(StringUtil.parseInt(tmp, 10));
		tmp = StringUtil.trim(config.get("memcached.minConnections")).intern();
		if (tmp != "") pool.setMinConn(StringUtil.parseInt(tmp, 5));
		tmp = StringUtil.trim(config.get("memcached.maxConnections")).intern();
		if (tmp != "") pool.setMaxConn(StringUtil.parseInt(tmp, 100));
		tmp = StringUtil.trim(config.get("memcached.maxIdleTime")).intern();
		if (tmp != "") pool.setMaxIdle(StringUtil.parseInt(tmp, 1000*60*30));
		tmp = StringUtil.trim(config.get("memcached.maxBusyTime")).intern();
		if (tmp != "") pool.setMaxBusyTime(StringUtil.parseLong(tmp, 1000*60*5));

		// 设置主线程的睡眠时间
		tmp = StringUtil.trim(config.get("memcached.maintThreadSleep")).intern();
		if (tmp != "") pool.setMaintSleep(StringUtil.parseLong(tmp, 1000*5));

		// 设置TCP的参数，连接超时等
		pool.setNagle( false );
		tmp = StringUtil.trim(config.get("memcached.socketTimeout")).intern();
		if (tmp != null) pool.setSocketTO(StringUtil.parseInt(tmp, 1000*3));
		//pool.setSocketConnectTO( 0 );

		// 初始化连接池
		pool.initialize();

		// 压缩设置（单位为K）,默认是15K
		mcc.setCompressEnable( true );
		//mcc.setCompressThreshold( 64 * 1024 );
	}
	
	/**
	 * 保护型构造方法，不允许实例化！
	 *
	 */
	protected MemCached()
	{
		
	}
	
	/**
	 * 关闭与缓存服务器的连接池.
	 *
	 */
	public void shutDown()
	{
		if (pool == null) return;
		pool.shutDown();
	}
	
	/**
	 * 获取唯一实例.
	 * @return
	 */
	public static MemCached getInstance()
	{
		return memCached;
	}
	
	/**
	 * 添加一个指定的值到缓存中.<P>
	 * 注意，使用重复的key来多次添加时只有第一个是有效的，即后续的都不会被存储在
	 * 缓存中！
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean add(String key, Object value)
	{
		if (mcc == null || value == null) return false;
		return mcc.add(key, value);
	}
	
	public boolean add(String key, Object value, Date expiry)
	{
		if (mcc == null || value == null) return false;
		return mcc.add(key, value, expiry);
	}
	
	/**
	 * 设置一个指定的值到缓存中.<P>
	 * 注意，如果对同一个key进行多次set，那么只有最后一个是有效的。这个与add刚好相反！
	 * 在实际应用的时候建议采用set方法，不过这尤其要注意key值的唯一性，否则就有可能
	 * 使数据出现混乱！
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(String key, Object value)
	{
		if (mcc == null || value == null) return false;
		return mcc.set(key, value);
	}
	
	public boolean set(String key, Object value, Date expiry)
	{
		if (mcc == null || value == null) return false;
		return mcc.set(key, value, expiry);
	}
	
	/**
	 * 使用指定的值替换原有的内容.<P>
	 * 注意，只有缓存中已有指定的key的对象后此replace才是有效的，否则将不替换任何对象！
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean replace(String key, Object value)
	{
		if (mcc == null || value == null) return false;
		return mcc.replace(key, value);
	}
	
	public boolean replace(String key, Object value, Date expiry)
	{
		if (mcc == null || value == null) return false;
		return mcc.replace(key, value, expiry);
	}
	
	/**
	 * 使指定的key的统计数加一.<P>
	 * 注意，该方法是线程安全的，可以处理并发。如果指定的key不存在则会自动添加，如果
	 * 存在则会自动加一。添加成功后返回当前的统计数
	 * @param key
	 * @return
	 */
	public long addOrIncrement(String key)
	{
		if (mcc == null) return 0L;
		return mcc.addOrIncr(key, 1);
	}
	
	/**
	 * 获取指定关键字的统计数.
	 * @param key
	 * @return
	 */
	public long getCounter(String key)
	{
		if (mcc == null) return 0L;
		return mcc.getCounter(key);
	}
	
	/**
	 * 从缓存服务器中删除某一个指定的key的对象.
	 * @param key
	 * @return
	 */
	public boolean remove(String key)
	{
		if (mcc == null) return false;
		return mcc.delete(key);
	}
	
	/**
	 * 根据关键字获取指定的对象.
	 * @param key
	 * @return
	 */
	public Object get(String key)
	{
		if (mcc == null) return null;
		return mcc.get(key);
	}
	
	/**
	 * 根据指定的关键字获取对象，如果缓存中没有获取到则执行指定的接口方法来获取
	 * 数据，同时该接口应该返回其获取的数据，由系统负责将其置入缓存中.
	 * @param key
	 * @param getValue
	 * @return
	 */
	public Object get(String key, NotFoundInvoke getValue)
	{
		if (mcc == null) return null;
		Object value = mcc.get(key);
		if (value != null) return value;
		if (getValue == null) return value;
		value = getValue.getValue();
		mcc.add(key, value);
		return value;
	}
	
	/**
	 * 获取多个指定关键字的缓存对象.返回一个map，以指定的keys为key进行检索.
	 * @param keys
	 * @return
	 */
	public Map<String,Object> getMulti(String[] keys)
	{
		if (mcc == null) return null;
		if (keys == null || keys.length < 1) return null;
		return mcc.getMulti(keys);
	}
	
	/**
	 * 获取多个指定的关键字的缓存对象.返回一数组.
	 * @param keys
	 * @return
	 */
	public Object[] getMutilArray(String[] keys)
	{
		if (mcc == null) return null;
		if (keys == null || keys.length < 1) return null;
		return mcc.getMultiArray(keys);
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"/spring/edu/cluster.xml");
		StringUtil.debug(StringUtil.reflectObj(context.getBean("cluster_config")));
		MemCached cache = MemCached.getInstance();
		String key = "myhljkythh";
		cache.remove(key);
		Date dt = DateUtil.getNowDate(0,0,0,0,5,0);
		StringUtil.debug("时间为：" + dt);
		cache.add(key, "我的值哦ff",dt);
		StringUtil.debug("add后的值为：" + cache.get(key));
		Thread.sleep(5*60*1000);
		StringUtil.debug("5m后的值为：" + cache.get(key));
		cache.remove(key);
	}
}

class TBean implements java.io.Serializable
{
	private static final long serialVersionUID = 1945562032261336919L;
	
	private String name;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
