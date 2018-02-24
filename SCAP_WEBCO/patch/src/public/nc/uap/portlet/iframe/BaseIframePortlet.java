package nc.uap.portlet.iframe;

import java.io.IOException;
import java.net.URLDecoder;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import nc.uap.lfw.util.StringUtil;
import nc.uap.portal.util.PortletSessionUtil;
import nc.uap.portlet.base.PtBasePortlet;

import org.apache.commons.lang.StringUtils;
import org.granite.hash.FNV164Hash;

import uap.json.JSONObject;
import uap.lfw.core.ml.LfwResBundle;
import uap.lfw.portal.log.PortalOperatorLogHelper;

/**
 * IframePortlet����
 * 
 * @author licza
 * @since 2010-8-10
 */
public class BaseIframePortlet extends PtBasePortlet {
	private static final String _IFRAME = "_iframe";
	/** Ĭ�ϵ�Iframeҳ�� **/
	protected static final String IFRAME_PORTLET_PAGE = "/pages/iframe.jsp";
	protected static final String SRC_PARAM = "if_src";
	protected static final String WIDTH_PARAM = "if_width";
	protected static final String HEIGHT_PARAM = "if_height";
	protected static final String SRC_TYPE = "if_src_type";
	protected static final String srcVal = "scr";
	protected static final String ALLOW_SCROLL = "scrolling";
	
	@Override
	protected String getEtag(RenderRequest request, RenderResponse response) {
		String frameURL = getValue(request, SRC_PARAM, getDefaultFrameURL());
		return String.valueOf(new FNV164Hash().hash(frameURL));
	}

	/**
	 * ���FrameURL
	 * 
	 * @return
	 */
	protected String getDefaultFrameURL() {
		return "/";
	}

	/**
	 * IframePortlet��Ĭ��ʵ��
	 * 
	 * @see javax.portlet.GenericPortlet
	 */
	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		String windowID = request.getWindowID();
		String frameURL = getValue(request, SRC_PARAM, getDefaultFrameURL());
		boolean urlScroll = frameURL.endsWith("scroll=1");
		frameURL = PortletSessionUtil.makeAnchor(frameURL, request.getWindowID());
		if(isAjaxMode()){
			/**
			 *  ajax̬���ػ�Portlet������������Portlet�п��SRC�Ľű�
			 */
			String pagecard = request.getParameter("pagecard");
			String title = getRequestPageTitle(request);
			String script = createChangeFrameSrcScript(title, pagecard, frameURL, buildFrameId(windowID));
			addExecScript(response, script);
		}else{
			String frameHeight = getValue(request, HEIGHT_PARAM, "0");
			boolean needScroll = frameURL.startsWith("http://") || frameURL.startsWith("https://")||urlScroll;
			String scrollVal = needScroll ? "yes" : "no";
			String scroll = getValue(request, ALLOW_SCROLL, scrollVal);
			String srcType = StringUtils.defaultIfEmpty(request.getParameter(SRC_TYPE), srcVal);
			initFrameAttr(request, frameURL, frameHeight, scroll, srcType);
			include(getFramePage(), request, response);
		}
		doPortletLog(windowID, frameURL);
	}

	/**
	 * ��ȡ�����ҳ�����
	 * @param request
	 * @return
	 */
	public String getRequestPageTitle(RenderRequest request) {
		String title = request.getParameter("title");
		if(title != null){
			try {
				title = StringUtil.convertToCorrectEncoding(title);
				title = URLDecoder.decode(title,"UTF-8");
				String[] titles = title.split(",,,");
				title = titles[titles.length -1];
			} catch (Exception e) {
			}
		}
		return title;
	}

	/**
	 * ��ʼ����ܲ���
	 * @param request
	 * @param frameURL
	 * @param frameHeight
	 * @param scroll
	 * @param srcType
	 */
	public void initFrameAttr(RenderRequest request, String frameURL,
			String frameHeight, String scroll, String srcType) {
		
		setFrameURL(request, frameURL);
		setFrameHeight(request, frameHeight);
		setFrameScroll(request, scroll);
		setFrameSrcType(request, srcType);
	}
	/**
	 * ���ÿ��SRC����
	 * if_src_type="src" ʱ�˿�ܻ��������أ�Ĭ���ǵȴ�ҳ��׼����Ϻ���أ��ܿ�IE���߳����⡣
	 * @param request
	 * @param srcType
	 */
	public void setFrameSrcType(RenderRequest request, String srcType) {
		request.setAttribute(SRC_TYPE, srcType);
	}

	
	/**
	 * ��¼Portlet��־
	 * @param portletName
	 * @param frameURL
	 */
	protected void doPortletLog(String portletName, String frameURL) {
		if(frameURL != null && frameURL.startsWith("http://")){
			String buttonname = LfwResBundle.getInstance().getStrByID("pserver", "BaseIframePortlet-000000")/*Portlet ��*/ + portletName;
			PortalOperatorLogHelper.doPortletLog(frameURL, buttonname);
		}
	}
	/**
	 * ���ÿ�ܹ���
	 * @param request
	 * @param scroll
	 */
	public void setFrameScroll(RenderRequest request, String scroll) {
		request.setAttribute(ALLOW_SCROLL, scroll);
	}
	/**
	 * ���ÿ�ܸ߶�
	 * @param request
	 * @param frameHeight
	 */
	public void setFrameHeight(RenderRequest request, String frameHeight) {
		request.setAttribute(HEIGHT_PARAM, frameHeight);
	}
	/**
	 * �����޸Ŀ������URL�Ľű�
	 * @param frameURL
	 * @param iframeId
	 * @return
	 */
	protected String createChangeFrameSrcScript(String title,String pagecard, String frameURL, String iframeId) {
		boolean urlScroll = frameURL.indexOf("scroll=1") != -1;
		boolean needScroll = frameURL.startsWith("http://") || frameURL.startsWith("https://") || urlScroll;
		JSONObject jso = new JSONObject();
		jso.put("title", title);
		jso.put("pagecard", pagecard);
		jso.put("frameURL", frameURL);
		jso.put("iframeId", iframeId);
		jso.put("needScroll",needScroll );
		return "setFrameContent(" + jso.toString() + ")";
	}
	/**
	 * ��ȡ����ֵ
	 * (������� > ���� > Ĭ��ֵ)
	 * @param request
	 * @param preference
	 * @param paramName
	 * @param dftVal
	 * @return
	 */
	public String getValue(RenderRequest request, String paramName, String dftVal) {
		String val = request.getPreferences().getValue(paramName, dftVal);
		if (StringUtils.isNotBlank(request.getParameter(paramName)))
			val = request.getParameter(paramName);
		return val;
	}
	/**
	 * ����Portlet����ID����IFrame��ID
	 * @param windowID
	 * @return
	 */
	protected String buildFrameId(String windowID) {
		return windowID.replaceAll("\\.","_") + _IFRAME;
	}

	/**
	 * ����Frame�ĵ�ַ
	 * @param request
	 * @param frameURL
	 */
	protected void setFrameURL(RenderRequest request, String frameURL){
		frameURL=PortletSessionUtil.makeAnchor(frameURL, request.getWindowID());
		request.setAttribute(SRC_PARAM, frameURL);
	}
	
	 
	/**
	 * ��ÿ�ܵ�ID
	 * @param request
	 * @return
	 */
	protected String getIFrameId(PortletRequest request){
		return request.getWindowID() + _IFRAME;
	}
	
	protected String getFramePage(){
		return IFRAME_PORTLET_PAGE;
	}
	/**
	 * Ĭ�ϵ���
	 * @param request
	 * @param response
	 * @throws PortletException
	 * @throws IOException
	 */
	protected void include(RenderRequest request, RenderResponse response) throws PortletException, IOException{
		include(getFramePage(), request, response);
	}

	@Override
	protected void doDesign(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		String frameHeight = getValue(request, HEIGHT_PARAM, "0");
		String scroll = getValue(request, ALLOW_SCROLL, "no");
		String srcType = StringUtils.defaultIfEmpty(request.getParameter(SRC_TYPE), srcVal);
		String frameURL = getValue(request, SRC_PARAM, "about:blank");
		initFrameAttr(request, frameURL, frameHeight, scroll, srcType);
		include(getFramePage(), request, response);
	}
	
}
