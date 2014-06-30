/**
 * 
 */
package cn.qtone.qtoneframework.web.view.poi;

import java.lang.annotation.*;

/**
 * 用在ExcelExtractor的ValueObject对像上，告诉ExcelExtractor该对像各方法的验证规则
 * 
 * @author 林子龙
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ValidateClass {
	/**
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	Class value();
}
