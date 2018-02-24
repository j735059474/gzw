package nc.scap.pub.combo;

import nc.scap.def.util.ScapDefUtil;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.DynamicComboData;
import nc.uap.portal.log.ScapLogger;
import nc.vo.bd.defdoc.DefdocVO;

public abstract class AbsDynCombo extends DynamicComboData {

	public static int typeCode = 1;
	public static int typePK = 0;

	public CombItem[] getAllCombItems() {
		return createCombItems();
	}

	/**
	 * 动态枚举生成方法
	 * 
	 * @author taoye 2013-7-16
	 */
	protected CombItem[] createCombItems() {
		String defCode = getDefCode();
		DefdocVO[] vos = ScapDefUtil.getDefDocVosByCode(defCode);
		if(vos==null||vos.length==0){
			//AppInteractionUtil.showErrorDialog("根据档案编码:"+defCode+"获取的档案为空，请与管理员联系。");
			ScapLogger.error("根据档案编码:"+defCode+"获取的档案为空，请与管理员联系。");
			return new CombItem[0];
		}
		CombItem[] combItems = new CombItem[vos.length];
		if (getValueType() == typePK) {
			for (int i = 0; i < vos.length; i++) {
				CombItem tempItem = new CombItem();
				tempItem.setText(vos[i].getName());
				tempItem.setValue(vos[i].getPk_defdoc());
				combItems[i] = tempItem;
			}
		} else if (getValueType() == typeCode) {
			for (int i = 0; i < vos.length; i++) {
				CombItem tempItem = new CombItem();
				tempItem.setText(vos[i].getName());
				tempItem.setValue(vos[i].getCode());
				combItems[i] = tempItem;
			}
		}
		return combItems;
	}

	/**
	 * 传入 自定义档案编码
	 * 
	 * @return
	 */
	public abstract String getDefCode();

	/**
	 * 动态枚举生成类型
	 * 
	 * @return
	 */
	public int getValueType() {
		return typePK;
	}
}
