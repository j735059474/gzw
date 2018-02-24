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
 * SCAP����ƽ̨��չ�㹫��ʵ��
 * @author jizhg
 * @�޸� ���꺭 2015-01-27
 */
public class ScapSmsExtentionUtil {

	public static final String SCAPSMSMANAGER = "scapsmsmanager";

	public static List<ISmsExtentionService> getSmsServiceList() {
		
		List<ISmsExtentionService> result = new ArrayList<ISmsExtentionService>();
		
		// �õ��������ù�����չ��
		List<PtExtension> exs = PluginManager.newIns().getExtensions(SCAPSMSMANAGER);
		// û����portal�����
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
	 * ����ScapSmsTaskVO�����������¼�����ݿ�
	 */
	public static void send(ScapSmsTaskVO task) throws Exception {
		
		List<ISmsExtentionService> exts = ScapSmsExtentionUtil.getSmsServiceList();
		if (exts.size() != 1) {
			throw new BusinessException("δ���û������˶��������չ��");
		}
		
		ISmsExtentionService ext = exts.get(0);
		
		StringBuilder msg = new StringBuilder();
		
		try {
			ext.send(task);
        } catch (Exception e) {
        	msg.append(e.getMessage()).append("\n\n");
        	ScapLogger.error("���Ͷ��Ź��̳����쳣��" + e.getMessage(), e);
        }
		
		try {
			ScapDAO.updateVO(task);
			if (task.getSmses() != null) {
				ScapDAO.updateVOArray(task.getSmses());
			}
        } catch (Exception e) {
        	msg.append(e.getMessage()).append("\n\n");
        	ScapLogger.error("��д���Ź��̳����쳣��" + e.getMessage(), e);
        }
		
		if (msg.length() > 0) {
			throw new Exception("���Ͷ��Ź��̳����쳣��" + msg.toString());
		}
	}
}
