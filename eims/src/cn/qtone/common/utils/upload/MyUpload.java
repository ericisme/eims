package cn.qtone.common.utils.upload;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.SecurityException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 处理文件的上传
 * 
 * @author 马必强
 * 
 */
public class MyUpload implements UploadInter
{
	private String realPath; // 文件上传的物理路径(项目运行的根路径)

	private String filePath; // 文件的实际存放路径

	private int sizeThreshold = 4096; // 允许存放在内存中的最大字节数

	private long sizeMax = -1; // 允许用户上传的最大字节数 -1为没有限制

	private HttpServletRequest uploadReq; // 文件上传请求

	private ServletFileUpload fileUpload;

	private boolean pathError = false; // 文件路径错误或创建时发生错误

	private boolean haveParsed = false; // 是否已经解析了HttpServletRequest
	
	private boolean coverRepeatedFile = false; // 是否覆盖重名的文件，默认不是
	
	private boolean randomFile = false; // 是否使用随机文件名来上传文件，默认不是
	
	private String endFix = null; // 使用随机文件名的时候指定的后缀，不指定则使用默认

	private String[] originalName; // 存储的文件的文件名(可以多个上传)
	
	private String[] newName; // 存储的文件的新文件名(可以多个上传)

	private String[][] attributes; // 文件上传表单中的非文件域属性集

	private List<FileItem> fileList; // 存储文件域的FileItem

	private List<FileItem> paraList; // 存储参数的FileItem
	
	private String encoding = "GBK"; // 参数的编码方式，默认是GBK

	/**
	 * 构造方法
	 */
	public MyUpload(HttpServletRequest req, String filePath)
	{
		this(req, filePath, -1, 4096);
	}

	public MyUpload(HttpServletRequest req, String filePath,
			long sizeMax)
	{
		this(req, filePath, sizeMax, 4096);
	}

	public MyUpload(HttpServletRequest req, String filePath,
			long sizeMax, int sizeThreshold)
	{
		this.uploadReq = req;
		this.realPath = standardPath(req.getSession().getServletContext()
				.getRealPath("/").replaceAll("\\\\", "/"), false);
		this.filePath = standardPath(filePath.replaceAll("\\\\", "/"), true);
		this.sizeMax = sizeMax;
		this.sizeThreshold = sizeThreshold;
		init();
	}

	/**
	 * 初始化方法
	 */
	private void init()
	{
		DiskFileItemFactory dfileFac = new DiskFileItemFactory();
		dfileFac.setRepository(new File(realPath + filePath + "/temp"));
		dfileFac.setSizeThreshold(sizeThreshold);

		fileUpload = new ServletFileUpload(dfileFac);
		fileUpload.setSizeMax(sizeMax);

		// 检查指定的文件夹是否存在，不存在则创建
		File myfile = null;
		try {
			myfile = new File(realPath + filePath);
			if (!myfile.isDirectory()) {
				myfile.mkdirs();
			}
			myfile = null;
			myfile = new File(realPath + filePath + "/temp");
			if (!myfile.isDirectory()) {
				myfile.mkdirs();
			}
			myfile = null;
		} catch (SecurityException ex) {
			ex.printStackTrace();
			pathError = true;
		}
	}

	/**
	 * 规范路径，路径必须最前加/最后不要/
	 * 
	 * @param path
	 *            要规范的路径
	 * @param stIndex
	 *            最前面是否要加/路径分割符
	 * @return
	 */
	private String standardPath(String path, boolean stIndex)
	{
		if (path == null) return null;
		StringBuffer tmp = new StringBuffer(path);
		if (stIndex && !path.startsWith("/")) {
			tmp.insert(0, "/");
		}
		if (path.endsWith("/")) {
			tmp.deleteCharAt(tmp.length() - 1);
		}
		return tmp.toString();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.qtone.zsdx.util.Upload#setCoverRepeatedFile(boolean)
	 */
	public void setCoverRepeatedFile(boolean cover)
	{
		this.coverRepeatedFile = cover;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.qtone.zsdx.util.Upload#upLoad()
	 */
	public boolean upLoad()
	{
		return upLoad(1);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.qtone.zsdx.util.Upload#getOriginalName()
	 */
	public String getOriginalName()
	{
		if (originalName == null) {
			return null;
		}
		return originalName[0];
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.qtone.zsdx.util.Upload#getFilePath()
	 */
	public String getFilePath()
	{
		String[] tmp = randomFile ? newName : originalName;
		if (tmp == null || tmp[0] == null) return null;
		return this.filePath + "/" + tmp[0];
	}

	public String getNewRandomName()
	{
		String[] tmp = randomFile ? newName : originalName;
		if (tmp == null || tmp[0] == null) return null;
		return tmp[0];
	}

	public String[] getNewRandomNames()
	{
		return randomFile ? newName : originalName;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.qtone.zsdx.util.Upload#getFilesName()
	 */
	public String[] getOriginalNames()
	{
		return originalName;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.qtone.zsdx.util.Upload#getFilesPath()
	 */
	public String[] getFilesPath()
	{
		String[] tmp = randomFile ? newName : originalName;
		if (tmp == null) return null;
		String[] files = new String[tmp.length];
		for (int i = 0; i < tmp.length; i++) {
			files[i] = tmp[i] == null ? null : (this.filePath + "/" + tmp[i]);
		}
		return files;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.qtone.zsdx.util.Upload#upLoad(int)
	 */
	public boolean upLoad(int fileNums)
	{
		// 文件路径错误时候返回false
		if (pathError) return false;

		if (!haveParsed) parseRequest();

		// 处理每个文件域的item
		originalName = new String[fileNums <= 0 ? fileList.size() : fileNums];
		newName = new String[fileNums <= 0 ? fileList.size() : fileNums];
		Iterator it = fileList.iterator();
		int icount = 0;
		while (it.hasNext()) {
			FileItem fileItem = (FileItem) it.next();
			if (!fileItem.isFormField()) { // 忽略其他不是文件域的表单域
				long size = fileItem.getSize();
				if ((fileItem.getName() == null || fileItem.getName().equals(""))
					&& size == 0) {
					continue;
				}

				String[] temp = fileItem.getName().replaceAll("\\\\", "/")
						.split("/");
				try {
					// 如果是使用随机文件名
					if (randomFile) {
						originalName[icount] = temp[temp.length - 1];
						// 获取后缀，如果长度为1则表示没有后缀
						if (endFix == null) {
							String[] fix = temp[temp.length - 1].split("[.]");
							newName[icount] = createRandomFile(fix);
						} else {
							newName[icount] = createRandomFile(null);
						}
					} else {
						// 如果没有设置覆盖重名文件则生成新的文件名
						if (!coverRepeatedFile) {
							if ((originalName[icount] = 
								checkFileName(temp[temp.length - 1])) == null) {
								continue;
							}
						} else {
							originalName[icount] = temp[temp.length - 1];
						}
					}
					// 写文件到指定的目录
					fileItem.write(new File(realPath + filePath + "/"
							+ (randomFile ? newName[icount] : originalName[icount])));
				} catch (Exception ex) {
					ex.printStackTrace();
					return false;
				}
				++icount;
			}
		}
		return true;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see cn.qtone.zsdx.util.Upload#getParameter(java.lang.String)
	 */
	public String getParameter(String paraName)
	{
		return this.getParameter(paraName, this.encoding);
	}

	public String getParameter(String paraName, String encoding)
	{
		if (paraName == null || paraName.equals("")) return null;
		if (attributes == null) {
			if (!haveParsed) parseRequest();
			attributes = new String[paraList.size()][2];

			// 存放所有的属性
			int icount = 0;
			Iterator it = paraList.iterator();
			while (it.hasNext()) {
				FileItem fileItem = (FileItem) it.next();
				if (fileItem.isFormField()) {
					attributes[icount][0] = fileItem.getFieldName();
					try {
						attributes[icount][1] = fileItem.getString(encoding);
					} catch (UnsupportedEncodingException ex) {
						attributes[icount][1] = fileItem.getString();
						ex.printStackTrace();
					}
					++icount;
				}
			}
		}
		// 遍历所有非文件域参数并取其值
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i][0].equalsIgnoreCase(paraName)) {
				return attributes[i][1];
			}
		}
		return null;
	}

	/**
	 * 设置是否使用指定的后缀来生成随机的文件
	 */
	public void setUseRandomFileName(boolean random, String endFix)
	{
		this.randomFile = random;
		if (endFix != null && endFix.trim().intern() != "") {
			this.endFix = endFix.trim();
		}
	}
	
	/**
	 * 生成随机文件名
	 * @param fix
	 * @return
	 * @throws IOException 
	 */
	private String createRandomFile(String[] fix) throws IOException
	{
		while (true) {
			String fileName = System.currentTimeMillis() +
				"" + new Double(8999 * Math.random() + 1000).intValue();
			File file;
			if (endFix != null) {
				file = new File(fileName + "." + endFix);
			} else {
				if (fix.length > 1) {
					file = new File(fileName + "." + fix[fix.length - 1]);
				} else {
					file = new File(fileName);
				}
			}
			if (file.exists() && file.isFile()) continue;
			if (!file.createNewFile()) continue;
			return file.getName();
		}
	}

	/**
	 * 解析HttpServletRequest请求。
	 * 
	 */
	private void parseRequest()
	{
		fileList = new ArrayList<FileItem>();
		paraList = new ArrayList<FileItem>();

		// 解析请求
		try {
			List fileItems = fileUpload.parseRequest(uploadReq);
			Iterator it = fileItems.iterator();
			while (it.hasNext()) {
				FileItem item = (FileItem) it.next();
				if (item.isFormField()) {
					paraList.add(item); // 存储所有非文件域的参数
				} else {
					fileList.add(item); // 存储所有文件域的参数
				}
			}
		} catch (FileUploadException ex) {
			ex.printStackTrace();
		}

		haveParsed = true;
	}

	/**
	 * 检查生成的文件名是否存在，存在则重新生成！
	 * 
	 * @param temp
	 * @return
	 */
	private String checkFileName(String temp) throws IOException
	{
		int icount = -1, pointIndex = temp.lastIndexOf('.');
		String[] curFile = null;
		if (pointIndex != -1 && pointIndex < temp.length() - 1) {
			curFile = new String[] {temp.substring(0, pointIndex),
					temp.substring(pointIndex + 1)};
		} else {
			curFile = new String[] {temp};
		}
		StringBuffer fileName = new StringBuffer();
		while (true) {
			fileName.append(curFile[0]);
			if (icount != -1) fileName.append(++icount);
			fileName.append(curFile.length > 1 ? ("." + curFile[1]) : "");
			// 这里使用ISO8859-1编码后出现中文文件上传时有两个文件的问题，一个乱码和一个不乱码的文件
			/*File file = new File(new String(
					(realPath + filePath + "/" + fileName).getBytes(),
					"ISO8859-1"));*/
			File file = new File(new String(
					(realPath + filePath + "/" + fileName).getBytes(),
					"GB2312"));
			if (file.exists() && file.isFile()) { // 当文件存在时继续生成新的文件名再检测
				fileName.delete(0, fileName.length());
				if (icount == -1) icount = 0;
				continue;
			} else if (file.createNewFile()) {
				break;
			}
		}
		return fileName.toString();
	}

	public void setEncoding(String encoding)
	{
		if (encoding != null && encoding.trim().intern() != "") this.encoding = encoding;
	}
}
