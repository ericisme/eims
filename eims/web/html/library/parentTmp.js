/**
 * 
 */
var pag = new PageExe();
var parentTmpBaseUrl = '/library/parenttmp.do';

 // 列表查询
function parentTmpQuery()
{
	if (pag == null) pag = new PageExe();
	var qv = '';
 	qv += '&student_name__like__string=' +new BaseClass().encode($F('studentName'), true);
 	qv += '&school_name__like__string=' +new BaseClass().encode($F('schoolName'), true);
	pag.query(
		parentTmpBaseUrl + '?action=list',
		'parenttmp_list',
		qv
	);
}
// 导入家长初始化�
function importinit_parent() {
	window.location.href = parentTmpBaseUrl + '?action=importParentInit';
}

// 导入家长临时表操作
var importCount = 0;
function importParentHandle()
{
   var et = $('importattach_path');
   if (!et || et.value.strip() == '') {
	 	base.alert('请先上传数据文件!',function(){});
	 	return false;
 	}
 	et = $('importAgencyId');
   if (!et || et.value.strip() == '' || et.value.strip() == '-1') {
	 	base.alert('请先选择要导入的机构',function(){});
	 	return false;
 	}
	// ajax方式提交表单
 	if (pag == null) pag = new PageExe();
	base.formSubmit('/library/parenttmp.do?action=importParentTmpSubmit', function(xmlHttp, error) {
		var obj = base.json(xmlHttp.responseText);
		   alert(obj.message);
		   importCount = obj.importCount;
		   parentTmpQuery();
	}, 'parentUpdateForm');
	
	return false;
}

// 导入家长模板的下载�
function downloadParent() {
	window.location.href = parentTmpBaseUrl + '?action=download';
}

//导入到家长信息
function saveParent() {
	this_ = this;
	button_disable("parentUpdateForm", true);
	base.formSubmit('/library/parent.do?action=importParentSubmit', function(xmlHttp, error) {
		var obj = base.json(xmlHttp.responseText);
		   alert(obj.message);
		   this_.button_disable("parentUpdateForm", false);
		   parentTmpQuery();
	}, 'parentUpdateForm');
	return false;
}

//删除临时家长信息
function parentTmpDelete(id){
	var _id = base.getChecked('id', true);
	if (!id && _id.length < 1) {
		base.confirm('您确信要删所有无效家长吗?', function() {
			base.formSubmit('/library/parenttmp.do?action=deleteParentTmp', function(xmlHttp, error) {
				var obj = base.json(xmlHttp.responseText);
		   		alert(obj.message);
			   	parentTmpQuery();
			}, 'parentUpdateForm');
			return false;
		});
	}
	else {
		base.log('要删除的家长管理ID为：' + (id || _id.join(',')));
		base.confirm('您确信要删除选中的家长管理吗?', function() {
			pag.del(
				parentTmpBaseUrl + '?action=remove',
				'',
				'id=' + (id || _id.join('&id=')),
				parentTmpQuery
			);
		});
	}
}

// 分页查询方法
function parentTmpJump(/*string*/_url, /*int*/curPage)
{
	pag.query(
		_url + curPage,
		'parenttmp_list'
	);
}

function exportParentSchoolEdit(schoolName,agencyName)
{
	pag.showEdit(
		'/library/parenttmp.do?action=updateSchoolTmp&schoolName='+ new BaseClass().encode(schoolName, true)+'&agencyName='+ new BaseClass().encode(agencyName, true),
		'parenttmp_update',
		'parenttmp_index',
		{},
		function() {}
	);
}

// 文件上传后调用的函数
function importend(){
 document.getElementById('file_delete').innerHTML = 
	 "<a href='JavaScript:void(0);' onclick='delete_file()'><font color='red'>"
	 +document.getElementById('importName').value+"</font><img src='/images/del.gif' border='0'/></a>";
	 document.getElementById('file_delete').style.display='';
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
					parentUpdateForm.importattach_path.value = '';
					parentUpdateForm.importName.value = '';
					document.getElementById('file_delete').style.display = 'none';
				}
			});
}

//更新家长信息有效性
function checkValid(){
	// ajax方式提交表单
	this_ = this;
	button_disable("parentUpdateForm", true);
	base.formSubmit(parentTmpBaseUrl + '?action=validParent', function(xmlHttp, error) {
		var obj = base.json(xmlHttp.responseText);
   		alert(obj.message);
   		this_.button_disable("parentUpdateForm", false);
		parentTmpQuery();
	}, 'parentUpdateForm');
}

// 添加和更新信息的内容检查 itype=1 添加 itype=2 更新
function schoolTmpUpdateCheck(itype) {
	var	et = $('agency_id');
	if (et.value.strip()==null||et.value.strip() == '-1') {
		base.alert('请选择机构！', function() {
			et.focus();
		});
		return false;
	} 
	et = $('newSchoolName');
	if (!et || et.value.strip() == '') {
		base.alert('请输入学校名称！', function() {
			et.focus();
		});
		return false;
	}
	// ajax方式提交表单
	pag.update( '/library/parenttmp.do?action=saveSchoolTmp', 'updateSchoolTmp', function() {
		pag.back('parenttmp_index', 'parenttmp_update');
		parentTmpQuery();
	});
	return false;
}

function exportParentNameEdit(parentName,studentName,mobile)
{
	pag.showEdit(
		'/library/parenttmp.do?action=updateParentTmp&parentName='+ new BaseClass().encode(parentName, true)
			+'&studentName='+ new BaseClass().encode(studentName, true)
			+'&mobile='+ new BaseClass().encode(mobile, true),
		'parenttmp_update',
		'parenttmp_index',
		{},
		function() {}
	);
}

// 添加和更新信息的内容检查 itype=1 添加 itype=2 更新
function parentTmpUpdateCheck(itype) {
	var	et = $('newParentName');
	if (!et || et.value.strip() == '') {
		base.alert('请输入家长名称！', function() {
			et.focus();
		});
		return false;
	}
	et = $('newStudentName');
	if (!et || et.value.strip() == '') {
		base.alert('请输入学生名称！', function() {
			et.focus();
		});
		return false;
	}
	et = $('newMobile');
	if(this.isMobile($F('newMobile')) == false){
		base.alert('请填写正确的手机号码!!');
		return false;
	}
	if (!et || et.value.strip() == '') {
		base.alert('请输入手机号码！', function() {
			et.focus();
		});
		return false;
	}
	// ajax方式提交表单
	pag.update( '/library/parenttmp.do?action=saveParentTmp', 'updateParentTmp', function() {
		pag.back('parenttmp_index', 'parenttmp_update');
		parentTmpQuery();
	});
	return false;
}

// 导出无效家长数据
function downloadParentTmp() {
	if (pag == null)
		pag = new PageExe();
	var qv = '';
	window.location.href = '/library/parenttmp.do?action=downloadParentTmp' + qv;
}