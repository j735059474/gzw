package nc.scap.pub.scindex.portlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import nc.bs.framework.common.NCLocator;
import nc.scap.pub.util.FreeMarkerUtil;
import nc.scap.pub.util.HttpUtils;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.portal.exception.PortalServiceException;
import nc.uap.portal.integrate.credential.PtCredentialVO;
import nc.uap.portal.integrate.sso.itf.ISSOQueryService;
import nc.uap.portal.log.ScapLogger;
import nc.uap.portal.task.ctrl.TaskQryParam;
import nc.uap.portal.task.ui.TaskPaginationQueryImpl;
import nc.uap.portal.util.PortalRenderEnv;
import nc.uap.portal.util.PtUtil;
import nc.uap.portal.util.freemarker.FreeMarkerTools;
import nc.uap.portlet.base.PtBasePortlet;
import nc.uap.wfm.vo.WfmTaskVO;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import sun.misc.BASE64Encoder;

import com.sursen.interfaces.filelist.service.IFilelistService;

import freemarker.template.Template;

public class SCEnterpriseRankPortlet extends PtBasePortlet {
	
	public static final String TEMPLATE_DIR = "/portal/scap/page/scindex/portlets/SCEnterpriseRankPortlet.html";
	
	@Override
	@SuppressWarnings(value = { "unchecked" })
	protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {

		Map<String, Object> root = new HashMap<String, Object>();
		
		try {
			Template t = FreeMarkerUtil.getFreeMarkerCfg().getTemplate(TEMPLATE_DIR);
			String html = FreeMarkerTools.render(t, root);
			response.getWriter().print(html);
		} catch (Throwable e) {
			ScapLogger.error("äÖÈ¾MyWorkPortlet³ö´í", e);
		}
	}
	
}
