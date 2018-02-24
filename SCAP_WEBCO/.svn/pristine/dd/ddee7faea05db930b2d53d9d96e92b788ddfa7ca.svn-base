package nc.scap.pub.attlist.comp;

import uap.web.bd.pub.AppUtil;
import nc.scap.pub.attlist.AttachRoleDao;
import nc.scap.pub.attlist.AttachRoleUtil;
import nc.scap.pub.vos.AttachGroupVO;
import nc.uap.lfw.core.model.IWidgetUIProvider;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIGridComp;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import nc.uap.lfw.jsp.uimeta.UITabItem;
import nc.uap.lfw.jsp.uimeta.UITabRightPanel;
import nc.ui.pub.beans.UIMenuBar;

public class AttachRoleCompUIProvider implements IWidgetUIProvider {

	@Override
	public UIMeta getDefaultUIMeta(LfwView widget) {
		UIMeta uiMeta = new UIMeta();
		uiMeta.setId(widget.getId() + "_um");

		UITabComp tabComp = new UITabComp();
		tabComp.setId("uiTabComp");
		tabComp.setWidgetId(widget.getId());
		uiMeta.setElement(tabComp);
		
		String codenode = (String) AppUtil.getAppAttr("nodecode");
		AttachGroupVO[] groups = AttachRoleDao.retrieveAttachGroupByNodeCode(codenode);

		for (int i = 0; i < (groups == null ? IAttachRoleCompConstant.MAX_ROLE_GROUP_COUNT : groups.length); i++) {

			// tabItem
			UITabItem tabItem = new UITabItem();
			tabItem.setId("tabItem" + i);
			tabItem.setWidgetId(widget.getId());
			tabItem.setLayout(tabComp);
			tabItem.setText(groups[i].getName());
			tabComp.addPanel(tabItem);
			
			// gridComp
			UIGridComp grid = new UIGridComp();
			grid.setId("grid" + i);
			grid.setWidgetId(widget.getId());
			grid.setPosition("relative");
			tabItem.setElement(grid);
		}

		return uiMeta;
	}

}
