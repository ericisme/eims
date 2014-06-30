package cn.qtone.library.borrow.service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import cn.qtone.common.components.syspurview.core.user.domain.IUser;
import cn.qtone.common.simplemvc.dao.HibernateSimpleDao;
import cn.qtone.library.book.domain.Book;
import cn.qtone.library.borrow.domain.Borrow;

/**
 * 借阅管理 - 借书管理
 *
 * @author 邝炳研
 * @version 1.0
 */
public class BorrowService extends HibernateSimpleDao<Borrow> {
	
	/**
	 *
	 */
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public IUser getUserById(Integer id){
		try {
			return (IUser) this.getHibernateTemplate().get(IUser.class, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void bathSave(final List<Borrow> borrowList) {
		try {
			this.getHibernateTemplate().execute(new HibernateCallback(){
				public Object doInHibernate(Session session) throws HibernateException, SQLException{
					try {
						for(int i=0;i<borrowList.size();i++){
							session.save(borrowList.get(i));
							if(i%15==0){
								session.flush();
								session.clear();
							}
						}
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
	
	public void updateBookStatus(final Integer id, final Integer status){
		try {
			this.getHibernateTemplate().execute(new HibernateCallback(){
				public Object doInHibernate(Session session) throws HibernateException, SQLException{
					try {
						org.hibernate.Query q = session.createQuery("update Book set status=:status where id=:id");
							q.setParameter("status", status);
							q.setParameter("id", id);
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
	
	public void updateBookStatus(final String idstr, final Integer status){
		
		try {
			this.getHibernateTemplate().execute(new HibernateCallback(){
				public Object doInHibernate(Session session) throws HibernateException, SQLException{
					try {
						org.hibernate.Query q = session.createQuery("update Book set status=:status where id in ("+idstr+")");
							q.setParameter("status", status);
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
	
	
	public Book getBookByNo(String bookNo){
		try {
			Criteria criteria = this.createCriteria(Book.class);
			criteria.add(Restrictions.eq("book_no", bookNo));
			List<Book> list = criteria.list();
			return list!=null&&list.size()>0?list.get(0):null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Borrow getBorrowByBookId(final Integer bookId){
		try {
		//	List<Borrow> list = getHibernateTemplate().executeFind(new HibernateCallback() {
	     //       public Object doInHibernate(Session arg0) throws HibernateException, SQLException {
	     //           org.hibernate.Query q = arg0.createQuery("select bw from  Borrow bw left outer join Book bo on  bw.book.id=bo.id where bo.id=:id and bw.return_time is null order by bw.id desc");
	      //          q.setParameter("id", bookId);
	     //           q.setCacheable(true);
	     //           return q.list();
	     //       }
	     //   });
	    //	return  list!=null&&list.size()>0?list.get(0):null;
			
			
			
			
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
	
	//还书操作
	public void updateBorrowStatus(final Integer id, final Integer status){
		try {
			this.getHibernateTemplate().execute(new HibernateCallback(){
				public Object doInHibernate(Session session) throws HibernateException, SQLException{
					try {
						org.hibernate.Query q = session.createQuery("update Borrow set book_status=:status ,set return_time=:return_time where book.id=:id and return_time is null");
							q.setParameter("status", status);
							q.setParameter("id", id);
							q.setTimestamp("return_time",new java.util.Date());
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
	
	//计算日期相差天数
	public static long calendarDayPlus(Date d1,Date d2) 
	{ 
		long milis=d1.getTime() -d2.getTime() ;
		milis=milis/1000/60/60/24; 
		return milis; 
	} 
	
	

	
}


