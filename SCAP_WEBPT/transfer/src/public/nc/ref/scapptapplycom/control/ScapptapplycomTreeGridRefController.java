package nc.ref.scapptapplycom.control;

import uap.lfw.ref.ctrl.LfwSqlTreeGridReferneceController;
import uap.lfw.ref.vo.LfwRefSqlVO;

public class ScapptapplycomTreeGridRefController extends LfwSqlTreeGridReferneceController {
	
	@Override
	public LfwRefSqlVO getGridSqlVO() {
		LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName("scappt_apply_b,scappt_apply_h");
		vo.setRealTableNames("scappt_apply_b,scappt_apply_h");
		vo.setOrderByPart("");
		return vo;
	}
	
	@Override
	public LfwRefSqlVO getTreeSqlVO() {
		LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName("scappt_apply_b,scappt_apply_h");
		vo.setRealTableNames("scappt_apply_b,scappt_apply_h");
		return vo;
	}
}