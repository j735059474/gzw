package nc.scap.transfer.land;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.scap.avpm.vos.StateHVO;
import nc.scap.avpm.vos.StateResultBVO;
import nc.scap.def.util.ScapDefUtil;
import nc.scap.prregiest.vos.ScapFirmBaseBVO;
import nc.scap.prregiest.vos.ScapFirmBaseHVO;
import nc.scap.pt.vos.LandVO;
import nc.scap.pt.vos.ScapptTransferHVO;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpUIctrl;
import nc.scap.pub.util.CpWinUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.util.ScapPAPPubmethod;
import nc.scap.transfer.land.utils.ILandConstants;
import nc.scap.transfer.land.wfm.WfmFlwFormVO;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifAddCmd;
import nc.uap.lfw.core.cmd.UifCopyCmd;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.lfw.core.cmd.UifLineDelCmd;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.cmd.UifSaveCmdRV;
import nc.uap.lfw.core.cmd.UifUpdateUIDataCmdRV;
import nc.uap.lfw.core.cmd.base.CommandStatus;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.MenubarComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.file.FillFileInfoHelper;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.uif.delegator.DefaultDataValidator;
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.portal.log.ScapLogger;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.exe.WfmCmd;
import nc.uap.wfm.utils.WfmCPUtilFacade;
import nc.uap.wfm.utils.WfmTaskUtil;
import nc.uap.wfm.vo.WfmFormInfoCtx;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.core.locator.ServiceLocator;
import uap.web.bd.pub.AppUtil;
/** 
 * ��Ƭ����Ĭ���߼�
 */
public class LandCardWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final String PLUGOUT_ID="afterSavePlugout";
  public static final String OPEN_BILL_ID="openBillId";
	public static final String MAINVO = "mainvo";
	public static final String MAINVO_R = "mainvo_r";
	public static final String MAINVO_S = "mainvo_s";
	private ScapFirmBaseHVO mainvo;
	private LandVO mainvo_b;
	private LandVO mainvo_r;
	private LandVO mainvo_s;
  /** 
 * ҳ����ʾ�¼�
 * @param dialogEvent
 */
  public void beforeShow(  DialogEvent dialogEvent){
	    Dataset masterDs = this.getMasterDs();
				masterDs.clear();
				
				final String oper = this.getOperator();
//				AppUtil.addAppAttr("wherePart", "pk_org = '"+CntUtil.getCntOrgPk()+"'  and vstatus = '"+SCAPCBConstants.PK_PROJECT_STATUS_04+"' or vstatus = '"+SCAPCBConstants.PK_PROJECT_STATUS_05+"'  ");
				AppUtil.addAppAttr(LfwFileConstants.SYSID,LfwFileConstants.SYSID_BAFILE);
				if(AppConsts.OPE_ADD.equals(oper)){
					CmdInvoker.invoke(new UifAddCmd(this.getMasterDsId()){
						@Override
						protected void onBeforeRowAdd(Row row) {
							setAutoFillValue(row);
							
							Dataset masterDs = getMasterDs();
							String pk_primarykey = generatePk();
							init(oper, masterDs, row);// ��ʼ������
							row.setValue(masterDs.nameToIndex(masterDs.getPrimaryKeyField()), pk_primarykey);
							row.setValue(masterDs.nameToIndex("attach"), pk_primarykey);
							FillFileInfoHelper.resetItem(pk_primarykey);
							FillFileInfoHelper.fillFileInfo(masterDs, row);
						}
					});
				}else if(AppConsts.OPE_EDIT.equals(oper)) {
					CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs){
						@Override
						protected void onAfterDatasetLoad() {
							setDSEnabledByTask(this.getDs());
							init(oper, this.getDs(), this.getDs().getSelectedRow());// ��ʼ������
							String primaryKey = this.getDs().getPrimaryKeyField();
							if (primaryKey == null) {
								throw new LfwRuntimeException("��ǰDatasetû����������!");
							}
							String primaryKeyValue = (String) this.getDs().getSelectedRow().getValue(this.getDs().nameToIndex(primaryKey));					
							FillFileInfoHelper.resetItem(primaryKeyValue);
						}
					});
				}
				else if(ILandConstants.SAVE_SIGN_AUDIT.equals(oper)){
					CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs){
						@Override
						protected void onAfterDatasetLoad() {
//							setDSEnabledByTask(this.getDs());
							this.getDs().setEnabled(false);
							init(oper, this.getDs(), this.getDs().getSelectedRow());// ��ʼ������
							String primaryKey = this.getDs().getPrimaryKeyField();
							if (primaryKey == null) {
								throw new LfwRuntimeException("��ǰDatasetû����������!");
							}
							String primaryKeyValue = (String) this.getDs().getSelectedRow().getValue(this.getDs().nameToIndex(primaryKey));					
							FillFileInfoHelper.resetItem(primaryKeyValue);
						}
						
					});
				}
				else if(ILandConstants.SAVE_SIGN_VIEW.equals(oper)||"look".equals(oper)){
					CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs){
						@Override
						protected void onAfterDatasetLoad() {
//							setDSEnabledByTask(this.getDs());
							this.getDs().setEnabled(false);
							init(oper, this.getDs(), this.getDs().getSelectedRow());// ��ʼ������
							String primaryKey = this.getDs().getPrimaryKeyField();
							if (primaryKey == null) {
								throw new LfwRuntimeException("��ǰDatasetû����������!");
							}
							String primaryKeyValue = (String) this.getDs().getSelectedRow().getValue(this.getDs().nameToIndex(primaryKey));					
							FillFileInfoHelper.resetItem(primaryKeyValue);
						}
						
					});
				}
	  }
	/**
	 * ��ʼ������
	 * 
	 * @param operation
	 */
	public void init(String operation, Dataset masterDs, Row row) {
		initData(masterDs, row);
		initUI(operation);
	}
	/**
	 * ��ʼ�����棭���ƽ��水ť���ֶ�����
	 * 
	 * @param operation
	 */
	public void initUI(String operation) {
		if ("look".equals(operation)) {
			CpUIctrl.hideMenuBarReal(CpWinUtil.getView(), "menubar",
					new String[] { "back" });
		} else if ("approve".equals(operation)) {
			CpUIctrl.hideMenuBarReal(CpWinUtil.getView(), "menubar", null);
			// setDatasetEable(new String[] { "scappt_transfer_h",
			// "scappt_transfer_transferor", "scappt_transfer_target",
			// "scappt_transfer_assess", "scappt_transfer_transferee" },
			// false);
			// setBtnImgHidden(new String[] { "scappt_transfer_transferor_grid",
			// "scappt_transfer_target_grid",
			// "scappt_transfer_assess_grid",
			// "scappt_transfer_transferee_grid" });
		}
	}
	/**
	 * ������ʼ��
	 * 
	 * @param masterDs
	 * @param row
	 */
	public void initData(Dataset masterDs, Row row) {
		String oper = this.getOperator();
		String billno = getBillNoPk();// Ĭ��ʹ��def9�洢��������
		String pk_borg = getPKBorg();// ��Ȩ���������ҵPK
		String pk_rorg = getPKRorg();// ת�÷���Ȩ�Ǽ�PK
		String mainpk = getMainPk();
		if (mainpk != null) {
			row.setValue(
					masterDs.nameToIndex(masterDs.getPrimaryKeyField()),
					mainpk);
		}
		row.setValue(masterDs.nameToIndex(LandVO.PK_FIRMTREE),pk_borg);
		row.setValue(masterDs.nameToIndex(LandVO.DEF9), billno);
		row.setValue(masterDs.nameToIndex(LandVO.PK_CZFIRMTREE),pk_rorg);
		if (oper != null && oper.equals("add")) {
			row.setValue(masterDs.nameToIndex(LandVO.BILLMAKER),CntUtil.getCntUserPk());
			row.setValue(masterDs.nameToIndex(LandVO.BILLMAKEDATE),new UFDateTime());
			row.setValue(masterDs.nameToIndex(LandVO.FORMSTATE),IGlobalConstants.SCAPPM_BILLSTATE_NOTTSTART);
			row.setValue(masterDs.nameToIndex(LandVO.APPROVESTATUS), ILandConstants.APPROVESTATE_ADD);
		} 
		setFormDataByTypt(billno, pk_borg, pk_rorg, null, masterDs, row);
//		initSelfData(masterDs, row);
	}
	/**
	 * �������ͽ������ݳ�ʼ��
	 * 
	 * @param billno
	 * @param pk_borg
	 * @param pk_rorg
	 * @param pk_sorg
	 * @param masterDs
	 * @param row
	 */
	public void setFormDataByTypt(String billno, String pk_borg,
			String pk_rorg, String pk_sorg, Dataset masterDs, Row row) {
		if (pk_borg != null) {
			mainvo = queryFirmVOByPk(pk_borg);
//			setMainvo(MAINVO, mainvo);
			//��֯������
			row.setValue(masterDs.nameToIndex(LandVO.VCODE),mainvo.getVcode());
			//��������ҵ����
			row.setValue(masterDs.nameToIndex(LandVO.CZNAME),mainvo.getVmaincode());
			//ע���
			if (mainvo.getVregistry() != null&& !"".equals(mainvo.getVregistry())) {
				row.setValue(masterDs.nameToIndex(LandVO.VREGISTRY),mainvo.getVregistry());
			}
			//����ʱ��
			if (mainvo.getCjoindate() != null&& !"".equals(mainvo.getCjoindate())) {
				row.setValue(masterDs.nameToIndex(LandVO.CJOINDATE),mainvo.getCjoindate());
			}
			//ע���ʱ�
			if (mainvo.getDregcapital() != null&& !"".equals(mainvo.getDregcapital())) {
				row.setValue(masterDs.nameToIndex(LandVO.DREGCAPITAL),mainvo.getDregcapital());
			}
			//��ҵ���
			if (mainvo.getPk_firmtype() != null&& !"".equals(mainvo.getPk_firmtype())) {
				row.setValue(masterDs.nameToIndex(LandVO.PK_FIRMTYPE),ScapDefUtil.getDefDocNamesByPks(mainvo.getPk_firmtype()));
			}
			//���ʼ�ܻ���
			if (mainvo.getPk_agency() != null&& !"".equals(mainvo.getPk_agency())) {
				row.setValue(masterDs.nameToIndex(LandVO.PK_AGENCY),mainvo.getPk_agency());
			}
			//�Ƿ��й��л�����
			//ְ������

		}
	}
	/**
	 * ��ȡ��ϵĽ�����Ϣ
	 * 
	 * @param pk
	 * @return
	 */
	public static ScapFirmBaseHVO queryFirmVOByPk(String pk) {
		ScapFirmBaseHVO mainvo = new ScapFirmBaseHVO();
		if (pk == null || "".equals(pk)) {
			throw new LfwRuntimeException("δ�ҵ���Ӧ��ҵ��Ȩ�Ǽ���Ϣ��");
		}
		try {
//			if (!isActiveModule("scappr")) {
//				ScapLogger.error("��ȡ��ϵĽ�����Ϣ:scapprģ��δ����");
//				return mainvo;
//			}
			String sql = "SELECT b.* FROM scappr_firmbase_h b ,scappr_firmtree c WHERE c.pk_firmtree='"
					+ pk
					+ "' AND b.vcode = c.org_code AND b.if_now='1' AND itype = '1' AND b.dr=0 ";
			ScapLogger.debug(sql);
			List<ScapFirmBaseHVO> lists = (List<ScapFirmBaseHVO>) ScapDAO.getBaseDAO().executeQuery(sql,new BeanListProcessor(ScapFirmBaseHVO.class));
			if (lists == null || lists.size() <= 0)
				return mainvo;
			// ��ʼ����������Ϣ
//			initTrsVo(mainvo, lists.get(0));
			mainvo = lists.get(0);
		} catch (DAOException e) {
			throw new LfwRuntimeException("δ�ҵ���Ӧ��ҵ��Ȩ�Ǽ���Ϣ��");
		}
		return mainvo;
	}
	public static String getOrgPkByCode(String code) {
		String str = "";
		if (code == null || code.equals("")) {
			return str;
		}
		String sql = "SELECT pk_firmbase_h FROM scappr_firmbase_h WHERE vcode='" + code
				+ "' AND if_now='1' AND itype = '1' AND dr=0";
		try {
			str = (String) ScapDAO.getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (DAOException e) {
			ScapLogger.error(sql);
			throw new LfwRuntimeException("ת�÷���֯����������!");
		}
		return str;
	}
	public String getPKBorg() {
		String oper = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("app_pk_borg");
		if (oper == null) {
			oper = (String) AppLifeCycleContext.current()
					.getApplicationContext().getAppAttribute("app_pk_borg");
		}
		return oper;
	}

	public String getPKRorg() {
		String oper = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("app_pk_rorg");
		if (oper == null) {
			oper = (String) AppLifeCycleContext.current()
					.getApplicationContext().getAppAttribute("app_pk_rorg");
		}
		return oper;
	}

	public String getBillNoPk() {
		String oper = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("app_pk_billno");
		if (oper == null) {
			oper = (String) AppLifeCycleContext.current()
					.getApplicationContext().getAppAttribute("app_pk_rorg");
		}
		return oper;
	}

	public String getMainPk() {
		String currentValue = LfwRuntimeEnvironment.getWebContext()
				.getWebSession().getOriginalParameter("openBillId");
		if (currentValue == null) {
			String value = (String) LfwRuntimeEnvironment.getWebContext()
					.getWebSession().getOriginalParameter("pk_land");
			LfwRuntimeEnvironment.getWebContext().getWebSession()
					.addOriginalParameter("openBillId", value);
		}
		return currentValue;
	}
  /** 
 * ��ȡ����PK
 * @return String
 */
  private String getPkTask(){
	    String pk = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(WfmConstants.WfmUrlConst_TaskPk);
				if(pk == null){
					pk = (String)this.getCurrentAppCtx().getAppAttribute(WfmConstants.WfmUrlConst_TaskPk);
				}
				if(pk == null){
					pk = (String)this.getCurrentAppCtx().getAppAttribute(WfmConstants.WfmAppAttr_TaskPk);
				}
				return pk;
	  }
  /** 
 * �������������������ݼ�ʹ��״̬
 * @param ds
 */
  private void setDSEnabledByTask(  Dataset ds){
    if(ds != null){
			Object task = WfmTaskUtil.getTaskFromSessionCache(this.getPkTask());
			if(task != null){
				if(WfmTaskUtil.isEndState(task) || WfmTaskUtil.isFinishState(task) ||  WfmTaskUtil.isSuspendedState(task) || WfmTaskUtil.isCanceledState(task)){
					ds.setEnabled(false);
				}else{
					ds.setEnabled(true);
				}
			}else{
				ds.setEnabled(true);
			}
		}
  }
  /** 
 * ����PK_ORG�ֶ�ֵ
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
					// �Ƶ���
					row.setValue(ds.nameToIndex(LandVO.BILLMAKER), CntUtil.getCntUserPk());
					// �Ƶ�����
					row.setValue(ds.nameToIndex(LandVO.BILLMAKEDATE), new UFDate());
					// �Ƶ���
					row.setValue(ds.nameToIndex(LandVO.BILLMAKER), CntUtil.getCntUserPk());
					// �Ƶ�����
					row.setValue(ds.nameToIndex(LandVO.BILLMAKEDATE), new UFDate());
					// ����״̬
					row.setValue(ds.nameToIndex(LandVO.FORMSTATE), ILandConstants.FOMRSTATE_NOTSTART);
					// ����״̬
					row.setValue(ds.nameToIndex(LandVO.APPROVESTATUS), ILandConstants.APPROVESTATE_ADD);
				}
	  }
  /** 
 * ������ѡ���߼�
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent dsEvent){
    Dataset ds = dsEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()){
		   protected void updateButtons(){
		    
		   }
		  });
  }
  /** 
 * ����
 */
  public void onAdd(  MouseEvent<?> mouseEvent) throws BusinessException {
    this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_BillID, null);
		this.resetWfmParameter();
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_FolwTypePk, this.getFlwTypePk());
		
		CmdInvoker.invoke(new UifAddCmd(this.getMasterDsId()){
			protected void onBeforeRowAdd(Row row){
				setAutoFillValue(row);
			}
		});
  }
  /** 
 * ��ӡ
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onPrint(  MouseEvent<?> mouseEvent) throws BusinessException {
    Dataset masterDs = this.getMasterDs();
		Row row = masterDs.getSelectedRow();
		if (row == null){
			throw new LfwRuntimeException("��ѡ������!");
		}
		try{
			List<Dataset> list = new ArrayList<Dataset>(1);
			list.add(masterDs);
			DefaultPrintService printService = new DefaultPrintService();
			printService.setDatasetList(list);
			ICpPrintTemplateService service = ServiceLocator.getService(ICpPrintTemplateService.class);
			service.print(printService, null, this.getNodeCode());
		}
		catch(Exception e){
			LfwLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
  }
  private String getNodeCode(){
    return "�����˴�ӡģ��Ĺ��ܽڵ��nodecode";
  }
  private String getFlwTypePk(){
    return "0001ZG100000000JWZQ9";
  }
  private void resetWfmParameter(){
    this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_TaskPk, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_ScratchPad, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE,null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.AttachFileList_Temp_Billitem,null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_ProInsPk,null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.RETURN_PK_TASK, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_FormInFoCtx_Billitem, null);
  }
  /** 
 * ɾ��
 */
  public void onDelete(  MouseEvent<?> mouseEvent) throws BusinessException {
    String pk_form = LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("openBillId");
		if(pk_form == null){
			pk_form = (String)LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParameter("pk_land");
			LfwRuntimeEnvironment.getWebContext().getWebSession().addOriginalParameter("openBillId", pk_form);
		}
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
 * ����
 */
  public void onBack(  MouseEvent<?> mouseEvent) throws BusinessException {
    this.getCurrentAppCtx().closeWinDialog();
  }
  /** 
 * ����
 */
  public void onCopy(  MouseEvent<?> mouseEvent) throws BusinessException {
    this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_BillID, null);
		this.resetWfmParameter();		
		CmdInvoker.invoke(new UifCopyCmd(this.getMasterDsId()));
  }
  /** 
 * ���ݼ���
 * @param aggVo
 * @return
 */
  public String checkData(  SuperVO formCtx){
    String esg = "";
		esg = checkSelfData(formCtx);
		return esg;
  }
  /** 
 * �����Լ����߼�
 * @param aggVo
 * @return
 */
  protected String checkSelfData(  SuperVO formCtx){
    String checkMsg = "";
		return checkMsg;
  }
  public void doTaskExecute(  Map keys){
	    //ƽ̨Ĭ��У��
			new DefaultDataValidator().validate(this.getMasterDs(), this.getCurrentView());
			WfmFormInfoCtx formCtx = this.getWfmFormInfoCtx();
			String errmsg = checkData((SuperVO) formCtx);// ��ȡ������Ϣ
			if (errmsg != null && !errmsg.equals("")) {
				throw new LfwRuntimeException(errmsg).setTitle("��ʾ");
			}
			//��������form
			this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_FormInFoCtx, formCtx);
			//������������pk
			this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_FolwTypePk, this.getFlwTypePk());
			//��������pk
			this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_TaskPk, this.getPkTask());
			// ��������
			CmdInvoker.invoke(new WfmCmd());
			if (CommandStatus.SUCCESS.equals(CommandStatus.getCommandStatus())) {
				CmdInvoker.invoke(new UifUpdateUIDataCmdRV((SuperVO)formCtx, getMasterDsId()));
				this.getCurrentAppCtx().closeWinDialog();
				//CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(), PLUGOUT_ID));
				Row savedRow = getMasterDs().getSelectedRow();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("OPERATE_ROW", savedRow);
				CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(), PLUGOUT_ID,paramMap));
				this.getCurrentAppCtx().closeWinDialog();
			}
	  
			LfwView editView = AppLifeCycleContext.current().getViewContext().getView();
			Dataset ds = editView.getViewModels().getDataset(getMasterDsId());
			//�����ܱ༭
			ds.setEnabled(false);
			MenubarComp mbc = editView.getViewMenus().getMenuBar(ILandConstants.MENUBAR);
			//���水ť�û�
			mbc.getItem(ILandConstants.BTN_SAVE).setEnabled(false);
	  }
  /** 
 * �ӱ�����
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
 * �ӱ�༭
 */
  public void onGridEditClick(  MouseEvent<?> mouseEvent){
    GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		ds.setEnabled(true);
  }
  /** 
 * �ӱ�ɾ��
 */
  public void onGridDeleteClick(  MouseEvent<?> mouseEvent){
    GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		CmdInvoker.invoke(new UifLineDelCmd(dsId));
  }
  @Override protected String getMasterDsId(){
    return "scappt_land";
  }
  protected WfmFormInfoCtx getWfmFormInfoCtx(){
    Dataset masterDs = this.getMasterDs();
		Dataset[] detailDss = this.getDetailDs(this.getDetailDsIds());
		SuperVO richVO = this.getDs2RichVOSerializer().serialize(masterDs, detailDss, this.getRichVoClazz());
		return (WfmFormInfoCtx) richVO;
  }
  protected String getRichVoClazz(){
    return WfmFlwFormVO.class.getName();
  }
  public void onSave(  MouseEvent mouseEvent){
	    Dataset masterDs = this.getMasterDs();
		  
		  
			  CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this.getDetailDsIds(), false));
			  
			  masterDs.setEnabled(true);
			  this.getCurrentAppCtx().closeWinDialog();

			  Map<String, Object> paramMap = new HashMap<String, Object>(2);
			  Row savedRow = masterDs.getSelectedRow();
			  paramMap.put(OPERATE_ROW, savedRow);
			  CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(), PLUGOUT_ID, paramMap));
	  }
}
