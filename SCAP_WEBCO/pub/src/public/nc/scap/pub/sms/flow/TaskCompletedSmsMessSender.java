package nc.scap.pub.sms.flow;

import java.util.Map;

import nc.scap.pub.sms.tools.ScapFlowSmsMessageSender;
import nc.uap.wfm.message.TaskCompletedMessageSender;

/**
 * ����_���������Ϣ���� ����Ϣ
 * @author jizhg
 *
 */
public class TaskCompletedSmsMessSender implements TaskCompletedMessageSender {

	@Override
	public void sendTaskCompletedMessage(Map<String, Object> messageMap) {
		ScapFlowSmsMessageSender.completeTaskMessage(messageMap);
	}

}
