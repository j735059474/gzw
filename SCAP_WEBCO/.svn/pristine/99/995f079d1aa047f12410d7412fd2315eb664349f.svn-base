package nc.scap.pub.workreport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.model.ScapCoConstants;
import nc.scap.pub.notice.util.NoticeUtil;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.WorkReportVO;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.bm.ButtonStateManager;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.EmptyRow;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.plug.TranslatedRow;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.portal.log.ScapLogger;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import uap.web.bd.pub.AppUtil;
/** 
 * 信息列表默认逻辑
 */
public class WorkreportListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="workreportComp.workreport_cardwin";
  private static final String CARD_WIN_TITLE="编辑";
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
		paramMap.put(IGlobalConstants.METHOD_TYPE, IGlobalConstants.BTN_ADD);
	        paramMap.put("model", "nc.scap.pub.workreport.model.WorkreportPageModel");
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
		paramMap.put(IGlobalConstants.METHOD_TYPE, IGlobalConstants.BTN_EDIT);
	        paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
	        paramMap.put("model", "nc.scap.pub.workreport.model.WorkreportPageModel");
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * 删除
 * @param scriptEvent
 */
  public void onDelete(  MouseEvent mouseEvent){
    //    CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
      Dataset ds = this.getMasterDs();
      if (ds.getSelectedIndex() < 0) {
          throw new LfwRuntimeException("请选中待删除数据");
      }
      Row[] rows = ds.getSelectedRows();
      for (Row tmp : rows) {
          if (tmp instanceof EmptyRow) {
              continue;
          }
          String state = (String) tmp.getValue(ds
                  .nameToIndex(WorkReportVO.STATE));
          if (!state.equals("0") && !state.equals("2")) {// 只有状态为未提交或退回状态时才能收回
              throw new LfwRuntimeException("单据状态为未提交或退回状态才能删除！");
          }
      }

      if (AppInteractionUtil.showConfirmDialog("提示", "是否确认删除？")) {
          CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
      }
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
		uap.web.bd.pub.AppUtil.addAppAttr(LfwFileConstants.SYSID,LfwFileConstants.SYSID_BAFILE);
		String primaryKey = ds.getPrimaryKeyField();
		if (primaryKey == null) {
			throw new LfwRuntimeException("当前Dataset没有设置主键!");
		}
		String primaryKeyValue = (String) row.getValue(ds.nameToIndex(primaryKey));

		String taskPk = WfmUtilFacade.getTaskPkPkByPkFormIns(primaryKeyValue);
		// 如果当前人不是当前单据的参与者,此时取不到任务.附件只能查看,需要设为浏览态.
		if (taskPk == null || taskPk.equals("")) {
			this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE, WfmConstants.BILLSTATE_BROWSE);
		}

		// 流程附件参数
		Map<String, String> wfmParam = WfmUtilFacade.getFileMgrParamsByTask(taskPk);

		// 附件参数
		Map<String, String> param = UploadFileHelper.BuildDefaultPamater("", primaryKeyValue, CommonObjectConstants.AttachFileType, "");
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
			ICpPrintTemplateService service = ServiceLocator.getService(ICpPrintTemplateService.class);
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
    //    TranslatedRow selRow = (TranslatedRow) keys.get(OPERATE_ROW);
//			if (selRow != null) {
//				Dataset ds = this.getMasterDs();
//				String sign = this.getOperator();
//				if (AppConsts.OPE_EDIT.equals(sign)) {
//					Row savedRow = ds.getSelectedRow();
//					copyTranslateRow2Row(selRow,savedRow,ds);
////					}
//				} else if (AppConsts.OPE_ADD.equals(sign)) {
//					Row savedRow = ds.getEmptyRow();
//					savedRow = copyTranslateRow2Row(selRow,savedRow,ds);
//					ds.addRow(savedRow);
//				}
//			}
      
      TranslatedRow selRow = (TranslatedRow) keys.get(OPERATE_ROW);
      String key = "workreportComp.workreport_cardwin_main_afterSavePlugout";
      Map<String, Object> paramMap = AppLifeCycleContext.current()
              .getApplicationContext().getPlug(key);
      if (selRow != null) {
          Dataset ds = this.getMasterDs();
          if (paramMap != null) {
              String oper = (String) paramMap.get("oper");
              String operation = (String) paramMap.get("operation");
              if (operation != null && operation.length() != 0) {
                  LfwRuntimeEnvironment.getWebContext().getWebSession()
                          .addOriginalParameter(AppConsts.OPEN_BILL_ID, null);
//                  updateAllState(operation, ds);
                  DataLoadEvent dataLoadEvent = new DataLoadEvent(ds);
                  if (ScapCoConstants.ADD.equals(oper)) {
                      onDataLoadChangeCurrentKeyOrignalPage(dataLoadEvent);
                  } else {
                      onDataLoad(dataLoadEvent);
                  }

                  String message = "";
                  if (ScapCoConstants.SUBMIT.equals(operation)) {
                      message = "提交成功！";
                  } else if (ScapCoConstants.RECYCLE.equals(operation)) {
                      message = "收回成功！";
                  } else if (ScapCoConstants.ROLLBACK.equals(operation)) {
                      message = "退回成功！";
                  } else if (ScapCoConstants.RECEIVE.equals(operation)) {
                      message = "接收成功！";
                  } else if (ScapCoConstants.ADD.equals(oper)
                          && ScapCoConstants.SAVE.equals(operation)) {
                      message = "新增成功！";
                  } else if (ScapCoConstants.EDIT.equals(oper)
                          && ScapCoConstants.SAVE.equals(operation)) {
                      message = "保存成功！";
                  }
                  AppInteractionUtil.showShortMessage(message);
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
 * 主组织变化
 * @param keys
 */
  public void doOrgChange(  Map<?,?> keys){
    //    CmdInvoker.invoke(new UifDatasetLoadCmd(this.getMasterDsId()) {
//				protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
//					String where = AppUtil.getGroupOrgFielterSqlForDesign(false, "pk_org");
//					ds.setLastCondition(where);
//					return where;
//				}
//	
//				// 避免翻页时重走缓存
//				protected void changeCurrentKey() {
//					setChangeCurrentKey(true);
//				}
//			});
//			this.clearDetailDs();
      String auto_load = (String) AppUtil.getAppAttr(ScapCoConstants.ORG_AUTO_LOAD);
      if("false".equals(auto_load)){
              AppUtil.addAppAttr(ScapCoConstants.ORG_AUTO_LOAD,"true");
              return;
      }

      CmdInvoker.invoke(new UifDatasetLoadCmd(this.getMasterDsId()) {
          protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
              AppUtil.addAppAttr(ScapCoConstants.QUERY_TYPE,ScapCoConstants.QUERY_TYPE_ORG);
              String where = getWheresql();
              ds.setLastCondition(where);
              ds.setExtendAttribute(Dataset.ORDER_PART,
                      "order by pk_org desc,input_date desc");
              return where;
          }

          // 避免翻页时重走缓存
          protected void changeCurrentKey() {
              setChangeCurrentKey(true);
          }
      });

      this.clearDetailDs();
  }
  /** 
 * 查询plugin
 * @param keys
 */
  public void doQueryChange(  Map<?,?> keys){
    //    FromWhereSQL whereSql = (FromWhereSQL) keys.get(FromWhereSQL.WHERE_SQL_CONST);
//			CmdInvoker.invoke(new QueryCmd(MAIN_VIEW_ID, this.getMasterDsId(), whereSql.getWhere()));
      FromWhereSQL whereSql = (FromWhereSQL) keys
              .get(FromWhereSQL.WHERE_SQL_CONST);
      AppUtil.addAppAttr("last_condition", whereSql.getWhere());
      AppUtil.addAppAttr(ScapCoConstants.QUERY_TYPE,ScapCoConstants.QUERY_TYPE_SIMPLE_QUERY);
      final String where = getWheresql();
      CmdInvoker.invoke(new QueryCmd(MAIN_VIEW_ID, this.getMasterDsId(),
              where) {
          protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
              ds.setExtendAttribute(Dataset.ORDER_PART,
                      "order by pk_org desc,input_date desc ");
              return super.postProcessQueryVO(vo, ds);
          }

          // 避免翻页时重走缓存
          protected void changeCurrentKey() {
              setChangeCurrentKey(true);
          }
      });

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
    return "workreport";
  }
  public void onLook(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
      if (ds.getSelectedIndex() < 0) {
          throw new LfwRuntimeException("请选中待查看数据");
      }

      Row row = ds.getSelectedRow();
      String pkValue = (String) row.getValue(ds.nameToIndex(ds
              .getPrimaryKeyField()));

      OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
      props.setButtonZone(false);
      Map<String, String> paramMap = new HashMap<String, String>(2);
      paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
      paramMap.put(IGlobalConstants.METHOD_TYPE, IGlobalConstants.BTN_LOOK);
      paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
      paramMap.put("model", "nc.scap.pub.workreport.model.WorkreportPageModel");
      props.setParamMap(paramMap);
      this.getCurrentAppCtx().navgateTo(props);
  }
  public void onSubmit(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
      if (ds.getSelectedIndex() < 0) {
          throw new LfwRuntimeException("请选中待提交数据");
      }
      Row[] rows = ds.getSelectedRows();
      for (Row tmp : rows) {
          if (tmp instanceof EmptyRow) {
              continue;
          }
          String state = (String) tmp.getValue(ds
                  .nameToIndex(WorkReportVO.STATE));
          if (!state.equals("0") && !state.equals("2")) {// 只有状态为未提交或退回状态时才能提交
              throw new LfwRuntimeException("单据状态为未提交或退回状态才能提交！");
          }
      }

      if (AppInteractionUtil.showConfirmDialog("提示", "是否确认提交？")) {
          updateAllState(ScapCoConstants.SUBMIT, ds);
          updateReceiveCompStatus(ScapCoConstants.SUBMIT, ds);
          onDataLoad(new DataLoadEvent(ds));
          AppInteractionUtil.showShortMessage("提交成功！");
      }
  }
  public void onRecycle(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
      if (ds.getSelectedIndex() < 0) {
          throw new LfwRuntimeException("请选中待收回数据");
      }
      Row[] rows = ds.getSelectedRows();
      for (Row tmp : rows) {
          if (tmp instanceof EmptyRow) {
              continue;
          }
          String state = (String) tmp.getValue(ds
                  .nameToIndex(WorkReportVO.STATE));
          if (!state.equals("1")) {// 只有状态为已提交状态时才能收回
              throw new LfwRuntimeException("单据状态为已提交状态才能收回！");
          }
      }
      if (AppInteractionUtil.showConfirmDialog("提示", "是否确认收回？")) {
          updateAllState(ScapCoConstants.RECYCLE, ds);
          updateReceiveCompStatus(ScapCoConstants.RECYCLE, ds);
          onDataLoad(new DataLoadEvent(ds));
          AppInteractionUtil.showShortMessage("收回成功！");
      }
  }
  public void onApprove(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
      if (ds.getSelectedIndex() < 0) {
          throw new LfwRuntimeException("请选中待审核数据");
      }

      Row row = ds.getSelectedRow();
      String state = (String) row.getValue(ds
              .nameToIndex(WorkReportVO.STATE));
      String handleState = (String) row.getValue(ds
              .nameToIndex(WorkReportVO.HANDLE_STATE));
      if (state == null || !state.equals("1") || handleState == null
              || !handleState.equals("0")) {
          throw new LfwRuntimeException("数据不能进行审核");
      }

      String pkValue = (String) row.getValue(ds.nameToIndex(ds
              .getPrimaryKeyField()));

      OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
      props.setButtonZone(false);
      Map<String, String> paramMap = new HashMap<String, String>(2);
      paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
      paramMap.put(IGlobalConstants.METHOD_TYPE, "approve");
      paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
      paramMap.put("model", "nc.scap.pub.workreport.model.WorkreportPageModel");
      props.setParamMap(paramMap);
      this.getCurrentAppCtx().navgateTo(props);
  }
  public void onReapprove(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
      if (ds.getSelectedIndex() < 0) {
          throw new LfwRuntimeException("请选中待重审数据");
      }

      Row row = ds.getSelectedRow();
      String state = (String) row.getValue(ds
              .nameToIndex(WorkReportVO.STATE));
      String handleState = (String) row.getValue(ds
              .nameToIndex(WorkReportVO.HANDLE_STATE));
      if (state == null || !state.equals("3") || handleState == null
              || !handleState.equals("1")) {
          throw new LfwRuntimeException("数据不能进行重审");
      }

      String pkValue = (String) row.getValue(ds.nameToIndex(ds
              .getPrimaryKeyField()));

      OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
      props.setButtonZone(false);
      Map<String, String> paramMap = new HashMap<String, String>(2);
      paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
      paramMap.put(IGlobalConstants.METHOD_TYPE, "reapprove");
      paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
      paramMap.put("model", "nc.scap.pub.workreport.model.WorkreportPageModel");
      props.setParamMap(paramMap);
      this.getCurrentAppCtx().navgateTo(props);
  }
  public void onReceive(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
      if (ds.getSelectedIndex() < 0) {
          throw new LfwRuntimeException("请选中待接收数据");
      }
      Row[] rows = ds.getSelectedRows();
      for (Row tmp : rows) {
          if (tmp instanceof EmptyRow) {
              continue;
          }
          String state = (String) tmp.getValue(ds
                  .nameToIndex(WorkReportVO.STATE));
          String handleState = (String) tmp.getValue(ds
                  .nameToIndex(WorkReportVO.HANDLE_STATE));
          if (!state.equals("1") || !handleState.equals("0")) {// 只有状态为已提交且办理情况为待办状态才能接收
              throw new LfwRuntimeException("单据状态为已提交且办理情况为待办状态才能接收！");
          }
      }
      updateAllState(ScapCoConstants.RECEIVE, ds);
      updateReceiveCompStatus(ScapCoConstants.RECEIVE, ds);
      onDataLoad(new DataLoadEvent(ds));
      AppInteractionUtil.showShortMessage("接收成功！");
  }
  public void onRollback(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
      if (ds.getSelectedIndex() < 0) {
          throw new LfwRuntimeException("请选中待退回数据");
      }
      Row[] rows = ds.getSelectedRows();
      for (Row tmp : rows) {
          if (tmp instanceof EmptyRow) {
              continue;
          }
          String state = (String) tmp.getValue(ds
                  .nameToIndex(WorkReportVO.STATE));
          String handleState = (String) tmp.getValue(ds
                  .nameToIndex(WorkReportVO.HANDLE_STATE));
          if (!state.equals("1") || !handleState.equals("0")) {// 只有状态为已提交且办理情况为待办状态时才能退回
              throw new LfwRuntimeException("单据状态为已提交且办理情况为待办状态才能退回！");
          }
      }
      updateAllState(ScapCoConstants.ROLLBACK, ds);
      updateReceiveCompStatus(ScapCoConstants.ROLLBACK, ds);
      onDataLoad(new DataLoadEvent(ds));
      AppInteractionUtil.showShortMessage("退回成功！");
  }
  /** 
 * 批量更新
 * @param operation
 * @param ds
 */
  public void updateAllState(  String operation,  Dataset ds){
    Row[] rows = ds.getSelectedRows();
          if (rows == null || rows.length <= 0) {
              return;
          }
          for (Row tmp : rows) {
              if (tmp instanceof EmptyRow) {
                  continue;
              }
              realUpdateState(operation, ds, tmp);
          }
          WorkReportVO[] vos = new Dataset2SuperVOSerializer<WorkReportVO>()
                  .serialize(ds, rows);
          try {
              ScapDAO.getBaseDAO().updateVOArray(vos);
              AppInteractionUtil.showShortMessage("更新成功!");
          } catch (DAOException e) {
              ScapLogger.error("更新单据状态时出错,操作：" + operation);
          }
  }
  /** 
 * 给通知功能中的接受企业子表回写填报状态 // luqzh add 20141126
 * @param operation
 * @param ds
 */
  public void updateReceiveCompStatus(  String operation,  Dataset ds){
	  Row[] rows = ds.getSelectedRows();
      if (rows == null || rows.length <= 0) {
          return;
      }
      for (Row tmp : rows) {
			String pk_notice = tmp.getString(ds.nameToIndex(WorkReportVO.PK_NOTICE));
			String pk_user = tmp.getString(ds.nameToIndex(WorkReportVO.INPUT_USER));
			if (pk_notice != null && StringUtils.isNotEmpty(pk_notice)) {
			      // 通知表状态会写
				if (pk_notice != null && StringUtils.isNotEmpty(pk_notice)) {			
				 if (ScapCoConstants.SAVE.equals(operation)) {
					NoticeUtil.noticeWriteBack(pk_notice,pk_user,IGlobalConstants.REPORT_STATUS_SAVE);
					} 
				 else if (ScapCoConstants.RECYCLE.equals(operation)) {
					 NoticeUtil.noticeWriteBack(pk_notice,pk_user,IGlobalConstants.REPORT_STATUS_UNCOMMIT);
				 }
					else if (ScapCoConstants.SUBMIT.equals(operation)) {
			    	  NoticeUtil.noticeWriteBack(pk_notice,pk_user,IGlobalConstants.REPORT_STATUS_COMMIT);
			      }else if(ScapCoConstants.RECEIVE.equals(operation)) {
			    	  NoticeUtil.noticeWriteBack(pk_notice,pk_user,IGlobalConstants.REPORT_STATUS_RECEIVED);
			      } else if (ScapCoConstants.ROLLBACK.equals(operation)) {
			    	  NoticeUtil.noticeWriteBack(pk_notice,pk_user,IGlobalConstants.REPORT_STATUS_RETURN);
			      }
				}
			}
      }

  }
  /** 
 * 更新单据状态
 * @param operation
 * @param row
 */
  public void realUpdateState(  String operation,  Dataset ds,  Row row){
    if (ScapCoConstants.SUBMIT.equals(operation)) {
              row.setValue(ds.nameToIndex(WorkReportVO.STATE), "1");
              row.setValue(ds.nameToIndex(WorkReportVO.SUBMIT_DATE), new UFDateTime());
          } else if (ScapCoConstants.RECYCLE.equals(operation)) {
              row.setValue(ds.nameToIndex(WorkReportVO.STATE), "0");
          } else if (ScapCoConstants.ROLLBACK.equals(operation)) {
              row.setValue(ds.nameToIndex(WorkReportVO.STATE), "2");
              row.setValue(ds.nameToIndex(WorkReportVO.HANDLE_STATE), "0");
          } else if (ScapCoConstants.RECEIVE.equals(operation)) {
              row.setValue(ds.nameToIndex(WorkReportVO.STATE), "3");
              row.setValue(ds.nameToIndex(WorkReportVO.HANDLE_STATE), "1");
              row.setValue(ds.nameToIndex(WorkReportVO.APPROVE_DATE),
                      new UFDate());
              row.setValue(ds.nameToIndex(WorkReportVO.APPROVER),
                      CntUtil.getCntUserPk());
          }
          clearDetailDs();
  }
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
    onDataLoadChangeCurrentKeySamePage(dataLoadEvent);
  }
  public void onDataLoadChangeCurrentKeyOrignalPage(  DataLoadEvent dataLoadEvent){
    final Dataset ds = dataLoadEvent.getSource();
          ds.setLastCondition(getWheresql());
          CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()) {
                  //避免翻页时重走缓存
              @Override
              protected void changeCurrentKey() {
                  setChangeCurrentKey(true);
              }
          });
          clearDetailDs();
  }
  public void onDataLoadChangeCurrentKeySamePage(  DataLoadEvent dataLoadEvent){
    final Dataset ds = dataLoadEvent.getSource();
          ds.setLastCondition(getWheresql());
          CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()) {
                  
                  

                          int currentPageIndex = 0;

              @Override
              protected void changeCurrentKey() {
                  setChangeCurrentKey(true);
                  currentPageIndex = ds.getCurrentRowSet().getPaginationInfo()
                          .getPageIndex();
              }

              @Override
              protected SuperVO[] queryVOs(PaginationInfo pinfo, SuperVO vo,
                      String wherePart, String orderPart)
                      throws LfwBusinessException {
                  pinfo.setPageIndex(currentPageIndex);
                  return super.queryVOs(pinfo, vo, wherePart, orderPart);
              }

          });
          clearDetailDs();
  }
  /** 
 * 获取SQL
 * @param flag简易检索用:true; 自动加载用:false。
 * @return
 */
  public String getWheresql(){
    String where = (String) AppUtil.getAppAttr("last_condition");
            String node_type = LfwRuntimeEnvironment.getWebContext()
                    .getOriginalParameter(IGlobalConstants.NODE_TYPE);
            if (node_type == null) {
                node_type = (String) AppLifeCycleContext.current()
                        .getApplicationContext()
                        .getAppAttribute(IGlobalConstants.NODE_TYPE);
            }

            String pk_org = (String) AppUtil.getAppAttr("pk_org");
            String pk_group = AppLifeCycleContext.current().getApplicationContext()
                    .getAppEnvironment().getPk_group();
            String pk_Org = AppLifeCycleContext.current().getApplicationContext()
                    .getAppEnvironment().getPk_org();

            String current_pk_org = CntUtil.getCntOrgPk();
            
            String queryType = (String) AppUtil.getAppAttr(ScapCoConstants.QUERY_TYPE);
            
            if (ScapCoConstants.QUERY_TYPE_ORG_TREE.equals(queryType) && IGlobalConstants.ORG_TREE_ROOT.equals(pk_org)) {
                where = "";
            }
            
            where = getFilteByNodeType(where, node_type);

            if (CpOrgUtil.isCompanyOrg(current_pk_org)) {
                if (where.contains("pk_org")) {
                    where += " and pk_group='" + pk_group + "' and dr=0 ";
                } else {
                    if (pk_Org == null) {
                        where += " and pk_org='" + current_pk_org
                                + "' and pk_group='" + pk_group + "' and dr=0 ";
                    } else {
                        where += " and pk_org='" + pk_Org + "' and pk_group='"
                                + pk_group + "' and dr=0 ";
                    }
                }

            } else {
                if (pk_org == null || IGlobalConstants.ORG_TREE_ROOT.equals(pk_org)) {
                    where += " and pk_group='" + pk_group + "' and dr=0 ";
                } else {
                    where += " and pk_org='" + pk_org + "' and pk_group='"
                            + pk_group + "' and dr=0 ";
                }
            }
            
            String businessType = (String)AppUtil.getAppAttr(ScapCoConstants.BUSINESS_TYPE);

            if(businessType != null) {
                where += " and " + WorkReportVO.BUSINESS_TYPE + "='" + businessType + "'";
            }

            return where;
  }
  public String getFilteByNodeType(  String where,  String node_type){
    String filtStr = "";
            if (where == null || "".equals(where)) {
                filtStr += " 1 = 1 ";
            } else {
                filtStr = where;
            }
            if (ScapCoConstants.ADD.equals(node_type)) {

            } else if (ScapCoConstants.APPROVE.equals(node_type)) {
                filtStr += " and ((state ='" + ScapCoConstants.STATE_YESSUBMIT
                        + "' and handle_state='"
                        + ScapCoConstants.HANDLE_STATE_NOT + "')"
                        + " or (state ='" + ScapCoConstants.STATE_RECIVE
                        + "' and handle_state='"
                        + ScapCoConstants.HANDLE_STATE_YES + "'))";
            } else if (ScapCoConstants.LOOK.equals(node_type)) {
                filtStr += " and state in ('" + ScapCoConstants.STATE_RECIVE
                        + "')";
            }

            return filtStr;
  }
  public void onAfterRowUnSelect(  DatasetEvent datasetEvent){
    ButtonStateManager.updateButtons();
      clearDetailDs();
  }
  public void doOrgTreeChange(  Map keys){
    LfwView main = AppLifeCycleContext.current().getWindowContext()
              .getViewContext(MAIN_VIEW_ID).getView();
      Dataset ds = main.getViewModels().getDataset(getMasterDsId());
      AppUtil.addAppAttr(ScapCoConstants.QUERY_TYPE,ScapCoConstants.QUERY_TYPE_ORG_TREE);
      ds.setLastCondition(getWheresql());

      CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()) {
          protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
              ds.setExtendAttribute(Dataset.ORDER_PART,
                      "order by pk_org desc,input_date desc");
              return super.postProcessQueryVO(vo, ds);
          }

          // 避免翻页时重走缓存
          protected void changeCurrentKey() {
              setChangeCurrentKey(true);
          }
      });

      clearDetailDs();
  }
}
