package cn.qtone.common.utils.xml;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * XML文件读取器，采用JAVA中定义的接口方式来读取分析XML文件.
 * 使用此类需要提供对应的解析器才能正常执行！建议使用Dom4jReader
 * 
 * @author 马必强
 * @version 1.0
 * 
 */
public class JavaReader implements XMLReaderInter
{
	private DocumentBuilderFactory dbf = null;

	private DocumentBuilder db = null;

	private Document doc = null;

	private Element root = null;

	private String spliter = "."; // 元素之间的分割符
	
	private String error = null; // 错误信息，没有错误则为null

	/**
	 * 构造函数，指定要解析的XML文件路径.<br>
	 * 该构造方法不需要进行DTD验证.
	 * 
	 * @param xmlFile 待解析的XML文件
	 */
	public JavaReader(String xmlFile)
	{
		this(xmlFile, false);
	}
	
	public JavaReader(String xmlFile, boolean validate)
	{
		// 通过文档模型的工厂模式逐步建立文档模型对象doc
		try {
			dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(validate);
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			ex.printStackTrace();
			error = ex.getMessage();
		}
		// doc=db.newDocument();

		// 加载并分析XML文件获得DOM树
		try {
			doc = db.parse(xmlFile);
		} catch (IOException ex) {
			ex.printStackTrace();
			error = ex.getMessage();
		} catch (SAXException ex) {
			ex.printStackTrace();
			error = ex.getMessage();
		}

		// 取得文档的根元素
		root = doc.getDocumentElement();
	}
	
	/* （非 Javadoc）
	 * @see cn.qtone.common.utils.xml.XMLInter#getErrorMsg()
	 */
	public String getErrorMsg()
	{
		return this.error;
	}

	/**
	 * 解析元素。对于多层结构以.为分割符号
	 * 
	 * @param tagName
	 *            元素的标记名称，如database,url
	 * @return nodes 该元素下的所有结点
	 */
	private NodeList parse(String tagName)
	{
		NodeList nodes = null;
		Element tag = root;

		String[] tags = tagName.split("[" + spliter + "]");
		for (int i = 0; i < tags.length; i++) {
			nodes = tag.getElementsByTagName(tags[i]);
			if (nodes.getLength() >= 1) { // 表示存在该元素
				tag = (Element) nodes.item(0); // 取第一个
			} else {
				return null;
			}
		}

		return nodes;
	}

	/* （非 Javadoc）
	 * @see cn.qtone.common.utils.xml.XMLInter#getValue(java.lang.String)
	 */
	public String getValue(String tagName)
	{
		NodeList nodelist = null;
		Element temp = null;

		nodelist = parse(tagName); // 解析
		if (nodelist == null) { // 解析错误，找不到元素
			return null;
		}

		// 返回指定的元素的值
		temp = (Element) nodelist.item(0);
		Text txt = (Text) temp.getFirstChild();
		return txt == null ? null : txt.getNodeValue().trim();
	}

	/* （非 Javadoc）
	 * @see cn.qtone.common.utils.xml.XMLInter#getValues(java.lang.String)
	 */
	public String[] getValues(String tagName)
	{
		NodeList nodes = getNodeList(tagName);
		if (nodes == null || nodes.getLength() < 1) return null;
		
		String[] tmp = new String[nodes.getLength()];
		for (int i = 0; i < tmp.length; i ++) {
			Text txt = (Text)((Element)nodes.item(i)).getFirstChild();
			if (txt != null) tmp[i] = txt.getNodeValue().trim().toLowerCase();
		}
		
		return tmp;
	}
	
	/* （非 Javadoc）
	 * @see cn.qtone.common.utils.xml.XMLInter#getValues(java.lang.String, java.lang.String[])
	 */
	public String[][] getValues(String tagName, String[] tags)
	{
		NodeList nodes = getNodeList(tagName);
		if (nodes == null || nodes.getLength() < 1) return null;
		
		String[][] tmp = new String[nodes.getLength()][tags.length];
		for (int i = 0; i < tmp.length; i ++) {
			Element et = (Element)nodes.item(i);
			for (int j = 0; j < tags.length; j ++) {
				NodeList _nodes = et.getElementsByTagName(tags[j]);
				if (_nodes.getLength() < 1) continue;
				Text txt = (Text)((Element)_nodes.item(0)).getFirstChild();
				if (txt == null) continue;
				tmp[i][j] = txt.getNodeValue().trim().toLowerCase();
			}
		}
		
		return tmp;
	}
	
	/**
	 * 获取指定标记下的所有标记元素
	 * @param tagName
	 * @return
	 */
	private NodeList getNodeList(String tagName)
	{
		NodeList nodes = null;
		Element tag = root;
		String[] tags = tagName.split("[" + spliter + "]");
		for (int i = 0; i < tags.length - 1; i ++) {
			nodes = tag.getElementsByTagName(tags[i]);
			if (nodes.getLength() >= 1) { // 表示存在该元素
				tag = (Element) nodes.item(0); // 取第一个
			} else {
				return null;
			}
		}
		return tag.getElementsByTagName(tags[tags.length - 1]);
	}

	public String toString(String tagName)
	{
		return null;
	}

	public List<HashMap> getValuesWithAttribute(String tagName)
	{
		return null;
	}
}
