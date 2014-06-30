/**
 * 菜单和功能模块管理的js
 */

var pag = null;

// 列表查询
var mNameExclude = ['%','&','@','#','*','`','$','!','?'];
function moduleQuery()
{
	if (pag == null) pag = new PageExe();
	var qName = $('moduleName').value.strip();
	for (var i = 0; i < mNameExclude.length; i ++) {
		if (qName.indexOf(mNameExclude[i]) != -1) {
			base.alert('模块的查询名称中不允许包含下列非法字符：\n' + mNameExclude.join('  '), function(){
				$('moduleName').focus();
			});
			return;
		}
	}
	pag.query(
		'/syspurview/module.do?action=query',
		'module_list',
		'sysId=' + $('searchSysId').value + '&moduleName=' + base.encode(qName)
	);
}

// 添加页面显示
function moduleAdd(pToken)
{
	pag.showAdd(
		'/syspurview/module.do?action=create&ptoken=' + (!pToken ? '' : pToken),
		'module_update', 
		'module_index',
		function() {
			$('MName').focus();
			moduleCacheSltSystemMenu();
		}
	);
}

// 添加页面的父菜单模块的选择，如果不是一级模块则无须选择子系统，自动继承其父菜单
function moduleSltParentMenu(/*string*/token, /*int*/systemSize)
{
	if (systemSize <= 1) return;
	if (token == "") {
		Element.hide('sys_tr');
		Select.select('sysId', 0);
	} else {
		Element.show('sys_tr');
	}
}


// 删除, 一次只允许删除一个模块
function moduleDel(/*string*/mToken, /*boolean*/sysModule)
{
	base.log('要删除的模块是否是系统模块：sysModule=' + sysModule, 'info');
	if (sysModule) {
		base.alert('系统模块不允许删除！');
		return;
	}
	if (!mToken) {
		base.log('没有选择要删除的模块，退出删除！', 'warn');
		return;
	}
	base.log('要删除的模块Token为：' + mToken);
	base.confirm('您确信要删除该模块吗？', function() {
		// 执行删除操作
		pag.del(
			'/syspurview/module.do?action=remove',
			'下列菜单模块因为包含子菜单模块或功能模块，系统不允许进行删除',
			'mToken=' + mToken,
			moduleQuery
		);
	});
}

// 编辑页面显示
function moduleEdit(moduleId)
{
	base.log('要编辑的模块ID为：' + moduleId);
	pag.showEdit(
		'/syspurview/module.do?action=edit',
		'module_update',
		'module_index',
		{
			mId : moduleId
		},
		function() {
			$('MName').focus();
			moduleCacheSltSystemMenu();
		}
	);
}

// 根据子系统ID获取其可用菜单的列表
var sysMenuCache = new Object();
function moduleCacheSltSystemMenu(sysId)
{
	var sltSys = sysId || $('sysId').value;
	if (!sysMenuCache[sltSys]) sysMenuCache[sltSys] = $('parentMenu').parentNode.innerHTML;
}

function moduleGetMenuBySystem(sysId)
{
	if (sysMenuCache[sysId]) {
		base.log('子系统[' + sysId + ']的菜单列表已被缓存~~~', 'info');
		$('parentMenu').parentNode.innerHTML = sysMenuCache[sysId];
	} else {
		base.log('从服务器获取子系统[' + sysId + ']的菜单列表~~', 'info');
		base.request(
			'/syspurview/module.do?action=getMenu',
			function(xmlHttp,error) {
				base.log('[回调方法]从服务器返回子系统的菜单信息！', 'info');
				var returnVal = xmlHttp.responseText;
				if (base.exe(returnVal)) {
					base.log('服务器返回信息中包含脚本，成功执行！', 'info');
					return;
				}
				var obj = base.json(returnVal);
				if (obj == null) {
					base.alert('系统发生异常：\n' + returnVal);
					return;
				}
				if (obj.success == false) {
					base.alert(obj.message);
				} else {
					Select.clear('parentMenu');
					Select.addOption('parentMenu', '', '---作为一级模块---');
					Select.add('parentMenu', obj.select);
					moduleCacheSltSystemMenu(sysId);
				}
			},
			'sysId=' + sysId
		);
	}
}

// 菜单模块和功能模块的添加区域切换
function moduleChangeToMenu(toMenu)
{
	base.log('菜单模块和功能模块的添加切换~~[value=' + toMenu + ']', 'info');
	if (toMenu == true) {
		Element.hide('tr_mlink');
		Element.hide('stat_pub');
		$('mStatus1').checked = true;
	} else {
		Element.show('tr_mlink');
		Element.show('stat_pub');
	}
}

// 添加和更新模块的内容检查 itype=1 添加 itype=2 更新
function moduleUpdateCheck(itype)
{
	var passed = false;
	if ($('menu1') && $('menu1').checked) passed = moduleCheckMenu();
	else passed = moduleCheckFuncModule();
	if (!passed) return false;
	
	// ajax方式提交表单
	pag.update(
		'/syspurview/module.do?action=' + (itype == 1 ? 'save' : 'update'),
		'moduleUpdateForm',
		function() {
			pag.back('module_index','module_update');
			moduleQuery();
		}
	);
	
	return false;
}

// 菜单添加的检测
function moduleCheckMenu()
{
	var et = $('sysId');
	if (et.value == '' || parseInt(et.value) < 1) {
		base.alert('请选择模块所属的子系统！',function(){et.focus();});
		return false;
	}
	
	et = $('mName');
	if (et.value.strip() == '') {
		base.alert('请输入模块的名称！',function(){et.value == '';et.focus();});
		return false;
	}
	
	et = $('mSequence');
	if (et.value.strip() == '' || isNaN(et.value.strip()) || parseInt(et.value.strip()) <= 0) {
		base.alert('请输入模块的排序序号，并且排序序号只能是1到999之间的数字！',function(){et.focus();});
		return false;
	}
	
	return true;
}

function moduleCheckFuncModule()
{
	if (moduleCheckMenu() == false) return false;
	
	var et = $('mUrl');
	if (et.value.strip() == '') {
		base.alert('请输入功能模块的链接地址！',function(){et.focus();});
		return false;
	}
	
	return true;
}

//-------------------------菜单模块的打开和关闭操作--------------//
// 查询指定菜单模块下的所有直接模块
function moduleQueryChild(/*element*/img, /*string*/mToken, /*boolean*/pMenu)
{
	// 二级菜单的打开要查询数据库并且执行页面替换操作
	if (!pMenu) {
		moduleQueryByToken(mToken);
		return;
	}
	var _img = img.getAttribute("src").toLowerCase();
	var tmp = _img.split('/');
	var _imgName = tmp[tmp.length - 1];
	if (_imgName == 'menu_close.gif') {
		base.log('执行' + (pMenu ? '一' : '二') + '级菜单模块[' + mToken + ']的打开动作', 'info');
		tmp[tmp.length - 1] = 'menu_open.gif';
		img.setAttribute("src", tmp.join("/"));
		moduleShowHideChilds(mToken, true);
	} else {
		base.log('执行' + (pMenu ? '一' : '二') + '级菜单模块[' + mToken + ']的关闭动作', 'info');
		tmp[tmp.length - 1] = 'menu_close.gif';
		img.setAttribute("src", tmp.join("/"));
		moduleShowHideChilds(mToken, false);
	}
}

// 打开或关闭指定的菜单模块下的子模块
function moduleShowHideChilds(/*string*/pToken, /*boolean*/show)
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

// 查询指定模块下一二级模块
function moduleQueryByToken(/*string*/token)
{
	pag.query(
		'/syspurview/module.do?action=query',
		'module_list',
		'token=' + token + '&sysId=' + $('searchSysId').value
	);
}



// -------------------------模块功能的操作---------------------- //
// 功能模块的功能管理（添加/修改/删除）
function moduleFuncManage(mId)
{
	base.log('当前要管理的功能模块是：' + mId, 'info');
	base.request(
		'/syspurview/module.do?action=moduleFunc',
		function(xmlHttp, error) {
			var returnVal = xmlHttp.responseText;
			if (base.exe(returnVal)) {
				base.log('返回信息中含有脚本，执行成功！', 'info');
				return;
			}
			var obj = base.json(returnVal);
			// 不为空则表示发生了错误
			if (obj != null) {
				base.alert(obj.message);
				return;
			}
			$('module_update').innerHTML = returnVal;
			Element.hide('module_index');
			Element.show('module_update');
		},
		'mId=' + mId
	);
}

// 保存功能模块的单个功能 tbody表示当前功能所在的表格的tbody对象
function moduleSaveFunc(/*element*/tbody)
{
	var mId = $('func_mId').value;
	var func = moduleParseFuncArea(tbody);
	
	// 检查功能信息的完整性
	if (func.funcName.value.strip() == '') {
		base.alert('功能名称不能为空，请输入名称！',function(){func.funcName.focus();});
		return;
	} else if(func.funcOperate.value.strip() == '') {
		base.alert('功能操作标识符不能为空，请输入其操作标识符！',function(){func.funcOperate.focus();});
		return;
	} else if (moduleOperateIsRepeat(document.getElementsByName('funcOperate'))) {
		base.alert('该模块中似乎有重复的功能操作标识符？请再次确认',function(){func.funcOperate.focus();});
		return;
	}
	
	moduleFuncDisabledOperate(func, true);
	base.log('保存模块[' + mId + ']的功能[' + func.funcId.value + ']', 'info');
	
	base.request(
		'/syspurview/module.do?action=updateFunc&single=true',
		function(xmlHttp, error) {
			var returnVal = xmlHttp.responseText;
			if (base.exe(returnVal)) {
				base.log('返回信息中包含脚本，成功执行！', 'info');
				return;
			}
			moduleFuncDisabledOperate(func, false);
			var obj = base.json(returnVal);
			if (obj == null) { // 为空则表示发生错误了
				base.alert('系统发生异常：\n' + returnVal);
				return;
			}
			base.tips(obj.message, $('moduleFuncForm'));
			window.setTimeout(function() {
				base.tips('', $('moduleFuncForm'));
			}, (obj.success == true) ? 3000 : 8000);
			// 获取当前操作的功能唯一ID，添加时则是新返回，更新则是已存在的
			if (!obj.FuncId || obj.FuncId == 0) {
				base.alert('系统发生错误：无法获取当前更新的功能的唯一ID！');
			} else {
				base.log('系统返回当前更新的功能ID为：' + obj.FuncId, 'info');
				// 更新页面上表示当前功能ID的隐藏域input的value值
				if (func.funcId.value == '0') func.funcId.value = obj.FuncId;
			}
		},
		'MId=' + $('func_mId').value + '&MName=' + base.encode($('func_mName').value)
		+ moduleFuncParamStr(func)
	);
}

// 删除模块的单个功能 tbody表示当前功能所在的表格的tbody对象
function moduleDelFunc(/*element*/tbody)
{
	var mId = $('func_mId').value;
	var func = moduleParseFuncArea(tbody);
	// 只有已存在的功能才确认删除
	if (parseInt(func.funcId.value.strip()) <= 0) { 
		tbody.parentNode.parentNode.removeChild(tbody.parentNode);
		moduleJudgeHaveFuncs();
		return;
	}
	base.log('删除模块[' + mId + ']的功能[' + func.funcId.value + ']', 'info');
	
	base.confirm('您确信要删除该功能吗？', function() {
		// 如果该功能已存在于数据库中，则提交到服务器进行删除
		moduleFuncDisabledOperate(func, true);
		base.request(
			'/syspurview/module.do?action=removeFunc',
			function(xmlHttp, error) {
				var returnVal = xmlHttp.responseText;
				if (base.exe(returnVal)) {
					base.log('返回信息中包含脚本，成功执行！', 'info');
					return;
				}
				
				moduleFuncDisabledOperate(func, false);
				var obj = base.json(returnVal);
				if (obj == null) { // 为空则表示发生错误了
					base.alert('系统发生异常：\n' + returnVal);
					return;
				}
				base.tips(obj.message, $('moduleFuncForm'));
				window.setTimeout(function() {
					base.tips('', $('moduleFuncForm'));
				}, (obj.success == true) ? 3000 : 8000);
				// 如果删除成功了，则删除页面上的区域
				if (obj.success) {
					tbody.parentNode.parentNode.removeChild(tbody.parentNode);
					moduleJudgeHaveFuncs();
				}
			},
			'mId=' + mId + '&funcId=' + func.funcId.value
		);
	});
}

// 获取指定功能的参数化字符串
function moduleFuncParamStr(/*object*/func)
{
	return '&funcId=' + func.funcId.value + '&funcName=' + base.encode(func.funcName.value)
			+ '&funcOperate=' + base.encode(func.funcOperate.value) 
			+ '&relOperate=' + base.encode(func.relOperate.value);
}

// 保存单个功能时使其操作按钮不可用或可用
function moduleFuncDisabledOperate(/*object*/func, /*boolean*/dis)
{
	func.btnSave.disabled = dis;
	func.btnDel.disabled = dis;
}

// 判断该模块是否有功能，有则隐藏提示区域，否则显示提示区域
function moduleJudgeHaveFuncs()
{
	var haveFuncs = $('func_area').getElementsByTagName('TABLE').length >= 1;
	if (haveFuncs == true) Element.hide('func_no');
	else Element.show('func_no');
}

// 检查功能标识是否重复
function moduleOperateIsRepeat(/*element*/operateList)
{
	if (operateList.length <= 1) return false;
	for (var i = 0; i < operateList.length; i ++) {
		for (var j = i + 1; j < operateList.length; j ++) {
			if (operateList[i].value.strip() == operateList[j].value.strip()) return true;
		}
	}
	return false;
}

// 解析当前操作的功能区域(每个功能区域对应一个table)，获取功能ID、保存和删除按钮和输入元素等
function moduleParseFuncArea(/*element*/tbody)
{
	var inputs = tbody.getElementsByTagName('INPUT');
	var obj = new Object();
	for (var i = 0; i < inputs.length; i ++) {
		var n = inputs[i].name;
		if (!n) continue;
		if (n == 'funcId') obj.funcId = inputs[i];
		else if (n == 'funcName') obj.funcName = inputs[i];
		else if (n == 'funcOperate') obj.funcOperate = inputs[i];
		else if (n == 'relOperate') obj.relOperate = inputs[i];
		else if (n == 'btnSave') obj.btnSave = inputs[i];
		else if (n == 'btnDel') obj.btnDel = inputs[i];
	}
	return obj;
}

// 在页面上动态的添加一个功能输入区域
function moduleAddFuncArea()
{
	base.log('添加一个功能输入区~~~', 'info');
	//new Insertion.Bottom('func_area', $('func_tpl').innerHTML);
	
	// 获取模板(要添加的功能)
	var mfunc_tpl = $('mfunc_tpl_span').getElementsByTagName('INPUT');
	base.log('总的input元素为：' + mfunc_tpl.length, 'info');
	var result = [];
	for (var i = 0; i < mfunc_tpl.length; i ++) {
		if (mfunc_tpl[i].type != 'checkbox') continue;
		if (!mfunc_tpl[i].checked) continue;
		if (!mfunc_tpl[i].name || mfunc_tpl[i].name != 'mfunc_tpl') continue;
		result.push(mfunc_tpl[i].value);
	}
	if (result.length == 0) result.push('new');
	base.log('要添加的功能有：' + result.join(','));
	
	// 获取功能输入区域的功能名称、功能操作标识符和关联操作元素
	var tpl = $('func_tpl').cloneNode(true);
	var opt = new Object();
	var tmp = tpl.getElementsByTagName('INPUT');
	for (var i = 0; i < tmp.length; i ++) {
		if (tmp[i].type != 'text') continue;
		if (!tmp[i].name) continue;
		if (tmp[i].name == 'funcName') opt.funcName = tmp[i];
		if (tmp[i].name == 'funcOperate') opt.funcOperate = tmp[i];
		if (tmp[i].name == 'relOperate') opt.relOperate = tmp[i];
	}
	
	// 添加功能
	for (var i = 0; i < result.length; i ++) {
		var insrt = false;
		if (result[i] == 'new') {
			opt.funcName.value = opt.funcOperate.value = opt.relOperate.value = '';
			insrt = true;
		} else if (result[i] == 'query') {
			opt.funcName.value = '查询';
			opt.funcOperate.value = 'list';
			opt.relOperate.value = 'query';
			insrt = true;
		} else if (result[i] == 'add') {
			opt.funcName.value = '添加';
			opt.funcOperate.value = 'save';
			opt.relOperate.value = 'create';
			insrt = true;
		} else if (result[i] == 'update') {
			opt.funcName.value = '修改';
			opt.funcOperate.value = 'update';
			opt.relOperate.value = 'edit';
			insrt = true;
		} else if (result[i] == 'delete') {
			opt.funcName.value = '删除';
			opt.funcOperate.value = 'remove';
			opt.relOperate.value = '';
			insrt = true;
		}
		if (insrt) new Insertion.Bottom('func_area', tpl.innerHTML);
	}
	
	moduleJudgeHaveFuncs();
}

// 功能模板的全选或反选
function moduleFuncTplCheck(/*boolean*/slt)
{
	base.log('执行全部功能模板为：' + (slt ? '' : '不') + '选中！', 'info');
	var mfunc_tpl = $('mfunc_tpl_span').getElementsByTagName('INPUT');
	for (var i = 0; i < mfunc_tpl.length; i ++) {
		if (mfunc_tpl[i].type != 'checkbox') continue;
		if (!mfunc_tpl[i].name || mfunc_tpl[i].name != 'mfunc_tpl') continue;
		mfunc_tpl[i].checked = slt;
	}
}

// 模块的功能表单的整体提交检查
function moduleFuncSubmit()
{
	var func = moduleParseFuncForm();
	var fName = func.funcName;
	var fOperate = func.funcFlag;
	var fRelOperate = func.funcRelFlag;
	if (fName.length < 1) {
		base.alert('当前模块暂时还没有任何功能，请先添加功能后再提交！');
		return false;
	}
	
	for (var i = 0; i < fName.length; i ++) {
		if (fName[i].value.strip() == '') {
			base.alert('功能名称不能为空，请输入名称！',function(){fName[i].focus();});
			return false;
		} else if (fOperate[i].value.strip() == '') {
			base.alert('功能操作标识符不能为空，请输入其操作标识符！',function(){fOperate[i].focus();});
			return false;
		} else if (moduleOperateIsRepeat(fOperate)) {
			base.alert('该模块中似乎有重复的功能操作标识符？请再次确认',function(){fOperate[i].focus();});
			return false;
		}
	}
	
	moduleDisabledAllBtn(true);
	
	// 表单提交
	base.formSubmit(
		'/syspurview/module.do?action=updateFunc',
		function(xmlHttp, error) {
			var returnVal = xmlHttp.responseText;
			if (base.exe(returnVal)) {
				base.log('返回信息中包含脚本，成功执行！', 'info');
				return;
			}
			moduleDisabledAllBtn(false);
			var obj = base.json(returnVal);
			if (obj == null) { // 为空则表示发生错误了
				base.alert('系统发生异常：\n' + returnVal);
				return;
			}
			base.tips(obj.message, $('moduleFuncForm'));
			if (obj.success) {
				base.tips('3秒钟后将会返回到列表页面！', $('moduleFuncForm'), true);
			}
			window.setTimeout(function() {
				base.tips('', $('moduleFuncForm'));
				if (obj.success) pag.back('module_index','module_update');
			}, 3000);
		},
		'moduleFuncForm',
		true
	);
	return false;
}

// 获取功能表单区域内的所有输入元素
function moduleParseFuncForm()
{
	var inputs = $('moduleFuncForm').getElementsByTagName('INPUT');
	var fName = [], fOperate = [], fRelOperate = [];
	for (var i = 0; i < inputs.length; i ++) {
		var n = inputs[i].name;
		if (!n) continue;
		if (n == 'funcName') fName[fName.length] = inputs[i];
		else if (n == 'funcOperate') fOperate[fOperate.length] = inputs[i];
		else if (n == 'relOperate') fRelOperate[fRelOperate.length] = inputs[i];
	}
	return {'funcName' : fName, 'funcFlag' : fOperate, 'funcRelFlag' : fRelOperate};
}

// 提交表单时使所有的按钮不可用
function moduleDisabledAllBtn(/*boolean*/dis)
{
	var inputs = $('moduleFuncForm').getElementsByTagName('INPUT');
	for (var i = 0; i < inputs.length; i ++) {
		if (inputs[i].type != 'button' && inputs[i].type != 'submit') continue;
		inputs[i].disabled = dis;
	}
}

// -------------------------模块功能的操作---------------------- //
