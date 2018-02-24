package nc.uap.portal.util.freemarker.functions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nc.bs.framework.core.conf.Configuration;
import nc.bs.framework.server.BusinessAppServer;
import nc.bs.framework.server.ServerConfiguration;
import nc.scap.pub.itf.IScapFileConstants;
import nc.scap.pub.util.CntUtil;
import nc.scap.sysinit.constant.ISysinitConstant;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.lfw.util.LanguageUtil;
import nc.uap.portal.constant.PortalEnv;
import nc.uap.portal.om.Page;
import nc.uap.portal.util.PortalRenderEnv;

import org.apache.commons.lang.StringUtils;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 获得系统参数(FreeMarker模板方法)
 * 
 * @author licza
 * 
 */
public class Env implements TemplateMethodModel {
 	private static final String WEB = "web";
 	private static final String CTX = "ctx";
 	private static final String TITLE = "title";
 	private static final String COMM = "comm";
	@Override
	public Object exec(final List arg) throws TemplateModelException {
		final Map<String, Object> env = new HashMap<String, Object>();
		String context = "/portal";
		env.put(WEB, context);
		if(LfwRuntimeEnvironment.getWebContext() != null){
			HttpServletRequest request = LfwRuntimeEnvironment.getWebContext().getRequest();
			String ctxpath = request.getScheme()+"://"+request.getServerName()+(request.getServerPort() == 80 ? "":":"+request.getServerPort())+context+"/";
			env.put(CTX, ctxpath);
		}else{
			ServerConfiguration sc = ServerConfiguration.getServerConfiguration();
			String serverURL = sc.getDefWebServerURL();
			Configuration configuration = BusinessAppServer.getInstance().getConfiguration();
			String serverName = configuration.getServerName();
			
			if(serverURL == null || serverURL.equals("http:/")){
				serverURL = configuration.getEndpoint(serverName);;
			}
			String ctxpath = serverURL + "/portal/";
			env.put(CTX, ctxpath);
		}
		/**将当前页面名称放入上下文中**/
		Page page = PortalRenderEnv.getCurrentPage();
		if(page != null){
			String title = page.getTitle();
			if(page.getI18nname() != null && !"".equals(page.getI18nname())){
				String _title = LanguageUtil.getWithDefaultByProductCode(page.getModule(),title, page.getI18nname());
				if(_title != null){
					title = _title;
				}
			}
			env.put(TITLE, title);
		}

		
		env.put(COMM,  "/" + PortalEnv.getPortalCoreName() + "/portalspec/ftl/portaldefine/comm");
		/**
		 * 语种
		 */
		env.put("langcode", LfwRuntimeEnvironment.getLangCode());
//		boolean isGZW = CntUtil.CtnUserIsCompanyUser();
//		if(isGZW){
//			env.put("logoType", "company");
//		}else{
//			env.put("logoType", "gzw");
//		}
		Page currentPage = PortalRenderEnv.getCurrentPage();
		String skin = null;
		if(currentPage == null){
			skin = "webclassic";
		}
		else
			skin = LfwRuntimeEnvironment.getThemeId();
		if(StringUtils.isBlank(skin))
			skin = "webclassic";
		env.put(WebConstant.THEME_ID, skin);
		env.put("designMode", PortalRenderEnv.isDesignMode());
	 
		//增加省份ID获取
		String proId = ScapSysinitUtil.getSysinitStrByCode(ISysinitConstant.PROVINCE_ID);
		proId = proId.toLowerCase();
		env.put("proId", proId);
		return env;
	}
}
