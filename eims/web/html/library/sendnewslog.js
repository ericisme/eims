/**
 * 
 */

var pag = null;
var baseUrl = '/library/sendnewslog.do';

 // 列表查询
function sendNewsLogQuery()
{
	if (pag == null) pag = new PageExe();
	var qv = '';
	if ('-1' != $F('xxlb')) {
		qv += '&news_type.id__eq__int=' + $F('xxlb');
	}
	qv+='&student_unique__like__string=' +new BaseClass().encode($F('student_unique'), true);
	qv+='&mobile__like__string=' +new BaseClass().encode($F('recivemobile'), true);
	qv+='&newscontent__like__string=' +new BaseClass().encode($F('newscontent'), true);
	qv+='&QDateTimeSelectStart='+$F('QDateTimeSelectStart')+'&QDateTimeSelectEnd='+$F('QDateTimeSelectEnd');
	qv+= '&orders=send_time__desc';
	pag.query(
		baseUrl + '?action=list',
		'sendNewsLog_list',
		qv
	);
}

// 分页查询方法
function sendNewsLogJump(/*string*/_url, /*int*/curPage)
{
	pag.query(
		_url + curPage,
		'sendNewsLog_list'
	);
}

   
// 查看页面显示
function sendNewsLogDetail(id)
{
	var _id = base.getChecked('id', true);
	base.log('要查看的信息发送日志ID为：' + (id || _id[0]));
	pag.showEdit(
		baseUrl + '?action=show',
		'sendNewsLog_update',
		'sendNewsLog_index',
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