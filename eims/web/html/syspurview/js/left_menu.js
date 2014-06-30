/**
 * 左边的菜单管理JS
 */
 
// 获取左边的菜单列表
function leftGetMenuList(/*string*/sysId, /*string*/pToken)
{
	window.location.href = '/syspurview/admin.do?action=leftMenu&sysId=' 
		+ sysId + '&token=' + (pToken ? pToken : '');
}

// 子模块的显示或获取
function leftQueryModuleChild(/*string*/sysId, /*object*/span, /*string*/mToken, /*boolean*/pMenu)
{
	// 二级菜单的打开要查询数据库并且执行页面替换操作
	if (!pMenu) {
		leftGetMenuList(sysId, mToken);
		return;
	}
	var img = span.getElementsByTagName('IMG')[0]; //span下的img对象
	var _img = img.getAttribute("src").toLowerCase();
	var tmp = _img.split('/');
	var _imgName = tmp[tmp.length - 1];
	if (_imgName == 'menu_close.gif') {
		tmp[tmp.length - 1] = 'menu_open.gif';
		img.setAttribute("src", tmp.join("/"));
		leftShowHideChilds(mToken, true);
	} else {
		tmp[tmp.length - 1] = 'menu_close.gif';
		img.setAttribute("src", tmp.join("/"));
		leftShowHideChilds(mToken, false);
	}
}

// 打开或关闭指定的菜单模块下的子模块
function leftShowHideChilds(/*string*/pToken, /*boolean*/show)
{
	var trs = $('menu_list').getElementsByTagName('TR');
	for (var i = 0; i < trs.length; i ++) {
		if (!trs[i].name) continue;
		if (trs[i].name == pToken) continue;
		if (trs[i].name.indexOf(pToken) != 0) continue;
		if (show) Element.show(trs[i]);
		else Element.hide(trs[i]);
	}
}