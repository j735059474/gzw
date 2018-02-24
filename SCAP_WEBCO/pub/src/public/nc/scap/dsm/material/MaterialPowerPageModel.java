package nc.scap.dsm.material;

import java.util.List;

import nc.scap.dsm.material.util.MenubarUtil;
import nc.scap.jjpub.util.JjUtil;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CntUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UIView;
import uap.web.bd.pub.AppUtil;


public class MaterialPowerPageModel extends PageModel {

	
	
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
		
		// 列表窗口
		if (id != null && id.lastIndexOf("listwin") != -1) {
			AppUtil.addAppAttr(IGlobalConstants.NODE_TYPE, node_type);
			AppUtil.addAppAttr(MaterialListWinMainViewCtrl.currZllx, new String[]{});
			
			MenubarUtil.hideMenubar(main, "menubar", new String[] {"refresh","scan"});
			
			// 卡片窗口
		} else if (id != null && id.lastIndexOf("cardwin") != -1) {
			if(node_type==null || "".equals(node_type))
				node_type=JjUtil.getStr(AppUtil.getAppAttr(IGlobalConstants.NODE_TYPE));
			
			if(CntUtil.CtnUserIsCompanyUser()){			
				FormComp formh=(FormComp) main.getViewComponents().getComponent("material_h_form");
				FormElement ele = formh.getElementById("wlorww");
				ele.setVisible(false);
				ele.setNullAble(true);
			}
			LfwView simple = this.getPageMeta().getView("pubview_simpleexetask");
			MenubarComp	smc = simple.getViewMenus().getMenuBar("simpleExeMenubar");
			MenubarUtil.hideMenubar(simple, "simpleExeMenubar", new String[] {});
			
                        //隐藏 审批界面
                        UIView mainview=UIElementFinder.findUIWidget(uimeta, "main");
                        UIElement appr=UIElementFinder.findElementById(uimeta, "mainWinSpliter");
                        uimeta.removeElement(appr);
                        uimeta.setElement(mainview);
			
			if("SCAN".equals(oper)){/** 非填报 或查看情况下不能 上传和删除*/
			    MenubarUtil.hideMenubar(main, "menubar", new String[] {"back"});
			    
			    GridComp gc1 = (GridComp) main.getViewComponents().getComponent("otheruser_b_grid");
	                        MenubarComp bmc = gc1.getMenuBar();
	                        setItemHidden(bmc,"otheruser_b_grid$HeaderBtn_Add");
	                        setItemHidden(bmc,"otheruser_b_grid$HeaderBtn_Edit");
	                        setItemHidden(bmc,"otheruser_b_grid$HeaderBtn_Delete");
			}
			
			GridComp attchlist= (GridComp) getPageMeta().getView("attachlist").getViewComponents().getComponent("attach_grid");
                        MenubarComp clmc=attchlist.getMenuBar();
                        setItemHidden(clmc,"attach_grid$HeaderBtn_Upload");
                        setItemHidden(clmc,"attach_grid$HeaderBtn_Delete");
//                      setItemHidden(clmc,"attach_grid$HeaderBtn_Download");
//			
			
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

