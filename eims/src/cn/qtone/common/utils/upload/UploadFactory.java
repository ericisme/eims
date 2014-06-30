package cn.qtone.common.utils.upload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件上传类的工厂产生方法。
 * @author 马必强
 *
 */
public class UploadFactory
{
	/**
	 * 以下三个方法都是调用UpLoadFile类进行文件上传的。
	 * @param realPath      web程序的运行路径根目录，一般使用request获取
	 * @param filePath      文件存储的路径，相对realPath而言
	 * @param sizeMax       最大允许上传的文件大小，单位字节
	 * @param sizeThreshold 允许存放在内存中的最大字节数
	 */
	public static UploadInter getUploadInstance(HttpServletRequest req, String filePath,
			long sizeMax, int sizeThreshold)
	{
		return new MyUpload(req, filePath, sizeMax, sizeThreshold);
	}

	public static UploadInter getUploadInstance(HttpServletRequest req, String filePath)
	{
		return new MyUpload(req, filePath);
	}

	public static UploadInter getUploadInstance(HttpServletRequest req, String filePath, long sizeMax)
	{
		return new MyUpload(req, filePath, sizeMax);
	}
	
	/**
	 * 文件下载.
	 * @param response 响应对象
	 * @return 实现DownloadInter接口的实例
	 */
	public static DownloadInter getDownloadInstance(HttpServletResponse response)
	{
		return new MyDownload(response);
	}
}
