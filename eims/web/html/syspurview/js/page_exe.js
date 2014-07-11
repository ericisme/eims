/**
 * 页面操作执行的公共JS类
 */

function PageExe(){};
PageExe.prototype = {
	/**
	 * 页面列表查询操作
	 */
	query : function(/*string*/_url, /*string or element object*/et, /*object*/param) {
		base.log('列表查询操作!URL=[' + _url + ']', 'info');
		_url = this._appendParam(_url, param);
		base.updater($(et), _url);
	},
	
	/**
	 * 显示添加页面
	 */
	showAdd : function(/*string*/_url, /*string*/show, /*string*/hide, /*function*/callback) {
		base.log('添加页面显示！URL=[' + _url + ']', 'info');
		base.request(_url, function(xmlHttp, error) {
			var returnVal = xmlHttp.responseText;
			if (base.exe(returnVal)) {
				base.log('返回信息中含有脚本，执行成功！', 'info');
				return;
			}
			$(show).innerHTML = returnVal;
			Element.hide(hide);
			Element.show(show);
			if (typeof(callback) == 'function') callback(xmlHttp);
		});
	},
	
	/**
	 * 显示编辑页面
	 */
	showEdit : function(/*string*/_url, /*string*/show, /*string*/hide, /*object*/param, /*function*/callback) {
		base.log('编辑页面显示！URL=[' + _url + ']', 'info');
		var _param = param ? $H(param).toQueryString() : null;
		base.log('请求的参数为[' + _param, 'info');
		_url = this._appendParam(_url, param);
		base.request(_url, function(xmlHttp, error) {
			var returnVal = xmlHttp.responseText;
			if (base.exe(returnVal)) {
				base.log('返回信息中含有脚本，执行成功！', 'info');
				return;
			}
			var obj = base.json(returnVal);
			if (obj != null && obj.success == false) {
				base.alert(obj.message);
			} else {
				$(show).innerHTML = returnVal;
				Element.hide(hide);
				Element.show(show);
				if (typeof(callback) == 'function') callback(xmlHttp);
			}
		}, _param);
		
	},
	
	/**
	 * 列表页面的删除操作
	 */
	del : function(/*string*/_url, /*string*/tips, /*string*/param, /*function*/onSuccess) {
		base.log('列表页面删除操作！URL=[' + _url + ']', 'info');
		base.log('请求的参数为[' + param, 'info');
		base.request(_url, function(xmlHttp, error) {
			var returnVal = xmlHttp.responseText;
			base.log('[回调函数]删除操作请求执行成功!系统返回：' + returnVal);
			
			// 执行返回中的js脚本,避免超时而导致JS错误
			if (base.exe(returnVal)) {
				base.log('返回信息中含有脚本，执行成功！', 'info');
				return;
			}
			
			var obj = base.json(returnVal);
			if (obj == null) {
				base.alert('系统发生异常：\n' + returnVal);
				return;
			}
			base.tips(obj.message);
			window.setTimeout(function() {
				base.tips();
			}, (obj.success == true) ? 3000 : 8000);
			
			// 不允许进行删除的实体进行显示（比如角色因为含有用户不允许被删除！）
			var delMsg = PageExe.delFailedMsg(obj, tips);
			base.log('检查是否有不允许进行删除的，返回信息：' + delMsg);
			if (delMsg != null) base.alert(delMsg);
			// 只有成功执行了删除并且指定进行刷新时才进行页面的刷新操作
			base.log(obj.success && obj.refresh);
			if (obj.success && obj.refresh) onSuccess();
		}, param);
	},
	
	/**
	 * 表单提交添加或更新操作
	 */
	update : function(/*string*/_url, /*string*/formId, /*function*/onSuccess, /*int*/times) {
		base.log('执行表单提交操作！URL=[' + _url + ']', 'info');
		var btn = PageExe.submitButton(formId); // 获取表单区域的提交按钮
		if (btn) btn.disabled = true;
		
		base.formSubmit(_url, function(xmlHttp, error) {
			var returnVal = xmlHttp.responseText;
			base.log('[回调函数]表单提交操作成功执行！系统返回：' + returnVal);
			if (base.exe(returnVal)) {
				base.log('返回信息中含有脚本，执行成功！', 'info');
				return;
			}
			
			var obj = base.json(returnVal);
			if (obj == null) {
				base.alert('系统发生异常2：\n' + returnVal);
				return;
			}
			base.tips(obj.message, $(formId));
			if (obj.success == true) {
				// 如果没有指定时间，则默认是3秒后执行回调方法，同时显示提示消息
				if (!times) {
					base.tips('3秒钟后将自动返回到列表页面！', $(formId), true);
					window.setTimeout(function() {
						onSuccess();
					}, 3000);
				} else {
					// 如果指定了时间，则在指定的时间后开始执行回调，信息提示需要自己实现
					if (times <= 0) onSuccess();
					else window.setTimeout(onSuccess, times);
				}
			} else {
				if (btn) btn.disabled = false; 
			}
		}, formId);
	},
	
	/**
	 * 添加和编辑页面的返回操作
	 */
	back : function(/*string*/show, /*string*/hide) {
		base.log('执行回退操作！', 'info');
		Element.hide(hide);
		Element.show(show);
	},
	
	/**
	 * 将参数附加到指定的URL地址后的辅助方法
	 */
	_appendParam : function(/*string*/_url, /*object*/param) {
		if (!param) return _url;
		var _p = '';
		if (typeof(param) == 'string') _p = param;
		else _p = $H(param).toQueryString();
		base.log('请求参数为：[' + _p + ']', 'info');
		_url += _url.indexOf('?') == -1 ? '?' : '&';
		_url += _p;
		return _url;
	},
	
	/**
	 * 发送请求，返回json对象.同时对登陆与异常进行过滤和判断
	 */
	jsonReq : function(/*string*/_url, /*function*/onSuccess, /*string*/param) {
	 	base.request(
			_url,
			function(xmlHttp,error) {
				var returnVal = xmlHttp.responseText;
				
				//base.alert(returnVal);
				
				if (base.exe(returnVal)) {
					base.log('返回信息中含有脚本，执行成功！', 'info');
					return;
				}
				var obj = base.json(returnVal);
				if (obj == null) {
					base.alert('系统发生异常：\n' + returnVal);
					return;
				}
				onSuccess(obj);
			},
			param
		);
	}
};

/**
 * PageExe的静态方法.删除检测没有被成功删除时的提示信息
 */
PageExe.delFailedMsg = function(/*object*/obj, /*string*/tips) {
	if (!obj.NoDel) return null; // 不存在则直接返回null
	var noDel = obj.NoDel.split(','), noDelNames = tips + '：\n[\n', haveNoDel = false;
	for (var i = 0; i < noDel.length; i ++) {
		var span = $('dmspan_' + noDel[i]);
		if (!span) continue;
		haveNoDel = true;
		noDelNames += '      ' + span.innerHTML + '\n';
	}
	return haveNoDel ? (noDelNames + ']') : null;
};

/**
 * 获取指定表单区域中的提交按钮对象
 */
PageExe.submitButton = function(/*string*/formId) {
	var form = $(formId);
	if (!form) return null;
	var inputs = form.getElementsByTagName('INPUT');
	for (var i = 0; i < inputs.length; i ++) {
		if (inputs[i].getAttribute('type') == 'submit') return inputs[i];
	}
	return null;
};