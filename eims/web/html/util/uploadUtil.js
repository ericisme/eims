
// 文件上传后调用的函数
function importend(){
 document.getElementById('file_delete').innerHTML = 
	 "<a href='JavaScript:void(0);' onclick='delete_file()'><font color='red'>"
	 +document.getElementById('importName').value+"</font><img src='/images/del.gif' border='0'/></a>";
	 document.getElementById('file_delete').style.display='';
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
					studentUpdateForm.importattach_path.value = '';
					studentUpdateForm.importName.value = '';
					document.getElementById('file_delete').style.display = 'none';
				}
			});
}