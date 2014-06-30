/**
 * 
 */

var pag = null;
var baseUrl = '/library/zfptclass.do';
//'msgTitle__like__string=' + $F('qryTitle') + '&item.itemId__eq__int=' + $F('qryItem') + '&orders=mdfTime__desc'
 // 列表查询
function zfptClassQuery()
{
	if (pag == null) pag = new PageExe();
	var qv = '';
	if ('-1' != $F('queryAgencyId')) {
		qv += '&queryAgencyId=' + $F('queryAgencyId');
	}
	if('-1'!=$F('queryGradeId')){
		qv += '&ordergrade__eq__int=' + $F('queryGradeId');
		qv +="&queryGradeId=" + $F('queryGradeId');
	}
	if('-1'!=$F('querySchoolId') && $F('querySchoolId') != null){
	 	qv+='&school_id='+$F('querySchoolId');
	}
	if('-1'!=$F('queryCityId')){
	 	qv+='&city_id='+$F('queryCityId');
	}
	if('-1'!=$F('queryCityId')){
	 	qv+='&queryCityId='+$F('queryCityId');
	}
	if('-1'!=$F('querySchoolId')){
	 	qv += '&querySchoolId='+$F('querySchoolId');
	}
	if('-1'!=$F('queryClassNameId')){
	 	qv += '&orderclass__eq__int='+$F('queryClassNameId');
	}
	pag.query(
		baseUrl + '?action=list',
		'zfptClass_list',
		qv	 
	);
}

// 分页查询方法
function zfptClassJump(/*string*/_url, /*int*/curPage)
{
	pag.query(
		_url + curPage,
		'zfptClass_list'
	);
}

// 添加页面显示
function zfptClassAdd()
{
	pag.showAdd(
		baseUrl + '?action=create',
		'zfptClass_update', 
		'zfptClass_index',
		function() { }
	);
}

// 删除
function zfptClassDel(id)
{
	var _id = base.getChecked('id', true);
	if (!id && _id.length < 1) {
		base.alert('请先选择要删除的班级！');
		return;
	}
	base.log('要删除的班级ID为：' + (id || _id.join(',')));
	base.confirm('您确信要删除选中的班级吗？', function() {
		pag.del(
			baseUrl + '?action=remove',
			'',
			'id=' + (id || _id.join('&id=')),
			zfptClassQuery
		);
	});
}

// 编辑页面显示
function zfptClassEdit(id)
{
	var _id = base.getChecked('id', true);
	if (!id && _id.length != 1) {
		base.alert('请先选择要编辑的班级，编辑只能一次选择一条记录！');
		return;
	}
	base.log('要编辑的班级ID为：' + (id || _id[0]));
	pag.showEdit(
		baseUrl + '?action=edit',
		'zfptClass_update',
		'zfptClass_index',
		{
			id : id || _id[0]
		},
		function() {}
	);
}

// 查看页面显示
function zfptClassDetail(id)
{
	var _id = base.getChecked('id', true);
	base.log('要查看的班级ID为：' + (id || _id[0]));
	pag.showEdit(
		baseUrl + '?action=show',
		'zfptClass_update',
		'zfptClass_index',
		{
			id : id || _id[0]
		},
		function() {}
	);
}

// 添加和更新信息的内容检查 itype=1 添加 itype=2 更新
function zfptClassUpdateCheck(itype)
{
    if ($F('editSchoolId') == '-1') {
 		base.alert('请选择班级所属学校！',function(){});
 		return false;
 	}
    if ($F('editGradeId') == '-1') {
 		base.alert('请选择班级所在年级！',function(){});
 		return false;
 	}
    if ($F('editClassNameId') == '-1') {
 		base.alert('请选择所在班级！',function(){});
 		return false;
 	}
	// ajax方式提交表单
	pag.update(
		baseUrl + '?action=save',
		'zfptClassUpdateForm',
		function() {
			pag.back('zfptClass_index','zfptClass_update');
			zfptClassQuery();
		}
	);
	
	return false;
}

// 导出班级数据
function downloadClass() {
	if (pag == null)
		pag = new PageExe();
	var qv = '';
	if ('-1' != $F('queryAgencyId')) {
		qv += '&queryAgencyId=' + $F('queryAgencyId');
	}
	if('-1'!=$F('queryCityId')){
	 	qv += '&city_id='+$F('queryCityId');
	}
	if('-1'!=$F('queryGradeId')){
		qv += '&ordergrade__eq__int=' + $F('queryGradeId');
	}
	if('-1'!=$F('querySchoolId')){
	 	qv += '&querySchoolId='+$F('querySchoolId');
	}
	if('-1'!=$F('queryCityId')){
	 	qv += '&queryCityId='+$F('queryCityId');
	}
	if('-1'!=$F('queryClassNameId')){
	 	qv += '&orderclass__eq__int='+$F('queryClassNameId');
	}
	window.location.href = baseUrl + '?action=downloadClass' + qv;
//	base.formSubmit(baseUrl + '?action=downloadClass', function(xmlHttp, error) {
//		var obj = base.json(xmlHttp.responseText);
//		   alert(obj.message);		  
//	}, 'zfptClassForm');
}