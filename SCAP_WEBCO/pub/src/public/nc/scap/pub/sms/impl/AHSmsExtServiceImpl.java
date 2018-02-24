package nc.scap.pub.sms.impl;

import java.util.Random;

import nc.scap.pub.sms.itf.ISmsExtentionService;
import nc.scap.pub.sms.service.ah.delegate.MsgWSService;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.ScapSmsStatusEnum;
import nc.scap.pub.vos.ScapSmsTaskStatusEnum;
import nc.scap.pub.vos.ScapSmsTaskVO;
import nc.scap.pub.vos.ScapSmsVO;
import nc.uap.portal.log.ScapLogger;
import nc.vo.pub.lang.UFDateTime;

/**
 * 安徽短信平台扩展点
 * @author jizhg
 * 
 */                                                        
public class AHSmsExtServiceImpl implements ISmsExtentionService {
	                                                       
	// 名字空间                                                
    public static final String targetNamespace = "http://192.168.1.43:9080/ahgzwsms/";
    //服务名                                                  
    public static final String serName = "MsgWSService";   
    //端口名
    public static final String pName = "MsgWSPort";
    //服务地址
    public static final String endpointAddress = "http://192.168.138.43:9080/ahgzwsms/MsgWSPort?wsdl";
    //方法名
    public static final String OPER_NAME = "sendMsgSvr";
    //参数名
    public static final String INPUT_NMAE = "arg0";
    
	@Override
    public void send(ScapSmsTaskVO task) throws Exception {
		
		UFDateTime taskSendtime = new UFDateTime(System.currentTimeMillis());
		Integer taskSendtimes = task.getSendtimes();
		if (taskSendtimes == null) {
			taskSendtimes = 0;
		}
		task.setSendtime(taskSendtime);
		task.setSendtimes(++taskSendtimes);
		
		ScapSmsVO[] smses = task.getSmses();
		if (smses != null) {
			MsgWSService service = new MsgWSService();
			task.setSendstatus(ScapSmsTaskStatusEnum.FINISHED);
			
			for (ScapSmsVO sms : smses) {
				
//				if (ScapSmsStatusEnum.SENT.equals(sms.getSendstatus()) || ScapSmsStatusEnum.SUCCEED.equals(sms.getSendstatus())) {
//					continue;
//				}
				
				UFDateTime smsSendtime = new UFDateTime(System.currentTimeMillis());
				Integer smsSendtimes = sms.getSendtimes();
				if (smsSendtimes == null) {
					smsSendtimes = 0;
				}
				sms.setSendtime(smsSendtime);
				sms.setSendtimes(++smsSendtimes);
				
				String random = "" + Math.abs(new Random().nextInt() % 10000);
				try {
					boolean success = service.getMsgWSPort().sendMsgSvr("502@ahgzw", "", task.getContent(), sms.getAddress(), "省国资委", random);
					if (success) {
						sms.setSendstatus(ScapSmsStatusEnum.SENT);
					} else {
						sms.setSendstatus(ScapSmsStatusEnum.FAILED);
						task.setSendstatus(ScapSmsTaskStatusEnum.NOT_FINISHED);
					}
                } catch (Exception e) {
					sms.setSendstatus(ScapSmsStatusEnum.FAILED);
                	task.setSendstatus(ScapSmsTaskStatusEnum.NOT_FINISHED);
                	ScapLogger.error("调用短信接口出现异常", e);
                }
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
		
		try {
			MsgWSService service = new MsgWSService();
			String random = "" + Math.abs(new Random().nextInt() % 10000);
			boolean success = service.getMsgWSPort().sendMsgSvr("502@ahgzw", "", task.getContent(), sms.getAddress(), "省国资委", random);
			if (success) {
				sms.setSendstatus(ScapSmsStatusEnum.SENT);
			} else {
	        	sms.setSendstatus(ScapSmsStatusEnum.FAILED);
			}
        } catch (Exception e) {
        	sms.setSendstatus(ScapSmsStatusEnum.FAILED);
        	ScapLogger.error("调用短信接口出现异常", e);
        }
	}

/*
	@Override
	public Object sendSmsByTask(ScapSmsTaskVO task) {
		
		// 更新任务状态
		{
			int execTimes = task.getExectimes() == null ? 0 : task.getExectimes();
			task.setExectimes(execTimes + 1);
			task.setLast_exectime(new UFDateTime(System.currentTimeMillis()));
			task.setLast_execstatus(Integer.parseInt(SmsManageService.TASK_STATUS_SUCCESS));
		}
	         
		boolean success = true;
		try {
	        MsgWSService service = new MsgWSService();
	        ScapSmsVO[] smsVos = task.getSmsvos();
	        if (smsVos != null) {
	        	for (ScapSmsVO sms : smsVos) {
	        		String random = "" + Math.abs(new Random().nextInt() % 10000);
	        		boolean result = false;
	        		try {
	        			result = service.getMsgWSPort().sendMsgSvr("502@ahgzw", task.getTitle(), task.getContent(), sms.getPhone_code(), "省国资委", random);
	        		} catch (Exception e) {
	        			ScapLogger.error("调用短信接口出现异常", e);
	        		}
	        		success = success && result;
	        		
	        		int sendTimes = sms.getSendtimes() == null ? 0 : sms.getSendtimes();
	        		sms.setSendtimes(sendTimes + 1);
	        		sms.setLast_sendtime(task.getLast_exectime());
	        		sms.setLast_sendstatus(result ? Integer.parseInt(SmsManageService.SMS_STATUS_SUCCESS) : Integer.parseInt(SmsManageService.SMS_STATUS_FAILURE));
	        	}
	        }
	        task.setLast_execstatus(Integer.parseInt(success ? SmsManageService.TASK_STATUS_SUCCESS: SmsManageService.TASK_STATUS_FAILURE));
        } catch (Exception e) {
        	ScapLogger.error("执行短信任务过程出现异常", e);
        	task.setLast_execstatus(Integer.parseInt(SmsManageService.TASK_STATUS_FAILURE));
        }
		
		// 回写任务和短信状态
		ScapDAO.updateVO(task);
		ScapDAO.updateVOArray(task.getSmsvos());
		
		return success ? "发送成功" : "发送失败";
	}

	@Override
	public Object sendSMS(HashMap<String, String> map) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void processArgs() {
		// TODO 自动生成的方法存根
		
	}

*/	
}
