package nc.scap.pub.reference.control;

import uap.lfw.ref.ctrl.LfwSqlGridReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;

public class ChartdsRefGridRefController extends LfwSqlGridReferenceController {
	
	@Override
	public LfwRefSqlVO getGridSqlVO() {
		LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName("scap_chart_ds");
		vo.setRealTableNames("scap_chart_ds");
		vo.setOrderByPart("");
		return vo;
	}
}