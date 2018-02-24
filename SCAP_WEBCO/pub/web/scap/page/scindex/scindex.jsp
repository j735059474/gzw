<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"> 
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>首页</title>
</head>

<style type="text/css">
</style>

<body>
<%@ include file="scindex_header.html"%>
<div class="left">
	<div class="pic">
		<img id="pic" src="/portal/scap/image/scindex/left-defaultpic.jpg" />
		<span id="picpages">
		</span>
	</div>
	<div class="split">&nbsp;</div>
	<div class="newsheader">
		<div class="newsheader-horn"></div> 
		<span class="text">新闻公告</span>
		<span class="more" style="display:none;">更多>></span>
	</div>
</div>
<div class="center">
	<div id="mywork" class="block">
		<div class="header">
			<span class="text">我的待办</span>
			<span class="more" onClick="javascript:window.parent.parent.location.href = '/portal/pt/home/view?pageModule=pint&pageName=task';">更多>></span>
		</div>
		<div class="body">
		</div>
	</div>
	<div id="gznews" class="block style2">
		<div class="header">
			<span class="text">国资动态</span>
		</div>
		<div class="body">
		</div>
	</div>
	<div id="mymsg" class="block style2">
		<div class="header">
			<span class="text">我的消息</span>
			<span class="num"></span>
		</div>
		<div class="body">
		</div>
	</div>
</div>
<div class="right">
	<div class="item">
		<div class="icon"></div>
		<span class="text">政策法规库</span>
	</div>
	<div class="item">
		<div class="icon"></div>
		<span class="text">纪检案件</span>
	</div>
	<div class="item">
		<div class="icon"></div>
		<span class="text">工程建设失信行为</span>
	</div>
	<div class="item">
		<div class="icon"></div>
		<span class="text">企业文化专栏</span>
	</div>
	<div class="header">友情链接</div>
	<div class="link">
		<span>国务院国资委</span>
		<a href="http://www.baidu.com" target="_blank">http://www.gwygzw.com/</a>
	</div>
	<div class="link">
		<span>江西省国资委</span>
		<a href="http://www.baidu.com" target="_blank">http://www.gwygzw.com/</a>
	</div>
	<div class="link">
		<span>浙江省国资委</span>
		<a href="http://www.baidu.com" target="_blank">http://www.gwygzw.com/</a>
	</div>
	<div class="link">
		<span>安徽省国资委</span>
		<a href="http://www.baidu.com" target="_blank">http://www.gwygzw.com/</a>
	</div>
	<div class="link">
		<span>福建省国资委</span>
		<a href="http://www.baidu.com" target="_blank">http://www.gwygzw.com/</a>
	</div>
	<div class="link">
		<span>北京市国资委</span>
		<a href="http://www.baidu.com" target="_blank">http://www.gwygzw.com/</a>
	</div>
	<div class="link">
		<span>成都市国资委</span>
		<a href="http://www.baidu.com" target="_blank">http://www.gwygzw.com/</a>
	</div>
	<div class="link">
		<span>江西省国资委</span>
		<a href="http://www.baidu.com" target="_blank">http://www.gwygzw.com/</a>
	</div>
	<div class="link">
		<span>浙江省国资委</span>
		<a href="http://www.baidu.com" target="_blank">http://www.gwygzw.com/</a>
	</div>
	<div class="link">
		<span>安徽省国资委</span>
		<a href="http://www.baidu.com" target="_blank">http://www.gwygzw.com/</a>
	</div>
	<div class="link">
		<span>福建省国资委</span>
		<a href="http://www.baidu.com" target="_blank">http://www.gwygzw.com/</a>
	</div>
	<div class="link">
		<span>北京市国资委</span>
		<a href="http://www.baidu.com" target="_blank">http://www.gwygzw.com/</a>
	</div>
	<div class="link">
		<span>成都市国资委</span>
		<a href="http://www.baidu.com" target="_blank">http://www.gwygzw.com/</a>
	</div>
</div>
<div class="itemlayout">
	<div class="header">
		<span class="caption"></span>
		<span class="close"></span>
	</div>
	<div class="itempanel">
		<div id="zcfgk" class="block"></div>
		<div id="jjajxx" class="block"></div>
		<div id="gcjssxxwxx" class="block"></div>
		<div id="qywhzl" class="block"></div>
	</div>
</div>
<img src="/portal/scap/image/scindex/popup-layout.png" class="popup-layout" />
<div class="popup">
	<div class="header">
		<span class="caption"></span>
		<span class="date"></span>
		<span class="close"></span>
	</div>
	<div class="body"></div>
</div>
<div class="copyright">
	<span class="text">国资国企信息化综合服务平台  版权所有 - 京20150514X号 <br/>用友网络科技股份有限公司  管理维护  admin.master@yonyou.com</span>
</div>
</body>

<script type="text/javascript">
$(document).ready(function() {
	initData_Pics();
	initData_notice();
	initData_myWork();
	initData_gzNews();
	initData_myMsgNum();
	initData_myMsg();
	initData_zcfgk();
	initData_jjajxx();
	initData_gcjssxxwxx();
	initData_qywhzl();
	
	initEleEffect();
	initEleEvent();
	initPopup();
});

function initEleEffect() {
	$(".right .link").mouseenter(function() {
		$(this).addClass("mouseenter");
	}).mouseleave(function() {
		$(this).removeClass("mouseenter");
	});
	
	$(".more").mouseenter(function() {
		$(this).addClass("moreme");
	}).mouseleave(function() {
		$(this).removeClass("moreme");
	});
	
	$(".itemlayout .header .close").hover(function() {
		$(this).addClass("closeme");
	},function() {
		$(this).removeClass("closeme");
	}).click(function() {
		$(".right .curItem").click();
	});
	
	if(navigator.userAgent.indexOf("MSIE") > 0) {
		$(".itemlayout").css("right", 288);
	}
}

function initEleEvent() {
	$(".right .item").click(function() {
		if($(this).hasClass("curItem")) {
			$(".curItem").removeClass("curItem");
			$(".itemlayout").hide();
			$(".itemlayout .block").hide();
		}else {
			$(".curItem").removeClass("curItem");
			$(".itemlayout").hide();
			$(this).addClass("curItem");
			$(".itemlayout .header .caption").html($(this).find(".text").html());
			var index = $(".right .item").index(this);
			$(".itemlayout .block").eq(index).show().siblings(".block").hide();
			$(".itemlayout").css("top", 11 + (index * 52));
			$(".itemlayout").show();
		}
	});
	
	$(".itemlayout").mouseleave(function() {
		//$(".right .curItem").removeClass("curItem");
		//$(this).hide();
	});
}

function initData_Pics() {
	$.post("/portal/pt/scIndexAction/getPics", function(data) {
		if(data.length > 0) {
			for(var i = data.length - 1; i >= 0; i--) {
				$("#picpages").append("<span class=\"picpage opaque\" url=\"" + data[i] + "\">" + (i + 1) + "</span>");
			}
			$(".left .picpage").mouseenter(function() {
				$(this).addClass("mouseenter");
			}).mouseleave(function() {
				$(this).removeClass("mouseenter");
			}).click(function() {
				$(this).addClass("selected").siblings(".selected").removeClass("selected");
				$("#pic").attr("src", $(this).attr("url"));
			});
			$(".picpage").last().click();
		}
	}, "json");
}

function initData_notice() {
	$.post("/portal/pt/scIndexAction/getNotices", function(data) {
		if(data != -1) {
			for(var i = 0; i < data.length; i++) {
				$newRow = $("<div class=\"new\" pkid=\"" + data[i][0] + "\" title=\"" + data[i][1] + "\"></div>");
				$newRow.append("<div class=\"caption logo" + (i + 1) + "\"><span>" + data[i][1] + "</span></div>");
				$newRow.append("<div class=\"date\">" + data[i][2] + "</div>");
				$(".left").append($newRow);
			}
			
			$(".left .new").mouseenter(function() {
				$(this).addClass("newme");
			}).mouseleave(function() {
				$(this).removeClass("newme");
			}).click(function() {
				loadNewsContent($(this).attr("pkid"), $(this).attr("title"), $(this).find(".date").html());
			});
		}else {
			$(".left").append("<div class=\"nodata\" style=\"background-color:#e6f3fb;\">暂无新闻公告</div>");
		} 
	}, "json");
}

function initData_myWork() {
	$.post("/portal/pt/scIndexAction/getMyWorks", function(data) {
		$("#mywork .row").remove();
		if(data != -1) {
			for(var i = 0; i < data.length; i++) {
				$newRow = $("<div class=\"row worklogo" + (i + 1) + "\" " + 
					"onClick=\"javascript:getTaskUrl(\'/portal/pt/task/process\', \'id=" + data[i][0] + "&pluginid=" + data[i][3] + "\', \'" + data[i][4] + "\');\">" + 
					"</div>");
				$newRow.append("<div class=\"caption\">" + data[i][1] + "</div>");
				//$newRow.append("<div class=\"new\"></div>");
				$newRow.append("<div class=\"date\">" + data[i][2] + "</div>");
				$("#mywork .body").append($newRow);
			}
			
			$("#mywork .row").mouseenter(function() {
				$(this).addClass("mouseenter");
			}).mouseleave(function() {
				$(this).removeClass("mouseenter");
			});
						
			var width = $("#mywork .row").first().width();
			$("#mywork .row").find(".caption").width(width - 130);
		}else {
			$("#mywork .body").append("<div class=\"nodata\">暂无待办</div>");
		} 
	}, "json");
}
		
function initData_gzNews() {
	$.post("/portal/pt/scIndexAction/getGzNews", function(data) {
		if(data != -1) {
			for(var i = 0; i < data.length; i++) {
				$newRow = $("<div class=\"row\" pkid=\"" + data[i][0] + "\" title=\"" + data[i][1] + "\"></div>");
				$newRow.append("<div class=\"caption\">" + data[i][1] + "</div>");
				$newRow.append("<div class=\"date\">" + data[i][2] + "</div>");
				$("#gznews .body").append($newRow);
			}
			
			$("#gznews .row").mouseenter(function() {
				$(this).addClass("mouseenter");
			}).mouseleave(function() {
				$(this).removeClass("mouseenter");
			}).click(function() {
				loadNewsContent($(this).attr("pkid"), $(this).attr("title"), $(this).find(".date").html());
			});
						
			var width = $("#gznews .row").first().width();
			$("#gznews .row").find(".caption").width(width - 120);
		}else {
			$("#gznews .body").append("<div class=\"nodata\">暂无国资动态</div>");
		} 
	}, "json");
}
//获取政策法规库列表
function initData_zcfgk() {
	$.post("/portal/pt/scIndexAction/getZcfgk", function(data) {
		if(data != -1) {
			for(var i = 0; i < data.length; i++) {
				$newRow = $("<div class=\"row\" pkid=\"" + data[i][0] + "\" title=\"" + data[i][1] + "\"></div>");
				$newRow.append("<div class=\"caption\">" + data[i][1] + "</div>");
				$newRow.append("<div class=\"date\">" + data[i][2] + "</div>");
				$("#zcfgk").append($newRow);
			}
			
			$("#zcfgk .row").mouseenter(function() {
				$(this).addClass("mouseenter");
			}).mouseleave(function() {
				$(this).removeClass("mouseenter");
			}).click(function() {
				showDialog("/portal/app/prd_policy_regulation/policyregComps.policy_regulation_cardwin?" + 
					"nodecode=E9B40302&nc.scap.prd.policyRegulation.model.PolicyRegulationPageModel&" + 
					"operate=detail&openBillId=" + $(this).attr("pkid"), 
					"编辑", '1200', '100%', "nc.scap.pub.officialdoc.officialdoc_cardwin", '', {isConfirmClose:true,isShowLine:false});
				//loadNewsContent($(this).attr("pkid"), $(this).attr("title"), $(this).find(".date").html());
			});
						
			var width = $("#zcfgk .row").first().width();
			$("#zcfgk .row").find(".caption").width(width - 120);
		}else {
			$("#zcfgk").append("<div class=\"nodata\">暂无政策法规信息</div>");
		} 
	}, "json");
}
		
//获取纪检案件信息列表
function initData_jjajxx() {
	$.post("/portal/pt/scIndexAction/getJjajxx", function(data) {
		if(data != -1) {
			for(var i = 0; i < data.length; i++) {
				$newRow = $("<div class=\"row\" pkid=\"" + data[i][0] + "\" title=\"" + data[i][1] + "\"></div>");
				$newRow.append("<div class=\"caption\">" + data[i][1] + "</div>");
				$newRow.append("<div class=\"date\">" + data[i][2] + "</div>");
				$("#jjajxx").append($newRow);
			}
			
			$("#jjajxx .row").mouseenter(function() {
				$(this).addClass("mouseenter");
			}).mouseleave(function() {
				$(this).removeClass("mouseenter");
			}).click(function() {
				loadNewsContent($(this).attr("pkid"), $(this).attr("title"), $(this).find(".date").html());
			});
						
			var width = $("#jjajxx .row").first().width();
			$("#jjajxx .row").find(".caption").width(width - 120);
		}else {
			$("#jjajxx").append("<div class=\"nodata\">暂无纪检案件信息</div>");
		} 
	}, "json");
}
		
//获取工程建设失信行为信息列表
function initData_gcjssxxwxx() {
	$.post("/portal/pt/scIndexAction/getGcjssxxwxx", function(data) {
		if(data != -1) {
			for(var i = 0; i < data.length; i++) {
				$newRow = $("<div class=\"row\" pkid=\"" + data[i][0] + "\" title=\"" + data[i][1] + "\"></div>");
				$newRow.append("<div class=\"caption\">" + data[i][1] + "</div>");
				$newRow.append("<div class=\"date\">" + data[i][2] + "</div>");
				$("#gcjssxxwxx").append($newRow);
			}
			
			$("#gcjssxxwxx .row").mouseenter(function() {
				$(this).addClass("mouseenter");
			}).mouseleave(function() {
				$(this).removeClass("mouseenter");
			}).click(function() {
				loadNewsContent($(this).attr("pkid"), $(this).attr("title"), $(this).find(".date").html());
			});
						
			var width = $("#gcjssxxwxx .row").first().width();
			$("#gcjssxxwxx .row").find(".caption").width(width - 120);
		}else {
			$("#gcjssxxwxx").append("<div class=\"nodata\">暂无工程建设失信行为信息</div>");
		} 
	}, "json");
}
		
//获取企业文化专栏信息列表
function initData_qywhzl() {
	$.post("/portal/pt/scIndexAction/getQywhzl", function(data) {
		if(data != -1) {
			for(var i = 0; i < data.length; i++) {
				$newRow = $("<div class=\"row\" pkid=\"" + data[i][0] + "\" title=\"" + data[i][1] + "\"></div>");
				$newRow.append("<div class=\"caption\">" + data[i][1] + "</div>");
				$newRow.append("<div class=\"date\">" + data[i][2] + "</div>");
				$("#qywhzl").append($newRow);
			}
			
			$("#qywhzl .row").mouseenter(function() {
				$(this).addClass("mouseenter");
			}).mouseleave(function() {
				$(this).removeClass("mouseenter");
			}).click(function() {
				loadNewsContent($(this).attr("pkid"), $(this).attr("title"), $(this).find(".date").html());
			});
						
			var width = $("#qywhzl .row").first().width();
			$("#qywhzl .row").find(".caption").width(width - 120);
		}else {
			$("#qywhzl").append("<div class=\"nodata\">暂无企业文化专栏信息</div>");
		} 
	}, "json");
}

function loadNewsContent(id, title, date) {
	$(".popup .caption").html(title);
	$(".popup .date").html(date);
	$(".popup .body").html("");
	$.post("/portal/pt/scIndexAction/getNewsById?pkid=" + id, function(data) {
		$(".popup .body").html(data);
	});
	$(".popup-layout").show();
	$(".popup").slideDown();
}

function initData_myMsgNum() {
	$.post("/portal/pt/scIndexAction/getMyMsgNum", function(data) {
		if(data != -1) {
			$("#mymsg .num").html(data);
		}else {
			$("#mymsg .num").html("0");
		} 
	});
}

function initData_myMsg() {
	$("#mymsg .row").remove();
	$.post("/portal/pt/scIndexAction/getMyMsgs", function(data) {
		if(data != -1) {
			for(var i = 0; i < data.length; i++) {
				$newRow = $("<div class=\"row\" pkid=\"" + data[i][0] + "\" title=\"" + data[i][1] + "\"></div>");
				$newRow.append("<div class=\"caption\">" + data[i][1] + "</div>");
				$newRow.append("<div class=\"date\">" + data[i][2] + "</div>");
				$("#mymsg .body").append($newRow);
			}
			
			$("#mymsg .row").mouseenter(function() {
				$(this).addClass("mouseenter");
			}).mouseleave(function() {
				$(this).removeClass("mouseenter");
			}).click(function() {
				loadMsgContent($(this).attr("pkid"), $(this).attr("title"), $(this).find(".date").html());
			});
			
			var width = $("#mymsg .row").first().width();
			$("#mymsg .row").find(".caption").width(width - 120);
		}else {
			$("#mymsg .body").append("<div class=\"nodata\">暂无未读消息</div>");
		} 
	}, "json");
}

function loadMsgContent(id, title, date) {
	$(".popup .caption").html(title);
	$(".popup .date").html(date);
	$(".popup .body").html("");
	$.post("/portal/pt/scIndexAction/getMsgById?pkid=" + id, function(data) {
		$(".popup .body").html(data);
		initData_myMsgNum();
		initData_myMsg();
	});
	$(".popup-layout").show();
	$(".popup").slideDown();
}

function initPopup() {
	$(".popup .close").mouseenter(function() {
		$(this).addClass("closeme");
	}).mouseleave(function() {
		$(this).removeClass("closeme");
	}).click(function() {
		$(".popup-layout").hide();
		$(".popup").slideUp(500);
	});
}

function getTaskUrl(url, arg, titile){
	$.ajax({
		type: "post",                   
		dataType: "json",               
        url: url+"?",
        data: arg,
        complete :function(){},        
        success: function(data){
        	var msg = data[0];
        	if(msg.msg && msg.msg != null){
            	showErrorDialog(msg.msg);
        	}
            else{
            	showDialog(msg.url, msg.title, msg.width, msg.height, 'wfl', null, {isShowLine:false}, null);
        		var dialogName = "$modalDialog" + (showDialog.dialogCount);
				var dls = new Listener("beforeClose");
				//dls.source_id = dialogName;
				//dls.listener_id = 'onBeforeClose_' + dialogName;
				//dls.beforeClose = function (){
				dls.func = function (){
					initData_myWork();
					//getContainer("#pub_scindex_pmng_MgrContentPortlet").doView();
				};
				window.parent.parent.$modalDialog0.addListener(dls);
            }
        },
        error:function(){
		}
		});
}

</script>

</html>