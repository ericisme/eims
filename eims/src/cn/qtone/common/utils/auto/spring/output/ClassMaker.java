package cn.qtone.common.utils.auto.spring.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import cn.qtone.common.utils.auto.spring.domain.Configer;
import cn.qtone.common.utils.auto.spring.domain.Module;
import cn.qtone.common.utils.base.StringUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 各模块的类的生成.
 * 
 * 模块的类分成controller、service、daoInterface和dao四个类，对应的关系如下：
 * 包 : controller    类名称   : 模块名称Controller
 * 包 : service       类名称   : 模块名称Service
 * 包 : dao           接口名称 : I模块名称DAO
 *                    类名称   : 模块名称DAO
 * 
 * 类的生成采用freemarker用模板来生成，对应的模板应该在项目路径下的autoSpring目录下，分别是：
 * 1.控制器的模板（ControllerTpl.ftl）
 *     该模板中可以使用的模板变量有：
 *     author         : 模块的作者
 *     version        : 模块的版本号
 *     moduleDescribe : 该模块的描述
 *     package        : 当前控制器类所在的包全路径
 *     serviceImport  : 与该包相关的业务处理类的导入全路径
 *     service        : 与该包相关的业务类的类名称
 *     controller     : 控制器的类名称
 *     
 * 2.业务类模板（ServiceTpl.ftl)
 *     该模板中可以使用的模板变量有：
 *     author         : 模块的作者
 *     version        : 模块的版本号
 *     moduleDescribe : 该业务类所在的模块的描述
 *     package        : 当前业务类所在的包全路径
 *     daoImport      ：该业务类使用的DAO对应的接口的导入全路径
 *     DAOInterface   : 该业务类所使用的DAO对应的DAO接口的名称
 *     service        : 该业务类的类名称
 * 
 * 3.DAO接口模板（DAOInterfaceTpl.ftl)
 *     该模板中可以使用的模板变量有：
 *     author         : 模块的作者
 *     version        : 模块的版本号
 *     moduleDescribe : 该接口所在的模块的描述
 *     package        : 当前DAO接口所在的包全路径
 *     DAOInterface   : DAO接口的接口名称
 * 
 * 4.DAO的接口实现类模板（DAOTpl.ftl)
 *     该模板中可以使用的模板变量有：
 *     author         : 模块的作者
 *     version        : 模块的版本号
 *     moduleDescribe : 该DAO所在的模块的描述
 *     package        : DAO类所在的包全路径
 *     DAOInterface   : 该DAO对应的DAO接口的接口名称
 *     DAO            ：该DAO的类名称
 * 
 * @author 马必强
 *
 */
public class ClassMaker
{
	private Configer configer;
	
	private Configuration conf; // freemarker 配置对象
	
	public ClassMaker(Configer configer)
	{
		this.configer = configer;
		this.initFreeMarker();
	}
	
	/**
	 * 初始化freemarker的配置
	 *
	 */
	protected void initFreeMarker()
	{
		try {
			this.conf = new Configuration();
			this.conf.setDirectoryForTemplateLoading(new File(this.configer.getProjectPath() 
					+ "/src/springBuilder"));
			this.conf.setDefaultEncoding("GBK");
		} catch (Exception ex) {
			throw new RuntimeException("[系统错误]" + ex.getMessage());
		}
	}
	
	/**
	 * 生成各个类.
	 * @param modules
	 */
	public void createClass(Module[] modules)
	{
		for (Module mod : modules) {
			this.generateControllerAndService(mod, "controller");
			this.generateControllerAndService(mod, "service");
			this.generateDAO(mod);
		}
	}
	
	/**
	 * 生成控制器、业务类的类源文件.
	 * @param mod
	 * @param type 分别为controller, service和dao
	 */
	protected void generateControllerAndService(Module mod, String type)
	{
		try {
			String baseDir = this.configer.getProjectPath() + "/src/";
			String basePackagePath = this.configer.getBasePackage().replaceAll("[.]", 
					"/") + "/" + mod.getModulePackage() + "/" + type;
			// 先生成文件
			String name = upperCase(mod.getModuleName()) + upperCase(type);
			Writer out = this.generateClass(baseDir + basePackagePath + "/" + name + ".java");
			
			// 读取模板设置数据
			Template tpl = this.conf.getTemplate(upperCase(type) + "Tpl.ftl");
			Map<String,String> map = this.getCommonData();
			map.put("package", this.configer.getBasePackage() + "." + mod.getModulePackage() + "." + type);
			map.put("moduleDescribe", mod.getModuleDescribe());
			map.put(type, name);
			String tmp = StringUtil.trim(mod.getAuthor()).intern();
			if (tmp != "") map.put("author", tmp);
			tmp = StringUtil.trim(mod.getVersion()).intern();
			if (tmp != "") map.put("version", tmp);
			
			// 设置controller的特有数据
			if (type.intern() == "controller") {
				String serviceName = upperCase(mod.getModuleName()) + "Service";
				map.put("serviceImport", this.configer.getBasePackage() + "." + mod.getModulePackage() 
						+ ".service." + serviceName);
				map.put("service",  serviceName);
			} else if (type.intern() == "service") {
				String daoInterName = "I" + upperCase(mod.getModuleName()) + "DAO";
				// 设置service的特有数据
				map.put("daoImport", this.configer.getBasePackage() + "." + mod.getModulePackage() 
						+ ".dao." + daoInterName);
				map.put("DAOInterface", daoInterName);
			}
			
			// 输出
			tpl.process(map, out);
			if (out != null) out.close();
			
		} catch (Exception ex) {
			throw new RuntimeException("[系统错误]" + ex.getMessage());
		}
	}
	
	/**
	 * 生成模块的dao和其接口类源文件.
	 * @param mod
	 */
	protected void generateDAO(Module mod)
	{
		Writer out = null;
		try {
			String baseDir = this.configer.getProjectPath() + "/src/";
			String basePackagePath = this.configer.getBasePackage().replaceAll("[.]", 
					"/") + "/" + mod.getModulePackage() + "/dao";
			// 先生成接口文件
			String name = "I" + upperCase(mod.getModuleName()) + "DAO";
			out = this.generateClass(baseDir + basePackagePath + "/" + name + ".java");
			
			// 读取dao接口模板设置数据
			Template tpl = this.conf.getTemplate("DAOInterfaceTpl.ftl");
			Map<String,String> map = this.getCommonData();
			map.put("package", this.configer.getBasePackage() + "." + mod.getModulePackage() + ".dao");
			map.put("moduleDescribe", mod.getModuleDescribe());
			map.put("DAOInterface", name);
			String tmp = StringUtil.trim(mod.getAuthor()).intern();
			if (tmp != "") map.put("author", tmp);
			tmp = StringUtil.trim(mod.getVersion()).intern();
			if (tmp != "") map.put("version", tmp);
			
			// 输出
			tpl.process(map, out);
			if (out != null) out.close();
			
			
			// 生成dao类文件
			String name2 = upperCase(mod.getModuleName()) + "DAO";
			out = this.generateClass(baseDir + basePackagePath + "/" + name2 + ".java");
			
			// 读取dao模板设置数据
			tpl = this.conf.getTemplate("DAOTpl.ftl");
			map.put("package", this.configer.getBasePackage() + "." + mod.getModulePackage() + ".dao");
			map.put("DAO", name2);
			
			// 输出
			tpl.process(map, out);
			if (out != null) out.close();
		} catch (Exception ex) {
			throw new RuntimeException("[系统错误]" + ex.getMessage());
		} finally {
			try {
				if (out != null) out.close();
			} catch (Exception ex) {}
		}
	}
	
	protected Map<String,String> getCommonData()
	{
		Map<String,String> map = new HashMap<String,String>();
		map.put("author", this.configer.getAuthor());
		map.put("version", this.configer.getVersion());
		return map;
	}
	
	public static String upperCase(String str)
	{
		String tmp = StringUtil.trim(str).intern();
		if (tmp == "") return "";
		if (tmp.length() < 2) {
			return str.toUpperCase();
		} else {
			return str.substring(0, 1).toUpperCase() + str.substring(1);
		}
	}
	
	/**
	 * 生成指定java文件的流.
	 * @param path
	 */
	protected Writer generateClass(String path)
	{
		try {
			File file = new File(path);
			StringUtil.debug("创建文件：" + path);
			if (!file.createNewFile()) throw new RuntimeException("无法创建类文件" + path);
			return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		} catch (Exception ex) {
			throw new RuntimeException("[系统错误]" + ex.getMessage());
		}
	}
}
