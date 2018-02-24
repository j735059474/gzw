/**
 * @private
 */
function Application(){
	this.pageUIMap = {};
}

/**
 * @param {} id
 * @param {} pageUI
 * @private
 */
Application.prototype.addPageUI = function(id, pageUI){
	this.pageUIMap[id] = pageUI;
};

/**
 * 
 * @param {} id
 * @return {}
 * @private
 */
Application.prototype.getPageUI = function(id){
	var pageUI = this.pageUIMap[id];
	if(pageUI == null)
		pageUI = getPopParent().pageUI;
	return pageUI;
};

/**
 * 
 * @return {}
 * @private
 */
function getApplication(){
	if(window.application == null)
		window.application = new Application();
	return window.application;
};


/**
 * @fileoverview Lfw客户端全局工具类。
 * @author dengjt
 */

/**
 * 通过此方法，获取json注册服务
 * 
 * @param sName 服务名
 * @public
 */
function getService(sName) {
	var service = new ServiceProxy(sName);
	return service;
};

/**
 * @class Service服务代理类
 * @param {} sName
 * @public
 */
function ServiceProxy(sName) {
	this.name = sName;
};

/**
 * 调用Service服务
 * @param {} method 方法名
 * @param {} args   参数列表
 * @return {}
 * @public
 */
ServiceProxy.prototype.execute = function(method, args){
	var obj = {};
	obj.rpcname = this.name;
	obj.method = method;
	for(var i = 1; i < arguments.length; i ++){
		obj["params" + (i - 1)] = arguments[i];
	}
	
	var data = toJSON(obj);
	var ajax = new Ajax();
	ajax.setPath(getCorePath() + "/rpc");
	ajax.setQueryStr("rpcdata=" + data);
	ajax.setReturnFunc(ServiceProxy.$returnFun);
	var innerArgs = [ null, null, ajax ];
	ajax.setReturnArgs(innerArgs);
	return ajax.post(false);
};

/**
 *@private 
 */
ServiceProxy.$returnFun = function(xmlHttpReq, returnArgs, exception) {
	var ajaxObj = returnArgs[2];
	var returnFunc = returnArgs[1];
	var userArgs = returnArgs[0];
	var text = xmlHttpReq.responseText;
	try{
		eval("var obj = " + text + ";");
	}
	catch(er ){
		eval("var obj = '" + text + "';");
	}
	return obj;
};
//调用后台接口集合
var ServerSet = {
	sign:{
		saveLog : function (){
			return getService("uap.lfw.sign.itf.ISignService");
		}
	}
};
/**
 * 获取编辑公式服务
 * @private
 */
function getFormularService() {
	return getService("nc.uap.lfw.core.model.formular.IEditFormularService");
};

/**
 * 获取上传文件服务
 * @private
 */
function getFileService() {
	return getService("uap.lfw.file.comp.IFileService");
};

/**
 * 获取页面参数Map表.此参数是从URL中获取
 * 
 * @return{HashMap} paramsMap
 * @private
 */
function getParamsMap() {
	return window.$paramsMap;
};

/**
 * 根据key获取页面参数
 * @private
 */
function getParameter(key) {
	var value = window.$paramsMap.get(key);
	return value == null ? null : decodeURIComponent(value);
};

/**
 * 设置某一参数
 * @private
 */
function setParameter(key, value) {
	window.$paramsMap.put(key, value);
};

/**
 * 获取客户端session属性map
 * @private
 */
function getSessionAttributeMap() {
	if (typeof $cs_clientSession != "undefined")
		return $cs_clientSession.map;
};

/**
 * 设置Session属性
 * @param key
 * @param value
 * @return
 * @private
 */
function setSessionAttribute(key, value) {
	$cs_clientSession.map[key] = value;
};

/**
 * 根据key获取客户段session中对应的value
 * @param key
 * @public
 */
function getSessionAttribute(key) {
	if (typeof $cs_clientSession != "undefined")
		return $cs_clientSession.map[key];
};

/**
 * 获取客户端StickString
 * @private
 */
function getStickString() {
	return window.$cs_clientStickKeys;
};

/**
 * 获取localStorage数据
 * @return {}
 */
function getStorageData(){
	if (window.localStorage == null || window.usercode == null)
		return null;
	if (window.$sd_storageData != null)
		return window.$sd_storageData;
	else{
		if (window.localStorage.getItem(window.usercode) == null){
			try{
				window.localStorage.setItem(window.usercode,"{}");
			}
			catch(e){
				return null;
			}
		}
		var storageDataStr = window.localStorage.getItem(window.usercode);
		if(typeof(JSON) == "undefined")
			window.$sd_storageData = eval("("+storageDataStr+")");
		else
			window.$sd_storageData = JSON.parse(storageDataStr);
		return window.$sd_storageData; 
	}
};

/**
 * 更新localStorage数据
 */
function saveStorageData(){
	if (window.$sd_storageData == null || window.localStorage == null || window.usercode == null)
		return;
	var storageDataStr = JSON.stringify(window.$sd_storageData);
	try{
		window.localStorage.setItem(window.usercode, storageDataStr);
	}catch(e){}
};

//function getSelfDefData(){}{
//	var storageData = getStorageData();
//	if (storageData == null)
//		return null;
//    return storageData["selfDef"];		
//};


/*
 * 页面在关闭或者刷新时调用该方法,如果processor中覆盖了基类 的destroy方法将会被调用,但用户可以阻止后台destroy方法的调用,
 * 只要重载onBeforeDestroyPage方法返回false即可
window.onbeforeunload = function() {
	if (window.pageUI == null)
		return;
	if (window.hasCloseConfirmed == true) {
		window.hasCloseConfirmed = null;
		return;
	}
	var result = window.pageUI.$onClosing();
	if (result == false) {
		return "是否关闭";
	} else {
		window.hasCloseConfirmed = null;
		return;
	}
};
 */
//if (IS_FF){
//	EventUtil.addEventHandler(window, "beforeunload", winunload);
//} 
//else{
	EventUtil.addEventHandler(window, "unload", winunload);
//}	

/***
 * @private
 */
function winunload(){
	if (window.pageUI == null)
		return;
	window.pageUI.$onClosed();
//	var proxyArray = getProxyArray();
//	if (proxyArray != null){
//		while(proxyArray.length > 0){
//			var proxy = proxyArray[0];
//			proxy.async = false;
//			proxy.execute();
//			proxyArray.splice(0, 1);
//		}
//	}
	removeAllComponent();
};
/**
 * 获取对应的数据格式渲染器
 * @private
 */
function getMasker(type) {
	if(typeof window.$maskerMeta != "undefined" &&  ((typeof IntegerTextComp != "undefined" && type == IntegerTextComp.prototype.componentType)
		||(typeof FloatTextComp != "undefined" && type == FloatTextComp.prototype.componentType)
		||(typeof MoneyTextComp != "undefined" && type == MoneyTextComp.prototype.componentType))){
		return new NumberMasker(window.$maskerMeta.NumberFormatMeta);
	}
	if(typeof window.$maskerMeta != "undefined" && typeof DateTextComp != "undefined" && type == DateTextComp.prototype.componentType){
		return new DateMasker(window.$maskerMeta.DateFormatMeta);
	}
	if(typeof window.$maskerMeta != "undefined" && type == "DateTimeText"){
		return new DateTimeMasker(window.$maskerMeta.DateTimeFormatMeta);
	}
	if(typeof PrecentTextComp != "undefined" && type == PrecentTextComp.prototype.componentType){
		return new PrecentMasker();
	}
	return null;
}

/**
 * 获取页面标识ID
 * @public
 */
function getPageId() {
	return getSessionAttribute("pageId");
};

/**
 * 获取页面唯一标识
 * @return
 * @public
 */
function getPageUniqueId() {
	return getSessionAttribute("pageUniqueId");
};

/**
 * 获取application唯一标识
 * @public
 */
function getAppUniqueId() {
	return getSessionAttribute("appUniqueId");
};

/**
 * 获取web应用context路径
 * @public
 */
function getRootPath() {
	return window.globalPath;
};

/**
 * 获取Web应用LfwDispatchServlet配置的路径
 * @public
 */
function getCorePath() {
	return window.corePath;
};

/**
 * 获取单据在nodes目录下的路径
 * @public
 */
function getNodePath() {
	return window.globalPath + "/html/nodes/" + window.$pageId;
};

/*
 * 非IE浏览器，增加XPath方式解析Dom的能力
 */
if (!IS_IE) {
	Element.prototype.selectNodes = function(xPath) {
		// XPathEvaluator利用方法evaluate()计算XPath表达式的值
		var evaluator = new XPathEvaluator();
		// evaluate的5个参数说明:XPath表达式,上下文节点,命名空间解释程序(只有在XML代码用到XML命名空间时才是必要的,通常留空null),
		// 返回的结果的类型(有10个常量值可以选择),在XPathResult中存放结果(通常为null)
		var nodeList = evaluator.evaluate(xPath, this, null,
				XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);
		var nodes = new Array();
		if (nodeList != null) {
			var node = nodeList.iterateNext();
			while (node) {
				nodes.push(node);
				node = nodeList.iterateNext();
			}
		}
		return nodes;
	};

	Element.prototype.selectSingleNode = function(xPath) {
		var evaluator = new XPathEvaluator();
		var oResult = evaluator.evaluate(xPath, this, null,
				XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);
		if (oResult != null) {
			var node = oResult.iterateNext();
			return node;
		} else {
			return null;
		}
	};
};

/**
 * 将传入string解析成Dom对象。避免浏览器兼容问题
 * @private
 */
function createXmlDom(strXML) {
	// IE
	if (window.ActiveXObject) {
		var sigArr = [ "MSXML2.DOMDocument.5.0", "MSXML2.DOMDocument.4.0",
				"MSXML2.DOMDocument.3.0", "MSXML2.DOMDocument",
				"Microsoft.XmlDom" ];
		for ( var i = 0; i < sigArr.length; i++) {
			try {
				var xmlDom = new ActiveXObject(sigArr[i]);
				// 文件按同步方式载入,默认为异步模式
				xmlDom.async = false;
				// loadXML()方法直接接受XML字符串
				// load()(参数为要载入的文件名)方法用于从服务器上载入XML文件(只能载入与包含JavaScript的页面存储于同一服务器上的文件)
				xmlDom.loadXML(strXML);
				return xmlDom;
			} catch (error) {
				// ignore
			}
		}
	}
	// FireFox
	else if (document.implementation && document.implementation.createDocument) {
		return new DOMParser().parseFromString(strXML, "text/xml");
	} else {
		throw new Error("Your browser doesn't support an XML DOM object.");
	}
};

/**
 * 显示进度对话框
 * 
 * @param message 要显示标题
 * @param attachComp 绑定控件，将显示于指定控件中心位置
 * @private
 */
function showProgressDialog(message, attachComp) {
	var topWin = getLfwTop();
	if (topWin == null) topWin = getTrueParent();
	topWin.ProgressDialogComp.showDialog(message);
};

/**
 * 隐藏进度对话框
 * @private
 */
function hideProgressDialog() {
	ProgressDialogComp.hideDialog();
};

/**
 * 显示错误对话框
 * @public
 */
function showErrorDialog(msg, func, title, okText) {
	var topWin = getLfwTop();
	if (topWin == null) topWin = getTrueParent();
	topWin.require('errordialog', function(){
		var dialog = topWin.ErrorDialogComp.showDialog(msg, title, okText, func);
//		if(func){
//			dialog.onclick = func;
//		}
	});
};

/**
 * 隐藏错误对话框
 * @public
 */
function hideErrorDialog() {
	ErrorDialogComp.hideDialog();
};

/**
 * 显示警告对话框
 * @public
 */
function showWarningDialog(msg) {
	var topWin = getLfwTop();
	if (topWin == null) topWin = getTrueParent();
	return topWin.WarningDialogComp.showDialog(msg);
};

/**
 * 隐藏警告对话框
 * @public
 */
function hideWarningDialog() {
	WarningDialogComp.hideDialog();
};

/**
 * 隐藏信息对话框
 * @public
 */
function hideMessageDialog() {
	MessageDialogComp.hideDialog();
};

/**
 * 在正中位置打开窗口
 * @public
 */
function openWindowInCenter(url, title, height, width) {
	if(!url){
		return;
	}
	if(url.indexOf("?") > 0){
		url = url + "&lrid=" + Math.UUID();
	}else{
		url = url + "?lrid=" + Math.UUID();
	}
	var bodyWidth = window.screen.availWidth;
	var bodyHeight = window.screen.availHeight;
	var left = 0;
	if(typeof (width) == 'number' || width.indexOf("%") == -1){
		var intWidth = parseInt(width);
		left = bodyWidth > intWidth ? (bodyWidth - intWidth)/2 : 0;
		width += "px";
	}else if(width.indexOf("%")>-1){
		var decimal = width.substring(0,width.indexOf("%"))/100;
		width = parseInt(bodyWidth*decimal) +"px";
	}else{
		//设置一个默认值
		width = "800px";
	}
	var top = 0;
	if(typeof (height) == 'number' || height.indexOf("%") == -1){
		var intHeight = parseInt(height);
		top = bodyHeight > intHeight ? (bodyHeight - intHeight)/2 : 0;
		height += "px";
	}
	else{
		height = bodyHeight + "px";
	}
	window.showModalDialog(url, self, "status:no;dialogHeight:" + height + ";dialogWidth:" + width + ";dialogLeft:" + left + "px;dialogTop:" + top + "px");
};

/**
 * 在正中位置打开窗口(非模态)
 * @param url
 * @param title
 * @param height
 * @param width
 * @param closeParent
 * @return
 * @public
 */
function openNormalWindowInCenter(url, title, height, width, closeParent) {
	if(!url){
		return;
	}
	if(url.indexOf("?") > 0){
		url = url + "&lrid=" + Math.UUID();
	}else{
		url = url + "?lrid=" + Math.UUID();
	}
	
	var bodyWidth = window.screen.availWidth;
	var bodyHeight = window.screen.availHeight;
	if(isPercent(width))
		width = bodyWidth*parseFloat(width)/100;
	if(isPercent(height))
		height = bodyHeight*parseFloat(height)/100;
	var left = bodyWidth > width ? (bodyWidth - width)/2 : 0;
	var top = bodyHeight > height ? (bodyHeight - height)/2 : 0;
	//title有空格会报错
	var win = window.open(url, "", "modal=yes, height=" + height + ", width=" + width + ", left=" + left + ", top=" + top, true);
	if(closeParent){
		if(win == window)
			return;
		closeWinWithNoWarn();
	}
};

/**
 * 最大化显示窗口
 * @param url
 * @param title
 * @param closeParent
 * @return
 * @public
 */
function showMaxWin(url, title, closeParent) {
	//showMaxWin('" + url + "', '" + title + "', " + closeParent + ")");
	if(!url){
		return;
	}
	if(url.indexOf("?") > 0){
		url = url + "&lrid=" + Math.UUID();
	}else{
		url = url + "?lrid=" + Math.UUID();
	}
	
	var win = window.open(url, title, 'resizable=no,scrollbars=yes');
	win.moveTo(-4, -4);
	var width = screen.availWidth+8;
	var height = screen.availHeight+7;

	win.resizeTo(width, height);
	if(closeParent){
		if(win == window)
			return;
		closeWinWithNoWarn();
	}
};

/**
 * 强行关闭窗口
 * @return
 * @public
 */
function closeWinWithNoWarn(){ 
	var browserName=navigator.appName; 
	
	if(IS_IE || IS_WEBKIT) { 
		window.opener=null; 
		window.open('','_self'); 
		window.close(); 
	} 
	//if (browserName=="Netscape") { 
	else{	
		window.open('','_parent',''); 
		window.close(); 
	} 
};

/**
 * 重定向当前页面
 * 
 * @param {} url
 * @public
 */
function sendRedirect(url, funcCode){
	if (getProxyReturnExecuting() > 0 || getProxyArray().length > 0 ){
		if (isNull(funcCode))
			setTimeout("sendRedirect(' "+ url + "')",100);
		else	
			setTimeout("sendRedirect(' "+ url + "', '" + funcCode + "')",100);
		return;
	}
	if (isNull(funcCode))
		window.location.href = url;
	else{
		try{
			var topWin = getLfwTop();
			if (topWin != null){
				topWin.MFSendRedirect(url, funcCode);
			}
			else
				window.location.href = url;
		}
		catch(e){
			window.location.href = url;
		}
	}	
};

/**
 * 打开一个模式窗体
 * @public
 */
function showWin(pageUrl, width, height) {
	
	
	
	var pos = pageUrl.indexOf("?");
	var randId = (Math.random() * 10000).toString().substring(0, 4);
	if (pos == -1)
		pageUrl += "?randid=" + randId;
	else
		pageUrl += "&randid=" + randId;
	
	pageUrl = pageUrl + "&lrid=" + Math.UUID();
	
	if (width == null || width == "")
		width = parseInt(window.screen.width) - 200;
	if (height == null || height == "")
		height = parseInt(window.screen.height) - 300;
	window.showModalDialog(pageUrl, self, "dialogHeight:" + height
			+ "px;dialogWidth:" + width
			+ "px;center:yes;resizable:yes;status:no");
};

/**
 * 显示导向某一页面的ModalDialog,id用来区分要显示的Dialog
 * @public
 */
function showDialog(pageUrl, title, width, height, id, refDiv, attr, twin) {
	//lrid在后台加，这里去掉，以保证etag逻辑
	/*
	if(pageUrl){
		if(pageUrl.indexOf("lrid=") == -1){
			if(pageUrl.indexOf("?") > 0){
				pageUrl = pageUrl + "&lrid=" + Math.UUID();
			}else{
				pageUrl = pageUrl + "?lrid=" + Math.UUID();
			}
		}
	}
	*/
	if(title){
		title = title.replaceAll(" ","&nbsp;");
	}
	if (showDialog.dialogCount == null)
		showDialog.dialogCount = 0;
	if (showDialog.dialogsTrueParent == null)
		showDialog.dialogsTrueParent = new Array();

	
	var dialogName = "$modalDialog" + showDialog.dialogCount;
	if (id == null)
		id = "";
	var nowWidth = document.body.clientWidth;
	var nowHeight = document.body.clientHeight;
	var topwin = getLfwTop();
	if(topwin != null){
		var topWidth = getLfwTop().document.body.clientWidth;
		var topHeight = getLfwTop().document.body.clientHeight;
	}else{
		var topWidth = nowWidth;
		var topHeight = nowHeight;
	}
	if(width == null)
		width = 400;
	if(height == null)
		height = 300;
	var oriWidth = width;
	var oriHeight = height;
	if(!isPercent(width) && width < 0)
		width = nowWidth + width;
	if(!isPercent(width) && width > topWidth)
		width = topWidth;  
	if (!isPercent(height) && height < 0)	
		height = nowHeight + height;
	if(!isPercent(height) && height > topHeight)
		height = topHeight;
	
	if(title == 'null')
		title = null;
	twin = (twin == null ? window : twin);
	if (showDialog.dialogsTrueParent == null)
		showDialog.dialogsTrueParent = new Object();
		
	var topwin = getLfwTop();
	if(topwin != null && topwin != window){
		
		return topwin.showDialog(pageUrl, title, width, height, id, null, attr, twin);
	}
		
	if (((!isPercent(width) && nowWidth < (width - 40)) || (!isPercent(height) && nowHeight < (height - 40))) && (window != top && parent.showDialog != null)) {
		showDialog.showInParent = true;
		if 	(parent.showDialog.dialogCount == null)
			parent.showDialog.dialogCount = 0;
		return parent.showDialog(pageUrl, title, width, height, id, null, attr, twin);
	}
	else{
		if (twin.isPopView && twin.isPopView == true)
			showDialog.dialogsTrueParent[showDialog.dialogCount] = twin.getTrueParent(); 
		else
			showDialog.dialogsTrueParent[showDialog.dialogCount] = twin; 
	}
	var isShowLine = true; 
	var isConfirmClose = false; 
	if(attr){
		isShowLine = getBoolean(attr.isShowLine, isShowLine);
		isConfirmClose = getBoolean(attr.isConfirmClose, isConfirmClose);
	}
	
	//弹出窗口比最外层页面大时，设置宽或高为100%
	var topWidth = window.document.body.clientWidth;
	var topHeight = window.document.body.clientHeight;
	//if(!isPercent(width) && width > topWidth)
	//	width = '100%';
	//if (!isPercent(height) && height > topHeight)	
	//	height = '100%';
	
	if (window[dialogName] == null) {
		window[dialogName] = new ModalDialogComp("g_modalDialog", title, 0, 0,
				width, height, null, {"isShowLine":isShowLine});
	} 
	else {
		window[dialogName].setSize(width, height);
		window[dialogName].setTitle(title);
		window[dialogName].showLine(isShowLine);
	}
	window[dialogName].oriHeight = oriHeight;
	window[dialogName].oriWidth = oriWidth;
	window[dialogName].onAfterClose = function() {
		destroyDialog(dialogName);
//		var frame = this.getContentPane().firstChild;
//		if(frame){
//			var id = this.dialogId;
//			var canDestroy = canDestroyDialog(frame);
//			if (canDestroy == false){
//				if(typeof(id) != 'undefined' && id != null){
//					setTimeout("destroyDialog('" + id + "')",100);
//				}else{
//					setTimeout("destroyDialog()",100);
//				}
//				return;
//			}
//			destroyDialog(dialogName);
//		}
	};
	if(isConfirmClose){
//		var dls = new DialogListener();
		var dls = new Listener("beforeClose");
		dls.source_id = dialogName;
		dls.listener_id = 'onBeforeClose_' + dialogName;
		
		dls.func = function (){
			if (window["$modalDialogFrame" + id] == null ||  typeof (window["$modalDialogFrame" + id].contentWindow) == "unknown") return;
			if(window["$modalDialogFrame" + id].contentWindow == null) return;
			var pageui = window["$modalDialogFrame" + id].contentWindow.pageUI;
			if(pageui && window[dialogName])
				return pageui.showCloseConfirm(window[dialogName]);
		};
		window[dialogName].addListener(dls);
	}
	
	var iframe = $ce("iframe");
	iframe.name = "in_frame";
	iframe.id = "in_frame";
	iframe.allowTransparency = "true";
	iframe.frameBorder = 0;
	iframe.style.width = "100%";
	iframe.style.height = "100%";
	// 处理弹出窗口flowMode为true时出现竖向滚动条问题
	if(!IS_IPAD){
		iframe.style.position = "absolute";
		iframe.style.left = "0px";
	}
	
	window["$modalDialogFrame" + id] = iframe;
	window[dialogName].getContentPane().appendChild(iframe);
	window[dialogName].dialogId = id;
	var reload = false;
	if(pageUrl != null && pageUrl != "")
		iframe.src = pageUrl;
	else{
		if(window.domain_key != null)
			iframe.src = "/lfw/setdomain.jsp";
	}
	window[dialogName].closeBt.style.visibility = "hidden";
	window[dialogName].show(refDiv);
	showDialogColseIcon(id, dialogName);
	
	showDialog.dialogCount++;
	// 记录modalDialog用于UIMetaRender时根据flowmode设置overflow
	document.modalDialog = window[dialogName];
	// 重新计算iframe的高度
	if (!IS_IE7)
		document._pt_frame_id =  iframe.id;
//	adjustIFramesHeightOnLoad(iframe);
	return [window[dialogName], iframe];
};

function canDestroyDialog(frame){
	if (frame && frame.contentWindow.ServerProxy && (frame.contentWindow.getProxyReturnExecuting() > 0 || frame.contentWindow.getProxyArray().length > 0 ))
		return false;
	if(typeof frame.contentWindow.getTrueParent == 'function'){
		var parent = frame.contentWindow.getTrueParent();
		if (parent != null && parent.ServerProxy && (parent.getProxyReturnExecuting() > 0 || parent.getProxyArray().length > 0))
			return false;
	}
	return true;
};

/**
 * 
 * @param {} id
 * @private
 */
function destroyDialog(dialogName) {
	if (dialogName == null) return;
	var dialog = window[dialogName];
	if (dialog == null) return;
	var frame = dialog.getContentPane().firstChild;
	if (frame == null) return;
	
	var canDestroy = canDestroyDialog(frame);
	if (canDestroy == false){
		setTimeout("destroyDialog('" + dialogName + "')",100);
		return;
	}
	var id = dialog.dialogId;
	var dialog = getDialog(id);
	if (dialog) {
		try{
			var frame = dialog.getContentPane().firstChild;
			if(frame.contentWindow.handleClose){
				frame.contentWindow.handleClose();
			}
			frame.src = "";
			frame.isDestroy = true;
			window["$modalDialogFrame" + id] = null;
			showDialog.dialogsTrueParent[showDialog.dialogCount] = null;
			dialog.getContentPane().removeChild(frame);
		}catch(e){};
		dialog = null;
	}
};

/**
 * @private
 */
function lazyRender(){
	var iframe = lazyRender.iframe;
	var templateStr = lazyRender.templateStr;
	try{
		lazyRender.iframe = null;
		lazyRender.templateStr = null;
		iframe.contentWindow.document;
	}
	catch(error){
		lazyRender.iframe = iframe;
		lazyRender.templateStr = templateStr;
		setTimeout("lazyRender()", 100);
		return;
	}
	iframe.contentWindow.document.write(templateStr);
}

/**
 * 显示弹出窗close图标
 * @param {} id
 * @param {} dialogName
 * @param {} count  setTimeout次数，限制在10次
 * @private
 */
function showDialogColseIcon(id, dialogName, count){
	if (count == null) 
		count = 1;
	if (window["$modalDialogFrame" + id] != null){
		try{
			 window["$modalDialogFrame" + id].contentWindow.renderDone;
		}
		catch(error){
			setTimeout("showDialogColseIcon('" + id+ "', '" + dialogName + "'," + count + ")",100);
			return;
		}
		if(window["$modalDialogFrame" + id].contentWindow.renderDone == true || count >=10)
			window[dialogName].closeBt.style.visibility = "";
		else{
			count += 1;
			setTimeout("showDialogColseIcon('" + id+ "', '" + dialogName + "'," + count + ")",100);
		}
	}
	else{
		count += 1;
		setTimeout("showDialogColseIcon('" + id+ "', '" + dialogName + "'," + count + ")",100);
	}
}


/**
 * 隐藏对话框
 * @param id 对话框id
 * @param hideImmediate 立即关闭，一般设置成false或不设置
 * @return
 * @public
 */
function hideDialog(id, hideImmediate) {
	//前台直接hideDialog在IE9，FF下会出错
	if ((hideImmediate == null ||hideImmediate == false)){
		if(typeof(id) != 'undefined' && id != null){
			setTimeout("hideDialog('" + id + "', true)",100);
		}else{
			setTimeout("hideDialog(null, true)",100);
		}
		return;
	}
	var dialog = getDialog(id);
	if (dialog) {
		var frame = dialog.getContentPane().firstChild;
		if (frame && frame.contentWindow.ServerProxy && (frame.contentWindow.getProxyReturnExecuting() > 0 || frame.contentWindow.getProxyArray().length > 0 )){
			if(typeof(id) != 'undefined' && id != null){
				setTimeout("hideDialog('" + id + "')",100);
			}else{
				setTimeout("hideDialog()",100);
			}
			return;
		}
		dialog.close();
	}
};

/**
 * 获取对话框，如果id为null获取最上层对话框
 * 
 * @param {} id
 * @public
 */
function getDialog(id){
	for (var i = showDialog.dialogCount - 1; i>=0; i --){
		var dialogName = "$modalDialog" + i;
		var dialog = window[dialogName];
		if (dialog == null)
			continue;
		var frm = null;
		if (dialog.getContentPane)
			frm = dialog.getContentPane().firstChild;
		if (frm == null || (frm.isDestroy != null && frm.isDestroy == true) )
			continue;
		
		if 	(typeof(id) == 'undefined' || id == null) 
			return dialog;
		
		if (dialog.dialogId == null) 
			continue;
		if 	(dialog.dialogId == id) 
			return dialog;
	}
	return null;
};

/**
 * 关闭窗口
 * @private
 */
function closeWindow(){
	if (window.ServerProxy && window.getProxyReturnExecuting() > 0){
		setTimeout("closeWindow()",100);
		return;
	}
	try{
		window.close();
	}catch(e){}
};

/**
 * 获取当前对话框
 * @param isOpen
 * @return
 * @public
 */
function getCurrentDialog(isOpen) {
	var dialogName = null;
	if (isOpen == null || isOpen == false)
		dialogName = "$modalDialog" + (showDialog.dialogCount);
	else
		dialogName = "$modalDialog" + (showDialog.dialogCount - 1);
	return window[dialogName];
};

/**
 * 获取真正所属父窗口，并非目前显示的父窗口
 * @public
 */
function getTrueParent() {
//	return parent.showDialog.trueParent != null ? parent.showDialog.trueParent
//			: parent;
	if (parent == window){
		return window;
	}
	if(parent.showDialog){
		if (parent.showDialog.dialogsTrueParent == null)
			return parent;
		else{
//			return parent.showDialog.dialogsTrueParent[parent.showDialog.dialogCount - 1] != null ? parent.showDialog.dialogsTrueParent[parent.showDialog.dialogCount - 1]
//					: parent;
			
			for (var i = parent.showDialog.dialogCount - 1; i>=0; i --){
				dialogName = "$modalDialog" + i;
				dialog = parent[dialogName];
				if (dialog == null) continue;
				
				var frame = parent["$modalDialogFrame" + dialog.dialogId];
				if (frame != null && frame.contentWindow != null && frame.contentWindow == window){
					return parent.showDialog.dialogsTrueParent[i] != null ? parent.showDialog.dialogsTrueParent[i] : parent;
				}
			}
			return parent;
		}
	}else{
		if (parent.pageUI)
			return parent;
		else
			return window;
	}
};

/**
 * 带有lfwtop标识的一层父(portal)
 * @return {}
 * @private
 */
function getLfwTop(){
	if (window.lfwtop){
		window.lfwtopwin = window;
		return window.lfwtopwin;
	}
	var parentWin = window.parent;
	try{
		while(parentWin != null && parentWin != window){
			
			if(parentWin.showDialog)
				window.hasShowDialogWin = parentWin;
				
			if(parentWin.lfwtop){
				window.lfwtopwin = parentWin;
				break;
			}
			
			if(parentWin == parentWin.parent)
				break;
			parentWin = parentWin.parent;
		}
	}
	catch(error){
	}
	
	if(window.lfwtopwin) 
		return window.lfwtopwin;
	return window.hasShowDialogWin;

};

/**
 * 获取顶端页面
 * @return
 * @public
 */
function getPopParent() {
	if(window.parentWindow != null)
		return window.parentWindow;
	try{
//		debugger;
//		if (window.location.indexOf("app/infpubcard/reference?") > 0)
//			return getTrueParent();
		if (window.parent != window)
			return getTrueParent();
		if (window.dialogArguments != null
				&& window.parent.dialogArguments == window.dialogArguments)
			return window.dialogArguments;
		if (window.opener)
			return window.opener;
	}
	catch(error){
		return null;
	}
};

window.parentWindow = getPopParent();
///**
// * 获取顶端窗口
// * @return
// */
//function getTop() {
//	var win = window;
//	while(win != null && (win.lfwTop == null || win.lfwTop == false)) {
//		win = getPopParent();
//	}
//	return win;
//};

/**
 * 
 * @param {} content
 * @return {}
 * @private
 */
function compress(content){
	if(window.debugMode == true){
		return null;
	}
	var top = getLfwTop();
	if(top == null)
		return null;
	if(top.compressContent){
		var result = top.compressContent(content);
		return result;
	}
	return null;
}

/**
 * 获取APP宿主窗口
 * @private
 * 
 */
function getAppTopWindow(){
	//取app自身的最顶层window
	var wid = getPopParent();
	var appTopWid = wid.parent;
	return appTopWid;
}

/**
 * 为文本编辑器所定制的显示对话框的方法
 * @public
 */
function showCommonDialog(pageUrl, title, width, height, id) {
	if (id == null)
		id = "";
	
	if(!pageUrl){
		return;
	}
	if(pageUrl.indexOf("?") > 0){
		pageUrl = pageUrl + "&lrid=" + Math.UUID();
	}else{
		pageUrl = pageUrl + "?lrid=" + Math.UUID();
	}
	
	var returnValue = showModalDialog(pageUrl, window, "dialogWidth:"
			+ width + ";dialogHeight:" + height + ";status:0;help:0;");
	return returnValue;
};

/**
 * 处理Ajax请求的异常信息
 * 
 * @return true 没有后台异常，处理成功；false表示不成功，并在异常对话框中显示后台的异常信息
 * @private
 */
function handleException(xmlHttpReq, exception, ajaxArgs, ajaxObj) {
	var doc = xmlHttpReq.responseXML;
	return handleExceptionByDoc(doc, exception, ajaxArgs, ajaxObj, xmlHttpReq);
};

/**
 * 进入登录页面
 * @private
 */
function openLoginPage() {
	var url = window.globalPath + "/app/mockapp/login.jsp&randid=" + (new Date()).getTime();
	if (window.top != window) {
		var parentPage = parent;
		while (parentPage){
			if (parentPage == parentPage.parent)
				break;
			parentPage = parentPage.parent;
		}
		if (parentPage)
			parentPage.location.href = url;
	} else {
		window.location.href = url;
	}
};

/**
 * 如果有异常发生，将后台的异常信息显示在异常对话框中，返回false。否则直接返回true
 * @private
 */
function handleExceptionByDoc(doc, exception, ajaxArgs, ajaxObj, xmlHttpReq) {
	if (exception) {
		if (xmlHttpReq != null && xmlHttpReq.status == 306) {  // 如果是会话失效，则跳转到登陆页
			openLoginPage();
		} else {
			showErrorDialog(exception);
		}
		return false;
	}

	if(doc == null)
		return;
	var rootNode = doc.documentElement;
	if (rootNode == null)
		return;
	var successNode = rootNode.selectSingleNode("success");
	if(successNode == null)
		return;
	var success = successNode.firstChild.nodeValue;
	if (success == "false") {
		var expTextNode = rootNode.selectSingleNode("exp-text");
		var expStackNode = rootNode.selectSingleNode("exp-stack");
		var showMessageNode = rootNode.selectSingleNode("show-message");
		var showTitleNode = rootNode.selectSingleNode("show-title");
		var expText = expTextNode.firstChild == null ? "": expTextNode.firstChild.nodeValue;
		if (IS_IE)
			expStack = expStackNode.firstChild.nodeValue;
		else
			expStack = expStackNode.firstChild.nextSibling.data;

		var showMessage = showMessageNode.firstChild == null ? "error occurred": showMessageNode.firstChild.nodeValue;
		var showTitle = null;
		if (showTitleNode)
			showTitle = showTitleNode.firstChild == null ? null : showTitleNode.firstChild.nodeValue;
		showErrorDialog(showMessage, null, showTitle, null);
		return false;
	}
	else if (success == "validator") {
		var expNode = rootNode.selectSingleNode("exp-text");
		var expText = null;
		if(expNode){
			expText = expNode.firstChild.nodeValue;
		}
		
		var expView = changeNodeToObject(rootNode.selectSingleNode("exp-view"));
		
		expNode = rootNode.selectSingleNode("exp-components");
		if(expNode){
			var exceptionMsg = null;
			var exp_children = expNode.childNodes;
			if(exp_children && exp_children.length > 0){
				for(var k=0; k<exp_children.length; k++){
					var expComponent = changeNodeToObject(exp_children[k]);
					if(exp_children[k]){
						expComponent.elements = new Array();
						var children = exp_children[k].childNodes;
						if(children && children.length > 0){
							for(var i=0; i<children.length; i++){
								var element = changeNodeToObject(children[i]);
								if(typeof(element.id) != 'undefined'){
									expComponent.elements.push(element);
								}
							}
						}
					}
					
					if(typeof(expView.id) == "string" && typeof(expComponent.id) == "string"){
						var widget = pageUI.getWidget(expView.id);
						if(widget){
							var component = widget.getComponent(expComponent.id);
							if(component){
								if(component.componentType == "AUTOFORM"){
									if(component.errorMsg && expComponent.errorMsg){
										component.errorMsg.innerHTML = expComponent.errorMsg;
										component.setWholeErrorPosition();
										component.errorMsgDiv.style.display = "block";
									}
									var index = "";
									if(component.dataset){
										if(component.dataset.focusRowIndex)
											index = "_" + component.dataset.focusRowIndex;
									}
									var eleArr = component.eleArr;
									if(eleArr && eleArr.length > 0){
										var element;
										for(var i=0; i<eleArr.length; i++){
											element = eleArr[i];
											if(typeof(element) == "object"){
												for(var j=0; j<expComponent.elements.length; j++){
													if(expComponent.elements[j].id == (element.id + index)){
														if(typeof(element.setError) == 'function'){
															element.setError(true);
														}
														if(typeof(element.setErrorMessage) == 'function'){
															element.setErrorMessage(expComponent.elements[j].errorMsg);
														}
														if(typeof(element.setErrorStyle) == 'function'){
															element.setErrorStyle();
														}
														if(typeof(element.setErrorPosition) == 'function'){
															element.setErrorPosition();
														}
														break;
													}
												}
											}
										}
									}
								}else if(component.componentType == "GRIDCOMP"){
									if(component.errorMsg && expComponent.errorMsg){
										component.errorMsg.innerHTML = expComponent.errorMsg;
										component.setWholeErrorPosition();
										component.errorMsgDiv.style.display = "block";
									}
									
									var eleArr = component.basicHeaders;
									if(eleArr && eleArr.length > 0){
										var cell = null;
										for(var i=0; i<eleArr.length; i++){
											for(var j=0; j<expComponent.elements.length; j++){
												if(eleArr[i].isHidden == false && expComponent.elements[j].id.split("_")[0] == eleArr[i].keyName){
													if(eleArr[i].dataDiv.cells.length == 1){
														cell = eleArr[i].dataDiv.cells[0];
													}else{
														if(expComponent.elements[j].id.split("_").length > 1){
															cell = eleArr[i].dataDiv.cells[expComponent.elements[j].id.split("_")[1]];
														}else{
															cell = eleArr[i].dataDiv.cells[0];
														}
													}
													//cell.errorMsg = expComponent.elements[j].errorMsg;//grid输入框错误提示显示位置有误，暂时注掉。
													
													var warningIcon = cell.warningIcon;
													if(typeof(warningIcon) == 'undefined'){
														warningIcon = $ce("DIV");
														warningIcon.className = "cellwarning";
														cell.warningIcon = warningIcon;
														cell.style.position = "relative";
													}
													cell.appendChild(warningIcon);
													if (typeof(cell.errorMsg) == "string" && cell.errorMsg != "") {
														warningIcon.style.display = "block";	
													}else{
														warningIcon.style.display = "none";
													}
												}
											}
										}
									}
								}else{// if(component.componentType == "TEXT")
									var element = component;
									if(typeof(element.setError) == 'function'){
										element.setError(true);
									}
									if(typeof(element.setErrorMessage) == 'function'){
										element.setErrorMessage(trans("ml_thisfieldcannotnull"));
									}
									if(typeof(element.setErrorStyle) == 'function'){
										element.setErrorStyle();
									}
									if(typeof(element.setErrorPosition) == 'function'){
										element.setErrorPosition();
										element.noShowErrorMsgDiv = true;
									}
									if(expComponent.errorMsg){
										exceptionMsg = expComponent.errorMsg;
									}
								}
							}else{
								//showErrorDialog('当前Component:'+expComponent.id+'不存在!');
							}
						}else{
							//showErrorDialog('当前View:'+expView.id+'不存在!');
						}
					}
				}
			}
			if(typeof(exceptionMsg) == "string"){
				showErrorDialog(exceptionMsg);
			}
		}
		return false;
	}
	else if (success == "interaction") {
//		pageUI.PROXYRETURN_EXECUTING ++;
		proxyReturnExecutingAdd();
//		var showMessageNode = rootNode.selectSingleNode("show-message");
//		var key = rootNode.selectSingleNode("exp-text").firstChild.nodeValue;
//		rePostReq.ajaxArgs = ajaxArgs;
//		rePostReq.key = key;
//		showConfirmDialog(showMessageNode.firstChild.nodeValue, rePostReq, cancelPost);
//		return window.isContinuePost;
		var rootNode = doc.documentElement;
		var contentsNode = rootNode.selectSingleNode("contents"); 
		var contentNodes = contentsNode.selectNodes("content");
		var content = getNodeValue(contentNodes[0]);
//		content = decodeURIComponent(content);
		eval("var interationInfo = " + content);
		rePostReq.dialogId = interationInfo.id;
		if(interationInfo.type == "OKCANCEL_DIALOG") {
			var msg = interationInfo.msg;
			var title = interationInfo.title;
			var okText = interationInfo.okText;
			var cancelText = interationInfo.cancelText;
			rePostReq.ajaxObj = ajaxObj.clone();
			showConfirmDialog(msg, rePostOk, rePostCancel, null, null, null, okText, cancelText, title);
		}
		else if(interationInfo.type == "THREE_BUTTONS_DIALOG") {
			var msg = interationInfo.msg;
			var title = interationInfo.title;
			rePostReq.ajaxObj = ajaxObj.clone();
			var topWin = getLfwTop();
			if (topWin == null) topWin = getTrueParent();
			topWin.require("threebuttondialog", function(){topWin.ThreeButtonsDialog.showDialog(msg, rePostOk, rePostCancel, rePostMiddle, interationInfo.btnTexts, null, null, null, null, null, title)});
		}
		else if (interationInfo.type == "MESSAGE_DIALOG") {
			var msg = interationInfo.msg;
			var title = interationInfo.title;
			var btnText = interationInfo.btnText;
			rePostReq.ajaxObj = ajaxObj.clone();
			rePostReq.okReturn = interationInfo.okReturn;
			showMessageDialog(msg, rePostOk, title, btnText,false);
		}
		else if (interationInfo.type == "ERROR_MESSAGE_DIALOG"){
			var msg = interationInfo.msg;
			var title = interationInfo.title;
			var btnText = interationInfo.btnText;
			rePostReq.ajaxObj = ajaxObj.clone();
			rePostReq.okReturn = interationInfo.okReturn;
			showErrorDialog(msg, rePostOk, title, btnText);
		}
		else if(interationInfo.type == "INPUT_DIALOG") {
			var items = interationInfo.items;
			if (items != null) {
				var title = interationInfo.title;
				var id = rePostReq.dialogId;
				ajaxObj = ajaxObj.clone();
				ajaxObj.addParam(id + "interactflag", "true");
				rePostReq.ajaxObj = ajaxObj;
				noPostCancel.ajaxObj = ajaxObj;
				var topWin = getLfwTop();
				if (topWin == null) topWin = getTrueParent();
				topWin.require("inputdialog", function(){
					var inputDlg = new topWin.InputDialogComp("input_dialog", title, 0, 0, null, null, null, rePostReq, noPostCancel);
					rePostReq.inputDlg = inputDlg;
					for (var i = 0; i < items.length; i++) {
						var item = items[i];
						var inputId = item.inputId;
						var labelText = item.labelText;
						var inputType = item.inputType;
						var required = item.required; 
						var value = item.value;
						if (inputType == "string" || inputType == "pswtext") {
							inputDlg.addItem(labelText, inputId, inputType, required, null, value);
						}
						else if (inputType == "int") {
							var attr = new Object;
							attr.minValue = item.minValue;
							attr.maxValue = item.maxValue;
							inputDlg.addItem(labelText, inputId, inputType, required, attr, value);
						}
						else if(inputType == "combo") {
							if (item.comboData){
								var datas = item.comboData.allCombItems;
								var comboData = new topWin.ComboData();
								var attr = new Object;
								attr.selectOnly = item.selectOnly;
								var combData = new topWin.ComboData();
								for(var j = 0; j < datas.length; j++){
									var text = datas[j].text;
									if(text == null)
										text = datas[j].i18nName;
									combData.addItem(new topWin.ComboItem(text, datas[j].value));
								}
								attr.comboData = combData;	
								inputDlg.addItem(labelText, inputId, inputType, required, attr, value);
							}
						}
						else if(inputType == "radio"){
							var datas = item.comboData.allCombItems;
							var comboData = new topWin.ComboData();
							var attr = new Object;
							attr.selectOnly = item.selectOnly;
							var combData = new topWin.ComboData();
							for(var j = 0; j < datas.length; j++){
								var text = datas[j].text;
								if(text == null)
									text = datas[j].i18nName;
								combData.addItem(new topWin.ComboItem(text, datas[j].value));
							}
							attr.comboData = combData;	
							inputDlg.addItem(labelText, inputId, inputType, required, attr, value);
						}
						else if(inputType == "languagecombotext"){
							var datas = item.comboData.allCombItems;
							var attr = new Object;
							attr.comboData = datas;	
							attr.currentLangCode = item.currentLangIndex;
							attr.defaultLangCode = item.defaultLangIndex;
							inputDlg.addItem(labelText, inputId, inputType, required, attr, value);
						}
					}
					inputDlg.show();
				});
			}
		}
		return false;
	}
	return true;
};

/**
 * Dom节点转换成对象
 * @private
 */
function changeNodeToObject(expNode){
	var expObj = new Object();
	if(expNode){
		if(expNode.attributes && expNode.attributes.length){
			for(var i=0; i<expNode.attributes.length; i++){
				if('nodeId' == expNode.attributes[i].nodeName){
					expObj.id = expNode.attributes[i].nodeValue;						
				}else if('errorMsg' == expNode.attributes[i].nodeName){
					expObj.errorMsg = expNode.attributes[i].nodeValue;
				}
			}
		}
	}
	return expObj;
}

/**
 * 点OK后执行方法
 * @private
 */
function rePostOk() {
	if(rePostReq.okReturn == null || rePostReq.okReturn == true){
		var ajaxObj = rePostReq.ajaxObj;
		var id = rePostReq.dialogId;
		ajaxObj.addParam(id + "interactflag", "true");
		showDefaultLoadingBar();
		ajaxObj.post();
	}
	else{
//		pageUI.PROXYRETURN_EXECUTING --;
		proxyReturnExecutingSub();
	}
	rePostReq.okReturn = null;
};

/**
 * @private
 */
function rePostMiddle() {
	if(rePostReq.okReturn == null || rePostReq.okReturn == true){
		var ajaxObj = rePostReq.ajaxObj;
		var id = rePostReq.dialogId;
		ajaxObj.addParam(id + "interactflag", "middle");
		showDefaultLoadingBar();
		ajaxObj.post();
	}
	else{
//		pageUI.PROXYRETURN_EXECUTING --;
		proxyReturnExecutingSub();
	}
	rePostReq.okReturn = null;
};

/**
 * 点CANCEL后执行方法
 * @private
 */
function rePostCancel() {
	var ajaxObj = rePostReq.ajaxObj;
	var id = rePostReq.dialogId;
	ajaxObj.addParam(id + "interactflag", "false");
	showDefaultLoadingBar();
	ajaxObj.post();
};

/**
 * 消息框关闭时不发请求，需要调整 pageUI.PROXYRETURN_EXECUTING
 * @private
 */
function noPostCancel(){
	var ajaxObj = noPostCancel.ajaxObj;
	//从请求队列中删除当前请求id
	if (ajaxObj != null && ajaxObj.req_id != null){
		for (i = 0 ; i < Ajax.REQ_ARRAY.length ; i++){
			if (Ajax.REQ_ARRAY[i] == ajaxObj.req_id){
				Ajax.REQ_ARRAY.splice(i,1);
				break;
			}
		}
		ajaxObj.destroySelf();
	}
	
//	pageUI.PROXYRETURN_EXECUTING --;
	proxyReturnExecutingSub();
} 

/**
 * @private
 */
function rePostReq() {
	var ajaxObj = rePostReq.ajaxObj;
	if (rePostReq.inputDlg) {
		var dlg = rePostReq.inputDlg;
		var itemsMap = dlg.getItems();
		var resultStr = "";
		
		var keySet = itemsMap.keySet();
		for(var i = 0, count = keySet.length; i < count; i++)
		{
			var inputId = keySet[i];
			var inputComp = itemsMap.get(inputId);
			
			if(inputComp.componentType == "LANGUAGECOMBOBOX"){
				var options = inputComp.getOptions();
				var optionsLength = options.length;
				var inputCompValue = '';
				for(var j = 0;j < optionsLength;j++){
					inputCompValue += options[j].langTip + ":" + options[j].value + ";";
				}
				resultStr += inputId + "=" + inputCompValue;
			}else{
				resultStr += inputId + "=" + inputComp.getValue();	
			}
			if(i != count - 1)
				resultStr += ",";
		}
		
		ajaxObj.addParam("interactresult", resultStr);
		var key = rePostReq.dialogId + "interactresult";
 		ajaxObj.addParam(key, resultStr);
		rePostReq.inputDlg = null;
	}
	showDefaultLoadingBar();
	ajaxObj.post();
};

/**
 * 显示异常对话框
 * @public
 */
function showExceptionDialog(friendMsg, errorMsg, stackMsg) {
	return showErrorDialog(friendMsg);
	/*
	// 如果当前框小到没法容纳错误框，则尝试在父框中显示
	if (document.body.clientWidth < 400 || document.body.clientHeight < 280) {
		if (typeof (parent.showExceptionDialog) != "undefined") {
			if (window.debugMode == "production")
				parent.showErrorDialog(friendMsg);
			else
				parent.showExceptionDialog(friendMsg, errorMsg, stackMsg);
		} else
			alert(friendMsg);
	} else {
		if (window.debugMode == "production")
			showErrorDialog(friendMsg);
		else
			ExceptionDialog.showDialog(friendMsg, errorMsg, stackMsg);
	}
	*/
};

/**
 * 隐藏异常对话框
 * @public
 */
function hideExceptionDialog() {
	ExceptionDialog.hideDialog();
};

/**
 * 显示信息对话框
 * 
 * @param func 点击对话框确认按钮要执行的函数
 * @public
 */
 
function showMessageDialog(msg, func, title, okBtnText, isShowSec) {
	var topWin = getLfwTop();
	if (topWin == null) topWin = getTrueParent();
	topWin.require("messagedialog", function(){
		var dialog = topWin.MessageDialogComp.showDialog(msg, title, okBtnText, func, isShowSec);
		/*
		if (func != null)
			dialog.onclick = func;
		*/
	});
};

/**
 * 显示反馈信息对话框
 * @param title
 * @param width
 * @return
 * @public
 */
function showMessage(title, attr){
	var topWin = getLfwTop();
	if (topWin == null) topWin = getTrueParent();
	topWin.require("messagecomp",function(){ topWin.MessageComp.showMessage(title, attr)})
};


/**
 * 隐藏信息对话框
 * @public
 */
function hideMessageDialog() {
	MessageDialogComp.hideDialog();
};

/**
 * 显示警告对话框
 * @public
 */
function showWarningDialog(msg, func) {
	var topWin = getLfwTop();
	if (topWin == null) topWin = getTrueParent();
	var dialog = topWin.WarningDialogComp.showDialog(msg);
	if (func != null)
		dialog.onclick = func;
	return dialog;
};

/**
 * 隐藏警告对话框
 * @public
 */
function hideWarningDialog() {
	WarningDialogComp.hideDialog();
};

/**
 * 显示确认对话框
 * @public
 */
function showConfirmDialog(msg, okFunc, cancelFunc, obj1, obj2, zIndex, okText, cancelText, title) {
	var topWin = getLfwTop();
	if (topWin == null) topWin = getTrueParent();
	topWin.require("confirmdialog", function(){topWin.ConfirmDialogComp.showDialog(msg, okFunc, cancelFunc, obj1, obj2, null, null, okText, cancelText, title)});
};

/**
 * 显示三个按钮的确认对话框
 * @public
 */
function showThreeButtonConfirmDialog(msg,rePostOk, rePostCancel, rePostMiddle, btnTexts, obj1, obj2, obj3, zIndex, another, title){
	var topWin = getLfwTop();
	if(topWin == null) topWin = getTrueParent();
	topWin.require("threebuttondialog", function(){topWin.ThreeButtonsDialog.showDialog(msg, rePostOk, rePostCancel, rePostMiddle, btnTexts, obj1, obj2, obj3, zIndex, another, title)});

};

/**
 * 隐藏确认对话框
 * @public
 */
function hideConfirmDialog() {
	ConfirmDialogComp.hideDialog();
};

/**
 * 获得节点值
 * @private
 */
function getNodeValue(node) {
	if (IS_IE)
		return node.text;
	var firstNode = node.firstChild;
	if(firstNode == null)
		return null;
	var nextSibling = firstNode.nextSibling;
	if(nextSibling == null)
		return firstNode.data;
	return nextSibling.data;
	//return node.textContent;
};

/**
 * 获取节点属性
 * @param node
 * @param attrName
 * @return
 * @private
 */
function getNodeAttribute(node, attrName) {
	if (IS_IE)
		return node.getAttribute(attrName);
	var attrs = node.attributes;
	if (attrs == null)
		return null;
	for ( var i = 0; i < attrs.length; i++) {
		if (attrs[i].nodeName == attrName)
			return attrs[i].nodeValue;
	}
	return null;
};

/**
 * 根据类型获取子项
 * @param node
 * @param type
 * @param index
 * @return
 * @private
 */
function getChildForType(node, type, index) {
	var nodes = node.childNodes;
	if (nodes == null)
		return null;
	var count = -1;
	for ( var i = 0; i < nodes.length; i++) {
		if (nodes[i].nodeName != null && nodes[i].nodeName.toLowerCase() == type) {
			count++;
			if (count == index)
				return nodes[i];
		}
	}
	return null;
};

/**
 * 从数组中删除子项
 * @param arr
 * @param ele
 * @return
 * @private
 */
function removeFromArray(arr, ele) {
	if (!arr)
		return false;
	for ( var i = 0; i < arr.length; i++) {
		if (arr[i] == ele) {
			arr.splice(i, 1);
			return true;
		}
	}
	return false;
};

///**
// * 提交表单
// * @param formName
// * @param actionUrl
// * @param target
// * @param method
// * @return
// */
//function submitForm(formName, actionUrl, target, method) {
//	var form = document[formName];
//	form.action = actionUrl;
//	form.target = getString(target, '_self');
//	form.method = getString(method, 'post');
//	// if(form.o )
//	// if(!form.onsubmit())
//	// return;
//	form.submit();
//};

/**
 * 显示状态信息
 * @param msg
 * @return
 * @private
 */
function showStatusMsg(msg) {
	window.status = msg;
};

/**
 * 根据key获取用户配置属性
 * @private
 */
function getConfigAttribute(key) {
	var value = getConfigFromCookieById(key);
	if (value != null)
		return value;
	else
		return getSessionAttribute(key);
};

/**
 * 从Cookie中获取配置信息
 * @param key
 * @return
 * @private
 */
function getConfigFromCookieById(key) {
	var allCookie = document.cookie;
	var pos = allCookie.indexOf("LFW_CONFIG_KEY=");
	if (pos != -1) {
		var start = pos + 15;
		var end = allCookie.indexOf(";", start);
		if (end == -1)
			end = allCookie.length;
		var value = allCookie.substring(start, end);
		var v = value.split("$");
		if (key == "connectServerCycle")
			return v[0];
		else if (key == "theme")
			return v[1];
		else if (key == "openNodeMode")
			return v[2];
		else if (key == "noticeRefreshCycle")
			return v[3];
		else if (key == "jobRefreshCycle")
			return v[4];
	} else
		return null;
};

/**
 * document对象有个cookie特性，是包含给定页面所有可访问cookie的字符串。Cookie特性也很特别，因为将这个cookie特性
 * 设置为新值只会警告对页面可访问的cookie，并不会真正改变cookie(特性)本身。即使制定了cookie的其他特性，如失效时间，
 * document.cookie也只是返回每个cookie的名称和值，并用分号来分隔这些cookie
 */

/**
 * 设置Cookie
 * @param sName cookie名称,cookie名称本来不区分大小写，但最好认为区分
 * @param sValue 保存在cookie中的值，这个值在存储之前必须用encodeURIComponent编码,以免丢失数据或占用了Cookie.
 *               名称和值加起来不能超过4095字节,4K
 * @param sDomain 域，出于安全考虑，网站不能访问由其他域创建的cookie。创建cookie后，域的信息会作为cookie的一部分
 *                存储起来，不过，虽然不常见，但还是可以覆盖这个设置以允许另一个网站访问这个cookie
 * @param sPath 路径，另一个cookie的安全特征，路径限制了对Web服务器上的特定目录的访问。
 * @param{Object} oExpires Date对象
 * @param bSecure 一个true/false值，用于表示cookie是否只能从安全网站(使用SSL和https协议的网站)中访问。可将这个值设为
 *                true以提供加强的保护，进而确保cookie不被其他网站访问
 * @private                
 */
function setCookie(sName, sValue, oExpires, sPath, sDomain, bSecure) {
	var sCookie = sName + "=" + encodeURIComponent(sValue);
	if (oExpires)
		sCookie += "; expires=" + oExpires.toGMTString();
	if (sPath)
		sCookie += "; path=" + sPath;
	if (sDomain)
		sCookie += "; domain=" + sDomain;
	if (bSecure)
		sCookie += "; secure=" + bSecure;
	document.cookie = sCookie;
};

/**
 * 获取Cookie
 * @param sName
 * @return
 * @private
 */
function getCookie(sName) {
	var sRE = "(?:; )?" + sName + "=([^;]*);?";
	var oRE = new RegExp(sRE);

	if (oRE.test(document.cookie)) {
		return decodeURIComponent(RegExp["$1"]);
	} else
		return null;
};

/**
 * 删除Cookie
 * @param sName
 * @param sPath
 * @param sDomain
 * @return
 * @private
 */
function deleteCookie(sName, sPath, sDomain) {
	setCookie(sName, "", new Date(0), sPath, sDomain);
}

/**
 * 上传成功后执行方法
 * @param data
 * @param targetComp
 * @return
 * @private
 */
function uploadSuccess(data, targetComp) {
	// alert("上传成功");
	var comp = getComponent(targetComp);
	comp.onUploaded(data);
};


/**
 * 从缓存中获取内容
 * @param key
 * @return
 * @private
 */
function getFromCache(key) {
	return window.globalObject[key];
};

/**
 * 向缓存中存入内容
 * @param key
 * @param value
 * @return
 * @private
 */
function putToCache(key, value) {
	window.globalObject[key] = value;
};


/**
 * 从页面删除一个控件并销毁所占资源
 * @private
 */
function removeComponent(compId) {
	var comp = window["$c_" + compId];
	if (comp) {
		comp.destroySelf();
		window["$c_" + compId] = null;
	}
};

/**
 * 从删除页面所有控件并销毁所占资源
 * @private
 */
function removeAllComponent() {
	for ( var i = 0; i < window.clickHolders.length; i++) {
		window.clickHolders[i] = null;
	}

	if(window.pageUI){
		pageUI.destroySelf();
	}

	for (var i in window.objects) {
		var comp = window.objects[i];
		if (comp && comp.destroySelf) {
			comp.destroySelf();
		}
		comp = null;
	}
	window.objects = null;
};

/**
 * 
 * @param {} node
 * @private
 */
function clearNodeProperties(node){
	for(var i in node){
		try{
			node[i] = null;
		}
		catch(error){
		}
	}
}

/**
 * 
 * @param {} node
 * @private
 */
function clearHtmlNodeProperties(node){
	if(node != null){
		var nodeName = node.nodeName;
		if(nodeName == "IMG" || nodeName == "img"){
			return;
		}
		try{
			//node.innerHTML = "";
			node.onclick = null;
			node.onmouseover = null;
			node.keypress = null;
			node.onfocus = null;
			node.onblur = null;
			node.owner = null;
		}
		catch(error){
		}
	}
}

/**
 * 删除事件对象（用于清除依赖关系）
 * @param event
 * @return
 * @private
 */
function clearEvent(event) {
	try {
	    for (var i in event) {
	    	if (i == "type" || i == "eventPhase" || i == "bubbles"
//		    	|| i == "originalTarget"
//	    		|| i == "target"
//				|| i == "currentTarget" 
//				|| i == "relatedTarget" 
//				|| i == "fromElement" 
//				|| i == "srcElement" 
//				|| i == "toElement" 
				|| i == "cancelable" || i == "timeStamp" || i == "which"
				|| i == "rangeParent" || i == "rangeOffset" || i == "pageX"
				|| i == "pageY" || i == "isChar" || i == "getPreventDefault"
				|| i == "screenX" || i == "screenY" || i == "clientX"
				|| i == "clientY" || i == "ctrlKey" || i == "shiftKey"
				|| i == "altKey" || i == "metaKey" || i == "button"
				|| i == "initMouseEvent"
				|| i == "stopPropagation" || i == "preventDefault"
				|| i == "initEvent" || i == "view" || i == "detail"
				|| i == "initUIEvent" || i == "CAPTURING_PHASE" || i == "AT_TARGET"
				|| i == "BUBBLING_PHASE" || i == "mozPressure"
				|| i == "initNSMouseEvent" || i == "explicitOriginalTarget"
				|| i == "preventBubble" || i == "preventCapture"
				|| i == "isTrusted" || i == "layerX" || i == "layerY"
				|| i == "cancelBubble" || i == "MOUSEDOWN" || i == "MOUSEUP"
				|| i == "MOUSEOVER" || i == "MOUSEOUT" || i == "MOUSEMOVE"
				|| i == "MOUSEDRAG" || i == "CLICK" || i == "DBLCLICK"
				|| i == "KEYDOWN" || i == "KEYUP" || i == "KEYPRESS"
				|| i == "DRAGDROP" || i == "FOCUS" || i == "BLUR" || i == "SELECT"
				|| i == "CHANGE" || i == "RESET" || i == "SUBMIT" || i == "SCROLL"
				|| i == "LOAD" || i == "UNLOAD" || i == "XFER_DONE" || i == "ABORT"
				|| i == "ERROR" || i == "LOCATE" || i == "MOVE" || i == "RESIZE"
				|| i == "FORWARD" || i == "HELP" || i == "BACK" || i == "TEXT"
				|| i == "ALT_MASK" || i == "CONTROL_MASK" || i == "SHIFT_MASK"
				|| i == "META_MASK" || i == "SCROLL_PAGE_UP"
				|| i == "SCROLL_PAGE_DOWN") {
	    		continue;
			}
			try {
				event[i] = null;
			}
			catch (error) {
				
			}
	    }
	}
	catch (error) {
		
	}
	event = null;
};

/**
 * 删除无自定义属性的事件对象（用于清除依赖关系）
 * @param event
 * @return
 * @private
 */
function clearEventSimply(event) {
	if(event){
		if (IS_IE) {
			if (event.originalTarget)
				event.originalTarget = null;
			if (event.target)
				event.target = null;
			if (event.currentTarget)
				event.currentTarget = null;
			if (event.relatedTarget)
				event.relatedTarget = null;
			try {
				event.fromElement = null;
			} catch (error) {
				
			}
			try {
				event.toElement = null;
			} catch (error) {
				
			}
			try {
				event.srcElement = null;
			} catch (error) {
				
			}
		}
		event = null;
	}
};

/**
 * 注册样式表
 * @param {} cssString
 * @private
 */
function addCssByStyle(cssString){
    var doc=document;  
    var style=doc.createElement("style");
    style.setAttribute("type", "text/css");
    if(style.styleSheet){// IE
        style.styleSheet.cssText = cssString;
    } else {// w3c
        var cssText = doc.createTextNode(cssString);  
         style.appendChild(cssText);  
     }  
     var heads = doc.getElementsByTagName("head");  
     if(heads.length)
         heads[0].appendChild(style);  
     else 
         doc.documentElement.appendChild(style);  
 } 
/**
 * 调整容器Frame高度
 * @public
 */
function adjustContainerFramesHeight(syncFlag){
	if (!IS_IE7 && !IS_IE8)
		syncFlag = true; 
	try {
		if(!document._pt_frame_id){
			document._pt_frame_id = getRequest()["$portletWind"] + "_iframe";
		}
		if(document._pt_frame_id){
			var frame =	parent.getParentsContainer(document._pt_frame_id);
			parent.adjustIFramesHeightOnLoad(frame, syncFlag);
		}else if(parent.document._pt_frame_id){
			var frame =	parent.parent.getParentsContainer(parent.document._pt_frame_id);
			parent.parent.adjustIFramesHeightOnLoad(frame, syncFlag);
		}else if(window.opener){
			if(window.opener.document._pt_frame_id){
				var frame =	window.opener.parent.getParentsContainer(window.opener.document._pt_frame_id);
				window.opener.parent.adjustIFramesHeightOnLoad(frame, syncFlag);
			}
		}
	} catch (e) {
	}
}

/**
 * @private
 */
function restoreContainerFramesHeight() {
	try {
		if(document._pt_frame_id){
			parent.initFrameMiniHeight(document._pt_frame_id);
		}else if(parent.document._pt_frame_id){
			parent.parent.initFrameMiniHeight(parent.document._pt_frame_id);
		}else if(window.opener){
			if(window.opener.document._pt_frame_id){
				window.opener.parent.initFrameMiniHeight(window.opener.document._pt_frame_id);
			}
		}
	} catch (e) {
	}
}

/**
 * 
 * @param {} result
 * @private
 */
function uploadedExcelFile(result){
	var proxy = new ServerProxy(null,null,false);
	var results = result.split(",");
	proxy.addParam('clc', results[0]);
	var method = results[3];
	if(method == null || method == "")
		method = 'onUploadedExcelFile';
	proxy.addParam('m_n', method);
	proxy.addParam('widget_id', results[2]);
	proxy.addParam('el', '2');
	proxy.addParam("excel_imp_path", results[1]);
	proxy.execute();
}

/**
 * 下载文件
 * @param {} url 文件url
 * @public
 */
function sysDownloadFile(url){
	if (window.sys_DownFileFrame == null) {
		var frm = $ce('iframe');
		frm.frameborder = 0;
		frm.vspace = 0;
		frm.hspace = 0;
		frm.style.width = '1px';
		frm.style.heigh = '0px';
		frm.style.display = 'none';
		window.sys_DownFileFrame = frm;
		document.body.appendChild(window.sys_DownFileFrame);
	}
	window.sys_DownFileFrame.src = url;
}

/**
 * 设置透明度
 * @private
 */
function _setOpacity(obj, value) {
	if (document.all) {
		if (value == 100) {
			obj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity=" + value + ")";
		} else {
			obj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity=" + value + ")";
		}
	} else {
		obj.style.opacity = value / 100;
	}
};

var ftTimeOutFunc;

/**
 * 用setTimeout循环改变透明度
 * @private
 */
function _changeOpacity(startValue, endValue, step, speed, divObj, type) {
	if (divObj.ftTimeOutFunc){
		clearTimeout(divObj.ftTimeOutFunc);
	}
	if(step > 0){
		//逐渐显示
		if(startValue > endValue){
			return;
		}
	}else if(step < 0){
		//逐渐隐藏
		if(startValue < endValue){
			if(typeof(type) == 'string'){
				if(type == 'display'){
					divObj.style.display = "none";
				}else if(type == 'visibility'){
					divObj.style.visibility = "hidden";
				}			
			}
			return;
		}
	}else if(step == 0){
		if(startValue >= endValue){
			if(typeof(type) == 'string'){
				if(type == 'display'){
					divObj.style.display = "block";
				}else if(type == 'visibility'){
					divObj.style.visibility = "visible";
				}			
			}
		}else{
			if(typeof(type) == 'string'){
				if(type == 'display'){
					divObj.style.display = "none";
				}else if(type == 'visibility'){
					divObj.style.visibility = "hidden";
				}			
			}
		}
		return;
	}
	_setOpacity(divObj, startValue);
	divObj.ftTimeOutFunc = setTimeout( function() {
		_changeOpacity(startValue + step, endValue, step, speed, divObj, type);
	}, speed);
};

/**
 * 显示提示信息
 * @private
 */
function fadeInDiv(divObj, step, speed, type) {
	if(typeof(divObj) == "object"){
		if(typeof(type) == 'string'){
			if(type == 'display'){
				divObj.style.display = "block";
			}else if(type == 'visibility'){
				divObj.style.visibility = "visible";
			}			
		}
		if(IS_IE && !IS_STANDARD){
			return;
		}
		if(document.all){
			divObj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity=0)";			
		}else{
			divObj.style.opacity = 0;
		}
		if(typeof(step) != "number"){
			step = 20;
		}
		if(typeof(speed) != "number"){
			speed = 100;
		}
		_changeOpacity(0, 100, step, speed, divObj, type);
	}
};

/**
 * 隐藏提示信息
 * @private
 */
function fadeOutDiv(divObj, step, speed, type) {
	if(typeof(divObj) == "object"){
		if(IS_IE && !IS_STANDARD){
			if(typeof(type) == 'string'){
				if(type == 'display'){
					divObj.style.display = "none";
				}else if(type == 'visibility'){
					divObj.style.visibility = "hidden";
				}			
			}
			return;
		}
		if(document.all){
			divObj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity=100)";			
		}else{
			divObj.style.opacity = 1;
		}
		if(typeof(step) != "number"){
			step = -20;
		}
		if(typeof(speed) != "number"){
			speed = 100;
		}
		_changeOpacity(100, 0, step, speed, divObj, type);
	}
};
window.__flash__removeCallback=function(e,f){try{if(e){e[f]=null}}catch(g){}};

// 页面操作脚本 --------------------------------------------------------
/**
 * 删除布局操作
 * @param {} id
 * @param {} type
 * @param {} params
 * @return {Boolean}
 */
window.execDynamicScript2RemoveLayout = function(id, type, params) {
	if (!id)
		return false;
	var obj = ("string" == typeof id) ? $ge(id) : id;
	if (!obj)
		return false;
	var parent;
	if (obj.objType) {
		parent = $ge(obj.id + "_raw");
		if (parent) {
			obj = parent;
			parent = obj.parentNode;
		}
	} 
	
	if (!parent) {
		parent = obj.parentNode;
	}
	if (parent) {
		parent.removeChild(obj);
		return true
	}
	return false;
};
/**
 * 删除容器操作
 * @param {} id
 * @param {} type
 * @param {} params
 * @return {Boolean}
 */
window.execDynamicScript2RemovePanel = function(id, type, params) {
	if (!id)
		return false;
	var obj = ("string" == typeof id) ? $ge(id) : id;
	if (!obj)
		return false;
	var parent;
	if (obj.objType) {
		parent = $ge(obj.id + "_raw");
		if (parent) {
			obj = parent;
			parent = obj.parentNode;
		}
	} else {
		parent = obj.parentNode;
	}
	if (parent) {
		parent.removeChild(obj);
		window.layoutInitFunc();
		return true
	}
	return false;
};
/**
 * 删除控件操作
 * @param {} id
 * @param {} widgetId
 * @param {} compId
 * @param {} params
 * @return {Boolean}
 */
window.execDynamicScript2RemoveComponent = function(id, widgetId, compId, params) {
	if (!id || !widgetId || !compId)
		return false;
	var obj = ("string" == typeof id) ? $ge(id) : id;
	if (!obj)
		return false;
	var parent = obj.parentNode;
	if (parent) {
		pageUI.getWidget(widgetId).removeComponent(compId);
		parent.removeChild(obj);
		return true
	}
	return false;
};

/**
 * 删除formElement
 * 
 * @param {} id
 * @param {} widgetId
 * @param {} formId
 * @param {} feId
 * @return {Boolean}
 */
window.execDynamicScript2RemoveFormElement2 = function(id, widgetId, formId, feId) {
	if (!id || !widgetId || !formId)
		return false;
	var obj = ("string" == typeof id) ? $ge(id) : id;
	if (!obj)
		return false;
	var parent = obj.parentNode;
	if (parent) {
		pageUI.getWidget(widgetId).getComponent(formId).removeElementById(feId);
		parent.removeChild(obj);
		return true
	}
	return false;
};
/**
 * 删除gridclumn操作
 * @param {} widgetId
 * @param {} gridId
 * @param {} keyName
 * @param {} params
 */
window.execDynamicScript2RemoveGridColumn = function(widgetId, gridId, keyName, params) {
	var comp = pageUI.getWidget(widgetId).getComponent(gridId);
	var header = comp.removeHeader(keyName);
	comp.paintData();
};
/**
 * 删除formelement操作
 * @param {} widgetId
 * @param {} formId
 * @param {} keyName
 * @param {} params
 */
window.execDynamicScript2RemoveFormElement = function(widgetId, formId,
		keyName, params) {
	try {
		var comp = pageUI.getWidget(widgetId).getComponent(formId);
		comp.removeElementById(keyName);
		comp.pLayout.paint(true);
	} catch (e) {
		alert(e);
	}

};

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
