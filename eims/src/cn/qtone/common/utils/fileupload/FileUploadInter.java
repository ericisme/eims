package cn.qtone.common.utils.fileupload;

/**
 * 文件上传的通用接口.
 * 
 * @author 马必强
 *
 */
public interface FileUploadInter
{
	/**
	 * 取上传文件表单域的非文件域参数属性。不区分大小写
	 * @param paraName      属性名称
	 * @return
	 */
	public abstract String getParameter(String paraName);
	
	/**
	 * 获取指定参数的值并用指定的编码方式进行编码.
	 * @param paraName
	 * @param encoding
	 * @return
	 */
	public abstract String getParameter(String paraName, String encoding);
	
	/**
	 * 设置参数的编码方式，默认是UTF-8.
	 * @param encoding
	 */
	public abstract void setEncoding(String encoding);
	
	/**
	 * 文件上传的主要调用方法.
	 * @return
	 */
	public abstract boolean upload();

	/**
	 * 获取上传文件的原始文件名称（不包括路径信息）.
	 * @return
	 */
	public abstract String getOriginalName();

	/**
	 * 获取上传文件在服务器上的相对路径信息.
	 * @return
	 */
	public abstract String getFilePath();
	
	/**
	 * 设置上传文件的最终文件名，covered用来标识是否覆盖已存在的同名文件.
	 * 如果covered为true并且同名文件存在则进行覆盖，否则抛出异常.
	 * @param fileName
	 */
	public abstract void setFileName(String fileName, boolean covered);
	
	/**
	 * 获取上传文件的大小.单位为字节，默认或发生错误时返回0.
	 *
	 */
	public abstract long getFileSize();
}
