package nc.scap.pub.sms.flow;

import java.util.Map;

import nc.scap.pub.sms.tools.ScapFlowSmsMessageSender;
import nc.uap.wfm.message.TaskOverTimeMessageSender;

/**
 * ����_��ʱ������Ϣ����(��������)  ����Ϣ
 * @author jizhg
 *
 */
public class TaskOvertimeSmsMessSender implements TaskOverTimeMessageSender {

	@Override
	public void sendTaskOverTimeMessage(Map<String, Object> messageMap) {
		ScapFlowSmsMessageSender.overTimeSmsToApprover(messageMap);
	}

}
