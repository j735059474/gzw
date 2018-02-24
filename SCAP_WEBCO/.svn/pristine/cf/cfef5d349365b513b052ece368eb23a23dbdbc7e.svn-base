package nc.scap.pub.contacts;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.notice.util.NoticeUtil;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.ScapDAO;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifDelCmdRVNoValidator;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.plug.TranslatedRow;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.scap.work_report_type.Work_report_type;
import nc.vo.scapco.work_notice_contacts.notice_contact_type;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
/** 
 * 信息列表默认逻辑
 */
public class ContactsListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="nc.scap.pub.contacts.contacts_cardwin";
  private static final String CARD_WIN_TITLE="编辑";
  /** 
 * 主数据加载逻辑
 * @param dataLoadEvent
 */
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
		String businessType = (String) LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("business_type");
		uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE_FUNCNODE, businessType);
		String lastCondition = ds.getLastCondition();
		// 对于常用联系人维护画面，不管是国资委还是企业用户, 都需要根据节点上配置的参数【business_type】进行过滤
		if (lastCondition == null) {
			ds.setLastCondition(getWheresql());
		}
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
  /** 
 * 主数据选中逻辑
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()) {
			protected String postProcessRowSelectVO(SuperVO vo, Dataset ds) {			
				LfwView main = LfwRuntimeEnvironment.getWebContext().getPageMeta()
						.getView("main");
				Dataset masteDs = main.getViewModels().getDataset("notice_contact_type");
				// 根据报告类型取得报告主体
				String report_body = "";
				String notice_style = (String)masteDs.getSelectedRow().getValue(masteDs.nameToIndex(notice_contact_type.NOTICE_STYLE));
				String report_type = (String)masteDs.getSelectedRow().getValue(masteDs.nameToIndex(notice_contact_type.REPORT_TYPE));
				// 如果是填报通知，那个根据报告类型取得报告主体
				if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
					Work_report_type WorkReportType = (Work_report_type) ScapDAO
							.retrieveByPK(Work_report_type.class, report_type);
					report_body = WorkReportType.getReport_body();
				} else if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
					// 如果是消息通知，则默认报告主体是个人
					report_body = IGlobalConstants.REPORT_BODY_MAN;
				} else if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
					// 如果是消息通知，则默认报告主体是个人
					report_body = IGlobalConstants.REPORT_BODY_MAN;
				}

				// 根据报告类型 取得报告主体，从而决定过滤条件是企业还是组织
				List<String> list_r = NoticeUtil
						.getAllChildPk_OrgByCurrentPk(CntUtil.getCntOrgPk());
				List<String> list_v = NoticeUtil
						.getAllChildPKVisualorgByPkUser(CntUtil.getCntUserPk());
				StringBuffer condition = new StringBuffer("pk_contact_type = '"
						+ vo.getAttributeValue("pk_contact_type") + "' and dr = '0'");
				String whereSql = "";
				// 报告主体是企业
				if (report_body != null && "1".equals(report_body)) {
					if (list_r != null && list_r.size() > 0) {
						condition
								.append(" and (")
								.append(NoticeUtil.getSqlStrByList(list_r, 1000,
										"pk_org")).append(")");
					}
				}
				// 报告主体是组织
				if (report_body != null && "2".equals(report_body)) {
					if (list_v != null && list_v.size() > 0) {
						condition
						.append(" and (")
						.append(NoticeUtil.getSqlStrByList(list_v, 1000,
								"pk_visualorg")).append(")");
					}
				}
				// 报告主体是个人
				if (report_body != null && "3".equals(report_body)) {
					if ((list_r != null && list_r.size() > 0) || (list_v != null && list_v.size() > 0)) {
						condition
						.append(" and (");
						if (list_r != null && list_r.size() > 0) {
							condition
							.append(" (")
							.append(NoticeUtil.getSqlStrByList(list_r, 1000,
									"pk_org")).append(")");
						}
						if (list_v != null && list_v.size() > 0) {
							condition
							.append(" or (")
							.append(NoticeUtil.getSqlStrByList(list_v, 1000,
									"pk_visualorg")).append(")");
						}
						condition
						.append(" )");
					}
				}

				ds.setLastCondition(condition.toString());
				return condition.toString();
			}

			// 重写此方法，避免选中第一行
			protected void postProcessChildRowSelect(Dataset ds) {
				return;
			}
		});
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
    CmdInvoker.invoke(new UifDelCmdRVNoValidator(this.getMasterDsId()));
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
 * 获取SQL
 * @param
 * @return
 */
  public String getWheresql(){
    StringBuffer whereSql = new StringBuffer();
		String businessType = (String) uap.web.bd.pub.AppUtil
				.getAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE_FUNCNODE);
		String[] businessTypeCode = businessType.substring(1,
				businessType.length() - 1).split(",");
		whereSql.append(NoticeUtil.getSqlIn("YE_TYPE", businessTypeCode));
		whereSql.append(" OR ( DATA_TYPE != '~') ");
		whereSql.append(" OR ( VDEF1 != '~') ");
		return whereSql.toString();
  }
  /** 
 * 查询plugin
 * @param keys
 */
  public void doQueryChange(  Map<?,?> keys){
    FromWhereSQL whereSql = (FromWhereSQL) keys
				.get(FromWhereSQL.WHERE_SQL_CONST);
		CmdInvoker.invoke(new QueryCmd(MAIN_VIEW_ID, this.getMasterDsId(),
				whereSql.getWhere() + " and " + getWheresql()));
		this.clearDetailDs();
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
    return "notice_contact_type";
  }
  public void doOrgChange(  Map keys){
    
  }
  public void onDataLoad_user(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
}
