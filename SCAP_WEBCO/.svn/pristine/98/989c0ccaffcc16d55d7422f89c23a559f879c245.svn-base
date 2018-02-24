<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ page import="nc.uap.portal.integrate.sso.itf.ISSOQueryService" %>
<%@ page import="nc.uap.portal.integrate.credential.PtCredentialVO" %>
<%@ page import="nc.bs.framework.common.NCLocator" %>
<%@ page import="nc.uap.lfw.core.LfwRuntimeEnvironment" %>
<% 
	// 浙江国资委杭飞OA
	
	ISSOQueryService service = NCLocator.getInstance().lookup(ISSOQueryService.class);
	String userId = LfwRuntimeEnvironment.getLfwSessionBean().getUser_code();
	PtCredentialVO vo = service.getCredentials(userId, "", "hfoa", 1);
	
	
	String action = "http://10.241.12.15/names.nsf?login";
	String input_username = vo.getUserid();
	String input_password = vo.getPassword();
	String input_RedirectTo = "/weboa/system/main.nsf/indexoa?openform";
%>
<form name="myform" action="<%=action%>" method="POST">
	<input type="hidden" name="username" value="<%=input_username%>">
	<input type="hidden" name="password" value="<%=input_password%>">
	<input type="hidden" name="RedirectTo" value="<%=input_RedirectTo%>">
</form>
<script>
document.myform.submit();
</script>