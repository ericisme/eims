/**
 * 
 */
var pag = null;
var baseUrl = '/library/student.do';

function studentInit()
{
	if (pag == null) pag = new PageExe();
}

 // 列表查询
function studentQuery()
{
	if (pag == null) pag = new PageExe();
	if ('-1' == $F('queryAgencyId')){
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
	if($F('studentName') !=null && $F('studentName') != ""){
	 	qv += '&studentName='+new BaseClass().encode($F('studentName'), true);
	}
	if($F('queryLoginName') !=null && $F('queryLoginName') != ""){
	 	qv += '&queryLoginName='+$F('queryLoginName');
	}
	if($F('queryIdCard') !=null && $F('queryIdCard') != ""){
	 	qv += '&queryIdCard='+$F('queryIdCard');
	}
	
	pag.query(
		baseUrl + '?action=list',
		'student_list',
		qv	
	);
}

// 分页查询方法
function studentJump(/*string*/_url, /*int*/curPage)
{
	pag.query(
		_url + curPage,
		'student_list'
	);
}

// 添加页面显示
function studentAdd()
{
	pag.showAdd(
		baseUrl + '?action=create',
		'student_update', 
		'student_index',
		function() { }
	);
}

// 删除
function studentDel(id)
{
	var _id = base.getChecked('id', true);
	if (!id && _id.length < 1) {
		base.alert('请先选择要删除的学生�');
		return;
	}
	base.log('要删除的学生ID为：' + (id || _id.join(',')));
	base.confirm('您确信要删除选中的学生吗？', function() {
		pag.del(
			baseUrl + '?action=remove',
			'',
			'userId=' + (id || _id.join('&userId=')),
			studentQuery
		);
	});
}

// 编辑页面显示
function studentEdit(id)
{
	var _id = base.getChecked('id', true);
	if (!id && _id.length != 1) {
		base.alert('请先选择要编辑的学生，编辑只能一次选择一条记录！');
		return;
	}
	base.log('要编辑的学生ID为：' + (id || _id[0]));
	pag.showEdit(
		baseUrl + '?action=edit',
		'student_update',
		'student_index',
		{
			userId : id || _id[0]
		},
		function() {}
	);
}
// 查看页面显示
function studentDetail(id)
{
	var _id = base.getChecked('id', true);
	base.log('要查看的学生ID为：' + (id ||  _id.join(',')));
	pag.showEdit(
		baseUrl + '?action=show',
		'student_update',
		'student_index',
		{
			userId : id ||  _id.join(',')
		},
		function() {}
	);
}

function studentUpdateCheck(itype)
{
	
	
	if ($F('groupId') != null && $F('groupId') == '-1') {
 		base.alert('请选择用户所属的用户组！',function(){});
 		return false;
 	}
 	if ($F('user_type') != null && $F('user_type') == '-1') {
 		base.alert('请选择用户类别！',function(){});
 		return false;
 	}
 	if ($F('editCityId') != null && $F('editCityId') == '-1') {
 		base.alert('请选择学生所在镇区！',function(){});
 		return false;
 	}
	if ($F('editAgencyId') != null && $F('editAgencyId') == '-1') {
 		base.alert('请选择学生所在机构！',function(){});
 		return false;
 	}
	if ($F('editSchoolId') != null && $F('editSchoolId') == '-1') {
 		base.alert('请选择学生所属学校！',function(){});
 		return false;
 	}
    if ($F('editGradeId') != null && $F('editGradeId') == '-1') {
 		base.alert('请选择学生所在年级！',function(){});
 		return false;
 	}
    if ($F('editClassNameId') != null && $F('editClassNameId') == '-1') {
 		base.alert('请选择学生所在班级！',function(){});
 		return false;
 	}
 	if ($F('realName') == null || $F('realName') == "") {
 		base.alert('请填写学生名字！',function(){});
 		return false;
 	}
 	if ($F('loginName') == null || $F('loginName') == "") {
 		base.alert('请填写学生学籍号！',function(){});
 		return false;
 	}
 	if ($F('birthday') == null || $F('birthday') == "") {
 		base.alert('请填写学生生日！',function(){});
 		return false;
 	}
	//if($('idCard1') && $F('idCard1') != null && $F('idCard1') != ""  && this.isIdCard($F('idCard1')) == false){
	//	base.alert('请填写家长1的正确的15位或18位身份证号码!');
	//	return false;
	//}
	//if($('idCard2') && $F('idCard2') != null && $F('idCard2') != ""  && this.isIdCard($F('idCard2')) == false){
	//	base.alert('请填写家长2的正确的15位或18位身份证号码!');
	//	return false;
	//}
 	
	 //ajax方式提交表单
	// alert(baseUrl + '?action=save');
	pag.update(
		baseUrl + '?action=save',
		'studentUpdateForm',
		function() {
			pag.back('student_index','student_update');
			if($('queryAgencyId') && $F('queryAgencyId') > 0)
				studentQuery();
		}
	);
	
	return false;
}
// 文件上传后调用的函数
function importend() {
	document.getElementById('file_delete').innerHTML = "<a href='JavaScript:void(0);' onclick='delete_file()'><font color='red'>"
			+ document.getElementById('importName').value
			+ "</font><img src='/images/del.gif' border='0'/></a>";
	document.getElementById('file_delete').style.display = '';
}
// 删除操作
function delete_file() {
	if (!window.confirm('您确定要删除该该附件资料')) {
		return;
	}
	var filepath = $('importattach_path').value;
	new Ajax.Request('/myDelete.do?filepath=' + filepath
			+ '&_temp=' + Math.random(), {
		method : 'POST',
		evalScripts : true,
		parameters : '',
		onComplete : function(xmlHttp, error) {
			studentUpdateForm.importattach_path.value = '';
			studentUpdateForm.importName.value = '';
			document.getElementById('file_delete').style.display = 'none';
		}
	});
}
function checkLoginName(loginName,type){
	var userId=0;
	if(type=='2')
		userId = $('id');
	if(loginName==''){
		alert('请填写学籍号！');
		return;
	}
	
	new Ajax.Request(
		'/library/common.do?action=checkLoginName&loginName='+loginName+'&type='+type+'&userId='+userId,
			{
				method : 'POST',
				evalScripts : true,
				parameters : '',
				onComplete : function(xmlHttp,error){
					var returnVal = xmlHttp.responseText;
					if(returnVal=='1'&&type=='1'){//新增
						alert('学籍号重复，请重新输入！');
						$('loginName').focus();
					}else if(returnVal=='1'&&type=='2'){//修改
						alert('学籍号重复，请重新输入！');
						$('loginName').focus();
					}
				}
			}
		);
}
// 导入学生初始化�
function importinit() {
	pag.showAdd(baseUrl + '?action=importStudentInit', 'student_update',
			'student_index', function() {
			});
}

// 导入模板的下载�
function download() {
	window.location.href = baseUrl + '?action=download';
}


// 导入学生操作
function importHandle()
{
	var et = $('importCityId');
    if (!et || et.value.strip() == ''||et.value.strip()=='-1') {
	 	base.alert('请选择镇区',function(){});
	 	return false;
 	}
    et = $('importAgencyId');
    if (!et || et.value.strip() == ''||et.value.strip()=='-1') {
	 	base.alert('请选择机构',function(){});
	 	return false;
 	}
 	et = $('importSchoolId');
    if (!et || et.value.strip() == ''||et.value.strip()=='-1') {
	 	base.alert('请选择学校',function(){});
	 	return false;
 	}
 	et = $('importClassId');
    if (!et || et.value.strip() == ''||et.value.strip()=='-1') {
	 	base.alert('请选择班级',function(){});
	 	return false;
 	}
 	et = $('importattach_path');
   if (!et || et.value.strip() == '') {
	 	base.alert('请先上传数据文件!',function(){});
	 	return false;
 	}
 	document.getElementById("sys_update_btn").disabled=true;
 	return true;
	// ajax方式提交表单
	//pag.update(
	//	baseUrl + '?action=importStudentSubmit',
	//	'studentUpdateForm',
	//	function() {
	//		pag.back('student_index','student_update');
	//		studentQuery();
	//	}
	//);
	return false;
}

// 导出学生数据
function downloadStudent() {
	if (pag == null)
		pag = new PageExe();
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
	if($F('studentName') !=null && $F('studentName') != ""){
	 	qv += '&studentName='+new BaseClass().encode($F('studentName'), true);
	}
	if($F('queryLoginName') !=null && $F('queryLoginName') != ""){
	 	qv += '&queryLoginName='+$F('queryLoginName');
	}
	if($F('queryIdCard') !=null && $F('queryIdCard') != ""){
	 	qv += '&queryIdCard='+$F('queryIdCard');
	}
	window.location.href = baseUrl + '?action=downloadStudent' + qv;
}