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
 * ��Ϣ�б�Ĭ���߼�
 */
public class WorkreportListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="workreportComp.workreport_cardwin";
  private static final String CARD_WIN_TITLE="�༭";
  /** 
 * ������ѡ���߼�
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
  }
  /** 
 * ����
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
 * �༭
 * @param scriptEvent
 */
  public void onEdit(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("��ѡ�д��༭����");
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
 * ɾ��
 * @param scriptEvent
 */
  public void onDelete(  MouseEvent mouseEvent){
    //    CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
      Dataset ds = this.getMasterDs();
      if (ds.getSelectedIndex() < 0) {
          throw new LfwRuntimeException("��ѡ�д�ɾ������");
      }
      Row[] rows = ds.getSelectedRows();
      for (Row tmp : rows) {
          if (tmp instanceof EmptyRow) {
              continue;
          }
          String state = (String) tmp.getValue(ds
                  .nameToIndex(WorkReportVO.STATE));
          if (!state.equals("0") && !state.equals("2")) {// ֻ��״̬Ϊδ�ύ���˻�״̬ʱ�����ջ�
              throw new LfwRuntimeException("����״̬Ϊδ�ύ���˻�״̬����ɾ����");
          }
      }

      if (AppInteractionUtil.showConfirmDialog("��ʾ", "�Ƿ�ȷ��ɾ����")) {
          CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
      }
  }
  /** 
 * ����
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onStart(  MouseEvent<?> mouseEvent) throws BusinessException {
    CmdInvoker.invoke(new UifEnableCmd(this.getMasterDsId(), true));
  }
  /** 
 * ͣ��
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onStop(  MouseEvent<?> mouseEvent) throws BusinessException {
    CmdInvoker.invoke(new UifEnableCmd(this.getMasterDsId(), false));
  }
  /** 
 * ����
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onAttchFile(  MouseEvent<?> mouseEvent) throws BusinessException {
    Dataset ds = this.getMasterDs();
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("��ѡ������!");
		}
		uap.web.bd.pub.AppUtil.addAppAttr(LfwFileConstants.SYSID,LfwFileConstants.SYSID_BAFILE);
		String primaryKey = ds.getPrimaryKeyField();
		if (primaryKey == null) {
			throw new LfwRuntimeException("��ǰDatasetû����������!");
		}
		String primaryKeyValue = (String) row.getValue(ds.nameToIndex(primaryKey));

		String taskPk = WfmUtilFacade.getTaskPkPkByPkFormIns(primaryKeyValue);
		// �����ǰ�˲��ǵ�ǰ���ݵĲ�����,��ʱȡ��������.����ֻ�ܲ鿴,��Ҫ��Ϊ���̬.
		if (taskPk == null || taskPk.equals("")) {
			this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE, WfmConstants.BILLSTATE_BROWSE);
		}

		// ���̸�������
		Map<String, String> wfmParam = WfmUtilFacade.getFileMgrParamsByTask(taskPk);

		// ��������
		Map<String, String> param = UploadFileHelper.BuildDefaultPamater("", primaryKeyValue, CommonObjectConstants.AttachFileType, "");
		param.put("usescanable", "true");
		param.put("state", String.valueOf(31));

		String title = "����";
		if (wfmParam != null && !wfmParam.isEmpty()) {
			param.putAll(wfmParam);
		}

		CmdInvoker.invoke(new UifAttachCmd(title, param));
  }
  /** 
 * ��ӡ
 */
  public void onPrint(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("��ѡ������!");
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
 * �ⲿ����ˢ��
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
                      message = "�ύ�ɹ���";
                  } else if (ScapCoConstants.RECYCLE.equals(operation)) {
                      message = "�ջسɹ���";
                  } else if (ScapCoConstants.ROLLBACK.equals(operation)) {
                      message = "�˻سɹ���";
                  } else if (ScapCoConstants.RECEIVE.equals(operation)) {
                      message = "���ճɹ���";
                  } else if (ScapCoConstants.ADD.equals(oper)
                          && ScapCoConstants.SAVE.equals(operation)) {
                      message = "�����ɹ���";
                  } else if (ScapCoConstants.EDIT.equals(oper)
                          && ScapCoConstants.SAVE.equals(operation)) {
                      message = "����ɹ���";
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
 * ����֯�仯
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
//				// ���ⷭҳʱ���߻���
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

          // ���ⷭҳʱ���߻���
          protected void changeCurrentKey() {
              setChangeCurrentKey(true);
          }
      });

      this.clearDetailDs();
  }
  /** 
 * ��ѯplugin
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

          // ���ⷭҳʱ���߻���
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
          throw new LfwRuntimeException("��ѡ�д��鿴����");
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
          throw new LfwRuntimeException("��ѡ�д��ύ����");
      }
      Row[] rows = ds.getSelectedRows();
      for (Row tmp : rows) {
          if (tmp instanceof EmptyRow) {
              continue;
          }
          String state = (String) tmp.getValue(ds
                  .nameToIndex(WorkReportVO.STATE));
          if (!state.equals("0") && !state.equals("2")) {// ֻ��״̬Ϊδ�ύ���˻�״̬ʱ�����ύ
              throw new LfwRuntimeException("����״̬Ϊδ�ύ���˻�״̬�����ύ��");
          }
      }

      if (AppInteractionUtil.showConfirmDialog("��ʾ", "�Ƿ�ȷ���ύ��")) {
          updateAllState(ScapCoConstants.SUBMIT, ds);
          updateReceiveCompStatus(ScapCoConstants.SUBMIT, ds);
          onDataLoad(new DataLoadEvent(ds));
          AppInteractionUtil.showShortMessage("�ύ�ɹ���");
      }
  }
  public void onRecycle(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
      if (ds.getSelectedIndex() < 0) {
          throw new LfwRuntimeException("��ѡ�д��ջ�����");
      }
      Row[] rows = ds.getSelectedRows();
      for (Row tmp : rows) {
          if (tmp instanceof EmptyRow) {
              continue;
          }
          String state = (String) tmp.getValue(ds
                  .nameToIndex(WorkReportVO.STATE));
          if (!state.equals("1")) {// ֻ��״̬Ϊ���ύ״̬ʱ�����ջ�
              throw new LfwRuntimeException("����״̬Ϊ���ύ״̬�����ջأ�");
          }
      }
      if (AppInteractionUtil.showConfirmDialog("��ʾ", "�Ƿ�ȷ���ջأ�")) {
          updateAllState(ScapCoConstants.RECYCLE, ds);
          updateReceiveCompStatus(ScapCoConstants.RECYCLE, ds);
          onDataLoad(new DataLoadEvent(ds));
          AppInteractionUtil.showShortMessage("�ջسɹ���");
      }
  }
  public void onApprove(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
      if (ds.getSelectedIndex() < 0) {
          throw new LfwRuntimeException("��ѡ�д��������");
      }

      Row row = ds.getSelectedRow();
      String state = (String) row.getValue(ds
              .nameToIndex(WorkReportVO.STATE));
      String handleState = (String) row.getValue(ds
              .nameToIndex(WorkReportVO.HANDLE_STATE));
      if (state == null || !state.equals("1") || handleState == null
              || !handleState.equals("0")) {
          throw new LfwRuntimeException("���ݲ��ܽ������");
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
          throw new LfwRuntimeException("��ѡ�д���������");
      }

      Row row = ds.getSelectedRow();
      String state = (String) row.getValue(ds
              .nameToIndex(WorkReportVO.STATE));
      String handleState = (String) row.getValue(ds
              .nameToIndex(WorkReportVO.HANDLE_STATE));
      if (state == null || !state.equals("3") || handleState == null
              || !handleState.equals("1")) {
          throw new LfwRuntimeException("���ݲ��ܽ�������");
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
          throw new LfwRuntimeException("��ѡ�д���������");
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
          if (!state.equals("1") || !handleState.equals("0")) {// ֻ��״̬Ϊ���ύ�Ұ������Ϊ����״̬���ܽ���
              throw new LfwRuntimeException("����״̬Ϊ���ύ�Ұ������Ϊ����״̬���ܽ��գ�");
          }
      }
      updateAllState(ScapCoConstants.RECEIVE, ds);
      updateReceiveCompStatus(ScapCoConstants.RECEIVE, ds);
      onDataLoad(new DataLoadEvent(ds));
      AppInteractionUtil.showShortMessage("���ճɹ���");
  }
  public void onRollback(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
      if (ds.getSelectedIndex() < 0) {
          throw new LfwRuntimeException("��ѡ�д��˻�����");
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
          if (!state.equals("1") || !handleState.equals("0")) {// ֻ��״̬Ϊ���ύ�Ұ������Ϊ����״̬ʱ�����˻�
              throw new LfwRuntimeException("����״̬Ϊ���ύ�Ұ������Ϊ����״̬�����˻أ�");
          }
      }
      updateAllState(ScapCoConstants.ROLLBACK, ds);
      updateReceiveCompStatus(ScapCoConstants.ROLLBACK, ds);
      onDataLoad(new DataLoadEvent(ds));
      AppInteractionUtil.showShortMessage("�˻سɹ���");
  }
  /** 
 * ��������
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
              AppInteractionUtil.showShortMessage("���³ɹ�!");
          } catch (DAOException e) {
              ScapLogger.error("���µ���״̬ʱ����,������" + operation);
          }
  }
  /** 
 * ��֪ͨ�����еĽ�����ҵ�ӱ��д�״̬ // luqzh add 20141126
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
			      // ֪ͨ��״̬��д
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
 * ���µ���״̬
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
                  //���ⷭҳʱ���߻���
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
 * ��ȡSQL
 * @param flag���׼�����:true; �Զ�������:false��
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

          // ���ⷭҳʱ���߻���
          protected void changeCurrentKey() {
              setChangeCurrentKey(true);
          }
      });

      clearDetailDs();
  }
}
