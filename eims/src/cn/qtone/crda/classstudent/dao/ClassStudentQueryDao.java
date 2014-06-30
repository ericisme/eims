package cn.qtone.crda.classstudent.dao;

import java.util.List;

import cn.qtone.crda.classstudent.domain.ClassStudent;
import cn.qtone.crda.classstudent.qvo.ClassStudentQVO;

/**
 * @author 王培森.<br>
 * 
 */
public interface ClassStudentQueryDao {
	 
	/**
	 * 分页查询家长信息
	 * @return
	 */
	List<ClassStudent> pageQueryClassStudent(ClassStudentQVO qvo);
	
	
	List<ClassStudent> noPageQueryClassStudent(ClassStudentQVO qvo);
	
	/**
	 * 根据家长查询对象查询家长数量.
	 */
	int queryClassStudentCount(ClassStudentQVO qvo);
	
	int queryIsValidCount();
	
	/**
	 * 匹配成人档案袋的学生唯一标识号
	 * @param qvo
	 */
	void updateClassStudentEffective(ClassStudentQVO qvo);
}
