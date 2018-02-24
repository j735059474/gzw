<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="utf-8"%>
<% 
	// 集成浙江国资委泰宇档案系统
	String url = "http://10.241.12.28/ddm/index.html?";
	String uc = request.getParameter("username");
	String up = request.getParameter("password");
	if (up != null) {
		up = nc.vo.iufo.data.MD5Util.encrypt(up).toLowerCase();
	}
	
	response.sendRedirect(url + "uc=" + uc + "&" + "up=" + up);
%>