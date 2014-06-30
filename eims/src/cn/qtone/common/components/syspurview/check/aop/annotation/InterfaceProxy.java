package cn.qtone.common.components.syspurview.check.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实现了业务处理接口的标注.<br>
 * 
 * 该标注将用在业务处理类的类名称前面，用来表示该业务
 * 处理类是实现业务接口，那么权限系统将使用Proxy的的
 * 动态代理方式来为其生成代理类，否则将使用CGLIB库来为
 * 其生成代理类.
 * 
 * @author 马必强
 * @version 1.0
 *
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InterfaceProxy
{

}
