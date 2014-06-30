
function checkMoney(checkStr)
{
	var checkOK = "0123456789.";
	var allValid = true;
	for (i=0;i<checkStr.length;i++)
	{
	  ch=checkStr.charAt(i);
	  for(j=0;j<checkOK.length;j++)
	  if(ch==checkOK.charAt(j))
	    break;
	  if(j==checkOK.length)
	   {
	    allValid = false;
	    break;
	   }
	}
        var j=0;
        for(i=0;i<checkStr.length;i++){
          if(checkStr.charAt(i)=="."){
             j++;
          }
        }
        if(j>1) return false;
        if(checkStr.indexOf(".")!=-1&&(checkStr.indexOf(".")<1||checkStr.indexOf(".")>=checkStr.length-1)){
           return false;
        }
        if(checkStr.indexOf("0")==0&&checkStr.indexOf(".")!=1)return false;
	return allValid;
}
function checkNumbers(checkStr) {
        var checkOK = "0123456789";
	var allValid = true;
	ch=checkStr.charAt(0);
	if(ch != "-") return false;
	for (i=1;i<checkStr.length;i++)
	{
	  ch=checkStr.charAt(i);
	  for(j=0;j<checkOK.length;j++)
	  if(ch==checkOK.charAt(j))
	    break;
	  if(j==checkOK.length)
	   {
	    allValid = false;
	    break;
	   }
	}
	if(checkStr.length>1&&checkStr.indexOf("0")==1) return false;
	return allValid;
}

function checkAreaNumber(checkStr) {
        var checkOK = "0123456789";
	var allValid = true;
	for (i=0;i<checkStr.length;i++)
	{
	  ch=checkStr.charAt(i);
	  for(j=0;j<checkOK.length;j++)
	  if(ch==checkOK.charAt(j))
	    break;
	  if(j==checkOK.length)
	   {
	    allValid = false;
	    break;
	   }
	}
	if(checkStr.length<3) return false;
	return allValid;
}

function checkNumber(checkStr) {
        var checkOK = "0123456789";
	var allValid = true;
	for (i=0;i<checkStr.length;i++)
	{
	  ch=checkStr.charAt(i);
	  for(j=0;j<checkOK.length;j++)
	  if(ch==checkOK.charAt(j))
	    break;
	  if(j==checkOK.length)
	   {
	    allValid = false;
	    break;
	   }
	}
	if(checkStr.indexOf("0")==0)return false;
	return allValid;
}
//检查是否为查询语句
function checkselect(selectstr){
	var check1="select ";
	var check2=" from ";
	var allValid = false;
	var upValid = false;
	for(i=0;i<selectstr.length-6;i++){
		if(check1==selectstr.substring(i,i+7)){
			allValid= true;
			break;
		}
	}
	for(i=6;i<selectstr.length-4;i++){
		if(check2==selectstr.substring(i,i+6)){
			upValid= true;
			break;
		}
	}
	return allValid*upValid;
}

function isPosInteger(e){ 
  var i=0;
  for(i=0;i<e.length;i++){
    var oneChar=e.charAt(i);
    if(oneChar<'0'||oneChar>'9'){
      return false;
    }
    }
  return true;
}

//检查是否是电话号码
function checkPhone(checkStr) {
        var checkOK = "0123456789()-";
	var allValid = true;
	for (i=0;i<checkStr.length;i++)
	{
	  ch=checkStr.charAt(i);
	  for(j=0;j<checkOK.length;j++)
	  if(ch==checkOK.charAt(j))
	    break;
	  if(j==checkOK.length)
	   {
	    allValid = false;
	    break;
	   }
	}
	return allValid;
}

//检查登陆帐号是否合法
function isValidUserID(checkStr)
{
  var re= new RegExp("^\\w+$");
  var isValid=true;
  if(checkStr.search(re)==-1) {
    isValid=false;
  } 
  return isValid;
}
//检查邮件地址合法性
function checkMail(s)
{
if (s.length > 100)    return false;
    if (s.indexOf("'")!=-1) return false;
    var regu = "^(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*[_.0-9a-zA-Z]+))@([a-zA-Z0-9-]+[.])+(.+)$";
    var re = new RegExp(regu);
    if (s.search(re) != -1)
        return true;
    else
        return false;
}

//比较日期大小不能相等
function compareDate(date1,date2){
    var aryDate1 = date1.split("-",3);
	var aryDate2 = date2.split("-",3);
	var result = false;
	for(i=0;i < aryDate1.length;i++){
		if(1 * aryDate1[i] > 1 * aryDate2[i]){
			result = false;
			break;
		}
		if(1 * aryDate1[i] < 1 * aryDate2[i]){
			result = true;
			break;
		}
	}
	return result;
}
//比较日期大小可以相等 
function compareDate1(date1,date2){
    var aryDate1 = date1.split("-",3);
	var aryDate2 = date2.split("-",3);
	var result = false;
	for(i=0;i < aryDate1.length-1;i++){
		if(1 * aryDate1[i] > 1 * aryDate2[i]){
			result = false;
			break;
		}
		if(1 * aryDate1[i] < 1 * aryDate2[i]){
			result = true;
			break;
		}
	}
	if(1 * aryDate1[2] <= 1 * aryDate2[2]){
			result = true;
	}
	return result;
}
//比较日期相等 
function compareDate2(date1,date2){
    var aryDate1 = date1.split("-",3);
	var aryDate2 = date2.split("-",3);
	var result = false;
	for(i=0;i < aryDate1.length-1;i++){
		if(1 * aryDate1[i] >1 * aryDate2[i]){
			result = false;
			break;
		}
		if(1 * aryDate1[i] < 1 * aryDate2[i]){
			result = false;
			break;
		}
	}
	if(1 * aryDate1[2] == 1 * aryDate2[2]){
			result = true;
	}
	return result;
}
//与当前日期比较
function checkDate(date){
    var aryDate = date.split("-",3);
	var year = aryDate[0];
	var month = aryDate[1];
	var day = aryDate[2];
	var checkDate = new Date(year,month-1,day);
	var nowDate = new Date();
//	var nowYear = nowDate.getYear();
//	var nowMonth = nowDate.getMonth()+1;
//	var nowDay = nowDate.getDate();
//	if(year==nowYear&&month==nowMonth&&day==nowDay){
//	  return false;
//	}
	
	var checkTime = checkDate.getTime();
	var nowTime = nowDate.getTime();
	if(checkTime > nowTime){
		return false;
	}else{
		return true;
	}
}

//与SCP系统上线日期比较;
function checkUpDate(date){
    var aryDate = date.split("-",3);
	var year = aryDate[0];
	var month = aryDate[1];
	var day = aryDate[2];
	var checkDate = new Date(year,month,day);
	var upDate = new Date(2005,12,17);
	var checkTime = checkDate.getTime();
	var upTime = upDate.getTime();
	if(checkTime < upTime){
		return false;
	}else{
		return true;
	}
}

//检查手机号码是否正确
function checkMobile(checkStr) {
        var checkOK = "0123456789";
	var allValid = true;
	if(checkStr.indexOf("0")==0||checkStr.length!=11)return false;
	for (i=0;i<checkStr.length;i++)
	{
	  ch=checkStr.charAt(i);
	  for(j=0;j<checkOK.length;j++)
	  if(ch==checkOK.charAt(j))
	    break;
	  if(j==checkOK.length)
	   {
	    allValid = false;
	    break;
	   }
	}
       
	return allValid;
}

//检查手机号码是否以86开头
function checkGsmMobile(checkStr) {
        var checkOK = "0123456789";
		var cherkNO = "86";
	var allValid = true;
	var check = checkStr.substring(0,2);
	if(check!="86") return false;
	for (i=2;i<checkStr.length;i++)
	{
	  ch=checkStr.charAt(i);
	  for(j=0;j<checkOK.length;j++)
	  if(ch==checkOK.charAt(j))
	    break;
	  if(j==checkOK.length)
	   {
	    allValid = false;
	    break;
	   }
	}
	
       if(checkStr.length!=13)return false;
	return allValid;
}

//检查手机号码段是否正确
function check_numMobile(checkStr) {
        var checkOK = "0123456789";
	var allValid = true;
	for (i=0;i<checkStr.length;i++)
	{
	  ch=checkStr.charAt(i);
	  for(j=0;j<checkOK.length;j++)
	  if(ch==checkOK.charAt(j))
	    break;
	  if(j==checkOK.length)
	   {
	    allValid = false;
	    break;
	   }
	}
       if(checkStr.length<6||checkStr.length>11)
	   allValid =  false;
	return allValid;
}

//检查复选框选中个数
function checkBox(theform,boxName){
    var i=0;
	var j=0;
    for(i=0;i<theform.elements.length;i++){
	  if(theform.elements[i].name==boxName&&theform.elements[i].checked==true){
         j++;
	  }
	}
    return j;
  }

//检查输入的是否是整数
function isNumber(checkStr){
  var checkOK = "0123456789";
  var allValid = true;
  for (i=0;i<checkStr.length;i++)
   {
	  ch=checkStr.charAt(i);
	  for(j=0;j<checkOK.length;j++)
	  if(ch==checkOK.charAt(j))
	    break;
	  if(j==checkOK.length)
	   {
	    allValid = false;
	    break;
	   }
   }
   if(checkStr.indexOf("0")==0)return false;
   return allValid;
}

function isId(checkStr){
  var checkOK = "0123456789xX";
  var allValid = true;
  for (i=0;i<checkStr.length;i++)
   {
	  ch=checkStr.charAt(i);
	  for(j=0;j<checkOK.length;j++)
	  if(ch==checkOK.charAt(j))
	    break;
	  if(j==checkOK.length)
	   {
	    allValid = false;
	    break;
	   }
   }
   return allValid;
}
//检查税率

function checkTax(checkStr)
{
	var checkOK = "0123456789.";
	var allValid = true;
	for (i=0;i<checkStr.length;i++)
	{
	  ch=checkStr.charAt(i);
	  for(j=0;j<checkOK.length;j++)
	  if(ch==checkOK.charAt(j))
	    break;
	  if(j==checkOK.length)
	   {
	    allValid = false;
	    break;
	   }
	}
        var j=0;
        for(i=0;i<checkStr.length;i++){
          if(checkStr.charAt(i)=="."){
             j++;
          }
        }
        if(j>1) return false;
        if(checkStr.indexOf(".")!=-1&&(checkStr.indexOf(".")<1||checkStr.indexOf(".")>=checkStr.length-1)){
           return false;
        }
	return allValid;
}


//textarea字数提示
function NoteHint(F,obj){
	s=obj.value;
	len=s.length;
	count=0;
	var ch=0;
	for(i=0;i<len;i++){
		ch=s.charCodeAt(i)&0xffff;
		if(ch<0xff){
			count++;
		}
		else{
			count+=2;
		}
	}
	num.innerHTML=count;
}

//限制输入最多两位小数
function lmtDigit(obj) {
  var allValid=true;
  var s=obj.value;
  if(s.indexOf(".")!=-1) {
    if(s.substring(s.indexOf(".")+1).length>2)
		allValid=false;
  }
  return allValid;
}

//查看是否是jpg图片
function checkJpg(checkStr){
	var allValid=true;
	if(checkStr.indexOf(".jpg")!=-1){
		allValid=false;
		  }
  return allValid;
}

//查看是否是SQL语句
function checkSql(checkStr){
	var allValid=true;
	if(checkStr.indexOf("select")!=-1&&checkStr.indexOf("from")!=-1){
		allValid=false;
		  }
  return allValid;
}

function checkInt(checkStr) {
        var checkOK = "0123456789";
		var checkNO = "-0123456789";
	var allValid = true;
	sh=checkStr.charAt(0);
	for(j=0;j<checkNO.length;j++){
	    if(sh==checkNO.charAt(j))
				break;
	}
	var check = checkStr.substring(1,checkStr.length);	
	for (i=0;i<check.length;i++)
	{
	  ch=check.charAt(i);
	  for(j=0;j<checkOK.length;j++)
	  if(ch==checkOK.charAt(j))
	    break;
	  if(j==checkOK.length)
	   {
	    allValid = false;
	    break;
	   }
	}
	if(checkStr.indexOf("0")==0)return false;
	return allValid;
}

function checkBlackNumbers(checkStr) {
        var checkOK = "0123456789";
	var allValid = true;
	for (i=0;i<checkStr.length;i++)
	{
	  ch=checkStr.charAt(i);
	  for(j=0;j<checkOK.length;j++)
	  if(ch==checkOK.charAt(j))
	    break;
	  if(j==checkOK.length)
	   {
	    allValid = false;
	    break;
	   }
	}
	if(!(checkStr.length==11 || checkStr.length==13)) return false;
	return allValid;
}
//号段统计 判断textnum
function checkTextNumber(checkStr) {
    var checkOK = "0123456789, \r\n";
	var allValid = true;
	for (i=0;i<checkStr.length;i++)
	{
	  ch=checkStr.charAt(i);
	  for(j=0;j<checkOK.length;j++)
	  if(ch==checkOK.charAt(j))
	    break;
	  if(j==checkOK.length)
	   {
	    allValid = false;
	    break;
	   }
	}
	return allValid;
}