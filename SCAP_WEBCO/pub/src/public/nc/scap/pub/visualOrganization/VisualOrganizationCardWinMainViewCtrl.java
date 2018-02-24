package nc.scap.pub.visualOrganization;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpCtrlUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.scapjj.pub.IConst4scapjj;
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
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.file.FillFileInfoHelper;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.plug.TranslatedRows;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.portal.log.ScapLogger;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scapco.pub_visualorganization.visualOrganization;
import nc.vo.scapco.pub_visualorganization.visualUserInfo;
import nc.vo.scapco.work_notice_contacts.notice_contact_type;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.core.locator.ServiceLocator;
/** 
 * 卡片窗口默认逻辑
 */
public class VisualOrganizationCardWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String PARAM_BILLITEM="billitem";
  private static final String PLUGOUT_ID="afterSavePlugout";
  /** 
 * 页面显示事件
 * @param dialogEvent
 */
  public void beforeShow(  DialogEvent dialogEvent){
    Dataset masterDs = this.getMasterDs();
		masterDs.clear();
		
		String oper = getOperator();
		if(AppConsts.OPE_ADD.equals(oper)){
			CmdInvoker.invoke(new UifAddCmd(getMasterDsId()) {
				protected void onBeforeRowAdd(Row row) {
					setAutoFillValue(row);
					
					Dataset masterDs = getMasterDs();
					String pk_primarykey = generatePk();
					String pk_visualOrganization = (String)uap.web.bd.pub.AppUtil.getAppAttr("pk_visualOrganization");
					if (pk_visualOrganization != null && StringUtils.isNotEmpty(pk_visualOrganization)) {
						// 上级组织
						row.setValue(masterDs.nameToIndex(visualOrganization.PK_PARENT),pk_visualOrganization);
						// 取得组织名字
						visualOrganization orgObject = (visualOrganization)ScapDAO.retrieveByPK(visualOrganization.class, pk_visualOrganization);
						// 上级组织
						row.setValue(masterDs.nameToIndex(visualOrganization.PARENT_NAME),orgObject.getVisualorg_name());
						//设置不可编辑字段
						CpCtrlUtil.setFormEditCol(masterDs, false, new String[] { "pk_parent_visualorg_name"}, "pub_visualOrganization_form");
					}
					// 设置启用状态
					row.setValue(masterDs
							.nameToIndex(notice_contact_type.ENABLESTATE),
							"2");
					// 设置填表单位
					row.setValue(masterDs
							.nameToIndex(notice_contact_type.REPORT_ORG),
							CntUtil.getCntOrgPk());
					// 设置填表人
					row.setValue(masterDs
							.nameToIndex(notice_contact_type.REPORT_PSN),
							CntUtil.getCntUserPk());
					// 设置填表人
					row.setValue(masterDs
							.nameToIndex(notice_contact_type.REPORT_DATE),
							new UFDate());
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
  }
  /** 
 * 主数据选中逻辑
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent dsEvent){
    Dataset ds = dsEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
  }
  /** 
 * 保存
 * @param datasetEvent
 */
  public void onSave(  MouseEvent<?> mouseEvent) throws BusinessException {
    Dataset masterDs = this.getMasterDs();
		CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this.getDetailDsIds(), false));
		masterDs.setEnabled(true);
		this.getCurrentAppCtx().closeWinDialog();
		
		CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(), PLUGOUT_ID));
  }
  /** 
 * 新增
 */
  public void onAdd(  MouseEvent<?> mouseEvent) throws BusinessException {
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
  public void onCopy(  MouseEvent<?> mouseEvent){
    CmdInvoker.invoke(new UifCopyCmd(this.getMasterDsId()));
  }
  /** 
 * 删除
 */
  public void onDelete(  MouseEvent<?> mouseEvent) throws BusinessException {
    CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
  }
  /** 
 * 返回
 */
  public void onBack(  MouseEvent<?> mouseEvent) throws BusinessException {
    this.getCurrentAppCtx().closeWinDialog();
  }
  /** 
 * 启用
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onStart(  MouseEvent<?> mouseEvent) throws BusinessException {
    CmdInvoker.invoke(new UifEnableCmd(this.getMasterDsId(), true));
  }
  /** 
 * 停用
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onStop(  MouseEvent<?> mouseEvent) throws BusinessException {
    CmdInvoker.invoke(new UifEnableCmd(this.getMasterDsId(), false));
  }
  /** 
 * 附件
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onAttchFile(  MouseEvent<?> mouseEvent) throws BusinessException {
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
  public void onPrint(  MouseEvent<?> mouseEvent) throws BusinessException {
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
  private void setAutoFillValue(  Row row){
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
  public void onGridAddClick(  MouseEvent<?> mouseEvent){
    OpenProperties props = new OpenProperties(
					IConst4scapjj.PUBLIC_VIEW_SELECTUSERBYORG, "人员选择", "800",
					"600");
			props.setButtonZone(false);
			this.getCurrentAppCtx().getCurrentWindowContext().popView(props);
//		GridComp grid = (GridComp) mouseEvent.getSource();
//		String dsId = grid.getDataset();
//		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
//		Row emptyRow = ds.getEmptyRow();
//		ds.addRow(emptyRow);
//		ds.setRowSelectIndex(ds.getRowIndex(emptyRow));
//		ds.setEnabled(true);
  }
  private Row[] copyTranslateRows2Row(  TranslatedRows translatedRows,  Dataset ds){
    String[] rowKeyStrings = translatedRows.getKeys();
				Row[] rows = new Row[translatedRows.getValue(rowKeyStrings[0]).size()];

				for (int j = 0; j < rows.length; j++) {
					rows[j] = ds.getEmptyRow();
					for (int i = 0; i < rowKeyStrings.length; i++) {
						String rowKeyString = rowKeyStrings[i];
						String targetkey = rowKeyString;
						if (targetkey.equals("cuserid")) // 用户主键
							targetkey = "pk_user";
						if (targetkey.equals("user_name"))// 联系人姓名
							targetkey = "user_name";
						if (targetkey.equals("pk_org")) // 所属企业
							targetkey = "pk_org";
						if (targetkey.equals("zw")) // 职务
							targetkey = "post";
						if (targetkey.equals("mobile")) // 电话
							targetkey = "phone_no";
						if (targetkey.equals("email")) // email
							targetkey = "email";
						int colIndex = ds.nameToIndex(targetkey);
						if (colIndex != -1) {
							rows[j].setValue(colIndex,
									translatedRows.getValue(rowKeyString).get(j));
						}


					}
				}
				return rows;
  }
  public void doSelectUser(  Map keys){
    ScapLogger.error("==================doselect=====start "+new UFDateTime());
		    TranslatedRows selRows = (TranslatedRows) keys.get(OPERATE_ROW);
				if (selRows != null) {
					Dataset ds = getCurrentView().getViewModels().getDataset(
							"visualUserInfo");
					Row[] savedRows = copyTranslateRows2Row(selRows, ds);
					// 需要把选择的人回写到相应的人员信息表中（去重）
					Map<String, String> mapExistedUser = isUserExisted();
					for (Row row : savedRows) {
						String pk_user = (String)row.getValue(ds.nameToIndex(visualUserInfo.PK_USER));
						if (mapExistedUser.containsKey(pk_user)) {
							continue;
						} else {
							ds.addRow(row);	
						}				
					}
					ds.setEnabled(true);
				}
  }
  private Map<String,String> isUserExisted(){
    LfwView view = AppLifeCycleContext.current().getWindowContext()
								.getViewContext("main").getView();
						Dataset visualUserInfods = view.getViewModels().getDataset(
								"visualUserInfo");
						// 人员信息子表去重处理
						Map<String, String> mapExisted = new HashMap<String, String>();
						Row[] rowExisted = visualUserInfods.getAllRow();
						for (int i = 0; i < rowExisted.length; i++) {
							String pkUser = (String) rowExisted[i].getValue(visualUserInfods
									.nameToIndex(visualUserInfo.PK_USER));
							if (mapExisted.containsKey(pkUser)) {
								continue;
							} else {
								mapExisted.put(pkUser, "1");
							}
						}
						return mapExisted;
  }
  /** 
 * 子表编辑
 */
  public void onGridEditClick(  MouseEvent<?> mouseEvent){
    
  }
  /** 
 * 子表删除
 */
  public void onGridDeleteClick(  MouseEvent<?> mouseEvent){
    GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		CmdInvoker.invoke(new UifLineDelCmd(dsId));
  }
  @Override protected String getMasterDsId(){
    return "pub_visualOrganization";
  }
}
