package cn.qtone.library;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import cn.qtone.ContantsUtil;
import cn.qtone.library.school.domain.School;

/**
 * 信息平台约定的系统常量.<br>
 * @author 贺少辉.<br />
 * create 2010-4-19.<br />
 */
public class XxptContants {
	
	public static final int SYSTEM_MANAGE=1;//系统管理员
	public static final int SJYJ_MANAGE=2;//市教育局管理员 
	
	
	/**
	 * 中山市的ID值【t_city】.
	 */
	public static final String ZSSCITYID = "2391";
	
	/**
	 * 系统默认班级编码,包含[1-25]班，如果班级不够该该数字就行了.<br />
	 */
	private static int SYSTEMClASS_CODE = 25;
	
	
	public static Map<String,String> BOOKSTATUS = new LinkedHashMap<String,String>();
	//0在库；1出借；2遗失；3损坏；
	static{
		BOOKSTATUS.put("0", "在库");
		BOOKSTATUS.put("1", "出借");
		BOOKSTATUS.put("2", "遗失");
		BOOKSTATUS.put("3", "损坏");
	}
	public static String getBookStatus (String selected){
    	StringBuffer sb = new StringBuffer();
		for(int i=0;i<BOOKSTATUS.size();i++){
			sb.append("<option  value='");
			sb.append(i);
			sb.append("'");
			sb.append(selected.equals(String.valueOf(i))?"selected":"");
			sb.append(">");
			sb.append(BOOKSTATUS.get(String.valueOf(i)));
			sb.append("</option>");	
		}
		return sb.toString();
    }
	//密级
	public static Map<String,String> BOOKPRIVATE = new LinkedHashMap<String,String>();
	static{
		BOOKPRIVATE.put("0", "一般");
		BOOKPRIVATE.put("1", "保密");
		BOOKPRIVATE.put("2", "绝密");
	}
	
	public static String getBookPrivate (String selected){
    	StringBuffer sb = new StringBuffer();
		for(int i=0;i<BOOKPRIVATE.size();i++){
			sb.append("<option  value='");
			sb.append(i);
			sb.append("'");
			sb.append(selected.equals(i+"")?"selected":"");
			sb.append(">");
			sb.append(BOOKPRIVATE.get(String.valueOf(i)));
			sb.append("</option>");	
		}
		return sb.toString();
    }
 
    /**
     * 学校所包含的年级类型
     */
    public static Map<String,String> GRADETYPE = new LinkedHashMap<String,String>();
    static{
    	GRADETYPE.put("1", "幼儿园");
    	GRADETYPE.put("2", "小学");
    	GRADETYPE.put("3", "初中");
    	GRADETYPE.put("4", "高中");
    	GRADETYPE.put("5", "雅思");
    }    
    /**
     * 学校所包含的年级类型
     */
    public static Map<String,String> USERSTATUS = new LinkedHashMap<String,String>();
    static{//0正常；1为毕业学生；2为教师调离；3为注销
    	USERSTATUS.put("0", "正常用户");
    	USERSTATUS.put("1", "毕业学生");
    	USERSTATUS.put("2", "调离教师");
    	USERSTATUS.put("3", "注销用户");
    }   
    public static String getUserStatus (String selected){
    	StringBuffer sb = new StringBuffer();
		for(int i=0;i<USERSTATUS.size();i++){
			sb.append("<option  value='");
			sb.append(i);
			sb.append("'");
			sb.append(selected.equals(i+"")?"selected":"");
			sb.append(">");
			sb.append(USERSTATUS.get(String.valueOf(i)));
			sb.append("</option>");	
		}
		return sb.toString();
    }
    
    /**
     * 信息发送状态
     */
    public static final int NEWS_SEND_NO=0;//未发送
    public static final int NEWS_SEND_STATUS_YES=1;//已发送
    public static Map<String,String> NEWS_SEND_STATUS = new LinkedHashMap<String,String>();
    static{
    	NEWS_SEND_STATUS.put(NEWS_SEND_NO+"", "未发送");
    	NEWS_SEND_STATUS.put(NEWS_SEND_STATUS_YES+"", "已发送");
    }    
    
    
    /**
     * Y_GRADE_MAP为幼儿园.<br />
     * X_GRADE_MAP为小学.<br />
     * C_GRADE_MAP为初中.<br />
     * G_GRADE_MAP为高中.<br />
	 * 年级标识约定.<key,value>--><年级名称,代表年级的字符串><br />
	 */
	public static Map<String,String> Y_GRADE_MAP = new LinkedHashMap<String,String>();
	public static Map<String,String> X_GRADE_MAP = new LinkedHashMap<String,String>();
	public static Map<String,String> C_GRADE_MAP = new LinkedHashMap<String,String>();
	public static Map<String,String> G_GRADE_MAP = new LinkedHashMap<String,String>();
	public static Map<String,String> YS_GRADE_MAP = new LinkedHashMap<String,String>();
	
    //统一给静态MAP赋值.
	static{
		Y_GRADE_MAP.put("1", "幼一");
		Y_GRADE_MAP.put("2", "幼儿小小班");
		Y_GRADE_MAP.put("3","幼儿小班");
		Y_GRADE_MAP.put("4","幼儿中班");
		Y_GRADE_MAP.put("5","幼儿大班");
		
		X_GRADE_MAP.put("6","一年级");
		X_GRADE_MAP.put("7","二年级");
		X_GRADE_MAP.put("8","三年级");
		X_GRADE_MAP.put("9","四年级");
		X_GRADE_MAP.put("10","五年级");
		X_GRADE_MAP.put("11","六年级");
		
		C_GRADE_MAP.put("12","初一");
		C_GRADE_MAP.put("13","初二");
		C_GRADE_MAP.put("14","初三");
		
		G_GRADE_MAP.put("15","高一");
		G_GRADE_MAP.put("16","高二");
		G_GRADE_MAP.put("17","高三");
		
		YS_GRADE_MAP.put("18","雅思周末班");
	}

	 /**
     * 性别类型
     */
    public static Map<String,String> GENDERTYPE = new LinkedHashMap<String,String>();
    static{
    	GENDERTYPE.put("0", "女");
    	GENDERTYPE.put("1", "男");
    }  
	
    /**
     * 性别类型
     */
    public static Map<String,String> PARENTMOBILETYPE = new LinkedHashMap<String,String>();
    static{
    	PARENTMOBILETYPE.put("0", "无手机号码");
    	PARENTMOBILETYPE.put("1", "一个手机号码");
    	PARENTMOBILETYPE.put("2", "多个手机号码");
    } 
    
	 /**
     * 后台管理用户类型 <br />
     */
    public static Map<String,String> MANAGE_USER_TYPE = new LinkedHashMap<String,String>();
    static{
    	MANAGE_USER_TYPE.put("1", "系统管理员");
    	MANAGE_USER_TYPE.put("2", "教办人员");
    	MANAGE_USER_TYPE.put("3", "图书管理员");
    	MANAGE_USER_TYPE.put("4", "学生");
    	MANAGE_USER_TYPE.put("5", "教师");
    	MANAGE_USER_TYPE.put("6", "其他");
    }    
    
    /**
     * 信息类别可选值
     *
     */
    public enum XXPT_PARAM_TYPE_ENUM{
    	XXLB,JZGX,WJXT
    } 

    public static Map<String,String> XXPT_PARAM_TYPE = new LinkedHashMap<String,String>();
    static{
    	XXPT_PARAM_TYPE.put("XXLB", "信息类别");
    	XXPT_PARAM_TYPE.put("JZGX", "家长与子女的关系");
    	XXPT_PARAM_TYPE.put("WJXT", "外接系统");
    }    
    
	 /**
     * 系统预留的班级编号,现在1-25<br />
     */
    public static String getSystemClassCodeSelectHtml(String selected){
		StringBuffer sb = new StringBuffer();
		for(int i=1;i<=SYSTEMClASS_CODE;i++){
			sb.append("<option  value='");
			sb.append(i);
			sb.append("'");
			sb.append(selected.equals(i+"")?"selected":"");
			sb.append(">");
			sb.append(i);
			sb.append("</option>");	
		}
		return sb.toString();
	}
    
	/**
	 * 获取学校包含年级的复选框.<br />
	 * @param checkBoxName 复选框的名称.<br />
	 * @param checked 要选正的值用逗号隔开的1，2，3，4<br />
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getGradeTypeCheckBoxHtml(String checkBoxName,String checked){
		StringBuffer sb = new StringBuffer();
		Iterator<String> gradeKey =  (Iterator<String>) GRADETYPE.keySet().iterator();
		while(gradeKey.hasNext()){
			String key = gradeKey.next();
			sb.append("<input type='checkbox' name='");
			sb.append(checkBoxName);
			sb.append("' value='");
			sb.append(key);
			sb.append("'");
			sb.append(checked.indexOf(key)>=0?"checked":"");
			sb.append(">");
			sb.append(GRADETYPE.get(key));
			sb.append("</checkbox>");
		}
		return sb.toString();
	}
	
	/**
	 * 根据学校包含的年级类型获取年级的下拉列表.<br />
	 * @param gradeType
	 * @param checked
	 * @return
	 */
	public static String getGradeSelectHtml(String gradeType,String checked){
		StringBuffer sb = new StringBuffer();
		if(gradeType.indexOf("1")>=0){
			sb.append(ContantsUtil.getSystemContantsSelectHtml(Y_GRADE_MAP,checked));
		}
		if(gradeType.indexOf("2")>=0){
			sb.append(ContantsUtil.getSystemContantsSelectHtml(X_GRADE_MAP,checked));
		}
		if(gradeType.indexOf("3")>=0){
			sb.append(ContantsUtil.getSystemContantsSelectHtml(C_GRADE_MAP,checked));
		}
		if(gradeType.indexOf("4")>=0){
			sb.append(ContantsUtil.getSystemContantsSelectHtml(G_GRADE_MAP,checked));
		}
		if(gradeType.indexOf("5")>=0){
			sb.append(ContantsUtil.getSystemContantsSelectHtml(YS_GRADE_MAP,checked));
		}
		return sb.toString();
	}
	
	/**
	 *  获取grade的值.
	 */
	public static String getGradeValue(String gradeNumber){
		if(StringUtils.isNotBlank(Y_GRADE_MAP.get(gradeNumber))){
			return Y_GRADE_MAP.get(gradeNumber);
		}else if(StringUtils.isNotBlank(X_GRADE_MAP.get(gradeNumber))){
			return X_GRADE_MAP.get(gradeNumber);
		}else if(StringUtils.isNotBlank(C_GRADE_MAP.get(gradeNumber))){
			return C_GRADE_MAP.get(gradeNumber);
		}else if(StringUtils.isNotBlank(G_GRADE_MAP.get(gradeNumber))){
			return G_GRADE_MAP.get(gradeNumber);
		}else{
			return YS_GRADE_MAP.get(gradeNumber);	
		}
	}
	
	
	/**
	 * 生成学校的年级
	 * @param school 学校
	 * @param checkBoxName checkbox的名称
	 * @param sel 已选中的值,可以为""
	 * @param disabled 是否可选
	 * @return
	 */
	public static String createSchoolGrade(School school,String checkBoxName,String sel,String disabled){
		StringBuffer strBuf=new StringBuffer();		
		if(!"".equals(sel)){
			if(!sel.startsWith(",")){
				sel=","+sel;
			}
			if(!sel.endsWith(",")){
				sel=sel+",";
			}
		};
		String containGrade=school.getContainGrade();
		if(containGrade!=null&&containGrade.length()>0){
			String[] grade=containGrade.split(",");
			for(int i=0;i<grade.length;i++){
				if(Integer.parseInt(grade[i])==1){//幼儿园
					strBuf.append(createCheckBox(checkBoxName,Y_GRADE_MAP,sel,disabled));								
				}else if(Integer.parseInt(grade[i])==2){//小学
					strBuf.append(createCheckBox(checkBoxName,X_GRADE_MAP,sel,disabled));		
				}else if(Integer.parseInt(grade[i])==3){//初中
					strBuf.append(createCheckBox(checkBoxName,C_GRADE_MAP,sel,disabled));	
				}else if(Integer.parseInt(grade[i])==4){//高中
					strBuf.append(createCheckBox(checkBoxName,G_GRADE_MAP,sel,disabled));
				}else if(Integer.parseInt(grade[i])==5){//雅思
					strBuf.append(createCheckBox(checkBoxName,YS_GRADE_MAP,sel,disabled));
				}
			}
		}
		
		return strBuf.toString();
		
	}
	
	private static String createCheckBox(String checkBoxName,Map<String,String> map,String sel,String disabled){
		StringBuffer strBuf=new StringBuffer();
		Iterator<String> gradeKey =  (Iterator<String>) map.keySet().iterator();
		while(gradeKey.hasNext()){
			String key = gradeKey.next();
			strBuf.append("<input type='checkbox' name='");
			strBuf.append(checkBoxName);
			strBuf.append("' id='");
			strBuf.append(checkBoxName);
			strBuf.append("' value='");
			strBuf.append(key);
			strBuf.append("' ");
			strBuf.append(sel.indexOf(String.valueOf(","+key+","))>=0?"checked":"");
			strBuf.append(" "+disabled+" ");
			strBuf.append(">");
			strBuf.append(map.get(key));
			strBuf.append("&nbsp;&nbsp;");
		}
		
		
		return strBuf.toString();
	}

	
	
	
	//书籍类型
	public static Map<String,String> SJLX = new LinkedHashMap<String,String>();
	static{
		SJLX.put("0", "学生图书");
		SJLX.put("1", "教师用书");
		SJLX.put("2", "期刊杂志");
	}
	/**
	 * 获得书籍类型下拉
	 * @param currentsjlx
	 * @return
	 */
	public  static String getSjlxHtml(String selected) {
    	StringBuffer sb = new StringBuffer();
    	
    	
		for(int i=0;i<SJLX.size();i++){
			System.out.println("第"+i+"次");
			sb.append("<option  value='");
			sb.append(i);
			sb.append("'");
			sb.append(selected.equals(i+"")?"selected":"");
			sb.append(">");
						
			sb.append(SJLX.get(String.valueOf(i)));
			sb.append("</option>");	
		}
		System.out.println("打印出来的html为："+sb.toString());
		return sb.toString();
		
	}

	//可阅读性
	public static Map<String,String> READABILITY = new LinkedHashMap<String,String>();
	static{
		READABILITY.put("0", "好");
		READABILITY.put("1", "中");
		READABILITY.put("2", "差");
	}
	/**
	 * 获得可阅读性下拉
	 * @param selected
	 * @return
	 */
	public static String getReadabilityHtml(String selected) {
		StringBuffer sb = new StringBuffer();    	
		for(int i=0;i<READABILITY.size();i++){
			sb.append("<option  value='");
			sb.append(READABILITY.get(String.valueOf(i)));
			sb.append("'");
			sb.append(selected.equals(READABILITY.get(String.valueOf(i)))?"selected":"");
			sb.append(">");						
			sb.append(READABILITY.get(String.valueOf(i)));
			sb.append("</option>");	
		}
		System.out.println("打印出来的html为："+sb.toString());
		return sb.toString();
	}
	
}