<!--
/**
 * @Title : JS�е����ڴ���
 * @Author : ���ǿ 
 * @Date : 2006-10-09
 */

/**
 * ���캯��
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
 * �������ڡ������ڵ�Լ����compare�����е�һ�£�������������Date��JDate
 * �������·ݶ��Ǵ�0��ʼ����֪��11������һ�㶼��ʹ��Date������Ϊ
 * Date��������Զ������ڽ��е���������������new Date(2007,8,32),��ô��
 * ��������Զ���Ϊ2007-9-1�ˣ���Ϊ8��û��32�ţ�������JDate������û���Զ�
 * �����Ĺ��ܣ������Ҫע�⣡
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
 * �Ƚϸ����ں�ָ�����ڵĴ�С��ע�����ý����Ĳ�����Date�����JDate����
 * ��new Date(year,month,day)ʱ�е�month��Date��Լ��һ�������Ǵ�0��ʼ
 * �����·ݣ������Ǵ�1��ʼ����new Date(2007,4,5)��ʾ��2007���5��5�ţ�
 * ������������˼��������2007��4��5�š�
 
 * ���� 1 - ����ָ��������   0 - ����ָ��������   -1 - С��ָ��������
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
 * �ж��Ƿ���ָ��������֮ǰ��֮������
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
 * �Ƚ���ͬ����Ĵ�С�������ꡢ�µȡ��ڲ�����
 */
JDate.prototype.compareField = function (field1, field2)
{
	return field1 > field2 ? 1 : (field1 < field2 ? -1 : 0);
};

/**
 * ��ȡ���
 */
JDate.prototype.getFullYear = function()
{
	return this.YEAR;
};

/**
 * ��ȡ�·�
 */
JDate.prototype.getMonth = function()
{
	return this.MONTH + 1;
};

/**
 * ��ȡ����
 */
JDate.prototype.getDate = function()
{
	return this.DAY_OF_MONTH;
};

/**
 * ��ȡʱ
 */
JDate.prototype.getHours = function()
{
	return this.HOUR_OF_DAY;
};

/**
 * ��ȡ��
 */
JDate.prototype.getMinutes = function()
{
	return this.MINUTE;
};

/**
 * ��ȡ��
 */
JDate.prototype.getSeconds = function()
{
	return this.SECOND;
};

/**
 * �������ڵ��ַ���������ʽ.
 * format ��ʽ�ַ���
 *        yy �� yyyy ��ʾ��
 *        m  �� mm   ��ʾ��
 *        d  �� dd   ��ʾ��
 *        h  �� hh   ��ʾСʱ(12Сʱ��)   hh24  ��24Сʱ��Сʱ
 *        M  �� MM   ��ʾ����
 *        s  �� ss   ��ʾ����
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