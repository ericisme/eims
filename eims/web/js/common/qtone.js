/**
 * @author 钟琼阁
 * @sdoc 
 */

//open窗口
function qtoneWinOpen(sURL, iWidth, iHeight, iTop, iLeft, sName) {
  if (arguments.length==1) {
    window.open(sURL);
    return;
  }

  if (iWidth == -1) iWidth = screen.availWidth;
  if (iHeight == -1) iHeight = screen.availHeight-20;

  iWidth =(emisEmpty(iWidth) ?300:iWidth);
  iHeight=(emisEmpty(iHeight)?100:iHeight);

  if (arguments.length<5) {
    iTop =(screen.availHeight/2)-(iHeight/2)-1;
    iLeft=(screen.availWidth/2)-(iWidth/2)-1;
  }

  sName=(qtoneEmpty(sName)?"":sName);
  return window.open(sURL, sName,"width=" + iWidth + "px,height=" + iHeight + "px," + "top=" + iTop + "px,Left=" + iLeft + "px," +"border=thin,help=no,menubar=no,toolbar=no,location=no,directories=no,status=no,resizable=0,scrollbars=1");
}

//是否为空判断
function qtoneEmpty(xValue) {
  if (xValue == null || xValue == "null" || xValue == "undefined" || xValue == "NaN" || xValue == "")
    return true;
  return false;
}

/*
   *  方法:Array.remove(dx)
   *  功能:删除数组元素.
   *  参数:dx删除元素的下标.
   *  返回:在原数组上修改数组
      a = ['1','2','3','4','5'];
      alert("elements: "+a+"\nLength: "+a.length);
      a.remove(0); //删除下标为0的元素
      alert("elements: "+a+"\nLength: "+a.length);
   */

  //经常用的是通过遍历,重构数组.
  Array.prototype.remove=function(dx)
  {
    if(isNaN(dx)||dx>this.length){return false;}
    for(var i=0,n=0;i<this.length;i++)
    {
        if(this[i]!=this[dx])
        {
            this[n++]=this[i]
        }
    }
    this.length-=1
  }

