package nc.scap.pub.sms.tools;

import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.scap.pub.sms.SmsManageService;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.itf.ICpUserQry;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.portal.log.ScapLogger;
import nc.uap.wfm.model.Task;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.pub.BusinessException;

public class ScapFlowSmsMessageSender {

	public static String msgType = "FLOWTYPE";
			
	public static String newTaskTitle = "新任务提醒";
	
	public static String finishTaskTitle = "任务完成提醒";
	
	public static String overTimeTaskTitle = "任务超时提醒";
	/**
	 * 新任务消息给创建者
	 * @param map
	 */
	public static void newTaskToCreater(Map<String, Object> map) {

		String phoneNum = getTaskCreater(map);
		sendSmsMessage(map, msgType, newTaskTitle, phoneNum);
		
	}

	/**
	 * 新任务消息给处理人
	 * @param map
	 */
	public static void newTaskToReceiver(Map<String, Object> map) {
		String phoneNum = getTaskApprover(map);
		sendSmsMessage(map, msgType, newTaskTitle, phoneNum);
	}

	/**
	 * 任务完成消息
	 * @param map
	 */
	public static void completeTaskMessage(Map<String, Object> map) {

		String phoneNum = getTaskCreater(map);
		sendSmsMessage(map, msgType, finishTaskTitle, phoneNum);
	}

	/**
	 * 任务超时消息
	 * @param map
	 */
	public static void overTimeSmsToApprover(Map<String, Object> map) {
		
		String phoneNum = getTaskApprover(map);
		sendSmsMessage(map, msgType, overTimeTaskTitle, phoneNum);
	}

	/**
	 * 任务超时给创建者
	 * @param map
	 */
	public static void overTimeSmsToCreater(Map<String, Object> map) {
		
		String phoneNum = getTaskCreater(map);
		sendSmsMessage(map, msgType, overTimeTaskTitle, phoneNum);
	}
	
	/**
	 * 获取任务创建人手机号
	 * @param map
	 * @return
	 */
	private static String getTaskCreater(Map<String, Object> map){
		
		String pk_user = (String) map.get("ReceiverUser");
		String phoneNum = null;
		try {
			if(pk_user != null){
				CpUserVO user = NCLocator.getInstance().lookup(ICpUserQry.class).getGlobalUserByPk(pk_user);
				if(user != null){
					String pk = user.getPk_base_doc();
					PsndocVO psn = (PsndocVO) new BaseDAO().retrieveByPK(PsndocVO.class, pk);
					if(psn!=null && psn.getMobile()!=null){
						phoneNum = psn.getMobile();
					}else{
						throw new LfwRuntimeException("获取任务接收人手机号失败，请检查手机号设置");
					}
				}
			}
		} catch (CpbBusinessException e) {
			ScapLogger.error("====获取任务创建人手机号码出现CpbBusinessException异常："+e.getMessage());
			throw new LfwRuntimeException("获取任务接收人手机号失败，请与管理员联系");
		} catch (BusinessException e) {
			ScapLogger.error("====获取任务创建人手机号码出现CpbBusinessException异常："+e.getMessage());
			throw new LfwRuntimeException("获取任务接收人手机号失败，请与管理员联系");
		}
		return phoneNum;
	}
	
	/**
	 * 获取任务接收人手机号
	 * @param map
	 * @return
	 */
	private static String getTaskApprover(Map<String, Object> map){

		String phoneNum = null;
		if(map.get("CurrentTask")!=null){
			Task task = (Task) map.get("CurrentTask");
			String pk_user = task.getPk_owner();
			try {
				if(pk_user != null){
					CpUserVO user = NCLocator.getInstance().lookup(ICpUserQry.class).getGlobalUserByPk(pk_user);
					String pk = user.getPk_base_doc();
					PsndocVO psn = (PsndocVO) new BaseDAO().retrieveByPK(PsndocVO.class, pk);
					if(psn!=null && psn.getMobile()!=null){
						phoneNum = psn.getMobile();
					}else{
						throw new LfwRuntimeException("获取任务接收人手机号失败，请检查手机号设置");
					}
				}
			} catch (CpbBusinessException e) {
				ScapLogger.error("====获取任务接收人手机号码出现CpbBusinessException异常："+e.getMessage());
				throw new LfwRuntimeException("获取任务接收人手机号失败，请与管理员联系");
			} catch (BusinessException e) {
				ScapLogger.error("====获取任务接收人手机号码出现BusinessException异常："+e.getMessage());
				throw new LfwRuntimeException("获取任务接收人手机号失败，请与管理员联系");
			}
		}
		return phoneNum;
	}
	
	private static void sendSmsMessage(Map<String, Object> map,String taskType,String taskTitle,String phoneNum){
		
		String content = (String) map.get("content");
		SmsManageService.addTask(content, phoneNum, msgType, null);
	}
}
