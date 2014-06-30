/**
 * 
 */

var pag = null;
var baseUrl = '/crda/classstudent.do';

 // 列表查询
function classstudentQuery(val)
{
	if (pag == null) pag = new PageExe();
	var qv = '';
	if(val != null && val == 0) {
		qv += "&is_valid=0";
	}
	qv+= '&queryClassId=-1';
	qv+= '&querySchoolId=-1';
	pag.query(
		baseUrl + '?action=list',
		'classstudent_list',
		qv
	);
}

// 分页查询方法
function classStudentJump(/*string*/_url, /*int*/curPage)
{
	pag.query(
		_url + curPage,
		'classstudent_list'
	);
}

// 添加页面显示
function classstudentAdd()
{
	pag.showAdd(
		baseUrl + '?action=create',
		'classstudent_update', 
		'classstudent_index',
		function() { }
	);
}

// 删除
function classstudentDel(id)
{
	var _id = base.getChecked('id', true);
	if (!id && _id.length < 1) {
		base.alert('请先选择要删除的学生匹配管理！');
		return;
	}
	base.log('要删除的学生匹配管理ID为：' + (id || _id.join(',')));
	base.confirm('您确信要删除选中的学生匹配管理吗？', function() {
		pag.del(
			baseUrl + '?action=remove',
			'',
			'id=' + (id || _id.join('&id=')),
			classstudentQuery
		);
	});
}

// 编辑页面显示
function classstudentEdit(id)
{
	var _id = base.getChecked('id', true);
	if (!id && _id.length != 1) {
		base.alert('请先选择要编辑的学生匹配管理，编辑只能一次选择一条记录！');
		return;
	}
	base.log('要编辑的学生匹配管理ID为：' + (id || _id[0]));
	pag.showEdit(
		baseUrl + '?action=edit',
		'classstudent_update',
		'classstudent_index',
		{
			id : id || _id[0]
		},
		function() {}
	);
}
// 查看页面显示
function classstudentDetail(id)
{
	var _id = base.getChecked('id', true);
	base.log('要查看的学生匹配管理ID为：' + (id || _id[0]));
	pag.showEdit(
		baseUrl + '?action=show',
		'classstudent_update',
		'classstudent_index',
		{
			id : id || _id[0]
		},
		function() {}
	);
}


// 添加和更新信息的内容检查 itype=1 添加 itype=2 更新
function classstudentUpdateCheck(itype)
{
	// ajax方式提交表单
	pag.update(
		baseUrl + '?action=save',
		'classstudentUpdateForm',
		function() {
			pag.back('classstudent_index','classstudent_update');
			classstudentQuery();
		}
	);
	
	return false;
}

//匹配成人档案袋学生信息
function classstudentCheck(){
	// ajax方式提交表单
	if ('-1' == $F('queryAgencyId')){
		alert('请选择机构');
		return;
	}
	this_ = this;
	button_disable("classStudentUpdateForm", true);
	base.formSubmit(baseUrl + '?action=validClassStudent', function(xmlHttp, error) {
		var obj = base.json(xmlHttp.responseText);
   		alert(obj.message);
   		this_.button_disable("classStudentUpdateForm", false);
		classstudentQuery();
	}, 'classStudentUpdateForm');
}

// 导出班级数据
function downloadClassStudent() {
	if (pag == null)
		pag = new PageExe();
	var qv = '';
	window.location.href = baseUrl + '?action=downloadClassStudent' + qv;

}