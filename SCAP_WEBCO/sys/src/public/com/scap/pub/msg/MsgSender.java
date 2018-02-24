package com.scap.pub.msg;

import java.util.ArrayList;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.message.util.MessageCenter;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

public class MsgSender {

	public void sendScapMsg(String filePath) throws BusinessException {
		// 对消息接收者发送业务消息
		ArrayList<String> alUserPK = new ArrayList<String>();
		
		if (alUserPK == null || alUserPK.size() == 0) {
			return;
		}
		
		StringBuffer sb = new StringBuffer();
		
		for (String pk : alUserPK) {
			sb.append(",");
			sb.append(pk);
		}
		
		String receivers = sb.substring(1);
		
		NCMessage ncmsg = new NCMessage();
		MessageVO msgVO = new MessageVO();
		
		msgVO.setSubject("message title");
		msgVO.setSender("messagesender");
		msgVO.setReceiver(receivers);
		msgVO.setMsgsourcetype("pareport");
		msgVO.setContenttype("BIZ");
		msgVO.setSendtime(new UFDateTime());
 		msgVO.setIshandled(new UFBoolean(false));
 		msgVO.setPk_group(InvocationInfoProxy.getInstance().getGroupId());
 		msgVO.setDetail("messagedetail");
 		
		
		ncmsg.setMessage(msgVO);
		
		try {
			MessageCenter.sendMessage(new NCMessage[] {ncmsg});
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}
}
