package cn.qtone.library.main.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.components.syspurview.check.filter.priviilege.PriviilegeFilter;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.library.book.domain.Book;
import cn.qtone.library.book.service.BookService;
import cn.qtone.library.borrow.domain.Borrow;
import cn.qtone.library.main.service.MainService;
import cn.qtone.library.summary.domain.BookSummary;
import cn.qtone.library.summary.domain.ClassSummary;

/*
 * 此类存放公用方法，不受过虑器，权限限制
 * author 邝炳研
 */
public class MainController extends SimpleManageController<Book, BookService> {
	BookService bookService;
	private String unReturnPage;
	private String bookTotalPage;
	MainService mainService;
	
	public ModelAndView bookTotalList(HttpServletRequest req, HttpServletResponse response) throws Exception {
		try {
			Map<String,Object> map=this.getMapWithUser(req);
			User user = (User) map.get("user");
			StringBuilder sql = new StringBuilder();
			sql.append("select count(*) total ,sum(book.status=0 or book.status is null) ,sum(book.status=1),sum(book.status=2),sum(book.status=3) from library_book book  ");
			boolean all = PriviilegeFilter.isAllowd(user, "/library/main.do", "all");
			//本市
			if(all){
				map.put("areaName","本市");
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/main.do", "area")){
				map.put("areaName","本区");
				sql.append(" left join library_school school on book.school_id=school.id left join library_agency agency on school.agency_id=agency.id left join t_city city on agency.town_id=city.id");
				sql.append(" where city.id=");
				sql.append(user.getTown_id());
			//本校	
			}else {
				map.put("areaName","本校");
				sql.append(" left join library_school school on book.school_id=school.id ");
				sql.append(" where school.id=");
				sql.append(user.getSchool_id());
			}
			
			List list = mainService.sqlQuerytoList(sql.toString());
			Object[] obj = (Object[]) list.get(0);
			map.put("total",obj[0]);
			map.put("inTotal",obj[1]);
			map.put("outTotal",obj[2]);
			map.put("loseTotal",obj[3]);
			map.put("badTotal",obj[4]);
			return new ModelAndView(this.bookTotalPage,map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public ModelAndView unReturnList(HttpServletRequest req, HttpServletResponse response) throws Exception {
		try {
			Map<String,Object> map=this.getMapWithUser(req);
			Criteria criteria=this.getDomainService().createCriteria(Borrow.class);
			Criteria userCriteria = criteria.createCriteria("borrowUser");
			//userCriteria.add(Restrictions.ne("zfptClass.id", -1));
			//userCriteria.add(Restrictions.ne("school.id", -1));
			criteria.add(Restrictions.sqlRestriction(" to_days(sysdate()) - to_days(this_.borrow_time)>this_.limit_day"));
			criteria.add(Restrictions.eq("book_status", 1));
			criteria.addOrder(Order.asc("id"));
			User user = (User) map.get("user");
			int userId=0;
			int townId=0;
			int SchoolId=0;
			boolean all = PriviilegeFilter.isAllowd(user, "/library/main.do", "all");
			//本市
			if(all){
				
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/main.do", "area")){
				townId = user.getTown_id();
			//本校	
			}else if (PriviilegeFilter.isAllowd(user, "/library/main.do", "unit")){
				SchoolId = user.getSchool_id();
			//本人
			}else {
				userId= user.getUserId();
				userCriteria.add(Restrictions.eq("userId", user.getUserId()));
			}
			if(userId>0)
				userCriteria.add(Restrictions.eq("userId", user.getUserId()));
			else if(SchoolId>0){
				Criteria schoolCriteria = userCriteria.createCriteria("school");
				schoolCriteria.add(Restrictions.eq("id", user.getSchool_id()));//限定用户所在学校
			}else if(townId>0){
				Criteria schoolCriteria = userCriteria.createCriteria("school");
				Criteria agencyCriteria = schoolCriteria.createCriteria("agency");
				agencyCriteria.add(Restrictions.eq("city.id", user.getTown_id()));//限定用户所在镇区
			}
			criteria.setMaxResults(5);
			
			map.put("list",criteria.list());
			return new ModelAndView(this.unReturnPage,map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ModelAndView bookSummaryList(HttpServletRequest req, HttpServletResponse response) throws Exception {
		try {
			Map<String,Object> map=this.getMapWithUser(req);
			User user = (User) map.get("user");
			StringBuilder sql = new StringBuilder();

			Integer queryCityId = 0;
			Integer queryAgencyId =0;
			Integer querySchoolId =0;
			Integer userId =0;
			
			sql.append("select count(*) total ,school.school_name ,book.book_name,book.category_name ,book.book_price,book.book_author,book.id ,book.book_index");
		    sql.append(" from library_borrow borrow ");
		    sql.append(" left join library_book book   on borrow.book_id=book.id");
		    sql.append(" left join library_school school on book.school_id=school.id");
		    sql.append(" left join sys_user user on borrow.user_id=user.userid");
		    sql.append(" left join library_agency agency on school.agency_id=agency.id ");
		    sql.append(" left join t_city city on agency.town_id=city.id");
		    sql.append(" where 1=1 ");
			
			boolean all = PriviilegeFilter.isAllowd(user, "/library/main.do", "all");
			//本市
			if(all){
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/main.do", "area")){
				queryCityId = user.getTown_id();
			//本校	
			}else if (PriviilegeFilter.isAllowd(user, "/library/main.do", "unit")){
				querySchoolId = user.getSchool_id();
			}else{
				userId=user.getUserId();
			}
			if(userId>0){
				sql.append(" and user.userId=");
				sql.append(userId);
			}else if(querySchoolId>0){
				sql.append(" and school.id=");
				sql.append(querySchoolId);
			}else if(queryAgencyId>0){
				sql.append(" and agency.id=");
				sql.append(queryAgencyId);
			}else if(queryCityId>0){
				sql.append(" and city.id=");
				sql.append(queryCityId);
			}
			
			sql.append(" group by book.book_name , school.id order by total desc , school.id asc");
			List<Object[]> list = mainService.summary(sql.toString(),5);
			List<BookSummary> bookList = new ArrayList<BookSummary>();
			BookSummary bookSummary =null;
			for(Object[] obj:list){
				bookSummary = new BookSummary();
				bookSummary.setTotal(String.valueOf(obj[0]));
				bookSummary.setSchool_name(String.valueOf(obj[1]));
				bookSummary.setBook_name(String.valueOf(obj[2]));
				bookSummary.setCategory_name(String.valueOf(obj[3]));
				bookSummary.setBook_price(String.valueOf(obj[4]));
				bookSummary.setBook_author(String.valueOf(obj[5]));
				bookSummary.setBook_id(String.valueOf(obj[6]));
				bookSummary.setBook_index(String.valueOf(obj[7]));
				bookList.add(bookSummary);
			}
			map.put("list",bookList);
			return new ModelAndView("/library/main/summary_book_list",map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public ModelAndView classSummaryList(HttpServletRequest req, HttpServletResponse response) throws Exception {
		try {
			Map<String,Object> map=this.getMapWithUser(req);
			User user = (User) map.get("user");
			StringBuilder sql = new StringBuilder();

			Integer queryCityId = 0;
			Integer queryAgencyId=0;
			Integer querySchoolId=0;
			Integer userId = 0;
			
			sql.append(" select  sum(borrow.id is not null) total , count(user.userid) usertotal,  school.school_name , class.class_name ,class.id");
			sql.append(" from  library_class class");
			sql.append(" left join  sys_user user on class.id=user.class_id");
			sql.append(" left join  library_borrow borrow  on borrow.user_id=user.userid");
			sql.append(" left join  library_school school on class.school_id=school.id");
			sql.append(" left join  library_agency agency on school.agency_id=agency.id ");
			sql.append(" left join  t_city city on agency.town_id=city.id");

			sql.append(" where  user.user_type=4");
			sql.append(" and user.status=0");
			sql.append(" and school.school_name is not null");
			sql.append(" and borrow.id is not null");
			
			boolean all = PriviilegeFilter.isAllowd(user, "/library/main.do", "all");
			//本市
			if(all){
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/main.do", "area")){
				queryCityId = user.getTown_id();
			//本校	
			}else if (PriviilegeFilter.isAllowd(user, "/library/main.do", "unit")){
				querySchoolId = user.getSchool_id();
				
			}else{
				userId = user.getUserId();
			}
			if(userId>0){
				sql.append(" and user.userId=");
				sql.append(userId);
			}else if(querySchoolId>0){
				sql.append(" and school.id=");
				sql.append(querySchoolId);
			}else if(queryAgencyId>0){
				sql.append(" and agency.id=");
				sql.append(queryAgencyId);
			}else if(queryCityId>0){
				sql.append(" and city.id=");
				sql.append(queryCityId);
			}
			
			sql.append(" group by class.id order by total desc , school.school_name asc, class.ordergrade asc,class.orderclass asc");
			List<Object[]> list = mainService.summary(sql.toString(),5);
			List<ClassSummary> classList = new ArrayList<ClassSummary>();
			ClassSummary classSummary =null;
			for(Object[] obj:list){
				classSummary = new ClassSummary();
				classSummary.setTotal(String.valueOf(obj[0]));
				classSummary.setUsertotal(String.valueOf(obj[1]));
				classSummary.setSchool_name(String.valueOf(obj[2]));
				classSummary.setClass_name(String.valueOf(obj[3]));
				classSummary.setClass_id(String.valueOf(obj[4]));
				classList.add(classSummary);
			}
			map.put("list",classList);
			return new ModelAndView("/library/main/summary_class_list",map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}
	
	public void setUnReturnPage(String unReturnPage) {
		this.unReturnPage = unReturnPage;
	}
	public void setBookTotalPage(String bookTotalPage) {
		this.bookTotalPage = bookTotalPage;
	}
	
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}

	
}
