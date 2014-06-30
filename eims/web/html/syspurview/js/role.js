/**
 * 角色列表管理的JS
 */

var pag = null;

// 页面加载执行函数
function roleQuery()
{
	if (pag == null) pag = new PageExe();
	pag.query(
		'/syspurview/role.do?action=query',
		'role_list', 
		'name=' + ($('qname').value.strip() == '' ? '' : base.encode($('qname').value))
	);
}

// 添加角色页面显示
function roleAdd()
{
	pag.showAdd(
		'/syspurview/role.do?action=create',
		'role_update', 
		'role_index',
		function() { $('name').focus();}
	);
}

// 编辑角色页面显示
function roleEdit(roleId)
{
	var _roleId = base.getChecked('roleId', true);
	if (!roleId && _roleId.length != 1) {
		base.alert('请先选择要编辑的角色，编辑只能一次选择一个角色！');
		return;
	}
	base.log('要编辑的角色ID为：' + (roleId || _roleId[0]));
	pag.showEdit(
		'/syspurview/role.do?action=edit',
		'role_update',
		'role_index',
		{
			id : roleId || _roleId[0]
		},
		function() { $('name').focus();}
	);
}

// 删除角色
function roleDel(roleId)
{
	var _roleId = base.getChecked('roleId', true);
	if (!roleId && _roleId.length < 1) {
		base.alert('请先选择要删除的角色！');
		return;
	}
	base.log('要删除的角色ID为：' + (roleId || _roleId.join(',')));
	base.confirm('您确信要删除选中的角色吗？', function() {
		// 执行删除操作
		pag.del(
			'/syspurview/role.do?action=remove',
			'下列角色因为包含用户，系统不允许进行删除',
			'id=' + (roleId || _roleId.join('&id=')),
			roleQuery
		);
	});
}

// 角色编辑或添加的检查函数（使用页面添加时） 1-添加 2-编辑
function roleUpdateCheck(itype)
{
	var name = roleUpdateForm.name, detail = roleUpdateForm.detail;
	if (name.value.strip() == '') {
		base.alert('请输入角色的名称！',function(){name.value = '';name.focus();});
		return false;
	} else if (detail.value.strip().length >= 250) {
		base.alert('角色的描述或介绍不能超过250个字符！',function(){detail.focus();});
		return false;
	}
	name.value = name.value.strip();
	detail.value = detail.value.strip();
	// var conf = confirm('您确信要' + (itype==1 ? '添加该角色' : '修改该角色') + '?');
	var conf = true;
	if (!conf) return false;
	//$('role_update_btn').disabled = true; // 提交按钮不可用
	
	// 使用ajax方式的表单提交
	pag.update(
		'/syspurview/role.do?action=' + (itype == 1 ? 'save' : 'update'),
		'roleUpdateForm',
		function() {
			pag.back('role_index','role_update');
			roleQuery();
		}
	);
	
	return false;
}

//*------------------------角色授权管理-------------------------------*//

// 角色授权页面显示
function roleGrant(roleId, roleName)
{
	base.log('要授权的角色是[Id=' + roleId + ',name=' + roleName + ']', 'info');
	pag.showEdit(
		'/syspurview/role.do?action=grantIndex&roleName='+base.encode(roleName),
		'role_update',
		'role_index',
		{
			roleId : roleId
		},
		roleGetMenuList
	);
}

// 获取左边的菜单列表
function roleGetMenuList(/*string*/pToken)
{
	base.log('开始获取菜单列表~~', 'info');
	pag.query(
		'/syspurview/role.do?action=grantMenu',
		'menu_list',
		'sysId=' + $('sysId').value + '&token=' + ((pToken != null && typeof(pToken) == 'strign') ? pToken : '')
	);
}

// 子模块的显示或获取
function roleQueryModuleChild(/*object*/img, /*string*/mToken, /*boolean*/pMenu)
{
	// 二级菜单的打开要查询数据库并且执行页面替换操作
	if (!pMenu) {
		roleGetMenuList(mToken);
		return;
	}
	var _img = img.getAttribute("src").toLowerCase();
	var tmp = _img.split('/');
	var _imgName = tmp[tmp.length - 1];
	if (_imgName == 'menu_close.gif') {
		base.log('执行' + (pMenu ? '一' : '二') + '级菜单模块[' + mToken + ']的打开动作', 'info');
		tmp[tmp.length - 1] = 'menu_open.gif';
		img.setAttribute("src", tmp.join("/"));
		roleShowHideChilds(mToken, true);
	} else {
		base.log('执行' + (pMenu ? '一' : '二') + '级菜单模块[' + mToken + ']的关闭动作', 'info');
		tmp[tmp.length - 1] = 'menu_close.gif';
		img.setAttribute("src", tmp.join("/"));
		roleShowHideChilds(mToken, false);
	}
}

// 打开或关闭指定的菜单模块下的子模块
function roleShowHideChilds(/*string*/pToken, /*boolean*/show)
{
	var trs = $('mod_body').getElementsByTagName('TR');
	for (var i = 0; i < trs.length; i ++) {
		if (!trs[i].name) continue;
		if (trs[i].name == pToken) continue;
		if (trs[i].name.indexOf(pToken) != 0) continue;
		if (show) Element.show(trs[i]);
		else Element.hide(trs[i]);
	}
}

// 查询指定模块（菜单或功能模块）下的所有模块和其功能
function roleQueryModuleFunc(/*element*/span, /*string*/mToken, /*boolean*/isMenu)
{
	base.log('查询模块' + mToken + '下的所有功能模块', 'info');
	// 首先获取所有的列表，同时置选中的模块文字为黑体，其他的为普通
	var spans = $('mod_body').getElementsByTagName('SPAN');
	for (var i = 0; i < spans.length; i ++) {
		if (spans[i].name != '_m653_Name_') continue;
		Element.removeClassName(spans[i], 'mLeftBold');
	}
	Element.addClassName(span, 'mLeftBold');
	base.request(
		'/syspurview/role.do?action=editRolePurview',
		function(xmlHttp, error) {
			var returnVal = xmlHttp.responseText;
			if (base.exe(returnVal)) {
				base.log('返回信息中含有脚本，执行成功！', 'info');
				return;
			}
			var obj = base.json(returnVal);
			if (obj != null && obj.success == false) {
				base.alert(obj.message);
			} else {
				$('menu_module').innerHTML = returnVal;
			}
		},
		'sysId=' + $('sysId').value + '&token=' + (mToken ? mToken : '')
			+ '&roleId=' + $('grantRoleId').value
	);
}

// 指定功能模块的所有功能全选或全不选
function roleSltAllFunc(/*element*/tr, /*boolean*/slt)
{
	var td = tr.getElementsByTagName('TD')[1];
	var inputs = td.getElementsByTagName('INPUT');
	for (var i = 0; i < inputs.length; i ++) {
		if (inputs[i].type != 'checkbox') continue;
		inputs[i].checked = slt;
	}
}

// 角色授权提交
function roleGrantSubmit()
{
	var inputs = $('menu_module').getElementsByTagName('INPUT');
	if (inputs.length < 1) {
		base.alert('当前还没有选择任何模块和其功能，无法提交！');
		return;
	}
	// 检查是否选择了，没有则提示
	var checkedCount = 0;
	for (var i = 0; i < inputs.length; i ++) {
		if (inputs[i].type != 'checkbox') continue;
		if (inputs[i].checked) checkedCount ++;
	}
	if (checkedCount == 0) {
		base.confirm('您确信要删除角色对当前选择的所有模块的权限吗？', function() {
			// 获取当前所有的模块ID
			var tmp = document.getElementsByName('moduleId');
			var mId = [];
			for (var i = 0; i < tmp.length; i ++) {
				if (tmp[i].type != 'hidden') continue;
				mId.push(tmp[i].value);
			}
			if (mId.length < 1) {
				base.alert('当前没有选择任何模块，无法进行授权！');
				return;
			}
			base.request(
				'/syspurview/role.do?action=removeModulePurview',
				roleGrantCallback,
				'grantRoleId=' + $('grantRoleId').value + '&moduleId=' + mId.join('&moduleId=')
			);
		});
		return;
	}
	base.confirm('您确信要提交对角色的授权吗？', function() {
		roleGrantDisabledBtn(true);
		base.formSubmit(
			'/syspurview/role.do?action=grantPurview',
			roleGrantCallback,
			'roleGrantForm'
		);
	});
}

// 角色授权成功后的回调方法
function roleGrantCallback(xmlHttp, error)
{
	var returnVal = xmlHttp.responseText;
	if (base.exe(returnVal)) {
		base.log('返回信息中包含脚本，执行成功！', 'info');
		return;
	}
	roleGrantDisabledBtn(false);
	var obj = base.json(returnVal);
	if (obj == null) {
		base.alert('系统发生异常；\n' + returnVal);
		return;
	}
	base.tips(obj.message, $('roleGrantForm'));
	window.setTimeout(function() {
		base.tips('', $('roleGrantForm'));
	}, obj.success ? 3000 : 6000);
}

// 角色授权页面的所有功能选择
function roleGrantSelectAll(/*boolean*/checked)
{
	var ckbox = $('roleGrantForm').getElementsByTagName('INPUT');
	for (var i = 0; i < ckbox.length; i ++) {
		if (ckbox[i].type != 'checkbox') continue;
		ckbox[i].checked = checked;
	}
}

// 授权页面的操作按钮不可用
function roleGrantDisabledBtn(/*boolean*/dis)
{
	$('grant_btn_submit').disabled = dis;
	$('grant_btn_cancle').disabled = dis;
	$('grant_btn_delete').disabled = dis;
}

// 删除角色的所有权限
function roleDeletePurview()
{
	base.confirm('您确信要删除当前角色的所有权限吗？', function() {
		roleGrantDisabledBtn(true);
		base.request(
			'/syspurview/role.do?action=removeAllPurview',
			function(xmlHttp, error) {
				var returnVal = xmlHttp.responseText;
				if (base.exe(returnVal)) {
					base.log('返回信息中包含脚本，执行成功！', 'info');
					return;
				}
				roleGrantDisabledBtn(false);
				var obj = base.json(returnVal);
				if (obj == null) {
					base.alert('系统发生异常；\n' + returnVal);
					return;
				}
				base.tips(obj.message, $('roleGrantForm'));
				window.setTimeout(function() {
					base.tips('', $('roleGrantForm'));
				}, obj.success ? 3000 : 6000);
			},
			'roleId=' + $('grantRoleId').value
		);
	});
}