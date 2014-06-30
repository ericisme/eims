package cn.qtone.common.utils.base;

import java.util.ArrayList;
import java.util.List;

/**
 * 集合操作实用类.
 * 
 * @author 马必强
 *
 */
public class CollectionUtil
{
	/**
	 * 将包含整型的list结果集转换成数组.
	 * @param list
	 * @return
	 */
	public final static int[] listToIntArray(List list)
	{
		int[] result = new int[list.size()];
		for (int i = 0; i < list.size(); i ++) {
			result[i] = (Integer)list.get(i);
		}
		return result;
	}

	/**
	 * 将包含字符串型的list结果集转换成数组.
	 * @param list
	 * @return
	 */
	public final static String[] listToStringArray(List<?> list)
	{
		String[] result = new String[list.size()];
		return (String[])list.toArray(result);
	}
	
	public static void main(String[] args)
	{
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(3);
		list1.add(5);
		list1.add(9);
		int[] array = listToIntArray(list1);
		StringUtil.debug(StringUtil.join(array, ","));
		

		List<String> list2 = new ArrayList<String>();
		list2.add("hello");
		list2.add("world");
		String[] str = listToStringArray(list2);
		StringUtil.debug(StringUtil.join(str, ","));
	}
}
