package cn.qtone.crda;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 信息平台-学生成长袋 约定的系统常量.<br>
 * 
 * @author 贺少辉.<br />
 *         create 2010-4-19.<br />
 */
public class CrdaContants {
	
	/**
	 * 成人档案袋的默认扣费金额
	 */
	public static final Integer CHARGEFEES = 5;
	
	/**
	 * 扣费状态
	 */
	public static Map<String, String> SUBSCRIBECHARGEBACK = new LinkedHashMap<String, String>();
	static {
		SUBSCRIBECHARGEBACK.put("1", "扣费成功");
		SUBSCRIBECHARGEBACK.put("0", "未扣费");
		SUBSCRIBECHARGEBACK.put("-1", "扣费失败");
	}
	
	/**
	 * 订单状态
	 */
	public static Map<String, String> SUBSCRIBESTATUS = new LinkedHashMap<String, String>();
	static {
		SUBSCRIBESTATUS.put("1", "正常");
		SUBSCRIBESTATUS.put("0", "取消");
	}
	
	/**
	 * 订单生成扣费记录状态
	 */
	public static Map<String, String> SUBSCRIBE_TO_CHARGEBACK_STATUS = new LinkedHashMap<String, String>();
	static {
		SUBSCRIBE_TO_CHARGEBACK_STATUS.put("0", "未生成");
		SUBSCRIBE_TO_CHARGEBACK_STATUS.put("1", "已生成");
	}
	
	/**
	 * 扣费记录导出状态
	 */
	public static Map<String, String> CHARGEBACK_EXPORT_STATUS = new LinkedHashMap<String, String>();
	static {
		CHARGEBACK_EXPORT_STATUS.put("0", "未导出");
		CHARGEBACK_EXPORT_STATUS.put("1", "已导出");
	}
	
	/**
	 * 获取年份的下拉选择框
	 */
	public static String getYearOptions() {
		int now = Calendar.getInstance().get(Calendar.YEAR);
		StringBuilder sb = new StringBuilder();
		for (int i = now - 5; i <= now; i++) {
			if (i > 2009) {
				sb.append("<option value='").append(i).append("'");
				if (i == now) {
					sb.append(" selected");
				}
				sb.append(">").append(i).append("</option>");
			}
		}
		return sb.toString();
	}

	/**
	 * 获取月份的下拉选择框
	 */
	public static String getMonthOptions() {
		int now = Calendar.getInstance().get(Calendar.MONTH);
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < 13; i++) {
			sb.append("<option value='").append(i).append("'");
			if (i == now + 1) {
				sb.append(" selected");
			}
			sb.append(">").append(i).append("</option>");
		}
		return sb.toString();
	}
}