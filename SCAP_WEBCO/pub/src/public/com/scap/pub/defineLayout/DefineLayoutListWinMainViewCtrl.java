package com.scap.pub.defineLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nc.scap.pub.util.CpChartUtil;
import nc.scap.pub.util.CpVOUtil;
import nc.scap.pub.util.CpWinUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.ScapLayoutContainerVO;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.web.bd.pub.AppUtil;
/** 
 * 信息列表默认逻辑
 */
public class DefineLayoutListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="com.scap.pub.chartComps.defineLayout_cardwin";
  private static final String CARD_WIN_TITLE="编辑";
  /** 
 * 主数据加载逻辑
 * @param dataLoadEvent
 */
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
  /** 
 * 主数据选中逻辑
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent datasetEvent){
    Dataset layout = CpWinUtil.getDataset(CpWinUtil.getView(MAIN_VIEW_ID), "scap_layout");
	  	Row row = layout.getSelectedRow();
	  	Dataset container = CpWinUtil.getDataset(CpWinUtil.getView(MAIN_VIEW_ID), "scap_layout_container");
	  	container.setLastCondition(" layout_container = '" + row.getValue(layout.nameToIndex("pk_layout")) + "'");
	  	container.setOrderByPart("sort");
		CmdInvoker.invoke(new UifDatasetLoadCmd(container));
  }
  /** 
 * 容器选中事件
 * @author taoye 2013-8-26
 */
  public void onAfterContainerSelect(  DatasetEvent datasetEvent){
    Dataset container = CpWinUtil.getDataset(CpWinUtil.getView(MAIN_VIEW_ID), "scap_layout_container");
	  System.out.println("");
  }
  /** 
 * 新增
 * @param mouseEvent
 */
  public void onAdd(  MouseEvent mouseEvent){
    OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		props.setWidth("800");
		props.setHeight("600");
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_ADD);
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * 编辑
 * @param scriptEvent
 */
  public void onEdit(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待编辑数据");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		props.setWidth("800");
		props.setHeight("600");
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * 删除
 * @param scriptEvent
 */
  public void onDelete(  MouseEvent mouseEvent){
    CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
  }
  /** 
 * 外部触发刷新
 * @param keys
 */
  public void doRefresh(  Map<?,?> keys){
    Row savedRow = (Row) keys.get(OPERATE_ROW);
			if (savedRow != null) {
				Dataset ds = this.getMasterDs();
				savedRow.setRowId(ds.getEmptyRow().getRowId());
				String sign = this.getOperator();
				if (AppConsts.OPE_EDIT.equals(sign)) {
					int index = ds.getRowIndex(ds.getSelectedRow());
					if (index >= 0) {
						ds.removeRow(index);
						ds.insertRow(index, savedRow);
					}
				} else if (AppConsts.OPE_ADD.equals(sign)) {
					ds.addRow(savedRow);
				}
			}
  }
  /** 
 * 查询plugin
 * @param keys
 */
  public void doQueryChange(  Map<?,?> keys){
    FromWhereSQL whereSql = (FromWhereSQL) keys.get(FromWhereSQL.WHERE_SQL_CONST);
			CmdInvoker.invoke(new QueryCmd(MAIN_VIEW_ID, this.getMasterDsId(), whereSql.getWhere()));
  }
  private void clearDetailDs(){
    Dataset[] detailDss = this.getDetailDs(this.getDetailDsIds());
		int len = detailDss != null ? detailDss.length : 0;
		for (int i = 0; i < len; i++) {
			if (detailDss[i] != null) {
				detailDss[i].clear();
			}
		}
  }
  @Override protected String getMasterDsId(){
    return "scap_layout";
  }
  /** 
 * 新增容器
 * @param mouseEvent
 */
  public void onAddContainer(  MouseEvent mouseEvent){
    String pk_parent = CpChartUtil.getParentContainer();
	  OpenProperties prop = new OpenProperties("container", "容器维护面板", "800", "600");
	  AppUtil.addAppAttr(AppConsts.OPE_SIGN, AppConsts.OPE_ADD);
	  AppLifeCycleContext.current().getWindowContext().popView(prop);
  }
  /** 
 * 新增弹出容器
 * @param mouseEvent
 */
  public void onAddPopContainer(  MouseEvent mouseEvent){
    Dataset layout = CpWinUtil.getDataset(CpWinUtil.getView("main"), "scap_layout");
    Dataset container = CpWinUtil.getDataset(CpWinUtil.getView("main"), "scap_layout_container");
    String pkLayout = layout.getSelectedRow().getString(layout.nameToIndex("pk_layout"));
    
    Row row = container.getEmptyRow();
    row.setValue(container.nameToIndex("layout_container"), pkLayout);
    row.setValue(container.nameToIndex("parent_container"), pkLayout);
    row.setValue(container.nameToIndex("code"), "pop" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
    row.setValue(container.nameToIndex("name"), "弹出容器");
    row.setValue(container.nameToIndex("container_type"), CpChartUtil.CON_TYPE_POP);
    row.setValue(container.nameToIndex("sort"), 100);
    ScapLayoutContainerVO vo = new CpVOUtil<ScapLayoutContainerVO>().getVO(container, row, new ScapLayoutContainerVO());
    ScapDAO.insertOrUpdateVO(vo);
    row.setValue(container.nameToIndex("pk_layout_container"), vo.getPrimaryKey());
    container.addRow(row);
  }
  /** 
 * 编辑容器
 * @param mouseEvent
 */
  public void onEditContainer(  MouseEvent mouseEvent){
    Dataset container = CpWinUtil.getDataset(CpWinUtil.getView("main"), "scap_layout_container");
	  if(container.getSelectedRow() == null) {
		  throw new LfwRuntimeException("请先选中一个容器！");
	  }
	  OpenProperties prop = new OpenProperties("container", "容器维护面板", "800", "600");
//	  Map<String, String> param = new HashMap<String, String>();
//	  param.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
//	  prop.setParamMap(param);
	  AppUtil.addAppAttr(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
	  AppLifeCycleContext.current().getWindowContext().popView(prop);
  }
  /** 
 * 删除容器
 * @param mouseEvent
 */
  public void onDelContainer(  MouseEvent mouseEvent){
    Dataset container = CpWinUtil.getDataset(CpWinUtil.getView(MAIN_VIEW_ID), "scap_layout_container");
	  Row row = container.getSelectedRow();
	  String pkValue = row.getValue(container.nameToIndex("pk_layout_container")).toString();
	  ScapDAO.deleteByPk(ScapLayoutContainerVO.class, pkValue);
	  container.removeRow(row);
  }
  public void doOrgChange(  Map keys){
    
  }
}
