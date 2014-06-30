function scrollDoor(){
}
scrollDoor.prototype = {
 sd : function(menus,divs,openClass,closeClass){
  var _this = this;
  if(menus.length != divs.length)
  {
   alert("菜单层数量和内容层数量不一样!");
   return false;
  }    
  for(var i = 0 ; i < menus.length ; i++)
  { 
   _this.IDn(menus[i]).value = i;    
   _this.IDn(menus[i]).onmouseover = function(){
    for(var j = 0 ; j < menus.length ; j++)
    {      
     _this.IDn(menus[j]).className = closeClass;
     _this.IDn(divs[j]).style.display = "none";
    }
    _this.IDn(menus[this.value]).className = openClass; 
    _this.IDn(divs[this.value]).style.display = "block";    
   }
  }
  },
  IDn : function(oid){
  if(typeof(oid) == "string"){
	  return document.getElementById(oid);
	  }
  return oid;
 }
}
window.onload = function(){
 var SDmodel = new scrollDoor();
 SDmodel.sd(["m01","m02","m03","m04"],["c01","c02","c03","c04"],"on","");
 SDmodel.sd(["type01","type02","type03","type04"],["t01","t02","t03","t04"],"on","");
}
function showID(idnum){
	for(var i=1;i<3;i++){
		document.getElementById("it"+i).style.display = "none"; 
		document.getElementById("ma"+i).style.display = "none"; 
	}
	document.getElementById("it"+idnum).style.display = "block"; 
	document.getElementById("ma"+idnum).style.display = "block"; 
}