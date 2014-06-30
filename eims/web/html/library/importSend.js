var pag = new PageExe();
var baseUrl = '/library/xxptnews.do';

// 模板下载
function download() {
	window.location.href = baseUrl + '?action=download';
}

// 批量数据上传后的操作
function uploadEnd() {
	document.getElementById('file_delete').innerHTML = "<a href='JavaScript:void(0);' onclick='delete_file()'><font color='red'>"
			+ document.getElementById('importName').value
			+ "</font><img src='/images/del.gif' border='0'/></a>";
	document.getElementById('file_delete').style.display = '';
}

// 删除上传文件操作
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
			importSendingForm.importattach_path.value = '';
			importSendingForm.importName.value = '';
			document.getElementById('file_delete').style.display = 'none';
		}
	});
}

// 导入操作
function importHandle() {
	et = $('importattach_path');
	if (!et || et.value.strip() == '') {
		base.alert('请先上传数据文件！', function() {
		});
		return false;
	}
	var btn = PageExe.submitButton('importSendingForm'); // 获取表单区域的提交按钮
	if (btn) btn.disabled = true;
	base.formSubmit(
		baseUrl + '?action=importSendingSubmit',
		function(xmlHttp, error) {
			var returnVal = xmlHttp.responseText;
			$('importSending_feedback').innerHTML=returnVal;
			Element.show("importSending_feedback");
		},
		'importSendingForm'
	);
	return false;
}

//返回导入初始状态
function back(){
	Element.hide("importSending_feedback");
	var btn = PageExe.submitButton('importSendingForm'); // 获取表单区域的提交按钮
	if (btn) btn.disabled = false;
	importSendingForm.importattach_path.value = '';
	importSendingForm.importName.value = '';
	document.getElementById('file_delete').style.display = 'none';
}