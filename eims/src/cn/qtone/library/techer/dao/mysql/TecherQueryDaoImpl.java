package cn.qtone.library.techer.dao.mysql;

import java.util.List;

import cn.qtone.common.components.syspurview.core.user.domain.IUser;
import cn.qtone.common.mvc.dao.BaseDAO;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.library.techer.dao.TecherQueryDao;
import cn.qtone.library.techer.qvo.TecherQVO;

public class TecherQueryDaoImpl extends BaseDAO implements TecherQueryDao {
	private String querySchoolSql = "select t.userId,t.realName,t.gender,t.grade_id,t.id_card,t.birthday,t.loginName,xs.school_name,xc.class_name,xa.agency_name,t.school_id,t.agency_id, t.class_id ,t.groupId,t.user_type,t.phone,t.mobile,t.email ";

	private String orderSchoolBy = " order by t.school_id, xc.ordergrade, xc.orderclass";

	public List<IUser> pageQueryTecher(TecherQVO qvo) {
		
		String sql = this.querySchoolSql + " from sys_user t "
				+ " left join library_agency xa on t.agency_id=xa.id   left join library_school xs on t.school_id=xs.id"
				+ " left join library_class xc on t.class_id=xc.id where 1=1 " + getTecherCondition(qvo) + orderSchoolBy
				+ " limit " + Page.getStartOfPage(qvo.getStartPage(), qvo.getPageSize()) + "," + qvo.getPageSize();
		//System.out.println("sql-->"+sql);
		return getSimpleJdbcTemplate().query(sql, new TecherMapper());
	}

	public List<IUser> noPagequeryTecher(TecherQVO qvo) {
		
		String sql = this.querySchoolSql + " from sys_user t "
				+ " left join library_agency xa on t.agency_id=xa.id   left join library_school xs on t.school_id=xs.id"
				+ " left join library_class xc on t.class_id=xc.id where 1=1 " + getTecherCondition(qvo) + orderSchoolBy;
		return getSimpleJdbcTemplate().query(sql, new TecherMapper());
	}

	public int queryTecherCount(TecherQVO qvo) {
		
		String sql = "select count(*)" + " from sys_user t "
				+ " left join library_agency xa on t.agency_id=xa.id   left join library_school xs on t.school_id=xs.id"
				+ " left join library_class xc on t.class_id=xc.id where 1=1 " + getTecherCondition(qvo);
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	public IUser queryTecherForObject(TecherQVO qvo) {
		
		String sql = this.querySchoolSql + " from sys_user t "
			+ " left join library_agency xa on t.agency_id=xa.id   left join library_school xs on t.school_id=xs.id"
			+ " left join library_class xc on t.class_id=xc.id where 1=1 " + getTecherCondition(qvo);
		
		return getSimpleJdbcTemplate().queryForObject(sql, new TecherMapper());
	}
	
	public int getNewStudnetUnique() {

		String sql = " select IFNULL((SELECT Max(unique_no+1) from sys_user) , 1)";
		return getSimpleJdbcTemplate().queryForInt(sql);
	}

	private String getTecherCondition(TecherQVO qvo) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" and t.user_type='5' ");//教师用户类型
		sb.append(" and t.status='0' ");//教师为正常使用状态
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
		if (qvo.getTecher_name() != null && !qvo.getTecher_name().equals("")) {
			sb.append(" and t.realname  like '%").append(qvo.getTecher_name()).append("%'");
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

	public void updateTecher(String ids) {
		this.getSimpleJdbcTemplate().update("update library_news set newsstatus=1 where id in("+ids+")");
	}
}
