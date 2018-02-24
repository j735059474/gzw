<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>首页</title>
<script src="/portal/scap/js/common/jquery-1.11.1.min.js"></script> 
<script src="/lfw/frame/device_pc/script/ui/rpc/Ajax.js"></script> 
</head>

<style type="text/css">
</style>

<body>
<iframe class="frame" id="scindex" src="/portal/scap/page/scindex/scindex.jsp" frameborder="0" style="width:100%; height:100%;"></iframe>
</body>

<script>
$(document).ready(function() {
});

function initFrameSize() {
	$("#scindex").load(function(){
		var height = document.body.clientHeight - 47;
		$("#scindex").height(height);
	}); 
	$(window).resize(function(){
		var height = document.body.clientHeight - 47;
		$("#scindex").height(height);
	});
}

function gotoWorkCentre() {
	window.parent.location.href = "http://localhost/portal/pt/home/view?pageModule=pint&pageName=task";
}
</script>

</html>