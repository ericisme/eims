package cn.qtone.common.components.syspurview.check.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 权限检查的annotation.<br>
 * 
 * 使用annotation将使权限检查不需要杂合在业务处理类中，
 * 业务处理类只需要专注处理自己的业务逻辑即可！该权限模块
 * 将使用AOP方法来作权限校验和拦截，采用的是BeforeAdvice
 * 检测模式！
 * 
 * 使用方法：
 * 假设某一业务逻辑处理类有add方法，如果需要对该方法进行权限
 * 控制，并且设置了其操作名称为"add"，那么使用如下：
 * 
 * @Check(purview="add")
 * public void add()
 * {
 * 		// 处理方法
 * }
 * 
 * @author 马必强
 * @version 1.0
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Check
{
	public String purview();
}
