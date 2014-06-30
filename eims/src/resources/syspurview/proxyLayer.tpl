<!-- 代理登陆时的浮动层 -->
<div id="proxyLayer_Div_08123" style="width:98%;height:20px;">
<table width="100%" border="0">
  <tr>
    <td>欢迎您：%proxyUserName%，您现在是代理<font color="red">%curUserName%</font>进行操作！</td>
    <td width="100px">
    	<a href="javascript:flClose();" title="关闭浮动提示条"><u>关闭</u></a>&nbsp;
    	<a href="javascript:flBack();" title="返回到个人操作平台"><u>返回</u></a>&nbsp;
    	<a href="javascript:flExit();" title="退出城市教育平台"><u>退出</u></a>
    </td>
  </tr>
</table>
</div>
<!-- 代理登陆时的浮动层JS控制和层定义 -->
<script language="javascript">
function flClose(){document.getElementById('proxyLayer_Div_08123').style.display='none';}
function flBack(){if(confirm('您确信要回到个人操作平台吗？'))parent.parent.window.location.href='/syspurview/login.do?action=proxyLoginBack';}
function flExit(){if(confirm('您确信要退出城市教育平台吗？'))parent.parent.window.location.href='/syspurview/login.do?action=loginout';}
function FloatLayer(){};FloatLayer.config ={ top : 6,id : '',times : 10};FloatLayer._oldPos = null;FloatLayer._tips = null;FloatLayer.prototype ={show : function(config){if(config && typeof(config) == 'object') this._setConfig(config);if(!FloatLayer.config.id) return;FloatLayer._tips = document.getElementById(FloatLayer.config.id);if(!FloatLayer._tips) return;setInterval(this.start,FloatLayer.config.times);},start : function(){var position = 0;if(window.innerHeight) position = window.pageYOffset;else if(document.documentElement && document.documentElement.scrollTop){position = document.documentElement.scrollTop;} else if(document.body){position = document.body.scrollTop;};position = position - FloatLayer._tips.offsetTop + FloatLayer.config.top;position = FloatLayer._tips.offsetTop + position/3;if(position < this._oldPos) position = FloatLayer.config.top;if(position != this._oldPos){FloatLayer._tips.style.top = position + 'px';FloatLayer.config.times = 10;};this._oldPos = position;},_setConfig : function( config){for(var prop in FloatLayer.config){if(config[prop]) FloatLayer.config[prop] = config[prop];}}};
new FloatLayer().show({id : 'proxyLayer_Div_08123', top : 3, times : 1});
</script>
<!-- 浮动层的CSS控制 -->
<style type="text/css">
div#proxyLayer_Div_08123{position:absolute;border:2px solid #FFD383;padding:3px;background:FFFFE1;opacity:0.9;filter:alpha(opacity=90);}
</style>