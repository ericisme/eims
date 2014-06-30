package cn.qtone.crda.classstudent.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.simplemvc.dao.HibernateSimpleDao;
import cn.qtone.crda.classstudent.dao.ClassStudentQueryDao;
import cn.qtone.crda.classstudent.domain.ClassStudent;
import cn.qtone.crda.classstudent.qvo.ClassStudentQVO;
import cn.qtone.qtoneframework.web.view.poi.ExcelUtils;

/**
 * 成人档案袋
 * 
 * @author 王培森
 * @version 1.0
 */
public class ClassStudentService extends HibernateSimpleDao<ClassStudent> {

	ClassStudentQueryDao classStudentQueryDao;

	/**
	 * 根据qvo分页查询学生数据.
	 */
	public Page pageQueryClassStudent(ClassStudentQVO qvo) {
		
		int totalCount = this.classStudentQueryDao.queryClassStudentCount(qvo);
		if (totalCount < 1) {
			return new Page();
		}
		int startIndex = Page.getStartOfPage(qvo.getStartPage(), qvo.getPageSize());
		List<ClassStudent> list = this.classStudentQueryDao.pageQueryClassStudent(qvo);
		return new Page(startIndex, totalCount, qvo.getPageSize(), list);
	}
	
	/**
	 * 根据qvo查询学生数据集合.
	 */
	public List<ClassStudent> noPageQueryClassStudent(ClassStudentQVO qvo) {
		return this.classStudentQueryDao.noPageQueryClassStudent(qvo);
	}
	
	public void updateClassStudentEffective(ClassStudentQVO qvo){
		this.classStudentQueryDao.updateClassStudentEffective(qvo);
	}
	
	public int queryIsValidCount() {
		
		return this.classStudentQueryDao.queryIsValidCount();
	}
	
	/**
	 * Excel数据导出.
	 */
	public HSSFWorkbook getClassStudentExport(List<ClassStudent> list, String templatePath) throws FileNotFoundException,
			IOException {
		HSSFWorkbook wb = ExcelUtils.loadWorkbook(templatePath);
		HSSFSheet sheet = wb.getSheetAt(0);
		ExcelUtils.setValue(sheet, 0, 0, "查询无效学生数据");
		int row = 2;
		for (ClassStudent classStudent : list) {
			ExcelUtils.setValue(sheet, row, 0, classStudent.getUnique_no());
			ExcelUtils.setValue(sheet, row, 1, classStudent.getTruename());
			ExcelUtils.setValue(sheet, row, 2, classStudent.getTeacher_no());
			ExcelUtils.setValue(sheet, row, 3, classStudent.getSchool_name());
			ExcelUtils.setValue(sheet, row, 4, classStudent.getClass_name());
			ExcelUtils.setValue(sheet, row, 5, classStudent.containIsValidValue());
			row++;
		}
		return wb;
	}
	
	public void setClassStudentQueryDao(ClassStudentQueryDao classStudentQueryDao) {
		this.classStudentQueryDao = classStudentQueryDao;
	}
}