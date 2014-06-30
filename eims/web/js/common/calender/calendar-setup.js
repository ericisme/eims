/**
  * 日期控件的调用 
  * Last modify by 马必强 2007.04.05
  */

var oldLink = null;
/**
 * code to change the active stylesheet.
 * 改变日期的样式，动态，没有太大用处
 */
function setActiveStyleSheet(link, title)
{
	var i, a, main;
	var _links = document.getElementsByTagName("link");
	for (i=0; i < _links.length; i++) {
		a = _links[i];
		if (a.getAttribute("rel").indexOf("style") != -1 && a.getAttribute("title")) {
			a.disabled = true;
			if (a.getAttribute("title") == title) a.disabled = false;
		}
	}
	if (oldLink) oldLink.style.fontWeight = 'normal';
	oldLink = link;
	link.style.fontWeight = 'bold';
	return false;
}

/**
 * This function gets called when the end-user clicks on some date.
 * 增加了控制单击某个日期是否关闭日历的选择，使用_click_close控制。
 */
function selected(cal, _date)
{
	cal.sel.value = _date;    // just update the date in the input field.
    /**
     * if we add this call we close the calendar on single-click.
     * just to exemplify both cases, we are using this only for the 1st
     * and the 3rd field, while 2nd and 4th will still require double-click.
     */
	if (cal.dateClicked && (cal.sel.id == "sel1" || cal.sel.id == "sel3")) {
    	cal.callCloseHandler();
    } else if (_click_close && cal.dateClicked) {
    	cal.callCloseHandler();
    }
}

/**
 * And this gets called when the end-user clicks on the _selected_ date,
 * or clicks on the "Close" button.  It just hides the calendar without
 * destroying it.
 */
function closeHandler(cal)
{
	cal.hide();    // hide the calendar
}

/**
 * This function shows the calendar under the element having the given id or object.
 * It takes care of catching "mousedown" signals on document and hiding the
 * calendar if the click was outside.
 *
 * 参数说明：
 * _input        输入框对象，使用this或它的ID标识
 * format        日期的格式化字符串.例如 y-m-d 或 yy-mm-dd
 *               年  y 或 yy
 *               月  m 或 mm
 *               日  d 或 dd
 * click_close   单击某个日期是否关闭日历，默认false
 * canMove       是否允许拖动该日历，true - 允许  false - 不允许，默认是不允许
 * _dir          日历显示的方向，用两个长度的字符串构成表示垂直和水平位置,例如：Bl 或 Br等。
 *               第一个字符代表垂直方向、第二个字符代表水平方向。推荐使用Bl，默认就是Bl
 *               垂直方向：
 *                   T - 日历在输入框的顶端，即日历的最下端在输入框的最上端
 *                   B - 日历在输入框的底端，即日历的最上端在输入框的最下端。[推荐使用]
 *                   C - 输入框在日历的垂直最中间
 *                   t - 日历的最下端和输入框的最下端在同一水平线上
 *                   b - 日历的最上端和输入框的最上端在同一水平线上
 *               水平方向：
 *                   L - 输入框的最左端，即日历的最右端对齐输入框的最左端
 *                   R - 输入框的最右端，即日历的最左端对齐输入框的最右端
 *                   C - 输入框在日历的水平正中间
 *                   r - 输入框的右端和日历的右端对齐
 *                   l - 输入框的左端和日历的左端对齐。[推荐使用]
 *
 * 使用方法如下:
 * showCalendar(this, 'y-m-d');
 * showCalendar(this, 'y/m/d', true);
 * showCalendar(this, 'y-m-d', true, true, 'BC');
 */
var _click_close = false;
function showCalendar(_input, format, click_close, canMove, _dir)
{
	// click some date to close the calendar - add by 马必强
	if (typeof(click_close) == 'undefined' || typeof(click_close) != 'boolean') {
		_click_close = false;
	} else {
		_click_close = click_close;
	}
	// input element get
	var el = typeof(_input) == 'object' ? _input : document.getElementById(_input);
	if (el == null) {
		alert("Error:Cann't find input element which you set to!");
		return;
	}
	
	if (calendar != null) {
		// we already have some calendar created.so we hide it first.
		calendar.hide();
	} else {
		// first-time call, create the calendar.
		var cal = new Calendar(false, null, selected, closeHandler);
		// uncomment the following line to hide the week numbers. cal.weekNumbers = false;
		calendar = cal;                  // remember it in the global var
		cal.setRange(1900, 2070);        // min/max year allowed.
		cal.create();
	}
	
	calendar.setDateFormat(format);      // set the specified date format
	calendar.parseDate(el.value);        // try to parse the text in field
	calendar.sel = el;                   // inform it what input field we use
	if (typeof(canMove) == 'undefined' || (typeof(canMove) == 'boolean' && !canMove)) {
		calendar.dragging = true;        // 不允许拖动该日历
	}

    /**
     * the reference element that we pass to showAtElement is the button that
     * triggers the calendar.  In this example we align the calendar bottom-right
     * to the button.
     */
    // calendar.showAtElement(el.nextSibling, "Br");        // show the calendar old
    var hv = (typeof(_dir) == 'undefined' || typeof(_dir) != 'string') ? 'Bl' : _dir;
    calendar.showAtElement(el, hv);      // show the calendar by 马必强

    return false;
}

var MINUTE = 60 * 1000;
var HOUR = 60 * MINUTE;
var DAY = 24 * HOUR;
var WEEK = 7 * DAY;

/**
 * If this handler returns true then the "date" given as
 * parameter will be disabled.  In this example we enable
 * only days within a range of 10 days from the current date.
 * You can use the functions date.getFullYear() -- returns the year
 * as 4 digit number, date.getMonth() -- returns the month as 0..11,
 * and date.getDate() -- returns the date of the month as 1..31, to
 * make heavy calculations here.  However, beware that this function
 * should be very fast, as it is called for each day in a month when
 * the calendar is (re)constructed.
 */
function isDisabled(Date)
{
	var today = new Date();
	return (Math.abs(date.getTime() - today.getTime()) / DAY) > 10;
}

function flatSelected(cal, Date)
{
	var el = document.getElementById("preview");
	el.innerHTML = date;
}

function showFlatCalendar()
{
	var parent = document.getElementById("display");
	
	// construct a calendar giving only the "selected" handler.
	var cal = new Calendar(false, null, flatSelected);
	
	// hide week numbers
	cal.weekNumbers = false;
	
	// We want some dates to be disabled; see function isDisabled above
	cal.setDisabledHandler(isDisabled);
	cal.setDateFormat("DD, M d");
	
	/**
	 * this call must be the last as it might use data initialized above; if
	 * we specify a parent, as opposite to the "showCalendar" function above,
	 * then we create a flat calendar -- not popup.  Hidden, though, but...
	 */
	cal.create(parent);
	
	// ... we can show it here.
	cal.show();
}