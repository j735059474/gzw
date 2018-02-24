package nc.ref.contactsref.control;

import uap.lfw.ref.ctrl.LfwSqlTreeGridReferneceController;
import uap.lfw.ref.vo.LfwRefSqlVO;

public class ContactsrefTreeGridRefController extends LfwSqlTreeGridReferneceController {
	
	@Override
	public LfwRefSqlVO getGridSqlVO() {
		LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName("cp_orgs,scapco_contacts_info");
		vo.setRealTableNames("cp_orgs,scapco_contacts_info");
		vo.setOrderByPart("code");
		return vo;
	}
	
	@Override
	public LfwRefSqlVO getTreeSqlVO() {
		LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName("cp_orgs,scapco_contacts_info");
		vo.setRealTableNames("cp_orgs,scapco_contacts_info");
		return vo;
	}
}