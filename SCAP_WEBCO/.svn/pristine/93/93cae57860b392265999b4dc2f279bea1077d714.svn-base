<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" import="java.io.*"  isErrorPage="true"%>   
<%@ page import="nc.uap.lfw.util.LanguageUtil" %>
<%--<%@ taglib uri="http://www.ufida.com/multilang" prefix="ml" %>--%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<% 
		nc.uap.cpb.org.itf.ICpSysinitQry sysInitQry = nc.uap.portal.service.PortalServiceUtil.getCpSysinitQry();
		//获取省份ID、平台Title等参数
		String title="国资监管信息系统";	
		String proId = "PRODUCT";
		try {
			title = sysInitQry.getSysinitValueByCodeAndPkorg("SYSTEM_TITLE",null);	
			proId = sysInitQry.getSysinitValueByCodeAndPkorg("provinceId",null);	
			if(proId != null)
				proId = proId.toLowerCase();
		} catch (Exception e) {
			 nc.uap.portal.log.PortalLogger.error(e.getMessage(), e);
		}
		pageContext.setAttribute("title", title);
		pageContext.setAttribute("proId", proId);
%>
<html>
<head>
<link rel = "Shortcut Icon" href="<%=request.getContextPath()%>/images/login/address.ico"> 
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
	<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
	<meta HTTP-EQUIV="Expires" CONTENT="0"> 
<script src="/portal/scap/js/common/jquery-1.8.3.min.js"></script> 
<title>${title}</title>
<style type="text/css">
<!--
*{margin:0px; padding:0px;}
a {color:#ffffff; text-decoration:none;}
a:hover {color:#ffffff; text-decoration:none;}

body {width:100%; height:100%; overflow:hidden; background-color:#94e0fa;}
.input_user {float:left; width:43px; height:32px; margin-left:8px; border:0px;}
.input_password {float:left; width:43px; height:32px; margin-left:8px; border:0px;}
.input_center {
	float:left; width: 150px; height:32px; line-height:32px; border:0px;
	background:url("<%=request.getContextPath()%>/images/login/login_input_center.gif") repeat-x; 
	font-family:Verdana, "??"; font-size: 14px; font-weight: bold;color:#666699;
}
.input_right {float:left; width:6px; height:32px; border:0px;}

-->
</style>
<script language="JavaScript" type="text/javascript">
var uid;
var upwd;

document.onkeydown=function(e){//网页内按下回车触发,只有在焦点不在用户名上的时候触发
	//if(document.activeElement.id != 'username'){
		switch ((window.event)?event.keyCode:e.which){
			case 13:
				if(document.activeElement.id == 'username'){
					$("#password").focus();
					break;
				}
				doLogin();
				return false;
				break;
			default:
				break;
		}
	//}
}

function changeImg(){
	var A = document.getElementById("imgVerify");
	var B = document.getElementById("imgCode");
	A.src="${base}/AuthCode?code="+Math.random();
	B.focus();
	B.select();
}

function doLogin(){
	
	uid = document.getElementById("username");
	upwd = document.getElementById("password");
	if(trim(uid.value).length == 0){
    	document.getElementById('showErrorInfo').innerHTML = '用户名不能为空';
	  	uid.focus();
	  	return false;
	} 
	if(trim(upwd.value).length == 0){
  		document.getElementById('showErrorInfo').innerHTML = '密码不能为空';
 		upwd.focus();
 		return false;
	} 
	document.loginForm.action = "/portal/pt/home/index";
	document.getElementById("loginForm").submit();
}
/**
* 删除左右两端的空格
*/
function trim(str)
{
	return str.replace(/(^\s*)(\s*$)/g, '');
}
</SCRIPT>
</head>

<body>
<div id=outerDiv class="bg_table" style="width:100%;height:100%;min-height:600px;min-width:800px;background-color:#d7d8dc;background:url('/portal/images/login/bg.jpg') repeat-x;">
	<form id="loginForm" name="loginForm" action="/portal/pt/home/index" method="post" autocomplete="off">
	<input type="hidden" name="isFirst" value="false"/>
	<div id="center_div" style="position:relative;top:100px;width:1150px;height:600px;margin-left:auto;margin-right:auto;">
		<div style="width:100%;height:278px;position:relative;background:url('/portal/images/login/cloud.png;') no-repeat 50% 50%;z-index:99;align:center;">
			<div style="position:relative;width:835px;height:80px;top:120px;left:150px;z-index:100;background:url('/portal/images/login/bg/login_sy.gif;') no-repeat 50% 50%;">
			</div>
			<div style="position:relative;width:380px;height:150px;top:200px;left:360px;z-index:100;">
				<div style="width:320px;height:32px;top:4px;left:2px;position:relative;float:left;">
					<div style="float:left;width:80px;height:32px;background:url('/portal/images/login/login_user.gif') no-repeat;"></div>
					<div style="float:left;width:210px;height:32px;">
						<img src="/portal/images/login/login_u_left.png" class="input_user" />
	            		<input id="username" name="uid" tabindex="1" type="text" value="<c:if test="${not empty userId}">${userId}</c:if>" class="input_center" maxlength="20" autocomplete="off"/>
	            		<img src="/portal/images/login/login_input_right.png" class="input_right"/>
					</div>
				</div>
				<div style="width:320px;height:32px;top:14px;left:2px;position:relative;float:left;">
					<div style="float:left;width:80px;height:32px;background:url('/portal/images/login/login_password.gif') no-repeat;"></div>
					<div style="float:left;width:210px;height:32px;">
						<img src="/portal/images/login/login_c_left.png" class="input_user" />
	            		<input id="password" name="upwd" tabindex="2" type="password" class="input_center" maxlength="20" />
	            		<img src="/portal/images/login/login_input_right.png" class="input_right"/>
					</div>
				</div>
				<div style="width:320px;height:32px;top:25px;left:90px;position:relative;float:left;">
					<input name="savepwd" type="checkbox" value="" style="padding-top:2px;" tabindex="3"/>
                 	<span style="margin-left:5px;font-size:14px;color:white;">是否保存密码</span>

				</div>
				<div id="loginButton" style="width:80px;height:80px;top:2px;right:2px;position:absolute;background:url('/portal/images/login/botton_.png') no-repeat 50% 30%;cursor:pointer;" onClick="doLogin()"; tabindex="4">
				</div>
			</div>
		</div>
		<div style="width:100%;height:10px;">
		</div>
		<div style="width:100%;height:385px;background:url('/portal/images/login/bg/bg_sy.png;')  no-repeat;z-index:99;">
		</div>
	</div>
	</form>
</div>
<script>
<c:if test="${not empty userId}">
	document.loginForm.upwd.focus();
</c:if>
<c:if test="${empty userId}">
	document.loginForm.uid.focus();
</c:if>
$(document).ready(function () {
	alert(document.body.clientWidth);
	alert(document.body.clientHeight);
    var p_userId= decodeURIComponent(getCookie('p_userId'));
    var p_userPwd = decodeURIComponent(getCookie('p_userPwd'));
    if(p_userId != 'null')
   		$('#username').attr('value', p_userId);
   	if(p_userPwd !='null')
   	    $('#password').attr('value', p_userPwd); 
    });
    
function getCookie(sName) {
	var sRE = "(?:; )?" + sName + "=([^;]*);?";
	var oRE = new RegExp(sRE);

	if (oRE.test(document.cookie)) {
		return decodeURIComponent(RegExp["$1"]);
	} else
		return null;
};    
</script>
</body>
</html>
