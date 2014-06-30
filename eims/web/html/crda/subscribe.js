/**
 * 
 */

var pag = null;
var baseUrl = '/crda/subscribe.do';

 // 列表查询
function subscribeQuery()
{
	if (pag == null) pag = new PageExe();
	var qv = '';
	if ('-2' != $F('chargeback')) {
		qv += '&chargeback__eq__int=' + $F('chargeback');
	}
	qv+='&stu_unique__like__string=' +new BaseClass().encode($F('stu_unique'), true);
	qv+='&mobile__like__string=' +new BaseClass().encode($F('mobile'), true);
	qv+='&QDateTimeSelectStart='+$F('QDateTimeSelectStart')+'&QDateTimeSelectEnd='+$F('QDateTimeSelectEnd');
	qv+= '&orders=subscribeTime__desc';
	pag.query(
		baseUrl + '?action=list',
		'subscribe_list',
		qv
	);
}

// 分页查询方法
function subscribeJump(/*string*/_url, /*int*/curPage)
{
	pag.query(
		_url + curPage,
		'subscribe_list'
	);
}

// 添加页面显示
function subscribeAdd()
{
	pag.showAdd(
		baseUrl + '?action=create',
		'subscribe_update', 
		'subscribe_index',
		function() { }
	);
}

// 删除
function subscribeDel(id)
{
	var _id = base.getChecked('id', true);
	if (!id && _id.length < 1) {
		base.alert('请先选择要删除的订阅管理！');
		return;
	}
	base.log('要删除的订阅管理ID为：' + (id || _id.join(',')));
	base.confirm('您确信要删除选中的订阅管理吗？', function() {
		pag.del(
			baseUrl + '?action=remove',
			'',
			'id=' + (id || _id.join('&id=')),
			subscribeQuery
		);
	});
}

// 编辑页面显示
function subscribeEdit(id)
{
	var _id = base.getChecked('id', true);
	if (!id && _id.length != 1) {
		base.alert('请先选择要编辑的订阅管理，编辑只能一次选择一条记录！');
		return;
	}
	base.log('要编辑的订阅管理ID为：' + (id || _id[0]));
	pag.showEdit(
		baseUrl + '?action=edit',
		'subscribe_update',
		'subscribe_index',
		{
			id : id || _id[0]
		},
		function() {}
	);
}
// 查看页面显示
function subscribeDetail(id)
{
	var _id = base.getChecked('id', true);
	base.log('要查看的订阅管理ID为：' + (id || _id[0]));
	pag.showEdit(
		baseUrl + '?action=show',
		'subscribe_update',
		'subscribe_index',
		{
			id : id || _id[0]
		},
		function() {}
	);
}


// 添加和更新信息的内容检查 itype=1 添加 itype=2 更新
function subscribeUpdateCheck(itype)
{
//	var et = $('userName');
//	if (!et || et.value.strip() == '') {
//		base.alert('请输入用户的真实姓名！',function(){et.focus();});
//		return false;
//	}

	// ajax方式提交表单
	pag.update(
		baseUrl + '?action=save',
		'subscribeUpdateForm',
		function() {
			pag.back('subscribe_index','subscribe_update');
			subscribeQuery();
		}
	);
	
	return false;
}


function getCurrentDate(){
	var firstDate = getCurentFistDay();
	$("QDateTimeSelectStart").value=firstDate.getFullYear()+"-"+format(firstDate.getMonth()+1)+"-"+format(firstDate.getDate());
	$("QDateTimeSelectEnd").value=new Date().getFullYear()+"-"+format(new Date().getMonth()+1)+"-"+format(new Date().getDate());
}
 

//清除页面上的查询时间
function clearTime(){
	$("QDateTimeSelectStart").value='';
	$("QDateTimeSelectEnd").value='';
}

//获得当月第一天
function getCurentFistDay()   
{   
	var year = new Date().getFullYear();
	var month = new Date().getMonth();
	var new_year = year;    //取当前的年份   
	var new_month = month++;//取下一个月的第一天，方便计算（最后一天不固定）   
	if(month>12)            //如果当前大于12月，则年份转到下一年   
	{   
		new_month -=12;        //月份减   
		new_year++;            //年份增   
	}   
    return new Date(new_year,new_month,1); //取当年当月中的第一天   
}