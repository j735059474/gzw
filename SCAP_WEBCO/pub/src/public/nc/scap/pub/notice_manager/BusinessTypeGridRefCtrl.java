package nc.scap.pub.notice_manager;

import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.notice.util.NoticeUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.refnode.RefNode;
import nc.uap.lfw.reference.ILfwRefModel;
import nc.uap.lfw.reference.app.AppReferenceController;
import uap.lfw.ref.ctrl.LfwSqlGridReferenceController;
import uap.lfw.ref.vo.LfwRefSqlVO;
import uap.web.bd.pub.AppUtil;

/**
* nc.scap.wrep.wrep.BusinessTypeGridRefCtrl
* @author Administrator
*
*/
public class BusinessTypeGridRefCtrl extends AppReferenceController {
	
	@Override
	protected void processSelfWherePart(Dataset ds, RefNode rfnode,
			String filterValue, ILfwRefModel refModel) {
		// TODO �Զ����ɵķ������
		super.processSelfWherePart(ds, rfnode, filterValue, refModel);
	  	String businessType = (String)uap.web.bd.pub.AppUtil.getAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE_FUNCNODE);
	  	String contact_business_type = (String)uap.web.bd.pub.AppUtil.getAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE_LIANXIREN);
	  	// ������ϵ�˻����card����ѡ��ҵ������
	  	if (businessType != null) {
		  	String[] businessTypeCode = businessType.substring(1, businessType.length()-1).split(",");
			refModel.addWherePart(NoticeUtil.getSqlIn("pk_defdoc", businessTypeCode));
		// �������͹������card����ѡ��ҵ������
	  	} else if (contact_business_type != null){
	  		refModel.addWherePart(" pk_defdoc in (" + contact_business_type + ")");
	  	}
	}
}