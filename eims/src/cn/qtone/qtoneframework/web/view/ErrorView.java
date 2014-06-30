package cn.qtone.qtoneframework.web.view;

import cn.qtone.qtoneframework.web.view.velocity.VelocityHelper;


/**
 * 错误页面
 * 
 * @author 张但
 *
 */
public class ErrorView {
	
	/**
	 * 返回错误页面
	 */
	public String parseErrorView() {
		VelocityHelper vHelperMain = new VelocityHelper("/error/error.htm");
		return vHelperMain.getParsedResult();
	}
}
