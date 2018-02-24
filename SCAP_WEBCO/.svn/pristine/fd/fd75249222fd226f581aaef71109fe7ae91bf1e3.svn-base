package nc.scap.pub.notice_manager;

import java.util.ArrayList;
import java.util.List;

import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.notice.util.NoticeUtil;
import nc.scap.pub.util.CntUtil;
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.DynamicComboData;

import org.apache.commons.lang.StringUtils;

import uap.web.bd.pub.AppUtil;

/**
 * nc.scap.pub.notice_manager.MessageTypeEnum
 * @author admin
 *
 */
public class MessageTypeNoticeEnum extends DynamicComboData{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5448007562424955555L;

	@Override
	protected CombItem[] createCombItems() {
		List<CombItem> combItems = new ArrayList<CombItem>();
		String pkUser = CntUtil.getCntUserPk();
		// messagecode List
		List<String> messageTypeList = NoticeUtil.selectMessageTypeByPkUser(pkUser);
		CombItem itemStandard = new CombItem();	
		itemStandard.setValue(IGlobalConstants.STANDARD_MESSAGE_TYPE_CODE);
		itemStandard.setText(IGlobalConstants.STANDARD_MESSAGE_TYPE_NAME);
		combItems.add(itemStandard);
		if (messageTypeList != null && messageTypeList.size() == 0) {
			return combItems.toArray(new CombItem[0]);
		} else {
			for (String type : messageTypeList) {
				CombItem item = new CombItem();	
				item.setValue(type);
				item.setText(NoticeUtil.getMessageTypeNameByCode(type));
				combItems.add(item);
			}
			String tzxf_message_type_code = (String)AppUtil.getAppAttr(IGlobalConstants.TZXF_MESSAGE_TYPE_CODE);
			if (StringUtils.isNotBlank(tzxf_message_type_code)) {
				String[] messageTypeCode = tzxf_message_type_code.substring(1,
						tzxf_message_type_code.length() - 1).split(",");
				if (messageTypeCode != null && messageTypeCode.length > 0) {
					for (int i = 0; i < messageTypeCode.length; i++) {
						if (!messageTypeList.contains(messageTypeCode[i]) ) {
							CombItem item = new CombItem();	
							item.setValue(messageTypeCode[i]);
							item.setText(NoticeUtil.getMessageTypeNameByCode(messageTypeCode[i]));
							combItems.add(item);
						}
					}
				}
			}
		}
		return combItems.toArray(new CombItem[0]);
	}

}
