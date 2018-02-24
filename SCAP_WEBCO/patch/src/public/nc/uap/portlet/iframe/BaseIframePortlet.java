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
 * IframePortlet基类
 * 
 * @author licza
 * @since 2010-8-10
 */
public class BaseIframePortlet extends PtBasePortlet {
	private static final String _IFRAME = "_iframe";
	/** 默认的Iframe页面 **/
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
	 * 获得FrameURL
	 * 
	 * @return
	 */
	protected String getDefaultFrameURL() {
		return "/";
	}

	/**
	 * IframePortlet的默认实现
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
			 *  ajax态不重画Portlet，仅返回设置Portlet中框架SRC的脚本
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
	 * 获取请求的页面标题
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
	 * 初始化框架参数
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
	 * 设置框架SRC类型
	 * if_src_type="src" 时此框架会立即加载，默认是等待页面准备完毕后加载，避开IE多线程问题。
	 * @param request
	 * @param srcType
	 */
	public void setFrameSrcType(RenderRequest request, String srcType) {
		request.setAttribute(SRC_TYPE, srcType);
	}

	
	/**
	 * 记录Portlet日志
	 * @param portletName
	 * @param frameURL
	 */
	protected void doPortletLog(String portletName, String frameURL) {
		if(frameURL != null && frameURL.startsWith("http://")){
			String buttonname = LfwResBundle.getInstance().getStrByID("pserver", "BaseIframePortlet-000000")/*Portlet ：*/ + portletName;
			PortalOperatorLogHelper.doPortletLog(frameURL, buttonname);
		}
	}
	/**
	 * 设置框架滚动
	 * @param request
	 * @param scroll
	 */
	public void setFrameScroll(RenderRequest request, String scroll) {
		request.setAttribute(ALLOW_SCROLL, scroll);
	}
	/**
	 * 设置框架高度
	 * @param request
	 * @param frameHeight
	 */
	public void setFrameHeight(RenderRequest request, String frameHeight) {
		request.setAttribute(HEIGHT_PARAM, frameHeight);
	}
	/**
	 * 生成修改框架内容URL的脚本
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
	 * 获取参数值
	 * (传入参数 > 配置 > 默认值)
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
	 * 根据Portlet窗口ID生成IFrame的ID
	 * @param windowID
	 * @return
	 */
	protected String buildFrameId(String windowID) {
		return windowID.replaceAll("\\.","_") + _IFRAME;
	}

	/**
	 * 设置Frame的地址
	 * @param request
	 * @param frameURL
	 */
	protected void setFrameURL(RenderRequest request, String frameURL){
		frameURL=PortletSessionUtil.makeAnchor(frameURL, request.getWindowID());
		request.setAttribute(SRC_PARAM, frameURL);
	}
	
	 
	/**
	 * 获得框架的ID
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
	 * 默认导入
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
