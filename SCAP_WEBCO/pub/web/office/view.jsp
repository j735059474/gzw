<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.ufida.com/lfw" prefix="lfw"%>
<%@ taglib	uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="nc.vo.ml.NCLangRes4VoTransl"%>

<%
	//img,txt,office
	String filetype = StringUtils.defaultIfEmpty(request.getParameter("filetype") , "");
	String url = StringUtils.defaultIfEmpty(request.getParameter("url") , "");
	String editonlieable = StringUtils.defaultIfEmpty(request.getParameter("editonlieable") , "false");
	String printable = StringUtils.defaultIfEmpty(request.getParameter("printable") , "false");
	url  = java.net.URLDecoder.decode(url, "UTF-8") ;
	
	String fileView="文件浏览";
	fileView=NCLangRes4VoTransl.getNCLangRes().getString("bc", fileView ,"viewjsp-000001");
	
	String noSupportStr = "未支持的类型";
	noSupportStr = NCLangRes4VoTransl.getNCLangRes().getString("bc", noSupportStr ,"viewjsp-000002");
	
	String print = "打印";
	print = NCLangRes4VoTransl.getNCLangRes().getString("bc", print ,"viewjsp-000003");
	

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
      <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate"/>
<meta http-equiv="Expires" content="0"/>
<meta http-equiv="Pragma" content="no-cache"/>
       <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
       
       <title><%=fileView%></title>
       </head>
       <style>
       		html,body{overflow:hidden;width:100%; height:100%;margin:0;padding:0; border:0;}
       		.view {border: solid 0px #ebebeb;padding:none; width:100%; height:100%; overflow:auto; }
       		.view img{border:none;}
       		.view object,.view iframe {border : none; readonly:true;};
       		.view textarea{border:none;padding:none;margin:none;width:100%;height:100%;overflow:auto; }
       		.popview {position:absolute;bottom:0px;width:100%;text-align:center;display:none;}
       		.popview img{cursor: pointer;}
       </style>
	<body>
	<script>	
        var flag = false;
        var maxwidth = 802;
        var maxheight = 427;
        function   DrawImage(ImgD){ 
            var   image=new   Image(); 
            image.src=ImgD.src; 
            if(image.width> 0   &&   image.height> 0){ 
                flag=true;
                var i = image.width/maxwidth;
                var j = image.height/maxheight;
                if(i > 1 || j > 1){
                	if(i > j){
                		ImgD.width = maxwidth;
                		ImgD.height = image.height * (maxwidth / image.width);
                	}
                	else{
                		ImgD.height = maxheight;
                		ImgD.width = image.width * (maxheight / image.height);
                	}
                }else{ 
                    ImgD.width=image.width;   
                    ImgD.height=image.height; 
                }
                $(".view").css("text-align","center");
                $(".view").css("padding-top",(maxheight - ImgD.height)/2);
                //ImgD.style.marginTop = (maxheight - ImgD.height)/2;
                //ImgD.style.marginLeft = (maxwidth - ImgD.width)/2;
            }
        }
        function PrintImg(){
        	$("div#myPrintArea").printArea(); 
        }
		(function($) {
			var printAreaCount = 0;
			$.fn.printArea = function() {
				var ele = $(this);
				var idPrefix = "printArea_";
				removePrintArea(idPrefix + printAreaCount);
				printAreaCount++;
				var iframeId = idPrefix + printAreaCount;
				var iframeStyle = 'position:absolute;width:0px;height:0px;left:-500px;top:-500px;';
				iframe = document.createElement('IFRAME');
				$(iframe).attr({
							style : iframeStyle,
							id : iframeId
						});
				document.body.appendChild(iframe);
				var doc = iframe.contentWindow.document;
				$(document).find("link").filter(function() {
							return $(this).attr("rel").toLowerCase() == "stylesheet";
						}).each(function() {
					doc.write('<link type="text/css" rel="stylesheet" href="'
							+ $(this).attr("href") + '" >');
				});
				doc.write('<div class="' + $(ele).attr("class") + '">' + $(ele).html()
						+ '</div>');
				doc.close();
				var frameWindow = iframe.contentWindow;
				frameWindow.close();
				frameWindow.focus();
				frameWindow.print();
			}
			var removePrintArea = function(id) {
				$("iframe#" + id).remove();
			};
		})(jQuery);		
		$(document).ready(function() {
				var filetype = '<%= filetype%>';
				var url = '<%= url%>';
				var printable = '<%= printable%>';
				if(filetype == "img"){
					maxhieght = $(".view").offsetHeight;
					maxwidth = $(".view").offsetWidth;
					if(maxhieght == undefined)
						maxhieght = $(".view").height();
					if(maxwidth== undefined)
						maxwidth = $(".view").width();
					var htmlstr = "<div id='myPrintArea'><img  src= '"+ url +"' onload='javascript:try{DrawImage(this)}catch(e){};'/></div>";
					$(".view").html(htmlstr);
					if (printable == 'true')
						$(".popview").css("display","block");
				}
				else if(filetype == "office" || filetype == "gw" || filetype == "sep"){
					var htmlstr = "<iframe src= '" + url + "' type='text/x-script'  width='100%' height='100%' marginwidth=0 align='top' scrolling='no'></iframe>";
					$(".view").html(htmlstr);
				}
				else if(filetype == "pdf"){
					window.open(url);
				}
				else{
					$(".view").css("text-align","center");
                	$(".view").css("padding-top",(maxheight)/2);
					$(".view").html("<div>"+'<%=noSupportStr%>'+"</div>");
				}
			});
			</script>
				<div class="view">
				</div>
				<div class="popview">
					<img title="<%=print%>" src="scan/img/print.png" onclick='javascript:PrintImg();'>
				</div>
			
	</boby>
</html>