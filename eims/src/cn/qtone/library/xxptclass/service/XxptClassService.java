package cn.qtone.library.xxptclass.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.qtone.common.components.syspurview.check.filter.priviilege.PriviilegeFilter;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.simplemvc.dao.HibernateSimpleDao;
import cn.qtone.library.XxptContants;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;
import cn.qtone.qtoneframework.web.view.poi.ExcelUtils;
import cn.qtone.library.school.domain.School;
import cn.qtone.library.xxptclass.domain.ZfptClass;

/**
 * 基础管理 - 班级管理业务类.<br>
 *
 * @author 贺少辉
 * @version 1.0
 */
public class XxptClassService extends HibernateSimpleDao<ZfptClass> {
	
	/**
	 * 根据学校ID和年级ID获取班级的下拉选择框. <br>
	 * @param schoolId 如果没有则用-1标示. <br>
	 * @param gradeId 如果没有则用-1标示. <br>
	 * @param seclected 默认选正项.<br>
	 * @return
	 */
	public String getZfptClassSelectHtml(String schoolId,String gradeId,String seclected){
		return getZfptClassHtmlSelectByZfptClassList(queryZfptClassBySchoolOrGrade(schoolId,gradeId),seclected);
	}
	
	/**
	 * 根据学校ID和年级ID获取班级的下拉选择框. <br>
	 * @param schoolId 如果没有则用-1标示. <br>
	 * @param gradeId 如果没有则用-1标示. <br>
	 * @param seclected 默认选正项.<br>
	 * @param userID 用户ID.<br>
	 * @return
	 */
	public String getZfptClassSelectHtml(String schoolId,String gradeId,String seclected,int userId){
		return getZfptClassHtmlSelectByZfptClassList(queryZfptClassBySchoolOrGradeAndUserId(schoolId,gradeId,userId),seclected);
	}
	
	/**
	 * 根据学校ID和年级ID获取班级对象.<br>
	 * @param schoolId 如果没有则用-1标示.<br>
	 * @param gradeId 如果没有则用-1标示. <br>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ZfptClass> queryZfptClassBySchoolOrGrade(String schoolId,String gradeId){
		Criteria criteria = this.createCriteria(ZfptClass.class);
		if(ServletUtil.notEqualF1(schoolId)){
			criteria.createAlias("school", "school");
			criteria.add(Restrictions.eq("school.id", ServletUtil.parseInt(schoolId, -1)));
		}
		if(ServletUtil.notEqualF1(gradeId)){
			criteria.add(Restrictions.like("class_code",gradeId+"_%"));
		}
		return criteria.list();
	}
	
	
	/**
	 * 根据学校ID和年级ID获取班级对象. <br>
	 * @param schoolId 如果没有则用-1标示.<br>
	 * @param gradeId 如果没有则用-1标示. <br>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<ZfptClass> queryZfptClassBySchoolOrGradeAndUserId(String schoolId,String gradeId,int userId){
		Criteria criteria = this.createCriteria(ZfptClass.class);
		if(ServletUtil.notEqualF1(schoolId)){
			criteria.createAlias("school", "school");
			criteria.add(Restrictions.eq("school.id", ServletUtil.parseInt(schoolId, -1)));
		}
		if(ServletUtil.notEqualF1(gradeId)){
			criteria.add(Restrictions.like("class_code",gradeId+"_%"));
		}
		criteria.add(Restrictions.eq("user_id",userId));
		return criteria.list();
	}
	
	//将集合对象整理成下拉选择框的形式.
	private  String getZfptClassHtmlSelectByZfptClassList(List<ZfptClass> list,String selected){
		StringBuffer sb = new StringBuffer();
		for(ZfptClass zfptClass:list){
			sb.append("<option value='");
			sb.append(zfptClass.getId());
            sb.append("'");
			if(ServletUtil.parseInt(selected,-1)==zfptClass.getId()){
				sb.append(" selected");
			}
            sb.append(">");
            sb.append(zfptClass.getClass_name());
            sb.append("</option>");
		}
		return sb.toString();
	}
	
	/**
	 * 保证班级编号的唯一性查询.<br>
	 * @param school_id.<br>
	 * @param class_code.<br>
	 * @param class_id.<br>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ZfptClass> queryZfptClassCodeBySchoolIdAndClassCode(int school_id,int ordergrade,int orderclass){
		Criteria criteria = this.createCriteria(ZfptClass.class);
		criteria.add(Restrictions.eq("ordergrade",ordergrade));
		criteria.add(Restrictions.eq("orderclass",orderclass));
		criteria.createAlias("school", "school");
		criteria.add(Restrictions.eq("school.id",school_id));
		
		return criteria.list();
	}
	
	/**
	 * 根据用户ID查询用户所管理的班级.<br>
	 * @param userId.<br>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ZfptClass> queryZfptClassByUserId(int userId){
		Criteria criteria = this.createCriteria(ZfptClass.class);
		criteria.add(Restrictions.eq("user_id",userId));
		return criteria.list();
	}
	
	//根据年级取班级下拉列表
	@SuppressWarnings("unchecked")
	public String getGradeSelectHtml(Integer school_id,int orderGrade){
		if(school_id==null)return "";
		StringBuffer sb = new StringBuffer();
		Criteria criteria = this.createCriteria(School.class);
		if (school_id > 0){
			criteria.add(Restrictions.eq("id", school_id));
			ArrayList<School> list = (ArrayList<School>) criteria.list();
			for (School school : list) {
				
				sb.append(XxptContants.getGradeSelectHtml(school.getContainGrade(), orderGrade+""));
			}
		}
		return sb.toString();
	}
	
	//根据年级取班级下拉列表
	@SuppressWarnings("unchecked")
	public String getClassSelectHtml(User user, int school_id,int orderGrade, String class_code){
		try {
			StringBuffer sb = new StringBuffer();
			boolean all = PriviilegeFilter.isAllowd(user, "/library/zfptclass.do", "all");
			//本市
			if(all){
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/zfptclass.do", "area")){
			//本校	
			}else{
				school_id = user.getSchool_id();
			}
			Criteria criteria = this.createCriteria(ZfptClass.class);
			if (school_id > 0) {
				criteria.createAlias("school", "school");
				criteria.add(Restrictions.eq("school.id", school_id));
				criteria.add(Restrictions.like("class_code", orderGrade+"%"));
				criteria.addOrder(Order.asc("orderclass"));
				ArrayList<ZfptClass> list = (ArrayList<ZfptClass>) criteria.list();
				for (ZfptClass zfptClass : list) {
					
					sb.append("<option value='");
					sb.append(zfptClass.getId());
					sb.append("'");
					if (zfptClass.getClass_code().equals(class_code)) {
						sb.append(" selected");
					}
					sb.append(">");
					sb.append(zfptClass.getClass_code().split("_")[1]);
					sb.append("</option>");
				}
			}else{
				criteria.add(Restrictions.like("class_code", orderGrade+"%"));
				criteria.addOrder(Order.asc("orderclass"));
				ArrayList<ZfptClass> list = (ArrayList<ZfptClass>) criteria.list();
				for (ZfptClass zfptClass : list) {
					
					sb.append("<option value='");
					sb.append(zfptClass.getId());
					sb.append("'");
					if (zfptClass.getClass_code().equals(class_code)) {
						sb.append(" selected");
					}
					sb.append(">");
					sb.append(zfptClass.getClass_code().split("_")[1]);
					sb.append("</option>");
				}
			}
			System.out.println("sb-------->"+sb.toString());
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
//	根据年级取班级名称下拉列表
	@SuppressWarnings("unchecked")
	public String getClassNameSelectHtml(User user, int school_id, int orderGrade, int class_id){
		
		StringBuffer sb = new StringBuffer();
		//boolean all = PriviilegeFilter.isAllowd(user, "/library/zfptclass.do", "all");
		boolean all = true;
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/zfptclass.do", "area")){
		//本校	
		}else{
			school_id = user.getSchool_id();
		}
		Criteria criteria = this.createCriteria(ZfptClass.class);
		if (school_id > 0) {
			criteria.createAlias("school", "school");
			criteria.add(Restrictions.eq("school.id", school_id));
			criteria.add(Restrictions.like("class_code", orderGrade+"%"));
			criteria.addOrder(Order.asc("orderclass"));
			ArrayList<ZfptClass> list = (ArrayList<ZfptClass>) criteria.list();
			for (ZfptClass zfptClass : list) {
				
				sb.append("<option value='");
				sb.append(zfptClass.getId());
				sb.append("'");
				if (zfptClass.getId() == class_id) {
					sb.append(" selected");
				}
				sb.append(">");
				sb.append(zfptClass.getClass_name());
				sb.append("</option>");
			}
		}
		return sb.toString();
	}
	
	/**
	 * Excel数据导出.
	 */
	public HSSFWorkbook getClassExport(List<ZfptClass> list,String templatePath) throws FileNotFoundException, IOException {
		HSSFWorkbook wb = ExcelUtils.loadWorkbook(templatePath);
		HSSFSheet sheet = wb.getSheetAt(0);
		ExcelUtils.setValue(sheet, 0, 0,"查询班级数据");
		int row = 2;
        for(ZfptClass zfptclass:list){
        	ExcelUtils.setValue(sheet, row, 0, zfptclass.getClass_name());
        	ExcelUtils.setValue(sheet, row, 1, zfptclass.getSchool().getAgency().getCity().getName());
        	ExcelUtils.setValue(sheet, row, 2, zfptclass.getSchool().getAgency().getAgency_name());
        	ExcelUtils.setValue(sheet, row, 3, zfptclass.getSchool().getSchool_name());
        	ExcelUtils.setValue(sheet, row, 4, zfptclass.gradeValue());
        	row++;
        }
		return wb;
	}
}


