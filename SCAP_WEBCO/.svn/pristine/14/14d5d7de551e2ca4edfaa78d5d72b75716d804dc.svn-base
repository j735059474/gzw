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

	// 该方法主要根据参数值判断该跳转到哪个页面去
	@Override
	protected void initPageMetaStruct() {

		UIMeta uimeta = (UIMeta) this.getUIMeta();
		// 得到view视图
		LfwView main = this.getPageMeta().getView("main");
		// 菜单控件
		MenubarComp mc = main.getViewMenus().getMenuBar("menubar");
		// 查找窗口id
		String id = this.getPageMeta().getId();

		String node_type = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(IGlobalConstants.NODE_TYPE);
		String oper =LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPE_SIGN);
		setItemHidden(mc, "startorstrop");
		// 列表窗口
		if (id != null && id.lastIndexOf("listwin") != -1) {

			// 卡片窗口
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
		// TODO 自动生成的方法存根
		super.initUIMetaStruct();
	}

	// 隐藏一些不必要的菜单
	private void setItemHidden(MenubarComp mb, String itemid) {
		if (mb == null)
			return;
		nc.uap.lfw.core.comp.MenuItem item = mb.getItem(itemid);
		if (item != null) {
			item.setVisible(false);
		}
	}
}
