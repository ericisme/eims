package cn.qtone.common.utils.upload;

/**
 * 文件上传组件的接口，实现文件上传的无依赖性。
 * @author 马必强
 *
 */
public interface UploadInter
{
	/**
	 * 设置是否覆盖存在的重名的文件，默认是不覆盖
	 * 
	 * @param cover
	 */
	public abstract void setCoverRepeatedFile(boolean cover);
	
	/**
	 * 设置是否使用系统生成的随机文件名称，默认是不使用.
	 * 注意使用随机文件名是不会覆盖已存在的文件的.
	 * 
	 * @param random
	 * @param endFix 指定的后缀，如果不指定则从原始文件获取
	 */
	public abstract void setUseRandomFileName(boolean random, String endFix);

	/**
	 * 处理文件上传操作，默认是一个文件
	 * 
	 * @return
	 */
	public abstract boolean upLoad();

	/**
	 * 返回第一个文件的原始文件名，对应处理一个文件上传时
	 * 
	 * @return
	 */
	public abstract String getOriginalName();

	/**
	 * 返回第一个文件的随机新文件名，对应处理一个文件上传时
	 * 如果没有设置使用随机名则返回原始文件名
	 * 
	 * @return
	 */
	public abstract String getNewRandomName();

	/**
	 * 返回上传文件的路径加文件名.
	 * 如果是使用随机文件名称则返回的是以随机文件名形成的文件路径信息；
	 * 如果不是使用随机文件名则返回以原始文件名为基础的文件路径信息。
	 * @return
	 */
	public abstract String getFilePath();

	/**
	 * 取所有上传的文件名
	 * 
	 * @return
	 */
	public abstract String[] getOriginalNames();
	
	/**
	 * 获取所有上传文件的随机文件名称，如果没有设置使用随机名称
	 * 则返回原始文件名
	 * @return
	 */
	public abstract String[] getNewRandomNames();

	/**
	 * 返回所有上传文件的路径加文件名信息。
	 * 如果是使用随机文件名称则返回的是以随机文件名形成的文件路径信息；
	 * 如果不是使用随机文件名则返回以原始文件名为基础的文件路径信息。
	 * @return
	 */
	public abstract String[] getFilesPath();

	/**
	 * 处理文件上传操作重构方法 fileNums 为文件上传的个数
	 * 
	 * @param fileNums
	 * @return
	 */
	public abstract boolean upLoad(int fileNums);

	/**
	 * 取上传文件表单域的非文件域参数属性。不区分大小写
	 * @param paraName      属性名称
	 * @return
	 */
	public abstract String getParameter(String paraName);
	
	/**
	 * 获取指定参数的值并进行指定的编码方式进行编码.
	 * @param paraName
	 * @param encoding
	 * @return
	 */
	public abstract String getParameter(String paraName, String encoding);
	
	/**
	 * 设置参数的编码方式，默认是GBK.
	 * @param encoding
	 */
	public abstract void setEncoding(String encoding);

}