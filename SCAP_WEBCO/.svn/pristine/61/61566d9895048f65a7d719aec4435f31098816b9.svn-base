package nc.scap.pub.entmatter.util;

import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.DynamicComboData;

public class SecrecyLevelDynamicEnum extends DynamicComboData{
	private static final long serialVersionUID = 7469108932440152330L;
	private final String[] mould_type = new String[] {"∑«±£√‹","±£√‹"};
	@Override
	protected CombItem[] createCombItems() {
		CombItem[] combItems = new CombItem[mould_type.length];
		for (int i = 0; i < mould_type.length; i++) {
			CombItem tempItem = new CombItem();
			tempItem.setText(mould_type[i]);
			tempItem.setValue(i + "");
			combItems[i] = tempItem;
		}
		return combItems;
	}
}
