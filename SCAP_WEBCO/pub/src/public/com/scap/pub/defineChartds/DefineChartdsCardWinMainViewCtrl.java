package com.scap.pub.defineChartds;

import java.io.File;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.jdbc.framework.processor.MapListProcessor;
import nc.scap.pub.util.CpWinUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.ScapChartDsVO;
import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifAddCmd;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.lfw.core.cmd.UifLineDelCmd;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.cmd.UifSaveCmdRV;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Field;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.file.FillFileInfoHelper;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.vo.pub.BusinessException;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;

 /**
 * 卡片窗口默认逻辑
 * 
 */
public class DefineChartdsCardWinMainViewCtrl <T extends WebElement> extends AbstractMasterSlaveViewController{
	private static final long serialVersionUID = -1;
	private static final String PARAM_BILLITEM = "billitem";
	private static final String PLUGOUT_ID = "afterSavePlugout";
  
	/**
	 * 页面显示事件
	 * 
	 * @param dialogEvent
	 */
	public void beforeShow(DialogEvent dialogEvent) {
		Dataset masterDs = this.getMasterDs();
		masterDs.clear();
		
		String oper = getOperator();
		if(AppConsts.OPE_ADD.equals(oper)){
			CmdInvoker.invoke(new UifAddCmd(getMasterDsId()) {
				protected void onBeforeRowAdd(Row row) {
					setAutoFillValue(row);
					
					Dataset masterDs = getMasterDs();
					String pk_primarykey = generatePk();
					row.setValue(masterDs.nameToIndex(masterDs.getPrimaryKeyField()), pk_primarykey);
					FillFileInfoHelper.resetItem(pk_primarykey);
					FillFileInfoHelper.fillFileInfo(masterDs, row);
				}
			});
		}else if(AppConsts.OPE_EDIT.equals(oper)){
			CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs){
				@Override
				protected void onAfterDatasetLoad() {
					this.getDs().setEnabled(true);
					
					String primaryKey = this.getDs().getPrimaryKeyField();
					if (primaryKey == null) {
						throw new LfwRuntimeException("当前Dataset没有设置主键!");
					}
					String primaryKeyValue = (String) this.getDs().getSelectedRow().getValue(this.getDs().nameToIndex(primaryKey));					
					FillFileInfoHelper.resetItem(primaryKeyValue);
				}
			});
		}
		String x = ContextResourceUtil.getCurrentAppPath();
		x = File.separator;
		
		UIMeta uimeta = CpWinUtil.getWinCtx().getViewContext("main").getUIMeta();
		UIFlowvPanel panel = (UIFlowvPanel) uimeta.findChildById("g_p_3");
		panel.setVisible(false);
	}
  
	/**
	 * 主数据选中逻辑
	 * 
	 * @param datasetEvent
	 */
	public void onAfterRowSelect(DatasetEvent dsEvent) {
		Dataset ds = dsEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
	}
  
	/**
	 * 保存
	 * 
	 * @param datasetEvent
	 */

	public void onSave(MouseEvent<?> mouseEvent) throws BusinessException {
		Dataset masterDs = this.getMasterDs();
		Object pkid = masterDs.getSelectedRow().getValue(masterDs.nameToIndex(ScapChartDsVO.PK_CHART_DS));
		Object code = masterDs.getSelectedRow().getValue(masterDs.nameToIndex(ScapChartDsVO.CODE));
		if(code != null) {
			String condition = "code = '" + code.toString() + "'";
			if(pkid != null) {
				condition += " and pk_chart_ds != '" + pkid.toString() + "'";
			}
			ScapChartDsVO[] vos = (ScapChartDsVO[]) ScapDAO.retrieveByCondition(ScapChartDsVO.class, condition);
			if(vos != null && vos.length > 0) {
				throw new LfwRuntimeException("编码已存在，请重新输入！");
			}
		}
		CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this.getDetailDsIds(), false));
		masterDs.setEnabled(true);
		this.getCurrentAppCtx().closeWinDialog();
		
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		Row savedRow = masterDs.getSelectedRow();
		paramMap.put(OPERATE_ROW, savedRow);
		CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(), PLUGOUT_ID, paramMap));
	}

  
	/**
	 * 预览
	 */
	public void onPreview(MouseEvent<?> mouseEvent) throws BusinessException {
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
	
	/**
	 * 删除
	 */
	public void onDelete(MouseEvent<?> mouseEvent) throws BusinessException {
		CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
	}

	/**
	 * 返回
	 */
	public void onBack(MouseEvent<?> mouseEvent) throws BusinessException {
		this.getCurrentAppCtx().closeWinDialog();
	}
	
	
	/**
	 * 设置PK_ORG字段值
	 * @param row
	 */
	private void setAutoFillValue(Row row){
		if(row != null){
			Dataset ds = this.getCurrentView().getViewModels().getDataset(this.getMasterDsId());
			
			String pkOrg = this.getCurrentAppCtx().getAppEnvironment().getPk_org();
			if(pkOrg != null){
				int pkOrgIndex = ds.nameToIndex(PK_ORG);
				if(pkOrgIndex >= 0){
					row.setValue(pkOrgIndex, pkOrg);		
				}
			}
			String pkGroup = this.getCurrentAppCtx().getAppEnvironment().getPk_group();
			if(pkGroup != null){
				int pkGroupIndex = ds.nameToIndex(PK_GROUP);
				if(pkGroupIndex >= 0){
					row.setValue(pkGroupIndex, pkGroup);		
				}
			}
		}
	}

  
	/**
	 * 子表新增
	 */
	public void onGridAddClick(MouseEvent<?> mouseEvent) {
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		Row emptyRow = ds.getEmptyRow();
		ds.addRow(emptyRow);
		ds.setRowSelectIndex(ds.getRowIndex(emptyRow));
		ds.setEnabled(true);
	}

	/**
	 * 子表编辑
	 */
	public void onGridEditClick(MouseEvent<?> mouseEvent) {
		
	}

	/**
	 * 子表删除
	 */
	public void onGridDeleteClick(MouseEvent<?> mouseEvent) {
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		CmdInvoker.invoke(new UifLineDelCmd(dsId));
	}


	@Override
	protected String getMasterDsId() {
		return "scap_chart_ds";
	}
}