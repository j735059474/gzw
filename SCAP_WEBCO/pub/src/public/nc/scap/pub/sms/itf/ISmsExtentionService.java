package nc.scap.pub.sms.itf;

import nc.scap.pub.vos.ScapSmsTaskVO;
import nc.scap.pub.vos.ScapSmsVO;

/**
 * ������չ����
 * @�޸� ���꺭 2015-01-26
 */
public interface ISmsExtentionService {

	/**
	 * ֻ���ù�Ӧ�̽ӿڣ����ݷ��ؽ������task״̬���������ݽ��и���
	 */
	public void send(ScapSmsTaskVO task) throws Exception;

	/**
	 * ֻ���ù�Ӧ�̽ӿڣ����ݷ��ؽ������sms״̬���������ݽ��и���
	 */
	public void send(ScapSmsVO sms) throws Exception;
}
