package nc.scap.pub.news;

import java.util.ArrayList;

import uap.lfw.md.vo.EnumValueVO;
import nc.scap.pub.util.ScapDAO;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.DynamicComboData;

public class NewsTypeComboData extends DynamicComboData {

	@Override
	public CombItem[] getAllCombItems() {
		return createCombItems();
	}

	@Override
	protected CombItem[] createCombItems() {
		CombItem[] result = new CombItem[0];
		
		EnumValueVO[] vos = (EnumValueVO[]) ScapDAO.retrieveByCondition(EnumValueVO.class, "id='d2775e1c-34e8-48cc-a189-9a1605b46ded'");
		if (vos != null) {
			ArrayList<CombItem> resultList = new ArrayList<CombItem>();
			for (int i = 0; i < vos.length; i++) {
				EnumValueVO vo = vos[i];
				if (!"4".equals(vo.getValue())) {
					CombItem item = new CombItem();
					item.setValue(vo.getValue());
					item.setText(vo.getName());
					resultList.add(item);
				}
			}
			result = resultList.toArray(result);
		}
		
		return result;
	}

}
