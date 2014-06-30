	var pag = null;
	var baseUrl = '/library/borrow.do';
	var interId;//循环函数ID
	var readTime=0;
	
	/*读卡器操作 start*/
	var connected=false;

	function init(){
	    var obj=document.getElementById("sensor"); 
	    var ret=0;	
	    var sn='';	
		try{	
			//alert(obj);
			ret = obj.InitDevice;
			//obj.readCardSN();
			if(ret==0){
				alert('设备连接失败,请检查设备是否正常连接,或重新安装组件后再试!');			
			}else{
				//alert('设备连接成功');
				//sn=obj.ReadCardSN;
				//alert(sn);
				connected=true;			
			}	
			return connected;	
		}
		catch(e){
			alert(e.message);
			return false;
		}
		return true;
	}
	
	function readCardSN(){
		if(!connected){
			//init();
			return ;
		}
		readTime+=100;
		if(readTime == 10000){//读卡10为超时
			readTime = 0;
			alert("读卡超时");
		}
	    var obj=document.getElementById("sensor"); 
	    var sn='';
		try{	
			if(connected){			
				sn=obj.ReadCardSN;
				//sn=obj.ReadCardSNRepeat;//8秒内连续读
				if(sn && sn!=''){
					//alert('卡号:'+sn);
					borrowForm.id_card.value=sn;
					window.clearInterval(interId); 
					userSubmit();
					
					
				}else{
					//alert('读卡超时!');
				}
				
			}else{
				//alert('请先连接设备');			
			}		
		}
		catch(e){
			alert("出错:"+e.message);
			return false;
		}
		return true;
	}	
/*读卡器操作 end*/
	
	
	
	
	
	// 列表查询
	function borrowQuery() {
		if (pag == null)
			pag = new PageExe();
		var param="";
		if($F('queryCityId')!=null && $F('queryCityId')!=-1)
			param+="&queryCityId="+$F('queryCityId');
		if($F('queryAgencyId')!=null && $F('queryAgencyId')!=-1)
			param+="&queryAgencyId="+$F('queryAgencyId');
		if($F('querySchoolId')!=null && $F('querySchoolId')!=-1)
			param+="&querySchoolId="+$F('querySchoolId');
		if($F('bookIndex')!=null && $F('bookIndex')!='')
			param+="&bookIndex="+$F('bookIndex');
		if($F('borrowTime')!=null && $F('borrowTime')!='')
			param+="&borrowTime="+$F('borrowTime');
		if($F('bookName')!=null && $F('bookName')!='')
			param+="&bookName="+new BaseClass().encode($F('bookName'), true);
		if($F('userNo')!=null && $F('userNo')!='')
			param+="&userNo="+$F('userNo');
		if($F('userName')!=null && $F('userName')!='')
			param+="&userName="+new BaseClass().encode($F('userName'), true);
		if($F('bookStatus')!=null && $F('bookStatus')!='')
			param+="&bookStatus="+$F('bookStatus');
		pag.query(baseUrl + '?action=list', 'borrow_list',param);
	}
	
	
	// 分页查询方法
	function borrowJump(/* string */_url, /* int */curPage) {
		pag.query(_url + curPage, 'borrow_list');
	}
	// 删除
	function borrowDel(id) {
		var _id = base.getChecked('id', true);
		if (!id && _id.length < 1) {
			base.alert('请先选择要删除的记录！');
			return;
		}
		base.log('要删除的ID为：' + (id || _id.join(',')));
		base.confirm('您确信要删除选中的记录吗？', function() {
			pag.del(baseUrl + '?action=remove', '', 'id='
					+ (id || _id.join('&id=')), borrowQuery);
		});
	}
	
	
	function selectUser(inputId,inputName)
	{
		URL="/library/common.do?action=userselect&inputid="+inputId+"&inputname="+inputName;
		loc_x=document.body.scrollLeft+event.clientX-event.offsetX+50;
		loc_y=document.body.scrollTop+event.clientY-event.offsetY+50;
		//window.open(URL);
		
	  	window.showModalDialog(URL,self,"edge:raised;scroll:yes;status:0;help:0;resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
	}



	//去除数组重复项方法
	var clearRepeat=function(a){
            var c=[],b={};   
               
            for(var i=0;i<a.length;i++){
                    if( ! b[0+a[i]]){
                            b[a[i]]=1;   
                            c.push(a[i])
                        } 
                }
            return c;
        }


  
    
	function selectBook(inputId,inputName)
	{   
		if(borrowForm.userId.value==""){
			alert("请先输入读者信息！");
			$('id_card').focus();
			return;
		}
		var book_limit= borrowForm.book_limit.value;
		var unreturn= borrowForm.unreturn.value;
		var bookIds= borrowForm.bookIds.value;
		var bookArr = bookIds.split(",");
		var booknum=clearRepeat(bookArr);   
		//alert(book_limit - unreturn - booknum.length);
		if((book_limit - unreturn - booknum.length)<0){
			alert("该读者超过了可借阅图书"+book_limit+"本限制！");
			return;
		}
			
		URL="/library/common.do?action=bookselect&inputid="+inputId+"&inputname="+inputName;
		loc_x=document.body.scrollLeft+event.clientX-event.offsetX+50;
		loc_y=document.body.scrollTop+event.clientY-event.offsetY+50;
		//window.open(URL);
		
	  	window.showModalDialog(URL,self,"edge:raised;scroll:yes;status:0;help:0;resizable:yes;dialogWidth:800px;dialogHeight:600px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
	}

    function checkBookLimit(limit,urreturn,outday){
    	var msg="";
    	if(urreturn>= limit){
    		alert("借阅图书超过了限制！");
    		msg="该读者借阅图书数超过了"+limit+"本限制！";
    	}
    		
    	if(outday>0){
    		alert("该读者有"+outday+"本过期未还！");
    		msg+="该读者有"+outday+"本过期未还！";
    	}
    	if(urreturn>= limit || outday>0){
    		interId=window.setInterval(readCardSN,100); 
    	}
    	$("userMsg").innerHTML=msg;
    	return;
    }
    

	// 列表查询
	function bookQuery() {
		var param =  "&id="+borrowForm.bookIds.value;
		if (pag == null)
			pag = new PageExe();
		pag.query('/library/common.do?action=borrowList', 'borrow_list',param);
	}
	
	function cancel(){
		borrowForm.reset();
		$('userId').value="";
		$('bookIds').value="";
		$('borrow_list').innerHTML="";
		$('userMsg').innerHTML="";
		$('id_card').focus();
		
		interId=window.setInterval(readCardSN,100); 
	}
	
	function borrowSubmit(){
		var et = $('userId');
		var param="";
		if (!et || et.value.strip() == '') {
			base.alert('请输入读者信息！', function() {
				$('id_card').focus();
			});
			return false;
		}
		param+="&userId="+et.value;
		et = $('day_limit');
		if (!et || et.value.strip() == '') {
			base.alert('读者借阅限制天数有误！', function() {
				et.focus();
			});
			return false;
		}
		param+="&day_limit="+et.value;
		et = $('bookIds');
		if (!et || et.value.strip() == '') {
			base.alert('请输入图书信息！', function() {
				$('bookNo').focus();
			});
			return false;
		}
		param+="&bookIds="+et.value;
		// ajax方式提交表单
			new Ajax.Request(baseUrl+'?action=borrowSubmit' + param, {
			method : 'POST',
			evalScripts : true,
			parameters : '',
			onComplete : function(xmlHttp, error) {
				var returnVal = xmlHttp.responseText;
				alert(returnVal);
				borrowForm.reset();
				$('borrow_list').innerHTML="";
				$('id_card').focus();
				interId=window.setInterval(readCardSN,100); 
			}
		});
	}
	
	function returnSubmit(){
		var et = $('bookNo');
		var param="";
		if (!et || et.value.strip() == '') {
			base.alert('请输入图书条形码信息！', function() {
				et.focus();
			});
			return false;
		}
		param+="&bookNo="+et.value;
		
		// ajax方式提交表单
			new Ajax.Request(baseUrl+'?action=returnSubmit' + param, {
			method : 'POST',
			evalScripts : true,
			parameters : '',
			onComplete : function(xmlHttp, error) {
				var returnVal = xmlHttp.responseText;
				//alert(returnVal);
				var str = returnVal.split("◎");
				if('1'==str[0]){
					document.getElementById("msg").innerHTML=str[1];
					//alert(str[1]);
					borrowForm.borrowkId.value=str[2];
					borrowForm.borrowTime.value=str[3];
					borrowForm.bookPrice.value=str[4];
					borrowForm.bookPlace.value=str[5];
					borrowForm.bookName.value=str[6];
					borrowForm.bookIndex.value=str[7];
					borrowForm.categoryName.value=str[8];
					borrowForm.bookAuthor.value=str[9];
					borrowForm.id_card.value=str[10];
					borrowForm.loginName.value=str[11];
					borrowForm.gender.value=str[12];
					borrowForm.book_limit.value=str[13];
					borrowForm.realName.value=str[14];
					borrowForm.user_type.value=str[15];
					borrowForm.unreturn.value=str[16];
					borrowForm.day_limit.value=str[17];
					//alert(str[18]);
					if(str[18]&&str[18]>0){
						document.getElementById("msg").innerHTML=str[1]+"<br>该读者超期"+str[18]+"天归还本书！！";
						alert("该读者超期"+str[18]+"天归还本书！！")
					}
						
				}else if ('0'==str[0]){
					document.getElementById("msg").innerHTML=str[1];
					//alert(str[1]);
				}
				borrowForm.bookNo.value="";
				borrowForm.bookNo.focus();
				//window.location.reload();
			}
		});
	
	}
	
	
	function userSubmit(id_card){
	if(!id_card){
		id_card = $("id_card").value;
	}
	if(!id_card||id_card==""){
		alert("请先选择读者！");
		return;
	}
	
	new Ajax.Request('/library/common.do?action=getUserByCard&id_card=' + id_card, {
			method : 'POST',
			evalScripts : true,
			parameters : '',
			onComplete : function(xmlHttp, error) {
				var returnVal = xmlHttp.responseText;
				if(returnVal.indexOf("◎")>0){
					var str = returnVal.split("◎");
					borrowForm.userId.value=str[0];
					borrowForm.id_card.value=str[1];
					borrowForm.realName.value=str[2];
					borrowForm.loginName.value=str[3];
					borrowForm.user_type.value=str[4];
					borrowForm.gender.value=str[5];
					borrowForm.unreturn.value=str[6];
					borrowForm.book_limit.value=str[7];
					borrowForm.day_limit.value=str[8];
					checkBookLimit(str[7],str[6],str[9]);
					borrowForm.bookNo.focus();
				}else{
					alert(returnVal);
					borrowForm.id_card.value="";
					borrowForm.reset();
					//window.location.reload();
					interId=window.setInterval(readCardSN,100); 
					borrowForm.id_card.focus();
				}
			}
		});
		}
	
	function bookSubmit(book_no){
		if(!book_no){
			book_no = $("bookNo").value;
		}
		if(!book_no||book_no==""){
			alert("请先选择图书！");
			return;
		}
		//检测是否超过借阅限制
		var book_limit= borrowForm.book_limit.value;
		var unreturn= borrowForm.unreturn.value;
		var bookIds= borrowForm.bookIds.value;
		var bookArr = bookIds.split(",");
		var booknum=clearRepeat(bookArr);   
		//alert(book_limit - unreturn - booknum.length);
		if((book_limit - unreturn - booknum.length)<0){
			alert("该读者超过了可借阅图书"+book_limit+"本限制！");
			return;
		}
		
		new Ajax.Request('/library/common.do?action=getBookBybookNo&book_no=' + book_no, {
			method : 'POST',
			evalScripts : true,
			parameters : '',
			onComplete : function(xmlHttp, error) {
				var returnVal = xmlHttp.responseText;
				if(returnVal.indexOf("◎")>0){
				var tds = returnVal.split("◎");
					 borrowForm.bookIds.value+=tds[0]+",";   
					// borrowForm.bookNo.value=tds[1];   
				     borrowForm.bookSchool.value=tds[1];
				     borrowForm.bookPrice.value=tds[2];
				     borrowForm.bookPlace.value=tds[3];
				     borrowForm.bookName.value=tds[4];
				     borrowForm.bookIndex.value=tds[5];
				     borrowForm.categoryName.value=tds[6];
				     borrowForm.bookAuthor.value=tds[7];
				     bookQuery();
				}else if(returnVal=="1"){
					alert("该图书已出借！");
					 borrowForm.bookNo.value="";
					 borrowForm.bookNo.focus();
				}else if(returnVal=="2"){
					alert("该图书已遗失！");
					 borrowForm.bookNo.value="";
					 borrowForm.bookNo.focus();
				}else if(returnVal=="3"){
					alert("该图书已损坏！");
					 borrowForm.bookNo.value="";
					 borrowForm.bookNo.focus();
				}
				else{
					alert("条形码输入有误！");
					 borrowForm.bookNo.value="";
					 borrowForm.bookNo.focus();
					 
				}
				 borrowForm.bookNo.value=""; 
			}
		});
}



	 function downloadFile(file) {
		window.location.href = baseUrl + '?action=downloadFile&file='+new BaseClass().encode(file, true);
	}
	
	
