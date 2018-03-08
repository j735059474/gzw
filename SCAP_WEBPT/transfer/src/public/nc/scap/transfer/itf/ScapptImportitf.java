package nc.scap.transfer.itf;

import java.util.List;

import nc.scap.pt.vos.ScapptTransferAssessVO;
import nc.scap.pt.vos.ScapptTransferHVO;

public interface ScapptImportitf {
	/**
	 * ÅúÁ¿±£´æ
	 * @param insertvos
	 * @param updatevos
	 * @param childistvos
	 * @param childudtvos
	 */
	public void save(List<ScapptTransferHVO> insertvos,
			List<ScapptTransferHVO> updatevos,
			List<ScapptTransferAssessVO> childistvos,
			List<ScapptTransferAssessVO> childudtvos);
}
