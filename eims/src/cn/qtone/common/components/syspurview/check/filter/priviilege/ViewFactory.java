package cn.qtone.common.components.syspurview.check.filter.priviilege;

import org.apache.log4j.Logger;

import org.apache.commons.lang.StringUtils;

/**
 * 没有权限操作的提示工厂类.
 * 
 * @author 马必强
 *
 */
public class ViewFactory
{
	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(ViewFactory.class);
	
	private static NoPurviewView view;

	private ViewFactory(){};
	
	/**
	 * 获取指定的提示用户没有权限操作的类的实例.
	 * @param className
	 * @return
	 */
	public static NoPurviewView getViewInstance(String className)
	{
		if (view != null) {
			if (log.isInfoEnabled()) log.info("视图类已初始化成功，直接返回！");
			return view;
		}
		if (StringUtils.trimToEmpty(className).intern() == "") {
			if (log.isInfoEnabled()) {
				log.info("没有指定view视图，使用系统默认的JS提示视图！");
			}
			return getJsView();
		}
		try {
			Object obj =Class.forName(className).newInstance();
			if (!(obj instanceof NoPurviewView)) {
				if (log.isInfoEnabled()) log.info("指定的视图类不是NoPurviewView的实现类！");
				return getJsView();
			}
			if (log.isInfoEnabled()) log.info("指定的视图类实例化成功！");
			view = (NoPurviewView)obj;
			return view;
		} catch (Exception ex) {
			if (log.isInfoEnabled()) {
				log.info("指定的视图类实例化失败，使用默认视图！");
			}
			return getJsView();
		}
	}
	
	/**
	 * 获取默认的JS提示视图.
	 * @return
	 */
	private static NoPurviewView getJsView()
	{
		view = new JsNoPurviewView();
		return view;
	}
}
