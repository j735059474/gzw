package nc.scap.transfer.imp;

import java.util.List;

import nc.bs.dao.DAOException;
import nc.scap.pt.vos.ScapptTransferAssessVO;
import nc.scap.pt.vos.ScapptTransferHVO;
import nc.scap.pub.util.ScapDAO;
import nc.scap.transfer.itf.ScapptImportitf;
import nc.uap.lfw.core.exception.LfwRuntimeException;

public class ScapptImportImp implements ScapptImportitf {

	@Override
	public void save(List<ScapptTransferHVO> insertvos,
			List<ScapptTransferHVO> updatevos,
			List<ScapptTransferAssessVO> childistvos,
			List<ScapptTransferAssessVO> childudtvos) {
		try {
			ScapDAO.getBaseDAO().insertVOList(insertvos);
			ScapDAO.getBaseDAO().insertVOList(childistvos);
			ScapDAO.getBaseDAO().updateVOList(updatevos);
			ScapDAO.getBaseDAO().updateVOList(childudtvos);
		} catch (DAOException e) {
			throw new LfwRuntimeException("数据批量保存有误！" + e.getMessage());
		}

	}

}
