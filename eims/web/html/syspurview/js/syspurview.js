/**
 * 后台首页多窗口管理JS类
 */


// 全局的窗体配置对象
function WindowConfiger() {};
WindowConfiger.prototype = {
	x : 225, // 窗体打开的初始x位置
	y : 160, // 窗体打开的初始y位置
	w : 900, // 窗体打开的宽度
	h : 600, // 窗体打开的高度
	max : 10, // 最多能打开的窗体数目
	winSkin : 'blue', // 窗体的默认皮肤名称,用户个人配置
	menuSkin : 'zero', // 左边菜单窗体的皮肤
	taskbarSkin : 'zero', // 窗口任务列表的窗体皮肤
	taskbarWinMaxWidth : 70, // 任务栏列表中每一个窗体的最大宽度
	leftWinName : 'leftMenuWin', // 左边菜单窗口的名称
	controlWinName : '00', // 控制面板的窗口名称
	
	/**
	 * 获取权限系统的根路径
	 */
	getBaseDir : function() {
		return $('sysBaseDir').value.strip();
	}
};

// 窗体管理对象
function WinManager() {};
WinManager.winNameList = []; // 所有打开的窗口的名称列表
WinManager.coveredOldWin = false; // 是否覆盖最不常用的窗口，默认是false
WinManager.prototype = {
	_winName : 'onlyOneWinName', // 只有一个窗口时窗口名称，固定！
	
	/**
	 * 打开一个新窗口.总是创建一个新的窗口，如果指定的已经被打开了，则直接使其居最前即可
	 */
	open : function(/*string*/_url, /*string*/_id, /*string*/title) {
		// 如果设置的窗口数小于2则直接保存一个即可
		if (cfg.max < 2) return this.openOneWindow(_url, title);
		
		// 否则进行多个窗口的打开
		base.log('打开窗口[url=' + _url + ',name=' + _id + '],当前已打开窗口总数为：' + WinLIKE.Actual, 'info');
		var _num = WinLIKE.searchwindow(_id);
		base.log('当前打开窗口：' + _num, 'info');
		if (!_num) {
			base.log('指定名称的窗口不存在，新建立一个窗体！');
			return this.createWin(_id, title, _url);
		} else {
			base.log('该窗体已经存在，直接居前显示！', 'info');
			var _win = WinLIKE.windows[_num];
			//_win.front();
			//winListBringToFront(_num);
			if (!_win) return null;
			_win.isMinToBar == true ? winListMinToShow(_num) : winListShowToMin(_num);
			return _win;
		}
	},
	
	/**
	 * 只允许打开一个窗口的调用方法.
	 */
	openOneWindow : function(/*string*/_url, /*string*/title) {
		var num = WinLIKE.searchwindow(this._winName);
		if (!num) return this.createWin(this._winName, title, _url);
		var win = WinLIKE.windows[num];
		win.Ttl = title;
		win.draw();
		var _link = WinLIKE.setlink(_url, num, false);
		WinLIKE.openaddress(_link, null, this._winName);
	},
	
	/**
	 * 使用指定的属性创建一个新的窗体
	 */
	createWin : function(/*string*/_id, /*string*/title, /*string*/_url) {
		if (WinManager.winNameList.length >= cfg.max) {
			if (WinManager.coveredOldWin == true) {
				winCloseLessUsedWin();
			} else {
				if (confirm('系统最多允许同时打开' + cfg.max + '个窗口！\n' + '如果要打开新窗口，系统将自动关闭最少使用的窗口，您确信要继续吗？')) {
					WinManager.coveredOldWin = true;
					winCloseLessUsedWin();
				} else {
					return null;
				}
			}
		}
		var win = new WinLIKE.window(title, cfg.x, cfg.y, cfg.w, cfg.h, 1000);
		win.Ski = cfg.winSkin;
		win.Nam = _id;
		win.Adr = _url;
		win.Rel = true;
		win.LD = false;
		win.Mx = true; // 最大化
		win.Min = false; // 最小化图标不可用
		win.Siz = false;
		win.Mov = false;
		win.HD = false; // 前进后退操作不可用
		WinLIKE.createwindow(win);
		WinManager.winNameList.push(_id); // 保存打开的窗口的名称
		_winStack.push(WinLIKE.searchwindow(_id)); // 当前活动的窗口堆栈
		//adminDebugWin(win);
		
		return win;
	},
	
	/**
	 * 获取指定名称的窗口编号，没有则返回null
	 */
	getWinNum : function(/*string*/winName) {
		var num = WinLIKE.searchwindow(winName);
		return !num ? null : num;
	}
};

// 关闭最不常用的一个窗口
function winCloseLessUsedWin()
{
	if (!_winStack || _winStack.length < 1) return false;
	var win = WinLIKE.windows[_winStack[0]];
	if (!win) return false;
	win.close();
	return true;
}

// 关闭指定编号的窗口
function winCloseByNum(/*int*/num)
{
	var win = WinLIKE.windows[num];
	if (!win) return;
	win.close();
}

// 关闭指定名称的窗口
function winCloseByName(/*string*/winName)
{
	var num = WinLIKE.searchwindow(winName);
	if (!num) return;
	var win = WinLIKE.windows[num];
	if (!win) return;
	win.close();
}

// 最小化所有的已打开窗口
function winMinAllWin()
{
	for (var i = 0; i < WinManager.winNameList.length; i ++) {
		var _num = WinLIKE.searchwindow(WinManager.winNameList[i]);
		if (!_num) continue;
		var _win = WinLIKE.windows[_num];
		if (!_win) continue;
		if (_win.isMinToBar == true) continue;
		_win.isMinToBar = true;
		_win.hideshow();
	}
}

// 后台管理页面的窗口的事件处理方法
//var tmp = new Object();
function winEventMag(win, what)
{
	//if (!tmp[what]) {
	//	base.alert(win.Nam + '\n' + what);
	//	tmp[what] = true;
	//}
	if (typeof(win.isMinToBar) == 'undefined' || win.isMinToBar == null) win.isMinToBar = false;
	// 窗体的最小化和还原 true-已最小化 false-没有最小化
	if (what == 1) {
		base.log('窗体' + win.Nam + (win.isMinToBar ? '非' : '') + '最小化到任务栏！', 'info');
		win.isMinToBar = !win.isMinToBar;
		win.hideshow();
		return false;
	}
	// 窗口的关闭操作
	if (what == 3) {
		base.log('关闭窗口' + win.Nam + '!', 'warn');
		var _num = WinLIKE.searchwindow(win.Nam);
		if (_num) {
			// 从窗口堆栈中移出该窗口的编号
			_winStack.pop();
			
			WinLIKE.windows[_num] = null;
			// 移除被关闭的窗口名称
			var names = [];
			for (var i = 0; i < WinManager.winNameList.length; i ++) {
				if (WinManager.winNameList[i] == win.Nam) continue;
				names.push(WinManager.winNameList[i]);
			}
			WinManager.winNameList = names;
		}
	}
};
	
// 窗口列表中每一个窗口显示的样式和内容
var _winStack = new Array(); // 当前的窗口序号堆栈，用来确定哪个窗口是处于激活状态的（最上面一个就是）
WinLIKE.winlist.getitem = function(num, active, title, name, skin) {
	// 左边菜单窗口和任务栏不显示在列表中
	if (name == "winlist" || name == "leftMenuWin") return "";
	if (!active) return "";
	
	// 获取每一个窗口应该占的宽度
	//var w = winListGetItemWidth();
	
	// 根据标题的数目来计算宽度
	var imgWidth = 16;
	var w1 = title.strip().length * 12 /*标题的宽度*/ + 8 /*空白*/ + imgWidth /*关闭图片的宽度*/; // 活动窗口的宽度
	var w2 = w1 - imgWidth /*关闭图片的宽度*/; // 非活动窗口的宽度
	
	// 如果当前窗口已经是最小化了，那么非突出显示；如果当前窗口是已经被激活的窗口，
	// 那么突出显示，否则非突出显示。
	if (WinLIKE.windows[num].isMinToBar == true) {
		// 已经是最小化了的窗口，设置点击最大化并居前显示的操作
		return winGetTaskbarPassiveWinHtml({title : title, click : 'parent.winListMinToShow(' + num + ');', width : w2});
	} else {
		// 如果当前窗口已被激活，则突出显示
		if (num == _winStack[_winStack.length - 1]) {
			return winGetTaskbarActiveWinHtml({title : title, click : 'parent.winListShowToMin(' + num + ');', width : w1, winNum : num});
		}
		// 非激活状态，非突出显示
		return winGetTaskbarPassiveWinHtml({title : title, click : 'parent.winListShowToMin(' + num + ');', width : w2});
	}
};


// 获取活动窗口元素的html代码(使用类似tab标签项时)
function winGetTaskbarActiveWinHtml(obj)
{
	var close_normal = cfg.getBaseDir() + '/css/taskbar/images/close_normal.gif';
	var close_focus = cfg.getBaseDir() + '/css/taskbar/images/close_focus.gif';
	return '<SPAN class="TabItemSelectedLeft"></SPAN>'
		 + '<SPAN class="TabItemSelectedMiddle" title="' + obj.title + '">'
		 + '<SPAN class="TabItemTextSelected" onclick="' + obj.click + '" style="width:' + obj.width + 'px;">'
		 + obj.title
		 //+ '&nbsp;&nbsp;&nbsp;<IMG SRC="' + close_normal + '" WIDTH="' + obj.imgWidth + 'px" BORDER="0" ALIGN="middle" '
		 //+ 'onMouseOver="this.src=\'' + close_focus + '\';" onMouseOut="this.src=\'' + close_normal + '\';" />'
		 + '&nbsp;&nbsp;&nbsp;<FONT title="关闭选项卡" onclick="parent.winCloseByNum(' + obj.winNum + ')" '
		 + 'class="TabItemCloseNormal" onMouseOver="this.className=\'TabItemCloseFocus\';" '
		 + 'onMouseOut="this.className=\'TabItemCloseNormal\';">'
		 + '<B>&nbsp;X&nbsp;</B></FONT>'
		 + '</SPAN>'
		 + '</SPAN>'
		 + '<SPAN class="TabItemSelectedRight"></SPAN>';
}


// 获取非活动窗口元素的html代码(使用类似tab标签项时)
function winGetTaskbarPassiveWinHtml(obj)
{
	return '<SPAN class="TabItemLeft"></SPAN>'
		 + '<SPAN class="TabItemMiddle" title="' + obj.title + '">'
		 + '<SPAN class="TabItemText" onclick="' + obj.click + '" style="width:' + obj.width + 'px;" '
		 + 'onMouseOver="this.className=\'TabItemText_OnMouseOver\';" onMouseOut="this.className=\'TabItemText\';">'
		 + obj.title + '</SPAN>'
		 + '</SPAN>'
		 + '<SPAN class="TabItemRight"></SPAN>';
}



//根据当前已有的窗口数和任务栏的宽度来计算任务栏列表中每一个窗体的宽度
function winListGetItemWidth()
{
	var nums = WinLIKE.Actual - 2; // 总的窗体数，应该减去左边的菜单窗体和任务栏窗体
	var w = taskbarWin.Width; // 任务栏的宽度
	var eveWidth = w / nums; // 每个窗口的宽度
	// 任务栏中的窗体有一个最大宽度值，如果超过了该值则限制其宽度
	if (eveWidth >= cfg.taskbarWinMaxWidth) eveWidth = cfg.taskbarWinMaxWidth;
	
	base.log('当前活动的窗体数为：' + nums + ',任务栏宽度为：' + w 
				+ ',每个窗口宽度为：' + eveWidth, 'info');
				
	return eveWidth;
}

// 窗口列表中窗口在最小化时的单击操作
function winListMinToShow(/*int*/num)
{
	base.log('使窗口[' + num + ']最大化！', 'info');
	var win = WinLIKE.windows[num];
	_winStack.push(num);
	win.minmax();
	win.front();
	win.isMinToBar = false;
}

// 窗口列表中窗口在显示的时候的单击操作，此操作有两种，一是当点击的窗口不是在最前的时候
// 系统将使其居最前，如果是最前，则系统使其最小化
function winListShowToMin(/*int*/num)
{
	var win = WinLIKE.windows[num];
	// 如果只有一个窗口或其已经是最居前了则直接最小化
	if (WinLIKE.Actual == num || win.Bac) {
		base.log('最小化窗口[' + num + ']到任务栏！', 'info');
		_winStack.pop();
		win.minmax();
		win.isMinToBar = true;
		
	} else {
		winListBringToFront(num);
	}
}

// 使已经存在的窗口居前操作
function winListBringToFront(/*int*/num)
{
	base.log('使窗口[' + num + ']居最前！', 'info');
	var tmp = new Array();
	for (var i = 0; i < _winStack.length; i ++) {
		if (_winStack[i] != num) tmp[tmp.length] = _winStack[i];
	}
	_winStack = null;
	_winStack = tmp;
	_winStack.push(num);
	var win = WinLIKE.windows[num];
	win.minmax();
	win.minmax();
	win.front();
}

// 显示某一个窗口的详细属性，调试方法
function adminDebugWin(/*object*/win)
{
	var txt = '';
	for (var prop in win) txt += prop + "=" + win[prop] + '\n';
	alert(txt);
}

// 系统退出
function adminExit()
{
	if (!confirm('您确信要退出系统？')) return;
	parent.parent.window.location.href = "/syspurview/login.do?action=loginout";
}

// 控制面板
function adminControlPanel(winName)
{
	var win = winMag.open('/syspurview/controlpanel.do', cfg.controlWinName, winName);
}