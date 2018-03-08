package nc.scap.librarycontrol;

import uap.lfw.ref.ctrl.LfwSqlGridReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;

public class ScapptattachrefmodelGridRefController extends LfwSqlGridReferenceController {
	
	@Override
	public LfwRefSqlVO getGridSqlVO() {
		LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName("scappt_attachent");
		vo.setRealTableNames("scappt_attachent");
		vo.setOrderByPart("");
		return vo;
	}
}