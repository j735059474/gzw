package nc.scap.pub.qryorgs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.QryOrgsVO;
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
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.file.FillFileInfoHelper;
import nc.uap.lfw.core.log.LfwLogger;
import nc.vo.pub.BusinessException;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.core.locator.ServiceLocator;
import uap.web.bd.pub.AppUtil;

 /**
 * 卡片窗口默认逻辑
 * 
 */
public class Qry_orgsCardWinMainViewCtrl <T extends WebElement> extends AbstractMasterSlaveViewController{
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
			//非快报让月份不能编辑
			String pk_type = (String) AppUtil.getAppAttr("pk_type");
			String type_name = (String) AppUtil.getAppAttr("type_name");
			if(pk_type == null || "".equals(pk_type)){
				throw new LfwRuntimeException("请选中类型后再修改");
			}
			FormComp gc = (FormComp)AppUtil.getCntWindowCtx().getCurrentViewContext().getView().getViewComponents().getComponent( "qry_orgs_form");
			if("快报".equals(type_name)){
				gc.getElementById(QryOrgsVO.VMONTH).setEnabled(true);
			}else {
				gc.getElementById(QryOrgsVO.VMONTH).setEnabled(false);
			}
		}
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
		
		this.checkIfCanSave(masterDs);//检查是否可以存盘
		
		CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this.getDetailDsIds(), false));
		masterDs.setEnabled(true);
		this.getCurrentAppCtx().closeWinDialog();
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		Row savedRow = masterDs.getSelectedRow();
		paramMap.put(OPERATE_ROW, savedRow);
		CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(), PLUGOUT_ID, paramMap));
//		AppInteractionUtil.showMessageDialog("保存成功");
	}

  
	/**
	 * 新增
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
	 * 复制
	 * @param mouseEvent
	 */
	public void onCopy(MouseEvent<?> mouseEvent) {
		CmdInvoker.invoke(new UifCopyCmd(this.getMasterDsId()));
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
	 * 启用
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onStart(MouseEvent<?> mouseEvent) throws BusinessException {
		CmdInvoker.invoke(new UifEnableCmd(this.getMasterDsId(), true));
	}

	/**
	 * 停用
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onStop(MouseEvent<?> mouseEvent) throws BusinessException {
		CmdInvoker.invoke(new UifEnableCmd(this.getMasterDsId(), false));
	}

	/**
	 * 附件
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onAttchFile(MouseEvent<?> mouseEvent) throws BusinessException {
		Dataset ds = this.getMasterDs();
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("请选中数据!");
		}
		String primaryKey = ds.getPrimaryKeyField();
		if (primaryKey == null){
			throw new LfwRuntimeException("当前Dataset没有设置主键!");
		}
		String primaryKeyValue = (String) row.getValue(ds.nameToIndex(primaryKey));
		Map<String, String> paramMap = null;
		if (primaryKeyValue != null){
			paramMap = new HashMap<String, String>(2);
			paramMap.put(PARAM_BILLITEM, primaryKeyValue);
		}
		CmdInvoker.invoke(new UifAttachCmd("附件", paramMap));
	}
	
	/**
	 * 打印
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onPrint(MouseEvent<?> mouseEvent) throws BusinessException {
		Dataset ds = this.getMasterDs();
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("请选中数据!");
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
	 * 设置PK_ORG字段值
	 * @param row
	 */
	private void setAutoFillValue(Row row){
		if(row != null){
			Dataset ds = this.getCurrentView().getViewModels().getDataset(this.getMasterDsId());
			
			String pkOrg = CntUtil.getCntOrgPk();
			if(pkOrg != null){
				int pkOrgIndex = ds.nameToIndex("pk_org");
				if(pkOrgIndex >= 0){
					row.setValue(pkOrgIndex, pkOrg);		
				}
			}
			String pkGroup = CntUtil.getCntGroupPk();
			if(pkGroup != null){
				int pkGroupIndex = ds.nameToIndex(PK_GROUP);
				if(pkGroupIndex >= 0){
					row.setValue(pkGroupIndex, pkGroup);		
				}
			}
			//非快报让月份不能编辑
			String pk_type = (String) AppUtil.getAppAttr("pk_type");
			String type_name = (String) AppUtil.getAppAttr("type_name");
			if(pk_type == null || "".equals(pk_type)){
				throw new LfwRuntimeException("请选中类型后在新增");
			}
			
			row.setValue(ds.nameToIndex(QryOrgsVO.PK_TYPE), pk_type);
			  
			FormComp gc = (FormComp)AppUtil.getCntWindowCtx().getCurrentViewContext().getView().getViewComponents().getComponent( "qry_orgs_form");
			if("快报".equals(type_name)){
				gc.getElementById(QryOrgsVO.VMONTH).setEnabled(true);
			}else {
				gc.getElementById(QryOrgsVO.VMONTH).setEnabled(false);
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
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		ds.setEnabled(true);
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
		return "qry_orgs";
	}
	
	/**
	 * 数据保存前的校验检查
	 * @param masterDs
	 */
	 public void checkIfCanSave(Dataset masterDs){
		 ScapDAO dao = new ScapDAO();
		  String swh,msg = "";
		  String type_name = (String) AppUtil.getAppAttr("type_name");
		  String pk_type = (String) AppUtil.getAppAttr("pk_type");
		  String vyear= (String) masterDs.getValue(masterDs.nameToIndex(QryOrgsVO.VYEAR));
		  if(vyear == null){  throw new LfwRuntimeException("请选择年份"); }
		  swh = " nvl(dr,0) = 0 and vyear ='" + vyear + "' and pk_type ='" + pk_type + "' ";
		  msg = vyear + "年" ;
				  
		  String pk_qry_orgs = (String) masterDs.getValue(masterDs.nameToIndex(QryOrgsVO.PK_QRY_ORGS));
		  if(pk_qry_orgs != null){
			  swh = swh + " and pk_qry_orgs <> '"+ pk_qry_orgs +"' ";
		  }
		  
		  String vmonth = (String) masterDs.getValue(masterDs.nameToIndex(QryOrgsVO.VMONTH));
		  if("快报".equals(type_name)){
			  if(vmonth != null){
				  String sql = "select * from scap_qryorgs where " + swh; 
				  List<QryOrgsVO> li = (List<QryOrgsVO>) dao.executeQuery(sql, new BeanListProcessor(QryOrgsVO.class));
				  if(li == null || li.size() == 0){
					  throw new LfwRuntimeException("当前在维护快报数据，当选择某年某月数据时必须先维护当年（只有年没有月份）的数据。");
				  }
				  swh = swh + " and vmonth ='" + vmonth + "' ";
				  msg = msg + vmonth + "月";
			  }
		  }
          if(vmonth==null){
        	  swh = swh + " and vmonth is null ";  
          }
		  String sql = " select * from scap_qryorgs where " + swh ;
		  QryOrgsVO vo = (QryOrgsVO) ScapDAO.executeQuery(sql, new BeanProcessor(QryOrgsVO.class));
		  if(vo != null){
			  throw new LfwRuntimeException(msg + "数据已经存在！");   
		  }
	  }
}