package cn.qtone.library.student.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import cn.qtone.common.components.syspurview.core.group.dao.IUserGroupDAO;
import cn.qtone.common.components.syspurview.core.group.domain.UserGroup;
import cn.qtone.common.components.syspurview.core.user.domain.IUser;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.simplemvc.dao.HibernateSimpleDao;
import cn.qtone.library.student.dao.StudentQueryDao;
import cn.qtone.library.student.domain.ErrorStudent;
import cn.qtone.library.student.qvo.StudentQVO;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;
import cn.qtone.qtoneframework.web.view.poi.ExcelExtractor;
import cn.qtone.qtoneframework.web.view.poi.ExcelUtils;

/**
 * 学校管理 - 学生管理
 * 
 * @author 贺少辉
 * @version 1.0
 */
public class StudentService extends HibernateSimpleDao<IUser> {

	StudentQueryDao studentQueryDao;
	private IUserGroupDAO groupDao; // 用户组管理的DAO，获取用户组的列表

	public void setGroupDao(IUserGroupDAO groupDao) {
		this.groupDao = groupDao;
	}
	
	public List getGroupList()
	{
		return this.groupDao.listAll();
	}
	public String getGroupSelectHtml(String selected){
		List<UserGroup> list = groupDao.listAll();
		StringBuilder sb=new StringBuilder();
		sb.append("<option value='-1'>--请选择--</option>");
		if(list==null||list.size()==0)return sb.toString();
		for(UserGroup userGroup:list){
			if(userGroup.getGroupId()<0)continue;
			sb.append("<option value='");
			sb.append(userGroup.getGroupId());
			if(selected.equals(String.valueOf(userGroup.getGroupId())))sb.append("' selected>");
			else sb.append("'>");
			sb.append(userGroup.getGroupName()+"</option>");
		}
		return sb.toString();
	}
	
	public UserGroup getUserGroup(int id)
	{
		return this.groupDao.getUserGroup(id);
	}


	/**
	 * 根据qvo分页查询学生数据.
	 */
	public Page pageQueryStudent(StudentQVO qvo) {
		int totalCount = this.studentQueryDao.queryStudentCount(qvo);
		if (totalCount < 1) {
			return new Page();
		}
		int startIndex = Page.getStartOfPage(qvo.getStartPage(), qvo.getPageSize());
		List<IUser> list = this.studentQueryDao.pageQueryStudent(qvo);
		return new Page(startIndex, totalCount, qvo.getPageSize(), list);

	}
	public Integer checkLoginName(String loginName) {
		try{
			Criteria criteria = this.createCriteria(IUser.class);
			//criteria.add(Restrictions.eq("user_type", "4"));
			criteria.add(Restrictions.eq("loginName", loginName));
			return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;

	}
	
	public Integer checkICCard(String idCard) {
		try{
			Criteria criteria = this.createCriteria(IUser.class);
			//criteria.add(Restrictions.eq("user_type", "4"));
			criteria.add(Restrictions.eq("id_card", idCard));
			return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;

	}
	
	public void delBorrow(final Integer userId){
		try {
			this.getHibernateTemplate().execute(new HibernateCallback(){
				public Object doInHibernate(Session session) throws HibernateException, SQLException{
					try {
						org.hibernate.Query q = session.createSQLQuery("delete from library_borrow where user_id=:userId");
							q.setParameter("userId", userId);
							q.executeUpdate();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
		              return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Integer checkLoginName2(String loginName,Integer userId) {
		try{
			Criteria criteria = this.createCriteria(IUser.class);
			//criteria.add(Restrictions.eq("user_type", "4"));
			criteria.add(Restrictions.eq("loginName", loginName));
			criteria.add(Restrictions.ne("userId", userId));
			return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;

	}
	


	/**
	 * 根据qvo查询学生数据集合.
	 */
	public List<IUser> noPagequeryStudentList(StudentQVO qvo) {
		return this.studentQueryDao.noPagequeryStudent(qvo);
	}
	
	/**
	 * 根据学生id,查询学生明细
	 */
	public IUser queryStudentForObject(StudentQVO qvo){
		return this.studentQueryDao.queryStudentForObject(qvo);
	}
	
	/**
	 * 获取新的学生唯一标识号
	 */
	public int getNewStudnetUnique() {
		return this.studentQueryDao.getNewStudnetUnique();
	}

	/**
	 * 保证学生的唯一性查询.<br>
	 * 
	 * @param unique_no.
	 *            <br>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<IUser> queryStudentByUniqueNo(String unique_no, String id) {
		Criteria criteria = this.createCriteria(IUser.class);
		criteria.add(Restrictions.eq("unique_no", unique_no));
		if (id != null && id.length() > 0) {
			criteria.add(Restrictions.ne("id", ServletUtil.parseInt(id, -1)));
		}
		return criteria.list();
	}

	/**
	 * Excel数据导出.
	 */
	public HSSFWorkbook getStudentExport(List<IUser> list, String templatePath) throws FileNotFoundException,
			IOException {
		HSSFWorkbook wb = ExcelUtils.loadWorkbook(templatePath);
		HSSFSheet sheet = wb.getSheetAt(0);
		ExcelUtils.setValue(sheet, 0, 0, "导出学生数据");
		int row = 2;
		for (IUser student : list) {
			ExcelUtils.setValue(sheet, row, 0, student.getRealName());
			ExcelUtils.setValue(sheet, row, 1, student.getLoginName());
			ExcelUtils.setValue(sheet, row, 2, student.getBirthday());
			ExcelUtils.setValue(sheet, row, 3, student.getGender()==1?"男":"女");
			ExcelUtils.setValue(sheet, row, 4, student.getSchool().getSchool_name());
			ExcelUtils.setValue(sheet, row, 5, student.getZfptClass().getClass_name());
			ExcelUtils.setValue(sheet, row, 6, student.getSchool().getAgency().getAgency_name());
			ExcelUtils.setValue(sheet, row, 7, student.getId_card());
			row++;
		}
		return wb;
	}
	
	public HSSFWorkbook getStudentErrorExport(List<ErrorStudent> list, String templatePath) throws FileNotFoundException,IOException {
		try{
			HSSFWorkbook wb = ExcelUtils.loadWorkbook(templatePath);
			HSSFSheet sheet = wb.getSheetAt(0);
			ExcelUtils.setValue(sheet, 0, 0, "导入失败学生数据（请修改此表错误数据，重新导入此表）");
			int row = 2;
			for (ErrorStudent student : list) {
				ExcelUtils.setValue(sheet, row, 0, student.getRealName());
				ExcelUtils.setValue(sheet, row, 1, student.getLoginName());
				ExcelUtils.setValue(sheet, row, 2, student.getBirthday());
				ExcelUtils.setValue(sheet, row, 3, student.getGender());
				ExcelUtils.setValue(sheet, row, 4, student.getId_card());
				ExcelUtils.setValue(sheet, row, 5, student.getErrorReason());
				row++;
			}
			return wb;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
		
	}

	/**
	 * 获取模板中的数据对象.
	 */
	public List<IUser> getImportStudentData(String filepath) throws FileNotFoundException, IOException,
			SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		ExcelExtractor<IUser> extractor = new ExcelExtractor<IUser>();
		
		extractor.setExcel(filepath);
		extractor.setInitPosition(2, 0);
		extractor.setValueType(IUser.class);
		extractor.setErrorMsgColumn(12);
		extractor.setMethodNames(new String[] { "setRealName","setLoginName","setBirthdayStr","setGender_str", "setId_card" });
		List<IUser> students = extractor.getResult();
		return students;
	}

	/**
	 * 验证身份证是否有效.<br>
	 * 
	 * @param id_card .
	 *            <br>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean checkIdCard(String id_card) {

		boolean flag = false;
		if (id_card.length() == 15) {

			// 15位身份证验证
			if (id_card.matches("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$") == true) {
				flag = true;
			}
		} else if (id_card.length() == 18) {

			// 18位身份证验证
			if (id_card.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([1-9]|X)$") == true) {

				flag = true;
			}
		} else
			flag = false;

		return flag;
	}
	
	public void updateStudent(String ids){
		this.studentQueryDao.updateStudent(ids);
	}

	public void setStudentQueryDao(StudentQueryDao studentQueryDao) {
		this.studentQueryDao = studentQueryDao;
	}
	
	 public void delBath(final String idstr){
			try {
				this.getHibernateTemplate().execute(new HibernateCallback(){
					public Object doInHibernate(Session session) throws HibernateException, SQLException{
						try {
							org.hibernate.Query q = session.createQuery("delete IUser  where userId in ("+idstr+")");
							q.executeUpdate();
						} catch (Exception e) {
							e.printStackTrace();
						}
			              return null;
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
