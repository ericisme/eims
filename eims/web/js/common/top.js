// 系统退出
function adminExit()
{
	if (!confirm('您确信要退出系统？')) return;
	parent.parent.window.location.href = "/syspurview/login.do?action=loginout";
}