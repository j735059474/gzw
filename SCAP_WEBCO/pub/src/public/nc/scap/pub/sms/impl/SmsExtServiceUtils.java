package nc.scap.pub.sms.impl;

import java.util.List;

import nc.scap.pub.sms.SmsManageService;
import nc.scap.pub.sms.itf.ISmsExtentionService;
import nc.scap.pub.sms.tools.ScapSmsExtentionUtil;
import nc.scap.pub.vos.ScapSmsTaskVO;
import nc.uap.lfw.core.exception.LfwRuntimeException;

public class SmsExtServiceUtils {

	public static ISmsExtentionService getService() {
		List<ISmsExtentionService> exts = ScapSmsExtentionUtil.getSmsServiceList();
		if (exts.size() != 1) {
			throw new LfwRuntimeException("δ���û������˶��������չ��");
		}
		return exts.get(0);
	}

	/**
	 * ֱ�ӷ��Ͷ���
	 * 
	 * @param content ��������
	 * @param number �ֻ�����
	 * @param mdName ģ������
	 * @param username ����������
	 * @throws Exception
	 */
	public static void send(String content, String number, String mdName, String username) throws Exception {

		ScapSmsTaskVO task = SmsManageService.addTask(content, number, mdName, username);
		getService().send(task);
		SmsManageService.saveTask(task);
	}
}
