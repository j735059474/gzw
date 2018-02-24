package nc.ref.visualOrganization.control;

import uap.lfw.ref.ctrl.LfwSqlTreeReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;

public class VisualOrganizationTreeRefController extends LfwSqlTreeReferenceController {
	
	@Override
	public LfwRefSqlVO getTreeSqlVO() {
		LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName("scapco_visualOrganization");
		vo.setRealTableNames("scapco_visualOrganization");
		return vo;
	}
}