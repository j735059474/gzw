package nc.scap.pub.baseservice;

import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.vo.pub.SuperVO;

public interface IUifPubService {
	
	/**
	 * �����ݿ���ɾ��һ��VO����
	 * 
	 * @param supervos
	 * @throws PortalServiceException
	 */
	public void deleteSuperVOs(SuperVO[] vos,boolean isneednotifycache,boolean truedel) throws CpbBusinessException;
	

}
