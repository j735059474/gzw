package nc.scap.treansfer.applyref.control;

import uap.lfw.ref.ctrl.LfwSqlTreeReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;

public class ApplyborgnoTreeRefController extends LfwSqlTreeReferenceController {
	
	@Override
	public LfwRefSqlVO getTreeSqlVO() {
		LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName("scappt_apply_b");
		vo.setRealTableNames("scappt_apply_b");
		return vo;
	}
}