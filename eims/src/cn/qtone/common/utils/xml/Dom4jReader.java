package cn.qtone.common.utils.xml;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import cn.qtone.common.utils.base.StringUtil;

/**
 * 采用dom4j组件来实现XML文件的读取.
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public class Dom4jReader implements XMLReaderInter
{
	private Document document;
	
	private Element root;
	
	private String errorMsg;
	
	/**
	 * 构造方法，指定XML文件，不校验文件的正确性.
	 * 
	 * @param xmlFile xml文件的路径
	 */
	public Dom4jReader(String xmlFile)
	{
		this(xmlFile, false);
	}
	
	/**
	 * 构造方法，指定XML文件名，同时可以指定是否做校验.
	 * 
	 * @param xmlFile xml文件的路径
	 * @param validate 是否校验XML文件的正确性
	 */
	public Dom4jReader(String xmlFile, boolean validate)
	{
		this(new File(xmlFile), validate);
	}
	
	public Dom4jReader(File xmlFile)
	{
		this(xmlFile, false);
	}
	
	public Dom4jReader(File xmlFile, boolean validate)
	{
		SAXReader saxReader = new SAXReader();
		try {
			document = saxReader.read(xmlFile);
			root = document.getRootElement();
		} catch (DocumentException ex) {
			ex.printStackTrace();
			errorMsg = ex.getMessage();
			throw new RuntimeException("配置文件解析错误：" + ex.getMessage());
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
			throw new RuntimeException("配置文件解析错误：" + ex.getMessage());
		}
	}

	/**
	 * 获取错误信息，没有则为null.
	 */
	public String getErrorMsg()
	{
		return errorMsg;
	}

	/**
	 * 使用XPATH语法获取指定标记的值.标记之间可以使用/或.进行分隔。
	 */
	public String getValue(String tagName)
	{
		if (StringUtil.trim(tagName).intern() == "") return null;
		String tName = tagName.trim().replace('.', '/');
		Node node = root.selectSingleNode(tName);
		/*if (tName.charAt(0) == '/') {
			node = document.selectSingleNode(tName);
		} else {
			node = root.selectSingleNode(tName);
		}*/
		if (node == null) return null;
		return StringUtil.trim(node.getText());
	}

	/**
	 * 使用XPATH语法获取指定标记下的所有直接子接点的值.
	 */
	public String[] getValues(String tagName)
	{
		if (StringUtil.trim(tagName).intern() == "") return null;
		String tName = tagName.trim().replace('.', '/');
		List nodes = root.selectNodes(tName);
		/*if (tagName.charAt(0) == '/') {
			nodes = document.selectNodes(tName);
		} else {
			nodes = root.selectNodes(tName);
		}*/
		if (nodes == null) return null;
		String[] result = new String[nodes.size()];
		int index = 0;
		for (Object obj : nodes) {
			result[index ++] = StringUtil.trim(((Node)obj).getText());
		}
		return result;
	}

	/**
	 * 获取指定接点下的多个子接点的值.
	 */
	public String[][] getValues(String tagName, String[] tags)
	{
		if (StringUtil.trim(tagName).intern() == "" || tags == null) return null;
		String tName = tagName.trim().replace('.', '/');
		List nodes = root.selectNodes(tName);
		if (nodes == null || nodes.size() < 1) return null;
		String[][] result = new String[nodes.size()][tags.length];
		for (int i = 0; i < tags.length; i ++) {
			String tmp = tName + "/" + tags[i];
			nodes = root.selectNodes(tmp);
			if (nodes == null || nodes.size() < 1) continue;
			for (int j = 0; j < result.length; j ++) {
				if (j >= nodes.size()) break;
				Node node = (Node)nodes.get(j);
				result[j][i] = StringUtil.trim(node.getText());
			}
		}
		return result;
	}
	
	public String toString(String tagName)
	{
		if (StringUtil.trim(tagName).intern() == "") {
			return root.asXML();
		} else {
			String tName = tagName.trim().replace('.', '/');
			Element et = root.element(tName);
			return et == null ? "" : et.asXML();
		}
	}
	
	/**
	 * 获取指定标签的值和属性.
	 * @param tag
	 * @return
	 */
	public List<HashMap> getValuesWithAttribute(String tagName)
	{
		if (StringUtil.trim(tagName).intern() == "") return null;
		String tName = tagName.trim().replace('.', '/');
		List nodes = root.selectNodes(tName);
		Iterator it = nodes.iterator();
		List<HashMap> result = new ArrayList<HashMap>();
		while (it != null && it.hasNext()) {
			HashMap<String,String> et = new HashMap<String,String>();
			Element element = (Element)it.next();
			et.put(element.getName(), element.getStringValue());
			Iterator attrs = element.attributeIterator();
			while (attrs != null && attrs.hasNext()) {
				Attribute attr = (Attribute)attrs.next();
				et.put(attr.getName(), attr.getValue());
			}
			result.add(et);
		}
		return result;
	}

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException
	{
		String file = "D:/StarTeamWork/zsdx3.0_cvs/dx3.0/web/conf/pubinfo.xml";
		Dom4jReader reader = new Dom4jReader(file);
		StringUtil.debug(reader.getValue("Message.ListOrderBy"));
		StringUtil.debug(StringUtil.join(reader.getValues("ExameConfig.ExameNames.Name"), ","));
		String[] tags = new String[]{"Type", "ShowName", "CSSName"};
		String[][] result = reader.getValues("Message.TitleStyles.Style", tags);
		for (String[] tmp : result) {
			StringUtil.debug(StringUtil.join(tmp, "="));
		}
		
		System.out.println("=============================");
		List<HashMap> att = reader.getValuesWithAttribute("Classes.GroupAOPClass.property");
		for (HashMap map : att) {
			System.out.println("name=" + map.get("name"));
			System.out.println("type=" + map.get("type"));
			System.out.println("property=" + map.get("property"));
		}
	}
}
