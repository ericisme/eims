/**
 * 
 */
var pag = null;
var baseUrl = '/library/common.do';

function userInit()
{
	if (pag == null) pag = new PageExe();
}

 // 列表查询
function userQuery()
{
	if (pag == null) pag = new PageExe();
	if ($F('queryAgencyId')==null || '-1' == $F('queryAgencyId')){
		return;
	}
	var qv = '';
	
	if ($F('queryAgencyId')!=null&&'-1' != $F('queryAgencyId')) {
		qv += '&queryAgencyId=' + $F('queryAgencyId');
	}
	if($F('querySchoolId')!=null&&'-1'!=$F('querySchoolId')){
	 	qv+='&school_id='+$F('querySchoolId');
	}
 
	if($F('queryGradeId')!=null&&'-1'!=$F('queryGradeId')){
	 	qv+='&queryGradeId='+$F('queryGradeId');
	}
	 
	if($F('querySchoolId')!=null&&'-1'!=$F('querySchoolId')){
	 	qv += '&querySchoolId='+$F('querySchoolId');
	}
	if($F('queryClassNameId')!=null&&'-1'!=$F('queryClassNameId')){
	 	qv += '&queryClassNameId='+$F('queryClassNameId');
	}
	if($F('userName') !=null && $F('userName') != ""){
	 	qv += '&userName='+new BaseClass().encode($F('userName'), true);
	}
	if($F('userNo') !=null && $F('userNo') != ""){
	 	qv += '&userNo='+$F('userNo');
	}
	if($F('idCard') !=null && $F('idCard') != ""){
	 	qv += '&idCard='+$F('idCard');
	}
	pag.query(
		baseUrl + '?action=userList',
		'user_list',
		qv	
	);
}


	// 列表查询
	function bookQuery() {
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
		if($F('bookStorageTime')!=null && $F('bookStorageTime')!='')
			param+="&bookStorageTime="+$F('bookStorageTime');
		if($F('bookName')!=null && $F('bookName')!='')
			param+="&bookName="+new BaseClass().encode($F('bookName'), true);
		pag.query(baseUrl + '?action=bookList', 'book_list',param);
	}

// 分页查询方法
	function bookJump(/* string */_url, /* int */curPage) {
		pag.query(_url + curPage, 'book_list');
	}
// 分页查询方法
function userJump(/*string*/_url, /*int*/curPage)
{
	pag.query(
		_url + curPage,
		'user_list'
	);
}

function userConfirm(id){
	if(!id){
		var _id = base.getChecked('id', true);
		id = id || _id[0];
	}
	if(!id){
		alert("请先选择人员！");
		return;
	}
	
	new Ajax.Request('/library/common.do?action=getUser&id=' + id, {
			method : 'POST',
			evalScripts : true,
			parameters : '',
			onComplete : function(xmlHttp, error) {
				var returnVal = xmlHttp.responseText;
				var str = returnVal.split("◎");
				var parent_window = parent.dialogArguments;
				parent_window.borrowForm.userId.value=str[0];
				parent_window.borrowForm.id_card.value=str[1];
				parent_window.borrowForm.realName.value=str[2];
				parent_window.borrowForm.loginName.value=str[3];
				parent_window.borrowForm.user_type.value=str[4];
				parent_window.borrowForm.gender.value=str[5];
				parent_window.borrowForm.unreturn.value=str[6];
				parent_window.borrowForm.book_limit.value=str[7];
				parent_window.borrowForm.day_limit.value=str[8];
				window.close();
				parent_window.checkBookLimit(str[7],str[6],str[9]);
				parent_window.borrowForm.bookNo.focus();
			}
		});
		}
		
function bookConfirm(tr,id){
	if(!id){
		var _id = base.getChecked('id', true);
		id = id || _id[0];
	}
	if(!id){
		alert("请先选择图书！");
		return;
	}
	if(!tr){
		tr=document.getElementById("tr_"+id);
	}
	 var tds=tr.getElementsByTagName('td'); 
     var parent_window = parent.dialogArguments;
     parent_window.borrowForm.bookIds.value+=id+",";   
	 parent_window.borrowForm.bookNo.value=tds[1].innerHTML;   
     parent_window.borrowForm.bookSchool.value=tds[5].innerHTML;
     parent_window.borrowForm.bookPrice.value=tds[6].innerHTML;
     parent_window.borrowForm.bookPlace.value=tds[8].innerHTML;
     parent_window.borrowForm.bookName.value=tds[2].innerHTML;
     parent_window.borrowForm.bookIndex.value=tds[3].innerHTML;
     parent_window.borrowForm.categoryName.value=tds[4].innerHTML;
     parent_window.borrowForm.bookAuthor.value=tds[7].innerHTML;
     window.close();
     parent_window.bookQuery();
     
}

