package cn.qtone.common.utils.xml;

import java.util.HashMap;
import java.util.List;

/**
 * XML文件读取的接口.
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public interface XMLReaderInter
{
	/**
	 * 获取错误信息，没有错误则返回null.
	 * @return
	 */
	public abstract String getErrorMsg();

	/**
	 * 解析某个元素并获取它的值（如果有多个的话则获取第一个的值）
	 * 
	 * @param tagName
	 *            元素的标记层次结构名称，相对根元素
	 * @return
	 */
	public abstract String getValue(String tagName);

	/**
	 * 解析某个元素并获取它的值，有多个则返回数组
	 * 
	 * @param tagName
	 *            元素的标记层次结构名称，相对根元素
	 * @return
	 */
	public abstract String[] getValues(String tagName);

	/**
	 * 获取指定标记下的所有子接点的值.
	 * @param tagName
	 * @param childNodeNums
	 * @return
	 */
	public abstract String[][] getValues(String tagName, String[] tags);
	
	/**
	 * 将指定的标记下的接点转换成字符串显示.
	 * @param tagName 标记名称
	 * @return
	 */
	public abstract String toString(String tagName);
	
	/**
	 * 获取指定标记的值，属性也同时获取。每一个标签节点的值都使用hashmap保存，
	 * 标签的值key使用其名称，每个属性的key也使用其名称。
	 * @param tagName
	 * @return
	 */
	public List<HashMap> getValuesWithAttribute(String tagName);

}