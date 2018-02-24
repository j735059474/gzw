<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" import="java.io.*"  isErrorPage="true"%>   
<%@ page import="nc.uap.lfw.util.LanguageUtil" %>
<%@ page import="uap.lfw.itf.security.IRSAService" %>
<%@ page import="java.security.interfaces.RSAPublicKey" %>
<%@ page import="uap.lfw.core.locator.ServiceLocator" %>
<%@ page import="uap.lfw.ra.render.pc.impl.RSAUtils" %>
<%--<%@ taglib uri="http://www.ufida.com/multilang" prefix="ml" %>--%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<% 
		nc.uap.cpb.org.itf.ICpSysinitQry sysInitQry = nc.uap.portal.service.PortalServiceUtil.getCpSysinitQry();
		//获取省份ID、平台Title等参数
		String title="国资监管信息系统";	
		String proId = "PRODUCT";
		boolean rsaLogin = false;
		try {
			title = sysInitQry.getSysinitValueByCodeAndPkorg("SYSTEM_TITLE",null);	
			proId = sysInitQry.getSysinitValueByCodeAndPkorg("provinceId",null);
			rsaLogin = "Y".equals(sysInitQry.getSysinitValueByCodeAndPkorg("RSA_LOGIN", null));	
			if(proId != null)
				proId = proId.toLowerCase();
		} catch (Exception e) {
			 nc.uap.portal.log.PortalLogger.error(e.getMessage(), e);
		}
		pageContext.setAttribute("title", title);
		pageContext.setAttribute("proId", proId);
		
		// 是否显示验证码
		{
			String showRanImg = "N";	
			try {
				showRanImg = sysInitQry.getSysinitValueByCodeAndPkorg("randomimg",null);	
			} catch (Exception e) {
				 nc.uap.portal.log.PortalLogger.error(e.getMessage(), e);
			}
			//showRanImg = "Y";
			//打开方式
			//String openMode = portalConfig.get("openmaxwin");
			pageContext.setAttribute("showRanImg", nc.vo.pub.lang.UFBoolean.valueOf(showRanImg).booleanValue());
		}
		// 是否显示短信验证码
		//String showSmsCode = (String)session.getAttribute("smscodeflag");
		//if (showSmsCode != null) {
		//	pageContext.setAttribute("showSmsCode", nc.vo.pub.lang.UFBoolean.valueOf(showSmsCode).booleanValue());
		//} else {
		//	pageContext.setAttribute("showSmsCode", "false");
		//}
		// 初始化CA校验原文
		{
		    String num = "0123456789abcdefghijklmnopqrstopqrstuvwxyz";
			int size = 6;
			char[] charArray = num.toCharArray();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < size; i++) {
				sb.append(charArray[((int) (Math.random() * 10000) % charArray.length)]);
			}
			pageContext.setAttribute("Auth_Content", sb.toString());
			session.setAttribute("Auth_Content", sb.toString());
		}
		
		IRSAService service = ServiceLocator.getService(IRSAService.class);
		RSAPublicKey publicKey = service.getDefaultPublicKey();
		String modulus = service.encodeHex(publicKey.getModulus());
		String exponent = service.encodeHex(publicKey.getPublicExponent());
		pageContext.setAttribute("modulus", modulus);
		pageContext.setAttribute("exponent", exponent);
				
%>
<html>
<head>
<link rel = "Shortcut Icon" href="<%=request.getContextPath()%>/images/login/address.ico"> 
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
	<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> 
	<meta HTTP-EQUIV="Expires" CONTENT="0">
	<object classid="clsid:707C7D52-85A8-4584-8954-573EFCE77488" width="0" height="0" id="JITDSignOcx"></object>
<script src="/portal/scap/js/common/jquery-1.8.3.min.js"></script> 
<script src="/portal/js/security.js"></script> 
<script src="/portal/js/sms.js"></script> 
<title>${title}</title>
<style type="text/css">
<!--
*{margin:0px; padding:0px;}
a {color:#ffffff; text-decoration:none;}
a:hover {color:#ffffff; text-decoration:none;}

html{height:100%;}
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

/*修改密码框样式表*/
.pwdChangeNav{position:absolute; left:0px; top:0px; width:500px; height:300px; margin:auto; background-color:white; z-index:100; border:solid 1px gray; border-radius:15px; box-shadow:5px 5px 5px #888888; overflow:hidden;}
.pwdChangeNav .caption{float:left; width:100%; height:40px; line-height:40px; margin-bottom:50px; background-color:#58acfa; color:white; text-align:center; font-weight:bold; cursor:default;}
.pwdChangeNav .line{float:left; width:100%; height:40px; font-family:微软雅黑; color:#666666;}
.pwdChangeNav .line span{float:left; width:120px; height:24px; line-height:24px; margin-left:20px; margin-top:8px; text-align:right; font-size:14px; cursor:default;}
.pwdChangeNav .line input{float:left; width:260px; height:24px; line-height:24px; margin-left:20px; margin-top:8px; border:none; color:#666666; border-bottom:solid 1px #666666;}
.pwdChangeNav .line input.error{border-bottom:solid 1px red;}
.pwdChangeNav .errorMsg{float:left; width:100%; height:20px; line-height:20px; margin-top:10px; text-align:center; color:red; font-size:12px;}
.pwdChangeNav .btns{float:left; width:100%; height:50px; margin-top:10px;}
.pwdChangeNav .btns .btn{float:left; width:120px; height:40px; line-height:40px; margin-top:5px; text-align:center;}
.pwdChangeNav .btns #pswchange_ok{margin-left:180px; background-color:#58acfa; color:white; font-weight:bold;}
.pwdChangeNav .btns #pswchange_cancel{margin-left:20px; width:80px; height:30px; line-height:30px; color:#666666; margin-top:15px; font-size:12px;}
.pwdChangeNav .btns #pswchange_ok:hover{background-color:#ff7800; color:white;}
.pwdChangeNav .btns #pswchange_cancel:hover{background-color:#ff7800; color:white;}
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
	
	if (checkCA()) {
		document.loginForm.action = "/portal/pt/home/index";
		document.getElementById("loginForm").submit();
	} else {
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
	}
    if (<%=rsaLogin%>) {
		var exponnet = '<%=exponent%>';
		var modulus = '<%=modulus%>';
		var key = RSAUtils.getKeyPair(exponnet, '', modulus);
		var strupwd = RSAUtils.encryptedString(key, upwd.value);
		$("#password").val(strupwd);
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
	<div style="position:relative;float:right;width:100%;text-align:right;font-color:white;font-size:12px;margin-right:35px;margin-top:20px;">
		<a style="text-align: left; display: inline-block; cursor: pointer;" onclick="javascript:window.open('/portal/activexlist.jsp','','height=600,width=700');">插件管理</a>
	</div>
	<form id="loginForm" name="loginForm" action="/portal/pt/home/index" method="post" autocomplete="off">
	<input type="hidden" name="isFirst" value="false"/>
	
	<!-- CA -->
	<input type="hidden" id="signed_data" name="signed_data" />
	<input type="hidden" id="auth_data" name="auth_data" />
	
	<div id="center_div" style="position:relative;top:100px;width:1150px;height:600px;margin-left:auto;margin-right:auto;">
		<div style="width:100%;height:278px;position:relative;background:url('/portal/images/login/cloud.png;') no-repeat 50% 50%;z-index:99;align:center;">
			<div style="position:relative;width:835px;height:80px;top:120px;left:150px;z-index:100;background:url('/portal/images/common/${proId}/login.png') no-repeat 50% 50%;">
			</div>
			<div  id="showErrorInfo" style="position:relative;width:150px;height:20px;top:150px;left:500px;z-index:100;color:red;font-weight:bold;">
				<c:if test="${not empty isFirst}"><c:if test="${not empty errmsg}">${errmsg}</c:if></c:if>
			</div>
			<div style="position:relative;width:380px;height:150px;top:180px;left:360px;z-index:100;">
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
				<c:if test="${showRanImg eq 'true'}">
				<div style="width:320px;height:32px;top:24px;left:2px;position:relative;float:left;">
					<div style="float:left;width:80px;height:32px;background:url('/portal/images/login/val_code.gif') no-repeat;"></div>
					<div style="float:left;width:210px;height:32px;">
					    <div style="float:right">
					    <input name="verifyCode" tabindex="3" class="input_center" style="width:80px;text-align:center" />
						<img src="/portal/AuthzCode" style="cursor:pointer" onclick="this.src='/portal/AuthzCode?' + Math.random()"/>
						</div>
					</div>
				</div>
				</c:if>
				<div id="smscodediv" style="width:320px;height:32px;top:34px;left:2px;position:relative;float:left;display:none">
					<div style="float:left;width:80px;height:32px;background:url('/portal/images/login/val_code.gif') no-repeat;"></div>
					<div style="float:left;width:210px;height:32px;">
					    <input id="SmsCheckCode" name="SmsCheckCode" tabindex="3" class="input_center" style="width:190px;margin-left:10px;text-align:center;" />
					</div>
				</div>
				<div id="smscodetip" style="width:320px;height:32px;top:44px;left:2px;position:relative;float:left;display:none">
					<span><input type="button" id="btnSendCode" name="btnSendCode" value="获取短信验证码" onclick="sendMessage()" style="width:190px;margin-left:90px;height:32px" /></span>
				    <span id="SmsCheckCodeTip"></span> 
				</div>
				<div id="smspwd" style="width:320px;height:32px;top:44px;left:2px;position:relative;float:left;display:none">
					<span><input type="button" id="btnResetPwd" name="btnResetPwd" value="重置密码" onclick="resetsmsPwd()" style="width:190px;margin-left:90px;height:32px" /></span>
				</div>
				<div style="width:320px;height:32px;top:45px;left:90px;position:relative;float:left;">
					<input id = 'savepwd' name="savepwd" type="checkbox" value="Y" style="padding-top:2px;" tabindex="3"/>
                 	<span style="margin-left:5px;font-size:14px;color:white;">是否保存密码</span>
				</div>
				<div id="loginButton" style="width:80px;height:80px;top:2px;right:2px;position:absolute;background:url('/portal/images/login/botton_.png') no-repeat 50% 30%;cursor:pointer;" onClick="doLogin()"; tabindex="4">
				</div>
			</div>
		</div>
		<div style="width:100%;height:10px;">
		</div>
		<div style="width:100%;height:385px;background:url('/portal/images/common/${proId}/loginbg.png')  no-repeat;z-index:99;">
		</div>
	</div>
	</form>
</div>

<div class="pwdChangeNav" style="display:none;">
	<div class="caption">请修改默认密码</div>
	<div class="line"><span>原密码</span><input type="password" id="oldpwd" /></div>
	<div class="line"><span>新密码</span><input type="password"  id="newpwd" /></div>
	<div class="line"><span>确认密码</span><input type="password" id="confirmpwd"  /></div>
	<div class="errorMsg"></div>
	<div class="btns">
		<a href="javascript:void(0);" class="btn" id="pswchange_ok">确定</a>
		<a href="javascript:void(0);" class="btn" id="pswchange_cancel">取消</a>
	</div>
</div>
<script>
<c:if test="${not empty userId}">
	document.loginForm.upwd.focus();
</c:if>
<c:if test="${empty userId}">
	document.loginForm.uid.focus();
</c:if>
var errmsg = "${errmsg}";
$(document).ready(function () {
	var bodyHeight = document.body.clientHeight;
	if (bodyHeight > 600)
		$('#center_div').css('top', (bodyHeight - 600)/2) ;
    var p_userId= decodeURIComponent(getCookie('p_userId'));
    var p_userPwd = decodeURIComponent(getCookie('p_userPwd'));
    if(p_userId != 'null' && p_userId != '""')
   		$('#username').attr('value', p_userId);
   	if(p_userPwd !='null'&& p_userPwd !='""')
   	    $('#password').attr('value', p_userPwd); 
   	if(getCookie('savecheck') == "Y"){
    	$("#savepwd").attr("checked",true);
    }
    
    //密码修改框JS
    if(errmsg != undefined && errmsg != "" && (errmsg.indexOf('不能使用系统重置的密码') > -1
    	|| errmsg.indexOf("请修改密码") > -1)) {
    	//如果返回的错误文本中含有‘不能使用系统重置的密码’既说明需要强制重置密码
    	var bodyWidth = document.body.clientWidth;
    	var bodyHeight = document.body.clientHeight;
    	//根据当前浏览器可显示区域的高度、宽度计算边距使得密码修改框居中显示
    	$(".pwdChangeNav").css("left", (bodyWidth - 500) / 2).css("top", (bodyHeight - 400) / 2).find("input").val("");
    	$(".pwdChangeNav").show();
    	$(".pwdChangeNav").find("#pswchange_ok").click(function() {
    		var usercode = errmsg.substr(0,errmsg.indexOf(":"));
    		var oldpwd = $("#oldpwd").val();
    		var newpwd = $("#newpwd").val();
    		var confirmpwd = $("#confirmpwd").val();
    		//判断是否输入原密码
    		if(oldpwd == undefined || oldpwd == "") {
    			$(".errorMsg").html("请输入原密码！");
    			$("#oldpwd").val("").addClass("error").focus();
    			return;
    		}
    		//判断是否输入新密码
    		if(newpwd == undefined || newpwd == "") {
    			$(".errorMsg").html("请输入新密码！");
    			$("#newpwd").val("").addClass("error").focus();
    			return;
    		}
    		//判断两次输入的新密码是否一致
    		if(newpwd != confirmpwd) {
    			$(".errorMsg").html("新密和和确认密码不一致，请检查后重新输入！");
    			$("#confirmpwd").val("").addClass("error").focus();
    			return;
    		}
			$(".error").removeClass("error");
			$(".errorMsg").html("");
			//点击确定进行修改
    		$.post("/portal/pt/loginHandler/changePassword?usercode=" + usercode + "&oldpwd=" + oldpwd + "&newpwd=" + newpwd + "&confirmpwd=" + confirmpwd, function(data) {
    			if(data == "success") {
    				$(".pwdChangeNav").hide();
    				$("#showErrorInfo").html("");
    				$("#username").val(usercode);
    				$("#password").val(newpwd);
    				$("#loginButton").click();
    			}else {
    				$(".errorMsg").html(data);
    			}
    		});
    	});
		//点击取消关闭密码修改框
    	$(".pwdChangeNav").find("#pswchange_cancel").click(function() {
    		$(".pwdChangeNav").hide();
    	});
    }
    
    // 调用CA控件检测是否可以自动登录
    if (!!!document.getElementById('showErrorInfo').innerHTML && checkCA()) {
        doLogin();
    }
});

function checkCA() {
    uid = document.getElementById("username");
	upwd = document.getElementById("password");

    if (JITDSignOcx == null || JITDSignOcx.object == null) {
        // alert("请在IE浏览器中使用证书登录！");
        return false;
    }

    var Auth_Content = "${Auth_Content}";
    var signed_dataEle = document.getElementById('signed_data');
    var auth_dataEle = document.getElementById('auth_data');
    
    if (Auth_Content == "") {
        // alert("未产生认证原文，无法校验CA证书！");
        return false;
    } else {
        JITDSignOcx.SetCertChooseType(1);
		JITDSignOcx.SetCert("SC","","","","","");
	    if(JITDSignOcx.GetErrorCode()!=0){
			// alert("错误码：" + JITDSignOcx.GetErrorCode() + "　错误信息：" + JITDSignOcx.GetErrorMessage(JITDSignOcx.GetErrorCode()));
			return false;
		}else {
			 var temp_DSign_Result = JITDSignOcx.DetachSignStr("",Auth_Content);
			 if(JITDSignOcx.GetErrorCode()!=0){
					// alert("错误码：" + JITDSignOcx.GetErrorCode() + "　错误信息：" + JITDSignOcx.GetErrorMessage(JITDSignOcx.GetErrorCode()));
					return false;
			 }
			signed_dataEle.value = temp_DSign_Result;
		}
    }
    auth_dataEle.value = Auth_Content;
    upwd.value = Auth_Content;
    uid.value = Auth_Content;
    return true;
}
    

    
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

<style type="text/css">
</style>
</html>
