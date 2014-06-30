/**
 * 
 */

var pag = null;
var baseUrl = '/library/agency.do';

// 列表查询
function agencyQuery() {
	if (pag == null)
		pag = new PageExe();
	var param="";
	if($F('citySelect')!=-1)param="&cityId="+$F('citySelect');
	pag.query(baseUrl + '?action=list', 'agency_list',
			'agency_name__like__string='
					+ new BaseClass().encode($F('agencyName'), true)
					+ '&orders=id__asc'+param);
}

// 分页查询方法
function agencyJump(/* string */_url, /* int */curPage) {
	pag.query(_url + curPage, 'agency_list');
}

// 添加页面显示
function agencyAdd() {
	pag.showAdd(baseUrl + '?action=create', 'agency_update', 'agency_index',
			function() {
			});
}

// 删除
function agencyDel(id) {
	var _id = base.getChecked('id', true);
	if (!id && _id.length < 1) {
		base.alert('请先选择要删除的机构管理！');
		return;
	}
	base.log('要删除的机构管理ID为：' + (id || _id.join(',')));
	base.confirm('您确信要删除选中的机构管理吗？', function() {
		pag.del(baseUrl + '?action=remove', '', 'id='
				+ (id || _id.join('&id=')), agencyQuery);
	});
}

// 编辑页面显示
function agencyEdit(id) {
	var _id = base.getChecked('id', true);
	if (!id && _id.length != 1) {
		base.alert('请先选择要编辑的机构管理，编辑只能一次选择一条记录！');
		return;
	}
	base.log('要编辑的机构管理ID为：' + (id || _id[0]));
	pag.showEdit(baseUrl + '?action=edit', 'agency_update', 'agency_index', {
		id : id || _id[0]
	}, function() {
	});
}
// 查看页面显示
function agencyDetail(id) {
	var _id = base.getChecked('id', true);
	base.log('要查看的机构管理ID为：' + (id || _id[0]));
	pag.showEdit(baseUrl + '?action=show', 'agency_update', 'agency_index', {
		id : id || _id[0]
	}, function() {
	});
}

// 添加和更新信息的内容检查 itype=1 添加 itype=2 更新
function agencyUpdateCheck(itype) {
	var et = $('agency_name');
	if (!et || et.value.strip() == '') {
		base.alert('请输入机构名称！', function() {
			et.focus();
		});
		return false;
	}
	et = $('editCityId');
	if (et.value.strip()==null||et.value.strip() == '-1') {
		base.alert('请选择机构所属的城市！', function() {
			et.focus();
		});
		return false;
	}
	et = $('agency_type');
	if (et.value.strip()==null||et.value.strip() == '-1') {
		base.alert('请选择机构类别！', function() {
			et.focus();
		});
		return false;
	}
	// ajax方式提交表单
	pag.update(baseUrl + '?action=save', 'agencyUpdateForm', function() {
		pag.back('agency_index', 'agency_update');
		agencyQuery();
	});
	return false;
}
//导出机构信息
function exportAgencys(){
	window.location.href=baseUrl+'?action=downloadAgency&agency_name__like__string='
					+ new BaseClass().encode($F('agencyName'), true)
					+ '&orders=id__asc';
}


