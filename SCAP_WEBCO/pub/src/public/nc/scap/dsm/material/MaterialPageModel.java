package nc.scap.dsm.material;

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
import nc.uap.lfw.jsp.uimeta.UITabItem;
import nc.uap.lfw.jsp.uimeta.UIView;
import nc.uap.wfm.constant.WfmConstants;
import uap.web.bd.pub.AppUtil;


public class MaterialPageModel extends PageModel {

	
	
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
			
			
			if("add".equals(node_type)){
//				String[] headBtn=new String[]{"appr","sendback"};
//				for (int i = 0; i < headBtn.length; i++) {
//					setItemHidden(mc,headBtn[i]);
//				}
				MenubarUtil.hideMenubar(main, "menubar", new String[] {"add","edit","del","scan","wf","submit",/*"recycle",*/"refresh"/*,"fborwfb"*/});
				AppUtil.addAppAttr(WfmConstants.BILLSTATE,WfmConstants.BILLSTATE_MAKEBILL);
			}
			else if("approve".equals(node_type)){
//                            String[] headBtn=new String[]{"add","edit","del","submit","recycle","fborwfb"};
//                            for (int i = 0; i < headBtn.length; i++) {
//                                    setItemHidden(mc,headBtn[i]);
//                            }
                            MenubarUtil.hideMenubar(main, "menubar", new String[] {"scan","wf","appr",/*"sendback",*/"refresh"/*,"fborwfb"*/});
                            AppUtil.addAppAttr(WfmConstants.BILLSTATE,WfmConstants.BILLSTATE_APPROVE);
                            
                    }
			else if("query".equals(node_type)){
//                            String[] headBtn=new String[]{"add","edit","del","fborwfb","appr","submit","recycle","sendback"};
//                            for (int i = 0; i < headBtn.length; i++) {
//                                    setItemHidden(mc,headBtn[i]);
//                            }
                            MenubarUtil.hideMenubar(main, "menubar", new String[] {"scan","wf","refresh"});
                            AppUtil.addAppAttr(WfmConstants.BILLSTATE,WfmConstants.BILLSTATE_BROWSE);
                    }
//			hiddenTab4Yh();
			
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
			setItemHidden(smc, "btn_save");
			
			
//			if(!"add".equals(node_type)){/** 非填报 */
			
			if("SCAN".equals(oper)){
				setItemHidden(mc,"save");
				
				String[] headBtn=new String[]{"btn_save","btn_ok","link_addattach","allFlow"};
				for (int i = 0; i < headBtn.length; i++) {
					setItemHidden(smc, headBtn[i]);
				}
				//隐藏 审批界面
				UIView mainview=UIElementFinder.findUIWidget(uimeta, "main");
				UIElement appr=UIElementFinder.findElementById(uimeta, "mainWinSpliter");
				uimeta.removeElement(appr);
				uimeta.setElement(mainview);
				
			}
			
			GridComp attchlist= (GridComp) getPageMeta().getView("attachlist").getViewComponents().getComponent("attach_grid");
			MenubarComp clmc=attchlist.getMenuBar();
			if(/*!"add".equals(node_type) ||*/ "SCAN".equals(oper)||"APPR".equals(oper)){/** 非填报 或查看情况下不能 上传和删除*/
				setItemHidden(clmc,"attach_grid$HeaderBtn_Upload");
				setItemHidden(clmc,"attach_grid$HeaderBtn_Delete");
			}
//			setItemHidden(clmc,"attach_grid$HeaderBtn_Download");
			
		}
		LfwView simple = this.getPageMeta().getView("pubview_simpleexetask");
		if ("SCAN".equals(oper)){
		    AppUtil.addAppAttr(WfmConstants.BILLSTATE,WfmConstants.BILLSTATE_BROWSE);
			FormComp formh=(FormComp) main.getViewComponents().getComponent("material_h_form");
			FormElement ele = formh.getElementById("wlorww");
			ele.setNullAble(true);
			
//			String[] headBtn=new String[]{"add","copy","del","save","attachfile","print","back"};
//			for (int i = 0; i < headBtn.length; i++) {
//				setItemHidden(mc,headBtn[i]);
//			}
			MenubarUtil.hideMenubar(simple, "simpleExeMenubar", new String[] {"allflow"});

			GridComp gc1 = (GridComp) main.getViewComponents().getComponent("otheruser_b_grid");
			MenubarComp bmc = gc1.getMenuBar();
			setItemHidden(bmc,"otheruser_b_grid$HeaderBtn_Add");
			setItemHidden(bmc,"otheruser_b_grid$HeaderBtn_Edit");
			setItemHidden(bmc,"otheruser_b_grid$HeaderBtn_Delete");
			
//			LfwView simple = this.getPageMeta().getView("pubview_simpleexetask");
			MenubarComp	smc = simple.getViewMenus().getMenuBar("simpleExeMenubar");
			String[] cardheadBtn=new String[]{"btn_save","btn_ok","btn_recall","reject","link_addattach","allFlow"};
			for (int i = 0; i < cardheadBtn.length; i++) {
				setItemHidden(smc, cardheadBtn[i]);
			}
			
			//隐藏 审批界面
			UIView mainview=UIElementFinder.findUIWidget(uimeta, "main");
			UIElement appr=UIElementFinder.findElementById(uimeta, "mainWinSpliter");
			uimeta.removeElement(appr);
			uimeta.setElement(mainview);	
			
			
		}else if ("APPR".equals(oper)){
		    AppUtil.addAppAttr(WfmConstants.BILLSTATE,WfmConstants.BILLSTATE_APPROVE);
			FormComp formh=(FormComp) main.getViewComponents().getComponent("material_h_form");
			FormElement ele = formh.getElementById("wlorww");
			ele.setNullAble(true);
			
//			String[] headBtn=new String[]{"add","copy","del","save","attachfile","print","back"};
//			for (int i = 0; i < headBtn.length; i++) {
//				setItemHidden(mc,headBtn[i]);
//			}
			MenubarUtil.hideMenubar(main, "menubar", new String[] {"back"});
			
//			LfwView simple = this.getPageMeta().getView("pubview_simpleexetask");
//                        MenubarComp     smc = simple.getViewMenus().getMenuBar("simpleExeMenubar");
//                        String[] cardheadBtn=new String[]{"btn_save","btn_ok","btn_recall","reject","link_addattach","allFlow"};
//                        for (int i = 0; i < cardheadBtn.length; i++) {
//                                setItemHidden(smc, cardheadBtn[i]);
//                        }
//                        if (smc.getItem("reject") != null) {
//                            smc.getItem("reject").setVisible(true);
//                        }
//                        if (smc.getItem("allFlow") != null) {
//                            smc.getItem("allFlow").setVisible(true);
//                        }
                        
                        MenubarUtil.hideMenubar(simple, "simpleExeMenubar", new String[] {"btn_ok", "reject", "allflow"});
                        MenubarComp     smc = simple.getViewMenus().getMenuBar("simpleExeMenubar");
                        if (smc != null && smc.getItem("btn_ok") != null) {
                        	smc.getItem("btn_ok").setText("审核");
                            smc.getItem("btn_ok").setTip("审核");
                        }

			GridComp gc1 = (GridComp) main.getViewComponents().getComponent("otheruser_b_grid");
			MenubarComp bmc = gc1.getMenuBar();
			setItemHidden(bmc,"otheruser_b_grid$HeaderBtn_Add");
			setItemHidden(bmc,"otheruser_b_grid$HeaderBtn_Edit");
			setItemHidden(bmc,"otheruser_b_grid$HeaderBtn_Delete");
			
			
		}else if (AppConsts.OPE_ADD.equals(oper) || AppConsts.OPE_EDIT.equals(oper)){
		    AppUtil.addAppAttr(WfmConstants.BILLSTATE,WfmConstants.BILLSTATE_MAKEBILL);
			//隐藏 审批界面
			UIView mainview=UIElementFinder.findUIWidget(uimeta, "main");
			UIElement appr=UIElementFinder.findElementById(uimeta, "mainWinSpliter");
			uimeta.removeElement(appr);
			uimeta.setElement(mainview);
			
			MenubarUtil.hideMenubar(main, "menubar", new String[] {"save", "back"});
			MenubarUtil.hideMenubar(simple, "simpleExeMenubar", new String[] {"btn_ok"});
////			LfwView simple = this.getPageMeta().getView("pubview_simpleexetask");
//			MenubarComp	smc = simple.getViewMenus().getMenuBar("simpleExeMenubar");
//			String[] cardheadBtn=new String[]{"btn_save","allFlow"};
//			for (int i = 0; i < cardheadBtn.length; i++) {
//				setItemHidden(smc, cardheadBtn[i]);
//			}
			
			
		}
		
//		AppUtil.addAppAttr(WfmConstants.BILLSTATE,WfmConstants.BILLSTATE_MAKEBILL);
//		LfwView simple = this.getPageMeta().getView("pubview_simpleexetask");
//		MenubarComp   smc = simple.getViewMenus().getMenuBar("simpleExeMenubar");
//		if (smc.getItem("btn_recall") != null) {
//                    smc.getItem("btn_recall").setVisible(true);
//                }
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
	
	 /** 
	 * 填报和查询是 隐藏其他人员页签 隐藏其他人员页签
	 */
	  private void hiddenTab4Yh(){
	    String node_type = (String) AppUtil
	                                .getAppAttr(IGlobalConstants.NODE_TYPE);
	                if ("add".equals(node_type) || "approve".equals(node_type) || "query".equals(node_type)) {
	                        UIMeta uimeta = (UIMeta) LfwRuntimeEnvironment.getWebContext()
	                                        .getUIMeta();
	                        UITabItem split = (UITabItem) UIElementFinder.findElementById(
	                                        uimeta, "tabitem_otheruser_b");
	                        split.setVisible(false);
	                }
	  }
}

