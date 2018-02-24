package nc.scap.pub.workreport.combo;

import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.DynamicComboData;
import nc.vo.pub.lang.UFDate;

public class SelYearCombo extends DynamicComboData {

	@Override
	protected CombItem[] createCombItems() {
		//获取当前系统的时间
		int year = new UFDate().getYear();
		CombItem[] items = new CombItem[11];
		items[0] = new CombItem("","","");
		for(int i = 1;i < items.length ;i++){
			items[i] = new CombItem(String.valueOf(year-5+i),(year-5+i)+"",(year-5+i)+"");
		}
		return items;
	}

}
