<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>${configer.systemName} - 用户管理</title>
<link href="${configer.prefix}/skins/default/body.css" rel="stylesheet" type="text/css" />
<script language="javascript" src="${configer.prefix}/js/base.js?load(page_exe.js,user.js)"></script>
<script language="javascript" src="/js/common/prototype.js"></script>
<script language="javascript" src="/js/common/log/log4javascript.js"></script>
<script language="javascript" src="/js/common/calender/calendar.js"></script>
<script language="javascript" src="/js/common/calender/calendar-setup.js"></script>
<script language="javascript" src="/js/common/calender/lang/calendar-zh.js"></script>
<script language="javascript" src="/html/pm/zzjg/zzjg.js"></script>
<link href="/js/common/calender/css/calendar-tas.css" rel="stylesheet" type="text/css">
</head>

<body class="body_bg">
<script type="text/javascript">
<!--
	BaseClass.addOnLoad(userQuery);
-->
</script>
<#include "../common_loading.ftl" />
<div id="user_index">
<table width="100%" border="0" cellspacing="0" cellpadding="3">
	<tr>
    	<td class="line">您现在的位置： 用户管理 -> 用户列表</td>
	</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td height="5"></td></tr>
</table>
<#include "../common_tips.ftl" />
<table width="100%" border="0" cellpadding="5" cellspacing="1" class="list_query">
	<tr>
		<td width="100%" align="left">
			用户姓名：<input name="qryName" id="qryName" type="text" maxlength="10" size="15" onFocus="this.className='box1'" onBlur="this.className='box0'" />&nbsp;&nbsp;&nbsp;&nbsp;
			岗位：
			<select name="qryGroupId" id="qryGroupId">
				<option value="">---请选择岗位---</option>
				<#list userGroups as ug>
					<option value="${ug.groupId}">${ug.groupName}</option>
				</#list>
			</select>&nbsp;&nbsp;&nbsp;&nbsp;
			<input name="sub" type="button" onclick="userQuery()" class="btn01" onmouseover="this.className='btn02'" onMouseOut="this.className='btn01'" value="查 询" />&nbsp;
			<input name="sub" sys_opt="save" type="button" onclick="userAdd()" class="btn01" onmouseover="this.className='btn02'" onMouseOut="this.className='btn01'" value="添 加" />&nbsp;
			<input name="sub" sys_opt="edit" type="button" onclick="userEdit()" class="btn01" onmouseover="this.className='btn02'" onMouseOut="this.className='btn01'" value="修 改" />&nbsp;
			<input name="sub" sys_opt="remove" type="button" onclick="userDel()" class="btn01" onmouseover="this.className='btn02'" onMouseOut="this.className='btn01'" value="删 除">&nbsp;
			<input name="sub" sys_opt="lock" type="button" onclick="userLockAndUnlock()" class="btn01" onmouseover="this.className='btn02'" onMouseOut="this.className='btn01'" value="封锁/解锁">&nbsp;
		        <input name="sub" sys_opt="updatePwd" onclick="updatePwd()" type="button" class="btn01" onmouseover="this.className='btn02'" onMouseOut="this.className='btn01'" value="密码重置" />
		</td>
	</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td height="5"></td></tr>
</table>
<span id="user_list">
</span>
</div>
<div id="user_update" style="display:none">
</div>
</body>
</html>
