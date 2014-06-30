function onUse(aim,src)
{
  if(src.value!="") aim.disabled=false;
  else aim.disabled=true;
}

function doGo(url,ColId,id){
  var enpId;
  enpId=form1.EnpId.value;
  if(enpId!="")
  {
   location.href=url+"?EnpId="+enpId+"&ColId="+ColId+"&id="+id;
   }
}


function SelCol(theform,theurl)
  {
   theform.action=theurl;
   theform.submit();
  }
function ShowPic(PicPath,ThePic)
{
  box = eval(ThePic);
  if(PicPath.length==0) return;
  box.src=PicPath;
  box.style.display="";
}
function ShowPic1(PicPath,ThePic)
{
  box = eval(ThePic);
  if(PicPath.length==0) 
    {
	box.style.display="none";
	return;
	}
  box.src="../../written/upfile/gykw/"+PicPath;
  box.style.display="";
}

 function caidan(id)
{
 if (id.style.display!="")
   {
    id.style.display="";
   }
 else
 {
    id.style.display="none";
  }
}
  function hid(id)
{
    id.style.display="none";
}

  function show(id)
{
    id.style.display="";
}
function textCounter(field, countfield, maxlimit) {
if (field.value.length >maxlimit) 
field.value = field.value.substring(0,maxlimit);
countfield.value = field.value.length;
}
//?衧2o?oy
function selectDate(objID,theform)
{
	var obj = eval('document.'+theform+'.'+objID);
	result = window.showModalDialog('js/Calendar.htm',obj.value,'dialogWidth=205px;dialogHeight=235px');
	if (result!=null)
	{
		obj.value = result;
	}


}
//选中所有复选框
function selectAll(form)
  {
  for (var i=0;i<form.elements.length;i++)
    {
    var e = form.elements[i];
    if (e.name != 'chkall' )
       e.checked = form.chkall.checked;
    }
  }


function SelectData(source,aim,theData,limit2){
  var i=0;
  var tempstr="";
  var tempValue="";
  for(i=0;i<source.length;i++)
  {
    if(source[i].selected==true)
	  {
       tempstr = source[i].value;
	   tempValue=source[i].value;
       break;
       } 
  }
  if(tempstr!="")
  {
    var oOption = document.createElement("OPTION");
    aim.length=0;
	var j=0;
    for(i=0;i<theData.length;i++)
	  {
		if(i==0&&limit2==1){
         oOption = document.createElement("OPTION");
         oOption.text="";
         oOption.value="";
         aim.options.add(oOption);					
		}
        if(theData[i].substring(0,tempstr.length)==tempstr)
		  {
			j++;
		  	var temp=theData[i].substring(tempstr.length);
              oOption = document.createElement("OPTION");
              oOption.text=temp;
              oOption.value=temp;
              aim.options.add(oOption);
		  }
	  }
	if(j==0){
       oOption = document.createElement("OPTION");
       oOption.text="";
       oOption.value="";
       aim.options.add(oOption);
	}
  }
  else {
	   aim[0].selected=true;
	}
  return true;
}


//选择刮卡类型，得到对应的面值,theData数组值形式为X-YYY-YYY
function SelectDataValue(source,aim,theData,limit2){
  form1.fillprice.value="";
  var i=0;
  var tempstr="";
  for(i=0;i<source.length;i++)
  {
    if(source[i].selected==true)
	  {
       tempstr = source[i].value;
       break;
       } 
  }
  if(tempstr!="")
  {
    var oOption = document.createElement("OPTION");
    aim.length=0;
	var j=0;
    for(i=0;i<theData.length;i++)
	  {
		if(i==0&&limit2==1){
         oOption = document.createElement("OPTION");
         oOption.text="";
         oOption.value="";
         aim.options.add(oOption);					
		}
        if(theData[i].substr(0,1)==tempstr)
		  {
			j++;
		  	var temp=theData[i].substring(1,theData[i].indexOf(","));
              oOption = document.createElement("OPTION");
              oOption.text=temp;
              oOption.value=temp;
              aim.options.add(oOption);
		  }
	  }
	if(j==0){
       oOption = document.createElement("OPTION");
       oOption.text="";
       oOption.value="";
       aim.options.add(oOption);
	}
  }
  else {
	   aim[0].selected=true;
	}
  return true;
}

//下拉框选择一个值，在另一个文本输入框产生对应代码
function SelectData1(source,aim,theData,limit2){
  var i=0;
  var tempstr="";
  var tempValue="";
  for(i=0;i<source.length;i++)
  {
    if(source[i].selected==true)
	  {
       tempstr = source[i].value;
	   tempValue=source[i].value;
       break;
       } 
  }
  if(tempstr!="") {
    for(i=0;i<theData.length;i++)
	  {
        if(theData[i].substring(0,tempstr.length)==tempstr)
		  {
			aim.value=theData[i].substring(tempstr.length);
			break;
		  }
	  }
  
  } else {
      aim.value="";
  }
  return true;
}



//选择营业厅
function selected_additem(theform,source,aim,length){
  itemname=source.options[source.selectedIndex].text;
  var option=document.createElement("OPTION");
  option.text=itemname;
  option.value=source.options[source.selectedIndex].value;
  if (aim.length<=length) {
    duplicate=false;
    for(i=0;i<=aim.length-1;i++){
	  if(aim.options[i].value==option.value){
	   duplicate=true;
	   break;
      }
    }
    if (duplicate==false) aim.add(option);
  }
}

//移除营业厅
function selected_removeitem(aim){
	if (aim.selectedIndex>0)
		aim.remove(aim.selectedIndex);
}
//增加或修改用户页面移除营业厅
function selected_removeshop(aim){
	if (aim.selectedIndex>=0)
		aim.remove(aim.selectedIndex);
}



//取得下拉菜单的值,从第i项开始
function allSelectValue(source,i){
  var result="";
  for(i=i;i<source.length-1;i++){
	result += source[i].value+",";
  }
  result += source[source.length-1].value;
  return result;
}


//服务厅对应铺卡点
function SelectDataPc(source,aim,theData,limit2){
  var i=0;
  var tempstr="";
  var tempValue="";
  for(i=0;i<source.length;i++)
  {
    if(source[i].selected==true)
	  {
       tempstr = source[i].value;
	   tempValue=source[i].value;
       break;
       } 
  }
  if(tempstr!="")
  {
    var oOption = document.createElement("OPTION");
    aim.length=0;
	var j=0;
    for(i=0;i<theData.length;i++)
	  {
		if(i==0&&limit2==1){
         oOption = document.createElement("OPTION");
         oOption.text="";
         oOption.value="";
         aim.options.add(oOption);					
		}
        if(theData[i].substring(0,tempstr.length)==tempstr)
		  {
			j++;
		  	var tempText=theData[i].substring(tempstr.length,theData[i].indexOf(","));
			var tempValue=theData[i].substring(theData[i].indexOf(",")+1);
              oOption = document.createElement("OPTION");
              oOption.text=tempText;
              oOption.value=tempValue;
              aim.options.add(oOption);
		  }
	  }
	if(j==0){
       oOption = document.createElement("OPTION");
       oOption.text="";
       oOption.value="";
       aim.options.add(oOption);
	}
  }
  else {
	   aim[0].selected=true;
	}
  return true;
}

//得到复选框的值
  function getCheckBox(theform,boxName){
    var i=0;
	var checkValue="";
    for(i=0;i<theform.length;i++){
	  if(theform.elements[i].name==boxName&&theform.elements[i].checked==true){
	    if(checkValue==""){
		   if(theform.elements[i].value!="") checkValue=theform.elements[i].value;
		}else{
		   checkValue=checkValue+","+theform.elements[i].value;
		}
	  }
	}
	return checkValue;
  }




//获取营业厅对应用户帐号
  function SelectData2(source,aim,theData,limit2){
  var i=0;
  var tempstr="";
  var tempValue="";
  for(i=0;i<source.length;i++)
  {
    if(source[i].selected==true)
	  {
       tempstr = source[i].value;
	   tempValue=source[i].value;
       break;
       } 
  }
  if(tempstr!="")
  {
    var oOption = document.createElement("OPTION");
    aim.length=0;
	var j=0;
    for(i=0;i<theData.length;i++)
	  {
		if(i==0&&limit2==1){
         oOption = document.createElement("OPTION");
         oOption.text="";
         oOption.value="";
         aim.options.add(oOption);
		 var pattern=new RegExp(tempstr+",");
		}//管理员帐号所属于营业厅字段为空，所以要判断null的情况
        if(theData[i].match(pattern) || theData[i].substring(0,4)=="null" )
		  {
			j++;
		  	var temp="";
			var tempv="";
			var startIndex="";
			var endIndex="";
			if(theData[i].substring(0,4)=="null") {
				temp=theData[i].substring(theData[i].indexOf("*")+1);
				tempv="0";
			}
			  else { 
			    temp=theData[i].substring(theData[i].indexOf("*")+1);
				startIndex=theData[i].lastIndexOf(",")+1;
				endIndex=theData[i].indexOf("*");
				tempv=theData[i].substring(startIndex,endIndex);
			  }
              oOption = document.createElement("OPTION");
              oOption.text=temp;
              oOption.value=tempv;
			  
              aim.options.add(oOption);
		  }
	  }
	if(j==0){
       oOption = document.createElement("OPTION");
       oOption.text="";
       oOption.value="";
       aim.options.add(oOption);
	}
  }
  else {
	   aim[0].selected=true;
	}
  return true;
}



//分组批发页面选择面值显示对应批发价
function SelectDataPrice(source1,source2,aim,theData,limit2){
  var i=0;
  var tempstr="";
  for(j=0;j<source1.length;j++)
  {
    if(source1[j].selected==true)
	  {
       tempv = source1[j].value;
       break;
       } 
  }
  for(i=0;i<source2.length;i++)
  {
    if(source2[i].selected==true)
	  {
       tempstr = source2[i].value;
       break;
       } 
  }
  if(tempstr!="") {
    for(i=0;i<theData.length;i++)
	  {
        if(theData[i].substring(1,tempstr.length+1)==tempstr && (tempv==theData[i].substr(0,1)))
		  {
			aim.value=theData[i].substring(theData[i].indexOf(",")+1);
			break;
		  }
	  }
  
  } else {
      aim.value="";
  }
  return true;
}

function ToggleVisible(targetID, imageID, linkImage, linkImageCollapsed)
{
	if (document.getElementById){
  		target = document.getElementById(targetID);
  		if (target.style.display == "none") {
  			target.style.display = "";
  		} else {
  			target.style.display = "none";
  		}
  		
  		if (linkImageCollapsed != "") {
  			image = document.getElementById(imageID);
  			if (target.style.display == "none") {
  				image.src = linkImageCollapsed;
  			} else {
  				image.src = linkImage;
  			}
  		}
  	}
}