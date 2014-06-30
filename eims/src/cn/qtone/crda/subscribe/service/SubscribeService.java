package cn.qtone.crda.subscribe.service;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import cn.qtone.common.simplemvc.dao.HibernateSimpleDao;
import cn.qtone.crda.subscribe.domain.Subscribe;

/**
 * 订阅管理 - 订阅管理
 *
 * @author 贺少辉
 * @version 1.0
 */
public class SubscribeService extends HibernateSimpleDao<Subscribe> {

	/**
	 * 查询正常状态的订阅记录
	 */
	@SuppressWarnings("unchecked")
	public List<Subscribe> querySubscribe(){
		Criteria criteria = this.createCriteria(Subscribe.class);
		criteria.add(Restrictions.eq("subscribeStatus",1));
		return criteria.list();
	}
}