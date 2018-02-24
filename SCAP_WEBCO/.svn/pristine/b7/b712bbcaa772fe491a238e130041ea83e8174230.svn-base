package nc.uap.lfw.core.tags;


import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.ClientSession;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.base.ExtAttribute;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.ModePhase;
import nc.uap.lfw.core.event.conf.EventSubmitRule;
import nc.uap.lfw.core.event.ctx.LfwPageContext;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.model.util.LfwMaskerUtil;
import nc.uap.lfw.core.page.IPlugoutDesc;
import nc.uap.lfw.core.page.IUIMeta;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.PlugoutDesc;
import nc.uap.lfw.core.page.PlugoutProxy;
import nc.uap.lfw.core.serializer.impl.LfwJsonSerializer;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.ra.itf.IDynamicAttributes;
import nc.uap.lfw.ra.render.JsClientUIRender;
import nc.uap.lfw.util.JsURLEncoder;
import nc.uap.lfw.util.LanguageUtil;
import nc.uap.lfw.util.StringUtil;


import uap.json.JSONObject;
import uap.json.MarshallException;
import uap.lfw.core.language.LfwLanguageVO;
import uap.lfw.core.ml.LfwResBundle;

/**
 * PageModel对应Tag.此Tag将初始化PageModel,写入页面必要片段.并将PageModel存放在PageContext以供
 * 后续控件调用。
 * 注意：所有其它Lfw对应Tag必须包含在此Tag内，以取得对应的上下文
 * 
 * @author dengjt
 * 2007-1-25 
 */ 
public class PageBodyTag extends WebElementTag {
//	private String modelId = null; 
//	private String className = null;  
	private boolean showLoadingPage = false;
	private boolean simple = false;
	protected static final String DS_SCRIPT = "dsScript";
	protected static final String ALL_SCRIPT = "allScript";
	
	protected void doRender() throws JspException, IOException {
		PageModel model = (PageModel) getJspContext().getAttribute("pageModel");
		try {
			long startTime = 0;
			if(Logger.isInfoEnabled())
				startTime = System.currentTimeMillis();
			
//			if(showLoadingPage){
//				getJspContext().getOut().write(addLoadingPage());
//				//getJspContext().getOut().flush();
//			}
			
			
			//获得此页面的PageMeta
			LfwWindow pageMeta = model.getPageMeta();
			//如果是简单模式，不进行脚本渲染
			if(simple)
				return;
			
			
			//在用户session中设置一个放置buf的map，此map根据pageId区分buf
//			synchronized(PageModelTag.class) {
//				if((bodyScriptMap = (ConcurrentMap<String, StringBuffer>) ((PageContext)this.getJspContext()).getSession().getAttribute("bodyScriptMap")) == null){
//					bodyScriptMap = new ConcurrentHashMap<String, StringBuffer>();
//					((PageContext)getJspContext()).getSession().setAttribute(BODY_SCRIPT_MAP, bodyScriptMap);
//				}
//			}
			//此Buf中的内容，将能够独立形成脚本文件
			StringBuffer scriptBuf = new StringBuffer(4096);
			getPageContext().setAttribute(ALL_SCRIPT, scriptBuf);
			//将此buf放在页面上下文中
//			bodyScriptMap.put(model.getPageModelId(), scriptBuf);
			
			//控件绑定ds的逻辑统一放在此处。最终合并到scriptBuf中，之所以这么做，是因为tree等画节点的时候需要完整的width信息。而只有
			//所有控件都渲染完成，这种信息才能由浏览器补充完整。所以将设置数据放在最后进行。注意：所有缓画的控件，例如TabItem和CardLayout等
			//都可能需要自己处理这种缓画策略
			StringBuffer dsScriptBuf = new StringBuffer();
//			getJspContext().setAttribute(DS_SCRIPT, dsScriptBuf);
			
			
			//页面初始化函数,所有js逻辑包含其中
			scriptBuf.append("function pageBodyScript(){\n");
			
			//加入masker信息
			LfwMaskerUtil.makeMaskerScript(scriptBuf);
			
			PageContext pageContext = (PageContext) getJspContext();
			Iterator it = pageContext.getRequest().getParameterMap().entrySet().iterator();
			
			scriptBuf.append(createPageUI());
			scriptBuf.append("showDefaultLoadingBar();\n");
			//将所有请求参数植入全局请求参数Map
			scriptBuf.append("window.$paramsMap = new HashMap();\n");
			LfwLanguageVO dftLangVO = LanguageUtil.getEnableLangVOs()[0];
			int defautlLanguage = dftLangVO==null?0:dftLangVO.getLangseq();
			scriptBuf.append("window.pageUI.defaultLanguageCode = " + defautlLanguage + ";\n");
			int currentLanguageCode = LanguageUtil.getCurrentLangSeq();
			scriptBuf.append("window.pageUI.currentLanguageCode = " + currentLanguageCode + ";\n");
			JSONObject paramjson = null;		
			while (it.hasNext()) {
				if (paramjson == null)
					paramjson = new JSONObject();
				Entry entry = (Entry) it.next();
				paramjson.put("key", (String)(entry.getKey()));
				paramjson.put("value", ((String[]) entry.getValue())[0]);
				String paramStr = paramjson.toString();
				paramStr = paramStr.replaceAll("[<]", "\\\\<");
				paramStr = paramStr.replaceAll("[>]", "\\\\>");
				scriptBuf.append("setParameter(").append(paramStr).append(");\n");
				
//				String key = StringUtil.convertToCorrectEncoding((String) entry.getKey());
//				if(key != null)
//					key = key.replaceAll("<(.|\n)+?>", "");
//				
//				//替换双引号，编辑公式处公式内有双引号
//				key = key.replaceAll("\"", "\\\\\"");  				
//				scriptBuf.append("setParameter(\"").append(key).append("\",\"");
//				String str = StringUtil.convertToCorrectEncoding(((String[]) entry.getValue())[0]);
//				scriptBuf.append(JsURLEncoder.encode(str, "UTF-8")).append("\");\n");
			}
			
			//render client attribute
			renderClientSessionAttribute(model, scriptBuf);
			
			if(pageMeta.getCaption() != null)
				scriptBuf.append("document.title = \"")
						 .append(pageMeta.getCaption())
						 .append("\";\n");
			
			Map<String, ExtAttribute> attrMap = pageMeta.getExtendMap();
			if(attrMap != null && !attrMap.isEmpty())
			{
				Iterator<String> attrIt = attrMap.keySet().iterator();
				while(attrIt.hasNext())
				{
					String attrName = attrIt.next();
					scriptBuf.append("pageUI.addAttribute('" + attrName + "', '" + attrMap.get(attrName) + "');\n");
				}
			}
			
			
			prepareRender(model.getUIMeta(), model.getPageMeta(), dsScriptBuf);
			if(getJspBody() != null)
				getJspBody().invoke(getJspContext().getOut());

			//将dsScript的设置脚本加入页面脚本中
			scriptBuf.append(dsScriptBuf.toString());
			
			scriptBuf.append("if(typeof(externalInit) != 'undefined'){\n externalInit();\n}\n");
			scriptBuf.append("pageUI.renderDone = true;\n");
			scriptBuf.append("pageUI.$afterPageInit();\n");
			
			// 添加ClientEnvironment
			
			scriptBuf.append("pageUI.$beforeInitData();\n")
					 .append("pageUI.$beforeActive();\n");
			
			
//			scriptBuf.append("layoutInitFunc();\n");
			//解决ie8下面，弹出的参数窗口出现纵向滚动条问题
			scriptBuf.append("if (IS_IE8){ setTimeout(function(){layoutInitFunc();},50);} else {layoutInitFunc();}\n");
			
			
			scriptBuf.append("window.renderDone = true;\n");
			scriptBuf.append("if(window.editMode){document.body.className='unselectable'}\n");
			scriptBuf.append("hideDefaultLoadingBar();\n");
			scriptBuf.append("\n");
			scriptBuf.append("}\n");
			
			addDynamicScript(scriptBuf);
			
			
			if(Logger.isInfoEnabled()){
				long endTime = System.currentTimeMillis();
				Logger.info(LfwResBundle.getInstance().getStrByID("ra", "PageBodyTag-000000")/*初始化PageMode所用时间:*/ + (endTime - startTime) + "ms");
			}
		} 
		catch (Exception e) {
			LfwLogger.error(e.getMessage(), e);
			if(model != null)
				model.destroy();
			if(e instanceof LfwRuntimeException)
				throw (LfwRuntimeException)e;
			throw new LfwRuntimeException(e);
		}
	}
	

	private void prepareRender(IUIMeta meta, LfwWindow pagemeta, StringBuffer scriptBuf) throws IOException {
//		String editModeStr = (String) EnvProvider.getInstance().getWebContextAttribute(WebConstant.EDIT_MODE_KEY);
//		boolean editMode = editModeStr == null ? false : editModeStr.equals(WebConstant.MODE_PERSONALIZATION);
		boolean editMode = false;
		if (LfwRuntimeEnvironment.getModePhase() != ModePhase.dft){
			editMode = true;
		}
		
		JspWriter out = getPageContext().getOut();
//		IUIRenderGroup group = RenderKit.getUIRenderGroup(RenderKit.RENDER_PC);
		JsClientUIRender render = (JsClientUIRender) ControlFramework.getInstance().getUIRender((UIMeta) meta, (UIElement) meta, pagemeta, null);
		if(render != null){
			((IDynamicAttributes)render).setContextAttribute(JsClientUIRender.DS_SCRIPT, scriptBuf);
//			this.initWebContext((IDynamicAttributes)render, getJspContext());
			String html = render.createRenderHtml();
			out.write(html);
			
//			StringWriter script = new StringWriter();
			String script = render.createRenderScript();
			
			StringBuffer buf = new StringBuffer();
			IPlugoutDesc[] pluginProxys = pagemeta.getPlugoutDescs();
			for (IPlugoutDesc proxy : pluginProxys){
				if(proxy instanceof PlugoutProxy)
					continue;
				PlugoutDesc plugOut = (PlugoutDesc) proxy;
				String id = plugOut.getId();
				buf.append("var ").append(id).append(" = new PlugOut('").append(id).append("');\n");
				EventSubmitRule sr = plugOut.getSubmitRule();
				if (sr != null){
					String submitRuleId = "sr";
					String srScript =   JsClientUIRender.generateSubmitRuleScript(sr, submitRuleId);
					buf.append(srScript);
					buf.append("").append(id).append(".submitRule = ").append(submitRuleId).append(";\n");
				}
				buf.append("pageUI.addPlugOut(").append(id).append(");\n");
			}
			script += buf.toString();
			
			
			addToBodyScript(script);
		}
	}


	private void addDynamicScript(StringBuffer scriptBuf) throws IOException {
//		if (!EnvProvider.getInstance().getMode().equals(WebConstant.MODE_PRODUCTION)){
//			System.out.println(scriptBuf.toString());
//		}
		String script = "<script>\n" + scriptBuf.toString() + "</script>";
		getJspContext().getOut().write(script);
	}

	private String createPageUI() {
		StringBuffer buf = new StringBuffer();
		LfwWindow pm = getPageModel().getPageMeta();
		String caption = pm.getCaption();
		if(caption == null)
			caption = "";
		buf.append("window.pageUI = new PageUI('")
		   .append(pm.getId())
		   .append("','")
		   .append(pm.getCaption())
		   .append("');\n");

		buf.append("getApplication().addPageUI('")
		   .append(pm.getId())
		   .append("', window.pageUI);\n");
		
		String eventStr = addEventSupport(pm, null, "pageUI", null);
		buf.append(eventStr);
		return buf.toString();
	}

	
	private void renderClientSessionAttribute(PageModel model, StringBuffer scriptBuf) {
		try {
			ClientSession cs = model.getClientSession();
			if(cs != null) {
				Map<String, Serializable> map = cs.getAttributeMap();
				if(map != null && map.size() > 0) {
					LfwJsonSerializer jsonSerialzer = LfwJsonSerializer.getInstance();
					String jsonStr = jsonSerialzer.toJSON(map);
					scriptBuf.append(CS_PRE)
					         .append("clientSession = ")
					         .append(jsonStr)
					         .append(";\n");
				}
				Map<String, Serializable> stickMap = cs.getStickAttributeMap();
				if(stickMap != null && stickMap.size() > 0){
					Iterator<Entry<String, Serializable>> it = stickMap.entrySet().iterator();
					scriptBuf.append(CS_PRE)
							 .append("clientStickKeys = '");
					while(it.hasNext()){
						Entry<String, Serializable> entry = it.next();
						if(entry.getValue() == null){
							Logger.warn("client session value is null, key is:" + entry.getKey());
							continue;
						}
						scriptBuf.append(entry.getKey())
								 .append("=")
								 .append(JsURLEncoder.encode(entry.getValue().toString(), "UTF-8"));
						if(it.hasNext())
							scriptBuf.append("&");
					}
					scriptBuf.append("';\n");
				}
			}
		} 
		catch (MarshallException e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(LfwResBundle.getInstance().getStrByID("lfw", "PageModelTag-000000")/*初始化客户端记录对象出现问题:*/ + e.getMessage());
		}
	}
//	
//	public void addJsonSupport(StringBuffer scriptBuf, HttpServletRequest request, PageMeta pageMeta) {
//		//boolean isHeadAdded = false;
//		//将服务按照注册名暴露为客户端服务，客户端只需调用 getService("服务名")即可获得对应服务
//		Map<String, Object> serviceMap = new HashMap<String, Object>();
//		if(pageMeta.getServiceMap().size() > 0){
//			Iterator<Entry<String, String>> it = pageMeta.getServiceMap().entrySet().iterator();
//			while(it.hasNext()){
//				Entry<String, String> entry = it.next();
//				serviceMap.put(entry.getKey(), LfwClassUtil.newInstance(entry.getValue()));
//			}
//		}
//		
//		String clazz = pageMeta.getEditFormularClazz();
//		//注册公式服务
//		if(clazz != null && !clazz.equals(""))
//			serviceMap.put("formular_service", LfwClassUtil.newInstance(clazz));
//		
//		if(serviceMap.size() > 0){
//			addJsonHead(scriptBuf);
//			JsonHelper.registerService(request, modelId, serviceMap);
//		}
//	}
//	
//	private void addJsonHead(StringBuffer scriptBuf) {
//		scriptBuf.append("$jsonrpc = new JSONRpcClient(window.corePath + \"/json?pageId=")
//			.append(modelId)
//			.append("\");\n");
//		scriptBuf.append("log(\"rpc service was successfully initialized\");\n");
//	}
	
	
	
//	private String addLoadingPage() {
//		StringBuffer buf = new StringBuffer();
//		buf.append("<table id=\"$lfw_loadingPage\" width=\"100%\" height=\"100%\" border=\"0\" style=\"background:#FFFFFF;position:absolute;z-index:10000\">\n<tr>\n<td align=\"center\"><img src=\"/lfw/frame/themes/ncclassic/images/common/loading.gif\"/></td></tr></table>");
//		return buf.toString();
//	}
//
//	protected DatasetRelations getDatasetRelations()
//	{
//		return getPageModel().getDsRelations();
//	}

//
//	@Override
//	protected Map<String, String[]> getEventParamMap() {
//		return eventParamMap;
//	}


	public boolean isShowLoadingPage() {
		return showLoadingPage;
	}


	public void setShowLoadingPage(boolean showLoadingPage) {
		this.showLoadingPage = showLoadingPage;
	}

	@Override
	protected String getSourceType(WebElement ele) {
		return LfwPageContext.SOURCE_TYPE_PAGEMETA;
	}


	public boolean isSimple() {
		return simple;
	}


	public void setSimple(boolean simple) {
		this.simple = simple;
	}

}
