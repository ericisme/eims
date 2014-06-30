	var pag = null;
	var baseUrl = '/library/main.do';
	
	// 图书信息查询
	function bookTotalQuery() {
		if (pag == null)
			pag = new PageExe();
		var param="";
		pag.query(baseUrl + '?action=bookTotalList', 'desk_book',param);
	}
	
	
	
	// 图书信息查询
	function unReturnQuery() {
		if (pag == null)
			pag = new PageExe();
		var param="";
		pag.query(baseUrl + '?action=unReturnList', 'desk_unreturn',param);
	}
	
	function bookSummaryQuery() {
		if (pag == null)
			pag = new PageExe();
		var param="";
		pag.query(baseUrl + '?action=bookSummaryList', 'desk_book_summary',param);
	}
	
	function classSummaryQuery() {
		if (pag == null)
			pag = new PageExe();
		var param="";
		pag.query(baseUrl + '?action=classSummaryList', 'desk_class_summary',param);
	}
	
	
	

	
	
	
	
	
