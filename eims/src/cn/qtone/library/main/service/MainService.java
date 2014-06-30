package cn.qtone.library.main.service;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import cn.qtone.common.simplemvc.dao.HibernateSimpleDao;
import cn.qtone.library.book.domain.Book;

/**
 * 桌面管理 - <br>
 * 
 * @author 邝炳研
 * @version 1.0
 */
public class MainService extends HibernateSimpleDao<Book> {
	
	public List sqlQuerytoList(final String sql) {
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session arg0) throws HibernateException, SQLException {
                Query q = arg0.createSQLQuery(sql);
                q.setMaxResults(1);      //每次只取maxResult 条数据。
                return q.list();
            }
        });
       return list;
	}
	
	
	public List summary(final String sql,final int size) {
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(Session arg0) throws HibernateException, SQLException {
                Query q = arg0.createSQLQuery(sql);
                q.setMaxResults(size);      //每次只取maxResult 条数据。
                return q.list();
            }
        });
       return list;
	}
}
