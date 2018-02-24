package uap.web.bd.org.referenceController;

import nc.uap.cpb.org.orgs.CpOrgVO;
import uap.lfw.ref.ctrl.LfwSqlTreeReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;
/**
 * 		协同集团(所有)   参照控制类
 * @author maokun
 *
 */
public class CpAllGroupTreeRefController extends LfwSqlTreeReferenceController{

	@Override
	public LfwRefSqlVO getTreeSqlVO() {
		LfwRefSqlVO sqlvo = new LfwRefSqlVO();
		sqlvo.setTableName("(SELECT pk_org,code,name,name2,name3,name4,name5,name6,def11,innercode,(case when pk_fatherorg = '~' then pk_orglevel1 else pk_fatherorg END) AS pk_fatherorg " +
				"FROM cp_orgs " +
				"WHERE orglevel = '1' and  enablestate = '2' )" +
						"cp_orgs");
		sqlvo.setRealTableNames("cp_orgs");
		sqlvo.setOrderByPart(CpOrgVO.CODE);
		return sqlvo;
	}
}
