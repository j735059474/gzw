package uap.web.bd.org.referenceController;

import uap.lfw.ref.ctrl.LfwSqlTreeReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;
/**
 *   协同集团+业务单元(所有)  参照控制类
 * @author maokun
 *
 */
public class CpAllGroupOrgTreeRefController extends LfwSqlTreeReferenceController{

	@Override
	public LfwRefSqlVO getTreeSqlVO() {
		LfwRefSqlVO sqlvo = new LfwRefSqlVO();
		//jizhg 增加def1存储简称
		sqlvo.setTableName("(SELECT pk_org,code,name,name2,name3,name4,name5,name6,def11,innercode,(case when pk_fatherorg = '~' then pk_orglevel1 else pk_fatherorg END) AS pk_fatherorg ,pk_orglevel1 " +
				"FROM cp_orgs " +
				"WHERE (  orglevel = '1' OR orglevel = '2' ) and  enablestate = '2' )" +
						"cp_orgs");
		sqlvo.setRealTableNames("cp_orgs");
		sqlvo.setOrderByPart("code");
		return sqlvo;
	}

}
