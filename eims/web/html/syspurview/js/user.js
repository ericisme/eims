/**
 * 系统用户管理的js
 */

var pag = null;

 // 系统用户的列表查询
function userQuery()
{
	if (pag == null) pag = new PageExe();
	var qv='';
	if($F('qryGroupId')!='-1'){
	  qv+='&qryGroupId=' + $F('qryGroupId');
	}
	if($F('queryUserType')!='-1'){
	  qv+='&queryUserType=' + $F('queryUserType');
	}
	pag.query(
		'/syspurview/user.do?action=query',
		'user_list',
		'qryName=' + base.encode($('qryName').value)+qv
	);
}

// 用户的分页查询方法
function userJump(/*string*/_url, /*int*/curPage)
{
	pag.query(
		_url + curPage,
		'user_list'
	);
}

// 添加页面显示
function userAdd()
{
	pag.showAdd(
		'/syspurview/user.do?action=create',
		'user_update', 
		'user_index',
		function() { $('realName').focus();}
	);
}

// 查看用户的详细资料
var userDetailCache = null;
function userDetail(userId)
{
	if (userDetailCache == null) userDetailCache = new Object();
	base.log('查看用户[' + userId + ']所属的详细情况！', 'info');
	if (userDetailCache[userId]) {
		base.alert(userDetailCache[userId]);
		return;
	}
	pag.jsonReq(
		'/syspurview/user.do?action=show',
		function(obj) {
			if (obj.success == false) base.alert(obj.message);
			else {
				var txt = '该用户的详细信息如下：\n\n';
				var user = obj.user;
				txt += '用户ID  : ' + user.userId + '\n';
				txt += '用户组  : ' + user.groupName + '\n';
				txt += '真实姓名: ' + user.realName + '\n';
				txt += '登陆名称: ' + user.loginName + '\n';
				txt += '用户性别: ' + (user.man ? '男' : '女') + '\n';
				txt += '手机号码: ' + user.mobile + '\n';
				txt += '电子邮件: ' + user.email + '\n';
				txt += '添加时间: ' + user.addTime + '\n';
				txt += '最后登陆时间: ' + user.lastLoginTime + '\n';
				txt += '最后登陆IP  : ' + user.lastLoginIP + '\n';
				txt += '登陆总次数  : ' + user.loginTimes + '\n';
				txt += '帐号状态: ' + (user.lock ? '封锁' : '正常') + '\n';
				userDetailCache[userId] = txt;
				base.alert(txt);
			}
		},
		{
			userId : userId
		}
	);
}

// 用户的解锁与封锁执行
var locking = false;
function userLockAndUnlock()
{
	if (locking == true) {
		base.alert('正在进行用户的封锁或解锁操作，请稍后再执行！');
		return;
	}
	var userId = base.getChecked('userId', true);
	if (userId.length < 1) {
		base.alert('请选择要进行操作的用户！');
		return;
	}
	var lock = null;
	for (var i = 0; i < userId.length; i ++) {
		if (lock == null && i == 0) lock = $('status_' + userId[i]).value;
		if ($('status_' + userId[i]).value != lock) {
			base.alert('请选择同一类型的用户进行操作！比如帐号都是被封锁状态或都是正常状态的用户！');
			return;
		}
	}
	base.confirm('您确信要对选择的用户进行' + (lock == 'false' ? '封锁' : '解锁') + '操作吗？', function() {
		pag.jsonReq(
			'/syspurview/user.do?action=lock',
			function(obj) {
				base.tips(obj.message);
				window.setTimeout(function() {
					base.tips();
				}, obj.success ? 3000 : 8000);
				if (!obj.success) return;
				for (var i = 0; i < userId.length; i ++) {
					var show = $('show_' + userId[i]);
					if (!show) continue;
					if (lock == "true") {
						show.innerHTML = '<img src="' + show.name + '/images/normal.gif" border="0" title="正常" />';
					} else {
						show.innerHTML = '<img src="' + show.name + '/images/lock.gif" border="0" title="封锁" />';
					}
					//show.innerHTML = lock == 'true' ? '正常' : '<font color="red">封锁</font>';
					var stat = $('status_' + userId[i]);
					stat.value = (lock == 'true' ? 'false' : 'true');
				}
			},
			'lock=' + (lock == 'false' ? 'true' : 'false') + '&userId=' + userId.join('&userId=')
		);
	});
}

// 删除用户
function userDel(userId)
{
	var _userId = base.getChecked('userId', true);
	if (!userId && _userId.length < 1) {
		base.alert('请先选择要删除的用户！');
		return;
	}
	base.log('要删除的用户ID为：' + (userId || _userId.join(',')));
	base.confirm('您确信要删除选中的用户吗？', function() {
		pag.del(
			'/syspurview/user.do?action=remove',
			'',
			'userId=' + (userId || _userId.join('&userId=')),
			userQuery
		);
	});
}

// 编辑页面显示
function userEdit(userId)
{
	var _userId = base.getChecked('userId', true);
	if (!userId && _userId.length != 1) {
		base.alert('请先选择要编辑的用户，编辑只能一次选择一个用户！');
		return;
	}
	base.log('要编辑的用户ID为：' + (userId || _userId[0]));
	pag.showEdit(
		'/syspurview/user.do?action=edit',
		'user_update',
		'user_index',
		{
			userId : userId || _userId[0]
		},
		function() { $('realName').focus();}
	);
}

// 用户的登陆名称的唯一性检测
var oldFlag = null;
function userLoginNameCheck(value)
{
	if (value.strip() == '' || !Reg.isUserName(value)) return;
	if (oldFlag == null || oldFlag == value) {
		if ($('ckresult').value == 'success') return true;
		if ($('ckresult').value == 'exist') return '该用户登陆名称已存在，请重新选择！';
	}
	oldFlag = value;
	if ($('ckresult').value == 'loading') return '正在检测用户登陆名称的唯一性，请稍后提交！';
	$('ckresult').value = 'loading';
	$('cktips').innerHTML = '用户登陆名称的唯一性检测中……';
	base.request('/syspurview/user.do?action=checkUserName', function(xmlHttp, error) {
		base.log('用户登陆名称检测返回~~~', 'info');
		if (base.exe(xmlHttp.responseText)) {
			base.log('检测到返回信息中包含脚本并执行成功，返回！','info');
			return;
		}
		var obj = base.json(xmlHttp.responseText);
		if (!$('cktips')) return;
		if (obj.success == false) {
			$('cktips').innerHTML = '<font color="red">' + obj.message + '</font>';
		} else {
			$('cktips').innerHTML = '<font color="green">该用户登陆名称还未被使用！</font>';
		}
		$('ckresult').value = obj.success ? 'success' : 'exist';
	}, 'loginName=' + value);
}

// 添加和更新用户信息的内容检查 itype=1 添加 itype=2 更新
function userUpdateCheck(itype)
{
    var et = $('groupId');
	if (!et || et.value.strip() == '-1') {
		base.alert('请选择用户所属的用户组！', function(){et.focus();});
		return false;
	}
	et = $('userType');
	if (!et || et.value.strip() == '-1') {
		base.alert('请选择用户所属的用户类型！',function(){et.focus();});
		return false;
	}
	 et = $('realName');
	if (!et || et.value.strip() == '') {
		base.alert('请输入用户的真实姓名！',function(){et.focus();});
		return false;
	}
	
	et = $('loginName');
	if (itype == 1 && (!et || et.value.strip() == '')) {
		base.alert('请输入用户的登陆名称，登陆名称只能包含英文字母、下划线和数字！',function(){et.focus();});
		return false;
	} else if (itype == 1 && !Reg.isUserName(et.value.strip())) {
		base.alert('用户登陆名称中只允许包含英文字母、下划线和数字，且长度在3到15个字符之间！',function(){et.focus();});
		return false;
	}
	if (itype == 1) {
		var msg = userLoginNameCheck(et.value.strip());
		if (typeof(msg) == 'undefined') return false;
		if (msg != true) {
			base.alert(msg);
			return false;
		}
		et = $('loginPassword');
		if (!et || et.value.strip() == '') {
			base.alert('请输入用户登陆密码！',function(){et.focus();});
			return false;
		} else if (et.value.strip().length < 6) {
			base.alert('为保证帐号安全，用户的登陆密码至少为6位！',function(){et.focus();});
			return false;
		}
		
		var et1 = $('reLoginPasspword');
		if (!et1 || et1.value.strip() == '') {
			base.alert('请再次输入用户登陆密码！',function(){et1.focus();});
			return false;
		} else if (et.value.strip() != et1.value.strip()) {
			base.alert('两次输入的密码不一致！',function(){et1.focus();});
			return false;
		}
	}
	
	/**
	et = $('mobile');
	if (!et || et.value.strip() == '') {
		base.alert('请输入用户的手机号码！',function(){et.focus();});
		return false;
	} else if (!Reg.isMobile(et.value.strip())) {
		base.alert('请输入正确的手机号码！',function(){et.focus();});
		return false;
	}
	**/

	et = $('email');
	if (et && et.value.strip() != "" && !Reg.isEmail(et.value.strip())) {
		base.alert('请输入正确的电子邮件地址！',function(){et.focus();});
		return false;
	}
	pag.update(
		'/syspurview/user.do?action=' + (itype == 1 ? 'save' : 'update'),
		'userUpdateForm',
		function() {
			pag.back('user_index','user_update');
			userQuery();
		}
	);
	
	return false;
}

function updatePwd(userId){
	var _userId = base.getChecked('userId', true);
	if (!userId && _userId.length < 1) {
		base.alert('请先选择要进行密码重置的用户！');
		return;
	}
	base.confirm('您确信要对选中的用户进行密码重置操作吗？', function() {
		pag.jsonReq(
			'/syspurview/user.do?action=updatePwd',
			function(obj) {
				window.setTimeout(function() {
					if (obj.success == true) base.alert(obj.message);
					else {
						base.tips(obj.message, null, false, false);
						window.setTimeout(function() {
							base.tips(null, null, false, false);
						}, 6000);
					}
				},50);
			},
			'userId=' + (userId || _userId.join('&userId='))
		);
	});
}