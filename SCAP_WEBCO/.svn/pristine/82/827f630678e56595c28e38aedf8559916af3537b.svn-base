package nc.scap.pub.workreport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.scap.def.util.ScapDefUtil;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.model.ScapCoConstants;
import nc.scap.pub.notice.util.NoticeUtil;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.pub.util.CpWinUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.WorkReportVO;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.cpb.baseservice.IUifCpbService;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifAddCmd;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.cmd.UifCopyCmd;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import nc.uap.lfw.core.cmd.UifLineDelCmd;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.cmd.UifSaveCmdRV;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DatasetCellEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.file.FillFileInfoHelper;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwView;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scap.work_report_type.Work_report_type;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.core.locator.ServiceLocator;
import uap.web.bd.pub.AppUtil;
/** 
 * 卡片窗口默认逻辑
 */
public class WorkreportCardWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final String PARAM_BILLITEM="billitem";
  private static final String PLUGOUT_ID="afterSavePlugout";
  /** 
 * 页面显示事件
 * @param dialogEvent
 */
  public void beforeShow(  DialogEvent dialogEvent){
    Dataset masterDs = this.getMasterDs();
		masterDs.clear();
		
		String oper = getOperator();
		
		String methodType = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(IGlobalConstants.METHOD_TYPE);
                if(methodType == null){
                        methodType = (String)AppLifeCycleContext.current().getApplicationContext().getAppAttribute(IGlobalConstants.METHOD_TYPE);
                }
                final String method = methodType;
                
		if(AppConsts.OPE_ADD.equals(oper)){
			CmdInvoker.invoke(new UifAddCmd(getMasterDsId()) {
				protected void onBeforeRowAdd(Row row) {
					setAutoFillValue(row);
					
					Dataset masterDs = getMasterDs();
					
					init(method, masterDs, row);// 初始化界面
					
					String pk_primarykey = generatePk();
					row.setValue(masterDs.nameToIndex(masterDs.getPrimaryKeyField()), pk_primarykey);
					FillFileInfoHelper.resetItem(pk_primarykey);
					FillFileInfoHelper.fillFileInfo(masterDs, row);
				}
			});
		}else if(AppConsts.OPE_EDIT.equals(oper)){
			CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs){
				@Override
				protected void onAfterDatasetLoad() {
					this.getDs().setEnabled(true);
					
					init(method, this.getDs(), this.getDs().getSelectedRow());// 初始化界面
					
					String primaryKey = this.getDs().getPrimaryKeyField();
					if (primaryKey == null) {
						throw new LfwRuntimeException("当前Dataset没有设置主键!");
					}
					String primaryKeyValue = (String) this.getDs().getSelectedRow().getValue(this.getDs().nameToIndex(primaryKey));					
					FillFileInfoHelper.resetItem(primaryKeyValue);
				}
			});
		}
  }
  /** 
 * 主数据选中逻辑
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent dsEvent){
    Dataset ds = dsEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
		
		setReportType();
		
		String methodType = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(IGlobalConstants.METHOD_TYPE);

                if (ScapCoConstants.LOOK.equals(methodType) || ScapCoConstants.APPROVE.equals(methodType) || ScapCoConstants.REAPPROVE.equals(methodType)) {
                        CpWinUtil
                        .refshAttachList("main", "attachout",
                                        ds.getValue(ds.getPrimaryKeyField())
                                                        .toString(), false, true, false);
                } else {
                        CpWinUtil
                        .refshAttachList("main", "attachout",
                                        ds.getValue(ds.getPrimaryKeyField())
                                                        .toString(), true, true, true);
                }
  }
  /** 
 * 初始化界面
 * @param operation
 */
  public void init(  String operation,  Dataset masterDs,  Row row){
    if (ScapCoConstants.ADD.equals(operation)) {
                        initData(masterDs, row);
                } else if (ScapCoConstants.EDIT.equals(operation)) {
                } else if (ScapCoConstants.APPROVE.equals(operation)
                                || ScapCoConstants.REAPPROVE.equals(operation)) {
                        initApproveInfo(masterDs, row);
                } else if (ScapCoConstants.LOOK.equals(operation)) {
                    masterDs.setEnabled(false);
                }
  }
  /** 
 * 新增初始化
 * @param masterDs
 * @param row
 */
  public void initData(  Dataset ds,  Row row){
    UFDate dt = new UFDate();
                int year = dt.getLocalYear();
                int month = dt.getLocalMonth();
                int day = dt.getLocalDay();
//              String org = LfwRuntimeEnvironment.getLfwSessionBean().getUser_org();
//              String org = CntUtil.getCntOrgName();
//              String title = "" + year + (month < 10 ? "0" + month : month) + (day < 10 ? "0" + day : day) + org + TITLE_TXT;
                String orgName = CntUtil.getCntOrgName();
                CpOrgVO cpOrgVO = CntUtil.getCntOrgVo();
                String orgShortName = cpOrgVO.getDef11();
                String business_type = (String)AppUtil.getAppAttr(ScapCoConstants.BUSINESS_TYPE);
                row.setValue(ds.nameToIndex(WorkReportVO.BUSINESS_TYPE), business_type);
//                row.setValue(ds.nameToIndex(WorkReportVO.PK_ORG), CntUtil.getCntOrgPk());
                row.setValue(ds.nameToIndex(WorkReportVO.INPUT_USER), CntUtil.getCntUserPk());
                row.setValue(ds.nameToIndex(WorkReportVO.INPUT_DATE), dt);
                row.setValue(ds.nameToIndex(WorkReportVO.YEAR), String.valueOf(new UFDate().getYear()));
//                row.setValue(ds.nameToIndex(WorkReportVO.MONTH), (month < 10 ? "0" + month : String.valueOf(month)));
                row.setValue(ds.nameToIndex(WorkReportVO.MONTH), String.valueOf(month));
                row.setValue(ds.nameToIndex(WorkReportVO.STATE), "0");
                row.setValue(ds.nameToIndex(WorkReportVO.HANDLE_STATE), "0");
                // PK_PARENT
                CpOrgVO orgVo = CpOrgUtil.getOrgVoByPk(CntUtil.getCntOrgPk());
                row.setValue(ds.nameToIndex(WorkReportVO.PK_PARENT), orgVo.getPk_fatherorg());
                
                String reportType = (String) AppUtil.getAppAttr("report_type");
        		row.setValue(ds.nameToIndex(WorkReportVO.REPORT_TYPE), reportType);
  }
  /** 
 * 审核人信息初始化
 * @param masterDs
 * @param row
 */
  public void initApproveInfo(  Dataset masterDs,  Row row){
    row.setValue(masterDs.nameToIndex(WorkReportVO.APPROVE_DATE),
                                new UFDate());
                row.setValue(masterDs.nameToIndex(WorkReportVO.APPROVER),
                                CntUtil.getCntUserPk());
  }
  /** 
 * 保存
 * @param datasetEvent
 */
  public void onSave(  MouseEvent<?> mouseEvent) throws BusinessException {
    //		Dataset masterDs = this.getMasterDs();
//		CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this.getDetailDsIds(), false));
//		masterDs.setEnabled(true);
//		this.getCurrentAppCtx().closeWinDialog();
//		
//		CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(), PLUGOUT_ID));
	    realSave(ScapCoConstants.SAVE);
  }
  /** 
 * 数据检验
 * @param aggVo
 * @return
 */
  public String checkData(  SuperVO aggVo){
    StringBuffer sb = new StringBuffer("");
                // TODO:数据检验处理
              WorkReportVO aggvo = (WorkReportVO) aggVo;
//              String state = aggvo.getState();
//              String handleState = aggvo.getHandle_state();
              
              String pkNotice = aggvo.getPk_notice();
              String primaryKeyValue = aggvo.getPrimaryKey();
              String year = aggvo.getYear();
              String month = aggvo.getMonth();
              String businessType = aggvo.getBusiness_type();
              String reportType = aggvo.getReport_type();
              String pkOrg = aggvo.getPk_org();
              
              String withNotice = (String)AppUtil.getAppAttr("withNotice");
              
//              FormComp fc = (FormComp) this.getCurrentView().getViewComponents().getComponent("workreport_form");
//              FormElement fe = null;
//              if (fc != null) {
//                  fe = fc.getElementById("pk_notice_notice_title");
//              }
              
              String provinceId = ScapSysinitUtil
      				.getSysinitStrByCode(IGlobalConstants.PROVINCE_ID);
              
              if (!IGlobalConstants.FJ.equals(provinceId) && "0".equals(withNotice) && (pkNotice == null || pkNotice.length() == 0)) {
                  sb.append("通知未设置!");
              }
              else if ("0".equals(withNotice) && pkNotice != null && pkNotice.length() != 0) {
                  
                  String condition = "select * from scapco_workreport t where t." + aggvo.getPKFieldName() + "<>'" + primaryKeyValue + "' and t.year='" + year +"'" + " and t.month='" + String.valueOf(month) +"'";
                  condition += " and t." + WorkReportVO.PK_ORG + "='" + pkOrg + "' ";
                  condition += " and t." + WorkReportVO.BUSINESS_TYPE + "='" + businessType + "' ";
                  condition += " and t." + WorkReportVO.REPORT_TYPE + "='" + reportType + "' ";
                  condition += " and t." + WorkReportVO.PK_NOTICE + "='" + pkNotice + "' ";
                  List<?> results = ScapDAO.queryVOs(WorkReportVO.class, condition);
                  if(!results.isEmpty()) {
                      condition = "select report_type from scapco_work_reportType where " + Work_report_type.PK_WORK_REPORT_TYPE + " = '" + reportType + "'";
                      String report_type = (String)ScapDAO.executeQuery(condition, new ColumnProcessor());
                      
                      condition = "select notice_no from scapco_notice_manager where pk_notice = '" + pkNotice + "'";
                      String noticeNo = (String)ScapDAO.executeQuery(condition, new ColumnProcessor());
                      
                      String pk_defdoc = null;
                      String defCode = "scapco_0003";
                      String businessTypeName = null;
                      DefdocVO[] vos = ScapDefUtil.getDefDocVosByCode(defCode);
                      if (vos != null && vos.length != 0) {
                          /*
                           * vos[i].getName(); 
                           * vos[i].getPk_defdoc();
                           * vos[i].getCode();
                           */
                          String itemValue = null;
                          String code = null;
                          for (int i = 0; i < vos.length; i++) {
                              pk_defdoc = vos[i].getPk_defdoc();
                              code = vos[i].getCode();
                              if (pk_defdoc != null && pk_defdoc.equals(businessType)) {
                                  // 取得定义值
                                  businessTypeName = vos[i].getName();
                              }
                          }
                      }
                      
                      sb.append(year + "年" + String.valueOf(month) + "月的\"" + businessTypeName + "\"的\"" + report_type + "\"的关联通知\"" + noticeNo + "\"报告已存在!");
                  }
              }
              else if (!"0".equals(withNotice)) {
                  
                  String condition = "select * from scapco_workreport t where t." + aggvo.getPKFieldName() + "<>'" + primaryKeyValue + "' and t.year='" + year +"'" + " and t.month='" + String.valueOf(month) +"'";
                  condition += " and t." + WorkReportVO.PK_ORG + "='" + pkOrg + "' ";
                  condition += " and t." + WorkReportVO.BUSINESS_TYPE + "='" + businessType + "' ";
                  condition += " and t." + WorkReportVO.REPORT_TYPE + "='" + reportType + "' ";
//                  condition += " and t." + WorkReportVO.PK_NOTICE + "='" + pkNotice + "' ";
                  List<?> results = ScapDAO.queryVOs(WorkReportVO.class, condition);
                  if(!results.isEmpty()) {
                      condition = "select report_type from scapco_work_reportType where " + Work_report_type.PK_WORK_REPORT_TYPE + " = '" + reportType + "'";
                      String report_type = (String)ScapDAO.executeQuery(condition, new ColumnProcessor());
                      
//                      condition = "select notice_no from scapco_notice_manager where pk_notice = '" + pkNotice + "'";
//                      String noticeNo = (String)ScapDAO.executeQuery(condition, new ColumnProcessor());
                      
                      String pk_defdoc = null;
                      String defCode = "scapco_0003";
                      String businessTypeName = null;
                      DefdocVO[] vos = ScapDefUtil.getDefDocVosByCode(defCode);
                      if (vos != null && vos.length != 0) {
                          /*
                           * vos[i].getName(); 
                           * vos[i].getPk_defdoc();
                           * vos[i].getCode();
                           */
                          String itemValue = null;
                          String code = null;
                          for (int i = 0; i < vos.length; i++) {
                              pk_defdoc = vos[i].getPk_defdoc();
                              code = vos[i].getCode();
                              if (pk_defdoc != null && pk_defdoc.equals(businessType)) {
                                  // 取得定义值
                                  businessTypeName = vos[i].getName();
                              }
                          }
                      }
                      
                      sb.append(year + "年" + String.valueOf(month) + "月的\"" + businessTypeName + "\"的\"" + report_type + "\"的报告已存在!");
                  }
              }

                return sb.toString();
  }
  /** 
 * 检验通过处理
 * @param aggVo
 */
  public void checkPassDeal(  SuperVO aggVo,  String operation){
    WorkReportVO aggvo = (WorkReportVO) aggVo;
                if (ScapCoConstants.SAVE.equals(operation)) {
                        aggvo.setModifiedtime(new UFDateTime());
                        aggvo.setModifier(CntUtil.getCntUserPk());
                } else if (ScapCoConstants.SUBMIT.equals(operation)) {
                        aggvo.setState("1");// 状态　已提交
                        aggvo.setModifiedtime(new UFDateTime());
                        aggvo.setModifier(CntUtil.getCntUserPk());
                        aggvo.setSubmit_date(new UFDateTime());
                } else if (ScapCoConstants.RECEIVE.equals(operation)) {
                        aggvo.setState("3");// 状态　已接收
                        aggvo.setHandle_state("1");// 办理情况　已办
                        aggvo.setApprove_date(new UFDate());
                } else if (ScapCoConstants.ROLLBACK.equals(operation)) {
                        aggvo.setState("2");// 状态　已退回
                        aggvo.setHandle_state("0");// 办理情况　待办
                }
  }
  public void realSave(  final String operation){
      Dataset masterDs = this.getMasterDs();
      LfwView attachlist = AppLifeCycleContext.current().getViewContext().getWindowContext().getViewContext("attachlist").getView();
      Dataset attachds = attachlist.getViewModels().getDataset("attach_ds");
      int count = attachds.getCurrentRowCount();
      Row row = masterDs.getSelectedRow();
      String state = (String)row.getValue(masterDs.nameToIndex("state"));
      if (count == 0) {
          if ((("0".equals(state) || "2".equals(state) ) && ScapCoConstants.SAVE.equals(operation) || ScapCoConstants.SUBMIT.equals(operation)) && !AppInteractionUtil.showConfirmDialog("提示", "附件未添加！是否继续？")) {
              return;
          }
      }
      // 通知表状态会写
		String pk_notice = row.getString(masterDs.nameToIndex(WorkReportVO.PK_NOTICE));
		String pk_user = row.getString(masterDs.nameToIndex(WorkReportVO.INPUT_USER));
		if (pk_notice != null && StringUtils.isNotEmpty(pk_notice)) {			
		 if (ScapCoConstants.SAVE.equals(operation)) {
			NoticeUtil.noticeWriteBack(pk_notice,pk_user,IGlobalConstants.REPORT_STATUS_SAVE);
			} 
			else if (ScapCoConstants.SUBMIT.equals(operation)) {
	    	  NoticeUtil.noticeWriteBack(pk_notice,pk_user,IGlobalConstants.REPORT_STATUS_COMMIT);
	      }else if(ScapCoConstants.RECEIVE.equals(operation)) {
	    	  NoticeUtil.noticeWriteBack(pk_notice,pk_user,IGlobalConstants.REPORT_STATUS_RECEIVED);
	      } else if (ScapCoConstants.ROLLBACK.equals(operation)) {
	    	  NoticeUtil.noticeWriteBack(pk_notice,pk_user,IGlobalConstants.REPORT_STATUS_RETURN);
	      }
		}

    
                        CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this.getDetailDsIds(), false) {

                                protected void onVoSave(SuperVO aggVo) {
                                        try {
                                                String errmsg = checkData(aggVo);// 获取错误信息
                                                if (!errmsg.equals("")) {
                                                        throw new LfwRuntimeException(errmsg).setTitle("提示");
                                                }
                                                checkPassDeal(aggVo, operation);// 检验通过后处理逻辑
                                                IUifCpbService cpbService = NCLocator.getInstance().lookup(
                                                                IUifCpbService.class);
                                                cpbService.insertOrUpdateSuperVO(aggVo,
                                                                this.isNotifyBDCache());
                                        } catch (BusinessException e) {
                                                Logger.error(e, e);
                                                throw new LfwRuntimeException(e.getMessage());
                                        }

                                }
                                
                        });
                        masterDs.setEnabled(true);
                        
                        // 设置页面改变状态
                        AppLifeCycleContext.current().getViewContext().getView().getWindow()
                        .setHasChanged(false);
                        
                        AppLifeCycleContext
                        .current()
                        .getApplicationContext()
                        .getCurrentWindowContext()
                        .closeView(
                                        AppLifeCycleContext.current().getViewContext().getId());
                        
                        this.getCurrentAppCtx().closeWinDialog();
                        
                    Map<String, Object> paramMap = new HashMap<String, Object>();
                    paramMap.put("oper", getOperator());
                    paramMap.put("operation", operation);
                        
                        CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(), PLUGOUT_ID, paramMap));
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
		Map<String, String> paramMap = null;
		if (primaryKeyValue != null){
			paramMap = new HashMap<String, String>(2);
			paramMap.put(PARAM_BILLITEM, primaryKeyValue);
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
				int pkOrgAllIndex = ds.nameToIndex("pk_org_all");
                                if(pkOrgAllIndex >= 0){
                                        row.setValue(pkOrgAllIndex, pkOrg);                
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
    return "workreport";
  }
  public void onReceive(  MouseEvent mouseEvent){
    realSave(ScapCoConstants.RECEIVE);
  }
  public void onSubmit(  MouseEvent mouseEvent){
    realSave(ScapCoConstants.SUBMIT);
  }
  public void onRollback(  MouseEvent mouseEvent){
    realSave(ScapCoConstants.ROLLBACK);
  }
  public void onAfterDataChange(  DatasetCellEvent datasetCellEvent){
      int colIndex = datasetCellEvent.getColIndex();
      int rowIndex = datasetCellEvent.getRowIndex();

      Object oldValue = datasetCellEvent.getOldValue();
      Object newValue = datasetCellEvent.getNewValue();

      if (newValue == null||rowIndex == -1) {
          return;
      }

      Dataset ds = datasetCellEvent.getSource();
      
      Row row = ds.getCurrentRowData().getRow(rowIndex);
      
      int reportTypeIndex = ds.nameToIndex(WorkReportVO.REPORT_TYPE);
      int pkNoticeIndex = ds.nameToIndex(WorkReportVO.PK_NOTICE);
      
      if (colIndex == reportTypeIndex) {
          FormComp fc = (FormComp) this.getCurrentView().getViewComponents().getComponent("workreport_form");
          FormElement fe = null;
          if (fc != null) {
              fe = fc.getElementById("pk_notice_notice_title");
          }
          
          setReportType();
          
          if(fe!=null) {
              ds.setValue(pkNoticeIndex, "");
          }
      }
      
  }
public void setReportType() {
    Dataset ds = this.getMasterDs();
    int reportTypeIndex = ds.nameToIndex(WorkReportVO.REPORT_TYPE);
    String pkReportType = (String)ds.getValue(reportTypeIndex);
    
    String methodType = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(IGlobalConstants.METHOD_TYPE);
    
    FormComp fc = (FormComp) this.getCurrentView().getViewComponents().getComponent("workreport_form");
    FormElement fe = null;
    if (fc != null) {
        fe = fc.getElementById("pk_notice_notice_title");
    }
    
    if(pkReportType == null || pkReportType.length() == 0) {
//        throw new LfwRuntimeException("所选数据的报告类型未设置!");
        if(fe != null) {
            fe.setEnabled(false);
        }
        return;
    }
    
    String condition = "select is_ass_noti from scapco_work_reportType where " + Work_report_type.PK_WORK_REPORT_TYPE + " = '" + pkReportType + "'";
      String withNotice = (String)ScapDAO.executeQuery(condition, new ColumnProcessor());
      AppUtil.addAppAttr("withNotice", withNotice);
      AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_TYPE, pkReportType);
      
      if("0".equals(withNotice) && (ScapCoConstants.ADD.equals(methodType) || ScapCoConstants.EDIT.equals(methodType))) {
          if(fe != null) {
              fe.setEnabled(true);
          }
      }
      else {
          if(fe != null) {
//                  fe.setNullAble(true);
//              fe.setValue("");
              fe.setEnabled(false);
          }
      }
}
}
