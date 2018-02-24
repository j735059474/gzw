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
 * ��̨����ʽ���÷��Ͷ��Žӿ�
 * @author ydyanyh
 */
public class SendSmsPlugin implements IBackgroundWorkPlugin {
	
	@Override
	public PreAlertObject executeTask(BgWorkingContext ctx) throws BusinessException {
		
		List<ISmsExtentionService> exts = ScapSmsExtentionUtil.getSmsServiceList();
		if (exts.size() != 1) {
			throw new BusinessException("δ���û������˶��������չ��");
		}
		
		ISmsExtentionService ext = exts.get(0);
		
		// ���ұ���δ��ɵ�������δ���ͺͷ���ʧ�ܵĶ���
		ScapSmsTaskVO[] tasks = SmsManageService.findTasks(
		        new String[] { ScapSmsTaskStatusEnum.NOT_FINISHED },
		        new String[] { ScapSmsStatusEnum.NOT_SENT, ScapSmsStatusEnum.FAILED });
		
		StringBuilder msg = new StringBuilder();
		for (ScapSmsTaskVO task : tasks) {
			try {
				ext.send(task);
            } catch (Exception e) {
            	msg.append(e.getMessage()).append("\n\n");
            	ScapLogger.error("���Ͷ��Ź��̳����쳣��" + e.getMessage(), e);
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
            	ScapLogger.error("��д���Ź��̳����쳣��" + e.getMessage(), e);
            }
        }
		
		if (msg.length() > 0) {
			throw new BusinessException("���Ͷ��Ź��̳����쳣��" + msg.toString());
		}
	
		return null;
	}

}
