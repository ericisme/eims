/**
 * 
 */

var pag = null;
var baseUrl = '/library/usersendnewslog.do';

 // 列表查询
function userSendNewsLogQuery()
{
	//'msgTitle__like__string=' + $F('qryTitle') + '&item.itemId__eq__int=' + $F('qryItem') + '&orders=mdfTime__desc'
	if (pag == null) pag = new PageExe();
	var qv = '';
	if ('-1' != $F('xxlb')) {
		qv += '&news_type.id__eq__int=' + $F('xxlb');
	}
	qv+='&user_name__like__string=' +new BaseClass().encode($F('createUsername'), true);
	qv+='&newscontent__like__string=' +new BaseClass().encode($F('newscontent'), true);
	qv+='&QDateTimeSelectStart='+$F('QDateTimeSelectStart')+'&QDateTimeSelectEnd='+$F('QDateTimeSelectEnd');
	qv+= '&orders=send_time__desc';
	pag.query(
		baseUrl + '?action=list',
		'userSendNewsLog_list',
		qv
	);
}

// 分页查询方法
function userSendNewsLogJump(/*string*/_url, /*int*/curPage)
{
	pag.query(
		_url + curPage,
		'userSendNewsLog_list'
	);
} 
// 查看页面显示
function userSendNewsLogDetail(id)
{
	var _id = base.getChecked('id', true);
	base.log('要查看的用户发送短信操作日志ID为：' + (id || _id[0]));
	pag.showEdit(
		baseUrl + '?action=show',
		'userSendNewsLog_update',
		'userSendNewsLog_index',
		{
			id : id || _id[0]
		},
		function() {}
	);
}

function getCurrentDate(){
	$("QDateTimeSelectStart").value=new Date().getFullYear()+"-"+format(new Date().getMonth()+1)+"-"+format(new Date().getDate());
	$("QDateTimeSelectEnd").value=new Date().getFullYear()+"-"+format(new Date().getMonth()+1)+"-"+format(new Date().getDate());
}

//清除页面上的查询时间
function clearTime(){
	$("QDateTimeSelectStart").value='';
	$("QDateTimeSelectEnd").value='';
}