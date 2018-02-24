package com.scap.pub.defineChartds;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.scap.pub.util.CpWinUtil;
import nc.scap.pub.util.ScapDAO;
import nc.uap.lfw.core.cmd.CmdInvoker;
import java.util.Map;
import nc.uap.lfw.core.event.DataLoadEvent;
import java.util.HashMap;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.data.Dataset;
import java.util.List;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.vo.pub.BusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.MouseEvent;
/** 
 * 信息列表默认逻辑
 */
public class DefineChartdsListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="com.scap.pub.chartComps.defineChartds_cardwin";
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
    Dataset ds = datasetEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
  }
  /** 
 * 新增
 * @param mouseEvent
 */
  public void onAdd(  MouseEvent mouseEvent){
    OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
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
    return "scap_chart_ds";
  }
  
	/**
	 * 预览
	 */
	public void previewResult() throws BusinessException {
		LfwView view = CpWinUtil.getView();
		Dataset ds = CpWinUtil.getDataset("scap_chart_ds");
		Dataset virtualds = CpWinUtil.getDataset(view, "virtual_ds");
		GridComp virtualgrid = (GridComp) CpWinUtil.getComponent(view, "virtual_grid");
		Object sql = ds.getSelectedRow().getValue(ds.nameToIndex("source_sql"));
		virtualds.clear();
		List<Field> fields = virtualds.getFieldSet().getFieldList();
		while(fields.size() > 0) {
			virtualgrid.removeColumnByField(fields.get(0).getId(), true);
			virtualds.getFieldSet().removeField(fields.get(0));
			
		}
		
		if(sql != null) {
			String source_sql = sql.toString();
			List<Map<String, Object>> results = (List<Map<String, Object>>) ScapDAO.executeQuery(source_sql, new MapListProcessor());
			if(results != null) {
				UIMeta uimeta = CpWinUtil.getWinCtx().getViewContext("main").getUIMeta();
				UIFlowvPanel panel = (UIFlowvPanel) uimeta.findChildById("g_p_3");
				panel.setVisible(true);
				
				Object[] sets = results.get(0).keySet().toArray();
				for(Object set : sets) {
					String setname = set.toString();
					Field field = new Field();
					field.setId(setname);
					field.setText(setname);
					virtualds.getFieldSet().addField(field);
					
					GridColumn column = new GridColumn();
					column.setId(setname);
					column.setField(setname);
					column.setText(setname);
					virtualgrid.addColumn(column, true);
				}
				
				for(Map<String, Object> result : results) {
					Row row = virtualds.getEmptyRow();
					for(Object set : sets) {
						if(virtualds.nameToIndex(set.toString()) > -1) {
							row.setValue(virtualds.nameToIndex(set.toString()), result.get(set.toString()));
						}
					}
					virtualds.addRow(row);
				}
				System.out.println("1");
			}
			System.out.println("1");
		}
	}
	
}
