/**
 * 控制面板JS控制
 */

var pag = null;

 // 初始化
function controlInit()
{
	if (pag == null) pag = new PageExe();
}

// 用户个人信息编辑
function controlUpdateUserInfo()
{
	var et = $('realName');
	if (!et || et.value.strip() == '') {
		base.alert('请输入真实姓名！');
		et.focus();
		return false;
	}
	
	et = $('mobile');
	if (!et || et.value.strip() == '') {
		base.alert('请输入用户的手机号码！');
		et.focus();
		return false;
	} else if (!Reg.isMobile(et.value.strip())) {
		base.alert('请输入正确的手机号码！');
		et.focus();
		return false;
	}
	
	et = $('email');
	if (et && et.value.strip() != "" && !Reg.isEmail(et.value.strip())) {
		base.alert('请输入正确的电子邮件地址！');
		et.focus();
		return false;
	}
	
	et = $('loginPassword');
	if (!et || et.value.strip() == '') {
		base.alert('请输入当前帐号的登陆密码！');
		et.focus();
		return false;
	}
	
	// ajax方式提交表单
	pag.update(
		'/syspurview/controlpanel.do?action=updatePersonalInfo',
		'userUpdateForm',
		function() {
			pag.back('control_index','control_update');
		}
	);
	
	return false;
}

// 用户密码修改确认
function controlUpdateUserPwd()
{
	var et = $('oldPassword');
	if (!et || et.value.strip() == '') {
		base.alert('请输入当前帐号的原始密码！', function() {et.focus();});
		return false;
	}
	
	et = $('loginPassword');
	if (!et || et.value.strip() == '' || et.value.strip().length < 6) {
		base.alert('请输入帐号的新密码，且密码长度不能少于6位！', function(){et.focus();});
		return false;
	}
	
	var et1 = $('reLoginPassword');
	if (!et1 || et1.value.strip() == '') {
		base.alert('请输入帐号的新密码确认密码！', function(){et1.focus();});
		return false;
	} else if (et1.value.strip() != et.value.strip()) {
		base.alert('新密码和确认密码不一致，请重新输入！', function(){et1.focus();});
		return false;
	}
	
	// ajax方式提交表单
	pag.update(
		'/syspurview/controlpanel.do?action=updatePassword',
		'userPwdUpdateForm',
		function() {
			pag.back('control_index','control_update');
		}
	);
	
	return false;
}

// 用户个人信息编辑页面显示
function controlUserInfoEdit()
{
	pag.showEdit(
		'/syspurview/controlpanel.do?action=editPersonalInfo',
		'control_update',
		'control_index'
	);
}

// 用户密码修改页面显示
function controlUserPassword()
{
	pag.showEdit(
		'/syspurview/controlpanel.do?action=editPassword',
		'control_update',
		'control_index'
	);
}

// 用户密码保护
function controlUserCryptoguard()
{
	pag.showEdit(
		'/syspurview/controlpanel.do?action=setCryptoguard',
		'control_update',
		'control_index'
	);
}

// 用户密码保护提交
function controlUpdateCryptoguard()
{
	var et = $('userQuestion');
	if (!et || et.value.strip() == '') {
		base.alert('请输入密码保护的问题！比如：\n\n我的生日是什么时候？',function(){et.focus();});
		return false;
	}
	
	et = $('userAnswer');
	if (!et || et.value.strip() == '') {
		base.alert('请输入密码保护问题的答案！',function(){et.focus();});
		return false;
	}
	
	var method = 'email';
	if ($('MobileGetMethod').checked) method = 'mobile';
	
	if (method == 'email') {
		et = $('userEmail');
		if (!et || et.value.strip() == '') {
			base.alert('您选择了使用Email取回密码，请输入Email地址！',function(){et.focus();});
			return false;
		} else if (!Reg.isEmail(et.value.strip())) {
			base.alert('请输入正确的电子邮件地址！',function(){et.focus();});
			return false;
		}
	} else {
		et = $('userMobile');
		if (!et || et.value.strip() == '') {
			base.alert('您选择了使用手机取回密码，请输入您的手机号码！',function(){et.focus();});
			return false;
		} else if (!Reg.isMobile(et.value.strip())) {
			base.alert('请输入正确的手机号码！',function(){et.focus();});
			return false;
		}
	}
	
	// ajax方式提交表单
	pag.update(
		'/syspurview/controlpanel.do?action=updateUserCryptoguard',
		'userCryptoguardUpdateForm',
		function() {
			pag.back('control_index','control_update');
		}
	);
	
	return false;
}

// 个人后台管理风格设置
function controlStyle()
{
	//base.alert('风格设置暂时禁止使用^_^!');return;
	pag.showEdit(
		'/syspurview/controlpanel.do?action=setPersonalStyle',
		'control_update',
		'control_index'
	);
}

// 用户的风格设置提交
function controlUpdateUserStyle()
{
	// 先保存设置的样式，然后数据库更新成功后再执行页面的样式更改
	var layout = $('layoutName').value;
	var css = $('cssName').value;
	
	if (layout == $('layoutName_old').value && css == $('cssName_old').value) {
		base.alert('请选择不同的风格再提交！');
		return false;
	}
	
	// ajax方式提交表单
	pag.update(
		'/syspurview/controlpanel.do?action=updateUserStyle',
		'userStyleUpdateForm',
		function() {
			// 如果修改的风格版面和当前版面不一致则不进行风格即时替换
			if (layout != $('layoutName_old').value) {
				//pag.back('control_index','control_update');
				base.tips('该风格需要刷新或重新登陆后才能看到效果！', $('userStyleUpdateForm'), true);
				// 立刻刷新页面
				parent.location.reload();
				return false;
			}
			
			// 否则进行风格的动态替换（相同版面布局的风格）
			// 1.设置admin_index的css样式
			var HeadElement = parent.document.getElementsByTagName("HEAD")[0];
			var _css = parent.$('cssStyle');
			var _cssClone = _css.cloneNode(true);
			parent.Element.remove(_css);
			_cssClone.href = '/css/styles/' + css + '/admin.css';
			HeadElement.appendChild(_cssClone);
			
			// 2.设置左边窗口的样式
			var _winNum = parent.winMag.getWinNum(parent.cfg.leftWinName);
			if (_winNum) {
				with(parent.WinLIKE.windows[_winNum].innerframe()) {
					var HeadElement = document.getElementsByTagName("HEAD")[0];
					var _css = document.getElementById('cssStyle');
					var _cssClone = _css.cloneNode(true);
					parent.Element.remove(_css);
					_cssClone.href = '/css/styles/' + css + '/left.css';
					HeadElement.appendChild(_cssClone);
				}
			}
			
			// 3.设置窗口皮肤
			var wins = parent.WinManager.winNameList; // 所有已打开的窗口
			for (var i = 0; i < wins.length; i ++) {
				_winNum = parent.winMag.getWinNum(wins[i]);
				if (!_winNum) continue;
				var win = parent.WinLIKE.windows[_winNum];
				win.Ski = css;
				win.draw();
				// 设置每个窗体内页的CSS样式文件
				with(win.innerframe()) {
					var _css = document.getElementById('sysBodyCss');
					if (_css) _css.href='/css/styles/' + css + '/body.css';
					else {
						// 没有取到则表示是权限系统的页面
						var sysCss = document.getElementsByTagName('LINK');
						for (var j = 0; j < sysCss.length; j ++) {
							if (sysCss[j].href.indexOf('/html/syspurview/css/') == 0) {
								sysCss[j].href = '/html/syspurview/css/' + css + '.css';
								break;
							}
						}	
					}
				}
			}
			// 修改配置文件中的窗口皮肤名称
			parent.cfg.winSkin = css;
			//pag.back('control_index','control_update');
		},
		500
	);
	
	return false;
}

// 风格的选择，修改隐藏域的值
function controlSetMyStyle(layout, css)
{
	$('layoutName').value = layout;
	$('cssName').value = css;
}

// 个人助理-日历 
function controlCalculate(/*string*/baseDir)
{
	window.location.href = baseDir + '/controlpanel/almanac.html';
	/*pag.showEdit(
		baseDir + '/controlpanel/calculate.html',
		'control_update',
		'control_index'
	);*/
}

// 个人助理-计算器 
function controlAlmanac(/*string*/baseDir)
{
	window.location.href = baseDir + '/controlpanel/calculate.html';
}