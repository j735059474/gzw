package nc.scap.transfer.model;

import nc.scap.ptpub.PtConstants;
import nc.scap.ptpub.method.ScapPtMethod;
import nc.scap.pub.util.CpCtrlUtil;
import nc.scap.pub.util.CpUIctrl;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIView;
import uap.web.bd.pub.AppUtil;

public class ApplyPageModel extends PageModel {
	@Override
	protected void initUIMetaStruct() {
		super.initUIMetaStruct();
	}

	@Override
	protected void initPageMetaStruct() {
		super.initPageMetaStruct();
		String node_type = ScapPtMethod.getWebParter(PtConstants.NODE_TYPE);
		String node_type2 = ScapPtMethod.getWebParter(PtConstants.NODE_TYPE2);
		AppUtil.addAppAttr(PtConstants.NODE_TYPE, node_type);
		AppUtil.addAppAttr(PtConstants.NODE_TYPE2, node_type2);

		initUI(node_type);// 初始化界面
		initGridCol(node_type2);// 初始化子表字段
	}

	/***
	 * 初始化子表字段
	 */
	public void initGridCol(String node_type2) {
		LfwView main = getPageMeta().getView("main");
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

	
	/***
	 * UI初始化
	 */
	public void initUI(String node_type) {
		LfwView main = getPageMeta().getView("main");
		UIMeta uimeta = (UIMeta) this.getUIMeta();
		String pk_org = AppLifeCycleContext.current().getApplicationContext()
				.getAppEnvironment().getPk_org();
		initTreeAndBtn(main, uimeta, pk_org, node_type);// 初始化组织化与按钮
	}

	/***
	 * 初始化组织化与按钮
	 * 
	 * @param main
	 * @param uimeta
	 * @param pk_org
	 */
	public void initTreeAndBtn(LfwView main, UIMeta uimeta, String pk_org,
			String node_type) {
		if (PtConstants.PAGECODE_MANAGER.equals(node_type)) {
			CpUIctrl.showMenuBarReal(main, "menubar",
					new String[] { "approve" });
		} else if (PtConstants.PAGECODE_APPROVE.equals(node_type)) {
			CpUIctrl.hideMenuBarReal(main, "menubar", new String[] { "approve",
					"wf", "look", });
		} else if (PtConstants.PAGECODE_BROWSE.equals(node_type)) {
			CpUIctrl.hideMenuBarReal(main, "menubar", new String[] { "look",
					"wf" });
		} else if ("compare".equals(node_type)) {
			CpUIctrl.hideMenuBarReal(main, "menubar", new String[] { "look"});
			UIView mainView = UIElementFinder.findUIWidget(uimeta, "main");
			UIView simplequeryView = (UIView) UIElementFinder.findElementById(uimeta, "simplequery");
			uimeta.removeElement(simplequeryView);
			uimeta.setElement(mainView);
		}
	}
}
