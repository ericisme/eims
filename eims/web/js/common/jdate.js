<!--
/**
 * @Title : JS中的日期处理。
 * @Author : 马必强 
 * @Date : 2006-10-09
 */

/**
 * 构造函数
 */
JDate = function ()
{
	this.YEAR = 1970;
	this.MONTH = 0;
	this.DAY_OF_MONTH = 1;
	this.HOUR_OF_DAY = 0;
	this.MINUTE = 0;
	this.SECOND = 0;
	
	this.setDate(new Date());
};

/**
 * 设置日期。该日期的约束和compare方法中的一致，即参数可以是Date或JDate
 * 对象，且月份都是从0开始计算知道11。不过一般都会使用Date对象，因为
 * Date对象可以自动对日期进行调整，比如我设置new Date(2007,8,32),那么被
 * 调整后就自动变为2007-9-1了，因为8月没有32号！而设置JDate进来则没有自动
 * 调整的功能，这点需要注意！
 */
JDate.prototype.setDate = function (dt)
{
	if (dt == null || typeof(dt) != 'object') return false;
	this.YEAR = dt.getFullYear();
	this.MONTH = dt.getMonth();
	this.DAY_OF_MONTH = dt.getDate();
	this.HOUR_OF_DAY = dt.getHours();
	this.MINUTE = dt.getMinutes();
	this.SECOND = dt.getSeconds();
};

/**
 * 比较该日期和指定日期的大小。注意设置进来的参数是Date对象或JDate对象，
 * 且new Date(year,month,day)时中的month与Date的约束一样，都是从0开始
 * 计算月份，而不是从1开始。即new Date(2007,4,5)表示的2007年的5月5号，
 * 而不是字面意思所表述的2007年4月5号。
 
 * 返回 1 - 大于指定的日期   0 - 等于指定的日期   -1 - 小于指定的日期
 */
JDate.prototype.compare = function (dt)
{
	var returnVal = this.compareField(this.YEAR, dt.getFullYear());
	if (returnVal != 0) return returnVal;
	
	returnVal = this.compareField(this.MONTH, dt.getMonth()-1);
	if (returnVal != 0) return returnVal;
	
	returnVal = this.compareField(this.DAY_OF_MONTH, dt.getDate());
	if (returnVal != 0) return returnVal;
	
	returnVal = this.compareField(this.HOUR_OF_DAY, dt.getHours());
	if (returnVal != 0) return returnVal;
	
	returnVal = this.compareField(this.MINUTE, dt.getMinutes());
	if (returnVal != 0) return returnVal;
	
	returnVal = this.compareField(this.SECOND, dt.getSeconds());
	return returnVal;
};

/**
 * 判断是否在指定的日期之前或之后或相等
 */
JDate.prototype.isAfter = function(dt) {
	return this.compare(dt) == 1;
}
JDate.prototype.isBefore = function(dt) {
	return this.compare(dt) == -1;
}
JDate.prototype.isEqual = function(dt) {
	return this.compare(dt) == 0;
}

/**
 * 比较相同字域的大小，比如年、月等。内部方法
 */
JDate.prototype.compareField = function (field1, field2)
{
	return field1 > field2 ? 1 : (field1 < field2 ? -1 : 0);
};

/**
 * 获取年份
 */
JDate.prototype.getFullYear = function()
{
	return this.YEAR;
};

/**
 * 获取月份
 */
JDate.prototype.getMonth = function()
{
	return this.MONTH + 1;
};

/**
 * 获取日期
 */
JDate.prototype.getDate = function()
{
	return this.DAY_OF_MONTH;
};

/**
 * 获取时
 */
JDate.prototype.getHours = function()
{
	return this.HOUR_OF_DAY;
};

/**
 * 获取分
 */
JDate.prototype.getMinutes = function()
{
	return this.MINUTE;
};

/**
 * 获取秒
 */
JDate.prototype.getSeconds = function()
{
	return this.SECOND;
};

/**
 * 返回日期的字符串表现形式.
 * format 格式字符串
 *        yy 或 yyyy 表示年
 *        m  或 mm   表示月
 *        d  或 dd   表示日
 *        h  或 hh   表示小时(12小时制)   hh24  表24小时制小时
 *        M  或 MM   表示分钟
 *        s  或 ss   表示秒钟
 */
JDate.prototype.toString = function (format)
{
	var str = new String(format);
	var fields = format.split(/\W+/);
	var s = new Array(), tmp;
	for (var i = 0; i < fields.length; i ++) {
		if (fields[i] == 'yy') {
			s['yy'] = new String(this.YEAR).substring(2);
		} else if(fields[i] == 'yyyy') {
			s['yyyy'] = this.YEAR;
		} else if(fields[i] == 'm') {
			s['m'] = this.MONTH + 1;
		} else if(fields[i] == 'mm') {
			tmp = this.MONTH + 1;
			s['mm'] = tmp < 10 ? ('0' + tmp) : tmp;
		} else if(fields[i] == 'd') {
			s['d'] = this.DAY_OF_MONTH;
		} else if(fields[i] == 'dd') {
			tmp = this.DAY_OF_MONTH;
			s['dd'] = tmp < 10 ? ('0' + tmp) : tmp;
		} else if(fields[i] == 'h') {
			tmp  = this.HOUR_OF_DAY;
			s['h'] = tmp > 12 ? (tmp - 12) : tmp;
		} else if(fields[i] == 'hh') {
			tmp  = this.HOUR_OF_DAY;
			s['hh'] = tmp < 10 ? ('0' + tmp) : (tmp > 12 ? (tmp - 12) : tmp);
		} else if(fields[i] == 'hh24') {
			tmp  = this.HOUR_OF_DAY;
			s['hh24'] = tmp < 10 ? ('0' + tmp) : tmp;
		} else if(fields[i] == 'M') {
			s['M'] = this.MINUTE;
		} else if(fields[i] == 'MM') {
			tmp = this.MINUTE;
			s['MM'] = tmp < 10 ? ('0' + tmp) : tmp;
		} else if(fields[i] == 's') {
			s['s'] = this.SECOND;
		} else if(fields[i] == 'ss') {
			tmp = this.SECOND;
			s['ss'] = tmp < 10 ? ('0' + tmp) : tmp;
		}
	}
	var re = /(.*)(\W|^)(yy|yyyy|m|mm|d|dd|h|hh|hh24|M|MM|s|ss)(\W|$)(.*)/;
	while (re.exec(str) != null) {
		str = RegExp.$1 + RegExp.$2 + s[RegExp.$3] + RegExp.$4 + RegExp.$5;
	}
	return str;
};
-->