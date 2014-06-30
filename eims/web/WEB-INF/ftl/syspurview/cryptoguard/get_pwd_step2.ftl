<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>�û�����ȡ�� - �������Ϣ</title>
<script language="javascript">
<!--
function checkUserInfo()
{
	var val = "";
	
	if (!${useEmailMethod?string("true","false")}) {
		var et = document.getElementById("userMobile");
		val = et.value.replace(/^\s+/, '').replace(/\s+$/, '');
		if (val == "") {
			alert("�������û����ֻ���룡");
			et.focus();
			return;
		} else if (!isMobile(val)) {
			alert("��������ȷ���ֻ���룡");
			et.focus();
			return;
		}
		window.location.href="/common/getpwd.do?action=initStep3&userMobile=" + val;
	} else {
		var et = document.getElementById("userEmail");
		val = et.value.replace(/^\s+/, '').replace(/\s+$/, '');
		if (val == "") {
			alert("�������û��ĵ����ʼ���ַ��");
			et.focus();
			return;
		} else if (!isEmail(val)) {
			alert("��������ȷ�ĵ����ʼ���ַ��");
			et.focus();
			return;
		}
		window.location.href="/common/getpwd.do?action=initStep3&userEmail=" + val;
	}
}

function isMobile(mobile)
{
	var p = /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/
	if (p.test(mobile)) return true;
	p = /^((\(\d{2,3}\))|(\d{3}\-))?15\d{9}$/
	return p.test(mobile);
}

function isEmail(email)
{
	var p = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/
	return p.test(email);
}
-->
</script>
</head>

<#assign etId=useEmailMethod?string("userEmail","userMobile")>
<body onload="javascript:document.getElementById('${etId}').focus();">
<center>
<table width="400" cellpadding="5" cellspacing="1" border="0" style="background-color:#f2f9fd;border:1px solid #94ba00;font-size:12px;">
	<tr>
		<td colspan="2" align="center"><Strong>�û�����ȡ�صڶ�����<#if useEmailMethod>��������ʼ���ַ<#else>�����ֻ����</#if></Strong></td>
	</tr>
	<tr style="background-color:#ffffff;<#if useEmailMethod>display:none</#if>" >
		<td  align="right">�ֻ���룺</td>
		<td><input name="userMobile" id="userMobile" type="text" maxlength="11" size="30" /></td>
	</tr>
	<tr  style="background-color:#ffffff;<#if !useEmailMethod>display:none</#if>" >
		<td  align="right">���ʵ�ַ��</td>
		<td><input name="userEmail" id="userEmail" type="text" maxlength="255" size="30" /></td>
	</tr>
	<tr style="background-color:#ffffff;">
		<td colspan="2" align="center">
			<input type="button" value="��һ��" onclick="checkUserInfo()" />
		</td>
	</tr>
</table>
</center>
</body>
</html>
