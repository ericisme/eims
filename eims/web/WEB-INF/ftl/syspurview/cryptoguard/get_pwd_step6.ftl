<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>�û�����ȡ�� - �ʼ�ȡ����������ҳ��</title>
<script language="javascript">
<!--
function checkUserPwd()
{
	var et = document.getElementById("userPwd");
	var val = et.value.replace(/^\s+/, '').replace(/\s+$/, '');
	if (val == "") {
		alert("��������������룡");
		et.focus();
		return;
	} else if (val.length < 6) {
		alert("Ϊ��֤���밲ȫ�����볤�Ȳ���������6λ��");
		et.focus();
		return;
	}
	var pwd = val;
	
	et = document.getElementById("userPwd2");
	val = et.value.replace(/^\s+/, '').replace(/\s+$/, '');
	if (val == "") {
		alert("���ٴ�ȷ����������룡");
		et.focus();
		return;
	} else if (val != pwd) {
		alert("}����������벻һ�£�");
		et.focus();
		return;
	}
	
	window.location.href="/common/getpwd.do?action=emailResetPwd&userPwd=" + pwd;
}
-->
</script>
</head>

<body onload="javascript:document.getElementById('userPwd').focus();">
<center>
<table width="400" cellpadding="5" cellspacing="1" border="0" style="background-color:#f2f9fd;border:1px solid #94ba00;font-size:12px;">
	<tr>
		<td colspan="2" align="center"><Strong>�û�����ȡ�ص��Ĳ�����������</Strong></td>
	</tr>
	<tr style="background-color:#ffffff;">
		<td align="right">���������룺</td>
		<td><input name="userPwd" id="userPwd" type="password" maxlength="18" size="30" /></td>
	</tr>
	<tr style="background-color:#ffffff;">
		<td align="right">������ȷ�ϣ�</td>
		<td><input name="userPwd2" id="userPwd2" type="password" maxlength="18" size="30" /></td>
	</tr>
	<tr style="background-color:#ffffff;">
		<td colspan="2" align="center">
			<input type="button" value="��һ��" onclick="checkUserPwd()" />
		</td>
	</tr>
</table>
</center>
</body>
</html>
