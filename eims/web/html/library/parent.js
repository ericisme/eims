/**
 * 家长管理
 */
var pag = new PageExe();
var parentBaseUrl = '/library/parent.do';
 // 家长管理下的列表查询
function parentIndexQuery()
{
	if (pag == null) pag = new PageExe();
//	if ('-1' == $F('queryAgencyId')){
//		alert('请选择机构');
//		return;
//	}
	if (pag == null) pag = new PageExe();
	var qv = '';
	if ('-1' != $F('queryAgencyId')) {
		qv += '&agencyId=' + $F('queryAgencyId');
	}
	if ('-1' != $F('queryAgencyId')) {
		qv += '&queryAgencyId=' + $F('queryAgencyId');
	}
	if('-1'!=$F('querySchoolId') && $F('querySchoolId') != null){
	 	qv+='&school_id='+$F('querySchoolId');
	}
	if('-1'!=$F('queryGradeId')){
	 	qv+='&queryGradeId='+$F('queryGradeId');
	}
	if('-1'!=$F('querySchoolId')){
	 	qv += '&querySchoolId='+$F('querySchoolId');
	}
	if('-1'!=$F('queryClassNameId')){
	 	qv += '&queryClassNameId='+$F('queryClassNameId');
	}
	if($('studentName') && $F('studentName') != ""){
	 	qv += '&studentName='+new BaseClass().encode($F('studentName'), true);
	}
	pag.query(
		parentBaseUrl + '?action=listIndex',
		'parent_list',
		qv
	);
}

//学生管理下的家长列表查询
function parentQuery()
{
	if (pag == null) pag = new PageExe();
	pag.query(
		parentBaseUrl + '?action=list&student_id='+$F('student_id'),
		'parent_list',
		''
	);
}

//选择返回家长列表
function selectQuery(){
	var page_name = "";
	if($('parent_index_list'))
		page_name = "parent_index_list";
	else if($('parent_list'))
		page_name = "parent_list";
		
	if(page_name == 'parent_index_list')
		parentIndexQuery();
	else if(page_name == "parent_list"){
		studentEdit($F('student_id'));
	}
}

// 分页查询方法
function parentJump(/*string*/_url, /*int*/curPage)
{
	pag.query(
		_url + curPage,
		'parent_list'
	);
}

function parentAdd(student_id)
{
	pag.showAdd(
		parentBaseUrl + '?action=create&student_id='+student_id,
		'parent_update', 
		'parent_index',
		function() {}
	);
}

// 删除
function parentDel(id)
{
	var _id = base.getChecked('parent_id', true);
	if (!id && _id.length < 1) {
		base.alert('请先选择要删除的家长管理！');
		return;
	}
	base.log('要删除的家长管理ID为：' + (id || _id.join(',')));
	base.confirm('您确信要删除选中的家长管理吗？', function() {
		pag.del(
			parentBaseUrl + '?action=remove',
			'',
			'id=' + (id || _id.join('&id=')),
			function() {
				selectQuery();
			}
		);
	});
}

// 编辑页面显示
function parentEdit(id)
{
	var _id = base.getChecked('parent_id', true);
	if (!id && _id.length != 1) {
		base.alert('请先选择要编辑的家长管理，编辑只能一次选择一条记录！');
		return;
	}
	base.log('要编辑的家长管理ID为：' + (id || _id[0]));
	pag.showEdit(
		parentBaseUrl + '?action=edit',
		'parent_update',
		'parent_index',
		{
			id : id || _id[0]
		},
		function() {}
	);
}
// 查看页面显示
function parentDetail(id)
{
	var _id = base.getChecked('parent_id', true);
	base.log('要查看的家长管理ID为：' + (id || _id[0]));
	pag.showEdit(
		parentBaseUrl + '?action=show',
		'parent_update',
		'parent_index',
		{
			id : id || _id[0]
		},
		function() {}
	);
}

// 添加和更新信息的内容检查 itype=1 添加 itype=2 更新
function parentUpdateCheck(itype)
{
	if($F('idCard') != null && $F('idCard') != "" && this.isIdCard($F('idCard')) == false){
		base.alert('请填写正确的15位或18位身份证号码!');
		return false;
	}
	if(this.isMobile($F('mobile')) == false){
		base.alert('请填写正确的手机号码!!');
		return false;
	}
	
	// ajax方式提交表单
	pag.update(
		parentBaseUrl + '?action=save',
		'parentUpdateForm',
		function() {
			selectQuery();
			pag.back('parent_index','parent_update');
		},
		''
	);
	return false;
}