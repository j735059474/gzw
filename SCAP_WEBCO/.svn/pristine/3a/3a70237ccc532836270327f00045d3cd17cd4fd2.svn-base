package uap.web.bd.org.referenceController;

import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;

import org.apache.commons.lang.StringUtils;

import uap.lfw.ref.ctrl.LfwSqlTreeReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;
/**
 * 		协同当前集团+业务单元    参照控制类
 * @author maokun
 *
 */
public class CpCurGroupOrgTreeRefController extends LfwSqlTreeReferenceController {

	@Override
	public LfwRefSqlVO getTreeSqlVO() {
		LfwRefSqlVO sqlvo = new LfwRefSqlVO();
		String pk_unit = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();
		String pk_org=CntUtil.getCntOrgPk();
		int flag=CntUtil.CtnGwzOrCompanyOrPartnerUser();//1是国资委用户，2为企业用户，3为中介机构用户
		boolean isFilterOrg = ScapSysinitUtil.isFilterOrg();
		String tbSql = "";
		if(isFilterOrg){
			if(flag==2){
				tbSql="(SELECT pk_org,code,name,name2,name3,name4,name5,name6,def11,innercode,(case when pk_fatherorg = '~' then pk_orglevel1 else pk_fatherorg END) AS pk_fatherorg " +
						"FROM cp_orgs where enablestate = '2'  START WITH pk_org = '"+pk_org+"' CONNECT BY PRIOR pk_org = pk_fatherorg )" +
								"cp_orgs";
			}else {
//				String curuserpk = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
//				String curGrouppk = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();
//				ModeOrgFilter filter = (ModeOrgFilter)AppLifeCycleContext.current().getApplicationContext().getAppAttribute(ModeOrgHelper.ModeFilter_AttID);
//				if(filter == null){
//					filter = new ModeOrgFilter();
//				}
//				filter.setIsneedGroup(false);
////		        String wheresql=" dr=0 START WITH  pk_org = '"+AppUtil.getPk_org()+"' CONNECT BY PRIOR pk_org=pk_fatherorg";//默认条件为当前登录用户所在组织及下级组织
//		        String wheresql="";//默认条件为当前登录用户所在组织及下级组织
			
		        String wheresql = CpOrgUtil.getFilterOrgWhereSql();
			if(StringUtils.isEmpty(wheresql)){
				wheresql="  START WITH pk_org = '"+pk_org+"' CONNECT BY PRIOR pk_org = pk_fatherorg ";
			}else{
				wheresql=" where "+wheresql;
			}
				tbSql = "(SELECT pk_org,code,name,name2,name3,name4,name5,name6,def11,innercode,(case when pk_fatherorg = '~' then pk_orglevel1 else pk_fatherorg END) AS pk_fatherorg " +
						"FROM cp_orgs " + wheresql + ")"+"cp_orgs";
			}
		}else{
			if(flag==2){
				tbSql = "(SELECT pk_org,code,name,name2,name3,name4,name5,name6,def11,innercode,(case when pk_fatherorg = '~' then pk_orglevel1 else pk_fatherorg END) AS pk_fatherorg " +
						"FROM cp_orgs  where enablestate = '2'  START WITH pk_org = '"+pk_org+"' CONNECT BY PRIOR pk_org = pk_fatherorg )" +
						"cp_orgs";
			}else {
				tbSql = "(SELECT pk_org,code,name,name2,name3,name4,name5,name6,def11,innercode,(case when pk_fatherorg = '~' then pk_orglevel1 else pk_fatherorg END) AS pk_fatherorg " +
						"FROM cp_orgs " +
						"WHERE ((orglevel = '1' and pk_org = '"+ pk_unit +"' ) OR (pk_orglevel1 = '"+pk_unit+"' AND orglevel = '2' ) ) and  enablestate = '2' )" +
								"cp_orgs";
			}
		}
	
		sqlvo.setTableName(tbSql);
		sqlvo.setRealTableNames("cp_orgs");
		sqlvo.setOrderByPart("code");
		return sqlvo;
		
//		LfwRefSqlVO sqlvo = new LfwRefSqlVO();
//		String pk_unit = LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit();
//		String pk_org=CntUtil.getCntOrgPk();
//		int flag=CntUtil.CtnGwzOrCompanyOrPartnerUser();//1是国资委用户，2为企业用户，3为中介机构用户
//		if(flag==2){
//			sqlvo.setTableName("(SELECT pk_org,code,name,name2,name3,name4,name5,name6,def11,innercode,(case when pk_fatherorg = '~' then pk_orglevel1 else pk_fatherorg END) AS pk_fatherorg " +
//					"FROM cp_orgs   START WITH pk_org = '"+pk_org+"' CONNECT BY PRIOR pk_org = pk_fatherorg )" +
//							"cp_orgs");
//		}else {
//			sqlvo.setTableName("(SELECT pk_org,code,name,name2,name3,name4,name5,name6,def11,innercode,(case when pk_fatherorg = '~' then pk_orglevel1 else pk_fatherorg END) AS pk_fatherorg " +
//					"FROM cp_orgs " +
//					"WHERE ((orglevel = '1' and pk_org = '"+ pk_unit +"' ) OR (pk_orglevel1 = '"+pk_unit+"' AND orglevel = '2' ) ) and  enablestate = '2' )" +
//							"cp_orgs");
//		}
//		sqlvo.setRealTableNames("cp_orgs");
//		sqlvo.setOrderByPart("code");
//		return sqlvo;
	}

}
