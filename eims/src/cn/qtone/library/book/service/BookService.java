package cn.qtone.library.book.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import cn.qtone.common.simplemvc.dao.HibernateSimpleDao;
import cn.qtone.library.XxptContants;
import cn.qtone.library.book.domain.Book;
import cn.qtone.library.book.domain.ErrorBook;
import cn.qtone.library.borrow.domain.Borrow;
import cn.qtone.library.category.domain.Category;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;
import cn.qtone.qtoneframework.web.view.poi.ExcelExtractor;
import cn.qtone.qtoneframework.web.view.poi.ExcelUtils;

/**
 * 机构管理 - 机构管理
 *
 * @author 邝炳研
 * @version 1.0
 */
public class BookService extends HibernateSimpleDao<Book> {
	
	public static final int SJLX_XSTS = 0; //书籍类型，学生图书
	public static final int SJLX_JSYS = 1; //书籍类型，教师用书
	public static final int SJLX_QKZZ = 2; //书籍类型，期刊杂志
	public static final int SFFQ_F = 0; //是否为废弃书籍，否
	public static final int SFFQ_S = 1; //是否为废弃书籍，是
	
	
	
	/**
	 * 把机构列表用Html的&lt;option&gt;标签表示
	 */
	@SuppressWarnings("unchecked")
	public String getBookOptionHtmlFromCity(String city_id , int SelectedAgcyId){
		return getBookOptionHtml(queryBookByCity(city_id),SelectedAgcyId);
	}
	/**
	 * 根据系统参数类型返回该系统参数类型对应的下拉选择列表<br>
	 * @param param_type
	 */
	public String getCategorySelectHtml(String selected){
		StringBuffer sb = new StringBuffer();
		Criteria criteria = this.createCriteria(Category.class);
		criteria.add(Restrictions.isNull("parent_id"));
	    for(Category category:(ArrayList<Category>)criteria.list()){
	    	sb.append("<option value='");
			sb.append(category.getId());
            sb.append("'");
            if(selected.equals(String.valueOf(category.getId()))){
				sb.append(" selected");
			}
            sb.append(">");
            sb.append(category.getCode());
            sb.append("&nbsp;");
            sb.append(category.getName());
            sb.append("</option>");
	    }
		return sb.toString();
	}
	
	public String getCategoryTwoSelectHtml(Integer parent_id,String selected){
		StringBuffer sb = new StringBuffer();
		Criteria criteria = this.createCriteria(Category.class);
		criteria.add(Restrictions.eq("parent_id",parent_id));
	    for(Category category:(ArrayList<Category>)criteria.list()){
	    	sb.append("<option value='");
			sb.append(category.getId());
            sb.append("'");
            if(selected.equals(String.valueOf(category.getId()))){
				sb.append(" selected");
			}
            sb.append(">");
            sb.append(category.getCode());
            sb.append("&nbsp;");
            sb.append(category.getName());
            sb.append("</option>");
	    }
		return sb.toString();
	}
	
	public Category getCategory(Integer id){
		Category category = this.get(Category.class,id);
		return category;
	}
	@SuppressWarnings("unchecked")
	private List<Book> queryBookByCity(String city_id){
		Criteria criteria = this.createCriteria(Book.class);
		if(!"-1".equals(city_id)&&StringUtils.isNotBlank(city_id)){
		  criteria.add(Restrictions.eq("city.id", ServletUtil.parseInt(city_id, -1)));
		}
		criteria.addOrder(Order.asc("book_order"));
		criteria.addOrder(Order.desc("id"));
		return (ArrayList<Book>)criteria.list(); 
	}
	
	public Book getMaxBook(String bookNo){
		try{
			Query q = this.getSession().createQuery("from Book book where book.book_no like '"+bookNo+"%' order by book.id desc");
			q.setFirstResult(0);
			q.setMaxResults(1);
			return q.list().size()>0?(Book)q.list().get(0):null;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		/*Criteria criteria = this.createCriteria(Book.class);
		criteria.add(Restrictions.like("book_no",bookNo+"%"));
		criteria.addOrder(Order.desc("id"));
		return (Book) criteria.list().get(0);*/
	}
	public Integer getCountCategoryNo(Integer categoryId, Integer schoolId, Integer sjlx){
		try {
			Criteria criteria = this.createCriteria(Book.class);
			criteria.add(Restrictions.eq("category.id",categoryId));
			criteria.setProjection( Projections.projectionList().add(Projections.max("category_no")));
			/**
			 * add 2011-6-10 //限定图书所属类型
			 */
			criteria.add(Restrictions.eq("sjlx", sjlx));
			/**
			 * add 2011-6-3 for //限定用户所在学校
			 */
			Criteria schoolCriteria =criteria.createCriteria("school");
			schoolCriteria.add(Restrictions.eq("id",schoolId));//限定用户所在学校
			/**
			 * 
			 */
			
			List list = criteria.list();
			return list.size()>0&&list.get(0)!=null?(Integer) criteria.list().get(0):0;
		} catch (Exception e) {
			return 0;
		}
		
	}

	public Category getCategoryByCode(String category_code){
		Criteria criteria = this.createCriteria(Category.class);
		criteria.add(Restrictions.eq("code",category_code));
		List<Category> list = criteria.list();
		if(list==null||list.size()==0){
			if(category_code.length()>1)
				return getCategoryByCode(category_code.substring(0,category_code.length()-1));
			else
				return null;
		}
		return list==null?null:(Category) list.get(0);
	}
	
	private String getBookOptionHtml(List<Book> list, int SelectedAgcyId){
		StringBuilder sb=new StringBuilder();
		sb.append("<option value='-1'>--请选择--</option>");
		if(list==null||list.size()==0)return sb.toString();
		
		return sb.toString();
	}
	
	public String getAllBookOptionHtml(int id){
		return getBookOptionHtml(this.getAll(),id);
	}
	
	@SuppressWarnings("unchecked")
	public Book queryBookByBookName(String BookName){
		Criteria criteria = this.createCriteria(Book.class);
		if(StringUtils.isNotBlank(BookName)){
		  criteria.add(Restrictions.like("book_name", BookName));
		  criteria.add(Restrictions.eq("parent_id",1));
		}
		List<Book> list = (ArrayList<Book>)criteria.list();
		return list.size()>0?list.get(0):null; 
	}
	
	/**
	 * Excel数据导出.
	 */
	public HSSFWorkbook getBookExport(List<Book> list,String templatePath) throws FileNotFoundException, IOException {
		HSSFWorkbook wb = ExcelUtils.loadWorkbook(templatePath);
		HSSFSheet sheet = wb.getSheetAt(0);
		ExcelUtils.setValue(sheet, 0, 0,"图书数据列表");
		int row = 2;
		int i;
		for (Book book : list) {
			i=-1;
			ExcelUtils.setValue(sheet, row, ++i, book.getBook_no());
			ExcelUtils.setValue(sheet, row, ++i, book.getBook_name());
			ExcelUtils.setValue(sheet, row, ++i, book.getBook_isbn());
			ExcelUtils.setValue(sheet, row, ++i, book.getCategory_code());
			ExcelUtils.setValue(sheet, row, ++i, book.getCategory_name());
			ExcelUtils.setValue(sheet, row, ++i, book.getBook_index());
			ExcelUtils.setValue(sheet, row, ++i, book.getBook_author());
			ExcelUtils.setValue(sheet, row, ++i, String.valueOf(book.getBook_price()));
			ExcelUtils.setValue(sheet, row, ++i, book.getSchool().getSchool_name());
			ExcelUtils.setValue(sheet, row, ++i, XxptContants.BOOKSTATUS.get(String.valueOf(book.getStatus())));
			ExcelUtils.setValue(sheet, row, ++i, book.getBook_place());
			ExcelUtils.setValue(sheet, row, ++i, book.getBook_storage_time());
			ExcelUtils.setValue(sheet, row, ++i, book.getBook_publish());
			ExcelUtils.setValue(sheet, row, ++i, book.getBook_publish_place());
			ExcelUtils.setValue(sheet, row, ++i, book.getBook_publish_year());
			ExcelUtils.setValue(sheet, row, ++i, XxptContants.BOOKPRIVATE.get(String.valueOf(book.getBook_private())));
			ExcelUtils.setValue(sheet, row, ++i, book.getBook_content());
			ExcelUtils.setValue(sheet, row, ++i, book.getBook_page());
			ExcelUtils.setValue(sheet, row, ++i, book.getBook_format());
			ExcelUtils.setValue(sheet, row, ++i, book.getBook_language());
			ExcelUtils.setValue(sheet, row, ++i, book.getBook_goods());
			ExcelUtils.setValue(sheet, row, ++i, book.getBook_publish_time());
			ExcelUtils.setValue(sheet, row, ++i, book.getBook_congshu());
			ExcelUtils.setValue(sheet, row, ++i, book.getPrintface());
			ExcelUtils.setValue(sheet, row, ++i, book.getContent());
			row++;
		}
		return wb;
	}
	
	/**
	 * Excel数据导出打印.
	 */
	public HSSFWorkbook getBookPrintExport(List<Book> list,String templatePath) throws FileNotFoundException, IOException {
		HSSFWorkbook wb = ExcelUtils.loadWorkbook(templatePath);
		HSSFSheet sheet = wb.getSheetAt(0);
		//ExcelUtils.setValue(sheet, 0, 0,"图书数据列表");
		int row = 1;
		for (Book book : list) {
			ExcelUtils.setValue(sheet, row, 0, book.getBook_name());
			ExcelUtils.setValue(sheet, row, 1, book.getBook_index());
			ExcelUtils.setValue(sheet, row, 2, book.getBook_no());
			ExcelUtils.setValue(sheet, row, 3, book.getSchool().getSchool_name());
			row++;
		}
		return wb;
	}
	
	public List<Book> getImportBookData(String filepath) throws FileNotFoundException, IOException,SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException,
	IllegalAccessException, InvocationTargetException {
		ExcelExtractor<Book> extractor = new ExcelExtractor<Book>();
		
		extractor.setExcel(filepath);
		extractor.setInitPosition(2, 0);
		extractor.setValueType(Book.class);
		extractor.setErrorMsgColumn(12);
		extractor.setMethodNames(new String[] { "setBook_name","setCategory_code","setBook_price",
		"setBook_place","setBook_author","setBook_publish","setReadability"	,"setBook_content"
		});
		List<Book> book = extractor.getResult();
		return book;
	}
	/**
	 * 图书迁移读模板数据方法。-----------------------
	 */
	public List<Book> getImportBookTransData(String filepath) throws FileNotFoundException, IOException,SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException,
	IllegalAccessException, InvocationTargetException {
		ExcelExtractor<Book> extractor = new ExcelExtractor<Book>();
		
		extractor.setExcel(filepath);
		extractor.setInitPosition(1, 0);
		extractor.setValueType(Book.class);
		extractor.setErrorMsgColumn(12);
		extractor.setMethodNames(new String[] { "setBook_no" });
		List<Book> book = extractor.getResult();
		return book;
	}
	
	public HSSFWorkbook getBookErrorExport(List<ErrorBook> list, String templatePath) throws FileNotFoundException,IOException {
		try{
			HSSFWorkbook wb = ExcelUtils.loadWorkbook(templatePath);
			HSSFSheet sheet = wb.getSheetAt(0);
			ExcelUtils.setValue(sheet, 0, 0, "图书信息导入失败数据 （注意：请根据最后一列的提示信息进行修改）");
			int row = 2;
			int i;
			for (ErrorBook errorBook : list) {
				i=-1;
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getBook_name());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getBook_isbn());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getCategory_code());
				//ExcelUtils.setValue(sheet, row, ++i, errorBook.getCategory_name());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getBook_price());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getStatus());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getBook_place());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getBook_author());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getBook_storage_time());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getBook_publish());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getBook_publish_place());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getBook_publish_year());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getBook_private());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getBook_content());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getBook_page());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getBook_format());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getBook_language());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getBook_goods());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getBook_publish_time());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getBook_congshu());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getPrintface());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getContent());
				ExcelUtils.setValue(sheet, row, ++i, errorBook.getErrorReason());
				row++;
			}
			return wb;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
		
	}
	
	public Borrow getBorrowByBookId(final Integer bookId){
		try {
			Criteria criteria = this.createCriteria(Borrow.class);
			criteria.add(Restrictions.eq("book.id", bookId));
			criteria.addOrder(Order.desc("id"));
			criteria.add(Restrictions.isNull("return_time"));
			criteria.setMaxResults(1);
			List<Borrow> list = criteria.list();
			return list!=null&&list.size()>0?list.get(0):null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/* * */
	 public void saveBath(final List<Book> list){
			 getHibernateTemplate().execute(new HibernateCallback() {
	            public Object doInHibernate(Session session) throws HibernateException, SQLException {
	            	int i=0;
	            	for(Book book:list){
	            		i++;
	            		session.save(book);
	            		if(i%20==0){
	            			session.flush();
	            			session.clear();
	            		}
	            		
	            		
	            	}
	            	return null;
	            }
	        });
	}
	 public void delBath(final String idstr){
			try {
				this.getHibernateTemplate().execute(new HibernateCallback(){
					public Object doInHibernate(Session session) throws HibernateException, SQLException{
						try {
							org.hibernate.Query q = session.createQuery("delete Book  where id in ("+idstr+")");
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


