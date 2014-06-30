package cn.qtone.common.utils.fileupload;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.qtone.common.utils.base.StringUtil;

/**
 * 文件上传接口的fileupload实现类.
 * 
 * @author 马必强
 *
 */
public class FileUploader implements FileUploadInter
{
	private HttpServletRequest request;
	
	private String realpath; // 系统运行的根路径信息
	
	private String storepath; // 文件存储目录，相对系统目录而言

	private int sizeThreshold = 4096; // 允许存放在内存中的最大字节数

	private long sizeMax = -1; // 允许用户上传的最大字节数 -1为没有限制
	
	private boolean isError; // 是否发生了错误

	private FileItem fileItem; // 存储文件域的FileItem

	private String[][] attributes; // 非文件域的参数存储列表

	private ServletFileUpload uploader;
	
	private String encoding = "UTF-8"; // 参数的编码方式
	
	private String originalName; // 文件的原始文件名称
	
	private String newName; // 文件的新名称（如果没有指定则自动生成）
	
	private boolean covered; // 是否覆盖已经存在的同名文件

	/**
	 * 构造方法
	 */
	public FileUploader(HttpServletRequest request, String storepath)
	{
		this(request, storepath, -1, 4096);
	}

	public FileUploader(HttpServletRequest request, String storepath,
			long sizeMax)
	{
		this(request, storepath, sizeMax, 4096);
	}

	public FileUploader(HttpServletRequest request, String storepath,
			long sizeMax, int sizeThreshold)
	{
		this.request = request;
		this.realpath = standardPath(request.getSession().getServletContext()
				.getRealPath("/").replaceAll("\\\\", "/"), false);
		this.storepath = standardPath(storepath.replaceAll("\\\\", "/"), true);
		this.sizeMax = sizeMax;
		this.sizeThreshold = sizeThreshold;
		init();
	}
	
	public String getFilePath()
	{
		return this.storepath + "/" + this.newName;
	}

	public String getOriginalName()
	{
		return this.originalName;
	}

	public String getParameter(String paraName)
	{
		return this.getParameter(paraName, encoding);
	}

	public String getParameter(String paraName, String encoding)
	{
		if (paraName == null || paraName.equals("")) return null;
		if (this.attributes == null) this.parseRequest();
		// 遍历所有非文件域参数并取其值
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i][0].equalsIgnoreCase(paraName)) {
				return attributes[i][1];
			}
		}
		return null;
	}

	public void setEncoding(String encoding)
	{
		if (StringUtil.trim(encoding).intern() != "") this.encoding = encoding;
	}

	public boolean upload()
	{
		if (this.isError) return false;
		if (this.attributes == null) this.parseRequest();
		if (this.fileItem == null) return false;
		
		// 进行文件上传
		try {
			// 文件的原始名称
			this.originalName = StringUtil.trim(this.fileItem.getName());
			if (this.newName == null) this.newName = this.createRandomFile();
			File f = new File(this.realpath + "/" + this.storepath + "/" + this.newName);
			// 如果没有指定覆盖同名文件则抛出异常
			if (!this.covered && f.exists()) throw new RuntimeException("文件" + f.getAbsolutePath() + "已存在！");
			this.fileItem.write(f);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public void setFileName(String fileName, boolean covered)
	{
		if (StringUtil.trim(fileName).intern() != "") this.newName = fileName;
		this.covered = covered;
	}

	/**
	 * 初始化方法
	 */
	private void init()
	{
		DiskFileItemFactory dfileFac = new DiskFileItemFactory();
		dfileFac.setRepository(new File(realpath + storepath + "/temp"));
		dfileFac.setSizeThreshold(sizeThreshold);

		uploader = new ServletFileUpload(dfileFac);
		uploader.setSizeMax(sizeMax);

		// 检查指定的文件夹是否存在，不存在则创建
		try {
			File myfile = new File(realpath + storepath);
			if (!myfile.isDirectory()) myfile.mkdirs();
			myfile = new File(realpath + storepath + "/temp");
			if (!myfile.isDirectory()) myfile.mkdirs();
		} catch (SecurityException ex) {
			ex.printStackTrace();
			isError = true;
		}
	}

	/**
	 * 规范路径，路径必须最前加/最后不要/
	 */
	private String standardPath(String path, boolean stIndex)
	{
		if (path == null) return null;
		if (stIndex && !path.startsWith("/")) return "/" + path;
		if (path.endsWith("/")) return path.substring(0, path.length());
		return path;
	}

	/**
	 * 解析HttpServletRequest请求。
	 * 
	 */
	private void parseRequest()
	{
		List<FileItem> paraList = new ArrayList<FileItem>();
		try {
			List fileItems = uploader.parseRequest(request);
			Iterator it = fileItems.iterator();
			while (it.hasNext()) {
				FileItem item = (FileItem) it.next();
				// 存储所有文件域的参数
				if (!item.isFormField()) {
					if (!"".equals(item.getName()) && item.getSize() > 0) {
						fileItem = item;
					}
				}
				// 存储所有非文件域的参数
				else paraList.add(item); 
			}
			// 解析非文件域参数
			attributes = new String[paraList.size()][2];
			int icount = 0;
			it = paraList.iterator();
			while (it.hasNext()) {
				FileItem fileItem = (FileItem) it.next();
				attributes[icount][0] = fileItem.getFieldName();
				try {
					attributes[icount][1] = fileItem.getString(encoding);
				} catch (UnsupportedEncodingException ex) {
					attributes[icount][1] = fileItem.getString();
					ex.printStackTrace();
				}
				++icount;
			}
		} catch (FileUploadException ex) {
			ex.printStackTrace();
			isError = true;
		}
	}
	
	/**
	 * 生成随机文件名
	 */
	private String createRandomFile() throws IOException
	{
		this.covered = true;
		// 获取原始文件的后缀
		int index = this.originalName.lastIndexOf('.');
		if (index == this.originalName.length() - 1) index = -1;
		String suffix = index == -1 ? "" : ("." + this.originalName.substring(index + 1));
		while (true) {
			String fileName = System.currentTimeMillis() +
				"" + new Double(899999 * Math.random() + 100000).intValue() + suffix;
			File file = new File(this.realpath + "/" + this.storepath + "/" + fileName);
			if (file.exists() && file.isFile()) continue;
			if (!file.createNewFile()) continue;
			return fileName;
		}
	}

	public long getFileSize()
	{
		return this.isError ? 0 : (this.fileItem == null ? 0 : this.fileItem.getSize());
	}

}
