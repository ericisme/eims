/**
 * 用户组管理的js
 */

var pag = null;

function groupQuery()
{
	if (pag == null) pag = new PageExe();
	pag.query(
		'/syspurview/group.do?action=query',
		'group_list'
	);
}

// 添加页面显示
function groupAdd()
{
	pag.showAdd(
		'/syspurview/group.do?action=create',
		'group_update', 
		'group_index',
		function() { $('groupName').focus();}
	);
}

// 用户组的角色详细情况查看
var groupDetailCache = null;
function groupDetail(groupId)
{
	if (groupDetailCache == null) groupDetailCache = new Object();
	base.log('查看用户组[' + groupId + ']所属的角色的详细情况！', 'info');
	if (groupDetailCache[groupId]) {
		base.alert(groupDetailCache[groupId]);
		return;
	}
	pag.jsonReq(
		'/syspurview/group.do?action=show',
		function(obj) {
			if (obj.success == false) base.alert(obj.message);
			else {
				var txt = '该用户组包含的角色信息如下：\n\n';
				var roles = obj.roles;
				for (var i = 0; i < roles.length; i ++) {
					txt += '角色ID：' + roles[i].roleId + '      角色名称：' + roles[i].name + '\n';
				}
				groupDetailCache[groupId] = txt;
				base.alert(txt);
			}
		},
		{
			groupId : groupId
		}
	);
}

// 系统默认用户组 1-系统管理员 2-单位管理员 3-单位普通用户 不允许被删除！
function groupDel(groupId)
{
	var _groupId = base.getChecked('groupId', true);
	if (!groupId && _groupId.length < 1) {
		base.alert('请先选择要删除的用户组！');
		return;
	}
	
	if (groupId && parseInt($(groupId + '_users').innerHTML) > 0) {
		base.alert('该用户组下已有用户，系统不允许删除！如果要删除请先删除该用户组下的用户！');
		return;
	}
	/*if (groupId && groupId <= 3) {
		base.alert('系统默认的用户组[系统管理员、单位管理员、单位普通用户]不允许被删除！');
		return;
	}*/
	if (!groupId) {
		var tips = '下列用户组因包含用户，系统不允许进行删除：\n', nums = 0;
		for (var i = 0; i < _groupId.length; i ++) {
			if (parseInt($(_groupId[i] + '_users').innerHTML) == 0) continue;
			tips += '      ' + $(_groupId[i] + '_name').innerHTML + '\n';
			nums += 1;
		}
		if (nums > 0 && nums < _groupId.length) base.alert(tips);
		if (nums == _groupId.length) {
			base.alert('所选择的用户组都包含用户，不允许进行删除操作！');
			return;
		}
	}
		
	base.log('要删除的用户组ID为：' + (groupId || _groupId.join(',')));
	base.confirm('您确信要删除选中的用户组吗？', function() {
		// 执行删除操作
		pag.del(
			'/syspurview/group.do?action=remove',
			'',
			'groupId=' + (groupId || _groupId.join('&groupId=')),
			groupQuery
		);
	});
}

// 编辑页面显示
function groupEdit(groupId)
{
	var _groupId = base.getChecked('groupId', true);
	if (!groupId && _groupId.length != 1) {
		base.alert('请先选择要编辑的用户组，编辑只能一次选择一个用户组信息！');
		return;
	}
	base.log('要编辑的用户组ID为：' + (groupId || _groupId[0]));
	pag.showEdit(
		'/syspurview/group.do?action=edit',
		'group_update',
		'group_index',
		{
			groupId : groupId || _groupId[0]
		},
		function() { $('groupName').focus();}
	);
}

// 添加和更新用户组的内容检查 itype=1 添加 itype=2 更新
function groupUpdateCheck(itype)
{
	var et = $('groupName');
	if (!et || et.value.strip() == '') {
		base.alert('请输用户的名称！',function(){et.focus();});
		return false;
	}
	
	if (Select.len('groupRole') < 1) {
		base.alert('请至少为用户组指定一个所属角色！');
		return false;
	} else {
		Select.selectAll('groupRole');
	}
	
	// ajax方式提交表单
	pag.update(
		'/syspurview/group.do?action=' + (itype == 1 ? 'save' : 'update'),
		'groupUpdateForm',
		function() {
			pag.back('group_index','group_update');
			groupQuery();
		}
	);
	
	return false;
}

// 用户组角色的添加
function groupAddRole()
{
	Select.copy('s_roleId', 'groupRole', function(opt, tOptions) {
		if (opt.selected) {
			for (var i = 0; i < tOptions.length; i ++) {
				if (tOptions[i].value == opt.value) return false;
			}
			return true;
		}
		return false;
	});
}

// 用户组角色的删除
function groupDelRole()
{
	Select.clear('groupRole', function(opt) {
		if (opt.selected) return true;
		return false;
	});
}