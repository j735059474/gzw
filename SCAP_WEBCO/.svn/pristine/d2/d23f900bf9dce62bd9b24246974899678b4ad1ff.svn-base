package nc.scap.pub.workreport.ref.control;

import java.util.List;

import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.notice.util.NoticeUtil;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import uap.lfw.ref.ctrl.LfwSqlGridReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;
import uap.web.bd.pub.AppUtil;

public class WorkNoticeGridRefCtrl extends LfwSqlGridReferenceController {
	@Override
	public LfwRefSqlVO getGridSqlVO() {

		String pkOrg = AppLifeCycleContext.current().getApplicationContext().getAppEnvironment().getPk_org();
		if(pkOrg == null || pkOrg.length() == 0) {
			pkOrg = CntUtil.getCntOrgPk();
		}
		String pkUser = AppLifeCycleContext.current().getApplicationContext().getAppEnvironment().getLoginUser();
		String report_type = (String)AppUtil.getAppAttr(IGlobalConstants.APPATTR_REPORT_TYPE);
		StringBuilder refWhere = new StringBuilder("");
		String reportBody = NoticeUtil.getReportBodyByNoticeType(report_type);
		refWhere.append(" select p.pk_notice  from scapco_notice_manager p ")
		.append(" inner join scapco_receive_notice r ")
		.append(" on p.pk_notice = r.pk_notice ")
		.append(" inner join scapco_receive_man m ")
		.append(" on p.pk_notice = m.pk_notice where ")
		.append( " p.notice_type = '")
		.append(report_type)
		.append("'")
		.append(" and m.RECEIVE_MAN = '")
		.append(pkUser)
		.append("'");
		if (IGlobalConstants.REPORT_BODY_QY.equals(reportBody)) {
			refWhere.append(" and (r.REPORT_STATUS = '1' or r.REPORT_STATUS = '4') ")
			.append(" and r.DEF1 is null ");
		    if (CpOrgUtil.isCompanyOrg(pkOrg)) {
		    	refWhere.append(" and r.notice_org = '")
		    	.append(pkOrg)
		    	.append("'");
		    }
		    else {
		    	refWhere.append(" and r.notice_org in ('")
		    	.append("~")
		    	.append("','")
		    	.append(pkOrg)
		    	.append("')");
		    }
		}
		List<String> pkVisualOrgList = NoticeUtil.getAllPKVisualorgByPkUser(pkUser);
		if (IGlobalConstants.REPORT_BODY_VISORG.equals(reportBody)) {
			if (pkVisualOrgList.size() > 0) {
				refWhere.append(" and (r.REPORT_STATUS = '1' or r.REPORT_STATUS = '4') ")
				.append(" and r.DEF1 is null ")
				.append("  and  (")
				.append(NoticeUtil.getSqlStrByList(pkVisualOrgList,1000,"r.NOTICE_VISUAL_ORG"))
				.append(")");
			}
		}
		if (IGlobalConstants.REPORT_BODY_MAN.equals(reportBody)) {
			if (pkVisualOrgList.size() > 0) {
				refWhere.append(" and (m.REPORT_STATUS = '1' or m.REPORT_STATUS = '4') ")
				.append(" and m.DEF1 is null ")
				.append(" and (m.RECEIVE_ORG = '")
				.append(pkOrg)
				.append("'")
				.append("  or (")
				.append(NoticeUtil.getSqlStrByList(pkVisualOrgList,1000,"m.RECEIVE_VISUAL_ORG"))
				.append("))");
			}
		}
		
		LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName("(select a.* from scapco_notice_manager a where a.pk_notice in ("+refWhere.toString()+") and a.notice_status = '2') scapco_notice_manager");
		vo.setRealTableNames("scapco_notice_manager");
		vo.setOrderByPart("ts");
		vo.setUseCache(false);
		return vo;
	
	}
}