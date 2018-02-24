package nc.scap.pub.meeting;
import nc.uap.lfw.core.cmd.UifAddCmd;
import nc.scap.pub.vos.ScapMeetingVO;
import nc.vo.pub.lang.UFDateTime;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetCellEvent;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import nc.uap.lfw.core.cmd.UifLineDelCmd;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import java.util.List;
import nc.uap.lfw.core.log.LfwLogger;
import nc.scap.pub.util.ScapDAO;
import nc.uap.lfw.core.comp.GridComp;
import nc.scap.pub.util.CpVOUtil;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.vos.ScapMeetingReportVO;
import nc.uap.lfw.core.comp.MenubarComp;
import java.util.Map;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.jsp.uimeta.UICardLayout;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.UifCopyCmd;
import nc.bs.dao.BaseDAO;
import java.util.HashMap;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.scap.pub.util.AttachAddPkUtil;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.scap.pub.vos.ScapMeetingStaffVO;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.Dataset;
import java.util.ArrayList;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.vo.pub.BusinessException;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.constants.AppConsts;
import nc.scap.pub.vos.ScapMeetingUserVO;
import uap.web.bd.pub.AppUtil;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.scap.pub.util.CpWinUtil;
import nc.scap.pub.vos.ScapUserChoiceBVO;
import uap.lfw.core.locator.ServiceLocator;
import nc.uap.lfw.core.event.MouseEvent;
/** 
 * 卡片窗口默认逻辑
 */
public class MeetingCardWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String PARAM_BILLITEM="billitem";
  private static final String PLUGOUT_ID="afterSavePlugout";
  private static final String MEETING_STATE_ADD="新增";
  private static final String MEETING_STATE_TOFEEDBACK="待反馈";
  private static final String MEETING_STATE_FEEDBACKING="反馈中";
  private static final String MEETING_STATE_FEEDBACKED="已反馈";
  /** 
 * 页面显示事件
 * @param dialogEvent
 */
  public void beforeShow(  DialogEvent dialogEvent){
    CpWinUtil.hideMenuItems(CpWinUtil.getView(), "menubar", new String[]{"add","copy","del","startorstrop","print"});
    GridComp grid = (GridComp) CpWinUtil.getComponent(CpWinUtil.getView(), "scapco_meetingstaff_grid");
    MenubarComp menuBar = grid.getMenuBar();
    menuBar.setEnabled(true);
    
		Dataset masterDs = this.getMasterDs();
		masterDs.clear();
		int userType = CntUtil.CtnGwzOrCompanyOrPartnerUser();
		
		String oper = getOperator();
		Row row = masterDs.getEmptyRow();
		
		if(AppConsts.OPE_ADD.equals(oper)){
			row.setValue(masterDs.nameToIndex(ScapMeetingVO.MEETINGRELEASEDATE), new UFDateTime());
			row.setValue(masterDs.nameToIndex(ScapMeetingVO.STATE), MEETING_STATE_ADD);
			masterDs.addRow(row);
			masterDs.setRowSelectIndex(masterDs.getRowIndex(row));
			masterDs.setEnabled(true);
			String pkid = generatePk();
			row.setValue(masterDs.nameToIndex(masterDs.getPrimaryKeyField()), pkid);
			//设置附件 主键
//			AttachAddPkUtil.setBillAttachPK(masterDs, pkid);

		}else if (AppConsts.OPE_EDIT.equals(oper)){
				String pkMeeting = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPEN_BILL_ID);
				//通知用户的信息
				StringBuffer condition = new StringBuffer();
				condition.append(" pk_meetingreport in (select pk_meetingreport from scapco_meetingreport where pk_meeting = '"+pkMeeting+"') ");
				ScapMeetingStaffVO[] staffVOs = (ScapMeetingStaffVO[]) ScapDAO.retrieveByCondition(ScapMeetingStaffVO.class, condition.toString());
				Dataset staffDataSet = CpWinUtil.getDataset("scapco_meetingstaff");
				if(staffVOs!=null&&staffVOs.length>0){
					for(ScapMeetingStaffVO staffvo:staffVOs){
						row = new CpVOUtil<ScapMeetingStaffVO>().getRow(staffDataSet, staffvo);
						staffDataSet.addRow(row);
					}
				}
				
				//上报用户信息
				condition.delete(0, condition.length());
				String org = CntUtil.getCntOrgPk();
				condition.append(" pk_meetingreport in (select pk_meetingreport from scapco_meetingreport where pk_meeting = '"+pkMeeting+"' ");
				condition.append(" and acceptorg = '"+org+"')");
				ScapMeetingUserVO[] userVOs = (ScapMeetingUserVO[]) ScapDAO.retrieveByCondition(ScapMeetingUserVO.class, condition.toString());
				Dataset userDataSet = CpWinUtil.getDataset("scapco_meetinguser");
				if(userVOs!=null&&userVOs.length>0){
					for(ScapMeetingUserVO usertvo:userVOs){
						row = new CpVOUtil<ScapMeetingUserVO>().getRow(userDataSet, usertvo);
						userDataSet.addRow(row);
					}
				}
				//主表会议通知的数据
				ScapMeetingVO vo = (ScapMeetingVO) ScapDAO.retrieveByPK(ScapMeetingVO.class, pkMeeting);
				row = new CpVOUtil<ScapMeetingVO>().getRow(masterDs, vo);
				masterDs.addRow(row);
				masterDs.setRowSelectIndex(masterDs.getRowIndex(row));
				masterDs.setEnabled(true);
		} else if ("showdetail".equals(oper)){//查看
			GridComp staffGrid = (GridComp) CpWinUtil.getComponent("scapco_meetingstaff_grid");
			staffGrid.setShowImageBtn(false);
			
			GridComp userGrid = (GridComp) CpWinUtil.getComponent("scapco_meetinguser_grid");
			userGrid.setShowImageBtn(false);
			
			CpWinUtil.hideMenuItems(CpWinUtil.getView(), "menubar", new String[]{"save","userChoice"});
			
			if(userType == 1){
				String pkMeeting = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPEN_BILL_ID);
				//通知用户的信息
				StringBuffer condition = new StringBuffer();
				condition.append(" pk_meetingreport in (select pk_meetingreport from scapco_meetingreport where pk_meeting = '"+pkMeeting+"') ");
				ScapMeetingStaffVO[] staffVOs = (ScapMeetingStaffVO[]) ScapDAO.retrieveByCondition(ScapMeetingStaffVO.class, condition.toString());
				Dataset staffDataSet = CpWinUtil.getDataset("scapco_meetingstaff");
				if(staffVOs != null && staffVOs.length > 0){
					for(ScapMeetingStaffVO staffvo:staffVOs){
						row = new CpVOUtil<ScapMeetingStaffVO>().getRow(staffDataSet, staffvo);
						String pkid = staffvo.getPk_meetingreport();
						ScapMeetingReportVO rvo = (ScapMeetingReportVO) ScapDAO.retrieveByPK(ScapMeetingReportVO.class, pkid);
						if(rvo != null) {
							row.setValue(staffDataSet.nameToIndex(ScapMeetingStaffVO.VBDEF1), rvo.getReportdate() != null ? rvo.getReportdate().toString() : "");
							row.setValue(staffDataSet.nameToIndex(ScapMeetingStaffVO.VBDEF2), rvo.getIsmeeting());
							row.setValue(staffDataSet.nameToIndex(ScapMeetingStaffVO.VBDEF3), rvo.getVbdef1());
						}
						staffDataSet.addRow(row);
					}
				}
				
				//上报用户信息
				condition.delete(0, condition.length());
				String org = CntUtil.getCntOrgPk();
				condition.append(" pk_meetingreport in (select pk_meetingreport from scapco_meetingreport where pk_meeting = '"+pkMeeting+"' ");
				condition.append(" and acceptorg = '"+org+"')");
				ScapMeetingUserVO[] userVOs = (ScapMeetingUserVO[]) ScapDAO.retrieveByCondition(ScapMeetingUserVO.class, condition.toString());
				Dataset userDataSet = CpWinUtil.getDataset("scapco_meetinguser");
				if(userVOs!=null&&userVOs.length>0){
					for(ScapMeetingUserVO usertvo:userVOs){
						row = new CpVOUtil<ScapMeetingUserVO>().getRow(userDataSet, usertvo);
						userDataSet.addRow(row);
					}
				}
				//主表会议通知的数据
				ScapMeetingVO vo = (ScapMeetingVO) ScapDAO.retrieveByPK(ScapMeetingVO.class, pkMeeting);
				row = new CpVOUtil<ScapMeetingVO>().getRow(masterDs, vo);
			} else if(userType == 2){
				String pkMeeting = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPEN_BILL_ID);
				//切换卡片
				UICardLayout cardLayout = (UICardLayout) CpWinUtil.getAppCtx().getWindowContext().getViewContext("main").getUIMeta().findChildById("card2052");
				cardLayout.setCurrentItem("1");
				//通知机构信息
				StringBuffer condition = new StringBuffer();
				String org = CntUtil.getCntOrgPk();
				condition.append(" pk_meeting = '"+pkMeeting+"' and acceptorg = '"+org+"'");
				
				ScapMeetingReportVO[] reportVOs = (ScapMeetingReportVO[]) ScapDAO.retrieveByCondition(ScapMeetingReportVO.class, condition.toString());
				Dataset reportDataSet = CpWinUtil.getDataset("scapco_meetingreport");
				if(reportVOs!=null&&reportVOs.length>0){
					row = new CpVOUtil<ScapMeetingReportVO>().getRow(reportDataSet, reportVOs[0]);
					if(row.getValue(reportDataSet.nameToIndex(ScapMeetingReportVO.REPORTPERSON))==null||"".equals(row.getValue(reportDataSet.nameToIndex(ScapMeetingReportVO.REPORTPERSON)))){						
						row.setValue(reportDataSet.nameToIndex(ScapMeetingReportVO.REPORTPERSON),CntUtil.getCntUserName());
					}
					reportDataSet.addRow(row);
				}
				reportDataSet.setRowSelectIndex(reportDataSet.getRowIndex(row));
				reportDataSet.setEnabled(false);
				
				//上报用户信息
				condition.delete(0, condition.length());
				condition.append(" pk_meetingreport in (select pk_meetingreport from scapco_meetingreport where pk_meeting = '"+pkMeeting+"' ");
				condition.append(" and acceptorg = '"+org+"')");
				ScapMeetingUserVO[] userVOs = (ScapMeetingUserVO[]) ScapDAO.retrieveByCondition(ScapMeetingUserVO.class, condition.toString());
				Dataset userDataSet = CpWinUtil.getDataset("scapco_meetinguser");
				if(userVOs!=null&&userVOs.length>0){
					for(ScapMeetingUserVO usertvo:userVOs){
						row = new CpVOUtil<ScapMeetingUserVO>().getRow(userDataSet, usertvo);
						userDataSet.addRow(row);
					}
				}
				//主表会议通知的数据
				ScapMeetingVO vo = (ScapMeetingVO) ScapDAO.retrieveByPK(ScapMeetingVO.class, pkMeeting);
				row = new CpVOUtil<ScapMeetingVO>().getRow(masterDs, vo);
			}
			masterDs.addRow(row);
			masterDs.setRowSelectIndex(masterDs.getRowIndex(row));
			masterDs.setEnabled(false);
		} else if ("feedback".equals(oper)){
			CpWinUtil.hideMenuItems(CpWinUtil.getView(), "menubar", new String[]{"userChoice"});
			GridComp userGrid = (GridComp) CpWinUtil.getComponent("scapco_meetinguser_grid");
			userGrid.setShowImageBtn(false);
			
			String pkMeeting = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPEN_BILL_ID);
			//切换卡片
			UICardLayout cardLayout = (UICardLayout) CpWinUtil.getAppCtx().getWindowContext().getViewContext("main").getUIMeta().findChildById("card2052");
			cardLayout.setCurrentItem("1");
			//通知机构信息
			StringBuffer condition = new StringBuffer();
			String org = CntUtil.getCntOrgPk();
			condition.append(" pk_meeting = '"+pkMeeting+"' and acceptorg = '"+org+"'");
			
			ScapMeetingReportVO[] reportVOs = (ScapMeetingReportVO[]) ScapDAO.retrieveByCondition(ScapMeetingReportVO.class, condition.toString());
			Dataset reportDataSet = CpWinUtil.getDataset("scapco_meetingreport");
			if(reportVOs!=null&&reportVOs.length>0){
				row = new CpVOUtil<ScapMeetingReportVO>().getRow(reportDataSet, reportVOs[0]);
				if(row.getValue(reportDataSet.nameToIndex(ScapMeetingReportVO.REPORTPERSON))==null||"".equals(row.getValue(reportDataSet.nameToIndex(ScapMeetingReportVO.REPORTPERSON)))){						
					row.setValue(reportDataSet.nameToIndex(ScapMeetingReportVO.REPORTPERSON),CntUtil.getCntUserName());
				}
				row.setValue(reportDataSet.nameToIndex(ScapMeetingReportVO.REPORTDATE),new UFDateTime());
				reportDataSet.addRow(row);
			}
			reportDataSet.setRowSelectIndex(reportDataSet.getRowIndex(row));
			reportDataSet.setEnabled(true);
			String isMeeting = reportDataSet.getSelectedRow().getString(reportDataSet.nameToIndex(ScapMeetingReportVO.ISMEETING));
			if(isMeeting != null && isMeeting.equals("1")) {
				userGrid.setShowImageBtn(true);
			}
			
			//上报用户信息
			condition.delete(0, condition.length());
			condition.append(" pk_meetingreport in (select pk_meetingreport from scapco_meetingreport where pk_meeting = '"+pkMeeting+"' ");
			condition.append(" and acceptorg = '"+org+"')");
			ScapMeetingUserVO[] userVOs = (ScapMeetingUserVO[]) ScapDAO.retrieveByCondition(ScapMeetingUserVO.class, condition.toString());
			Dataset userDataSet = CpWinUtil.getDataset("scapco_meetinguser");
			if(userVOs!=null&&userVOs.length>0){
				for(ScapMeetingUserVO usertvo:userVOs){
					row = new CpVOUtil<ScapMeetingUserVO>().getRow(userDataSet, usertvo);
					userDataSet.addRow(row);
				}
			}
			//主表会议通知的数据
			ScapMeetingVO vo = (ScapMeetingVO) ScapDAO.retrieveByPK(ScapMeetingVO.class, pkMeeting);
			row = new CpVOUtil<ScapMeetingVO>().getRow(masterDs, vo);
			masterDs.addRow(row);
			masterDs.setRowSelectIndex(masterDs.getRowIndex(row));
			masterDs.setEnabled(false);
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
    // 1是国资委用户，2为企业用户，3为中介机构用户
		int userType = CntUtil.CtnGwzOrCompanyOrPartnerUser();
		
		String oper = getOperator();
		//首先根据登录人的机构
		if(userType == 1){ //国资委新增
				//1.0   增加主表数据，下发会议通知
				Dataset masterDs = this.getMasterDs();
				ScapMeetingVO meetingVO = new CpVOUtil<ScapMeetingVO>().getVO(masterDs, masterDs.getSelectedRow(), new ScapMeetingVO());
				meetingVO.setState(MEETING_STATE_ADD);
				String pk_meeting = null;
				if(AppConsts.OPE_ADD.equals(oper)) {
					BaseDAO dao = new BaseDAO();
					pk_meeting = dao.insertVOWithPK(meetingVO);
				}else {
					pk_meeting = ScapDAO.insertOrUpdateVO(meetingVO);
					
				}
				masterDs.setEnabled(true);
				this.getCurrentAppCtx().closeWinDialog();
				//2.0 根据选择人员进行过滤去重机构数据，维护子表report 和staff
				Dataset staffDataSet = CpWinUtil.getDataset("scapco_meetingstaff");
				Row[] staffRows = staffDataSet.getAllRow();
				ScapMeetingStaffVO[] staffvos = new ScapMeetingStaffVO[staffRows.length];

				Map orgMap = new HashMap();
				if(staffvos!=null&&staffvos.length>0){
					for(int i = 0; i < staffvos.length; i++) {
						staffvos[i] = new CpVOUtil<ScapMeetingStaffVO>().getVO(staffDataSet, staffRows[i], new ScapMeetingStaffVO());
					}
				
				
					for(ScapMeetingStaffVO staffvo:staffvos){
						String pk_org = staffvo.getPk_org();
						if(!"".equals(staffvo.getPk_meetingreport())&&staffvo.getPk_meetingreport()!=null){
							orgMap.put(pk_org, staffvo.getPk_meetingreport());
						}
					}
					
					for(ScapMeetingStaffVO staffvo:staffvos){
						String pk_org = staffvo.getPk_org();
						String staff = staffvo.getStaff();
						if(!orgMap.containsKey(pk_org)){
							ScapMeetingReportVO meetingReport = new ScapMeetingReportVO();
							meetingReport.setPk_meeting(pk_meeting);
							meetingReport.setAcceptdate(new UFDateTime());
							meetingReport.setAcceptorg(pk_org);
							String pk_meetingreport = ScapDAO.insertOrUpdateVO(meetingReport);
							
							staffvo.setPk_meetingreport(pk_meetingreport);
							staffvo.setStaff(staff);
							ScapDAO.insertOrUpdateVO(staffvo);
							orgMap.put(pk_org, pk_meetingreport);
						}else {
							//查询report表主键
							String pk_meetingreport = (String) orgMap.get(pk_org);
							staffvo.setPk_meetingreport(pk_meetingreport);
							staffvo.setStaff(staff);
							ScapDAO.insertOrUpdateVO(staffvo);
						}
						
					}
				}
				
				//3.0  插入国资委自己填写的上报人员信息，pk_meetingreport为国资委机构的那一个report表主键
				String pk_meetingreport =(String) orgMap.get(CntUtil.getCntOrgPk());
				Dataset userDataSet = CpWinUtil.getDataset("scapco_meetinguser");
				Row[] userRows = userDataSet.getAllRow();
				if(userRows!=null&&userRows.length>0){
					ScapMeetingUserVO[] uservos = new ScapMeetingUserVO[userRows.length];
					for(int i = 0; i < userRows.length; i++) {
						uservos[i] = new CpVOUtil<ScapMeetingUserVO>().getVO(userDataSet, userRows[i], new ScapMeetingUserVO());
						uservos[i].setPk_meetingreport(pk_meetingreport);
					}
					ScapDAO.insertOrUpdateVOs(uservos);
				}
				
				Map<String, Object> paramMap = new HashMap<String, Object>(2);
				meetingVO = (ScapMeetingVO) ScapDAO.retrieveByPK(ScapMeetingVO.class, pk_meeting);
				Row savedRow = new CpVOUtil<ScapMeetingVO>().getRow(masterDs, meetingVO);
				paramMap.put(OPERATE_ROW, savedRow);
				CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(), PLUGOUT_ID, paramMap));
				
		//企业登录人员
		} else if (userType == 2) {
			//2.0  维护子表report
			ScapMeetingReportVO meetingReport = new ScapMeetingReportVO();
			Dataset reportDateSet = CpWinUtil.getDataset("scapco_meetingreport");
			Row reportRow = reportDateSet.getSelectedRow();
			meetingReport = new CpVOUtil<ScapMeetingReportVO>().getVO(reportDateSet, reportRow, new ScapMeetingReportVO());
			String pk_meetingreport = ScapDAO.insertOrUpdateVO(meetingReport);
			
			//3.0  插入填写的上报人员信息
			Dataset userDataSet = CpWinUtil.getDataset("scapco_meetinguser");
			Row[] userRows = userDataSet.getAllChangedRows();
			if(userRows!=null&&userRows.length>0){
				ScapMeetingUserVO[] uservos = new ScapMeetingUserVO[userRows.length];
				for(int i = 0; i < userRows.length; i++) {
					uservos[i] = new CpVOUtil<ScapMeetingUserVO>().getVO(userDataSet, userRows[i], new ScapMeetingUserVO());
					uservos[i].setPk_meetingreport(pk_meetingreport);
				}
				ScapDAO.insertOrUpdateVOs(uservos);
			}
			
			
			//1.0   主表数据更新状态
			Dataset masterDs = this.getMasterDs();
			ScapMeetingVO meetingVO = new CpVOUtil<ScapMeetingVO>().getVO(masterDs, masterDs.getSelectedRow(), new ScapMeetingVO());
			String pk_meeting = meetingVO.getPk_meeting();
			String condition = "pk_meeting = '"+pk_meeting+"' and ismeeting is null";
			ScapMeetingReportVO[] reportVOs= (ScapMeetingReportVO[]) ScapDAO.retrieveByCondition(ScapMeetingReportVO.class, condition);
			if(reportVOs==null||reportVOs.length==0){
				meetingVO.setState(MEETING_STATE_FEEDBACKED);
			}else {
				meetingVO.setState(MEETING_STATE_FEEDBACKING);
			}
			pk_meeting = ScapDAO.insertOrUpdateVO(meetingVO);
			this.getCurrentAppCtx().closeWinDialog();
			
			
			Map<String, Object> paramMap = new HashMap<String, Object>(2);
			meetingVO = (ScapMeetingVO) ScapDAO.retrieveByPK(ScapMeetingVO.class, pk_meeting);
			Row savedRow = new CpVOUtil<ScapMeetingVO>().getRow(masterDs, meetingVO);
			paramMap.put(OPERATE_ROW, savedRow);
			CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(), PLUGOUT_ID, paramMap));
		}
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
		String state = row.getString(ds.nameToIndex(ScapMeetingVO.STATE));
		Map<String, String> paramMap = null;
		if (primaryKeyValue != null){
			paramMap = new HashMap<String, String>(2);
			paramMap.put(PARAM_BILLITEM, primaryKeyValue);
			if(!state.equals(MEETING_STATE_ADD)) {
				paramMap.put("state", "8");
			}
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
				int pkOrgIndex = ds.nameToIndex("pk_org");
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
 * 子表删除 上报人员信息
 */
  public void onGridDeleteClick(  MouseEvent<?> mouseEvent){
    Dataset userDataSet = CpWinUtil.getDataset("scapco_meetinguser");
		Row row = userDataSet.getSelectedRow();
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		CmdInvoker.invoke(new UifLineDelCmd(dsId));
		
		ScapMeetingUserVO vo =  new CpVOUtil<ScapMeetingUserVO>().getVO(userDataSet, row, new ScapMeetingUserVO());
		if(vo!=null&&vo.getPk_meetinguser()!=null){
			ScapDAO.deleteByPk(ScapMeetingUserVO.class,vo.getPk_meetinguser());
		}
  }
  @Override protected String getMasterDsId(){
    return "scapco_meeting";
  }
  /** 
 * 子表删除通知人员
 * @param mouseEvent
 */
  public void ondeleteStaff(  MouseEvent mouseEvent){
    Dataset staffDataSet = CpWinUtil.getDataset("scapco_meetingstaff");
		Row row = staffDataSet.getSelectedRow();
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		CmdInvoker.invoke(new UifLineDelCmd(dsId));
		
		ScapMeetingStaffVO vo =  new CpVOUtil<ScapMeetingStaffVO>().getVO(staffDataSet, row, new ScapMeetingStaffVO());
		if(vo!=null&&vo.getId()!=null){
			ScapDAO.deleteByPk(ScapMeetingStaffVO.class,vo.getId());
			
			ScapMeetingStaffVO[] vos = (ScapMeetingStaffVO[]) ScapDAO.retrieveByCondition(ScapMeetingStaffVO.class, "pk_org ='"+vo.getPk_org()+"'");
			if(vos==null||vos.length==0){
				StringBuffer condition = new StringBuffer();
				condition.append(" pk_meetingreport = '"+vo.getPk_meetingreport()+"'");
				ScapMeetingUserVO[] userVOs = (ScapMeetingUserVO[]) ScapDAO.retrieveByCondition(ScapMeetingUserVO.class, condition.toString());
				if(userVOs!=null&&userVOs.length>0){
					for(ScapMeetingUserVO userVO:userVOs){
						ScapDAO.deleteByPk(ScapMeetingUserVO.class, userVO.getPk_meetinguser());
					}
				}
				
				ScapDAO.deleteByPk(ScapMeetingReportVO.class,vo.getPk_meetingreport());
				
			}
		}
  }
  public void userChoice(  Map keys){
    /**
		 * 构造通知人员信息
		 * 1 先取得目前已经存在的user 
		 * 2 将选择的user和存在的user在进行过滤
		 * 3如果是新增那就将国资委自己的信息构造出来
		 */
	  	Dataset staffDataSet = CpWinUtil.getDataset("scapco_meetingstaff");
		Row[] staffRows = staffDataSet.getAllRow();
		Map userMap = new HashMap();
		ScapMeetingStaffVO[] staffvos = new ScapMeetingStaffVO[staffRows.length];
		if (staffvos != null && staffvos.length > 0) {
			for (int i = 0; i < staffvos.length; i++) {
				staffvos[i] = new CpVOUtil<ScapMeetingStaffVO>().getVO(staffDataSet, staffRows[i], new ScapMeetingStaffVO());
				userMap.put(staffvos[i].getStaff(), staffvos[i].getStaff());
			}
		}
		
		ScapUserChoiceBVO[] vos = (ScapUserChoiceBVO[]) keys.get("users");
		ScapMeetingStaffVO staffVO = new ScapMeetingStaffVO();
		if (vos != null && vos.length > 0) {
			for (ScapUserChoiceBVO vo : vos) {
				if (userMap.containsKey(vo.getPk_user())) {
					continue;
				}
				staffVO.setStaff(vo.getPk_user());
				CpUserVO user = (CpUserVO) ScapDAO.retrieveByPK(CpUserVO.class,vo.getPk_user());
				staffVO.setPk_org(user.getPk_org());
				
				Row staffrow = new CpVOUtil<ScapMeetingStaffVO>().getRow(staffDataSet, staffVO);
				staffDataSet.addRow(staffrow);
			}
		}

//		String oper = getOperator();
//		if (AppConsts.OPE_ADD.equals(oper)) {
//			// 国资委自己
//			staffVO = new ScapMeetingStaffVO();
//			staffVO.setStaff(CntUtil.getCntUserPk());
//			staffVO.setPk_org(CntUtil.getCntOrgPk());
//			Row staffrow = new CpVOUtil<ScapMeetingStaffVO>().getRow(staffDataSet, staffVO);
//			staffDataSet.addRow(staffrow);
//		}
  }
  public void onUserChoice(  MouseEvent mouseEvent){
    //	  OpenProperties props = new OpenProperties(USERCHOICE_WIN_ID, USERCHOICE_WIN_TITLE);
//		this.getCurrentAppCtx().getCurrentWindowContext().getCurrentViewContext().navgateTo(props);
//		CpUIctrl.showView(USERCHOICE_WIN_ID,  USERCHOICE_WIN_TITLE, new HashMap<String, String>(){});
	  OpenProperties prop = new OpenProperties();
	  prop.setTitle("");
	  prop.setOpenId("userChoice");
	  prop.setWidth("1000");
	  prop.setHeight("500");
	  prop.setButtonZone(false);
	  AppUtil.addAppAttr("userChoiceType", "4");
	  AppLifeCycleContext.current().getWindowContext().popView(prop);
  }
  public void onAfterDataChange(  DatasetCellEvent datasetCellEvent){
    Dataset reportDateSet = CpWinUtil.getDataset("scapco_meetingreport");
	  Dataset userDateSet = CpWinUtil.getDataset("scapco_meetinguser");
	  GridComp userGrid = (GridComp) CpWinUtil.getComponent("scapco_meetinguser_grid");
	  
	  if(datasetCellEvent.getColIndex() == reportDateSet.nameToIndex(ScapMeetingReportVO.ISMEETING)) {
		  String ismeeting = (String) datasetCellEvent.getNewValue();
		  if("1".equals(ismeeting)){//是
				userGrid.setShowImageBtn(true);
		  }else {//否
			  userDateSet.clear();
			   userGrid.setShowImageBtn(false);
		  }
	  }
  }
  public void onAfterStaffRowSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
    Dataset uds = CpWinUtil.getDataset("scapco_meetinguser");
    uds.clear();
    Row row = ds.getSelectedRow();
    String pkReport = row.getString(ds.nameToIndex(ScapMeetingStaffVO.PK_MEETINGREPORT));
    if(pkReport != null && !"".equals(pkReport)) {
    	ScapMeetingUserVO[] uvos = (ScapMeetingUserVO[]) ScapDAO.retrieveByCondition(ScapMeetingUserVO.class, ScapMeetingUserVO.PK_MEETINGREPORT + " = '" + pkReport + "'");
    	if(uvos != null) {
    		for(ScapMeetingUserVO uvo : uvos) {
        		Row urow = new CpVOUtil<ScapMeetingUserVO>().getRow(uds, uvo);
        		uds.addRow(urow);
    		}
    	}
    }
  }
}
