package nc.scap.pub.selfquery.reference.control;

import uap.lfw.ref.ctrl.LfwSqlGridReferenceController;
import uap.lfw.ref.sqlvo.ILfwRefSqlVO;
import uap.lfw.ref.vo.LfwRefSqlVO;

public class ReportOrgsQryRefController extends LfwSqlGridReferenceController {

	@Override
	public ILfwRefSqlVO getGridSqlVO() {
		LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName("org_reportmanastru");
		vo.setRealTableNames("org_reportmanastru");
		vo.setOrderByPart("");
		return vo;
	}

		
}
