package nc.scap.transfer.apply;

import uap.lfw.ref.ctrl.LfwSqlTreeReferenceController;
import uap.lfw.ref.sqlvo.ILfwRefSqlVO;
import uap.lfw.ref.vo.LfwRefSqlVO;

public class SPReferenceControl extends LfwSqlTreeReferenceController {

	@Override
	public ILfwRefSqlVO getTreeSqlVO() {
		LfwRefSqlVO vo = new LfwRefSqlVO();
		String sql = "(SELECT * FROM bd_defdoc c WHERE c.pk_defdoclist = (SELECT b.pk_defdoclist FROM bd_defdoclist b WHERE b.code='scappt_0001') AND c.code LIKE '001%')";
		vo.setTableName(sql);
		return vo;
	}

}
