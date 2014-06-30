package cn.qtone.crda.classstudent.qvo;

import javax.servlet.http.HttpServletRequest;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;

public class ClassStudentQVOUtil {
	/**
	 * 根据请求获取查询学生对象.
	 */
	public static ClassStudentQVO getClassStudentQVO(HttpServletRequest request){
		ClassStudentQVO qvo = new ClassStudentQVO();
    	qvo.setAgency_id(ServletUtil.parseInt(ServletUtil.removeSpace(request, "queryAgencyId"), -1));
    	qvo.setIs_valid(ServletUtil.parseInt(ServletUtil.removeSpace(request, "is_valid"), -1));
    	return qvo;
    }
}
