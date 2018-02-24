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
	 *            ����
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
	 * ���ð�ť״̬
	 * 
	 * @param view
	 * @param menuBar
	 * @param exceptionItems
	 *            ��Ҫ���ư�ť��������
	 * @param state
	 *            ״̬
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
	 * �����ӱ�ť
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
	 * �����ֶ��� �����ֶ�ֵ
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
	 * ���� �Ƿ񱸰����Ƿ�ƻ� ��ʾ
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
	 * ����View
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
	 * copy�ӱ����ݼ�
	 * 
	 * @param detailDs
	 *            �ӱ����ݼ�
	 * @param mainrow
	 *            ��row
	 * @param relaterows
	 *            copy��������row
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
																				 * "���ݼ�"
																				 */
						+ detailDs.getId()
						+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
								.getStrByID("weberm_0", "0E010001-0045")/*
																		 * @res
																		 * "û�����������ֶ�!"
																		 */);
			// �����µ�һҳ
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
	 *            ���Ƶ��ֶ�
	 * @param eableflag
	 *            true eableFields���ɱ༭; false eableFields:���ɱ༭
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
	 * ����Grid �в���Ϊ��
	 * 
	 * @param editView
	 * @param gridName
	 * @param eableFields
	 *            �ֶ�����
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
	 * ����Grid �в���Ϊ��
	 * 
	 * @param editView
	 * @param gridName
	 * @param eableFields
	 *            �ֶ�����
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
	 * ���ñ���ֶ�����
	 * 
	 * @param ds
	 * @param gridName
	 * @param eableFields
	 *            //��ʾ�ֶ�
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
	 * ���ñ���ֶ�����
	 * 
	 * @param ds
	 * @param gridName
	 * @param eableFields
	 *            //��ʾ�ֶ�
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
	 * ���ñ���ʾ�ֶ�
	 * 
	 * @param ds
	 * @param formName
	 * @param eableFields
	 *            //��ʾ�ֶ�
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

	// �б����ˢ��
	public static void doRefresh(Row savedRow, Dataset ds) {
		savedRow.setRowId(ds.getEmptyRow().getRowId());
		// ��ʶ�Ƿ�༭
		boolean isEdit = false;
		// ��ǰҳ����ѡ����
		Row[] selRows = ds.getSelectedRows();
		int len = selRows != null ? selRows.length : 0;
		if (len > 0) {
			int pkIndex = ds.nameToIndex(ds.getPrimaryKeyField());
			for (int i = 0; i < len; i++) {
				if (selRows[i] == null || selRows[i].getValue(pkIndex) == null) {
					continue;
				} // PKֵ��ͬ,��ҳ���������.
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
