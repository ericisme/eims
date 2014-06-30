<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>�û�����ȡ�� - �����û���</title>
<script language="javascript">
<!--
function checkUserName()
{
	var et = document.getElementById("userName");
	var val = et.value.replace(/^\s+/, '').replace(/\s+$/, '');
	if (val == "") {
		alert("������Ҫȡ��������û���ƣ�");
		et.focus();
		return;
	}
	window.location.href="/common/getpwd.do?action=initStep2&userName=" + val;
}
-->
</script>
</head>

<body onload="javascript:document.getElementById('userName').focus();">
<center>
<table width="400" cellpadding="5" cellspacing="1" border="0" style="background-color:#f2f9fd;border:1px solid #94ba00;font-size:12px;">
	<tr>
		<td colspan="2" align="center"><Strong>�û�����ȡ�ص�һ���������û����</Strong></td>
	</tr>
	<tr style="background-color:#ffffff;">
		<td align="right">�û���</td>
		<td><input name="userName" id="userName" type="text" maxlength="50" size="30" /></td>
	</tr>
	<tr style="background-color:#ffffff;">
		<td colspan="2" align="center">
			<input type="button" value="��һ��" onclick="checkUserName()" />
		</td>
	</tr>
</table>
</center>
</body>
</html>
