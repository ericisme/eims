package cn.qtone.common.components.syspurview.check.filter.gzip;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.qtone.common.components.syspurview.check.filter.wrapper.CharArrayWrapper;

/**
 * gzip编码压缩过滤器.可以将输出结果进行压缩后再输出，提高系统的输出吞吐量！
 * 
 * @author 马必强
 * 
 */
public class CompressionFilter implements Filter
{

	public void destroy()
	{
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		// 如果不支持，则直接返回
		if (!isGzipSupported(req)) {
			chain.doFilter(req, res);
		} else {
			// 设置响应头，告诉浏览器返回流的编码方式为GZIP
			res.setHeader("Content-Encoding", "gzip");
			
			// 截获输出流
			CharArrayWrapper wrapper = new CharArrayWrapper(res);
			chain.doFilter(req, wrapper);
			
			// 压缩输出结果.
			GZipCompress gzip = new GZipCompress(wrapper.toCharArray());
			gzip.compress();
			
			// 更新内容的长度信息并输出到客户端
			res.setContentLength(gzip.getSize());
			gzip.write(res.getOutputStream());
		}

	}

	public void init(FilterConfig arg0) throws ServletException
	{
	}

	/**
	 * 判断浏览器是否支持gzip压缩格式.
	 * 
	 * @param req
	 * @return
	 */
	private boolean isGzipSupported(HttpServletRequest req)
	{
		String browserEncodings = req.getHeader("Accept-Encoding");
		return ((browserEncodings != null) && (browserEncodings.indexOf("gzip") != -1));
	}
}
