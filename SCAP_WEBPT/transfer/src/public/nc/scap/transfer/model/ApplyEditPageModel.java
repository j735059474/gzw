package nc.scap.transfer.model;

import java.util.UUID;

import uap.web.bd.pub.AppUtil;
import nc.scap.ptpub.PtConstants;
import nc.scap.ptpub.method.ScapPtMethod;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CpCtrlUtil;
import nc.scap.pub.util.CpPubMethod;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIMeta;

public class ApplyEditPageModel extends PageModel {
	@Override
	protected void initUIMetaStruct() {
		super.initUIMetaStruct();

		String node_type2 = ScapPtMethod.getWebParter(PtConstants.NODE_TYPE2);
		if (node_type2 == null || "".equals(node_type2)) {
			node_type2 = (String) AppUtil.getAppAttr(PtConstants.NODE_TYPE2);
		} else {
			AppUtil.addAppAttr(PtConstants.NODE_TYPE2, node_type2);
		}
		String operate = CpPubMethod.getWebParter(AppConsts.OPE_SIGN);
		if (operate == null || "".equals(operate)) {
			return;
		}
		UIMeta uimeta = (UIMeta) this.getUIMeta();
		LfwView main = getPageMeta().getView("main");
		if(LfwRuntimeEnvironment.isDevelopMode()){
			getPageMeta().setEtag(UUID.randomUUID().toString());
		}
		if (IGlobalConstants.BTN_ADD.equals(operate)
				|| IGlobalConstants.BTN_EDIT.equals(operate)) {
			CpPubMethod.hidnWfmPanle((UIMeta) this.getUIMeta());
			CpPubMethod.hidnWfmBtn(this.getPageMeta(), new String[] {
				"btn_save", "btn_ok" });
		} else if ("look".equals(operate)) {
			CpPubMethod.hidnWfmPanle((UIMeta) this.getUIMeta());
			CpPubMethod.hidnWfmBtn(this.getPageMeta(), null);
			hidChBtn(main);
		} else if ("approve".equals(operate)) {
			CpPubMethod.hidnWfmBtn(this.getPageMeta(), new String[] {
					"btn_save", "btn_ok" });
		}

		//初始化子表字段
		if(PtConstants.GPZR.equals(node_type2)){
			CpCtrlUtil.ifVisibleCol(main,"scappt_apply_b_grid", true, 
					new String[]{"gp_xmmc","gp_gpjg","gp_pgz","gp_bdate","gp_edate","gp_jcqk","gp_cjjg","gp_jjfs","gp_srfmc"});
		}else if(PtConstants.XYZR.equals(node_type2)){
			CpCtrlUtil.ifVisibleCol(main,"scappt_apply_b_grid", true, 
					new String[]{"xx_zrf","xx_srf","xx_djyj","xx_zrjg"});
		}else if(PtConstants.WCZR.equals(node_type2)){
			CpCtrlUtil.ifVisibleCol(main,"scappt_apply_b_grid", true, 
					new String[]{"wc_hcf","wc_hrf"});
		}
	}


	@Override
	protected void initPageMetaStruct() {
		super.initPageMetaStruct();
		String node_type2 = ScapPtMethod.getWebParter(PtConstants.NODE_TYPE2);
		if (node_type2 == null || "".equals(node_type2)) {
			node_type2 = (String) AppUtil.getAppAttr(PtConstants.NODE_TYPE2);
		} else {
			AppUtil.addAppAttr(PtConstants.NODE_TYPE2, node_type2);
		}
		LfwView main = getPageMeta().getView("main");
		//不同比例增减资，隐藏转让方字段。
		if("btblzz".equals(node_type2)||"tblzz".equals(node_type2)){
			FormComp fc=(FormComp) main.getViewComponents().getComponent("scappt_apply_h_form");
			GridComp gc=(GridComp) main.getViewComponents().getComponent("scappt_apply_b_grid");
			fc.getElementById("pk_rorg_vname").setVisible(false);
			fc.getElementById("treelevel").setVisible(false);
			fc.getElementById("pk_rorg_vname").setNullAble(true);
			gc.getColumnById("treelevel_b").setVisible(true);
		}
		String operate = CpPubMethod.getWebParter(AppConsts.OPE_SIGN);
		if (operate == null || "".equals(operate)) {
			return;
		}
		if (IGlobalConstants.BTN_ADD.equals(operate)
				|| IGlobalConstants.BTN_EDIT.equals(operate)) {

		} else if ("look".equals(operate)) {

		} else if ("approve".equals(operate)) {

		}
	}

	/**
	 * 隐藏子表按键 pos
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
		hidChBtn(main, new String[] { "scappt_apply_b_grid" });
	}
}
