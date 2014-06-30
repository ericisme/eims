/**
 * @author �����
 * @sdoc 
 */

//open����
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

//�Ƿ�Ϊ���ж�
function qtoneEmpty(xValue) {
  if (xValue == null || xValue == "null" || xValue == "undefined" || xValue == "NaN" || xValue == "")
    return true;
  return false;
}

/*
   *  ����:Array.remove(dx)
   *  ����:ɾ������Ԫ��.
   *  ����:dxɾ��Ԫ�ص��±�.
   *  ����:��ԭ�������޸�����
      a = ['1','2','3','4','5'];
      alert("elements: "+a+"\nLength: "+a.length);
      a.remove(0); //ɾ���±�Ϊ0��Ԫ��
      alert("elements: "+a+"\nLength: "+a.length);
   */

  //�����õ���ͨ������,�ع�����.
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

