package nc.scap.transfer.dataimp;
import nc.uap.lfw.core.cmd.CmdInvoker;
import java.util.Map;
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.pt.vos.ScapptTransferHVO;
import java.util.HashMap;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.scap.pub.util.ScapPAPPubmethod;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.data.Dataset;
import java.util.ArrayList;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.model.plug.TranslatedRow;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.file.LfwFileConstants;
import java.util.List;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.vo.pub.BusinessException;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.pub.lang.UFBoolean;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.lfw.core.constants.AppConsts;
import uap.web.bd.pub.AppUtil;
import nc.uap.portal.log.ScapLogger;
import nc.uap.lfw.core.event.DatasetEvent;
import uap.lfw.core.locator.ServiceLocator;
import nc.uap.lfw.core.event.MouseEvent;
/** 
 * 信息列表默认逻辑
 */
public class DataimpListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="dataimpComps.dataimp_cardwin";
  private static final String CARD_WIN_TITLE="编辑";
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
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

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
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待删除数据");
		}

		Row row = ds.getSelectedRow();
		String ismegge = (String) row.getValue(ds
				.nameToIndex(ScapptTransferHVO.ISMERGE));
		if (ismegge.equals("Y")) {
			AppInteractionUtil.showShortMessage("此数据存在原始数据,不能删除!");
			return;
		}
		CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
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
		uap.web.bd.pub.AppUtil.addAppAttr(LfwFileConstants.SYSID,
				LfwFileConstants.SYSID_BAFILE);
		String primaryKey = ds.getPrimaryKeyField();
		if (primaryKey == null) {
			throw new LfwRuntimeException("当前Dataset没有设置主键!");
		}
		String primaryKeyValue = (String) row.getValue(ds
				.nameToIndex(primaryKey));

		String taskPk = WfmUtilFacade.getTaskPkPkByPkFormIns(primaryKeyValue);
		// 如果当前人不是当前单据的参与者,此时取不到任务.附件只能查看,需要设为浏览态.
		if (taskPk == null || taskPk.equals("")) {
			this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE,
					WfmConstants.BILLSTATE_BROWSE);
		}

		// 流程附件参数
		Map<String, String> wfmParam = WfmUtilFacade
				.getFileMgrParamsByTask(taskPk);

		// 附件参数
		Map<String, String> param = UploadFileHelper.BuildDefaultPamater("",
				primaryKeyValue, CommonObjectConstants.AttachFileType, "");
		param.put("usescanable", "true");
		param.put("state", String.valueOf(31));

		String title = "附件";
		if (wfmParam != null && !wfmParam.isEmpty()) {
			param.putAll(wfmParam);
		}

		CmdInvoker.invoke(new UifAttachCmd(title, param));
  }
  /** 
 * 打印
 */
  public void onPrint(  MouseEvent mouseEvent){
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
 * 外部触发刷新
 * @param keys
 */
  public void doRefresh(  Map<?,?> keys){
    TranslatedRow selRow = (TranslatedRow) keys.get(OPERATE_ROW);
		if (selRow != null) {
			Dataset ds = this.getMasterDs();
			String sign = this.getOperator();
			if (AppConsts.OPE_EDIT.equals(sign)) {
				Row savedRow = ds.getSelectedRow();
				copyTranslateRow2Row(selRow, savedRow, ds);
				// }
			} else if (AppConsts.OPE_ADD.equals(sign)) {
				Row savedRow = ds.getEmptyRow();
				savedRow = copyTranslateRow2Row(selRow, savedRow, ds);
				ds.addRow(savedRow);
			}
		}
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
  /** 
 * 主组织变化
 * @param keys
 */
  public void doOrgChange(  Map<?,?> keys){
    // CmdInvoker.invoke(new UifDatasetLoadCmd(this.getMasterDsId()) {
		// protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
		// String where = AppUtil.getGroupOrgFielterSqlForDesign(false,
		// "pk_org");
		// ds.setLastCondition(where);
		// return where;
		// }
		//
		// // 避免翻页时重走缓存
		// protected void changeCurrentKey() {
		// setChangeCurrentKey(true);
		// }
		// });
		// this.clearDetailDs();
  }
  /** 
 * 查询plugin
 * @param keys
 */
  public void doQueryChange(  Map<?,?> keys){
    FromWhereSQL whereSql = (FromWhereSQL) keys
				.get(FromWhereSQL.WHERE_SQL_CONST);
		CmdInvoker.invoke(new QueryCmd(MAIN_VIEW_ID, this.getMasterDsId(),
				whereSql.getWhere()));
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
    return "scappt_transfer_h";
  }
  /** 
 * 数据导入
 * @param scriptEvent
 */
  public void onImport(  MouseEvent mouseEvent){
    OpenProperties props = new OpenProperties("selecttrs", "机构库");
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		props.setParamMap(paramMap);
		props.setWidth("500");
		props.setHeight("240");
		AppLifeCycleContext.current().getWindowContext().popView(props);
		// UifExcelImportCmd cmd = new UifExcelImportCmd(
		// "nc.scap.transfer.upload.CallBackLoad",
		// "dataimpComps.dataimp_listwin");
		// CmdInvoker.invoke(cmd);
  }
  /** 
 * 信息合并
 * @param scriptEvent
 */
  public void onInfomegr(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待合并数据");
		}
		Row row = ds.getSelectedRow();
		String ismerge = (String) row.getValue(ds
				.nameToIndex(ScapptTransferHVO.ISMERGE));
		if (ismerge!=null&&ismerge.equals("Y")) {
			AppInteractionUtil.showShortMessage("此数据存在原始数据无需合并!");
			return;
		}
		ScapptTransferHVO[] tmpvo = new Dataset2SuperVOSerializer<ScapptTransferHVO>()
				.serialize(ds, row);
		AppUtil.addAppAttr("mergeinfo", tmpvo[0]);// 需合并信息
		OpenProperties props = new OpenProperties("merge", "信息合并界面");
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		props.setParamMap(paramMap);
		props.setWidth("500");
		props.setHeight("240");
		AppLifeCycleContext.current().getWindowContext().popView(props);
  }
  /** 
 * 查看
 * @param scriptEvent
 */
  public void onLook(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待查看数据");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(CARD_WIN_ID, "查看");
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, "look");
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * 查看
 * @param scriptEvent
 */
  public void onBilllook(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待查看数据");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(
				"transferComps.trs_look_cardwin", "查看");
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, "look");
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  public String getWheresql(){
    String pk_Org = AppLifeCycleContext.current().getApplicationContext()
				.getAppEnvironment().getPk_org();
//		String where = "  1=1 ";
		String where = " isleadin='Y' and 1=1 ";
		if(pk_Org==null){
			AppInteractionUtil.showShortMessage("当前组织未关联职责！");
			return where;
		}
		if (CpOrgUtil.isCompanyOrg(pk_Org)) {
			where += " and pk_rorg in('"
					+ ScapPAPPubmethod.getFreeTreePkByUser() + "') and dr=0 ";
		}
		ScapLogger.error(where);
		return where;
  }
  /** 
 * @param dataLoadEvent
 */
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
		ds.setLastCondition(getWheresql());
		ds.setExtendAttribute(Dataset.ORDER_PART,
				"order by vrequestno desc,pk_borg desc,ts desc ");
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
  public void onAfterRowSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
  }
}
