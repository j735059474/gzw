package nc.scap.pub.sms.flow;

import java.util.Map;

import nc.scap.pub.sms.tools.ScapFlowSmsMessageSender;
import nc.uap.wfm.message.TaskCreatedMessageSender;

/**
 * ����_�²���������Ϣ����(�Ƶ���) ����Ϣ
 * @author jizhg
 *
 */
public class SmsMessageToCreater implements TaskCreatedMessageSender {

	@Override
	public void sendTaskCteatedMessage(Map<String, Object> messageMap) {
		ScapFlowSmsMessageSender.newTaskToCreater(messageMap);
	}

}
