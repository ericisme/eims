/**
 * 
 */

var pag = null;
var baseUrl = '/library/xxptparam.do';

 // 列表查询
function xxptParamQuery()
{
	if (pag == null) pag = new PageExe();
	var qv = '';
	var et = $('queryParamType');
	if('-1'!=et.value){
	 qv+='&param_type__eq__string='+et.value;
	}
	pag.query(
		baseUrl + '?action=list',
		'xxptParam_list',
		'param_name__like__string='+new BaseClass().encode($F('queryparam_name'),true)+qv	 
	);
}

// 分页查询方法
function xxptParamJump(/*string*/_url, /*int*/curPage)
{
	pag.query(
		_url + curPage,
		'xxptParam_list'
	);
}

// 添加页面显示
function xxptParamAdd()
{
	pag.showAdd(
		baseUrl + '?action=create',
		'xxptParam_update', 
		'xxptParam_index',
		function() { }
	);
}

// 删除
function xxptParamDel(id)
{
	var _id = base.getChecked('id', true);
	if (!id && _id.length < 1) {
		base.alert('请先选择要删除的系统参数管理！');
		return;
	}
	base.log('要删除的系统参数管理ID为：' + (id || _id.join(',')));
	base.confirm('您确信要删除选中的系统参数管理吗？', function() {
		pag.del(
			baseUrl + '?action=remove',
			'',
			'id=' + (id || _id.join('&id=')),
			xxptParamQuery
		);
	});
}

// 编辑页面显示
function xxptParamEdit(id)
{
	var _id = base.getChecked('id', true);
	if (!id && _id.length != 1) {
		base.alert('请先选择要编辑的系统参数管理，编辑只能一次选择一条记录！');
		return;
	}
	base.log('要编辑的系统参数管理ID为：' + (id || _id[0]));
	pag.showEdit(
		baseUrl + '?action=edit',
		'xxptParam_update',
		'xxptParam_index',
		{
			id : id || _id[0]
		},
		function() {}
	);
}
// 查看页面显示
function xxptParamDetail(id)
{
	var _id = base.getChecked('id', true);
	base.log('要查看的系统参数管理ID为：' + (id || _id[0]));
	pag.showEdit(
		baseUrl + '?action=show',
		'xxptParam_update',
		'xxptParam_index',
		{
			id : id || _id[0]
		},
		function() {}
	);
}


// 添加和更新信息的内容检查 itype=1 添加 itype=2 更新
function xxptParamUpdateCheck(itype)
{
 	var et = $('param_type');
 	if (!et || et.value.strip() == '-1') {
 		base.alert('请选择参数类型！',function(){et.focus();});
 		return false;
 	}
 	et = $('param_name');
 	if (!et || et.value.strip() == '') {
 		base.alert('请输入参数名称！',function(){et.focus();});
 		return false;
 	}

	// ajax方式提交表单
	pag.update(
		baseUrl + '?action=save',
		'xxptParamUpdateForm',
		function() {
			pag.back('xxptParam_index','xxptParam_update');
			xxptParamQuery();
		}
	);
	
	return false;
}