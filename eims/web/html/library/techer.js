/**
 * 
 */
var pag = null;
var baseUrl = '/library/techer.do';

function techerInit()
{
	if (pag == null) pag = new PageExe();
}

 // 列表查询
function techerQuery()
{
	if (pag == null) pag = new PageExe();
	if ('-1' == $F('queryAgencyId')){
		return;
	}
	if (pag == null) pag = new PageExe();
	var qv = '';
	if ('-1' != $F('queryCityId')) {
		qv += '&queryCityId=' + $F('queryCityId');
	}
	qv = '';
	if ('-1' != $F('queryAgencyId')) {
		qv += '&queryAgencyId=' + $F('queryAgencyId');
	}
	if('-1'!=$F('querySchoolId') && $F('querySchoolId') != null){
	 	qv+='&querySchoolId='+$F('querySchoolId');
	}
	 
	if($('techerName') && $F('techerName') != ""){
	 	qv += '&techerName='+new BaseClass().encode($F('techerName'), true);
	}
	if ($('loginName') && '' != $F('loginName')) {
		qv += '&loginName=' + $F('loginName');
	}
	if($('idCard') && $F('idCard') != ""){
	 	qv += '&idCard='+new BaseClass().encode($F('idCard'), true);
	}
	pag.query(
		baseUrl + '?action=list',
		'techer_list',
		qv	
	);
}

// 分页查询方法
function techerJump(/*string*/_url, /*int*/curPage)
{
	pag.query(
		_url + curPage,
		'techer_list'
	);
}

// 添加页面显示
function techerAdd()
{
	pag.showAdd(
		baseUrl + '?action=create',
		'techer_update', 
		'techer_index',
		function() { }
	);
}

// 删除
function techerDel(id)
{
	var _id = base.getChecked('id', true);
	if (!id && _id.length < 1) {
		base.alert('请先选择要删除的教师');
		return;
	}
	base.log('要删除的教师ID为：' + (id || _id.join(',')));
	base.confirm('您确信要删除选中的教师吗？', function() {
		pag.del(
			baseUrl + '?action=remove',
			'',
			'userId=' + (id || _id.join('&userId=')),
			techerQuery
		);
	});
}

// 编辑页面显示
function techerEdit(id)
{
	var _id = base.getChecked('id', true);
	if (!id && _id.length != 1) {
		base.alert('请先选择要编辑的教师，编辑只能一次选择一条记录！');
		return;
	}
	base.log('要编辑的教师ID为：' + (id || _id[0]));
	pag.showEdit(
		baseUrl + '?action=edit',
		'techer_update',
		'techer_index',
		{
			userId : id || _id[0]
		},
		function() {}
	);
}
// 查看页面显示
function techerDetail(id)
{
	var _id = base.getChecked('id', true);
	base.log('要查看的教师ID为：' + (id || _id[0]));
	pag.showEdit(
		baseUrl + '?action=show',
		'techer_update',
		'techer_index',
		{
			userId : id || _id[0]
		},
		function() {}
	);
}

function techerUpdateCheck(itype)
{
		
	if ($F('loginName') == null || $F('loginName') == "") {
 		base.alert('请填写教师继教号！',function(){});
 		return false;
 	}
 	if(itype==3){
 		if ($F('loginPassword') == null || $F('loginPassword') == "") {
 		base.alert('请填写密码！',function(){});
 		return false;
 	}
 	}
	if ($F('editAgencyId') == '-1') {
 		base.alert('请选择教师所在机构！',function(){});
 		return false;
 	}
	//if ($F('editSchoolId') == '-1') {
 	//	base.alert('请选择教师所属学校！',function(){});
 	//	return false;
 	//}
   // if ($F('editGradeId') == '-1') {
 	//	base.alert('请选择教师所在年级！',function(){});
 	//	return false;
 	//}
   // if ($F('editClassNameId') == '-1') {
 	//	base.alert('请选择教师所在班级！',function(){});
 	//	return false;
 	//}
 	if ($F('realName') == null || $F('realName') == "") {
 		base.alert('请填写教师名字！',function(){});
 		return false;
 	}
	 //ajax方式提交表单
	pag.update(
		baseUrl + '?action=save',
		'techerUpdateForm',
		function() {
			pag.back('techer_index','techer_update');
			if($('queryAgencyId') && $F('queryAgencyId') > 0)
				techerQuery();
		}
	);
	
	return false;
}


function checkLoginName(loginName,type){
	var userId=0;
	if(type=='2')
		userId = $('id');
	if(loginName==''){
		alert('请填写继教号！');
		return;
	}
	
	new Ajax.Request(
		'/library/common.do?action=checkTeacherLoginName&loginName='+loginName+'&type='+type+'&userId='+userId,
			{
				method : 'POST',
				evalScripts : true,
				parameters : '',
				onComplete : function(xmlHttp,error){
					var returnVal = xmlHttp.responseText;
					if(returnVal=='1'&&type=='1'){//新增
						alert('继教号重复，请重新输入！');
						$('loginName').focus();
					}else if(returnVal=='1'&&type=='2'){//修改
						alert('继教号重复，请重新输入！');
						$('loginName').focus();
					}
				}
			}
		);
}

// 导入教师初始化�
function importinit() {
	pag.showAdd(baseUrl + '?action=importTecherInit', 'techer_update',
			'techer_index', function() {
			});
}

// 导入模板的下载�
function download() {
	window.location.href = baseUrl + '?action=download';
}


// 导入教师操作
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
 	et = $('importattach_path');
   if (!et || et.value.strip() == '') {
	 	base.alert('请先上传数据文件!',function(){});
	 	return false;
 	}
	// ajax方式提交表单
	//pag.update(
	//	baseUrl + '?action=importTecherSubmit',
	//	'techerUpdateForm',
	//	function() {
	//		pag.back('techer_index','techer_update');
	//		techerQuery();
	//	}
	//);
	//return false;
	document.getElementById("sys_update_btn").disabled=true;
 	return true;
	
}

// 导出教师数据
function downloadTecher() {
	if (pag == null)
		pag = new PageExe();
	var qv = '';
	if ('-1' == $F('querySchoolId')){
		alert("请选择学校再导出！");
		return;
	}
	if ('-1' != $F('queryCityId')) {
		qv += '&queryCityId=' + $F('queryCityId');
	}
	if ('-1' != $F('queryAgencyId')) {
		qv += '&queryAgencyId=' + $F('queryAgencyId');
	}
	if('-1'!=$F('querySchoolId') && $F('querySchoolId') != null){
	 	qv+='&querySchoolId='+$F('querySchoolId');
	}
	 
	if($('techerName') && $F('techerName') != ""){
	 	qv += '&techerName='+new BaseClass().encode($F('techerName'), true);
	}
	if ($('loginName') && '' != $F('loginName')) {
		qv += '&loginName=' + $F('loginName');
	}
	if($('idCard') && $F('idCard') != ""){
	 	qv += '&idCard='+new BaseClass().encode($F('idCard'), true);
	}
	window.location.href = baseUrl + '?action=downloadTecher' + qv;
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
			techerUpdateForm.importattach_path.value = '';
			techerUpdateForm.importName.value = '';
			document.getElementById('file_delete').style.display = 'none';
		}
	});
}