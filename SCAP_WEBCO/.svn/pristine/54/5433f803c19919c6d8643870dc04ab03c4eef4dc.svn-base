package nc.ref.scapReportmanastru.control;

import uap.lfw.ref.ctrl.LfwSqlGridReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;

public class ScapReportmanastruGridRefController extends LfwSqlGridReferenceController {
	
	@Override
	public LfwRefSqlVO getGridSqlVO() {
		LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName("org_reportmanastru");
		vo.setRealTableNames("org_reportmanastru");
		vo.setWherePart(" enablestate='2' ");
		vo.setOrderByPart(" order by code desc");
		return vo;
	}
}