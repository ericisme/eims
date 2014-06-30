//选择带有"_index"的div进行<option>过滤
function selectOptionByDiv(){
	var divs = document.getElementsByTagName('DIV');
	var form_id = "";
	for (var i = 0; i < divs.length; i ++) {
		if(divs[i].id.indexOf('_index') != -1){
			form_id = divs[i].id;
		}
	}
	updateOptionStr(form_id, "-1", "--所有--");
}

//由镇区获取机构的下拉列表
function changeZhptAgencyIndex(city_id,name1,innerName1,name2,innerName2)
{
	//alert("haha");
	new Ajax.Request(
		'/library/common.do?action=getAgencySelect&city_id='+city_id,
			{
				method : 'POST',
				evalScripts : true,
				parameters : '',
				onComplete : function(xmlHttp,error){
					var returnVal = xmlHttp.responseText;
					$(innerName1).innerHTML = "<select name=\""+name1+"\" id=\""+name1+"\" onchange=\"changeZhptSchoolIndex(this.value,'"+name2+"','"+innerName2+"');\">"+returnVal+"</select>";
					//selectOptionByDiv();
				}
			}
		);
}
//由机构获取学校的下拉列表 
function changeZhptSchoolIndex(agency_id,name1,innerName1)
{	
	new Ajax.Request(
		'/library/common.do?action=getSchoolSelect&agency_id='+agency_id,
		{
			method : 'POST',
			evalScripts : true,
			parameters : '',
			onComplete : function(xmlHttp,error){
				var returnVal = xmlHttp.responseText;
				$(innerName1).innerHTML = "<select name=\""+name1+"\" id=\""+name1+"\" >"+returnVal+"</select>";
			  	//selectOptionByDiv();
			}
		}
	);
}
//由学校获取年级的下拉列表
function changeZhptGrade(school_id, name, innerName)
{	
	new Ajax.Request(
		'/library/zfptclass.do?action=getGradeSelectHtmlByChangeSchool&school_id='+school_id,
		{
			method : 'POST',
			evalScripts : true,
			parameters : '',
			onComplete : function(xmlHttp,error){
				var returnVal = xmlHttp.responseText;
				if(innerName.indexOf("edit") != -1){
					$(innerName).innerHTML = "<select name=\""+name+"\" onchange=\"changeClassName(this.value,'editClassNameId', 'editClassName')\"><option value=\"-1\">--请选择--</option>"+returnVal+"</select>";
					$('editClassName').innerHTML = "<select name=\"editClassNameId\" id=\"editClassNameId\"><option value=\"-1\">--请选择--</option></select>";
				}
				else if(innerName.indexOf("import") != -1)
					$(innerName).innerHTML = "<select name=\""+name+"\" onchange=\"changeClassName(this.value,'importClassId', 'importClass')\"><option value=\"-1\">--请选择--</option>"+returnVal+"</select>";
				else if(innerName.indexOf("query") != -1)
					$(innerName).innerHTML = "<select name=\""+name+"\" onchange=\"changeClassName(this.value,'queryClassNameId', 'queryClassName')\"><option value=\"-1\">--请选择--</option>"+returnVal+"</select>";
				else if(innerName!='')
					$(innerName).innerHTML = "<select name=\""+name+"\"><option value=\"-1\">--请选择--</option>"+returnVal+"</select>";
				selectOptionByDiv();
					
			}
		}
	);
}
//由年级获取班级号的下拉列表
function changeClassCode(orderGrade, name, innerName){
	var param = "";
	if(innerName.indexOf("edit") != -1)
		param = 'editSchoolId';
	else if(innerName.indexOf("import") != -1)
		param = 'importSchoolId';
	new Ajax.Request(
		'/library/zfptclass.do?action=getClassCodeSelectHtmlByChangeSchoolOrGrade&orderGrade='+orderGrade+'&school_id='+$F(param),
		{
			method : 'POST',
			evalScripts : true,
			parameters : '',
			onComplete : function(xmlHttp,error){
				var returnVal = xmlHttp.responseText;
				$(innerName).innerHTML = "<select name=\""+name+"\"><option value=\"-1\">--请选择--</option>"+returnVal+"</select>";
				selectOptionByDiv();
			}
		}
	);
};
//由年级获取班级名称的下拉列表
function changeClassName(orderGrade, name, innerName){
	var param = "";
	var param2 = "";
	if(innerName.indexOf("edit") != -1){
		param = 'editSchoolId';
		param2 = 'editClassNameId';
	}
	else if(innerName.indexOf("import") != -1){
		param = 'importSchoolId';
		param2 = 'importClassId';
	}
	else if(innerName.indexOf("query") != -1){
		param = 'querySchoolId';
		param2 = 'queryClassNameId';
	}
	var qv = "";
	if($('student_index') != null )
		qv += "&student_index=1";
	new Ajax.Request(
		'/library/zfptclass.do?action=getClassNameSelect&orderGrade='+orderGrade+'&school_id='+$F(param)+qv,
		{
			method : 'POST',
			evalScripts : true,
			parameters : '',
			onComplete : function(xmlHttp,error){
				var returnVal = xmlHttp.responseText;
				$(innerName).innerHTML = "<select name=\""+name+"\"><option value=\"-1\">--请选择--</option>"+returnVal+"</select>";
				selectOptionByDiv();
			}
		}
	);
};