package cn.qtone.library.student.dao.mysql;

import java.util.List;

import cn.qtone.common.components.syspurview.core.user.domain.IUser;
import cn.qtone.common.mvc.dao.BaseDAO;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.library.student.dao.StudentQueryDao;
import cn.qtone.library.student.qvo.StudentQVO;

public class StudentQueryDaoImpl extends BaseDAO implements StudentQueryDao {
	private static String querySchoolSql = "select  t.userId,t.realName,t.gender,t.grade_id,t.id_card,t.birthday,t.loginName,xs.school_name,xc.class_name,xa.agency_name,t.school_id,t.agency_id, t.class_id ,t.groupId,t.user_type";

	private static String orderSchoolBy = " order by t.school_id, xc.ordergrade, xc.orderclass";

	public List<IUser> pageQueryStudent(StudentQVO qvo) {
		
		String sql = this.querySchoolSql + " from sys_user t "
				+ " left join library_agency xa on t.agency_id=xa.id   left join library_school xs on t.school_id=xs.id"
				+ " left join library_class xc on t.class_id=xc.id where 1=1 " + getStudentCondition(qvo) + orderSchoolBy
				+ " limit " + Page.getStartOfPage(qvo.getStartPage(), qvo.getPageSize()) + "," + qvo.getPageSize();
		return getSimpleJdbcTemplate().query(sql, new StudentMapper());
	}

	public List<IUser> noPagequeryStudent(StudentQVO qvo) {
		
		String sql = this.querySchoolSql + " from sys_user t "
				+ " left join library_agency xa on t.agency_id=xa.id   left join library_school xs on t.school_id=xs.id"
				+ " left join library_class xc on t.class_id=xc.id where 1=1 " + getStudentCondition(qvo) + orderSchoolBy;
		return getSimpleJdbcTemplate().query(sql, new StudentMapper());
	}

	public int queryStudentCount(StudentQVO qvo) {
		
		String sql = "select count(*)" + " from sys_user t "
				+ " left join library_agency xa on t.agency_id=xa.id   left join library_school xs on t.school_id=xs.id"
				+ " left join library_class xc on t.class_id=xc.id where 1=1 " + getStudentCondition(qvo);
		System.out.println("sql-------->"+sql);
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	public IUser queryStudentForObject(StudentQVO qvo) {
		
		String sql = this.querySchoolSql + " from sys_user t "
			+ " left join library_agency xa on t.agency_id=xa.id   left join library_school xs on t.school_id=xs.id"
			+ " left join library_class xc on t.class_id=xc.id where 1=1 " + getStudentCondition(qvo);
		return getSimpleJdbcTemplate().queryForObject(sql, new StudentMapper());
	}
	
	public int getNewStudnetUnique() {

		String sql = " select IFNULL((SELECT Max(unique_no+1) from sys_user) , 1)";
		return getSimpleJdbcTemplate().queryForInt(sql);
	}

	private String getStudentCondition(StudentQVO qvo) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" and t.user_type='4' ");//学生用户类型
		sb.append(" and t.status='0' ");//学生为未毕业学生
		if (qvo.getId() != null && qvo.getId() > 0) {
			sb.append(" and t.userId=").append(qvo.getId());
		}
		if (qvo.getClass_id() != null && qvo.getClass_id() > 0) {
			sb.append(" and t.class_id=").append(qvo.getClass_id());
		}
		if (qvo.getOrderclass() > 0) {
			sb.append(" and xc.orderclass=").append(qvo.getOrderclass());
		}
		if (qvo.getGrade() > 0) {
			sb.append(" and t.grade_id=").append(qvo.getGrade());
		}
		if (qvo.getSchool_id() > 0) {
			sb.append(" and t.school_id=").append(qvo.getSchool_id());
		}
		if (qvo.getAgency_id() > 0) {
			sb.append(" and t.agency_id=").append(qvo.getAgency_id());
		}
		if (qvo.getStudent_name() != null && !qvo.getStudent_name().equals("")) {
			sb.append(" and t.realname  like '%").append(qvo.getStudent_name()).append("%'");
		}
		if (qvo.getAgency_name() != null && !qvo.getAgency_name().equals("")) {
			sb.append(" and xa.agency_name  like '%").append(qvo.getAgency_name()).append("%'");
		}
		if (qvo.getClass_name() != null && !qvo.getClass_name().equals("")) {
			sb.append(" and xc.class_name  like '%").append(qvo.getClass_name()).append("%'");
		}
		if (qvo.getSchool_name() != null && !qvo.getSchool_name().equals("")) {
			sb.append(" and xs.school_name  like '%").append(qvo.getSchool_name()).append("%'");

			if (qvo.getGrade_name() != null && !qvo.getGrade_name().equals("")) {

			}
		}
		
		return sb.toString();
	}

	public void updateStudent(String ids) {
		this.getSimpleJdbcTemplate().update("update library_news set newsstatus=1 where id in("+ids+")");
	}
}
