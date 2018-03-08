package nc.scap.transfer.model;

import nc.scap.ptpub.PtConstants;
import nc.scap.ptpub.method.ScapPtMethod;
import nc.scap.pub.util.CpUIctrl;
import nc.scap.transfer.land.utils.ILandConstants;
import nc.scap.transfer.land.utils.LandUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIView;
import uap.web.bd.pub.AppUtil;

public class LandPageModel extends PageModel{
	@Override
	protected void initUIMetaStruct() {
		super.initUIMetaStruct();
	}
	@Override
	protected void initPageMetaStruct(){
		super.initPageMetaStruct();
		//隐藏列表顶部按钮
		LfwView editView = getWebContext().getPageMeta().getView(ILandConstants.MAIN_VIEW_ID);
//		GridComp gc = (GridComp) editView.getViewComponents().getComponent("scapcb_progress_h_grid");
		String isAudit = (String) LfwRuntimeEnvironment.getWebContext().getOriginalParameter(ILandConstants.IS_AUDIT);
		AppUtil.addAppAttr(ILandConstants.SAVE_OPE, null);
		if (isAudit!= null){
			AppUtil.addAppAttr(ILandConstants.IS_AUDIT, isAudit);
		}
		//产权交易导入
		String node_type = ScapPtMethod.getWebParter(PtConstants.NODE_TYPE);
		node_type = (String) AppUtil.getAppAttr(PtConstants.NODE_TYPE);
		LfwView main = getPageMeta().getView("main");
		UIMeta uimeta = (UIMeta) this.getUIMeta();
		if ("compare".equals(node_type)) {
			CpUIctrl.hideMenuBarReal(main, "menubar", new String[] { "view"});
			UIView mainView = UIElementFinder.findUIWidget(uimeta, "main");
			UIView simplequeryView = (UIView) UIElementFinder.findElementById(uimeta, "simplequery");
			uimeta.removeElement(simplequeryView);
			uimeta.setElement(mainView);
			AppUtil.addAppAttr(PtConstants.NODE_TYPE, null);
		}
		
		if(isAudit!=null&&isAudit.equals(ILandConstants.AUDIT_FALSE)){//填报
			LandUtil.setBtnFalse(editView, ILandConstants.MENUBAR, new String[]{
					ILandConstants.BTN_PRINT, ILandConstants.BTN_QYTY, ILandConstants.BTN_AUDIT
//					, "compare"
			});		
		}
		else if(isAudit!=null&&isAudit.equals(ILandConstants.AUDIT_TRUE)){//审核			
			LandUtil.setBtnFalse(editView, ILandConstants.MENUBAR, new String[]{
					ILandConstants.BTN_PRINT, ILandConstants.BTN_QYTY,
					ILandConstants.BTN_ADD, ILandConstants.BTN_DEL, ILandConstants.BTN_EDIT, "compare"
		});		
		}
		else if(isAudit!=null&&isAudit.equals(ILandConstants.AUDIT_VIEW)){//查看			
			LandUtil.setBtnFalse(editView, ILandConstants.MENUBAR, new String[]{
					ILandConstants.BTN_PRINT, ILandConstants.BTN_QYTY, 
					ILandConstants.BTN_ADD, ILandConstants.BTN_DEL, ILandConstants.BTN_EDIT, ILandConstants.BTN_AUDIT, "compare", "export", "refresh"
		});		
		}
	}
}
