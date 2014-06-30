package cn.qtone.library.sendnews;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import cn.qtone.library.XxptUtil;

/**
 * 短信发送类.<br/>
 * 
 * @author 贺少辉<br>
 */
public class NewsSendUtil {

	public static String sendMessage(String mobile, String content) {
		String sendResult = "";
		try {
			// 移动的号码用校讯通、其它号码用短信猫
			if (XxptUtil.getMobileType(mobile) == 1) {
				System.out.println(mobile+"校讯通");
				sendResult = sendMobileMsgByComBackMsg(mobile, content, "zsoasend", CreateRadom(8, 0));
				System.out.println(sendResult);
			} else {
				System.out.println(mobile+"短信猫");
				sendResult = sendMobileMsgByModemBackMsg(mobile, content, "jgxxptsend_" + CreateRadom(8, 0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 保留发送成功的id，一次更新 保存发送不成功的号码，并发送信息到自己手机上
		return sendResult;//sendResult
	}

	public static final String CreateRadom(int iLen, int iType) {
		StringBuffer strRandom = new StringBuffer();// 随机字符串
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
    //朱老板发   
	// @SuppressWarnings("finally")
	// public static boolean sendBySmsModem(String phone,String content){
	// String msg = "OK";
	// boolean returnValue=true;
	// try {
	// String validate="jxjy_"+System.currentTimeMillis();
	// String
	// myurl="http://219.128.51.238:8090/sendsms.do?phone="+phone+"&content="+content+"&validate="+validate;
	// URL url = new URL(myurl.replace(" ", "%20"));
	// URLConnection con = url.openConnection();
	// con.setRequestProperty("User-Agent",
	// "Mozilla/4.0(compatible;MSIE7.0;windows NT 5)");
	// con.setRequestProperty("Content-Type", "text/html");
	// con.setConnectTimeout(7200);
	// con.connect();
	// BufferedReader bufReader = new BufferedReader(new
	// InputStreamReader(url.openStream(), "UTF-8"));
	// msg = bufReader.readLine();
	// bufReader.close();
	// } catch (Exception e) {
	// //System.out.println(msg+"发送失败");
	// returnValue=false;
	// e.printStackTrace();
	// }finally{
	// return returnValue;
	// }
	// }
	// 短信猫
	private final static String HTTP_MODEM = "http://219.128.51.238:8090/sendsms.do?1";

	// 移动正在使用端口
	private final static String HTTP_COM = "http://edu.gd.chinamobile.com/zsoasendsms?1";

	/**
	 * HTTP请求发送操作
	 * 
	 * @param sendURL
	 * @param strValidate
	 * @param phoneNo
	 * @param sendContent
	 * @return
	 * @throws Exception
	 * @throws IOException
	 * @throws HttpException
	 */
	/**
	 * 校讯通短信接口(不带返回值)
	 */
	public final static void sendMobileMsgByCom(String phoneNos, String sendContent, String validate) throws Exception {
		sendMobileMsgByCom(phoneNos, sendContent, validate, "");
	}

	@SuppressWarnings("deprecation")
	public final static void sendMobileMsgByCom(String phoneNos, String sendContent, String validate, String strPort)
			throws Exception {
		PostMethod postMethod = null;
		try {
			phoneNos = getDelCommaString(phoneNos);
			strPort = strPort.length() >= 8 ? strPort.substring(0, 7) : strPort;

			postMethod = new UTF8PostMethod(HTTP_COM);
			// postMethod.setParameter("port", "1");
			postMethod.setParameter("port", "1" + strPort);
			postMethod.setParameter("phone", phoneNos);
			postMethod.setParameter("content", sendContent);
			postMethod.setParameter("validate", validate);
			postMethod.setParameter("isZS", "true");
			//
			HttpClient httpClient = new HttpClient();
			// 执行postMethod，发送短信
			httpClient.executeMethod(postMethod);
			httpClient.setConnectionTimeout(7200);
		} finally {
			if (postMethod != null)
				postMethod.recycle();
		}
	}

	/**
	 * 校讯通短信接口(带返回值)
	 */
	@SuppressWarnings("deprecation")
	public final static String sendMobileMsgByComBackMsg(String phoneNos, String sendContent, String validate,
			String strPort) throws Exception {
		String strBackMsg = "1";
		PostMethod postMethod = null;
		try {
			phoneNos = getDelCommaString(phoneNos);
			strPort = strPort.length() >= 8 ? strPort.substring(0, 7) : strPort;
			//
			postMethod = new UTF8PostMethod(HTTP_COM);
			// postMethod.setParameter("port", "1");
			postMethod.setParameter("port", "1" + strPort);
			postMethod.setParameter("phone", phoneNos);
			postMethod.setParameter("content", sendContent);
			postMethod.setParameter("validate", validate);
			postMethod.setParameter("isZS", "true");
			//
			HttpClient httpClient = new HttpClient();
			// 执行postMethod，发送短信
			httpClient.executeMethod(postMethod);
			httpClient.setConnectionTimeout(7200);

			// 读取发送回执内容
			InputStream resStream = postMethod.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(resStream));
			StringBuffer resBuffer = new StringBuffer();
			String resTemp = "";
			while ((resTemp = br.readLine()) != null) {
				resBuffer.append(resTemp);
			}
			strBackMsg = resBuffer.toString();

			/*
			 * byte[] responseBody = postMethod.getResponseBody(); strBackMsg =
			 * new String(responseBody);
			 */
		} finally {
			if (postMethod != null)
				postMethod.recycle();
		}
		return strBackMsg;
	}

	/**
	 * 短信猫(无返回状态)
	 */
	@SuppressWarnings("deprecation")
	public final static void sendMobileMsgByModem(String phoneNos, String sendContent, String validate)
			throws Exception {
		PostMethod postMethod = null;
		try {
			phoneNos = getDelCommaString(phoneNos);
			String sendURL = HTTP_MODEM;
			postMethod = new UTF8PostMethod(sendURL);
			postMethod.setParameter("phone", phoneNos);
			postMethod.setParameter("content", sendContent);
			postMethod.setParameter("validate", validate);
			HttpClient httpClient = new HttpClient();
			// 执行postMethod，发送短信
			httpClient.executeMethod(postMethod);
			httpClient.setConnectionTimeout(7200);
		} finally {
			if (postMethod != null)
				postMethod.recycle();
		}
	}

	/**
	 * 短信猫(带返回状态)
	 */
	@SuppressWarnings("deprecation")
	public final static String sendMobileMsgByModemBackMsg(String phoneNos, String sendContent, String validate)
			throws Exception {
		String strBackMsg = "1";
		PostMethod postMethod = null;
		try {
			phoneNos = getDelCommaString(phoneNos);
			String sendURL = HTTP_MODEM;
			postMethod = new UTF8PostMethod(sendURL);
			postMethod.setParameter("phone", phoneNos);
			postMethod.setParameter("content", sendContent);
			postMethod.setParameter("validate", validate);
			HttpClient httpClient = new HttpClient();
			// 执行postMethod，发送短信
			httpClient.executeMethod(postMethod);
			httpClient.setConnectionTimeout(7200);

			// 读取发送回执内容
			InputStream resStream = postMethod.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(resStream));
			StringBuffer resBuffer = new StringBuffer();
			String resTemp = "";
			while ((resTemp = br.readLine()) != null) {
				resBuffer.append(resTemp);
			}
			strBackMsg = resBuffer.toString();
			if (strBackMsg.equals("true"))
				strBackMsg = "0";
			else
				strBackMsg = "1";
			/*
			 * byte[] responseBody = postMethod.getResponseBody(); strBackMsg =
			 * new String(responseBody);
			 */
		} finally {
			if (postMethod != null)
				postMethod.recycle();
		}
		return strBackMsg;
	}

	/**
	 * 除乱码内置对象
	 */
	public static class UTF8PostMethod extends PostMethod {
		public UTF8PostMethod(String url) {
			super(url);
		}

		public String getRequestCharSet() {
			return "utf-8";
		}
	}

	/**
	 * 移动号段
	 * 
	 * @return
	 */
	public final static String getMobileNumPreYD() {
		return "|134|135|136|137|138|139|150|151|152|157|158|159|187|188|";
	}

	/**
	 * 联通号段
	 * 
	 * @return
	 */
	public final static String getMobileNumPreLT() {
		return "|130|131|132|155|156|185|186|";
	}

	/**
	 * 电信号段
	 * 
	 * @return
	 */
	public final static String getMobileNumPreDX() {
		return "|180|189|133|153|";
	}

	/**
	 * 判断是否为有效手机号
	 * 
	 * @param strMobileNumber
	 * @return
	 */
	public final static String isTrueMobileNumber(String strMobileNumber) {
		if (strMobileNumber.length() > 7 && strMobileNumber.length() < 11) {
			strMobileNumber = "0760" + strMobileNumber;
		}
		// 以1开头的11位号码
		if (strMobileNumber.length() == 11 && strMobileNumber.substring(0, 1).endsWith("1"))
			return strMobileNumber;
		// 以0开头的外地号码
		else if (strMobileNumber.length() == 12 && strMobileNumber.substring(0, 1).endsWith("0")
				&& strMobileNumber.substring(0, 2).endsWith("01"))
			return strMobileNumber.substring(1, 12);
		// 以0开头的12位号码[加区位的小灵通]
		else if (strMobileNumber.length() == 12 && strMobileNumber.substring(0, 1).endsWith("0"))
			return strMobileNumber;
		else
			return "";
		/*
		 * if(strMobileNumber.length()>=11 && strMobileNumber.length()<=12)
		 * return strMobileNumber; else return "";
		 */
	}

	/**
	 * 是否使用短信猫发送短信
	 * 
	 * @param strMobileNumber
	 * @return
	 */
	public final static boolean isSendByModem(String strMobileNumber) {
		boolean bool = false;
		String strPre = "";
		// 零开头(区号均为0开头)
		strPre = strMobileNumber.substring(0, 1);
		if (strPre.equals("0")) {
			bool = true;
		} else {
			strPre = "|" + strMobileNumber.substring(0, 3) + "|";
			if (getMobileNumPreDX().indexOf(strPre) >= 0 || getMobileNumPreLT().indexOf(strPre) >= 0) {
				bool = true;
			}
		}
		return bool;
	}

	// 另一种发送方式
	public final static void sendMobileMsgByModem_GetWay(String phoneNos, String sendContent) {
		// String strBackMsg = "";
		try {
			// 特殊符号转换
			sendContent = getConcentByReplace(sendContent);
			//
			String sendURL = HTTP_MODEM;
			String requestUrl = "";
			String[] arrPhoneNo = phoneNos.split(",");
			String phoneNo = "";
			URL url;
			URLConnection con;
			for (int i = 0; i < arrPhoneNo.length; i++) {
				phoneNo = arrPhoneNo[i];
				if (phoneNo.length() > 0) {
					requestUrl = sendURL + "&phone=" + phoneNo + "&content=" + sendContent;
					url = new URL(requestUrl);
					con = url.openConnection();
					con.setRequestProperty("User-Agent", "Mozilla/4.0(compatible;MSIE7.0;windows NT 5)");
					con.setRequestProperty("Content-Type", "text/html");
					con.setConnectTimeout(7200);
					con.connect();
					new InputStreamReader(url.openStream(), "UTF-8");
				}
			}
			/*
			 * BufferedReader bufReader = new BufferedReader(new
			 * InputStreamReader(url.openStream(),"UTF-8")); strBackMsg =
			 * bufReader.readLine(); bufReader.close();
			 * 
			 * if(strBackMsg.toLowerCase() == "true") strBackMsg = "发送成功"; else
			 * strBackMsg = "发送失败";
			 */
		} catch (Exception e) {
			e.printStackTrace();

		}
		// return strBackMsg;
	}

	/**
	 * 过滤http请求特殊符号
	 * 
	 * @param strConcent
	 * @return
	 */
	public final static String getConcentByReplace(String strConcent) {
		strConcent = strConcent.replace("%", "%25"); // 指定特殊字符
		strConcent = strConcent.replace("?", "%3F"); // 分隔实际的URL和参数
		strConcent = strConcent.replace("&", "%26"); // 分隔目录和子目录
		strConcent = strConcent.replace(" ", "%20"); // 空格
		strConcent = strConcent.replace("/", "%2F"); // 分隔目录和子目录
		strConcent = strConcent.replace("#", "%23"); // 表示书签
		return strConcent;
	}

	public final static String getDelCommaString(String strString) {
		if (strString == null) {
			strString = "";
		} else {
			strString = strString.intern();
		}
		if (strString.length() > 0) {
			String strComma = strString.substring(strString.length() - 1, strString.length()).intern();
			if (strComma == ",") {
				strString = strString.substring(0, strString.length() - 1);
			}
		}
		return strString;
	}
}
