package nc.scap.pub.contacts;

import java.util.ArrayList;
import java.util.List;

import nc.scap.pub.notice.util.NoticeUtil;
import nc.scap.pub.util.CntUtil;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.DynamicComboData;
import nc.vo.bd.defdoc.DefdocVO;

/**
 * nc.scap.pub.contacts.MessageTypeContactsEnum
 * @author admin
 *
 */
public class MessageTypeContactsEnum extends DynamicComboData{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5448007562424955555L;

	@Override
	protected CombItem[] createCombItems() {
		List<CombItem> combItems = new ArrayList<CombItem>();
		String pkUser = CntUtil.getCntUserPk();
		DefdocVO[] vos = NoticeUtil.getMessageTypeAll();
		if (vos != null && vos.length != 0) {
			for (int i = 0; i < vos.length; i++) {
				CombItem item = new CombItem();	
				item.setValue(vos[i].getCode());
				item.setText(vos[i].getName());
				combItems.add(item);
			}
		}
		return combItems.toArray(new CombItem[0]);
	}

}
