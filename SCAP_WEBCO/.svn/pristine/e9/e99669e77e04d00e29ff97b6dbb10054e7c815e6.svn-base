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
 * 信息列表默认逻辑
 */
public class MeetingListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="nc.scap.pub.Meeting.meeting_cardwin";
  private static final String CARD_WIN_TITLE="编辑";
  private static final String MEETING_STATE_ADD="新增";
  private static final String MEETING_STATE_TOFEEDBACK="待反馈";
  private static final String MEETING_STATE_FEEDBACKING="反馈中";
  private static final String MEETING_STATE_FEEDBACKED="已反馈";
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
		  // 1是国资委用户，2为企业用户，3为中介机构用户
			int userType = CntUtil.CtnGwzOrCompanyOrPartnerUser();
			// 用户的主键，用途 list数据权限
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
 * 主数据选中逻辑
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
 * 子表report数据选中逻辑
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
 * 新增
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
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * 查看
 * @param mouseEvent
 */
  public void onshowdetail(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
			if (ds.getSelectedIndex() < 0) {
				throw new LfwRuntimeException("请选中待查看数据");
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
			  AppInteractionUtil.showShortMessage("下发成功！");
				 CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[]{"edit","del","sendout"} , false);
		  }else {
			  AppInteractionUtil.showShortMessage("下发失败！");
		  }
		 //throw new LfwRuntimeException("下发成功!");
  }
  public void onfeedback(  MouseEvent mouseEvent){
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
		paramMap.put(AppConsts.OPE_SIGN, "feedback");
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
		CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[]{"feedback"} , false);
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
		String state = row.getString(ds.nameToIndex(ScapMeetingVO.STATE));
		Map<String, String> paramMap = null;
		if (primaryKeyValue != null){
			paramMap = new HashMap<String, String>(2);
			paramMap.put("billitem", primaryKeyValue);
			if(!state.equals(MEETING_STATE_ADD)) {
				paramMap.put("state", "8");
			}
		}
		CmdInvoker.invoke(new UifAttachCmd("附件", paramMap));

//		String taskPk = WfmUtilFacade.getTaskPkPkByPkFormIns(primaryKeyValue);
//		// 如果当前人不是当前单据的参与者,此时取不到任务.附件只能查看,需要设为浏览态.
//		if (taskPk == null || taskPk.equals("")) {
//			this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE, WfmConstants.BILLSTATE_BROWSE);
//		}
//
//		// 流程附件参数
//		Map<String, String> wfmParam = WfmUtilFacade.getFileMgrParamsByTask(taskPk);
//
//		// 附件参数
//		Map<String, String> param = UploadFileHelper.BuildDefaultPamater("", primaryKeyValue, CommonObjectConstants.AttachFileType, "");
//		param.put("usescanable", "true");
//		param.put("state", String.valueOf(31));
//
//		String title = "附件";
//		if (wfmParam != null && !wfmParam.isEmpty()) {
//			param.putAll(wfmParam);
//		}
//
//		CmdInvoker.invoke(new UifAttachCmd(title, param));
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
 * 主组织变化
 * @param keys
 */
  public void doOrgChange(  Map<?,?> keys){
    CmdInvoker.invoke(new UifDatasetLoadCmd(this.getMasterDsId()) {
				protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
					String where = AppUtil.getGroupOrgFielterSqlForDesign(false, "pk_org");
					ds.setLastCondition(where);
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
