package nc.uap.portal.util.freemarker.functions;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.logging.Logger;
import nc.uap.lfw.core.ClientSession;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.WebContext;
import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.importer.SmartImporter;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.IUIMeta;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.serializer.impl.LfwJsonSerializer;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.util.JsURLEncoder;
import nc.uap.lfw.util.StringUtil;
import nc.uap.portal.log.PortalLogger;
import nc.uap.portal.om.Page;
import nc.uap.portal.util.PortalRenderEnv;
import nc.uap.portal.util.freemarker.FreeMarkerTools;
import nc.vo.pub.lang.UFDateTime;


import uap.json.JSONObject;
import uap.json.MarshallException;
import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.portal.page.ImportCollection;
import uap.lfw.portal.page.itf.IPage2GUIService;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * Lfw引入宏
 * @author licza
 *
 */
public class LfwImporter implements TemplateDirectiveModel{
	
	private static final String _TS = "_TS";
	private static final String PK = "pk";
	
	/**
	 * 存放上次请求时page的ts
	 */
	private Map<String, UFDateTime> tsMap = new HashMap<String, UFDateTime>();
	
	/** page的css,js路径缓存*/
	public static final String PAGE_GUI_CACAHE = "PAGE_GUI_CACHE";
	
	/** page的css、js压缩文件缓存*/
	public static final String PAGE_GUI_COMPRESSED_CACAHE = "PAGE_GUI_COMPRESSED_CACHE";
	
	@Override
	public void execute(Environment env, Map arg1, TemplateModel[] arg2,
			TemplateDirectiveBody arg3) throws TemplateException, IOException {
		WebContext wc = LfwRuntimeEnvironment.getWebContext();
		String head = "";
		StringBuffer sb = new StringBuffer("\n");
		if(wc != null){
			PageModel model = new PageModel(){
				@Override
				protected LfwWindow createPageMeta() {
					LfwWindow pm = new LfwWindow();
					pm.setId("portal");
					return pm;
				}
	
				@Override
				public IUIMeta getUIMeta() {
					UIMeta uimeta = new UIMeta();
					uimeta.setJquery(1);
					return uimeta;
				}
				
			};
			model.internalInitialize();
			
			SmartImporter si = new SmartImporter();
			LfwWindow pm = LfwRuntimeEnvironment.getWebContext().getPageMeta();;
			UIMeta uimeta = (UIMeta) model.getUIMeta();
			head = si.genImporters(pm, uimeta , null, LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_PRODUCTION), null);
			sb.append("<script>");
			sb.append("\n");
			sb.append(createPageUI(model));
			sb.append("\n");
			sb.append("</script>");
			sb.append("\n");
		}
		
		/**
		 * 加入页面引用的css,js
		 */
		//1.  获取页面引用的css js
		ImportCollection ic = null;
		
		Page page = PortalRenderEnv.getCurrentPage();
		
		boolean skipCache = false; // 是否跳过缓存
		if(null == page.getExtendAttribute(PK) || null == page.getExtendAttribute(_TS)) {
			skipCache = true;
		}
		
		String pagePK = null;
		UFDateTime pageTS = null;
		String theme = null;
		UFDateTime preTS = null;
		if(skipCache) {
			ic = getImportCollection(page);
		} else {
			pagePK = (String)page.getExtendAttribute(PK).getValue();
			pageTS = (UFDateTime)page.getExtendAttribute(_TS).getValue();
			
			preTS = tsMap.get(pagePK);
			theme = LfwRuntimeEnvironment.getThemeId();
			String cacheKey = pagePK + theme;
			
			// 到缓存中取ic
			if(null != preTS && pageTS.compareTo(preTS) == 0) { 
				ILfwCache cache = LfwCacheManager.getStrongCache(PAGE_GUI_CACAHE, null);
				ic = (ImportCollection)cache.get(cacheKey);
			}
			
			if(null == ic) {
				ic = getImportCollection(page);
				ILfwCache cache = LfwCacheManager.getStrongCache(PAGE_GUI_CACAHE, null);
				cache.put(cacheKey, ic);
				tsMap.put(pagePK, pageTS);
			}
		}
		
		String[] cssPaths = ic.getImportCss();  
		String[] jsPaths = ic.getImportJs();
		
		String[] staticCss = {"css.css", "msgtip.css"};
		String[] staticJs = {"portal.js"};
		boolean isProductMode = LfwRuntimeEnvironment.getMode().equals(WebConstant.MODE_PRODUCTION);
		
		//2. 产品态进行css和js的合并
		if(isProductMode) {
			String path = "tpl/" + LfwRuntimeEnvironment.getThemeId() + "/allcompressed/";
			File pressCss = null;
			File pressJs = null;
			
			if(skipCache) {
				StringBuffer pressCssPath = new StringBuffer();
				StringBuffer pressJsPath = new StringBuffer();
				mergerCssAndJs(cssPaths, jsPaths, path, staticCss, staticJs, pressCssPath, pressJsPath);
				pressCss = new File(pressCssPath.toString());
				pressJs = new File(pressJsPath.toString());
			} else {
				String ck = pagePK + "#" + "css" + "#" + theme;
				String jk = pagePK + "#" + "js" + "#" + theme;
				ILfwCache cache = LfwCacheManager.getStrongCache(PAGE_GUI_COMPRESSED_CACAHE, null);
				// 到缓存中取pressCss,pressJs
				if(null != preTS && !pageTS.after(preTS)) {
					pressCss = (File)cache.get(ck);
					pressJs = (File)cache.get(jk);
				} 
				if(null == pressCss || null == pressJs || !pressCss.exists() || !pressJs.exists()) {
					StringBuffer pressCssPath = new StringBuffer();
					StringBuffer pressJsPath = new StringBuffer();
					mergerCssAndJs(cssPaths, jsPaths, path, staticCss, staticJs, pressCssPath, pressJsPath);
					pressCss = new File(pressCssPath.toString());
					pressJs = new File(pressJsPath.toString());
					cache.put(ck, pressCss);
					cache.put(jk, pressJs);
				}
			}
			
			sb.append("<link href='/portal/"  + path + pressCss.getName() +"' rel='stylesheet' type='text/css'></link>");
			sb.append("\n<script src='/portal/"  + path + pressJs.getName() +"'></script>");
		} else {// 开发态不合并
			for(int i=0;i < cssPaths.length;i++) {
				sb.append("<link href='/portal/tpl/" + cssPaths[i] +"' rel='stylesheet' type='text/css'></link>");
			}
			for(int i=0; i < staticCss.length;i++) {
				sb.append("<link href='/portal/" + staticCss[i] +"' rel='stylesheet' type='text/css'></link>");
			}
			for(int i=0;i < jsPaths.length;i++) {
				sb.append("\n<script src='/portal/tpl/"  + jsPaths[i] + "'></script>");
			}
			for(int i=0; i < staticJs.length;i++) {
				sb.append("\n<script src='/portal/js/"  + staticJs[i] + "'></script>");
			}
		}
//		if(PortalRenderEnv.isDesignMode())
//			sb.append("<script src='/portal/js/editable.js'></script>");
		env.getOut().write(head + sb.toString());
	}
	
	/**
	 * 合并css和js
	 * @param cssPaths
	 * @param jsPaths
	 * @param path
	 * @param staticCss
	 * @param staticJs
	 * @param pressCss
	 * @param pressJs
	 */
	private void mergerCssAndJs(String[] cssPaths, String[] jsPaths, String path,
			String[] staticCss, String[] staticJs, StringBuffer pressCssPath, StringBuffer pressJsPath) {
		File compressedDir = ContextResourceUtil.getFile(path);
		if(!compressedDir.exists()) {
			compressedDir.mkdir();
		}
		
		String tplHome = FreeMarkerTools.getTplHome();
		for(int i=0;i < cssPaths.length;i++) {
			cssPaths[i] = tplHome + cssPaths[i].substring(0, cssPaths[i].indexOf("/")) + "/allcompressed" + cssPaths[i].substring(cssPaths[i].indexOf("/"));
		}
		
		for(int i=0;i < jsPaths.length;i++) {
			jsPaths[i] = tplHome + jsPaths[i].substring(0, jsPaths[i].indexOf("/")) + "/allcompressed" + jsPaths[i].substring(jsPaths[i].indexOf("/"));
		}
		
		for(int i=0; i < staticCss.length;i++) {
			staticCss[i] = ContextResourceUtil.getAbsPath(path + staticCss[i]);
		}
		String[] csss = mergeArray(cssPaths, staticCss);
		for(int i=0; i < staticJs.length;i++) {
			staticJs[i] = ContextResourceUtil.getAbsPath(path + staticJs[i]);
		}
		String[] jss = mergeArray(jsPaths, staticJs);
		
		IPage2GUIService page2GUIService = ServiceLocator.getService(IPage2GUIService.class);
		try {
			pressCssPath.append(page2GUIService.merger(compressedDir.getAbsolutePath(), csss, "css").getAbsolutePath());
			pressJsPath.append(page2GUIService.merger(compressedDir.getAbsolutePath(), jss, "js").getAbsolutePath());
		} catch (IOException e) {
			PortalLogger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 获取Page引用的css和js
	 * @param page
	 * @return
	 */
	private ImportCollection getImportCollection(Page page) {
		IPage2GUIService page2GUIService = ServiceLocator.getService(IPage2GUIService.class);
		return page2GUIService.getImporters(page, LfwRuntimeEnvironment.getThemeId());
	}
	
	private String[] mergeArray(String[] cssPaths, String[] strings) {
		List<String> paths = new ArrayList<String>();
		paths.addAll(Arrays.asList(cssPaths));
		paths.addAll(Arrays.asList(strings));
		Object[] objs = paths.toArray();
		String[] ret = new String[objs.length];
		for(int i=0;i < objs.length;i++) {
			ret[i] = (String)objs[i];
		}
		return ret;
	}

	private String createPageUI(PageModel model) {
		String CS_PRE = "$cs_";
		StringBuffer scriptBuf = new StringBuffer();
		LfwWindow pm = LfwRuntimeEnvironment.getWebContext().getPageMeta();
		String caption = pm.getCaption();
		if(caption == null)
			caption = "";
		scriptBuf.append("window.pageUI = new PageUI('")
		   .append(pm.getId())
		   .append("','")
		   .append(pm.getCaption())
		   .append("');\n");

		scriptBuf.append("getApplication().addPageUI('")
		   .append(pm.getId())
		   .append("', window.pageUI);\n");
		try {
			Iterator it = LfwRuntimeEnvironment.getWebContext().getRequest().getParameterMap().entrySet().iterator();
			
			//将所有请求参数植入全局请求参数Map
			scriptBuf.append("window.$paramsMap = new HashMap();\n");
			
			while(it.hasNext())
			{
				Entry entry = (Entry) it.next();
				scriptBuf.append("setParameter(")
				         .append(JSONObject.quote((String)entry.getKey()))
				         .append(",");
				         
				         scriptBuf.append(JSONObject.quote(((String[])entry.getValue())[0])  ) 
				         .append(");\n");
			}
		} catch (Exception e) {
			LfwLogger.error(e);
		}
		
		
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
		}
		
		return scriptBuf.toString();
	}
}
