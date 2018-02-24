package com.scap.pub.defdoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifAddCmd;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.cmd.UifCopyCmd;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import nc.uap.lfw.core.cmd.UifLineDelCmd;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.cmd.UifSaveCmdRV;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.uap.lfw.core.file.FillFileInfoHelper;
import nc.uap.lfw.core.log.LfwLogger;
import nc.vo.pub.BusinessException;
import uap.lfw.core.locator.ServiceLocator;

 /**
 * ��Ƭ����Ĭ���߼�
 * 
 */
public class DefdocCardWinMainViewCtrl <T extends WebElement> extends AbstractMasterSlaveViewController{
	private static final long serialVersionUID = -1;
	private static final String PARAM_BILLITEM = "billitem";
	private static final String PLUGOUT_ID = "afterSavePlugout";
  
	/**
	 * ҳ����ʾ�¼�
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
						throw new LfwRuntimeException("��ǰDatasetû����������!");
					}
					String primaryKeyValue = (String) this.getDs().getSelectedRow().getValue(this.getDs().nameToIndex(primaryKey));					
					FillFileInfoHelper.resetItem(primaryKeyValue);
				}
			});
		}
	}
  
	/**
	 * ������ѡ���߼�
	 * 
	 * @param datasetEvent
	 */
	public void onAfterRowSelect(DatasetEvent dsEvent) {
		Dataset ds = dsEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
	}
  
	/**
	 * ����
	 * 
	 * @param datasetEvent
	 */

	public void onSave(MouseEvent<?> mouseEvent) throws BusinessException {
		Dataset masterDs = this.getMasterDs();
		CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this.getDetailDsIds(), false));
		masterDs.setEnabled(true);
		this.getCurrentAppCtx().closeWinDialog();
		
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		Row savedRow = masterDs.getSelectedRow();
		paramMap.put(OPERATE_ROW, savedRow);
		CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(), PLUGOUT_ID, paramMap));
	}

  
	/**
	 * ����
	 */
	public void onAdd(MouseEvent<?> mouseEvent) throws BusinessException {
		CmdInvoker.invoke(new UifAddCmd(this.getMasterDsId()) {
			@Override
			protected void onBeforeRowAdd(Row row) {
				setAutoFillValue(row);
			}
		});
	}
	
	/**
	 * ����
	 * @param mouseEvent
	 */
	public void onCopy(MouseEvent<?> mouseEvent) {
		CmdInvoker.invoke(new UifCopyCmd(this.getMasterDsId()));
	}

	/**
	 * ɾ��
	 */
	public void onDelete(MouseEvent<?> mouseEvent) throws BusinessException {
		CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
	}

	/**
	 * ����
	 */
	public void onBack(MouseEvent<?> mouseEvent) throws BusinessException {
		this.getCurrentAppCtx().closeWinDialog();
	}
	
	/**
	 * ����
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onStart(MouseEvent<?> mouseEvent) throws BusinessException {
		CmdInvoker.invoke(new UifEnableCmd(this.getMasterDsId(), true));
	}

	/**
	 * ͣ��
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onStop(MouseEvent<?> mouseEvent) throws BusinessException {
		CmdInvoker.invoke(new UifEnableCmd(this.getMasterDsId(), false));
	}

	/**
	 * ����
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onAttchFile(MouseEvent<?> mouseEvent) throws BusinessException {
		Dataset ds = this.getMasterDs();
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("��ѡ������!");
		}
		String primaryKey = ds.getPrimaryKeyField();
		if (primaryKey == null){
			throw new LfwRuntimeException("��ǰDatasetû����������!");
		}
		String primaryKeyValue = (String) row.getValue(ds.nameToIndex(primaryKey));
		Map<String, String> paramMap = null;
		if (primaryKeyValue != null){
			paramMap = new HashMap<String, String>(2);
			paramMap.put(PARAM_BILLITEM, primaryKeyValue);
		}
		CmdInvoker.invoke(new UifAttachCmd("����", paramMap));
	}
	
	/**
	 * ��ӡ
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onPrint(MouseEvent<?> mouseEvent) throws BusinessException {
		Dataset ds = this.getMasterDs();
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("��ѡ������!");
		}
		try{
			List<Dataset> list = new ArrayList<Dataset>(1);
			list.add(ds);
			DefaultPrintService printService = new DefaultPrintService();
			printService.setDatasetList(list);
			ICpPrintTemplateService service = ServiceLocator.getService(ICpPrintTemplateService.class);
			service.print(printService);
		}catch(Exception e){
			LfwLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * ����PK_ORG�ֶ�ֵ
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
	 * �ӱ�����
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
	 * �ӱ��༭
	 */
	public void onGridEditClick(MouseEvent<?> mouseEvent) {
		
	}

	/**
	 * �ӱ�ɾ��
	 */
	public void onGridDeleteClick(MouseEvent<?> mouseEvent) {
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		CmdInvoker.invoke(new UifLineDelCmd(dsId));
	}


	@Override
	protected String getMasterDsId() {
		return "defdoc";
	}
}