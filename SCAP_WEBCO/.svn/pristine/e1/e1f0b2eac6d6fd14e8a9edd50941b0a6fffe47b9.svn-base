package nc.scap.dsm.material.util;

import java.util.List;

import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.page.LfwView;

public class MenubarUtil {
	public static void hideMenubar(LfwView view,String menubar,String[] exceptItems){
		MenubarComp cp = view.getViewMenus().getMenuBar(menubar);
		if(cp == null) return;
		List<MenuItem> itemlist = cp.getMenuList();
		if(itemlist == null || itemlist.isEmpty()) return;
		for(MenuItem menuItem:itemlist){
			if(menuItem.isSep()) continue;
			if(menuItem.isVisible()){
				menuItem.setVisible(false);
			}
		}
		MenuItem item = null;
		for(String itemId:exceptItems){
			item = cp.getItem(itemId);
			if(item != null){
				item.setVisible(true);
			}
		}
	}
	
	public static void showMenubar(LfwView view,String menubar,String[] exceptItems){
		MenubarComp cp = view.getViewMenus().getMenuBar(menubar);
		if(cp == null) return;
		List<MenuItem> itemlist = cp.getMenuList();
		if(itemlist == null || itemlist.isEmpty()) return;
		for(MenuItem menuItem:itemlist){
			if(menuItem.isSep()) continue;
			if(!menuItem.isVisible()){
				menuItem.setVisible(true);
			}
		}
		MenuItem item = null;
		for(String itemId:exceptItems){
			item = cp.getItem(itemId);
			if(item != null){
				item.setVisible(false);
			}
		}
	}
	
	public static void  disableMenubar(LfwView view,String menubar,String[] exceptItems){
		MenubarComp cp = view.getViewMenus().getMenuBar(menubar);
		if(cp == null) return;
		List<MenuItem> itemlist = cp.getMenuList();
		if(itemlist == null || itemlist.isEmpty()) return;
		for(MenuItem menuItem:itemlist){
			if(menuItem.isSep()) continue;
			if(menuItem.isEnabled()){
				menuItem.setEnabled(false);
			}
		}
		MenuItem item = null;
		for(String itemId:exceptItems){
			item = cp.getItem(itemId);
			if(item != null){
				item.setEnabled(true);
			}
		}
	}
	
	public static void enableMenubar(LfwView view,String menubar,String[] exceptItems){
		MenubarComp cp = view.getViewMenus().getMenuBar(menubar);
		if(cp == null) return;
		List<MenuItem> itemlist = cp.getMenuList();
		if(itemlist == null || itemlist.isEmpty()) return;
		for(MenuItem menuItem:itemlist){
			if(menuItem.isSep()) continue;
			if(!menuItem.isEnabled()){
				menuItem.setEnabled(true);
			}
		}
		MenuItem item = null;
		for(String itemId:exceptItems){
			item = cp.getItem(itemId);
			if(item  != null){
				item.setEnabled(false);
			}
		}
	}
}
