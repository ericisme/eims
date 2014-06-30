package cn.qtone.library.common.service;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cn.qtone.common.components.syspurview.core.user.domain.IUser;
import cn.qtone.common.simplemvc.dao.HibernateSimpleDao;
import cn.qtone.library.book.domain.Book;
import cn.qtone.library.borrow.domain.Borrow;
import cn.qtone.library.xxptclass.domain.ZfptClass;

/**
 * 基础管理 - 公用业务类.<br>
 * 
 * @author 邝炳研
 * @version 1.0
 */
public class CommonService extends HibernateSimpleDao<ZfptClass> {
	
	public IUser getUserById(Integer id){
		try {
			return (IUser)this.getHibernateTemplate().get(IUser.class, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public IUser getUserByCard(String id_card){
		try {
			Criteria criteria = this.createCriteria(IUser.class);
			criteria.add(Restrictions.eq("id_card", id_card));
			List<IUser> list = criteria.list();
			return list!=null&&list.size()>0?list.get(0):null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public Book getBookBybookNo(String book_no){
		try {
			Criteria criteria = this.createCriteria(Book.class);
			criteria.add(Restrictions.eq("book_no", book_no));
			List<Book> list = criteria.list();
			return list!=null&&list.size()>0?list.get(0):null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public Book getBookById(Integer id){
		try {
			return (Book)this.getHibernateTemplate().get(Book.class, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	/*
	 * 计算一个用户外借总数
	 */
	public Integer getUnReturn(Integer userId){
		Criteria criteria = this.createCriteria(Borrow.class);
		criteria.add(Restrictions.eq("book_status", 1));
		criteria.add(Restrictions.eq("borrowUser.userId", userId));
		return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
	}
	/*
	 * 计算读者过期借阅数
	 */
	public Integer getExpired(Integer userId,Integer limit_day){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new java.util.Date());
		calendar.add(Calendar.DAY_OF_MONTH,0-limit_day);
		Criteria criteria = this.createCriteria(Borrow.class);
		criteria.add(Restrictions.eq("book_status", 1));
		criteria.add(Restrictions.lt("borrow_time", calendar.getTime()));
		criteria.add(Restrictions.eq("borrowUser.userId", userId));
		return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
	}
	
	
}
