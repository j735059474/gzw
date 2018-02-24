package nc.scap.pub.index.portlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.log.PortalLogger;
import nc.uap.portal.task.ctrl.TaskQryParam;
import nc.uap.portal.task.ui.TaskPaginationQueryImpl;
import nc.uap.portal.util.PortalRenderEnv;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.util.freemarker.FreeMarkerTools;
import nc.uap.portlet.base.PtBasePortlet;
import nc.uap.wfm.vo.WfmTaskVO;
import nc.vo.ml.NCLangRes4VoTransl;

public class CopyrightPortlet extends PtBasePortlet {

	@Override
	protected void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
	
		Map<String,Object> root = new HashMap<String,Object>();
		
		String html = null;
		
		String resourcePath = PtUtil.getResourcePath(PortalRenderEnv.getCurrentPage());
		root.put("RES_PATH", resourcePath);
		try {
			root.put("WINID", request.getWindowID());
			String template = "portlets/CopyrightPortlet.html";
//				html = FreeMarkerTools.contextTemplatRender(template, root);
			html = FreeMarkerTools.render(template, root);
		} catch (Exception e) {
			html = NCLangRes4VoTransl.getNCLangRes().getStrByID("pint", "MyWflPortlet-000000")/*获取待办流程失败:*/ + e.getMessage();
			PortalLogger.error(html, e);
		}
		response.getWriter().print(html);
//		response.getWriter().flush();
		
	}

}
