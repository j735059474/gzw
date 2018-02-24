package nc.scap.pub.sms.flow;

import java.util.Map;

import nc.scap.pub.sms.tools.ScapFlowSmsMessageSender;
import nc.uap.wfm.message.TaskCreatedMessageSender;

/**
 * 流程_新产生任务信息发送(制单人) 短消息
 * @author jizhg
 *
 */
public class SmsMessageToCreater implements TaskCreatedMessageSender {

	@Override
	public void sendTaskCteatedMessage(Map<String, Object> messageMap) {
		ScapFlowSmsMessageSender.newTaskToCreater(messageMap);
	}

}
