package nc.scap.pub.officialdoc;
import nc.uap.lfw.core.cmd.UifAddCmd;
import nc.vo.pub.lang.UFDateTime;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.scap.pub.vos.ScapFeedBackUserVO;
import nc.uap.cpb.persist.dao.PtBaseDAO;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import nc.scap.pub.vos.ScapOfficialdocVO;
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
import nc.uap.lfw.core.model.PageModel;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpCtrlUtil;
import java.util.Map;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.jsp.uimeta.UIFlowvPanel;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.UifCopyCmd;
import nc.bs.dao.BaseDAO;
import java.util.HashMap;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.scap.pub.util.AttachAddPkUtil;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.scap.pub.vos.ScapFeedBackVO;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.Dataset;
import java.util.ArrayList;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.vo.pub.BusinessException;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.constants.AppConsts;
import uap.web.bd.pub.AppUtil;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.scap.pub.util.CpWinUtil;
import nc.scap.pub.vos.ScapUserChoiceBVO;
import uap.lfw.core.locator.ServiceLocator;
import nc.uap.lfw.core.event.MouseEvent;
/** 
 * 卡片窗口默认逻辑
 */
public class OfficialdocCardWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String PARAM_BILLITEM="billitem";
  private static final String PLUGOUT_ID="afterSavePlugout";
  private static final String OFFICIAL_STATE_ADD="新增";
  private static final String OFFICIAL_STATE_TOFEEDBACK="待反馈";
  private static final String OFFICIAL_STATE_FEEDBACKING="反馈中";
  private static final String OFFICIAL_STATE_FEEDBACKED="已反馈";
  private static final String OFFICIAL_STATE_NOFEEDBACK="无需反馈";
  /** 
 * 页面显示事件
 * @param dialogEvent
 */
  public void beforeShow(  DialogEvent dialogEvent){
    CpWinUtil.hideMenuItems(CpWinUtil.getView(), "menubar", new String[]{"add","copy","del","startorstrop","print"});
		Dataset masterDs = this.getMasterDs();
		masterDs.clear();
		String oper = getOperator();
		Row row = masterDs.getEmptyRow();
		
		GridComp grid = (GridComp) CpWinUtil.getComponent(CpWinUtil.getView(), "scapco_feedbackuser_grid");
		
		int userType = CntUtil.CtnGwzOrCompanyOrPartnerUser();
		
//		MenubarComp menuBar = grid.getMenuBar();
//	    menuBar.setEnabled(false);
		
		if(AppConsts.OPE_ADD.equals(oper)){
			CpUserVO user = CntUtil.getCntUserVo();
			row.setValue(masterDs.nameToIndex(ScapOfficialdocVO.SENDDOCORG), user.getPk_org());
			row.setValue(masterDs.nameToIndex(ScapOfficialdocVO.SENDDOCDATE), new UFDateTime());
			row.setValue(masterDs.nameToIndex(ScapOfficialdocVO.ISFEEDBACK), 2);
			row.setValue(masterDs.nameToIndex(ScapOfficialdocVO.ISMESSAGE), 2);
			row.setValue(masterDs.nameToIndex(ScapOfficialdocVO.STATE), OFFICIAL_STATE_ADD);
			masterDs.addRow(row);
			masterDs.setRowSelectIndex(masterDs.getRowIndex(row));
			masterDs.setEnabled(true);
			
			UIFlowvPanel panel =  (UIFlowvPanel) CpWinUtil.getWinCtx().getViewContext("main").getUIMeta().findChildById("scapco_feedback_flowvpane2");
			panel.setVisible(false);
			
			String pkid = generatePk();
			row.setValue(masterDs.nameToIndex(masterDs.getPrimaryKeyField()), pkid);
			if(userType!=1){
				CpCtrlUtil.setFormEditCol(masterDs, false, new String[] {ScapOfficialdocVO.ISFEEDBACK }, "scapco_officialdoc_form");
			}
			//设置附件 主键
//			AttachAddPkUtil.setBillAttachPK(masterDs, pkid);
			
			grid.setShowImageBtn(true);
		}else if(AppConsts.OPE_EDIT.equals(oper)){
			String pk_officialdoc = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPEN_BILL_ID);
			
			UIFlowvPanel panel =  (UIFlowvPanel) CpWinUtil.getWinCtx().getViewContext("main").getUIMeta().findChildById("scapco_feedback_flowvpane2");
			panel.setVisible(false);
			
			StringBuffer condition = new StringBuffer();
			condition.append(" pk_officialdoc = '"+pk_officialdoc+"' ");
			ScapFeedBackUserVO[] staffVOs = (ScapFeedBackUserVO[]) ScapDAO.retrieveByCondition(ScapFeedBackUserVO.class, condition.toString());
			Dataset staffDataSet = CpWinUtil.getDataset("scapco_feedbackuser");
			if(staffVOs!=null&&staffVOs.length>0){
				for(ScapFeedBackUserVO staffvo:staffVOs){
					row = new CpVOUtil<ScapFeedBackUserVO>().getRow(staffDataSet, staffvo);
					staffDataSet.addRow(row);
				}
			}
			
			ScapOfficialdocVO vo = (ScapOfficialdocVO) ScapDAO.retrieveByPK(ScapOfficialdocVO.class, pk_officialdoc);
			row = new CpVOUtil<ScapOfficialdocVO>().getRow(masterDs, vo);
			masterDs.addRow(row);
			masterDs.setRowSelectIndex(masterDs.getRowIndex(row));
			masterDs.setEnabled(true);
			
			grid.setShowImageBtn(true);
		}else if ("showdetail".equals(oper)){
			CpWinUtil.hideMenuItems(CpWinUtil.getView(), "menubar", new String[]{"save","userChoice"});
			String pk_officialdoc = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPEN_BILL_ID);
			ScapOfficialdocVO vo = (ScapOfficialdocVO) ScapDAO.retrieveByPK(ScapOfficialdocVO.class, pk_officialdoc);
			row = new CpVOUtil<ScapOfficialdocVO>().getRow(masterDs, vo);
			masterDs.addRow(row);
			masterDs.setRowSelectIndex(masterDs.getRowIndex(row));
			masterDs.setEnabled(false);
			grid.setShowImageBtn(false);
			
			String cntorg = CntUtil.getCntOrgPk();
			String sendorg = vo.getSenddocorg();
			StringBuffer condition = new StringBuffer();
			if(cntorg.equals(sendorg)){
				condition.append(" pk_officialdoc = '"+pk_officialdoc+"' ");
				ScapFeedBackUserVO[] staffVOs = (ScapFeedBackUserVO[]) ScapDAO.retrieveByCondition(ScapFeedBackUserVO.class, condition.toString());
				Dataset staffDataSet = CpWinUtil.getDataset("scapco_feedbackuser");
				if(staffVOs!=null&&staffVOs.length>0){
					for(ScapFeedBackUserVO staffvo:staffVOs){
						row = new CpVOUtil<ScapFeedBackUserVO>().getRow(staffDataSet, staffvo);
						staffDataSet.addRow(row);
					}
				}
			}else {
				condition.delete(0, condition.length());
				condition.append(" pk_officialdoc = '"+pk_officialdoc+"' and feedbackorg ='"+CntUtil.getCntOrgPk()+"'");
				ScapFeedBackVO [] feedbackVOs = (ScapFeedBackVO[]) ScapDAO.retrieveByCondition(ScapFeedBackVO.class, condition.toString());
				Dataset feedbackDataSet = CpWinUtil.getDataset("scapco_feedback");
				feedbackDataSet.clear();
				if(feedbackVOs!=null&&feedbackVOs.length>0){
					for(ScapFeedBackVO feedbackVO:feedbackVOs){
						row = new CpVOUtil<ScapFeedBackVO>().getRow(feedbackDataSet, feedbackVO);
						feedbackDataSet.addRow(row);
						feedbackDataSet.setRowSelectIndex(feedbackDataSet.getRowIndex(row));
					}
				}
				feedbackDataSet.setEnabled(false);
			}
		}else if ("FeedBack".equals(oper)){
			CpWinUtil.hideMenuItems(CpWinUtil.getView(), "menubar", new String[]{"userChoice"});
			String pk_officialdoc = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPEN_BILL_ID);
			ScapOfficialdocVO vo = (ScapOfficialdocVO) ScapDAO.retrieveByPK(ScapOfficialdocVO.class, pk_officialdoc);
			row = new CpVOUtil<ScapOfficialdocVO>().getRow(masterDs, vo);
			masterDs.addRow(row);
			masterDs.setRowSelectIndex(masterDs.getRowIndex(row));
			masterDs.setEnabled(false);
			
			UIFlowvPanel panel =  (UIFlowvPanel) CpWinUtil.getWinCtx().getViewContext("main").getUIMeta().findChildById("scapco_feedbackuser_flowvpane2");
			panel.setVisible(false);
			
			StringBuffer condition = new StringBuffer();
			condition.append(" pk_officialdoc = '"+pk_officialdoc+"' and staff = '"+CntUtil.getCntUserPk()+"'");
			ScapFeedBackUserVO[] feedbackUserVOs = (ScapFeedBackUserVO[]) ScapDAO.retrieveByCondition(ScapFeedBackUserVO.class, condition.toString());
			Dataset feedbackDataSet = CpWinUtil.getDataset("scapco_feedbackuser");
			if(feedbackUserVOs!=null&&feedbackUserVOs.length>0){
					row = new CpVOUtil<ScapFeedBackUserVO>().getRow(feedbackDataSet, feedbackUserVOs[0]);
					row.setValue(feedbackDataSet.nameToIndex(ScapFeedBackUserVO.VBDEF2), new UFDateTime().toString());
					feedbackDataSet.addRow(row);
					feedbackDataSet.setRowSelectIndex(feedbackDataSet.getRowIndex(row));
					feedbackDataSet.setEnabled(true);
			}
			grid.setShowImageBtn(false);
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
    Dataset masterDs = this.getMasterDs();
	  Row row = masterDs.getSelectedRow();
	  String oper = getOperator();
	  if(AppConsts.OPE_ADD.equals(oper)||AppConsts.OPE_EDIT.equals(oper)){
		  //1.0   增加主表数据，下发会议通知
			ScapOfficialdocVO officialdocVO = new CpVOUtil<ScapOfficialdocVO>().getVO(masterDs,row, new ScapOfficialdocVO());
			officialdocVO.setState(OFFICIAL_STATE_ADD);
			String pk_officialdoc = null;
			if(AppConsts.OPE_ADD.equals(oper)) {
				String userPk = CntUtil.getCntUserPk();
				officialdocVO.setVbdef1(userPk);
				BaseDAO dao = new BaseDAO();
				pk_officialdoc = dao.insertVOWithPK(officialdocVO);
			}else {
				pk_officialdoc = ScapDAO.insertOrUpdateVO(officialdocVO);
				
			}
			this.getCurrentAppCtx().closeWinDialog();
			masterDs.setEnabled(true);
			
			//2.0 根据选择人员进行过滤去重机构数据，维护子表feedback 和staff
			Dataset staffDataSet = CpWinUtil.getDataset("scapco_feedbackuser");
			Row[] staffRows = staffDataSet.getAllRow();
			ScapFeedBackUserVO[] staffvos = new ScapFeedBackUserVO[staffRows.length];

			Map orgMap = new HashMap();
			if(staffvos!=null&&staffvos.length>0){
				for(int i = 0; i < staffvos.length; i++) {
					staffvos[i] = new CpVOUtil<ScapFeedBackUserVO>().getVO(staffDataSet, staffRows[i], new ScapFeedBackUserVO());
				}
			
				for(ScapFeedBackUserVO staffvo:staffvos){
					String pk_org = staffvo.getPk_org();
					if(!"".equals(staffvo.getPk_officialdoc())&&staffvo.getPk_officialdoc()!=null){
						orgMap.put(pk_org, staffvo.getPk_officialdoc());
					}
				}
				
				for(ScapFeedBackUserVO staffvo:staffvos){
					String pk_org = staffvo.getPk_org();
					String staff = staffvo.getStaff();
					if(!orgMap.containsKey(pk_org)){
						ScapFeedBackVO feedBack = new ScapFeedBackVO();
						feedBack.setPk_officialdoc(pk_officialdoc);
						feedBack.setFeedbackorg(pk_org);
						String pk_feedback = ScapDAO.insertOrUpdateVO(feedBack);
						
						staffvo.setPk_officialdoc(pk_officialdoc);
						staffvo.setStaff(staff);
						ScapDAO.insertOrUpdateVO(staffvo);
						orgMap.put(pk_org, pk_officialdoc);
					}else {
						//查询report表主键
						staffvo.setPk_officialdoc(pk_officialdoc);
						staffvo.setStaff(staff);
						ScapDAO.insertOrUpdateVO(staffvo);
					}
					
				}
			}
			
			Map<String, Object> paramMap = new HashMap<String, Object>(2);
//			officialdocVO = (ScapOfficialdocVO) ScapDAO.retrieveByPK(ScapOfficialdocVO.class, pk_officialdoc);
			Row savedRow = new CpVOUtil<ScapOfficialdocVO>().getRow(masterDs, officialdocVO);
			 paramMap.put(OPERATE_ROW, savedRow);
			 CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(), PLUGOUT_ID, paramMap));
			 
	  }else if("FeedBack".equals(oper)){
		  //3 反馈信息填写
		  Dataset feedbackUserDataSet = CpWinUtil.getDataset("scapco_feedbackuser");
		  row = feedbackUserDataSet.getSelectedRow();
		  ScapFeedBackUserVO feedbackUserVo = new CpVOUtil<ScapFeedBackUserVO>().getVO(feedbackUserDataSet,row, new ScapFeedBackUserVO());
		  String pk_feedbackuser = ScapDAO.insertOrUpdateVO(feedbackUserVo);
		  this.getCurrentAppCtx().closeWinDialog();
		  feedbackUserDataSet.setEnabled(true);
		  
		  row = masterDs.getSelectedRow();
		  ScapOfficialdocVO officialdocVO = new CpVOUtil<ScapOfficialdocVO>().getVO(masterDs,row, new ScapOfficialdocVO());
		  String condition = "pk_officialdoc = '"+officialdocVO.getPk_officialdoc()+"' and feedbackdate is null";
		  ScapFeedBackVO[] reportVOs= (ScapFeedBackVO[]) ScapDAO.retrieveByCondition(ScapFeedBackVO.class, condition);
		  if(reportVOs==null||reportVOs.length==0){
			  officialdocVO.setState(OFFICIAL_STATE_FEEDBACKED);
		  }else {
			  officialdocVO.setState(OFFICIAL_STATE_FEEDBACKING);
		  }
		  
		  String pk_officialdoc = ScapDAO.insertOrUpdateVO(officialdocVO);
		  this.getCurrentAppCtx().closeWinDialog();
		  masterDs.setEnabled(false);

		  Map<String, Object> paramMap = new HashMap<String, Object>(2);
		  officialdocVO = (ScapOfficialdocVO) ScapDAO.retrieveByPK(ScapOfficialdocVO.class, pk_officialdoc);
		  Row savedRow = new CpVOUtil<ScapOfficialdocVO>().getRow(masterDs, officialdocVO);
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
		String state = row.getString(ds.nameToIndex(ScapOfficialdocVO.STATE));
		Map<String, String> paramMap = null;
		if (primaryKeyValue != null){
			paramMap = new HashMap<String, String>(2);
			paramMap.put(PARAM_BILLITEM, primaryKeyValue);
			if(!state.equals(OFFICIAL_STATE_ADD)) {
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
 * 子表删除
 */
  public void onGridDeleteClick(  MouseEvent<?> mouseEvent){
    GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		CmdInvoker.invoke(new UifLineDelCmd(dsId));
  }
  @Override protected String getMasterDsId(){
    return "scapco_officialdoc";
  }
  public void onDataLoad_scapco_officialdoc(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
	  	CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
  public void onUserChoice(  MouseEvent mouseEvent){
    OpenProperties prop = new OpenProperties();
	  prop.setTitle("");
	  prop.setOpenId("userChoice");
	  prop.setWidth("1000");
	  prop.setHeight("500");
	  prop.setButtonZone(false);
	  int userType = CntUtil.CtnGwzOrCompanyOrPartnerUser();
	  if (userType==1){//公文下发			  
		  AppUtil.addAppAttr("userChoiceType", "3");
	  }else if (userType==2){//公文上报	
		  AppUtil.addAppAttr("userChoiceType", "2");
	  }
	  
	  LfwRuntimeEnvironment.getWebContext().getWebSession().getAttribute(PageModel.PK_TEMPLATE_DB);
	  AppLifeCycleContext.current().getWindowContext().popView(prop);
  }
  public void userChoice(  Map keys){
    /**
	 * 构造通知人员信息
	 * 1 先取得目前已经存在的user 
	 * 2 将选择的user和存在的user在进行过滤
	 */
  	Dataset staffDataSet = CpWinUtil.getDataset("scapco_feedbackuser");
	Row[] staffRows = staffDataSet.getAllRow();
	Map userMap = new HashMap();
	ScapFeedBackUserVO[] staffvos = new ScapFeedBackUserVO[staffRows.length];
	if (staffvos != null && staffvos.length > 0) {
		for (int i = 0; i < staffvos.length; i++) {
			staffvos[i] = new CpVOUtil<ScapFeedBackUserVO>().getVO(staffDataSet, staffRows[i], new ScapFeedBackUserVO());
			userMap.put(staffvos[i].getStaff(), staffvos[i].getStaff());
		}
	}
	
	ScapUserChoiceBVO[] vos = (ScapUserChoiceBVO[]) keys.get("users");
	ScapFeedBackUserVO staffVO = new ScapFeedBackUserVO();
	if (vos != null && vos.length > 0) {
		for (ScapUserChoiceBVO vo : vos) {
			if (userMap.containsKey(vo.getPk_user())) {
				continue;
			}
			staffVO.setStaff(vo.getPk_user());
			CpUserVO user = (CpUserVO) ScapDAO.retrieveByPK(CpUserVO.class,vo.getPk_user());
			staffVO.setPk_org(user.getPk_org());
			
			Row staffrow = new CpVOUtil<ScapFeedBackUserVO>().getRow(staffDataSet, staffVO);
			staffDataSet.addRow(staffrow);
		}
	}
  }
  public void onDeleteUser(  MouseEvent mouseEvent){
    Dataset staffDataSet = CpWinUtil.getDataset("scapco_feedbackuser");
	  Row row = staffDataSet.getSelectedRow();
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		CmdInvoker.invoke(new UifLineDelCmd(dsId));
  }
  public void onAfterStaffRowSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
    String pkOrg = ds.getSelectedRow().getString(ds.nameToIndex(ScapFeedBackUserVO.PK_ORG));
    String pkOfficialdoc = ds.getSelectedRow().getString(ds.nameToIndex(ScapFeedBackUserVO.PK_OFFICIALDOC));
    String condition = "feedbackorg = '" + pkOrg + "' and pk_officialdoc = '" + pkOfficialdoc + "'";
    ScapFeedBackVO[] fbvos = (ScapFeedBackVO[]) ScapDAO.retrieveByCondition(ScapFeedBackVO.class, condition);
    if(fbvos != null && fbvos.length > 0) {
    	ScapFeedBackVO fbvo = fbvos[0];
    	Dataset fbdataset = CpWinUtil.getDataset("scapco_feedback");
    	fbdataset.clear();
    	Row row = new CpVOUtil<ScapFeedBackVO>().getRowWithRefshData(fbdataset, fbvo);
    	fbdataset.setRowSelectIndex(fbdataset.getRowIndex(row));
    }
  }
}
