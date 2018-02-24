package nc.scap.pub.util;

import java.io.File;
import java.io.IOException;

import nc.bs.framework.common.RuntimeEnv;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.common.WebConstant;
import nc.uap.portal.log.PortalLogger;
import nc.uap.portal.util.freemarker.FreeMarkerTools;
import freemarker.cache.CacheStorage;
import freemarker.cache.StrongCacheStorage;
import freemarker.template.Configuration;

public class FreeMarkerUtil {
	
	private static Configuration config;
	
	/**
	 * 
	 * @return
	 */
	public static Configuration getFreeMarkerCfg() {
		if (config == null) {
			synchronized(FreeMarkerTools.class){
				if (config == null) {
					String ftlFolder = getTplHome();
					config = new Configuration();
					if(WebConstant.MODE_PRODUCTION.equals(LfwRuntimeEnvironment.getMode())){
						CacheStorage storage = new StrongCacheStorage();
						config.setCacheStorage(storage);
					}
					config.setDefaultEncoding("UTF-8");
					try {
						File dir = new File(ftlFolder);
						config.setDirectoryForTemplateLoading(dir);
					} catch (IOException e) {
						PortalLogger.error(e.getMessage(), e.getCause());
					}
				}
			}
		}
		return config;
	}

	/**
	 * 
	 * @return
	 */
	public static String getTplHome() {
		return RuntimeEnv.getInstance().getNCHome() + "/hotwebs/";
	}

}
