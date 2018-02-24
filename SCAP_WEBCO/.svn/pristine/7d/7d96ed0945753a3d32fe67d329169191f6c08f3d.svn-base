package nc.scap.pub.sms;

import java.util.List;

import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.scap.pub.sms.itf.ISmsExtentionService;
import nc.scap.pub.sms.tools.ScapSmsExtentionUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.ScapSmsStatusEnum;
import nc.scap.pub.vos.ScapSmsTaskStatusEnum;
import nc.scap.pub.vos.ScapSmsTaskVO;
import nc.uap.portal.log.ScapLogger;
import nc.vo.pub.BusinessException;

/**
 * 后台任务方式调用发送短信接口
 * @author ydyanyh
 */
public class SendSmsPlugin implements IBackgroundWorkPlugin {
	
	@Override
	public PreAlertObject executeTask(BgWorkingContext ctx) throws BusinessException {
		
		List<ISmsExtentionService> exts = ScapSmsExtentionUtil.getSmsServiceList();
		if (exts.size() != 1) {
			throw new BusinessException("未启用或启用了多个短信扩展点");
		}
		
		ISmsExtentionService ext = exts.get(0);
		
		// 查找遍历未完成的任务及其未发送和发送失败的短信
		ScapSmsTaskVO[] tasks = SmsManageService.findTasks(
		        new String[] { ScapSmsTaskStatusEnum.NOT_FINISHED },
		        new String[] { ScapSmsStatusEnum.NOT_SENT, ScapSmsStatusEnum.FAILED });
		
		StringBuilder msg = new StringBuilder();
		for (ScapSmsTaskVO task : tasks) {
			try {
				ext.send(task);
            } catch (Exception e) {
            	msg.append(e.getMessage()).append("\n\n");
            	ScapLogger.error("发送短信过程出现异常：" + e.getMessage(), e);
            }
        }
		
		for (ScapSmsTaskVO task : tasks) {
			try {
				ScapDAO.updateVO(task);
				if (task.getSmses() != null) {
					ScapDAO.updateVOArray(task.getSmses());
				}
            } catch (Exception e) {
            	msg.append(e.getMessage()).append("\n\n");
            	ScapLogger.error("回写短信过程出现异常：" + e.getMessage(), e);
            }
        }
		
		if (msg.length() > 0) {
			throw new BusinessException("发送短信过程出现异常：" + msg.toString());
		}
	
		return null;
	}

}
