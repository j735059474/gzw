package nc.scap.pub.combo;

import nc.scap.def.constant.IDefDocListConstants;
import nc.scap.def.util.ScapDefUtil;
import nc.uap.lfw.core.combodata.CombItem;
import nc.vo.bd.defdoc.DefdocVO;

public class ChartPresetStyleCombo extends AbsDynCombo {

	@Override
	public String getDefCode() {
		return IDefDocListConstants.SCAPFD_TBYZYS;
	}
	
	@Override
	protected CombItem[] createCombItems() {
		String defCode = getDefCode();
		DefdocVO[] vos = ScapDefUtil.getDefDocVosByCode(defCode);
		CombItem[] combItems = new CombItem[vos.length + 1];
		CombItem space = new CombItem();
		space.setText("");
		space.setValue("");
		combItems[0] = space;
		for (int i = 1; i < vos.length + 1; i++) {
			CombItem tempItem = new CombItem();
			tempItem.setText(vos[i-1].getName());
			tempItem.setValue(vos[i-1].getCode());
			combItems[i] = tempItem;
		}
		return combItems;
	}
	
	@Override
	public int getValueType() {
		return super.typeCode;
	}

}
