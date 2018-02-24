package nc.scap.pub.sms.flow;

import java.util.Map;

import nc.scap.pub.sms.tools.ScapFlowSmsMessageSender;
import nc.uap.wfm.message.TaskCreatedMessageSender;

/**
 * 流程_新产生任务信息发送(待处理人) 短消息
 * @author jizhg
 *
 */
public class SmsMessageSender implements TaskCreatedMessageSender {

	@Override
	public void sendTaskCteatedMessage(Map<String, Object> map) {
		ScapFlowSmsMessageSender.newTaskToReceiver(map);
	}

}
