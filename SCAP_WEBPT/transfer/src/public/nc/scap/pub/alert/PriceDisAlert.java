package nc.scap.pub.alert;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.IPreAlertPlugin;
import nc.bs.pub.pa.PreAlertContext;
import nc.bs.pub.pa.PreAlertObject;
import nc.vo.pub.BusinessException;

public class PriceDisAlert implements IPreAlertPlugin {

	@Override
	public PreAlertObject executeTask(PreAlertContext context)
			throws BusinessException {
		IAlertDeal al = NCLocator.getInstance().lookup(IAlertDeal.class);
		al.priceDisAert(context);
		return null;
	}

}
