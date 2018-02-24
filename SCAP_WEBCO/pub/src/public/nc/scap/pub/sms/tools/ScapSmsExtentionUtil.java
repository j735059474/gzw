package nc.scap.pub.sms.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.scap.pub.sms.itf.ISmsExtentionService;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.ScapSmsTaskVO;
import nc.uap.cpb.log.CpLogger;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.itf.IPtPluginQryService;
import nc.uap.portal.log.ScapLogger;
import nc.uap.portal.plugins.PluginManager;
import nc.uap.portal.plugins.model.PtExtension;
import nc.vo.pub.BusinessException;

/**
 * SCAP短信平台扩展点公共实现
 * @author jizhg
 * @修改 晏雨涵 2015-01-27
 */
public class ScapSmsExtentionUtil {

	public static final String SCAPSMSMANAGER = "scapsmsmanager";

	public static List<ISmsExtentionService> getSmsServiceList() {
		
		List<ISmsExtentionService> result = new ArrayList<ISmsExtentionService>();
		
		// 得到短信配置管理扩展点
		List<PtExtension> exs = PluginManager.newIns().getExtensions(SCAPSMSMANAGER);
		// 没启动portal情况下
		if (exs == null || exs.size() == 0) {
			IPtPluginQryService pluginQry = NCLocator.getInstance().lookup(IPtPluginQryService.class);
			PtExtension[] extensions = null;
			try {
				extensions = pluginQry.getExtensionByPoint(SCAPSMSMANAGER);
			} catch (CpbBusinessException e) {
				CpLogger.error(e.getMessage(), e);
			}
			if (extensions != null && extensions.length > 0)
				exs = Arrays.asList(extensions);
		}
		if (exs == null || exs.size() == 0) {
			return result;
		}
		for (PtExtension ex : exs) {
			ISmsExtentionService smsExtService = null;
			smsExtService = (ISmsExtentionService) ex.newInstance();
			result.add(smsExtService);
		}
		
		return result;
	}
	
	/**
	 * 发送ScapSmsTaskVO，并将结果记录到数据库
	 */
	public static void send(ScapSmsTaskVO task) throws Exception {
		
		List<ISmsExtentionService> exts = ScapSmsExtentionUtil.getSmsServiceList();
		if (exts.size() != 1) {
			throw new BusinessException("未启用或启用了多个短信扩展点");
		}
		
		ISmsExtentionService ext = exts.get(0);
		
		StringBuilder msg = new StringBuilder();
		
		try {
			ext.send(task);
        } catch (Exception e) {
        	msg.append(e.getMessage()).append("\n\n");
        	ScapLogger.error("发送短信过程出现异常：" + e.getMessage(), e);
        }
		
		try {
			ScapDAO.updateVO(task);
			if (task.getSmses() != null) {
				ScapDAO.updateVOArray(task.getSmses());
			}
        } catch (Exception e) {
        	msg.append(e.getMessage()).append("\n\n");
        	ScapLogger.error("回写短信过程出现异常：" + e.getMessage(), e);
        }
		
		if (msg.length() > 0) {
			throw new Exception("发送短信过程出现异常：" + msg.toString());
		}
	}
}
