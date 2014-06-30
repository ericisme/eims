package cn.qtone.common.utils.auto.spring.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qtone.common.utils.auto.spring.domain.Configer;
import cn.qtone.common.utils.auto.spring.domain.Module;
import cn.qtone.common.utils.base.StringUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Bean定义文件的生成.
 * 在Bean定义文件的模板中可以使用的变量有：
 *     moduleDescribe    模块的描述
 *     controllerId      模块的控制器的ID
 *     controllerClass   模块的控制器的类全路径（包括类名称）
 *     serviceProperty   模块的控制器中使用set/get表示业务处理类的属性名称，如果没有则抛出错误
 *     serviceId         模块的业务处理类的ID
 *     serviceClass      模块的业务处理类的类全路径（包括类名称）
 *     DAOProperty       模块的业务处理类中使用set/get表示DAO类的属性名称，如果没有则抛出错误
 *     DAOId             模块的DAO的ID
 *     DAOClass          模块的DAO的类的类路径（包括类名称）
 *     ftlFolder         总的FTL存储目录（相对根目录而言），这里设置的是根据命名空间的名称来设置的
 *     moduleFtlFoler    每一个模块的FTL存储目录，在ftlFolder下一级，使用模块的包名称来命名
 * @author 马必强
 *
 */
public class BeanFileMaker
{
	private Configer configer;
	
	public BeanFileMaker(Configer configer)
	{
		this.configer = configer;
		this.checkInfo();
	}
	
	/**
	 * 生成bean的定义文件.
	 * @param modules
	 */
	public void createBeanFile(Module[] modules)
	{
		List<Map<String,String>> props = new ArrayList<Map<String,String>>();
		for (Module mod : modules) {
			Map<String,String> map = new HashMap<String,String>();
			Class[] clazz= this.getModuleClass(mod);
			// 获取控制器中对业务类的引用属性名称和业务类中对dao类的引用属性名称
			String serviceProperty = mod.getServiceProperty().intern();
			if (serviceProperty == "") serviceProperty = this.getPropertyName(clazz[0], clazz[1]);
			String daoProperty = mod.getDaoProperty().intern();
			if (daoProperty == "") daoProperty = this.getPropertyName(clazz[1], clazz[2]);
			// 控制器属性设置
			map.put("moduleDescribe", mod.getModuleDescribe());
			map.put("controllerId", configer.getNamingSpace() + clazz[0].getSimpleName());
			map.put("controllerClass", clazz[0].getName());
			map.put("serviceProperty", serviceProperty);
			// 业务处理类设置
			map.put("serviceId", configer.getNamingSpace() + clazz[1].getSimpleName());
			map.put("serviceClass", clazz[1].getName());
			map.put("DAOProperty", daoProperty);
			// dao设置
			map.put("DAOId", configer.getNamingSpace() + clazz[3].getSimpleName());
			map.put("DAOClass", clazz[3].getName());
			// 模块的名称和命名空间的名称设置，用来设置FTL模板的文件路径信息
			map.put("ftlFolder", configer.getNamingSpace());
			map.put("moduleFtlFoler", mod.getModulePackage());
			
			props.add(map);
		}
		this.generateBeanFile(props);
	}
	
	/**
	 * 解析模板、输出内容.
	 * @param properties
	 */
	protected void generateBeanFile(List<Map<String,String>> props)
	{
		Writer out = null;
		try {
			// 初始化freemarker
			Configuration conf = new Configuration();
			conf.setDirectoryForTemplateLoading(new File(this.configer.getProjectPath() 
					+ "/src/springBuilder"));
			conf.setDefaultEncoding("GBK");
			
			// 生成Bean定义文件
			File f = new File(configer.getProjectPath() + "/src/springBuilder/" + configer.getBeanFile());
			f.createNewFile();
			
			// 输出
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
			Template tpl = conf.getTemplate("BeanFileTpl.ftl");
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("props", props);
			tpl.process(map, out);
		} catch (Exception ex) {
			throw new RuntimeException("[系统错误]" + ex.getMessage());
		} finally {
			try {
				if (out != null) out.close();
			} catch (Exception ex) {}
		}
	}
	
	/**
	 * 从指定的container类中查找看是否有refClass的set方法，有则返回其属性名称，
	 * 没有则提示错误.
	 * @param container
	 * @param refClass
	 * @return
	 */
	protected String getPropertyName(Class container, Class refClass)
	{
		String property = null;
		Method[] methods = container.getMethods();
		for (Method method : methods) {
			String name = method.getName();
			if (!name.startsWith("set")) continue;
			Class[] parameters = method.getParameterTypes();
			if (parameters.length != 1) continue;
			if (parameters[0] != refClass) continue;
			property = StringUtil.unCapitalize(name.substring(3));
			break;
		}
		if (property == null) {
			throw new RuntimeException("[系统错误]类[" + container.getName() 
					+ "]中不存在对类[" + refClass.getName() + "]的set方法！");
		}
		return property;
	}
	
	/**
	 * 获取指定模块的各个类和接口的class，返回的数组包含的顺序是：
	 * 控制器的Class、业务处理类的Class、DAO接口Class、DAO的Class
	 * @param mod
	 * @return
	 */
	protected Class[] getModuleClass(Module mod)
	{
		String base = configer.getBasePackage() + "." + mod.getModulePackage();
		Class[] clazz = new Class[4];
		try {
			// 控制器的Class
			String tmpClazz = base + ".controller." + ClassMaker.upperCase(mod.getModuleName()) + "Controller";
			clazz[0] = Class.forName(tmpClazz);
			// 业务处理类的Class
			tmpClazz = base + ".service." + ClassMaker.upperCase(mod.getModuleName()) + "Service";
			clazz[1] = Class.forName(tmpClazz);
			// DAO接口的Class
			tmpClazz = base + ".dao.I" + ClassMaker.upperCase(mod.getModuleName()) + "DAO";
			clazz[2] = Class.forName(tmpClazz);
			// DAO接口实现类的Class
			tmpClazz = base + ".dao." + ClassMaker.upperCase(mod.getModuleName()) + "DAO";
			clazz[3] = Class.forName(tmpClazz);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("[系统错误]找不到指定的类！" + ex.getMessage());
		}
		return clazz;
	}
	
	/**
	 * Bean定义文件的单独检测，主要是检测模板文件.
	 *
	 */
	protected void checkInfo()
	{
		File file = new File(configer.getProjectPath() + "/src/springBuilder/BeanFileTpl.ftl");
		if (!file.exists() || !file.isFile()) {
			throw new RuntimeException("[系统错误]Bean定义文件不存在！");
		}
		if (configer.getNamingSpace().intern() == null) {
			throw new RuntimeException("[系统错误]请指定Bean定义文件的命名空间！");
		}
	}
}
