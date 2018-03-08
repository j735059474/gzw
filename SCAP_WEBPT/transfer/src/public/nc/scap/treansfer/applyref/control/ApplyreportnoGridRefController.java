package nc.scap.treansfer.applyref.control;

import uap.lfw.ref.ctrl.LfwSqlGridReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;

public class ApplyreportnoGridRefController extends LfwSqlGridReferenceController {
	
	@Override
	public LfwRefSqlVO getGridSqlVO() {
		LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName("scappt_apply_h");
		vo.setRealTableNames("scappt_apply_h");
		vo.setOrderByPart("");
		return vo;
	}
}