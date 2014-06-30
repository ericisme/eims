//计算字符输入数
function checkMaxLength(event,maxcount){
	var event=event || window.event;
	var target=event.target || event.srcElement;
	var keyCode=event.charCode || event.keyCode;
	var trueLength=0;
	var c=-1;
	if(keyCode != 8 && keyCode != 46){
		for(i=0;i<target.value.length;i++){
			if(target.value.charCodeAt(i)>0&&target.value.charCodeAt(i)<255){
				trueLength++;
			}else{
				trueLength+=2;
			}
			if(trueLength>maxcount){
				c=i;
				break;
			}
		}
	}
	if(trueLength>maxcount){
		target.value=target.value.substring(0,c);
	}
}
function checkNumber(checkStr) {
    var checkOK = "0123456789";
	var allValid = true;
	for (i=0;i<checkStr.length;i++)
	{
	  ch=checkStr.charAt(i);
	  for(j=0;j<checkOK.length;j++)
	  if(ch==checkOK.charAt(j))
	    break;
	  if(j==checkOK.length)
	   {
	    allValid = false;
	    break;
	   }
	}
	return allValid;
}

//验证手机号码是否正确
function isMobile(value) {
	if (/^13\d{9}$/g.test(value) || (/^15[0-35-9]\d{8}$/g.test(value))
			|| (/^18[05-9]\d{8}$/g.test(value))) {
		return true;
	} else {
		return false
	}
}

// 验证身份证是否正确
function isIdCard(value) {
	if (/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/.test(value)
			|| (/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([1-9]|X)$/.test(value))) {
		return true;
	} else {
		return false
	}
}

//批量禁用或启用按钮
function button_disable(formId, type) {
	var form = $(formId);
	if (!form) return null;
	var inputs = form.getElementsByTagName('INPUT');
	for (var i = 0; i < inputs.length; i ++) {
		if (inputs[i].getAttribute('type') == 'submit' || inputs[i].getAttribute('type') == 'button') 
			inputs[i].disabled = type;
	}
	return null;
};

//批量修改<options>, 根据隐藏值修改显示值
function updateOptionStr(formId, val, str) {
	var form = $(formId);
	if (!form) return null;
	var options = form.getElementsByTagName('OPTION');
	for (var i = 0; i < options.length; i ++) {
		if(options[i].value == val)
			options[i].text = str;
	}
	return null;
};