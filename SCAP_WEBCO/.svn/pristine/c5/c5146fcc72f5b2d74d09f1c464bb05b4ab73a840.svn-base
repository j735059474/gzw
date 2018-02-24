var InterValObj; //timer变量，控制时间  
var count = 60; //间隔函数，1秒执行  
var curCount;//当前剩余秒数  
  
function sendMessage() {  
    curCount = count;  
    var uid = $("#username").val();
    if(trim(uid).length == 0){
    	document.getElementById('showErrorInfo').innerHTML = '用户名不能为空';
	  	uid.focus();
	  	return false;
	}
    // 设置button效果，开始计时  
    $("#btnSendCode").attr("disabled", "true");  
    //$("#SmsCheckCode").attr("disabled", "false");
    $("#btnSendCode").val(curCount + "秒后重新获取");  
    InterValObj = window.setInterval(SetRemainTime, 1000); // 启动计时器，1秒执行一次  
    var param = {"action":"getSmsCode","uid":uid };
    // 向后台发送处理数据  
    $.ajax({  
        type: "POST",
        url: "/portal/SmsCode",
        dataType: "json", 
        data: param,
        success: function (data){
        	if (data != -1) {
        		//两种都可以
        		//data["errmsg"];
        		//data.errmsg;
        		if(data.errmsg != null) {
					$("#showErrorInfo").html(data.errmsg).show(); 
					setTimeout( function(){$("#showErrorInfo").fadeOut();}, ( 3000 ) ); 
        		}
        	}
        }  
    }); 
}  
function resetsmsPwd() {  
    curCount = count;  
    var uid = $("#username").val();
    if(trim(uid).length == 0){
    	document.getElementById('showErrorInfo').innerHTML = '用户名不能为空';
	  	uid.focus();
	  	return false;
	}
    var param = {"action":"resetSmsPwd","uid":uid };
    // 向后台发送处理数据  
    $.ajax({  
        type: "POST",
        url: "/portal/SmsCode",
        dataType: "json", 
        data: param,
        success: function (data){
        	if (data != -1) {
        		//两种都可以
        		//data["errmsg"];
        		//data.errmsg;
        		if(data.errmsg != null) {
					$("#showErrorInfo").html(data.errmsg).show(); 
					setTimeout( function(){$("#showErrorInfo").fadeOut();}, ( 3000 ) ); 
        		}
        	}
        }  
    }); 
}  
//timer处理函数  
function SetRemainTime() {  
    if (curCount == 0) {                  
        window.clearInterval(InterValObj);// 停止计时器  
        $("#btnSendCode").removeAttr("disabled");// 启用按钮  
        $("#btnSendCode").val("重新发送验证码");
    }else {  
        curCount--;  
        $("#btnSendCode").val("" + curCount + "秒后重新获取");  
    }  
}  


function isSmsCode () {
 		var uid = $("#username").val();
 		if (trim(uid).length == 0) {
 			return;
 		}
		var param = {"action":"ifSmsCode","uid":uid };
	    // 向后台发送处理数据  
	    $.ajax({  
	        type: "POST",
	        url: "/portal/SmsCode",
	        dataType: "json", 
	        data: param,
	        success: function (data){    
		        if (data != -1) {
	        		//两种都可以
	        		//data["errmsg"];
	        		//data.errmsg;
	        		if(data.errmsg != null) {
						$("#showErrorInfo").html(data.errmsg).show(); 
						setTimeout( function(){$("#showErrorInfo").fadeOut();}, ( 3000 ) ); 
	        		}
					if(data.smscodeflag != null && data.smscodeflag == "true") {
	        			$('#smscodediv').css("display","block");
	        			$('#smscodetip').css("display","block");
	        		} else {
	        			$('#smscodediv').css("display","none");
	        			$('#smscodetip').css("display","none");
	        		}
	        		/*
					if(data.smspwdflag != null && data.smspwdflag == "true") {
	        			$('#smspwd').css("display","block");
	        		} else {
	        			$('#smspwd').css("display","none");
	        		}
	        		*/
	        	}
	        }
	    }); 
}  
$(document).ready(function() {
 	$("#username").blur(function() {  
		isSmsCode();
 	});
 	var uid = document.getElementById("username");
	if(trim(uid.value).length != 0){
		$("#username").blur();
	} else if(getCookie('savecheck') == "Y"){
    	$("#username").blur();
    }
});
function getCookie(sName) {
	var sRE = "(?:; )?" + sName + "=([^;]*);?";
	var oRE = new RegExp(sRE);

	if (oRE.test(document.cookie)) {
		return decodeURIComponent(RegExp["$1"]);
	} else
		return null;
};
/*
function validateSmsCode() {
        var SmsCheckCodeVal = $("#SmsCheckCode").val();  
		var param = {"action":"validateSmsCode","uid":uid,"smscheckcode":SmsCheckCodeVal };
	    // 向后台发送处理数据  
	    $.ajax({  
	        type: "POST",
	        url: "/portal/SmsCode",
	        dataType: "json", 
	        data: param,
            success : function(data) {  
                if (data != -1) {  
                	if(data.errmsg != null) {
	         			$("#showErrorInfo").html(data.errmsg).show();             	
                	}
                	if (data.validate_flag != null && data.validate_flag == "true") {
                			curCount = 0;
                		    window.clearInterval(InterValObj);// 停止计时器  
						    $("#btnSendCode").val("获取短信验证码");
		        			//$('#smscodediv').css("display","none");
		        			//$('#smscodetip').css("display","none");
		        			setTimeout( function(){$("#showErrorInfo").fadeOut();}, ( 1000 ) );
                	} else {
                		setTimeout( function(){$("#showErrorInfo").fadeOut();}, ( 3000 ) ); 
                	}
                } 
            }  
        });  
}
*/