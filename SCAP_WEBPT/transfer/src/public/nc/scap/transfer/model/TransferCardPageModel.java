package nc.scap.transfer.model;

import java.util.UUID;

import uap.web.bd.pub.AppUtil;
import nc.scap.ptpub.PtConstants;
import nc.scap.ptpub.method.ScapPtMethod;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CpCtrlUtil;
import nc.scap.pub.util.CpPubMethod;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIMeta;

public class TransferCardPageModel extends PageModel {

	@Override
	protected void initUIMetaStruct() {
		super.initUIMetaStruct();

		String node_type = ScapPtMethod.getWebParter(PtConstants.NODE_TYPE);
		if (node_type == null || "".equals(node_type)) {
			node_type = (String) AppUtil.getAppAttr(PtConstants.NODE_TYPE) ;
		}else {
			AppUtil.addAppAttr(PtConstants.NODE_TYPE, node_type);
		}
		String operate = CpPubMethod.getWebParter(AppConsts.OPE_SIGN);
		if (operate == null || "".equals(operate)) {
			return;
		}
		LfwView main = getPageMeta().getView("main");
		
		if(LfwRuntimeEnvironment.isDevelopMode()){
			getPageMeta().setEtag(UUID.randomUUID().toString());
		}
		
		if (IGlobalConstants.BTN_ADD.equals(operate)
				|| IGlobalConstants.BTN_EDIT.equals(operate)) {
			CpPubMethod.hidnWfmPanle((UIMeta) this.getUIMeta());
			
			//chenmeng1 卡片新增：只保留保存、 返回按钮
			CpPubMethod.hidnWfmBtn(this.getPageMeta(), null);
			CpCtrlUtil.hideOtherMenuBar(main, "menubar", new String[]{"save","back"});
		} else if ("look".equals(operate)) {
			CpPubMethod.hidnWfmPanle((UIMeta) this.getUIMeta());
			CpPubMethod.hidnWfmBtn(this.getPageMeta(), null);
			
			//pos 2014-5-28
			hidChBtn(main);	
		} else if ("approve".equals(operate)) {
			CpPubMethod.hidnWfmBtn(this.getPageMeta(), new String[] {
					"btn_save", "btn_ok" });
			
			//pos 2014-5-28
//			hidChBtn(main);	
		}
		//chenmeng1  隐藏卡片界面的附件部分
	//	hidChildTab(node_type);

	}
	
	public void hidChildTab(String node_type){
		UIMeta uimeta = (UIMeta) this.getUIMeta();
		if("qybg".equals(node_type)){
			//SCAP_WEBPT/transfer/web/html/nodes/transferComps/commerger_cardwin/main/uimeta.um
			ScapPtMethod.removeGridPanle(uimeta,"scappt_attach_flowvpane2","scappt_attach_panel");
		}
	}

	@Override
	protected void initPageMetaStruct() {
		super.initPageMetaStruct();

		String operate = CpPubMethod.getWebParter(AppConsts.OPE_SIGN);
		if (operate == null || "".equals(operate)) {
			return;
		}
		LfwView main = getPageMeta().getView("main");
		if (IGlobalConstants.BTN_ADD.equals(operate)
				|| IGlobalConstants.BTN_EDIT.equals(operate)) {

		} else if ("look".equals(operate)) {

		} else if ("approve".equals(operate)) {

		}
	}
	
	/**
	 * 隐藏子表按键
	 * pos 
	 * @param main
	 * @param str
	 */
	public void hidChBtn(LfwView main, String[] str){
		for(int i = 0;i<str.length;i++){
			GridComp gc = (GridComp) main.getViewComponents().getComponent(str[i]);
			if(gc != null){
				gc.setShowImageBtn(false);
			}
		}
	}
	
	public void hidChBtn(LfwView main){
		hidChBtn(main,new String[]{
				"scappt_transfer_transferor_grid",
				"scappt_transfer_target_grid",
				"scappt_transfer_assess_grid",
				"scappt_transfer_transferee_grid" 
		});	
	}
}
