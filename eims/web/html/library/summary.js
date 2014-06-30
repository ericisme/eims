	var pag = null;
	var baseUrl = '/library/summary.do';
	
	
	// 图书信息统计
	function bookSummaryQuery() {
		if (pag == null)
			pag = new PageExe();
		var param="";
		if($F('queryCityId')!=null && $F('queryCityId')!=-1)
			param+="&queryCityId="+$F('queryCityId');
		if($F('queryAgencyId')!=null && $F('queryAgencyId')!=-1)
			param+="&queryAgencyId="+$F('queryAgencyId');
		if($F('querySchoolId')!=null && $F('querySchoolId')!=-1)
			param+="&querySchoolId="+$F('querySchoolId');
		
		if($F('bookSn')!=null && $F('bookSn')!='')
			param+="&bookSn="+$F('bookSn');
		if($F('bookIndex')!=null && $F('bookIndex')!='')
			param+="&bookIndex="+$F('bookIndex');
		if($F('bookName')!=null && $F('bookName')!='')
			param+="&bookName="+new BaseClass().encode($F('bookName'), true);
		
		pag.query(baseUrl + '?action=bookSummaryList', 'summary_list',param);
	}
	
	// 分页查询方法
	function bookListJump(/*string*/_url, /*int*/curPage)
	{
		pag.query(
			_url + curPage,
			'summary_list'
		);
	}
	
	// 班级信息统计
	function classSummaryQuery() {
		if (pag == null)
			pag = new PageExe();
		var param="";
		if($F('queryCityId')!=null && $F('queryCityId')!=-1)
			param+="&queryCityId="+$F('queryCityId');
		if($F('queryAgencyId')!=null && $F('queryAgencyId')!=-1)
			param+="&queryAgencyId="+$F('queryAgencyId');
		if($F('querySchoolId')!=null && $F('querySchoolId')!=-1)
			param+="&querySchoolId="+$F('querySchoolId');
		if($F('queryGradeId')!=null && $F('queryGradeId')!=-1)
			param+="&queryGradeId="+$F('queryGradeId');
		if($F('queryClassNameId')!=null && $F('queryClassNameId')!=-1)
			param+="&queryClassNameId="+$F('queryClassNameId');
		
		
		pag.query(baseUrl + '?action=classSummaryList', 'class_summary_list',param);
	}
	
	
	
	// -----------------------班级信息统计  (提供给东区教育信息网的页面)2011-12-12
	function zsdqeduClassSummaryQuery() {
		if (pag == null)
			pag = new PageExe();
		var param="";
		if($F('queryCityId1')!=null && $F('queryCityId1')!=-1)
			param+="&queryCityId="+$F('queryCityId1');
		if($F('queryAgencyId1')!=null && $F('queryAgencyId1')!=-1)
			param+="&queryAgencyId="+$F('queryAgencyId1');
		if($F('querySchoolId1')!=null && $F('querySchoolId1')!=-1)
			param+="&querySchoolId="+$F('querySchoolId1');
		if($F('queryGradeId1')!=null && $F('queryGradeId1')!=-1)
			param+="&queryGradeId="+$F('queryGradeId1');
		if($F('queryClassNameId1')!=null && $F('queryClassNameId1')!=-1)
			param+="&queryClassNameId="+$F('queryClassNameId1');		
			
			param+="&zsdqedu=1"
		pag.query(baseUrl + '?action=classSummaryList', 'class_summary_list',param);
	}	
	
	// 分页查询方法
	function classListJump(/*string*/_url, /*int*/curPage)
	{
		pag.query(
			_url + curPage,
			'class_summary_list'
		);
	}
		// -----------------------学生借阅统计 (提供给东区教育信息网的页面)2011-12-12
	function zsdqeduStudentSummaryQuery() {
		if (pag == null)
			pag = new PageExe();
		var param="";
		if($F('queryCityId')!=null && $F('queryCityId')!=-1)
			param+="&queryCityId="+$F('queryCityId');
		if($F('queryAgencyId')!=null && $F('queryAgencyId')!=-1)
			param+="&queryAgencyId="+$F('queryAgencyId');
		if($F('querySchoolId')!=null && $F('querySchoolId')!=-1)
			param+="&querySchoolId="+$F('querySchoolId');
		if($F('queryGradeId')!=null && $F('queryGradeId')!=-1)
			param+="&queryGradeId="+$F('queryGradeId');
		if($F('queryClassNameId')!=null && $F('queryClassNameId')!=-1)
			param+="&queryClassNameId="+$F('queryClassNameId');
		if($F('studentName')!=null && $F('studentName')!='')
			param+="&studentName="+new BaseClass().encode($F('studentName'), true);
		
		param+="&zsdqedu=1"
		
		pag.query(baseUrl + '?action=studentSummaryList', 'summary_list',param);
	}
	
	
	// 学生借阅统计
	function studentSummaryQuery() {
		if (pag == null)
			pag = new PageExe();
		var param="";
		if($F('queryCityId')!=null && $F('queryCityId')!=-1)
			param+="&queryCityId="+$F('queryCityId');
		if($F('queryAgencyId')!=null && $F('queryAgencyId')!=-1)
			param+="&queryAgencyId="+$F('queryAgencyId');
		if($F('querySchoolId')!=null && $F('querySchoolId')!=-1)
			param+="&querySchoolId="+$F('querySchoolId');
		if($F('queryGradeId')!=null && $F('queryGradeId')!=-1)
			param+="&queryGradeId="+$F('queryGradeId');
		if($F('queryClassNameId')!=null && $F('queryClassNameId')!=-1)
			param+="&queryClassNameId="+$F('queryClassNameId');
		if($F('studentName')!=null && $F('studentName')!='')
			param+="&studentName="+new BaseClass().encode($F('studentName'), true);
		pag.query(baseUrl + '?action=studentSummaryList', 'summary_list',param);
	}
	
	// 分页查询方法
	function studentListJump(/*string*/_url, /*int*/curPage)
	{
		pag.query(
			_url + curPage,
			'summary_list'
		);
	}
	
	function bookSummaryDetail(bookName)
	{   
		URL="/library/summary.do?action=bookSummaryDetail&bookName="+new BaseClass().encode(bookName, true);
		loc_x=document.body.scrollLeft+event.clientX-event.offsetX+0;
		loc_y=document.body.scrollTop+event.clientY-event.offsetY+0;
		//window.open(URL);
	  	window.showModalDialog(URL,self,"edge:raised;scroll:yes;status:0;help:0;resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
	}
	
	function bookSummaryDetailQuery(){
		if (pag == null)
				pag = new PageExe();
			var param="";
		if($F('bookName')!=null && $F('bookName')!="")
			param+="&bookName="+new BaseClass().encode($F('bookName'), true);
		pag.query(baseUrl + '?action=bookSummaryDetailList', 'summary_list',param);
	}
	
	
	function classSummaryDetail(classId)
	{   
		URL="/library/summary.do?action=classSummaryDetail&classId="+new BaseClass().encode(classId, true);
		loc_x=document.body.scrollLeft+event.clientX-event.offsetX+0;
		loc_y=document.body.scrollTop+event.clientY-event.offsetY+0;
		//window.open(URL);
	  	window.showModalDialog(URL,self,"edge:raised;scroll:yes;status:0;help:0;resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
	}
	
	function classSummaryDetailQuery(){
		if (pag == null)
				pag = new PageExe();
			var param="";
		if($F('classId')!=null && $F('classId')!="")
			param+="&classId="+new BaseClass().encode($F('classId'), true);
		pag.query(baseUrl + '?action=classSummaryDetailList', 'summary_list',param);
	}
	function studentSummaryDetail(studentId)
	{   
		URL="/library/summary.do?action=studentSummaryDetail&studentId="+studentId;
		loc_x=document.body.scrollLeft+event.clientX-event.offsetX+0;
		loc_y=document.body.scrollTop+event.clientY-event.offsetY+0;
		//window.open(URL);
	  	window.showModalDialog(URL,self,"edge:raised;scroll:yes;status:0;help:0;resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
	}
	
	function studentSummaryDetailQuery(){
		if (pag == null)
				pag = new PageExe();
			var param="";
		if($F('studentId')!=null && $F('studentId')!="")
			param+="&studentId="+new BaseClass().encode($F('studentId'), true);
		pag.query(baseUrl + '?action=studentSummaryDetailList', 'summary_list',param);
	}
	
	function SummaryDetailJump(/*string*/_url, /*int*/curPage)
	{
		pag.query(
			_url + curPage,
			'summary_list'
		);
	}
	
	
	function expiredQuery() {
		if (pag == null)
			pag = new PageExe();
		var param="";
		if($F('queryCityId')!=null && $F('queryCityId')!=-1)
			param+="&queryCityId="+$F('queryCityId');
		if($F('queryAgencyId')!=null && $F('queryAgencyId')!=-1)
			param+="&queryAgencyId="+$F('queryAgencyId');
		if($F('querySchoolId')!=null && $F('querySchoolId')!=-1)
			param+="&querySchoolId="+$F('querySchoolId');
		if($F('bookIndex')!=null && $F('bookIndex')!='')
			param+="&bookIndex="+$F('bookIndex');
		if($F('borrowTime')!=null && $F('borrowTime')!='')
			param+="&borrowTime="+$F('borrowTime');
		if($F('bookName')!=null && $F('bookName')!='')
			param+="&bookName="+new BaseClass().encode($F('bookName'), true);
		if($F('userNo')!=null && $F('userNo')!='')
			param+="&userNo="+$F('userNo');
		if($F('userName')!=null && $F('userName')!='')
			param+="&userName="+new BaseClass().encode($F('userName'), true);
		pag.query(baseUrl + '?action=expiredList', 'borrow_list',param);
	}
	
	
	function downloadBorrow() {
		if (pag == null)
			pag = new PageExe();
		var param="";
		if($F('queryCityId')!=null && $F('queryCityId')!=-1)
			param+="&queryCityId="+$F('queryCityId');
		if($F('queryAgencyId')!=null && $F('queryAgencyId')!=-1)
			param+="&queryAgencyId="+$F('queryAgencyId');
		if($F('querySchoolId')!=null && $F('querySchoolId')!=-1)
			param+="&querySchoolId="+$F('querySchoolId');
		if($F('bookIndex')!=null && $F('bookIndex')!='')
			param+="&bookIndex="+$F('bookIndex');
		if($F('borrowTime')!=null && $F('borrowTime')!='')
			param+="&borrowTime="+$F('borrowTime');
		if($F('bookName')!=null && $F('bookName')!='')
			param+="&bookName="+new BaseClass().encode($F('bookName'), true);
		if($F('userNo')!=null && $F('userNo')!='')
			param+="&userNo="+$F('userNo');
		if($F('userName')!=null && $F('userName')!='')
			param+="&userName="+new BaseClass().encode($F('userName'), true);
		window.location.href = baseUrl + '?action=downloadExpired' + param;
	}
	
