package nc.scap.pub.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uap.web.bd.pub.AppUtil;

import com.scap.pub.attlist.AttachParam;

import nc.scap.pub.itf.IGlobalConstants;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebComponent;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.ctx.WindowContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.DatasetRelation;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwView;

public class CpWinUtil implements IGlobalConstants {

	/**
	 * 获取当前View对象，等同于getCurrentViewContext().getView();
	 * @author taoye 2013-7-23
	 */
	public static LfwView getView() {
		return getWinCtx().getCurrentViewContext().getView();
	}
	
	/**
	 * 根据viewId获取指定的View对象
	 * @author taoye 2013-7-23
	 */
	public static LfwView getView(String viewId) {
		return getWinCtx().getViewContext(viewId).getView();
	}
	
	/**
	 * 根据数据集ID获取当前View对象的指定数据集
	 * @author taoye 2013-7-23
	 */
	public static Dataset getDataset(String datasetId) {
		return getView().getViewModels().getDataset(datasetId);
	}
	/**
	 * 获取指定View的指定数据集
	 * @author taoye 2013-7-23
	 */
	public static Dataset getDataset(LfwView view, String datasetId) {
		return view.getViewModels().getDataset(datasetId);
	}
	/**
	 * 获取指定View的指定控件
	 * @author taoye 2013-7-23
	 */
	public static WebComponent getComponent(String compId) {
		return getView().getViewComponents().getComponent(compId);
	}
	/**
	 * 获取指定View的指定控件
	 * @author taoye 2013-7-23
	 */
	public static WebComponent getComponent(LfwView view, String compId) {
		return view.getViewComponents().getComponent(compId);
	}
	
	/**
	 * 获取指定数据集的所有行
	 * @author taoye 2013-7-23
	 */
	public static Row[] getRows(Dataset ds) {
		return ds.getCurrentRowSet().getCurrentRowData().getRows();
	}
	
	/**
	 * 获取指定数据集选中行的指定字段的值
	 * @author taoye 2013-7-24
	 */
	public static Object getValue(Dataset ds, String field) {
		return ds.getSelectedRow().getValue(ds.nameToIndex(field));
	}
	
	/**
	 * 给指定数据集选中行的指定字段赋值
	 * @author taoye 2013-7-24
	 */
	public static void setValue(Dataset ds, String field, Object value) {
		ds.getSelectedRow().setValue(ds.nameToIndex(field), value);
	}
	
	/**
	 * 获取指定View的指定文本域控件
	 * @author taoye 2013-7-23
	 */
	public static WebComponent getTextComp(LfwView view, String compId) {
		return view.getViewComponents().getComponent(compId);
	}
	
	/**
	 * 获取指定View的指定菜单控件
	 * @author taoye 2013-8-7
	 */
	public static MenubarComp getMenuBarComp(LfwView view, String compId) {
		return view.getViewMenus().getMenuBar(compId);
	}
	
	
	/**
	 * 按钮相关的获取、设置可见性、设置可编辑
	 */
	/**
	 * 获取指定View中指定菜单控件的指定按钮
	 * @author taoye 2013-7-23
	 */
	public static WebComponent getMenuItemsComp(LfwView view, String menuBarId, String menuItemId) {
		return view.getViewMenus().getMenuBar(menuBarId).getItem(menuItemId);
	}
	/**
	 * 将指定View的指定菜单中的指定按钮的可见性设置成指定的状态
	 * @author taoye 2013-7-23
	 */
	public static void setMenuItemVisible(LfwView view, String menuBarId, String itemId, boolean flag) {
		view.getViewMenus().getMenuBar(menuBarId).getItem(itemId).setVisible(flag);
	}
	/**
	 * 将指定View的指定菜单中的指定按钮的可见性设置成指定的状态
	 * @author taoye 2013-7-23
	 */
	public static void setMenuItemsVisible(LfwView view, String menuBarId, String[] itemIds, boolean flag) {
		MenubarComp menuBar = view.getViewMenus().getMenuBar(menuBarId);
		if(itemIds != null && itemIds.length > 0) {
			for(String itemId : itemIds) {
				menuBar.getItem(itemId).setVisible(flag);
			}
		}else {
			List<MenuItem> items = menuBar.getMenuList();
			for(MenuItem item : items) {
				item.setVisible(flag);
			}
		}
	}
	/**
	 * 将指定按钮状态设置成可见
	 * @author taoye 2013-7-23
	 */
	public static void showMenuItem(LfwView view, String menuBarId, String itemId) {
		setMenuItemVisible(view, menuBarId, itemId, true);
	}	
	/**
	 * 批量设置按钮状态为可见
	 * @author taoye 2013-7-23
	 */
	public static void showMenuItems(LfwView view, String menuBarId, String[] itemIds) {
		setMenuItemsVisible(view, menuBarId, itemIds, true);
	}
	/**
	 * 将指定按钮状态设置成不可见
	 * @author taoye 2013-7-23
	 */
	public static void hideMenuItem(LfwView view, String menuBarId, String itemId) {
		setMenuItemVisible(view, menuBarId, itemId, false);
	}
	/**
	 * 批量设置按钮状态为不可见
	 * @author taoye 2013-7-23
	 */
	public static void hideMenuItems(LfwView view, String menuBarId, String[] itemIds) {
		setMenuItemsVisible(view, menuBarId, itemIds, false);
	}
	/**
	 * 将所有按钮装都都设置为不可见
	 * @author taoye 2013-7-23
	 */
	public static void hideMenuItemsAll(LfwView view, String menuBarId) {
		setMenuItemsVisible(view, menuBarId, null, false);
	}
	/**
	 * 将指定按钮之外批量设置按钮状态为不可见
	 * @author taoye 2013-7-23
	 */
	public static void hideMenuItemsWithoutOne(LfwView view, String menuBarId, String itemId) {
		hideMenuItemsAll(view, menuBarId);
		showMenuItem(view, menuBarId, itemId);
	}
	/**
	 * 将指定按钮之外批量设置按钮状态为不可见
	 * @author taoye 2013-7-23
	 */
	public static void hideMenuItemsWithoutAny(LfwView view, String menuBarId, String[] itemIds) {
		hideMenuItemsAll(view, menuBarId);
		showMenuItems(view, menuBarId, itemIds);
	}
	
	/**
	 * 将指定子表Grid控件中的按钮隐藏
	 * @author taoye 2013-7-24
	 */
    public static void hideChildMenuItem(LfwView view, String gridId){
    	GridComp grid = (GridComp) view.getViewComponents().getComponent(gridId);
		MenubarComp menuBar = grid.getMenuBar();
		List<MenuItem> menuilist = menuBar.getMenuList();
		for (MenuItem item : menuilist){
			item.setVisible(false);
		}
    }
	/**
	 * 将指定View的指定菜单中的指定按钮的可编辑性设置成指定的状态
	 * @author taoye 2013-7-23
	 */
	public static void setMenuItemEnable(LfwView view, String menuBarId, String itemId, boolean flag) {
		view.getViewMenus().getMenuBar(menuBarId).getItem(itemId).setEnabled(flag);
	}
	/**
	 * 将指定View的指定菜单中的指定按钮的可编辑性设置成指定的状态
	 * @author taoye 2013-7-23
	 */
	public static void setMenuItemsEnable(LfwView view, String menuBarId, String[] itemIds, boolean flag) {
		MenubarComp menuBar = view.getViewMenus().getMenuBar(menuBarId);
		if(itemIds != null && itemIds.length > 0) {
			for(String itemId : itemIds) {
				menuBar.getItem(itemId).setEnabled(flag);
			}
		}else {
			List<MenuItem> items = menuBar.getMenuList();
			for(MenuItem item : items) {
				item.setEnabled(flag);
			}
		}
	}
	/**
	 * 将指定按钮状态设置成可见
	 * @author taoye 2013-7-23
	 */
	public static void EnableMenuItemTrue(LfwView view, String menuBarId, String itemId) {
		setMenuItemEnable(view, menuBarId, itemId, true);
	}	
	/**
	 * 批量设置按钮状态为可见
	 * @author taoye 2013-7-23
	 */
	public static void EnableMenuItemsTrue(LfwView view, String menuBarId, String[] itemIds) {
		setMenuItemsEnable(view, menuBarId, itemIds, true);
	}
	/**
	 * 将指定按钮状态设置成不可见
	 * @author taoye 2013-7-23
	 */
	public static void EnableMenuItemFalse(LfwView view, String menuBarId, String itemId) {
		setMenuItemEnable(view, menuBarId, itemId, false);
	}
	/**
	 * 批量设置按钮状态为不可见
	 * @author taoye 2013-7-23
	 */
	public static void EnableMenuItemsFalse(LfwView view, String menuBarId, String[] itemIds) {
		setMenuItemsEnable(view, menuBarId, itemIds, false);
	}
	/**
	 * 将所有按钮装都都设置为不可见
	 * @author taoye 2013-7-23
	 */
	public static void EnableMenuItemsFalseAll(LfwView view, String menuBarId) {
		setMenuItemsEnable(view, menuBarId, null, false);
	}
	/**
	 * 将所有按钮装都都设置为不可见
	 * @author taoye 2013-7-23
	 */
	public static void EnableMenuItemsTrueAll(LfwView view, String menuBarId) {
		setMenuItemsEnable(view, menuBarId, null, true);
	}
	/**
	 * 将指定按钮之外批量设置按钮状态为不可见
	 * @author taoye 2013-7-23
	 */
	public static void EnableMenuItemsFalseWithoutOne(LfwView view, String menuBarId, String itemId) {
		EnableMenuItemsFalseAll(view, menuBarId);
		EnableMenuItemTrue(view, menuBarId, itemId);
	}
	/**
	 * 将指定按钮之外批量设置按钮状态为不可见
	 * @author taoye 2013-7-23
	 */
	public static void EnableMenuItemsFalseWithoutAny(LfwView view, String menuBarId, String[] itemIds) {
		EnableMenuItemsFalseAll(view, menuBarId);
		EnableMenuItemsTrue(view, menuBarId, itemIds);
	}
	
	
	
	/**
	 * 获取当前应用上下文
	 * @author taoye 2013-7-23
	 */
	public static AppLifeCycleContext getAppCtx() {
		return AppLifeCycleContext.current();
	}
	
	/**
	 * 获取当前窗口上下文
	 * @author taoye 2013-7-23
	 */
	public static WindowContext getWinCtx() {
		return getAppCtx().getWindowContext();
	}
	
	/**
	 * 获取信息合成窗口的弹窗对象，需要调用者传入处理业务所需主键，该主键后期将由调用者在具体实现类中使用，本方法只做过程，不做业务
	 * @author taoye 2013-7-30
	 */
	public static void getInfoComposeWin(String pkValue) {
		OpenProperties props = new OpenProperties(INFO_COMPOSE_WIN_ID, INFO_COMPOSE_WIN_NAME);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put(IGlobalConstants.MAIN_BILL_ID, pkValue);
		props.setParamMap(paramMap);
		AppLifeCycleContext.current().getApplicationContext().navgateTo(props);
	}
	
	/**
	 * 获取信息合成窗口的弹窗对象，需要调用者传入处理业务所需主键，该主键后期将由调用者在具体实现类中使用，本方法只做过程，不做业务
	 * @author taoye 2013-7-30
	 */
	public static void getCentrallyMaintainedWin(Dataset ds) {
		if (ds.getSelectedIndex() < 0)
			throw new LfwRuntimeException("请选中行");
		Row[] rows = ds.getSelectedRows();
		String[] pkValues = new String[rows.length];
		int index = ds.nameToIndex("pk_psndoc");
		for(int n = 0; n < rows.length; n++) {
			pkValues[n] = (String) rows[n].getValue(index);
		}
		OpenProperties props = new OpenProperties(CENTRALLY_MAINTAINED_WIN_ID, CENTRALLY_MAINTAINED_WIN_NAME);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>();
		String param = "";
		for(String pkValue : pkValues) {
			param += pkValue + ";";
		}
		paramMap.put(IGlobalConstants.MAIN_BILL_IDS, param);
		props.setParamMap(paramMap);
		AppLifeCycleContext.current().getApplicationContext().navgateTo(props);
	}
	
	/**
	 * 获取人员转出面板
	 * @author taoye 2013-8-2
	 */
	public static void getRolloutWin(String psntype, Row[] rows, Dataset ds) {
		OpenProperties props = new OpenProperties("rowout", "转出页面");
		// props.setButtonZone(false);
		props.setHeight("265");
		props.setWidth("600");
		AppUtil.addAppAttr("psntype",psntype);
		AppUtil.addAppAttr("rows", rows);
		AppUtil.addAppAttr("ds", ds);
		Map<String, String> paramMap = new HashMap<String, String>();
		props.setParamMap(paramMap);
		AppLifeCycleContext.current().getApplicationContext().getCurrentWindowContext().popView(props);
	}
	
	/**
	 * 获取转入转出原因界面
	 * @author taoye 2013-8-2
	 */
	public static void getRollinoutWin() {
		OpenProperties props = new OpenProperties();
		props.setOpenId("psnm_psnmanage.rollinout_cardwin");
		// props.setButtonZone(false);
		props.setTitle("转入转出原因");
		props.setHeight("300");
		props.setWidth("600");
		Map<String, String> paramMap = new HashMap<String, String>();
		props.setParamMap(paramMap);		
		AppLifeCycleContext.current().getApplicationContext().navgateTo(props);

	}
	
	/**
	 * 关闭当前窗口
	 * @author taoye 2013-7-30
	 */
	public static void closeWinDialog() {
		AppLifeCycleContext.current().getApplicationContext().closeWinDialog();  
	}
	
	public static void refshAttachList(String viewId, String plugId, String pkValue) {
		refshAttachList(viewId, plugId, pkValue, true, true, true);
	}
	
	public static void refshAttachList(String viewId, String plugId, String pkValue, boolean canUpload, boolean canDownload, boolean canDel) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		AttachParam attachParam = new AttachParam(pkValue, canUpload, canDownload, canDel);
		paramMap.put(IGlobalConstants.ATTACH_PARAM, attachParam);
		UifPlugoutCmd plugcmd = new UifPlugoutCmd(viewId, plugId, paramMap);
		plugcmd.execute();
		
	}
	public static void refshAttachList(String viewId, String plugId, String pkValue, boolean canUpload, boolean canDownload, boolean canDel, String billtype) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		AttachParam attachParam = new AttachParam(pkValue, canUpload, canDownload, canDel);
		attachParam.setBillType(billtype);
		paramMap.put(IGlobalConstants.ATTACH_PARAM, attachParam);
		UifPlugoutCmd plugcmd = new UifPlugoutCmd(viewId, plugId, paramMap);
		plugcmd.execute();
		
	}
	
	/**
	 * 设置所有子表可编辑
	 * @author taoye 2013-8-8
	 */
	public static void setDetailDsEnableFalse(Dataset ds) {
		LfwView view = ds.getView();
		DatasetRelation[] rels = view.getViewModels().getDsrelations().getDsRelations(ds.getId());
		int len = rels != null ? rels.length : 0;
		if (len > 0) {
			String[] detailDsIds = new String[len];
			for (int i = 0; i < len; i++) {
				detailDsIds[i] = rels[i].getDetailDataset();
				view.getViewModels().getDataset(detailDsIds[i]).setEnabled(true);
			}
		}
	}
	
	/**
	 * 将同一个数据集下面的两行数据同步，row 需要同步的行， oldrow 提取数据的行，注意，可以不是同一数据集，但是数据集的列顺序和含义必须是一致的
	 * @author taoye 2013-9-9
	 */
	public static void RowData2Row(Dataset ds, Row row, Dataset sourceDs, Row sourceRow) {
		List<Field> fields = sourceDs.getFieldSet().getFieldList();
		for(Field field : fields) {
			String name = field.getId();
			if(ds.nameToIndex(name) > -1) {
				row.setValue(ds.nameToIndex(name), sourceRow.getValue(sourceDs.nameToIndex(name)));
			}
		}
	}
	
}
