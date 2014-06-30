package cn.qtone.library.common.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.components.syspurview.check.filter.priviilege.PriviilegeFilter;
import cn.qtone.common.components.syspurview.core.group.domain.UserGroup;
import cn.qtone.common.components.syspurview.core.group.service.UserGroupService;
import cn.qtone.common.components.syspurview.core.user.domain.IUser;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.view.spring.TextView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.library.XxptContants;
import cn.qtone.library.agency.service.AgencyService;
import cn.qtone.library.book.domain.Book;
import cn.qtone.library.book.service.BookService;
import cn.qtone.library.city.service.CityService;
import cn.qtone.library.common.service.CommonService;
import cn.qtone.library.school.service.SchoolService;
import cn.qtone.library.student.service.StudentService;
import cn.qtone.library.techer.service.TecherService;
import cn.qtone.library.xxptclass.domain.ZfptClass;
import cn.qtone.library.xxptclass.service.XxptClassService;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;

/*
 * 此类存放公用方法，不受过虑器，权限限制
 * author 邝炳研
 */
public class CommonController extends SimpleManageController<ZfptClass, XxptClassService> {
	XxptClassService zfptClassService;
	StudentService studentService;
	private String checkLoginPage;
	TecherService techerService;
	private String checkTeacherLoginPage;
	private String categorySelectPage;
	BookService bookService;
	private String userSelectPage;
	private String userListPage;
	private String bookSelectPage;
	private String bookListPage;
	private CommonService commonService;
	private String ajaxPage;
	private String borrowListPage;
	AgencyService agencyService;
	SchoolService schoolService;
	CityService cityService;
	UserGroupService userGroupService;

	public ModelAndView userselect(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		try {
			Map<String, Object> map = this.getMapWithUser(request);
			User user  = (User) map.get("user");
			boolean all = PriviilegeFilter.isAllowd(user, "/library/borrow.do", "all");
			map.put("editGradeHtmlSelect", XxptContants.getGradeSelectHtml("1,2,3,4,5", "-1"));
			//本市
			if(all){
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/borrow.do", "area")){
				map.put("disableTown", "disabled");
			//本校	
			}else{
				map.put("disableTown", "disabled");
				map.put("disableAgency", "disabled");
				map.put("disableSchool", "disabled");
				map.put("editGradeHtmlSelect", this.zfptClassService.getGradeSelectHtml(user.getSchool_id(), -1));
			}
			map.put("citySelectHtml", this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(user.getTown_id())));
			map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(String.valueOf(user.getTown_id()) , user.getAgency_id()));
			map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(String.valueOf(user.getAgency_id()) , user.getSchool_id()));
			map.put("editGradeHtmlSelect", XxptContants.getGradeSelectHtml("1,2,3,4,5", "-1"));
			return new ModelAndView(userSelectPage,map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	@SuppressWarnings("unchecked")
	public ModelAndView userList(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> map = this.getMapWithUser(request);
			int curPage = this.getCurrentPage(request); // 当前查询页数
			Criteria criteria = getDomainService().createCriteria(IUser.class);
			criteria.add(Restrictions.eq("status", 0));//正常用户
			
			Integer queryCityId = ServletUtil.getIntParam(request, "queryCityId");
			Integer queryAgencyId=ServletUtil.getIntParam(request, "queryAgencyId");
			Integer querySchoolId=ServletUtil.getIntParam(request, "querySchoolId");
			Integer queryGradeId=ServletUtil.getIntParam(request, "queryGradeId");
			Integer queryClassNameId=ServletUtil.getIntParam(request, "queryClassNameId");
			String userName  =ServletUtil.removeSpace(request, "userName");
			String userNo  =ServletUtil.removeSpace(request, "userNo");
			String idCard  =ServletUtil.removeSpace(request, "idCard");
			
			
			
			User user = (User) map.get("user");
			boolean all = PriviilegeFilter.isAllowd(user, "/library/borrow.do", "all");
			//本市
			if(all){
				
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/borrow.do", "area")){
				//queryCityId = user.getTown_id();
				//queryAgencyId = user.getAgency_id();
			//本校	
			}else{
				//querySchoolId = user.getSchool_id();
			}
			criteria.add(Restrictions.ne("zfptClass.id", -1));
			if(querySchoolId>0)
				criteria.add(Restrictions.eq("school.id", querySchoolId));
			else if(queryAgencyId>0){
				Criteria schoolCriteria =criteria.createCriteria("school");
				schoolCriteria.add(Restrictions.eq("agency.id", queryAgencyId));
			}else if(queryCityId>0){
				Criteria schoolCriteria =criteria.createCriteria("school");
				Criteria agencyCriteria = schoolCriteria.createCriteria("agency");
				agencyCriteria.add(Restrictions.eq("city.id", queryCityId));
			}
			if(queryGradeId>0)
				criteria.add(Restrictions.eq("grade_id", queryGradeId));
			if(queryClassNameId>0)
				criteria.add(Restrictions.eq("zfptClass.id", queryClassNameId));
			if(userName!=null&&userName.length()>0)
				criteria.add(Restrictions.like("realName", new StringBuilder("%").append(userName).append("%").toString()));
			if(userNo!=null&&userNo.length()>0)
				criteria.add(Restrictions.like("loginName", new StringBuilder("%").append(userNo).append("%").toString()));
			if(idCard!=null&&idCard.length()>0)
				criteria.add(Restrictions.like("id_card", new StringBuilder("%").append(idCard).append("%").toString()));
			
			criteria.addOrder(Order.asc("town_id"));
			criteria.addOrder(Order.asc("agency_id"));
			criteria.addOrder(Order.asc("school.id"));
			criteria.addOrder(Order.asc("grade_id"));
			criteria.addOrder(Order.asc("zfptClass.id"));
			criteria.addOrder(Order.asc("loginName"));
			Page page = getDomainService().pagedQuery(criteria, curPage, Page.DEFAULT_PAGE_SIZE+5);
			page.setPaginate(this.getAjaxPage(request, curPage, page, "user" + "Jump"));
			map.put("page", page);
			
			return new ModelAndView(this.userListPage, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public ModelAndView getUserByCard(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> map = this.getMapWithUser(request);
			
			String id_card = ServletUtil.removeSpace(request, "id_card");
			IUser user = commonService.getUserByCard(id_card);
			if(user == null){
				map.put("ajax","找不到卡号对应的读者！");
				return new ModelAndView(this.ajaxPage, map);
			}
			UserGroup ug = this.userGroupService.getDao().getUserGroup(user.getGroupId());
			StringBuilder ajax = new  StringBuilder();
			ajax.append(user.getUserId());
			ajax.append("◎");
			ajax.append(user.getId_card()==null?" ":user.getId_card());
			ajax.append("◎");
			ajax.append(user.getRealName());
			ajax.append("◎");
			ajax.append(user.getLoginName());
			ajax.append("◎");
			ajax.append(user.uerTypeValue());
			ajax.append("◎");
			ajax.append(user.getGender()==null||user.getGender()==1?"男":"女");
			ajax.append("◎");
			ajax.append(commonService.getUnReturn(user.getUserId()));
			ajax.append("◎");
			ajax.append(ug.getBook_limit());
			ajax.append("◎");
			ajax.append(ug.getDay_limit());
			ajax.append("◎");
			ajax.append(commonService.getExpired(user.getUserId(), ug.getDay_limit()));
			map.put("ajax",ajax.toString());
			return new ModelAndView(this.ajaxPage, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ModelAndView getUser(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> map = this.getMapWithUser(request);
			
			Integer id = ServletUtil.getIntParam(request, "id");
			IUser user = commonService.getUserById(id);
			if(user==null){
				map.put("ajax","找不到该读者！");
				return new ModelAndView(this.ajaxPage, map);
			}
			UserGroup ug = this.userGroupService.getDao().getUserGroup(user.getGroupId());
			StringBuilder ajax = new  StringBuilder();
			ajax.append(user.getUserId());
			ajax.append("◎");
			ajax.append(user.getId_card()==null?" ":user.getId_card());
			ajax.append("◎");
			ajax.append(user.getRealName());
			ajax.append("◎");
			ajax.append(user.getLoginName());
			ajax.append("◎");
			ajax.append(user.uerTypeValue());
			ajax.append("◎");
			ajax.append(user.getGender()==null||user.getGender()==1?"男":"女");
			ajax.append("◎");
			ajax.append(commonService.getUnReturn(user.getUserId()));
			ajax.append("◎");
			ajax.append(ug.getBook_limit());
			ajax.append("◎");
			ajax.append(ug.getDay_limit());
			ajax.append("◎");
			ajax.append(commonService.getExpired(user.getUserId(), ug.getDay_limit()));
			map.put("ajax",ajax.toString());
			return new ModelAndView(this.ajaxPage, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public ModelAndView getBookBybookNo(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> map = this.getMapWithUser(request);
			
			String book_no = ServletUtil.removeSpace(request, "book_no");
			Book book = commonService.getBookBybookNo(book_no);
			if(book==null){
				map.put("ajax","");
				return new ModelAndView(this.ajaxPage, map);
			}
			if(book.getStatus()==0){//0在库；1出借；2遗失；3损坏
				
				StringBuilder ajax = new  StringBuilder();
				ajax.append(book.getId());
				ajax.append("◎");
				ajax.append(book.getSchool().getSchool_name());
				ajax.append("◎");
				ajax.append(book.getBook_price());
				ajax.append("◎");
				ajax.append(book.getBook_place());
				ajax.append("◎");
				ajax.append(book.getBook_name());
				ajax.append("◎");
				ajax.append(book.getBook_index());
				ajax.append("◎");
				ajax.append(book.getCategory_name());
				ajax.append("◎");
				ajax.append(book.getBook_author());
				ajax.append("◎");
				map.put("ajax",ajax.toString());
				
			}else if(book.getStatus()==1){//0在库；1出借；2遗失；3损坏
				map.put("ajax","1");
			}else if(book.getStatus()==2){//0在库；1出借；2遗失；3损坏
				map.put("ajax","2");
			}else if(book.getStatus()==3){//0在库；1出借；2遗失；3损坏
				map.put("ajax","3");
			}
			return new ModelAndView(this.ajaxPage, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public ModelAndView bookselect(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		try {
			Map<String, Object> map = this.getMapWithUser(request);
			
			User user = (User) map.get("user");
			boolean all = PriviilegeFilter.isAllowd(user, "/library/borrow.do", "all");
			//本市
			if(all){
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/borrow.do", "area")){
				map.put("disableTown", "disabled");
			//本校	
			}else{
				map.put("disableTown", "disabled");
				map.put("disableAgency", "disabled");
				map.put("disableSchool", "disabled");
			}
			map.put("citySelectHtml", this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(user.getTown_id())));
			map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(String.valueOf(user.getTown_id()) , user.getAgency_id()));
			map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(String.valueOf(user.getAgency_id()) , user.getSchool_id()));
			
			map.put("categorySelectHtml", bookService.getCategorySelectHtml("-1"));
			
			return new ModelAndView(bookSelectPage,map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	@SuppressWarnings("unchecked")
	public ModelAndView bookList(HttpServletRequest req, HttpServletResponse response) {
		try {
			Map<String, Object> map = this.getMapWithUser(req);
			int currentPage=this.getCurrentPage(req, "page");
			Criteria criteria=this.getDomainService().createCriteria(Book.class);
			criteria.addOrder(Order.asc("book_no"));
			criteria.add(Restrictions.eq("status", 0));//图书状态为在库
			
			Integer queryCityId = ServletUtil.getIntParam(req, "queryCityId");
			Integer queryAgencyId=ServletUtil.getIntParam(req, "queryAgencyId");
			Integer querySchoolId=ServletUtil.getIntParam(req, "querySchoolId");
			Integer queryCategoryId=ServletUtil.getIntParam(req, "queryCategoryId");
			String bookStorageTime =ServletUtil.removeSpace(req, "bookStorageTime");
			String bookName = ServletUtil.removeSpace(req, "bookName");
			String bookIndex = ServletUtil.removeSpace(req, "bookIndex");
			if(bookStorageTime!=null && bookStorageTime.length()>0){
				criteria.add(Restrictions.eq("book_storage_time", bookStorageTime));
			}
			if(bookName!=null && bookName.length()>0){
				criteria.add(Restrictions.like("book_name", "%"+bookName+"%"));
			}
			if(queryCategoryId != null && queryCategoryId >0){
				Criteria categoryCriteria = criteria.createCriteria("category");
				categoryCriteria.add(Restrictions.or(Restrictions.eq("id", queryCategoryId),Restrictions.eq("parent_id", queryCategoryId)));
			}
			if(bookIndex!=null && bookIndex.length()>0){
				criteria.add(Restrictions.like("book_index", "%"+bookIndex.toUpperCase()+"%"));
			}
			
			User user = (User) map.get("user");
			boolean all = PriviilegeFilter.isAllowd(user, "/library/borrow.do", "all");
			//本市
			if(all){
				if(querySchoolId!=null&&querySchoolId>0)
					criteria.add(Restrictions.eq("school.id", querySchoolId));
				else if(queryAgencyId!=null&&queryAgencyId>0){
					Criteria schoolCriteria = criteria.createCriteria("school");
					schoolCriteria.add(Restrictions.eq("agency.id", queryAgencyId));
				}
				else if(queryCityId!=null&&queryCityId>0){
					Criteria schoolCriteria = criteria.createCriteria("school");
					Criteria agencyCriteria = schoolCriteria.createCriteria("city");
					agencyCriteria.add(Restrictions.eq("city.id", queryCityId));
				}
				
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/borrow.do", "area")){
				Criteria schoolCriteria = criteria.createCriteria("school");
				Criteria agencyCriteria = schoolCriteria.createCriteria("city");
				if(querySchoolId!=null&&querySchoolId>0)
					criteria.add(Restrictions.eq("school.id", querySchoolId));
				if(queryAgencyId!=null&&queryAgencyId>0){
					schoolCriteria.add(Restrictions.eq("agency.id", queryAgencyId));
				}
				agencyCriteria.add(Restrictions.eq("city.id", user.getTown_id()));//限定用户所在镇区
			//本校	
			}else{
				criteria.add(Restrictions.eq("school.id", user.getSchool_id()));//限定用户所在学校
			}
			
			//this.setSqlExpression(req, criteria);
			Page page=this.getDomainService().pagedQuery(criteria, currentPage, 15);
			page.setPaginate(this.getAjaxPage(req, currentPage, page,"bookJump"));
			map.put("page",page);
			
			return new ModelAndView(bookListPage, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView borrowList(HttpServletRequest req, HttpServletResponse response) {
		try {
			Map<String, Object> map = this.getMapWithUser(req);
			Criteria criteria=this.getDomainService().createCriteria(Book.class);
			criteria.addOrder(Order.asc("book_no"));
			String bookId = ServletUtil.removeSpace(req, "id");
			String[] bookIds = bookId.split(",");
			Integer[] ids = ServletUtil.parseInteger(bookIds, 0);
			criteria.add(Restrictions.in("id", ids));
			User user = (User) map.get("user");
			boolean all = PriviilegeFilter.isAllowd(user, "/library/borrow.do", "all");
			//本市
			if(all){
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/borrow.do", "area")){
				Criteria schoolCriteria = criteria.createCriteria("school");
				Criteria agencyCriteria = schoolCriteria.createCriteria("city");
				agencyCriteria.add(Restrictions.eq("city.id", user.getTown_id()));//限定用户所在镇区
			//本校	
			}else{
				criteria.add(Restrictions.eq("school.id", user.getSchool_id()));//限定用户所在学校
			}
			
			map.put("list",criteria.list());
			
			return new ModelAndView(borrowListPage, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ModelAndView getClassCodeSelectHtmlByChangeSchoolOrGrade(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		User user  = (User) UserUtil.getUserBean(request);
		return new ModelAndView(new TextView(this.zfptClassService.getClassSelectHtml(user,
				ServletUtil.getIntParam(request, "school_id"), ServletUtil.getIntParam(request, "orderGrade"), ServletUtil.removeSpace(request, "class_code"))));
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView getClassNameSelect(HttpServletRequest request, HttpServletResponse response){
		
		String htmlResult = "";
		User user  = (User) UserUtil.getUserBean(request);
		//String student_index = ServletUtil.removeSpace(request, "student_index");
			htmlResult = this.zfptClassService.getClassNameSelectHtml(user,
					ServletUtil.getIntParam(request, "school_id"), ServletUtil.getIntParam(request, "orderGrade"), -1);
		return new ModelAndView(new TextView(htmlResult));
	}
	
	
	
	public ModelAndView checkLoginName(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> map = this.getMap();
		String type = ServletUtil.removeSpace(request, "type");
		int userId = ServletUtil.getIntParam(request, "userId");
		String loginName = ServletUtil.removeSpace(request, "loginName");
		int userCount;
		if("1".equals(type))
		   userCount=studentService.checkLoginName(loginName);
		else
			userCount=studentService.checkLoginName2(loginName,userId);//编辑检查
		map.put("userCount", userCount);
		return new ModelAndView(this.checkLoginPage,map);
	}
	
	public ModelAndView checkTeacherLoginName(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> map = this.getMap();
		String type = ServletUtil.removeSpace(request, "type");
		int userId = ServletUtil.getIntParam(request, "userId");
		String loginName = ServletUtil.removeSpace(request, "loginName");
		int userCount;
		if("1".equals(type))
			userCount=techerService.checkLoginName(loginName);
		else
			userCount=techerService.checkLoginName2(loginName,userId);//编辑检查
		map.put("userCount", userCount);
		return new ModelAndView(this.checkTeacherLoginPage,map);
	}
	
	public ModelAndView getCategoryTwoSelect(HttpServletRequest req, HttpServletResponse res){
		Map<String, Object> map = this.getMap();
		Integer parent_id=ServletUtil.getIntParam(req, "parent_id");
		map.put("categorySelectHtml",  bookService.getCategoryTwoSelectHtml(parent_id,"-1"));
		return new ModelAndView(getCategorySelectPage(), map);
	}
	
	
	/**
	 * 请求机构列表
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView getAgencySelect(HttpServletRequest req,
			HttpServletResponse res) {
		String htmlResult = this.agencyService.getAgencyOptionHtmlFromCity(
				ServletUtil.removeSpace(req, "city_id"), -1);
		return new ModelAndView(new TextView(htmlResult));
	}
	/**
	 * 请求学校列表
	 */
	public ModelAndView getSchoolSelect(HttpServletRequest req, HttpServletResponse res){
		
		String htmlResult = this.schoolService.getSchoolOptionHtmlByAgency(
				ServletUtil.removeSpace(req, "agency_id") , -1);
		return new ModelAndView(new TextView(htmlResult));
	}

	/**
	 * 请求年级列表
	 * @return
	 */
	public ModelAndView getGradeSelectHtmlByChangeSchool(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if(ServletUtil.getIntParam(request, "school_id") == -1)
			return new ModelAndView(
					new TextView(XxptContants.getGradeSelectHtml("1,2,3,4,5", "#")));
		else
			return new ModelAndView(new TextView(XxptContants.getGradeSelectHtml(
					this.schoolService.get(
							ServletUtil.getIntParam(request, "school_id"))
							.getContainGrade(), "#")));
	}
	
	public void setZfptClassService(XxptClassService zfptClassService) {
		this.zfptClassService = zfptClassService;
	}
	
	public void setStudentService(StudentService studentService) {
		this.studentService = studentService;
	}



	public void setCheckLoginPage(String checkLoginPage) {
		this.checkLoginPage = checkLoginPage;
	}

	public void setCheckTeacherLoginPage(String checkTeacherLoginPage) {
		this.checkTeacherLoginPage = checkTeacherLoginPage;
	}

	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}
	
	public String getCategorySelectPage() {
		return categorySelectPage;
	}

	public void setCategorySelectPage(String categorySelectPage) {
		this.categorySelectPage = categorySelectPage;
	}

	public void setTecherService(TecherService techerService) {
		this.techerService = techerService;
	}

	public void setUserSelectPage(String userSelectPage) {
		this.userSelectPage = userSelectPage;
	}
	
	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}
	public void setAgencyService(AgencyService agencyService) {
		this.agencyService = agencyService;
	}
	public void setSchoolService(SchoolService schoolService) {
		this.schoolService = schoolService;
	}
	public void setCityService(CityService cityService) {
		this.cityService = cityService;
	}
	public void setUserListPage(String userListPage) {
		this.userListPage = userListPage;
	}
	public String getAjaxPage() {
		return ajaxPage;
	}
	public void setAjaxPage(String ajaxPage) {
		this.ajaxPage = ajaxPage;
	}
	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}
	public void setBookSelectPage(String bookSelectPage) {
		this.bookSelectPage = bookSelectPage;
	}
	public void setBookListPage(String bookListPage) {
		this.bookListPage = bookListPage;
	}
	public void setBorrowListPage(String borrowListPage) {
		this.borrowListPage = borrowListPage;
	}

}
