package cn.qtone.crda.classstudent.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import cn.qtone.common.mvc.dao.BaseDAO;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.crda.classstudent.dao.ClassStudentQueryDao;
import cn.qtone.crda.classstudent.domain.ClassStudent;
import cn.qtone.crda.classstudent.qvo.ClassStudentQVO;

public class ClassStudentQueryDaoImpl extends BaseDAO implements ClassStudentQueryDao {
	  
	private String orderBy = " order by m.is_valid desc, s.school_id, c.class_id";
	
	public List<ClassStudent> pageQueryClassStudent(ClassStudentQVO qvo) {
		String sql = "";
		sql = "select m.unique_no,"
			+"        m.truename,"
			+"        m.teacher_no,"
			+"        c.class_name,"
			+"        s.school_name,"
			+"        m.is_valid"
			+"   from crda_class_student cs"
			+"  inner join crda_member m"
			+"     on cs.member_id = m.member_id"
			+"    and m.is_checked = 1"
			+"    and m.member_type = 1"
			+"  inner join crda_class c"
			+"     on cs.class_id = c.class_id"
			+"  inner join crda_school s"
			+"     on c.school_id = s.school_id"
			+"     and s.school_id in(54,65,476,480,626)";
		sql += getCondition(qvo);
		sql += orderBy;
		sql += " limit " + Page.getStartOfPage(qvo.getStartPage(), qvo.getPageSize()) + "," + qvo.getPageSize();
		return getSimpleJdbcTemplate().query(sql, new ClassStudentMapper());
	}
	
	public List<ClassStudent> noPageQueryClassStudent(ClassStudentQVO qvo) {
		String sql = "";
		sql = "select m.unique_no,"
			+"        m.truename,"
			+"        m.teacher_no,"
			+"        c.class_name,"
			+"        s.school_name,"
			+"        m.is_valid"
			+"   from crda_class_student cs"
			+"  inner join crda_member m"
			+"     on cs.member_id = m.member_id"
			+"    and m.is_checked = 1"
			+"    and m.member_type = 1"
			+"  inner join crda_class c"
			+"     on cs.class_id = c.class_id"
			+"  inner join crda_school s"
			+"     on c.school_id = s.school_id"
			+"     and s.school_id in(54,65,476,480,626)";
		sql += " where m.is_valid <> 1";
		sql += orderBy;
		return getSimpleJdbcTemplate().query(sql, new ClassStudentMapper());
	}
	
	public int queryClassStudentCount(ClassStudentQVO qvo) {
		
		String sql = "";
		sql = "select count(*)"
			+"   from crda_class_student cs"
			+"   inner join crda_member m"
			+"     on cs.member_id = m.member_id and m.is_checked = 1 and m.member_type = 1"
			+"   inner join crda_class c"
			+"     on cs.class_id = c.class_id"
			+"   inner join crda_school s"
			+"     on c.school_id = s.school_id"
			+"     and s.school_id in(54,65,476,480,626)";
		sql += getCondition(qvo);
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	private String getCondition(ClassStudentQVO qvo) {
		
		StringBuffer sb = new StringBuffer();
		if (qvo.getIs_valid() != null && qvo.getIs_valid() == 0) {
			sb.append(" where m.is_valid <> 1");
		}
		return sb.toString();
	}
	
	public int queryIsValidCount() {
		
		String sql = "";
		sql = "select count(*)"
			+"   from crda_member m where is_valid = 1";
		return getSimpleJdbcTemplate().queryForInt(sql);
	}
	
	public void updateClassStudentEffective(ClassStudentQVO qvo) {
		
		String sql = "";
		sql = "select m.member_id, xt.unique_no"
			+"   from crda_class_student cs"
			+"  inner join crda_member m"
			+"     on cs.member_id = m.member_id"
			+"    and m.is_checked = 1"
			+"    and m.member_type = 1"
			+"  inner join crda_class c"
			+"     on cs.class_id = c.class_id"
			+"  inner join crda_school s"
			+"     on c.school_id = s.school_id"
			+"    and s.school_id in (54, 65, 476, 480, 626)"
			+"  inner join library_student xt"
			+"     on xt.agency_id = ?"
			+"    and m.teacher_no = xt.studyNub";

		SqlRowSet rs = getJdbcTemplate().queryForRowSet(
				sql,
				new Object[]{qvo.getAgency_id()},
				new int[]{java.sql.Types.INTEGER});
		final List<Integer[]> list = new ArrayList<Integer[]>();
		while(rs.next()){
			
			list.add(new Integer[]{rs.getInt("unique_no"),rs.getInt("member_id")});
		}
		getJdbcTemplate().batchUpdate("update crda_member set unique_no = ?, is_valid = 1 where member_id = ?", 
				new BatchPreparedStatementSetter(){
			public int getBatchSize() {
				return list.size();
			}
			public void setValues(PreparedStatement ps, int index)
					throws SQLException {
				Integer[] ids = list.get(index);
				ps.setInt(1, ids[0]); ps.setInt(2, ids[1]);
			}
		});
	}
}
