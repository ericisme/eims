/**
 * 
 */
package cn.qtone.qtoneframework.web.view.poi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用在ExcelExtractor的值对像的方法上，告诉ExcelExtractor，当调用设置方法时，哪些验证方法应该被调用
 * 
 * @author 林子龙
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ValidateMethod {
	/**
	 * 验证方法所在的类，默为不填写时。验证方法所在的类为ValidateClass中指定的类。若 在此填写了验证类，则此验证方法所在的类为这里指定的类。
	 */
	@SuppressWarnings("unchecked")
	Class validateClass() default NoSpecialClass.class;

	/**
	 * 验证的方法名
	 */
	String validateMethodName();
}
