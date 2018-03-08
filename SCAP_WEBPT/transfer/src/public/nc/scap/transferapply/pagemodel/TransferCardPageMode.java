package nc.scap.transferapply.pagemodel;

import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CpPubMethod;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIMeta;

public class TransferCardPageMode extends PageModel {

	@Override
	protected void initUIMetaStruct() {
		super.initUIMetaStruct();

		String operate = CpPubMethod.getWebParter(AppConsts.OPE_SIGN);
		if (operate == null || "".equals(operate)) {
			return;
		}
		LfwView main = getPageMeta().getView("main");
		MenubarComp pubbar = main.getViewMenus().getMenuBar("menubar");
		String methodType = CpPubMethod
				.getWebParter(IGlobalConstants.METHOD_TYPE);

		if (IGlobalConstants.BTN_ADD.equals(operate)
				|| IGlobalConstants.BTN_EDIT.equals(operate)) {

			if (methodType != null
					&& methodType.equals(IGlobalConstants.BTN_LOOK)) {
				CpPubMethod.hidnWfmPanle((UIMeta) this.getUIMeta());
				CpPubMethod.hidnWfmBtn(this.getPageMeta(), null);
				// pos 2014-5-28
				hidChBtn(main);
			} else if (methodType != null
					&& methodType.equals(IGlobalConstants.BTN_AUDIT)) {
				// CpPubMethod.hidnWfmPanle((UIMeta) this.getUIMeta());
				CpPubMethod.hidnWfmBtn(this.getPageMeta(), null);
				// pos 2014-5-28
				hidChBtn(main);
			} else {
				CpPubMethod.hidnWfmPanle((UIMeta) this.getUIMeta());
				CpPubMethod.hidnWfmBtn(this.getPageMeta(), new String[] {
				// "btn_save",
						"btn_ok" });
			}

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
	 * Òþ²Ø×Ó±í°´¼ü pos
	 * 
	 * @param main
	 * @param str
	 */
	public void hidChBtn(LfwView main, String[] str) {
		for (int i = 0; i < str.length; i++) {
			GridComp gc = (GridComp) main.getViewComponents().getComponent(
					str[i]);
			if (gc != null) {
				gc.setShowImageBtn(false);
			}
		}
	}

	public void hidChBtn(LfwView main) {
		hidChBtn(main, new String[] {
		// "scappt_transfer_transferor_grid",
		// "scappt_transfer_target_grid",
		// "scappt_transfer_assess_grid",
		// "scappt_transfer_transferee_grid"
				"scappt_apply_b_grid" });
	}

}
