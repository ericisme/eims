﻿/* WinLIKE 1.5.03 (c) 1998-2007 by CEITON technologies GmbH - www.ceiton.com
   all rights reserved - patent pending */
y_=document;b_=null;F_=false;a_=true;c_=new Object;WinLIKE=c_;aS_=WinLIKEerrorpage;c_.init=ch_;function d_(a,b){return a.indexOf(b)}function P_(a){return a.location}l_=0;s_=0;G_=navigator.userAgent;cC_=G_.length;cd_="MSIE";dc_="rv:";t_='';cN_=d_(G_,'acin');if(d_(G_,dc_)>0&&d_(G_,'Cami')==-1){G_=G_.slice(d_(G_,dc_)+3,cC_);cf_=parseFloat(G_);G_=G_.slice(3,cC_);dV_=parseFloat(G_);isNaN(dV_)?0:cf_+=dV_/10;s_=!isNaN(cf_)&&cf_ > 0.91}else if(d_(G_,cd_)>0&&d_(G_,'Wind')>0&&d_(G_,'Oper')==-1&&d_(G_,'mdkc')==-1){G_=G_.slice(d_(G_,cd_)+5,cC_);cf_=parseFloat(G_);l_=!isNaN(cf_)&&cf_ > 5.4}c_.ie=l_;if(!l_&&!s_)aS_?P_(document).replace(aS_):c_.ie=b_;function ch_(a){if(c_.ie==b_){ax_();return}fV_='__';if(window.name==fV_)return;cK_=0;et_='/';cD_=0;bc_=b_;fY_='1_';fZ_='2_';fT_='3_';g0_='4_';g2_='x';fU_='y';fW_='_';for(a=65;a<91;a++)t_+='var '+a3_(a)+';';fa_=et_+et_+et_+et_;dk_=new Array('Mn','Ed','Cls','Tit','HD','Min','Mov','Siz','Vis','Fro','Bac','SD','LD','Rel',"ca_","eQ_","_W_","_V_",'Del','Mx','Bg');eA_=new Array('Nam','onUnload','onClose','onEvent',"Ttl",'Ski',"Height","Width","Left","Top","RWidth","RHeight","RLeft","RTop",'Adr');a__=v_('_');if(a__)P_(n_(a__)).replace(em_+'sys/sys.html');else cM_();dv_(fY_);dv_(fZ_);dv_(fT_);dv_(g0_)}
