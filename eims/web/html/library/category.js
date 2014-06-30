/**
 * 
 */

var pag = null;
var baseUrl = '/library/category.do';

// 列表查询
function categoryQuery() {
	if (pag == null)
		pag = new PageExe();
	var param="";
	pag.query(baseUrl + '?action=list', 'category_list',
			'categoryName='+ new BaseClass().encode($F('categoryName'), true)
			+'&categoryCode='+new BaseClass().encode($F('categoryCode'), true)
			+ '&orders=id__asc'+param);
}
function childrenQuery(childrenId,parent_id) {
	if (pag == null)
		pag = new PageExe();
	var param="";
	pag.query(baseUrl + '?action=childrenList&parent_id='+parent_id, childrenId);
}

function categoryQueryChild(obj,childrenid){
	var cj = document.getElementsByName(childrenid);
	if(cj==null){
		alert("该分类没有子类别");
		return;
	}
	if(cj[0].style.display=='none'){
		obj.src="/html/syspurview/images/menu_open.gif";
		for(var i=0;i<cj.length;i++)
			cj[i].style.display="block";
	}else{
		obj.src="/html/syspurview/images/menu_close.gif";
		for(var i=0;i<cj.length;i++)
			cj[i].style.display='none';
	}
}

// 分页查询方法
function categoryJump(/* string */_url, /* int */curPage) {
	pag.query(_url + curPage, 'category_list');
}

// 添加页面显示
function categoryAdd() {
	pag.showAdd(baseUrl + '?action=create', 'category_update', 'category_index',
			function() {
			});
}

// 删除
function categoryDel(id) {
	var _id = base.getChecked('id', true);
	if (!id && _id.length < 1) {
		base.alert('请先选择要删除的分类！');
		return;
	}
	base.log('要删除的分类ID为：' + (id || _id.join(',')));
	base.confirm('您确信要删除选中的分类吗？', function() {
		pag.del(baseUrl + '?action=remove', '', 'id='
				+ (id || _id.join('&id=')), categoryQuery);
	});
}

// 编辑页面显示
function categoryEdit(id) {
	var _id = base.getChecked('id', true);
	if (!id && _id.length != 1) {
		base.alert('请先选择要编辑的分类，编辑只能一次选择一条记录！');
		return;
	}
	base.log('要编辑的分类ID为：' + (id || _id[0]));
	pag.showEdit(baseUrl + '?action=edit', 'category_update', 'category_index', {
		id : id || _id[0]
	}, function() {
	});
}
// 查看页面显示
function categoryDetail(id) {
	var _id = base.getChecked('id', true);
	base.log('要查看的分类ID为：' + (id || _id[0]));
	pag.showEdit(baseUrl + '?action=show', 'category_update', 'category_index', {
		id : id || _id[0]
	}, function() {
	});
}

// 添加和更新信息的内容检查 itype=1 添加 itype=2 更新
function categoryUpdateCheck(itype) {
	var et = $('name');
	if (!et || et.value.strip() == '') {
		base.alert('请输入分类名称！', function() {
			et.focus();
		});
		return false;
	}
	et = $('code');
	if (!et || et.value.strip() == '') {
		base.alert('请输入分类名称！', function() {
			et.focus();
		});
		return false;
	}
	// ajax方式提交表单
	pag.update(baseUrl + '?action=save', 'categoryUpdateForm', function() {
		pag.back('category_index', 'category_update');
		categoryQuery();
	});
	return false;
}
//导出分类信息
function exportAgencys(){
	window.location.href=baseUrl+'?action=downloadAgency&category_name__like__string='
					+ new BaseClass().encode($F('categoryName'), true)
					+ '&orders=id__asc';
}


