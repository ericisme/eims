<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>${configer.systemName} - ��½</title>
<link href="${configer.prefix}/skins/default/body.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="${configer.prefix}/js/base.js"></script>
<script language="javascript" src="/js/common/prototype.js"></script>
<script language="javascript">
<!--
function checkLogin()
{
	if ($('userName').value.strip() == '') {
		base.alert('�������û���');
		$('userName').focus();
		return false;
	} else if ($('userPassword').value.strip() == '') {
		base.alert('�������½���룡');
		$('userPassword').focus();
		return false;
	} else if ($('randCode').value.strip() == '') {
		base.alert('��������֤�룡');
		$('randCode').focus();
		return false;
	}
	Element.hide("image1");Element.show("image2");
	base.formSubmit('/syspurview/login.do?action=login', function(xmlHttp, error) {
		var obj = base.json(xmlHttp.responseText);
		if (obj.success == false) {
			Element.hide("image2");Element.show("image1");
			base.alert(obj.message);
		}
		if (obj.success == true && obj.location) {
			$("image2").innerHTML = "��½�ɹ�����";
			window.location.href = obj.location;
		}
	}, 'form1');
	return false;
}

function keyDown(){
	var evt = window.event || arguments.callee.caller.arguments[0];
	if(evt.keyCode==13) checkLogin();
}

function refreshRand(img)
{
	img.src = '/rand.do?len=4&rand=' + Math.random();
}

BaseClass.addOnLoad(function(){ $("userName").focus(); });
-->
</script>
<style type="text/css">
<!--
body {
	margin: 0px;
}
a:link {
	text-decoration: none;
	color: #003399;
}
a:visited {
	text-decoration: none;
	color: #003399;
}
a:hover {
	text-decoration: none;
	color: #990033;
}
a:active {
	text-decoration: none;
	color: #003399;
}
body,td,th {
	font-size: 12px;
}
-->
</style></head>

<body>
<script type="text/javascript">
<!--
	BaseClass.addOnLoad(function(){
		form1.userName.focus();
	});
-->
</script>
<form name="form1" id="form1" method="POST" onsubmit="return checkLogin();">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center" valign="middle"><table width="734" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><img src="../images/login_01.gif" width="111" height="87" alt=""></td>
        <td><img src="../images/login_02.gif" width="249" height="87" alt=""></td>
        <td><img src="../images/login_03.gif" width="374" height="87" alt=""></td>
      </tr>
      <tr>
        <td colspan="3"><img src="../images/login_04.gif" width="734" height="58" alt=""></td>
        </tr>
      <tr>
        <td><img src="../images/login_05.gif" width="111" height="59" alt=""></td>
        <td><img src="../images/login_06.gif" width="249" height="59" alt=""></td>
        <td><img src="../images/login_07.gif" width="374" height="59" alt=""></td>
      </tr>
      <tr>
        <td background="../images/login_09.gif"><img src="../images/login_08.gif" width="111" height="137" alt=""></td>
        <td align="center" valign="top" background="../images/login_09.gif"><table width="100%" border="0" cellspacing="2" cellpadding="4">
          <tr>
            <td width="25%" align="center" class="logintt">�û���</td>
            <td width="75%"><input name="userName" value="" id="userName" type="text" class="loginbox" size="20"></td>
          </tr>
          <tr>
            <td width="25%" align="center" class="logintt">�ܡ��룺</td>
            <td width="75%"><input name="userPassword" value="" id="userPassword" type="password" class="loginbox" size="20"></td>
          </tr>
          <tr>
            <td width="25%" align="center" class="logintt">��֤�룺</td>
            <td width="75%"><input name="randCode" id="randCode"  type="text" class="loginbox" size="5">
             <span id="image1"><img class="loginbox" src="/rand.do?len=4" onclick="refreshRand(this)" title="�������������ˢ��" style="cursor:pointer;" border="0"/></span>
             <span id="image2" style="display:none;"><img src="/html/syspurview/images/loading_16x16.gif" border="0">��½�С���</span></td>
          </tr>
        </table>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<#--tr>
		<td height="5"><#include "common_tips.ftl"/></td>
	</tr-->
	<tr>
		<td height="5" align="center">
			<input name="btn01" type="submit" class="btn01" onmouseover="this.className='btn02'" onMouseOut="this.className='btn01'" value="�� ½" />&nbsp;&nbsp;
			<input name="btn01" type="reset" class="btn01" onmouseover="this.className='btn02'" onMouseOut="this.className='btn01'" value="�� ��" />
		</td>
	</tr>
</table>
		
		</td>
        <td><img src="../images/login_10.gif" width="374" height="137" alt=""></td>
      </tr>
      <tr>
        <td><img src="../images/login_11.gif" width="111" height="27" alt=""></td>
        <td><img src="../images/login_12.gif" width="249" height="27" alt=""></td>
        <td><img src="../images/login_13.gif" width="374" height="27" alt=""></td>
      </tr>
      <tr>
        <td height="35" colspan="3" background="../images/login_14.gif"><table width="100%"  border="0" cellspacing="0" cellpadding="5" >
          <tr>
            <td align="center">Copyright 2009 ��ɽ��ũҵ�ƹ����� ALL RIGHT RESERVED.</td>
          </tr>
        </table></td>
        </tr>
      <tr>
        <td><img src="../images/login_15.gif" width="111" height="91" alt=""></td>
        <td><img src="../images/login_16.gif" width="249" height="91" alt=""></td>
        <td><img src="../images/login_17.gif" width="374" height="91" alt=""></td>
      </tr>
    </table></td>
  </tr>
</table>
</form>
</body>
</html>
