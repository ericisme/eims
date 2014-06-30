/**
 * 
 */

var pag = null;
var baseUrl = '/library/school.do';
// 列表查询
function schoolQuery() {
	if (pag == null)
		pag = new PageExe();
	var param1;
	if ('-1' == $F('citySelect') || $F('citySelect') == null) {
		param1 = '';
	} else {
		param1 = '&cityId=' + $F('citySelect');
	}
	var param2;
	if ('-1' == $F('agencySelect') || $F('agencySelect') == null) {
		param2 = '';
	} else {
		param2 = '&agencyId=' + $F('agencySelect');
	}
	pag.query(baseUrl + '?action=list', 'school_list', 'schoolName='
			+ new BaseClass().encode($F('schoolName'), true) + param1 + param2);
}

// 分页查询方法
function schoolJump(/* string */_url, /* int */curPage) {
	pag.query(_url + curPage, 'school_list');
}

// 根据所选城市来动态地获取这城市中的机构列表
function getAgencySelectByCityId(city_id,/* id */innerHtml,/* id */agcySelect) {
	new Ajax.Request('/library/school.do?action=getAgencySelect&city_id='
			+ city_id, {
		method : 'POST',
		evalScripts : true,
		parameters : '',
		onComplete : function(xmlHttp, error) {
			$(innerHtml).innerHTML = "<select id=\""+ agcySelect
					+ "\" style= \"width:250\" name=\"" + agcySelect + "\">" + xmlHttp.responseText
					+ "</select>";
		}
	});
}

// 添加页面显示
function schoolAdd() {
	pag.showAdd(baseUrl + '?action=create', 'school_update', 'school_index',
			function() {
			});
}

// 删除
function schoolDel(id) {
	var _id = base.getChecked('id', true);
	if (!id && _id.length < 1) {
		base.alert('请先选择要删除的学校！');
		return;
	}
	base.log('要删除的学校ID为：' + (id || _id.join(',')));
	base.confirm('您确信要删除选中的学校吗？', function() {
		pag.del(baseUrl + '?action=remove', '', 'id='
				+ (id || _id.join('&id=')), schoolQuery);
	});
}

// 编辑页面显示
function schoolEdit(id) {
	var _id = base.getChecked('id', true);
	if (!id && _id.length != 1) {
		base.alert('请先选择要编辑的学校，编辑只能一次选择一条记录！');
		return;
	}
	base.log('要编辑的学校ID为：' + (id || _id[0]));
	pag.showEdit(baseUrl + '?action=edit', 'school_update', 'school_index', {
		id : id || _id[0]
	}, function() {
	});
}
// 查看页面显示
function schoolDetail(id) {
	var _id = base.getChecked('id', true);
	base.log('要查看的学校ID为：' + (id || _id[0]));
	pag.showEdit(baseUrl + '?action=show', 'school_update', 'school_index', {
		id : id || _id[0]
	}, function() {
	});
}

// 添加和更新信息的内容检查 itype=1 添加 itype=2 更新
function schoolUpdateCheck(itype) {
	var et = $('school_name');
	if (!et || et.value.strip() == '') {
		base.alert('请输入学校名称！', function() {
			et.focus();
		});
		return false;
	}

	et = $('editAgencySelect');
	if (et.value.strip() == '-1') {
		base.alert('请选择学校所属的机构！', function() {
			et.focus();
		});
		return false;
	}
	et = base.getChecked('containGrade', true);
	if (et.length < 1) {
		base.alert('请选择学校包含的年级类型！');
		return false;
	}
	pag.update(baseUrl + '?action=save', 'schoolUpdateForm', function() {
		pag.back('school_index', 'school_update');
		schoolQuery();
	});

	return false;
}

// 导入初始化
function importinit() {
	pag.showAdd(baseUrl + '?action=importSchoolInit', 'school_update',
			'school_index', function() {
			});
}

// 导入模板的下载
function download() {
	window.location.href = baseUrl + '?action=download';
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
			schoolUpdateForm.importattach_path.value = '';
			schoolUpdateForm.importName.value = '';
			document.getElementById('file_delete').style.display = 'none';
		}
	});
}

// 导入操作
function importHandle() {
	var et = $('importAgencyId');
	if (!et || et.value.strip() == '' || et.value.strip() == '-1') {
		base.alert('请选择机构！', function() {
		});
		return false;
	}
	et = $('importattach_path');
	if (!et || et.value.strip() == '') {
		base.alert('请先上传数据文件！', function() {
		});
		return false;
	}
	// ajax方式提交表单
	pag.update(baseUrl + '?action=importSchoolSubmit', 'schoolUpdateForm',
			function() {
				pag.back('school_index', 'school_update');
				schoolQuery();
			});
	return false;
}

// 导出学校数据
function downloadSchool() {
	if (pag == null)
		pag = new PageExe();
	var param1;
	if ('-1' == $F('citySelect') || $F('citySelect') == null) {
		param1 = '';
	} else {
		param1 = '&cityId=' + $F('citySelect');
	}
	var param2;
	if ('-1' == $F('agencySelect') || $F('agencySelect') == null) {
		param2 = '';
	} else {
		param2 = '&agencyId=' + $F('agencySelect');
	}
	window.location.href = baseUrl + '?action=downloadSchool&schoolName='
			+ new BaseClass().encode($F('schoolName'), true) + param1 + param2;
}
