<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>�û�����ȡ�� - ���뱣������ش�</title>
<script language="javascript">
<!--
function checkUserAnswer()
{
	var et = document.getElementById("userAnswer");
	var val = et.value.replace(/^\s+/, '').replace(/\s+$/, '');
	if (val == "") {
		alert("���������뱣������Ĵ𰸣�");
		et.focus();
		return;
	}
	
	window.location.href="/common/getpwd.do?action=checkAnswer&answer=" + encodeURIComponent(val);
}
-->
</script>
</head>

<body onload="javascript:document.getElementById('userAnswer').focus();">
<center>
<table width="400" cellpadding="5" cellspacing="1" border="0" style="background-color:#f2f9fd;border:1px solid #94ba00;font-size:12px;">
	<tr>
		<td colspan="2" align="center"><Strong>�û�����ȡ�ص���ش����뱣������</Strong></td>
	</tr>
	<tr style="background-color:#ffffff;">
		<td align="right">���뱣�����⣺</td>
		<td>${userQuestion}</td>
	</tr>
	<tr style="background-color:#ffffff;">
		<td align="right">����𰸻ش�</td>
		<td><input name="userAnswer" id="userAnswer" type="text" maxlength="200" size="30" /></td>
	</tr>
	<tr style="background-color:#ffffff;">
		<td colspan="2" align="center">
			<input type="button" value="��һ��" onclick="checkUserAnswer()" />
		</td>
	</tr>
</table>
</center>
</body>
</html>
