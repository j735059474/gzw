package nc.scap.pub.sms.flow;

import java.util.Map;

import nc.scap.pub.sms.tools.ScapFlowSmsMessageSender;
import nc.uap.wfm.message.TaskOverTimeMessageSender;

/**
 * 流程_超时任务消息发送(待处理人)  短消息
 * @author jizhg
 *
 */
public class TaskOvertimeSmsMessSender implements TaskOverTimeMessageSender {

	@Override
	public void sendTaskOverTimeMessage(Map<String, Object> messageMap) {
		ScapFlowSmsMessageSender.overTimeSmsToApprover(messageMap);
	}

}
