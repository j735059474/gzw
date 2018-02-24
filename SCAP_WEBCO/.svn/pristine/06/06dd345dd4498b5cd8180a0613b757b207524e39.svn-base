package uap.web.bd.org.referenceController;

import nc.uap.lfw.core.LfwRuntimeEnvironment;
import uap.lfw.ref.ctrl.LfwSqlTreeReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;
/**
 * 协同业务单元(当前集团) 参照控制类 
 * @author maokun
 *
 */
public class CpCurOrgsTreeRefController extends LfwSqlTreeReferenceController{
	@Override
	public LfwRefSqlVO getTreeSqlVO() {
		LfwRefSqlVO sqlvo = new LfwRefSqlVO();
		String pk_unit = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();
		sqlvo.setTableName("(SELECT pk_org,code,name,name2,name3,name4,name5,name6,def11,innercode,(case when pk_fatherorg = '~' then pk_orglevel1 else pk_fatherorg END) AS pk_fatherorg " +
				"FROM cp_orgs " +
				"WHERE ( orglevel = '2' and pk_orglevel1 = '"+pk_unit+"' ) and  enablestate = '2' )" +
						"cp_orgs");
		sqlvo.setRealTableNames("cp_orgs");
		sqlvo.setOrderByPart("code");
		return sqlvo;
	}
}