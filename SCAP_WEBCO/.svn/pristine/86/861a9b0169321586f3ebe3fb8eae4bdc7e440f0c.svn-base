package nc.scap.pub.workreport.util;

import java.util.List;

import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.portal.log.ScapLogger;

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
	
	public static void hideMenubar(GridComp gc,String[] exceptItems){
		MenubarComp cp = gc.getMenuBar();
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
	
	public static void showMenubar(GridComp gc,String[] exceptItems){
		MenubarComp cp = gc.getMenuBar();
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
	
	public static void  disableMenubar(GridComp gc,String[] exceptItems){
		MenubarComp cp = gc.getMenuBar();
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
	
	public static void enableMenubar(GridComp gc,String[] exceptItems){
		MenubarComp cp = gc.getMenuBar();
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
