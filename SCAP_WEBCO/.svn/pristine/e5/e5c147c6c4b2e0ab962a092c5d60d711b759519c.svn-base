package nc.scap.pub.baseservice.impl;

import nc.scap.pub.baseservice.IUifPubService;
import nc.uap.cpb.baseservice.MmtBaseService;
import nc.uap.cpb.baseservice.bd.BDCacheCPMultiManageTypeBaseService;
import nc.uap.cpb.baseservice.bd.CPMultiManageTypeBaseService;
import nc.uap.cpb.baseservice.util.VersionConflictException;
import nc.uap.cpb.baseservice.validation.Validator;
import nc.uap.cpb.log.CpLogger;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

public class UifPubServiceImpl implements IUifPubService{

	@Override
	public void deleteSuperVOs(SuperVO[] vos, boolean isneednotifycache,
			boolean truedel) throws CpbBusinessException {
		if(vos == null || vos.length < 1) return;
		MmtBaseService<SuperVO> service =null;
		if(isneednotifycache){
			service = new BDCacheCPMultiManageTypeBaseService(vos[0].getClass(),null);
		}
		else{
			service = new CPMultiManageTypeBaseService(vos[0].getClass(),null){
				@Override   //删除时不校验主表数据已被子表数据引用，直接删除主子表数据 yhl20130812
				protected Validator[] getDeleteValidator() {
					return null;
				}
			};
		}
		try {
			service.setTrueDel(truedel);
			service.deleteVO(vos);
		}catch (VersionConflictException e) {
			CpLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException((String)e.getBusiObject());
		}catch (BusinessException e) {
			CpLogger.error(e.getMessage(), e);
			throw new CpbBusinessException(e.getMessage());
		}
	}

}
