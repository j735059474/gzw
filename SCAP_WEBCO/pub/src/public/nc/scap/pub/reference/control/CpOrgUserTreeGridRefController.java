package nc.scap.pub.reference.control;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.vo.ml.NCLangRes4VoTransl;

import org.apache.commons.lang.StringUtils;

import uap.lfw.ref.ctrl.LfwSqlTreeGridReferneceController;
import uap.lfw.ref.sqlvo.ILfwRefSqlVO;
import uap.lfw.ref.sqlvo.LfwReferenceSqlVO;
import uap.lfw.ref.sqlvo.LfwWherePartVO;
import uap.lfw.ref.vo.LfwRefSqlVO;

public class CpOrgUserTreeGridRefController extends LfwSqlTreeGridReferneceController {

	private String LFW_ADMIN_GROUP = "admin";
		
	@Override
	public ILfwRefSqlVO getTreeSqlVO() {
		LfwReferenceSqlVO sqlvo = new LfwReferenceSqlVO();
		sqlvo.setTableName("cp_orgs");
		sqlvo.setRealTableNames("cp_orgs");
		String pk_unit = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();
		if(StringUtils.isNotBlank(pk_unit)){
			LfwWherePartVO vo = new LfwWherePartVO();
			vo.setWherePart(" pk_orglevel1 = '" + pk_unit + "' and orglevel in (2, 3) and ((pk_fatherorg = '~' and pk_orglevel2 = '~') or def13 is not null)");
			sqlvo.setWherePartVO(vo);
		}
		
		return sqlvo;
	}

	@Override
	public ILfwRefSqlVO getGridSqlVO() {
		LfwReferenceSqlVO sqlvo = new LfwReferenceSqlVO();
		sqlvo.setTableName("cp_user");
		sqlvo.setRealTableNames("cp_user");
		String pk_unit = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();
		if(StringUtils.isNotBlank(pk_unit)){
			LfwWherePartVO vo = new LfwWherePartVO();
			vo.setWherePart(" pk_group = '" + pk_unit + "' and enablestate = 2 ");
			sqlvo.setWherePartVO(vo);
		}
		return sqlvo;
	}
	
	@Override
	public void treeDsOnDataLoad(DataLoadEvent e) {
		Dataset ds = (Dataset) e.getSource();
		super.treeDsOnDataLoad(e);
		int userType = LfwRuntimeEnvironment.getLfwSessionBean().getUser_type();
		if(userType == 0 || userType == 2){
			Row emptyRow = ds.getEmptyRow();
			emptyRow.setString(ds.nameToIndex("pk_usergroup"), "~");
			emptyRow.setString(ds.nameToIndex("group_name"), NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("role", "MainController-0000021")/*����Ա*/);
			emptyRow.setString(ds.nameToIndex("group_code"),LFW_ADMIN_GROUP);
			ds.addRow(emptyRow);
		}
	}
	
	@Override
	public void onAfterRowSelect(DatasetEvent datasetEvent) {
		super.onAfterRowSelect(datasetEvent);
	}
}