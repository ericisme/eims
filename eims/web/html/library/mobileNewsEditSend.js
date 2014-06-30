/**
 * 
 */

var pag = null;
var baseUrl = '/library/xxptnews.do';

 // 列表查询
function xxptNewsQuery()
{
	if (pag == null) pag = new PageExe();
	pag.query(
		baseUrl + '?action=mobileNewsEditSend',
		'xxptNews_list',
		'' //TODO params
	);
}

//编辑发送列表查询学生按钮
function studentQuery()
{
	if (pag == null) pag = new PageExe();
	var qv = '';
	if ('-1' != $F('queryAgencyId')) {
		qv += '&queryAgencyId=' + $F('queryAgencyId');
	}
	if('-1'!=$F('querySchoolId')){
	 	qv+='&querySchoolId='+$F('querySchoolId');
	}
	if('-1'!=$F('queryGradeId')){
	 	qv+='&queryGradeId='+$F('queryGradeId');
	}
	pag.query(
		baseUrl + '?action=mobileNewsEditSend',
		'xxptNews_list',
		'1=1'+qv
	);
}
 
// 分页查询方法
function xxptNewsJump(/*string*/_url, /*int*/curPage)
{
	pag.query(
		_url + curPage,
		'xxptNews_list'
	);
}

// 添加页面显示
function xxptNewsAdd()
{
	pag.showAdd(
		baseUrl + '?action=create',
		'xxptNews_update', 
		'xxptNews_index',
		function() { }
	);
}

// 删除
function xxptNewsDel(id)
{
	var _id = base.getChecked('id', true);
	if (!id && _id.length < 1) {
		base.alert('请先选择要删除的短信管理！');
		return;
	}
	base.log('要删除的短信管理ID为：' + (id || _id.join(',')));
	base.confirm('您确信要删除选中的短信管理吗？', function() {
		pag.del(
			baseUrl + '?action=remove',
			'',
			'id=' + (id || _id.join('&id=')),
			xxptNewsQuery
		);
	});
}

// 编辑页面显示
function xxptNewsEdit(id)
{
	var _id = base.getChecked('id', true);
	if (!id && _id.length != 1) {
		base.alert('请先选择要编辑的短信管理，编辑只能一次选择一条记录！');
		return;
	}
	base.log('要编辑的短信管理ID为：' + (id || _id[0]));
	pag.showEdit(
		baseUrl + '?action=edit',
		'xxptNews_update',
		'xxptNews_index',
		{
			id : id || _id[0]
		},
		function() {}
	);
}
// 查看页面显示
function xxptNewsDetail(id)
{
	var _id = base.getChecked('id', true);
	base.log('要查看的短信管理ID为：' + (id || _id[0]));
	pag.showEdit(
		baseUrl + '?action=show',
		'xxptNews_update',
		'xxptNews_index',
		{
			id : id || _id[0]
		},
		function() {}
	);
}


// 添加和更新信息的内容检查 itype=1 添加 itype=2 更新
function xxptNewsUpdateCheck(itype)
{
//	var et = $('userName');
//	if (!et || et.value.strip() == '') {
//		base.alert('请输入用户的真实姓名！',function(){et.focus();});
//		return false;
//	}

	// ajax方式提交表单
	pag.update(
		baseUrl + '?action=save',
		'xxptNewsUpdateForm',
		function() {
			pag.back('xxptNews_index','xxptNews_update');
			xxptNewsQuery();
		}
	);
	
	return false;
}

function mobileNewsSend(){
	var et = $('xxlb');
 	if (!et || et.value.strip() == '-1') {
 		base.alert('请选择信息类型!',function(){et.focus();});
 		return false;
 	}
	et = $('newscontent');
 	if (!et || et.value.strip() == '') {
 		base.alert('请输入信息内容!',function(){et.focus();});
 		return false;
 	}
	et = $('querySchoolId');
 	if (!et || et.value.strip() == '-1') {
 		base.alert('请选择学校!',function(){et.focus();});
 		return false;
 	}
 	et = $('queryGradeId');
 	if (!et || et.value.strip() == '-1') {
 		base.alert('短信发送的最小单位为年级,请选择年级!',function(){et.focus();});
 		return false;
 	}
	base.formSubmit(baseUrl + '?action=mobileNewsSend', function(xmlHttp, error) {
		var obj = base.json(xmlHttp.responseText);
		   alert(obj.message);		  
	}, 'mobileNewsSendForm');
	return false;
}