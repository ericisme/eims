package cn.qtone.library.techer.qvo;

import javax.servlet.http.HttpServletRequest;

import cn.qtone.common.components.syspurview.check.filter.priviilege.PriviilegeFilter;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;

public class TecherQVOUtil {
	/**
	 * 根据请求获取查询学生对象.
	 */
	public static TecherQVO getTecherQVO(HttpServletRequest request){
    	TecherQVO qvo = new TecherQVO();
    	//qvo.setAgency_id(ServletUtil.parseInt(ServletUtil.removeSpace(request, "queryAgencyId"), -1));
    	//qvo.setSchool_id(ServletUtil.parseInt(ServletUtil.removeSpace(request, "querySchoolId"), -1));
    	User user = (User) UserUtil.getUserBean(request);
		boolean all = PriviilegeFilter.isAllowd(user, "/library/student.do", "all");
		//本市
		if(all){
			qvo.setAgency_id(ServletUtil.parseInt(ServletUtil.removeSpace(request, "queryAgencyId"), -1));
	    	qvo.setSchool_id(ServletUtil.parseInt(ServletUtil.removeSpace(request, "querySchoolId"), -1));
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/student.do", "area")){
			qvo.setAgency_id(user.getAgency_id());
	    	qvo.setSchool_id(ServletUtil.parseInt(ServletUtil.removeSpace(request, "querySchoolId"), -1));
		//本校	
		}else{
			qvo.setAgency_id(user.getAgency_id());
	    	qvo.setSchool_id(user.getSchool_id());
		}
    	
		qvo.setGrade(ServletUtil.parseInt(ServletUtil.removeSpace(request, "queryGradeId"), -1));
		qvo.setClass_id(ServletUtil.parseInt(ServletUtil.removeSpace(request, "queryClassNameId"), -1));
		qvo.setTecher_name(ServletUtil.removeSpace(request, "techerName"));
		//qvo.setMobile_type(ServletUtil.parseInt(ServletUtil.removeSpace(request, "queryMobileType"), -1));
    	return qvo;
    }
}
