package nc.ref.scapptapplybillcom.control;

import uap.lfw.ref.ctrl.LfwSqlGridReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;

public class ScapptapplybillcomGridRefController extends LfwSqlGridReferenceController {
	
	@Override
	public LfwRefSqlVO getGridSqlVO() {
		LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName("scappt_apply_b");
		vo.setRealTableNames("scappt_apply_b");
		vo.setOrderByPart("");
		return vo;
	}
}