package nc.scap.pub.entmatter;
import nc.uap.lfw.core.comp.MenuItem;
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import java.util.List;
import nc.uap.lfw.core.log.LfwLogger;
import nc.scap.pub.util.ScapDAO;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.wfm.vo.WfmTaskVO;
import java.util.UUID;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.cmd.CmdInvoker;
import java.util.Map;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import java.util.HashMap;
import nc.uap.lfw.core.page.LfwView;
import java.util.regex.Pattern;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import nc.scap.pub.entmatter.vos.EntmattersVO;
import nc.uap.wfm.utils.WfmCPUtilFacade;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.data.Dataset;
import java.util.ArrayList;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.vo.pub.SuperVO;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.vo.pub.BusinessException;
import nc.scap.pub.entmatter.util.MatterUtil;
import java.util.regex.Matcher;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.lfw.core.constants.AppConsts;
import uap.web.bd.pub.AppUtil;
import nc.scap.pub.entmatter.util.ListPageQueryCmd;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.scap.pub.util.CpWinUtil;
import uap.lfw.core.locator.ServiceLocator;
import nc.uap.lfw.core.event.MouseEvent;
/** 
 * 信息列表默认逻辑
 */
public class EntmatterListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final String CARD_WIN_ID="entmatterComps.entmatter_cardwin";
  private static final String CARD_WIN_TITLE="编辑";
  /** 
 * 主数据加载逻辑
 * @param dataLoadEvent
 */
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
    String pagecode = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("pagecode");
		AppUtil.getAppAttr("pagecode");
		AppUtil.addAppAttr("pagecode", pagecode);
		String report_user = MatterUtil.getAddressParameter("report_user");
		AppUtil.addAppAttr("report_user", report_user);
		Dataset ds = dataLoadEvent.getSource();
		CmdInvoker.invoke(new ListPageQueryCmd(ds, this.getFlwTypePk()));
  }
  /** 
 * 主数据选中逻辑
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
		Row row = ds.getSelectedRow();
		LfwView main = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		MenubarComp mc = getViewMenubarComp(main, "menubar");
		String releasestate = (String) row.getValue(ds.nameToIndex("def2"));
		if ("0".equals(releasestate)) {// 未发布
			setItemStateEnabled(mc, "release", true);
			setItemStateEnabled(mc, "unrelease", false);
		} else if ("1".equals(releasestate)) {// 已发布
			setItemStateEnabled(mc, "release", false);
			setItemStateEnabled(mc, "unrelease", true);
		}
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));

		// 刷新附件
		String pk = (String) ds.getValue(ds.getPrimaryKeyField());
		CpWinUtil.refshAttachList("main", "plugout_winpopattchlist", pk, false,
				true, false);
  }
  /** 
 * 设置指定菜单的按钮是否可用，
 * @param mb
 * @param itemid
 * @param flag
 */
  public static void setItemStateEnabled(  MenubarComp mb,  String itemid,  boolean flag){
    if (mb == null)
			return;
		MenuItem it1 = mb.getItem(itemid);
		if (it1 != null)
			it1.setEnabled(flag);
  }
  /** 
 * 获取视图的指定菜单
 * @param view
 * @param menuID
 * @return
 */
  public static MenubarComp getViewMenubarComp(  LfwView view,  String menuID){
    MenubarComp mc = null;
		if (view != null) {
			mc = view.getViewMenus().getMenuBar(menuID);
		}
		return mc;
  }
  /** 
 * 外部触发刷新
 * @param keys
 */
  public void doRefresh(  Map<?,?> keys){
    Row savedRow = (Row) keys.get(OPERATE_ROW);
		if (savedRow != null) {
			Dataset ds = getMasterDs();
			savedRow.setRowId(ds.getEmptyRow().getRowId());
			// 标识是否编辑
			boolean isEdit = false;
			// 当前页所有选中行
			Row[] selRows = ds.getSelectedRows();
			int len = selRows != null ? selRows.length : 0;
			if (len > 0) {
				int pkIndex = ds.nameToIndex(ds.getPrimaryKeyField());
				for (int i = 0; i < len; i++) {
					if (selRows[i] == null
							|| selRows[i].getValue(pkIndex) == null) {
						continue;
					} // PK值相同,父页面更新数据.
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
		} else {
			Dataset ds = getMasterDs();
			LfwRuntimeEnvironment.getWebContext().getWebSession()
			.addOriginalParameter("openBillId", null);
			ds.setCurrentKey(Dataset.MASTER_KEY + UUID.randomUUID());
			CmdInvoker.invoke(new ListPageQueryCmd(ds, this.getFlwTypePk()));
		}
  }
  /** 
 * 新增
 * @param mouseEvent
 */
  public void onAdd(  MouseEvent<?> mouseEvent){
    this.onAdd_wfm();

		String func_type = MatterUtil.getMatterFuncType();
		String report_user = MatterUtil.getAddressParameter("report_user");
		OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_ADD);
		paramMap.put("model", "nc.scap.pub.entmatter.util.CardWinPageModel");
		paramMap.put("pro_action", "add");
		paramMap.put("func_type", func_type);
		paramMap.put("report_user", report_user);
		props.setParamMap(paramMap);
		getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * 新增-流程业务
 */
  private void onAdd_wfm(){
    getCurrentAppCtx()
		.addAppAttribute(WfmConstants.WfmAppAttr_BillID, null);
		this.resetWfmParameter();
		getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_FolwTypePk,
				this.getFlwTypePk());
  }
  /** 
 * 打开页面与当前页面属于同一app范围，每次需要清除app中的变量
 */
  private void resetWfmParameter(){
    getCurrentAppCtx()
		.addAppAttribute(WfmConstants.WfmAppAttr_TaskPk, null);
		getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_ScratchPad,
				null);
		getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE, null);
		getCurrentAppCtx().addAppAttribute(
				WfmConstants.AttachFileList_Temp_Billitem, null);
		getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_ProInsPk,
				null);
		getCurrentAppCtx().addAppAttribute(WfmConstants.RETURN_PK_TASK, null);
		getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_FormInFoCtx_Billitem, null);
  }
  /** 
 * 获得单据类型PK
 * @return
 */
  private String getFlwTypePk(){
    return "0001ZZ100000000JE3Y1";
  }
  /** 
 * 编辑
 * @param scriptEvent
 */
  public void onEdit(  MouseEvent<?> mouseEvent){
    Dataset ds = getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待编辑数据");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		String func_type = MatterUtil.getMatterFuncType();
		String report_user = MatterUtil.getAddressParameter("report_user");

		OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		paramMap.put("model", "nc.scap.pub.entmatter.util.CardWinPageModel");
		paramMap.put("pro_action", "edit");
		paramMap.put("func_type", func_type);
		paramMap.put("report_user", report_user);
		props.setParamMap(paramMap);

		this.onEdit_wfm(pkValue);

		getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * 编辑-流程业务
 * @param pkValue
 */
  private void onEdit_wfm(  String pkValue){
    this.resetWfmParameter();
		try {
			String pk_user = LfwRuntimeEnvironment.getLfwSessionBean()
					.getPk_user();
			WfmTaskVO task = MatterUtil.getLasterTaskByBillIdAndUser(pkValue,
					pk_user, "approve");
			if (task != null) {
				getCurrentAppCtx().addAppAttribute(
						WfmConstants.WfmAppAttr_TaskPk, task.getPk_task());
				getCurrentAppCtx().addAppAttribute(
						WfmConstants.WfmUrlConst_TaskPk, task.getPk_task());
			}
		} catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
  }
  /** 
 * 删除
 * @param scriptEvent
 */
  public void onDelete(  MouseEvent<?> mouseEvent){
    Dataset ds = getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待删除数据");
		}

		Row row = ds.getSelectedRow();
		String pk_form = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));
		if (pk_form != null && !pk_form.equals("")) {
			boolean isCanDel = WfmCPUtilFacade.isCanDelBill(pk_form);
			if (isCanDel) {
				WfmCPUtilFacade.delWfmInfo(pk_form);
				CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
			} else {
				throw new LfwRuntimeException("流程已启动，无法删除单据");
			}

		} else {
			throw new LfwRuntimeException("未获取到流程单据主键值");
		}
  }
  /** 
 * 流程进度
 * @param mouseEvent
 */
  public void onFlow(  MouseEvent<?> mouseEvent){
    // WfmUtilFacade.openFlowImage();
		Dataset ds = getMasterDs();
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("请先选择数据!");
		}
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_BillID, pkValue);
		WfmUtilFacade.openFlowImage();
  }
  /** 
 * 启用
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onStart(  MouseEvent<?> mouseEvent) throws BusinessException {
    CmdInvoker.invoke(new UifEnableCmd(getMasterDsId(), true));
  }
  /** 
 * 停用
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onStop(  MouseEvent<?> mouseEvent) throws BusinessException {
    CmdInvoker.invoke(new UifEnableCmd(getMasterDsId(), false));
  }
  /** 
 * 附件
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onAttchFile(  MouseEvent<?> mouseEvent){
    Dataset ds = getMasterDs();
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

		String taskPk = WfmUtilFacade.getTaskPkPkByPkFormIns(primaryKeyValue);
		// 如果当前人不是当前单据的参与者,此时取不到任务.附件只能查看,需要设为浏览态.
		if (taskPk == null || taskPk.equals("")) {
			getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE,
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
  public void onPrint(  MouseEvent<?> mouseEvent){
    Dataset ds = getMasterDs();
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
 * 主组织变化
 */
  public void doOrgChange(  Map<?,?> keys){
    CmdInvoker.invoke(new UifDatasetLoadCmd(getMasterDsId()) {
			@Override
			protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
				String where = AppUtil.getGroupOrgFielterSqlForDesign(false,
						PK_ORG);
				ds.setLastCondition(where);
				return where;
			}

			// 避免翻页时重走缓存
			@Override
			protected void changeCurrentKey() {
				setChangeCurrentKey(true);
			}
		});
		clearDetailDs();
  }
  private void clearDetailDs(){
    Dataset[] detailDss = getDetailDs(getDetailDsIds());
		int len = detailDss != null ? detailDss.length : 0;
		for (int i = 0; i < len; i++) {
			if (detailDss[i] != null) {
				detailDss[i].clear();
			}
		}
  }
  /** 
 * 查询pluguin
 * @param keys
 */
  public void doQueryChange(  Map<?,?> keys){
    Dataset ds = getMasterDs();
		FromWhereSQL whereSql = (FromWhereSQL) keys
				.get(FromWhereSQL.WHERE_SQL_CONST);
		// 选择事项分类的时候，要查询某一事项分类下的所有小项
		StringBuilder where = new StringBuilder(whereSql.getWhere());
		int index = where.indexOf("scapco_ent_matters.matter_class");
		if (index > 0) {
			int end = where.indexOf(")", index);
			String tmp = where.substring(0, index);
			int start = tmp.lastIndexOf("(");
			String matter_class = where.subSequence(start, end + 1).toString();// where.substring(start,
			// end+1);
			if (matter_class.indexOf("in") > 0) {
				end = where.indexOf(")", end + 1);
				matter_class = where.subSequence(start, end + 1).toString();
			}
			Pattern p = Pattern.compile("('[^']*')");
			Matcher m = p.matcher(matter_class);
			String class_where = "(";
			while (m.find()) {
				class_where += m.group() + ",";
			}
			class_where = class_where.substring(0, class_where.length() - 1)
					+ ")";
			String tmp_where = "((scapco_ent_matters.matter_class in "
					+ class_where
					+ ") or (scapco_ent_matters.matter_class in (select pk_defdoc from bd_defdoc where pid in "
					+ class_where
					+ " and pk_defdoclist=(select pk_defdoclist from bd_defdoclist where code='scapco_0006'))))";
			where.replace(start, end + 1, tmp_where);
		}
		String releaseWhere ="";
		String pagecode = MatterUtil.getPageCode();
		if ("release".equals(pagecode)) {
			releaseWhere = "and formstate = 'End' ";
		}
		CmdInvoker.invoke(new ListPageQueryCmd(ds, this.getFlwTypePk(), where
				.toString() + releaseWhere, null));
  }
  @Override protected String getMasterDsId(){
    return "ent_matters";
  }
  public void onDetail(  MouseEvent<?> mouseEvent){
    Dataset ds = getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待编辑数据");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		String func_type = MatterUtil.getMatterFuncType();
		String report_user = MatterUtil.getAddressParameter("report_user");

		OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		paramMap.put("model", "nc.scap.pub.entmatter.util.CardWinPageModel");
		paramMap.put("pro_action", "detail");
		paramMap.put("func_type", func_type);
		paramMap.put("report_user", report_user);
		props.setParamMap(paramMap);

		this.onEdit_wfm(pkValue);

		getCurrentAppCtx().navgateTo(props);
  }
  public void onApprove(  MouseEvent<?> mouseEvent){
    Dataset ds = getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待编辑数据");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		String func_type = MatterUtil.getMatterFuncType();
		String report_user = MatterUtil.getAddressParameter("report_user");

		OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		paramMap.put("model", "nc.scap.pub.entmatter.util.CardWinPageModel");
		paramMap.put("pro_action", "approve");
		paramMap.put("func_type", func_type);
		paramMap.put("report_user", report_user);
		props.setParamMap(paramMap);

		this.onEdit_wfm(pkValue);
		paramMap.put(
				WfmConstants.WfmUrlConst_TaskPk,
				(String) getCurrentAppCtx().getAppAttribute(
						WfmConstants.WfmAppAttr_TaskPk));

		getCurrentAppCtx().navgateTo(props);
  }
  public void onRelease(  MouseEvent mouseEvent){
    Dataset ds = getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待发布数据");
		}
		Row[] rows = ds.getAllRow();
		for (Row row : rows) {
			Object isRelease = row.getValue(ds.nameToIndex(EntmattersVO.DEF2));
			if (isRelease != null
					&& Integer.parseInt(isRelease.toString()) == 1)
				throw new LfwRuntimeException("所选数据存在已发布信息！");
			row.setValue(ds.nameToIndex(EntmattersVO.DEF2), "1");
		}
		EntmattersVO[] vos = (EntmattersVO[]) new Dataset2SuperVOSerializer<SuperVO>()
				.serialize(ds);
		List<EntmattersVO> updates = new ArrayList<EntmattersVO>();
		if (vos == null)
			return;
		for (EntmattersVO entmattersVO : vos) {
			updates.add(entmattersVO);
		}
		if (updates.size() > 0) {
			ScapDAO.updateVOArray(updates.toArray(new EntmattersVO[updates
			                                                       .size()]));
		}
		AppInteractionUtil.showMessageDialog("发布成功！");
  }
  public void onUnrelease(  MouseEvent mouseEvent){
    Dataset ds = getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中已发布数据");
		}

		Row[] rows = ds.getAllRow();
		for (Row row : rows) {
			Object isRelease = row.getValue(ds.nameToIndex(EntmattersVO.DEF2));
			if (isRelease != null
					&& Integer.parseInt(isRelease.toString()) == 0)
				throw new LfwRuntimeException("所选数据存在未发布信息！");
			row.setValue(ds.nameToIndex(EntmattersVO.DEF2), "0");
		}
		EntmattersVO[] vos = (EntmattersVO[]) new Dataset2SuperVOSerializer<SuperVO>()
				.serialize(ds);
		List<EntmattersVO> updates = new ArrayList<EntmattersVO>();
		if (vos == null)
			return;
		for (EntmattersVO entmattersVO : vos) {
			updates.add(entmattersVO);
		}
		if (updates.size() > 0) {
			ScapDAO.updateVOArray(updates.toArray(new EntmattersVO[updates
			                                                       .size()]));
		}
		AppInteractionUtil.showMessageDialog("已取消发布！");
  }
}
