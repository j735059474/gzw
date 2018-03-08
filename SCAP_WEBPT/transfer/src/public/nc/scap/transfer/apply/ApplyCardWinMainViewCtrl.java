package nc.scap.transfer.apply;
import nc.scap.ptpub.method.ScapPtMethod;
import nc.uap.lfw.core.cmd.UifAddCmd;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.uap.lfw.core.cmd.UifUpdateUIDataCmdRV;
import nc.uap.lfw.core.data.EmptyRow;
import nc.scap.ptpub.PtConstants;
import nc.scap.pt.vos.ScapptApplyHVO;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.event.DatasetCellEvent;
import nc.scap.pt.vos.ScapptTransferHVO;
import nc.uap.wfm.model.ProDef;
import nc.uap.wfm.model.HumAct;
import nc.uap.lfw.core.event.ScriptEvent;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.bs.dao.DAOException;
import nc.uap.lfw.core.cmd.UifLineDelCmd;
import nc.scap.library.vos.AttachEntVO;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifSaveCmdRV;
import nc.scap.pub.itf.IGlobalConstants;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.vo.bd.defdoc.DefdocVO;
import nc.uap.lfw.file.LfwFileConstants;
import java.util.List;
import nc.vo.pub.VOStatus;
import nc.uap.lfw.core.log.LfwLogger;
import nc.scap.pub.util.ScapDAO;
import nc.uap.lfw.core.file.FillFileInfoHelper;
import nc.uap.lfw.core.comp.GridComp;
import nc.scap.transfer.apply.wfm.WfmFlwFormVO;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.cpb.baseservice.IUifCpbService;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import java.util.Iterator;
import nc.scap.pub.util.CntUtil;
import nc.vo.pub.lang.UFDate;
import nc.uap.portal.log.ScapLogger;
import java.util.Arrays;
import nc.uap.wfm.exe.WfmCmd;
import java.util.Map;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.scap.pt.vos.ScapptApplyBVO;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.cmd.UifCopyCmd;
import nc.bs.dao.BaseDAO;
import java.util.HashMap;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.scap.pub.util.ScapPAPPubmethod;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import nc.uap.wfm.utils.WfmCPUtilFacade;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.Dataset;
import java.util.Map.Entry;
import java.util.ArrayList;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.scap.pt.vos.ScapptApplyAttachVO;
import nc.uap.lfw.core.cmd.base.CommandStatus;
import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.vo.pub.SuperVO;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.lfw.file.UploadFileHelper;
import nc.vo.pub.BusinessException;
import nc.bs.logging.Logger;
import nc.scap.pub.util.CpPubMethod;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.wfm.utils.WfmTaskUtil;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.lfw.core.constants.AppConsts;
import uap.web.bd.pub.AppUtil;
import nc.scap.def.util.ScapDefUtil;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.scap.pub.util.CpWinUtil;
import nc.jdbc.framework.processor.ArrayListProcessor;
import uap.lfw.core.locator.ServiceLocator;
import nc.scap.pub.util.CpUIctrl;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.wfm.vo.WfmFormInfoCtx;
/** 
 * ��Ƭ����Ĭ���߼�
 */
public class ApplyCardWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final String PLUGOUT_ID="afterSavePlugout";
  public static final String OPEN_BILL_ID="openBillId";
  /** 
 * ҳ����ʾ�¼�
 * @param dialogEvent
 */
  public void beforeShow(  DialogEvent dialogEvent){
    Dataset masterDs = this.getMasterDs();
		masterDs.clear();

		String oper = this.getOperator();

		uap.web.bd.pub.AppUtil.addAppAttr(LfwFileConstants.SYSID,
				LfwFileConstants.SYSID_BAFILE);

		if (AppConsts.OPE_ADD.equals(oper)) {
			CmdInvoker.invoke(new UifAddCmd(this.getMasterDsId()) {
				@Override
				protected void onBeforeRowAdd(Row row) {
					setAutoFillValue(row);

					Dataset masterDs = getMasterDs();
					String pk_primarykey = generatePk();
					row.setValue(
							masterDs.nameToIndex(masterDs.getPrimaryKeyField()),
							pk_primarykey);
					//2018��3��7�� 11:17:08
					row.setValue(masterDs.nameToIndex("attach"),pk_primarykey);
					row.setValue(masterDs.nameToIndex(ScapptApplyHVO.VDEF1),
							AppUtil.getAppAttr(PtConstants.NODE_TYPE2));//�洢����
					row.setValue(masterDs
							.nameToIndex(ScapptApplyHVO.CAPPLICANT),
							LfwRuntimeEnvironment.getLfwSessionBean()
									.getPk_user());
					row.setValue(
							masterDs.nameToIndex(ScapptApplyHVO.CAPPLICANTDATE),
							new UFDate());
					row.setValue(
							masterDs.nameToIndex(ScapptApplyHVO.BILLMAKER),
							LfwRuntimeEnvironment.getLfwSessionBean()
									.getPk_user());
					row.setValue(
							masterDs.nameToIndex(ScapptApplyHVO.BILLMAKEDATE),
							new UFDate());
					row.setValue(masterDs.nameToIndex(ScapptApplyHVO.PK_ORG),
							LfwRuntimeEnvironment.getLfwSessionBean()
									.getUser_org());
					DefdocVO defvo = ScapDefUtil.getDefdocByListCodeAndCode(
							"scappt_0001", "00101");
					if (defvo != null) {
						row.setValue(masterDs
								.nameToIndex(ScapptApplyHVO.CPROCESSTYPE),
								defvo.getPk_defdoc());
					}
					setFormElementAble(masterDs, row);
					//2018��3��7�� 11:17:25
					setCardDefault(row,masterDs);

					FillFileInfoHelper.resetItem(pk_primarykey);
					FillFileInfoHelper.fillFileInfo(masterDs, row);
				}
			});
		} else if (AppConsts.OPE_EDIT.equals(oper)) {
			String currentValue = LfwRuntimeEnvironment.getWebContext()
					.getWebSession().getOriginalParameter("openBillId");
			if (currentValue == null) {
				String value = (String) LfwRuntimeEnvironment.getWebContext()
						.getWebSession().getOriginalParameter("pk_apply_h");
				LfwRuntimeEnvironment.getWebContext().getWebSession()
						.addOriginalParameter("openBillId", value);
			}
			CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs) {
				@Override
				protected void onAfterDatasetLoad() {
					setDSEnabledByTask(this.getDs());
					String primaryKey = this.getDs().getPrimaryKeyField();
					if (primaryKey == null) {
						throw new LfwRuntimeException("��ǰDatasetû����������!");
					}
					// changeChildPdu(this.getDs());
					String primaryKeyValue = (String) this.getDs()
							.getSelectedRow()
							.getValue(this.getDs().nameToIndex(primaryKey));
					FillFileInfoHelper.resetItem(primaryKeyValue);
				}
			});
			masterDs.setEnabled(false);
		} else if ("approve".equals(oper)) {
			String currentValue = LfwRuntimeEnvironment.getWebContext()
					.getWebSession().getOriginalParameter("openBillId");
			if (currentValue == null) {
				String value = (String) LfwRuntimeEnvironment.getWebContext()
						.getWebSession().getOriginalParameter("pk_apply_h");
				LfwRuntimeEnvironment.getWebContext().getWebSession()
						.addOriginalParameter("openBillId", value);
			}
			CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs) {
				@Override
				protected void onAfterDatasetLoad() {
					setDSEnabledByTask(this.getDs());
					String primaryKey = this.getDs().getPrimaryKeyField();
					if (primaryKey == null) {
						throw new LfwRuntimeException("��ǰDatasetû����������!");
					}
					// changeChildPdu(this.getDs());
					String primaryKeyValue = (String) this.getDs()
							.getSelectedRow()
							.getValue(this.getDs().nameToIndex(primaryKey));
					FillFileInfoHelper.resetItem(primaryKeyValue);
				}
			});
			masterDs.setEnabled(false);
		} else if ("look".equals(oper)) {
			String currentValue = LfwRuntimeEnvironment.getWebContext()
					.getWebSession().getOriginalParameter("openBillId");
			if (currentValue == null) {
				String value = (String) LfwRuntimeEnvironment.getWebContext()
						.getWebSession().getOriginalParameter("pk_apply_h");
				LfwRuntimeEnvironment.getWebContext().getWebSession()
						.addOriginalParameter("openBillId", value);
			}
			CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs) {
				@Override
				protected void onAfterDatasetLoad() {
					setDSEnabledByTask(this.getDs());
					String primaryKey = this.getDs().getPrimaryKeyField();
					if (primaryKey == null) {
						throw new LfwRuntimeException("��ǰDatasetû����������!");
					}
					// changeChildPdu(this.getDs());
					String primaryKeyValue = (String) this.getDs()
							.getSelectedRow()
							.getValue(this.getDs().nameToIndex(primaryKey));
					FillFileInfoHelper.resetItem(primaryKeyValue);
				}
			});
			masterDs.setEnabled(false);
		}
  }
  /** 
 * ��ȡ����PK
 * @return String
 */
  private String getPkTask(){
    String pk = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(
				WfmConstants.WfmUrlConst_TaskPk);
		if (pk == null) {
			pk = (String) this.getCurrentAppCtx().getAppAttribute(
					WfmConstants.WfmUrlConst_TaskPk);
		}
		if (pk == null) {
			pk = (String) this.getCurrentAppCtx().getAppAttribute(
					WfmConstants.WfmAppAttr_TaskPk);
		}
		return pk;
  }
  /** 
 * �������������������ݼ�ʹ��״̬
 * @param ds
 */
  private void setDSEnabledByTask(  Dataset ds){
    if (ds != null) {
			Object task = WfmTaskUtil.getTaskFromSessionCache(this.getPkTask());
			if (task != null) {
				if (WfmTaskUtil.isEndState(task)
						|| WfmTaskUtil.isFinishState(task)
						|| WfmTaskUtil.isSuspendedState(task)
						|| WfmTaskUtil.isCanceledState(task)) {
					ds.setEnabled(false);
				} else {
					ds.setEnabled(true);
				}
			} else {
				ds.setEnabled(true);
			}
		}
  }
  /** 
 * ����PK_ORG�ֶ�ֵ
 * @param row
 */
  private void setAutoFillValue(  Row row){
    if (row != null) {
			Dataset ds = this.getCurrentView().getViewModels()
					.getDataset(this.getMasterDsId());

			String pkOrg = this.getCurrentAppCtx().getAppEnvironment()
					.getPk_org();
			if (pkOrg != null) {
				int pkOrgIndex = ds.nameToIndex("pk_org");
				int pkGroupIndex = ds.nameToIndex(PK_GROUP);
				if (pkOrgIndex >= 0) {
					row.setValue(pkOrgIndex, pkOrg);
					row.setValue(pkGroupIndex, pkOrg);
				}
			}
			// String pkGroup = this.getCurrentAppCtx().getAppEnvironment()
			// .getPk_group();
			// if (pkGroup != null) {
			// int pkGroupIndex = ds.nameToIndex(PK_GROUP);
			// if (pkGroupIndex >= 0) {
			// row.setValue(pkGroupIndex, pkGroup);
			// }
			// }
		}
  }
  /** 
 * ������ѡ���߼�
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent dsEvent){
    Dataset ds = dsEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()) {
			protected void updateButtons() {

			}
		});
		String value = (String) ds.getValue(ds
				.nameToIndex(ScapptApplyHVO.PK_APPLY_H));
		queryApproveInfo(this.getPkTask(), value);
		// queryApprove(ds);�ź�
		// Dataset attachDs = CpWinUtil.getDataset("apply_attach");
		// if (attachDs.getRowCount() <= 0) {
		// Row row = ds.getSelectedRow();
		// String cchangeType = (String) row.getValue(ds
		// .nameToIndex(ScapptTransferHVO.CCHANGETYPE));
		// String ctradetype = (String) row.getValue(ds
		// .nameToIndex(ScapptTransferHVO.CTRADETYPE));
		// String cgreattype = (String) row.getValue(ds
		// .nameToIndex(ScapptTransferHVO.CGREATTYPE));
		// String cprocessType = (String) row.getValue(ds
		// .nameToIndex(ScapptTransferHVO.CPROCESSTYPE));
		// queryAttach(CpWinUtil.getDataset("apply_attach"),
		// ScapPAPPubmethod.getAttachType(cchangeType, ctradetype,
		// cgreattype, cprocessType), value);
		// }
  }
  /** 
 * ����
 */
  public void onAdd(  MouseEvent<?> mouseEvent) throws BusinessException {
    this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_BillID,
				null);
		this.resetWfmParameter();
		this.getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_FolwTypePk, this.getFlwTypePk());

		CmdInvoker.invoke(new UifAddCmd(this.getMasterDsId()) {
			protected void onBeforeRowAdd(Row row) {
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
		if (row == null) {
			throw new LfwRuntimeException("��ѡ������!");
		}
		try {
			List<Dataset> list = new ArrayList<Dataset>(1);
			list.add(masterDs);
			DefaultPrintService printService = new DefaultPrintService();
			printService.setDatasetList(list);
			ICpPrintTemplateService service = ServiceLocator
					.getService(ICpPrintTemplateService.class);
			service.print(printService, null, this.getNodeCode());
		} catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
  }
  private String getNodeCode(){
    return "�����˴�ӡģ��Ĺ��ܽڵ��nodecode";
  }
  private String getFlwTypePk(){
    return "0001ZG100000000EPTAX";
  }
  private void resetWfmParameter(){
    this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_TaskPk,
				null);
		this.getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_ScratchPad, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE, null);
		this.getCurrentAppCtx().addAppAttribute(
				WfmConstants.AttachFileList_Temp_Billitem, null);
		this.getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_ProInsPk, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.RETURN_PK_TASK,
				null);
		this.getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_FormInFoCtx_Billitem, null);
  }
  /** 
 * ɾ��
 */
  public void onDelete(  MouseEvent<?> mouseEvent) throws BusinessException {
    String pk_form = LfwRuntimeEnvironment.getWebContext().getWebSession()
				.getOriginalParameter("openBillId");
		if (pk_form == null) {
			pk_form = (String) LfwRuntimeEnvironment.getWebContext()
					.getWebSession().getOriginalParameter("pk_apply_h");
			LfwRuntimeEnvironment.getWebContext().getWebSession()
					.addOriginalParameter("openBillId", pk_form);
		}
		if (pk_form != null && !pk_form.equals("")) {
			boolean isCanDel = WfmCPUtilFacade.isCanDelBill(pk_form);
			if (isCanDel) {
				WfmCPUtilFacade.delWfmInfo(pk_form);
				CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
			} else {
				throw new LfwRuntimeException("�������������޷�ɾ������");
			}
		} else {
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
    this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_BillID,
				null);
		this.resetWfmParameter();
		CmdInvoker.invoke(new UifCopyCmd(this.getMasterDsId()));
  }
  /** 
 * ����У��
 * @param mainvo
 * @return
 */
  public String checkData(  ScapptApplyHVO mainvo){
    String err = "";
		if (mainvo == null) {
			return "����Ϣ����";
		}
//		err = checkDataType(mainvo);
		ScapptApplyBVO[] bvos = mainvo.getId_scappt_apply_b();
		if (bvos == null || bvos.length <= 0) {
			return "�����������鲻��Ϊ��!";
		}
		List<ScapptApplyBVO> childvos = new ArrayList<ScapptApplyBVO>();
		for (ScapptApplyBVO tmp : bvos) {
			if (tmp.getStatus() == VOStatus.DELETED) {
				continue;
			}
			childvos.add(tmp);
			if (tmp.getDbpercent().doubleValue() > 100) {
				err += "��ת�ɱ������ܳ���100%" + "<br>";
			}
		}
		for (int i = 0; i < childvos.size() - 1; i++) {
			for (int j = i + 1; j <= childvos.size() - i - 1; j++) {
				if (childvos.get(i).getPk_borg()
						.equals(childvos.get(j).getPk_borg())) {
					err += "������ͬ�����ҵ!<br>";
				}
			}
		}
		return err;
  }
  /** 
 * ����ǿ��ֶ��л����̾������
 * @param mainvo
 * @return
 */
  public String checkDataType(  ScapptApplyHVO mainvo){
    DefdocVO defvo = ScapDefUtil.getDefDocVo(mainvo.getCchangetype());
		String change_code = defvo.getCode();
		if (change_code.equals(PtConstants.CCHANGE_TYPE_CQZR)) {
			if (mainvo.getCtradetype() == null
					|| "".equals(mainvo.getCtradetype())) {
				return "��Ȩ���׷�ʽ����Ϊ�գ�";
			}
		} else if (change_code.equals(PtConstants.CCHANGE_TYPE_MAJOR)) {
			String cgre = mainvo.getCgreattype();
			String cprocesstype = mainvo.getCprocesstype();
			if (cgre == null || "".equals(cgre)) {
				return "�ش��ʲ����÷�ʽ����Ϊ�գ�";
			}
			if (cprocesstype == null || "".equals(cprocesstype)) {
				return "�����������Ͳ���Ϊ�գ�";
			}
		}
		return "";
  }
  public void doTaskExecute(  Map keys){
    CpPubMethod.doValidate(this.getCurrentView(), this.getMasterDs(),
				Arrays.asList(this
						.getDetailDs(new String[] { "scappt_apply_b" })), null,
				false);
		WfmFormInfoCtx formCtx = this.getWfmFormInfoCtx();
		ScapptApplyHVO mainvo = (ScapptApplyHVO) formCtx;
		String err = checkData(mainvo);
		if (err != null && !"".equals(err)) {
			AppInteractionUtil.showShortMessage(err);
			return;
		}
		// ��������form
		this.getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_FormInFoCtx, formCtx);
		// ������������pk
		this.getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_FolwTypePk,
				CpPubMethod.getFlwTypePk(ScapPtMethod
						.getNodeCode((ScapptApplyHVO) formCtx)));
		// ��������pk
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_TaskPk,
				this.getPkTask());
		// ��������
		CmdInvoker.invoke(new WfmCmd());
		if (CommandStatus.SUCCESS.equals(CommandStatus.getCommandStatus())) {
			CmdInvoker.invoke(new UifUpdateUIDataCmdRV((SuperVO) formCtx,
					getMasterDsId()));
			// this.setFormIsNull(this.getMasterDs(), "scappt_apply_h_form",
			// new String[] { "cchangetype_name" });
			// ����ҳ��ı�״̬
			AppLifeCycleContext.current().getViewContext().getView()
					.getPagemeta().setHasChanged(false);
			// �رյ�ǰ����
			AppLifeCycleContext
					.current()
					.getApplicationContext()
					.getCurrentWindowContext()
					.closeView(
							AppLifeCycleContext.current().getViewContext()
									.getId());
			this.getCurrentAppCtx().closeWinDialog();
			CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(),
					PLUGOUT_ID));
		}
  }
  /** 
 * �ӱ�����
 */
  public void onGridAddClick(  MouseEvent<?> mouseEvent){
    GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		Row emptyRow = ds.getEmptyRow();
		Row mainRow = this.getMasterDs().getSelectedRow();
		String pk = (String) mainRow.getValue(this.getMasterDs().nameToIndex(
				ScapptApplyHVO.PK_APPLY_H));
		emptyRow.setValue(ds.nameToIndex(ScapptApplyBVO.PK_APPLY_H), pk);
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
    return "scappt_apply_h";
  }
  protected WfmFormInfoCtx getWfmFormInfoCtx(){
    Dataset masterDs = this.getMasterDs();
		Dataset[] detailDss = this.getDetailDs(this.getDetailDsIds());
		SuperVO richVO = this.getDs2RichVOSerializer().serialize(masterDs,
				detailDss, this.getRichVoClazz());
		CpPubMethod.fillCachedDeletedVO(richVO, detailDss);
		return (WfmFormInfoCtx) richVO;
  }
  protected String getRichVoClazz(){
    return WfmFlwFormVO.class.getName();
  }
  public void onSave(  MouseEvent mouseEvent){
    Dataset masterDs = this.getMasterDs();
		CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this
				.getDetailDsIds(), false) {
			protected void onVoSave(SuperVO aggVo) {
				try {
					String errmsg = checkData((ScapptApplyHVO) aggVo);// ��ȡ������Ϣ
					if (errmsg != null && !errmsg.equals("")) {
						throw new LfwRuntimeException(errmsg).setTitle("��ʾ");
					}
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
		// ����ҳ��ı�״̬
		AppLifeCycleContext.current().getViewContext().getView().getPagemeta()
				.setHasChanged(false);
		// �رյ�ǰ����
		AppLifeCycleContext
				.current()
				.getApplicationContext()
				.getCurrentWindowContext()
				.closeView(
						AppLifeCycleContext.current().getViewContext().getId());
		this.getCurrentAppCtx().closeWinDialog();

		CmdInvoker.invoke(new UifPlugoutCmd(this.getCurrentView().getId(),
				PLUGOUT_ID));
  }
  /** 
 * ��ѯģ���Ӧ����
 * @param ds
 */
  public void queryAttach(  Dataset ds,  String code,  String value){
    String sql = "select * from scappt_attachent sa where sa.id_entity=(select sc.pk_filetype from scappt_attachmgr sc where sc.v_code='"
				+ code + "')  order by v_num asc";
		try {
			ScapLogger.debug(sql);
			List<AttachEntVO> vos = (List<AttachEntVO>) ScapDAO
					.getBaseDAO()
					.executeQuery(sql, new BeanListProcessor(AttachEntVO.class));
			if (vos != null && vos.size() > 0) {
				for (AttachEntVO tmpvo : vos) {
					Row row = ds.getEmptyRow();
					row.setValue(
							ds.nameToIndex(ScapptApplyAttachVO.ATTACH_NAME),
							tmpvo.getPk_attchfile());
					row.setValue(
							ds.nameToIndex(ScapptApplyAttachVO.PK_APPLY_ATTACH),
							this.generatePk());
					row.setValue(
							ds.nameToIndex(ScapptApplyAttachVO.SUBMITSTATE),
							"1");
					row.setValue(
							ds.nameToIndex(ScapptApplyAttachVO.PK_APPLY_H),
							value);
					ds.addRow(row);
				}
			}
		} catch (DAOException e) {
			ScapLogger.error(sql);
			throw new LfwRuntimeException("��ѯ����ʱ����!");

		}
  }
  /** 
 * ͨ��������Ϣ����
 * @param taskPK
 * @param pkValue
 */
  public void queryApproveInfo(  String taskPK,  String pkValue){
    if (taskPK == null || "".equals(taskPK) || pkValue == null
				|| "".equals(pkValue)) {
			return;
		}
		ProDef pd = (ProDef) WfmTaskUtil.getProDefByTask(WfmTaskUtil
				.getTaskFromSessionCache(taskPK));
		Map<String, HumAct> humacts = pd.getHumActs();
		Map<String, String> tmphum = new HashMap<String, String>();
		Iterator<Map.Entry<String, HumAct>> its = humacts.entrySet().iterator();
		Iterator<Map.Entry<String, HumAct>> abits = humacts.entrySet()
				.iterator();
		while (its.hasNext()) {
			Entry<String, HumAct> tmpa = its.next();
			tmphum.put(tmpa.getKey(), tmpa.getValue().getName());
		}
		Iterator<Map.Entry<String, String>> tmpits = tmphum.entrySet()
				.iterator();
		StringBuffer sb = new StringBuffer("");
//		int k = 0;
//		while (tmpits.hasNext()) {
//			k++;
			Entry<String, String> ens = tmpits.next();
			StringBuffer sb1 = new StringBuffer(
					"select * from ( select case wt.port_id ");
			while (abits.hasNext()) {
				Entry<String, HumAct> entry = abits.next();
				sb1.append(" when '" + entry.getKey() + "' then '"
						+ entry.getValue().getName() + "' ");
			}
			sb1.append(" else '' end ��Ϣ  ,CU.USER_NAME ����� ,WT.ts ����, WT.SYSEXT8 ���,WT.OPINION �⽨  from wfm_task wt left join CP_USER cu on CU.CUSERID = WT.PK_OWNER where 1=1 and nvl(WT.DR,0) = 0 and nvl(CU.DR,0) = 0  and  WT.PK_FRMINS = '"
					+ pkValue
//					+ "' and WT.PORT_ID ='"
//					+ ens.getKey()
					+ "' AND WT.Sysext8 IS NOT NULL order by wt.ts desc )" 
//					+" where rownum < 2 " 
					+" ");
//			if (k != tmphum.size()) {
//				sb1.append(" union all ");
//			}
			sb.append(sb1.toString());
			abits = humacts.entrySet().iterator();// ����
//		}
		try {
			ScapLogger.error(sb.toString());
			List list = (List) new BaseDAO().executeQuery(sb.toString(),
					new ArrayListProcessor());
			if (list != null) {
				Dataset dsCh = this.getCurrentView().getViewModels()
						.getDataset("approveds");
				Object[][] objArray = new Object[list.size()][5];
				for (int i = 0; i < list.size(); i++) {
					objArray[i] = (Object[]) list.get(i);

					Row emptyRow = dsCh.getEmptyRow();
					emptyRow.setValue(dsCh.nameToIndex("info"), objArray[i][0]);// ��Ϣ
					emptyRow.setValue(dsCh.nameToIndex("pk_user_name"),
							objArray[i][1]);// �����
					emptyRow.setValue(dsCh.nameToIndex("use_date"),
							objArray[i][2]);// ����
					emptyRow.setValue(dsCh.nameToIndex("result"),
							objArray[i][3]);// ���
					emptyRow.setValue(dsCh.nameToIndex("suggest"),
							objArray[i][4]);// ���
					dsCh.addRow(emptyRow);
					// dsCh.setRowSelectIndex(ds.getRowIndex(emptyRow));
				}
			}

		} catch (DAOException e) {
			ScapLogger.error(sb.toString());
			throw new LfwRuntimeException("��ѯ������Ϣʱ����!");
		}
  }
  public void queryApprove(  Dataset ds){
    Row row = ds.getSelectedRow();
		if (row == null)
			return;
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));
		String formstate = (String) row.getValue(ds
				.nameToIndex(ScapptApplyHVO.FORMSTATE));

		String sql = " select * from (																																 "
				+ " select                                                                          "
				+ " case WT.PORT_ID                                                                 "
				+ " when '3' then '������Ϣ'                                                        "
				+ " when '4' then '������ҵԤ����Ϣ'                                                "
				+ " when '5' then '����ίԤ����Ϣ'                                                  "
				+ " when '6' then '������ҵ������Ϣ'                                                "
				+ " when '7' then '����ί������Ϣ'                                                  "
				+ " else '' end ��Ϣ                                                                "
				+ " ,CU.USER_NAME ����� ,WT.ts ����, WT.SYSEXT8 ���,WT.OPINION �⽨ ,wt.sysext8   "
				+ " from wfm_task wt                                                                "
				+ " left join CP_USER cu on CU.CUSERID = WT.PK_OWNER                                "
				+ " where 1=1                                                                       "
				+ " and nvl(WT.DR,0) = 0 and nvl(CU.DR,0) = 0                                       "
				+ " and  WT.PK_FRMINS = '"
				+ pkValue
				+ "' and WT.PORT_ID = '3'                 "
				+ " order by wt.ts desc ) where rownum < 2                                          "
				+ "                                                                                 "
				+ " union all                                                                       "
				+ "                                                                                 "
				+ " select * from (                                                                 "
				+ " select                                                                          "
				+ " case WT.PORT_ID                                                                 "
				+ " when '3' then '������Ϣ'                                                        "
				+ " when '4' then '������ҵԤ����Ϣ'                                                "
				+ " when '5' then '����ίԤ����Ϣ'                                                  "
				+ " when '6' then '������ҵ������Ϣ'                                                "
				+ " when '7' then '����ί������Ϣ'                                                  "
				+ " else '' end ��Ϣ                                                                "
				+ " ,CU.USER_NAME ����� ,WT.ts ����, WT.SYSEXT8 ���,WT.OPINION �⽨ ,wt.sysext8   "
				+ " from wfm_task wt                                                                "
				+ " left join CP_USER cu on CU.CUSERID = WT.PK_OWNER                                "
				+ " where 1=1                                                                       "
				+ " and nvl(WT.DR,0) = 0 and nvl(CU.DR,0) = 0                                       "
				+ " and  WT.PK_FRMINS = '"
				+ pkValue
				+ "' and WT.PORT_ID = '4'                 "
				+ " order by wt.ts desc ) where rownum < 2                                          "
				+ "                                                                                 "
				+ " union all                                                                       "
				+ "                                                                                 "
				+ " select * from (                                                                 "
				+ " select                                                                          "
				+ " case WT.PORT_ID                                                                 "
				+ " when '3' then '������Ϣ'                                                        "
				+ " when '4' then '������ҵԤ����Ϣ'                                                "
				+ " when '5' then '����ίԤ����Ϣ'                                                  "
				+ " when '6' then '������ҵ������Ϣ'                                                "
				+ " when '7' then '����ί������Ϣ'                                                  "
				+ " else '' end ��Ϣ                                                                "
				+ " ,CU.USER_NAME ����� ,WT.ts ����, WT.SYSEXT8 ���,WT.OPINION �⽨ ,wt.sysext8   "
				+ " from wfm_task wt                                                                "
				+ " left join CP_USER cu on CU.CUSERID = WT.PK_OWNER                                "
				+ " where 1=1                                                                       "
				+ " and nvl(WT.DR,0) = 0 and nvl(CU.DR,0) = 0                                       "
				+ " and  WT.PK_FRMINS = '"
				+ pkValue
				+ "' and WT.PORT_ID = '5'                 "
				+ " order by wt.ts desc ) where rownum < 2                                          "
				+ "                                                                                 "
				+ " union all                                                                       "
				+ "                                                                                 "
				+ " select * from (                                                                 "
				+ " select                                                                          "
				+ " case WT.PORT_ID                                                                 "
				+ " when '3' then '������Ϣ'                                                        "
				+ " when '4' then '������ҵԤ����Ϣ'                                                "
				+ " when '5' then '����ίԤ����Ϣ'                                                  "
				+ " when '6' then '������ҵ������Ϣ'                                                "
				+ " when '7' then '����ί������Ϣ'                                                  "
				+ " else '' end ��Ϣ                                                                "
				+ " ,CU.USER_NAME ����� ,WT.ts ����, WT.SYSEXT8 ���,WT.OPINION �⽨ ,wt.sysext8   "
				+ " from wfm_task wt                                                                "
				+ " left join CP_USER cu on CU.CUSERID = WT.PK_OWNER                                "
				+ " where 1=1                                                                       "
				+ " and nvl(WT.DR,0) = 0 and nvl(CU.DR,0) = 0                                       "
				+ " and  WT.PK_FRMINS = '"
				+ pkValue
				+ "' and WT.PORT_ID = '6'                 "
				+ " order by wt.ts desc ) where rownum < 2                                          "
				+ "                                                                                 "
				+ " union all                                                                       "
				+ "                                                                                 "
				+ " select * from (                                                                 "
				+ " select                                                                          "
				+ " case WT.PORT_ID                                                                 "
				+ " when '3' then '������Ϣ'                                                        "
				+ " when '4' then '������ҵԤ����Ϣ'                                                "
				+ " when '5' then '����ίԤ����Ϣ'                                                  "
				+ " when '6' then '������ҵ������Ϣ'                                                "
				+ " when '7' then '����ί������Ϣ'                                                  "
				+ " else '' end ��Ϣ                                                                "
				+ " ,CU.USER_NAME ����� ,WT.ts ����, WT.SYSEXT8 ���,WT.OPINION �⽨ ,wt.sysext8   "
				+ " from wfm_task wt                                                                "
				+ " left join CP_USER cu on CU.CUSERID = WT.PK_OWNER                                "
				+ " where 1=1                                                                       "
				+ " and nvl(WT.DR,0) = 0 and nvl(CU.DR,0) = 0                                       "
				+ " and  WT.PK_FRMINS = '"
				+ pkValue
				+ "' and WT.PORT_ID = '7'                 "
				+ " order by wt.ts desc ) where rownum < 2                                          ";
		try {
			ScapLogger.error(sql);
			List list = (List) new BaseDAO().executeQuery(sql,
					new ArrayListProcessor());
			if (list != null) {
				Dataset dsCh = this.getCurrentView().getViewModels()
						.getDataset("approveds");
				Object[][] objArray = new Object[list.size()][5];
				for (int i = 0; i < list.size(); i++) {
					objArray[i] = (Object[]) list.get(i);

					Row emptyRow = dsCh.getEmptyRow();
					emptyRow.setValue(dsCh.nameToIndex("info"), objArray[i][0]);// ��Ϣ
					emptyRow.setValue(dsCh.nameToIndex("pk_user_name"),
							objArray[i][1]);// �����
					emptyRow.setValue(dsCh.nameToIndex("use_date"),
							objArray[i][2]);// ����
					emptyRow.setValue(dsCh.nameToIndex("result"),
							objArray[i][3]);// ���
					emptyRow.setValue(dsCh.nameToIndex("suggest"),
							objArray[i][4]);// ���
					dsCh.addRow(emptyRow);
					// dsCh.setRowSelectIndex(ds.getRowIndex(emptyRow));
				}
			}

		} catch (DAOException e) {
			ScapLogger.error(sql);
			throw new LfwRuntimeException("��ѯ������Ϣʱ����!");
		}
  }
  public void onlook(  MouseEvent mouseEvent){
    
  }
  public void onApprove(  MouseEvent mouseEvent){
    
  }
  public void setCardDefault(  Row row,  Dataset masterDs){
    String node_type = (String) AppUtil
				.getAppAttr(IGlobalConstants.NODE_TYPE);
		if (node_type == null)
			return;
		if (node_type.endsWith(PtConstants.ZDZCGPZR)) {
			// �ش��ʲ�����
			// pos �ش��ʲ�����ʽ 1001ZG10000000009Y63���� 1001ZG10000000009Y64Э��
			// 1001ZG10000000009Y65����
			// pos ��Ȩ�䶯���� 1001ZG10000000009Y3H 1001ZG10000000009Y3I
			// 1001ZG10000000009Y3J 1001ZG10000000009Y3K�ش��ʲ�
			row.setValue(
					masterDs.nameToIndex(ScapptApplyHVO.CCHANGETYPE),
					ScapPtMethod.getDefdocByListCodeAndCode(
							PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_MAJOR).getPk_defdoc());// 1001ZG10000000009Y3K
			row.setValue(
					masterDs.nameToIndex(ScapptApplyHVO.CGREATTYPE),
					ScapPtMethod.getDefdocByListCodeAndCode(
							PtConstants.SCAPPT_ZDZC,
							PtConstants.MAJORLISTMeTHODS).getPk_defdoc());// 1001ZG10000000009Y63
		} else if (node_type.endsWith(PtConstants.ZDZCXYZR)) {
			// �ش��ʲ�Э��
			row.setValue(
					masterDs.nameToIndex(ScapptApplyHVO.CCHANGETYPE),
					ScapPtMethod.getDefdocByListCodeAndCode(
							PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_MAJOR).getPk_defdoc());// 1001ZG10000000009Y3K
			row.setValue(
					masterDs.nameToIndex(ScapptApplyHVO.CGREATTYPE),
					ScapPtMethod.getDefdocByListCodeAndCode(
							PtConstants.SCAPPT_ZDZC,
							PtConstants.MAJORAGREEMeTHODS).getPk_defdoc());// 1001ZG10000000009Y63
		} else if (node_type.endsWith(PtConstants.ZDZCXYZR)) {
			// �ش��ʲ�����
			row.setValue(
					masterDs.nameToIndex(ScapptApplyHVO.CCHANGETYPE),
					ScapPtMethod.getDefdocByListCodeAndCode(
							PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_MAJOR).getPk_defdoc());// 1001ZG10000000009Y3K
			row.setValue(
					masterDs.nameToIndex(ScapptApplyHVO.CGREATTYPE),
					ScapPtMethod.getDefdocByListCodeAndCode(
							PtConstants.SCAPPT_ZDZC,
							PtConstants.MAJORLEASEMeTHODS).getPk_defdoc());// 1001ZG10000000009Y63
		} else if (node_type.endsWith(PtConstants.GPZR)) {
			// ����
			row.setValue(
					masterDs.nameToIndex(ScapptApplyHVO.CCHANGETYPE),
					ScapPtMethod.getDefdocByListCodeAndCode(
							PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_CQZR).getPk_defdoc());
			row.setValue(
					masterDs.nameToIndex(ScapptApplyHVO.CTRADETYPE),
					ScapPtMethod.getDefdocByListCodeAndCode(
							PtConstants.SCAPPT_CQJY,
							PtConstants.CTRADETTYPE_GPZR).getPk_defdoc());
		} else if (node_type.endsWith(PtConstants.XYZR)) {
			// Э��
			row.setValue(
					masterDs.nameToIndex(ScapptApplyHVO.CCHANGETYPE),
					ScapPtMethod.getDefdocByListCodeAndCode(
							PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_CQZR).getPk_defdoc());
			row.setValue(
					masterDs.nameToIndex(ScapptApplyHVO.CTRADETYPE),
					ScapPtMethod.getDefdocByListCodeAndCode(
							PtConstants.SCAPPT_CQJY,
							PtConstants.CTRADETTYPE_XYZR).getPk_defdoc());
		} else if (node_type.endsWith(PtConstants.WCZR)) {
			// �޳�
			row.setValue(
					masterDs.nameToIndex(ScapptApplyHVO.CCHANGETYPE),
					ScapPtMethod.getDefdocByListCodeAndCode(
							PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_CQZR).getPk_defdoc());
			row.setValue(
					masterDs.nameToIndex(ScapptApplyHVO.CTRADETYPE),
					ScapPtMethod.getDefdocByListCodeAndCode(
							PtConstants.SCAPPT_CQJY,
							PtConstants.CTRADETTYPE_WCZR).getPk_defdoc());
		} else if (node_type.endsWith(PtConstants.BTBLZZ)) {
			// ����
			row.setValue(
					masterDs.nameToIndex(ScapptApplyHVO.CCHANGETYPE),
					ScapPtMethod.getDefdocByListCodeAndCode(
							PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_TBLJZ).getPk_defdoc());
		} else if (node_type.endsWith(PtConstants.TBLZZ)) {
			// ͬ��������
			row.setValue(
					masterDs.nameToIndex(ScapptApplyHVO.CCHANGETYPE),
					ScapPtMethod.getDefdocByListCodeAndCode(
							PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_BLZZ).getPk_defdoc());
		} else if (node_type.endsWith(PtConstants.BTBLJZ)) {
			// ����
			row.setValue(
					masterDs.nameToIndex(ScapptApplyHVO.CCHANGETYPE),
					ScapPtMethod.getDefdocByListCodeAndCode(
							PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_BLZZ).getPk_defdoc());
		} else if (node_type.endsWith(PtConstants.QYBG)) {
			// ����
			row.setValue(
					masterDs.nameToIndex(ScapptTransferHVO.CCHANGETYPE),
					ScapPtMethod.getDefdocByListCodeAndCode(
							PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_QYBG).getPk_defdoc());
		}
  }
  /** 
 * ����
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onAttchFile(  MouseEvent<?> mouseEvent){
    Dataset ds = this.getMasterDs();
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("��ѡ������!");
		}
		uap.web.bd.pub.AppUtil.addAppAttr(LfwFileConstants.SYSID,
				LfwFileConstants.SYSID_BAFILE);
		String primaryKey = ds.getPrimaryKeyField();
		if (primaryKey == null) {
			throw new LfwRuntimeException("��ǰDatasetû����������!");
		}
		String primaryKeyValue = (String) row.getValue(ds
				.nameToIndex(primaryKey));

		String taskPk = WfmUtilFacade.getTaskPkPkByPkFormIns(primaryKeyValue);
//		// �����ǰ�˲��ǵ�ǰ���ݵĲ�����,��ʱȡ��������.����ֻ�ܲ鿴,��Ҫ��Ϊ���̬.
//		if (taskPk == null || taskPk.equals("")) {
//			this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE,
//					WfmConstants.BILLSTATE_BROWSE);
//		}

		// ���̸�������
		Map<String, String> wfmParam = WfmUtilFacade
				.getFileMgrParamsByTask(taskPk);

		// ��������
		Map<String, String> param = UploadFileHelper.BuildDefaultPamater("",
				primaryKeyValue, CommonObjectConstants.AttachFileType, "");
		param.put("usescanable", "true");
		param.put("state", String.valueOf(31));

		String title = "����";
//		if (wfmParam != null && !wfmParam.isEmpty()) {
//			param.putAll(wfmParam);
//		}

		CmdInvoker.invoke(new UifAttachCmd(title, param));
  }
  public void onAttach(  MouseEvent mouseEvent){
    GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("��ѡ������!");
		}
		String primaryKey = ds.getPrimaryKeyField();
		if (primaryKey == null) {
			throw new LfwRuntimeException("��ǰDatasetû����������!");
		}
		int cstate = 1;
		boolean flag = CntUtil.CtnUserIsCompanyUser();
		if (flag) {
			cstate = 31;
		} else {
			cstate = 9;
		}
		String oper = this.getOperator();
		if ("look".equals(oper)) {
			cstate = 1;
		}
		String primaryKeyValue = (String) row.getValue(ds
				.nameToIndex(primaryKey));
		ScapptApplyAttachVO[] attvos = (ScapptApplyAttachVO[]) new Dataset2SuperVOSerializer<SuperVO>()
				.serialize(ds, ds.getCurrentRowSet().getCurrentRowData()
						.getRows());
		AppUtil.addAppAttr("attvos", attvos);
		AppUtil.addAppAttr(LfwFileConstants.SYSID,
				LfwFileConstants.SYSID_BAFILE);
		Map<String, String> param = UploadFileHelper.BuildDefaultPamater(
				LfwFileConstants.SYSID_BAFILE, primaryKeyValue,
				CommonObjectConstants.AttachFileType, "");
		param.put("usescanable", "true");
		param.put("state", String.valueOf(cstate));
		param.put(LfwFileConstants.Filemgr_Para_OperateClazz,
				"nc.scap.transfer.fileback.TraFileBackImp");
		String title = "����";
		CmdInvoker.invoke(new UifAttachCmd(title, param));
  }
  /** 
 * ���ݲ�Ȩ���ͱ䶯+���׷�ʽ+�ش��ʲ����÷�ʽ+�����������͡���� �Զ������㽭ʡ���ڲ�ͬ���Ͳ�Ȩ�䶯,�ϴ���ͬ����,�߲�ͬ����������Ҫ��.
 * CCHANGETYPE���䶯���͡���CTRADETYPE�䶯��ʽ����CGREATTYPE�ش��á���CPROCESSTYPE������������
 * @param datasetCellEvent
 */
  public void onAfterDataChange(  DatasetCellEvent datasetCellEvent){
    Dataset ds = datasetCellEvent.getSource();
		Row mainRow = this.getMasterDs().getSelectedRow();
		String pk = (String) mainRow.getValue(this.getMasterDs().nameToIndex(
				ScapptApplyHVO.PK_APPLY_H));
		String c_type = (String) mainRow.getValue(this.getMasterDs()
				.nameToIndex(ScapptApplyHVO.CPROCESSTYPE));
		// if (ds.nameToIndex(ScapptApplyHVO.CCHANGETYPE) == datasetCellEvent
		// .getColIndex()) {
		// String newvalue = (String) datasetCellEvent.getNewValue();
		// if (newvalue == null || newvalue.equals(""))
		// return;
		// DefdocVO defvo = ScapDefUtil.getDefDocVo(newvalue);
		// String change_code = defvo.getCode();
		// if (change_code.equals(PtConstants.CCHANGE_TYPE_CQZR)) {
		// setForm(ds, "scappt_apply_h_form", true, new String[] {
		// "ctradetype_name", "cchangetype_name", "pk_group_name",
		// "pk_rorg_vname", "cprocesstype_name" });
		// setChildGrid(ds, true);// �����ӱ��ش��ʲ�ʱ������У���������
		// clearChildData(ds, "scappt_apply_b_grid", true);
		// } else if (change_code.equals(PtConstants.CCHANGE_TYPE_BLZZ)
		// || change_code.equals(PtConstants.CCHANGE_TYPE_BLJZ)
		// || change_code.equals(PtConstants.CCHANGE_TYPE_QYBG)) {
		// setForm(ds, "scappt_apply_h_form", true, new String[] {
		// "cchangetype_name", "pk_group_name", "pk_rorg_vname",
		// "cprocesstype_name" });
		// // setFormIsNull(ds, "scappt_apply_h_form",
		// // new String[] { "ctradetype_name" });//��Ȩת�÷�ʽ����Ϊ��
		// setChildGrid(ds, true);// �����ӱ��ش��ʲ�ʱ������У���������
		// clearChildData(ds, "scappt_apply_b_grid", true);
		// Dataset appDs = CpWinUtil.getDataset("apply_attach");
		// appDs.clear();
		// queryAttach(appDs, ScapPAPPubmethod.getAttachType(newvalue,
		// null, null, c_type), pk);
		// } else if (change_code.equals(PtConstants.CCHANGE_TYPE_MAJOR)) {
		// setForm(ds, "scappt_apply_h_form", true, new String[] {
		// "cgreattype_name", "cprocesstype_name",
		// "cchangetype_name", "pk_group_name", "pk_rorg_vname" });
		// setChildGrid(ds, false);// �����ӱ��ش��ʲ�ʱ������У���������
		// clearChildData(ds, "scappt_apply_b_grid", false);
		// }
		// clearChangeType(ds, "scappt_apply_h_form");// ���ͱ��֮���������
		//
		// }
		if (ds.nameToIndex(ScapptApplyHVO.CTRADETYPE) == datasetCellEvent
				.getColIndex()) {
			Row row = ds.getSelectedRow();
			String newvalue = (String) datasetCellEvent.getNewValue();
			if (newvalue == null || newvalue.equals("")) {
				return;
			}
			Dataset appDs = CpWinUtil.getDataset("apply_attach");
			appDs.clear();
			queryAttach(appDs, ScapPAPPubmethod.getAttachType((String) row
					.getValue(ds.nameToIndex(ScapptTransferHVO.CCHANGETYPE)),
					newvalue, null, c_type), pk);
		}
		if (ds.nameToIndex(ScapptApplyHVO.CGREATTYPE) == datasetCellEvent
				.getColIndex()) {
			Row row = ds.getSelectedRow();
			String newvalue = (String) datasetCellEvent.getNewValue();
			if (newvalue == null || newvalue.equals("")) {
				// setFormIsNull(ds, "scappt_apply_h_form", new String[] {
				// "cgreattype", "cprocesstype" });
				return;
			}
			Dataset appDs = CpWinUtil.getDataset("apply_attach");
			appDs.clear();
			queryAttach(appDs, ScapPAPPubmethod.getAttachType((String) row
					.getValue(ds.nameToIndex(ScapptTransferHVO.CCHANGETYPE)),
					null, newvalue, c_type), pk);
		}
		if (ds.nameToIndex(ScapptApplyHVO.CPROCESSTYPE) == datasetCellEvent
				.getColIndex()) {
			String newvalue = (String) datasetCellEvent.getNewValue();
			if (newvalue == null || newvalue.equals("")) {
				return;
			}
			Row row = ds.getSelectedRow();
			String cchangetype = (String) row.getValue(this.getMasterDs()
					.nameToIndex(ScapptApplyHVO.CCHANGETYPE));
			DefdocVO defvo = ScapDefUtil.getDefDocVo(cchangetype);
			String change_code = defvo.getCode();
			if (change_code.equals(PtConstants.CCHANGE_TYPE_QYBG)) {
				Dataset appDs = CpWinUtil.getDataset("apply_attach");
				appDs.clear();
				queryAttach(appDs, ScapPAPPubmethod.getAttachType(cchangetype,
						null, null, newvalue), pk);
			}
		}
  }
  /** 
 * ���ݽڵ�������ò�Ȩ���׷�ʽ״̬
 */
  public void setFormElementAble(  Dataset ds,  Row row){
    String node = (String)AppUtil.getAppAttr(PtConstants.NODE_TYPE2);
		if(node==null){
			return;
		}
		FormComp form = (FormComp) ds.getView().getViewComponents()
				.getComponent("scappt_apply_h_form");
		FormElement fe = form.getElementById("ctradetype_name");
		if(node.equals(PtConstants.GPZR)){
			DefdocVO defvo = ScapDefUtil.getDefdocByListCodeAndCode(
					"scappt_0003", "001");
			if (defvo != null) {
				row.setValue(ds
						.nameToIndex(ScapptApplyHVO.CTRADETYPE),
						defvo.getPk_defdoc());
			}
			fe.setEnabled(false);
		}else if(node.equals(PtConstants.XYZR)){
			DefdocVO defvo = ScapDefUtil.getDefdocByListCodeAndCode(
					"scappt_0003", "002");
			if (defvo != null) {
				row.setValue(ds
						.nameToIndex(ScapptApplyHVO.CTRADETYPE),
						defvo.getPk_defdoc());
			}
			fe.setEnabled(false);
		}else if(node.equals(PtConstants.WCZR)){
			DefdocVO defvo = ScapDefUtil.getDefdocByListCodeAndCode(
					"scappt_0003", "003");
			if (defvo != null) {
				row.setValue(ds
						.nameToIndex(ScapptApplyHVO.CTRADETYPE),
						defvo.getPk_defdoc());
			}
			fe.setEnabled(false);
		}else if(node.equals(PtConstants.BTBLZZ)){
			DefdocVO defvo = ScapDefUtil.getDefdocByListCodeAndCode(
					"scappt_0003", "005");
			if (defvo != null) {
				row.setValue(ds
						.nameToIndex(ScapptApplyHVO.CTRADETYPE),
						defvo.getPk_defdoc());
			}
			fe.setEnabled(false);
		}else if(node.equals(PtConstants.TBLZZ)){
			DefdocVO defvo = ScapDefUtil.getDefdocByListCodeAndCode(
					"scappt_0003", "004");
			if (defvo != null) {
				row.setValue(ds
						.nameToIndex(ScapptApplyHVO.CTRADETYPE),
						defvo.getPk_defdoc());
			}
			fe.setEnabled(false);
		}else if(node.equals(PtConstants.BTBLJZ)){
			DefdocVO defvo = ScapDefUtil.getDefdocByListCodeAndCode(
					"scappt_0003", "004");
			if (defvo != null) {
				row.setValue(ds
						.nameToIndex(ScapptApplyHVO.CTRADETYPE),
						defvo.getPk_defdoc());
			}
			fe.setEnabled(false);
		}
  }
  /** 
 * ���ͷ������֮�����֮ǰ����
 * @param ds
 */
  public void clearChangeType(  Dataset ds,  String formName){
    ds.setValue(ds.nameToIndex("ctradetype"), null);
		ds.setValue(ds.nameToIndex("cgreattype"), null);
		// ds.setValue(ds.nameToIndex("cprocesstype"), null);
		FormComp form = (FormComp) ds.getView().getViewComponents()
				.getComponent(formName);
		form.getElementById("ctradetype_name").setValue(null);
		form.getElementById("cgreattype_name").setValue(null);
		// form.getElementById("cprocesstype_name").setValue(null);
  }
  /** 
 * �������ͷ����仯֮���ӱ�Ҳ��Ҫ�����Ӧ����
 * @param ds
 * @param formName
 * @param flagtrue��ʾ����ش��ʲ���Ӧ���ݡ�false����������ͱ����Ϣ
 */
  public void clearChildData(  Dataset ds,  String gridName,  boolean flag){
    Row[] rows = ds.getCurrentRowSet().getCurrentRowData().getRows();
		for (Row tmp : rows) {
			if (tmp instanceof EmptyRow)
				continue;
			if (flag) {
				tmp.setValue(ds.nameToIndex("vcassessname"), null);
				tmp.setValue(ds.nameToIndex("vcdetail"), null);
			} else {
				// tmp.setValue(ds.nameToIndex("pk_borg"), null);
				// tmp.setValue(ds.nameToIndex("dbpercent"), null);
				// GridComp grid = (GridComp)
				// CpWinUtil.getView().getViewComponents().getComponent(
				// gridName);
			}
		}
  }
  /** 
 * �����ش��ʲ���������Ȩ�䶯 �ӱ�����
 * @param ds
 */
  public void changeChildPdu(  Dataset ds){
    Row row = ds.getSelectedRow();
		String changeType = (String) row.getValue(ds
				.nameToIndex(ScapptApplyHVO.CCHANGETYPE));
		if (changeType == null || "".equals(changeType)) {
			return;
		}
		DefdocVO defvo = ScapDefUtil.getDefDocVo(changeType);
		String change_code = defvo.getCode();
		if (change_code.equals(PtConstants.CCHANGE_TYPE_MAJOR)) {
			setChildGrid(ds, false);
		} else {
			setChildGrid(ds, true);
		}
  }
  /** 
 * �����ӱ��ش��ʲ�ʱ������У���������
 * @param ds
 * @param flag
 */
  public void setChildGrid(  Dataset ds,  boolean flag){
    if (flag) {
			CpUIctrl.setGridColVisable(ds, "scappt_apply_b_grid", new String[] {
					"pk_borg_vname", "dbpercent" });
			CpUIctrl.setGridColNullAble(ds, "scappt_apply_b_grid",
					new String[] { "pk_borg_vname", "dbpercent" }, false);
			CpUIctrl.setGridColNullAble(ds, "scappt_apply_b_grid",
					new String[] { "vcassessname", "vcdetail" }, true);
		} else {
			// �ش��ʲ����
			CpUIctrl.setGridColVisable(ds, "scappt_apply_b_grid", new String[] {
					"pk_borg_vname", "dbpercent", "vcassessname", "vcdetail" });
			CpUIctrl.setGridColNullAble(ds, "scappt_apply_b_grid",
					new String[] { "pk_borg_vname", "dbpercent",
							"vcassessname", "vcdetail" }, false);
			// CpUIctrl.setGridColNullAble(ds, "scappt_apply_b_grid",
			// new String[] { "pk_borg_vname", "dbpercent" }, true);

		}
  }
  /** 
 * @param ds
 * @param formName
 * @param flagtrue�ɱ༭����Ϊ��false ���ܱ༭����Ϊ��
 * @param editFields
 */
  public void setForm(  Dataset ds,  String formName,  boolean flag,  String[] editFields){
    FormComp form = (FormComp) ds.getView().getViewComponents()
				.getComponent(formName);
		if (form == null)
			return;
		for (FormElement fe : form.getElementList()) {
			boolean bl = true;
			for (String eableField : editFields) {
				if (fe.getField().equals(eableField)) {
					bl = false;
					fe.setEditable(flag);
					// fe.setNullAble(!flag);
					fe.setEnabled(flag);
				}
			}
			if (bl) {
				fe.setEditable(!flag);
				// fe.setNullAble(flag);
				fe.setEnabled(!flag);
			}
		}
  }
  public void setFormIsNull(  Dataset ds,  String formName,  String[] editFields){
    FormComp form = (FormComp) ds.getView().getViewComponents()
				.getComponent(formName);
		if (form == null)
			return;
		for (FormElement fe : form.getElementList()) {
			for (String eableField : editFields) {
				if (fe.getField().equals(eableField)) {
					// fe.setNullAble(true);
					fe.setEnabled(false);
					fe.setEditable(false);
				}
			}
		}
  }
  /** 
 * ������Ϣ
 * @param mouseEvent
 */
  public void onDetail(  ScriptEvent mouseEvent){
    String oper = this.getOperator();
		String title = "����";
		Dataset ds = this.getCurrentView().getViewModels()
				.getDataset("scappt_apply_b");
		Dataset mainds = this.getCurrentView().getViewModels()
				.getDataset("scappt_apply_h");
		String mainpk = (String) mainds.getValue(mainds.getPrimaryKeyField());
		// ��Ȩ����ת�÷�PK
		String pk_rorg = (String) mainds.getValue(ScapptApplyHVO.PK_RORG);
		Row row = ds.getSelectedRow();
		// ��Ȩ���������ҵPK
		String pk = (String) row.getValue(ds
				.nameToIndex(ScapptApplyBVO.PK_BORG));
		if (pk == null) {
			AppInteractionUtil.showShortMessage("��ѡ������ҵ!");
			return;
		}
		if (this.getOperator().equals(AppConsts.OPE_EDIT)) {
			oper = AppConsts.OPE_EDIT;
			title = "�޸�";
		} else if (this.getOperator().equals("look")) {
			oper = "look";
			title = "�鿴";
		} else if (this.getOperator().equals("approve")) {
			oper = "look";
			title = "�鿴";
		} else if (this.getOperator().equals(AppConsts.OPE_ADD)) {
			
		} 
		String billpk = "" ;
		String node_type = (String) AppUtil.getAppAttr(PtConstants.NODE_TYPE2);
		//�����ʲ����ò�ѯ�Ƿ�������
				if (PtConstants.TDCZ.equals(node_type)){
					billpk = ScapPtMethod.getBillPkland(mainpk, pk);
					if (billpk == null) {
						oper = AppConsts.OPE_ADD;
						title = "����";
						billpk = null;
					}
				}else {
					// ������������ѯ�Ƿ�����Ȩ����
					billpk = ScapPtMethod.getBillPk(mainpk, pk);
					if (billpk == null) {
						oper = AppConsts.OPE_ADD;
						title = "����";
						billpk = null;
					}
				}
				if(billpk!=null&&!oper.equals("look")){
					oper = AppConsts.OPE_EDIT;
				}
		if (pk_rorg == null) {
			AppInteractionUtil.showShortMessage("��ѡ��ת�÷�!");
			return;
		}
		// ��ȡת�÷���Ȩ�Ǽ���Ϣ
		String rorg = ScapPtMethod.getFirmBaseHVOByTreePk(pk_rorg);
		if (rorg == null) {
			AppInteractionUtil.showShortMessage("ת�÷�δ�Ǽǲ�Ȩ��Ϣ!");
			return;
		}
		ScapPtMethod.onLink(ScapPtMethod.getWinByType(), title, oper, billpk,
				pk, rorg, mainpk);
  }
	public void onAfterDetailDataChange(DatasetCellEvent datasetCellEvent) {
		
		Dataset ds = datasetCellEvent.getSource();
		Row rowSelected = ds.getSelectedRow();
		Integer colIndex = datasetCellEvent.getColIndex();
		//�����ҵ
		Integer Ipk_borg = ds.nameToIndex("pk_borg");
		String Spk_borg = (String) CpWinUtil.getValue(ds, "pk_borg");
		if((Ipk_borg == colIndex)&&(null != Spk_borg)&&(!"".equals(Spk_borg))){
			Row[] rows = ds.getAllRow();
			int i = 0;
			for(Row row : rows){
				String pk_borg = (String) row.getValue(Ipk_borg);
				if(pk_borg.equals(Spk_borg)){
					i++;
				}
			}
			if(i>1){
				AppInteractionUtil.showMessageDialog("�����ҵ�����ظ���");
				i=0;
				rowSelected.setValue(colIndex, null);
				rowSelected.setValue(colIndex+1, null);
			}
		}
		
	}
}
