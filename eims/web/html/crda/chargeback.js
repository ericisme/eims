/**
 * 
 */

var pag = null;
var baseUrl = '/crda/chargeback.do';

 // 列表查询
function chargebackQuery()
{
	if (pag == null) pag = new PageExe();
	var qv ='&year__like__string=' +new BaseClass().encode($F('year'), true);
	qv+='&month__eq__string=' +new BaseClass().encode($F('month'), true);
	qv+='&mobile__eq__string=' +new BaseClass().encode($F('mobile'), true);
	pag.query(
		baseUrl + '?action=list',
		'chargeback_list',
		qv
	);
}

// 分页查询方法
function chargebackJump(/*string*/_url, /*int*/curPage)
{
	pag.query(
		_url + curPage,
		'chargeback_list'
	);
}

// 添加页面显示
function chargebackAdd()
{
	pag.showAdd(
		baseUrl + '?action=create',
		'chargeback_update', 
		'chargeback_index',
		function() { }
	);
}

// 删除
function chargebackDel(id)
{
	var _id = base.getChecked('id', true);
	if (!id && _id.length < 1) {
		base.alert('请先选择要删除的扣费记录！');
		return;
	}
	base.log('要删除的扣费记录ID为：' + (id || _id.join(',')));
	base.confirm('您确信要删除选中的扣费记录吗？', function() {
		pag.del(
			baseUrl + '?action=remove',
			'',
			'id=' + (id || _id.join('&id=')),
			chargebackQuery
		);
	});
}

// 编辑页面显示
function chargebackEdit(id)
{
	var _id = base.getChecked('id', true);
	if (!id && _id.length != 1) {
		base.alert('请先选择要编辑的扣费记录，编辑只能一次选择一条记录！');
		return;
	}
	base.log('要编辑的扣费记录ID为：' + (id || _id[0]));
	pag.showEdit(
		baseUrl + '?action=edit',
		'chargeback_update',
		'chargeback_index',
		{
			id : id || _id[0]
		},
		function() {}
	);
}
// 查看页面显示
function chargebackDetail(id)
{
	var _id = base.getChecked('id', true);
	base.log('要查看的扣费记录ID为：' + (id || _id[0]));
	pag.showEdit(
		baseUrl + '?action=show',
		'chargeback_update',
		'chargeback_index',
		{
			id : id || _id[0]
		},
		function() {}
	);
}


// 添加和更新信息的内容检查 itype=1 添加 itype=2 更新
function chargebackUpdateCheck(itype)
{
//	var et = $('userName');
//	if (!et || et.value.strip() == '') {
//		base.alert('请输入用户的真实姓名！',function(){et.focus();});
//		return false;
//	}

	// ajax方式提交表单
	pag.update(
		baseUrl + '?action=save',
		'chargebackUpdateForm',
		function() {
			pag.back('chargeback_index','chargeback_update');
			chargebackQuery();
		}
	);
	return false;
}


function creatChargeback(){
	base.formSubmit(baseUrl + '?action=creatChargeback', function(xmlHttp, error) {
		var obj = base.json(xmlHttp.responseText);
		   alert(obj.message);
		   chargebackQuery();
	}, 'chargebackForm');
	return false;
}

// 导出扣费记录
function downloadChargeback() {
	window.location.href = baseUrl + '?action=downloadChargeback&year='+$F('year')+'&month='+$F('month');
}

// 导入移动扣费情况初始化�
function importChargebackInit() {
	pag.showAdd(
		baseUrl + '?action=importChargebackInit',
		'chargeback_update', 
		'chargeback_index',
		function() { }
	);
}

//导入扣费情况
function importHandle()
{
   var et = $('importattach_path');
   if (!et || et.value.strip() == '') {
	 	base.alert('请先上传数据文件!',function(){});
	 	return false;
 	}
	// ajax方式提交表单
	pag.update(
		baseUrl + '?action=importChargeBackSubmit',
		'chargebackUpdateForm',
		function() {
			pag.back('chargeback_index','chargeback_update');
			chargebackQuery();
		}
	);
	return false;
}

// 导入模板的下载�
function download() {
	window.location.href = baseUrl + '?action=download';
}

// 同步扣费情况到成长档案袋�
function synchronizationToCrda() {
	//1:同步扣费情况
	
	//2:如果三次扣费都失败则取消订阅
}