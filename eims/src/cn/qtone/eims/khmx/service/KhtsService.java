package cn.qtone.eims.khmx.service;

import java.util.List;
import java.util.Map;

import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.simplemvc.dao.HibernateSimpleDao;
import cn.qtone.eims.khmx.dao.KhtsDAO;
import cn.qtone.eims.khmx.domain.Khts;

public class KhtsService extends HibernateSimpleDao<Khts>{

	private KhtsDAO dao;

	public void setDao(KhtsDAO dao) {
		this.dao = dao;
	}
	
	public void query(Page page,int curPage, String bgdh, String khmc, String ywy,
			String ksrq, String jsrq, String gsmc) {
		int start = page.getPageSize() * (curPage - 1) + 1;
		page.setStartIndex(start);
		
		dao.query(page, bgdh, khmc, ywy, ksrq, jsrq, gsmc);
	}
	
	public List<Map<String, Object>> findAll(String bgdh, String khmc, String ywy,
			String ksrq, String jsrq, String gsmc){
		return dao.findAll(bgdh, khmc, ywy, ksrq, jsrq, gsmc);
	}
}
