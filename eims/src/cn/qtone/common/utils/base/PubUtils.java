package cn.qtone.common.utils.base;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 实用方法类
 * 
 * @author 马必强
 * 
 */
public class PubUtils {

	/**
	 * 设置翻页，取当前的页码
	 * 
	 * @param yePage
	 *            页面传过来的page
	 * @param totalRows
	 *            总记录数
	 * @param perPage
	 *            每一页显示的记录数
	 */
	public static int getCurrentPage(String yePage, int totalRows, int perPage) {
		String curPage = yePage; // 取页号的参数值

		// 为空则是表示第一页
		if (curPage == null) {
			return 1;
		}

		// 不为空则判断是哪一页
		int page = 0;
		int totalPages = 0;
		totalPages = totalRows / perPage + (totalRows % perPage == 0 ? 0 : 1);
		try {
			page = Integer.parseInt(curPage);
			if (page <= 0) {
				page = 1;
			} else if (page > totalPages) {
				page = totalPages;
			}
		} catch (NumberFormatException ex) {
			page = 1;
		}
		if(page < 1){
			page = 1;
		}
		return page;
	}
	
	/**
	 * java过滤html标签函数
	 * @param inputString 含html标签的字符串 
	 * @return
	 */
	public static String HtmlText(String inputString) {
		String htmlStr = inputString;
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
																										// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
																									// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("HtmlText: " + e.getMessage());
		}

		return textStr;// 返回文本字符串
	} 
	
	/**
	 *  该函数获得随机数字符串   
	 * @param iLen:需要获得随机数的长度   
	 * @param iType:随机数的类型：'0':表示仅获得数字随机数；'1'：表示仅获得字符随机数；'2'：表示获得数字字符混合随机数   
	 * @return
	 */
	 public static final String CreateRadom(int iLen, int iType) {
		StringBuffer strRandom = new StringBuffer();//随机字符串   
		Random rnd = new Random();
		if (iLen < 0) {
			iLen = 5;
		}
		if ((iType > 2) || (iType < 0)) {
			iType = 2;
		}
		switch (iType) {
		case 0:
			for (int iLoop = 0; iLoop < iLen; iLoop++) {
				strRandom.append(Integer.toString(rnd.nextInt(10)));
			}
			break;
		case 1:
			for (int iLoop = 0; iLoop < iLen; iLoop++) {
				strRandom.append(Integer.toString((35 - rnd.nextInt(10)), 36));
			}
			break;
		case 2:
			for (int iLoop = 0; iLoop < iLen; iLoop++) {
				strRandom.append(Integer.toString(rnd.nextInt(36), 36));
			}
			break;
		}
		return strRandom.toString();
	}
	 
	 /**
	     * 获取两日期之间的每周的起始日期(Select下拉菜单)[阳历周,即以星期日为一周开始,星期六为一周结束]
	     * @param startDate_ 开始日期
	     * @param endDate_结束日期
	     * @param pattern 格式化样式,如：yyyy-MM-dd 要与startDate_、endDate_格式一致
	     * @return
	     */
	     public static String computeTwoDayWeekYL(String startDate_, String endDate_,String pattern){   
	     	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	 		Date startDate = new Date();
	 		Date endDate =new Date();
	 		try {
	 			startDate = sdf.parse(startDate_);
	 			endDate =sdf.parse(endDate_);
	 		} catch (ParseException e) {
	 			return "";
	 		}		       
	         Calendar beginCalendar = Calendar.getInstance();   
	         beginCalendar.setTime(startDate);   
	   
	         Calendar endCalendar = Calendar.getInstance();   
	         endCalendar.setTime(endDate);   
	         
	         StringBuffer strBuf=new StringBuffer();
	 		
	         //周日的日期       
	         String weekOfFirstDay = getWeekday(sdf.format(beginCalendar.getTime()),1,pattern);
	         //周六的日期
	         String weekOfEndDay = getWeekday(sdf.format(beginCalendar.getTime()),7,pattern);
	         
	         int weeks = 0;          
	         while (beginCalendar.before(endCalendar)) {   
	   
	             // 如果开始日期和结束日期在同年、同月且当前月的同一周时结束循环   
	             if (beginCalendar.get(Calendar.YEAR) == endCalendar   
	                     .get(Calendar.YEAR)   
	                     && beginCalendar.get(Calendar.MONTH) == endCalendar   
	                             .get(Calendar.MONTH)   
	                     && beginCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) == endCalendar   
	                             .get(Calendar.DAY_OF_WEEK_IN_MONTH)) {   
	                 break;   
	   
	             } else { 
	             	strBuf.append("<option value=\""+getDateByOperation(weekOfFirstDay,0,0,7*weeks,pattern)+","+getDateByOperation(weekOfEndDay,0,0,7*weeks,pattern)+"\">");
	             	strBuf.append("第"+(weeks+1)+"周");
	             	strBuf.append("</option>");         
	                beginCalendar.add(Calendar.DAY_OF_YEAR, 7);   
	                weeks += 1;   
	             }   
	         }   
	   
	        return strBuf.toString();
	     }  
	     
	     /**
	      * 获取两日期之间的每周的起始日期(Select下拉菜单)[农历周,即以星期一为一周开始,星期日为一周结束]
	      * @param startDate_ 开始日期
	      * @param endDate_结束日期
	      * @param pattern 格式化样式,如：yyyy-MM-dd 要与startDate_、endDate_格式一致
	      * @return
	      */
	      public static String computeTwoDayWeek(String startDate_, String endDate_,String pattern){   
	      	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	  		Date startDate = new Date();
	  		Date endDate =new Date();
	  		try {
	  			startDate = sdf.parse(startDate_);
	  			endDate =sdf.parse(endDate_);
	  		} catch (ParseException e) {
	  			return "";
	  		}		       
	          Calendar beginCalendar = Calendar.getInstance();   
	          beginCalendar.setTime(startDate);   
	    
	          Calendar endCalendar = Calendar.getInstance();   
	          endCalendar.setTime(endDate);   
	          
	          StringBuffer strBuf=new StringBuffer();
	  		
	          String weekOfFirstDay = "";
	          String weekOfEndDay = "";
	          if(beginCalendar.get(Calendar.DAY_OF_WEEK)==1){        	
	          	//周一的日期 
	          	weekOfFirstDay = getWeekday(getDateByOperation(sdf.format(beginCalendar.getTime()),0,0,-7,pattern),2,pattern);
	          	weekOfEndDay = getWeekday(sdf.format(beginCalendar.getTime()),1,pattern);
	          }else{
	              //周日的日期
	          	weekOfFirstDay = getWeekday(sdf.format(beginCalendar.getTime()),2,pattern);
	          	weekOfEndDay = getWeekday(getDateByOperation(sdf.format(beginCalendar.getTime()),0,0,7,pattern),1,pattern);
	          }
	                   
	          int weeks = 0;      
	          
	          String curdate=getFmtSystime(pattern);//当前时间
	          String sel="";
	          
	          while (beginCalendar.before(endCalendar)) {   
	    
	              // 如果开始日期和结束日期在同年、同月且当前月的同一周时结束循环   
	              if (beginCalendar.get(Calendar.YEAR) == endCalendar   
	                      .get(Calendar.YEAR)   
	                      && beginCalendar.get(Calendar.MONTH) == endCalendar   
	                              .get(Calendar.MONTH)   
	                      && beginCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) == endCalendar   
	                              .get(Calendar.DAY_OF_WEEK_IN_MONTH)) {   
	                  break;   
	    
	              } else { 
	              	String beginDate=getDateByOperation(weekOfFirstDay,0,0,7*weeks,pattern);
	              	String lastDate=getDateByOperation(weekOfEndDay,0,0,7*weeks,pattern);
	              	if(beginDate.compareTo(curdate)<=0&&lastDate.compareTo(curdate)>=0){
	              		sel="selected";
	              	}else{
	              		sel="";
	              	}
	              	strBuf.append("<option value=\""+beginDate+","+lastDate+"\" "+sel+" >");
	              	strBuf.append("第"+(weeks+1)+"周 ("+beginDate+"至"+lastDate+")");
	              	strBuf.append("</option>");   
	              	
	                  beginCalendar.add(Calendar.DAY_OF_YEAR, 7);   
	                  weeks += 1;   
	              }   
	          }   
	    
	         return strBuf.toString();
	      }  
	     
	     /**
	 	 * 根据基准日期获取任意运算后的日期
	 	 * @param strDate 基准日期
	 	 * @param intYear 运算年数
	 	 * @param intMonth 运算月数
	 	 * @param intDay 运算日数
	 	 * @param pattern 输出格式(如:yyyy-MM-dd)
	 	 * @return
	 	 */
	 	@SuppressWarnings("static-access")
	 	public final static String getDateByOperation(String strDate,int intYear,int intMonth,int intDay,String pattern) {
	 		SimpleDateFormat f = new SimpleDateFormat(pattern);
	 		Calendar cal = Calendar.getInstance();   
	 		try {
	 			f.parse(strDate);
	 		} catch (ParseException e){
	 			strDate = f.format(new Date());
	 			e.printStackTrace();
	 		}   
	         cal.setTime(f.parse((strDate),new ParsePosition(0)));  
	         if(intDay != 0)
	         	cal.add(cal.DATE,intDay);
	         if(intMonth != 0)
	         	cal.add(cal.MONTH,intMonth);
	         if(intYear != 0)
	         	cal.add(cal.YEAR,intYear);
	         return f.format(cal.getTime());    
	 	}
	 	
	 	 /**
		 * 获取基准日期所在周的星期X日期
		 * @param strDate 基准日期
		 * @param intWeekday 要返回的星期X(1至7表示：周日至周六)
	     * @param pattern 日期格式
	     * @return
	     */
	 	private final static String getWeekday(String strDate,int intWeekday,String pattern){
			SimpleDateFormat f = new SimpleDateFormat(pattern);
			Calendar cal = Calendar.getInstance();   
			try {
				f.parse(strDate);
			} catch (ParseException e){
				strDate = f.format(new Date());
				e.printStackTrace();
			}   
	        cal.setTime(f.parse((strDate),new ParsePosition(0)));  
	        cal.set(Calendar.DAY_OF_WEEK,intWeekday);
	        return f.format(cal.getTime());   
		}
	 	
	 	 /**
	     * 根据时间格式匹配返回当前时间 如yyyyMMdd 表示20060810
	     * @param pattern
	     * @return
	     */
	    public static String getFmtSystime(String pattern){
	        DateFormat dateFormat=new SimpleDateFormat(pattern);
	        String sTmp=dateFormat.format(new java.util.Date());
	        return sTmp;
	    }
	    
	    /**
		 * 輸出當前日期前或後N天的日期 
		 * @param pattern 输出的日期格式
		 * @param num 前或后N天的天数
		 * @return
		 */
		public static String getDistanceDay(String pattern,int num){
			Calendar c=java.util.Calendar.getInstance(); 
			c.add(Calendar.DATE,num); 
			DateFormat dateFormat = new SimpleDateFormat(pattern);
			String sTmp = dateFormat.format(c.getTime());
			return sTmp;		
		}
	    
		/**  
	     * 得到两个日期之间相差多少天   
	     * @param beginDate  开始日期  
	     * @param endDate    结束日期  
	     * @return  
	     */  
	    public static int dateDiff(String beginDate,String endDate){   
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
	        Date date = null;   
	        try {   
	            date = sdf.parse(endDate);   
	        } catch (Exception e) {   
	            date = new Date();   
	            e.printStackTrace();   
	        }   
	           
	           
	        long end    = date.getTime();   
	        try {   
	            date = sdf.parse(beginDate);   
	        } catch (Exception e) {   
	            date = new Date();   
	            e.printStackTrace();   
	        }   
	        long  begin     = date.getTime();   
	           
	        long day = (end-begin)/(1000*3600*24);      //除1000是把毫秒变成秒   
	           
	        return Integer.parseInt(Long.toString(day));           
	    }  
	    
	    /**
	     * 取得对象的字符串表示
	     * @param obj
	     * @return
	     */
	    public static String valueOf(Object obj) {
	        return (obj == null || "null".equals(obj)) ? "" : String.valueOf(obj);
	    }
	   
	    
	    /**
		 * 判断是否是电信手机号码
		 * @param mobile
		 * @return
		 */
		public static boolean isDianxinMobile(String mobile) {
			if (mobile == null || "".equals(mobile) || (mobile.startsWith("0760")&&mobile.length()!=12)) {
				return false;
			}
			boolean flag = false;
			String frontMobile = mobile.substring(0, 3);
			if ((frontMobile.equals("133"))  || (frontMobile.equals("153")) || (frontMobile.equals("189"))||(mobile.startsWith("0760")&&mobile.length()==12)) {
				flag = true;
			} else {
				flag = false;
			}
			return flag;
		}
		

		/**
		 * 判断是否是联通手机号码
		 * @param mobile
		 * @return
		 */
		public static boolean isLiantongMobile(String mobile) {
			if (mobile == null || "".equals(mobile) || mobile.length() != 11) {
				return false;
			}
			boolean flag = false;
			String frontMobile = mobile.substring(0, 3);
			if ((frontMobile.equals("130")) || (frontMobile.equals("131")) || (frontMobile.equals("132"))
					 || (frontMobile.equals("156")) || (frontMobile.equals("186"))) {
				flag = true;
			} else {
				flag = false;
			}
			return flag;
		}
		
		/**
		 * 判断是否是移动手机号码
		 * @param mobile
		 * @return
		 */
		public static boolean isYidongMobile(String mobile) {
			if (mobile == null || "".equals(mobile) || mobile.length() != 11) {
				return false;
			}
			boolean flag = false;

			String frontMobile = mobile.substring(0, 3);
			if ((frontMobile.equals("134")) || (frontMobile.equals("135")) || (frontMobile.equals("136"))
					|| (frontMobile.equals("137")) || (frontMobile.equals("138")) || (frontMobile.equals("139"))
					|| (frontMobile.equals("150")) || (frontMobile.equals("151")) || (frontMobile.equals("152"))
					|| (frontMobile.equals("158")) || (frontMobile.equals("159")) || (frontMobile.equals("188"))) {
				flag = true;
			} else {
				flag = false;
			}
			return flag;
		} 

		
	  
}
