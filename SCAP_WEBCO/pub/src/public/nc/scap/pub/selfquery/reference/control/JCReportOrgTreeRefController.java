package nc.scap.pub.selfquery.reference.control;

import nc.scap.pub.util.CpFdUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;

import org.apache.commons.lang.StringUtils;

import uap.lfw.ref.ctrl.LfwSqlTreeReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;

public class JCReportOrgTreeRefController extends LfwSqlTreeReferenceController {

	@Override
	public LfwRefSqlVO getTreeSqlVO() {
		LfwRefSqlVO sqlvo = new LfwRefSqlVO();
		String pk_unit = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();
		boolean isFilterOrg = ScapSysinitUtil.isFilterOrg();
		String tbSql = "";
		String year_mon[] = CpFdUtil.getQryYearMonth("JC");
		//String qryorgs_swh = CpFdUtil.joinQryOrgsSwh("决算", year_mon[0], year_mon[1]);
//		if(isFilterOrg){
//				
//		        String wheresql = CpOrgUtil.getFilterOrgWhereSql();
//			if(StringUtils.isEmpty(wheresql)){
//				wheresql=" where 1=1 " + qryorgs_swh;
//			}else{
//				wheresql=" where "+wheresql + qryorgs_swh;
//			}
//				tbSql = "(SELECT pk_org,code,name,name2,name3,name4,name5,name6,def11,innercode,(case when pk_fatherorg = '~' then pk_orglevel1 else pk_fatherorg END) AS pk_fatherorg " +
//						"FROM cp_orgs " + wheresql + ")"+"cp_orgs";
//			
//		}else{
//			tbSql = "(SELECT pk_org,code,name,name2,name3,name4,name5,name6,def11,innercode,(case when pk_fatherorg = '~' then pk_orglevel1 else pk_fatherorg END) AS pk_fatherorg " +
//					"FROM cp_orgs " +
//					"WHERE ((orglevel = '1' and pk_org = '"+ pk_unit +"' ) OR (pk_orglevel1 = '"+pk_unit+"' AND orglevel = '2' ) ) and  enablestate = '2'"+ qryorgs_swh + ") " +
//							"cp_orgs";
//		}
		tbSql=CpFdUtil.getReportSql("决策支持", year_mon[0], year_mon[1]);
		sqlvo.setTableName("("+tbSql+") cp_orgs");
		sqlvo.setRealTableNames("cp_orgs");
		sqlvo.setOrderByPart("code");
		return sqlvo;

	}

}
