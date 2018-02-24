package nc.scap.dsm.dtp.ctrl;

import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CntUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.wfm.utils.AppUtil;
import nc.vo.pub.lang.UFDate;

public class DtpPageModel extends PageModel {

	// �÷�����Ҫ���ݲ���ֵ�жϸ���ת���ĸ�ҳ��ȥ
	@Override
	protected void initPageMetaStruct() {

		UIMeta uimeta = (UIMeta) this.getUIMeta();
		// �õ�view��ͼ
		LfwView main = this.getPageMeta().getView("main");
		// �˵��ؼ�
		MenubarComp mc = main.getViewMenus().getMenuBar("menubar");
		// ���Ҵ���id
		String id = this.getPageMeta().getId();

		String node_type = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(IGlobalConstants.NODE_TYPE);
		String oper =LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPE_SIGN);
		setItemHidden(mc, "startorstrop");
		// �б���
		if (id != null && id.lastIndexOf("listwin") != -1) {

			// ��Ƭ����
		} else if (id != null && id.lastIndexOf("cardwin") != -1) {
			GridComp gc1 = (GridComp) main.getViewComponents().getComponent("poweruser_grid");
			GridComp gc2 = (GridComp) main.getViewComponents().getComponent("powerrole_grid");
			MenubarComp bmc = gc1.getMenuBar();
			MenubarComp bmc2 = gc2.getMenuBar();
			
			setItemHidden(bmc,"poweruser_grid$HeaderBtn_Add");
			setItemHidden(bmc,"poweruser_grid$HeaderBtn_Edit");
			setItemHidden(bmc,"poweruser_grid$HeaderBtn_Delete");
			
			setItemHidden(bmc2,"powerrole_grid$HeaderBtn_Add");
			setItemHidden(bmc2,"powerrole_grid$HeaderBtn_Edit");
			setItemHidden(bmc2,"powerrole_grid$HeaderBtn_Delete");
			
			
		}
		
		if ("SCAN".equals(oper)){
			String[] headBtn=new String[]{"add","copy","del","save","attachfile","print","back"};
			for (int i = 0; i < headBtn.length; i++) {
				setItemHidden(mc,headBtn[i]);
			}
			MenubarComp mc1 = main.getViewMenus().getMenuBar("menubar_user_menu");
			setItemHidden(mc1,"add");
			setItemHidden(mc1,"edit");
			setItemHidden(mc1,"del");
			
			mc1 = main.getViewMenus().getMenuBar("menubar_role_menu");
			setItemHidden(mc1,"add");
			setItemHidden(mc1,"edit_role");
			setItemHidden(mc1,"del_role");
			
		}
		
	}

	@Override
	protected void initUIMetaStruct() {
		// TODO �Զ����ɵķ������
		super.initUIMetaStruct();
	}

	// ����һЩ����Ҫ�Ĳ˵�
	private void setItemHidden(MenubarComp mb, String itemid) {
		if (mb == null)
			return;
		nc.uap.lfw.core.comp.MenuItem item = mb.getItem(itemid);
		if (item != null) {
			item.setVisible(false);
		}
	}
}
