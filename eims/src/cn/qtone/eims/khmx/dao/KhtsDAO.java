package cn.qtone.eims.khmx.dao;

import java.util.List;
import java.util.Map;

import cn.qtone.common.mvc.dao.Page;

public interface KhtsDAO {

	public void query(Page page, String bgdh, String khmc, String ywy, String ksrq, String jsrq, String gsmc);
	
	public List<Map<String, Object>> findAll(String bgdh, String khmc, String ywy,
			String ksrq, String jsrq, String gsmc);
}
