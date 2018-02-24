package nc.uap.portal.log;

import java.text.SimpleDateFormat;
import java.util.Date;

import nc.bs.logging.Log;
import nc.uap.lfw.core.log.LfwSysOutWrapper;
import uap.lfw.core.factory.LfwMultiSysFactory;

public class ScapLogger implements IScapLogger {
	private static Log logger = Log.getInstance("scap");
	private static boolean develop = LfwMultiSysFactory.getMultiSysFactory().isDevMode();
	public static void info(String msg){
//		if(develop)
//			LfwSysOutWrapper.println("portal_info:" + msg);
		console(msg);
		logger.info(msg);
	}
	
	public static void console(String msg){
		if(develop) {
			LfwSysOutWrapper.println("scap_debug:" + msg);
		}
		debug(msg);
	}
	public static void debug(String msg){
		if(logger.isDebugEnabled())
			logger.debug(getMsgBeginStr() + msg);
	}
	
	public static void error(String msg, Throwable t){
		if(develop) {
			LfwSysOutWrapper.println("scap_error:" + msg);
			LfwSysOutWrapper.println("scap_error:" + t.getClass().getName() + t.getCause());
		}
		logger.error(getMsgBeginStr() + msg, t);
	}
	
	public static void error(String msg){
		if(develop)
			LfwSysOutWrapper.println("scap_error:" + msg);
		logger.error(getMsgBeginStr() + msg);
	}
	
	public static void error(Throwable e){
		if(develop)
			LfwSysOutWrapper.println("scap_error:" + e.getCause());
		logger.error(e.getMessage(), e);
	}
	
	private static String getMsgBeginStr() {
		String startStr = "scap_error(" + 
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "): ";
		return startStr;
	}
	
	public static void warn(String msg) {
		logger.warn(msg);
	}
	
	public static boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	public static boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}


	public static boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}
}
