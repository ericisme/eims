package cn.qtone.common.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用通道进行文件复制的实用类.<P>
 * 
 * 使用通道的方式直接将文件的部分内容映射到内存中，在读取大文件的时候比
 * 直接使用read和write要高效得多。
 * 
 * @author 马必强
 *
 */
public class FileCopy
{
	private File sourceFile; // 要复制的源文件
	
	private File destFile; // 要复制到的目标文件
	
	private String message; //复制文件发生错误的提示信息
	
	/**
	 * 空构造方法.
	 *
	 */
	public FileCopy()
	{
		
	}
	
	/**
	 * 使用File作为参数的构造方法.
	 * @param sourceFile
	 * @param destFile
	 */
	public FileCopy(File sourceFile, File destFile)
	{
		this.sourceFile = sourceFile;
		this.destFile = destFile;
	}
	
	/**
	 * 使用文件名作为参数的构造方法，注意必须是绝对文件名！
	 * @param sourceFile
	 * @param destFile
	 */
	public FileCopy(String sourceFile, String destFile)
	{
		this.sourceFile = new File(sourceFile);
		this.destFile = new File(destFile);
	}
	
	/**
	 * 复制指定的源文件到目的文件上.
	 * @param sourceFile
	 * @param destFile
	 * @return
	 */
	public boolean copy(File sourceFile, File destFile)
	{
		this.sourceFile = sourceFile;
		this.destFile = destFile;
		return this.copy();
	}
	
	/**
	 * 复制指定的文件，指定文件名称，注意必须是绝对文件名称！
	 * @param sourceFile
	 * @param destFile
	 * @return
	 */
	public boolean copy(String sourceFile, String destFile)
	{
		this.sourceFile = new File(sourceFile);
		this.destFile = new File(destFile);
		return this.copy();
	}
	
	/**
	 * 文件复制的主调用方法.
	 * @return
	 */
	public boolean copy()
	{
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel infc = null, outfc = null; // 输入和输出文件的通道
		try {
			this.createDestFile(); // 创建目的文件
			fis = new FileInputStream(this.sourceFile);
			fos = new FileOutputStream(this.destFile);
			infc = fis.getChannel();
			outfc = fos.getChannel();
			// 映射源文件内容到内存中
			MappedByteBuffer buf = infc.map(FileChannel.MapMode.READ_ONLY, 
					0, infc.size());
			// 复制文件
			outfc.write(buf);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			this.message = ex.getMessage();
			return false;
		} finally {
			try {
				if (outfc != null) outfc.close();
				if (infc != null) infc.close();
				if (fos != null) fos.close();
				if (fis != null) fis.close();
			} catch (Exception ex) {
				
			}
		}
	}
	
	/**
	 * 获取复制失败时的系统提示信息.
	 * @return
	 */
	public String getErroMessage()
	{
		return this.message;
	}
	
	/**
	 * 创建目标文件.
	 *
	 */
	private void createDestFile() throws IOException
	{
		if (this.destFile == null) return;
		File parent = this.destFile.getParentFile();
		if (parent != null && !parent.exists()) parent.mkdirs();
		this.destFile.createNewFile();
	}
}
