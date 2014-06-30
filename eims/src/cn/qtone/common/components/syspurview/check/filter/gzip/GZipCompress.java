package cn.qtone.common.components.syspurview.check.filter.gzip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPOutputStream;

/**
 * GZip压缩处理类.通常是用在servlet的过滤器中来对输出结果进行压缩过滤.
 * 
 * @author 马必强
 *
 */
public class GZipCompress
{
	private char[] content; // 字符数组形式的内容.
	
	private byte[] result; // 压缩后的结果
	
	public GZipCompress(char[] content)
	{
		this.content = content;
	}
	
	public GZipCompress(String content)
	{
		this.content = content.toCharArray();
	}
	
	/**
	 * 显示的调用该方法进行压缩处理.
	 *
	 */
	public void compress() throws IOException
	{
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		GZIPOutputStream zipOut = new GZIPOutputStream(byteStream);
		OutputStreamWriter tempOut = new OutputStreamWriter(zipOut);
		tempOut.write(this.content);
		tempOut.close();
		this.result = byteStream.toByteArray();
		byteStream.close();
	}
	
	/**
	 * 获取压缩后的大小.必须在调用compress后获取，否则返回-1.
	 * @return
	 */
	public int getSize()
	{
		return this.result == null ? -1 : this.result.length;
	}
	
	/**
	 * 将压缩结果输出到指定的流中.
	 * @param realout
	 */
	public void write(OutputStream out) throws IOException
	{
		if (out != null) out.write(this.result);
	}
	
	/**
	 * 获取压缩后的结果字符数组.
	 * @return
	 */
	public byte[] getBytes()
	{
		return this.result;
	}
}
