/**
 * 
 */
package cn.qtone.qtoneframework.web.view.poi;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * excel报表的附助类<br>
 * 此类的使用方法为，先调用setInitPosition()、setMethodName()、setTemplateUrl()、setValueObject()
 * 然后才可以调用getWorkbook()、saveToHardist()。
 * 
 * @author 林子龙
 * 
 */
public class ExcelReport<T> {
	// 模版地址
	private String templateUrl;
	// 初始列位置
	private Integer initColumn;
	// 初始行位置
	private Integer initRow;
	// 方法名
	private String[] methodNames;
	// 值对像集合
	private List<T> valueObjects;

	private String getTemplateUrl() {
		return templateUrl;
	}

	/**
	 * 解释，并把excel保存到磁盘
	 * 
	 * @param url
	 * @throws FileNotFoundException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void saveToHardist(String url) throws FileNotFoundException, SecurityException, IllegalArgumentException,
			IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(url));
		getWorkbook().write(bo);
		bo.close();
	}

	/**调用此方法，直接下载到客户端
	 * @param response
	 * @param fileName
	 * @throws FileNotFoundException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void download(HttpServletResponse response, String fileName) throws FileNotFoundException,
			SecurityException, IllegalArgumentException, IOException, NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		response.setHeader("content-disposition", "attachment;filename=\"" + new String(fileName.getBytes("utf-8"), "iso8859-1") + "\"");
		response.setContentType("APPLICATION/msexcel");
		OutputStream out = response.getOutputStream();
		getWorkbook().write(out);
		out.close();
	}

	/**
	 * @return 已填充数据的 excel
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public HSSFWorkbook getWorkbook() throws FileNotFoundException, IOException, SecurityException,
			NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(getTemplateUrl())));
		HSSFSheet sheet = workbook.getSheetAt(0);
		//样式
		HSSFCellStyle style = workbook.createCellStyle();
        style.setWrapText(true);// 自动换行
        style.setBorderBottom((short) 1);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//左右居中    
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		for (T valueObject : valueObjects) {
			HSSFRow row = sheet.createRow((short) initRow.intValue());
			int currentColumn = initColumn;
			for (String methodName : methodNames) {
				Method method = valueObject.getClass().getMethod(methodName);
				Object value = method.invoke(valueObject);
				HSSFCell cell = row.createCell((short) currentColumn);
				cell.setCellStyle(style);// 样式
				setCellValue(cell, value);
				// cell.setCellValue(String.valueOf(value));
				currentColumn++;
			}
			initRow++;
		}
		return workbook;
	}

	/**
	 * 根据value的值设置单元格的类型
	 * 
	 * @param cell
	 * @param value
	 */
	private void setCellValue(HSSFCell cell, Object value) {
		if(value == null){
			return;
		}
		cell.setEncoding(HSSFWorkbook.ENCODING_UTF_16);
		if (value instanceof Long || value instanceof Integer || value instanceof Double) {
			cell.setCellValue(Double.valueOf(String.valueOf(value)));
		} else {
			cell.setCellValue(String.valueOf(value));
		}
	}

	/**
	 * 设放模版的URL
	 * 
	 * @param templateUrl
	 */
	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}

	/**
	 * 填入excel数据的初始化位置
	 * 
	 * @param row行
	 * @param column列
	 */
	public void setInitPosition(Integer row, Integer column) {
		if (0 > row || 0 > column)
			throw new IllegalArgumentException();
		this.initColumn = column;
		this.initRow = row;
	}

	/**
	 * 放入方法名，方法名的顺序与输出顺序一至
	 * 
	 * @param methodName
	 */
	public void setMethodName(String[] methodName) {
		this.methodNames = methodName;
	}

	/**
	 * 填入excel的值对像集合
	 * 
	 * @param valueObjects
	 */
	public void setValueObject(List<T> valueObject) {
		this.valueObjects = valueObject;
	}
}
