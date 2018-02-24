package nc.scap.pub.util;

import java.util.List;
import java.util.Map;

import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.IGridColumn;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.EmptyRow;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwView;

public class CpUIctrl {
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
	 * 
	 * 
	 * @param view
	 * @param menuBar
	 *            名称
	 * @param exceptionItems
	 * 
	 */
	public static void hideMenuBar(LfwView view, String menuBar,
			String[] exceptionItems) {
		MenubarComp pubbar = view.getViewMenus().getMenuBar(menuBar);
		if (pubbar == null)
			return;
		List<MenuItem> menuilist = pubbar.getMenuList();
		for (MenuItem menuitem : menuilist) {
			if (menuitem.isVisible()) {
				menuitem.setEnabled(false);
			}
		}
		if (exceptionItems != null) {
			for (String exceptionItem : exceptionItems) {
				if (pubbar.getItem(exceptionItem) != null) {
					pubbar.getItem(exceptionItem).setEnabled(true);
				}
			}
		}
	}

	public static void hideMenuBarReal(LfwView view, String menuBar,
			String[] exceptionItems) {
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

	public static void showMenuBar(LfwView view, String menuBar,
			String[] exceptionItems) {
		MenubarComp pubbar = view.getViewMenus().getMenuBar(menuBar);
		if (pubbar == null)
			return;
		List<MenuItem> menuilist = pubbar.getMenuList();
		for (MenuItem menuitem : menuilist) {
			if (menuitem.isVisible()) {
				menuitem.setEnabled(true);
			}
		}
		if (exceptionItems != null) {
			for (String exceptionItem : exceptionItems) {
				if (pubbar.getItem(exceptionItem) != null) {
					pubbar.getItem(exceptionItem).setEnabled(false);
				}
			}
		}

	}

	public static void showMenuBarReal(LfwView view, String menuBar,
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

	public static void hideOneBar(LfwView view, String menuBar,
			String exceptionItem) {
		MenubarComp pubbar = view.getViewMenus().getMenuBar(menuBar);
		if (pubbar == null)
			return;
		if (pubbar.getItem(exceptionItem) != null) {
			pubbar.getItem(exceptionItem).setVisible(false);
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
	 * 隐藏子表按钮
	 * 
	 * @param view
	 * @param menuBar
	 */
	public static void hideChildBar(LfwView view, String menuBar,
			String[] menuIds) {
		GridComp gc = (GridComp) view.getViewComponents().getComponent(menuBar);
		if (gc == null)
			return;
		MenubarComp mbc_c = gc.getMenuBar();
		if (mbc_c == null)
			return;
		for (String menuid : menuIds) {
			if (mbc_c.getItem(menuid) != null) {
				mbc_c.getItem(menuid).setVisible(false);
			}

		}
	}

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
	 */
	public static void setColVal(Dataset ds, Row row, String field, Object val) {
		int fieldindex = ds.nameToIndex(field);
		row.setValue(fieldindex, val);
	}

	/**
	 * 控制 是否备案、是否计划 显示
	 * 
	 * @param ds
	 * @param isvisible
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

	/**
	 * 弹出View
	 * 
	 * @param winid
	 * @param title
	 * @param paramMap
	 */
	public static void showView(String winid, String title, Map paramMap) {
		OpenProperties props = new OpenProperties(winid, title, "-1", "-1",
				paramMap);
		AppLifeCycleContext.current().getViewContext().navgateTo(props);
	}

	/**
	 * copy子表数据集
	 * 
	 * @param detailDs
	 *            子表数据集
	 * @param mainrow
	 *            主row
	 * @param relaterows
	 *            copy来的所有row
	 * @return
	 */
	public static Dataset CopyDetalRow(Dataset detailDs, Row mainrow,
			Row[] relaterows) {
		if (detailDs != null) {
			Field childPrimaryField = null;
			FieldSet childFields = detailDs.getFieldSet();
			for (int j = 0; j < childFields.getFieldCount(); j++) {
				if (childFields.getField(j).isPrimaryKey()) {
					childPrimaryField = childFields.getField(j);
					break;
				}
			}
			if (childPrimaryField == null)
				throw new LfwRuntimeException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("weberm_0", "0E010001-0044")/*
																				 * @
																				 * res
																				 * "数据集"
																				 */
						+ detailDs.getId()
						+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
								.getStrByID("weberm_0", "0E010001-0045")/*
																		 * @res
																		 * "没有配置主键字段!"
																		 */);
			// 增加新的一页
			detailDs.setCurrentKey(mainrow.getRowId());
			detailDs.setEnabled(true);
			for (int j = 0; j < relaterows.length; j++) {
				Row childRow = relaterows[j];
				if (childRow instanceof EmptyRow)
					continue;
				Row childCopyRow = (Row) childRow.clone();
				childCopyRow.setValue(
						detailDs.nameToIndex(childPrimaryField.getId()), null);
				detailDs.addRow(childCopyRow);
			}

		}
		return detailDs;
	}

	/**
	 * 
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

	public static void setGridColIsEditable(LfwView editView, String gridName,
			String[] eableFields, boolean eableflag) {
		GridComp grid = (GridComp) editView.getViewComponents().getComponent(
				gridName);
		List<IGridColumn> gridcomp = (List<IGridColumn>) grid.getColumnList();
		for (int i = 0; i < gridcomp.size(); i++) {
			GridColumn gridcolumn = (GridColumn) gridcomp.get(i);
			gridcolumn.setEditable(eableflag);
			for (String eableField : eableFields) {
				if (gridcolumn.getField().equals(eableField)) {
					gridcolumn.setEditable(!eableflag);
				}
			}
		}
	}

	/**
	 * 设置Grid 列不能为空
	 * 
	 * @param editView
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
	/**
	 * 设置Grid 列不能为空
	 * 
	 * @param editView
	 * @param gridName
	 * @param eableFields
	 *            字段数组
	 */
	public static void setGridColNullAble(Dataset ds, String gridName,
			String[] eableFields,boolean flag) {
		GridComp grid = (GridComp) ds.getView().getViewComponents()
				.getComponent(gridName);
		if (grid == null)
			return;
		List<IGridColumn> gridcomp = (List<IGridColumn>) grid.getColumnList();
		for (int i = 0; i < gridcomp.size(); i++) {
			GridColumn gridcolumn = (GridColumn) gridcomp.get(i);
			for (String eableField : eableFields) {
				if (gridcolumn.getField().equals(eableField)) {
					gridcolumn.setNullAble(flag);
				}
			}
		}
	}

	/***
	 * 设置表格字段隐藏
	 * 
	 * @param ds
	 * @param gridName
	 * @param eableFields
	 *            //显示字段
	 */
	public static void setGridColVisable(Dataset ds, String gridName,
			String[] eableFields) {
		GridComp grid = (GridComp) ds.getView().getViewComponents()
				.getComponent(gridName);
		if (grid == null)
			return;
		List<IGridColumn> gridcomp = (List<IGridColumn>) grid.getColumnList();
		for (int i = 0; i < gridcomp.size(); i++) {
			GridColumn gridcolumn = (GridColumn) gridcomp.get(i);
			gridcolumn.setVisible(false);
			for (String eableField : eableFields) {
				if (gridcolumn.getField().equals(eableField)) {
					gridcolumn.setVisible(true);
				}
			}
		}
	}

	/***
	 * 设置表格字段隐藏
	 * 
	 * @param ds
	 * @param gridName
	 * @param eableFields
	 *            //显示字段
	 */
	public static void setGridColVisableFalse(Dataset ds, String gridName,
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
					gridcolumn.setVisible(false);
				}
			}
		}
	}
	
	public static void setGridColVisableFalse(LfwView main, String gridName,
			String[] eableFields) {
		GridComp grid = (GridComp) main.getViewComponents()
				.getComponent(gridName);
		if (grid == null)
			return;
		List<IGridColumn> gridcomp = (List<IGridColumn>) grid.getColumnList();
		for (int i = 0; i < gridcomp.size(); i++) {
			GridColumn gridcolumn = (GridColumn) gridcomp.get(i);
			for (String eableField : eableFields) {
				if (gridcolumn.getField().equals(eableField)) {
					gridcolumn.setVisible(false);
				}
			}
		}
	}

	/***
	 * 设置表单显示字段
	 * 
	 * @param ds
	 * @param formName
	 * @param eableFields
	 *            //显示字段
	 */
	public static void setFormElementVisable(Dataset ds, String formName,
			String[] eableFields) {
		FormComp form = (FormComp) ds.getView().getViewComponents()
				.getComponent(formName);
		if (form == null)
			return;
		for (FormElement fe : form.getElementList()) {
			fe.setVisible(false);
			for (String eableField : eableFields) {
				if (fe.getField().equals(eableField)) {
					fe.setVisible(true);
				}
			}
		}
	}

	public static void setFormElementNullable(Dataset ds, String formName,
			String[] eableFields) {
		FormComp form = (FormComp) ds.getView().getViewComponents()
				.getComponent(formName);
		if (form == null)
			return;
		for (FormElement fe : form.getElementList()) {
			fe.setNullAble(true);
			for (String eableField : eableFields) {
				if (fe.getField().equals(eableField)) {
					fe.setNullAble(false);
				}
			}
		}
	}
	
	public static void setFormElementNullable(Dataset ds, String formName,
			String[] eableFields,boolean flag) {
		FormComp form = (FormComp) ds.getView().getViewComponents()
				.getComponent(formName);
		if (form == null)
			return;
		for (FormElement fe : form.getElementList()) {
			fe.setNullAble(flag);
			for (String eableField : eableFields) {
				if (fe.getField().equals(eableField)) {
					fe.setNullAble(!flag);
				}
			}
		}
	}
	
	public static void setFormElementEdit(Dataset ds, String formName,
			String[] editFields,boolean flag) {
		FormComp form = (FormComp) ds.getView().getViewComponents()
				.getComponent(formName);
		if (form == null)
			return;
		for (FormElement fe : form.getElementList()) {
			fe.setEditable(flag);
			for (String eableField : editFields) {
				if (fe.getField().equals(eableField)) {
					fe.setEditable(!flag);
				}
			}
		}
	}

	// 列表界面刷新
	public static void doRefresh(Row savedRow, Dataset ds) {
		savedRow.setRowId(ds.getEmptyRow().getRowId());
		// 标识是否编辑
		boolean isEdit = false;
		// 当前页所有选中行
		Row[] selRows = ds.getSelectedRows();
		int len = selRows != null ? selRows.length : 0;
		if (len > 0) {
			int pkIndex = ds.nameToIndex(ds.getPrimaryKeyField());
			for (int i = 0; i < len; i++) {
				if (selRows[i] == null || selRows[i].getValue(pkIndex) == null) {
					continue;
				} // PK值相同,父页面更新数据.
				if (selRows[i].getValue(pkIndex).equals(
						savedRow.getValue(pkIndex))) {
					isEdit = true;
					int index = ds.getRowIndex(selRows[i]);
					if (index >= 0) {
						ds.removeRow(index);
						ds.insertRow(index, savedRow);
						ds.setRowSelectIndex(index);
					}
					break;
				}
			}
		}
		if (!isEdit) {
			int pageSize = ds.getPageSize();
			if (pageSize <= 0) {
				if (ds.getCurrentRowSet() != null
						&& ds.getCurrentRowSet().getPaginationInfo() != null) {
					pageSize = ds.getCurrentRowSet().getPaginationInfo()
							.getPageSize();
				}
			}
			if (pageSize > 0 && ds.getCurrentRowData() != null
					&& ds.getCurrentRowData().getRowCount() >= pageSize) {
				ds.removeRow(ds.getCurrentRowData().getRowCount() - 1);
				ds.insertRow(0, savedRow);
				ds.setRowSelectIndex(0);
			} else {
				ds.insertRow(0, savedRow);
				ds.setRowSelectIndex(0);
			}
		}
	}
}
