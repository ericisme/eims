<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>�û�����ȡ�� - �ֻ������֤������</title>
<script language="javascript">
<!--
function checkRandomCode()
{
	var et = document.getElementById("code");
	var val = et.value.replace(/^\s+/, '').replace(/\s+$/, '');
	if (val == "") {
		alert("�������ֻ�ص������֤�룡");
		et.focus();
		return;
	}
	
	window.location.href="/common/getpwd.do?action=initSMSRandomCode&userCode=" + val;
}
-->
</script>
</head>

<body onload="javascript:document.getElementById('code').focus();">
<center>
<table width="400" cellpadding="5" cellspacing="1" border="0" style="background-color:#f2f9fd;border:1px solid #94ba00;font-size:12px;">
	<tr>
		<td colspan="2" align="center"><Strong>�û�����ȡ�ص��Ĳ����ֻ���֤������</Strong></td>
	</tr>
	<tr style="background-color:#ffffff;">
		<td align="right">�ֻ���֤�룺</td>
		<td><input name="code" id="code" type="text" maxlength="30" size="30" /></td>
	</tr>
	<tr style="background-color:#ffffff;">
		<td colspan="2" style="padding-left:20px;"><Strong>��ʾ&gt;&gt;&gt;</Strong>�������ֻ��յ��Ķ�����ʾ�룡���û���յ��������ĵȴ���Ӽ��ɣ�</td>
	</tr>
	<tr style="background-color:#ffffff;">
		<td colspan="2" align="center">
			<input type="button" value="��һ��" onclick="checkRandomCode()" />
		</td>
	</tr>
</table>
</center>
</body>
</html>
