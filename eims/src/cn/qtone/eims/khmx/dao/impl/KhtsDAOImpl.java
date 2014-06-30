package cn.qtone.eims.khmx.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.qtone.common.mvc.dao.BaseDAO;
import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.utils.base.DBUtil;
import cn.qtone.eims.khmx.dao.KhtsDAO;

public class KhtsDAOImpl extends BaseDAO implements KhtsDAO{

	@Override
	public void query(Page page, String bgdh, String khmc, String ywy,
			String ksrq, String jsrq, String gsmc) {
		StringBuffer sql = new StringBuffer();
		StringBuffer countSql = new StringBuffer();
		
		sql.append("select a.*,b.GSMC from eims_khts a LEFT join eims_fkzf b on a.BGDH = b.BGDH WHERE 1 = 1");
		if(!StringUtils.isBlank(bgdh))
			sql.append(" AND a.BGDH = '").append(bgdh).append("'");
		if(!StringUtils.isBlank(khmc))
			sql.append(" AND a.KHMC LIKE '%").append(khmc).append("%'");
		if(!StringUtils.isBlank(ywy))
			sql.append(" AND a.YWY LIKE '%").append(ywy).append("%'");
		if(!StringUtils.isBlank(ksrq))
			sql.append(" AND a.bgrq >=TO_DATE('").append(ksrq).append("', 'yyyy-MM-dd hh24:mi:ss')");
		if(!StringUtils.isBlank(jsrq))
			sql.append(" AND a.bgrq <=TO_DATE('").append(jsrq).append("', 'yyyy-MM-dd hh24:mi:ss')");
		if(!StringUtils.isBlank(gsmc))
			sql.append(" AND b.gsmc LIKE '%").append(gsmc).append("%'");
		sql.append(" order by a.id");
		
		countSql.append("SELECT COUNT(*) FROM (").append(sql).append(") as total");
		sql = DBUtil.mysqlExePageQuery(sql,"", page.getStartIndex(), page.getPageSize());
		
		page.setResult(getSimpleJdbcTemplate().queryForList(sql.toString()));
		page.setTotals(queryCount(countSql.toString()));
	}

	private Integer queryCount(String sql){
		return this.getJdbcTemplate().queryForInt(sql);
	}

	@Override
	public List<Map<String, Object>> findAll(String bgdh, String khmc,
			String ywy, String ksrq, String jsrq, String gsmc) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("select a.*,b.GSMC from eims_khts a LEFT join eims_fkzf b on a.BGDH = b.BGDH WHERE 1 = 1");
		if(!StringUtils.isBlank(bgdh))
			sql.append(" AND a.BGDH = '").append(bgdh).append("'");
		if(!StringUtils.isBlank(khmc))
			sql.append(" AND a.KHMC LIKE '%").append(khmc).append("%'");
		if(!StringUtils.isBlank(ywy))
			sql.append(" AND a.YWY LIKE '%").append(ywy).append("%'");
		if(!StringUtils.isBlank(ksrq))
			sql.append(" AND a.bgrq >=TO_DATE('").append(ksrq).append("', 'yyyy-MM-dd hh24:mi:ss')");
		if(!StringUtils.isBlank(jsrq))
			sql.append(" AND a.bgrq <=TO_DATE('").append(jsrq).append("', 'yyyy-MM-dd hh24:mi:ss')");
		if(!StringUtils.isBlank(gsmc))
			sql.append(" AND b.gsmc LIKE '%").append(gsmc).append("%'");
		sql.append(" order by a.id");
		
		return getSimpleJdbcTemplate().queryForList(sql.toString());
	}
}
