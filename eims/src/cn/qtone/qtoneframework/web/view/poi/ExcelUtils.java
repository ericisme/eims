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

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * 操作poi excel的工具类
 * 
 * @author 林子龙
 * 
 */
public class ExcelUtils {
	/**
	 * 把excel保存到磁盘
	 * 
	 * @param url
	 * @param hw
	 * @throws IOException
	 */
	public static void saveToHarddisk(String url, HSSFWorkbook workbook) throws IOException {
		BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(url));
		workbook.write(bo);
		bo.close();
	}

	/**
	 * 从url读出excel
	 * 
	 * @param url
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static HSSFWorkbook loadWorkbook(String url) throws FileNotFoundException, IOException {
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(url));
		return new HSSFWorkbook(fs);
	}

	/**
	 * 为单元格设值
	 * 
	 * @param hs
	 * @param style
	 * @param row
	 * @param col
	 * @param value
	 */
	public static void setValue(HSSFSheet hs, int row, int col, String value, HSSFCellStyle... style) {
		HSSFRow hr = hs.getRow(row) == null ? hs.createRow(row) : hs.getRow(row);
//		HSSFCell hc = hs.createRow(row).createCell((short) col);
		HSSFCell hc = hr.getCell((short)col) == null ? hr.createCell((short)col) : hr.getCell((short)col);
		hc.setEncoding(HSSFCell.ENCODING_UTF_16);
		if (null != style && style.length > 0) {
			hc.setCellStyle(style[0]);
		}
		if(null!=style&&style.length>1){
			throw new RuntimeException("只能输入一种样式");
		}
//		try {
//			hc.setCellValue(Double.parseDouble(value));
//		} catch (Exception e) {
//			hc.setCellValue(value);
//		}
		hc.setCellValue(value);
	}
	
	/**
	 * 为单元格设值，同时给单元格添加边框。
	 * 
	 * @param hs
	 * @param style
	 * @param row
	 * @param col
	 * @param value
	 */
	public static void setValueWithBorder(HSSFWorkbook wb, HSSFSheet hs, int row, int col, String value, HSSFCellStyle... style) {
		HSSFRow hr = hs.getRow(row) == null ? hs.createRow(row) : hs.getRow(row);
		HSSFCell hc = hr.getCell((short)col) == null ? hr.createCell((short)col) : hr.getCell((short)col);
		hc.setEncoding(HSSFCell.ENCODING_UTF_16);
		HSSFCellStyle cs;
		if (null != style && style.length > 0) {
			cs = style[0];
		}else{
			cs = wb.createCellStyle();
		}
		setCsBorder(cs);
		hc.setCellStyle(cs);
//		try {
//			hc.setCellValue(Double.parseDouble(value));
//		} catch (Exception e) {
			hc.setCellValue(value);
//		}
	}

	public static void setCsBorder(HSSFCellStyle cs) {
		cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
	}
	
	public static void setCellValue(HSSFCell cell, Object value) {
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

	/**创建样式
	 * @param workbook
	 * @param color
	 * @return
	 */
	public static HSSFCellStyle createFCStyle(HSSFWorkbook workbook, short color) {
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont fort = workbook.createFont();
		fort.setColor(color);
		style.setFont(fort);
		return style;
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
	public static void download(HttpServletResponse response, HSSFWorkbook workbook, String fileName) throws FileNotFoundException,
			SecurityException, IllegalArgumentException, IOException, NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		response.setHeader("content-disposition", "attachment;filename="+java.net.URLEncoder.encode(fileName, "utf-8"));
		response.setContentType("APPLICATION/msexcel");
		OutputStream out = response.getOutputStream();
		workbook.write(out);
		out.close();
	}
	
	/**
	 * 合并单元格
	 * @param sheet
	 * @param startRow 开始单元格所在行
	 * @param startCol 开始单元格所在列
	 * @param endRow 结束单元格所在行
	 * @param endCol 结束单元格所在列
	 */
	public static void mergeRegion(HSSFSheet sheet, int startRow, short startCol, int endRow, short endCol, HSSFCellStyle cs){
		Region region = new Region(startRow, startCol, endRow, endCol);
		sheet.addMergedRegion(region);
		if(cs != null){
			setRegionStyle(sheet, region, cs);
		}
	}
	
	private static void setRegionStyle(HSSFSheet sheet, Region region , HSSFCellStyle cs) {
        for (int i = region.getRowFrom(); i <= region.getRowTo(); i ++) {
            HSSFRow row = sheet.getRow(i) == null? sheet.createRow(i) : sheet.getRow(i);
            for (int j = region.getColumnFrom(); j <= region.getColumnTo(); j++) {
                HSSFCell cell = row.getCell((short)j) == null? row.createCell((short)j) : row.getCell((short)j);
                cell.setCellStyle(cs);
            }
        }
	}
	
	/**
	 * 拷贝excel区域
	 * @param wb
	 * @param pSourceSheetName 源sheet
	 * @param pTargetSheetName 目标sheet
	 * @param pStartRow 开始行
	 * @param pEndRow 结束行
	 * @param pPosition 目标sheet的开始行
	 */
	public static void copyRows(HSSFWorkbook wb, String pSourceSheetName, String pTargetSheetName, int pStartRow,
			int pEndRow, int pPosition) {
		HSSFRow sourceRow = null;
		HSSFRow targetRow = null;
		HSSFCell sourceCell = null;
		HSSFCell targetCell = null;
		HSSFSheet sourceSheet = null;
		HSSFSheet targetSheet = null;
		Region region = null;
		int cType;
		int i;
		short j;
		int targetRowFrom;
		int targetRowTo;

		if ((pStartRow == -1) || (pEndRow == -1)) {
			return;
		}
		sourceSheet = wb.getSheet(pSourceSheetName);
		targetSheet = wb.getSheet(pTargetSheetName);
		// 拷贝合并的单元格
		for (i = 0; i < sourceSheet.getNumMergedRegions(); i++) {
			region = sourceSheet.getMergedRegionAt(i);
			if ((region.getRowFrom() >= pStartRow) && (region.getRowTo() <= pEndRow)) {
				targetRowFrom = region.getRowFrom() - pStartRow + pPosition;
				targetRowTo = region.getRowTo() - pStartRow + pPosition;
				region.setRowFrom(targetRowFrom);
				region.setRowTo(targetRowTo);
				targetSheet.addMergedRegion(region);
			}
		}
		// 设置列宽
		for (i = pStartRow; i <= pEndRow; i++) {
			sourceRow = sourceSheet.getRow(i);
			if (sourceRow != null) {
				for (j = sourceRow.getLastCellNum(); j >= sourceRow.getFirstCellNum(); j--) {
					targetSheet.setColumnWidth(j, sourceSheet.getColumnWidth(j));
					targetSheet.setColumnHidden(j, false);
				}
				break;
			}
		}
		// 拷贝行并填充数据
		for (; i <= pEndRow; i++) {
			sourceRow = sourceSheet.getRow(i);
			if (sourceRow == null) {
				continue;
			}
			targetRow = targetSheet.createRow(i - pStartRow + pPosition);
			targetRow.setHeight(sourceRow.getHeight());
			for (j = sourceRow.getFirstCellNum(); j < sourceRow.getPhysicalNumberOfCells(); j++) {
				sourceCell = sourceRow.getCell(j);
				if (sourceCell == null) {
					continue;
				}
				targetCell = targetRow.createCell(j);
				targetCell.setEncoding(sourceCell.getEncoding());
				targetCell.setCellStyle(sourceCell.getCellStyle());
				cType = sourceCell.getCellType();
				targetCell.setCellType(cType);
				switch (cType) {
				case HSSFCell.CELL_TYPE_BOOLEAN:
					targetCell.setCellValue(sourceCell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_ERROR:
					targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA:
					targetCell.setCellFormula(parseFormula(sourceCell.getCellFormula()));
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					targetCell.setCellValue(sourceCell.getNumericCellValue());
					break;
				case HSSFCell.CELL_TYPE_STRING:
					targetCell.setCellValue(sourceCell.getRichStringCellValue());
					break;
				}
			}
		}
	}

	private static String parseFormula(String pPOIFormula) {
		final String cstReplaceString = "ATTR(semiVolatile)"; //$NON-NLS-1$
		StringBuffer result = null;
		int index;

		result = new StringBuffer();
		index = pPOIFormula.indexOf(cstReplaceString);
		if (index >= 0) {
			result.append(pPOIFormula.substring(0, index));
			result.append(pPOIFormula.substring(index + cstReplaceString.length()));
		} else {
			result.append(pPOIFormula);
		}

		return result.toString();
	}
	
	public static void main(String[] args) {
		  try {
		   POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(
		     "d:\\test.xls"));
		   HSSFWorkbook wb = new HSSFWorkbook(fs);

		//source ,target 为,源sheet 页和目标sheet页,
//		   copyRows(wb, "Sheet1", "Sheet2", 14, 23, 0);
		   wb.cloneSheet(0);
		   wb.setSheetName(2, "Sheet1");
		   FileOutputStream fileOut = new FileOutputStream("d:\\test333.xls");
		   wb.write(fileOut);
		   fileOut.flush();
		   fileOut.close();
		   System.out.println("Operation finished");
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
		 }


}
