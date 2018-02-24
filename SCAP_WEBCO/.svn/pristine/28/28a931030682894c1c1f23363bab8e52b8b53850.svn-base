package nc.scap.pub.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.scap.pub.vos.TemplateVO;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridColumnGroup;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.ReferenceComp;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.serializer.impl.Datasets2RichVOSerializer;
import nc.uap.portal.log.ScapLogger;
import nc.vo.pub.SuperVO;

/**
 * 
 * @author xulong
 * 
 */
public class CpCtrlUtil {
	
	public static void hideMenuBar(LfwView view, String menuBar,
			String exceptionItem) {
		String[] str = { exceptionItem };
		hideMenuBar(view, menuBar, str);
	}

	public static void hideMenuBar(LfwView view, String menuBar) {
		String[] strs = {};
		hideMenuBar(view, menuBar, strs);
	}
	/**
	 * 隐藏MenuBar
	 * 
	 * @param view
	 * @param menuBar
	 *            名称
	 * @param exceptionItems
	 *            不需要隐藏的
	 */
	public static void hideMenuBar(LfwView view, String menuBar,
			String[] exceptionItems) {
		MenubarComp pubbar = view.getViewMenus().getMenuBar(menuBar);
		if (pubbar==null) return;
		List<MenuItem> menuilist = pubbar.getMenuList();
		for (MenuItem menuitem : menuilist) {
			menuitem.setVisible(false);
		}
		if (exceptionItems != null) {
			for (String exceptionItem : exceptionItems) {
				if (pubbar.getItem(exceptionItem) != null) {
					pubbar.getItem(exceptionItem).setVisible(true);
				}
			}
		}
	}
	
	public static void hideMenuBar(MenubarComp menuBar,
			String ... items) {
		menubarVisible(menuBar,false,items);
	}
	
	public static void showMenuBar(MenubarComp menuBar,
			String ... items) {
		menubarVisible(menuBar,true,items);
	}
	
	public static void menubarVisible(MenubarComp menuBar,boolean visiable,
			String ... items){
		if (menuBar==null || items == null || items.length == 0) {
			return;
		}
		for (String btn : items) {
			if (menuBar.getItem(btn) != null) {
				menuBar.getItem(btn).setVisible(visiable);
			}
		}
	}

	/**
	 * 根据数据集、行、字段名 取值
	 * 
	 * @param ds
	 * @param row
	 * @param field
	 * @return Object
	 */
	public static Object getNametoValue(Dataset ds, Row row, String field) {
		return (Object) row.getValue(ds.nameToIndex(field));
	}

	/**
	 * 根据字段名 设置字段值
	 * 
	 * @param ds
	 * @param row
	 * @param field
	 * @param val
	 *            void
	 */
	public static void setColVal(Dataset ds, Row row, String field, Object val) {
		int fieldindex = ds.nameToIndex(field);
		if (fieldindex >= 0) {
			row.setValue(fieldindex, val);
		}
	}
	
	/**
	 * 清空form里面多个字段的值
	 * 
	 * @param ds
	 * @param row
	 * @param field
	 * @param val
	 *            void
	 */
	public static void setColValNull(Dataset ds, Row row, String[] fields) {
		for(int i=0;i<fields.length;i++){
			int fieldindex = ds.nameToIndex(fields[i]);
			if (fieldindex >= 0) {
				row.setValue(fieldindex, null);
			}
		}
	}

	/**
	 * 设置按钮状态
	 * 
	 * @param view
	 * @param menuBar
	 * @param exceptionItems
	 *            需要控制按钮名的数组
	 * @param state
	 *            状态
	 */
	public static void setEableBar(LfwView view, String menuBar,
			String[] exceptionItems, boolean state) {
		MenubarComp pubbar = view.getViewMenus().getMenuBar(menuBar);
		if (pubbar == null)
			return;
		for (String item : exceptionItems) {
			if (pubbar.getItem(item) != null) {
				pubbar.getItem(item).setEnabled(state);
			}
		}
	}

	public static void setEableBarTrue(LfwView view, String menuBar,
			String[] exceptionItems) {
		setEableBar(view, menuBar, exceptionItems, true);
	}

	public static void setEableBarTrue(LfwView view, String menuBar,
			String exceptionItem) {
		String[] str = { exceptionItem };
		setEableBar(view, menuBar, str, true);
	}

	public static void setEableBarFalse(LfwView view, String menuBar,
			String exceptionItem) {
		String[] str = { exceptionItem };
		setEableBar(view, menuBar, str, false);
	}

	public static void setEableBarFalse(LfwView view, String menuBar,
			String[] exceptionItems) {
		setEableBar(view, menuBar, exceptionItems, false);
	}

	/**
	 * 设置From表单 字段是否能编辑
	 * 
	 * @param ds
	 * @param isvisible
	 * @param fieldVisibles
	 * @param formName
	 *            void
	 */
	public static void setFormEditCol(Dataset ds, boolean isvisible,
			String[] fieldVisibles, String formName) {
		FormComp fc = (FormComp) ds.getView().getViewComponents()
				.getComponent(formName);
		if (fc == null)
			return;
		for (String fieldVisible : fieldVisibles) {
			if (fc.getElementById(fieldVisible) != null) {
				fc.getElementById(fieldVisible).setEnabled(isvisible);
			}
		}
	}
	
	/**
	 * 设置From表单里面所有 字段是否能编辑
	 * 
	 * @param ds
	 * @param isvisible 能否编辑
	 * @param formName
	 *            void
	 */
	public static void setFormEditCol(Dataset ds, boolean isvisible,
			 String formName) {
		FormComp fc = (FormComp) ds.getView().getViewComponents()
				.getComponent(formName);
		if (fc == null)
			return;
		List<FormElement> elelist = fc.getElementList();
		for(int i=0;i<elelist.size();i++){
			FormElement fe = elelist.get(i);
			fe.setEnabled(isvisible);
		}
	}
	
	/**
	 * 设置From表单里面 字段是否能编辑
	 * 
	 * @param ds
	 * @param isvisible 能否编辑
	 * @param formName
	 *            void
	 */
	public static void setFormEditCol(FormComp fc, boolean editable,String ... colms) {
		if (fc == null)
			return;
		for(String colm : colms){
			FormElement element = fc.getElementById(colm);
			if(element!=null){
				element.setEnabled(editable);
			}
		}
	}

	/**
	 * 隐藏其它按钮 除 exceptionItem 外 
	 * 
	 * @param view
	 * @param menuBar
	 * @param exceptionItem
	 *            void
	 */
	public static void hideOtherMenuBar(LfwView view, String menuBar,
			String exceptionItem) {
		String[] str = { exceptionItem };
		hideOtherMenuBar(view, menuBar, str);
	}
	/**
	 * 隐藏其它按钮 除 exceptionItem 外   用于卡片子表按钮
	 * 
	 * @param view
	 * @param menuBar
	 * @param exceptionItem
	 *            void
	 */
	public static void hideChildOtherMenuBar(LfwView view, String menuBar,
			String exceptionItem) {
		String[] str = { exceptionItem };
		hideChildOtherMenuBar(view, menuBar, str);
	}
	

	/**
	 * 隐藏MenuBar
	 * 
	 * @param view
	 * @param menuBar
	 *            名称
	 * @param exceptionItems
	 *            不需要隐藏的
	 */
	public static void hideOtherMenuBar(LfwView view, String menuBar,
			String[] exceptionItems) {
		if (view == null)
			return;
		MenubarComp pubbar = view.getViewMenus().getMenuBar(menuBar);
		if (pubbar == null)
			return;
		List<MenuItem> menuilist = pubbar.getMenuList();
		for (MenuItem menuitem : menuilist) {
			menuitem.setVisible(false);
		}
		if (exceptionItems != null) {
			for (String exceptionItem : exceptionItems) {
				if (pubbar.getItem(exceptionItem) != null) {
					pubbar.getItem(exceptionItem).setVisible(true);
				}
			}
		}
	}
	
	/**
	 * 隐藏MenuBar    用于卡片子表按钮
	 * 
	 * @param view
	 * @param menuBar
	 *            名称
	 * @param exceptionItems
	 *            不需要隐藏的
	 */
	public static void hideChildOtherMenuBar(LfwView view, String child_grid,
			String[] exceptionItems) {
		if (view == null)
			return;
		GridComp gc =(GridComp) view.getViewComponents().getComponent(
				child_grid);
		if (gc==null) return; 
		MenubarComp pubbar = gc.getMenuBar();
		if (pubbar == null)
			return;
		List<MenuItem> menuilist = pubbar.getMenuList();
		for (MenuItem menuitem : menuilist) {
			menuitem.setVisible(false);
		}
		if (exceptionItems != null) {
			for (String exceptionItem : exceptionItems) {
				if (pubbar.getItem(exceptionItem) != null) {
					pubbar.getItem(exceptionItem).setVisible(true);
				}
			}
		}
	}

	/**
	 * 显示其它按钮 除 exceptionItems 数组外
	 * 
	 * @param view
	 * @param menuBar
	 * @param exceptionItems
	 *            void
	 */
	public static void showOtherMenuBar(LfwView view, String menuBar,
			String[] exceptionItems) {
		MenubarComp pubbar = view.getViewMenus().getMenuBar(menuBar);
		if (pubbar == null)
			return;
		List<MenuItem> menuilist = pubbar.getMenuList();
		for (MenuItem menuitem : menuilist) {
			menuitem.setVisible(true);
		}
		if (exceptionItems != null) {
			for (String exceptionItem : exceptionItems) {
				if (pubbar.getItem(exceptionItem) != null) {
					pubbar.getItem(exceptionItem).setVisible(false);
				}
			}
		}
	}
	
	/**
	 * 显示其它按钮 除 exceptionItems 数组外     用于卡片子表按钮
	 * 
	 * @param view
	 * @param menuBar
	 * @param exceptionItems
	 *            void
	 */
	public static void showChildOtherMenuBar(LfwView view, String child_grid,
			String[] exceptionItems) {
		GridComp gc =(GridComp) view.getViewComponents().getComponent(
				child_grid);
		MenubarComp pubbar = gc.getMenuBar();
		if (pubbar == null)
			return;
		List<MenuItem> menuilist = pubbar.getMenuList();
		for (MenuItem menuitem : menuilist) {
			menuitem.setVisible(true);
		}
		if (exceptionItems != null) {
			for (String exceptionItem : exceptionItems) {
				if (pubbar.getItem(exceptionItem) != null) {
					pubbar.getItem(exceptionItem).setVisible(false);
				}
			}
		}
	}

	/**
	 *  设置Grid的列的显示与隐藏
	 * @param view
	 * @param gridName
	 * @param isvisible
	 * @param fieldVisibles
	 */
	public static void ifVisibleCol(LfwView view, String gridName,
			boolean isvisible, String[] fieldVisibles) {
		GridComp gc = (GridComp) view.getViewComponents()
				.getComponent(gridName);
		if (gc == null)
			return;
		for (String fieldVisible : fieldVisibles) {
			gc.getColumnByField(fieldVisible).setVisible(isvisible);
		}
	}
	public static void gridColmVisiable(GridComp grid,String[] colm,boolean visiable){
		if(grid==null||colm==null) return;
		for(String c : colm){
			IGridColumn gcolm = grid.getColumnById(c);
			if(gcolm!=null){
				gcolm.setVisible(visiable);
			}
		}
		
	}
	/**
	 * 显示表格列
	 * @param grid
	 * @param visiable
	 * @param colms
	 */
	public static void visiableGridColms(GridComp grid,boolean visiable,String ... colms){
		if(grid==null||colms==null) return;
		for(String c : colms){
			GridColumn gcolm = (GridColumn)grid.getColumnById(c);
			if(gcolm!=null){
				gcolm.setVisible(visiable);
			}
		}
		
	}
	/**
	 * 编辑表格列
	 * @param grid
	 * @param editable
	 * @param colms
	 */
	public static void editableGridColms(GridComp grid,boolean editable,String ... colms){
		if(grid==null||colms==null) return;
		for(String c : colms){
			GridColumn gcolm = (GridColumn)grid.getColumnById(c);
			if(gcolm!=null){
				gcolm.setEditable(editable);
			}
		}
		
	}
	/**
	 * 表格列为空
	 * @param grid
	 * @param nullable
	 * @param colms
	 */
	public static void nullableGridColms(GridComp grid,boolean nullable,String ... colms){
		if(grid==null||colms==null) return;
		for(String c : colms){
			GridColumn gcolm = (GridColumn)grid.getColumnById(c);
			if(gcolm!=null){
				gcolm.setNullAble(nullable);
			}
		}
		
	}
	/**
	 * 
	 * @param grid
	 * @param colm
	 * @param visiable
	 */
	public static void gridColmVisiableExcept(GridComp grid,String[] colm,boolean visiable){
		if(grid==null||colm==null) return;
		Set<String> set = new HashSet<String>();
		for(String s : colm){
			set.add(s);
		}
		List<IGridColumn> columns = grid.getAllColumnList();
		for(IGridColumn gcolm : columns){
			if(set.contains(gcolm.getId())){
				gcolm.setVisible(visiable);
			}else{
				gcolm.setVisible(!visiable);
			}
		}
		
	}
	public static void gridColmVisiable(GridComp grid,String[] colms,
			boolean[] visiable){
		if(grid==null||colms==null) return;
		if(visiable==null){
			visiable = new boolean[]{false};
		}
		
		int visiablelen = visiable.length;
		for(int i=0;i<colms.length;i++){
			String colm = colms[i];
			int v = i;
			if(visiablelen<=i){
				v = visiablelen-1;
			}
			GridColumn gcolm = (GridColumn)grid.getColumnById(colm);
			if(gcolm!=null){
				gcolm.setVisible(visiable[v]);
			}
		}
		
	}
	public static void gridColmVisiableNullable(GridComp grid,String[] colms,
			Boolean[] visiable,Boolean[] nullable){
		if(grid==null||colms==null) return;
		if(visiable==null){
			visiable = new Boolean[]{false};
		}
		if(nullable==null){
			nullable = new Boolean[]{false};
		}
		
		int visiablelen = visiable.length , nulllen = nullable.length;
		for(int i=0;i<colms.length;i++){
			String colm = colms[i];
			int n = i , v = i;
			if(visiablelen<=i){
				v = visiablelen-1;
			}
			if(nulllen<=i){
				n = nulllen-1;
			}
			GridColumn gcolm = (GridColumn)grid.getColumnById(colm);
			if(gcolm!=null){
				if(visiable[n]!=null)
					gcolm.setVisible(visiable[v]);
				if(nullable[n]!=null)
					gcolm.setNullAble(nullable[n]);
			}
		}
		
	}
	/**
	 * 设置Form列的显示与隐藏
	 * @param view
	 * @param formName
	 * @param isvisible
	 * @param fieldVisibles
	 */
	public static void ifVisibleFromCol(LfwView view, String formName,
			boolean isvisible, String[] fieldVisibles) {
		FormComp fc = (FormComp) view.getViewComponents()
				.getComponent(formName);
		if (fc == null)
			return;
		for (String fieldVisible : fieldVisibles) {
			fc.getElementById(fieldVisible).setVisible(isvisible);
		}
	}
	public static void ifVisibleFromCol(FormComp fc,
			boolean isvisible, String[] fieldVisibles) {
		if (fc == null)
			return;
		for (String fieldVisible : fieldVisibles) {
			fc.getElementById(fieldVisible).setVisible(isvisible);
		}
	}

	/**
	 * 用于设置Grid列是否可编辑
	 * @param editView
	 * @param gridName
	 * @param eableFields
	 *            控制的字段
	 * @param eableflag
	 *            true eableFields：可编辑; false eableFields:不可编辑
	 */
	public static void setGridColEditable(LfwView editView, String gridName,
			String[] eableFields, boolean eableflag) {
		boolean notEable = true;
		GridComp grid = (GridComp) editView.getViewComponents().getComponent(
				gridName);
		if (eableflag) {
			notEable = false;
		}
		List<IGridColumn> gridcomp = (List<IGridColumn>) grid.getColumnList();
		for (int i = 0; i < gridcomp.size(); i++) {
			GridColumn gridcolumn = (GridColumn) gridcomp.get(i);
			gridcolumn.setEditable(notEable);
			for (String eableField : eableFields) {
				if (gridcolumn.getField().equals(eableField)) {
					gridcolumn.setEditable(eableflag);
				}
			}
		}
	}
	
	/**
	 * 用于设置带ColumnGroup的Grid列是否可编辑
	 * @param editView
	 * @param gridName
	 * @param eableFields
	 *            控制的字段
	 * @param eableflag
	 *            true eableFields：可编辑; false eableFields:不可编辑
	 */
	public static void setGridGroupColEditable(LfwView editView, String gridName,
			String[] eableFields, boolean eableflag) {
		boolean notEable = true;
		GridComp grid = (GridComp) editView.getViewComponents().getComponent(
				gridName);
		if (eableflag) {
			notEable = false;
		}
		List<IGridColumn> gridcomp = (List<IGridColumn>) grid.getColumnList();
		for (int i = 0; i < gridcomp.size(); i++) {
			IGridColumn igc =  gridcomp.get(i);
			if (igc instanceof GridColumn) {
				GridColumn gridcolumn = (GridColumn) igc;
				gridcolumn.setEditable(notEable);
				for (String eableField : eableFields) {
					if (gridcolumn.getField().equals(eableField)) {
						gridcolumn.setEditable(eableflag);
					}
				}
			} else if(igc instanceof GridColumnGroup){
				GridColumnGroup gridColGroup = (GridColumnGroup) igc;
				List<IGridColumn> childList = gridColGroup.getChildColumnList();
				for (int j = 0; j < childList.size(); j++) {
					GridColumn gridcolumn = (GridColumn) childList.get(j);
					gridcolumn.setEditable(notEable);
					for (String eableField : eableFields) {
						if (gridcolumn.getField().equals(eableField)) {
							gridcolumn.setEditable(eableflag);
						}
					}
				}
			}
		}
	}
	
	public static void setGridGroupColEditable(GridComp grid,
			String[] fields, boolean enable) {
		if(grid==null||fields==null) return;
		for(String field : fields){
			GridColumn gcolm = (GridColumn)grid.getColumnById(field);
			if(gcolm!=null){
				gcolm.setEditable(enable);
			}
		}
	}

	/**
	 * 设置Grid 列不能为空 传递DS
	 * 
	 * @param Dataset
	 * @param gridName
	 * @param eableFields
	 *            字段数组
	 */
	public static void setGridColNullAble(Dataset ds, String gridName,
			String[] eableFields) {
		GridComp grid = (GridComp) ds.getView().getViewComponents()
				.getComponent(gridName);
		if (grid == null)
			return;
		List<IGridColumn> gridcomp = (List<IGridColumn>) grid.getColumnList();
		for (int i = 0; i < gridcomp.size(); i++) {
			GridColumn gridcolumn = (GridColumn) gridcomp.get(i);
			for (String eableField : eableFields) {
				if (gridcolumn.getField().equals(eableField)) {
					gridcolumn.setNullAble(false);
				}
			}
		}
	}
	
	public static void setGridColNullAble(GridComp grid,String[] eableFields) {
		setGridColNullAble(grid,eableFields,false);
	}
	
	public static void setGridColNullAble(GridComp grid,String[] eableFields,boolean nullable) {
		if (grid == null||eableFields==null)
			return;
		for(String colm : eableFields){
			GridColumn gridColm = (GridColumn)grid.getColumnById(colm);
			gridColm.setNullAble(nullable);
		}
	}
	public static void renameGridCol(GridComp grid,Map<String,String> renameMap) {
		if (grid == null||renameMap==null)
			return;
		for(Map.Entry<String, String> entry : renameMap.entrySet()){
			GridColumn gridColm = (GridColumn)grid.getColumnById(entry.getKey());
			gridColm.setText(entry.getValue());
		}
	}
	public static void renameFormEle(FormComp form,Map<String,String> renameMap) {
		if (form == null||renameMap==null)
			return;
		for(Map.Entry<String, String> entry : renameMap.entrySet()){
			FormElement ele = (FormElement)form.getElementById(entry.getKey());
			ele.setText(entry.getValue());
		}
	}
	/**
	 * 设置Grid 列不能为空 传递View
	 * 
	 * @param editView
	 * @param gridName
	 * @param eableFields
	 *            字段数组
	 */
	public static void setGridColNullAble(LfwView editView, String gridName,
			String[] eableFields) {
		GridComp grid = (GridComp) editView.getViewComponents()
				.getComponent(gridName);
		if (grid == null)
			return;
		List<IGridColumn> gridcomp = (List<IGridColumn>) grid.getColumnList();
		for (int i = 0; i < gridcomp.size(); i++) {
			GridColumn gridcolumn = (GridColumn) gridcomp.get(i);
			for (String eableField : eableFields) {
				if (gridcolumn.getField().equals(eableField)) {
					gridcolumn.setNullAble(false);
				}
			}
		}
	}
	
	/**
	 * 设置form 列不能为空
	 * 
	 * @param editView
	 * @param formName
	 * @param eableFields
	 *            字段数组
	 */
	public static void setFormNullAble(Dataset ds, String formName,
			String[] eableFields) {
		setFormNullAble(ds, formName, eableFields, false);
	}
	public static void setFormNullAble(FormComp form,
			String[] eableFields) {
		setFormNullAble(form, eableFields, false);
	}
	
	/**
	 * 设置form 列不能为空
	 * @param ds
	 * @param formName
	 * @param eableFields
	 * @param flag
	 */
	public static void setFormNullAble(Dataset ds, String formName,
			String[] eableFields,boolean flag) {
		FormComp form = (FormComp) ds.getView().getViewComponents()
				.getComponent(formName);
		if (form == null)
			return;
		List<FormElement> formlist = (List<FormElement>) form.getElementList();
		for (int i = 0; i < formlist.size(); i++) {
			FormElement formcolumn = (FormElement) formlist.get(i);
			for (String eableField : eableFields) {
				if (formcolumn.getField().equals(eableField)) {
					formcolumn.setNullAble(flag);
				}
			}
		}
	}
	public static void setFormNullAble(FormComp form,
			String[] eableFields,boolean flag) {
		if (form == null)
			return;
		List<FormElement> formlist = (List<FormElement>) form.getElementList();
		for (int i = 0; i < formlist.size(); i++) {
			FormElement formcolumn = (FormElement) formlist.get(i);
			for (String eableField : eableFields) {
				if (formcolumn.getField().equals(eableField)) {
					formcolumn.setNullAble(flag);
				}
			}
		}
	}

	/**
	 * 隐藏子表按钮
	 * 
	 * @param view
	 * @param menuBar
	 */
	public static void hideChildBar(LfwView view, String menuBar,
			String[] menuIds) {
		ChildBar(view, menuBar, menuIds, false);
	}

	/**
	 * 显示子表按钮
	 * 
	 * @param view
	 * @param menuBar
	 */
	public static void showChildBar(LfwView view, String menuBar,
			String[] menuIds) {
		ChildBar(view, menuBar, menuIds, true);
	}

	private static void ChildBar(LfwView view, String menuBar,
			String[] menuIds, boolean flag) {
		GridComp gc = (GridComp) view.getViewComponents().getComponent(menuBar);
		if (gc == null)
			return;
		MenubarComp mbc_c = gc.getMenuBar();
		if (mbc_c == null)
			return;
		for (String menuid : menuIds) {
			if (mbc_c.getItem(menuid) != null) {
				mbc_c.getItem(menuid).setVisible(flag);
			}

		}
	}

	public static void ChildEnableBar(LfwView view, String menuBar,
			String[] menuIds, boolean flag) {
		GridComp gc = (GridComp) view.getViewComponents().getComponent(menuBar);
		if (gc == null)
			return;
		MenubarComp mbc_c = gc.getMenuBar();
		if (mbc_c == null)
			return;
		for (String menuid : menuIds) {
			if (mbc_c.getItem(menuid) != null) {
				mbc_c.getItem(menuid).setEnabled(flag);
			}

		}
	}
	
	/**
	 * 设置From表单 动态改变Text
	 * 
	 * @param ds
	 * @param fields
	 * @param formName
	 *            void
	 */
	public static void setFormTitle(LfwView view,String[] fields, String formName) {
		FormComp fc = (FormComp) view.getViewComponents()
				.getComponent(formName);
		if (fc == null)
			return;
		for (String field : fields) {
		String[] fieldss=field.split(":");
		String fieldName=fieldss[0];
		String fieldText=fieldss[1];
			if (fc.getElementById(fieldName) != null) {
				fc.getElementById(fieldName).setText(fieldText);
			}
		}
	}

	private static Datasets2RichVOSerializer getRichVOSerializer() {
		Datasets2RichVOSerializer richVOSer = null;
		if (richVOSer == null)
			richVOSer = new Datasets2RichVOSerializer();
		return richVOSer;
	}

	public static SuperVO getRichVO(LfwView view, String masterDsId,
			String[] detailDsIds) {
		Dataset masterDs = view.getViewModels().getDataset(masterDsId);
		List<String> idList = new ArrayList<String>();
		idList.add(masterDsId);
		if (detailDsIds != null && detailDsIds.length > 0)
			idList.addAll(Arrays.asList(detailDsIds));
		ArrayList<Dataset> detailDs = new ArrayList<Dataset>();
		if (detailDsIds != null && detailDsIds.length > 0) {
			for (int i = 0; i < detailDsIds.length; i++) {
				Dataset ds = view.getViewModels().getDataset(detailDsIds[i]);
				if (ds != null)
					detailDs.add(ds);
			}
		}
		Dataset[] detailDss = detailDs.toArray(new Dataset[0]);
		return getRichVOSerializer().serialize(masterDs, detailDss);
	}

	/**
	 * 弹出View
	 * 
	 * @param winid
	 * @param title
	 * @param paramMap
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void showView(String winid, String title, Map paramMap) {
		OpenProperties props = new OpenProperties(winid, title, "-1", "-1",
				paramMap);
		AppLifeCycleContext.current().getViewContext().navgateTo(props);
	}

	/**
	 * 通过模板编码获取主键
	 * 暂不考虑按单位过滤
	 * @param templatecode
	 * return pk_template
	 *     
	 */
	public static String getPk_templateByCode(String templatecode) {
		String pk_template = null;
		if (templatecode == null)
			ScapLogger
					.error("===========CpCtrlUtil.getPk_templateByCode(String templatecode), templatecode is null");
		TemplateVO[] temp = (TemplateVO[]) ScapDAO.retrieveByCondition(
				TemplateVO.class, " templatecode ='" + templatecode + "'");
		if (temp[0] == null) {
			ScapLogger
					.error("===========CpCtrlUtil.getPk_templateByCode(String templatecode), TemplateVO[] is null 、templatecode is null");
		} else {
			pk_template = temp[0].getPk_template();
		}
		return pk_template;
	}
	/**
	 * 隐藏表单项并使表单项可为空
	 * @param form
	 * @param eles
	 */
	public static void hideFormEleAndNullable(FormComp form,String ... eles){
		if(form==null||eles==null){
			return ;
		}
		for(String ele : eles){
			FormElement element = form.getElementById(ele);
			if(element==null) continue;
			element.setNullAble(true);
			element.setVisible(false);
		}
	}
	/**
	 * 显示表单项并使表单不可为空
	 * @param form
	 * @param eles
	 */
	public static void showFormEleAndNotNullable(FormComp form,String ... eles){
		if(form==null||eles==null){
			return ;
		}
		for(String ele : eles){
			FormElement element = form.getElementById(ele);
			if(element==null) continue;
			element.setNullAble(false);
			element.setVisible(true);
		}
	}
	/**
	 * 显示表单项
	 * @param form
	 * @param eles
	 */
	public static void visiableFormEles(FormComp form,boolean visiable,String ... eles){
		if(form==null||eles==null){
			return ;
		}
		for(String ele : eles){
			FormElement element = form.getElementById(ele);
			if(element==null) continue;
			element.setVisible(visiable);
		}
	}
	/**
	 * 编辑表单项
	 * @param form
	 * @param eles
	 */
	public static void editableFormEles(FormComp form,boolean editable,String ... eles){
		if(form==null||eles==null){
			return ;
		}
		for(String ele : eles){
			FormElement element = form.getElementById(ele);
			if(element==null) continue;
			element.setEnabled(editable);
		}
	}
	/**
	 * 不可空表单项
	 * @param form
	 * @param eles
	 */
	public static void nullableFormEles(FormComp form,boolean nullable,String ... eles){
		if(form==null||eles==null){
			return ;
		}
		for(String ele : eles){
			FormElement element = form.getElementById(ele);
			if(element==null) continue;
			element.setNullAble(nullable);
		}
	}
        /**
         * 表单项可以用
         * @param form
         * @param eles
         */
	public static void enableFormEles(FormComp form,boolean enable,String ... eles){
	    if(form==null||eles==null){
                return ;
            }
            for(String ele : eles){
                    FormElement element = form.getElementById(ele);
                    if(element==null) continue;
                    element.setEnabled(enable);
            }
	}
	/**
	 * 显示表单项并使表单不可为空
	 * @param form
	 * @param eles
	 */
	public static void formEleEditable(FormComp form,boolean nullable,String ... eles){
		if(form==null||eles==null){
			return ;
		}
		for(String ele : eles){
			FormElement element = form.getElementById(ele);
			if(element==null) continue;
			element.setNullAble(nullable);
			element.setEnabled(true);
		}
	}
	
	/**
	 *  设置左边组织不可编辑
	 * @param modeorg
	 * xulong
	 */
	public static void setModeOrgEnabled(LfwView modeorg){
		if (modeorg==null) return;
		ReferenceComp refcomp=(ReferenceComp)modeorg.getViewComponents().getComponent(
				"refcomp");
		if (refcomp!=null)
		refcomp.setEnabled(false);
	}

}
