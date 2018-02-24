// ADD by licza ---start--
function addLoadListener(fn){
   if (typeof window.addEventListener != 'undefined') {
        window.addEventListener('load',fn,false);
   }
    else if (typeof document.addEventListener != 'undefined'){
       document.addEventListener('load',fn,false);
   } else if (typeof window.attachEvent != 'undefined'){
       window.attachEvent('onload',fn);
   }else {
        var oldfn = window.onload;
        if (typeof window.onload != 'function'){
             window.onload = fn;
        } else{
             window.onload = function() {
             lodfn();
            fn();
        };
    }
  }
};
function resizePortlet(){
	try{
		var param = getRequest();
		var portletWind = param["$portletWind"];
		var h3r =  param["_h3ra"];
		var bodyHeight = document.body.scrollHeight;	
		
		var uri = "http://" + h3r + "/sync/websm/pserver/html/nodes/crosssite.jsp?eval=document.getElementById('"+portletWind + "_iframe').style.height= '" + bodyHeight + "px'";

		var crossFrame = document.createElement("iframe");
	 
		document.body.appendChild(crossFrame);
		crossFrame.frameBorder = "none";
		crossFrame.style.height = "0px";
		crossFrame.style.width = "0px";
		crossFrame.src = uri;
	}catch(e){
		if(window.console){
			window.console.error(e);
		}
	}
	
};
function getRequest() {
	   var url = location.search; 
	   var theRequest = new Object();
	   if (url.indexOf("?") != -1) {
		  var str = url.substr(1);
		  strs = str.split("&");
		  for(var i = 0; i < strs.length; i ++) {
			 theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
		  }
	   }
	   return theRequest;
};
	
addLoadListener(resizePortlet);
/**
 * 执行父页面脚本
 */
function pEval(scripts){
	var uri = "http://" + h3r + "/html/nodes/crosssite.jsp?eval=" + scripts;
	var crossFrame = document.createElement("iframe");
	document.body.appendChild(crossFrame);
	crossFrame.frameBorder = "none";
	crossFrame.style.height = "0px";
	crossFrame.style.width = "0px";
	crossFrame.src = uri;
}
// ADD by licza ---end---