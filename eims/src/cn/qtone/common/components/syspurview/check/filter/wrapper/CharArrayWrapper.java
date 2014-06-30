package cn.qtone.common.components.syspurview.check.filter.wrapper;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 一个对response的包装器，将response的输出流封装在一个大的字符数组里便于修改！
 * <P>
 * 在过滤器中使用的时候应该这样使用：
 * CharArrayWrapper wrapper = new CharArrayWrapper((HttpServletResponse)response);
 * //这步很重要，将调用资源将输出流累计在我们的CharArrayWrapper中
 * chain.doFilter(request, wrapper);
 * String result = wrapper.toString(); // 获取完整的输出流
 * ……  // 处理字符串
 * PrintWriter out = response.getWriter(); // 输出结果
 * out.write(responseString);
 * out.close();
 * 
 * @author 马必强
 */
public class CharArrayWrapper extends HttpServletResponseWrapper
{
	private CharArrayWriter charWriter;

	/**
	 * 包装器初始化
	 * <P>
	 * 首先这个包装器将调用父类的构造方法，并设置这些方法如setHeader, setStatus,
	 * addCookie等
	 * <P>
	 * 另外将新建立一个字符输出流来执行response的响应输出操作.
	 */
	public CharArrayWrapper(HttpServletResponse response)
	{
		super(response);
		charWriter = new CharArrayWriter();
	}
	
	public PrintWriter getWriter()
	{
		return new PrintWriter(charWriter);
	}

	/**
	 * 将缓冲区的字符转换成一个字符串返回.
	 * <P>
	 * 需要注意的是不要对同一个包装器多次调用该方法，否则将每次返回一个新的字符串！
	 */
	public String toString()
	{
		return charWriter.toString();
	}

	public char[] toCharArray()
	{
		return charWriter.toCharArray();
	}
}
