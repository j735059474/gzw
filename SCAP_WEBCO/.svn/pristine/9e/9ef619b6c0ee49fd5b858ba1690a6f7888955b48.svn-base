package nc.scap.dsm.dtp.ctrl;
import nc.vo.scapjj.dsm.Datetype_HVO;
import nc.uap.lfw.core.cmd.UifAddCmd;
import nc.vo.pub.lang.UFDateTime;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import nc.uap.lfw.core.cmd.UifLineDelCmd;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.scap.jjpub.util.JjUtil;
import nc.uap.lfw.core.cmd.UifSaveCmdRV;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.model.plug.TranslatedRow;
import java.util.List;
import nc.scap.scapjj.pub.IConst4scapjj;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.file.FillFileInfoHelper;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.cpb.baseservice.IUifCpbService;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.cpb.log.CpLogger;
import java.util.Arrays;
import nc.uap.lfw.core.cmd.CmdInvoker;
import java.util.Map;
import nc.scap.jjpub.checkutil.CheckUnion;
import nc.uap.lfw.core.cmd.UifCopyCmd;
import java.util.HashMap;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.Dataset;
import java.util.ArrayList;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.vo.pub.SuperVO;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.vo.pub.BusinessException;
import nc.bs.logging.Logger;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.constants.AppConsts;
import uap.web.bd.pub.AppUtil;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.model.plug.TranslatedRows;
import uap.lfw.core.locator.ServiceLocator;
import nc.uap.lfw.core.event.MouseEvent;
/** 
 * 卡片窗口默认逻辑
 */
public class DtpCardWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
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
		clearAllRow(POWERROLE);
	
		String oper = getOperator();
		if (AppConsts.OPE_ADD.equals(oper)) {
			CmdInvoker.invoke(new UifAddCmd(getMasterDsId()) {
				protected void onBeforeRowAdd(Row row) {
					setAutoFillValue(row);

					Dataset masterDs = getMasterDs();
					String pk_primarykey = generatePk();
					row.setValue(
							masterDs.nameToIndex(masterDs.getPrimaryKeyField()),
							pk_primarykey);
					FillFileInfoHelper.resetItem(pk_primarykey);
					FillFileInfoHelper.fillFileInfo(masterDs, row);
				}
			});
		} else if (AppConsts.OPE_EDIT.equals(oper)) {
			CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs) {
				@Override
				protected void onAfterDatasetLoad() {
					this.getDs().setEnabled(true);

					String primaryKey = this.getDs().getPrimaryKeyField();
					if (primaryKey == null) {
						throw new LfwRuntimeException("当前Dataset没有设置主键!");
					}
					String primaryKeyValue = (String) this.getDs()
							.getSelectedRow()
							.getValue(this.getDs().nameToIndex(primaryKey));
					FillFileInfoHelper.resetItem(primaryKeyValue);
				}
			});
		} else if ("SCAN".equals(oper)) {
			CpLogger.debug("click button scan!");
			CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs) {
				@Override
				protected void onAfterDatasetLoad() {
					this.getDs().setEnabled(false);

					String primaryKey = this.getDs().getPrimaryKeyField();
					if (primaryKey == null) {
						throw new LfwRuntimeException("当前Dataset没有设置主键!");
					}
					String primaryKeyValue = (String) this.getDs()
							.getSelectedRow()
							.getValue(this.getDs().nameToIndex(primaryKey));
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
    Dataset poweruser = this.getCurrentView().getViewModels().getDataset("poweruser");
    poweruser.setOrderByPart("order by qxlx asc, def1 asc");
    Dataset powerrole = this.getCurrentView().getViewModels().getDataset("powerrole");
    powerrole.setOrderByPart("order by qxlx asc");
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
  }
  /** 
 * 保存
 * @param datasetEvent
 */
  public void onSave(  MouseEvent<?> mouseEvent) throws BusinessException {
    Dataset ds = this.getMasterDs();
		
//		String[] fields = new String[] { "pk_org", "code" };
//		CheckUnion cu = new CheckUnion();
//		cu.execute_selectRow(ds,fields, " enablestate = 2 ", null);
//		String code = (String) ds.getValue(ds.nameToIndex(Datetype_HVO.CODE));
		
		if(ds.getSelectedRow().getState()==Row.STATE_UPDATE)
			JjUtil.setDSvalue(ds, ds.getSelectedRow(), "xgdw",AppUtil.getPk_org());

//		CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this.getDetailDsIds(), false));
//		ds.setEnabled(true);
		
//		Dataset user_ds = getCurrentView().getViewModels().getDataset("poweruser");
//		Dataset role_ds = getCurrentView().getViewModels().getDataset("powerrole");
//		cu.execute_allRow(user_ds, new String[] { "qxlx", "yh","pk_datetype_h" }," sfqy = 'Y' ", null);
//		cu.execute_allRow(role_ds, new String[] { "qxlx", "jsmx","pk_datetype_h" }," sfqy = 'Y' ", null);
//		
		CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this.getDetailDsIds(), false){
			protected void onVoSave(SuperVO aggVo) {
				checkData(aggVo);// 获取错误信息
				IUifCpbService cpbService = NCLocator.getInstance().lookup(
						IUifCpbService.class);
				try {
					cpbService.insertOrUpdateSuperVO(aggVo,
							this.isNotifyBDCache());
				} catch (CpbBusinessException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}

			};
		});
		
		AppInteractionUtil.showShortMessage("保存成功!");
		
		// 设置页面改变状态
                AppLifeCycleContext.current().getViewContext().getView()
                        .getWindow().setHasChanged(false);

                AppLifeCycleContext
                        .current()
                        .getApplicationContext()
                        .getCurrentWindowContext()
                        .closeView(
                                AppLifeCycleContext.current().getViewContext()
                                        .getId());

                this.getCurrentAppCtx().closeWinDialog();

		CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(),PLUGOUT_ID));
		clearAllRow(POWERROLE);
  }
  public void checkData(  SuperVO aggVo){
    // TODO:数据检验处理
	        Datetype_HVO headvo = (Datetype_HVO) aggVo;
	        String code  =  headvo.getCode();
	        String wheresql = " 1=1 and dr=0 and code='"+code+"'";
	        Datetype_HVO[] dhvos = JjUtil.getDataTypeVoByCondition(wheresql);
	        if (dhvos==null)
	        	return ;
	        if(!dhvos[0].getPk_datetype_h().equals(headvo.getPk_datetype_h()))
	        	throw new LfwRuntimeException("【编码】"+headvo.getCode()+" 已经存在，请重新输入").setTitle("提示");/*编码已经存在，请重新输入*/
	        if(headvo.getEnablestate().equals("N")){
	        	String subsql = " 1=1 and dr=0 and pk_father='"+headvo.getPk_datetype_h()+"'";
	        	Datetype_HVO[] subvos = JjUtil.getDataTypeVoByCondition(subsql);
	        	if(subvos==null)
	        		return;
	        	for (int i=0;i<subvos.length;i++){
	        		if(subvos[i].getEnablestate().equals("Y")){
	        			throw new LfwRuntimeException("<"+headvo.getName()+">  的下级资料类型<" +subvos[i].getName() + "> 中【是否启用】字段为‘是’不允许本级资料类型改为‘否’;如想改为‘否’,请先把下级【是否启用】字段改为‘否’").setTitle("提示");/*编码已经存在，请重新输入*/
	        		}
	        			
	        	}
	        }
	        CheckUnion cu = new CheckUnion();
	        Dataset user_ds = getCurrentView().getViewModels().getDataset("poweruser");
			Dataset role_ds = getCurrentView().getViewModels().getDataset("powerrole");
			cu.execute_allRow(user_ds, new String[] { "qxlx", "yh","pk_datetype_h" }," sfqy = 'Y' ", null);
			cu.execute_allRow(role_ds, new String[] { "qxlx", "jsmx","pk_datetype_h" }," sfqy = 'Y' ", null);
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
		if (primaryKey == null) {
			throw new LfwRuntimeException("当前Dataset没有设置主键!");
		}
		String primaryKeyValue = (String) row.getValue(ds
				.nameToIndex(primaryKey));
		Map<String, String> paramMap = null;
		if (primaryKeyValue != null) {
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
		try {
			List<Dataset> list = new ArrayList<Dataset>(1);
			list.add(ds);
			DefaultPrintService printService = new DefaultPrintService();
			printService.setDatasetList(list);
			ICpPrintTemplateService service = ServiceLocator
					.getService(ICpPrintTemplateService.class);
			service.print(printService);
		} catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
  }
  /** 
 * 设置PK_ORG字段值
 * @param row
 */
  private void setAutoFillValue(  Row row){
    if (row != null) {
			Dataset ds = this.getCurrentView().getViewModels()
					.getDataset(this.getMasterDsId());

			String pkOrg = this.getCurrentAppCtx().getAppEnvironment()
					.getPk_org();
			if (pkOrg != null) {
				int pkOrgIndex = ds.nameToIndex("pk_org");
				if (pkOrgIndex >= 0) {
					row.setValue(pkOrgIndex, pkOrg);
				}
			}
			String pkGroup = this.getCurrentAppCtx().getAppEnvironment()
					.getPk_group();
			if (pkGroup != null) {
				int pkGroupIndex = ds.nameToIndex(PK_GROUP);
				if (pkGroupIndex >= 0) {
					row.setValue(pkGroupIndex, pkGroup);
				}
			}

			String creator = this.getCurrentAppCtx().getAppEnvironment()
					.getLoginUser();
			if (creator != null) {
				int creator_index = ds.nameToIndex("creator");
				if (creator_index >= 0) {
					row.setValue(creator_index, creator);
				}
			}

			UFDateTime ts = new UFDateTime(System.currentTimeMillis());
			int creationtime_index = ds.nameToIndex("creationtime");
			if (creationtime_index >= 0) {
				row.setValue(creationtime_index, ts.toString());
			}
			int enablestate_index = ds.nameToIndex("enablestate");
			if (enablestate_index >= 0)
				row.setValue(enablestate_index, "Y");
			String pk_father=(String) AppUtil.getAppAttr("pk_father");
			int pk_father_index = ds.nameToIndex("pk_father");
			if (pk_father_index >= 0)
				row.setValue(pk_father_index, pk_father);

			
			
			
			
		}
  }
  /** 
 * 子表新增
 */
  public void onGridAddClick(  MouseEvent<?> mouseEvent){
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
  public void onGridEditClick(  MouseEvent<?> mouseEvent){
    GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		ds.setEnabled(true);
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
    return "datetype_h";
  }
  public void onRep(  MouseEvent mouseEvent){
    OpenProperties props = new OpenProperties(
				IConst4scapjj.PUBLIC_VIEW_SELECTUSERBYORG, "新增用户维护权限", "800",
				"600");
		props.setButtonZone(false);
		AppUtil.addAppAttr(IConst4scapjj.QXLX, IConst4scapjj.QXLX_REP);
		this.getCurrentAppCtx().getCurrentWindowContext().popView(props);
  }
  public void onBro(  MouseEvent mouseEvent){
    OpenProperties props = new OpenProperties(
				IConst4scapjj.PUBLIC_VIEW_SELECTUSERBYORG, "新增用户浏览权限", "800",
				"600");
		props.setButtonZone(false);
		AppUtil.addAppAttr(IConst4scapjj.QXLX, IConst4scapjj.QXLX_BRO);
		this.getCurrentAppCtx().getCurrentWindowContext().popView(props);
  }
  public void onMan(  MouseEvent mouseEvent){
    OpenProperties props = new OpenProperties(
				IConst4scapjj.PUBLIC_VIEW_SELECTUSERBYORG, "新增用户管理权限", "800",
				"600");
		props.setButtonZone(false);
		AppUtil.addAppAttr(IConst4scapjj.QXLX, IConst4scapjj.QXLX_MAN);
		this.getCurrentAppCtx().getCurrentWindowContext().popView(props);
  }
  private Row copyTranslateRow2Row(  TranslatedRow translatedRow,  Row row,  Dataset ds){
    String[] rowKeyStrings = translatedRow.getKeys();
		for (int i = 0; i < rowKeyStrings.length; i++) {
			String rowKeyString = rowKeyStrings[i];
			int colIndex = ds.nameToIndex(rowKeyString);
			if (colIndex != -1)
				row.setValue(colIndex, translatedRow.getValue(rowKeyString));
		}
		return row;
  }
  private Row[] copyTranslateRows2Row(  TranslatedRows translatedRows,  Dataset ds){
    String[] rowKeyStrings = translatedRows.getKeys();
		Row[] rows = new Row[translatedRows.getValue(rowKeyStrings[0]).size()];
		for (int j = 0; j < rows.length; j++) {
			rows[j] = ds.getEmptyRow();
			for (int i = 0; i < rowKeyStrings.length; i++) {
				String rowKeyString = rowKeyStrings[i];
				String targetkey = rowKeyString;
				if (targetkey.equals("cuserid"))
					targetkey = "yh";
				if (targetkey.equals("pk_role"))
					targetkey = "jsmx";
				if (targetkey.equals("user_code"))
                                    targetkey = "def1";
				int colIndex = ds.nameToIndex(targetkey);
				if (colIndex != -1)
					rows[j].setValue(colIndex,
							translatedRows.getValue(rowKeyString).get(j));
			}
		}
		return rows;
  }
  public void doSelectUser(  Map keys){
    Object QXLX = AppUtil.getAppAttr(IConst4scapjj.QXLX);
		TranslatedRows selRows = (TranslatedRows) keys.get(OPERATE_ROW);
		if (selRows != null) {
			Dataset ds = getCurrentView().getViewModels().getDataset(
					"poweruser");
			;
			Row[] savedRows = copyTranslateRows2Row(selRows, ds);
			// 去除重复数据
			Row[] allrow = ds.getAllRow();
			List<Row> list = new ArrayList<Row>();
			if (!(allrow == null || allrow.length == 0)) {
				for (Row row : savedRows) {
					String yh = row.getString(ds.nameToIndex("yh"));
					boolean addflag=true;
					for (int i = 0; i < allrow.length; i++) {
						Row row2 = allrow[i];
						if(row2 == null)
							continue;
						String yh2 = row2.getString(ds.nameToIndex("yh"));
						String qxlx2 = row2.getString(ds.nameToIndex("qxlx"));
						if ((yh2.equals(yh) && qxlx2.equals(QXLX))) {
							addflag=false;
							
						}
					}
					if(addflag)
						list.add(row);

				}
				savedRows = list.toArray(new Row[list.size()]);// 去除重复后的数据
			}

			for (Row row : savedRows) {
				row.setValue(ds.nameToIndex("qxlx"), QXLX);
				row.setValue(ds.nameToIndex("sfqy"), "Y");
				ds.addRow(row);
			}

			ds.setEnabled(true);
		}
  }
  private Row[] getAllRow(  Dataset ds,  String key){
    if(AppUtil.getAppAttr(key)!=null){
			return (Row[]) AppUtil.getAppAttr(key);
		}else{
			return ds.getAllRow();
		}
  }
  private void putAllRow(  String key,  Row[] rows){
    AppUtil.addAppAttr(key,rows);
  }
  private void clearAllRow(  String key){
    AppUtil.addAppAttr(key, null);
  }
  static String POWERROLE="powerrole";
  public void pluginselectRoleAfterplugin(  Map keys){
    getCurrentAppCtx().closeWinDialog();
		Object QXLX = AppUtil.getAppAttr(IConst4scapjj.QXLX);
		TranslatedRows selRows = (TranslatedRows) keys.get("ok");
		if (selRows != null) {
			Dataset ds = getCurrentView().getViewModels().getDataset("powerrole");
			
			Row[] savedRows = copyTranslateRows2Row(selRows, ds);
			// 去除重复数据
			Row[] allrow =getAllRow(ds, POWERROLE);  //ds.getAllRow();
			
			ds.getCurrentRowData().getRows();
			List<Row> list = new ArrayList<Row>();
			if (!(allrow == null || allrow.length == 0)) {
				for (Row row : savedRows) {
					String jsmx = row.getString(ds.nameToIndex("jsmx"));
					boolean addflag=true;
					for (int i = 0; i < allrow.length; i++) {
						Row row2 = allrow[i];
						if(row2==null)
							continue;
						String jsmx2 = row2.getString(ds.nameToIndex("jsmx"));
						String qxlx2 = row2.getString(ds.nameToIndex("qxlx"));
						if ((jsmx2.equals(jsmx) && qxlx2.equals(QXLX))) {
							addflag=false;
						}
					}
					if(addflag)
						list.add(row);

				}
				savedRows = list.toArray(new Row[list.size()]);// 去除重复后的数据
			}
			
			for (Row row : savedRows) {
				row.setValue(ds.nameToIndex("qxlx"), QXLX);
				row.setValue(ds.nameToIndex("sfqy"), "Y");
				ds.addRow(row);
//				ds.getAllRow();
			}
			ds.setEnabled(true);
			
			ArrayList<Row> putrows=new ArrayList<Row>();
			for (Row row : savedRows) {
				putrows.add(row);
			}
			for (Row row : allrow) {
				putrows.add(row);
			}
			
			putAllRow(POWERROLE, putrows.toArray(new Row[putrows.size()]));
			
			
		}
  }
  public void onManRoles(  MouseEvent mouseEvent){
    OpenProperties props = new OpenProperties(IConst4scapjj.PUBLIC_ROLE,
				"新增角色管理权限", "800", "600");
		props.setButtonZone(false);
		AppUtil.addAppAttr(IConst4scapjj.QXLX, IConst4scapjj.QXLX_MAN);
		this.getCurrentAppCtx().getCurrentWindowContext().popView(props);
  }
  public void onRepRoles(  MouseEvent mouseEvent){
    OpenProperties props = new OpenProperties(IConst4scapjj.PUBLIC_ROLE,
				"新增角色维护权限", "800", "600");
		props.setButtonZone(false);
		AppUtil.addAppAttr(IConst4scapjj.QXLX, IConst4scapjj.QXLX_REP);
		this.getCurrentAppCtx().getCurrentWindowContext().popView(props);
  }
  public void onBroRoles(  MouseEvent mouseEvent){
    OpenProperties props = new OpenProperties(IConst4scapjj.PUBLIC_ROLE,
				"新增角色浏览权限", "800", "600");
		props.setButtonZone(false);
		AppUtil.addAppAttr(IConst4scapjj.QXLX, IConst4scapjj.QXLX_BRO);
		this.getCurrentAppCtx().getCurrentWindowContext().popView(props);
  }
  public void onEditUser(  MouseEvent mouseEvent){
    String dsId = "poweruser";
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		ds.setEnabled(true);
  }
  public void onDelUser(  MouseEvent mouseEvent){
    String dsId = "poweruser";
		CmdInvoker.invoke(new UifLineDelCmd(dsId));
  }
  public void onEditRole(  MouseEvent mouseEvent){
    String dsId = "powerrole";
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		ds.setEnabled(true);
  }
  public void onDelRole(  MouseEvent mouseEvent){
    String dsId = "powerrole";
		CmdInvoker.invoke(new UifLineDelCmd(dsId));
  }
}
