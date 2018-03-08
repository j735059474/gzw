package nc.scap.transfer.model;

import nc.scap.ptpub.PtConstants;
import nc.scap.ptpub.method.ScapPtMethod;
import nc.scap.pub.util.CpCtrlUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.pub.util.CpUIctrl;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIFlowvLayout;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import nc.uap.lfw.jsp.uimeta.UITabItem;
import uap.web.bd.pub.AppUtil;

public class TransferPageModel extends PageModel {
	@Override
	protected void initUIMetaStruct() {
		super.initUIMetaStruct();
	}

	@Override
	protected void initPageMetaStruct() {
		super.initPageMetaStruct();
		String node_type = ScapPtMethod.getWebParter(PtConstants.NODE_TYPE);

		// if (node_type == null || "".equals(node_type)) {
		// return;
		// }

		AppUtil.addAppAttr(PtConstants.NODE_TYPE, node_type);

		// pos
		String method_type = ScapPtMethod.getWebParter("method_type");
		// if (method_type == null || "".equals(method_type)) {
		// return;
		// }
		AppUtil.addAppAttr("method_type", method_type);

		initUI(node_type, method_type);// 初始化界面

		// chenmeng1 隐藏列表界面的附件部分
//		hidChildTab();
	}

	public void hidChildTab() {
		UIMeta uimeta = (UIMeta) this.getUIMeta();
		// SCAP_WEBPT/transfer/web/html/nodes/transferComps/trs_listwin/main/uimeta.um
		ScapPtMethod.removeTabItem(uimeta, "flowvpaneltab", "tabLayout",
				"tabitem_scappt_attach");
	}

	/***
	 * 初始化组织化与按钮
	 * 
	 * @param main
	 * @param uimeta
	 * @param pk_org
	 */
	public void initTreeAndBtn(LfwView main, UIMeta uimeta, String pk_org,
			String node_type, String method_type) {
		if (CpOrgUtil.isCompanyOrg(pk_org)) {
			UIElement orgtree = (UIElement) UIElementFinder.findElementById(
					uimeta, "g_p_2");
			orgtree.setVisible(false);
			CpUIctrl.hideMenuBarReal(main, "menubar", new String[] { "edit",
					"look" });

			// if("qybg".equals(node_type) && "approve".equals(method_type)){
			// CpUIctrl.showMenuBarReal(main, "menubar",
			// new String[] { ""});
			// }

			// chenmeng 企业列表只保留：新增、修改、 删除 、查看
			// CpCtrlUtil.hideOtherMenuBar(main, "menubar", new
			// String[]{"add","edit","del","look"});

		} else {
			CpUIctrl.hideMenuBarReal(main, "menubar", new String[] { "edit",
					"look" });
			// CpUIctrl.hideMenuBarReal(main, "menubar", new String[] {
			// "approve",
			// "wf", "look",});
			String query = ScapPtMethod.getWebParter(PtConstants.QUERYOPER);
			if (query == null || "".equals(query)) {
				return;
			}
			CpUIctrl.hideMenuBarReal(main, "menubar", new String[] { "look" });
			// AppUtil.addAppAttr(PtConstants.QUERYOPER, query);
			// CpUIctrl.hideMenuBarReal(main, "menubar", new String[] { "look",
			// "wf" });
			//
			// chenmeng 国资委列表只保留：删除 、查看
			// CpCtrlUtil.hideOtherMenuBar(main, "menubar", new
			// String[]{"del","look"});
		}
	}

	/***
	 * UI初始化
	 */
	public void initUI(String node_type, String method_type) {
		LfwView main = getPageMeta().getView("main");
		UIMeta uimeta = (UIMeta) this.getUIMeta();
		String pk_org = AppLifeCycleContext.current().getApplicationContext()
				.getAppEnvironment().getPk_org();
		initTreeAndBtn(main, uimeta, pk_org, node_type, method_type);// 初始化组织化与按钮
		if (PtConstants.XYZR.equals(node_type)) {
			hiddenTabItem(uimeta, new String[] {
					"tabitem_scappt_transfer_target",
					"tabitem_scappt_transfer_assess",
					"tabitem_scappt_transfer_transferor" });
			CpUIctrl.setGridColVisableFalse(main, "scappt_transfer_h_grid",
					new String[] {"cgreattype_name" });
		} else if (PtConstants.WCZR.equals(node_type)) {
			hiddenTabItem(uimeta, new String[] {
					"tabitem_scappt_transfer_assess",
					"tabitem_scappt_transfer_transferor",
					"tabitem_scappt_transfer_target", });
			changeTabInfo(uimeta, "tabitem_scappt_transfer_transferee",
					"划入方股权结构");
			CpUIctrl.setGridColVisableFalse(main, "scappt_transfer_h_grid",
					new String[] {"cgreattype_name" });
		} else if (PtConstants.ZDZCGPZR.equals(node_type)
				|| PtConstants.ZFZCZL.equals(node_type)
				|| PtConstants.ZDZCXYZR.equals(node_type)) {
//			hiddenTabItem(uimeta, new String[] {
//					"tabitem_scappt_transfer_assess",
//					"tabitem_scappt_transfer_transferee",
//					"tabitem_scappt_transfer_transferor","tabitem_scappt_transfer_target" });
			UIFlowvLayout aa = (UIFlowvLayout) UIElementFinder.findElementById(uimeta,
					"flowvLayout");
			UIFlowvPanel cc = (UIFlowvPanel)aa.findChildById("flowvpaneltab");
			aa.removePanel(cc);
			CpUIctrl.setGridColVisableFalse(main, "scappt_transfer_h_grid",
					new String[] { "ctradetype_name", });
		} else if (PtConstants.BTBLZZ.equals(node_type)||PtConstants.TBLZZ.equals(node_type)) {
			hiddenTabItem(uimeta,
					new String[] { "tabitem_scappt_transfer_transferor" });
			setTabItemName(uimeta, new String[] {
					"tabitem_scappt_transfer_target",
					"tabitem_scappt_transfer_transferee", }, new String[] {
					"增资前", "增资后" });
			CpUIctrl.setGridColVisableFalse(main, "scappt_transfer_h_grid",
					new String[] { "cgreattype_name", "ctradetype_name" });
		} else if (PtConstants.BTBLJZ.equals(node_type)) {
			hiddenTabItem(uimeta,
					new String[] { "tabitem_scappt_transfer_transferor" });
			setTabItemName(uimeta, new String[] {
					"tabitem_scappt_transfer_target",
					"tabitem_scappt_transfer_transferee", }, new String[] {
					"减资前", "减资后" });
			CpUIctrl.setGridColVisableFalse(main, "scappt_transfer_h_grid",
					new String[] { "cgreattype_name", "ctradetype_name" });
		}else if (PtConstants.QYBG.equals(node_type)) {
			setTabItemName(uimeta, new String[] {
					"tabitem_scappt_transfer_target",
					"tabitem_scappt_transfer_assess",
					"tabitem_scappt_transfer_transferor",
					"tabitem_scappt_transfer_transferee" }, new String[] {
					"标的方并购前股权结构", "资产评估项目", "并购方股权结构", "标的方并购后股权结构" });
			CpUIctrl.setGridColVisableFalse(main, "scappt_transfer_h_grid",
					new String[] { "cgreattype_name", "ctradetype_name" });
		}else{
			CpUIctrl.setGridColVisableFalse(main, "scappt_transfer_h_grid",
					new String[] {"cgreattype_name" });
		}
		// }tabitem_scappt_transfer_assess 资产评估项目
		// tabitem_scappt_transfer_transferor 转让方股权结构
		// tabitem_scappt_transfer_target 标的企业股权结构
		// tabitem_scappt_transfer_transferee 受让方股权结构
	}

	/***
	 * 隐藏TabItem
	 * 
	 * @param uimeta
	 * @param tabItem
	 */
	public void hiddenTabItem(UIMeta uimeta, String[] tabItems) {
		UITabComp aa = (UITabComp) UIElementFinder.findElementById(uimeta,
				"tabLayout");
		if (tabItems == null)
			return;
		for (String tabItem : tabItems) {
			UITabItem cc = (UITabItem) aa.findChildById(tabItem);
			aa.removePanel(cc);
		}
	}

	public void changeTabInfo(UIMeta uimeta, String tabItem, String info) {
		UITabComp aa = (UITabComp) UIElementFinder.findElementById(uimeta,
				"tabLayout");
		if (tabItem == null)
			return;
		UITabItem cc = (UITabItem) aa.findChildById(tabItem);
		cc.setText(info);
	}

	/***
	 * 设置TabItem名字
	 * 
	 * @param uimeta
	 * @param tabItem
	 */
	public void setTabItemName(UIMeta uimeta, String[] tabItems, String[] name) {
		UITabComp aa = (UITabComp) UIElementFinder.findElementById(uimeta,
				"tabLayout");
		if (tabItems == null)
			return;
		for (int i = 0; i < tabItems.length; i++) {
			UITabItem cc = (UITabItem) aa.findChildById(tabItems[i]);
			// aa.removePanel(cc);
			// cc.setI18nName(name[i]);
			cc.setText(name[i]);
		}
	}

}
