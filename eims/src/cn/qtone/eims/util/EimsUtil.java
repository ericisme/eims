package cn.qtone.eims.util;

import cn.qtone.common.mvc.dao.Page;

public class EimsUtil {

	
	public static boolean ifLastPage(int curPage, Page page){
		int totalPages = 1;
		if (page.getTotals() / page.DEFAULT_PAGE_SIZE < 1) {
			totalPages = 1;
		} else {
			totalPages = page.getTotals() / page.DEFAULT_PAGE_SIZE;
			if ((page.getTotals() % page.DEFAULT_PAGE_SIZE) != 0) {
				totalPages += 1;
			}
		}
		
		return totalPages==curPage;
	}
}
