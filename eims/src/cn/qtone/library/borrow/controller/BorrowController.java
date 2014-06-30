package cn.qtone.library.borrow.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.components.syspurview.check.filter.priviilege.PriviilegeFilter;
import cn.qtone.common.components.syspurview.core.group.domain.UserGroup;
import cn.qtone.common.components.syspurview.core.group.service.UserGroupService;
import cn.qtone.common.components.syspurview.core.user.domain.IUser;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.mvc.view.spring.TextView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.library.XxptContants;
import cn.qtone.library.XxptUtil;
import cn.qtone.library.agency.service.AgencyService;
import cn.qtone.library.book.domain.Book;
import cn.qtone.library.book.service.BookService;
import cn.qtone.library.borrow.domain.Borrow;
import cn.qtone.library.borrow.service.BorrowService;
import cn.qtone.library.city.service.CityService;
import cn.qtone.library.common.service.CommonService;
import cn.qtone.library.school.service.SchoolService;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;

/**
 * 图书管理 
 * @author 邝炳研
 * @version 1.0
 */
public class BorrowController extends SimpleManageController<Borrow, BorrowService> {

	BorrowService borrowService;
	BookService bookService;
	SchoolService schoolService;
	CityService cityService;
	AgencyService agencyService;
	UserGroupService userGroupService;
	CommonService commonService;
	private String categorySelectPage;
	private String nameCodePage;
	private String borrowPage;
	private String returnPage;
	private String ajaxPage;
	
	
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
			map.put("areaSel", "none");
			
		}
		map.put("citySelectHtml", this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(user.getTown_id())));
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(String.valueOf(user.getTown_id()) , user.getAgency_id()));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(String.valueOf(user.getAgency_id()) , user.getSchool_id()));
		
		map.put("categorySelectHtml", bookService.getCategorySelectHtml("-1"));
		return new ModelAndView(getIndexPage(), map);
	}
	public ModelAndView list(HttpServletRequest req,HttpServletResponse res){
		try {
			Map<String,Object> map=this.getMapWithUser(req);
			int currentPage=this.getCurrentPage(req, "page");
			Criteria criteria=this.getDomainService().createCriteria(this.getDomainClass());
			Criteria userCriteria = criteria.createCriteria("borrowUser");
			Criteria schoolCriteria = userCriteria.createCriteria("school");
			Criteria bookCriteria = criteria.createCriteria("book");
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
			Integer bookStatus=ServletUtil.getIntParam(req, "bookStatus");
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
			if(bookStatus!=null && bookStatus>=0){
				criteria.add(Restrictions.eq("book_status", bookStatus));
			}
			User user = (User) map.get("user");
			int userId=0;
			boolean all = PriviilegeFilter.isAllowd(user, "/library/borrow.do", "all");
			//本市
			if(all){
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/borrow.do", "area")){
				queryCityId = user.getTown_id();
			//本校	
			}else  if (PriviilegeFilter.isAllowd(user, "/library/borrow.do", "unit")){
				queryCityId = user.getTown_id();
				queryAgencyId = user.getAgency_id();
				querySchoolId = user.getSchool_id();
			}else{
				userId = user.getUserId();
				
			}
			if(userId>0){
				userCriteria.add(Restrictions.eq("id", userId));//限定用户所在学校
			}else if(querySchoolId>0){
				schoolCriteria.add(Restrictions.eq("id",querySchoolId));//限定用户所在学校
			}else if(queryAgencyId>0){
				Criteria agencyCriteria = schoolCriteria.createCriteria("agency");
				agencyCriteria.add(Restrictions.eq("id", user.getTown_id()));//限定用户所在镇区
			}else if(queryCityId>0){
				Criteria agencyCriteria = schoolCriteria.createCriteria("agency");
				agencyCriteria.add(Restrictions.eq("city.id", user.getTown_id()));//限定用户所在镇区
			}
			
			
			
			//this.setSqlExpression(req, criteria);
			Page page=this.getDomainService().pagedQuery(criteria, currentPage, 15);
			page.setPaginate(this.getAjaxPage(req, currentPage, page,StringUtil.unCapitalize(this.getDomainName())+"Jump"));
			map.put("page",page);
			return new ModelAndView(this.getListPage(),map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	/**
	public ModelAndView expiredIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
			map.put("areaSel", "none");
		}
		map.put("citySelectHtml", this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(user.getTown_id())));
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(String.valueOf(user.getTown_id()) , user.getAgency_id()));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(String.valueOf(user.getAgency_id()) , user.getSchool_id()));
		
		//map.put("categorySelectHtml", bookService.getCategorySelectHtml("-1"));
		return new ModelAndView(this.expiredIndexPage, map);
	}
	public ModelAndView expiredList(HttpServletRequest req,HttpServletResponse res){
		try {
			Map<String,Object> map=this.getMapWithUser(req);
			int currentPage=this.getCurrentPage(req, "page");
			Criteria criteria=this.getDomainService().createCriteria(this.getDomainClass());
			Criteria userCriteria = criteria.createCriteria("borrowUser");
			Criteria schoolCriteria = userCriteria.createCriteria("school");
			Criteria agencyCriteria = schoolCriteria.createCriteria("agency");
			Criteria bookCriteria = criteria.createCriteria("book");
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
			return new ModelAndView(this.expiredListPage,map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	*/
	
	
	/**
	 * 删除
	 */
	public ModelAndView remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] idStrs = request.getParameterValues(idName);
		if (log.isInfoEnabled()) {
			log.info("要删除的对象ID为：" + StringUtil.join(idStrs, ","));
		}
		for(String idStr : idStrs){
			if(idStr.length() > 0){
				Integer id=(Integer)ConvertUtils.convert(idStr, idClass);
				getDomainService().removeById(id);
			}
		}
		AjaxView view = new AjaxView(true, "操作成功！");
		view.setProperty("refresh", true);
		return new ModelAndView(view);
	}
	 

	public ModelAndView borrow(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		return new ModelAndView(borrowPage, map);
	}
	
	public ModelAndView returnBook(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		return new ModelAndView(returnPage, map);
	}
	public ModelAndView borrowSubmit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		try {
			User user = (User) map.get("user");
			Integer userId =ServletUtil.getIntParam(request, "userId");
			String bookIds =ServletUtil.removeSpace(request, "bookIds");
			IUser borrowUser = this.borrowService.getUserById(userId);
			if(borrowUser==null ){
				map.put("ajax","借阅失败，未找到对应读者信息！");
				return new ModelAndView(ajaxPage, map);
			}
			
			if(bookIds==null ||bookIds.length()<1){
				map.put("ajax","借阅失败，请输入图书信息！");
				return new ModelAndView(ajaxPage, map);
			}
			
			IUser operaUser = new IUser();
			operaUser.setUserId(user.getUserId());
			Book book = null;
			Borrow borrow = null;
			String[] bookId = bookIds.split(",");
			bookId = XxptUtil.array_unique(bookId);//去除重复ID
			UserGroup ug = this.userGroupService.getDao().getUserGroup(borrowUser.getGroupId());
			Integer unReturn =  commonService.getUnReturn(userId);
			if(ug.getBook_limit() - unReturn - bookId.length <0){
				map.put("ajax",new StringBuilder("借阅失败，该读者有").append(unReturn).append("本图书未还，最多可以再借").append(ug.getBook_limit() - unReturn).append("本！").toString());
				return new ModelAndView(ajaxPage, map);
			}
			List<Borrow> list = new ArrayList<Borrow>();
			StringBuilder  bookIdStr = new StringBuilder();
			for(String id:bookId){
				bookIdStr.append(id);
				bookIdStr.append(",");
				borrow = new Borrow();
				book = new Book();
				book.setId(Integer.parseInt(id));
				borrow.setBook(book);
				borrow.setStatus(0);//可借状态
				borrow.setBorrow_time(new java.util.Date());
				borrow.setBook_status(1);//借出状态
				borrow.setBorrowUser(borrowUser);
				borrow.setOperaUser(operaUser);
				borrow.setLimit_day(ug.getDay_limit());
				list.add(borrow);
				
			}
			this.borrowService.bathSave(list);
			this.borrowService.updateBookStatus(bookIdStr.substring(0, bookIdStr.length()-1),1);
			map.put("ajax","借阅成功！");
			return new ModelAndView(ajaxPage, map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("ajax","借阅失败，请检查输入信息是否有错！");
		}
		return new ModelAndView(ajaxPage, map);
		
	}
	
	public ModelAndView returnSubmit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		StringBuilder ajax= new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			//User user = (User) map.get("user");
			String bookNo =ServletUtil.removeSpace(request, "bookNo");
			Book book = this.borrowService.getBookByNo(bookNo);
			if(book==null){
				map.put("ajax","0◎系统没有该图书信息！");
				return new ModelAndView(ajaxPage, map);
			}				
			/** 2011-6-8,增加还书条件，对于学校级图书管理员用户，只能接收归还本校的图书。
			 */
			User user = (User) map.get("user");
			boolean all = PriviilegeFilter.isAllowd(user, "/library/borrow.do", "all");
			//本市
			if(all){
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/borrow.do", "area")){
			//本校	
			}else  if (PriviilegeFilter.isAllowd(user, "/library/borrow.do", "unit")){				
				if(!user.getSchool_id().equals(book.getSchool().getId())){
					System.out.println("本图书不是本校借出图书，请到 "+book.getSchool().getSchool_name()+" 归还!");
					
					map.put("ajax","0◎本图书不是本校借出图书，请到 "+book.getSchool().getSchool_name()+" 归还!");
					return new ModelAndView(ajaxPage, map);
				}
			}else{}
			/** 
			 */			
			Borrow borrow =null;
			borrow = borrowService.getBorrowByBookId(book.getId());
			if(borrow==null){
				map.put("ajax","0◎【"+book.getBook_name()+"】该图书已归还！");
				this.borrowService.updateBookStatus(book.getId(),0);//更新为在库	
				return new ModelAndView(ajaxPage, map);
			}		
				
			borrow.setBook_status(0);
			borrow.setReturn_time(new java.util.Date());
			this.borrowService.saveOrUpdate(borrow);//更新为在库
			//borrowService.updateBorrowStatus(book.getId(),0);
			this.borrowService.updateBookStatus(book.getId(),0);//更新为在库	
			//map.put("ajax","0,还书操作失败！");
			IUser iuser = borrow.getBorrowUser();
			UserGroup ug = this.userGroupService.getDao().getUserGroup(iuser.getGroupId());
			ajax.append("1");
			ajax.append("◎");
			ajax.append("【"+book.getBook_name()+"】还书成功！");
			ajax.append("◎");
			ajax.append(borrow.getId());
			ajax.append("◎");
			ajax.append(sdf.format(borrow.getBorrow_time()));
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
			ajax.append(iuser.getId_card()==null?" ":iuser.getId_card());
			ajax.append("◎");
			ajax.append(iuser.getLoginName());
			ajax.append("◎");
			ajax.append(iuser.getGender()==null||iuser.getGender()==1?"男":"女");
			ajax.append("◎");
			ajax.append(iuser.getIsSuper()==1?-1:ug.getBook_limit());
			ajax.append("◎");
			ajax.append(iuser.getRealName());
			ajax.append("◎");
			ajax.append(iuser.uerTypeValue());
			ajax.append("◎");
			ajax.append(commonService.getUnReturn(iuser.getUserId())-1);
			ajax.append("◎");
			ajax.append(iuser.getIsSuper()==1?-1:ug.getDay_limit());
			ajax.append("◎");
			/*
			 * 检查过期天数
			 */
			long day = borrowService.calendarDayPlus(new Date(),borrow.getBorrow_time());
			ajax.append(iuser.getIsSuper()==1?0:day-ug.getDay_limit());//过期天数
			ajax.append("◎");
			map.put("ajax",ajax.toString());
			return new ModelAndView(ajaxPage, map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("ajax","0◎还书操作失败！");
		}
		return new ModelAndView(ajaxPage, map);
		
	}

	public ModelAndView downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		try {
			String file = ServletUtil.removeSpace(request, "file");
			
			String fileName;
			if(file.lastIndexOf("/")>0)
				fileName = file.substring(file.lastIndexOf("/"), file.length());
			else
				fileName = file.substring(file.lastIndexOf("\\"), file.length());
			String filePath =request.getSession().getServletContext().getRealPath("/")+File.separator+file;
			XxptUtil.download(filePath, fileName, request, response);
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView(new TextView("未知原因导致文件下载失败！"));
		}
		return null;
	}

	public void setBorrowService(BorrowService borrowService) {
		this.borrowService = borrowService;
	}


	public void setSchoolService(SchoolService schoolService) {
		this.schoolService = schoolService;
	}
	public String getCategorySelectPage() {
		return categorySelectPage;
	}
	public void setCategorySelectPage(String categorySelectPage) {
		this.categorySelectPage = categorySelectPage;
	}
	public void setCityService(CityService cityService) {
		this.cityService = cityService;
	}
	public void setAgencyService(AgencyService agencyService) {
		this.agencyService = agencyService;
	}
	public String getNameCodePage() {
		return nameCodePage;
	}
	public void setNameCodePage(String nameCodePage) {
		this.nameCodePage = nameCodePage;
	}
	
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}
	public void setAjaxPage(String ajaxPage) {
		this.ajaxPage = ajaxPage;
	}

	public void setReturnPage(String returnPage) {
		this.returnPage = returnPage;
	}

	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public void setBorrowPage(String borrowPage) {
		this.borrowPage = borrowPage;
	}
}

