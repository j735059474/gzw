package nc.scap.pub.notice_manager;

import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CntUtil;
import uap.lfw.ref.ctrl.LfwSqlGridReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;

/**
* nc.scap.wrep.wrep.RepTypGridRefCtrl
* @author Administrator
* 
*/
public class RepportTypeGridRefCtrl extends LfwSqlGridReferenceController {
	@Override
	public LfwRefSqlVO getGridSqlVO() {
		String pk_business_type = (String)uap.web.bd.pub.AppUtil.getAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE);
	  	// 通知画面进入的用户能够使用的报告类型；（在用户没有选择业务类型，而直接选择报告类型是使用）
		String contact_business_type = (String)uap.web.bd.pub.AppUtil.getAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE_LIANXIREN);
		String contact_report_type = (String)uap.web.bd.pub.AppUtil.getAppAttr(IGlobalConstants.APPATTR_REPORT_TYPE_LIANXIREN);
		StringBuilder refWhere = new StringBuilder("1=1");
		// 编辑画面进来的时候,选择工作报告类型时 直接用画面上已经选择的业务类型进行过滤
		if(pk_business_type!=null  && !pk_business_type.equals("")) {
			refWhere.append(" and YE_TYPE = '")
			.append(pk_business_type)
			.append("'");
			// 还需要过滤 当前用户在常用联系人表里确实可以使用该报告类型
			refWhere.append(" and pk_work_report_type in (").append(" select report_type ")
					.append(" from scapco_notice_contact_type a ")
					.append(" inner join scapco_notice_contact_info b ")
					.append(" on a.pk_contact_type = b.pk_contact_type ")
					.append(" where a.ENABLESTATE = '2' and a.ye_type = '").append(pk_business_type)
					.append("'").append(" and b.pk_user = '").append(CntUtil.getCntUserPk())
					.append("')");
		} else if (contact_business_type != null && !contact_business_type.equals("")) {
			refWhere.append(" and YE_TYPE in (").append(contact_business_type).append(")");
			// 还需要过滤 当前用户在常用联系人表里确实可以使用该报告类型
			refWhere.append(" and pk_work_report_type in (").append(" select report_type ")
					.append(" from scapco_notice_contact_type a ")
					.append(" inner join scapco_notice_contact_info b ")
					.append(" on a.pk_contact_type = b.pk_contact_type ")
					.append(" where a.ENABLESTATE = '2' and a.ye_type in (").append(contact_business_type)
					.append(")").append(" and b.pk_user = '").append(CntUtil.getCntUserPk()).append("'")
					;
			if (contact_report_type != null && !contact_report_type.equals("")) {
				refWhere.append(" and a.report_type in (").append(contact_report_type).append(")").append(")");
			} else {
				refWhere.append(")");
			}
		}

		
		LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName("(select * from scapco_work_reportType where "+refWhere.toString()+") scapco_work_reportType");
		vo.setRealTableNames("scapco_work_reportType");
		vo.setOrderByPart("ts");
		vo.setUseCache(false);
		return vo;
	}
}