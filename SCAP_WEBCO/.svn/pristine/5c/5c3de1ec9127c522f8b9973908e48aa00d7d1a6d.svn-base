<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="nc.uap.lfw.core.LfwRuntimeEnvironment"%>
<!DOCTYPE html>
<%
    String id = request.getParameter("id");
    String sysid = request.getParameter("sysid");
    String rootpath = LfwRuntimeEnvironment.getRootPath();
    if (rootpath == null) {
        rootpath = "/portal";
    }
%>
<html>
<head>
    <object id="SedFun" classid="clsid:6268EAAB-B15A-4243-96CF-DBD645B5C3C9" width="0" height="0"></object>
	<object id="SedOI" classid="clsid:567FF69D-56E3-11D6-81D1-00E04CE60E84" width="0" height="0"></object>
	<script>
	function init() {
	    
	    var url = "<%=rootpath%>/pt/doc/file/down?sysid=<%=sysid%>&id=<%=id%>";
	    url = window.location.href.substr(0, window.location.href.indexOf("/", 10)) + url;
		try{
			if (!SedFun.object || !SepReader.object) {
			    alert("无法预览文档：请安装书生电子印章客户端软件，并在IE浏览器中打开！");
			    return;
			}
			var tempDir = SedFun.GetWinTempDir();
	        var path = SedFun.DownLoadFile(tempDir + "\\", url, "sursen_gw");
	        if (!path) {
	            alert("缓存文档失败！");
	            return;
	        }
			SepReader.FileName = path
			SepReader.Page=0;
			SepReader.Ratio=100;
			SepReader.focus();
		}catch(e){
		    alert(e);
		}
	}
	</script>
</head>
<body onload="init()">
	<object id="SepReader" classid="clsid:34E23F0A-1F7A-423b-826A-BB780154357D" width="780px" height="400px">
		<param name="Language" value="CHS">
		<param name="DrawBoder" value="0">
	</object>
</body>
</html>