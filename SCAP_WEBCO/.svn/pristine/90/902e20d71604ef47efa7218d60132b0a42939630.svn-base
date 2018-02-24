package nc.scap.pub.notice_manager;

import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.notice.util.NoticeUtil;
import nc.scap.pub.util.CntUtil;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.reference.ILfwRefModel;
import nc.uap.lfw.reference.app.AppReferenceController;
import uap.lfw.ref.ctrl.LfwSqlGridReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;
import uap.web.bd.pub.AppUtil;

/**
 * nc.scap.wrep.wrep.RepTypGridRefCtrl
 * 
 * @author Administrator
 * 
 */
public class RepportTypeGridRefCtrl_contacts extends LfwSqlGridReferenceController {
	@Override
	public LfwRefSqlVO getGridSqlVO() {
		String businessType = (String) uap.web.bd.pub.AppUtil
				.getAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE_FUNCNODE);
		String pk_business_type = (String) uap.web.bd.pub.AppUtil
				.getAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE);
		StringBuilder refWhere = new StringBuilder(" 1=1 ");
		// 常用联系人画面的card画面编辑时如果已经选择了相应的业务类型 则按照业务类型进行过滤
		if (pk_business_type != null) {
			refWhere.append(" and YE_TYPE = '")
			.append(pk_business_type)
			.append("'");
		// 常用联系人画面的card画面新建时选择工作报告类型时 需要用功能节点注册是传入的参数进行报告类型的过滤
		} else if (businessType != null) {
			String[] businessTypeCode = businessType.substring(1,
					businessType.length() - 1).split(",");
			refWhere.append(" and ").append(
					NoticeUtil.getSqlIn("YE_TYPE", businessTypeCode));
		}
		LfwRefSqlVO vo = new LfwRefSqlVO();
		vo.setTableName("(select * from scapco_work_reportType where "
				+ refWhere.toString() + ") scapco_work_reportType");
		vo.setRealTableNames("scapco_work_reportType");
		vo.setOrderByPart("ts");
		vo.setUseCache(false);
		return vo;
	}
}