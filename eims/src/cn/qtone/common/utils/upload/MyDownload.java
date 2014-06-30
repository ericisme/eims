package cn.qtone.common.utils.upload;

import java.io.FileInputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件下载的接口实现类.
 * 
 * @author 马必强
 * 
 */
public class MyDownload implements DownloadInter
{
	private HttpServletResponse response;

	private String encoding = "UTF-8";
	
	private String name; // 文件的下载名称

	private int blockSize = 65000;

	public MyDownload(HttpServletResponse response)
	{
		this.response = response;
	}

	public void download(String fileName) throws Exception
	{
		// 文件为空或不存在
		if (fileName == null || fileName.trim() == "") {
			throw new Exception("指定的文件不存在！");
		}
		// 设置文件下载
		response.setContentType("application/x-msdownload");
		String myFile = fileName.replaceAll("\\\\", "/");
		if (myFile.charAt(myFile.length() - 1) == '\\') {
			myFile = myFile.substring(0, myFile.length() - 1);
		}
		if (name == null) {
			name = myFile.substring(myFile.lastIndexOf("/") + 1);
		}
		response.setHeader("Content-Disposition", "attachment; filename="
				+ new String(name.getBytes(encoding), "ISO8859_1"));
		// 下载文件
		java.io.File file = new java.io.File(myFile);
		FileInputStream fileIn = new FileInputStream(file);
		long fileLen = file.length();
		int readBytes = 0;
		int totalRead = 0;
		byte b[] = new byte[blockSize];
		while ((long) totalRead < fileLen) {
			readBytes = fileIn.read(b, 0, blockSize);
			totalRead += readBytes;
			response.getOutputStream().write(b, 0, readBytes);
		}
		fileIn.close();
	}

	public void download(String fileName, String encoding) throws Exception
	{
		this.encoding = encoding;
		download(fileName);
	}

	/**
	 * 指定下载文件时的附件名称.
	 */
	public void setFileName(String name)
	{
		if (name != null && name.trim().intern() != "") {
			this.name = name;
		}
	}

}
