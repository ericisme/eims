
/**
 * JS类库，简化客户端的JS编程量。需要prototype.js的支持！
 * @author 马必强
 * @version 0.1
 */


/**-----------------------------------------------------------------------
 * ------------------------基础类对象，主要是进行AJAX操作-----------------
 * -----------------------------------------------------------------------
 */
function BaseClass(){};
/**
 * BaseClass的静态方法，添加页面加载执行函数
 */
BaseClass.funcList = [];
BaseClass.addOnLoad = function(/*function*/func) {
	BaseClass.funcList.push(func);
};

/**
 * 一次提供多个JS同时加载的方法.加载的格式为：load(JS名称,JS名称)
 */
BaseClass.load = function() {
	var p = /base\.js\?load\((.+)\)/
	var scripts = document.getElementsByTagName("script");
	for (var i = 0; i < scripts.length; i ++) {
		if (!scripts[i].src) continue;
		if (!p.test(scripts[i].src)) continue;
		BaseClass.require(scripts[i].src, scripts[i].src.match(p));
	}
};
BaseClass.require = function(/*string*/jsPath, /*array*/jsLib) {
	if (!jsLib || !jsLib[1]) return;
	var path = jsPath.substring(0, jsPath.indexOf("base.js?load("));
	var libs = jsLib[1].split(",");
	for (var i = 0; i < libs.length; i ++) {
		var tmp = libs[i].replace(/(^\s*)|(\s*$)/g, "");
		if (tmp == "") continue;
		if (tmp.charAt(0) != '/') tmp = path + tmp;
		// alert(tmp);
		document.write('<script type="text/javascript" src="' + tmp + '"></script>');
	}
};
BaseClass.load(); // 自动运行

/**
 * BaseClass原型方法，必须new之后才能使用
 */
BaseClass.prototype = {
	baseLogger : null,  // 调试对象
	_debug : false,      // 是否调试，默认是
	_msgboxError : false, // 信息提示框是否错误，如果错误则直接使用默认的alert
	
	/**
	 * 调试方法
	 */
	log : function(/*string*/ message, /*string*/level) {
		if (!this._debug) return;
		try {
			if (this.baseLogger == null) {
				this.baseLogger = log4javascript.getLogger('logger by mabiqiang!');
				var appender = new log4javascript.PopUpAppender();
				appender.setNewestMessageAtTop(true);
				this.baseLogger.addAppender(appender);
			}
			switch (level) {
				case 'trace' : this.baseLogger.trace(message);break;
				case 'debug' : this.baseLogger.debug(message);break;
				case 'info' : this.baseLogger.info(message);break;
				case 'warn' : this.baseLogger.warn(message);break;
				case 'error' : this.baseLogger.error(message);break;
				case 'fatal' : this.baseLogger.fatal(message);break;
				default : this.baseLogger.debug(message);break;
			}
		} catch (ex) {
			this._debug = false; //  发生异常时就停止调试
		}
	},
	debug : function(/*string*/message) {
		this.log(message, 'debug');
	},
	info : function(/*string*/message) {
		this.log(message, 'info');
	},
	warn : function(/*string*/message) {
		this.log(message, 'warn');
	},
	error : function(/*string*/message) {
		this.log(message, 'error');
	},
	
	/**
	 * 操作成功或失败的提示信息类.containerId为包含common_tips的页面元素，该方法自动滚动到提示元素！
	 */
	
	tips : function (/*string*/message, /*element*/containerId, /*boolean*/append, /*boolean*/twink) {
		this.stip({
			message : message,
			containerId : containerId,
			append : append == true,
			twink : twink == true || typeof(twink) == 'undefined',
			scroll : true
		});
	},
	
	/**
	 * 操作成功或失败的提示信息类.
	 */
	stip : function (/*object*/config) {
		var tip = null;
		if (config.containerId && typeof(config.containerId) == 'object') {
			var divs = config.containerId.getElementsByTagName('DIV');
			for (var i = 0; i < divs.length; divs ++) {
				if (divs[i] && divs[i].id == 'common_tips') {
					tip = divs[i];break;
				}
			}
		} else {
			tip = $('common_tips');
		}
		if (!tip) return;
		if (!config.message || typeof(config.message) != 'string' || config.message.strip() == '') {
			Element.hide(tip);
		} else {
			tip.innerHTML = (config.append == true) ? (tip.innerHTML + config.message) : config.message;
			Element.show(tip);
			if (config.scroll == true) Element.scrollTo(document.body);
			if (config.twink == true) {
				// 提示闪烁效果
				new HighLight().exe({
					id : tip,
					className : 'highlight',
					times : 2000
				});
			}
		}
	},

	/**
	 * 将服务器端返回的JSON对象转换成js中的对象
	 */
	json : function(/*string*/jsonString) {
		try {
			return eval('(' + jsonString + ')');
		} catch (ex) {
			return null;
		}
	},
	
	/**
	 * 执行指定的文本中的所有脚本,执行成功返回true，否则返回false
	 */
	exe : function(/*string*/str) {
		if (!str || typeof(str) != 'string') return false;
		try {
			str.evalScripts();
			if (str.extractScripts().length > 0) return true;
			return false;
		} catch (ex) {}
	},

	/**
	 * 全局的提示类.需要prototype.js的支持
	 */
	registry : function () {
		var loadingHandler = {
			onCreate : function() {
				Element.show('common_loading');
			},
			onComplete : function() {
				if (Ajax.activeRequestCount == 0) {
					Element.hide('common_loading');
				}
			}
		}
		Ajax.Responders.register(loadingHandler);
	},

	/**
	 * 动态更新指定区域的实用方法.需要prototype.js的支持
	 */
	updater : function(/*string*/_id, /*string*/_url, /*string*/_method) {
		new Ajax.Updater(
			_id,
			_url,
			{
				method : !_method ? 'POST' : _method,
				evalScripts : true
			}
		);
	},

	/**
	 * 向服务器发送请求，并指定处理函数
	 */
	request : function(/*string*/_url, /*function*/onSuccess, /*string*/paras, /*string*/_method) {
		new Ajax.Request(
			_url,
			{
				method : !_method ? 'POST' : _method,
				evalScripts : true,
				parameters : !paras ? '' : paras,
				onComplete : onSuccess
			}
		);
	},

	/**
	 * 使用form进行表单提交.
	 * 参数arrayToJSON表示是否将同名称的多个参数转换成JSON对象传送到服务器.
	 * 比如request中有一个参数name有两个值1和2，如果使用JSON，那么传送到服务器端的应该是：
	 * {"name":["1","2"]}
	 * 那么就需要使用JSONLIB进行解析了，已提供该类的简易使用方法
	 */
	formSubmit : function(/*string*/_url, /*function*/onSuccess, /*string*/_formId, /*boolean*/arrayToJSON)
	{
		var paras = this._formValues($(_formId));
		if (paras == null) return;
		if (arrayToJSON == true) paras = '&json=' + this.encode(this.toJSON(paras), true);
		this.request(_url, onSuccess, paras, 'POST');
	},
	
	/**
	 * 将指定的查询字符串转换成JSON字符串.注意参数的值中的"将全部被替换成'
	 */
	toJSON : function(/*string*/queryStr) {
		if (!queryStr || typeof(queryStr) != 'string') return "{}";
		var match = queryStr.strip().match(/([^?#]*)(#.*)?$/);
    	if (!match) return "{}";
    	var param = match[1].split('&'), obj = new Object();
    	for (var i = 0; i < param.length; i ++) {
    		var pair = param[i].split('=');
    		if (!pair[0]) continue;
    		if (!obj[pair[0]]) obj[pair[0]] = [];
    		var value = obj[pair[0]];
    		if (!pair[1]) value[value.length] = '';
    		else value[value.length] = this.decode(pair[1]).gsub(/"/,"'");
    	}
		var result = '{';
		for (var property in obj) {
			result += property + ":[";
			var value = obj[property];
			for (var i = 0; i< value.length; i ++) {
				result += '"' + value[i] + '"';
				if (i != value.length - 1) result += ',';
			}
			result += "],";
		}
		if (result.charAt(result.length - 1) == ',') result = result.substring(0, result.length - 1);
		return result + '}';
	},

	/**
	 * 将指定form节点的所有可提交元素组合成字符串返回
	 */
	_formValues : function(/*object*/formNode)
	{
		if ((!formNode) || (!formNode.tagName) || (!formNode.tagName.toLowerCase() == "form")) {
			this.alert('请指定一个正确的form节点！');
			return null;
		}
		var values = [];
		for (var i = 0; i < formNode.elements.length; i++) {
			var elm = formNode.elements[i];
			if (!elm || elm.tagName.toLowerCase() == "fieldset" || !this._formFilter(elm)) {
				continue;
			}
			var name = this.encode(elm.name);
			var type = elm.type.toLowerCase();
			if (type == "select-multiple") {
				for (var j = 0; j < elm.options.length; j++) {
					if (elm.options[j].selected) {
						values.push(name + "=" + this.encode(elm.options[j].value));
					}
				}
			} else {
				if (["radio", "checkbox"].include(type)) {
					if (elm.checked) {
						values.push(name + "=" + this.encode(elm.value));
					}
				} else {
					values.push(name + "=" + this.encode(elm.value));
				}
			}
		}
		var inputs = formNode.getElementsByTagName("input");
		for (var i = 0; i < inputs.length; i++) {
			var input = inputs[i];
			if (input.type.toLowerCase() == "image" && input.form == formNode && this._formFilter(input)) {
				var name = this.encode(input.name);
				values.push(name + "=" + this.encode(input.value));
				values.push(name + ".x=0");
				values.push(name + ".y=0");
			}
		}
		return values.join("&") + "&";
	},

	/**
	 * 表单可提交元素过滤器.
	 */
	_formFilter : function(/*object*/node) {
		var type = (node.type || "").toLowerCase();
		return !node.disabled && node.name && !(["file", "submit", "image", "reset", "button"].include(type));
	},

	/**
	 * 提示信息方法，全局.
	 */
	alert : function(/*string*/msg, /*function*/callback) {
		if (this._msgboxError == true) {alert(msg);if (typeof(callback) == 'function') callback();return;}
		try {
			Ext.MessageBox.alert('系统提示', msg.split('\n').join('<br>'), callback);
		} catch (ex) {this._msgboxError = true; alert(msg);if (typeof(callback) == 'function') callback();}
	},
	
	/**
	 * 确认对话框
	 */
	confirm : function(/*string*/msg, /*function*/callback) {
		if (this._msgboxError == true) {
			if (window.confirm(msg) && typeof(callback) == 'function') callback(); 
			return;
		}
		try {
			Ext.MessageBox.confirm('系统提示', msg.split('\n').join('<br>'), function(btn) {
				if (btn == 'yes' && typeof(callback) == 'function') callback();
			});
		} catch (ex) {this._msgboxError = true; if (window.confirm(msg) && typeof(callback) == 'function') callback();}
	},

	/**
	 * 使指定的checkbox框全部被选中或不选中
	 */
	checkAll : function(/*string*/ checkboxName, /*boolean*/ check) {
		var ckboxs = document.getElementsByName(checkboxName);
		if (ckboxs.length == 0) ckboxs = document.getElementsByTagName('INPUT');
		if (typeof(check) != 'boolean') check = false;
		for (var i = 0; i < ckboxs.length; i ++) {
			if (ckboxs[i].type != 'checkbox') continue;
			if (ckboxs[i].name != checkboxName) continue;
			ckboxs[i].checked = check;
		}
	},

	/**
	 * 获取指定名称并且被选中的checkbox，数组返回.
	 * 如果getValue为true，那么将返回其checkbox的值，否则返回checkbox对象
	 */
	getChecked : function(/*string*/ checkboxName, /*boolean,default is false*/ getValue) {
		var ckboxs = document.getElementsByName(checkboxName);
		if (ckboxs.length == 0) ckboxs = document.getElementsByTagName('INPUT');
		var result = [];
		for (var i = 0; i < ckboxs.length; i ++) {
			if (ckboxs[i].type != 'checkbox') continue;
			if (ckboxs[i].name != checkboxName) continue;
			if (!ckboxs[i].checked) continue;
			if (getValue == true) result[result.length] = ckboxs[i].value;
			else result[result.length] = ckboxs[i];
		}
		return result;
	},
	
	/**
	 * 对指定的汉字进行编码设置，尤其是在使用prototype.js的时候，如果传递的参数
	 * （不是加在URL后面的）中含有中文的话就会报错误。
	 */
	encode : function(/*string*/str, /*boolean*/multiPart) {
		if (typeof(str) != 'string') return null;
		var mt = multiPart || false;
		return mt == true ? encodeURI(str) : encodeURIComponent(str);
	},
	
	/**
	 * 对使用encode进行编码的进行解编码
	 */
	decode : function(/*string*/str, /*boolean*/multiPart) {
		if (typeof(str) != 'string') return null;
		var mt = multiPart || false;
		return mt == true ? decodeURI(str) : decodeURIComponent(str);
	}
};


/**
 * 全局使用的唯一基础类对象
 */
var base = null;
window.onload = function() {
	base = new BaseClass();
	base.registry();
	// 自动加载ext2的资源文件CSS和JS
	//new ResourceLoader().load("/js/common/ext2/resources/css/ext-all.css","css");
	for (var i = 0; i < BaseClass.funcList.length; i ++) {
		try {
			BaseClass.funcList[i]();
		} catch (ex) {
			base.alert(ex.message);
		}
	}
}
/**
 * 捕获执行JS中的错误信息，并打印
 */
window.onerror = function(_msg, _url, _line) {
	var error = '发现JS错误，报告如下：\n\n地址 : ' + _url + '\n'
		+ '行数 : ' + _line + '\n错误 : ' + _msg;
	base.alert(error);
	return true; // 在IE中可以避免在地址栏中出现错误提示
};

/**-----------------------------------------------------------------------
 * ------------------------针对select操作的实用Select类-------------------
 * -----------------------------------------------------------------------
 */
function Select(){};
/**
 * 根据指定的JSON对象来生成指定的select的options项（清除原来的options）.
 */
Select.create = function(/*string*/selectId,/*json object*/json) {
	Select.clear(selectId);
	Select.add(selectId, json);
};
/**
 * 该方法同create,只不过是在原来的基础上进行追加
 */
Select.add = function(/*string*/selectId,/*json object*/json) {
	try {
		if (!json.options) return;
		for (var i = 0; i < json.options.length; i ++) {
			Select.addOption(selectId,json.options[i].value,json.options[i].text);
		}
	} catch (ex) {
		base.alert('设置select错误：指定的JSON对象不符合Select对象的解析要求！');
	}
};
/**
 * 创建一个options并返回
 */
Select.createOption = function(/*string*/value, /*string*/text) {
	var opt = document.createElement('option');
	opt.setAttribute('value', value);
	opt.innerHTML = text;
	return opt;
};
/**
 * 给指定的select添加一个option,并返回当前option对象
 */
Select.addOption = function(/*string*/selectId, /*string*/value, /*string*/text) {
	var opt = Select.createOption(value, text);
	$(selectId).appendChild(opt);
	return opt;
};
/**
 * 获取指定select的当前被选中的options对象，如果为多选且有多个被选中则返回数组.
 */
Select.getSelected = function(/*string*/selectId) {
	var slt = $(selectId);
	if (!slt) return null;
	if (slt.type.toLowerCase() == "select-multiple") {
		var len = Select.len(selectId);
		var result = [];
		for (var i = 0; i < len; i ++) {
			if (slt.options[i].selected) result.push(slt.options[i]);
		}
		return result.length > 1 ? result : (result.length == 0 ? null : result[0]);
	} else {
		var index = $(selectId).selectedIndex;
		return $(selectId).options[index];
	}
};
/**
 * 使指定索引位置的option被选中.从0开始.
 */
Select.select = function(/*string*/selectId, /*int*/index) {
	var slt = $(selectId);
	if (!slt) return false;
	for (var i = 0; i < slt.options.length; i ++) {
		if (index == i) {
			slt.options[i].setAttribute("selected", "selected");
			return true;
		}
	}
	return false;
};
/**
 * 选中指定的select的所有option选项，如果支持多选的话
 */
Select.selectAll = function(/*string*/selectId) {
	var len = Select.len(selectId);
	for (var i = 0; i < len; i ++) Select.select(selectId, i);
};
/**
 * 获取指定select的总的options个数
 */
Select.len = function(/*string*/selectId) {
	return $(selectId).options.length;
};
/**
 * 清除select中满足条件的options，如果没有指定处理方法则清除所有options项
 */
Select.clear = function(/*string*/selectId, /*function*/iterator) {
	if (typeof(iterator) != 'function') {
		$(selectId).length = 0;
	} else {
		var slt = $(selectId);
		for (var i = slt.options.length - 1; i >= 0; i --) {
			if (iterator(slt.options[i]) == true) slt.removeChild(slt.options[i]);
		}
	}
};
/**
 * 复制指定的select的option对象到另外一指定的select对象上.如果指定了处理
 * 函数，那么只有返回true时才会copy.
 * 函数iterator参数：当前处理的option对象、目标select的options数组
 */
Select.copy = function(/*string*/srcSlt, /*string*/targetSlt, /*function*/iterator) {
	var s = $(srcSlt), t = $(targetSlt);
	for (var i = 0; i < s.options.length; i ++) {
		if (typeof(iterator) == 'function') {
			if (iterator(s.options[i], $(targetSlt).options) == true) {
				t.appendChild(s.options[i].cloneNode(true));
			}
		} else {
			t.appendChild(s.options[i].cloneNode(true));
		}
	}
};

/**-----------------------------------------------------------------------
 * ----------------------------常用正则式测试-----------------------------
 * -----------------------------------------------------------------------
 */
function Reg(){};
/**
 * 测试指定的字符串是否是正确的手机号码,以13和15开头的
 */
Reg.isMobile = function(/*string*/mobile) {
	var p = /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/
	if (p.test(mobile)) return true;
	p = /^((\(\d{2,3}\))|(\d{3}\-))?15\d{9}$/
	return p.test(mobile);
}

/**
 * 测试指定的字符串是否是正确的电话号码.固定电话有可能是12位的
 */
Reg.isPhone = function(/*string*/phone) {
	var p = /\d{6,12}$/
	return p.test(phone);
};

/**
 * 测试用户名称是否正确,用户名称的长度为3到15个字符
 */
Reg.isUserName = function(/*string*/userName) {
	var p = /^[a-zA-Z][a-zA-Z0-9_]{2,14}$/
	return p.test(userName);
};
/**
 * 测试是否是正确的email地址
 */
Reg.isEmail = function(/*string*/email) {
	var p = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/
	return p.test(email);
};

/**-----------------------------------------------------------------------
 * ----------------------------密码强度检测类-----------------------------
 * -----------------------------------------------------------------------
 */
function Password() {};
Password.check = function(/*string*/pwd, /*string*/tipsDivId) {
	var id = Password.getResult(pwd);
	var msg = ["密码过短", "密码强度差", "密码强度良好", "密码强度高"];
	var sty = [-45, -30, -15, 0];
	var col = ["#999999", "#66CC00"];
	var sWidth = 300, sHeight = 15;
  	var Bobj = $(tipsDivId);
  	if (!Bobj) return;
  	
  	with (Bobj) {
	  	style.fontSize = "12px";
	  	style.width = sWidth + "px";
	  	style.height = sHeight + "px";
	  	style.lineHeight = sHeight + "px";
	}
	var html = "";
	for (var i = 0; i < msg.length; i ++) {
		var bg_color = (i <= id) ? col[1] : col[0];
		html += "<span style='width:30px;background-color=" + bg_color + ";'>&nbsp;&nbsp;&nbsp;</span>";
	}
	Bobj.innerHTML = html;
	Bobj.title = msg[id];
};
Password.getResult = function(/*string*/pwd) {
	if (pwd.length < 6) return 0;
	var ls = 0;
  	if (pwd.match(/[a-z]/ig)) ls++;
	if (pwd.match(/[0-9]/ig)) ls++;
	if (pwd.match(/(.[^a-z0-9])/ig)) ls++;
	if (pwd.length < 6 && ls > 0) ls--;
  	return ls;
};

/**-----------------------------------------------------------------------
 * ----------------------------高亮度指定的元素---------------------------
 * -----------------------------------------------------------------------
 */
function HighLight() {};
HighLight.options = {
	id : null,
	className : null,
	interval : 255,
	times : 3000
};
HighLight.prototype = {
	exe : function(/*object*/options) {
		var _options = {};
		if (typeof(options) == 'object') {
			_options.id = options.id || HighLight.options.id;
			_options.className = options.className || HighLight.options.className;
			_options.interval = options.interval || HighLight.options.interval;
			_options.times = options.times || HighLight.options.times;
		}
		if (_options.id == null || !$(_options.id)) {
			base.alert('必须指定要高亮度显示的元素ID！');
			return false;
		} else if (!_options.className || typeof(_options.className) != 'string' || _options.className.strip() == '') {
			base.alert('请指定高亮度显示的CSS名称！');
			return false;
		}
		var elt = $(_options.id);
		if (elt.highLightHandle != null) return;
		elt.highLightHandle = setInterval(function() {
			Element.toggleClassName(elt, _options.className);
		}, _options.interval);
		window.setTimeout(function() {
			clearInterval(elt.highLightHandle);
			Element.removeClassName(_options.className);
			elt.removeAttribute('highLightHandle');
		}, _options.times);
		return true;
	}
};

/**-----------------------------------------------------------------------
 * -------------------------资源(包括CSS和JS)动态加载类-------------------
 * ---注意使用时必须保证被加载的JS文件是使用UTF-8保存，否则会出现中文乱码！
 * -----------------------------------------------------------------------
 */
function ResourceLoader()	{};
ResourceLoader.execute = function(/*function*/callback) {
	if (!callback) return;
    try {
    	if (typeof(callback) == 'function')  callback();
        else eval(callback);
    } catch (ex) {base.alert(ex.message);};
};
ResourceLoader.loadJS = function(/*Object*/xmlHttp) {
	var script = document.createElement('script');
	script.type = "text/javascript";
    script.text = xmlHttp.responseText;
    return script;
};
ResourceLoader.loadCSS = function(/*string*/path) {
	var css = document.createElement('link');
	css.type = "text/css";
	css.rel = "stylesheet";
    css.href = path;
    return css;
};
ResourceLoader.loaded = [];
ResourceLoader.prototype = {
	_path : null, // 要加载的资源的路径
	_type : null, // 要加载的资源类型，为js或css
	_head : null, // 文档对象的head头对象
	/**
	 * 主要调用方法.
	 */
	require : function(/*String*/path, /*string*/type, /*function*/callback) {
		if (!this._check(path, type)) return false;
		if (this._isload(path)) {
			if (type == 'js') ResourceLoader.execute(callback);
			return true;
		}
		if (type == 'js') this._ajaxLoad(callback);
		else this._head.appendChild(ResourceLoader.loadCSS(path));
		return true;
	},
	load : function(/*String*/path, /*string*/type, /*function*/callback) {
		return this.require(path, type, callback);
	},
	_check : function(path, type) {
		if (!path) {
			base.alert('请指定要加载的资源文件路径！');return false;
		} else if (!type || !['js','css'].include(type)) {
			base.alert('请正确指定要加载的资源类型(js或css)!');return false;
		}
		var head = document.getElementsByTagName('head');
        if (!head || head.length < 1) {
        	base.alert('文档对象document必须有HEAD头！');return false;
        }
		this._path = path;
		this._type = type;
        this._head = head[0];
		return true;
	},
	_isload : function(path) {
		for (var i = 0; i < ResourceLoader.loaded.length; i ++) {
			if (ResourceLoader.loaded[i].toLowerCase() == path.toLowerCase()) return true;
		}
		ResourceLoader.loaded.push(path);
		return false;
	},
	_ajaxLoad : function(callback) {
		var head = this._head;
		base.request(this._path, function(xmlHttp, error) {
        	head.appendChild(ResourceLoader.loadJS(xmlHttp));
        	ResourceLoader.execute(callback);
		});
	}
};

/**-----------------------------------------------------------------------
 * ----------------------------即时编辑功能控件---------------------------
 * 单行文本编辑：type="text"(默认)，cols表示input的size，maxlength表示允许输入的长度
 * 多行文本编辑：type="textarea"，cols表示列宽，rows表示行数，maxlength表示允许输入的长度
 * 下拉列表编辑：type="select"，selectList下拉列表(如[{text:'选项1',value:1},{text:'选项2',value:2}])，selectedValue选中的值
 * 公用：onComplete  更新成功后调用的回调方法，参数为：原始元素对象、编辑器对象。回调方法主要是处理原始元素的显示内容
 *       onSubmit    提交前的值检查方法，参数为：原始元素对象、编辑器对象。这个是在指定的长度之后的检查,返回true则提交，其他不提交
 *       paramName   编辑后的内容传递到服务器端时的参数名称，默认是value
 * -----------------------------------------------------------------------
 */
BaseClass.InPlaceEditor=function(){this.initialize.apply(this, arguments);}
BaseClass.InPlaceEditor.member=0;
BaseClass.InPlaceEditor.prototype = {
	initialize : function(element, url, options) {
		this._element = $(element);
		if (!this._element) { alert("指定的页面元素不存在！"); return; };
		this._url = url.indexOf('?')==-1 ? (url+'?') : (url+'&');
		this._options = Object.extend({
			paramName: "value",
			type: "text",
			rows: 1,
			cols: 0,
			maxlength: 0,
			selectList: null,
			selectedValue: null,
			stripTags: true,
			onComplete: null,
			onSubmit: null,
			clickToEditText: "双击进行编辑",
			hoverBackground: "#FFFF99",
			editorBackground: "#FFFF99"
		}, options || {});
			
		BaseClass.InPlaceEditor.member ++;
		
		this.originalBackground = Element.getStyle(this._element, 'backgroundColor') || "transparent";
		this._element.title = this._options.clickToEditText;
		this._dbclickListener = this._editMode.bindAsEventListener(this);
		this._mouseoverListener = this._enterHover.bindAsEventListener(this);
		this._mouseoutListener = this._leaveHover.bindAsEventListener(this);
		Event.observe(this._element, 'dblclick', this._dbclickListener);
    	Event.observe(this._element, 'mouseover', this._mouseoverListener);
    	Event.observe(this._element, 'mouseout', this._mouseoutListener);
	},
	
	_editMode: function() {
		var html = '';
		if (this._options.type.toLowerCase() == "textarea") html = this._getTextareaEditor(this._element.innerHTML);
		else if (this._options.type.toLowerCase() == "select") html = this._getSelectEditor();
		else html = this._getInputEditor(this._element.innerHTML);
		Element.hide(this._element);
		new Insertion.Before(this._element, html);
		var btn = this._getOkAndCancelBtn();
		new Insertion.Before(this._element, btn.ok);new Insertion.Before(this._element, btn.cancel);
		this.okClick = this._submit.bindAsEventListener(this);
		this.cancelClick = this._cancel.bindAsEventListener(this);
		Event.observe($(this._okBtnId), 'click', this.okClick);
		Event.observe($(this._cancelBtnId), 'click', this.cancelClick);
	},
	
	_enterHover: function() { Element.setStyle(this._element, {backgroundColor: this._options.hoverBackground}); },
	_leaveHover: function() { Element.setStyle(this._element, {backgroundColor: this.originalBackground}); },
	
	_getInputEditor: function(value) {
		this._editorId = 'ipe_editor_' + BaseClass.InPlaceEditor.member + '_' + Math.random();
		var txt = '<input type="text" style="background:' + this._options.editorBackground + ';"';
		if (this._options.cols > 0) txt += ' size="' + this._options.cols + '"';
		if (this._options.maxlength > 0) txt += ' maxlength="' + this._options.maxlength + '"';
		return txt + ' value="' + value + '" id="' + this._editorId + '"/>';
	},
	
	_getTextareaEditor: function(value) {
		this._editorId = 'ipe_editor_' + BaseClass.InPlaceEditor.member + '_' + Math.random();
		var txt = '<textarea style="background:' + this._options.editorBackground + ';"';
		txt += ' rows="' + this._options.rows + '"';
		if (this._options.cols > 0) txt += ' cols="' + this._options.cols + '"';
		return txt + ' id="' + this._editorId + '">' + value + '</textarea>';
	},
	
	_getSelectEditor: function() {
		this._editorId = 'ipe_editor_' + BaseClass.InPlaceEditor.member + '_' + Math.random();
		var txt = '<select style="background:' + this._options.editorBackground + ';" id="' + this._editorId + '">"';
		if(this._options.selectList) {
			for (var i = 0; i < this._options.selectList.length; i ++) {
				var opt = this._options.selectList[i];
				txt += '<option value="' + opt.value + '"';
				if (this._options.selectedValue && this._options.selectedValue == opt.value) txt += ' selected';
				txt += '>' + opt.text + '</option>';
			}
		}
		return txt + '</select>';
	},
	
	_getOkAndCancelBtn: function() {
		this._okBtnId = 'ipe_ok_' + BaseClass.InPlaceEditor.member + '_' + Math.random();
		this._cancelBtnId = 'ipe_cancel_' + BaseClass.InPlaceEditor.member + '_' + Math.random();
		var okBtn = '<input type="button" value="保存" id="' + this._okBtnId + '" />';
		var cancelBtn = '<input type="button" value="取消" id="' + this._cancelBtnId + '" />';
		return {ok: okBtn, cancel: cancelBtn};
	},
	
	_submit: function() {
		var editor = $(this._editorId), editMode = this._options.type.toLowerCase();
		if (editMode != "select") {
			editor.value = editor.value.strip();
			if (this._options.stripTags == true) editor.value = editor.value.stripTags();
			if (editor.value == "") { alert('要编辑的值不能为空！');editor.focus();return; }
			if (this._options.maxlength > 0 && editor.value.length > this._options.maxlength) {
				alert('输入的内容已经超过' + this._options.maxlength + '个字符的限制！');
				editor.focus();return;
			}
		}
		try {
			var checkReturn = true;
			if (this._options.onSubmit) checkReturn = this._options.onSubmit(this._element, editor);
			if (checkReturn != true) return;
		} catch(ex){alert('提交检查方法错误：' + ex.message);return;}
		$(this._okBtnId).disabled="disabled";$(this._cancelBtnId).disabled="disabled";
		var eId = this._editorId, oId = this._okBtnId, cId = this._cancelBtnId, et = this._element;
		var callback = this._options.onComplete || this._innerCallback;
		try {
			base.request(
				this._url + this._options.paramName + '=' + base.encode(editor.value),
				function(xmlHttp) {
					var returnVal = xmlHttp.responseText;
					var obj = base.json(returnVal);
					if (!obj) { base.exe(returnVal); alert('系统发生错误，暂时无法处理该请求！'); return; }
					if (obj.success == false) { alert(obj.message); return; }
					try {
						callback(et, editor);
					} catch(ex) {alert('回调方法错误：' + ex.message);}
					alert(obj.message);
					Element.remove(eId);Element.remove(oId);Element.remove(cId);
					Element.show(et);
				}
			);
		}catch(ex){alert(ex.message);}
		$(this._okBtnId).disabled="";$(this._cancelBtnId).disabled="";
	},
	
	_innerCallback: function(originalElement, editor) {
		if (editor.tagName.toLowerCase() != "select") originalElement.innerHTML = editor.value;
		else originalElement.innerHTML = Select.getSelected(editor).firstChild.nodeValue;
	},
	
	_cancel: function() {
		Element.remove(this._editorId);Element.remove(this._okBtnId);Element.remove(this._cancelBtnId);
		Element.show(this._element);
	}
};