/**
 * 
 */

var pag = null;
var baseUrl = '/library/xxptnews.do';

// 列表查询
function xxptNewsQuery() {
	if (pag == null)
		pag = new PageExe();
	var param="QDateTimeSelectStart="+$F('QDateTimeSelectStart')+"&QDateTimeSelectEnd="+$F('QDateTimeSelectEnd');
	if($F('QAgencySelect')!=-1)param+='&agency_id__eq__int='+$F('QAgencySelect');
	if($F('QSchoolSelect')!=-1)param+='&school_id__eq__int='+$F('QSchoolSelect');
	if($F('QGradeSelect')!=null&&$F('QGradeSelect')!=-1)param+='&grade_id__eq__int='+$F('QGradeSelect');
	if($F('QMsgTypeSelect')!=-1)param+='&newstype_id__eq__int='+$F('QMsgTypeSelect');
	if($F('QSendingSysSelect')!=-1)param+='&outsystem_paramid__eq__int='+$F('QSendingSysSelect');
	if($F('QSendingStateSelect')!=-1)param+='&newsstatus__eq__int='+$F('QSendingStateSelect');
	param+='&orders=send_time__desc'
	pag.query(baseUrl + '?action=list', 
	'xxptNews_list',
	param);
}

// 改变学校列表内容
function xxptChangeSchoolSelect(agency_id,name1,name2,innerName1,innerName2) {
	if(agency_id==-1){
		$(innerName1).innerHTML ="<select name=\"" + name1 + "\" id=\""
					+ name1 + "\" style=\"width:280\">" +
							"<option value=\"-1\">--请选择--</option></select>";
		$(innerName2).innerHTML ="<select name=\"" + name2 + "\" id=\""
					+ name2 + "\" style=\"width:80\">" +
							"<option value=\"-1\">--请选择--</option></select>";
		return;
	}
	new Ajax.Request('/library/school.do?action=getSchoolSelect&agency_id='+ agency_id, 
		{
			method : 'POST',
			evalScripts : true,
			parameters : '',
			onComplete : function(xmlHttp, error) {
			var returnVal = xmlHttp.responseText;
			$(innerName1).innerHTML = "<select name=\"" + name1 + "\" id=\""
				+ name1 + "\" style=\"width:280\" onchange=\"xxptChangeGradeSelect(this.value,'"
				+ name2 + "','" + innerName2 + "');\">" + returnVal
				+ "</select>";
			}
		});
	xxptChangeGradeSelect(-1,name2 , innerName2);
}

// 改变年级列表内容
function xxptChangeGradeSelect(schoolId,name,innerName) {
	new Ajax.Request(
		'/library/school.do?action=getGradeSelectHtmlByChangeSchool&school_id='+schoolId,
		{
			method : 'POST',
			evalScripts : true,
			parameters : '',
			onComplete : function(xmlHttp,error){
				var returnVal = xmlHttp.responseText;
				$(innerName).innerHTML ="<select name=\"" + name + "\" id=\""
					+ name + "\" style=\"width:80\"><option value=\"-1\">--请选择--</option>"
					+ returnVal + "</select>";
			}
		}
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

// 查看页面显示
function xxptNewsDetail(id)
{
	pag.showEdit(
		baseUrl + '?action=show',
		'xxptNews_update',
		'xxptNews_index',
		{
			id : id
		},
		function() {}
	);
}

//清除页面上的查询时间
function clearTime(){
	$("QDateTimeSelectStart").value='';
	$("QDateTimeSelectEnd").value='';
}