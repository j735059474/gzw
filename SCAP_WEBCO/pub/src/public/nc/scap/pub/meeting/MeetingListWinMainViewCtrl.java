package nc.scap.pub.meeting;
import nc.scap.pub.vos.ScapMeetingVO;
import nc.scap.pub.vos.ScapOfficialdocVO;

import java.util.Map;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.event.DataLoadEvent;
import java.util.HashMap;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.scap.pub.vos.ScapMeetingStaffVO;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.data.Dataset;
import java.util.ArrayList;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.model.plug.TranslatedRow;
import nc.uap.lfw.file.LfwFileConstants;
import java.util.List;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.scap.pub.util.ScapDAO;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.vo.pub.SuperVO;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.lfw.file.UploadFileHelper;
import nc.scap.pub.util.CpVOUtil;
import nc.vo.pub.BusinessException;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.lfw.core.constants.AppConsts;
import uap.web.bd.pub.AppUtil;
import nc.scap.pub.vos.ScapMeetingUserVO;
import nc.scap.pub.util.CntUtil;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.scap.pub.util.CpWinUtil;
import uap.lfw.core.locator.ServiceLocator;
import nc.scap.pub.vos.ScapMeetingReportVO;
import nc.uap.lfw.core.event.MouseEvent;
/** 
 * ��Ϣ�б�Ĭ���߼�
 */
public class MeetingListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="nc.scap.pub.Meeting.meeting_cardwin";
  private static final String CARD_WIN_TITLE="�༭";
  private static final String MEETING_STATE_ADD="����";
  private static final String MEETING_STATE_TOFEEDBACK="������";
  private static final String MEETING_STATE_FEEDBACKING="������";
  private static final String MEETING_STATE_FEEDBACKED="�ѷ���";
  public void beforeShow(  DialogEvent dialogEvent){
    int userType = CntUtil.CtnGwzOrCompanyOrPartnerUser();
      if(userType ==1){
    	  CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[]{"showdetail","sendout"} , false);
    	  CpWinUtil.hideMenuItems(CpWinUtil.getView(), "menubar", new String[]{"feedback"});
      }else if(userType ==2){
    	  CpWinUtil.hideMenuItems(CpWinUtil.getView(), "menubar", new String[]{"add","del","edit","sendout"});
		  CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[]{"showdetail","feedback"} , false);
	  }
  }
  public void onDataLoad_scapco_meeting(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
		  // 1�ǹ���ί�û���2Ϊ��ҵ�û���3Ϊ�н�����û�
			int userType = CntUtil.CtnGwzOrCompanyOrPartnerUser();
			// �û�����������; list����Ȩ��
			String userPk = CntUtil.getCntUserPk();
			
			if(userType!=1){
				 StringBuffer condition = new StringBuffer();
				 condition.append(" pk_meeting in (");
				 condition.append(" select pk_meeting from scapco_meetingreport where pk_meetingreport in (");
				 condition.append(" select pk_meetingreport from scapco_meetingstaff where staff = '"+userPk+"'))");
				 condition.append(" and state <>'"+MEETING_STATE_ADD+"'");
				 ds.setLastCondition(condition.toString());
			}
		 
			CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
  /** 
 * ������ѡ���߼�
 * @param datasetEvent
 */
  public void onAfterRowSelectMeeting(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
    	//CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
    	
	  	Row row = ds.getSelectedRow();
		String pkMeeting = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));
		String state = (String) row.getValue(ds.nameToIndex(ScapMeetingVO.STATE));
		StringBuffer condition = new StringBuffer();
		condition.append(" pk_meeting = '"+pkMeeting+"'");
		int userType = CntUtil.CtnGwzOrCompanyOrPartnerUser();
		if(userType == 1){
			condition.append(" and pk_meetingreport in (select a.pk_meetingreport From scapco_meetingreport a ");
			condition.append(" join scapco_meetingstaff b on a.pk_meetingreport = b.pk_meetingreport )");
		}
		if(userType == 2){
			String org = CntUtil.getCntOrgPk();
			condition.append(" and acceptorg = '"+org+"'");
		}
		ScapMeetingReportVO[] reportVOs = (ScapMeetingReportVO[]) ScapDAO.retrieveByCondition(ScapMeetingReportVO.class, condition.toString());

		Dataset reportDataSet = CpWinUtil.getDataset("scapco_meetingreport");
		reportDataSet.clear();
		if(reportVOs!=null&&reportVOs.length>0){
			for(ScapMeetingReportVO reportvo:reportVOs){
				row = new CpVOUtil<ScapMeetingReportVO>().getRow(reportDataSet, reportvo);
				reportDataSet.addRow(row);
			}
		}

	  if(userType == 1){
		  CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[]{"attachfile","showdetail"} , true);
		  if(MEETING_STATE_ADD.equals(state)){
			  CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[]{"del","edit","sendout"} , true);
		  } else {
			  CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[]{"del","edit","sendout"} , false);
		  }
	  }else if(userType == 2) {
			CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[]{"attachfile","showdetail"} , true);
			if(reportVOs!=null&&reportVOs.length>0){
				String ismeeting = reportVOs[0].getIsmeeting();
				if(ismeeting==null||"".equals(ismeeting)){
					CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[]{"feedback"} , true);
				} else {
					CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[]{"feedback"} , false);
				}
			}
	  }
  }
  /** 
 * �ӱ�report����ѡ���߼�
 * @param datasetEvent
 */
  public void onAfterRowSelectReport(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
		  	//CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));

		  	Row row = ds.getSelectedRow();
			String pkMeetingReport = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));
			String condition = " pk_meetingreport = '"+pkMeetingReport+"'";
			ScapMeetingUserVO[] userVOs = (ScapMeetingUserVO[]) ScapDAO.retrieveByCondition(ScapMeetingUserVO.class, condition);

			Dataset userDataSet = CpWinUtil.getDataset("scapco_meetinguser");
			userDataSet.clear();
			if(userVOs!=null&&userVOs.length>0){
				for(ScapMeetingUserVO usertvo:userVOs){
					row = new CpVOUtil<ScapMeetingUserVO>().getRow(userDataSet, usertvo);
					userDataSet.addRow(row);
				}
			}
  }
  /** 
 * ����
 * @param mouseEvent
 */
  public void onAdd(  MouseEvent mouseEvent){
    OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		props.setWidth("1024");
		props.setHeight("768");
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_ADD);
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
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * �鿴
 * @param mouseEvent
 */
  public void onshowdetail(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
			if (ds.getSelectedIndex() < 0) {
				throw new LfwRuntimeException("��ѡ�д��鿴����");
			}

			Row row = ds.getSelectedRow();
			String pkValue = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));

			OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
			props.setButtonZone(false);
			Map<String, String> paramMap = new HashMap<String, String>(2);
			paramMap.put(AppConsts.OPE_SIGN, "showdetail");
			paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
			props.setParamMap(paramMap);
			this.getCurrentAppCtx().navgateTo(props);
  }
  public void onsendout(  MouseEvent mouseEvent){
    Dataset masterDs = this.getMasterDs();
    masterDs.getSelectedRow().setValue(masterDs.nameToIndex(ScapMeetingVO.STATE), MEETING_STATE_TOFEEDBACK);
		ScapMeetingVO meetingVO = new CpVOUtil<ScapMeetingVO>().getVO(masterDs, masterDs.getSelectedRow(), new ScapMeetingVO());
		meetingVO.setState(MEETING_STATE_TOFEEDBACK);
		String pk_meeting = ScapDAO.insertOrUpdateVO(meetingVO);
		masterDs.setValue(masterDs.nameToIndex(ScapMeetingVO.STATE), MEETING_STATE_TOFEEDBACK);
		  if(pk_meeting != null && !"".equals(pk_meeting)){
			  AppInteractionUtil.showShortMessage("�·��ɹ���");
				 CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[]{"edit","del","sendout"} , false);
		  }else {
			  AppInteractionUtil.showShortMessage("�·�ʧ�ܣ�");
		  }
		 //throw new LfwRuntimeException("�·��ɹ�!");
  }
  public void onfeedback(  MouseEvent mouseEvent){
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
		paramMap.put(AppConsts.OPE_SIGN, "feedback");
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
		CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[]{"feedback"} , false);
  }
  /** 
 * ɾ��
 * @param scriptEvent
 */
  public void onDelete(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
			if (ds.getSelectedIndex() < 0) {
				throw new LfwRuntimeException("��ѡ�д�ɾ������");
			}
			Row row = ds.getSelectedRow();
			ds.removeRow(row);
			String pkMeeting = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));

			StringBuffer condition = new StringBuffer();
			condition.append(" pk_meetingreport in (select pk_meetingreport from scapco_meetingreport where pk_meeting = '"+pkMeeting+"' )");
			ScapMeetingStaffVO[] staffVOs = (ScapMeetingStaffVO[]) ScapDAO.retrieveByCondition(ScapMeetingStaffVO.class, condition.toString());
			if(staffVOs!=null&&staffVOs.length>0){
				for(ScapMeetingStaffVO staffVO:staffVOs){
					ScapDAO.deleteByPk(ScapMeetingStaffVO.class, staffVO.getId());
				}
			}
			ds = CpWinUtil.getDataset("scapco_meetingstaff");
			ds.clear();
			
			condition.delete(0, condition.length());
			condition.append(" pk_meetingreport in (select pk_meetingreport from scapco_meetingreport where pk_meeting = '"+pkMeeting+"' )");
			ScapMeetingUserVO[] userVOs = (ScapMeetingUserVO[]) ScapDAO.retrieveByCondition(ScapMeetingUserVO.class, condition.toString());
			if(userVOs!=null&&userVOs.length>0){
				for(ScapMeetingUserVO userVO:userVOs){
					ScapDAO.deleteByPk(ScapMeetingUserVO.class, userVO.getPk_meetinguser());
				}
			}
			ds = CpWinUtil.getDataset("scapco_meetinguser");
			ds.clear();
			
			condition.delete(0, condition.length());
			condition.append(" pk_meeting = '"+pkMeeting+"'");
			ScapMeetingReportVO[] reportVOs = (ScapMeetingReportVO[]) ScapDAO.retrieveByCondition(ScapMeetingReportVO.class, condition.toString());
			if(reportVOs!=null&&reportVOs.length>0){
				for(ScapMeetingReportVO reportVO:reportVOs){
					ScapDAO.deleteByPk(ScapMeetingReportVO.class, reportVO.getPk_meetingreport());
				}
			}
			
			ScapDAO.deleteByPk(ScapMeetingVO.class, pkMeeting);
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
		String state = row.getString(ds.nameToIndex(ScapMeetingVO.STATE));
		Map<String, String> paramMap = null;
		if (primaryKeyValue != null){
			paramMap = new HashMap<String, String>(2);
			paramMap.put("billitem", primaryKeyValue);
			if(!state.equals(MEETING_STATE_ADD)) {
				paramMap.put("state", "8");
			}
		}
		CmdInvoker.invoke(new UifAttachCmd("����", paramMap));

//		String taskPk = WfmUtilFacade.getTaskPkPkByPkFormIns(primaryKeyValue);
//		// �����ǰ�˲��ǵ�ǰ���ݵĲ�����,��ʱȡ��������.����ֻ�ܲ鿴,��Ҫ��Ϊ���̬.
//		if (taskPk == null || taskPk.equals("")) {
//			this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE, WfmConstants.BILLSTATE_BROWSE);
//		}
//
//		// ���̸�������
//		Map<String, String> wfmParam = WfmUtilFacade.getFileMgrParamsByTask(taskPk);
//
//		// ��������
//		Map<String, String> param = UploadFileHelper.BuildDefaultPamater("", primaryKeyValue, CommonObjectConstants.AttachFileType, "");
//		param.put("usescanable", "true");
//		param.put("state", String.valueOf(31));
//
//		String title = "����";
//		if (wfmParam != null && !wfmParam.isEmpty()) {
//			param.putAll(wfmParam);
//		}
//
//		CmdInvoker.invoke(new UifAttachCmd(title, param));
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
    Row savedRow = (Row) keys.get(OPERATE_ROW);
	if (savedRow != null) {
		Dataset ds = this.getMasterDs();
		savedRow.setRowId(ds.getEmptyRow().getRowId());
		String sign = this.getOperator();
		if (AppConsts.OPE_EDIT.equals(sign)||"feedback".equals(sign)) {
			int index = ds.getRowIndex(ds.getSelectedRow());
			if (index >= 0) {
				ds.removeRow(index);
				ds.insertRow(index, savedRow);
			}
		} else if (AppConsts.OPE_ADD.equals(sign)) {
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
 * ����֯�仯
 * @param keys
 */
  public void doOrgChange(  Map<?,?> keys){
    CmdInvoker.invoke(new UifDatasetLoadCmd(this.getMasterDsId()) {
				protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
					String where = AppUtil.getGroupOrgFielterSqlForDesign(false, "pk_org");
					ds.setLastCondition(where);
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
    FromWhereSQL whereSql = (FromWhereSQL) keys.get(FromWhereSQL.WHERE_SQL_CONST);
			CmdInvoker.invoke(new QueryCmd(MAIN_VIEW_ID, this.getMasterDsId(), whereSql.getWhere()));
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
    return "scapco_meeting";
  }
}
