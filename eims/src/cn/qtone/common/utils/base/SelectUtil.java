package cn.qtone.common.utils.base;

public class SelectUtil {
	
	public static String getAgencyTypeSelectHtml(Integer selected){
		StringBuffer sb = new StringBuffer();
		String[] str = {"教育局","教办","学校","其他"};//对应{1,2,3,4}
		sb.append("<option value='-1'>--请选择--</option>");
		for(int i=0;i<str.length;i++){
	    	sb.append("<option value='");
	    	sb.append(i+1);
            sb.append("'");
			if(selected==i+1){
				sb.append(" selected");
			}
            sb.append(">");
            sb.append(str[i]);
            sb.append("</option>");
		}
		return sb.toString();
	}

}
