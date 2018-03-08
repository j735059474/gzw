package nc.scap.transfer.land;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.scap.pt.vos.LandVO;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpPubMethod;
import nc.scap.pub.util.CpWfmUtilFacade;
import nc.scap.pub.util.ScapDAO;
import nc.scap.transfer.land.utils.ILandConstants;
import nc.scap.transfer.land.utils.LandUtil;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.cpb.persist.dao.PtBaseDAO;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
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
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.plug.TranslatedRow;
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.exception.WfmServiceException;
import nc.uap.wfm.itf.IWfmTaskQry;
import nc.uap.wfm.model.Task;
import nc.uap.wfm.utils.WfmCPUtilFacade;
import nc.uap.wfm.utils.WfmTaskUtil;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.uap.wfm.vo.WfmTaskVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import uap.web.bd.pub.AppUtil;
/** 
 * ��Ϣ�б�Ĭ���߼�
 */
public class LandListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="transferComps.land_cardwin";
  private static final String CARD_WIN_TITLE="�༭";
  private static final String CARD_WIN_TITLE_VIEW="�鿴";
  private static final String CARD_WIN_TITLE_AUDIT="���";
  /** 
 * �����ݼ����߼�
 * @param dataLoadEvent
 */
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
			String wheresql = filterSql();
			ds.setLastCondition(wheresql);
			ds.setExtendAttribute(Dataset.ORDER_PART, "order by pk_org,ts desc ");
			CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
  /** 
 * ������ѡ���߼�
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
		  Row row = ds.getSelectedRow();
		  if(row!=null){
			  String isAudit = (String) LfwRuntimeEnvironment.getWebContext().getOriginalParameter(ILandConstants.IS_AUDIT);
			  String pk = (String) row.getValue(ds.nameToIndex(LandVO.PK_LAND));
			  LandVO changvo = (LandVO) ScapDAO.retrieveByPK(LandVO.class, pk);
			  String formState = changvo.getFormstate();
			  CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
			  boolean flag = false;
			  try {
				  flag = WfmCPUtilFacade.isCanDelBill(pk);
			  } catch (Exception e) {
				  flag = false;
			  }
			  // ֻ�в��صĵ��ݲ��ܱ��
			  if (formState != null && isAudit != null && isAudit.equals(ILandConstants.AUDIT_FALSE)) {
				  // ֻ��׼���ĵ��ݲ��ܱ�ɾ��
				  LandUtil.ctrlMainBtn(ILandConstants.MENUBAR,ILandConstants.BTN_WF, true);
				  LandUtil.setBtnEnState(AppLifeCycleContext.current().getViewContext().getView(), ILandConstants.MENUBAR, new String[]{
					  ILandConstants.BTN_EDIT, ILandConstants.BTN_DEL 
				  } , flag);
			  } else if (formState != null && isAudit != null&& isAudit.equals(ILandConstants.AUDIT_TRUE)) {// ����ί���
//				  if (ILandConstants.FOMRSTATE_END.equals(formState) || ILandConstants.FOMRSTATE_CANCELLATION.equals(formState)) {
//					  LandUtil.setBtnEnState(AppLifeCycleContext.current().getViewContext().getView(), ILandConstants.MENUBAR, new String[]{
//						  ILandConstants.BTN_AUDIT
//					  } , false);
//				  } else {
					  LandUtil.ctrlMainBtn(ILandConstants.MENUBAR,ILandConstants.BTN_WF, true);
					  LandUtil.ctrlMainBtn(ILandConstants.MENUBAR,ILandConstants.BTN_AUDIT, !flag);
//				  }
			  } else if (formState .equals( ILandConstants.FOMRSTATE_NOTSTART)) {
				  LandUtil.ctrlMainBtn(ILandConstants.MENUBAR,ILandConstants.BTN_WF, false);
			  }
		  }
  }
  /** 
 * �ⲿ����ˢ��
 * @param keys
 */
  public void doRefresh(  Map<?,?> keys){
    Dataset ds = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset(getMasterDsId());
					LfwRuntimeEnvironment.getWebContext().getWebSession()
							.addOriginalParameter("openBillId", null);
					// ds.setCurrentKey(ds.getSelectedIndex() + "1");
					onDataLoad(new DataLoadEvent(ds));
			  
//			  TranslatedRow selRow = (TranslatedRow) keys.get(OPERATE_ROW);
//				if (selRow != null) {
//					Dataset ds = this.getMasterDs();
//					String sign = this.getOperator();
//					if (AppConsts.OPE_EDIT.equals(sign)) {
//						Row savedRow = ds.getSelectedRow();
//						copyTranslateRow2Row(selRow,savedRow,ds);
////						}
//					} else if (AppConsts.OPE_ADD.equals(sign)) {
//						Row savedRow = ds.getEmptyRow();
//						savedRow = copyTranslateRow2Row(selRow,savedRow,ds);
//						ds.addRow(savedRow);
//					}
//				}
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
 * ����
 * @param mouseEvent
 */
  public void onAdd(  MouseEvent<?> mouseEvent){
    this.onAdd_wfm();
			OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE,"1200","500");
			props.setButtonZone(false);
			Map<String, String> paramMap = new HashMap<String, String>(2);
			paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_ADD);
			paramMap.put("model", "nc.scap.transfer.model.LandPopPageModel");
			props.setParamMap(paramMap);
			CpPubMethod.resetWfmParameter();
			this.getCurrentAppCtx().navgateTo(props);

			AppUtil.addAppAttr(ILandConstants.SAVE_OPE,ILandConstants.SAVE_SIGN_ADD);
  }
  /** 
 * ����-����ҵ��
 */
  private void onAdd_wfm(){
    this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_BillID, null);
		this.resetWfmParameter();
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_FolwTypePk, this.getFlwTypePk());
  }
  /** 
 * ��ҳ���뵱ǰҳ������ͬһapp��Χ��ÿ����Ҫ���app�еı���
 */
  private void resetWfmParameter(){
    this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_TaskPk, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_ScratchPad, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.AttachFileList_Temp_Billitem, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_ProInsPk, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.RETURN_PK_TASK, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_FormInFoCtx_Billitem, null);
  }
  /** 
 * ��õ�������PK
 * @return
 */
  private String getFlwTypePk(){
    return "0001ZG100000000JWZQ9";
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
			String pkValue = (String) row.getValue(ds.nameToIndex(ds
					.getPrimaryKeyField()));

			OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE,"1200","500");
			props.setButtonZone(false);
			Map<String, String> paramMap = new HashMap<String, String>(2);
			paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
			paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
//			paramMap.put(WfmConstants.WfmUrlConst_billID, pkValue);
			paramMap.put("model", "nc.scap.transfer.model.LandPopPageModel");
			props.setParamMap(paramMap);

//			this.onEdit_wfm(pkValue);
			CpPubMethod.setWfmInfo(paramMap, pkValue,"E9AI020225", "edit");

			this.getCurrentAppCtx().navgateTo(props);
			AppUtil.addAppAttr(ILandConstants.SAVE_OPE,ILandConstants.SAVE_SIGN_EDIT);
  }
  /** 
 * �༭-����ҵ��
 * @param pkValue
 */
  private void onEdit_wfm(  String pkValue){
    this.resetWfmParameter();
			try {
				String pk_user = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
				IWfmTaskQry taskQry = ServiceLocator.getService(IWfmTaskQry.class);
				WfmTaskVO task = taskQry.getLastTaskVOByFormPkAndUserPk(pkValue, pk_user);
				if (task != null) {
					this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_TaskPk, task.getPk_task());
					this.getCurrentAppCtx().addAppAttribute(
							WfmConstants.WfmUrlConst_TaskPk, task.getPk_task());
				}
			} catch (Exception e) {
				LfwLogger.error(e);
				throw new LfwRuntimeException(e.getMessage());
			}
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
		String pk_form = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));
		if(pk_form != null && !pk_form.equals("")){
			boolean isCanDel = WfmCPUtilFacade.isCanDelBill(pk_form);
			if(isCanDel){
				WfmCPUtilFacade.delWfmInfo(pk_form);
				CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
			}
			else{
				throw new LfwRuntimeException("�������������޷�ɾ������");
				}

		}else{
			throw new LfwRuntimeException("δ��ȡ�����̵�������ֵ");
		}
  }
  /** 
 * ���̽���
 * @param mouseEvent
 */
  public void onFlow(  MouseEvent<?> mouseEvent){
    // WfmUtilFacade.openFlowImage();
	 			String windowId = "wfm_flowhistory";
	 			String isNcWorkFlow = (String) AppUtil.getAppAttr("NC");
	 			Map<String, String> paramMap = new HashMap<String, String>();

	 			if ("Y".equals(isNcWorkFlow))
	 				windowId = "pfinfo";
	 			else {
	 				isExistFloInf();
	 				String taskPk = WfmTaskUtil.getTaskPkFromSession();
	 				Task task = (Task) WfmTaskUtil.getTaskFromDB(taskPk);
	 				if (taskPk != null && !"".equals(taskPk)) {
	 					String proInsPK = task.getRootProIns().getPk_proins();
	 					AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ProInsPk, proInsPK);
	 				}
	 			}
	 			String title = "���̽���";
	 			OpenProperties props = new OpenProperties();
	 			props.setWidth("900");
	 			props.setHeight("650");
	 			props.setTitle(title);
	 			props.setOpenId(windowId);
	 			props.setParamMap(paramMap);
	 			AppLifeCycleContext.current().getViewContext().navgateTo(props);
  }
  private void isExistFloInf(){
    Dataset ds = AppLifeCycleContext.current().getViewContext().getView()
	 			 		 						.getViewModels().getDataset(getMasterDsId());
	 			 		 				if (ds.getSelectedIndex() < 0)
	 			 		 					throw new LfwRuntimeException("��ѡ�д��༭����");
	 			 		 				Row row = ds.getSelectedRow();
	 			 		 				String billId = (String) row.getValue(ds.nameToIndex(ds
	 			 		 						.getPrimaryKeyField()));
	 			 		 				String taskPk = null;
	 			 		 				if (StringUtils.isNotBlank(billId)) {
	 			 		 					String pk_user = LfwRuntimeEnvironment.getLfwSessionBean()
	 			 		 							.getPk_user();
	 			 		 					WfmTaskVO task = null;
	 			 		 					try {
	 			 		 						task = getLasterTaskByBillIdAndUser(billId, pk_user);
	 			 		 					} catch (WfmServiceException e) {
	 			 		 						// TODO Auto-generated catch block
	 			 		 						LfwLogger.error(e.getMessage(), e);
	 			 		 					}

	 			 		 					if (task != null) {
	 			 		 						taskPk = task.getPk_task();
	 			 		 						AppUtil.addAppAttr(WfmConstants.WfmAppAttr_TaskPk,
	 			 		 								task.getPk_task());
	 			 		 					} else
	 			 		 						AppUtil.addAppAttr(WfmConstants.WfmAppAttr_TaskPk, null);
	 			 		 				}
	 			 		 				String flowTypePk = WfmTaskUtil.getFlowTypePkFromSession();
	 			 		 				String prodefPk = WfmTaskUtil.getProdefPkFromSession();
	 			 		 				if ((taskPk != null && !"".equals(taskPk))
	 			 		 						|| (flowTypePk != null && !"".equals(flowTypePk))
	 			 		 						|| (prodefPk != null && !"".equals(prodefPk))) {
	 			 		 				} else {
	 			 		 					throw new LfwRuntimeException("û�ж�������");
	 			 		 				}
  }
  private WfmTaskVO getLasterTaskByBillIdAndUser(  String billId,  String pk_user) throws WfmServiceException {
    if (billId == null || billId.length() == 0) {
	 			 		 					return null;
	 			 		 				}
	 			 		 				String sql = "SELECT * FROM wfm_task WHERE pk_frmins = ? and pk_owner = ? order by ts desc";
	 			 		 				SQLParameter param = new SQLParameter();
	 			 		 				param.addParam(billId);
	 			 		 				param.addParam(pk_user);
	 			 		 				PtBaseDAO dao = new PtBaseDAO();
	 			 		 				try {
	 			 		 					List<WfmTaskVO> vos = (List<WfmTaskVO>) dao.executeQuery(sql,
	 			 		 							param, new BeanListProcessor(WfmTaskVO.class));
	 			 		 					if (vos != null && vos.size() > 0) {
	 			 		 						return vos.get(0);
	 			 		 					}
	 			 		 				} catch (DAOException e) {
	 			 		 					LfwLogger.error(e.getMessage(), e);
	 			 		 					throw new WfmServiceException(e.getMessage());
	 			 		 				}
	 			 		 				return null;
  }
  /** 
 * ����
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onStart(  MouseEvent<?> mouseEvent) throws BusinessException {
    CmdInvoker.invoke(new UifEnableCmd(getMasterDsId(), true));
  }
  /** 
 * ͣ��
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onStop(  MouseEvent<?> mouseEvent) throws BusinessException {
    CmdInvoker.invoke(new UifEnableCmd(getMasterDsId(), false));
  }
  /** 
 * ����
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onAttchFile(  MouseEvent<?> mouseEvent){
    String isAudit = (String) LfwRuntimeEnvironment.getWebContext().getOriginalParameter(ILandConstants.IS_AUDIT);
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
//			if (taskPk == null || taskPk.equals("")) {
//				this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE, WfmConstants.BILLSTATE_BROWSE);
//			}
			CpWfmUtilFacade.setWfmBillState(primaryKeyValue, taskPk);
			// ����ֻ�ܲ鿴,��Ҫ��Ϊ���̬.
			if (isAudit != null&& ILandConstants.AUDIT_VIEW.equals(isAudit)) {
				this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE, WfmConstants.BILLSTATE_BROWSE);
			}
			// ���̸�������
			Map<String, String> wfmParam = WfmUtilFacade.getFileMgrParamsByTask(taskPk);

			// ��������
			Map<String, String> param = UploadFileHelper.BuildDefaultPamater(LfwFileConstants.SYSID_BAFILE, primaryKeyValue, CommonObjectConstants.AttachFileType, "");
			param.put("usescanable", "true");
			param.put("state", String.valueOf(31));

			String title = "����";
			wfmParam.put("billitem", primaryKeyValue);
			if (wfmParam != null && !wfmParam.isEmpty()) {
				param.putAll(wfmParam);
			}

			CmdInvoker.invoke(new UifAttachCmd(title, param));
  }
  /** 
 * ��ӡ
 */
  public void onPrint(  MouseEvent<?> mouseEvent){
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
 * ����֯�仯
 */
  public void doOrgChange(  Map keys){
    CmdInvoker.invoke(new UifDatasetLoadCmd(getMasterDsId()) {
				protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
					// String where = AppUtil.getGroupOrgFielterSqlForDesign(false,
					// PK_ORG);
					String where = filterSql();
					ds.setLastCondition(where);
					ds.setExtendAttribute("ORDER_PART", " order by ts desc");
					return where;
				}

				// ���ⷭҳʱ���߻���
				protected void changeCurrentKey() {
					setChangeCurrentKey(true);
				}
			});
				clearDetailDs();
  }
  /** 
 * ������Ⱦ��������
 * @param
 * @return where
 */
  public String filterSql(){
    String where = "1=1";
					String isAudit = LfwRuntimeEnvironment.getWebContext()
							.getOriginalParameter(ILandConstants.IS_AUDIT);
					String saveope = (String) AppUtil.getAppAttr(ILandConstants.SAVE_OPE);
					
					isAudit = isAudit == null ? saveope : isAudit;
					if (ILandConstants.AUDIT_TRUE.equals(isAudit)||ILandConstants.SAVE_SIGN_AUDIT.equals(isAudit)) {// ����ί���and formstate = 'Run'
						where += " and dr = 0 and "+LandVO.FORMSTATE+"='"+IGlobalConstants.SCAPPM_BILLSTATE_RUN+"'"+LandUtil.getAllWfmTaskByUserPK(null, Task.State_Run, LandVO.PK_LAND,getFlwTypePk());
					} else if (ILandConstants.AUDIT_FALSE.equals(isAudit)
							||ILandConstants.SAVE_SIGN_ADD.equals(isAudit)
							||ILandConstants.SAVE_SIGN_EDIT.equals(isAudit)) {// ��ҵ�걨
						where += " and dr = 0 and pk_org=" + "'" + CntUtil.getCntOrgPk()+ "'";
					} else if (ILandConstants.AUDIT_VIEW.equals(isAudit)) {// �鿴��org������
						where += " and dr = 0 and "+LandVO.FORMSTATE+" != '"+IGlobalConstants.SCAPPM_BILLSTATE_NOTTSTART+"' ";
					}
					return where;
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
  /** 
 * ��ѯpluguin
 * @param keys
 */
  public void doQueryChange(  Map keys){
    FromWhereSQL whereSql = (FromWhereSQL) keys
					.get(FromWhereSQL.WHERE_SQL_CONST);
			String sql = whereSql.getWhere() + " and " + this.filterSql();
			CmdInvoker.invoke(new QueryCmd(MAIN_VIEW_ID, getMasterDsId(), sql));
  }
  @Override protected String getMasterDsId(){
    return "scappt_land";
  }
  public void onAfterRowUnSelect(  DatasetEvent datasetEvent){
	    Dataset ds = datasetEvent.getSource();
			CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
	  }
  public void onAudit(  MouseEvent mouseEvent){
	    Dataset ds = this.getMasterDs();
			if (ds.getSelectedIndex() < 0) {
				throw new LfwRuntimeException("��ѡ�д��༭����");
			}

			Row row = ds.getSelectedRow();
			String pkValue = (String) row.getValue(ds.nameToIndex(ds
					.getPrimaryKeyField()));

			OpenProperties props = new OpenProperties(CARD_WIN_ID,
					CARD_WIN_TITLE_AUDIT,"1200","500");
			props.setButtonZone(false);
			Map<String, String> paramMap = new HashMap<String, String>(3);
			paramMap.put(AppConsts.OPE_SIGN, ILandConstants.SAVE_SIGN_AUDIT);
			paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
			paramMap.put("model", "nc.scap.transfer.model.LandPopPageModel");
			props.setParamMap(paramMap);

//			this.onEdit_wfm(pkValue);
			paramMap.put(
					WfmConstants.WfmUrlConst_TaskPk,
					(String) this.getCurrentAppCtx().getAppAttribute(
							WfmConstants.WfmAppAttr_TaskPk));
			CpPubMethod.setWfmInfo(paramMap, pkValue,
					"E9AI020225", "edit");
			this.getCurrentAppCtx().navgateTo(props);

			AppUtil.addAppAttr(ILandConstants.SAVE_OPE,
					ILandConstants.SAVE_SIGN_AUDIT);
	  }
  public void onView(  MouseEvent mouseEvent){
	    Dataset ds = this.getMasterDs();
			if (ds.getSelectedIndex() < 0) {
				throw new LfwRuntimeException("��ѡ�д��鿴����");
			}

			Row row = ds.getSelectedRow();
			String pkValue = (String) row.getValue(ds.nameToIndex(ds
					.getPrimaryKeyField()));

			OpenProperties props = new OpenProperties(CARD_WIN_ID,
					CARD_WIN_TITLE_VIEW,"1200","500");
			props.setButtonZone(false);
			Map<String, String> paramMap = new HashMap<String, String>(3);
			paramMap.put(AppConsts.OPE_SIGN, ILandConstants.SAVE_SIGN_VIEW);
			paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
			paramMap.put("model", "nc.scap.transfer.model.LandPopPageModel");
			props.setParamMap(paramMap);

//			this.onEdit_wfm(pkValue);
			CpPubMethod.setWfmInfo(paramMap, pkValue,
				"E9AI020225", ILandConstants.BTN_WF);

			this.getCurrentAppCtx().navgateTo(props);
			AppUtil.addAppAttr(ILandConstants.SAVE_OPE,
					ILandConstants.SAVE_SIGN_VIEW);
	  }
}
