package nc.scap.pub.sms.itf;

import nc.scap.pub.vos.ScapSmsTaskVO;
import nc.scap.pub.vos.ScapSmsVO;

/**
 * 短信扩展抽象
 * @修改 晏雨涵 2015-01-26
 */
public interface ISmsExtentionService {

	/**
	 * 只调用供应商接口，根据返回结果设置task状态，不对数据进行更新
	 */
	public void send(ScapSmsTaskVO task) throws Exception;

	/**
	 * 只调用供应商接口，根据返回结果设置sms状态，不对数据进行更新
	 */
	public void send(ScapSmsVO sms) throws Exception;
}
