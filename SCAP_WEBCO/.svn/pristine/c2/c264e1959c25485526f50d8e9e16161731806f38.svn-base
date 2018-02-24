package nc.scap.pub.util;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.page.LfwWindow;
import nc.uap.lfw.core.page.ViewComponents;
import nc.uap.lfw.core.page.ViewMenus;
import nc.uap.lfw.core.page.ViewModels;

public class CommonOperate {
	
	/**
	 * 提交以后,禁止修改数据
	 * @param masterDs 主表数据集
	 * @param slaveGridIds
	 */
	public static void afterApprove(LfwView lfwView){
		disableLfwView(lfwView,null);
	}
	
	/**
	 * 提交以后,禁止修改数据
	 * @param masterDs 主表数据集
	 * @param enableBtnMap
	 */
	public static void afterApprove(LfwView lfwView,Map<String,String[]> enableBtnMap){
		disableLfwView(lfwView,enableBtnMap);
	}
	
	/**
	 * 禁用页面的按钮,禁止修改页面的表格和表单
	 * @param lfwView
	 */
	public static void disableLfwView(LfwView lfwView){
		disableLfwView(lfwView,null);
	}
	
	/**
	 * 禁用页面的按钮,禁止修改页面的表格和表单
	 * @param lfwView
	 * @param enableBtnMap 列出可以使用的view id对应按钮id
	 */
	public static void disableLfwView(LfwView lfwView,Map<String,String[]> enableBtnMap){
		if(lfwView==null) return;
		ViewComponents views = lfwView.getViewComponents();
		if(views==null) return;
		
		//隐藏菜单中的保存按钮
		ViewMenus menus = lfwView.getViewMenus();
		
		if(menus!=null){
			MenubarComp menuBar = menus.getMenuBar("menubar");
			if(menuBar!=null)
				disableButton(menuBar,"save","resetdata");
		}
		
		disableApproveBtns(lfwView);
		
		WebComponent[] comps = views.getComponents();
		if(comps==null||comps.length==0) return;
		ViewModels models = lfwView.getViewModels();
		
		for(WebComponent comp : comps){
			if(comp instanceof FormComp){
				FormComp form = (FormComp)comp;
				String dsId = form.getDataset();
				disableDataset(models,dsId);
			}else if(comp instanceof GridComp){
				GridComp grid = (GridComp)comp;
				String dsId = grid.getDataset();
				disableDataset(models,dsId);
				MenubarComp gridMenuBar = grid.getMenuBar();
				if(enableBtnMap!=null){
					disableButtonExcept(gridMenuBar,enableBtnMap.get(grid.getId()));
				}else{
					disableButton(gridMenuBar);
				}
			}
		}
	}
	
	/**
	 * 禁用页面的按钮,禁止修改页面的表格和表单
	 * @param lfwView
	 * @param enabletopbtns 顶部显示的按钮
	 * @param enableBtnMap 列出可以使用的view id对应按钮id
	 */
	public static void disableLfwView(LfwView lfwView,String[] enabletopbtns,Map<String,String[]> enableBtnMap){
		if(lfwView==null) return;
		ViewComponents views = lfwView.getViewComponents();
		if(views==null) return;
		
		//隐藏菜单中的保存按钮
		ViewMenus menus = lfwView.getViewMenus();
		
		if(menus!=null){
			MenubarComp menuBar = menus.getMenuBar("menubar");
			disableButtonExcept(menuBar,enabletopbtns);
		}
		
		disableApproveBtns(lfwView);
		
		WebComponent[] comps = views.getComponents();
		if(comps==null||comps.length==0) return;
		ViewModels models = lfwView.getViewModels();
		
		for(WebComponent comp : comps){
			if(comp instanceof FormComp){
				FormComp form = (FormComp)comp;
				String dsId = form.getDataset();
				disableDataset(models,dsId);
			}else if(comp instanceof GridComp){
				GridComp grid = (GridComp)comp;
				String dsId = grid.getDataset();
				disableDataset(models,dsId);
				MenubarComp gridMenuBar = grid.getMenuBar();
				if(enableBtnMap!=null){
					disableButtonExcept(gridMenuBar,enableBtnMap.get(grid.getId()));
				}else{
					disableButton(gridMenuBar);
				}
			}
		}
	}
	
	private static void disableApproveBtns(LfwView lfwView){
		LfwWindow win = lfwView.getWindow();
		if(win==null) return;
		LfwView pubview_simpleexetask = win.getView("pubview_simpleexetask");
		if (pubview_simpleexetask == null)
			return;
		MenubarComp pubbar = pubview_simpleexetask.getViewMenus().getMenuBar("simpleExeMenubar");
		if (pubbar == null)
			return;
		List<MenuItem> menus = pubbar.getMenuList();
		for (MenuItem menu : menus) {
			menu.setEnabled(false);
		}
	}
	
	private static void disableDataset(ViewModels models,String dsId){
		if(models==null) return;
		Dataset ds = models.getDataset(dsId);
		if(ds==null) return;
		ds.setEnabled(false);
	}
	
	private static void disableButton(MenubarComp menuBar,String ...btns){
		if(menuBar==null) return;
		if(btns==null||btns.length==0){
			List<MenuItem> items = menuBar.getMenuList();
			if(items==null||items.isEmpty()) return;
			for(MenuItem item:items){
				item.setEnabled(false);
			}
		}else{
			for(String btn:btns){
				if(menuBar.getItem(btn)!=null){
					menuBar.getItem(btn).setEnabled(false);
				}
			}
		}
	}
	
	private static void disableButtonExcept(MenubarComp menuBar,String ...excepts){
		if(menuBar==null) return;
		Set<String> set = new HashSet<String>();
		if(excepts!=null){
			for(String btn:excepts){
				set.add(btn);
			}
		}
		List<MenuItem> items = menuBar.getMenuList();
		if(items==null||items.isEmpty()) return;
		for(MenuItem item:items){
			if(!set.contains(item.getId())){
				item.setEnabled(false);
			}
		}
	}
}
