package nc.scap.pub.sms.flow;

import java.util.Map;

import nc.scap.pub.sms.tools.ScapFlowSmsMessageSender;
import nc.uap.wfm.message.TaskCompletedMessageSender;

/**
 * 流程_任务完成消息发送 短信息
 * @author jizhg
 *
 */
public class TaskCompletedSmsMessSender implements TaskCompletedMessageSender {

	@Override
	public void sendTaskCompletedMessage(Map<String, Object> messageMap) {
		ScapFlowSmsMessageSender.completeTaskMessage(messageMap);
	}

}
