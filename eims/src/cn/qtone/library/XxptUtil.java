package cn.qtone.library;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XxptUtil {


	/**
	 * 判断手机号码类型, 并返回类型值 1移动 0其它
	 * @param mobile
	 * @return 
	 */
	public static Integer getMobileType(String mobile) {
		
		Integer mobileType = 0;
		if (mobile != null && mobile.length() > 0) {
			
			String str = mobile.substring(0, 3);
			if (str.indexOf("134") ==0 || str.indexOf("135") ==0 || str.indexOf("136") ==0 ||
					str.indexOf("137") ==0 || str.indexOf("138") ==0 || str.indexOf("139") ==0 ||
					str.indexOf("150") ==0|| str.indexOf("151") ==0 || str.indexOf("152") ==0 ||
					str.indexOf("158") ==0 || str.indexOf("159")==0|| str.indexOf("187") ==0 ||
					str.indexOf("188")==0) {
				
				mobileType = 1;
			}
		}
		
		return mobileType;
	}
	public static void main(String... args){
		/*String sn =  new XxptUtil().getBookNumToSn("00000015");
		System.out.println(sn);*/
	}
	//zs00900000001
	//00000001
	public static String getBookSnToNum(String sn){
		if(sn.indexOf("0")==0){
			sn = sn.substring(1,sn.length());
			sn = getBookSnToNum(sn);
		}
		return sn;
			
	}
	public static String getBookNumToSn(String sn){
		if(sn==null)return "00000001";
		
		Integer nu = Integer.parseInt(getBookSnToNum(sn));
		nu++;
		StringBuilder sb = new StringBuilder();
		String Strsn = String.valueOf(nu);
		int i = 8 -Strsn.length();
		for(;i>0;i--){
			sb.append("0");
		}
		sb.append(Strsn);
		return sb.toString();
			
	}
	
	public static Integer[] getIntFromStr(String[] str){
		if(str==null||str.length<1)return new Integer[0];
		int len = str.length;
		Integer[] re = new Integer[len];
		for(int i=0;i<len;i++){
			re[i]=Integer.parseInt(str[i]);
		}
		return re;
	}
	
	//字符串数组去除重复
    public static String[] array_unique(String[] a) {
		List<String> list = new LinkedList<String>();

		for (int i = 0; i < a.length; i++) {
			if (!list.contains(a[i])) {

				list.add(a[i]);
			}
		}
		return (String[]) list.toArray(new String[list.size()]);

	}  
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static String getReturnDate(Date boroow_time,Integer limit_day){
	    Calendar calendar = Calendar.getInstance();
		calendar.setTime(boroow_time);
		calendar.add(Calendar.DAY_OF_MONTH,limit_day);
		return sdf.format(calendar.getTime());
    }
    
    public static long expiredDay(Date boroow_time){
    	
    	long milis=new Date().getTime() -boroow_time.getTime() ;
		milis=milis/1000/60/60/24; 
		return milis; 
    }
    
    public static void download(String filePath,String fileName,HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
	response.setContentType("text/html;charset=utf-8");
	response.setCharacterEncoding("UTF-8");
	request.setCharacterEncoding("UTF-8");
	try {
		File file = new File(filePath);
		if(file.isFile()){
			InputStream fis = null;
			OutputStream out = null;
			try {
				int len = 0;
				byte[] buf = new byte[1024];
	            fis = new BufferedInputStream(new FileInputStream(filePath));
	            response.reset();
	            // ����response��Header
	            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(),"iso-8859-1"));
	            response.addHeader("Content-Length", "" + file.length());
	            out = response.getOutputStream();
		        while ((len = fis.read(buf)) > 0)
		            out.write(buf, 0, len);
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }finally{
	        	if(fis!=null)
	        		fis.close();
	        	if(out!=null)
	        		out.close();
	        }
	        
		}else{
			PrintWriter out = response.getWriter();
			out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
			out.println("<HTML>");
			out.println("  <HEAD><TITLE>�ļ�����</TITLE></HEAD>");
			out.println("  <BODY>");
			out.println("<script>");
			out.println("alert('下载出错');");
			out.println("window.history.back();");
			out.println("</script>");
			out.println("  </BODY>");
			out.println("</HTML>");
			out.flush();
			out.close();
		}
	} catch (Exception e) {
		//e.printStackTrace();   
	}
	}
    
}	

