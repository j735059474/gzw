package nc.ref.userpsn.control;

import uap.lfw.ref.ctrl.LfwSqlGridReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;

public class UserpsnGridRefController extends LfwSqlGridReferenceController {
	
	@Override
	public LfwRefSqlVO getGridSqlVO() {
		LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName("v_userpsn");
		vo.setRealTableNames("v_userpsn");
		vo.setOrderByPart("szdw,user_code");
		return vo;
	}
}