package nc.ui.pub.print;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.itf.uap.cil.ICilService;

/**
 * <b>   </b>
 *
 * <p>  
 *
 * </p>
 *
 * Create at 2008-8-13 下午01:16:34
 * 
 * @author bq 
 * @since V5.5
 */

public class PrintCil {

	public PrintCil() {
		super();
	}

	public static boolean isTraining() {
		return  false;
	}
	
	private static boolean isNCDemo() {
		return NCLocator.getInstance().lookup(ICilService.class).isNCDEMO();
	}

	/**
	 * 是否是开发模式
	 */
	private static boolean isDevMode() {
		return RuntimeEnv.getInstance().isDevelopMode();
	}
}
