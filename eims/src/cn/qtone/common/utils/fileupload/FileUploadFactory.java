package cn.qtone.common.utils.fileupload;

import javax.servlet.http.HttpServletRequest;

/**
 * 文件上传类的工厂产生方法。
 * @author 马必强
 *
 */
public class FileUploadFactory
{
	/**
	 * 以下三个方法都是调用UpLoadFile类进行文件上传的。
	 * @param storepath     文件存储的路径，相对realPath而言
	 * @param sizeMax       最大允许上传的文件大小，单位字节
	 * @param sizeThreshold 允许存放在内存中的最大字节数
	 */
	public static FileUploadInter getUploadInstance(HttpServletRequest req, String storepath,
			long sizeMax, int sizeThreshold)
	{
		return new FileUploader(req, storepath, sizeMax, sizeThreshold);
	}

	public static FileUploadInter getUploadInstance(HttpServletRequest req, String storepath)
	{
		return new FileUploader(req, storepath);
	}

	public static FileUploadInter getUploadInstance(HttpServletRequest req, String storepath, long sizeMax)
	{
		return new FileUploader(req, storepath, sizeMax);
	}
}
