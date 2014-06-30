package cn.qtone.common.utils.auto.spring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cn.qtone.common.utils.auto.spring.domain.Configer;
import cn.qtone.common.utils.auto.spring.domain.Module;
import cn.qtone.common.utils.auto.spring.output.BeanFileMaker;
import cn.qtone.common.utils.auto.spring.output.ClassMaker;
import cn.qtone.common.utils.auto.spring.output.PackageMaker;
import cn.qtone.common.utils.auto.spring.parser.JSONParser;
import cn.qtone.common.utils.auto.spring.parser.ParserInter;
import cn.qtone.common.utils.auto.spring.read.Check;
import cn.qtone.common.utils.auto.spring.read.FileReader;
import cn.qtone.common.utils.base.StringUtil;

/**
 * spring自动模块生成器的启动程序.
 * 
 * @author 马必强
 *
 */
public class MainStart
{
	public static void main(String[] args) throws IOException
	{
		StringUtil.debug("[autoSpring]开始创建~~~~\n");
		
		// 配置文件的路径
		String path = "/springBuilder/module.conf";
		
		// 读取文件并解析结果
		String content = FileReader.readFile(MainStart.class.getResourceAsStream(path));
		ParserInter parser = new JSONParser(content);
		Configer configer = parser.getConfiger();
		Module[] modules = parser.getModules();
		
		// 检查配置文件内容准确性
		Check.checkAll(configer, modules);
		
		// 创建包和类文件
		new PackageMaker(configer).createPackage(modules);
		new ClassMaker(configer).createClass(modules);
		
		// 如果指定了bean定义文件和命名空间则执行Bean文件的自动生成
		if (configer.getBeanFile().intern() != "") {
			System.out.print("\n********************************\n请刷新整个项目，"
					+ "确定成功后再继续！\n********************************\n");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String input = null;
			while (true) {
				System.out.print("已成功刷新项目？(y/n):");
				input = br.readLine();
				if ("y".equalsIgnoreCase(input)) break;
			}
			
			// 生成bean定义文件
			new BeanFileMaker(configer).createBeanFile(modules);
		}
		
		StringUtil.debug("[autoSpring]创建成功！");
	}
}
