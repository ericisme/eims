/**
 * 子系统管理的js
 */

var pag = null;

 // 子系统的列表查询
function sysQuery()
{
	if (pag == null) pag = new PageExe();
	pag.query(
		'/syspurview/system.do?action=query',
		'sys_list'
	);
}

// 添加页面显示
function sysAdd()
{
	pag.showAdd(
		'/syspurview/system.do?action=create',
		'sys_update', 
		'sys_index',
		function() { $('sysName').focus();}
	);
}

// 子系统的标记检测
var oldFlag = null;
function sysCheckFlag(value)
{
	if (value.strip() == '') return;
	if (oldFlag == null || oldFlag == value) {
		if ($('ckresult').value == 'success') return true;
		if ($('ckresult').value == 'exist') return '该标记已存在，请重新选择！';
	}
	oldFlag = value;
	if ($('ckresult').value == 'loading') return '正在检测标记唯一性，请稍后提交！';
	$('ckresult').value = 'loading';
	$('cktips').innerHTML = '子系统标记的唯一性检测中……';
	base.request('/syspurview/system.do?action=checkFlag', function(xmlHttp, error) {
		base.log('子系统的标记检测返回~~~', 'info');
		if (base.exe(xmlHttp.responseText)) {
			base.log('检测到返回信息中包含脚本并执行成功，返回！','info');
			return;
		}
		var obj = base.json(xmlHttp.responseText);
		if (!$('cktips')) return;
		if (obj.success == false) {
			$('cktips').innerHTML = obj.message;
		} else {
			$('cktips').innerHTML = obj.exist ? '<font color="red">该标记已存在，请重新选择！</font>' : '该标记还未被使用！';
		}
		if (obj.success) $('ckresult').value = !obj.exist ? 'success' : 'exist';
	}, 'flag=' + value);
}

// 刷新子系统的缓存
function sysRefreshCache()
{
	base.request(
		'/syspurview/system.do?action=refreshCache', 
		function(xmlHttp, error) {
			base.log('子系统缓存刷新返回~~~', 'info');
			if (base.exe(xmlHttp.responseText)) {
				base.log('检测到返回信息中包含脚本并执行成功，返回！','info');
				return;
			}
			var obj = base.json(xmlHttp.responseText);
			if (obj != null) base.alert(obj.message);
			else alert('子系统缓存刷新失败！');
		}
	);
}

// 删除子系统
function sysDel(sysId)
{
	var _sysId = base.getChecked('sysId', true);
	if (!sysId && _sysId.length < 1) {
		base.alert('请先选择要删除的子系统！');
		return;
	}
	base.log('要删除的子系统ID为：' + (sysId || _sysId.join(',')));
	base.confirm('您确信要删除选中的子系统吗？', function() {
		// 执行删除操作
		pag.del(
			'/syspurview/system.do?action=remove',
			'下列子系统因为包含菜单或功能模块，系统不允许进行删除',
			'sysId=' + (sysId || _sysId.join('&sysId=')),
			sysQuery
		);
	});
}

// 编辑页面显示
function sysEdit(sysId)
{
	var _sysId = base.getChecked('sysId', true);
	if (!sysId && _sysId.length != 1) {
		base.alert('请先选择要编辑的子系统，编辑只能一次选择一个子系统！');
		return;
	}
	base.log('要编辑的子系统ID为：' + (sysId || _sysId[0]));
	pag.showEdit(
		'/syspurview/system.do?action=edit',
		'sys_update',
		'sys_index',
		{
			sysId : sysId || _sysId[0]
		},
		function() { $('sysName').focus();}
	);
}

// 添加和更新子系统内容检查 itype=1 添加 itype=2 更新
function sysUpdateCheck(itype)
{
	var et = $('sysName');
	if (!et || et.value.strip() == '') {
		base.alert('请输入子系统的名称！',function(){et.focus();});
		return false;
	}
	
	et = $('sysFlag');
	if (itype == 1 && (!et || et.value.strip() == '')) {
		base.alert('请输入子系统的标记！',function(){et.focus();});
		return false;
	} else if (itype == 1 && !sysIsRightFlag(et.value.strip())) {
		base.alert('子系统标记只允许包含字母个下划线，并且只允许以字母开头和结束！',function(){et.focus();});
		return false;
	}
	if (itype == 1) {
		var msg = sysCheckFlag(et.value.strip());
		if (typeof(msg) == 'undefined') return false;
		if (msg != true) {
			base.alert(msg);
			return false;
		}
	}
	
	et = $('sysUrl');
	if (!et || et.value.strip() == '') {
		base.alert('请输入子系统的链接地址！',function(){et.focus();});
		return false;
	}
	
	et = $('sysSequence');
	if (!et || et.value.strip() == '' || isNaN(et.value.strip())) {
		base.alert('子系统的排序序号不能为空，且只允许是数字！',function(){et.focus();});
		return false;
	}
	
	// ajax方式提交表单
	pag.update(
		'/syspurview/system.do?action=' + (itype == 1 ? 'save' : 'update'),
		'sysUpdateForm',
		function() {
			pag.back('sys_index','sys_update');
			sysQuery();
		}
	);
	
	return false;
}

// 判断是否是正确的flag标记
function sysIsRightFlag(str)
{
	var p = /^[a-zA-Z][a-zA-Z0-9_]{2,14}$/
	return p.test(str);
}