<#-- �����html��body��Ϊ��ʹ��ҳ�漶�İ�ťȨ�޿��� -->
<html>
<body>
<#-- ���Ȼ�ȡ��ǰ���û���size��С -->
<#assign pUserSize=page.result?size />
<table width="100%" border="0" cellpadding="5" cellspacing="1" class="list_table" style="word-break:break-all">
	<tr class="list_title">
		<td width="8%" align="center">
		<input type="checkbox" name="ckbox" value="checkbox" onclick="base.checkAll('userId',this.checked)" />ȫѡ
		</td>
    	<td width="10%" align="center">��ʵ����</td>
    	<td width="10%" align="center">��½����</td>
    	<td width="5%" align="center">�Ա�</td>
    	<td width="5%" align="center">״̬</td>
    	<td width="11%" align="center">�ֻ�����</td>
    	<td width="10%" align="center">�����û���</td>
    	<!--td width="10%" align="center">������֯����</td-->
    	<td width="20%" align="center">����</td>
	</tr>
	<#if pUserSize = 0>
	<tr bgcolor="#FFFFFF" onMouseOver="this.className='list_over'" onMouseOut="this.className='list_out'">
		<td align="center" colspan="9"><font color="red">��ʱû���κ��û���</font></td>
	</tr>
	</#if>
	<#list page.result as user>
	<tr bgcolor="#FFFFFF" onMouseOver="this.className='list_over'" onMouseOut="this.className='list_out'">
	  	<td align="center"><input type="checkbox" name="userId" value="${user.userId}" /> ${user_index+page.startIndex}</td>
	    <td align="center">${user.realName}</td>
	    <td align="center">${user.loginName}</td>
	    <td align="center">${user.man?string("��","Ů")}</td>
	    <td align="center">
	    	<span id="show_${user.userId}" name="${configer.prefix}">
	    		<#if user.lock>
	    			<img src="${configer.prefix}/images/lock.gif" border="0" title="����" />
	    		<#else>
	    			<img src="${configer.prefix}/images/normal.gif" border="0" title="����" />
	    		</#if>
	    	</span>
	    	<input id="status_${user.userId}" type="hidden" value='${user.lock?string("true","false")}'/>
	    </td>
	    <td align="center">${user.mobile}</td>
	    <td align="center">${user.groupName}</td>
	    <td align="center">
	    	<input sys_opt="edit" onclick="userEdit('${user.userId}')" type="button" class="btn01" onmouseover="this.className='btn02'" onMouseOut="this.className='btn01'" value="�� ��" />&nbsp;
	    	<input sys_opt="remove" onclick="userDel('${user.userId}')" type="button" class="btn01" onmouseover="this.className='btn02'" onMouseOut="this.className='btn01'" value="ɾ ��" />&nbsp;
	    	<input sys_opt="show" onclick="userDetail('${user.userId}')" type="button" class="btn01" onmouseover="this.className='btn02'" onMouseOut="this.className='btn01'" value="�鿴����" />
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