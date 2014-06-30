package cn.qtone.common.utils.auto.spring.read;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.qtone.common.utils.base.StringUtil;


/**
 * 读取配置文件内容.
 * 
 * @author 马必强
 *
 */
public class FileReader
{
	/**
	 * 从指定的文件中读取文件内容.
	 * @param filePath
	 * @return
	 */
	public static String readFile(String filePath)
	{
		try {
			return readFile(new FileInputStream(filePath));
		} catch (FileNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * 通过指定的流来读取内容.
	 * @param is
	 * @return
	 */
	public static String readFile(InputStream is)
	{
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is, "GBK"));
			StringBuilder buf = new StringBuilder();
			String tmp = null;
			while ((tmp = br.readLine()) != null) buf.append(StringUtil.trim(tmp));
			return buf.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				if (is != null) is.close();
				if (isr != null) isr.close();
				if (br != null) br.close();
			} catch (Exception ex) {
				
			}
		}
	}
}
