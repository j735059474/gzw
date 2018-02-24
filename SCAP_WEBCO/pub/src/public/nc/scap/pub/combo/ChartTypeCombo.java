package nc.scap.pub.combo;

import nc.scap.def.constant.IDefDocListConstants;

public class ChartTypeCombo extends AbsDynCombo {

	@Override
	public String getDefCode() {
		return IDefDocListConstants.SCAPFD_TBLX;
	}
	
	@Override
	public int getValueType() {
		return super.typeCode;
	}

}
