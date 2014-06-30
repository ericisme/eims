package cn.qtone.library.summary.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.components.syspurview.check.filter.priviilege.PriviilegeFilter;
import cn.qtone.common.components.syspurview.core.user.domain.IUser;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.library.XxptContants;
import cn.qtone.library.agency.service.AgencyService;
import cn.qtone.library.book.domain.Book;
import cn.qtone.library.borrow.domain.Borrow;
import cn.qtone.library.city.service.CityService;
import cn.qtone.library.school.service.SchoolService;
import cn.qtone.library.summary.domain.BookSummary;
import cn.qtone.library.summary.domain.ClassSummary;
import cn.qtone.library.summary.domain.StudentSummary;
import cn.qtone.library.summary.service.SummaryService;
import cn.qtone.library.xxptclass.domain.ZfptClass;
import cn.qtone.library.xxptclass.service.XxptClassService;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;
import cn.qtone.qtoneframework.web.view.poi.ExcelUtils;

/*
 * author 邝炳研
 */
public class SummaryController extends SimpleManageController<Book, SummaryService> {
	private String bookIndexPage;
	private String bookListPage;
	private String bookDetailPage;
	private String bookDetailListPage;
	private String classIndexPage;
	private String classListPage;
	private String studentIndexPage;
	private String studentListPage;
	
	//add for 提供给东区教育信息网的页面
	private String zsdqeduSummaryIndexPage;
	private String zsdqeduClassListPage;
	private String zsdqeduStudentListPage;
	
	SummaryService summaryService;
	SchoolService schoolService;
	CityService cityService;
	AgencyService agencyService;
	XxptClassService zfptClassService;
	
	public ModelAndView bookSummaryIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		User user = (User) map.get("user");
		boolean all = PriviilegeFilter.isAllowd(user, "/library/summary.do", "all");
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/summary.do", "area")){
			map.put("disableTown", "disabled");
		//本校	
		}else{
			map.put("disableTown", "disabled");
			map.put("disableAgency", "disabled");
			map.put("disableSchool", "disabled");
			map.put("areaSel", "none");
			
		}
		map.put("citySelectHtml", this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(user.getTown_id())));
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(String.valueOf(user.getTown_id()) , user.getAgency_id()));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(String.valueOf(user.getAgency_id()) , user.getSchool_id()));
		
		return new ModelAndView(bookIndexPage, map);
	}
	
	public ModelAndView bookSummaryList(HttpServletRequest req, HttpServletResponse response) throws Exception {
		try {
			Map<String,Object> map=this.getMapWithUser(req);
			User user = (User) map.get("user");
			int currentPage=this.getCurrentPage(req, "page");
			StringBuilder sql = new StringBuilder();
			StringBuilder countSql = new StringBuilder();

			Integer queryCityId = ServletUtil.getIntParam(req, "queryCityId");
			Integer queryAgencyId=ServletUtil.getIntParam(req, "queryAgencyId");
			Integer querySchoolId=ServletUtil.getIntParam(req, "querySchoolId");
			String bookSn  =ServletUtil.removeSpace(req, "bookSn");
			String bookName  =ServletUtil.removeSpace(req, "bookName");
			String bookIndex  =ServletUtil.removeSpace(req, "bookIndex");
			
			countSql.append("select count(distinct book.book_name) ");
			countSql.append(" from library_borrow borrow ");
			countSql.append(" left join library_book book   on borrow.book_id=book.id");
			countSql.append(" left join library_school school on book.school_id=school.id");
			countSql.append(" left join sys_user user on borrow.user_id=user.userid");
			countSql.append(" left join library_agency agency on school.agency_id=agency.id ");
			countSql.append(" left join t_city city on agency.town_id=city.id");
			
			sql.append("select count(*) total ,school.school_name ,book.book_name,book.category_name ,book.book_price,book.book_author,book.id ,book.book_index");
		    sql.append(" from library_borrow borrow ");
		    sql.append(" left join library_book book   on borrow.book_id=book.id");
		    sql.append(" left join library_school school on book.school_id=school.id");
		    sql.append(" left join sys_user user on borrow.user_id=user.userid");
		    sql.append(" left join library_agency agency on school.agency_id=agency.id ");
		    sql.append(" left join t_city city on agency.town_id=city.id");
		    
		    countSql.append(" where book.id is not null ");
		    sql.append(" where  book.id is not null ");
			
			boolean all = PriviilegeFilter.isAllowd(user, "/library/summary.do", "all");
			//本市
			if(all){
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/summary.do", "area")){
				map.put("disableTown", "disabled");
				queryCityId = user.getTown_id();
			//本校	
			}else {
				map.put("disableTown", "disabled");
				map.put("disableAgency", "disabled");
				map.put("disableSchool", "disabled");
				map.put("areaSel", "none");
				querySchoolId = user.getSchool_id();
			}
			if(querySchoolId>0){
				sql.append(" and school.id=");
				sql.append(querySchoolId);
				countSql.append(" and school.id=");
				countSql.append(querySchoolId);
			}else if(queryAgencyId>0){
				sql.append(" and agency.id=");
				sql.append(queryAgencyId);
				countSql.append(" and agency.id=");
				countSql.append(queryAgencyId);
			}else if(queryCityId>0){
				sql.append(" and city.id=");
				sql.append(queryCityId);
				countSql.append(" and city.id=");
				countSql.append(queryCityId);
			}
			if(bookSn!=null&&bookSn.length()>0){
				countSql.append(" and book.book_no like '%");
				countSql.append(bookSn);
				countSql.append("%'");
				sql.append(" and book.book_no like '%");
				sql.append(bookSn);
				sql.append("%'");
				
			}
			if(bookName!=null&&bookName.length()>0){
				countSql.append(" and book.book_name like '%");
				countSql.append(bookName);
				countSql.append("%'");
				sql.append(" and book.book_name like '%");
				sql.append(bookName);
				sql.append("%'");
			}
			if(bookIndex!=null&&bookIndex.length()>0){
				countSql.append(" and book.book_index like '%");
				countSql.append(bookIndex);
				countSql.append("%'");
				sql.append(" and book.book_index like '%");
				sql.append(bookIndex);
				sql.append("%'");
				
			}
			
			sql.append(" group by book.book_name , school.id order by total desc , school.id asc");
			int totalCount = summaryService.summaryCount(countSql.toString());
			List<Object[]> list = summaryService.summary(sql.toString(),currentPage,Page.DEFAULT_PAGE_SIZE);
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
			Page page = new Page(currentPage, totalCount, Page.DEFAULT_PAGE_SIZE, bookList);
			page.setPaginate(this.getAjaxPage(req, currentPage, page,"bookListJump"));
			map.put("page",page);
			return new ModelAndView(bookListPage,map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ModelAndView bookSummaryDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMap();
		String bookName =ServletUtil.removeSpace(request, "bookName");
		map.put("bookName", bookName);
		return new ModelAndView(bookDetailPage,map);
	}
	
	public ModelAndView bookSummaryDetailList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			Map<String,Object> map=this.getMapWithUser(request);
			User user = (User) map.get("user");
			int currentPage=this.getCurrentPage(request, "page");
			String bookName =ServletUtil.removeSpace(request, "bookName");
			Criteria criteria=this.getDomainService().createCriteria(Borrow.class);
			Criteria bookCriteria = criteria.createCriteria("book");
			bookCriteria.add(Restrictions.eq("book_name", bookName));
			criteria.addOrder(Order.desc("id"));
			int queryCityId=0;
			int querySchoolId=0;
			boolean all = PriviilegeFilter.isAllowd(user, "/library/summary.do", "all");
			//本市
			if(all){
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/summary.do", "area")){
				queryCityId = user.getTown_id();
			//本校	
			}else {
				querySchoolId = user.getSchool_id();
			}
			if(querySchoolId>0){
				bookCriteria.add(Restrictions.eq("school.id", querySchoolId));
			}else if(queryCityId>0){
				Criteria schoolCriteria = bookCriteria.createCriteria("school");
				Criteria agencyCriteria = schoolCriteria.createCriteria("agency");
				agencyCriteria.add(Restrictions.eq("city.id", queryCityId));
			}
			
			Page page=this.getDomainService().pagedQuery(criteria, currentPage, Page.DEFAULT_PAGE_SIZE);
			page.setPaginate(this.getAjaxPage(request, currentPage, page,"SummaryDetailJump"));
			map.put("page",page);
			return new ModelAndView(bookDetailListPage,map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public ModelAndView classSummaryIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		User user = (User) map.get("user");
		boolean all = PriviilegeFilter.isAllowd(user, "/library/summary.do", "all");
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/summary.do", "area")){
			map.put("disableTown", "disabled");
		//本校	
		}else{
			map.put("disableTown", "disabled");
			map.put("disableAgency", "disabled");
			map.put("disableSchool", "disabled");
			map.put("areaSel", "none");
			
		}
		map.put("citySelectHtml", this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(user.getTown_id())));
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(String.valueOf(user.getTown_id()) , user.getAgency_id()));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(String.valueOf(user.getAgency_id()) , user.getSchool_id()));
		map.put("editGradeHtmlSelect", this.zfptClassService.getGradeSelectHtml(user.getSchool_id(), -1));
		
		return new ModelAndView(classIndexPage, map);
	}
	
	/**
	 * ----------------------------------------------- 提供给东区教育信息网的页面
	 */
	public ModelAndView zsdqeduClassSummaryIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		User user = (User) map.get("user");
		//boolean all = PriviilegeFilter.isAllowd(user, "/library/summary.do", "all");
		boolean all = true;
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/summary.do", "area")){
			map.put("disableTown", "disabled");
		//本校	
		}else{
			map.put("disableTown", "disabled");
			map.put("disableAgency", "disabled");
			map.put("disableSchool", "disabled");
			map.put("areaSel", "none");
			
		}
		map.put("citySelectHtml", this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(0)));
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(String.valueOf(0) , 18));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(String.valueOf(18) , 0).replaceFirst("请选择", "全部"));
		map.put("editGradeHtmlSelect", this.zfptClassService.getGradeSelectHtml(0, -1));
		
		return new ModelAndView(zsdqeduSummaryIndexPage, map);
	}
	
	public ModelAndView classSummaryList(HttpServletRequest req, HttpServletResponse response) throws Exception {
		try {
			Map<String,Object> map=this.getMapWithUser(req);
			User user = (User) map.get("user");
			int currentPage=this.getCurrentPage(req, "page");
			StringBuilder sql = new StringBuilder();
			StringBuilder countSql = new StringBuilder();

			Integer queryCityId = ServletUtil.getIntParam(req, "queryCityId");
			Integer queryAgencyId=ServletUtil.getIntParam(req, "queryAgencyId");
			Integer querySchoolId=ServletUtil.getIntParam(req, "querySchoolId");
			Integer queryGradeId = ServletUtil.getIntParam(req, "queryGradeId");
			Integer queryClassNameId=ServletUtil.getIntParam(req, "queryClassNameId");
			
			Integer zsdqedu=ServletUtil.getIntParam(req, "zsdqedu");
			
			
			countSql.append("select count(distinct class.id) ");
			countSql.append(" from  library_class class");
			countSql.append(" left join  sys_user user on class.id=user.class_id");
			countSql.append(" left join  library_borrow borrow  on borrow.user_id=user.userid");
			countSql.append(" left join  library_school school on class.school_id=school.id");
			countSql.append(" left join  library_agency agency on school.agency_id=agency.id ");
			countSql.append(" left join  t_city city on agency.town_id=city.id");
			
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
			
			countSql.append(" where  user.user_type=4");
			countSql.append(" and user.status=0");
			countSql.append(" and school.school_name is not null");
			countSql.append(" and borrow.id is not null");
			
			//-------------------------2011-12-12 add for zsdqedu
			boolean all = false;
			if(zsdqedu==1){		
				all = true;
			}else{
				all = PriviilegeFilter.isAllowd(user, "/library/summary.do", "all");
			}
			
			
			//boolean all = PriviilegeFilter.isAllowd(user, "/library/summary.do", "all");
			//本市
			if(all){
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/summary.do", "area")){
				map.put("disableTown", "disabled");
				queryCityId = user.getTown_id();
			//本校	
			}else {
				map.put("disableTown", "disabled");
				map.put("disableAgency", "disabled");
				map.put("disableSchool", "disabled");
				map.put("areaSel", "none");
				querySchoolId = user.getSchool_id();
				
			}
			if(queryGradeId>0){
				sql.append(" and class.grade=");
				sql.append(queryGradeId);
				countSql.append(" and class.grade=");
				countSql.append(queryGradeId);
			}
			if(queryClassNameId>0){
				sql.append(" and class.id=");
				sql.append(queryClassNameId);
				countSql.append(" and class.id=");
				countSql.append(queryClassNameId);
			}
			if(querySchoolId>0){
				sql.append(" and school.id=");
				sql.append(querySchoolId);
				countSql.append(" and school.id=");
				countSql.append(querySchoolId);
			}else if(queryAgencyId>0){
				sql.append(" and agency.id=");
				sql.append(queryAgencyId);
				countSql.append(" and agency.id=");
				countSql.append(queryAgencyId);
			}else if(queryCityId>0){
				sql.append(" and city.id=");
				sql.append(queryCityId);
				countSql.append(" and city.id=");
				countSql.append(queryCityId);
			}
			
			sql.append(" group by class.id order by total desc , school.school_name asc, class.ordergrade asc,class.orderclass asc");
			int totalCount = summaryService.summaryCount(countSql.toString());
			List<Object[]> list = summaryService.summary(sql.toString(),currentPage,Page.DEFAULT_PAGE_SIZE);
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
			Page page = new Page(currentPage, totalCount, Page.DEFAULT_PAGE_SIZE, classList);
			page.setPaginate(this.getAjaxPage(req, currentPage, page,"classListJump"));
			map.put("page",page);
			
			map.put("currentPageSize", Page.DEFAULT_PAGE_SIZE);
			map.put("currentPageNum", currentPage);
			if(zsdqedu==1){
				return new ModelAndView(zsdqeduClassListPage,map);
			}else{			
				return new ModelAndView(classListPage,map);
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ModelAndView classSummaryDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMap();
		String classId =ServletUtil.removeSpace(request, "classId");
		ZfptClass zfptClass = this.zfptClassService.get(Integer.parseInt(classId));
		map.put("schoolName", zfptClass.getSchool().getSchool_name());
		map.put("className", zfptClass.getClass_name());
		map.put("classId", classId);
		return new ModelAndView("/library/summary/summary_class_detail_index",map);
	}
	
	public ModelAndView classSummaryDetailList(HttpServletRequest req, HttpServletResponse response) throws Exception {
		try {
			Map<String,Object> map=this.getMap();
			int currentPage=this.getCurrentPage(req, "page");
			StringBuilder sql = new StringBuilder();
			StringBuilder countSql = new StringBuilder();

			Integer classId = ServletUtil.getIntParam(req, "classId");
			
			countSql.append("select count(distinct user.userid) ");
			countSql.append(" from  sys_user user");
			countSql.append(" left join  library_class class  on class.id=user.class_id");
			countSql.append(" left join  library_borrow borrow  on borrow.user_id=user.userid");
			
			sql.append("select  sum(borrow.id is not null) total , user.loginName ,user.gender,user.realname,user.userid");
			sql.append(" from  sys_user user");
			sql.append(" left join  library_class class  on class.id=user.class_id");
			sql.append(" left join  library_borrow borrow  on borrow.user_id=user.userid");
			sql.append(" left join  library_school school on class.school_id=school.id");
			sql.append(" left join  library_agency agency on school.agency_id=agency.id ");
			sql.append(" left join  t_city city on agency.town_id=city.id");
			
			sql.append(" where  user.user_type=4");
			sql.append(" and user.status=0");
			sql.append(" and borrow.id is not null");

			countSql.append(" where  user.user_type=4");
			countSql.append(" and user.status=0");
			countSql.append(" and borrow.id is not null");
			
			sql.append(" and class.id=");
			sql.append(classId);
			countSql.append(" and class.id=");
			countSql.append(classId);
			
			sql.append(" group by user.userid order by total desc ");
			int totalCount = summaryService.summaryCount(countSql.toString());
			List<Object[]> list = summaryService.summary(sql.toString(),currentPage,Page.DEFAULT_PAGE_SIZE);
			List<StudentSummary> studentList = new ArrayList<StudentSummary>();
			StudentSummary studentSummary =null;
			for(Object[] obj:list){
				studentSummary = new StudentSummary();
				studentSummary.setTotal(String.valueOf(obj[0]));
				studentSummary.setLoginName(String.valueOf(obj[1]));
				studentSummary.setGender(String.valueOf(obj[2]));
				studentSummary.setUser_name(String.valueOf(obj[3]));
				studentSummary.setUser_id(String.valueOf(obj[4]));
				studentList.add(studentSummary);
			}
			Page page = new Page(currentPage, totalCount, Page.DEFAULT_PAGE_SIZE, studentList);
			page.setPaginate(this.getAjaxPage(req, currentPage, page,"studentListJump"));
			map.put("page",page);
			return new ModelAndView("/library/summary/summary_class_detail_list",map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public ModelAndView studentSummaryDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMap();
		String studentId =ServletUtil.removeSpace(request, "studentId");
		IUser student = this.summaryService.getStudent(Integer.parseInt(studentId));
		map.put("schoolName", student.getSchool().getSchool_name());
		map.put("className", student.getZfptClass().getClass_name());
		map.put("studnetName", student.getRealName());
		
		map.put("studentId", student.getUserId());
		return new ModelAndView("/library/summary/summary_student_detail_index",map);
	}
	
	public ModelAndView studentSummaryDetailList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			Map<String,Object> map=this.getMapWithUser(request);
			User user = (User) map.get("user");
			int currentPage=this.getCurrentPage(request, "page");
			System.out.println(request.getParameter("studentId"));
			Integer studentId =ServletUtil.getIntParam(request, "studentId");
			Criteria criteria=this.getDomainService().createCriteria(Borrow.class);
			Criteria userCriteria = criteria.createCriteria("borrowUser");
			Criteria bookCriteria = criteria.createCriteria("book");
			bookCriteria.add(Restrictions.isNotNull("id"));
			userCriteria.add(Restrictions.eq("userId", studentId));
			criteria.addOrder(Order.desc("id"));

			
			Page page=this.getDomainService().pagedQuery(criteria, currentPage, Page.DEFAULT_PAGE_SIZE);
			page.setPaginate(this.getAjaxPage(request, currentPage, page,"SummaryDetailJump"));
			map.put("page",page);
			return new ModelAndView("/library/summary/summary_student_detail_list",map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ModelAndView studentSummaryIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		User user = (User) map.get("user");
		boolean all = PriviilegeFilter.isAllowd(user, "/library/summary.do", "all");
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/summary.do", "area")){
			map.put("disableTown", "disabled");
		//本校	
		}else{
			map.put("disableTown", "disabled");
			map.put("disableAgency", "disabled");
			map.put("disableSchool", "disabled");
			map.put("areaSel", "none");
			
		}
		map.put("citySelectHtml", this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(user.getTown_id())));
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(String.valueOf(user.getTown_id()) , user.getAgency_id()));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(String.valueOf(user.getAgency_id()) , user.getSchool_id()));
		map.put("editGradeHtmlSelect", this.zfptClassService.getGradeSelectHtml(user.getSchool_id(), -1));
		return new ModelAndView(studentIndexPage, map);
	}
	
	public ModelAndView studentSummaryList(HttpServletRequest req, HttpServletResponse response) throws Exception {
		try {
			Map<String,Object> map=this.getMapWithUser(req);
			User user = (User) map.get("user");
			int currentPage=this.getCurrentPage(req, "page");
			StringBuilder sql = new StringBuilder();
			StringBuilder countSql = new StringBuilder();

			Integer queryCityId = ServletUtil.getIntParam(req, "queryCityId");
			Integer queryAgencyId=ServletUtil.getIntParam(req, "queryAgencyId");
			Integer querySchoolId=ServletUtil.getIntParam(req, "querySchoolId");
			Integer queryGradeId = ServletUtil.getIntParam(req, "queryGradeId");
			Integer queryClassNameId=ServletUtil.getIntParam(req, "queryClassNameId");
			String studentName=ServletUtil.removeSpace(req, "studentName");
			
			Integer zsdqedu=ServletUtil.getIntParam(req, "zsdqedu");
			
			countSql.append("select count(distinct user.userid) ");
			countSql.append(" from  sys_user user");
			countSql.append(" left join  library_class class  on class.id=user.class_id");
			countSql.append(" left join  library_borrow borrow  on borrow.user_id=user.userid");
			countSql.append(" left join  library_book book  on borrow.book_id=book.id");
			countSql.append(" left join  library_school school on class.school_id=school.id");
			countSql.append(" left join  library_agency agency on school.agency_id=agency.id ");
			countSql.append(" left join  t_city city on agency.town_id=city.id");
			
			sql.append("select  sum(borrow.id is not null) total ,  school.school_name , class.class_name ,user.realname,user.userid");
			sql.append(" from  sys_user user");
			sql.append(" left join  library_class class  on class.id=user.class_id");
			sql.append(" left join  library_borrow borrow  on borrow.user_id=user.userid");
			sql.append(" left join  library_book book  on borrow.book_id=book.id");
			sql.append(" left join  library_school school on class.school_id=school.id");
			sql.append(" left join  library_agency agency on school.agency_id=agency.id ");
			sql.append(" left join  t_city city on agency.town_id=city.id");
			
			sql.append(" where  user.user_type=4");
			sql.append(" and user.status=0");
			sql.append(" and book.id is not null ");
			sql.append(" and school.school_name is not null");
			sql.append(" and borrow.id is not null");

			
			
			countSql.append(" where  user.user_type=4");
			countSql.append(" and user.status=0");
			countSql.append(" and book.id is not null ");
			countSql.append(" and school.school_name is not null");
			countSql.append(" and borrow.id is not null");
			
			//-------------------------2011-12-12 add for zsdqedu
			boolean all = false;
			if(zsdqedu==1){		
				all = true;
			}else{
				all = PriviilegeFilter.isAllowd(user, "/library/summary.do", "all");
			}
			
			//本市
			if(all){
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/summary.do", "area")){
				map.put("disableTown", "disabled");
				queryCityId = user.getTown_id();
			//本校	
			}else {
				map.put("disableTown", "disabled");
				map.put("disableAgency", "disabled");
				map.put("disableSchool", "disabled");
				map.put("areaSel", "none");
				querySchoolId = user.getSchool_id();
				
			}
			if(queryGradeId>0){
				sql.append(" and class.grade=");
				sql.append(queryGradeId);
				countSql.append(" and class.grade=");
				countSql.append(queryGradeId);
			}
			if(queryClassNameId>0){
				sql.append(" and class.id=");
				sql.append(queryClassNameId);
				countSql.append(" and class.id=");
				countSql.append(queryClassNameId);
			}
			if(studentName!=null&&studentName.length()>0){
				sql.append(" and user.realName like '%");
				sql.append(studentName);
				sql.append("%'");
				
				countSql.append(" and user.realName like '%");
				countSql.append(studentName);
				countSql.append("%'");
			}
			
			if(querySchoolId>0){
				sql.append(" and school.id=");
				sql.append(querySchoolId);
				countSql.append(" and school.id=");
				countSql.append(querySchoolId);
			}else if(queryAgencyId>0){
				sql.append(" and agency.id=");
				sql.append(queryAgencyId);
				countSql.append(" and agency.id=");
				countSql.append(queryAgencyId);
			}else if(queryCityId>0){
				sql.append(" and city.id=");
				sql.append(queryCityId);
				countSql.append(" and city.id=");
				countSql.append(queryCityId);
			}
			
			
			sql.append(" group by user.userid order by total desc , school.school_name asc, class.ordergrade asc,class.orderclass asc");
			int totalCount = summaryService.summaryCount(countSql.toString());
			List<Object[]> list = summaryService.summary(sql.toString(),currentPage,Page.DEFAULT_PAGE_SIZE);
			List<StudentSummary> studentList = new ArrayList<StudentSummary>();
			StudentSummary studentSummary =null;
			for(Object[] obj:list){
				studentSummary = new StudentSummary();
				studentSummary.setTotal(String.valueOf(obj[0]));
				studentSummary.setSchool_name(String.valueOf(obj[1]));
				studentSummary.setClass_name(String.valueOf(obj[2]));
				studentSummary.setUser_name(String.valueOf(obj[3]));
				studentSummary.setUser_id(String.valueOf(obj[4]));
				studentList.add(studentSummary);
			}
			Page page = new Page(currentPage, totalCount, Page.DEFAULT_PAGE_SIZE, studentList);
			page.setPaginate(this.getAjaxPage(req, currentPage, page,"studentListJump"));
			map.put("page",page);
			
			map.put("currentPageSize", Page.DEFAULT_PAGE_SIZE);
			map.put("currentPageNum", currentPage);
			
			if(zsdqedu==1){
				return new ModelAndView(zsdqeduStudentListPage,map);
			}else{					
				return new ModelAndView(studentListPage,map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ModelAndView expiredIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		User user = (User) map.get("user");
		boolean all = PriviilegeFilter.isAllowd(user, "/library/summary.do", "all");
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/summary.do", "area")){
			map.put("disableTown", "disabled");
		//本校	
		}else{
			map.put("disableTown", "disabled");
			map.put("disableAgency", "disabled");
			map.put("disableSchool", "disabled");
			map.put("areaSel", "none");
		}
		map.put("citySelectHtml", this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(user.getTown_id())));
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(String.valueOf(user.getTown_id()) , user.getAgency_id()));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(String.valueOf(user.getAgency_id()) , user.getSchool_id()));
		
		//map.put("categorySelectHtml", bookService.getCategorySelectHtml("-1"));
		return new ModelAndView("/library/summary/borrow_expired_index", map);
	}
	public ModelAndView expiredList(HttpServletRequest req,HttpServletResponse res){
		try {
			Map<String,Object> map=this.getMapWithUser(req);
			int currentPage=this.getCurrentPage(req, "page");
			Criteria criteria=this.getDomainService().createCriteria(Borrow.class);
			Criteria userCriteria = criteria.createCriteria("borrowUser");
			userCriteria.add(Restrictions.isNotNull("id"));
			Criteria schoolCriteria = userCriteria.createCriteria("school");
			Criteria agencyCriteria = schoolCriteria.createCriteria("agency");
			Criteria bookCriteria = criteria.createCriteria("book");
			bookCriteria.add(Restrictions.isNotNull("id"));
			criteria.add(Restrictions.sqlRestriction(" to_days(sysdate()) - to_days(this_.borrow_time)>this_.limit_day"));
			criteria.add(Restrictions.eq("book_status", 1));
			//criteria.add(Restrictions.isEmpty("return_time"));
			criteria.addOrder(Order.desc("id"));
			Integer queryCityId = ServletUtil.getIntParam(req, "queryCityId");
			Integer queryAgencyId=ServletUtil.getIntParam(req, "queryAgencyId");
			Integer querySchoolId=ServletUtil.getIntParam(req, "querySchoolId");
			Integer queryCategoryId=ServletUtil.getIntParam(req, "queryCategoryId");
			String borrowTime =ServletUtil.removeSpace(req, "borrowTime");
			String bookName = ServletUtil.removeSpace(req, "bookName");
			String bookIndex = ServletUtil.removeSpace(req, "bookIndex");
			String userNo = ServletUtil.removeSpace(req, "userNo");
			String userName = ServletUtil.removeSpace(req, "userName");
			//Integer bookStatus=ServletUtil.getIntParam(req, "bookStatus");
			if(borrowTime!=null && borrowTime.length()>0){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				criteria.add(Restrictions.eq("borrow_time", sdf.parse(borrowTime)));
			}
			if(bookName!=null && bookName.length()>0){
				bookCriteria.add(Restrictions.like("book_name", "%"+bookName+"%"));
			}
			if(queryCategoryId != null && queryCategoryId >0){
				Criteria categoryCriteria = criteria.createCriteria("category");
				categoryCriteria.add(Restrictions.or(Restrictions.eq("id", queryCategoryId),Restrictions.eq("parent_id", queryCategoryId)));
			}
			if(bookIndex!=null && bookIndex.length()>0){
				bookCriteria.add(Restrictions.like("book_index", "%"+bookIndex.toUpperCase()+"%"));
			}
			if(userNo!=null && userNo.length()>0){
				userCriteria.add(Restrictions.like("loginName", "%"+userNo+"%"));
			}
			if(userName!=null && userName.length()>0){
				userCriteria.add(Restrictions.like("realName", "%"+userName+"%"));
			}
			//if(bookStatus!=null && bookStatus>=0){
			//	criteria.add(Restrictions.eq("book_status", bookStatus));
			//}
			User user = (User) map.get("user");
			int userid=0;
			boolean all = PriviilegeFilter.isAllowd(user, "/library/summary.do", "all");
			//本市
			if(all){
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/summary.do", "area")){
				queryCityId = user.getTown_id();
			//本校	
			}else if (PriviilegeFilter.isAllowd(user, "/library/summary.do", "unit")){
				querySchoolId = user.getSchool_id();
			}else {
				userid = user.getUserId();//限定用户
			}
			if(userid>0)
				userCriteria.add(Restrictions.eq("userId", user.getUserId()));//限定用户
			else if(querySchoolId!=null&&querySchoolId>0)
				schoolCriteria.add(Restrictions.eq("id", querySchoolId));
			else if(queryAgencyId!=null&&queryAgencyId>0)
				agencyCriteria.add(Restrictions.eq("id", queryAgencyId));
			else if(queryCityId!=null&&queryCityId>0)
				agencyCriteria.add(Restrictions.eq("city.id", queryCityId));
			
			//this.setSqlExpression(req, criteria);
			Page page=this.getDomainService().pagedQuery(criteria, currentPage, 15);
			page.setPaginate(this.getAjaxPage(req, currentPage, page,StringUtil.unCapitalize(this.getDomainName())+"Jump"));
			map.put("page",page);
			return new ModelAndView("/library/summary/borrow_expired_list",map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public ModelAndView downloadExpired(HttpServletRequest req,HttpServletResponse response) throws IOException, ParseException{
		try {
			Map<String,Object> map=this.getMapWithUser(req);
			Criteria criteria=this.getDomainService().createCriteria(Borrow.class);
			Criteria userCriteria = criteria.createCriteria("borrowUser");
			userCriteria.add(Restrictions.isNotNull("id"));
			Criteria schoolCriteria = userCriteria.createCriteria("school");
			Criteria agencyCriteria = schoolCriteria.createCriteria("agency");
			Criteria bookCriteria = criteria.createCriteria("book");
			bookCriteria.add(Restrictions.isNotNull("id"));
			criteria.add(Restrictions.sqlRestriction(" to_days(sysdate()) - to_days(this_.borrow_time)>this_.limit_day"));
			criteria.add(Restrictions.eq("book_status", 1));
			//criteria.add(Restrictions.isEmpty("return_time"));
			criteria.addOrder(Order.desc("id"));
			Integer queryCityId = ServletUtil.getIntParam(req, "queryCityId");
			Integer queryAgencyId=ServletUtil.getIntParam(req, "queryAgencyId");
			Integer querySchoolId=ServletUtil.getIntParam(req, "querySchoolId");
			Integer queryCategoryId=ServletUtil.getIntParam(req, "queryCategoryId");
			String borrowTime =ServletUtil.removeSpace(req, "borrowTime");
			String bookName = ServletUtil.removeSpace(req, "bookName");
			String bookIndex = ServletUtil.removeSpace(req, "bookIndex");
			String userNo = ServletUtil.removeSpace(req, "userNo");
			String userName = ServletUtil.removeSpace(req, "userName");
			//Integer bookStatus=ServletUtil.getIntParam(req, "bookStatus");
			if(borrowTime!=null && borrowTime.length()>0){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				criteria.add(Restrictions.eq("borrow_time", sdf.parse(borrowTime)));
			}
			if(bookName!=null && bookName.length()>0){
				bookCriteria.add(Restrictions.like("book_name", "%"+bookName+"%"));
			}
			if(queryCategoryId != null && queryCategoryId >0){
				Criteria categoryCriteria = criteria.createCriteria("category");
				categoryCriteria.add(Restrictions.or(Restrictions.eq("id", queryCategoryId),Restrictions.eq("parent_id", queryCategoryId)));
			}
			if(bookIndex!=null && bookIndex.length()>0){
				bookCriteria.add(Restrictions.like("book_index", "%"+bookIndex.toUpperCase()+"%"));
			}
			if(userNo!=null && userNo.length()>0){
				userCriteria.add(Restrictions.like("loginName", "%"+userNo+"%"));
			}
			if(userName!=null && userName.length()>0){
				userCriteria.add(Restrictions.like("realName", "%"+userName+"%"));
			}
			//if(bookStatus!=null && bookStatus>=0){
			//	criteria.add(Restrictions.eq("book_status", bookStatus));
			//}
			User user = (User) map.get("user");
			int userid=0;
			boolean all = PriviilegeFilter.isAllowd(user, "/library/summary.do", "all");
			//本市
			if(all){
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/summary.do", "area")){
				queryCityId = user.getTown_id();
			//本校	
			}else if (PriviilegeFilter.isAllowd(user, "/library/summary.do", "unit")){
				querySchoolId = user.getSchool_id();
			}else {
				userid = user.getUserId();//限定用户
			}
			if(userid>0)
				userCriteria.add(Restrictions.eq("userId", user.getUserId()));//限定用户
			else if(querySchoolId!=null&&querySchoolId>0)
				schoolCriteria.add(Restrictions.eq("id", querySchoolId));
			else if(queryAgencyId!=null&&queryAgencyId>0)
				agencyCriteria.add(Restrictions.eq("id", queryAgencyId));
			else if(queryCityId!=null&&queryCityId>0)
				agencyCriteria.add(Restrictions.eq("city.id", queryCityId));
				
				
				List<Borrow> list=criteria.list();
				if(list.size()<1||list.size()>65500){
					String msg=list.size()<1 ?
							"<script>alert('导出的记录数为空，请选择合适的查询条件！');window.history.back();</script>"
							: "<script>alert('导出的记录数超出65500条限制，请选择合适的查询条件以便分批导出！');window.history.back();</script>";
					ServletOutputStream out=response.getOutputStream();
					out.print(msg);
					out.close(); return null;
				}
				HSSFWorkbook workbook = this.summaryService.getBookExport(list,
						req.getSession().getServletContext().getRealPath("/")
						+ "/template/borrow_export_template.xls");
				try {
					ExcelUtils.download(response, workbook, "图书借阅过期数据表.xls");
				} catch (Exception e) {
					e.printStackTrace();
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public void setSummaryService(SummaryService summaryService) {
		this.summaryService = summaryService;
	}

	public void setBookIndexPage(String bookIndexPage) {
		this.bookIndexPage = bookIndexPage;
	}

	public void setBookListPage(String bookListPage) {
		this.bookListPage = bookListPage;
	}

	public void setSchoolService(SchoolService schoolService) {
		this.schoolService = schoolService;
	}

	public void setCityService(CityService cityService) {
		this.cityService = cityService;
	}

	public void setAgencyService(AgencyService agencyService) {
		this.agencyService = agencyService;
	}
	
	public void setZfptClassService(XxptClassService zfptClassService) {
		this.zfptClassService = zfptClassService;
	}

	public void setClassIndexPage(String classIndexPage) {
		this.classIndexPage = classIndexPage;
	}

	public void setClassListPage(String classListPage) {
		this.classListPage = classListPage;
	}

	public void setStudentIndexPage(String studentIndexPage) {
		this.studentIndexPage = studentIndexPage;
	}

	public void setStudentListPage(String studentListPage) {
		this.studentListPage = studentListPage;
	}

	public void setBookDetailPage(String bookDetailPage) {
		this.bookDetailPage = bookDetailPage;
	}

	public void setBookDetailListPage(String bookDetailListPage) {
		this.bookDetailListPage = bookDetailListPage;
	}

	public String getZsdqeduSummaryIndexPage() {
		return zsdqeduSummaryIndexPage;
	}

	public void setZsdqeduSummaryIndexPage(String zsdqeduSummaryIndexPage) {
		this.zsdqeduSummaryIndexPage = zsdqeduSummaryIndexPage;
	}

	public String getZsdqeduClassListPage() {
		return zsdqeduClassListPage;
	}

	public void setZsdqeduClassListPage(String zsdqeduClassListPage) {
		this.zsdqeduClassListPage = zsdqeduClassListPage;
	}

	public String getZsdqeduStudentListPage() {
		return zsdqeduStudentListPage;
	}

	public void setZsdqeduStudentListPage(String zsdqeduStudentListPage) {
		this.zsdqeduStudentListPage = zsdqeduStudentListPage;
	}
	
}
