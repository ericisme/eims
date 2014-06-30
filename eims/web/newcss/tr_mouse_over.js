// JavaScript Document

function overLine(src, clrOver) {
	if (!src.contains(event.fromElement)) {
		src.bgColor = clrOver;
	}
}
function outLine(src,clrIn) {
	if (!src.contains(event.toElement)) {
		src.bgColor = clrIn;
	}
}
