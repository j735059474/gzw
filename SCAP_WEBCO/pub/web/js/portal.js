// JavaScript Document
//Portlet链接地址前缀
var link_Pre = "P_U_R_L_";
var tipShowFlag = false;
var currentTipMeta = "";
$(function() {
	window.NCPortalSupportPortletMode = ["view","edit","help"];
	window.NCPortalSupportPortletModeName = {view:trans("ml_portlet_mode_view"),edit:trans("ml_portlet_mode_edit"),help:trans("ml_portlet_mode_help")};
	if(DESIGN_MODE == "N"){
		//初始化定时刷新Portlet
		initRefreshPortlet();
		//处理Portlet链接地址
		disposeURL ();
		//初始化拖动
		initDragable();
		//初始化Portlet容器工具条
		initTips();
		
		initHeartBeat();
		RollEventFixed();
	}else{
		initPageDesigner();
	}
	//解决IE多线程问题
	IECorrect();
	fixIE7ScrollBug();
	/**
	 * 初始化消息提醒
	 */
	initMsgTip();
});
/**
 * 解决IE7、IS_IE9下滚动条问题
 */
function fixIE7ScrollBug(){
	if(IS_IE7)
		$(document).find("html").get(0).style.overflowY = "visible";//ie7下"<html>"纵向滚动条不显示
	if(IS_IE7 || IS_IE9){
		document.body.style.position = "relative";
	}else if (IS_IPAD){
		document.body.style.WebkitOverflowScrolling = "touch";
	}
	else{
		//document.body.style.overflowY = "auto";
		//浏览器非最大化时浮动菜单双滚动条,覆盖global.css里的 overflow-y:auto
		document.body.style.overflowY = "visible";
	}
//	var _body = $(document.body);
//  	_body.width(_body.width());
//  	_body.attr("oriWidth", _body.width());
//  	
//  	_body.attr("oriHeight", _body.height());
//  	_body.attr("windowHeight", getWinSize().WinH);
}
function getWinSize() {
	  var winW, winH;
	  if(window.innerHeight) { // all except IE
	    winW = window.innerWidth;
	    winH = window.innerHeight;
	  } else if (document.documentElement && document.documentElement.clientHeight){
	    // IE 6 Strict Mode
	    winW = document.documentElement.clientWidth; 
	    winH = document.documentElement.clientHeight;
	  } else if (document.body) { //other
	    winW = document.body.clientWidth;
	    winH = document.body.clientHeight;
	  }
	  return {WinW:winW, WinH:winH};
}

/**
 * 为当前窗口绑定心跳事件
 */
function initHeartBeat(){
	EventUtil.addEventHandler(window, "unload", function(){
		var ajax = new Ajax();
		ajax.setPath("/portal/pt/home/stopBreat");
		ajax.get(false);
	});
}
/**
 * 解决滚轮和浮动对象一起移动的事件
 */
function RollEventFixed(){
	if(document.attachEvent){
		document.attachEvent("onmousewheel",mouseScrollFunc);
	}else if(document.addEventListener){ 
		document.addEventListener('DOMMouseScroll',mouseScrollFunc,false); 
	} 
	EventUtil.addEventHandler(document.body,'scroll', mouseScrollFunc);
}

function mouseScrollFunc(){
	var e = EventUtil.getEvent();
	var tar = getTarget(e);
	if(tar.tagName && "MARQUEE" == tar.tagName)
		return false;
	var tipspanel = document.getElementById("tipspanel");
	if(tipspanel)
		tipspanel.style.display = "none";
	$(".blankPortlet .content .divBody").attr("placein","0").hide();
}

/**Portlet刷新关系**/
var refresh_circle = {};
/**
 * 初始化Portlet容器工具条
 */
function initTips(){
	if(CUR_PPAGE_READONLY == "false"){
		//初始化双击隐藏事件
		$("[tp=pBody]").mouseover(function(){
			//var _pt =  $(this).parents("[tp=portlet]");
			if(tipShowFlag && currentTipMeta != "")
			getContainer("#"+currentTipMeta).hideTips();
		});
		//初始化工具条显示鼠标事件
		$("[tp='portlet']").find("[tp=pHead]").mouseover(function(){
			var _pt =  $(this).parents("[tp=portlet]");
		    if(!tipShowFlag || (currentTipMeta != "" && currentTipMeta != _pt.attr("id")))
			   getContainer(_pt).showTips();
		});
		//右边栏不触发工具条
		$("[tp='portlet']").find("[tp='pPart']").mouseover(function(){
			return false;
		});
	}
}


/**
 * 初始化定时刷新Portlet
 */
function initRefreshPortlet(){
	setInterval(checkPortletRefresh, 1000);
}

/**
 * 处理定时刷新Portlet
 */
function checkPortletRefresh(){
	ensureUserStateSecurity();
	for( xa in refresh_circle){
		 var ctn = getContainer("#"+xa);
		 if(!ctn.ModifiedSince || ctn.ModifiedSince < (new Date()).getTime()-refresh_circle[xa]*1000){
		 	ctn.doView();
		 	ctn.ModifiedSince = (new Date()).getTime();
		 }
	}
}
function ensureUserStateSecurity(){
	try{
		var p_userId = decodeURIComponent(getCookie("p_userId"));
		var p_logoutflag = getCookie("p_logoutflag");
		if((p_userId != null && p_userId != "" && window.usercode.toLowerCase() != p_userId.toLowerCase()) || "y" == p_logoutflag){
			sendRedirect("/portal/app/mockapp/login.jsp?lrid=1");
		}
	}catch(e){
		
	}
	try{
		var p_forcelogoutflag = getCookie("p_forcelogoutflag");
		if(p_forcelogoutflag != null && p_forcelogoutflag == "1" ){
			deleteCookie("p_forcelogoutflag", "/portal");
			showErrorDialog("由于当前用户在其他客户端强制登录，本客户端将退出工作台", function(){sendRedirect("/portal/pt/home/logout");},"提示","确定");
		}
	}catch(e){
	
	}
}

/**
初始化Iframe中的Portlet环境
**/
function initIframeEnv(){
		//为Iframe注册getContainer方法
	var container = $(this).parents("[tp='portlet']");
	//初始化Container  顺便取其ID
	var containerId = getContainer(container).id;
	
	try {
		//注册ContainerID
		$(this).get(0).contentWindow.document._pt_container_id=containerId;
		//注册getContainer()方法
		$(this).get(0).contentWindow.document.getContainer=getIframeContainer;
		$(this).get(0).contentWindow.document._pt_frame_id = $(this).get(0).id;
		if($(this).attr("flowMode") == "1"){
 			setTimeout("initFrameMiniHeight('#"+ $(this).get(0).id +"')",300);
		}
		
	} catch (e) {
		// 跨域 抛弃
		}
}

function initFrameMiniHeight(frameId){
	var hScorll = window.document.body.offsetHeight -	window.document.body.children[0].scrollHeight;
				if(hScorll > 0){
					var minHeight = $(frameId).height() + hScorll;
					if(DESIGN_MODE == "Y"){
						minHeight = minHeight/2;
					}
					$(frameId).attr("minHeight", minHeight);
					$(frameId).attr("fullHeight", minHeight);
					$(frameId).height(minHeight);
				}
				adjustIFramesHeightOnLoad($(frameId).get(0)); 				
}

function initParentIframeHeight(){
	try {
		 
		var frame =	parent.getParentsContainer(this._pt_frame_id);
		$(frame).height(window.document.body.scrollHeight);
	} catch (e) {
		 
	}
}

/**
初始化Iframe的高度、宽度
**/
function initIframeArea(iframeId, assignHeight){
	var thisFrame=$("#" + iframeId);
	if (assignHeight == -1) {
		thisFrame.attr("flowMode","1");
		
	} else if (assignHeight == 0) {
		// 适应容器
		$(function() {
			var footObj = $("[tp='foot']");
			
			var container = thisFrame.parents("[tp='portlet']");
			// 页面空隙
			var nheight = $(window).height() ;
			
			if(footObj.length > 0){
				nheight = nheight - footObj.offset().top
					- footObj.outerHeight(true);
			}
			
			nheight = nheight + getContainer(container).getOuter().height()	- container.outerHeight(true);
			// 页面底部仍有空隙
			if (nheight <= 0) {
				nheight = container.innerHeight() - thisFrame.height() - 31;
				if(nheight <= 0){
					// 算出Layout底部与Foot顶的距离
					nheight = getContainer(container).getOuter().height()
						- container.outerHeight(true) ;
				}
				thisFrame.height(nheight + thisFrame.height() );
			} else if (nheight > 0) {
				// 如果距离过小 可能是浏览器多线程加载造成,要检查Layout底与Foot顶距离
				if (nheight < 5) {
					var _t_height = getContainer(container).getOuter().height()
							- container.outerHeight(true) ;
					if (_t_height > 0) {
						thisFrame.height(_t_height + thisFrame.height() );
					}
				} else {
					thisFrame.height(nheight + thisFrame.height() );
					// var mheight= getContainer(container).getOuter().height()
					// - container.outerHeight(true) -5;
					// if(mheight > nheight)
					// thisFrame.attr("height",mheight+thisFrame.height());
				}
			}
		})
	} else {
		//自定义
		thisFrame.attr("height", assignHeight);
	}
	if(DESIGN_MODE == "Y"){
		//thisFrame.css("visibility","hidden");
	}
}

/**
 * 重画iframe
 * @param {} iframeId
 */
function resizeIframe(iframeId,assignHeight){
	var thisFrame=$("#" + iframeId);
	if(assignHeight == -1){
		
	}else if(assignHeight == 0){
		var top = thisFrame.offset().top;
		if(top < 0){
			top = 0;
		}
		var h = $(window).height() - top - 15;
		if(h > 0){
			thisFrame.height(h);
		}
	}else if(assignHeight > 0){
		thisFrame.height(assignHeight); 
	}
}

/**
获得当前Portlet
仅限Iframe内使用
**/
function getParentsContainer(containerId){
	return document.getElementById(containerId);
}

 
/**
Iframe中获得Container
**/
function getIframeContainer(){
	try {
		return	parent.getParentsContainer(this._pt_container_id);
	} catch (e) {
		return	window.getParentsContainer(this._pt_container_id);
	}
}


/**
解决IE多线程加载问题
**/
function IECorrect(){
  //if($.browser.msie){
	  $("iframe").each(function(index,ele){
			if($(ele).attr("src")==undefined||$(ele).attr("src")==""){
				$(ele).attr("src",$(ele).attr("scr"));	
			}
	  });
  //}
}
/**
*处理Portlet链接及表单
**/
function  disposeURL (){
	//处理A标签
 	var hrefObjs=[];
	try {
		hrefObjs=$("a[href*="+link_Pre+"]");
	} catch (e) {
		//跨域.抛弃异常
	}
	burnURL(hrefObjs);

//处理表单
	var formObjs=[];
 	try {
 		formObjs=    $("form[action*="+link_Pre+"]");
 	} catch (e) {
		//跨域.抛弃异常
	}
	burnFORM(formObjs);
	disposeFrameURL();
}
/**
*处理Iframe中的Portlet链接及表单
**/
function disposeFrameURL(){
	var fms = window.frames;
	if(fms == null || fms.length <1)
		return;
	try {
		for(var fi = 0; fi < fms.length; fi++){
			var fm = fms[fi];
			if(fm.name =="portlet"){
				$(fm).ready(function (){
					try{
						burnURL($(fm.contentWindow.document).contents().find("a[href*="+link_Pre+"]"));
						burnFORM($(fm.contentWindow.document).contents().find("form[action*="+link_Pre+"]"));
					} catch (e) {
						//跨域.抛弃异常
					}
				})
			}

		}
	} catch (e) {
		// 跨域 抛弃
	}
}


/**
 * 将Portlet链接地址替换为Portlet请求
 * @param hrefObjs 要处理的链接
 */
function burnURL(hrefObjs){
	$.each(hrefObjs,function(i,target){
		var _t_url=getTrueUrl ($(target).attr("href"));
		$(target).click(function(){
			 openPortlet(_t_url) ;
		})
		//去掉Href属性
		$(target).attr("href","javascript:void(0)");
	})
}

/**
 * 将PortletForm替换为Portlet请求
 * @param formObjs 要处理的Form
 */
function burnFORM(formObjs){
	$.each(formObjs,function(i,target){
		var _t_url=getTrueUrl ($(target).attr("action"));
		$(target).submit(function(){
			openPortlet(_t_url,$(target).serializeArray());
			//直接返回
			return false;
		})
		//去掉action以净化状态栏
		$(target).attr("action","");
	})
}


/**
得到真实的PortletURL
@param URL Tag生成的伪URL
**/
function getTrueUrl(fakeurl){
	var start=fakeurl.indexOf(link_Pre)+8;
	var _t_url=fakeurl.substr(start);
	return _t_url;
}
/**
处理Portlet请求
使用AJAX方式
@url 请求地址
@fdata 序列化的表单(可选参数)
**/
function openPortlet(url,fdata,fn){
$.ajax({
	type: "GET",
	url: url,
	data:fdata,
	cache:false,
	error: function(XMLHttpRequest, textStatus, errorThrown){
		if(XMLHttpRequest.status == 306){
			window.location = ROOT_PATH;
		}
	},
    success: function(data){
	    if(data && typeof  data == "object"){
			if(data.err){
				alert(data.err);
				eval(data.exec);
			}else{
				$(data).each(function (index, el) {
					var protocol = el[RESPONSE_PROTOCOL];
					if(protocol == RESPONSE_MODE_SCRIPT){
						eval(el.content);				
					}else{
						try{
							var _ctn = getContainer("#"+el.name);
							_ctn.setCurrentMode(el.mode);
							_ctn.setContent(el.content);
						}catch(e){
							
						}
					}
				 });
				//先加载
				disposeURL ();
				if(fn){
					 if(typeof fn == "string"){
						eval(fn);
					 }else if(typeof fn == "function"){
					 	fn.call(this);
					 }
				}
			}
		}
   }
}); 
}
/**
初始化拖放
**/
function initDragable(){
	//仅在页面非只读状态下初始化拖动
	if(CUR_PPAGE_READONLY != "true"){
		makePageDragable();
	}
}
function makePageDragable(){
	$.baseball({
		accepter:$("[tp='layout']").filter(function(index) {return $("[tp='layout']", this).length == 0;}).add($("[tp='portlet']").siblings("[tp='layout']").parent("[tp='layout']")),
		target:"[tp='portlet']",	
		handle:"[tp='pHead']"
	});
}

/**
 * 创建本地缓存
 */
function createClientCache(key, content){
	try{
		var storage = window.localStorage;
		if(storage){
			storage[key] = content;
		}
	}catch(e){
		if(storage){
			storage.clear();//针对缓存溢出时情况处理
			storage[key] = content;
		}
	}
}

/**
 * 使用本地缓存
 */
function writeClientCache(key){
	var storage = window.localStorage;
	var content = null;
	if(storage){
		content = storage[key];
	}
	if(content){
		document.write(content);
	}else{
		//本地缓存失效.调用服务器端渲染
		var portletId = key.split(":")[1];
		getContainer("#"+portletId).doRestore()
		
	}
}
/**
 * 获取本地缓存
 */
function getLocalCache(key){
	var storage = window.localStorage;
	var content = null;
	if(storage){
		content = storage[key];
	}
	return content;
}
/**内容区是否有变化**/
window.contentHasChanged = false;

function setContentHasChanged(){
	window.contentHasChanged = true;
};
function reSetContentChangeState(){
	window.contentHasChanged = false;
};
function setFrameContent(param){
	if(window.contentHasChanged){
		require("confirmdialog", function(){
			ConfirmDialogComp.showDialog(trans("ml_confirm_pageHasChanged")/*"内容未保存，是否切换页面?"*/, changeFrameContent ,null, param, null);
		});
	}else{
		changeFrameContent(param);
	}
};

function changeFrameContent(param){
	reSetContentChangeState();
	var frameId = param.iframeId;
	var url = param.frameURL;
	var needScroll = param.needScroll;
	var pagecard = param.pagecard;
	var title = param.title;
	
	if(window.$adjustFrameId == frameId){
		window.$adjustFrameId='';
	} 
	var jqFrm = $('#' + frameId);
	 try{
		 jqFrm[0].contentWindow.showDefaultLoadingBar();
	}catch(e){
	};
	jqFrm.attr("src", "about:blank");
	var scrollVal = needScroll ? "yes" : "no";
	jqFrm.attr('scrolling', scrollVal);
	if(needScroll){
		if(jqFrm.attr('minheight')){ 
			 jqFrm.height(parseInt(jqFrm.attr('minheight'))); 
		};
	}else{
		 if(jqFrm.attr('fullHeight')){ 
			 jqFrm.height(parseInt(jqFrm.attr('fullHeight')));
		};
	}
	jqFrm.attr("src", url);
	if(pagecard != null && pagecard != ""){
		$('#menutd').find('li').attr('class','');
		$('#' + pagecard).attr('class','current');
		initMoreMenu();//重新初始化页签  from navigation.ftl
	}
	if(title != null && title != ""){
		document.title =  title;
	}
	
};
/**
 * 重定向功能节点
 */
function MFSendRedirect(funurl, funcode){
	var nHref = $("[funcode="+ funcode +"]");
	if(nHref && nHref.length > 0){
		var funNode = $(nHref[0]);
		var oriURL = funNode.attr("funurl");
		funNode.attr("funurl", funurl);
		try{
			funNode.click();
		}catch(e){
			
		}
		funNode.attr("funurl", oriURL);
	}
};

var $tabs;
var $dialog;

//系统编码与portlet对应关系
var portlet2SystemStore={};
//系统编码与页签对应关系
var portlet2TabStore={};

//Tab页签数量
var auth_tab_counter = 1;

/**
*弹出验证对话框
@param systemName 系统名称
@param systemCode 系统编号
@param portletId Portlet窗口ID
@param shareLevel 共享级别
*/
function showAuthDialog(systemName,systemCode,portletId,shareLevel){
	//未初始化
	if(!$tabs||!$dialog){
		initAuthDialog();
	}
	//如果在隐藏状态则弹出
	$("#authDialog" ).dialog();
	var portletArr=portlet2SystemStore[systemCode+shareLevel];
	//没有系统编码的记录
	if(!portletArr){
		$tabs.tabs( "add", "#tabs-"+auth_tab_counter, systemName);
		$("div#tabs-"+auth_tab_counter+".ui-tabs-panel").append('<iframe frameborder="0" width="100%" height="200" src="'+ ROOT_PATH +'/core/uimeta.jsp?pageId=credential&model=nc.portal.sso.pagemodel.CredentialEditPageModel&wmode=dialog&portletId='+portletId+'&systemCode='+systemCode+'&sharelevel='+shareLevel+'"> </iframe>');
		//记录systemCode+sharelevel与tab页签的关系
		portlet2TabStore[systemCode+shareLevel]=auth_tab_counter;
		//增加tab页签计数
		auth_tab_counter++;
		portlet2SystemStore[systemCode+shareLevel]=new Array(portletId);
	}else{
		//没有Portlet的记录
		if(jQuery.inArray(portletId, portletArr) == -1){
			portletArr.push(portletId);
		}
	}
}
/**
*初始化对话框
**/
function initAuthDialog(){
 	$(document.body).append('<div id="authDialog" title="帐户关联"><div id="authTabs"><ul></ul></div></div>');
	$tabs = $( "#authTabs").tabs({
			tabTemplate: "<li><a href='#{href}'>#{label}</a> </li>"
		});
		$dialog = $( "#authDialog" ).dialog({
			minWidth: 480 ,
			maxWidth: 480 ,
			minHeight: 320 ,
			maxHeight: 320 ,
			buttons: {
				完成: function() {
					$( this ).dialog( "close" );
				},
				取消: function() {
					$( this ).dialog( "close" );
				}
			},close: function(event, ui) {
				//关闭时销毁iFrame
				$( "#authDialog" ).find("iframe").attr("src","");
			}
		});
}
/**
 * SSO验证成功回调函数
 */
function authCorrect(portletWindId,systemcode,sharelevel){
	if(!$.isEmptyObject(portlet2SystemStore)){
		var pwindArr=portlet2SystemStore[systemcode+sharelevel];
		var exec="removeAuthForm('"+systemcode+"','"+sharelevel+"');";
		if(pwindArr!=null){
			for(var i = 0;i<pwindArr.length;i++){
				getContainer("#"+pwindArr[i]).doView(exec);
			}
		}
	}else{
			getContainer("#"+portletWindId).doView();
	}
}
/**
 * 移除验证表单
 */
function removeAuthForm(systemcode,sharelevel){
	 var tabIndex=portlet2TabStore[systemcode+sharelevel];
	 var tabId="tabs-"+tabIndex;
	 var tabItem=$("#authTabs").children("div");
	 //找出tabindex 移除
	 for(var i = 0;i<tabItem.length;i++){
	 	if(tabItem[i].id==tabId){
	 		$tabs.tabs( "remove",i);
	 	}
	 }
	 //最后一个 关闭对话框
	 if($("#authTabs").children("div").length==0){
	 	$dialog.dialog( "close" );
	 }
}
/**
* 隐藏
**/
function hideFrame(){
	$outer =  $("[tp=framelayout]" );
	$outer.show();
	if( $("#coverFrame_system" ).length > 0){
		$( "#coverFrame_system" ).find("iframe")[0].src="";
		$("#coverFrame_system").hide();
	}
}
/**
 * 打开节点
 * @param event 事件
 */
function openFrame(title,url){
	//得到框架布局
	$outer =  $("[tp=framelayout]" );
	if($outer.size()<=0){
		alert("未配置显示框架!请在页面中选择一个Layout并设置样式为框架布局!");
		return false;
	}
	$outer.hide();
	if( $("#coverFrame_system" ).length == 0){
		var frameHeight = $outer.height();
		$outer.parent().append('<div id="coverFrame_system"><iframe frameborder="0" width="100%" height="'+frameHeight+'"></iframe><div>');
	}
	$( "#coverFrame_system" ).find("[tp=pTitle]").html(title);
	var fm = $( "#coverFrame_system_iframe" );
	fm[0].src=url;
	$("#coverFrame_system").show();
	initIframeArea('coverFrame_system_iframe',0);
}

function openFrameInCurrentPage(title,url){
	//得到框架布局
	$outer =  $("[tp=framelayout]" );
	if($outer.size()<=0){
		alert("未配置显示框架!请在页面中选择一个Layout并设置样式为框架布局!");
		return false;
	}
	$outer.hide();
	if( $("#coverFrame_system" ).length == 0){
		var frameHeight = $outer.height();
		$outer.parent().append('<div id="coverFrame_system"><iframe frameborder="0" width="100%" height="'+frameHeight+'"></iframe><div>');
	}
	$( "#coverFrame_system" ).find("[tp=pTitle]").html(title);
	var fm = $( "#coverFrame_system_iframe" );
	fm[0].src=url;
	$("#coverFrame_system").show();
	initIframeArea('coverFrame_system_iframe',0);
}
/**
 * 打开页面
 */
function showFrameDailog(title,w,h,url,closeable){
	if( $("#tsFrame_system" ).length == 0)
	$(document.body).append('<div id="tsFrame_system"><iframe scrolling="no" marginheight="0" marginwidth="0" style="border:0px; padding:0;" frameborder="0" width="100%" src=""></iframe><div>'); 
	$( "#tsFrame_system" ).dialog({
			title:title,
			width: w,
			height: h,
			modal: true,
			beforeclose: function(event, ui) {
				/**设置对话框是否可以手工关闭**/
				if(closeable == "undefined")
					return true;
				return closeable;
			},close: function(event, ui) {
				//关闭时销毁iFrame
				$( "#tsFrame_system" ).find("iframe")[0].src="";
			}
			
		});
	var fm = $( "#tsFrame_system" ).find("iframe");
	fm.height( $("#tsFrame_system" ).height() - 5);
	fm[0].src=url;
}
/**
 * 关闭当前的弹出对话框
 */
function closeFrameDailog(){
	$( "#tsFrame_system" ).dialog("close");
}
/**
 * 停止事件传递
 */
function stopDefault(e) {
	//prevetnDefault()是DOM事件的核心方法,用于阻止事件的默认行为
	if (e.preventDefault)
		e.preventDefault();
	else {
		//returnValue是IE中事件的默认属性,将其设置为false以取消事件的默认动做
		e.returnValue = false;
	}
};
/**
 * 调整Iframe高度
 */
function adjustIFramesHeightOnLoad(iframe, syncFlag) {
	if(syncFlag){
		doAdjustIFrameHeight(iframe, syncFlag);
	}else{
		if(window.doAdjustIFrameHeightFunc){
			clearTimeout(window.doAdjustIFrameHeightFunc);
		}
		window._tmp_Frame = iframe;
		window.doAdjustIFrameHeightFunc = window.setTimeout("window.doAdjustIFrameHeight()",500);
	}
 };
 
 function doAdjustIFrameHeight(iframe, syncFlag){
 	if(!iframe)
 		iframe = window._tmp_Frame;
 	//var iframe_top=getScrollTop();//ifrme top
    try{
		var minHeight = getInteger(iframe.getAttribute("minHeight"),0);
		if(iframe.contentWindow.window.document.body != null && iframe.contentWindow.window.document.body.children && iframe.contentWindow.window.document.body.children.length > 0){
			// 通过realHeight记录iframe最初设置的height，每次重算高度之前将height设为最初值
			//if(syncFlag){
				//if(iframe.realHeight ==null || iframe.realHeight == ""){
				//	iframe.realHeight = iframe.style.height;
				//}
			//}
			//if(iframe.realHeight)
			//iframe.style.height = minHeight + "px";
			var eles = null;
			//if (IS_IE8 || IS_IE7)
			//	eles = getElementsByName_iefix(iframe.contentWindow.window.document, "div", "flowv");
			//else	
			//	eles = iframe.contentWindow.window.document.getElementsByName("flowv");
				eles = iframe.contentWindow.window.$("div [name=flowv]");
			var iframeHeight = iframe.contentWindow.window.document.body.children[0].scrollHeight;;
			if(eles != null){
				var	maxHeight = 	getMaxEleHeight(eles);
				
				iframeHeight = iframeHeight < maxHeight ? maxHeight : iframeHeight;
			}
			if( minHeight < iframeHeight){
		       iframe.style.height = (iframeHeight + 'px');
		    }
		}
		 
	} catch (e) {
		//抛弃异常
	}
	if(!syncFlag){
		try{
			window._tmp_Frame = null;
			delete window._tmp_Frame;
		}catch (e) {
			//抛弃异常
		}
	}
	//获取窗口滚动的高度，控制滚动条回到顶端
	//$(document.body).scrollTop(iframe_top);
 }
 function getMaxEleHeight(eles) {
 	var height = 0;
 	for(var i = 0; i < eles.length; i ++){
 		var tmpHeight = eles[i].offsetHeight;
 		if(tmpHeight > height)
 			height = tmpHeight;
 	}
 	return height;
 }
 /**
  * 消息中心脚本初始化
  * @type 
  */
var msgcen_curmsgpks=null;//当前显示消息的pk集合
var msgcen_msgetag=null;//消息前台缓存key
var msgcen_swtts=null;//消息弹出框切换时间
//初始化消息提醒
function initMsgTip(){
	$.ajax({
		type: "GET",                  
		dataType: "json",              
	    url:"/portal/pt/msgcennum/getMsgNumInfor",
	    sync:false,
	    cache:false,
	    success:function(obj){
	    	refreshMsgData(obj);
	    	//配置消息间隔
	    	var interval=obj[0].mesboxint;
			var intervalNum = Number(interval);
			if(intervalNum > 0){
				window.setInterval("msgCenNum()",intervalNum * 60000);//后台取数据  单位:分钟
			}
	    }
	});
}
//add by jizhg 修改后消息条目个数
function initMsgCount(obj){
	var msgbox=document.getElementById("msgCount");
	if(msgbox){
		msgbox.innerHTML=obj[0].count+"条";
	}

}
function refreshMsgData(obj){

	//设置国资委特有消息中心 add by jizhg
	initMsgCount(obj);
	//设置图片和数字
	setImgDig(obj[0]);
	//当前消息pks
	msgcen_curmsgpks=obj[1].msgpk;
	//缓存key
	msgcen_msgetag=obj[1].msgetg;
	//消息弹出框切换时间
	msgcen_swtts=Number(obj[0].mesboxswt);
	//清理旧会话缓存
	clearOldSessionCach();
	//后台所有消息
	var allmsgs=obj[1].msgs;
	//过滤消息
	var msgs=filterMsg(allmsgs);
	if(msgs.length>0){
		var msgshtml=getmsgHtml(msgs);
		showMsgBox(msgshtml);
	}
}
/**
 * 清除上个session期的缓存
 */
function clearOldSessionCach(){
	var msgflag=msgcen_msgetag.substring(0,msgcen_msgetag.lastIndexOf(":"));
	var lastmsgetag=getMsgCach(msgflag);
	//记录当前session的msgetag
	addMsgCach(msgflag,msgcen_msgetag);
	if(lastmsgetag==null || lastmsgetag=="")
	 	return ;
	if(lastmsgetag && lastmsgetag!=msgcen_msgetag){
		clearMsgCach(lastmsgetag);
	}
}
/**
 * 消息缓存工具方法 定义
 */
function getMsgCach(cachKey){
	if(cachKey==null || cachKey=="")
		return null;
	var retval=null;
	if(IS_IE7 && UserData.init()){
		var user=msgcen_msgetag.substring(0,msgcen_msgetag.lastIndexOf(":")).toString();
		retval=UserData.load(user,cachKey);
	}else
		retval=getLocalCache(cachKey);
	return retval;
}
function addMsgCach(cachKey,caVal){
	if(cachKey==null || cachKey=="")
		return ;
	if(IS_IE7 && UserData.init()){
		var user=msgcen_msgetag.substring(0,msgcen_msgetag.lastIndexOf(":")).toString();
		//使用userdata
		UserData.save(user,cachKey,caVal);
	}else{
		//添加缓存
		createClientCache(cachKey,caVal);
	}
}
function updateMsgCach(cachKey,cachVal){
	if(cachKey==null || cachKey=="")
		return ;
	var user=msgcen_msgetag.substring(0,msgcen_msgetag.lastIndexOf(":")).toString();
	if(IS_IE7 && UserData.init() && UserData.exist(user,cachKey)){//使用userdata
		UserData.remove(user);
		UserData.save(user,cachKey,cachVal);
	}else{
		createClientCache(cachKey,cachVal);
	}
}
function clearMsgCach(cachKey){
	if(cachKey==null || cachKey=="")
		return ;
	var user=msgcen_msgetag.substring(0,msgcen_msgetag.lastIndexOf(":")).toString();
	if(IS_IE7 && UserData.init() && UserData.exist(user,cachKey)){
		//使用userdata
		UserData.remove(user);
	}else{
		var storage = window.localStorage;
		if(storage){
			storage.removeItem(cachKey);
		}
	}
}
/**
 * 后台刷新ajax
 */
function msgCenNum(){
	$.ajax({
	type: "GET",                  
	dataType: "json",              
    url:"/portal/pt/msgcennum/getMsgNumInfor",
    data: "",
    sync:false,
    cache:false,
    success: function(obj){
    	refreshMsgData(obj);
    }
  });
}
/**
 * 过滤消息
 * @param {} msgObj 消息pks、etag、list
 */
function filterMsg(msges){
	var cachPks=getMsgCach(msgcen_msgetag);
	if(cachPks==null || cachPks=="")
		return msges;
	var filtedMsg = new Array();//过滤后的消息集
	var curShowPks = new Array();//更新消息pks 
	for (var m=0;m<msges.length;m++) {
		if(cachPks.indexOf(msges[m].pk)==-1){
			filtedMsg.push(msges[m]);
			curShowPks.push(msges[m].pk);
		}
	}
	/**
	 * 删除缓存中已被用户置为已读的消息pk(减小缓存容量)
	 */
	clearUnUsedPksCach(cachPks);
	/**
	 * 过滤后消息的pks
	 */
	msgcen_curmsgpks=curShowPks.join(":");
	return filtedMsg;
}
/**
 * 清理客户端无用pk缓存
 * @param {} cachPks
 */
function clearUnUsedPksCach(cachPks){
	var res = new Array();
	var cachmsgpks=cachPks.split(":");
	for (var m=0;m<cachmsgpks.length;m++) {
		if(msgcen_curmsgpks.indexOf(cachmsgpks[m])!=-1){
			res.push(cachmsgpks[m]);
		}
	}
	if(res.length < cachmsgpks.length){//缓存需要减小
		var newmsgpks=res.join(":");
		updateMsgCach(msgcen_msgetag,newmsgpks);
	}
}
/**
 * 拼装html
 */
function getmsgHtml(msgs){
	var banner = $ce("DIV");
	banner.className = "banner";
	banner.id="banner";
	for(var i =  0; i < msgs.length; i++){
		var table = $ce("table");
		table.id="table_"+i;
		//发送人
		var tr = $ce("tr");
		var td = $ce("td");
		td.style.color = "#333333";
		td.innerHTML=msgs[i].sender;
		tr.appendChild(td);
		table.appendChild(tr);
		//标题
		var trt = $ce("tr");
		var tdt = $ce("td");
		var divt = $ce("div");
		divt.className="title_div";
		var at = $ce("a");
		at.title=msgs[i].title;
		at.id=msgs[i].pk+":"+msgs[i].pluginid;
		at.innerHTML=substrb(msgs[i].title,66);
		divt.appendChild(at);
		tdt.appendChild(divt);
		trt.appendChild(tdt);
		table.appendChild(trt);
		//分页
		var trp = $ce("tr");
		var tdp = $ce("td");
		tdp.style.textAlign="right";
		var left = $ce("div");
		left.id="divLeftBtn_"+i;
		left.className="divLeftBtn";
		var cen = $ce("div");
		cen.className="pagenation";
		var pageInf=(i+1)+"/"+(msgs.length>99?"99+":msgs.length);
		var paga = $ce("a");
		paga.id="msgPageA_"+i;
		paga.innerHTML=pageInf;
		cen.appendChild(paga);
		var riht = $ce("div");
		riht.id="divRightBtn_"+i;
		riht.className="divRightBtn";
		tdp.appendChild(left);
		tdp.appendChild(cen);
		tdp.appendChild(riht);
		trp.appendChild(tdp);
		table.appendChild(trp);
		banner.appendChild(table);
	}
	return banner.outerHTML;
}
 /**
  * IE7客户端缓存对象
  * @type 
  */
var UserData = {
	isinit : false,
    // 初始化userdate对象
    init : function(){
       try{
       	 if(!UserData.isinit){
       	 	document.documentElement.addBehavior("#default#userdata");
       	  	UserData.isinit=true;
       	 }
	     return UserData.isinit;
       }catch(error){
       		return false;
       }
    },
    // 保存文件到userdata文件夹中 user-用户，key-建，value-值
    save : function(user, key, tva){
    	try{
    		//userdata分隔符"_",其它符号报错
			user=user.replace(/:/g,"_");
			key=key.replace(/:/g,"_");
			var ex;
		    if(!tva){
		        tva = key;
		        key=user;
		        user="defaultUser"
		    }
		    with(document.documentElement){
			     load(user);
			    //有效期1天(当前时间往后推1天)
			    expires = new Date(new Date()-(-86400000)).toGMTString();
			    setAttribute(key, tva);
			    save(user);
		    }
    	}catch(error){}
    }, 
    // 从uerdata文件夹中读取指定key，并以字符串形式返回。
    load : function(user,key){
    	try{
    		//userdata分隔符"_",其它符号报错
			user=user.replace(/:/g,"_");
			key=key.replace(/:/g,"_");
    		if(!key){
			    key=user;user="defaultUser";
			}
		  	var ex;
		    with(document.documentElement){
		     	load(user);
		    	return getAttribute(key);
		    }
    	}catch(error){
    		return null;
    	}
    },
    // 检查userdata是否存在 
    exist : function(user,key){
        return UserData.load(user,key) != null;
    },
    // 删除userdata指定数据
    remove : function(user){
    	//userdata分隔符"_",其它符号报错
		user=user.replace(/:/g,"_");
	    try{   
    		var ex; 
	    	if(!user)
	    		user="defaultUser";
	    	with(document.documentElement){
	    	  	load(user);
			    expires = new Date(new Date()-86400000).toGMTString();
			    save(user);
	    	}
		}catch(error){}  
    }
    // UserData函数定义结束
};
/**
 * 消息弹出框关闭事件回调方法
 * @param {} id
 */
function MessageCompAfterClose(id){
	if(id == "msgcen_messageComp"){
		var cachpk=getMsgCach(msgcen_msgetag);
		if(cachpk!=null && cachpk!=""){//更新
			cachpk=cachpk+":"+msgcen_curmsgpks;
			updateMsgCach(msgcen_msgetag,cachpk);
		}else{//首次
			cachpk=msgcen_curmsgpks;
			addMsgCach(msgcen_msgetag,cachpk);
		}
  	}
}
//设置图片和数字
function setImgDig(msgcen_num){
	var msg_img_div=document.getElementById("number_digit");
	if(msg_img_div){
		//更换图片
		msg_img_div.className="number_"+msgcen_num.onerowcs+"_digit";
		//设置数字
		msg_img_div.innerHTML=msgcen_num.count;
	}
}
function substrb(str,n){ 
	var r=/[^\x00-\xff]/g; 
	if(str.replace(r,"mm").length<=n){return str;} 
	var m=Math.floor(n/2); 
	for(var i=m;i<str.length;i++){ 
	if(str.substr(0,i).replace(r,"mm").length>=n){ 
	return str.substr(0,i)+"..."; 
	} 
	}
	return str; 
}
/**
 * 弹出消息框
 */
function showMsgBox(msgshtml){
	  if(msgshtml==null || msgshtml=="")
	  	return ;
	  var msgboxId="msgcen_messageComp";//消息弹出框的id
	  var msgboxCom=window.objects[msgboxId];//消息弹框控件
	  /**
	   * 如果正在显示，更新内容
	   */
	  if(msgboxCom && msgboxCom.Div_gen.style.display=="block"){
		 msgboxCom.setText(msgshtml);
		 startDynSwitch();
		 return ;
	  }
	  //位置计算
	  var scrollWidth = document.body.scrollWidth;
	  var xWidth = 0;
	  if(scrollWidth > 1200){
		  xWidth = (scrollWidth - 1200) / 2;
	  }
	if(msgboxCom){
		msgboxCom.showMsg();
		msgboxCom.setText(msgshtml);
	}else{
		showMessage(msgshtml,{
			showPosition:'bottom-right',
			hasImg:false,
			width:"260",
			height:"100",
			isOpacity:false,
			isNew:true,
			id:msgboxId,
			x:xWidth-25
//				x:15,
//				y:-34
		});
	}
}
function startDynSwitch(){
	if(window.msgcen_interval)
		clearInterval(window.msgcen_interval);
	if(!window.msgcen_msg && window.msgcen_msg!=0)
		window.msgcen_msg=-1;
	$("#banner").msgbox(msgcen_swtts*1000);//单位：秒
}
 /*消息弹出框切换*/
 jQuery.fn.extend({
	    msgbox:function(t){
			var id="#"+$(this).attr("id");
			//var n = -1;
			var num=-1;//上个消息的index
			var lefBtnOverClassName="divLeftBtn_mouseover";
			var rigBtnOverClassName="divRightBtn_mouseover";
			var allBtns=$(id+" table").find("div[id^='divRightBtn_'],div[id^='divLeftBtn_']");
			//click事件
			allBtns.click(function(e) {
				e.stopPropagation();
				var t_id=$(this).attr('id');
				var t_count=$(id+" table").length;
				num=parseInt(t_id.split("_")[1], 10);
				//自动播放时到最后一条或者播放第100条消息时（超过3位,宽度变宽出滚动条），不关闭
				if((num==(t_count-1) || num==98) && window.objects["msgcen_messageComp"]){
					clearInterval(window.msgcen_interval);
//					window.objects["msgcen_messageComp"].destroySelf();
//					return ;
				}
				if(t_id.indexOf('divLeftBtn')>-1){
					window.msgcen_msg=num-1>-1?(num-1):0;
				}
				if(t_id.indexOf('divRightBtn')>-1){
					window.msgcen_msg=num+1>(t_count-1)?0:(num+1);
				}
				show();
			});
			//mouserover
			allBtns.mouseover(function(e) {
				var tid=$(this).attr('id');
				if(tid.split("_")[0]=="divLeftBtn")
					$(this).addClass(lefBtnOverClassName);
				else
					$(this).addClass(rigBtnOverClassName);
			});
			//mouseout 
			allBtns.mouseout(function(e) {
				var tid=$(this).attr('id');
				if(tid.split("_")[0]=="divLeftBtn")
					$(this).removeClass(lefBtnOverClassName);
				else
					$(this).removeClass(rigBtnOverClassName);
			});
			//a click
			$("#banner").find(".title_div a").click(function(i){
				openMsg('TITLE_CLICK',$(this).attr("id").split(":")[0],$(this).attr("id").split(":")[1]);
			});
			//99+,msgPageA
			$("#banner").find("a[id^='msgPageA_']").click(function (i){
				openPublicPortlet('pint','MsgCenterPopupPortlet',800,580);
			});
			window.msgcen_interval = setInterval(showAuto, t);
			$(this).hover(function(){
					clearInterval(window.msgcen_interval);
				},
				function(){
				    window.msgcen_interval = setInterval(showAuto, t);
				}
			 );
			function showAuto(){
				var ta_count=$(id+" table").length;
				//自动播放时到最后一条或者播放第100条消息时（超过3位,宽度变宽出滚动条），停止不关闭
				if((window.msgcen_msg==(ta_count-1) || window.msgcen_msg==98) && window.objects["msgcen_messageComp"]){
					clearInterval(window.msgcen_interval);
					//后台刷新过来时要显示当前table
					$("#table_"+window.msgcen_msg).show();
//					window.objects["msgcen_messageComp"].destroySelf();
					return ;
				}
				num=window.msgcen_msg;
				window.msgcen_msg = window.msgcen_msg >=(ta_count-1) ? 0 : ++window.msgcen_msg;
				show();
			}
			function show(){
				var lastTable=$("#table_"+num);
				var curTable=$("#table_"+window.msgcen_msg);
				if(num==-1){//初始情况
					curTable.fadeIn(1000); 
				}else{
					//上个消息
					lastTable.fadeOut(500);
					window.setTimeout(function(){
						lastTable.hide();
						curTable.fadeIn(1000);
					},500);
				}
			}
			showAuto();
	    }
	});
 
/*消息弹出框回调*/
 function MessageCompAfterShow(id){
 	if(id == "msgcen_messageComp"){
 		var messageComp = window.objects[id];
 		messageComp.bgLeftTopDiv.className = 'portal_bg_left_top_div';
 		messageComp.bgCenterTopDiv.className = 'portal_bg_center_top_div';
		messageComp.bgRightTopDiv.className = 'portal_bg_right_top_div';
		messageComp.bgLeftCenterDiv.className = 'portal_bg_left_center_div';
		messageComp.bgCenterDiv.className = 'portal_bg_center_div';
		messageComp.bgRightCenterDiv.className = 'portal_bg_right_center_div';
		messageComp.bgLeftBottomDiv.className = 'portal_bg_left_bottom_div';
		messageComp.bgCenterBottomDiv.className = 'portal_bg_center_bottom_div';
		messageComp.bgRightBottomDiv.className = 'portal_bg_right_bottom_div';
		messageComp.closeImg.className = "portal_message_close_img";
		messageComp.closeImg.onmouseover = function(){
			this.className = "portal_message_close_img_over";
		};
		messageComp.closeImg.onmouseout = function(){
			this.className = "portal_message_close_img";
		};
		messageComp.closeImg.style.top = "10px";
		messageComp.closeImg.style.right = "10px";
	    if($("#banner").length>0){
	    	startDynSwitch();
	    }
 	}
 };

 /*点击消息标题*/
 function openMsg(cmd, pk, pluginid){
	var proxy = new ServerProxy(null,null,true);
	proxy.addParam('clc', 'nc.uap.portal.msg.ctrl.MainController');
	proxy.addParam('m_n', 'doCmd');
 	proxy.addParam('pk', pk);
 	proxy.addParam('cmd', cmd);
 	proxy.addParam('pluginid', pluginid);
	proxy.execute(); 
	setTimeout(function(){
	try{
		msgCenNum();
	}catch(e){
	}}, 300);
}
 /**
  * 打开一个公共的Portlet
  */
function openPublicPortlet(module,name,w,h,category){
	var url = "/portal/app/msg?nodecode=11110105&lrid=1";
	if(category){
		url = url + "&category=" + category;
	}
	showDialog(url , /*"消息中心"*/ trans("ml_openPublicPortlet_title"), w,h, name, "" ,{isShowLine:false},null);	
	var dialogName = "$modalDialog" + (showDialog.dialogCount -1);
	var dls = new Listener("beforeClose");
	dls.func = function (){
	  	msgCenNum();//消息中心数目
	};
	window[dialogName].addListener(dls);
	/**
	showDialog("");
	var frameName = module + "_" + name + "_" + "PF";
	var mockPortletName = CUR_PPAGE_MODULE + "_" + CUR_PAGE_NAME + "_" + module + '_' + name;
	if($("#" + frameName).length == 0)
		$(document.body).append('<div id="'+frameName+'"><div>'); 
	$("#" + frameName).append('<div tp="portlet" setTitle="setPublicPortletTitle"  pid="P13466617358" id="'+ mockPortletName +'"><div class="content"  tp="pBody"></div></div>');
	
	$("#" + frameName).dialog({
			title:"Loading...",
			width: w,
			height: h,
			modal: true
	});
    var param = getActionParam(mockPortletName);
    param.if_src_type = "src";
    param.category = category;
    param.h = h;
	var actURL = ROOT_PATH + ACTION_URL;
	openPortlet(actURL,param);
	 **/
};
/**
 * 设置公共Portlet的标题
 */
function setPublicPortletTitle(title){
	var idCol = this.id.split("_");
	var frameName = idCol[2] + "_" + idCol[3] + "_" + "PF";
	$("#" + frameName).dialog({title:title});
};
/**
 * 增加事件
 */
function addEventHandler(oTarget, sEventType, fnHandler) {
	if (oTarget.addEventListener) {  // 用于支持DOM的浏览器
		oTarget.addEventListener(sEventType, fnHandler, true);
	} else if (oTarget.attachEvent) {  // 用于IE浏览器
		oTarget.attachEvent("on" + sEventType, fnHandler);
	} else {  // 用于其它浏览器
		oTarget["on" + sEventType] = fnHandler;
	}
}

function getElementsByName_iefix(doc, tag, name) {
     var elem = doc.getElementsByTagName(tag);
     var arr = new Array();
     for(i = 0,iarr = 0; i < elem.length; i++) {
          att = elem[i].getAttribute("name");
          if(att == name) {
　              arr[iarr] = elem[i];
               iarr++;
          }
     }
     return arr;
} 
function getRequest(url) {
	   if(!url)
	   		 url = location.search; 
	   var theRequest = new Object();
	   if (url.indexOf("?") != -1) {
		  var str = url.substring(url.indexOf("?")+1);
		  strs = str.split("&");
		  for(var i = 0; i < strs.length; i ++) {
			 theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
		  }
	   }
	   return theRequest;
};
/**
 * 初始化菜单
 */
var ncNodeAppletMap = new HashMap();//存放格式：systemid-applet
var systemid_defVal="local";
function initFloatMenu(){
	$(".floatmenu").find("a").click(function () {
		var isFun = $(this).attr("isFun");
		var newPage = $(this).attr("isnewpage");
		if(isFun == "1"){
			var frameURL = $(this).attr("funURL");
			if(newPage && newPage == 'N'){
				var parents = $(this).parents("[menu=t]");
				var titles = new Array();
				titles.push($(this).attr("title"));
				for(var i = 0; i < parents.length; i ++){
					titles.push($(parents[i]).attr("title"));
				}
				var pagecard =  window.pargeCard;
				titles.push($("#"+pagecard).find("span").html());
				openFrame(titles.join(",,,"), frameURL, pagecard);
			}else{
				if((frameURL.startWith("/") &&  frameURL.indexOf("systemCode=NC")==-1) || frameURL.startWith("http://") || frameURL.startWith("https://")){
					window.open(frameURL);
				}else{
					//重置系统编码
					if(frameURL.indexOf("systemCode=")!=-1){
						systemid_defVal=getRequest(frameURL)["systemCode"];
					}else
						systemid_defVal="local";
					//nc节点且非首次打开,打开NC的页签 
					var isNcNode=false;
					if(frameURL.startWith("app/mockapp/ncnode") && ncNodeAppletMap.containsKey(systemid_defVal)){
						isNcNode=true;//系统预置nc节点
					}
					if(frameURL.startWith("/portal/pt/integr/nodes") && frameURL.indexOf("systemCode=NC")!=-1 && ncNodeAppletMap.containsKey(systemid_defVal)){
						isNcNode=true;//同步的nc节点
					}
					if(isNcNode){
						//检查窗口是否关闭
						var chDo=ncNodeAppletMap.get('ch_do_'+systemid_defVal);
						if(chDo.closed){//已关闭
							ncNodeAppletMap.remove(systemid_defVal);
							ncNodeAppletMap.remove('ch_do_'+systemid_defVal);
						}else{
							var applet=ncNodeAppletMap.get(systemid_defVal);
							var nodeid=getRequest(frameURL)["nodeId"];
							if(!nodeid){
								nodeid=getRequest(frameURL)["node"];
							}
							if(!nodeid){
								nodeid=getRequest(frameURL)["nodecode"];
							}
							applet.callNC( "nc.uap.lfw.applet.NCNodeOpener", "appletTabItem",nodeid);
							chDo.focus();
							return ;
						}
					}
					var width=window.screen.availWidth - 30;
					var height=window.screen.availHeight- 30;
					var bodyWidth = window.screen.availWidth;
					var bodyHeight = window.screen.availHeight ;
					var left = bodyWidth > width ? (bodyWidth - width)/2 : 0;
					var top = bodyHeight > height ? (bodyHeight - height)/2 : 0;
					
					var wh="width="+bodyWidth+","+"height="+bodyHeight+",left="+left+",top="+top;
					var urln=frameURL;
					if(!urln.startWith("/"))
					  urln=ROOT_PATH + "/" + frameURL;
					var child=window.open(urln,'ncNode_'+systemid_defVal,wh);
					ncNodeAppletMap.put('ch_do_'+systemid_defVal,child);
				}
			}
		}
	});
	$(".floatmenu").find("a").each(function(i){
		var isFun = $(this).attr("isFun");
		var pkFunnode = $(this).attr("pkFunnode");
		var title = $(this).attr("title");
		var pk_menuitem= $(this).attr("pk_menuitem");
		if(isFun == "1"){
			//为父div添加重命名图标mouseover、mouseout事件
			if(IS_IPAD)
				$(this).parent().bind('touchend',function(){
					changeLinkBtn="true";
					hideOrShowImg($(this));
				});
		    else{
		    	$(this).parent().bind('mouseover mouseout',function(){hideOrShowImg($(this).parent());});
		    }
			//添加重命名图片click事件
		    if($(this).next().length>0){
		    	var addOrDel=$(this).next().attr("src");
				if(addOrDel.indexOf("add")!=-1){
					$(this).next().click(function(){
						addFrqtItem(pkFunnode,pk_menuitem,title);
					});
				}
				if(addOrDel.indexOf("delete")!=-1){
					$(this).next().click(function(){
						callServer("/portal/pt/deleteMenu/deleteOftenMenu?pk="+pkFunnode);
					});
				}
		    }
		}
	});
}
function callServer(url){
	if (window.sys_DownFileFrame == null) {
		var frm = $ce('iframe');
		frm.frameborder = 0;
		frm.vspace = 0;
		frm.hspace = 0;
		frm.style.width = '1px';
		frm.style.heigh = 0;
		window.sys_DownFileFrame = frm;
		document.body.appendChild(window.sys_DownFileFrame);
	}
	window.sys_DownFileFrame.src=url;
}

function compressContent(content){
	preCompressContent();
	if(window.compressObj){
		try{
			return window.compressObj.compress(content);
		}
		catch(error){
			return null;
		}
	}
	return null;
}

function preCompressContent(){
	if(window.compressObjSign == null){
		window.compressObjSign = 1;
	    var div = document.createElement("DIV");
	    div.id = "compressDivContent";
	    document.body.appendChild(div);
	    try{
	    	var swfVersionStr = "0.0.0";
		    var xiSwfUrlStr = "";
		    var flashvars = {};
		    var params = {};
	//	    params.quality = "high";
	//	    params.bgcolor = "#ffffff";
		    params.allowscriptaccess = "always";
	//	    params.allowfullscreen = "true";
		    var attributes = {};
		    attributes.id = "Compress";
		    attributes.name = "Compress";
		    attributes.align = "middle";
		    swfobject.embedSWF(
		        "/lfw/frame/device_pc/script/ui/external/Compress.swf", "compressDivContent", 
		        "0", "0", 
		        swfVersionStr, xiSwfUrlStr, 
		        flashvars, params, attributes);
		    
	    }catch(e){
	    
	    }
	   
	}
}
function compressPluginReady(){
			window.compressObj = document.getElementById("Compress");
			
		    window.compressObj.style.position = "absolute";
		    window.compressObj.style.right = "0px";
		    window.compressObj.style.top = "0px";
}
/**
 * 调整容纳NC的Iframe
 */
function adjustNCFrame(frmId){
	window.$adjustFrameId=frmId;
};

//隐藏内容区
function setContentVisible(isVisible){
	var iframeId = window.$adjustFrameId;
	if(iframeId && typeof(iframeId)!="undefined" && ""!=iframeId){
		var contentFrame = $("#"+iframeId);
		if(contentFrame){
			if(isVisible){
				contentFrame.width("100%");
				contentFrame.css("float","");
			}else{
				contentFrame.width("1px");
				contentFrame.css("float","left");
			}
		}				
	}		
}
/**
默认的创建Part方法
返回创建元素的Jquery对象,可链式调用  
如$(obj).createPart(title,href).click(function (){}).mouserover(function(){})...
@param title 标题
@param href 链接 可以使用javascript:void(0)
@return 该Part的Jquery对象
**/
function _pt_defaultCreatePart(title, href) {
    var  _pt_tmp_part = $('<li><a href="' + href + '">' + title + '</a></li>');
	_pt_tmp_part.appendTo($(this).find("[tp='pPart']"));
    return  _pt_tmp_part;
}

/**
默认的创建Part分割符方法
返回创建元素的Jquery对象,可链式调用  
如$(obj).createSep(title,href).css("color","red");...
@param title 标题
@param href 链接 可以使用javascript:void(0)
@return 该Part的Jquery对象
**/
function _pt_defaultCreateSep() {
//默认分隔符
var _pt_segment="|";
//自定义分隔符
if(arguments.length==1){
	_pt_segment=arguments[0];
}
var _pt_tmp_part = $('<li><a>' + _pt_segment + '</a></li>');
 
   _pt_tmp_part.appendTo($(this).find("[tp='pPart']"));
 
    return _pt_tmp_part;
}

/**
默认的创建空Part方法
返回创建元素的Jquery对象,可链式调用  
如$(obj).createBlankPart(title,href).html("<span>some html here</span>")...
@param title 标题
@param href 链接 可以使用javascript:void(0)
@return 该Part的Jquery对象
**/
function _pt_defaultCreateBlankPart() {
var _pt_tmp_part = $('<li></li>');
   _pt_tmp_part.appendTo($(this).find("[tp='pPart']"));
    return _pt_tmp_part;
}

/**
默认的设置模式的方法
返回创建元素的Jquery对象,可链式调用
@param mode 模式
@return Mode的Jquery对象
**/
function _pt_defaultSetMode(mode){
	
}
/**
默认的设置标题的方法
返回创建元素的Jquery对象,可链式调用
@param title 标题
@return Mode的Jquery对象
**/
function _pt_defaultSetTitle(title){
		return $(this).find("[tp='pTitle']").html(title);
	}

/**
默认的设置内容的方法
返回创建元素的Jquery对象,可链式调用
@param content 内容
@return content的Jquery对象
**/
function _pt_defaultSetContent(content){
	return $(this).find("[tp='pBody']").html(content);
	}
/**
默认的设置不显示外框方法
@return Portlet容器

**/
function _pt_defaultSetExposed(){
	var jqObj=$(this);
	//防止多次操作导致原样式丢失
	if(this.clsName==undefined){
		this.clsName=jqObj.attr("class");
	}
	jqObj.attr("class","margeach");
	var phd = jqObj.find("[tp=pHead]");
	phd.children().hide();
	phd.attr("oheight",phd.height());
	phd.height(3);
	jqObj.find("[tp=intine]").parent().prevAll().hide();
	jqObj.find("[tp=intine]").parent().nextAll().hide();
	return this;
	}
/**
默认的恢复显示外框方法
@return Portlet容器

**/
function _pt_defaultSetUnExposed(){
	var jqObj=$(this);
	jqObj.attr("class",this.clsName);
	var phd = jqObj.find("[tp=pHead]");
	phd.children().show();
	phd.height(phd.attr("oheight"));
	jqObj.find("[tp=intine]").parent().prevAll().show();
	jqObj.find("[tp=intine]").parent().nextAll().show();
	return jqObj;
	}
/********************
 * 取窗口滚动条高度 guoshg
 ******************/
function getScrollTop()
{
    var scrollTop=0;
    if(document.documentElement&&document.documentElement.scrollTop)
    {
        scrollTop=document.documentElement.scrollTop;
    }
    else if(document.body)
    {
        scrollTop=document.body.scrollTop;
    }
    return scrollTop;
}

/**
 * 默认的显示工具条方法
 */
function _pt_defaultShowTips(){
	var jqObj=$(this);
	//工具条
	var tp = $("#tipspanel");
	tp.attr("portletid",jqObj.attr("id"));
	var tipLeft  = jqObj.position().left-tp.width()+jqObj.width();
	var tipTop = 0;
	var headHeight = jqObj.find("[tp=pHead]").height();
	if(headHeight > tp.height()){
		tipTop = jqObj.position().top + (headHeight - tp.height())/2;
	}else{
		tipTop = jqObj.position().top - tp.height() + headHeight;
	}
	//减去窗口滚动条滚动的高度
	tipTop=tipTop-getScrollTop();
	
	tp.css({left:tipLeft,top:tipTop});
	var _modes = tp.find("[tp=pmodes]");
	//先清除其中的所有子节点
	_modes.empty();
	//重建子节点
	var sms = this.getSupportModes();
	for(var sm_idx in sms){
		if(!isNaN(sm_idx)){
			var sm = sms[sm_idx].toLowerCase();
			//如果是NCPortal支持的模式
			if(jQuery.inArray(sm, NCPortalSupportPortletMode) != -1 && this.getCurrentMode() != sm){
				var funName = "_toolbar_" + sm + "()";
				var showName = NCPortalSupportPortletModeName[sm];
				_modes.append(" <a href='javascript:" + funName + "'>"+ showName +"</a> ");
			}
		}
	}
	//非只读模式下
	if(CUR_PPAGE_READONLY != "true"){
		_modes.append(" <a href='javascript:deletePortlet();' ><img src='/portal/frame/themes/"+PAGE_THEME_ID+"/homepage/moving_panel/icon/pp_close.png'></a>");
	}
	
	tp.show();
//	tp.draggable(); jquery的方法,注释掉
	tipShowFlag = true;
	currentTipMeta = jqObj.attr("id");
}
/**
 * 默认的显示工具条隐藏方法
 */
function _pt_defaultHideTips(){
	var jqObj=$(this);
	//工具条
	var tp = $("#tipspanel");
	tp.hide();
//	tp.hide(2000);
	tipShowFlag = false;
	currentTipMeta = "";
}

/**
 * 工具条查看按钮事件监听方法
 */
function _toolbar_view(){
	if(currentTipMeta != ""){
		var _ctn = getContainer("#"+currentTipMeta);
		_ctn.doView(function (){
			getContainer("#"+currentTipMeta).hideTips();
		});
		
	}
	
}

/**
 * 工具条编辑按钮事件监听方法
 */
function _toolbar_edit(){
	if(currentTipMeta != ""){
		var _ctn = getContainer("#"+currentTipMeta);
		_ctn.doEdit(function (){
			getContainer("#"+currentTipMeta).hideTips();
		});
	}
}
/**
 * 工具条帮助按钮事件监听方法
 */
function _toolbar_help(){
	if(currentTipMeta != ""){
		var _ctn = getContainer("#"+currentTipMeta);
		_ctn.doHelp(function (){
			getContainer("#"+currentTipMeta).hideTips();
		});
	}
}
/**
 * 改变Portlet皮肤
 */
function changePortletTheme(){
	var ptid = $("#tipspanel").attr("portletid");
	var ctr = $("#"+ptid);
	var pid = ctr.attr("pid");
	var param= getActionParam(ptid);
	param["pid"] = pid;
//	var url = "/portal/pt/setting/changePortletTheme?portlet=" + ptid + "&pid=" + pid
//	showFrameDailog("设置Portlet皮肤",480,320,url,true);
	//to model
	var url="/portal/app/mockapp/cdref?model=nc.uap.portal.comm.setting.SetMorePageModel";
	//add param
	url=url+"&portlet="+ptid+"&pid="+pid+"&settype=portletskin";
	window.showDialog(url, /*设置portlet皮肤*/trans("ml_changePortletTheme_title"), 400, 270, pid, null, null, null, {isShowLine:false});
}
function setRequestParam(theRequest) {
	   var url = location.search; 
	   if (url.indexOf("?") != -1) {
		  var str = url.substr(1);
		  strs = str.split("&");
		  for(var i = 0; i < strs.length; i ++) {
			 theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
		  }
	   }
	   return theRequest;
};
/**
 * 删除Portlet
 */
function deletePortlet(){
		showConfirmDialog("是否删除此Portlet?",
		function(){
			var ptid = $("#tipspanel").attr("portletid");
			var ctr = $("#"+ptid);
			var pid = ctr.attr("pid");
			var param= getActionParam(ptid);
			param["pid"] = pid;
			$.ajax({
				type: "GET",
				url: ROOT_PATH + "/pt/home/doDelPortlet",
				data:param,
				cache:false,
			    success: function(data){
			     	var d = eval(data)[0];
				    if(d && typeof  d == "object"){
				    	alert(d.msg);
						if(d.err == 0){
							document.location.reload();
						} 
					}
			     }
			}); 
		},
		null, null, null, null, "确定", "取消", "删除提醒");
}

/**
获得最外层Layout
**/
function _pt_getOuter(){
	return 	$("[tp='layout']").filter(function(index){return $(this).parents("[tp='layout']").length<1;});
	}

function _pt_getSelfLayout(){
	
	}
function _pt_getRow(){
	return $(this).parent("[tp='layout']");
	}	
/**
 * 默认的切换到"查看"视图方法
 * @param fn 切换完成后回调方法(可选)
 */
function _pt_doView(fn){
	var ptid=$(this).attr("id") ;
	var param= getActionParam(ptid);
	var actURL=ROOT_PATH+ACTION_URL;
	openPortlet(actURL,param,fn);
}
function _pt_doRestore(fn){
	var ptid=$(this).attr("id") ;
	var param = getActionParam(ptid);
	param[PORTLET_MODE]="restore";
	param["if_src_type"]="src";
	setRequestParam(param);
	var actURL=ROOT_PATH+ACTION_URL;
	openPortlet(actURL,param);
}

/**
 * 默认的切换到"编辑"视图方法
 * @param fn 切换完成后回调方法(可选)
 */
function _pt_doEdit(fn){
	var ptid=$(this).attr("id") ;
	var param= getActionParam(ptid);
	param[PORTLET_MODE]="edit";
	var actURL=ROOT_PATH+ACTION_URL;
	openPortlet(actURL,param,fn);
}
/**
 * 默认的切换到"帮助"视图方法
 * @param fn 切换完成后回调方法(可选)
 */
function _pt_doHelp(fn){
	var ptid=$(this).attr("id") ;
	var param= getActionParam(ptid);
	param[PORTLET_MODE]="help";
	var actURL=ROOT_PATH+ACTION_URL;
	openPortlet(actURL,param,fn);
}
/**
 * 默认刷新方法
 * @param timesamp 时间间隔
 */
function _pt_doRefresh(timesamp){
	var ptid=$(this).attr("id") ;
	refresh_circle[ptid] = timesamp;
}
/**
 * 设置Portlet支持模式
 * @param modes 模式
 */
function _pt_setSupportModes(modes){
	$(this).data("supportModes",modes);
}

/**
 * 获得Portlet支持模式
 * @return 支持的模式
 */
function _pt_getSupportModes(){
	return $(this).data("supportModes");
}

/**
 * 获得当前Portlet模式
 */
function _pt_getCurrentMode(){
	var cm = $(this).data("currentMode");
	if(cm && cm != "")
		return cm;
	else
		return "view";
}

/**
 * 设置当前Portlet模式
 */
function _pt_setCurrentMode(mode){
	return $(this).data("currentMode",mode);
}
/**
默认触发事件方法
**/
function _pt_doAction(){
	var ptid=$(this).attr("id").split("_");
	
	var pageModule= ptid[0];
	var pageName=ptid[1];
 	var portletModule=ptid[2];
	var portletName=ptid[3];
	
	var actURL=ROOT_PATH+ACTION_URL;
	var argCount=arguments.length;
	var param={};
	if(argCount==0){
		//无参数 触发默认Action  无默认事件则返回
		//是否有必要对Portlet Action进行感知？
		}else if(argCount==1){
		//一个参数 默认为参数名
			if($.isPlainObject(arguments[0])){
			//为Object
				param=arguments[0]; 
				if(!param[ACTION_NAME]){
					param[ACTION_NAME]="processAction";
				}
				param[PAGE_NAME]=pageName;
				param[PORTLET_MODULE]=portletModule;
				param[WINDOW_STATE]="normal";
				param[PORTLET_MODE]="view";
				param[PAGE_MODULE]=pageModule;
				param[PORTLET_NAME]=portletName;
			}else{
				param[ACTION_NAME]="processAction";
				param[PAGE_NAME]=pageName;
				param[PORTLET_MODULE]=portletModule;
				param[WINDOW_STATE]="normal";
				param[PORTLET_MODE]="view";
				param[PAGE_MODULE]=pageModule;
				param[PORTLET_NAME]=portletName;
				param.frameUrl=arguments[0];
			}
		}else {
			//只处理前两个参数
			param=arguments[1];
			param[ACTION_NAME]=arguments[0];
			param[PAGE_NAME]=pageName;
			param[PORTLET_MODULE]=portletModule;
			param[PAGE_MODULE]=pageModule;
			param[PORTLET_NAME]=portletName;
			if(param[WINDOW_STATE]==null){
				param[WINDOW_STATE]="normal";
			}
			if(param[PORTLET_MODE]==null){
				param[PORTLET_MODE]="view";
			}
		}
		openPortlet(actURL,param);
	}
/**
 * 获得默认的Portlet参数
 */
function getActionParam(portletWinId){
	var ptid=portletWinId.split("_");
	var pageModule= ptid[0];
	var pageName=ptid[1];
	var portletModule=ptid[2];
	var portletName=ptid[3];
	var param={};
	param[PAGE_NAME]=pageName;
	param[PORTLET_MODULE]=portletModule;
	param[PAGE_MODULE]=pageModule;
	param[PORTLET_NAME]=portletName;
	param[WINDOW_STATE]="normal";
	param[PORTLET_MODE]="view";
	return param;
}

/**
 * 默认最大化实现
 * @return
 */
function _pt_defaultdoMax(){
	var jqObj=$(this);
	var outer=this.getOuter();
	var outerHeight=outer.height();
	var outerWidth=outer.width() -10;
	if(!this.oldWidth)
	this.oldWidth=jqObj.width();
	if(!this.oldHeight)
		this.oldHeight=jqObj.height();
	var oldHeight=jqObj.height();
	var pBody=jqObj.find("[tp='pBody']");
	var bodyHeight=pBody.height();
	if(!pBody[0].oldHeight)
		pBody[0].oldHeight = bodyHeight;
	var portlets = outer.find("[tp=portlet]");
	for(var i = 0;i< portlets.length; i++){
		var ctn = getContainer(portlets[i]);
		ctn.doHide();
	}
	
	//pBody.height(bodyHeight +outerHeight-oldHeight); 
	//jqObj.height(outerHeight ); 
	jqObj.parents("td").siblings().hide();
	jqObj.parents("td").attr("_width",jqObj.parents("td").attr("width"));
	jqObj.parents("td").attr("width","100%");
//	jqObj.css("position","absolute");
	//jqObj.width(outerWidth); 
//	jqObj.width("100%"); 
	jqObj.show(500);
	jqObj.find("[tp='pHander']").find("a").toggle();
}

function _pt_defaultdoHide(){
	var jqObj=$(this);
	jqObj.hide();
}
/**
 * 默认重设窗口大小
 * @return
 */
function _pt_defaultdoReSize(){
	var jqObj=$(this);
	var outer=this.getOuter();
	//jqObj.width(this.oldWidth);
	//jqObj.height(this.oldHeight);
	var pBody=jqObj.find("[tp='pBody']");
	var bodyHeight=pBody[0].oldHeight;
	//pBody.height(bodyHeight);
	jqObj.parents("td").attr("width",jqObj.parents("td").attr("_width")),
	jqObj.parents("td").siblings().show();
	outer.find("[tp=portlet]").show(500);
	jqObj.find("[tp='pHander']").find("a").toggle();
}
/**
 * 默认的显示最大化按钮
 */
function _pt_defaultAddDoMaxResize(windowState){
	var jqObj=$(this);
	var portletWindowID = this.id;
	var windowStates = windowState.toString();
	var $ul = jqObj.find("[tp='pPart']");
	/*
	if(windowStates.indexOf('minimized')!=-1){
		$ul.append("<li tp=\"pHander\">" +
			"<a onclick=\"getContainer('#"+portletWindowID+"').doMax();\"><img src=\""+ROOT_PATH+"/images/10.gif\" style=\"border:none;cursor:pointer;\"/></a>" +
			"<a onclick=\"getContainer('#"+portletWindowID+"').doReSize();\" style=\"display:none\"><img src=\""+ROOT_PATH+"/images/11.gif\" style=\"border:none;cursor:pointer;\"/></a></li>");	
	}else
	*/ 
	if(windowStates.indexOf('maximized')!=-1){
		$ul.append("<li tp=\"pHander\">" +
			"<a onclick=\"getContainer('#"+portletWindowID+"').doMax();\" style=\"display:none\"><img src=\""+ROOT_PATH+"/images/10.gif\" style=\"border:none;cursor:pointer;\"/></a>" +
			"<a onclick=\"getContainer('#"+portletWindowID+"').doReSize();\"><img src=\""+ROOT_PATH+"/images/11.gif\" style=\"border:none;cursor:pointer;\"/></a></li>");
	}
}



/**
返回容器的DOM
@param objId container元素ID
@return 容器DOM对象
**/
function getContainer(objId) {
	//得到对象
	var _pt_tmp_container=$(objId);
	if(_pt_tmp_container==null){
		return null;
		}
	//得到DOM对象
	var _pt_tmp_container_dom_=_pt_tmp_container.get(0);
	
	//检查是否注册createPart函数
	if(typeof(_pt_tmp_container_dom_.createPart) != "function"){
		 var _pt_tmp_createPart =_pt_tmp_container.attr("createPart");
		 //是否自定义
        if (_pt_tmp_createPart != null) {
			//自定义
            _pt_tmp_container_dom_.createPart = eval(_pt_tmp_createPart);
        } else {
            _pt_tmp_container_dom_.createPart = _pt_defaultCreatePart;
        }
	}
		
	//检查是否注册createSep函数
	if(typeof(_pt_tmp_container_dom_.createSep) != "function"){
		 var _pt_tmp_createSep =_pt_tmp_container.attr("createSep");
		 //是否自定义
        if (_pt_tmp_createSep != null) {
			//自定义
            _pt_tmp_container_dom_.createSep = eval(_pt_tmp_createSep);
        } else {
            _pt_tmp_container_dom_.createSep = _pt_defaultCreateSep;
        }
	}
		
	//检查是否注册createBlankPart函数
	if(typeof(_pt_tmp_container_dom_.createBlankPart) != "function"){
		 var _pt_tmp_createBlankPart =_pt_tmp_container.attr("createBlankPart");
		 //是否自定义
        if (_pt_tmp_createBlankPart != null) {
			//自定义
            _pt_tmp_container_dom_.createBlankPart = eval(_pt_tmp_createBlankPart);
        } else {
            _pt_tmp_container_dom_.createBlankPart = _pt_defaultCreateBlankPart;
        }
	}

	//检查是否注册setTitle函数
	if(typeof(_pt_tmp_container_dom_.setTitle) != "function"){
		 var _pt_tmp_setTitle =_pt_tmp_container.attr("setTitle");
		 //是否自定义
        if (_pt_tmp_setTitle != null) {
			//自定义
            _pt_tmp_container_dom_.setTitle = window[_pt_tmp_setTitle]
        } else {
            _pt_tmp_container_dom_.setTitle = _pt_defaultSetTitle;
        }
	}
		
	//检查是否注册setMode函数
	if(typeof(_pt_tmp_container_dom_.setMode) != "function"){
		 var _pt_tmp_setMode =_pt_tmp_container.attr("setMode");
		 //是否自定义
        if (_pt_tmp_setMode != null) {
			//自定义
            _pt_tmp_container_dom_.setMode = eval(_pt_tmp_setMode);
        } else {
            _pt_tmp_container_dom_.setMode = _pt_defaultSetMode;
        }
	}

	//检查是否注册setContent函数
	if(typeof(_pt_tmp_container_dom_.setContent) != "function"){
		 var _pt_tmp_setContent =_pt_tmp_container.attr("setContent");
		 //是否自定义
        if (_pt_tmp_setContent != null) {
			//自定义
            _pt_tmp_container_dom_.setContent = eval(_pt_tmp_setContent);
        } else {
            _pt_tmp_container_dom_.setContent = _pt_defaultSetContent;
        }
	}
		
		
	//检查是否注册setExposed函数
	if(typeof(_pt_tmp_container_dom_.setExposed) != "function"){
		 var _pt_tmp_setExposed =_pt_tmp_container.attr("setExposed");
		 //是否自定义
        if (_pt_tmp_setExposed != null) {
			//自定义
            _pt_tmp_container_dom_.setExposed = eval(_pt_defaultSetExposed);
        } else {
            _pt_tmp_container_dom_.setExposed = _pt_defaultSetExposed;
        }
	}
		
	//检查是否注册setUnExposed函数
	if(typeof(_pt_tmp_container_dom_.setUnExposed) != "function"){
		 var _pt_tmp_setUnExposed =_pt_tmp_container.attr("setUnExposed");
		 //是否自定义
        if (_pt_tmp_setUnExposed != null) {
			//自定义
            _pt_tmp_container_dom_.setUnExposed = eval(_pt_tmp_setUnExposed);
        } else {
            _pt_tmp_container_dom_.setUnExposed = _pt_defaultSetUnExposed;
        }
	}
		
	if(typeof(_pt_tmp_container_dom_.doMax) != "function"){
		 var _pt_tmp_doMax=_pt_tmp_container.attr("doMax");
		 //是否自定义
		 if (_pt_tmp_doMax != null) {
		//自定义
			_pt_tmp_container_dom_.doMax = eval(_pt_tmp_doMax);
		} else {
			_pt_tmp_container_dom_.doMax = _pt_defaultdoMax;
		}
	}
	if(typeof(_pt_tmp_container_dom_.doHide) != "function"){
		 var _pt_tmp_doHide = _pt_tmp_container.attr("doHide");
		 //是否自定义
		 if (_pt_tmp_doHide != null) {
		//自定义
		 	_pt_tmp_container_dom_.doHide = eval(_pt_tmp_doHide);
		} else {
			_pt_tmp_container_dom_.doHide = _pt_defaultdoHide;
		}
	}	
	
	if(typeof(_pt_tmp_container_dom_.doReSize) != "function"){
			
			 var _pt_tmp_doReSize=_pt_tmp_container.attr("doReSize");
			 //是否自定义
			 if (_pt_tmp_doReSize != null) {
			//自定义
				_pt_tmp_container_dom_.doReSize = eval(_pt_tmp_doReSize);
			} else {
				_pt_tmp_container_dom_.doReSize = _pt_defaultdoReSize;
			}
	}
	//注册显示工具条
	if(typeof(_pt_tmp_container_dom_.showTips) != "function"){
			
			 var _pt_tmp_showTips=_pt_tmp_container.attr("showTips");
			 //是否自定义
			 if (_pt_tmp_showTips != null) {
			//自定义
				_pt_tmp_container_dom_.showTips = eval(_pt_tmp_showTips);
			} else {
				_pt_tmp_container_dom_.showTips = _pt_defaultShowTips;
			}
	}
	//注册隐藏工具条
	if(typeof(_pt_tmp_container_dom_.hideTips) != "function"){
			
			 var _pt_tmp_hideTips=_pt_tmp_container.attr("hideTips");
			 //是否自定义
			 if (_pt_tmp_hideTips != null) {
			//自定义
				_pt_tmp_container_dom_.hideTips = eval(_pt_tmp_hideTips);
			} else {
				_pt_tmp_container_dom_.hideTips = _pt_defaultHideTips;
			}
	}
	//注册增加最大/还原按钮
	if(typeof(_pt_tmp_container_dom_.addDoMaxResize) != "function"){
			
			 var _pt_tmp_addDoMaxResize=_pt_tmp_container.attr("addDoMaxResize");
			 //是否自定义
			 if (_pt_tmp_addDoMaxResize != null) {
			//自定义
				_pt_tmp_container_dom_.addDoMaxResize = eval(_pt_tmp_addDoMaxResize);
			} else {
				_pt_tmp_container_dom_.addDoMaxResize = _pt_defaultAddDoMaxResize;
			}
	}
	// --------------------------------系统方法(用户不可自定义)-----------------------------//
	//注册doView函数
	if(_pt_tmp_container_dom_.doView==null){
		_pt_tmp_container_dom_.doView=_pt_doView;
	}
	if(_pt_tmp_container_dom_.doRestore==null){
		_pt_tmp_container_dom_.doRestore=_pt_doRestore;
	}
	
	//注册doView函数
	if(_pt_tmp_container_dom_.doEdit==null){
		_pt_tmp_container_dom_.doEdit=_pt_doEdit;
	}
	//注册doView函数
	if(_pt_tmp_container_dom_.doHelp==null){
		_pt_tmp_container_dom_.doHelp=_pt_doHelp;
	}
	//注册doRefresh函数
	if(_pt_tmp_container_dom_.doRefresh==null){
		_pt_tmp_container_dom_.doRefresh=_pt_doRefresh;
	}
 		
	//注册doAction函数
	if(_pt_tmp_container_dom_.doAction==null){
		_pt_tmp_container_dom_.doAction=_pt_doAction;
	}
		//注册getOuter函数
	if(_pt_tmp_container_dom_.getOuter==null){
		_pt_tmp_container_dom_.getOuter=_pt_getOuter;
	}
	//注册getRow函数
	if(_pt_tmp_container_dom_.getRow==null){
		_pt_tmp_container_dom_.getRow=_pt_getRow;
	}
	if(_pt_tmp_container_dom_.setSupportModes==null){
		_pt_tmp_container_dom_.setSupportModes=_pt_setSupportModes;
	}
	if(_pt_tmp_container_dom_.getSupportModes==null){
		_pt_tmp_container_dom_.getSupportModes=_pt_getSupportModes;
	}
	if(_pt_tmp_container_dom_.setCurrentMode==null){
		_pt_tmp_container_dom_.setCurrentMode=_pt_setCurrentMode;
	}
	if(_pt_tmp_container_dom_.getCurrentMode==null){
		_pt_tmp_container_dom_.getCurrentMode=_pt_getCurrentMode;
	}
	
    return _pt_tmp_container_dom_;
}

function updatePageLayout(_param){
	$.get("/portal/pt/home/layout",_param);
}

/**
 * 单次加载函数
 * Portlet在拖放时,Dom内的Javascript会再次执行,如果不想再次执行,可以使用$.container(function(){ ... })
 * @author licza 
 */
(function($) {
	$.extend({
	container : function (fn){
		var fnHash=$.sha1(fn.toString()) ;
			if(document[fnHash]){
				//log('has done');
			}else{
				fn.call(this);
				document[fnHash]=-1;
			}
		}
	})
})(jQuery);

/**
 * SHA加密
 */
(function($) {
$.extend({
	sha1:function (s)
	{
	       return binb2hex(core_sha1(AlignSHA1(s)));
	} 
})

function core_sha1(blockArray)
{
  var x = blockArray;  //append padding
  var w = Array(80);
  var a =  1732584193;
  var b = -271733879;
  var c = -1732584194;
  var d =  271733878;
  var e = -1009589776;
 
  for(var i = 0; i < x.length; i += 16)  
  {
    var olda = a;
    var oldb = b;
    var oldc = c;
    var oldd = d;
    var olde = e;
 
    for(var j = 0; j < 80; j++)  
    {
      if(j < 16) w[j] = x[i + j];
      else w[j] = rol(w[j-3] ^ w[j-8] ^ w[j-14] ^ w[j-16], 1);
      
      var t = safe_add(safe_add(rol(a, 5), sha1_ft(j, b, c, d)),
                       safe_add(safe_add(e, w[j]), sha1_kt(j)));
      e = d;
      d = c;
      c = rol(b, 30);
      b = a;
      a = t;
    }
 
    a = safe_add(a, olda);
    b = safe_add(b, oldb);
    c = safe_add(c, oldc);
    d = safe_add(d, oldd);
    e = safe_add(e, olde);
  }
  return new Array(a, b, c, d, e);
 
}
function sha1_ft(t, b, c, d)
{
  if(t < 20) return (b & c) | ((~b) & d);
  if(t < 40) return b ^ c ^ d;
  if(t < 60) return (b & c) | (b & d) | (c & d);
  return b ^ c ^ d;  //t<80
}
 
 
function sha1_kt(t)
{
  return (t < 20) ?  1518500249 : (t < 40) ?  1859775393 :
         (t < 60) ? -1894007588 : -899497514;
}

/**
 * 将32位数拆成高16位和低16位分别进行相加，从而实现 MOD 2^32 的加法
 */
function safe_add(x, y)
{
  var lsw = (x & 0xFFFF) + (y & 0xFFFF);
  var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
  return (msw << 16) | (lsw & 0xFFFF);
}

/**
 * 32位二进制数循环左移
 */
function rol(num, cnt)
{
  return (num << cnt) | (num >>> (32 - cnt));
}

function AlignSHA1(str){
  var nblk=((str.length+8)>>6)+1, blks=new Array(nblk*16);
  for(var i=0;i<nblk*16;i++)blks[i]=0;
  for(i=0;i<str.length;i++)
    blks[i>>2]|=str.charCodeAt(i)<<(24-(i&3)*8);
  blks[i>>2]|=0x80<<(24-(i&3)*8);
  blks[nblk*16-1]=str.length*8;
  return blks;
}
/**
 * 16进制转换
 * @param 二进制数组
 */
function binb2hex(binarray)
{
  var hex_tab = 0 ? "0123456789ABCDEF" : "0123456789abcdef";
  var str = "";
  for(var i = 0; i < binarray.length * 4; i++)
  {
    str += hex_tab.charAt((binarray[i>>2] >> ((3 - i%4)*8+4)) & 0xF) +
           hex_tab.charAt((binarray[i>>2] >> ((3 - i%4)*8  )) & 0xF);
  }
  return str;
}
})(jQuery);

(function($){
	  $.baseball=function(p){
		 var  cols=$(p.accepter),//accepter集
		      ground=[],//坐标空间
			  t,//被拖动元素
			  m={},//被拖动元素属性集
			  tip=$("<div style='position:absolute;height:10px;border:4px dashed;overflow:hidden; background:#D7F6FA'></div>"),//占位元素
			  clone,
			  fly,//拖动启动开关
			  base,//被选中的垒
			  mark,//最后一次插入的相对对象
			  above=true,
		      befordrag=function(v){
				  v.stopPropagation();
				  v.preventDefault();
				  m={ex:v.clientX,ey:v.clientY+$(document).scrollTop(),x:t.position().left,y:t.position().top,w:t.width(),h:t.height()};
				  $(document).mousemove(ondrag).mouseup(afterdrag);
			      if(document.body.setCapture){t.get(0).setCapture();t.get(0).onmousewheel=mousewheel}
				  },
			  ondrag=function(v){
				  v.preventDefault();
				  if(!fly){
					  fly=true;
					  makeGround();
					  tip.insertBefore(t);
					  mark=t.get(0);
					  clone=t.clone().css({"position":"absolute","opacity":0.5,"left":m.x,"top":m.y,"width":m.w,"z-index":125058687}).insertAfter(t);
					  };
				  selectBase(v.clientX+$(document).scrollLeft(),v.clientY+$(document).scrollTop());
				  clone.css({"left":v.clientX-m.ex+m.x,"top":v.clientY-m.ey+m.y+$(document).scrollTop()});
				  },
			  afterdrag=function(v){
				  if(fly){
				  var _param=new Object();
				  _param.portletId=$(t).attr("pid");
				   _param.pageName=CUR_PAGE_NAME;
				   _param.pageModule=CUR_PPAGE_MODULE;
				  var  needUpdate=false;
					    if(m.lonely){
							t.appendTo(m.lonely);
							_param.destinationId=$(m.lonely).attr("pid");
							needUpdate=true;
							}else if(mark!==t.get(0)){
							if(above){t.insertBefore(mark)}else{t.insertAfter(mark)}
							_param.destinationId=$(mark).attr("pid");
							_param.isAfter=!above;
								needUpdate=true;
							}
					    	//如果拖放目标是自身  则不需要发送Ajax请求
							if(needUpdate){
								updatePageLayout(_param);
							}
							tip.remove();
							clone.remove();
				        }
				  fly=false;
			      if(document.body.releaseCapture) {t.get(0).releaseCapture();t.get(0).onmousewheel=null};
				  $(document).unbind("mousemove",ondrag).unbind("mouseup",afterdrag);
				  },
			  //make base poz
			  makeGround=function(){
				  ground.length=0;
				  cols.each(function(i,o){
					  var _o=$(o);
					  ground.push([_o.offset().left,_o.width(),homebase(_o),_o]);
					  })
				  },
			  //建立各垒坐标系统
			  homebase=function(q){
				  var area=[];
				  q.find(p.target).each(function(i,o){
						var _o=$(o),_t=_o.offset().top,_h=_o.height();
						area.push([_t+_h,_o.offset().left+_o.width()/2,_o.offset().top+_h/2,o]);
						});
				  return area;
				  },
			  //寻垒
			  selectBase=function(x,y){
				  var ball,_at,pi=Math.PI/4;
				  for(var i=0,el;el=ground[i];i++){
					  if(x>el[0]&&x<el[0]+el[1]) {base=el;break};
					  };
				  for(var i =0,el;el=base[2][i];i++){
					  if(y<el[0]){ball=el;break}
					  };
				   if(base[2].length==0){
					   tip.css({"width":base[3].width(),"height":t.height(),"left":base[3].offset().left,"top":base[3].offset().top});
					   m.lonely=base[3];
					   }else{
					   m.lonely=null;  
					   if(ball==null) {ball=base[2][base[2].length-1]};
					   _at=Math.atan2(ball[2]-y,x-ball[1])+Math.PI;
					   if(_at>pi&&_at<3*pi){niceShock(ball[3],3,false)}
					   else if(_at>3*pi&&_at<5*pi){niceShock(ball[3],2,false)}
					   else if(_at>5*pi&&_at<7*pi){niceShock(ball[3],1,true)}
					   else{niceShock(ball[3],4,true)};
					   };
				  },
				niceShock=function(o,n,u){
					if(n==1){
							tip.css({"width":$(o).width(),"height":$(o).height(),"left":$(o).offset().left,"top":$(o).offset().top-4})
							}
					else if(n==2){
						tip.css({"width":$(o).width(),"height":$(o).height(),"left":$(o).offset().left,"top":$(o).offset().top-4})
							}
					else if(n==3){
							tip.css({"width":$(o).width(),"height":$(o).height(),"left":$(o).offset().left,"top":$(o).offset().top+$(o).height()})
							}
					else{
							tip.css({"width":$(o).width(),"height":$(o).height(),"left":$(o).offset().left,"top":$(o).offset().top})
							};
					if(o==mark&&above===u) return false;
					mark=o;
					above=u;
					},
				mousewheel=function(){
				  window.scrollTo(0,document.documentElement.scrollTop-window.event.wheelDelta/4)
				  };
		    //初始绑定事件
			cols.find(p.target).each(function(i,o){
				  (p.handle?$(o).find(p.handle):$(o)).mousedown(function(v){t=$(o);befordrag(v)})
				  });
			  };
	})(jQuery);
/*	SWFObject v2.2 <http://code.google.com/p/swfobject/> 
is released under the MIT License <http://www.opensource.org/licenses/mit-license.php> 
*/
var swfobject=function(){
var D="undefined",r="object",S="Shockwave Flash",W="ShockwaveFlash.ShockwaveFlash",q="application/x-shockwave-flash",R="SWFObjectExprInst",x="onreadystatechange",O=window,j=document,t=navigator,T=false,U=[h],o=[],N=[],I=[],l,Q,E,B,J=false,a=false,n,G,m=true,M=function(){var aa=typeof j.getElementById!=D&&typeof j.getElementsByTagName!=D&&typeof j.createElement!=D,ah=t.userAgent.toLowerCase(),Y=t.platform.toLowerCase(),ae=Y?/win/.test(Y):/win/.test(ah),ac=Y?/mac/.test(Y):/mac/.test(ah),af=/webkit/.test(ah)?parseFloat(ah.replace(/^.*webkit\/(\d+(\.\d+)?).*$/,"$1")):false,X=!+"\v1",ag=[0,0,0],ab=null;if(typeof t.plugins!=D&&typeof t.plugins[S]==r){ab=t.plugins[S].description;if(ab&&!(typeof t.mimeTypes!=D&&t.mimeTypes[q]&&!t.mimeTypes[q].enabledPlugin)){T=true;X=false;ab=ab.replace(/^.*\s+(\S+\s+\S+$)/,"$1");ag[0]=parseInt(ab.replace(/^(.*)\..*$/,"$1"),10);ag[1]=parseInt(ab.replace(/^.*\.(.*)\s.*$/,"$1"),10);ag[2]=/[a-zA-Z]/.test(ab)?parseInt(ab.replace(/^.*[a-zA-Z]+(.*)$/,"$1"),10):0}}else{if(typeof O.ActiveXObject!=D){try{var ad=new ActiveXObject(W);if(ad){ab=ad.GetVariable("$version");if(ab){X=true;ab=ab.split(" ")[1].split(",");ag=[parseInt(ab[0],10),parseInt(ab[1],10),parseInt(ab[2],10)]}}}catch(Z){}}}return{w3:aa,pv:ag,wk:af,ie:X,win:ae,mac:ac}}(),k=function(){if(!M.w3){return}if((typeof j.readyState!=D&&j.readyState=="complete")||(typeof j.readyState==D&&(j.getElementsByTagName("body")[0]||j.body))){f()}if(!J){if(typeof j.addEventListener!=D){j.addEventListener("DOMContentLoaded",f,false)}if(M.ie&&M.win){j.attachEvent(x,function(){if(j.readyState=="complete"){j.detachEvent(x,arguments.callee);f()}});if(O==top){(function(){if(J){return}try{j.documentElement.doScroll("left")}catch(X){setTimeout(arguments.callee,0);return}f()})()}}if(M.wk){(function(){if(J){return}if(!/loaded|complete/.test(j.readyState)){setTimeout(arguments.callee,0);return}f()})()}s(f)}}();function f(){if(J){return}try{var Z=j.getElementsByTagName("body")[0].appendChild(C("span"));Z.parentNode.removeChild(Z)}catch(aa){return}J=true;var X=U.length;for(var Y=0;Y<X;Y++){U[Y]()}}function K(X){if(J){X()}else{U[U.length]=X}}function s(Y){if(typeof O.addEventListener!=D){O.addEventListener("load",Y,false)}else{if(typeof j.addEventListener!=D){j.addEventListener("load",Y,false)}else{if(typeof O.attachEvent!=D){i(O,"onload",Y)}else{if(typeof O.onload=="function"){var X=O.onload;O.onload=function(){X();Y()}}else{O.onload=Y}}}}}function h(){if(T){V()}else{H()}}function V(){var X=j.getElementsByTagName("body")[0];var aa=C(r);aa.setAttribute("type",q);var Z=X.appendChild(aa);if(Z){var Y=0;(function(){if(typeof Z.GetVariable!=D){var ab=Z.GetVariable("$version");if(ab){ab=ab.split(" ")[1].split(",");M.pv=[parseInt(ab[0],10),parseInt(ab[1],10),parseInt(ab[2],10)]}}else{if(Y<10){Y++;setTimeout(arguments.callee,10);return}}X.removeChild(aa);Z=null;H()})()}else{H()}}function H(){var ag=o.length;if(ag>0){for(var af=0;af<ag;af++){var Y=o[af].id;var ab=o[af].callbackFn;var aa={success:false,id:Y};if(M.pv[0]>0){var ae=c(Y);if(ae){if(F(o[af].swfVersion)&&!(M.wk&&M.wk<312)){w(Y,true);if(ab){aa.success=true;aa.ref=z(Y);ab(aa)}}else{if(o[af].expressInstall&&A()){var ai={};ai.data=o[af].expressInstall;ai.width=ae.getAttribute("width")||"0";ai.height=ae.getAttribute("height")||"0";if(ae.getAttribute("class")){ai.styleclass=ae.getAttribute("class")}if(ae.getAttribute("align")){ai.align=ae.getAttribute("align")}var ah={};var X=ae.getElementsByTagName("param");var ac=X.length;for(var ad=0;ad<ac;ad++){if(X[ad].getAttribute("name").toLowerCase()!="movie"){ah[X[ad].getAttribute("name")]=X[ad].getAttribute("value")}}P(ai,ah,Y,ab)}else{p(ae);if(ab){ab(aa)}}}}}else{w(Y,true);if(ab){var Z=z(Y);if(Z&&typeof Z.SetVariable!=D){aa.success=true;aa.ref=Z}ab(aa)}}}}}function z(aa){var X=null;var Y=c(aa);if(Y&&Y.nodeName=="OBJECT"){if(typeof Y.SetVariable!=D){X=Y}else{var Z=Y.getElementsByTagName(r)[0];if(Z){X=Z}}}return X}function A(){return !a&&F("6.0.65")&&(M.win||M.mac)&&!(M.wk&&M.wk<312)}function P(aa,ab,X,Z){a=true;E=Z||null;B={success:false,id:X};var ae=c(X);if(ae){if(ae.nodeName=="OBJECT"){l=g(ae);Q=null}else{l=ae;Q=X}aa.id=R;if(typeof aa.width==D||(!/%$/.test(aa.width)&&parseInt(aa.width,10)<310)){aa.width="310"}if(typeof aa.height==D||(!/%$/.test(aa.height)&&parseInt(aa.height,10)<137)){aa.height="137"}j.title=j.title.slice(0,47)+" - Flash Player Installation";var ad=M.ie&&M.win?"ActiveX":"PlugIn",ac="MMredirectURL="+O.location.toString().replace(/&/g,"%26")+"&MMplayerType="+ad+"&MMdoctitle="+j.title;
if(typeof ab.flashvars!=D){ab.flashvars+="&"+ac}else{ab.flashvars=ac}if(M.ie&&M.win&&ae.readyState!=4){var Y=C("div");X+="SWFObjectNew";Y.setAttribute("id",X);ae.parentNode.insertBefore(Y,ae);ae.style.display="none";(function(){if(ae.readyState==4){ae.parentNode.removeChild(ae)}else{setTimeout(arguments.callee,10)}})()}u(aa,ab,X)}}function p(Y){if(M.ie&&M.win&&Y.readyState!=4){var X=C("div");Y.parentNode.insertBefore(X,Y);X.parentNode.replaceChild(g(Y),X);Y.style.display="none";(function(){if(Y.readyState==4){Y.parentNode.removeChild(Y)}else{setTimeout(arguments.callee,10)}})()}else{Y.parentNode.replaceChild(g(Y),Y)}}function g(ab){var aa=C("div");if(M.win&&M.ie){aa.innerHTML=ab.innerHTML}else{var Y=ab.getElementsByTagName(r)[0];if(Y){var ad=Y.childNodes;if(ad){var X=ad.length;for(var Z=0;Z<X;Z++){if(!(ad[Z].nodeType==1&&ad[Z].nodeName=="PARAM")&&!(ad[Z].nodeType==8)){aa.appendChild(ad[Z].cloneNode(true))}}}}}return aa}function u(ai,ag,Y){var X,aa=c(Y);if(M.wk&&M.wk<312){return X}if(aa){if(typeof ai.id==D){ai.id=Y}if(M.ie&&M.win){var ah="";for(var ae in ai){if(ai[ae]!=Object.prototype[ae]){if(ae.toLowerCase()=="data"){ag.movie=ai[ae]}else{if(ae.toLowerCase()=="styleclass"){ah+=' class="'+ai[ae]+'"'}else{if(ae.toLowerCase()!="classid"){ah+=" "+ae+'="'+ai[ae]+'"'}}}}}var af="";for(var ad in ag){if(ag[ad]!=Object.prototype[ad]){af+='<param name="'+ad+'" value="'+ag[ad]+'" />'}}aa.outerHTML='<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"'+ah+">"+af+"</object>";N[N.length]=ai.id;X=c(ai.id)}else{var Z=C(r);Z.setAttribute("type",q);for(var ac in ai){if(ai[ac]!=Object.prototype[ac]){if(ac.toLowerCase()=="styleclass"){Z.setAttribute("class",ai[ac])}else{if(ac.toLowerCase()!="classid"){Z.setAttribute(ac,ai[ac])}}}}for(var ab in ag){if(ag[ab]!=Object.prototype[ab]&&ab.toLowerCase()!="movie"){e(Z,ab,ag[ab])}}aa.parentNode.replaceChild(Z,aa);X=Z}}return X}function e(Z,X,Y){var aa=C("param");aa.setAttribute("name",X);aa.setAttribute("value",Y);Z.appendChild(aa)}function y(Y){var X=c(Y);if(X&&X.nodeName=="OBJECT"){if(M.ie&&M.win){X.style.display="none";(function(){if(X.readyState==4){b(Y)}else{setTimeout(arguments.callee,10)}})()}else{X.parentNode.removeChild(X)}}}function b(Z){var Y=c(Z);if(Y){for(var X in Y){if(typeof Y[X]=="function"){Y[X]=null}}Y.parentNode.removeChild(Y)}}function c(Z){var X=null;try{X=j.getElementById(Z)}catch(Y){}return X}function C(X){return j.createElement(X)}function i(Z,X,Y){Z.attachEvent(X,Y);I[I.length]=[Z,X,Y]}function F(Z){var Y=M.pv,X=Z.split(".");X[0]=parseInt(X[0],10);X[1]=parseInt(X[1],10)||0;X[2]=parseInt(X[2],10)||0;return(Y[0]>X[0]||(Y[0]==X[0]&&Y[1]>X[1])||(Y[0]==X[0]&&Y[1]==X[1]&&Y[2]>=X[2]))?true:false}function v(ac,Y,ad,ab){if(M.ie&&M.mac){return}var aa=j.getElementsByTagName("head")[0];if(!aa){return}var X=(ad&&typeof ad=="string")?ad:"screen";if(ab){n=null;G=null}if(!n||G!=X){var Z=C("style");Z.setAttribute("type","text/css");Z.setAttribute("media",X);n=aa.appendChild(Z);if(M.ie&&M.win&&typeof j.styleSheets!=D&&j.styleSheets.length>0){n=j.styleSheets[j.styleSheets.length-1]}G=X}if(M.ie&&M.win){if(n&&typeof n.addRule==r){n.addRule(ac,Y)}}else{if(n&&typeof j.createTextNode!=D){n.appendChild(j.createTextNode(ac+" {"+Y+"}"))}}}function w(Z,X){if(!m){return}var Y=X?"visible":"hidden";if(J&&c(Z)){c(Z).style.visibility=Y}else{v("#"+Z,"visibility:"+Y)}}function L(Y){var Z=/[\\\"<>\.;]/;var X=Z.exec(Y)!=null;return X&&typeof encodeURIComponent!=D?encodeURIComponent(Y):Y}var d=function(){if(M.ie&&M.win){window.attachEvent("onunload",function(){var ac=I.length;for(var ab=0;ab<ac;ab++){I[ab][0].detachEvent(I[ab][1],I[ab][2])}var Z=N.length;for(var aa=0;aa<Z;aa++){y(N[aa])}for(var Y in M){M[Y]=null}M=null;for(var X in swfobject){swfobject[X]=null}swfobject=null})}}();return{registerObject:function(ab,X,aa,Z){if(M.w3&&ab&&X){var Y={};Y.id=ab;Y.swfVersion=X;Y.expressInstall=aa;Y.callbackFn=Z;o[o.length]=Y;w(ab,false)}else{if(Z){Z({success:false,id:ab})}}},getObjectById:function(X){if(M.w3){return z(X)}},embedSWF:function(ab,ah,ae,ag,Y,aa,Z,ad,af,ac){var X={success:false,id:ah};if(M.w3&&!(M.wk&&M.wk<312)&&ab&&ah&&ae&&ag&&Y){w(ah,false);K(function(){ae+="";ag+="";var aj={};if(af&&typeof af===r){for(var al in af){aj[al]=af[al]}}aj.data=ab;aj.width=ae;aj.height=ag;var am={};if(ad&&typeof ad===r){for(var ak in ad){am[ak]=ad[ak]}}if(Z&&typeof Z===r){for(var ai in Z){if(typeof am.flashvars!=D){am.flashvars+="&"+ai+"="+Z[ai]}else{am.flashvars=ai+"="+Z[ai]}}}if(F(Y)){var an=u(aj,am,ah);if(aj.id==ah){w(ah,true)}X.success=true;X.ref=an}else{if(aa&&A()){aj.data=aa;P(aj,am,ah,ac);return}else{w(ah,true)}}if(ac){ac(X)}})}else{if(ac){ac(X)}}},switchOffAutoHideShow:function(){m=false},ua:M,getFlashPlayerVersion:function(){return{major:M.pv[0],minor:M.pv[1],release:M.pv[2]}},hasFlashPlayerVersion:F,createSWF:function(Z,Y,X){if(M.w3){return u(Z,Y,X)}else{return undefined}},showExpressInstall:function(Z,aa,X,Y){if(M.w3&&A()){P(Z,aa,X,Y)}},removeSWF:function(X){if(M.w3){y(X)}},createCSS:function(aa,Z,Y,X){if(M.w3){v(aa,Z,Y,X)}},addDomLoadEvent:K,addLoadEvent:s,getQueryParamValue:function(aa){
	var Z=j.location.search||j.location.hash;if(Z){if(/\?/.test(Z)){Z=Z.split("?")[1]}
if(aa==null){return L(Z)}var Y=Z.split("&");for(var X=0;X<Y.length;X++){if(Y[X].substring(0,Y[X].indexOf("="))==aa){return L(Y[X].substring((Y[X].indexOf("=")+1)))}}}return""},expressInstallCallback:function(){if(a){var X=c(R);if(X&&l){X.parentNode.replaceChild(l,X);if(Q){w(Q,true);if(M.ie&&M.win){l.style.display="block"}}if(E){E(B)}}a=false}}}}();
