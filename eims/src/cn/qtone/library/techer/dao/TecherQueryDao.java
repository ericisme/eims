package cn.qtone.library.techer.dao;

import java.util.List;

import cn.qtone.common.components.syspurview.core.user.domain.IUser;
import cn.qtone.library.techer.qvo.TecherQVO;

/**
 * @author 贺少辉.<br>
 *         学生查询数据持久化接口.
 * 
 */
public interface TecherQueryDao {

	/**
	 * 根据学生查询对象分页查询学生列表.
	 */
	List<IUser> pageQueryTecher(TecherQVO qvo);
	
	/**
	 * 根据学生查询对象查询学生列表.
	 */
	List<IUser> noPagequeryTecher(TecherQVO qvo);

	/**
	 * 根据学生查询对象查询学生数量.
	 */
	int queryTecherCount(TecherQVO qvo);
	
	/**
	 * 根据学生ID,查询学生详细信息
	 * @param qvo
	 * @return
	 */
	IUser queryTecherForObject(TecherQVO qvo);
	
	/**
	 * 获取新的学生唯一标识号
	 */
	int getNewStudnetUnique();
	
	/**
	 * @param 更新查出来的号码的值
	 */
	void updateTecher(String ids);
}
