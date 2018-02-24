package nc.scap.pub.officialdoc;
import nc.scap.pub.vos.ScapMeetingVO;
import nc.uap.lfw.core.cmd.CmdInvoker;
import java.util.Map;
import nc.vo.pub.lang.UFDateTime;
import nc.uap.lfw.core.event.DataLoadEvent;
import java.util.HashMap;
import nc.scap.pub.vos.ScapFeedBackUserVO;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.scap.pub.vos.ScapOfficialdocVO;
import nc.scap.pub.vos.ScapMeetingStaffVO;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.comp.WebElement;
import nc.scap.pub.vos.ScapFeedBackVO;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.data.Dataset;
import java.util.ArrayList;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.model.plug.TranslatedRow;
import nc.uap.lfw.file.LfwFileConstants;
import java.util.List;
import nc.scap.pub.util.ScapDAO;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.vo.pub.SuperVO;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.scap.pub.util.CpVOUtil;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.vo.pub.BusinessException;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.lfw.core.constants.AppConsts;
import uap.web.bd.pub.AppUtil;
import nc.scap.pub.util.CntUtil;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.scap.pub.util.CpWinUtil;
import uap.lfw.core.locator.ServiceLocator;
import nc.scap.pub.vos.ScapMeetingReportVO;
import nc.uap.lfw.core.event.MouseEvent;
/** 
 * 信息列表默认逻辑
 */
public class OfficialdocListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="nc.scap.pub.officialdoc.officialdoc_cardwin";
  private static final String CARD_WIN_TITLE="编辑";
  private static final String OFFICIAL_STATE_ADD="新增";
  private static final String OFFICIAL_STATE_TOFEEDBACK="待反馈";
  private static final String OFFICIAL_STATE_FEEDBACKING="反馈中";
  private static final String OFFICIAL_STATE_FEEDBACKED="已反馈";
  private static final String OFFICIAL_STATE_NOFEEDBACK="无需反馈";
  /** 
 * @param dialogEvent
 */
  public void beforeShow(  DialogEvent dialogEvent){
    CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[]{"edit", "del","sendout","report","feedback"} , false);
  }
  /** 
 * 主数据选中逻辑
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent datasetEvent){
    /*
	   * 国资委      a.下发    b.接收上报
	   * 企业          c.反馈    d.上报
	   * 
	   * 1 根据当前机构和发送机构来判断，公文是不是自己发送的
	   * 2根据 userType 1是国资委用户，2为企业用户，3为中介机构用户 
	   * 通过1和2的组合来判断数据属于 a，b，c，d哪一种
	   * 
	   * 3状态   
	   *  -- 只有新增状态才能下发和上报
	   *  -- 只有待反馈的状态，并且从未反馈过的  --> 才能反馈
	   */
		Dataset ds = datasetEvent.getSource();
		Row row = ds.getSelectedRow();
		String pk_officialdoc = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));
		String cntorg = CntUtil.getCntOrgPk();
		String cntuser = CntUtil.getCntUserPk();
		String senduser = (String) row.getValue(ds.nameToIndex(ScapOfficialdocVO.VBDEF1));
		int userType = CntUtil.CtnGwzOrCompanyOrPartnerUser();
		/*状态 */
		String state = (String) row.getValue(ds.nameToIndex(ScapOfficialdocVO.STATE));
		
		StringBuffer condition = new StringBuffer();
		if (cntuser.equals(senduser)) { //自己发送的公文		
			condition.append(" pk_officialdoc = '"+pk_officialdoc+"' ");
		} else {
			condition.append(" pk_officialdoc = '"+pk_officialdoc+"' and staff ='"+cntuser+"'");
		}
		ScapFeedBackUserVO [] feedbackUserVOs = (ScapFeedBackUserVO[]) ScapDAO.retrieveByCondition(ScapFeedBackUserVO.class, condition.toString());
	
		Dataset feedbackUserDataSet = CpWinUtil.getDataset("scapco_feedbackuser");
		feedbackUserDataSet.clear();
		if(feedbackUserVOs!=null&&feedbackUserVOs.length>0){
			for(ScapFeedBackUserVO feedbackUserVO:feedbackUserVOs){
				row = new CpVOUtil<ScapFeedBackUserVO>().getRow(feedbackUserDataSet, feedbackUserVO);
				feedbackUserDataSet.addRow(row);
			}
		}
		
		if (cntuser.equals(senduser)) { //自己发送的公文
			if(OFFICIAL_STATE_ADD.equals(state)){
				CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[]{"edit", "del"}, true);
//				if(userType==1){ //a下发
					CpWinUtil.setMenuItemEnable(CpWinUtil.getView(), "menubar", "sendout", true);
//				}else if (userType==2){//c上报
//					CpWinUtil.setMenuItemEnable(CpWinUtil.getView(), "menubar", "report", true);
//				}
			}else {
				CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[]{"edit", "del","sendout"}, false);
			}
			CpWinUtil.setMenuItemEnable(CpWinUtil.getView(), "menubar", "feedback", false);
		}else {
			CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[]{"edit", "del", "report"}, false);
//			if (userType==2){//d反馈	
				if(feedbackUserVOs!=null&&feedbackUserVOs.length>0){
					String feedbackdate = feedbackUserVOs[0].getVbdef1();
					if(!OFFICIAL_STATE_NOFEEDBACK.equals(state)&&feedbackdate==null){
						CpWinUtil.setMenuItemEnable(CpWinUtil.getView(), "menubar", "feedback", true);
					}else {
						CpWinUtil.setMenuItemEnable(CpWinUtil.getView(), "menubar", "feedback", false);
					}
				}
//			}else {
//				CpWinUtil.setMenuItemEnable(CpWinUtil.getView(), "menubar", "feedback", false);
//			}
		}
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
  }
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
	  
	  StringBuffer condition = new StringBuffer();
	  String userOrg = CntUtil.getCntOrgPk();
	  String userPk = CntUtil.getCntUserPk();
	  
	  condition.append(" vbdef1 = '"+userPk+"' or ");
	  condition.append(" (pk_officialdoc in (select pk_officialdoc from scapco_feedbackuser where staff ='"+userPk+"') ");
	  condition.append(" and state <> '"+OFFICIAL_STATE_ADD+"')");
	  ds.setLastCondition(condition.toString());
	  CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
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
 * 修改
 * @param mouseEvent
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
 * 反馈
 * @param mouseEvent
 */
  public void onFeedBack(  MouseEvent mouseEvent){
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
		paramMap.put(AppConsts.OPE_SIGN, "FeedBack");
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		//paramMap.put("model", "nc.scap.pub.officialdoc.pagemodel.OfficedocCardPagemodel");
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
		
		CpWinUtil.setMenuItemEnable(CpWinUtil.getView(), "menubar", "feedback", false);
  }
  /** 
 * 查看
 * @param mouseEvent
 */
  public void onShowDetail(  MouseEvent mouseEvent){
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
		paramMap.put(AppConsts.OPE_SIGN, "showdetail");
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * 下发
 * @param mouseEvent
 */
  public void onSendOut(  MouseEvent mouseEvent){
    Dataset masterDs = this.getMasterDs();
	  ScapOfficialdocVO officialVO = new CpVOUtil<ScapOfficialdocVO>().getVO(masterDs, masterDs.getSelectedRow(), new ScapOfficialdocVO());
	  
	  StringBuffer condition = new StringBuffer();
	  condition.append(" pk_officialdoc = '"+officialVO.getPk_officialdoc()+"' ");
	  ScapFeedBackUserVO[] staffVOs = (ScapFeedBackUserVO[]) ScapDAO.retrieveByCondition(ScapFeedBackUserVO.class, condition.toString());
	  if(staffVOs==null||staffVOs.length==0){
		  throw new LfwRuntimeException("下发范围为空，不能下发！");
	  }
	  
	  String isFeedBack = officialVO.getIsfeedback();
	  if("1".equals(isFeedBack)){
		  officialVO.setState(OFFICIAL_STATE_TOFEEDBACK);
		  masterDs.getSelectedRow().setValue(masterDs.nameToIndex(ScapOfficialdocVO.STATE), OFFICIAL_STATE_TOFEEDBACK);
	  }else {
		  officialVO.setState(OFFICIAL_STATE_NOFEEDBACK);
		  masterDs.getSelectedRow().setValue(masterDs.nameToIndex(ScapOfficialdocVO.STATE), OFFICIAL_STATE_NOFEEDBACK);
	  }
	  String pk_official = ScapDAO.insertOrUpdateVO(officialVO);
	  CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[]{"edit","del","sendout"} , false);
	  if(pk_official!=null&&!"".equals(pk_official)){
		  AppInteractionUtil.showShortMessage("下发成功！");
	  }else {
		  AppInteractionUtil.showShortMessage("下发失败！");
	  }
  }
  /** 
 * 上报
 * @param mouseEvent
 */
  public void onReport(  MouseEvent mouseEvent){
    Dataset masterDs = this.getMasterDs();
	  ScapOfficialdocVO officialVO = new CpVOUtil<ScapOfficialdocVO>().getVO(masterDs, masterDs.getSelectedRow(), new ScapOfficialdocVO());
	  officialVO.setState(OFFICIAL_STATE_NOFEEDBACK);
	  String pk_official = ScapDAO.insertOrUpdateVO(officialVO);
	  CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[]{"edit","del","report"} , false);
	  if(pk_official!=null&&!"".equals(pk_official)){
		  AppInteractionUtil.showShortMessage("上报成功！");
	  }else {
		  AppInteractionUtil.showShortMessage("上报失败！");
	  }
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
		String  pk_officialdoc= (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));
	  
		StringBuffer condition = new StringBuffer();
		condition.append(" pk_officialdoc ='"+pk_officialdoc+"'");
		ScapFeedBackVO[] feedBackVOs = (ScapFeedBackVO[]) ScapDAO.retrieveByCondition(ScapFeedBackVO.class, condition.toString());
		if(feedBackVOs!=null&&feedBackVOs.length>0){
			for(ScapFeedBackVO feedBackVO:feedBackVOs){
				ScapDAO.deleteByPk(ScapFeedBackVO.class, feedBackVO.getPk_feedback());
			}
		}
		ds = CpWinUtil.getDataset("scapco_feedbackuser");
		ds.clear();
		
		condition = new StringBuffer();
		condition.append(" pk_officialdoc ='"+pk_officialdoc+"'");
		ScapFeedBackUserVO[] feedBackUserVOs = (ScapFeedBackUserVO[]) ScapDAO.retrieveByCondition(ScapFeedBackUserVO.class, condition.toString());
		if(feedBackUserVOs!=null&&feedBackUserVOs.length>0){
			for(ScapFeedBackUserVO feedBackUserVO:feedBackUserVOs){
				ScapDAO.deleteByPk(ScapFeedBackUserVO.class, feedBackUserVO.getPk_feedbackuser());
			}
		}
		
		ScapDAO.deleteByPk(ScapOfficialdocVO.class, pk_officialdoc);
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
		String primaryKeyValue = (String) row.getValue(ds.nameToIndex(primaryKey));
		String state = row.getString(ds.nameToIndex(ScapOfficialdocVO.STATE));
		Map<String, String> paramMap = null;
		if (primaryKeyValue != null){
			paramMap = new HashMap<String, String>(2);
			paramMap.put("billitem", primaryKeyValue);
			if(!state.equals(OFFICIAL_STATE_ADD)) {
				paramMap.put("state", "8");
			}
		}
		CmdInvoker.invoke(new UifAttachCmd("附件", paramMap));

//		String taskPk = WfmUtilFacade.getTaskPkPkByPkFormIns(primaryKeyValue);
//		// 如果当前人不是当前单据的参与者,此时取不到任务.附件只能查看,需要设为浏览态.
//		if (taskPk == null || taskPk.equals("")) {
//			this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE,
//					WfmConstants.BILLSTATE_BROWSE);
//		}
//
//		// 流程附件参数
//		Map<String, String> wfmParam = WfmUtilFacade
//				.getFileMgrParamsByTask(taskPk);
//
//		// 附件参数
//		Map<String, String> param = UploadFileHelper.BuildDefaultPamater("",
//				primaryKeyValue, CommonObjectConstants.AttachFileType, "");
//		param.put("usescanable", "true");
//		param.put("state", String.valueOf(31));
//
//		String title = "附件";
//		if (wfmParam != null && !wfmParam.isEmpty()) {
//			param.putAll(wfmParam);
//		}

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
		Dataset ds = this.getMasterDs();
		savedRow.setRowId(ds.getEmptyRow().getRowId());
		String sign = this.getOperator();
		if (AppConsts.OPE_EDIT.equals(sign)||"FeedBack".equals(sign)) {
			int index = ds.getRowIndex(ds.getSelectedRow());
			if (index >= 0) {
				ds.removeRow(index);
				ds.insertRow(index, savedRow);
			}
		} else if (AppConsts.OPE_ADD.equals(sign)) {
			ds.addRow(savedRow);
		}
		ds = CpWinUtil.getDataset("scapco_feedbackuser");
		ds.clear();
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
    return "scapco_officialdoc";
  }
}
