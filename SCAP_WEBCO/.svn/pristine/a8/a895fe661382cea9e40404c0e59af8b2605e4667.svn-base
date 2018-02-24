<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ page import="nc.uap.portal.integrate.sso.itf.ISSOQueryService" %>
<%@ page import="nc.uap.portal.integrate.credential.PtCredentialVO" %>
<%@ page import="nc.bs.framework.common.NCLocator" %>
<%@ page import="nc.uap.lfw.core.LfwRuntimeEnvironment" %>
<% 
	// 浙江国资党建
	
	ISSOQueryService service = NCLocator.getInstance().lookup(ISSOQueryService.class);
	String userId = LfwRuntimeEnvironment.getLfwSessionBean().getUser_code();
	PtCredentialVO vo = service.getCredentials(userId, "", "dj", 1);
	
	
	String action = "http://10.241.12.25/index.aspx";
	String input_username = vo.getUserid();
	String input_password = vo.getPassword();
%>
<form name="myform" action="<%=action%>" method="POST">
	<input type="hidden" name="Userid" value="<%=input_username%>">
	<input type="hidden" name="Password" value="<%=input_password%>">
</form>
<script>
document.myform.submit();
</script>