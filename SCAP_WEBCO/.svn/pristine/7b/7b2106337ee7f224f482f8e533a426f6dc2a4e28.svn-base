package nc.uap.lfw.core.importer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

import javax.servlet.ServletRequest;

import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.AbstractPresentPlugin;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.LfwTheme;
import nc.uap.lfw.core.LfwThemeManager;
import nc.uap.lfw.core.cache.ILfwCache;
import nc.uap.lfw.core.cache.LfwCacheManager;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.common.EditorTypeConst;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridColumnGroup;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.ctrlfrm.ControlFramework;
import nc.uap.lfw.core.ctrlfrm.DevicePhase;
import nc.uap.lfw.core.ctrlfrm.ModePhase;
import nc.uap.lfw.core.ctrlfrm.plugin.IControlPlugin;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.DatasetRelations;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.page.ViewModels;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.RefNodeRelation;
import nc.uap.lfw.core.refnode.RefNodeRelations;
import nc.uap.lfw.core.util.LFWAllComponetsFetcher;
import nc.uap.lfw.core.util.LangResoTranf;
import nc.uap.lfw.jsp.uimeta.UIControl;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIFormComp;
import nc.uap.lfw.jsp.uimeta.UIFormElement;
import nc.uap.lfw.jsp.uimeta.UIGridComp;
import nc.uap.lfw.jsp.uimeta.UILayout;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import nc.uap.lfw.jsp.uimeta.UITextField;
import nc.uap.lfw.jsp.uimeta.UIView;
import nc.uap.lfw.login.vo.LfwSessionBean;
import nc.uap.lfw.ra.itf.ITemplateParserService;
import nc.uap.lfw.util.HashIdUtil;
import nc.uap.lfw.util.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.core.ml.LfwResBundle;
import uap.lfw.sign.SignInfo;
import uap.lfw.sign.itf.ISignService;


/**
 * 智能依赖引入工具类
 * @author dengjt
 *
 */
public final class SmartImporter {
	
//	private static final String IMPORT_KEY = "SMART_IMPORT_KEY";
	private static final String IMPORT_PRESS_KEY = "IMPORT_PRESS_KEY";
	/**
	 * 所有lib的缓存字符串,非优化
	 */
	private String libArrStr;
	/**
	 * 所有lib的缓存字符串,优化
	 */
	private String optimizedLibArrStr;
	
	private ArrayList<String> includeJsSet = new ArrayList<String>();//修改为ArrayList增加排序
	private ArrayList<String> includeCssSet = new ArrayList<String>();
	private ArrayList<String> includeIdSet = new ArrayList<String>();
	private StringBuffer viewLfwScriptBuf;
	private StringBuffer viewLfwCssBuf;
	private LfwWindow pm;
	//页面中是否存在印章元素
	private Boolean havSign = false;
//	@SuppressWarnings("unchecked")
	public String genImporters(LfwWindow pm, UIMeta um, UIView targetWidget, boolean optimized, String etag){
		this.pm = pm;
//		Map<String, String> impStrMap = null;
//		String cacheKey = null;
		boolean clientMode = LfwRuntimeEnvironment.isClientMode();
		boolean editMode = LfwRuntimeEnvironment.isEditMode();
		boolean wEditMode = LfwRuntimeEnvironment.isWindowEditorMode();
		this.havSign = false;
//		String langCode = LfwRuntimeEnvironment.getLangCode();
//		if(etag != null){
//			ILfwCache cache = LfwCacheManager.getStrongCache(WebConstant.LFW_CORE_CACHE, null);
//			impStrMap = (Map<String, String>) cache.get(IMPORT_KEY);
//			if(impStrMap == null){
//				impStrMap = new HashMap<String, String>();
//				cache.put(IMPORT_KEY, impStrMap);
//			}
//			cacheKey = etag + optimized + editMode + wEditMode + clientMode;// + themeId + langCode;
////			//有的window会同时用在多个app中，此时要缓存成不同的pm
////			if(AppLifeCycleContext.current() != null){
////				String appId = (String) LfwRuntimeEnvironment.getWebContext().getAppSession().getAttribute("appId");
////				cacheKey += appId; 
////			}	
//			String cacheStr = impStrMap.get(cacheKey);
//			if(cacheStr != null)
//				return cacheStr;
//		}
		
		StringBuffer scriptBuf = new StringBuffer();
		StringBuffer cssBuf = new StringBuffer();
		StringBuffer headBuf = new StringBuffer();
		genCommonHeader(headBuf, scriptBuf, cssBuf, editMode, wEditMode, clientMode);
		
		//加载多语资源
		genLangRes(scriptBuf);
		
		String themeId = LfwRuntimeEnvironment.getThemeId();
		if(!optimized)
			genCommonNotOptimizedImporter(pm, um, targetWidget, scriptBuf, cssBuf, clientMode, themeId);
		else
			genCommonOptimizedImporter(pm, um, targetWidget, scriptBuf, cssBuf, clientMode, themeId);
		
		String result = headBuf.toString() + cssBuf.toString() + scriptBuf.toString();
		
//		if(cacheKey != null)
//			impStrMap.put(cacheKey, result);
		return result;
	}
	
	public List<String> getIdList(LfwWindow window, UIElement ele){
		this.pm = window;
		LinkedHashSet<Importer> importList = new LinkedHashSet<Importer>();
		LinkedHashSet<Importer> cssImportList = new LinkedHashSet<Importer>();
		List<String> idList = new ArrayList<String>();
		if(ele instanceof UIView){
			UIView widget = (UIView) ele;
			getWidgetImportList(importList, cssImportList, idList, widget);
		}
		else if(ele instanceof UILayout){
			getLayoutImportList(importList, cssImportList, idList, (UILayout) ele);
		}
		else if(ele instanceof UIControl){
			doGetUIEleImportList(importList, cssImportList, idList, ele);
		}
		return idList;
	}
	
	/**
	 * 加载多语资源
	 * @param scriptBuf
	 */
	private void genLangRes(StringBuffer scriptBuf) {
		String pageId = LfwRuntimeEnvironment.getWebContext().getPageId();
		if(pageId == null || pageId.equals(""))
			return;
		String pagePath = LFWAllComponetsFetcher.getWindowPath(pageId);
		if(pagePath != null){
			String langResoFilePath = pagePath + "/langres.properties";
			Properties langResources = AbstractPresentPlugin.loadNodeLangResources(langResoFilePath);
			if(langResources != null){
				String transStr = LangResoTranf.tranf(langResources);
				String langCode = LfwRuntimeEnvironment.getLangCode();
				String relativePath = WebConstant.TEMP_JSSELFDEFINE_PATH + pageId + "/" + langCode;
				//将资源写到临时文件夹里
				writeToTmpFile(transStr, relativePath);
				
				String langResPath = LfwRuntimeEnvironment.getRootPath() + relativePath + WebConstant.TEMP_JSSELFDEFINE_FILE;
				scriptBuf.append("<script type='text/javascript' src='" + langResPath + "'></script>\n");
			}
		}
	}
	
	private void writeToTmpFile(String transStr, String relativePath) {
		//将js多语资源生成在webtemp目录下
		String importDirectory =  ContextResourceUtil.getCurrentAppPath()+ relativePath;
		File directory = new File(importDirectory);
		if (!directory.exists()) {
			directory.mkdirs();
		}
				
		String importJsFilePath = importDirectory + "/" + WebConstant.TEMP_JSSELFDEFINE_FILE;
		try {
			File file = new File(importJsFilePath);
			file.createNewFile();
			FileUtils.writeStringToFile(file, transStr, "UTF-8");
		} 
		catch (IOException e) {
			LfwLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		}
	}
	
	private void genCommonOptimizedImporter(LfwWindow pm2, UIMeta um,
			UIView targetWidget, StringBuffer scriptBuf, StringBuffer cssBuf,
			boolean clientMode, String themeId) {
		//控件未压缩完成
		if (CompressUtil.ISCOMPRESSING){
			throw new LfwRuntimeException("control is compressing...");
		}
		
		LinkedHashSet<Importer> importList = new LinkedHashSet<Importer>();
		LinkedHashSet<Importer> cssImportList = new LinkedHashSet<Importer>();
		LinkedHashSet<Importer> beforeImportList = new LinkedHashSet<Importer>(1);
		LinkedHashSet<Importer> cssbeforeImportList = new LinkedHashSet<Importer>(1);
//		Set<String> idSet = new HashSet<String>();
		List<String> idList = new ArrayList<String>();
//		String editModeStr = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(WebConstant.EDIT_MODE_KEY);
//		boolean editMode = editModeStr == null ? false : editModeStr.equals(WebConstant.MODE_PERSONALIZATION);
		boolean editMode = false;
		if (LfwRuntimeEnvironment.getModePhase() != ModePhase.dft){
			editMode = true;
		}
		loadLib("jquery", beforeImportList, cssbeforeImportList, idList);
		loadLib("scrollbar", beforeImportList, cssbeforeImportList, idList);
		idList.clear();
		if(editMode)
			loadLib("coreedit", importList, cssImportList, idList);
		else{
			loadLib("core", importList, cssImportList, idList);
			loadLib("layout", importList, cssImportList, idList);
		}	
		loadLib("app", importList, cssImportList, idList);
		loadLib("widget", importList, cssImportList, idList);
		loadLib("modaldialog", importList, cssImportList, idList);
		loadLib("contextmenu", importList, cssImportList, idList);
		
		// 取um下所有view
		List<UIView> UIViewList = new ArrayList<UIView>();
		if (um != null) {
			UIViewList = um.findAllUIView();
		}

		Integer isReference = null;
		if (um == null)
			isReference = 0;
		else {
			isReference = um.isReference();
			// 如果window不加载
			if (isReference == null || isReference.intValue() != 1) {
				// 遍历所有子项view判断是否需要加载
				if (UIViewList != null) {
					int i = 0;
					int j = UIViewList.size();
					while (i < j) {
						UIMeta uimeta = UIViewList.get(i).getUimeta();
					      if(uimeta == null){
					       i++;       
					       continue;
					      }
						isReference = uimeta.isReference();
						if (isReference != null && isReference.intValue() == 1) {
							break;
						}
						i++;
					}
				}
			}
		}
		if (um != null && isReference != null && isReference.intValue() == 1) {
			loadLib("reflib", importList, cssImportList, idList);
		}
		
		getImportList(importList, cssImportList, idList, um, pm, targetWidget);
		if(um != null){
			String includeId = um.getIncludeId();
			if(includeId != null){
				String[] includeIds = includeId.split(",");
				for (int i = 0; i < includeIds.length; i++) {
					loadLib(includeIds[i], importList, cssImportList, idList);
				}
			}
		}
		
		Iterator<String> idIt = includeIdSet.iterator();
		while(idIt.hasNext()){
			loadLib(idIt.next(), importList, cssImportList, idList);
		}
		
		TreeSet<String> ts = new TreeSet<String>(idList);
		StringBuffer imports = new StringBuffer();
		LinkedHashSet<Importer> noCompressImportList = new LinkedHashSet<Importer>(1);
		LinkedHashSet<Importer> noCompressCssImportList = new LinkedHashSet<Importer>(1);
		List<String> noCompressIdList = new ArrayList<String>(1);
		Iterator<String> importIt = ts.iterator();
		while(importIt.hasNext()){
			String importId = importIt.next();
			IControlPlugin cplugin = ControlFramework.getInstance(LfwRuntimeEnvironment.getDevicePhase()).getControlPluginByType(importId);
			String compress = cplugin.getConfig().getCompress();
			if (compress!= null && compress.equals("false")){
				String[] imps = cplugin.getImports(false);
				if(imps != null){
					for (int i = 0; i < imps.length; i++) {
						noCompressImportList.add(new Importer(imps[i], cplugin.getDevice()));
					}
				}
				String[] cssimps = cplugin.getCssImports(false);
				if(cssimps != null){
					for (int i = 0; i < cssimps.length; i++) {
						noCompressCssImportList.add(new Importer(cssimps[i], cplugin.getDevice()));
					}
				}
				idList.remove(importId);
				noCompressIdList.add(importId);
			}
			else
				imports.append(importId);
		}
		String pressName = HashIdUtil.hashStr(imports.toString() + getDeviceName(LfwRuntimeEnvironment.getDevicePhase()));
		String ctx = LfwRuntimeEnvironment.getRootPath();
		ILfwCache cache = LfwCacheManager.getStrongCache(IMPORT_PRESS_KEY + ctx, null);
		
		if (cache.get(pressName) == null){
			String lfwPath = LfwRuntimeEnvironment.getLfwPath();
			String ctxPath = ContextResourceUtil.getCurrentAppPath();
			dynamicPress(pressName, idList, LfwRuntimeEnvironment.getDevicePhase(), lfwPath, ctxPath, cache);
		}
		
		ctx = ctx.substring(1);
		importList.clear();
		for (Importer importer : beforeImportList){
				importList.add(importer);
		}
		importList.add(new Importer("compressed/" + pressName, LfwRuntimeEnvironment.getDevicePhase(), ctx));
		for (Importer importer : noCompressImportList){
			importList.add(importer);
		}
		cssImportList.clear();
		for (Importer importer : cssbeforeImportList){
			cssImportList.add(importer);
		}
		cssImportList.add(new Importer("allcompressed/" + pressName, LfwRuntimeEnvironment.getDevicePhase(), ctx));
		for (Importer importer : noCompressCssImportList){
			cssImportList.add(importer);
		}
		
		String rootPath = LfwRuntimeEnvironment.getRootPath();
		if(clientMode)
			rootPath = ".." + rootPath;
		
		LfwTheme theme = LfwThemeManager.getLfwTheme(rootPath, themeId);
		if(theme == null){
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("bc", "SmartImporter-000000")/*根据themeid找不到对应的theme,*/ + themeId);
		}
		
		Iterator<Importer> cssIt = cssImportList.iterator();
		while(cssIt.hasNext()){
			Importer imp = cssIt.next();
			String fullName = imp.getFullname();
			String currThemePath = null;
			String ctxStr = imp.getCtx();
			currThemePath = "/" + ctxStr  + getFramePathByDevice(imp.getDevice()) + "themes/"+ themeId;
			String str = currThemePath + "/" + fullName;
			cssBuf.append("<link rel='STYLESHEET' type='text/css' href='" + str + ".css'>\n");
		}
	
		Iterator<Importer> it = importList.iterator();
		while(it.hasNext()){
			Importer imp = it.next();
			String ctxStr = imp.getCtx();
			String fullName = imp.getFullname();
			String str = "/" + ctxStr + getFramePathByDevice(imp.getDevice()) + "script/" + fullName;
			scriptBuf.append("<script type='text/javascript' src='" + str + ".js'></script>\n");
		}

		String includeJs = null;
		String includeCss = null;
		String lfwIncludeJs = null;
		String lfwIncludeCss = null;
		String externalJs = null;
		String externalCss = null;
		
		String pageCss = null;
		String pageJS = null;
		if(um != null){
			includeJs = um.getIncludejs();
			includeCss = um.getIncludecss();
			lfwIncludeJs = um.getLfwIncludejs();
			lfwIncludeCss = um.getLfwIncludecss();
			externalJs = um.getExternaljs();
			externalCss = um.getExternalcss();
			pageCss = um.getPagecss();
			pageJS = um.getPagejs();
		}
		
		addExtendJs(includeJs, scriptBuf);
		Iterator<String> jsIt = includeJsSet.iterator();
		while(jsIt.hasNext())
			addExtendJs(jsIt.next(), scriptBuf);
		
		addExtendCss(includeCss, cssBuf);
		Iterator<String> incCssIt = includeCssSet.iterator();
		while(incCssIt.hasNext())
			addExtendCss(incCssIt.next(), cssBuf);
		
		addPageCss(pageCss,scriptBuf);
		addPageJs(pageJS,scriptBuf);
		String cssInclude = includeSelfDefCss("include.css");
		cssBuf.append(cssInclude);
		String include = includeSelfDefScript("include.js");
		scriptBuf.append(include);

		addLfwExtendJs(lfwIncludeJs, scriptBuf);
		addLfwExtendCss(lfwIncludeCss, cssBuf);
		
		addExternalJs(externalJs, scriptBuf);
		addExternalCss(externalCss, scriptBuf);

		if(viewLfwScriptBuf != null)
			scriptBuf.append(viewLfwScriptBuf.toString());
		if(viewLfwCssBuf != null)
			cssBuf.append(viewLfwCssBuf.toString());
		idList.addAll(noCompressIdList);
		scriptBuf.append("<script>\n")
			.append("window.debugMode = false;\n")
			.append("window.loadedLib = ")
			.append(generateLoadedLib(idList))
			.append(";\n")
			.append("window.libArray = ")
			.append(generateAllLibArray(true))
			.append(";\n")
			.append("window.onload = function(){\n")
			.append("")
			.append("pageBodyScript();\n")
			.append("}\n")
			.append("</script>\n");

		genUserInfo(scriptBuf);
		genSignInfo(scriptBuf);
	}

	private void genCommonNotOptimizedImporter(LfwWindow pm, UIMeta um, UIView targetWidget, StringBuffer scriptBuf, StringBuffer cssBuf, boolean clientMode, String themeId) {
		LinkedHashSet<Importer> importList = new LinkedHashSet<Importer>();
		LinkedHashSet<Importer> cssImportList = new LinkedHashSet<Importer>();
//		Set<String> idSet = new HashSet<String>();
		List<String> idList = new ArrayList<String>();
		// 取um下所有view
		List<UIView> UIViewList = new ArrayList<UIView>();
		if (um != null) {
			UIViewList = um.findAllUIView();
		}
		boolean editMode = LfwRuntimeEnvironment.isEditMode();
		loadLib("jquery", importList, cssImportList, idList);
		loadLib("scrollbar", importList, cssImportList, idList);
		if(editMode)
			loadLib("coreedit", importList, cssImportList, idList);
		else{
			loadLib("core", importList, cssImportList, idList);
			loadLib("layout", importList, cssImportList, idList);
		}
		
		loadLib("app", importList, cssImportList, idList);
		
		loadLib("widget", importList, cssImportList, idList);
		
		loadLib("modaldialog", importList, cssImportList, idList);
		
		loadLib("contextmenu", importList, cssImportList, idList);
		
		Integer jquery = null;
		if (um == null)
			jquery = 0;
		else {
			jquery = um.isJquery();
			// 如果window不加载jquery
			if (jquery == null || jquery.intValue() != 1) {
				// 遍历所有子项view判断是否需要加载jQuery
				if (UIViewList != null) {
					int i = 0;
					int j = UIViewList.size();
					while (i < j) {
						UIMeta uimeta = UIViewList.get(i).getUimeta();
					      if(uimeta == null){
					       i++;       
					       continue;
					      }
						jquery = uimeta.isJquery();
						if (jquery != null && jquery.intValue() == 1) {
							break;
						}
						i++;
					}
				}
			}
		}

		Integer isReference = null;
		if (um == null)
			isReference = 0;
		else {
			isReference = um.isReference();
			// 如果window不加载
			if (isReference == null || isReference.intValue() != 1) {
				// 遍历所有子项view判断是否需要加载
				if (UIViewList != null) {
					int i = 0;
					int j = UIViewList.size();
					while (i < j) {
						UIMeta uimeta = UIViewList.get(i).getUimeta();
					      if(uimeta == null){
					       i++;       
					       continue;
					      }
						isReference = uimeta.isReference();
						if (isReference != null && isReference.intValue() == 1) {
							break;
						}
						i++;
					}
				}
			}
		}
		if (um != null && isReference != null && isReference.intValue() == 1) {
			loadLib("reflib", importList, cssImportList, idList);
		}
		
		getImportList(importList, cssImportList, idList, um, pm, targetWidget);
		
		if(um != null){
			String includeId = um.getIncludeId();
			if(includeId != null){
				String[] includeIds = includeId.split(",");
				for (int i = 0; i < includeIds.length; i++) {
					loadLib(includeIds[i], importList, cssImportList, idList);
				}
			}
		}
		
		Iterator<String> idIt = includeIdSet.iterator();
		while(idIt.hasNext()){
			loadLib(idIt.next(), importList, cssImportList, idList);
		}
		
//		String frameDevice = "/frame/device_pc";
//		if(LfwRuntimeEnvironment.getDevicePhase() == DevicePhase.Ipad){
//			frameDevice = "/frame/device_pad";
//		}
		String rootPath = LfwRuntimeEnvironment.getRootPath();
		if(clientMode)
			rootPath = ".." + rootPath;
		
		LfwTheme theme = LfwThemeManager.getLfwTheme(rootPath, themeId);
		if(theme == null){
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("bc", "SmartImporter-000000")/*根据themeid找不到对应的theme,*/ + themeId);
		}
		String themeCtxPath = theme.getCtxPath();
		if(clientMode)
			themeCtxPath = ".." + themeCtxPath;
//		String currThemePath = themeCtxPath + frameDevice + "/themes/" + themeId;
		
		Iterator<Importer> cssIt = cssImportList.iterator();
		while(cssIt.hasNext()){
			Importer imp = cssIt.next();
			String fullName = imp.getFullname();
			String currThemePath = themeCtxPath + getFramePathByDevice(imp.getDevice()) + "themes/" + themeId;
			String str = currThemePath + "/" + fullName;
			cssBuf.append("<link rel='STYLESHEET' type='text/css' href='" + str + ".css'>\n");
		}
		
		Iterator<Importer> it = importList.iterator();
		while(it.hasNext()){
			Importer imp = it.next();
			String ctxStr = imp.getCtx();
			String fullName = imp.getFullname();
			String str = "/" + ctxStr + getFramePathByDevice(imp.getDevice()) + "script/" + fullName;
			scriptBuf.append("<script type='text/javascript' src='" + str + ".js'></script>\n");
		}

		String includeJs = null;
		String includeCss = null;
		String lfwIncludeJs = null;
		String lfwIncludeCss = null;
		String externalJs = null;
		String externalCss = null;
		String pageCss = null;
		String pageJS = null;
		if(um != null){
			includeJs = um.getIncludejs();
			includeCss = um.getIncludecss();
			lfwIncludeJs = um.getLfwIncludejs();
			lfwIncludeCss = um.getLfwIncludecss();
			externalJs = um.getExternaljs();
			externalCss = um.getExternalcss();
			pageCss = um.getPagecss();
			pageJS = um.getPagejs();
		}
		
		addExtendJs(includeJs, scriptBuf);
		Iterator<String> jsIt = includeJsSet.iterator();
		while(jsIt.hasNext())
			addExtendJs(jsIt.next(), scriptBuf);
		
		addExtendCss(includeCss, cssBuf);
		Iterator<String> incCssIt = includeCssSet.iterator();
		while(incCssIt.hasNext())
			addExtendCss(incCssIt.next(), cssBuf);
		
		addPageCss(pageCss,scriptBuf);
		addPageJs(pageJS,scriptBuf);
		String cssInclude = includeSelfDefCss("include.css");
		cssBuf.append(cssInclude);
		String include = includeSelfDefScript("include.js");
		scriptBuf.append(include);

		addLfwExtendJs(lfwIncludeJs, scriptBuf);
		addLfwExtendCss(lfwIncludeCss, cssBuf);

		addExternalJs(externalJs, scriptBuf);
		addExternalCss(externalCss, scriptBuf);
		
		if(viewLfwScriptBuf != null)
			scriptBuf.append(viewLfwScriptBuf.toString());
		if(viewLfwCssBuf != null)
			cssBuf.append(viewLfwCssBuf.toString());
		scriptBuf.append("<script>\n")
			.append("window.debugMode = true;\n")
			.append("window.loadedLib = ")
			.append(generateLoadedLib(idList))
			.append(";\n")
			.append("window.libArray = ")
			.append(generateAllLibArray(false))
			.append(";\n")
			.append("window.onload = function(){\n")
			.append("")
			.append("pageBodyScript();\n")
			.append("}\n")
			.append("</script>\n");

		genUserInfo(scriptBuf);
		genSignInfo(scriptBuf);
	}
	
	private void genUserInfo(StringBuffer scriptBuf){
		//打印用户
		String userPK = "";
		String userName = "";
		String userCode = "";
		boolean isCAUser = false;
		LfwSessionBean lfwsession =  nc.uap.lfw.core.LfwRuntimeEnvironment.getLfwSessionBean();
		if(null != lfwsession){
			userName = lfwsession.getUser_name();
			userCode = lfwsession.getUser_code();
			userPK = lfwsession.getPk_user();
			isCAUser = lfwsession.isCaUser();
		}
		if(userPK == null) userPK = "";
		if(userName == null) userName = "";
		if(userCode == null) userCode = "";
		
		//取
		
		scriptBuf.append("<script>\n")
		.append("window.userpk = \"")
		.append(userPK)
		.append("\";\n")
		.append("window.usercode = \"")
		.append(userCode)
		.append("\";\n")
		.append("window.username = \"")
		.append(userName)
		.append("\";\n")		
		.append("window.isCAUser = \"")
		.append(isCAUser)
		.append("\";\n")		
		
		.append("</script>\n");
	}
	
	private void genSignInfo(StringBuffer scriptBuf){
		if (this.havSign == false) return;
		SignInfo signInfo = null;
		try{
			ISignService signService = ServiceLocator.getService(ISignService.class);
			signInfo = signService.getSignInfo();
		}catch(Exception e){
			LfwLogger.error(e.getMessage(),e);
			signInfo = new SignInfo();
		}
//		signInfo.setCanSign(true);
//		signInfo.setSignType("server");
		scriptBuf.append("<script>\n")
		.append("window.signType = \"")
		.append(signInfo.getSignType())
		.append("\";\n")
		.append("window.signRight = \"")
		.append(signInfo.isCanSign())
		.append("\";\n")
		.append("</script>\n");
	}

	private void loadLib(String id, LinkedHashSet<Importer> importList, LinkedHashSet<Importer> cssImportList, List<String> idList) {
		IControlPlugin cplugin = ControlFramework.getInstance(LfwRuntimeEnvironment.getDevicePhase()).getControlPluginByType(id);
		doGetPluginImportList(importList, cssImportList, idList, cplugin);
	}

	private void genCommonHeader(StringBuffer headBuf, StringBuffer scriptBuf, StringBuffer cssBuf, boolean editMode, boolean wEditMode, boolean clientMode) {
//		String frameDevice = getFramePathByDevice(LfwRuntimeEnvironment.getDevicePhase());
		//v63临时方案  
		String frameDevice = getFramePathByDevice(DevicePhase.PC);
		String langCode = LfwRuntimeEnvironment.getLangCode();
		if (langCode == null || langCode.isEmpty() || langCode.equals("null"))
			langCode = "simpchn";
//		scriptBuf.append("<script type='text/javascript' src='/lfw" + frameDevice + "/script/ui/lang/ml_" + langCode + ".js'></script>\n");
		scriptBuf.append("<script type='text/javascript' src='/lfw/frame/script/ui/lang/ml.j?langId="+ langCode +"'></script>\n");
		
		String lfwCtx = LfwRuntimeEnvironment.getLfwCtx();
		
		if(clientMode)
			lfwCtx = ".." + lfwCtx;
		String rootPath = LfwRuntimeEnvironment.getRootPath();
		if(clientMode)
			rootPath = ".." + rootPath;
		
		String themeId = LfwRuntimeEnvironment.getThemeId();
		LfwTheme theme = LfwThemeManager.getLfwTheme(rootPath, themeId);
		if(theme == null){
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("bc", "SmartImporter-000000")/*根据themeid找不到对应的theme,*/ + themeId);
		}
		String themeCtxPath = theme.getCtxPath();
		if(clientMode)
			themeCtxPath = ".." + themeCtxPath;
		String currThemePath = themeCtxPath + frameDevice + "themes/" + themeId;
		
		String domain = LfwRuntimeEnvironment.getDomain();
		headBuf.append("<script>\n\t\t\t\t");
		// 如果指定页面所处domain，则设置domain
		if (domain != null) {
			headBuf.append("document.domain=\"").append(domain).append("\";\n\t\t\t\t");
			headBuf.append("window.domain_key=\"").append(domain).append("\";\n\t\t\t\t");
		}
		
		headBuf.append("window.globalPath = \"").append(rootPath).append("\";\n\t\t\t\t");
		headBuf.append("window.corePath = \"").append(LfwRuntimeEnvironment.getCorePath()).append("\";\n\t\t\t\t");
		headBuf.append("window.themeId = \"").append(themeId).append("\";\n\t\t\t\t");
		headBuf.append("window.baseGlobalPath = \"" + LfwRuntimeEnvironment.getLfwCtx() + "\";\n\t\t\t\t");
		headBuf.append("window.frameGlobalPath = window.baseGlobalPath + \"" + frameDevice + "script\";\n\t\t\t\t");
		headBuf.append("window.themeGlobalPath = \"" + themeCtxPath + "\";\n\t\t\t\t");
		headBuf.append("window.themePath = \"").append(currThemePath).append("\";\n\t\t\t\t");
		headBuf.append("window.JSessionID = \"" + LfwRuntimeEnvironment.getWebContext().getRequest().getSession().getId() + "\";\n\t\t\t\t");

		if(clientMode){
			String offlineCachePath = (String) LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(WebConstant.OFFLINE_CACHEPATH);
			if(offlineCachePath != null)
				headBuf.append("window.offlineCachePath = \"").append(offlineCachePath).append("\";\n\t\t\t\t");
		}
		ServletRequest request = LfwRuntimeEnvironment.getWebContext().getRequest();
		String nodePath = (String) request.getAttribute(WebConstant.NODE_PATH);
		String nodeThemePath = (String) request.getAttribute(WebConstant.NODE_THEME_PATH);
		String nodeStylePath = (String) request.getAttribute(WebConstant.NODE_STYLE_PATH);
		String nodeImagePath = (String) request.getAttribute(WebConstant.NODE_IMAGE_PATH);

		headBuf.append("window.nodePath = \"").append(nodePath).append("\";\n\t\t\t\t");
		headBuf.append("window.nodeThemePath = \"").append(nodeThemePath).append("\";\n\t\t\t\t");
		headBuf.append("window.nodeStylePath = \"").append(nodeStylePath).append("\";\n\t\t\t\t");
		headBuf.append("window.nodeImagePath = \"").append(nodeImagePath).append("\";\n\t\t\t\t");
		
		if(AppLifeCycleContext.current() != null){
			String appId = (String) LfwRuntimeEnvironment.getWebContext().getAppSession().getAttribute("appId");
			headBuf.append("window.appId = '" + appId + "';\n");
			headBuf.append("window.appType = 'true';\n");
		}
			
		if(LfwRuntimeEnvironment.isFromLfw()){
			headBuf.append("window.tempPath = 'webtemp';\n");
		}
		else{
			headBuf.append("window.tempPath = '';\n");
		}
		headBuf.append("window.debugMode = '").append(LfwRuntimeEnvironment.getMode()).append("';\n");
		
		if(editMode){
			headBuf.append("window.editMode = true;\n");
		}
		else{
			headBuf.append("window.editMode = false;\n");
		}
		
		if(wEditMode){
			headBuf.append("window.windowEditorMode = true;\n");
		}
		else{
			headBuf.append("window.windowEditorMode = false;\n");
		}
		headBuf.append("window.datasourceName = '").append(LfwRuntimeEnvironment.getDatasource()).append("';\n");

		headBuf.append("window.clientMode = ").append(clientMode).append(";\n");

		headBuf.append("\t\t</script>\n");
	}
	
	private String generateLoadedLib(List<String> idList) {
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		Iterator<String> it = idList.iterator();
		while(it.hasNext()){
			String str = it.next();
			buf.append(str)
			   .append(":1");
			if(it.hasNext()){
				buf.append(",");
			}
		}
		buf.append("}");
		return buf.toString();
	}

	private String generateAllLibArray(boolean optimized) {
		if(optimized){
			if(optimizedLibArrStr == null){
//				StringBuffer buf = new StringBuffer();
//				buf.append("{");
//				List<ControlPluginConfig> pluginList = ControlFramework.getInstance(LfwRuntimeEnvironment.getDevicePhase()).getControlPluginConfigList();
//				Iterator<ControlPluginConfig> it = pluginList.iterator();
//				boolean hasPre = false;
//				while(it.hasNext()){
//					ControlPluginConfig config = it.next();
//					String compressDyn = config.getCompressdyn();
//					if(compressDyn != null && compressDyn.equals("true")){
//						String id = config.getId();
//						if(hasPre)
//							buf.append(",");
//						buf.append(id)
//							.append(":")
//							.append("{jslib:['compressed/")
//							.append(id)
//							.append("'],'csslib':['compressed/")
//							.append(id)
//							.append("']}");
//						hasPre = true;
//					}
//				}
				StringBuffer buf = new StringBuffer();
				buf.append("{");
//				IControlPlugin[] ctrlPlugins = ControlFramework.getInstance(LfwRuntimeEnvironment.getDevicePhase()).getAllControlPlugins();
				//v63临时方案
				IControlPlugin[] ctrlPlugins = ControlFramework.getInstance(DevicePhase.PC).getAllControlPlugins();
				for (int i = 0; i < ctrlPlugins.length; i++) {
					IControlPlugin ctrlPlugin = ctrlPlugins[i];
					buf.append("'")
					   .append(ctrlPlugin.getId())
					   .append("'")
					   .append(":{")
					   .append("'dp':")
					   .append(StringUtil.mergeScriptArrayStr(ctrlPlugin.getDependences()));
					String scriptArr = StringUtil.mergeScriptArrayStr(ctrlPlugin.getImports(optimized));
					if(scriptArr != null && !scriptArr.equals("")){
						buf.append(",'jslib':")
					   .append(scriptArr);
					}
					
					String cssArr = StringUtil.mergeScriptArrayStr(ctrlPlugin.getCssImports(optimized));
					if(cssArr != null && !cssArr.equals("")){
						buf.append(",'csslib':")
						   .append(cssArr);
					}
					buf.append("}");
					if( i != ctrlPlugins.length - 1)
						buf.append(",");
				}
				
				buf.append("}");
				optimizedLibArrStr = buf.toString();
			}
			return optimizedLibArrStr;
		}
		else{
			if(libArrStr == null){
				StringBuffer buf = new StringBuffer();
				buf.append("{");
//				IControlPlugin[] ctrlPlugins = ControlFramework.getInstance(LfwRuntimeEnvironment.getDevicePhase()).getAllControlPlugins();
				//v63临时方案
				IControlPlugin[] ctrlPlugins = ControlFramework.getInstance(DevicePhase.PC).getAllControlPlugins();
				for (int i = 0; i < ctrlPlugins.length; i++) {
					IControlPlugin ctrlPlugin = ctrlPlugins[i];
					buf.append("'")
					   .append(ctrlPlugin.getId())
					   .append("'")
					   .append(":{")
					   .append("'dp':")
					   .append(StringUtil.mergeScriptArrayStr(ctrlPlugin.getDependences()));
					String scriptArr = StringUtil.mergeScriptArrayStr(ctrlPlugin.getImports(optimized));
					if(scriptArr != null && !scriptArr.equals("")){
						buf.append(",'jslib':")
					   .append(scriptArr);
					}
					
					String cssArr = StringUtil.mergeScriptArrayStr(ctrlPlugin.getCssImports(optimized));
					if(cssArr != null && !cssArr.equals("")){
						buf.append(",'csslib':")
						   .append(cssArr);
					}
					buf.append("}");
					if( i != ctrlPlugins.length - 1)
						buf.append(",");
				}
				buf.append("}");
				libArrStr = buf.toString();
			}
			return libArrStr;
		}
	}

	/**
	 * 扩展引入CSS接口，子类主要重写此方法 <b>注意:实现过程中可以区分是否优化载入情况，根据isCustomized()方法可区分</b>
	 * 
	 * @param buf
	 */
	private void addExtendCss(String includeCss, StringBuffer buf) {
		if (StringUtils.isNotEmpty(includeCss)) {
			String[] csss = includeCss.split(",");
			for (String css : csss) {
				buf.append(addPublicInclude(css));
			}
		}
	}
	
	private void addExternalCss(String externalCss, StringBuffer buf) {
		if (StringUtils.isNotEmpty(externalCss)) {
			String[] csss = externalCss.split(",");
			for (String css : csss) {
				buf.append(addExternalInclude(css));
			}
		}
	}
	
	/**
	 * 扩展引入CSS接口，子类主要重写此方法 <b>注意:实现过程中可以区分是否优化载入情况，根据isCustomized()方法可区分</b>
	 * 
	 * @param buf
	 */
	private void addLfwExtendCss(String includeCss, StringBuffer buf) {
		if (StringUtils.isNotEmpty(includeCss)) {
			String[] csss = includeCss.split(",");
			for (String css : csss) {
				buf.append(addLfwPublicInclude(css));
			}
		}
	}
	
	/**
	 * 添加页面级样式
	 * @param pagecss
	 * @param buf
	 */
	private void addPageCss(String pagecss,StringBuffer buf){
		if(StringUtils.isNotEmpty(pagecss)){
			buf.append("<style type='text/css' id='page_style'> \n");
			buf.append(pagecss);
			buf.append("</style> \n");
		}
	}
	
	private void addPageJs(String pagejs,StringBuffer buf){
		if(StringUtils.isNotEmpty(pagejs)){
			buf.append("<script>\n")
			.append(pagejs)
			.append("</script>\n");
		}
	}
	
	/**
	 * 扩展引入js接口，子类主要重写此方法 <b>注意:实现过程中可以区分是否优化载入情况，根据isCustomized()方法可区分</b>
	 * 
	 * @param buf
	 */
	private void addExtendJs(String includeJs, StringBuffer buf) {
		if (StringUtils.isNotEmpty(includeJs)) {
			String[] jss = includeJs.split(",");
			for (String js : jss) {
				buf.append(addPublicInclude(js));
			}
		}
	}
	
	/**
	 * 扩展引入js接口，子类主要重写此方法 <b>注意:实现过程中可以区分是否优化载入情况，根据isCustomized()方法可区分</b>
	 * 
	 * @param buf
	 */
	private void addLfwExtendJs(String includeJs, StringBuffer buf) {
		if (StringUtils.isNotEmpty(includeJs)) {
			String[] jss = includeJs.split(",");
			for (String js : jss) {
				buf.append(addLfwPublicInclude(js));
			}
		}
	}
	
	/**
	 * 引入外部js，可任意路径
	 * 
	 * @param externalJs
	 * @param buf
	 */
	private void addExternalJs(String externalJs, StringBuffer buf) {
		if (StringUtils.isNotEmpty(externalJs)) {
			String[] jss = externalJs.split(",");
			for (String js : jss) {
				buf.append(addExternalInclude(js));
			}
		}
	}

	/**
	 * 添加外部引入。不在进行文件是否存在判断，业务指定，由业务自己保证
	 * @param fileName
	 * @return
	 */
	private String addPublicInclude(String fileName) {
		StringBuffer includeHead = new StringBuffer();
		StringBuffer includeTrail = new StringBuffer();
		String folder = "includejs";
		if (fileName.toLowerCase().endsWith(".css")) {
			folder = "includecss";
			includeHead.append("<link rel='STYLESHEET' type='text/css' href='");
			includeTrail.append("'>\n");
		} else {
			includeHead.append("<script type='text/javascript' src='");
			includeTrail.append("'></script>\n");
		}
		includeHead.append(LfwRuntimeEnvironment.getRootPath() + "/" + folder + "/" + fileName);
		includeHead.append(includeTrail);
		
		//includejs目录下有多语资源
		if(fileName.indexOf(".") != -1){
			String langResoFilePath = "/" + folder + "/" + fileName.substring(0, fileName.indexOf(".")) + ".properties";
			Properties langResources = AbstractPresentPlugin.loadNodeLangResources(langResoFilePath);
			if(langResources != null){
				String transStr = LangResoTranf.tranfs(langResources);
				String langCode = LfwRuntimeEnvironment.getLangCode();
				String relativePath = WebConstant.TEMP_JSSELFDEFINE_PATH + folder + "/" + langCode;
				//将资源写到临时文件夹里
				writeToTmpFile(transStr, relativePath);
				
				String langResPath = LfwRuntimeEnvironment.getRootPath() + relativePath + "/" + WebConstant.TEMP_JSSELFDEFINE_FILE;
				includeHead.append("<script type='text/javascript' src='" + langResPath + "'></script>\n");
			}
		}
		
		return includeHead.toString();
	}
	
	/**
	 * 添加外部引入, 任意目录
	 * @param fileName
	 * @return
	 */
	private String addExternalInclude(String fileName) {
		if (fileName.contains("${theme}")) {
			LfwTheme theme = LfwRuntimeEnvironment.getTheme();
			fileName = fileName.replace("${theme}", theme.getId());
		}
		StringBuffer includeHead = new StringBuffer();
		StringBuffer includeTrail = new StringBuffer();
		if (fileName.toLowerCase().endsWith(".css")) {
			includeHead.append("<link rel='STYLESHEET' type='text/css' href='");
			includeTrail.append("'>\n");
		} else {
			includeHead.append("<script type='text/javascript' src='");
			includeTrail.append("'></script>\n");
		}
		String filePath = null;
		if (fileName.startsWith("/"))
			filePath = fileName;
		else
			filePath = LfwRuntimeEnvironment.getRootPath() + "/" + fileName;
		includeHead.append(filePath);
		includeHead.append(includeTrail);
		
		return includeHead.toString();
	}
	
	/**
	 * 添加Lfw外部引入。不在进行文件是否存在判断，业务指定，由业务自己保证
	 * @param fileName
	 * @return
	 */
	private String addLfwPublicInclude(String fileName) {
		StringBuffer includeHead = new StringBuffer();
		StringBuffer includeTrail = new StringBuffer();
		String folder = "includejs";
		if (fileName.toLowerCase().endsWith(".css")) {
			folder = "includecss";
			includeHead.append("<link rel='STYLESHEET' type='text/css' href='");
			includeTrail.append("'>\n");
		} else {
			includeHead.append("<script type='text/javascript' src='");
			includeTrail.append("'></script>\n");
		}
		includeHead.append(LfwRuntimeEnvironment.getLfwCtx() + "/" + folder + "/" + fileName);
		includeHead.append(includeTrail);
		return includeHead.toString();
	}
	
	
	private void getImportList(LinkedHashSet<Importer> importList, LinkedHashSet<Importer> cssImportList, List<String> idList, UIMeta um, LfwWindow pm, UIView targetWidget) {
		if(um != null)
			getImportList(importList, cssImportList, idList, um, targetWidget);
		LfwView[] widgets = null;
		if(targetWidget != null)
			widgets = new LfwView[]{pm.getView(targetWidget.getId())};
		else
			widgets = pm.getViews();
		for (int i = 0; i < widgets.length; i++) {
			LfwView widget = widgets[i];
			ViewModels vms = widget.getViewModels();
			Dataset[] dss = vms.getDatasets();
			if(dss != null && dss.length > 0)
				doGetImportList(importList, cssImportList, idList, dss[0]);
			
			ComboData[] combos = vms.getComboDatas();
			if(combos != null && combos.length > 0)
				doGetImportList(importList, cssImportList, idList, combos[0]);
			
			DatasetRelations dsRels = vms.getDsrelations();
			if(dsRels != null)
				doGetImportList(importList, cssImportList, idList, new DatasetRelation());
			
			IRefNode[] rfs = vms.getRefNodes();
			if(rfs != null && rfs.length > 0)
				doGetImportList(importList, cssImportList, idList, rfs[0]);
			
			RefNodeRelations rfrels = vms.getRefNodeRelations();
			if(rfrels != null)
				doGetImportList(importList, cssImportList, idList, new RefNodeRelation());
			
			if(um == null){
				ViewComponents vcs = widget.getViewComponents();
				WebComponent[] coms = vcs.getComponents();
				if(coms != null && coms.length > 0){
					for (int j = 0; j < coms.length; j++) {
						doGetImportList(importList, cssImportList, idList, coms[j]);
						if (coms[j] instanceof FormComp){
							doGetFormElementImportList(importList, cssImportList, idList, (FormComp)coms[j]);
						}
						else if (coms[j] instanceof GridComp){
							doGetGridImportList(importList, cssImportList, idList, (GridComp)coms[j]);
						}
					}
				}
			}
		}
	}

	/**
	 * 跟据grid的具体内容处理grid的引入js
	 * @param importList
	 * @param cssImportList
	 * @param idSet
	 * @param gridComp
	 */
	private void doGetGridImportList(LinkedHashSet<Importer> importList,
			LinkedHashSet<Importer> cssImportList, List<String> idList,GridComp gridComp) {
		IControlPlugin plugin = null;
		//存在用户自定义的CellEditor，需要把alltext都加载上
		if (!StringUtils.isEmpty(gridComp.getExtendCellEditor())){
			String type = "alltext";
			plugin = ControlFramework.getInstance(LfwRuntimeEnvironment.getDevicePhase()).getControlPluginByType(type);
			doGetPluginImportList(importList, cssImportList, idList, plugin);
		}
		else{
			for (IGridColumn column : gridComp.getColumnList()){
				doGetGridColumnImportList(importList, cssImportList, idList, column);
			}
		}
		//翻页工具条页码输入框
		plugin = ControlFramework.getInstance(LfwRuntimeEnvironment.getDevicePhase()).getControlPluginByType("integertext");
		doGetPluginImportList(importList, cssImportList, idList, plugin);
	}

	private void doGetGridColumnImportList(LinkedHashSet<Importer> importList,
			LinkedHashSet<Importer> cssImportList, List<String> idList,IGridColumn column){
		if (column instanceof GridColumnGroup){
			GridColumnGroup group = (GridColumnGroup)column;
			for (IGridColumn childColumn : group.getChildColumnList())
				doGetGridColumnImportList(importList, cssImportList, idList, childColumn);
		}
		else {
			GridColumn childColumn = (GridColumn)column;
			String editorType = childColumn.getEditorType();
			String type = null;
			if(editorType == null || editorType.equals(EditorTypeConst.STRINGTEXT))
				type = "stringtext";
			else if(editorType.equals(EditorTypeConst.INTEGERTEXT))
				type = "integertext";
			else if(editorType.equals(EditorTypeConst.DECIMALTEXT))
				type = "floattext";
			else if(editorType.equals(EditorTypeConst.DATETEXT) || editorType.equals(EditorTypeConst.DATETIMETEXT))
				type = "datetext";
			else if(editorType.equals(EditorTypeConst.REFERENCE))
				type = "reftext";
			else if(editorType.equals(EditorTypeConst.PWDTEXT))
				type = "pswtext";
			else if(editorType.equals(EditorTypeConst.COMBODATA))
				type = "combotext";
			else if(editorType.equals(EditorTypeConst.TEXTAREA))
				type = "textarea";
			else if(editorType.equals(EditorTypeConst.LANGUAGECOMBODATA))
				type = "languagecombotext";
			else if(editorType.equals(EditorTypeConst.RADIOCOMP))
				type = "radiotext";
			else if(editorType.equals(EditorTypeConst.FILECOMP))
				type = "filetext";
			else if(editorType.equals(EditorTypeConst.CHECKBOX))
				type = "checkboxtext";
			else if(editorType.equals(EditorTypeConst.CHECKBOXGROUP))
				type = "checkboxgrouptext";
			else if(editorType.equals(EditorTypeConst.RICHEDITOR))
				type = "editortext";
			else if(editorType.equals(EditorTypeConst.RADIOGROUP))
				type = "radiogrouptext";
			else if(editorType.equals(EditorTypeConst.SELFDEF))
				type = "selfdefcomp";
			else if(editorType.equals(EditorTypeConst.IMAGECOMP))
				type = "image";
			else if(editorType.equals(EditorTypeConst.SHORTDATETEXT))
				type = "datetext";
			else if(editorType.equals(EditorTypeConst.MONTHTEXT))
				type = "monthtext";
			else if(editorType.equals(EditorTypeConst.YEARTEXT))
				type = "yeartext";
			else if(editorType.equals(EditorTypeConst.YEARMONTHTEXT))
				type = "yearmonthtext";
			else if(editorType.equals(EditorTypeConst.EMAILTEXT))
				type = "emailtext";
			else if(editorType.equals(EditorTypeConst.PHONETEXT))
				type = "phonetext";
			else if(editorType.equals(EditorTypeConst.LINKTEXT))
				type = "linktext";
			else if(editorType.equals(EditorTypeConst.MONEYTEXT))
				type = "moneytext";
			else if(editorType.equals(EditorTypeConst.PRECENTTEXT))
				type = "precenttext";
			
			else
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("mvc", "SmartImporter-000000")/*处理grid中的元素依赖js、css时，未能处理元素类型:*/ + editorType);
			IControlPlugin plugin = ControlFramework.getInstance(LfwRuntimeEnvironment.getDevicePhase()).getControlPluginByType(type);
			doGetPluginImportList(importList, cssImportList, idList, plugin);
		}
	}

	/**
	 * 跟据form的具体内容处理form的引入js
	 * @param importList
	 * @param cssImportList
	 * @param idSet
	 * @param webComponent
	 */
	private void doGetFormElementImportList(LinkedHashSet<Importer> importList,
			LinkedHashSet<Importer> cssImportList, List<String> idList,FormComp form) {
		IControlPlugin plugin = null;
		for (FormElement ele : form.getElementList()){
			String editorType = ele.getEditorType();
			String type = getFormElementEditType(editorType);
			if (type == null)
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("mvc", "SmartImporter-000001")/*处理form:*/ + form.getId() +NCLangRes4VoTransl.getNCLangRes().getStrByID("mvc", "SmartImporter-000003")/* 中的元素依赖js、css时，未能处理元素类型:*/ + editorType);
			plugin = ControlFramework.getInstance(LfwRuntimeEnvironment.getDevicePhase()).getControlPluginByType(type);
			doGetPluginImportList(importList, cssImportList, idList, plugin);
		}
	}

	private String getFormElementEditType(String editorType){
		String type = null;
		if(editorType == null || editorType.equals(EditorTypeConst.STRINGTEXT))
			type = "stringtext";
		else if(editorType.equals(EditorTypeConst.INTEGERTEXT))
			type = "integertext";
		else if(editorType.equals(EditorTypeConst.DECIMALTEXT))
			type = "floattext";
		else if(editorType.equals(EditorTypeConst.DATETEXT) || editorType.equals(EditorTypeConst.DATETIMETEXT))
			type = "datetext";
		else if(editorType.equals(EditorTypeConst.REFERENCE))
			type = "reftext";
		else if(editorType.equals(EditorTypeConst.PWDTEXT))
			type = "pswtext";
		else if(editorType.equals(EditorTypeConst.COMBODATA))
			type = "combotext";
		else if(editorType.equals(EditorTypeConst.TEXTAREA))
			type = "textarea";
		else if(editorType.equals(EditorTypeConst.LANGUAGECOMBODATA))
			type = "languagecombotext";
		else if(editorType.equals(EditorTypeConst.RADIOCOMP))
			type = "radiotext";
		else if(editorType.equals(EditorTypeConst.FILECOMP))
			type = "filetext";
		else if(editorType.equals(EditorTypeConst.CHECKBOX))
			type = "checkboxtext";
		else if(editorType.equals(EditorTypeConst.CHECKBOXGROUP))
			type = "checkboxgrouptext";
		else if(editorType.equals(EditorTypeConst.RICHEDITOR))
			type = "editortext";
		else if(editorType.equals(EditorTypeConst.RADIOGROUP))
			type = "radiogrouptext";
		else if(editorType.equals(EditorTypeConst.SELFDEF))
			type = "selfdefcomp";
		else if(editorType.equals(EditorTypeConst.IMAGECOMP))
			type = "image";
		else if(editorType.equals(EditorTypeConst.SHORTDATETEXT))
			type = "datetext";
		else if(editorType.equals(EditorTypeConst.MONTHTEXT))
			type = "monthtext";
		else if(editorType.equals(EditorTypeConst.YEARTEXT))
			type = "yeartext";
		else if(editorType.equals(EditorTypeConst.YEARMONTHTEXT))
			type = "yearmonthtext";
		else if(editorType.equals(EditorTypeConst.EMAILTEXT))
			type = "emailtext";
		else if(editorType.equals(EditorTypeConst.PHONETEXT))
			type = "phonetext";
		else if(editorType.equals(EditorTypeConst.LINKTEXT))
			type = "linktext";
		else if(editorType.equals(EditorTypeConst.MONEYTEXT))
			type = "moneytext";
		else if(editorType.equals(EditorTypeConst.PRECENTTEXT))
			type = "precenttext";
		else if(editorType.equals(EditorTypeConst.SIGNCOMP)){
			type = "sign";
			this.havSign = true;
		}
		return type;
	}

	private void getImportList(LinkedHashSet<Importer> importList, LinkedHashSet<Importer> cssImportList, List<String> idList, UIMeta um, UIView targetWidget) {
		UIElement ele = null;
		if(targetWidget != null)
			ele = targetWidget;
		else
			ele = um.getElement();
		if(ele instanceof UIView){
			UIView widget = (UIView) ele;
			getWidgetImportList(importList, cssImportList, idList, widget);
		}
		else if(ele instanceof UILayout){
			getLayoutImportList(importList, cssImportList, idList, (UILayout) ele);
		}
		else if(ele instanceof UIControl){
			doGetUIEleImportList(importList, cssImportList, idList, ele);
		}
	}

	private void getLayoutImportList(LinkedHashSet<Importer> importList, LinkedHashSet<Importer> cssImportList, List<String> idList, UILayout layout) {
		doGetUIEleImportList(importList, cssImportList, idList, layout);
		List<UILayoutPanel> panelList = layout.getPanelList();
		Iterator<UILayoutPanel> it = panelList.iterator();
		while(it.hasNext()){
			UILayoutPanel panel = it.next();
			if (StringUtils.isNotBlank(panel.getTemplate())){
				doGetTemplateImportList(importList, cssImportList, idList, panel);
			}
			else{
				UIElement ele = panel.getElement();
				if(ele == null)
					continue;
				doGetUIEleImportList(importList, cssImportList, idList, ele);
				if(ele instanceof UIView){
					getWidgetImportList(importList, cssImportList, idList, (UIView) ele);
				}
				else if(ele instanceof UILayout){
					getLayoutImportList(importList, cssImportList, idList, (UILayout) ele);
				}
			}
		}
		if(layout instanceof UITabComp){
			UILayoutPanel rigthPanel = ((UITabComp) layout).getRightPanel();
			if(rigthPanel != null){
				if (StringUtils.isNotBlank(rigthPanel.getTemplate())){
					doGetTemplateImportList(importList, cssImportList, idList, rigthPanel);
				}
				else{
					UIElement ele = rigthPanel.getElement();
					if(ele != null){
						doGetUIEleImportList(importList, cssImportList, idList, ele);
						if(ele instanceof UIView){
							getWidgetImportList(importList, cssImportList, idList, (UIView) ele);
						}
						else if(ele instanceof UILayout){
							getLayoutImportList(importList, cssImportList, idList, (UILayout) ele);
						}
					}
				}
			}
		}
	}
	
	private void doGetTemplateImportList(LinkedHashSet<Importer> importList,LinkedHashSet<Importer> cssImportList, List<String> idList,UILayoutPanel panel) {
		String template = panel.getTemplate();
		if (StringUtils.isBlank(template))
			return;
//		String filePath = pm.getFoldPath() +  panel.getWidgetId() + "/" + template;
		String filePath = pm.getView(panel.getWidgetId()).getFoldPath() + "/" + template;
		File templateFile = ContextResourceUtil.getFile(filePath);
		if (!templateFile.exists()){
			throw new LfwRuntimeException("panel:"+ panel.getId() +LfwResBundle.getInstance().getStrByID("mvc", "SmartImporter-000004")/* 对应的模板文件不存在!*/);
		}
//		try {
//			templateDoc = Jsoup.parse(templateFile, "UTF-8");
//		} catch (IOException e) {
//			LfwLogger.error(e);
//			throw  new LfwRuntimeException("panel:"+ panel.getId() +" 解析模板文件时出错!");
//		}
//		Elements eles = templateDoc.getElementsByAttribute(TemplateHelper.COMP_TYPE);
		ITemplateParserService templateparserService = NCLocator.getInstance().lookup(ITemplateParserService.class);
		List<UIElement> eles = templateparserService.getUIElement(panel.getWidgetId(), templateFile);
		for (UIElement ele : eles){
//			UIElement uiElement = TemplateHelper.getUIElement(ele,panel.getWidgetId());
			doGetUIEleImportList(importList, cssImportList, idList, ele);
		}
		
	}

	private void getWidgetImportList(LinkedHashSet<Importer> importList, LinkedHashSet<Importer> cssImportList, List<String> idList, UIView widget){
		UIMeta um = widget.getUimeta();
		if(um == null)
			return;
		String includeJs = um.getIncludejs();
		String includeCss = um.getIncludecss();
		String includeId = um.getIncludeId();
		if(includeJs != null && !includeJs.equals(""))
			includeJsSet.addAll(Arrays.asList(includeJs.split(",")));
		
		if(includeCss != null && !includeCss.equals(""))
			includeCssSet.addAll(Arrays.asList(includeCss.split(",")));
		
		String lfwIncludeJs = um.getLfwIncludejs();
		if(lfwIncludeJs != null && !lfwIncludeJs.equals("")){
			if(viewLfwScriptBuf == null)
				viewLfwScriptBuf = new StringBuffer();
			addLfwExtendJs(lfwIncludeJs, viewLfwScriptBuf);
		}
		
		String lfwIncludeCss = um.getLfwIncludecss();
		if(lfwIncludeCss != null && !lfwIncludeCss.equals("")){
			if(viewLfwCssBuf == null)
				viewLfwCssBuf = new StringBuffer();
			addLfwExtendCss(lfwIncludeCss, viewLfwCssBuf);
		}
		
		if(includeId != null && !includeId.equals(""))
			includeIdSet.addAll(Arrays.asList(includeId.split(",")));
		getImportList(importList, cssImportList, idList, um, null);
	}

	private void doGetUIEleImportList(LinkedHashSet<Importer> importList, LinkedHashSet<Importer> cssImportList, List<String> idList, UIElement ele) {
		IControlPlugin plugin = null;
		if(ele instanceof UITextField){
			LfwView widget = pm.getView(ele.getWidgetId());
			WebComponent wc = widget.getViewComponents().getComponent(ele.getId());
			
			if(wc instanceof TextComp){
				TextComp text = (TextComp)wc;
				String editorType = text.getEditorType();
				String type = null;
				if(editorType == null || editorType.equals(EditorTypeConst.STRINGTEXT))
					type = "stringtext";
				else if(editorType.equals(EditorTypeConst.INTEGERTEXT))
					type = "integertext";
				else if(editorType.equals(EditorTypeConst.DECIMALTEXT))
					type = "floattext";
				else if(editorType.equals(EditorTypeConst.DATETEXT) || editorType.equals(EditorTypeConst.DATETIMETEXT))
					type = "datetext";
				else if(editorType.equals(EditorTypeConst.REFERENCE))
					type = "reftext";
				else if(editorType.equals(EditorTypeConst.PWDTEXT))
					type = "pswtext";
				else if(editorType.equals(EditorTypeConst.COMBODATA))
					type = "combotext";
				else if(editorType.equals(EditorTypeConst.TEXTAREA))
					type = "textarea";
				else if(editorType.equals(EditorTypeConst.CHECKBOX))
					type = "checkboxtext";
				else if(editorType.equals(EditorTypeConst.MONTHTEXT))
					type = "monthtext";
				else if(editorType.equals(EditorTypeConst.YEARTEXT))
					type = "yeartext";
				else if(editorType.equals(EditorTypeConst.YEARMONTHTEXT))
					type = "yearmonthtext";
				else if(editorType.equals(EditorTypeConst.SEARCHTEXT))
					type = "searchtext";
				else if(editorType.equals(EditorTypeConst.EMAILTEXT))
					type = "emailtext";
				else if(editorType.equals(EditorTypeConst.PHONETEXT))
					type = "phonetext";
				else if(editorType.equals(EditorTypeConst.LINKTEXT))
					type = "linktext";
				else if(editorType.equals(EditorTypeConst.MONEYTEXT))
					type = "moneytext";
				else if(editorType.equals(EditorTypeConst.PRECENTTEXT))
					type = "precenttext";
				else if(editorType.equals(EditorTypeConst.RADIOGROUP))
					type = "radiogrouptext";
				else if(editorType.equals(EditorTypeConst.RADIOCOMP))
					type = "radiotext";
				plugin = ControlFramework.getInstance(LfwRuntimeEnvironment.getDevicePhase()).getControlPluginByType(type);
			}
		}
		else
			plugin = ControlFramework.getInstance(LfwRuntimeEnvironment.getDevicePhase()).getControlPluginByUIClass(ele.getClass());
		doGetPluginImportList(importList, cssImportList, idList, plugin);
		if (ele instanceof UIFormElement){
			UIFormElement uiFormEle = (UIFormElement)ele;
			String formId = uiFormEle.getFormId();
			String viewId = uiFormEle.getWidgetId();
			LfwView view = pm.getView(viewId);
			FormComp form =  (FormComp)view.getViewComponents().getComponent(formId);
			FormElement formEle = form.getElementById(ele.getId());
			String type = getFormElementEditType(formEle.getEditorType());
			if (type == null)
				throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("mvc", "SmartImporter-000001")/*处理form:*/ + form.getId() +NCLangRes4VoTransl.getNCLangRes().getStrByID("mvc", "SmartImporter-000003")/* 中的元素依赖js、css时，未能处理元素类型:*/ + formEle.getEditorType());
			plugin = ControlFramework.getInstance(LfwRuntimeEnvironment.getDevicePhase()).getControlPluginByType(type);
			doGetPluginImportList(importList, cssImportList, idList, plugin);
		}
		if (ele instanceof UIFormComp){
			String formId = ele.getId();
			String viewId = ele.getWidgetId();
			LfwView view = pm.getView(viewId);
			FormComp form =  (FormComp)view.getViewComponents().getComponent(formId);
			doGetFormElementImportList(importList, cssImportList, idList, form);
		}
		else if (ele instanceof UIGridComp){
			String gridId = ele.getId();
			String viewId = ele.getWidgetId();
			LfwView view = pm.getView(viewId);
			GridComp grid =  (GridComp)view.getViewComponents().getComponent(gridId);
			doGetGridImportList(importList, cssImportList, idList, grid);
		}

	}
	
	private void doGetImportList(LinkedHashSet<Importer> importList, LinkedHashSet<Importer> cssImportList, List<String> idList, Object ele) {
		IControlPlugin plugin = null;
		if(ele instanceof TextComp){
			String editorType = ((TextComp)ele).getEditorType();
			String type = null;
			if(editorType == null || editorType.equals(EditorTypeConst.STRINGTEXT))
				type = "stringtext";
			else if(editorType.equals(EditorTypeConst.INTEGERTEXT))
				type = "integertext";
			else if(editorType.equals(EditorTypeConst.DECIMALTEXT))
				type = "floattext";
			else if(editorType.equals(EditorTypeConst.DATETEXT))
				type = "datetext";
			else if(editorType.equals(EditorTypeConst.REFERENCE))
				type = "reftext";
			else if(editorType.equals(EditorTypeConst.PWDTEXT))
				type = "pswtext";
			else if(editorType.equals(EditorTypeConst.COMBODATA))
				type = "combotext";
			else if(editorType.equals(EditorTypeConst.TEXTAREA))
				type = "textarea";
			else if(editorType.equals(EditorTypeConst.CHECKBOX))
				type = "checkboxtext";
			else if(editorType.equals(EditorTypeConst.MONTHTEXT))
				type = "monthtext";
			else if(editorType.equals(EditorTypeConst.YEARTEXT))
				type = "yeartext";
			else if(editorType.equals(EditorTypeConst.YEARMONTHTEXT))
				type = "yearmonthtext";
			else if(editorType.equals(EditorTypeConst.SEARCHTEXT))
				type = "searchtext";
			else if(editorType.equals(EditorTypeConst.EMAILTEXT))
				type = "emailtext";
			else if(editorType.equals(EditorTypeConst.PHONETEXT))
				type = "phonetext";
			else if(editorType.equals(EditorTypeConst.LINKTEXT))
				type = "linktext";
			else if(editorType.equals(EditorTypeConst.MONEYTEXT))
				type = "moneytext";
			else if(editorType.equals(EditorTypeConst.PRECENTTEXT))
				type = "precenttext";
			else if(editorType.equals(EditorTypeConst.RADIOGROUP))
				type = "radiogrouptext";
			else if(editorType.equals(EditorTypeConst.RADIOCOMP))
				type = "radiotext";
			plugin = ControlFramework.getInstance(LfwRuntimeEnvironment.getDevicePhase()).getControlPluginByType(type);
		}
		else{
			Class<?> c = ele.getClass();
			if(ele instanceof ComboData)
				c = ComboData.class;
			else if(ele instanceof IRefNode)
				c = IRefNode.class;
			else if(ele instanceof Dataset)
				c = Dataset.class;
			plugin = ControlFramework.getInstance(LfwRuntimeEnvironment.getDevicePhase()).getControlPluginByClass(c);
		}
			
		doGetPluginImportList(importList, cssImportList, idList, plugin);
	}

	private void doGetPluginImportList(LinkedHashSet<Importer> importList, LinkedHashSet<Importer> cssImportList, List<String> idList, IControlPlugin plugin) {
		if(plugin != null){
			if(idList.contains(plugin.getId()))
				return;
//			String[] dependences = plugin.calculateDependences(false);
			String[] dependenceIds = plugin.calculateDependenceIds();
			if(dependenceIds != null){
				for (int i = 0; i < dependenceIds.length; i++) {
					IControlPlugin cPlugin = ControlFramework.getInstance(LfwRuntimeEnvironment.getDevicePhase()).getControlPluginByType(dependenceIds[i]);
					String[] imports = cPlugin.getImports(false);
					if (imports != null){
						for(int j = 0; j < imports.length; j++){
							importList.add(new Importer(imports[j], cPlugin.getDevice()));
						}
					}
					String[] cssImports = cPlugin.getCssImports(false);
					if (cssImports != null){
						for (int k = 0; k < cssImports.length; k++) {
							cssImportList.add(new Importer(cssImports[k], cPlugin.getDevice()));
						}
					}
				}
			}
//			if(dependences != null){
//				for (int i = 0; i < dependences.length; i++) {
//					IControlPlugin cPlugin = ControlFramework.getInstance(LfwRuntimeEnvironment.getDevicePhase()).getControlPluginByFullName(dependences[i]);
//					importList.add(new Importer(dependences[i], cPlugin.getDevice()));
//				}
//			}

//			String[] cssDependences = plugin.calculateCssDependences(false);
//			if(cssDependences != null){
//				for (int i = 0; i < cssDependences.length; i++) {
//					IControlPlugin cPlugin = ControlFramework.getInstance(LfwRuntimeEnvironment.getDevicePhase()).getControlPluginByFullName(cssDependences[i]);
//					cssImportList.add(new Importer(cssDependences[i], cPlugin.getDevice()));
//				}
//			}
			
			String[] idDependences = plugin.calculateDependenceIds();
			if(idDependences != null){
				for (int i = 0; i < idDependences.length; i++) {
					if (!idList.contains(idDependences[i]))
						idList.add(idDependences[i]);
				}
			}
			
			String[] imps = plugin.getImports(false);
			if(imps != null){
				for (int i = 0; i < imps.length; i++) {
					importList.add(new Importer(imps[i], plugin.getDevice()));
				}
			}
			
			String[] cssimps = plugin.getCssImports(false);
			if(cssimps != null){
				for (int i = 0; i < cssimps.length; i++) {
					cssImportList.add(new Importer(cssimps[i], plugin.getDevice()));
				}
			}
			idList.add(plugin.getId());
		}
	}
	
	/**
	 * 
	 * @param isJs
	 * @return 0 not exist, 1 exist in normal path, 2 exist in temp path
	 */
	private String includeSelfDefScript(String fileName) {
		StringBuffer buf = new StringBuffer();
		LfwWindow pm = LfwRuntimeEnvironment.getWebContext().getPageMeta();
		String fp = pm.getFoldPath();
		String appPath = ContextResourceUtil.getCurrentAppPath();
		String ctxPath = LfwRuntimeEnvironment.getRootPath();
		if(fp != null && !fp.equals("")){
			String folderPath = fp.replace("\\", "/");
			String filePath = folderPath + fileName;
			addIncludedSelfDefScript(buf, appPath, ctxPath, filePath, null);
		}
		
		LfwView[] widgets = pm.getViews();
		if(widgets != null){
			for (int i = 0; i < widgets.length; i++) {
				LfwView widget = widgets[i];
				String widgetFolderPath = widget.getFoldPath();
				if(widgetFolderPath != null && !widgetFolderPath.equals("")){
					String filePath = widgetFolderPath + "/" + fileName;
					addIncludedSelfDefScript(buf, appPath, ctxPath, filePath, widgetFolderPath);
				}
			}
		}
		return buf.toString();
	}
	
	private void addIncludedSelfDefScript(StringBuffer buf, String appPath, String ctxPath, String filePath, String widgetFolderPath) {
		String scriptPath = appPath + filePath;
		File scriptFile = new File(scriptPath);
		if(scriptFile.exists()){
			buf.append("<script type='text/javascript' src='" + ctxPath + filePath + "'></script>\n");
			
			if(widgetFolderPath != null){
				String includeMultiPath = widgetFolderPath + "/include.properties";
				String includeRealMultiPath = appPath + includeMultiPath;
				File multiScriptFile = new File(includeRealMultiPath);
				if(multiScriptFile.exists()){
					Properties langResources = AbstractPresentPlugin.loadNodeLangResources(includeMultiPath);
					if(langResources != null){
						String transStr = LangResoTranf.tranfPublicView(langResources);
						String langCode = LfwRuntimeEnvironment.getLangCode();
						String relativePath = WebConstant.TEMP_JSSELFDEFINE_PATH + "widgetpool/" + widgetFolderPath +"/" + langCode;
						//将资源写到临时文件夹里
						writeToTmpFile(transStr, relativePath);
						
						String langResPath = LfwRuntimeEnvironment.getRootPath() + relativePath + "/" + WebConstant.TEMP_JSSELFDEFINE_FILE;
						buf.append("<script type='text/javascript' src='" + langResPath + "'></script>\n");
					}
				}
			}
		}
		
	}
	
	/**
	 * 
	 * @param isJs
	 * @return 0 not exist, 1 exist in normal path, 2 exist in temp path
	 */
	private String includeSelfDefCss(String fileName) {
		StringBuffer buf = new StringBuffer();
		LfwWindow pm = LfwRuntimeEnvironment.getWebContext().getPageMeta();
		String fp = pm.getFoldPath();
		String appPath = ContextResourceUtil.getCurrentAppPath();
		String ctxPath = LfwRuntimeEnvironment.getRootPath();
		if(fp != null && !fp.equals("")){
			String pmFolderPath = fp.replace("\\", "/");
			String filePath = pmFolderPath + fileName;
			addIncludedSelfDefCss(buf, appPath, ctxPath, filePath);
		}
		
		LfwView[] widgets = pm.getViews();
		if(widgets != null){
			for (int i = 0; i < widgets.length; i++) {
				LfwView widget = widgets[i];
				String folderPath = widget.getFoldPath();
				String filePath = folderPath + fileName;
				addIncludedSelfDefCss(buf, appPath, ctxPath, filePath);
			}
		}
		return buf.toString();
	}

	private void addIncludedSelfDefCss(StringBuffer buf, String appPath, String ctxPath, String filePath) {
		String scriptPath = appPath + filePath;
		File scriptFile = new File(scriptPath);
		if(scriptFile.exists())
			buf.append("<link rel='STYLESHEET' type='text/css' href='" + ctxPath + filePath + "'>\n");
	}
	
	
	private void dynamicPress(String pressName, List<String> idList, DevicePhase devicePhase, String lfwPath, String ctxPath, ILfwCache cache){
		String basePath = getFramePathByDevice(devicePhase);
//		if (devicePhase == DevicePhase.PC)
//			basePath = "/frame/device_pc/";
//		else if (devicePhase == DevicePhase.Ipad)
//			basePath = "/frame/device_pad/";
//		String sourceDir = lfwPath  + basePath + CompressUtil.COMPRESS_PATH;
		String targetDir = ctxPath  + basePath + CompressUtil.COMPRESS_PATH;
		File dir = new File(targetDir);
		if (!dir.exists()){
			dir.mkdirs();
		}
		File lockFile = null;
		try{
			lockFile = new File(targetDir + pressName + ".lock");
			//已经有其它线程创建此压缩文件了
			if (lockFile.exists()){
				while (true){
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						LfwLogger.error(e);
					}
					lockFile = new File(targetDir + pressName + ".lock");
					if (!lockFile.exists())
						return;
				}
			}
			try {
				lockFile.createNewFile();
			} catch (IOException e) {
				LfwLogger.error("create lock File:" + targetDir + pressName + ".lock fail", e);
				throw new LfwRuntimeException(e);
			}
			
			File pressFile = new File(targetDir + pressName + ".js");
			if (pressFile.exists() && cache.get(pressName) != null){
				return;
			}
			StringBuffer jsBuf = new StringBuffer();
			Iterator<String> it = idList.iterator();
			while(it.hasNext()){
				String name = it.next();
				IControlPlugin cplugin = ControlFramework.getInstance(LfwRuntimeEnvironment.getDevicePhase()).getControlPluginByType(name);
				basePath = getFramePathByDevice(cplugin.getDevice());
	//			if (cplugin.getDevice() == DevicePhase.PC)
	//				basePath = "/frame/device_pc/";
	//			else if (cplugin.getDevice() == DevicePhase.Ipad)
	//				basePath = "/frame/device_pad/";
				
				String comPressJs = CompressUtil.getCompressedJs(name, lfwPath, basePath);
				jsBuf.append(comPressJs).append("\n");
			}
			basePath = getFramePathByDevice(devicePhase);
			CompressUtil.addCompressedJs(pressName, jsBuf.toString(), ctxPath, basePath);
			//压缩css
			LfwTheme[] themes = LfwThemeManager.getAllThemes();
			for (int j = 0; j < themes.length; j++) {
				LfwTheme theme = themes[j];
				String themeId = theme.getId();
				StringBuffer cssBuf = new StringBuffer();
				it = idList.iterator();
				while(it.hasNext()){
					String name = it.next();
					String comPressCss = CompressUtil.getCompressedCss(name, themeId, lfwPath, basePath);
					cssBuf.append(comPressCss).append("\n");
				}
				basePath = getFramePathByDevice(devicePhase);
				CompressUtil.addCompressedCss(pressName, themeId, cssBuf.toString(), ctxPath, basePath);
			}	
			cache.put(pressName, pressName);
		}
		finally{
			if(lockFile != null)
				lockFile.delete();
		}
	}
	
	private String getFramePathByDevice(DevicePhase device){
		/*
		//v65待确认
		if (device == DevicePhase.Ipad)
			return "/frame/device_pad/";
		else if (device == DevicePhase.Iphone)
			return "/frame/device_phone/";
		else
			return "/frame/device_pc/";
		*/
		return "/frame/device_pc/";
	}
	
	private String getDeviceName(DevicePhase device){
		/*
		//v65待确认
		if (device == DevicePhase.Ipad)
			return "device_pad";
		else if (device == DevicePhase.Iphone)
			return "device_phone";
		else
			return "device_pc";
		*/
		return "device_pc";
	} 
}



///**
// * 
// * 
// */
//class PressThread implements Runnable {
//	protected String pressName = null;
//	protected Set<String> idSet = null;
//	protected DevicePhase devicePhase = null;
//	protected String appPath = null;
//	protected Map<String, String> impPressMap = null;
//	public PressThread() {
//	}
//
//	@Override
//	public void run() {
//		String basePath = null;
//		if (devicePhase == DevicePhase.PC)
//			basePath = "/frame/device_pc/";
//		else if (devicePhase == DevicePhase.Ipad)
//			basePath = "/frame/device_pad/";
//		String jsdir = appPath  + basePath + CompressUtil.COMPRESS_PATH;
//		File pressFile = new File(jsdir + pressName + ".js");
//		if (pressFile.exists() && impPressMap.get(pressName) != null){
//			return;
//		}
//		File lockFile = new File(jsdir + pressName + ".lock");
//		if (lockFile.exists())
//			return;
//		try {
//			lockFile.createNewFile();
//		} catch (IOException e) {
//			//已经被其它线程创建了 直接返回
//			return;
//		}
//		StringBuffer jsBuf = new StringBuffer();
//		Iterator<String> it = idSet.iterator();
//		while(it.hasNext()){
//			String name = it.next();
//			jsBuf.append(CompressUtil.getCompressedJs(name, appPath, basePath)).append("\n");
//		}
//		CompressUtil.addCompressedJs(pressName, jsBuf.toString(), appPath, basePath);
//		//压缩css
//		LfwTheme[] themes = LfwThemeManager.getAllThemes();
//		for (int j = 0; j < themes.length; j++) {
//			LfwTheme theme = themes[j];
//			String themeId = theme.getId();
//			StringBuffer cssBuf = new StringBuffer();
//			it = idSet.iterator();
//			while(it.hasNext()){
//				String name = it.next();
//				cssBuf.append(CompressUtil.getCompressedCss(name, themeId, appPath, basePath)).append("\n");
//			}
//			CompressUtil.addCompressedCss(pressName, themeId, cssBuf.toString(), appPath, basePath);
//		}	
//		impPressMap.put(pressName, pressName);
//		lockFile.delete();
//	}
//}

