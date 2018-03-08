package nc.ref.scappt.control;

import uap.lfw.ref.ctrl.LfwSqlGridReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;

public class ScapptGridRefController extends LfwSqlGridReferenceController {
	
	@Override
	public LfwRefSqlVO getGridSqlVO() {
		LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName("scappt_transfer_h");
		vo.setRealTableNames("scappt_transfer_h");
		vo.setOrderByPart("");
		return vo;
	}
}