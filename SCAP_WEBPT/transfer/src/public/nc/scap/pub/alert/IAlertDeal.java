package nc.scap.pub.alert;

import nc.bs.pub.pa.PreAlertContext;

public interface IAlertDeal {
	/***
	 * 未按时开工的项目预警提示
	 * @param context
	 */
	public void priceDisAert(PreAlertContext context);
}
