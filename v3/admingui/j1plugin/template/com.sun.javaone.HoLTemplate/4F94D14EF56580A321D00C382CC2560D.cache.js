(function(){var $wnd = window;var $doc = $wnd.document;var $moduleName, $moduleBase;var _,Ex='com.google.gwt.core.client.',Fx='com.google.gwt.http.client.',ay='com.google.gwt.lang.',by='com.google.gwt.user.client.',cy='com.google.gwt.user.client.impl.',dy='com.google.gwt.user.client.ui.',ey='com.sun.javaone.client.',fy='java.lang.',gy='java.util.';function Dx(){}
function as(a){return this===a;}
function bs(){return Ds(this);}
function Er(){}
_=Er.prototype={};_.eQ=as;_.hC=bs;_.tI=1;function o(){return u();}
var p=null;function s(a){return a==null?0:a.$H?a.$H:(a.$H=v());}
function t(a){return a==null?0:a.$H?a.$H:(a.$H=v());}
function u(){var b=$doc.location.href;var a=b.indexOf('#');if(a!= -1)b=b.substring(0,a);a=b.indexOf('?');if(a!= -1)b=b.substring(0,a);a=b.lastIndexOf('/');if(a!= -1)b=b.substring(0,a);return b.length>0?b+'/':'';}
function v(){return ++w;}
var w=0;function Fs(b,a){a;return b;}
function bt(b,a){if(b.a!==null){throw nr(new mr(),"Can't overwrite cause");}if(a===b){throw kr(new jr(),'Self-causation not permitted');}b.a=a;return b;}
function Es(){}
_=Es.prototype=new Er();_.tI=3;_.a=null;function hr(b,a){Fs(b,a);return b;}
function gr(){}
_=gr.prototype=new Es();_.tI=4;function ds(b,a){hr(b,a);return b;}
function cs(){}
_=cs.prototype=new gr();_.tI=5;function y(c,b,a){ds(c,'JavaScript '+b+' exception: '+a);return c;}
function x(){}
_=x.prototype=new cs();_.tI=6;function C(b,a){if(!kd(a,2)){return false;}return bb(b,jd(a,2));}
function D(a){return s(a);}
function E(){return [];}
function F(){return function(){};}
function ab(){return {};}
function cb(a){return C(this,a);}
function bb(a,b){return a===b;}
function db(){return D(this);}
function A(){}
_=A.prototype=new Er();_.eQ=cb;_.hC=db;_.tI=7;function ec(b,d,c,a){if(d===null){throw new xr();}if(a===null){throw new xr();}if(c<0){throw new jr();}b.a=c;b.c=d;if(c>0){b.b=lb(new kb(),b,a);fg(b.b,c);}else{b.b=null;}return b;}
function gc(a){var b;if(a.c!==null){b=a.c;a.c=null;vc(b);fc(a);}}
function fc(a){if(a.b!==null){cg(a.b);}}
function ic(e,a){var b,c,d,f;if(e.c===null){return;}fc(e);f=e.c;e.c=null;b=wc(f);if(b!==null){c=ds(new cs(),b);a.C(e,c);}else{d=kc(f);a.F(e,d);}}
function jc(b,a){if(b.c===null){return;}gc(b);a.C(b,bc(new ac(),b,b.a));}
function kc(b){var a;a=gb(new fb(),b);return a;}
function lc(a){var b;b=p;{ic(this,a);}}
function eb(){}
_=eb.prototype=new Er();_.q=lc;_.tI=0;_.a=0;_.b=null;_.c=null;function mc(){}
_=mc.prototype=new Er();_.tI=0;function gb(a,b){a.a=b;return a;}
function ib(a){return yc(a.a);}
function jb(a){return xc(a.a);}
function fb(){}
_=fb.prototype=new mc();_.tI=0;function dg(){dg=Dx;lg=kv(new iv());{kg();}}
function bg(a){dg();return a;}
function cg(a){if(a.c){gg(a.d);}else{hg(a.d);}tv(lg,a);}
function eg(a){if(!a.c){tv(lg,a);}a.hb();}
function fg(b,a){if(a<=0){throw kr(new jr(),'must be positive');}cg(b);b.c=false;b.d=ig(b,a);mv(lg,b);}
function gg(a){dg();$wnd.clearInterval(a);}
function hg(a){dg();$wnd.clearTimeout(a);}
function ig(b,a){dg();return $wnd.setTimeout(function(){b.r();},a);}
function jg(){var a;a=p;{eg(this);}}
function kg(){dg();qg(new Df());}
function Cf(){}
_=Cf.prototype=new Er();_.r=jg;_.tI=8;_.c=false;_.d=0;var lg;function mb(){mb=Dx;dg();}
function lb(b,a,c){mb();b.a=a;b.b=c;bg(b);return b;}
function nb(){jc(this.a,this.b);}
function kb(){}
_=kb.prototype=new Cf();_.hb=nb;_.tI=9;function ub(){ub=Dx;xb=qb(new pb(),'GET');qb(new pb(),'POST');yb=ai(new Fh());}
function sb(b,a,c){ub();tb(b,a===null?null:a.a,c);return b;}
function tb(b,a,c){ub();qc('httpMethod',a);qc('url',c);b.a=a;b.c=c;return b;}
function vb(g,d,a){var b,c,e,f,h;h=fi(yb);{b=zc(h,g.a,g.c,true);}if(b!==null){e=Eb(new Db(),g.c);bt(e,Bb(new Ab(),b));throw e;}wb(g,h);c=ec(new eb(),h,g.b,a);f=Ac(h,c,d,a);if(f!==null){throw Bb(new Ab(),f);}return c;}
function wb(a,b){{Bc(b,'Content-Type','text/plain; charset=utf-8');}}
function ob(){}
_=ob.prototype=new Er();_.tI=0;_.a=null;_.b=0;_.c=null;var xb,yb;function qb(b,a){b.a=a;return b;}
function pb(){}
_=pb.prototype=new Er();_.tI=0;_.a=null;function Bb(b,a){hr(b,a);return b;}
function Ab(){}
_=Ab.prototype=new gr();_.tI=10;function Eb(a,b){Bb(a,'The URL '+b+' is invalid or violates the same-origin security restriction');return a;}
function Db(){}
_=Db.prototype=new Ab();_.tI=11;function bc(b,a,c){Bb(b,dc(c));return b;}
function dc(a){return 'A request timeout has expired after '+ur(a)+' ms';}
function ac(){}
_=ac.prototype=new Ab();_.tI=12;function qc(a,b){rc(a,b);if(0==ns(us(b))){throw kr(new jr(),a+' can not be empty');}}
function rc(a,b){if(null===b){throw yr(new xr(),a+' can not be null');}}
function vc(a){a.onreadystatechange=gi;a.abort();}
function wc(b){try{if(b.status===undefined){return 'XmlHttpRequest.status == undefined, please see Safari bug '+'http://bugs.webkit.org/show_bug.cgi?id=3810 for more details';}return null;}catch(a){return 'Unable to read XmlHttpRequest.status; likely causes are a '+'networking error or bad cross-domain request. Please see '+'https://bugzilla.mozilla.org/show_bug.cgi?id=238559 for more '+'details';}}
function xc(a){return a.responseText;}
function yc(a){return a.status;}
function zc(e,c,d,b){try{e.open(c,d,b);return null;}catch(a){return a.message||a.toString();}}
function Ac(e,c,d,b){e.onreadystatechange=function(){if(e.readyState==uc){e.onreadystatechange=gi;c.q(b);}};try{e.send(d);return null;}catch(a){e.onreadystatechange=gi;return a.message||a.toString();}}
function Bc(d,b,c){try{d.setRequestHeader(b,c);return null;}catch(a){return a.message||a.toString();}}
var uc=4;function Dc(c,a,d,b,e){c.a=a;c.b=b;e;c.tI=d;return c;}
function Fc(a,b,c){return a[b]=c;}
function ad(b,a){return b[a];}
function bd(a){return a.length;}
function dd(e,d,c,b,a){return cd(e,d,c,b,0,bd(b),a);}
function cd(j,i,g,c,e,a,b){var d,f,h;if((f=ad(c,e))<0){throw new vr();}h=Dc(new Cc(),f,ad(i,e),ad(g,e),j);++e;if(e<a){j=rs(j,1);for(d=0;d<f;++d){Fc(h,d,cd(j,i,g,c,e,a,b));}}else{for(d=0;d<f;++d){Fc(h,d,b);}}return h;}
function ed(a,b,c){if(c!==null&&a.b!=0&& !kd(c,a.b)){throw new Cq();}return Fc(a,b,c);}
function Cc(){}
_=Cc.prototype=new Er();_.tI=0;function hd(b,a){return !(!(b&&nd[b][a]));}
function id(a){return String.fromCharCode(a);}
function jd(b,a){if(b!=null)hd(b.tI,a)||md();return b;}
function kd(b,a){return b!=null&&hd(b.tI,a);}
function md(){throw new cr();}
function ld(a){if(a!==null){throw new cr();}return a;}
function od(b,d){_=d.prototype;if(b&& !(b.tI>=_.tI)){var c=b.toString;for(var a in _){b[a]=_[a];}b.toString=c;}return b;}
var nd;function rd(a){if(kd(a,3)){return a;}return y(new x(),td(a),sd(a));}
function sd(a){return a.message;}
function td(a){return a.name;}
function xd(){if(wd===null||Ad()){wd=Bw(new bw());zd(wd);}return wd;}
function yd(b){var a;a=xd();return jd(bx(a,b),1);}
function zd(e){var b=$doc.cookie;if(b&&b!=''){var a=b.split('; ');for(var d=0;d<a.length;++d){var f,g;var c=a[d].indexOf('=');if(c== -1){f=a[d];g='';}else{f=a[d].substring(0,c);g=a[d].substring(c+1);}f=decodeURIComponent(f);g=decodeURIComponent(g);e.eb(f,g);}}}
function Ad(){var a=$doc.cookie;if(a!=''&&a!=Bd){Bd=a;return true;}else{return false;}}
function Cd(a){$doc.cookie=a+"='';expires='Fri, 02-Jan-1970 00:00:00 GMT'";}
function Ed(a,b){Dd(a,b,0,null,null,false);}
function Dd(d,g,c,b,e,f){var a=encodeURIComponent(d)+'='+encodeURIComponent(g);if(c)a+=';expires='+new Date(c).toGMTString();if(b)a+=';domain='+b;if(e)a+=';path='+e;if(f)a+=';secure';$doc.cookie=a;}
var wd=null,Bd=null;function ae(){ae=Dx;Ee=kv(new iv());{ze=new Fg();hh(ze);}}
function be(b,a){ae();mh(ze,b,a);}
function ce(a,b){ae();return bh(ze,a,b);}
function de(){ae();return oh(ze,'A');}
function ee(){ae();return oh(ze,'div');}
function fe(){ae();return oh(ze,'tbody');}
function ge(){ae();return oh(ze,'td');}
function he(){ae();return oh(ze,'tr');}
function ie(){ae();return oh(ze,'table');}
function le(b,a,d){ae();var c;c=p;{ke(b,a,d);}}
function ke(b,a,c){ae();var d;if(a===De){if(ne(b)==8192){De=null;}}d=je;je=b;try{c.B(b);}finally{je=d;}}
function me(b,a){ae();ph(ze,b,a);}
function ne(a){ae();return qh(ze,a);}
function oe(a){ae();ch(ze,a);}
function pe(b,a){ae();return rh(ze,b,a);}
function qe(a){ae();return sh(ze,a);}
function se(a,b){ae();return uh(ze,a,b);}
function re(a,b){ae();return th(ze,a,b);}
function te(a){ae();return vh(ze,a);}
function ue(a){ae();return dh(ze,a);}
function ve(a){ae();return wh(ze,a);}
function we(a){ae();return eh(ze,a);}
function xe(a){ae();return fh(ze,a);}
function ye(a){ae();return gh(ze,a);}
function Ae(c,a,b){ae();ih(ze,c,a,b);}
function Be(a){ae();var b,c;c=true;if(Ee.b>0){b=ld(pv(Ee,Ee.b-1));if(!(c=null.lb())){me(a,true);oe(a);}}return c;}
function Ce(b,a){ae();xh(ze,b,a);}
function Fe(a,b,c){ae();yh(ze,a,b,c);}
function af(a,b){ae();zh(ze,a,b);}
function bf(a,b){ae();Ah(ze,a,b);}
function cf(a,b){ae();jh(ze,a,b);}
function df(b,a,c){ae();Bh(ze,b,a,c);}
function ef(a,b){ae();kh(ze,a,b);}
function ff(){ae();return Ch(ze);}
function gf(){ae();return Dh(ze);}
var je=null,ze=null,De=null,Ee;function kf(a){if(kd(a,4)){return ce(this,jd(a,4));}return C(od(this,hf),a);}
function lf(){return D(od(this,hf));}
function hf(){}
_=hf.prototype=new A();_.eQ=kf;_.hC=lf;_.tI=13;function pf(a){return C(od(this,mf),a);}
function qf(){return D(od(this,mf));}
function mf(){}
_=mf.prototype=new A();_.eQ=pf;_.hC=qf;_.tI=14;function tf(){tf=Dx;yf=kv(new iv());{zf=new oi();if(!si(zf)){zf=null;}}}
function uf(a){tf();mv(yf,a);}
function vf(){tf();$wnd.history.back();}
function wf(a){tf();var b,c;for(b=vt(yf);ot(b);){c=jd(pt(b),5);c.D(a);}}
function xf(){tf();return zf!==null?zi(zf):'';}
function Af(a){tf();if(zf!==null){li(zf,a);}}
function Bf(b){tf();var a;a=p;{wf(b);}}
var yf,zf=null;function Ff(){while((dg(),lg).b>0){cg(jd(pv((dg(),lg),0),6));}}
function ag(){return null;}
function Df(){}
_=Df.prototype=new Er();_.bb=Ff;_.cb=ag;_.tI=15;function pg(){pg=Dx;sg=kv(new iv());Cg=kv(new iv());{yg();}}
function qg(a){pg();mv(sg,a);}
function rg(a){pg();mv(Cg,a);}
function tg(){pg();var a,b;for(a=vt(sg);ot(a);){b=jd(pt(a),7);b.bb();}}
function ug(){pg();var a,b,c,d;d=null;for(a=vt(sg);ot(a);){b=jd(pt(a),7);c=b.cb();{d=c;}}return d;}
function vg(){pg();var a,b;for(a=vt(Cg);ot(a);){b=jd(pt(a),8);b.db(xg(),wg());}}
function wg(){pg();return ff();}
function xg(){pg();return gf();}
function yg(){pg();__gwt_initHandlers(function(){Bg();},function(){return Ag();},function(){zg();$wnd.onresize=null;$wnd.onbeforeclose=null;$wnd.onclose=null;});}
function zg(){pg();var a;a=p;{tg();}}
function Ag(){pg();var a;a=p;{return ug();}}
function Bg(){pg();var a;a=p;{vg();}}
function Dg(a){pg();$doc.title=a;}
var sg,Cg;function mh(c,b,a){b.appendChild(a);}
function oh(b,a){return $doc.createElement(a);}
function ph(c,b,a){b.cancelBubble=a;}
function qh(b,a){switch(a.type){case 'blur':return 4096;case 'change':return 1024;case 'click':return 1;case 'dblclick':return 2;case 'focus':return 2048;case 'keydown':return 128;case 'keypress':return 256;case 'keyup':return 512;case 'load':return 32768;case 'losecapture':return 8192;case 'mousedown':return 4;case 'mousemove':return 64;case 'mouseout':return 32;case 'mouseover':return 16;case 'mouseup':return 8;case 'scroll':return 16384;case 'error':return 65536;case 'mousewheel':return 131072;case 'DOMMouseScroll':return 131072;}}
function rh(d,b,a){var c=b.getAttribute(a);return c==null?null:c;}
function sh(c,b){var a=$doc.getElementById(b);return a||null;}
function uh(d,a,b){var c=a[b];return c==null?null:String(c);}
function th(d,a,c){var b=parseInt(a[c]);if(!b){return 0;}return b;}
function vh(b,a){return a.__eventBits||0;}
function wh(c,a){var b=a.innerHTML;return b==null?null:b;}
function xh(c,b,a){b.removeChild(a);}
function yh(c,a,b,d){a[b]=d;}
function zh(c,a,b){a.__listener=b;}
function Ah(c,a,b){if(!b){b='';}a.innerHTML=b;}
function Bh(c,b,a,d){b.style[a]=d;}
function Ch(a){return $doc.body.clientHeight;}
function Dh(a){return $doc.body.clientWidth;}
function Eg(){}
_=Eg.prototype=new Er();_.tI=0;function bh(c,a,b){if(!a&& !b)return true;else if(!a|| !b)return false;return a.uniqueID==b.uniqueID;}
function ch(b,a){a.returnValue=false;}
function dh(c,b){var a=b.firstChild;return a||null;}
function eh(c,a){var b=a.innerText;return b==null?null:b;}
function fh(c,a){var b=a.nextSibling;return b||null;}
function gh(c,a){var b=a.parentElement;return b||null;}
function hh(d){try{$doc.execCommand('BackgroundImageCache',false,true);}catch(a){}$wnd.__dispatchEvent=function(){var c=lh;lh=this;if($wnd.event.returnValue==null){$wnd.event.returnValue=true;if(!Be($wnd.event)){lh=c;return;}}var b,a=this;while(a&& !(b=a.__listener))a=a.parentElement;if(b)le($wnd.event,a,b);lh=c;};$wnd.__dispatchDblClickEvent=function(){var a=$doc.createEventObject();this.fireEvent('onclick',a);if(this.__eventBits&2)$wnd.__dispatchEvent.call(this);};$doc.body.onclick=$doc.body.onmousedown=$doc.body.onmouseup=$doc.body.onmousemove=$doc.body.onmousewheel=$doc.body.onkeydown=$doc.body.onkeypress=$doc.body.onkeyup=$doc.body.onfocus=$doc.body.onblur=$doc.body.ondblclick=$wnd.__dispatchEvent;}
function ih(d,c,a,b){if(b>=c.children.length)c.appendChild(a);else c.insertBefore(a,c.children[b]);}
function jh(c,a,b){if(!b)b='';a.innerText=b;}
function kh(c,b,a){b.__eventBits=a;b.onclick=a&1?$wnd.__dispatchEvent:null;b.ondblclick=a&(1|2)?$wnd.__dispatchDblClickEvent:null;b.onmousedown=a&4?$wnd.__dispatchEvent:null;b.onmouseup=a&8?$wnd.__dispatchEvent:null;b.onmouseover=a&16?$wnd.__dispatchEvent:null;b.onmouseout=a&32?$wnd.__dispatchEvent:null;b.onmousemove=a&64?$wnd.__dispatchEvent:null;b.onkeydown=a&128?$wnd.__dispatchEvent:null;b.onkeypress=a&256?$wnd.__dispatchEvent:null;b.onkeyup=a&512?$wnd.__dispatchEvent:null;b.onchange=a&1024?$wnd.__dispatchEvent:null;b.onfocus=a&2048?$wnd.__dispatchEvent:null;b.onblur=a&4096?$wnd.__dispatchEvent:null;b.onlosecapture=a&8192?$wnd.__dispatchEvent:null;b.onscroll=a&16384?$wnd.__dispatchEvent:null;b.onload=a&32768?$wnd.__dispatchEvent:null;b.onerror=a&65536?$wnd.__dispatchEvent:null;b.onmousewheel=a&131072?$wnd.__dispatchEvent:null;}
function Fg(){}
_=Fg.prototype=new Eg();_.tI=0;var lh=null;function di(a){gi=F();return a;}
function fi(a){return ci(a);}
function Eh(){}
_=Eh.prototype=new Er();_.tI=0;var gi=null;function ai(a){di(a);return a;}
function ci(a){return new ActiveXObject('Msxml2.XMLHTTP');}
function Fh(){}
_=Fh.prototype=new Eh();_.tI=0;function zi(a){return $wnd.__gwt_historyToken;}
function Ai(a,b){$wnd.__gwt_historyToken=b;}
function Bi(a){Bf(a);}
function hi(){}
_=hi.prototype=new Er();_.tI=0;function ki(a){var b;a.a=mi();if(a.a===null){return false;}ri(a);b=ni(a.a);if(b!==null){Ai(a,qi(a,b));}else{ui(a,a.a,zi(a),true);}ti(a);return true;}
function li(b,a){b.z(b.a,a,false);}
function mi(){var a=$doc.getElementById('__gwt_historyFrame');return a||null;}
function ni(b){var c=null;if(b.contentWindow){var a=b.contentWindow.document;c=a.getElementById('__gwt_historyToken')||null;}return c;}
function ii(){}
_=ii.prototype=new hi();_.tI=0;_.a=null;function qi(a,b){return b.innerText;}
function si(a){if(!ki(a)){return false;}wi();return true;}
function ri(c){var b=$wnd.location.hash;if(b.length>0){try{$wnd.__gwt_historyToken=decodeURIComponent(b.substring(1));}catch(a){$wnd.location.hash='';$wnd.__gwt_historyToken='';}return;}$wnd.__gwt_historyToken='';}
function ti(b){$wnd.__gwt_onHistoryLoad=function(a){if(a!=$wnd.__gwt_historyToken){$wnd.__gwt_historyToken=a;$wnd.location.hash=encodeURIComponent(a);Bi(a);}};}
function ui(e,c,d,b){d=vi(d||'');if(b||$wnd.__gwt_historyToken!=d){var a=c.contentWindow.document;a.open();a.write('<html><body onload="if(parent.__gwt_onHistoryLoad)parent.__gwt_onHistoryLoad(__gwt_historyToken.innerText)"><div id="__gwt_historyToken">'+d+'<\/div><\/body><\/html>');a.close();}}
function vi(b){var a;a=ee();cf(a,b);return ve(a);}
function wi(){var d=function(){var b=$wnd.location.hash;if(b.length>0){var c='';try{c=decodeURIComponent(b.substring(1));}catch(a){$wnd.location.reload();}if($wnd.__gwt_historyToken&&c!=$wnd.__gwt_historyToken){$wnd.location.reload();}}$wnd.setTimeout(d,250);};d();}
function xi(b,c,a){ui(this,b,c,a);}
function oi(){}
_=oi.prototype=new ii();_.z=xi;_.tI=0;function qm(b,a){rm(b,um(b)+id(45)+a);}
function rm(b,a){an(b.i,a,true);}
function tm(a){return re(a.i,'offsetWidth');}
function um(a){return Em(a.i);}
function vm(b,a){wm(b,um(b)+id(45)+a);}
function wm(b,a){an(b.i,a,false);}
function xm(d,b,a){var c=b.parentNode;if(!c){return;}c.insertBefore(a,b);c.removeChild(b);}
function ym(b,a){if(b.i!==null){xm(b,b.i,a);}b.i=a;}
function zm(b,a){Fm(b.i,a);}
function Am(b,a){bn(b.i,a);}
function Bm(a,b){cn(a.i,b);}
function Cm(b,a){ef(b.i,a|te(b.i));}
function Dm(a){return se(a,'className');}
function Em(a){var b,c;b=Dm(a);c=ks(b,32);if(c>=0){return ss(b,0,c);}return b;}
function Fm(a,b){Fe(a,'className',b);}
function an(c,j,a){var b,d,e,f,g,h,i;if(c===null){throw ds(new cs(),'Null widget handle. If you are creating a composite, ensure that initWidget() has been called.');}j=us(j);if(ns(j)==0){throw kr(new jr(),'Style names cannot be empty');}i=Dm(c);e=ls(i,j);while(e!=(-1)){if(e==0||gs(i,e-1)==32){f=e+ns(j);g=ns(i);if(f==g||f<g&&gs(i,f)==32){break;}}e=ms(i,j,e+1);}if(a){if(e==(-1)){if(ns(i)>0){i+=' ';}Fe(c,'className',i+j);}}else{if(e!=(-1)){b=us(ss(i,0,e));d=us(rs(i,e+ns(j)));if(ns(b)==0){h=d;}else if(ns(d)==0){h=b;}else{h=b+' '+d;}Fe(c,'className',h);}}}
function bn(a,b){if(a===null){throw ds(new cs(),'Null widget handle. If you are creating a composite, ensure that initWidget() has been called.');}b=us(b);if(ns(b)==0){throw kr(new jr(),'Style names cannot be empty');}dn(a,b);}
function cn(a,b){a.style.display=b?'':'none';}
function dn(b,f){var a=b.className.split(/\s+/);if(!a){return;}var g=a[0];var h=g.length;a[0]=f;for(var c=1,d=a.length;c<d;c++){var e=a[c];if(e.length>h&&(e.charAt(h)=='-'&&e.indexOf(g)==0)){a[c]=f+e.substring(h);}}b.className=a.join(' ');}
function pm(){}
_=pm.prototype=new Er();_.tI=0;_.i=null;function En(a){if(a.g){throw nr(new mr(),"Should only call onAttach when the widget is detached from the browser's document");}a.g=true;af(a.i,a);a.n();a.E();}
function Fn(a){if(!a.g){throw nr(new mr(),"Should only call onDetach when the widget is attached to the browser's document");}try{a.ab();}finally{a.o();af(a.i,null);a.g=false;}}
function ao(a){if(a.h!==null){a.h.gb(a);}else if(a.h!==null){throw nr(new mr(),"This widget's parent does not implement HasWidgets");}}
function bo(b,a){if(b.g){af(b.i,null);}ym(b,a);if(b.g){af(a,b);}}
function co(c,b){var a;a=c.h;if(b===null){if(a!==null&&a.g){Fn(c);}c.h=null;}else{if(a!==null){throw nr(new mr(),'Cannot set a new parent without first clearing the old parent');}c.h=b;if(b.g){En(c);}}}
function eo(){}
function fo(){}
function go(a){}
function ho(){}
function io(){}
function nn(){}
_=nn.prototype=new pm();_.n=eo;_.o=fo;_.B=go;_.E=ho;_.ab=io;_.tI=16;_.g=false;_.h=null;function kl(b,a){co(a,b);}
function ml(b,a){co(a,null);}
function nl(){var a,b;for(b=this.x();b.w();){a=jd(b.A(),9);En(a);}}
function ol(){var a,b;for(b=this.x();b.w();){a=jd(b.A(),9);Fn(a);}}
function pl(){}
function ql(){}
function jl(){}
_=jl.prototype=new nn();_.n=nl;_.o=ol;_.E=pl;_.ab=ql;_.tI=17;function ij(a){a.f=un(new on(),a);}
function jj(a){ij(a);return a;}
function kj(c,a,b){ao(a);vn(c.f,a);be(b,a.i);kl(c,a);}
function lj(d,b,a){var c;nj(d,a);if(b.h===d){c=pj(d,b);if(c<a){a--;}}return a;}
function mj(b,a){if(a<0||a>=b.f.b){throw new pr();}}
function nj(b,a){if(a<0||a>b.f.b){throw new pr();}}
function qj(b,a){return xn(b.f,a);}
function pj(b,a){return yn(b.f,a);}
function rj(e,b,c,a,d){a=lj(e,b,a);ao(b);zn(e.f,b,a);if(d){Ae(c,b.i,a);}else{be(c,b.i);}kl(e,b);}
function sj(b,a){return b.gb(qj(b,a));}
function tj(b,c){var a;if(c.h!==b){return false;}ml(b,c);a=c.i;Ce(ye(a),a);Cn(b.f,c);return true;}
function uj(){return An(this.f);}
function vj(a){return tj(this,a);}
function hj(){}
_=hj.prototype=new jl();_.x=uj;_.gb=vj;_.tI=18;function Di(a){jj(a);bo(a,ee());df(a.i,'position','relative');df(a.i,'overflow','hidden');return a;}
function Ei(a,b){kj(a,b,a.i);}
function aj(a){df(a,'left','');df(a,'top','');df(a,'position','');}
function bj(b){var a;a=tj(this,b);if(a){aj(b.i);}return a;}
function Ci(){}
_=Ci.prototype=new hj();_.gb=bj;_.tI=19;function dj(a){jj(a);a.e=ie();a.d=fe();be(a.e,a.d);bo(a,a.e);return a;}
function fj(c,b,a){Fe(b,'align',a.a);}
function gj(c,b,a){df(b,'verticalAlign',a.a);}
function cj(){}
_=cj.prototype=new hj();_.tI=20;_.d=null;_.e=null;function xj(a){jj(a);bo(a,ee());return a;}
function yj(a,b){kj(a,b,a.i);Aj(a,b);}
function Aj(b,c){var a;a=c.i;df(a,'width','100%');df(a,'height','100%');Bm(c,false);}
function Bj(a,b){df(b.i,'width','');df(b.i,'height','');Bm(b,true);}
function Cj(b,a){mj(b,a);if(b.a!==null){Bm(b.a,false);}b.a=qj(b,a);Bm(b.a,true);}
function Dj(b){var a;a=tj(this,b);if(a){Bj(this,b);if(this.a===b){this.a=null;}}return a;}
function wj(){}
_=wj.prototype=new hj();_.gb=Dj;_.tI=21;_.a=null;function gl(a){bo(a,ee());Cm(a,131197);zm(a,'gwt-Label');return a;}
function il(a){switch(ne(a)){case 1:break;case 4:case 8:case 64:case 16:case 32:break;case 131072:break;}}
function fl(){}
_=fl.prototype=new nn();_.B=il;_.tI=22;function Fj(a){gl(a);bo(a,ee());Cm(a,125);zm(a,'gwt-HTML');return a;}
function ak(b,a){Fj(b);ck(b,a);return b;}
function ck(b,a){bf(b.i,a);}
function Ej(){}
_=Ej.prototype=new fl();_.tI=23;function ik(){ik=Dx;gk(new fk(),'center');jk=gk(new fk(),'left');gk(new fk(),'right');}
var jk;function gk(b,a){b.a=a;return b;}
function fk(){}
_=fk.prototype=new Er();_.tI=0;_.a=null;function ok(){ok=Dx;pk=mk(new lk(),'bottom');mk(new lk(),'middle');qk=mk(new lk(),'top');}
var pk,qk;function mk(a,b){a.a=b;return a;}
function lk(){}
_=lk.prototype=new Er();_.tI=0;_.a=null;function uk(a){a.a=(ik(),jk);a.c=(ok(),qk);}
function vk(a){dj(a);uk(a);a.b=he();be(a.d,a.b);Fe(a.e,'cellSpacing','0');Fe(a.e,'cellPadding','0');return a;}
function wk(b,c){var a;a=yk(b);be(b.b,a);kj(b,c,a);}
function yk(b){var a;a=ge();fj(b,a,b.a);gj(b,a,b.c);return a;}
function zk(c,d,a){var b;nj(c,a);b=yk(c);Ae(c.b,b,a);rj(c,d,b,a,false);}
function Ak(c,d){var a,b;b=ye(d.i);a=tj(c,d);if(a){Ce(c.b,b);}return a;}
function Bk(b,a){b.c=a;}
function Ck(a){return Ak(this,a);}
function tk(){}
_=tk.prototype=new cj();_.gb=Ck;_.tI=24;_.b=null;function Ek(a){bo(a,ee());be(a.i,a.a=de());Cm(a,1);zm(a,'gwt-Hyperlink');return a;}
function Fk(c,b,a){Ek(c);cl(c,b);bl(c,a);return c;}
function bl(b,a){b.b=a;Fe(b.a,'href','#'+a);}
function cl(b,a){cf(b.a,a);}
function dl(a){if(ne(a)==1){Af(this.b);oe(a);}}
function Dk(){}
_=Dk.prototype=new nn();_.B=dl;_.tI=25;_.a=null;_.b=null;function xl(){xl=Dx;Cl=Bw(new bw());}
function wl(b,a){xl();Di(b);if(a===null){a=yl();}bo(b,a);En(b);return b;}
function zl(){xl();return Al(null);}
function Al(c){xl();var a,b;b=jd(bx(Cl,c),10);if(b!==null){return b;}a=null;if(c!==null){if(null===(a=qe(c))){return null;}}if(Cl.c==0){Bl();}cx(Cl,c,b=wl(new rl(),a));return b;}
function yl(){xl();return $doc.body;}
function Bl(){xl();qg(new sl());}
function rl(){}
_=rl.prototype=new Ci();_.tI=26;var Cl;function ul(){var a,b;for(b=ou(Cu((xl(),Cl)));vu(b);){a=jd(wu(b),10);if(a.g){Fn(a);}}}
function vl(){return null;}
function sl(){}
_=sl.prototype=new Er();_.bb=ul;_.cb=vl;_.tI=27;function em(a){fm(a,ee());return a;}
function fm(b,a){bo(b,a);return b;}
function gm(a,b){if(a.a!==null){throw nr(new mr(),'SimplePanel can only contain one child widget');}jm(a,b);}
function im(a,b){if(a.a!==b){return false;}ml(a,b);Ce(a.i,b.i);a.a=null;return true;}
function jm(a,b){if(b===a.a){return;}if(b!==null){ao(b);}if(a.a!==null){im(a,a.a);}a.a=b;if(b!==null){be(a.i,a.a.i);kl(a,b);}}
function km(){return am(new El(),this);}
function lm(a){return im(this,a);}
function Dl(){}
_=Dl.prototype=new jl();_.x=km;_.gb=lm;_.tI=28;_.a=null;function Fl(a){a.a=a.b.a!==null;}
function am(b,a){b.b=a;Fl(b);return b;}
function cm(){return this.a;}
function dm(){if(!this.a||this.b.a===null){throw new zx();}this.a=false;return this.b.a;}
function El(){}
_=El.prototype=new Er();_.w=cm;_.A=dm;_.tI=0;function fn(a){a.a=(ik(),jk);a.b=(ok(),qk);}
function gn(a){dj(a);fn(a);Fe(a.e,'cellSpacing','0');Fe(a.e,'cellPadding','0');return a;}
function hn(b,d){var a,c;c=he();a=kn(b);be(c,a);be(b.d,c);kj(b,d,a);}
function kn(b){var a;a=ge();fj(b,a,b.a);gj(b,a,b.b);return a;}
function ln(c,e,a){var b,d;nj(c,a);d=he();b=kn(c);be(d,b);Ae(c.d,d,a);rj(c,e,b,a,false);}
function mn(c){var a,b;b=ye(c.i);a=tj(this,c);if(a){Ce(this.d,ye(b));}return a;}
function en(){}
_=en.prototype=new cj();_.gb=mn;_.tI=29;function un(b,a){b.a=dd('[Lcom.google.gwt.user.client.ui.Widget;',[0],[9],[4],null);return b;}
function vn(a,b){zn(a,b,a.b);}
function xn(b,a){if(a<0||a>=b.b){throw new pr();}return b.a[a];}
function yn(b,c){var a;for(a=0;a<b.b;++a){if(b.a[a]===c){return a;}}return (-1);}
function zn(d,e,a){var b,c;if(a<0||a>d.b){throw new pr();}if(d.b==d.a.a){c=dd('[Lcom.google.gwt.user.client.ui.Widget;',[0],[9],[d.a.a*2],null);for(b=0;b<d.a.a;++b){ed(c,b,d.a[b]);}d.a=c;}++d.b;for(b=d.b-1;b>a;--b){ed(d.a,b,d.a[b-1]);}ed(d.a,a,e);}
function An(a){return qn(new pn(),a);}
function Bn(c,b){var a;if(b<0||b>=c.b){throw new pr();}--c.b;for(a=b;a<c.b;++a){ed(c.a,a,c.a[a+1]);}ed(c.a,c.b,null);}
function Cn(b,c){var a;a=yn(b,c);if(a==(-1)){throw new zx();}Bn(b,a);}
function on(){}
_=on.prototype=new Er();_.tI=0;_.a=null;_.b=0;function qn(b,a){b.b=a;return b;}
function sn(){return this.a<this.b.b-1;}
function tn(){if(this.a>=this.b.b){throw new zx();}return this.b.a[++this.a];}
function pn(){}
_=pn.prototype=new Er();_.w=sn;_.A=tn;_.tI=0;_.a=(-1);function pp(){pp=Dx;aq=ts('abcdefghijklmnopqrstuvwxyz');}
function mp(a){a.a=iq(new dq());}
function np(a){pp();mp(a);return a;}
function op(a){rg(lo(new ko(),a));}
function qp(c,a){var b;b=jb(a);return (ib(a)==200||ib(a)==203||ib(a)<100)&&b!==null&& !js(b,'');}
function rp(e,d){var a,c,f;f=o()+'/appendix'+id(aq[d])+'.html';c=sb(new ob(),(ub(),xb),f);try{vb(c,null,dp(new cp(),e,d,f));}catch(a){a=rd(a);if(kd(a,14)){}else throw a;}}
function sp(e,d){var a,c,f;f=o()+'/exercise'+d+'.html';c=sb(new ob(),(ub(),xb),f);try{vb(c,null,po(new oo(),e,d,f));}catch(a){a=rd(a);if(kd(a,14)){a;vp(e);}else throw a;}}
function tp(d){var a,c,e;e=o()+'/intro.html';c=sb(new ob(),(ub(),xb),e);try{vb(c,null,zo(new yo(),d,e));}catch(a){a=rd(a);if(kd(a,14)){a;sp(d,0);}else throw a;}}
function up(e,d){var a,c,f;f=o()+'/solution'+d+'.html';c=sb(new ob(),(ub(),xb),f);try{vb(c,null,uo(new to(),e,d,f));}catch(a){a=rd(a);if(kd(a,14)){a;sp(e,d+1);}else throw a;}}
function vp(d){var a,c,e;e=o()+'/summary.html';c=sb(new ob(),(ub(),xb),e);try{vb(c,null,Eo(new Do(),d,e));}catch(a){a=rd(a);if(kd(a,14)){a;Ep(d);rp(d,0);}else throw a;}}
function wp(e,d,f){var a,c;c=sb(new ob(),(ub(),xb),f);try{vb(c,null,ip(new hp(),e,d));}catch(a){a=rd(a);if(kd(a,14)){}else throw a;}}
function xp(d,c){var a,b,e,f;b=os(c,',');for(a=0;a<b.a;a++){if(!js(b[a],'')){e=Cp(d,b[a]);f=Dp(d,b[a]);nq(d.a,b[a],e,null);if(f!==null&& !js(f,'')){wp(d,b[a],f);}}}}
function yp(b,a){if(js(a,'Clear')){Ap(b);}uq(b.a,a);}
function zp(c){var a,b;a=Al('j1holframe');if(a===null){a=zl();}Am(c.a.e,'j1holtabbar');rm(c.a.e,'d7v0');Ei(a,c.a.e);Ei(a,c.a.a);uf(c);b=null;if(js(xf(),'Clear')){Ap(c);}else{b=Bp(c);}if(b!==null&& !js(b,'')){xp(c,b);Ep(c);}else{tp(c);}op(c);}
function Ap(d){var a,b,c;c=yd('j1holtablist');if(c!==null&& !js(c,'')){b=os(c,',');for(a=0;a<b.a;a++){if(!js(b[a],'')){Cd('j1holtab.'+b[a]);}}Cd('j1holtablist');}}
function Bp(b){var a;a=yd('j1holtablist');return a;}
function Cp(d,c){var a,b;a=yd('j1holtab.'+c);b=ks(a,124);if(b==(-1)){b=ns(a);}return ss(a,0,b);}
function Dp(d,c){var a,b;a=yd('j1holtab.'+c);b=ks(a,124)+1;if(b==(-1)){b=0;}return rs(a,b);}
function Ep(a){var b;b=xf();if(ns(b)>0){yp(a,b);}else{tq(a.a,0);}bq();}
function Fp(f,c,a){var b,d,e,g;e=yd('j1holtablist');d=null;if(e===null||js(e,'')){d=','+c+',';}else if(ls(e,','+c+',')<0){d=e+c+',';}b=qq(f.a,c);g=c;if(b>=0){g=rq(f.a,b);}if(d!==null){Ed('j1holtablist',d);Ed('j1holtab.'+c,g+'|'+a);}}
function bq(){pp();var f=$doc.getElementsByTagName('span');for(var c=0;c<f.length;c++){var e=f[c];if(e.className=='collapsed'||e.classname=='uncollapsed'){var b=$doc.createElement('div');var a=$doc.createElement('div');var d=e.parentNode;if(e.className=='collapsed'){e.className='xcollapsed';}else{e.className='xuncollapsed';}b.spanElement=e;b.className='collapseboxclosed';b.onclick=function(){if(this.spanElement.className=='xcollapsed'){this.spanElement.className='xuncollapsed';this.className='collapseboxopen';}else if(this.spanElement.className=='xuncollapsed'){this.spanElement.className='xcollapsed';this.className='collapseboxclosed';}};a.className='collapsewidget';b.appendChild(a);d.insertBefore(b,e);}}}
function cq(a){yp(this,a);}
function jo(){}
_=jo.prototype=new Er();_.D=cq;_.tI=30;_.b=0;var aq;function lo(b,a){b.a=a;return b;}
function no(b,a){if(b!=this.a.b){sq(this.a.a,false);this.a.b=b;}}
function ko(){}
_=ko.prototype=new Er();_.db=no;_.tI=31;function po(b,a,c,d){b.a=a;b.b=c;b.c=d;return b;}
function ro(a,b){vp(this.a);}
function so(a,b){if(qp(this.a,b)){kq(this.a.a,'Exercise_'+this.b,jb(b));Fp(this.a,'Exercise_'+this.b,this.c);up(this.a,this.b);}else{vp(this.a);}}
function oo(){}
_=oo.prototype=new Er();_.C=ro;_.F=so;_.tI=0;function uo(b,a,c,d){b.a=a;b.b=c;b.c=d;return b;}
function wo(a,b){sp(this.a,this.b+1);}
function xo(a,b){if(qp(this.a,b)){kq(this.a.a,'Solution_'+this.b,jb(b));Fp(this.a,'Solution_'+this.b,this.c);}sp(this.a,this.b+1);}
function to(){}
_=to.prototype=new Er();_.C=wo;_.F=xo;_.tI=0;function zo(b,a,c){b.a=a;b.b=c;return b;}
function Bo(a,b){sp(this.a,0);}
function Co(b,c){var a,d;if(qp(this.a,c)){kq(this.a.a,'Intro',jb(c));Fp(this.a,'Intro',this.b);a=qe('j1holtitleid');if(a!==null){d=we(a);if(d!==null&& !js(d,'')){Dg(d);}}}sp(this.a,0);}
function yo(){}
_=yo.prototype=new Er();_.C=Bo;_.F=Co;_.tI=0;function Eo(b,a,c){b.a=a;b.b=c;return b;}
function ap(a,b){Ep(this.a);rp(this.a,0);}
function bp(a,b){if(qp(this.a,b)){kq(this.a.a,'Summary',jb(b));Fp(this.a,'Summary',this.b);}Ep(this.a);rp(this.a,0);}
function Do(){}
_=Do.prototype=new Er();_.C=ap;_.F=bp;_.tI=0;function dp(b,a,c,d){b.a=a;b.b=c;b.c=d;return b;}
function fp(a,b){}
function gp(a,b){if(qp(this.a,b)){kq(this.a.a,'Appendix_'+id(br((pp(),aq)[this.b])),jb(b));Fp(this.a,'Appendix_'+id(br((pp(),aq)[this.b])),this.c);}rp(this.a,this.b+1);}
function cp(){}
_=cp.prototype=new Er();_.C=fp;_.F=gp;_.tI=0;function ip(b,a,c){b.a=a;b.b=c;return b;}
function kp(a,b){}
function lp(a,b){if(qp(this.a,b)){vq(this.a.a,this.b,jb(b));bq();}}
function hp(){}
_=hp.prototype=new Er();_.C=kp;_.F=lp;_.tI=0;function hq(a){a.e=gn(new en());a.a=xj(new wj());a.c=kv(new iv());a.d=kv(new iv());}
function iq(b){var a;hq(b);a=vk(new tk());Bk(a,(ok(),pk));mv(b.d,a);hn(b.e,a);return b;}
function kq(c,b,a){lq(c,b,a,c.c.b);}
function nq(d,b,e,a){var c;c=a;if(c===null){c='<p class="xxbig j1holwarn centertext">LOADING...<\/p>';}oq(d,b,e,c,d.c.b);}
function lq(e,d,a,c){var b,f;b=wq(a);f=zq(b);if(f===null){f=Aq(d);}mq(e,d,f,b,c);}
function oq(d,c,e,a,b){mq(d,c,e,wq(a),b);}
function mq(f,c,g,a,b){var d,e;d=xq(a);e=yq(g,c);jq(f,e);yj(f.a,d);lv(f.c,b,fq(new eq(),c,g,e,d,a,f));if(f.c.b==1){qm(e,'selected');Cj(f.a,0);}else{vm(e,'selected');}}
function jq(b,a){wk(jd(pv(b.d,b.d.b-1),15),a);sq(b,true);}
function qq(d,c){var a,b;b=(-1);for(a=0;a<d.c.b;a++){if(js(jd(pv(d.c,a),16).b,c)){b=a;break;}else if(qs(c,jd(pv(d.c,a),16).b+'=')){b=a;break;}}return b;}
function rq(b,a){return jd(pv(b.c,a),16).d;}
function sq(f,c){var a,b,d,e,g;for(b=f.d.b-1;b>=0;b--){a=jd(pv(f.d,b),15);if(tm(a)>xg()){e=null;if(b>0){e=jd(pv(f.d,b-1),15);}else if(a.f.b>1){e=vk(new tk());lv(f.d,0,e);ln(f.e,e,0);b++;}while(a.f.b>1&&tm(a)>xg()){g=qj(a,0);sj(a,0);wk(e,g);}}else if(!c){e=null;d=b-1;if(d>=0){e=jd(pv(f.d,d),15);}else{break;}while(tm(a)<xg()){if(e.f.b>0){g=qj(e,e.f.b-1);Ak(e,g);zk(a,g,0);}else if(d>0){d--;e=jd(pv(f.d,d),15);}else{break;}}if(tm(a)>xg()){g=qj(a,0);sj(a,0);wk(e,g);}}else{break;}}while(!c){if(jd(pv(f.d,0),15).f.b==0){sv(f.d,0);sj(f.e,0);}else{break;}}}
function uq(d,b){var a,c;a=qq(d,b);if(a>=0){tq(d,a);c=ks(b,61);if(c>=1){vf();Af(rs(b,c+1));}}}
function tq(d,b){var a,c;if(d.b!=b){a=jd(pv(d.c,d.b),16);vm(a.c,'selected');d.b=b;c=jd(pv(d.c,b),16);qm(c.c,'selected');Cj(d.a,b);}}
function vq(e,d,a){var b,c;c=qq(e,d);if(c>=0){b=jd(pv(e.c,c),16);ck(b.a,a);}}
function wq(a){var b;b=ak(new Ej(),a);zm(b,'j1holpanel');return b;}
function xq(a){var b,c,d,e;d=em(new Dl());e=em(new Dl());b=em(new Dl());c=em(new Dl());zm(d,'d7');zm(e,'d7v4');zm(b,'cornerBL');zm(c,'cornerBR');gm(c,a);gm(b,c);gm(e,b);gm(d,e);return d;}
function yq(b,d){var a,c;c=em(new Dl());a=Fk(new Dk(),b,d);zm(c,'j1holtab');gm(c,a);zm(a,'j1holtablink');return c;}
function zq(d){var a,b,c,e;e=null;a=d.i;b=ue(a);while(b!==null){c=pe(b,'name');if(c!==null&&is(c,'j1holtabname')){e=pe(b,'content');break;}else{b=xe(b);}}return e;}
function Aq(c){var a,b;b=c;a=(-1);while((a=ks(b,95))>=0){if(a==0){b=rs(b,1);}else{b=ss(b,0,a)+id(32)+rs(b,a+1);}}return b;}
function dq(){}
_=dq.prototype=new Er();_.tI=0;_.b=0;function fq(f,b,g,d,c,a,e){f.b=b;f.d=g;f.c=d;f.a=a;return f;}
function eq(){}
_=eq.prototype=new Er();_.tI=32;_.a=null;_.b=null;_.c=null;_.d=null;function Cq(){}
_=Cq.prototype=new cs();_.tI=33;function br(a){return String.fromCharCode(a).toUpperCase().charCodeAt(0);}
function cr(){}
_=cr.prototype=new cs();_.tI=34;function kr(b,a){ds(b,a);return b;}
function jr(){}
_=jr.prototype=new cs();_.tI=35;function nr(b,a){ds(b,a);return b;}
function mr(){}
_=mr.prototype=new cs();_.tI=36;function qr(b,a){ds(b,a);return b;}
function pr(){}
_=pr.prototype=new cs();_.tI=37;function Br(){Br=Dx;{Dr();}}
function Dr(){Br();Cr=/^[+-]?\d*\.?\d*(e[+-]?\d+)?$/i;}
var Cr=null;function tr(){tr=Dx;Br();}
function ur(a){tr();return As(a);}
function vr(){}
_=vr.prototype=new cs();_.tI=38;function yr(b,a){ds(b,a);return b;}
function xr(){}
_=xr.prototype=new cs();_.tI=39;function gs(b,a){return b.charCodeAt(a);}
function js(b,a){if(!kd(a,1))return false;return ws(b,a);}
function is(b,a){if(a==null)return false;return b==a||b.toLowerCase()==a.toLowerCase();}
function ks(b,a){return b.indexOf(String.fromCharCode(a));}
function ls(b,a){return b.indexOf(a);}
function ms(c,b,a){return c.indexOf(b,a);}
function ns(a){return a.length;}
function os(b,a){return ps(b,a,0);}
function ps(j,i,g){var a=new RegExp(i,'g');var h=[];var b=0;var k=j;var e=null;while(true){var f=a.exec(k);if(f==null||(k==''||b==g-1&&g>0)){h[b]=k;break;}else{h[b]=k.substring(0,f.index);k=k.substring(f.index+f[0].length,k.length);a.lastIndex=0;if(e==k){h[b]=k.substring(0,1);k=k.substring(1);}e=k;b++;}}if(g==0){for(var c=h.length-1;c>=0;c--){if(h[c]!=''){h.splice(c+1,h.length-(c+1));break;}}}var d=vs(h.length);var c=0;for(c=0;c<h.length;++c){d[c]=h[c];}return d;}
function qs(b,a){return ls(b,a)==0;}
function rs(b,a){return b.substr(a,b.length-a);}
function ss(c,a,b){return c.substr(a,b-a);}
function ts(d){var a,b,c;c=ns(d);a=dd('[C',[0],[(-1)],[c],0);for(b=0;b<c;++b)a[b]=gs(d,b);return a;}
function us(c){var a=c.replace(/^(\s*)/,'');var b=a.replace(/\s*$/,'');return b;}
function vs(a){return dd('[Ljava.lang.String;',[0],[1],[a],null);}
function ws(a,b){return String(a)==b;}
function xs(a){return js(this,a);}
function zs(){var a=ys;if(!a){a=ys={};}var e=':'+this;var b=a[e];if(b==null){b=0;var f=this.length;var d=f<64?1:f/32|0;for(var c=0;c<f;c+=d){b<<=1;b+=this.charCodeAt(c);}b|=0;a[e]=b;}return b;}
function As(a){return ''+a;}
_=String.prototype;_.eQ=xs;_.hC=zs;_.tI=2;var ys=null;function Ds(a){return t(a);}
function dt(b,a){ds(b,a);return b;}
function ct(){}
_=ct.prototype=new cs();_.tI=40;function gt(d,a,b){var c;while(a.w()){c=a.A();if(b===null?c===null:b.eQ(c)){return a;}}return null;}
function it(a){throw dt(new ct(),'add');}
function jt(b){var a;a=gt(this,this.x(),b);return a!==null;}
function ft(){}
_=ft.prototype=new Er();_.k=it;_.m=jt;_.tI=0;function ut(b,a){throw qr(new pr(),'Index: '+a+', Size: '+b.b);}
function vt(a){return mt(new lt(),a);}
function wt(b,a){throw dt(new ct(),'add');}
function xt(a){this.j(this.jb(),a);return true;}
function yt(e){var a,b,c,d,f;if(e===this){return true;}if(!kd(e,17)){return false;}f=jd(e,17);if(this.jb()!=f.jb()){return false;}c=vt(this);d=f.x();while(ot(c)){a=pt(c);b=pt(d);if(!(a===null?b===null:a.eQ(b))){return false;}}return true;}
function zt(){var a,b,c,d;c=1;a=31;b=vt(this);while(ot(b)){d=pt(b);c=31*c+(d===null?0:d.hC());}return c;}
function At(){return vt(this);}
function Bt(a){throw dt(new ct(),'remove');}
function kt(){}
_=kt.prototype=new ft();_.j=wt;_.k=xt;_.eQ=yt;_.hC=zt;_.x=At;_.fb=Bt;_.tI=41;function mt(b,a){b.c=a;return b;}
function ot(a){return a.a<a.c.jb();}
function pt(a){if(!ot(a)){throw new zx();}return a.c.u(a.b=a.a++);}
function qt(a){if(a.b<0){throw new mr();}a.c.fb(a.b);a.a=a.b;a.b=(-1);}
function rt(){return ot(this);}
function st(){return pt(this);}
function lt(){}
_=lt.prototype=new Er();_.w=rt;_.A=st;_.tI=0;_.a=0;_.b=(-1);function Au(f,d,e){var a,b,c;for(b=ww(f.p());pw(b);){a=qw(b);c=a.s();if(d===null?c===null:d.eQ(c)){if(e){rw(b);}return a;}}return null;}
function Bu(b){var a;a=b.p();return Et(new Dt(),b,a);}
function Cu(b){var a;a=ax(b);return mu(new lu(),b,a);}
function Du(a){return Au(this,a,false)!==null;}
function Eu(d){var a,b,c,e,f,g,h;if(d===this){return true;}if(!kd(d,18)){return false;}f=jd(d,18);c=Bu(this);e=f.y();if(!fv(c,e)){return false;}for(a=au(c);hu(a);){b=iu(a);h=this.v(b);g=f.v(b);if(h===null?g!==null:!h.eQ(g)){return false;}}return true;}
function Fu(b){var a;a=Au(this,b,false);return a===null?null:a.t();}
function av(){var a,b,c;b=0;for(c=ww(this.p());pw(c);){a=qw(c);b+=a.hC();}return b;}
function bv(){return Bu(this);}
function cv(a,b){throw dt(new ct(),'This map implementation does not support modification');}
function Ct(){}
_=Ct.prototype=new Er();_.l=Du;_.eQ=Eu;_.v=Fu;_.hC=av;_.y=bv;_.eb=cv;_.tI=42;function fv(e,b){var a,c,d;if(b===e){return true;}if(!kd(b,19)){return false;}c=jd(b,19);if(c.jb()!=e.jb()){return false;}for(a=c.x();a.w();){d=a.A();if(!e.m(d)){return false;}}return true;}
function gv(a){return fv(this,a);}
function hv(){var a,b,c;a=0;for(b=this.x();b.w();){c=b.A();if(c!==null){a+=c.hC();}}return a;}
function dv(){}
_=dv.prototype=new ft();_.eQ=gv;_.hC=hv;_.tI=43;function Et(b,a,c){b.a=a;b.b=c;return b;}
function au(b){var a;a=ww(b.b);return fu(new eu(),b,a);}
function bu(a){return this.a.l(a);}
function cu(){return au(this);}
function du(){return this.b.a.c;}
function Dt(){}
_=Dt.prototype=new dv();_.m=bu;_.x=cu;_.jb=du;_.tI=44;function fu(b,a,c){b.a=c;return b;}
function hu(a){return pw(a.a);}
function iu(b){var a;a=qw(b.a);return a.s();}
function ju(){return hu(this);}
function ku(){return iu(this);}
function eu(){}
_=eu.prototype=new Er();_.w=ju;_.A=ku;_.tI=0;function mu(b,a,c){b.a=a;b.b=c;return b;}
function ou(b){var a;a=ww(b.b);return tu(new su(),b,a);}
function pu(a){return Fw(this.a,a);}
function qu(){return ou(this);}
function ru(){return this.b.a.c;}
function lu(){}
_=lu.prototype=new ft();_.m=pu;_.x=qu;_.jb=ru;_.tI=0;function tu(b,a,c){b.a=c;return b;}
function vu(a){return pw(a.a);}
function wu(a){var b;b=qw(a.a).t();return b;}
function xu(){return vu(this);}
function yu(){return wu(this);}
function su(){}
_=su.prototype=new Er();_.w=xu;_.A=yu;_.tI=0;function jv(a){{nv(a);}}
function kv(a){jv(a);return a;}
function lv(c,a,b){if(a<0||a>c.b){ut(c,a);}uv(c.a,a,b);++c.b;}
function mv(b,a){Dv(b.a,b.b++,a);return true;}
function nv(a){a.a=E();a.b=0;}
function pv(b,a){if(a<0||a>=b.b){ut(b,a);}return zv(b.a,a);}
function qv(b,a){return rv(b,a,0);}
function rv(c,b,a){if(a<0){ut(c,a);}for(;a<c.b;++a){if(yv(b,zv(c.a,a))){return a;}}return (-1);}
function sv(c,a){var b;b=pv(c,a);Bv(c.a,a,1);--c.b;return b;}
function tv(c,b){var a;a=qv(c,b);if(a==(-1)){return false;}sv(c,a);return true;}
function vv(a,b){lv(this,a,b);}
function wv(a){return mv(this,a);}
function uv(a,b,c){a.splice(b,0,c);}
function xv(a){return qv(this,a)!=(-1);}
function yv(a,b){return a===b||a!==null&&a.eQ(b);}
function Av(a){return pv(this,a);}
function zv(a,b){return a[b];}
function Cv(a){return sv(this,a);}
function Bv(a,c,b){a.splice(c,b);}
function Dv(a,b,c){a[b]=c;}
function Ev(){return this.b;}
function iv(){}
_=iv.prototype=new kt();_.j=vv;_.k=wv;_.m=xv;_.u=Av;_.fb=Cv;_.jb=Ev;_.tI=45;_.a=null;_.b=0;function Dw(){Dw=Dx;ex=kx();}
function Aw(a){{Cw(a);}}
function Bw(a){Dw();Aw(a);return a;}
function Cw(a){a.a=E();a.d=ab();a.b=od(ex,A);a.c=0;}
function Ew(b,a){if(kd(a,1)){return ox(b.d,jd(a,1))!==ex;}else if(a===null){return b.b!==ex;}else{return nx(b.a,a,a.hC())!==ex;}}
function Fw(a,b){if(a.b!==ex&&mx(a.b,b)){return true;}else if(jx(a.d,b)){return true;}else if(hx(a.a,b)){return true;}return false;}
function ax(a){return uw(new lw(),a);}
function bx(c,a){var b;if(kd(a,1)){b=ox(c.d,jd(a,1));}else if(a===null){b=c.b;}else{b=nx(c.a,a,a.hC());}return b===ex?null:b;}
function cx(c,a,d){var b;if(kd(a,1)){b=rx(c.d,jd(a,1),d);}else if(a===null){b=c.b;c.b=d;}else{b=qx(c.a,a,d,a.hC());}if(b===ex){++c.c;return null;}else{return b;}}
function dx(c,a){var b;if(kd(a,1)){b=ux(c.d,jd(a,1));}else if(a===null){b=c.b;c.b=od(ex,A);}else{b=tx(c.a,a,a.hC());}if(b===ex){return null;}else{--c.c;return b;}}
function fx(e,c){Dw();for(var d in e){if(d==parseInt(d)){var a=e[d];for(var f=0,b=a.length;f<b;++f){c.k(a[f]);}}}}
function gx(d,a){Dw();for(var c in d){if(c.charCodeAt(0)==58){var e=d[c];var b=fw(c.substring(1),e);a.k(b);}}}
function hx(f,h){Dw();for(var e in f){if(e==parseInt(e)){var a=f[e];for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.t();if(mx(h,d)){return true;}}}}return false;}
function ix(a){return Ew(this,a);}
function jx(c,d){Dw();for(var b in c){if(b.charCodeAt(0)==58){var a=c[b];if(mx(d,a)){return true;}}}return false;}
function kx(){Dw();}
function lx(){return ax(this);}
function mx(a,b){Dw();if(a===b){return true;}else if(a===null){return false;}else{return a.eQ(b);}}
function px(a){return bx(this,a);}
function nx(f,h,e){Dw();var a=f[e];if(a){for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.s();if(mx(h,d)){return c.t();}}}}
function ox(b,a){Dw();return b[':'+a];}
function sx(a,b){return cx(this,a,b);}
function qx(f,h,j,e){Dw();var a=f[e];if(a){for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.s();if(mx(h,d)){var i=c.t();c.ib(j);return i;}}}else{a=f[e]=[];}var c=fw(h,j);a.push(c);}
function rx(c,a,d){Dw();a=':'+a;var b=c[a];c[a]=d;return b;}
function tx(f,h,e){Dw();var a=f[e];if(a){for(var g=0,b=a.length;g<b;++g){var c=a[g];var d=c.s();if(mx(h,d)){if(a.length==1){delete f[e];}else{a.splice(g,1);}return c.t();}}}}
function ux(c,a){Dw();a=':'+a;var b=c[a];delete c[a];return b;}
function bw(){}
_=bw.prototype=new Ct();_.l=ix;_.p=lx;_.v=px;_.eb=sx;_.tI=46;_.a=null;_.b=null;_.c=0;_.d=null;var ex;function dw(b,a,c){b.a=a;b.b=c;return b;}
function fw(a,b){return dw(new cw(),a,b);}
function gw(b){var a;if(kd(b,20)){a=jd(b,20);if(mx(this.a,a.s())&&mx(this.b,a.t())){return true;}}return false;}
function hw(){return this.a;}
function iw(){return this.b;}
function jw(){var a,b;a=0;b=0;if(this.a!==null){a=this.a.hC();}if(this.b!==null){b=this.b.hC();}return a^b;}
function kw(a){var b;b=this.b;this.b=a;return b;}
function cw(){}
_=cw.prototype=new Er();_.eQ=gw;_.s=hw;_.t=iw;_.hC=jw;_.ib=kw;_.tI=47;_.a=null;_.b=null;function uw(b,a){b.a=a;return b;}
function ww(a){return nw(new mw(),a.a);}
function xw(c){var a,b,d;if(kd(c,20)){a=jd(c,20);b=a.s();if(Ew(this.a,b)){d=bx(this.a,b);return mx(a.t(),d);}}return false;}
function yw(){return ww(this);}
function zw(){return this.a.c;}
function lw(){}
_=lw.prototype=new dv();_.m=xw;_.x=yw;_.jb=zw;_.tI=48;function nw(c,b){var a;c.c=b;a=kv(new iv());if(c.c.b!==(Dw(),ex)){mv(a,dw(new cw(),null,c.c.b));}gx(c.c.d,a);fx(c.c.a,a);c.a=vt(a);return c;}
function pw(a){return ot(a.a);}
function qw(a){return a.b=jd(pt(a.a),20);}
function rw(a){if(a.b===null){throw nr(new mr(),'Must call next() before remove().');}else{qt(a.a);dx(a.c,a.b.s());a.b=null;}}
function sw(){return pw(this);}
function tw(){return qw(this);}
function mw(){}
_=mw.prototype=new Er();_.w=sw;_.A=tw;_.tI=0;_.a=null;_.b=null;function zx(){}
_=zx.prototype=new cs();_.tI=49;function Bq(){zp(np(new jo()));}
function gwtOnLoad(b,d,c){$moduleName=d;$moduleBase=c;if(b)try{Bq();}catch(a){b(d);}else{Bq();}}
var nd=[{},{},{1:1},{3:1},{3:1},{3:1},{3:1},{2:1},{6:1},{6:1},{3:1,14:1},{3:1,14:1},{3:1,14:1},{2:1,4:1},{2:1},{7:1},{9:1,11:1,12:1,13:1},{9:1,11:1,12:1,13:1},{9:1,11:1,12:1,13:1},{9:1,11:1,12:1,13:1},{9:1,11:1,12:1,13:1},{9:1,11:1,12:1,13:1},{9:1,11:1,12:1,13:1},{9:1,11:1,12:1,13:1},{9:1,11:1,12:1,13:1,15:1},{9:1,11:1,12:1,13:1},{9:1,10:1,11:1,12:1,13:1},{7:1},{9:1,11:1,12:1,13:1},{9:1,11:1,12:1,13:1},{5:1},{8:1},{16:1},{3:1},{3:1},{3:1},{3:1},{3:1},{3:1},{3:1},{3:1},{17:1},{18:1},{19:1},{19:1},{17:1},{18:1},{20:1},{19:1},{3:1}];if (com_sun_javaone_HoLTemplate) {  var __gwt_initHandlers = com_sun_javaone_HoLTemplate.__gwt_initHandlers;  com_sun_javaone_HoLTemplate.onScriptLoad(gwtOnLoad);}})();