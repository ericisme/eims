package cn.qtone.library.school.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import cn.qtone.common.simplemvc.dao.HibernateSimpleDao;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;
import cn.qtone.qtoneframework.web.view.poi.ExcelExtractor;
import cn.qtone.qtoneframework.web.view.poi.ExcelUtils;
import cn.qtone.library.book.domain.Book;
import cn.qtone.library.school.domain.School;

/**
 * 基础管理 - 学校管理业务类.<br>
 * 
 * @author 贺少辉
 * @version 1.0
 */
public class SchoolService extends HibernateSimpleDao<School> {
	
	/**
	 * 按机构Id获得学校列表
	 */
	@SuppressWarnings("unchecked")
	public List<School> getSchoolsByAgencyId(Integer agcyId){
		Criteria criteria=this.createCriteria();
		criteria.createAlias("agency", "agency");
		criteria.add(Restrictions.eq("agency.id",agcyId));
		return criteria.list();
	}
	
	/**
	 * Excel数据导出.
	 */
	public HSSFWorkbook getStudentExport(List<School> list, String templatePath)
			throws FileNotFoundException, IOException {
		HSSFWorkbook wb = ExcelUtils.loadWorkbook(templatePath);
		HSSFSheet sheet = wb.getSheetAt(0);
		ExcelUtils.setValue(sheet, 0, 0, "学校列表");
		int row = 2;
		for (School school : list) {
			ExcelUtils.setValue(sheet, row, 0, school.getAgency() == null ? ""
					: school.getAgency().getAgency_name());
			ExcelUtils.setValue(sheet, row, 1, school.getSchool_name());
			ExcelUtils.setValue(sheet, row, 2, school.getContainGrade());
			ExcelUtils.setValue(sheet, row, 3, school.getSchoolDesc());
			ExcelUtils.setValue(sheet, row, 4, school.getSchool_no());
			ExcelUtils.setValue(sheet, row, 5, school.getSchool_code());
			row++;
		}
		return wb;
	}

	/**
	 * 获取模板中的数据对象.
	 */
	public List<School> getImportStudentData(String filepath)
			throws FileNotFoundException, IOException, SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		ExcelExtractor<School> extractor = new ExcelExtractor<School>();
		extractor.setExcel(filepath);
		extractor.setInitPosition(2, 0);
		extractor.setValueType(School.class);
		extractor.setErrorMsgColumn(12);
		extractor.setMethodNames(new String[] { "setSchool_name",
				"setContainGrade", "setSchoolDesc" ,"setSchool_no","setSchool_code"});
		List<School> students = extractor.getResult();
		return students;
	}
	
	/**
	 *  按机构Id获取学校下拉列表
	 */
	@SuppressWarnings("unchecked")
	public String getSchoolOptionHtmlByAgency(String agencyId,Integer SelectedShcoolId) {
		Criteria criteria = this.createCriteria(School.class);
		if (StringUtils.isNotBlank(agencyId)) {
			criteria.add(Restrictions.eq("agency.id", ServletUtil.parseInt(
					agencyId, -1)));
		}
		ArrayList<School> list = (ArrayList<School>) criteria.list();
		StringBuilder sb = new StringBuilder();
		sb.append("<option value='-1'>--请选择--</option>");
		if (list == null || list.size() == 0)
			return sb.toString();
		for (School school : list) {
			sb.append("<option value='");
			sb.append(school.getId());
			if (String.valueOf(school.getId()).equals(String.valueOf(SelectedShcoolId)))
				sb.append("' selected>");
			else
				sb.append("'>");
			sb.append(school.getSchool_name() + "</option>");
		}
		return sb.toString();
	}
	
	/**
	 * 判断同一个镇区下是否已经有同名的学校
	 */
	public boolean isUniqueInTheSameCity(School schl,Integer cityId){
		if(schl==null)return true;
		Criteria criteria=this.createCriteria();
		criteria.createCriteria("agency").add(Restrictions.eq("city.id", cityId));
		criteria.add(Restrictions.not(Restrictions.eq("id", schl.getId())))
					.add(Restrictions.eq("school_name",schl.getSchool_name()));
		if(criteria.list().size()>0) return false;
		else return true;
	}
	/**
	 * 批量判断同一个镇区下以及在列表schlList中，是否有同名的学校，主要用于批量导入新的学校
	 */
	public boolean isUniqueInTheSameCity(List<School> schlList,Integer cityId){
		if(schlList==null||schlList.size()==0)return true;
		List<String> schlNames=new ArrayList<String>();
		for(School schl : schlList){
			if(schlNames.contains(schl.getSchool_name())) return false;
			schlNames.add(schl.getSchool_name());
		}
		Criteria criteria=this.createCriteria();
		criteria.createCriteria("agency").add(Restrictions.eq("city.id", cityId));
		criteria.add(Restrictions.in("school_name",schlNames));
		if(criteria.list().size()>0) return false;
		else return true;
	}
	
	public Integer getMaxSchoolNO() {
		int count = 100;
		try {
			Criteria criteria = this.createCriteria(School.class);
			//criteria.add(Restrictions.eq("category.id",categoryId));
			criteria.setProjection( Projections.projectionList().add(Projections.max("school_no")));
			List list = criteria.list();
			count =  (list.size()>0&&list.get(0)!=null)?Integer.parseInt( String.valueOf(criteria.list().get(0))):100;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
		 /* Integer count = ((BigInteger)(getHibernateTemplate().executeFind(new HibernateCallback() {
	            public Object doInHibernate(Session arg0) throws HibernateException, SQLException {
	                Query q = arg0.createSQLQuery("select max(school_no) from library_school");
	                return q.list();
	            }
	        }).get(0))).intValue();
		  if(count<100)
			  count = 100;
	      return count;*/
	}
}
