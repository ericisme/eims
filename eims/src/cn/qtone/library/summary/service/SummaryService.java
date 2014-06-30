package cn.qtone.library.summary.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import cn.qtone.common.components.syspurview.core.user.domain.IUser;
import cn.qtone.common.simplemvc.dao.HibernateSimpleDao;
import cn.qtone.library.XxptContants;
import cn.qtone.library.book.domain.Book;
import cn.qtone.library.borrow.domain.Borrow;
import cn.qtone.qtoneframework.web.view.poi.ExcelUtils;

/**
 * 桌面管理 - <br>
 * 
 * @author 邝炳研
 * @version 1.0
 */
public class SummaryService extends HibernateSimpleDao<Book> {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public List summary(final String sql,final int start,final int size) {
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(Session arg0) throws HibernateException, SQLException {
                Query q = arg0.createSQLQuery(sql);
                q.setFirstResult((start-1)*size);
                q.setMaxResults(size);      //每次只取maxResult 条数据。
                return q.list();
            }
        });
       return list;
	}
	public int summaryCount(final String sql) {
		  Integer count = ((BigInteger)(getHibernateTemplate().executeFind(new HibernateCallback() {
	            public Object doInHibernate(Session arg0) throws HibernateException, SQLException {
	                Query q = arg0.createSQLQuery(sql);
	                return q.list();
	            }
	        }).get(0))).intValue();
	        return count;
	}
	
	public IUser getStudent(Integer id){
		try {
			return (IUser) this.getHibernateTemplate().get(IUser.class, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Excel数据导出.
	 */
	public HSSFWorkbook getBookExport(List<Borrow> list,String templatePath) throws FileNotFoundException, IOException {
		try {
			HSSFWorkbook wb = ExcelUtils.loadWorkbook(templatePath);
			HSSFSheet sheet = wb.getSheetAt(0);
			ExcelUtils.setValue(sheet, 0, 0,"图书借阅过期数据表");
			int row = 2;
			int i;
			for (Borrow borrow : list) {
				i=-1;
				ExcelUtils.setValue(sheet, row, ++i, borrow.getBorrowUser().getLoginName());
				ExcelUtils.setValue(sheet, row, ++i, borrow.getBorrowUser().getRealName());
				ExcelUtils.setValue(sheet, row, ++i, (borrow.getBorrowUser().getSchool()!=null)?borrow.getBorrowUser().getSchool().getSchool_name():"");
				ExcelUtils.setValue(sheet, row, ++i, (borrow.getBorrowUser().getZfptClass()!=null)?borrow.getBorrowUser().getZfptClass().getClass_name():"");
				ExcelUtils.setValue(sheet, row, ++i, borrow.getBook().getBook_name());
				ExcelUtils.setValue(sheet, row, ++i, borrow.getBook().getBook_index());
				ExcelUtils.setValue(sheet, row, ++i, borrow.getBook().getCategory().getName());
				ExcelUtils.setValue(sheet, row, ++i, sdf.format(borrow.getBorrow_time()));
				ExcelUtils.setValue(sheet, row, ++i, borrow.getShould_return());
				ExcelUtils.setValue(sheet, row, ++i, String.valueOf(borrow.expiredDay()));
				row++;
			}
			return wb;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
}
