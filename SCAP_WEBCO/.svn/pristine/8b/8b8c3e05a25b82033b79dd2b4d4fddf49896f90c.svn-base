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
			throw new LfwRuntimeException("未启用或启用了多个短信扩展点");
		}
		return exts.get(0);
	}

	/**
	 * 直接发送短信
	 * 
	 * @param content 短信内容
	 * @param number 手机号码
	 * @param mdName 模块名称
	 * @param username 发送人名称
	 * @throws Exception
	 */
	public static void send(String content, String number, String mdName, String username) throws Exception {

		ScapSmsTaskVO task = SmsManageService.addTask(content, number, mdName, username);
		getService().send(task);
		SmsManageService.saveTask(task);
	}
}
