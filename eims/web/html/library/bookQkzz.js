	var pag = null;
	var baseUrl = '/library/bookQkzz.do';
	
	// 列表查询
	function bookQuery() {
		if (pag == null)
			pag = new PageExe();
		var param="";
		if($F('queryCityId')!=null && $F('queryCityId')!=-1)
			param+="&queryCityId="+$F('queryCityId');
		if($F('queryAgencyId')!=null && $F('queryAgencyId')!=-1)
			param+="&queryAgencyId="+$F('queryAgencyId');
		if($F('querySchoolId')!=null && $F('querySchoolId')!=-1)
			param+="&querySchoolId="+$F('querySchoolId');
		if($F('bookIndex')!=null && $F('bookIndex')!='')
			param+="&bookIndex="+$F('bookIndex');
		if($F('bookStorageTime')!=null && $F('bookStorageTime')!='')
			param+="&bookStorageTime="+$F('bookStorageTime');
		if($F('bookName')!=null && $F('bookName')!='')
			param+="&bookName="+new BaseClass().encode($F('bookName'), true);
		/**
		 * 2011-6-10 add for 可阅读性查询
		 */
		param+="&kydx="+$F('kydx')
		
		pag.query(baseUrl + '?action=list', 'book_list',param);
	}
	
	// 分页查询方法
	function bookJump(/* string */_url, /* int */curPage) {
		pag.query(_url + curPage, 'book_list');
	}
	
	// 添加页面显示
	function bookAdd() {
		pag.showAdd(baseUrl + '?action=create', 'book_update', 'book_index',
				function() {
				});
	}
	
	// 删除
	function bookDel(id) {
		var _id = base.getChecked('id', true);
		if (!id && _id.length < 1) {
			base.alert('请先选择要删除的图书！');
			return;
		}
		base.log('要删除的图书ID为：' + (id || _id.join(',')));
		base.confirm('您确信要删除选中的图书吗？', function() {
			pag.del(baseUrl + '?action=remove', '', 'id='
					+ (id || _id.join('&id=')), bookQuery);
		});
	}
	
	// 废弃（2011-6-22）
	function bookFq(id) {
		var _id = base.getChecked('id', true);
		if (!id && _id.length < 1) {
			base.alert('请先选择要废弃的书籍！');
			return;
		}
		base.log('要废弃的图书ID为：' + (id || _id.join(',')));
		base.confirm('废弃后的书籍将不能复原，确定废弃？', function() {
			pag.del(baseUrl + '?action=fq', '', 'id='
					+ (id || _id.join('&id=')), bookQuery);
		});
	}
	
	
	
	
	
	
	
	// 编辑页面显示
	function bookEdit(id) {
		var _id = base.getChecked('id', true);
		if (!id && _id.length != 1) {
			base.alert('请先选择要编辑的图书，编辑只能一次选择一条记录！');
			return;
		}
		base.log('要编辑的图书ID为：' + (id || _id[0]));
		pag.showEdit(baseUrl + '?action=edit', 'book_update', 'book_index', {
			id : id || _id[0]
		}, function() {
		});
	}
	// 查看页面显示
	function bookDetail(id) {
		var _id = base.getChecked('id', true);
		base.log('要查看的图书ID为：' + (id || _id[0]));
		pag.showEdit(baseUrl + '?action=show', 'book_update', 'book_index', {
			id : id || _id[0]
		}, function() {
		});
	}
	
	// 添加和更新信息的内容检查 itype=1 添加 itype=2 更新
	function bookUpdateCheck(itype) {
		var et = $('book_name');
		if (!et || et.value.strip() == '') {
			base.alert('请输入图书名称！', function() {
				et.focus();
			});
			return false;
		}
		et = $('category_id');
		if (!et || et.value.strip() == '-1') {
			base.alert('请选择图书分类！', function() {
				et.focus();
			});
			return false;
		}
		/**  修改在2011-6-21
		 */
//		et = $('category_name');
//		if (!et || et.value.strip() == '') {
//			base.alert('请输入图书分类名称！', function() {
//				et.focus();
//			});
//			return false;
//		}
//		et = $('category_code');
//		if (!et || et.value.strip() == '') {
//			base.alert('请输入图书分类号！', function() {
//				et.focus();
//			});
//			return false;
//		}
		et = $('book_price');
		if (!et || et.value.strip() == '') {
			base.alert('请输入图书价格！', function() {
				et.focus();
			});
			return false;
		}
		et = $('book_place');
		if (!et || et.value.strip() == '') {
			base.alert('请输入图书存放位置！', function() {
				et.focus();
			});
			return false;
		}
		et = $('querySchoolId2');
		if (!et || et.value.strip() == '-1') {
			base.alert('请选择图书所属学校！', function() {
				et.focus();
			});
			return false;
		}
		
		// ajax方式提交表单
		if(itype=='1'){
			pag.update(baseUrl + '?action=save', 'bookUpdateForm', function() {
			pag.back('book_index', 'book_update');
			bookQuery();
			});
		}else{
			pag.update(baseUrl + '?action=saveUpdate', 'bookUpdateForm', function() {
			pag.back('book_index', 'book_update');
			bookQuery();
			});
		}
		return false;
	}
	//导出机构信息
	//function exportAgencys(){
	////	window.location.href=baseUrl+'?action=downloadAgency&book_name__like__string='
	//					+ new BaseClass().encode($F('bookName'), true)
	//					+ '&orders=id__asc';
	//}
	
	// 导出图书数据
	function downloadBook() {
		if (pag == null)
			pag = new PageExe();
		var qv = '';
		var et = $('queryCityId');
		if ( et&&et!=null&&'-1' != et.value) {
			qv += '&queryCityId=' + et.value;
		}
		var et = $('queryAgencyId');
		if(et&&et!=null&&'-1'!=et.value){
		 	qv += '&queryAgencyId='+et.value;
		}
		var et = $('querySchoolId');
		if(et&&et!=null&&'-1'!=et.value){
		 	qv += '&querySchoolId='+et.value;
		}
		var et = $('bookIndex');
		if(et&&et!=null&&''!=et.value){
		 	qv += '&bookIndex=' + et.value;
		}
		var et = $('bookName');
		if(et&&et!=null&&''!=et.value){
		 	qv += '&bookName=' + et.value;
		}
		var et = $('bookStorageTime');
		if(et&&et!=null&&''!=et.value){
		 	qv += '&bookStorageTime=' + et.value;
		}
		/**
		 * 2011-6-10 add for 可阅读性查询
		 */
		qv+="&kydx="+$F('kydx')
		
		window.location.href = baseUrl + '?action=downloadBook' + qv;
		
	}
	
	// 导出图书数据
	function downloadPrint() {
		if (pag == null)
			pag = new PageExe();
		var qv = '';
		var et = $('queryCityId');
		if ( et&&et!=null&&'-1' != et.value) {
			qv += '&queryCityId=' + et.value;
		}
		var et = $('queryAgencyId');
		if(et&&et!=null&&'-1'!=et.value){
		 	qv += '&queryAgencyId='+et.value;
		}
		var et = $('querySchoolId');
		if(et&&et!=null&&'-1'!=et.value){
		 	qv += '&querySchoolId='+et.value;
		}
		var et = $('bookIndex');
		if(et&&et!=null&&''!=et.value){
		 	qv += '&bookIndex=' + et.value;
		}
		var et = $('bookName');
		if(et&&et!=null&&''!=et.value){
		 	qv += '&bookName=' + et.value;
		}
		var et = $('bookStorageTime');
		if(et&&et!=null&&''!=et.value){
		 	qv += '&bookStorageTime=' + et.value;
		}
		/**
		 * 2011-6-10 add for 可阅读性查询
		 */
		qv+="&kydx="+$F('kydx')
		
		window.location.href = baseUrl + '?action=downloadPrint' + qv;
		
	}
	
	// 导入学生初始化�
	function importinit() {
		pag.showAdd(baseUrl + '?action=importBookInit', 'book_update',
				'book_index', function() {
				});
	}
	
	// 导入模板的下载�
	function download() {
		window.location.href = baseUrl + '?action=download';
	}
	
	
	// 导入学生操作
	function importHandle()
	{
		var et = $('importCityId');
	    if (!et || et.value.strip() == ''||et.value.strip()=='-1') {
		 	base.alert('请选择镇区',function(){});
		 	return false;
	 	}
	    et = $('importAgencyId');
	    if (!et || et.value.strip() == ''||et.value.strip()=='-1') {
		 	base.alert('请选择机构',function(){});
		 	return false;
	 	}
	 	et = $('importSchoolId');
	    if (!et || et.value.strip() == ''||et.value.strip()=='-1') {
		 	base.alert('请选择学校',function(){});
		 	return false;
	 	}
	 	et = $('importattach_path');
	   if (!et || et.value.strip() == '') {
		 	base.alert('请先上传数据文件!',function(){});
		 	return false;
	 	}
	 	document.getElementById("sys_update_btn").disabled=true;
	 	return true;
		// ajax方式提交表单
		//pag.update(
		//	baseUrl + '?action=importStudentSubmit',
		//	'bookUpdateForm',
		//	function() {
		//		pag.back('book_index','book_update');
		//		bookQuery();
		//	}
		//);
		return false;
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
				studentUpdateForm.importattach_path.value = '';
				studentUpdateForm.importName.value = '';
				document.getElementById('file_delete').style.display = 'none';
			}
		});
	}
	
	function changeCategory(parent_id,toId)
	{	
		new Ajax.Request(
			'/library/common.do?action=getCategoryTwoSelect&parent_id='+parent_id,
				{
					method : 'POST',
					evalScripts : true,
					parameters : '',
					onComplete : function(xmlHttp,error){
						var returnVal = xmlHttp.responseText;
						$(toId).innerHTML = returnVal;
					}
				}
			);
	}
	function toCategory(id,nameId,codeId){
		new Ajax.Request(
			'/library/bookQkzz.do?action=getCategoryNameCode&id='+id,
				{
					method : 'POST',
					evalScripts : true,
					parameters : '',
					onComplete : function(xmlHttp,error){
						var returnVal = xmlHttp.responseText;
						try{
							var nameCode = returnVal.split(",");
							$(nameId).value = nameCode[0];
							$(codeId).value = nameCode[1];
						}catch(e){
						
						}
					
					}
				}
			);
	}
	
	function downloadFile(file) {
		window.location.href = baseUrl + '?action=downloadFile&file='+new BaseClass().encode(file, true);
	}

	
	
	
	
	
