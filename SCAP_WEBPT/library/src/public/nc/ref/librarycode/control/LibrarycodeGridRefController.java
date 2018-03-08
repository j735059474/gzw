package nc.ref.librarycode.control;

import uap.lfw.ref.ctrl.LfwSqlGridReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;

public class LibrarycodeGridRefController extends LfwSqlGridReferenceController {
	
	@Override
	public LfwRefSqlVO getGridSqlVO() {
		LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName("scappt_library");
		vo.setRealTableNames("scappt_library");
		vo.setOrderByPart("");
		return vo;
	}
}