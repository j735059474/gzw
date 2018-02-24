package nc.scap.pub.news;

import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CpCtrlUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UISplitter;
import nc.uap.lfw.jsp.uimeta.UIView;

public class NewsPageModel extends PageModel {

	@Override
	protected void initUIMetaStruct() {
		String operation = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPE_SIGN);
		if (operation == null || "".equals(operation)) {
			return;
		}
		LfwView main = getPageMeta().getView("main");
		LfwView pubview_simpleexetask = getPageMeta().getView("pubview_simpleexetask");
		if (IGlobalConstants.BTN_EDIT.equals(operation) || IGlobalConstants.BTN_ADD.equals(operation) || "view".equals(operation)) {
			UIMeta uimeta = (UIMeta) this.getUIMeta();
			UIView mainView = UIElementFinder.findUIWidget(uimeta, "main");
			UISplitter split = (UISplitter) UIElementFinder.findElementById(
					uimeta, "mainWinSpliter");
			uimeta.removeElement(split);
			uimeta.setElement(mainView);
		}
		if("view".equals(operation)) {
			CpCtrlUtil.hideMenuBar(pubview_simpleexetask, "simpleExeMenubar");
		}else {
			CpCtrlUtil.showOtherMenuBar(pubview_simpleexetask, "simpleExeMenubar", new String[] { "link_addattach" });
		}
		
		super.initUIMetaStruct();

	}

}
