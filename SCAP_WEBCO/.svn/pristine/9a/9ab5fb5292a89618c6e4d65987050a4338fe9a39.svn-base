/**
 * 
 */
package nc.scap.pub.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;


/**
 * @author Administrator
 *
 */
public class PubFinereportServlet extends HttpServlet{
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doPost(request, response);

	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String ppt=request.getParameter("reportlet");
		String op = request.getParameter("op");
		String para = "";
		if (op!=null && op.length()>0) 
			para="&op="+op;
		//用于帆软节点。根据当前用户所在的组织只能看本企业的。国资委的看到所有企业  
		int flag=CpOrgUtil.intGwzOrCompanyOrPartnerUser(CntUtil.getCntUserPk());
		String url = "";
		if (ppt.equalsIgnoreCase("scaprar_cqgz.cpt")) {
			url = "/portal/ReportServer?reportlet="+ppt+"&cur_org="+CntUtil.getCntOrgPk()+"&flag="+flag+para;
		} else {
			url = "/portal/ReportServer?reportlet="+ppt+"&cur_org="+CntUtil.getCntOrgPk()+"&flag="+flag+para;
		}
		response.sendRedirect(url);
	}
}
