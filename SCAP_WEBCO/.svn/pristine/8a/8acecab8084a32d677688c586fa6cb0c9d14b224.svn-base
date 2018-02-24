package nc.uap.lfw.core.cmd;
import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.scap.pub.baseservice.IUifPubService;
import nc.uap.cpb.log.CpLogger;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

/**
 * 对RichVO的删除逻辑 
 * 
 * 此类继承UifDelCmdRV，解决，在删除操作时提示：数据已被引用，不能删除 问题
 *  @author yhl ，20130812
 *
 */
public class UifDelCmdRVNoValidator extends UifDelCmdRV {
	

	public UifDelCmdRVNoValidator(String masterDsId, boolean trueDel) {
		super(masterDsId, trueDel);
	}

	public UifDelCmdRVNoValidator(String masterDsId) {
		super(masterDsId);
	}

	protected void onDeleteVO(ArrayList<SuperVO> vos, boolean trueDel) {
		try {
			IUifPubService pubService = NCLocator.getInstance().lookup(IUifPubService.class);
			pubService.deleteSuperVOs(vos.toArray(new SuperVO[0]),this.isNotifyBDCache(), trueDel);
		} 
		catch (BusinessException e) {
			CpLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		}
	}
}
