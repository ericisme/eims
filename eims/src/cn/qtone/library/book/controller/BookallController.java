package cn.qtone.library.book.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.components.syspurview.check.filter.priviilege.PriviilegeFilter;
import cn.qtone.common.components.syspurview.core.user.domain.User;
import cn.qtone.common.components.syspurview.login.UserUtil;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.mvc.view.spring.TextView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.common.utils.upload.DownloadInter;
import cn.qtone.common.utils.upload.UploadFactory;
import cn.qtone.library.XxptContants;
import cn.qtone.library.XxptUtil;
import cn.qtone.library.agency.service.AgencyService;
import cn.qtone.library.book.domain.Book;
import cn.qtone.library.book.domain.ErrorBook;
import cn.qtone.library.book.domain.TestBook;
import cn.qtone.library.book.service.BookService;
import cn.qtone.library.borrow.domain.Borrow;
import cn.qtone.library.category.domain.Category;
import cn.qtone.library.city.service.CityService;
import cn.qtone.library.school.domain.School;
import cn.qtone.library.school.service.SchoolService;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;
import cn.qtone.qtoneframework.web.view.poi.ExcelUtils;

/**
 * 图书管理 
 *
 * @author 邝炳研
 * @version 1.0
 */
public class BookallController extends SimpleManageController<Book, BookService> {

	BookService bookService;
	SchoolService schoolService;
	CityService cityService;
	AgencyService agencyService;
	private String categorySelectPage;
	private String nameCodePage;
	private String newPage;
	String importPage;
	
	
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		User user = (User) map.get("user");
		boolean all = PriviilegeFilter.isAllowd(user, "/library/book.do", "all");
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/book.do", "area")){
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
		Map<String,Object> map=this.getMapWithUser(req);
		int currentPage=this.getCurrentPage(req, "page");
		Criteria criteria=this.getDomainService().createCriteria(this.getDomainClass());
		Criteria schoolCriteria =criteria.createCriteria("school");
		criteria.addOrder(Order.desc("book_no"));
		Integer queryCityId = ServletUtil.getIntParam(req, "queryCityId");
		Integer queryAgencyId=ServletUtil.getIntParam(req, "queryAgencyId");
		Integer querySchoolId=ServletUtil.getIntParam(req, "querySchoolId");
				
		System.out.println("##############################################################");
		System.out.println("##############################################################");
		System.out.println("queryCityId:"+queryCityId);System.out.println("queryAgencyId:"+queryAgencyId);System.out.println("querySchoolId:"+querySchoolId);
		System.out.println("##############################################################");
		System.out.println("##############################################################");
		
		
		Integer queryCategoryId=ServletUtil.getIntParam(req, "queryCategoryId");
		String bookStorageTime =ServletUtil.removeSpace(req, "bookStorageTime");
		String bookName = ServletUtil.removeSpace(req, "bookName");
		String bookIndex = ServletUtil.removeSpace(req, "bookIndex");
		
		/** 2011-6-10 add for 可阅读性查询,书籍类型（0：学生图书，1：老师用书，2：期刊杂志），是否废弃图书（0：否，1：是）
		 */
		Integer kydx = ServletUtil.getIntParam(req, "kydx");
			if(kydx==1) criteria.add(Restrictions.like("readability", "%"+"好"+"%"));
			if(kydx==2) criteria.add(Restrictions.like("readability", "%"+"中"+"%"));
			if(kydx==3) criteria.add(Restrictions.like("readability", "%"+"差"+"%"));	
		Integer sjlx = ServletUtil.getIntParam(req, "sjlx");	
			if(sjlx>=0) criteria.add(Restrictions.eq("sjlx", sjlx));
			criteria.add(Restrictions.eq("sffq", 0));
		
		if(bookStorageTime!=null && bookStorageTime.length()>0){
			criteria.add(Restrictions.like("book_storage_time", "%"+bookStorageTime+"%"));
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
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/borrow.do", "area")){
			queryCityId = user.getTown_id();
		//本校	
		}else  {
//			queryCityId = user.getTown_id();
//			queryAgencyId = user.getAgency_id();
//			querySchoolId = user.getSchool_id();
		}
		if(querySchoolId>0){
			schoolCriteria.add(Restrictions.eq("id",querySchoolId));//限定用户所在学校
		}else if(queryAgencyId>0){
			Criteria agencyCriteria = schoolCriteria.createCriteria("agency");
			agencyCriteria.add(Restrictions.eq("id", queryAgencyId));//限定用户所在镇区
		}else if(queryCityId>0){
			Criteria agencyCriteria = schoolCriteria.createCriteria("agency");
			agencyCriteria.add(Restrictions.eq("city.id", queryCityId));//限定用户所在镇区
		}
		
		//this.setSqlExpression(req, criteria);
		Page page=this.getDomainService().pagedQuery(criteria, currentPage, 15);
		page.setPaginate(this.getAjaxPage(req, currentPage, page,StringUtil.unCapitalize(this.getDomainName())+"Jump"));
		map.put("page",page);
		return new ModelAndView(this.getListPage(),map);
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView create(HttpServletRequest req, HttpServletResponse res){
		Map<String, Object> map = this.getMapWithUser(req);
		map.put(getDomainName(), doNewDomain());
		//map.put("parentSelectHtml", this.bookService.getAllBookOptionHtml(-1));
		
		User user = (User) map.get("user");
		boolean all = PriviilegeFilter.isAllowd(user, "/library/book.do", "all");
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/book.do", "area")){
			map.put("disableTown", "disabled");
		//本校	
		}else{
			map.put("disableTown", "disabled");
			map.put("disableAgency", "disabled");
			map.put("disableSchool", "disabled");
		}
		map.put("citySelectHtml", this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(user.getTown_id())));
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(String.valueOf(user.getTown_id()) , user.getAgency_id()));
		map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(String.valueOf(user.getAgency_id()) , user.getSchool_id()==null?-1:user.getSchool_id()));
		
		map.put("categoryHtmlSelect",bookService.getCategorySelectHtml("-1"));
		map.put("bookPrivateHtml", XxptContants.getBookPrivate("0"));
		map.put("statusHtmlSelect", XxptContants.getBookStatus("0"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		map.put("book_storage_time", sdf.format(new java.util.Date()));
		return new ModelAndView(this.getNewPage(), map);
	}
	
	
	public ModelAndView edit(HttpServletRequest req, HttpServletResponse res){
		try {
			Map<String, Object> map = this.getMapWithUser(req);
			Book book=doGetDomain(req);
			map.put(getDomainName(), book);
			map.put("shool_id_old", book.getSchool().getId());
			
			if(book.getCategory()!=null){
				map.put("category_id_old", book.getCategory().getId());
				if(book.getCategory().getParent_id()==null){
					map.put("categoryHtmlSelect",bookService.getCategorySelectHtml(String.valueOf(book.getCategory().getId())));
				}else{
					map.put("categoryTwoHtmlSelect",bookService.getCategoryTwoSelectHtml(book.getCategory().getParent_id(),String.valueOf(book.getCategory().getId())));
					map.put("categoryHtmlSelect",bookService.getCategorySelectHtml(String.valueOf(book.getCategory().getParent_id())));
				}
			}else
				map.put("categoryHtmlSelect",bookService.getCategorySelectHtml("-1"));
			
			User user = (User) map.get("user");
			boolean all = PriviilegeFilter.isAllowd(user, "/library/book.do", "all");
			//本市
			if(all){
			//本镇区	
			}else if (PriviilegeFilter.isAllowd(user, "/library/book.do", "area")){
				map.put("disableTown", "disabled");
			//本校	
			}else{
				map.put("disableTown", "disabled");
				map.put("disableAgency", "disabled");
				map.put("disableSchool", "disabled");
			}
			
			map.put("citySelectHtml", this.cityService.getThirdCityHtmlSelect(XxptContants.ZSSCITYID, String.valueOf(book.getSchool().getAgency().getCity().getId())));
			map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(
					ServletUtil.removeSpace(req, "city_id") , book.getSchool().getAgency().getId()));
			map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(	String.valueOf(book.getSchool().getAgency().getId()), book.getSchool().getId()));
			map.put("bookPrivateHtml", XxptContants.getBookPrivate(String.valueOf(book.getBook_private())));
			map.put("statusHtmlSelect", XxptContants.getBookStatus(String.valueOf(book.getStatus())));
			return new ModelAndView(getEditPage(), map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public ModelAndView save(HttpServletRequest req, HttpServletResponse res){
//		Book book=(Book)this.getCommandObject(req, Book.class);
//		Integer category_two_id=ServletUtil.getIntParamWithNoException(req, "category_two_id");
//		Integer category_id=ServletUtil.getIntParamWithNoException(req, "category_id");
//		Integer shool_id=ServletUtil.getIntParamWithNoException(req, "querySchoolId2");
//		
//		User user = (User) UserUtil.getUserBean(req);
//		boolean all = PriviilegeFilter.isAllowd(user, "/library/book.do", "all");
//		//本市
//		if(all){
//		//本镇区	
//		}else if (PriviilegeFilter.isAllowd(user, "/library/book.do", "area")){
//			
//		//本校	
//		}else{
//			shool_id = user.getSchool_id();
//		}
//		
//		//if(StringUtils.isBlank(req.getParameter("id"))) {//新增
//			if(category_two_id!=null&&category_two_id>0){
//				Category category = this.bookService.get(Category.class, category_two_id);
//				//种次号
//				int countCategory = this.bookService.getCountCategoryNo(category_two_id,shool_id)+1;
//				book.setCategory_no(countCategory);
//				book.setBook_index(category.getCode()+"/"+transNum2Str(countCategory)+"-"+this.schoolService.get(shool_id).getSchool_no());
//				book.setCategory(category);
//				book.setCategory_name(category.getName());
//				book.setCategory_code(category.getCode());
//			}else if(category_id!=null&&category_id>0){
//				Category category = this.bookService.get(Category.class, category_id);
//				//种次号
//				int countCategory = this.bookService.getCountCategoryNo(category_id,shool_id)+1;
//				book.setCategory_no(countCategory);
//				book.setBook_index(category.getCode()+"/"+transNum2Str(countCategory)+"-"+this.schoolService.get(shool_id).getSchool_no());
//				book.setCategory(category);
//				book.setCategory_name(category.getName());
//				book.setCategory_code(category.getCode());
//			}else
//				return new ModelAndView(new AjaxView(true, "请输入图书所在分类,保存失败！"));
//			
//			
//			
//
//			
//			if(shool_id!=null&&shool_id>0){
//				School school = this.schoolService.get(shool_id);
//				//设置图书编号
//				String schoolSn = school.getSchool_no();
//				Book maxBook = bookService.getMaxBook(schoolSn);
//				if(maxBook!=null&&maxBook.getBook_no()!=null){
//					String maxBookNo = maxBook.getBook_no();
//					book.setBook_no(schoolSn+XxptUtil.getBookNumToSn(maxBookNo.substring(3, maxBookNo.length())));
//					System.out.println("book_no = "+book.getBook_no());
//				}else{
//					book.setBook_no(schoolSn+"00000001");
//					System.out.println("book_no = "+book.getBook_no());
//				}
//				book.setSchool(school);
//				/**
//				 * 2011-6-3
//				 */
//				book.setOriginal_school(school);
//			}else
//				return new ModelAndView(new AjaxView(true, "请输入图书所在学校,保存失败！"));
//			this.bookService.save(book);
//		//}
//			
//		return new ModelAndView(new AjaxView(true, "操作成功！"));
//	}
	
//	public ModelAndView saveUpdate(HttpServletRequest req, HttpServletResponse res){//编辑
//		try {
//			Book book=(Book)this.getCommandObject(req, Book.class);
//			String category_two_id=ServletUtil.removeSpace(req, "category_two_id");
//			String category_id=ServletUtil.removeSpace(req, "category_id");
//			Integer shool_id= ServletUtil.getIntParam(req, "querySchoolId2");
//			Integer shool_id_old=ServletUtil.getIntParam(req, "shool_id_old");
//			String category_id_old=ServletUtil.removeSpace(req, "category_id_old");
//			
//			User user = (User) UserUtil.getUserBean(req);
//			boolean all = PriviilegeFilter.isAllowd(user, "/library/book.do", "all");
//			//本市
//			if(all){
//			//本镇区	
//			}else if (PriviilegeFilter.isAllowd(user, "/library/book.do", "area")){
//				
//			//本校	
//			}else{
//				shool_id = user.getSchool_id();
//			}
//			
//			
//			if(category_id_old!=null && category_id_old.length()>0 && (category_id_old.equals(category_two_id)||category_id_old.equals(category_id))){//类型没有改变
//				Integer id = ServletUtil.getIntParam(req, "id");
//				Book book2= this.bookService.get(id);
//				book.setCategory(book2.getCategory());
//				book.setCategory_code(book2.getCategory_code());
//				book.setCategory_name(book2.getCategory_name());
//				book.setCategory_no(book2.getCategory_no());
//				book.setBook_index(book2.getBook_index());
//				book.setBook_no(book2.getBook_no());
//			}else{
//				if( category_two_id!=null&&category_two_id.length()>0){
//					Category category = this.bookService.get(Category.class, Integer.parseInt(category_two_id));
//					//种次号
//					int countCategory = this.bookService.getCountCategoryNo(Integer.parseInt(category_two_id), shool_id)+1;
//					book.setCategory_no(countCategory);
//					book.setBook_index(category.getCode()+"/"+transNum2Str(countCategory)+"-"+this.schoolService.get(shool_id).getSchool_no());
//					book.setCategory(category);
//					book.setCategory_name(category.getName());
//					book.setCategory_code(category.getCode());
//				}else if( category_id!=null&&category_id.length()>0){
//					Category category = this.bookService.get(Category.class, Integer.parseInt(category_id));
//					//种次号
//					int countCategory = this.bookService.getCountCategoryNo(Integer.parseInt(category_id), shool_id)+1;
//					book.setCategory_no(countCategory);
//					book.setBook_index(category.getCode()+"/"+transNum2Str(countCategory)+"-"+this.schoolService.get(shool_id).getSchool_no());
//					book.setCategory(category);
//					book.setCategory_name(category.getName());
//					book.setCategory_code(category.getCode());
//				}else
//					return new ModelAndView(new AjaxView(true, "请输入图书所在分类,保存失败！"));
//			}
//
//			
//			School school = this.schoolService.get(shool_id);
//			book.setSchool(school);
//			if(shool_id!=null&&!String.valueOf(shool_id).equals(String.valueOf(shool_id_old))){//更改学校
//				//School school = this.schoolService.get(Integer.parseInt(shool_id));
//				//设置图书编号
//				String schoolSn = school.getSchool_no();
//				Book maxBook = bookService.getMaxBook(schoolSn);
//				if(maxBook!=null&&maxBook.getBook_no()!=null){
//					String maxBookNo = maxBook.getBook_no();
//					book.setBook_no(schoolSn+XxptUtil.getBookNumToSn(maxBookNo.substring(3, maxBookNo.length())));
//				}else{
//					book.setBook_no(schoolSn+"00000001");
//				}
//				
//			}
//			
//			if(book.getStatus()==2){//损失操作，变为归还状态
//				Borrow borrow =null;
//				borrow = this.bookService.getBorrowByBookId(book.getId());
//				if(borrow != null){
//					borrow.setBook_status(0);
//					borrow.setReturn_time(new java.util.Date());
//					this.bookService.saveOrUpdate(borrow);//更新为在库
//				}
//				
//			}
//			
//			this.bookService.getHibernateTemplate().merge(book);
//			return new ModelAndView(new AjaxView(true, "操作成功！"));
//		
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//		
//	}
	@Override
	public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
		Map<String, Object> map = this.getMapWithUser(req);
		Book book=doGetDomain(req);
		map.put(getDomainName(), book);
		map.put("shool_id_old", book.getSchool().getId());
		if(book.getCategory()!=null){
			if(book.getCategory().getParent_id()==null){
				map.put("categoryHtmlSelect",book.getCategory().getName());
			}else{
				map.put("categoryTwoHtmlSelect",book.getCategory().getName());
				map.put("categoryHtmlSelect",bookService.getCategory(book.getCategory().getParent_id()).getName());
			}
		}
		map.put("citySelectHtml", book.getSchool().getAgency().getCity().getName());
		map.put("agencySelectHtml", book.getSchool().getAgency().getAgency_name());
		map.put("schoolHtmlSelect", book.getSchool().getSchool_name());
		map.put("bookPrivateHtml", XxptContants.BOOKPRIVATE.get(String.valueOf(book.getBook_private())));
		map.put("statusHtmlSelect", XxptContants.BOOKSTATUS.get(String.valueOf(book.getStatus())));
		return new ModelAndView(getShowPage(), map);
	}
	/**
	 * 删除
	 */
	public ModelAndView remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] idStrs = request.getParameterValues(idName);
		if (log.isInfoEnabled()) {
			log.info("要删除的对象ID为：" + StringUtil.join(idStrs, ","));
		}
		int i=0;
		for(String idStr : idStrs){
			if(idStr.length() > 0){
				Integer id=(Integer)ConvertUtils.convert(idStr, idClass);
				getDomainService().removeById(id);
			}
		}
		AjaxView view = new AjaxView(true, i>0?i+"个机构有下属学校,未删除!":"操作成功！");
		view.setProperty("refresh", true);
		return new ModelAndView(view);
	}
	public ModelAndView getCategoryTwoSelect(HttpServletRequest req, HttpServletResponse res){
		Map<String, Object> map = this.getMap();
		Integer parent_id=ServletUtil.getIntParam(req, "parent_id");
		map.put("categorySelectHtml",  bookService.getCategoryTwoSelectHtml(parent_id,"-1"));
		return new ModelAndView(getCategorySelectPage(), map);
	}
	public ModelAndView getCategoryNameCode(HttpServletRequest req, HttpServletResponse res){
		Map<String, Object> map = this.getMap();
		Integer id=ServletUtil.getIntParam(req, "id");
		Category category = bookService.getCategory(id);
		map.put("nameCode", category.getName()+","+category.getCode());
		return new ModelAndView(getNameCodePage(), map);
	}
	/**
	 * 导出数据
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView downloadBook(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Criteria criteria=this.getDomainService().createCriteria(this.getDomainClass());
		/**
		 * new
		 */
		criteria.addOrder(Order.desc("book_no"));
		//this.setSqlExpression(request, criteria);
		Integer querySchoolId=ServletUtil.getIntParam(request, "querySchoolId");
		Integer queryCategoryId=ServletUtil.getIntParam(request, "queryCategoryId");
		String bookStorageTime =ServletUtil.removeSpace(request, "bookStorageTime");
		String bookName = ServletUtil.removeSpace(request, "bookName");
		String bookIndex = ServletUtil.removeSpace(request, "bookIndex");
		if(bookStorageTime!=null&&bookStorageTime.length()>0){
			criteria.add(Restrictions.like("book_storage_time", "%"+bookStorageTime+"%"));
		}
		if(bookName!=null&&bookName.length()>0){
			criteria.add(Restrictions.like("book_name", "%"+bookName+"%"));
		}
		if(bookIndex!=null&&bookIndex.length()>0){
			criteria.add(Restrictions.like("book_index", "%"+bookIndex.toUpperCase()+"%"));
		}
		
		/** 2011-6-10 add for 可阅读性查询,书籍类型（0：学生图书，1：老师用书，2：期刊杂志），是否废弃图书（0：否，1：是）
		 */
		Integer kydx = ServletUtil.getIntParam(request, "kydx");
			if(kydx==1) criteria.add(Restrictions.like("readability", "%"+"好"+"%"));
			if(kydx==2) criteria.add(Restrictions.like("readability", "%"+"中"+"%"));
			if(kydx==3) criteria.add(Restrictions.like("readability", "%"+"差"+"%"));	
		Integer sjlx = ServletUtil.getIntParam(request, "sjlx");	
			if(sjlx>=0) criteria.add(Restrictions.eq("sjlx", sjlx));
			criteria.add(Restrictions.eq("sffq", 0));
		
		if(queryCategoryId!=null&&queryCategoryId>0){
			Criteria categoryCriteria = criteria.createCriteria("category");
			categoryCriteria.add(Restrictions.or(Restrictions.eq("id", queryCategoryId),Restrictions.eq("parent_id", queryCategoryId)));
		}
		
		
		User user = (User) UserUtil.getUserBean(request);
		boolean all = PriviilegeFilter.isAllowd(user, "/library/book.do", "all");
		//本市
		if(all){
			if(querySchoolId!=null&&querySchoolId>0)
				criteria.add(Restrictions.eq("school.id", querySchoolId));
			
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/book.do", "area")){
			if(querySchoolId!=null&&querySchoolId>0)
				criteria.add(Restrictions.eq("school.id", querySchoolId));
		//本校	
		}else{
			criteria.add(Restrictions.eq("school.id", user.getSchool_id()));//限定用户所在学校
		}
		
		
		
		List<Book> list=criteria.list();
		if(list.size()<1||list.size()>65500){
			String msg=list.size()<1 ?
					"<script>alert('导出的记录数为空，请选择合适的查询条件！');window.history.back();</script>"
					: "<script>alert('导出的记录数超出65500条限制，请选择合适的查询条件以便分批导出！');window.history.back();</script>";
			ServletOutputStream out=response.getOutputStream();
			out.print(msg);
			out.close(); return null;
		}
		HSSFWorkbook workbook = this.bookService.getBookExport(list,
				request.getSession().getServletContext().getRealPath("/")
						+ "/template/book_export_template.xls");
		try {
			ExcelUtils.download(response, workbook, "图书数据表.xls");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 导出打印条形码、打印索书号数据
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView downloadPrint(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Criteria criteria=this.getDomainService().createCriteria(this.getDomainClass());
		/**
		 * new
		 */
		criteria.addOrder(Order.desc("book_no"));
		//this.setSqlExpression(request, criteria);
		Integer querySchoolId=ServletUtil.getIntParam(request, "querySchoolId");
		Integer queryCategoryId=ServletUtil.getIntParam(request, "queryCategoryId");
		String bookStorageTime =ServletUtil.removeSpace(request, "bookStorageTime");
		String bookName = ServletUtil.removeSpace(request, "bookName");
		String bookIndex = ServletUtil.removeSpace(request, "bookIndex");
		if(bookStorageTime!=null&&bookStorageTime.length()>0){
			criteria.add(Restrictions.like("book_storage_time", "%"+bookStorageTime+"%"));
		}
		if(bookName!=null&&bookName.length()>0){
			criteria.add(Restrictions.like("book_name", "%"+bookName+"%"));
		}
		if(bookIndex!=null&&bookIndex.length()>0){
			criteria.add(Restrictions.like("book_index", "%"+bookIndex.toUpperCase()+"%"));
		}
		
		/** 2011-6-10 add for 可阅读性查询,书籍类型（0：学生图书，1：老师用书，2：期刊杂志），是否废弃图书（0：否，1：是）
		 */
		Integer kydx = ServletUtil.getIntParam(request, "kydx");
			if(kydx==1) criteria.add(Restrictions.like("readability", "%"+"好"+"%"));
			if(kydx==2) criteria.add(Restrictions.like("readability", "%"+"中"+"%"));
			if(kydx==3) criteria.add(Restrictions.like("readability", "%"+"差"+"%"));	
		Integer sjlx = ServletUtil.getIntParam(request, "sjlx");	
			if(sjlx>=0) criteria.add(Restrictions.eq("sjlx", sjlx));
			criteria.add(Restrictions.eq("sffq", 0));
		
		
		
		if(queryCategoryId!=null&&queryCategoryId>0){
			Criteria categoryCriteria = criteria.createCriteria("category");
			categoryCriteria.add(Restrictions.or(Restrictions.eq("id", queryCategoryId),Restrictions.eq("parent_id", queryCategoryId)));
		}
		
		
		User user = (User) UserUtil.getUserBean(request);
		boolean all = PriviilegeFilter.isAllowd(user, "/library/book.do", "all");
		//本市
		if(all){
			if(querySchoolId!=null&&querySchoolId>0)
				criteria.add(Restrictions.eq("school.id", querySchoolId));
			
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/book.do", "area")){
			if(querySchoolId!=null&&querySchoolId>0)
				criteria.add(Restrictions.eq("school.id", querySchoolId));
		//本校	
		}else{
			criteria.add(Restrictions.eq("school.id", user.getSchool_id()));//限定用户所在学校
		}
		
		
		
		List<Book> list=criteria.list();
		if(list.size()<1||list.size()>65500){
			String msg=list.size()<1 ?
					"<script>alert('导出的记录数为空，请选择合适的查询条件！');window.history.back();</script>"
					: "<script>alert('导出的记录数超出65500条限制，请选择合适的查询条件以便分批导出！');window.history.back();</script>";
			ServletOutputStream out=response.getOutputStream();
			out.print(msg);
			out.close(); return null;
		}
		HSSFWorkbook workbook = this.bookService.getBookPrintExport(list,
				request.getSession().getServletContext().getRealPath("/")
						+ "/template/book_export_print_template.xls");
		try {
			ExcelUtils.download(response, workbook, "图书条形码索书号打印数据表.xls");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 导入初始化
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView importBookInit(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> map = this.getMapWithUser(request);
		//String agencySelectHtml = this.agencyService.getAgencyOptionHtmlFromCity("-1", -1);
		//map.put("agencySelectHtml", agencySelectHtml);

		//map.put("schoolHtmlSelect", this.schoolService.getSchoolOptionHtmlByAgency(ServletUtil.removeSpace(request,
		//		"agency_id"), -1));
		User user = (User) map.get("user");
		boolean all = PriviilegeFilter.isAllowd(user, "/library/book.do", "all");
		//本市
		if(all){
		//本镇区	
		}else if (PriviilegeFilter.isAllowd(user, "/library/book.do", "area")){
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
		return new ModelAndView(this.importPage, map);
	}

	/**
	 * 模板下载
	 */
	public ModelAndView download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		DownloadInter download = UploadFactory.getDownloadInstance(response);
		try {
			
			download.download(request.getSession().getServletContext().getRealPath("/")
					+ "/template/book_import_template.xls", "UTF-8");
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			return new ModelAndView(new TextView("未知原因导致文件下载失败！"));
		}
		return null;
	}
	
	public void setErrorBook(ErrorBook errorBook, Book book, List<ErrorBook> errorList, Integer line) {
		errorBook.setLine(line);
		errorBook.setBook_name(book.getBook_name());
		errorBook.setBook_isbn(book.getBook_isbn());
		errorBook.setCategory_code(book.getCategory_code());
		errorBook.setCategory_name(book.getCategory_name());
		errorBook.setBook_price(String.valueOf(book.getBook_price()));
		errorBook.setStatus(XxptContants.BOOKSTATUS.get(book.getStatus()));
		errorBook.setBook_place(book.getBook_place());
		errorBook.setBook_author(book.getBook_author());
		errorBook.setBook_storage_time(book.getBook_storage_time());
		errorBook.setBook_publish(book.getBook_publish());
		errorBook.setBook_publish_place(book.getBook_publish_place());
		errorBook.setBook_publish_year(book.getBook_publish_year());
		errorBook.setBook_private(XxptContants.BOOKPRIVATE.get(book.getBook_private()));
		errorBook.setBook_content(book.getBook_content());
		errorBook.setBook_page(book.getBook_page());
		errorBook.setBook_format(book.getBook_format());
		errorBook.setBook_language(book.getBook_language());
		errorBook.setBook_goods(book.getBook_goods());
		errorBook.setBook_publish_time(book.getBook_publish_time());
		errorBook.setBook_congshu(book.getBook_congshu());
		errorBook.setPrintface(book.getPrintface());
		errorBook.setContent(book.getContent());
		//errorBook.setErrorReason("学籍号出现重复，请检查是否有误！");
		errorList.add(errorBook);
	}
	
	/**
	 * 导入学生后的提交操作.
	 * @throws IOException 
	 */
//	public ModelAndView importBookSubmit(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		List<ErrorBook> errorList = new ArrayList<ErrorBook>();
//		String nowDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
//		ErrorBook errorBook = null;
//		//List<Book> bookList = new ArrayList<Book>(); 
//		List<Integer> bookIds = new ArrayList<Integer>(); 
//		int i=2;
//		try {
//			for (Book book :  this.bookService.getImportBookData(request.getSession().getServletContext().getRealPath("/")+ ServletUtil.removeSpace(request, "importattach_path"))) {
//				i++;
//				/**
//				 * 获得当前schoolId
//				 */
//				Integer schoolId = ServletUtil.getIntParam(request, "importSchoolId");
//				User user = (User) UserUtil.getUserBean(request);
//				boolean all = PriviilegeFilter.isAllowd(user, "/library/book.do", "all");
//				//本市
//				if(all){
//				//本镇区	
//				}else if (PriviilegeFilter.isAllowd(user, "/library/book.do", "area")){
//					
//				//本校	
//				}else{
//					schoolId = user.getSchool_id();
//				}
//				
//
//				try{
//					/**
//					 * 2011-6-1
//					 */
//					book.setStatus_str("在库");
//					if (book.getBook_name() == null ||book.getBook_name().trim().length()<1){
//						errorBook = new ErrorBook();
//						errorBook.setErrorReason("请检查图书名称是否有误！");
//						setErrorBook(errorBook,book,errorList, i);
//						continue;
//					}
//					
//					if (book.getCategory_code() == null ||book.getCategory_code().trim().length()<1){
//						errorBook = new ErrorBook();
//						errorBook.setErrorReason("请检查图书分类号是否有误！");
//						setErrorBook(errorBook,book,errorList, i);
//						continue;
//					}
//					if (book.getBook_price() == null ||book.getBook_price()<0.001){
//						errorBook = new ErrorBook();
//						errorBook.setErrorReason("请检查图书价格是否有误！");
//						setErrorBook(errorBook,book,errorList, i);
//						continue;
//					}
//					
//					book.setStatus(book.setStatusStrToInt());
//					book.setBook_private(book.setBook_privateStrToInt());
//					if (book.getStatus() == null ||book.getStatus()<0){
//						errorBook = new ErrorBook();
//						errorBook.setErrorReason("请检查图书状态是否有误！");
//						setErrorBook(errorBook,book,errorList, i);
//						continue;
//					}
//					
//					
//					if (book.getBook_place() == null ||book.getBook_place().trim().length()<1){
//						errorBook = new ErrorBook();
//						errorBook.setErrorReason("请检查图书存放位置是否有误！");
//						setErrorBook(errorBook,book,errorList, i);
//						continue;
//					}
//					
//						Category category = this.bookService.getCategoryByCode(book.getCategory_code().toUpperCase());
//						if (category == null) {
//							errorBook = new ErrorBook();
//							errorBook.setErrorReason("请检查图书分类号是否有误！系统无法找到对应分类！");
//							setErrorBook(errorBook,book,errorList, i);
//							continue;
//						}
//						//种次号
//						int countCategory = this.bookService.getCountCategoryNo(category.getId(),schoolId)+1;
//						book.setCategory_no(countCategory);
//						book.setBook_index(category.getCode()+"/"+transNum2Str(countCategory)+"-"+schoolService.get(schoolId).getSchool_no());
//						book.setCategory(category);
//						book.setCategory_name(category.getName());
//						book.setCategory_code(category.getCode());
//					
//
//					School school = schoolService.get(schoolId);
//					if(school == null){
//						errorBook = new ErrorBook();
//						errorBook.setErrorReason("请检查所选择学校是否有误！");
//						setErrorBook(errorBook,book,errorList, i);
//						continue;
//					}
//					String schoolSn = school.getSchool_no();
//					Book maxBook = bookService.getMaxBook(schoolSn);
//					if(maxBook!=null&&maxBook.getBook_no()!=null){
//						String maxBookNo = maxBook.getBook_no();
//						book.setBook_no(schoolSn+XxptUtil.getBookNumToSn(maxBookNo.substring(3, maxBookNo.length())));
//					}else{
//						book.setBook_no(schoolSn+"00000001");
//					}
//					book.setSchool(school);
//					/**
//					 * 2011-6-3
//					 */
//					book.setOriginal_school(school);
//					
//					if(book.getBook_storage_time()==null||"".equals(book.getBook_storage_time()))
//						book.setBook_storage_time(nowDateTime);
//					//保存测试
//					
//					this.bookService.save(book);
//					bookIds.add(book.getId());
//					//bookList.add(book);
//				}catch(Exception ex){
//					errorBook = new ErrorBook();
//					errorBook.setErrorReason("无法保存！请检查Excel数据是否按要求填写！");
//					setErrorBook(errorBook,book,errorList, i);
//					//this.bookService.delBath(bookId);//删除保存的。。
//					ex.printStackTrace();
//				}
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//			ServletOutputStream out = response.getOutputStream();
//			out.print("<script>alert('数据导入失败！');window.history.back();</script>");
//			out.close();
//			return  null;
//		}
//		//AjaxView view=null;
//		String msg=null;
//		if(errorList.size()>0){
//			if(bookIds.size()>0){
//				StringBuilder idStr = new StringBuilder();
//				for (Integer id:bookIds){
//					idStr.append(id);
//					idStr.append(",");
//				}
//				idStr.delete(idStr.length()-1, idStr.length());
//				this.bookService.delBath(idStr.toString());//删除保存的。。
//			}
//		
//			Map<String, Object> map = this.getMap();
//			map.put("errorList", errorList);
//			return new ModelAndView("/library/book/book_error_list", map);//显示错误提示页面
//			
//			//view = new AjaxView(true, "部分导入失败！请下载导入失败名单进行修改！");
//		/*	try {
//			HSSFWorkbook workbook = this.bookService.getBookErrorExport(errorList, request.getSession().getServletContext()
//					.getRealPath("/")
//					+ "/template/book_error_template.xls");
//				ExcelUtils.download(response, workbook, "导入失败图书数据表.xls");
//				msg = "请下载“导入失败图书数据表” 修改完继续上传";
//			} 
//			catch (Exception e) {
//				e.printStackTrace();
//			}*/
//			
//			
//		}else
//			/*
//			try{
//				this.bookService.saveBath(bookList);
//			}catch (Exception e) {
//				e.printStackTrace();
//				ServletOutputStream out = response.getOutputStream();
//				out.print("<script>alert('数据导入失败！');window.history.back();</script>");
//				out.close();
//				return  null;
//			}
//			*/
//			//view = new AjaxView(true, "导入成功！");
//			//view.setProperty("refresh", true);
//		msg = "<script>alert('数据导入成功！');window.history.back();</script>";
//		ServletOutputStream out = response.getOutputStream();
//		out.print(msg);
//		out.close();
//		return  null;
//	}
//	
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
	
	
	/**
	 * 把数字变成带0的字符，，比如把数字387变成00000387
	 * @param i
	 * @return
	 */
	private static String transNum2Str(Integer i){
		String i_no = String.valueOf(i);
		Integer times = 8-i_no.length();
		for(int j=0;j<times;j++){
			i_no = "0"+i_no;
		}
		return i_no;
	}
	
	

	public void setBookService(BookService bookService) {
		this.bookService = bookService;
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
	public String getNewPage() {
		return newPage;
	}
	public void setNewPage(String newPage) {
		this.newPage = newPage;
	}
	
	public String getImportPage() {
		return importPage;
	}
	public void setImportPage(String importPage) {
		this.importPage = importPage;
	}
}

