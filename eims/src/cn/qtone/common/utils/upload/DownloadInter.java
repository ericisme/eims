package cn.qtone.common.utils.upload;

/**
 * 文件下载的接口.
 * 
 * @author 马必强
 * @version 1.0
 *
 */
public interface DownloadInter
{
	/**
	 * 设置文件下载时的名称,可以指定后缀.
	 * 
	 * @param name 指定的文件名称
	 */
	public void setFileName(String name);
	
	/**
	 * 使用默认编码进行文件下载的方法.
	 * 
	 * @param fileName 文件的路径信息，这个路径是文件的物理路径.
	 */
	public void download(String fileName) throws Exception;

	/**
	 * 使用指定编码进行文件下载的方法.
	 * 
	 * @param fileName 文件的路径信息，这个路径是文件的物理路径.
	 * @param encoding 指定的编码格式
	 */
	public void download(String fileName, String encoding) throws Exception;
}
