<#-- 这里加html和body是为了使用页面级的按钮权限控制 -->
<html>
<body>
<#-- 首先获取当前的用户的size大小 -->
<#assign pUserSize=page.result?size />
<table width="100%" border="0" cellpadding="5" cellspacing="1" class="list_table" style="word-break:break-all">
	<tr class="list_title">
		<td width="8%" align="center">
		<input type="checkbox" name="ckbox" value="checkbox" onclick="base.checkAll('userId',this.checked)" />全选
		</td>
    	<td width="10%" align="center">真实姓名</td>
    	<td width="10%" align="center">登陆名称</td>
    	<td width="5%" align="center">性别</td>
    	<td width="5%" align="center">状态</td>
    	<td width="11%" align="center">手机号码</td>
    	<td width="10%" align="center">所属用户组</td>
    	<!--td width="10%" align="center">所属组织机构</td-->
    	<td width="20%" align="center">操作</td>
	</tr>
	<#if pUserSize = 0>
	<tr bgcolor="#FFFFFF" onMouseOver="this.className='list_over'" onMouseOut="this.className='list_out'">
		<td align="center" colspan="9"><font color="red">暂时没有任何用户！</font></td>
	</tr>
	</#if>
	<#list page.result as user>
	<tr bgcolor="#FFFFFF" onMouseOver="this.className='list_over'" onMouseOut="this.className='list_out'">
	  	<td align="center"><input type="checkbox" name="userId" value="${user.userId}" /> ${user_index+page.startIndex}</td>
	    <td align="center">${user.realName}</td>
	    <td align="center">${user.loginName}</td>
	    <td align="center">${user.man?string("男","女")}</td>
	    <td align="center">
	    	<span id="show_${user.userId}" name="${configer.prefix}">
	    		<#if user.lock>
	    			<img src="${configer.prefix}/images/lock.gif" border="0" title="封锁" />
	    		<#else>
	    			<img src="${configer.prefix}/images/normal.gif" border="0" title="正常" />
	    		</#if>
	    	</span>
	    	<input id="status_${user.userId}" type="hidden" value='${user.lock?string("true","false")}'/>
	    </td>
	    <td align="center">${user.mobile}</td>
	    <td align="center">${user.groupName}</td>
	    <td align="center">
	    	<input sys_opt="edit" onclick="userEdit('${user.userId}')" type="button" class="btn01" onmouseover="this.className='btn02'" onMouseOut="this.className='btn01'" value="编 辑" />&nbsp;
	    	<input sys_opt="remove" onclick="userDel('${user.userId}')" type="button" class="btn01" onmouseover="this.className='btn02'" onMouseOut="this.className='btn01'" value="删 除" />&nbsp;
	    	<input sys_opt="show" onclick="userDetail('${user.userId}')" type="button" class="btn01" onmouseover="this.className='btn02'" onMouseOut="this.className='btn01'" value="查看详情" />
	    </td>
	</tr>
	</#list>
</table>
<table width="100%" border="0" cellpadding="5" cellspacing="1">
	<tr>
		<td width="60%">&nbsp;</td>
		<td align="right">${page.paginate}</td>
	</tr>
</table>
</body>
</html>