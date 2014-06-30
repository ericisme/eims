<#-- 后台管理列表页面的温馨提示的公用宏定义 -->

<#macro listTips tips>
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td><font color="red">温馨提示&gt;&gt;&gt;</font></td>
  </tr>
  <#list tips as tip>
  <tr>
    <td height="4">${tip_index+1}.${tip}</td>
  </tr>
  </#list>
</table>
</#macro>