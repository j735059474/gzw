package nc.scap.pub.sms.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nc.scap.pub.sms.itf.ISmsExtentionService;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.ScapSmsStatusEnum;
import nc.scap.pub.vos.ScapSmsTaskStatusEnum;
import nc.scap.pub.vos.ScapSmsTaskVO;
import nc.scap.pub.vos.ScapSmsVO;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.portal.log.ScapLogger;
import nc.vo.pub.lang.UFDateTime;

import org.apache.ws.axis2.SmsServiceStub;

/**
 * 海南短信平台扩展点
 * @author jizhg
 *
 */
public class HNSmsExtServiceImpl implements ISmsExtentionService {

	@Override
    public void send(ScapSmsTaskVO task) throws Exception {
	   
		UFDateTime taskSendtime = new UFDateTime(System.currentTimeMillis());
		Integer taskSendtimes = task.getSendtimes();
		if (taskSendtimes == null) {
			taskSendtimes = 0;
		}
		task.setSendtime(taskSendtime);
		task.setSendtimes(++taskSendtimes);
		task.setSendstatus(ScapSmsTaskStatusEnum.FINISHED);
		
		ScapSmsVO[] smses = task.getSmses();
		if (smses != null) {
			// 准备调用webservice的参数
			String content = task.getContent();
			String groupid = "143054240";
			String user = "9890";
			String password = "123123";
			String sendUser = "海南省国有资产监督管理委员会";
			if (task.getUsername() != null && task.getUsername().length() > 0) {
				sendUser = task.getUsername();
			}
			String timeStamp = new SimpleDateFormat("MMddHHmmss").format(new Date());		//时间戳
			SmsServiceStub.SendMessage sendMessage = new SmsServiceStub.SendMessage();
			sendMessage.setGroupID(groupid);		//groupid
	   		sendMessage.setTimeStamp(timeStamp);	//时间戳
	   		char c = 0;
	   		String str = user + c + c + c + c + c + c + c + password + timeStamp;
	   		String md5 = md5s(str);
	   		sendMessage.setAuthenticatorClient(md5);	//客户端认证码		
	   		String sendTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	   		
	   		Map<String, ScapSmsVO> sendMap = new HashMap<String, ScapSmsVO>();
	   		for (ScapSmsVO sms : smses) {
	   			
//	   			if (ScapSmsStatusEnum.SENT.equals(sms.getSendstatus()) || ScapSmsStatusEnum.SUCCEED.equals(sms.getSendstatus())) {
//					continue;
//				}
	   			
	   			UFDateTime smsSendtime = new UFDateTime(System.currentTimeMillis());
				Integer smsSendtimes = sms.getSendtimes();
				if (smsSendtimes == null) {
					smsSendtimes = 0;
				}
				sms.setSendtime(smsSendtime);
				sms.setSendtimes(++smsSendtimes);
				sms.setSendstatus(ScapSmsStatusEnum.FAILED);
				
				String number = sms.getAddress();
				SmsServiceStub.SmsService_Message message = new  SmsServiceStub.SmsService_Message();
				message.setAppMessageID(number);		      	//应用方流水
				message.setDestTermID(number);                	//短消息接收号码
				message.setMsgContent(content);          		//短信内容
				message.setApplyTime(sendTime);         		//计划发送时间	格式为：YYYYMMDDHHMMSS（年月日时分秒）
				message.setSendMan(sendUser);                   //发送人
				message.setRem1("");                            //备用字段1
				message.setRem2("");                            //备用字段2
				sendMessage.addMessage(message);
				
				sendMap.put(number, sms);
            }
	   		
	   		
	   		// 调用webservice
	   		SmsServiceStub.SmsService_MessageStatus[] statusArray = null;
			try {
				SmsServiceStub stub = new SmsServiceStub();
				SmsServiceStub.SendMessageResponse resp = stub.sendMessage(sendMessage) ; 
				statusArray = resp.get_return();
				for (SmsServiceStub.SmsService_MessageStatus status : statusArray) {
					ScapSmsVO sms = sendMap.get(status.getAppMessageID());
					if ("0".equals(status.getStatus())) {
						sms.setSendstatus(ScapSmsStatusEnum.SENT);
					} else {
						task.setSendstatus(ScapSmsTaskStatusEnum.NOT_FINISHED);
					}
				}
			} catch (Exception e) {
				task.setSendstatus(ScapSmsTaskStatusEnum.NOT_FINISHED);
				LfwRuntimeException e1 = new LfwRuntimeException("调用发送短信Webservice出现异常", e);
				ScapLogger.error(e1);
			}
		}
    }

	@Override
    public void send(ScapSmsVO sms) throws Exception {
	    
//		if (ScapSmsStatusEnum.SENT.equals(sms.getSendstatus()) || ScapSmsStatusEnum.SUCCEED.equals(sms.getSendstatus())) {
//			return;
//		}
		
		ScapSmsTaskVO task = (ScapSmsTaskVO) ScapDAO.retrieveByPK(ScapSmsTaskVO.class, sms.getPk_task());
		
		UFDateTime sendtime = new UFDateTime(System.currentTimeMillis());
		Integer sendtimes = sms.getSendtimes();
		if (sendtimes == null) {
			sendtimes = 0;
		}
		sms.setSendtime(sendtime);
		sms.setSendtimes(++sendtimes);
		sms.setSendstatus(ScapSmsStatusEnum.FAILED);
		
		
		// 调用webservice
		String content = task.getContent();
		String groupid = "143054240";
		String user = "9890";
		String password = "123123";
		String sendUser = "海南省国有资产监督管理委员会";
		if (task.getUsername() != null && task.getUsername().length() > 0) {
			sendUser = task.getUsername();
		}
		String timeStamp = new SimpleDateFormat("MMddHHmmss").format(new Date());		//时间戳
		SmsServiceStub.SendMessage sendMessage = new SmsServiceStub.SendMessage();
		sendMessage.setGroupID(groupid);		//groupid
		sendMessage.setTimeStamp(timeStamp);	//时间戳
		char c = 0;
		String str = user + c + c + c + c + c + c + c + password + timeStamp;
		String md5 = md5s(str);
		sendMessage.setAuthenticatorClient(md5);	//客户端认证码		
		String sendTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		String number = sms.getAddress();
		SmsServiceStub.SmsService_Message message = new  SmsServiceStub.SmsService_Message();
		message.setAppMessageID(number);		      	//应用方流水
		message.setDestTermID(number);                	//短消息接收号码
		message.setMsgContent(content);          		//短信内容
		message.setApplyTime(sendTime);         		//计划发送时间	格式为：YYYYMMDDHHMMSS（年月日时分秒）
		message.setSendMan(sendUser);                   //发送人
		message.setRem1("");                            //备用字段1
		message.setRem2("");                            //备用字段2
		sendMessage.addMessage(message);
		
   		SmsServiceStub.SmsService_MessageStatus[] statusArray = null;
		try {
			SmsServiceStub stub = new SmsServiceStub();
			SmsServiceStub.SendMessageResponse resp = stub.sendMessage(sendMessage) ; 
			statusArray = resp.get_return();
			for (SmsServiceStub.SmsService_MessageStatus status : statusArray) {
				if ("0".equals(status.getStatus())) {
					sms.setSendstatus(ScapSmsStatusEnum.SENT);
				}
			}
		} catch (Exception e) {
			task.setSendstatus(ScapSmsTaskStatusEnum.NOT_FINISHED);
			LfwRuntimeException e1 = new LfwRuntimeException("调用发送短信Webservice出现异常", e);
			ScapLogger.error(e1);
		}
    }
	
	private static String md5s(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i).toUpperCase());
			}
			String str = buf.toString();
			//System.out.println("result: " + buf.toString());// 32位的加密

			return str;
		} catch (NoSuchAlgorithmException e) {
			ScapLogger.debug("MD5加密出错!");
			return "";
		}
	}

}
