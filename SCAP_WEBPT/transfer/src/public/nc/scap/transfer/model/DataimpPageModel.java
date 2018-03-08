package nc.scap.transfer.model;

import nc.scap.ptpub.PtConstants;
import nc.scap.ptpub.method.ScapPtMethod;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.pub.util.CpUIctrl;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import nc.uap.lfw.jsp.uimeta.UITabItem;
import uap.web.bd.pub.AppUtil;

public class DataimpPageModel extends PageModel {
	@Override
	protected void initUIMetaStruct() {
		super.initUIMetaStruct();
	}

	@Override
	protected void initPageMetaStruct() {
		super.initPageMetaStruct();
		String node_type = ScapPtMethod.getWebParter(PtConstants.NODE_TYPE);
		if (node_type == null || "".equals(node_type)) {
			return;
		}
		AppUtil.addAppAttr(PtConstants.NODE_TYPE, node_type);
		initUI(node_type);// 初始化界面
	}

	/***
	 * 初始化组织化与按钮
	 * 
	 * @param main
	 * @param uimeta
	 * @param pk_org
	 */
	public void initTreeAndBtn(LfwView main, UIMeta uimeta, String pk_org) {
		if (CpOrgUtil.isCompanyOrg(pk_org)) {
			CpUIctrl.hideMenuBarReal(main, "menubar", new String[] { "look",
					"billlook" });
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
		initTreeAndBtn(main, uimeta, pk_org);// 初始化组织化与按钮

	}

}
