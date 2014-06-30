/**
 * 
 */
package cn.qtone.qtoneframework.web.view.poi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * @author 林子龙
 * 
 */
public class ExcelExtractor<T> {
	/**
	 * 被导入的excel文件
	 */
	private HSSFWorkbook workbook;
	/**
	 * 被导入的excel
	 */
	private HSSFSheet sheet;
	/**
	 * 初始行
	 */
	private Integer initRow;
	/**
	 * 初始列
	 */
	private Integer initColumn;
	/**
	 * T对像的设置值的方法
	 */
	private String[] methodNames;

	/**
	 * 放错误信息的行
	 */
	private Integer errorMsgColumn;

	/**
	 * 与T对应的Class对像
	 */
	private Class<T> valueType;

	/**
	 * 全部数据通过验证，表示成功。<br>
	 * 有数据验证不能过，反馈信息将被写入excel，导入失败。
	 */
	private Boolean success = true;

	/**
	 * 错误信息
	 */
	private List<String> errorMsg = new ArrayList<String>();
	
	public Boolean isSuccess() {
		return success;
	}

	public void setValueType(Class<T> valueType) {
		this.valueType = valueType;
	}

	public void setMethodNames(String[] methodNames) {
		this.methodNames = methodNames;
	}

	public void setExcel(String url) throws FileNotFoundException, IOException {
		this.workbook = ExcelUtils.loadWorkbook(url);
		this.sheet = workbook.getSheetAt(0);
	}

	public void setInitPosition(Integer initRow, Integer initColumn) {
		this.initRow = initRow;
		this.initColumn = initColumn;
	}

	public void setErrorMsgColumn(Integer errorMsgColumn) {
		this.errorMsgColumn = errorMsgColumn;
	}

	public HSSFWorkbook getFeedbackWorkbook() {
		return this.workbook;
	}

	/**
	 * @return 值对像
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 */
	private T getValueObjectInstance() throws InstantiationException, IllegalAccessException {
		return (T) valueType.newInstance();
	}

	/**
	 * 返回第一个和输入方法名相同的方法
	 * 
	 * @param methodName
	 *            方法名
	 * @return 方法
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private Method getMethodByMethodName(String methodName) throws SecurityException, IllegalArgumentException,
			InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		for (Method method : valueType.getMethods()) {
			if (method.getName().equals(methodName))
				return method;
		}
		throw new NoSuchMethodException();
	}

	/**
	 * 提取单元格的值，并放到值对像对应的属性上。
	 * 
	 * @param t
	 * @param method
	 * @param cell
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	/*private Object extractCellValue(Method method, HSSFCell cell) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		if (method.getParameterTypes()[0].equals(String.class)) {
			return cell.getStringCellValue();
		} else if (method.getParameterTypes()[0].equals(Date.class)) {
			return cell.getDateCellValue();
		} else {
			if (method.getParameterTypes()[0].equals(Integer.class) || method.getParameterTypes()[0].equals(int.class)) {
				return ((Double) cell.getNumericCellValue()).intValue();
			} else if (method.getParameterTypes()[0].equals(Long.class)) {
				return ((Double) cell.getNumericCellValue()).longValue();
			} else if (method.getParameterTypes()[0].equals(Double.class)) {
				return cell.getNumericCellValue();
			} else {
				throw new IllegalArgumentException("目前的ExcelExractor不支持" + method.getParameterTypes()[0].getName()
						+ "类型");
			}
		}
	}*/
	private Object extractCellValue(Method method, HSSFCell cell) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		if (method.getParameterTypes()[0].equals(String.class)) {
			return cell.getCellType() == 0 ? ((Double)cell.getNumericCellValue()).longValue() + "" : cell.getStringCellValue();
		} else if (method.getParameterTypes()[0].equals(Date.class)) {
			return cell.getDateCellValue();
		} else {
			if (method.getParameterTypes()[0].equals(Integer.class) || method.getParameterTypes()[0].equals(int.class)) {
				return ((Double) (cell.getCellType() == 0 ? cell.getNumericCellValue() : Double.parseDouble(cell.getStringCellValue()))).intValue();
			} else if (method.getParameterTypes()[0].equals(Long.class)) {
				return ((Double) (cell.getCellType() == 0 ? cell.getNumericCellValue() : Double.parseDouble(cell.getStringCellValue()))).longValue();
			} else if (method.getParameterTypes()[0].equals(Double.class)) {
				return (Double)(cell.getCellType() == 0 ? cell.getNumericCellValue() : Double.parseDouble(cell.getStringCellValue()));
			} else {
				throw new IllegalArgumentException("目前的ExcelExractor不支持" + method.getParameterTypes()[0].getName()
						+ "类型");
			}
		}
	}

	/**
	 * @param method
	 *            被验证的方法
	 * @param value
	 *            被验证的值
	 * @return 若验证通过，返回null，不通过，返回错误的提示信息
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings( { "unused", "unchecked", "unchecked" })
	private Object validateValue(Method method, Object value) throws InstantiationException, IllegalAccessException,
			SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		if (!method.isAnnotationPresent(ValidateMethod.class)) {
			return null;
		}
		Class objectValidator = method.getDeclaringClass().getAnnotation(ValidateClass.class).value();
		Class specialValidator = method.getAnnotation(ValidateMethod.class).validateClass();
		String validateMethodName = method.getAnnotation(ValidateMethod.class).validateMethodName();
		if (NoSpecialClass.class.equals(specialValidator)) {
			return objectValidator.getMethod(validateMethodName, method.getParameterTypes()[0]).invoke(
					objectValidator.newInstance(), value);
		} else {
			return specialValidator.getMethod(validateMethodName, method.getParameterTypes()[0]).invoke(
					specialValidator.newInstance(), value);
		}
	}

	/**
	 * 调用验查方法，看值是否有效，若无效，把错误信息写在原来的excel上。<br>
	 * 错误信息的位置以errorMsgColumn和当前行来定位。
	 * 
	 * @param method
	 *            被检查的方法
	 * @param value
	 *            被检查的值
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	private void feedbackMsgToExcel(Method method, Object value) throws SecurityException, IllegalArgumentException,
			InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		if (null != validateValue(method, value)) {
			success = false;
			ExcelUtils.setValue(sheet, initRow.intValue(), errorMsgColumn.intValue(), (String) validateValue(method,
					value), ExcelUtils.createFCStyle(workbook, new HSSFColor.RED().getIndex()));
			errorMsg.add("第" + (initRow + 1) + "行，" + validateValue(method, value));
		}
	}

	public List<T> getResult() throws SecurityException, IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		List<T> result = new LinkedList<T>();
		while (true) {
			int currentColumn = this.initColumn;
			boolean isBlankLine = true;
			T valueObject = getValueObjectInstance();
			for (String methodName : methodNames) {
				Method method = getMethodByMethodName(methodName);
				try {
					Object value = extractCellValue(method, sheet.getRow(initRow).getCell((short) currentColumn));
					isBlankLine = false;
					feedbackMsgToExcel(method, value);
					method.invoke(valueObject, value);
				} catch (NullPointerException npe) {
					// 对应该值的单元格为空
				}
				currentColumn++;
			}
			initRow++;
			if (isBlankLine) {
				break;
			} else {
				result.add(valueObject);
			}
		}
		return result;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public List<String> getErrorMsg() {
		return errorMsg;
	}
}
