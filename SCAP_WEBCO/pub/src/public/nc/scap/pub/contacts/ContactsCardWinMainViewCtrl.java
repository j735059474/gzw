package nc.scap.pub.contacts;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.jdbc.framework.processor.BeanProcessor;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.notice.util.NoticeUtil;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.pub.util.ScapDAO;
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
import nc.uap.lfw.core.combodata.CombItem;
import nc.uap.lfw.core.combodata.ComboData;
import nc.uap.lfw.core.combodata.StaticComboData;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetCellEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.InputItem;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.exception.RadioGroupItem;
import nc.uap.lfw.core.file.FillFileInfoHelper;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.portal.log.ScapLogger;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.scap.work_report_type.Work_report_type;
import nc.vo.scapco.pub_visualorganization.visualUserInfo;
import nc.vo.scapco.work_notice_contacts.notice_contact_info;
import nc.vo.scapco.work_notice_contacts.notice_contact_type;
import nc.vo.scapco.work_notice_manager.Receive_man;
import nc.vo.scapjj.userpsn.V_userpsnVO;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.core.locator.ServiceLocator;
import uap.web.bd.pub.AppUtil;
/** 
 * ��Ƭ����Ĭ���߼�
 */
public class ContactsCardWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String PARAM_BILLITEM="billitem";
  private static final String PLUGOUT_ID="afterSavePlugout";
  /** 
 * ҳ����ʾ�¼�
 * @param dialogEvent
 */
  public void beforeShow(  DialogEvent dialogEvent){
    Dataset masterDs = this.getMasterDs();
		masterDs.clear();

		String oper = getOperator();
		if (AppConsts.OPE_ADD.equals(oper)) {
			CmdInvoker.invoke(new UifAddCmd(getMasterDsId()) {
				protected void onBeforeRowAdd(Row row) {
					setAutoFillValue(row);

					Dataset masterDs = getMasterDs();
					String pk_primarykey = generatePk();
					row.setValue(
							masterDs.nameToIndex(masterDs.getPrimaryKeyField()),
							pk_primarykey);
					// ����Ĭ��֪ͨ����
					row.setValue(masterDs
							.nameToIndex(notice_contact_type.NOTICE_STYLE),
							"1");
					// ����Ĭ���ʱ��
					row.setValue(masterDs
							.nameToIndex(notice_contact_type.REPORT_DATE),
							new UFDate());
					// ����Ĭ�����
					row.setValue(masterDs
							.nameToIndex(notice_contact_type.REPORT_PSN),
							CntUtil.getCntUserPk());
					// ����Ĭ�����ҵ
					row.setValue(masterDs
							.nameToIndex(notice_contact_type.REPORT_ORG),
							CntUtil.getCntOrgPk());
					// ��������״̬
					row.setValue(masterDs
							.nameToIndex(notice_contact_type.ENABLESTATE), "2");
					FillFileInfoHelper.resetItem(pk_primarykey);
					FillFileInfoHelper.fillFileInfo(masterDs, row);
				}
			});
		} else if (AppConsts.OPE_EDIT.equals(oper)) {
			CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs) {
				@Override
				protected void onAfterDatasetLoad() {
					this.getDs().setEnabled(true);

					String primaryKey = this.getDs().getPrimaryKeyField();
					if (primaryKey == null) {
						throw new LfwRuntimeException("��ǰDatasetû����������!");
					}
					String primaryKeyValue = (String) this.getDs()
							.getSelectedRow()
							.getValue(this.getDs().nameToIndex(primaryKey));
					uap.web.bd.pub.AppUtil
							.addAppAttr(
									IGlobalConstants.APPATTR_BUSINESS_TYPE,
									(String) this
											.getDs()
											.getSelectedRow()
											.getValue(
													this.getDs()
															.nameToIndex(
																	notice_contact_type.YE_TYPE)));
					FillFileInfoHelper.resetItem(primaryKeyValue);
				}
			});
		}
		// �������ҵ����������enabled��false�����ܱ༭
		// �ж��Ƿ���ҵ�û�
		if (CpOrgUtil.isCompanyOrg(CntUtil.getCntOrgPk())) {
			masterDs.setEnabled(false);
		}
  }
  /** 
 * ������ѡ���߼�
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent dsEvent){
    Dataset ds = dsEvent.getSource();
		Row row = (Row)ds.getSelectedRow();
		String notice_style = (String)row.getValue(ds.nameToIndex(notice_contact_type.NOTICE_STYLE));
		LfwView main = LfwRuntimeEnvironment.getWebContext().getPageMeta()
				.getView("main");
		FormComp formcomp = (FormComp) main.getViewComponents()
				.getComponent("notice_contact_type_form");
		if (notice_style != null && "1".equals(notice_style)) {// �֪ͨ
			formcomp.getElementById("ye_type_name").setVisible(
					true);		
			formcomp.getElementById("report_type_report_type").setVisible(
					true);			
			formcomp.getElementById("data_type_name").setVisible(
					false);
			formcomp.getElementById("vdef1").setVisible(
					false);
		} else if (notice_style != null && "2".equals(notice_style)) { // ������Ϣ֪ͨ
			formcomp.getElementById("ye_type_name").setVisible(
					false);		
			formcomp.getElementById("report_type_report_type").setVisible(
					false);	
			formcomp.getElementById("data_type_name").setVisible(
					true);
			formcomp.getElementById("vdef1").setVisible(
					false);
		} else if (notice_style != null && "3".equals(notice_style)) { // ��Ϣ֪ͨ
			formcomp.getElementById("ye_type_name").setVisible(
					false);
			formcomp.getElementById("report_type_report_type").setVisible(
					false);
			formcomp.getElementById("data_type_name").setVisible(
					false);
			formcomp.getElementById("vdef1").setVisible(
					true);
		}	
		String oper = getOperator();
		if (AppConsts.OPE_EDIT.equals(oper)) {
			CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()) {
				protected String postProcessRowSelectVO(SuperVO vo, Dataset ds) {
					LfwView main = LfwRuntimeEnvironment.getWebContext().getPageMeta()
							.getView("main");
					Dataset masteDs = main.getViewModels().getDataset("notice_contact_type");
					// ���ݱ�������ȡ�ñ�������
					String report_body = "";
					String notice_style = (String)masteDs.getSelectedRow().getValue(masteDs.nameToIndex(notice_contact_type.NOTICE_STYLE));
					String report_type = (String)masteDs.getSelectedRow().getValue(masteDs.nameToIndex(notice_contact_type.REPORT_TYPE));
					// ������֪ͨ���Ǹ����ݱ�������ȡ�ñ�������
					if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
						Work_report_type WorkReportType = (Work_report_type) ScapDAO
								.retrieveByPK(Work_report_type.class, report_type);
						report_body = WorkReportType.getReport_body();
					} else if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)) {
						// �����������Ϣ֪ͨ����Ĭ�ϱ��������Ǹ���
						report_body = IGlobalConstants.REPORT_BODY_MAN;
					} else if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
						// �������Ϣ֪ͨ����Ĭ�ϱ��������Ǹ���
						report_body = IGlobalConstants.REPORT_BODY_MAN;
					}
					// ���ݱ������� ȡ�ñ������壬�Ӷ�����������������ҵ������֯	
					List<String> list_r = NoticeUtil
							.getAllChildPk_OrgByCurrentPk(CntUtil.getCntOrgPk());
					List<String> list_v = NoticeUtil
							.getAllChildPKVisualorgByPkUser(CntUtil.getCntUserPk());
					StringBuffer condition = new StringBuffer("pk_contact_type = '"
							+ vo.getAttributeValue("pk_contact_type") + "' and dr = '0' ");
					String whereSql = "";
					// ������������ҵ
					if (report_body != null && "1".equals(report_body)) {
						if (list_r != null && list_r.size() > 0) {
							condition
									.append(" and (")
									.append(NoticeUtil.getSqlStrByList(list_r, 1000,
											"pk_org")).append(")");
						}
					}
					// ������������֯
					if (report_body != null && "2".equals(report_body)) {
						if (list_v != null && list_v.size() > 0) {
							condition
							.append(" and (")
							.append(NoticeUtil.getSqlStrByList(list_v, 1000,
									"pk_visualorg")).append(")");
						}
					}
					// ���������Ǹ���
					if (report_body != null && "3".equals(report_body)) {
						if ((list_r != null && list_r.size() > 0) || (list_v != null && list_v.size() > 0)) {
							condition
							.append(" and (");
							if (list_r != null && list_r.size() > 0) {
								condition
								.append(" (")
								.append(NoticeUtil.getSqlStrByList(list_r, 1000,
										"pk_org")).append(")");
							}
							if (list_v != null && list_v.size() > 0) {
								condition
								.append(" or (")
								.append(NoticeUtil.getSqlStrByList(list_v, 1000,
										"pk_visualorg")).append(")");
							}
							condition
							.append(" )");
						}
					}
	
					ds.setLastCondition(condition.toString());
					return condition.toString();
				}
	
				// ��д�˷���������ѡ�е�һ��
				protected void postProcessChildRowSelect(Dataset ds) {
					return;
				}
			});
		}
  }
  /** 
 * ����
 * @param datasetEvent
 */
  public void onSave(  MouseEvent<?> mouseEvent) throws BusinessException {
    Dataset masterDs = this.getMasterDs();
		// ����ʱcheck ��ÿ�֣�ҵ�����ͣ��������ͣ�����������,��Ϣ����Ӧ��ֻ��һ������
		check();
		CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this
				.getDetailDsIds(), false));
		masterDs.setEnabled(true);
		this.getCurrentAppCtx().closeWinDialog();

		// ����listҳ��
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		Row savedRow = masterDs.getSelectedRow();
		paramMap.put(OPERATE_ROW, savedRow);
		CmdInvoker.invoke(new UifPlugoutCmd(getCurrentView().getId(),
				PLUGOUT_ID, paramMap));
  }
  /** 
 * ����
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
 * ����
 * @param mouseEvent
 */
  public void onCopy(  MouseEvent<?> mouseEvent){
    CmdInvoker.invoke(new UifCopyCmd(this.getMasterDsId()));
  }
  /** 
 * ɾ��
 */
  public void onDelete(  MouseEvent<?> mouseEvent) throws BusinessException {
    CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
  }
  /** 
 * ����
 */
  public void onBack(  MouseEvent<?> mouseEvent) throws BusinessException {
    this.getCurrentAppCtx().closeWinDialog();
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
		String primaryKey = ds.getPrimaryKeyField();
		if (primaryKey == null) {
			throw new LfwRuntimeException("��ǰDatasetû����������!");
		}
		String primaryKeyValue = (String) row.getValue(ds
				.nameToIndex(primaryKey));
		Map<String, String> paramMap = null;
		if (primaryKeyValue != null) {
			paramMap = new HashMap<String, String>(2);
			paramMap.put(PARAM_BILLITEM, primaryKeyValue);
		}
		CmdInvoker.invoke(new UifAttachCmd("����", paramMap));
  }
  /** 
 * ��ӡ
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onPrint(  MouseEvent<?> mouseEvent) throws BusinessException {
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
			ICpPrintTemplateService service = ServiceLocator
					.getService(ICpPrintTemplateService.class);
			service.print(printService);
		} catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
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
				int pkOrgIndex = ds.nameToIndex(PK_ORG);
				if (pkOrgIndex >= 0) {
					row.setValue(pkOrgIndex, pkOrg);
				}
			}
			String pkGroup = this.getCurrentAppCtx().getAppEnvironment()
					.getPk_group();
			if (pkGroup != null) {
				int pkGroupIndex = ds.nameToIndex(PK_GROUP);
				if (pkGroupIndex >= 0) {
					row.setValue(pkGroupIndex, pkGroup);
				}
			}
		}
  }
  /** 
 * �ӱ�����
 */
  public void onGridAddClick(  MouseEvent<?> mouseEvent){
	  	// ȡ���������������ֶΣ����û�����ã���ô����ѡ����ϵ��
		Dataset masterDs = this.getMasterDs();
		String notice_style = (String) masterDs.getSelectedRow().getValue(
				masterDs.nameToIndex(notice_contact_type.NOTICE_STYLE));
		String report_type = (String) masterDs.getSelectedRow().getValue(
				masterDs.nameToIndex(notice_contact_type.REPORT_TYPE));
		String data_type = (String) masterDs.getSelectedRow().getValue(
				masterDs.nameToIndex(notice_contact_type.DATA_TYPE));
		String message_type = (String) masterDs.getSelectedRow().getValue(
				masterDs.nameToIndex(notice_contact_type.VDEF1));
		if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
			if (report_type == null && StringUtils.isEmpty(report_type)) {
				throw new LfwRuntimeException("û��ѡ����Ҫ�ı������ͣ�����ѡ����ϵ�ˣ�");
			}
			uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_TYPE, report_type);
		}
		if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)) {
			if (data_type == null && StringUtils.isEmpty(data_type)) {
				throw new LfwRuntimeException("û��ѡ����Ҫ���������ͣ�����ѡ����ϵ�ˣ�");
			}
			uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_DATA_TYPE, data_type);
		}
		if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
			if (message_type == null && StringUtils.isEmpty(message_type)) {
				throw new LfwRuntimeException("û��ѡ����Ҫ����Ϣ���ͣ�����ѡ����ϵ�ˣ�");
			}
			uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_MESSAGE_TYPE, message_type);
		}
	  	ScapLogger.console("doSelectUser");
	  	RadioGroupItem radioGroupItem = new RadioGroupItem(IGlobalConstants.APPATTR_SENDER_OR_RECEIVER,"��ѡ��",true);
//	  	radioGroupItem.setValue(value)
	  	ComboData comboData = new StaticComboData();
	  	CombItem sender = new CombItem(IGlobalConstants.CONTENT_TYPE_SENDER,"���ͷ�");
	  	comboData.addCombItem(sender);	  	
	  	CombItem receiver = new CombItem(IGlobalConstants.CONTENT_TYPE_RECEIVER,"���շ�");
	  	comboData.addCombItem(receiver);
	  	radioGroupItem.setComboData(comboData);
	  	radioGroupItem.setValue(IGlobalConstants.CONTENT_TYPE_RECEIVER);
	  	AppInteractionUtil.showInputDialog("��ѡ����շ����Ƿ��ͷ�", new InputItem[]{radioGroupItem});
	  	
	  	Map<String, String> rs = AppInteractionUtil.getInputDialogResult();
	  	AppUtil.addAppAttr(IGlobalConstants.APPATTR_SENDER_OR_RECEIVER, rs.get(IGlobalConstants.APPATTR_SENDER_OR_RECEIVER));
		OpenProperties props = new OpenProperties("selectUserByVisualOrg",
				"��ϵ��ѡ��", "800", "600");
		props.setButtonZone(false);
		this.getCurrentAppCtx().getCurrentWindowContext().popView(props);
  }
  /** 
 * �ӱ��༭
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
    return "notice_contact_type";
  }
  public void doSelectUser(  Map keys){

		LfwView view = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		LfwView view_comp = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("selectUserByVisualOrg").getView();
		Dataset masterDs = this.getMasterDs();
		Row selectedRow = masterDs.getSelectedRow();
		Object pk_contact_type = selectedRow.getValue(masterDs
				.nameToIndex(masterDs.getPrimaryKeyField()));
		String report_type = (String) AppUtil.getAppAttr(IGlobalConstants.APPATTR_REPORT_TYPE);
	    String data_type = (String) AppUtil.getAppAttr(IGlobalConstants.APPATTR_DATA_TYPE);
	    String message_type = (String) AppUtil.getAppAttr(IGlobalConstants.APPATTR_MESSAGE_TYPE);
	    String senderOrReceiver = (String) AppUtil.getAppAttr(IGlobalConstants.APPATTR_SENDER_OR_RECEIVER);
	    String report_body = "";
	    if (report_type != null) {
	    	report_body = NoticeUtil.getReportBodyByNoticeType(report_type);
	    } else if (data_type != null) {
	    	report_body = "3";
	    } else if (message_type != null) {
	    	report_body = "3";
	    }
		// ȡ����ϵ����ϸ��Ϣ�ӱ���dataset
		Dataset notice_contact_infods = view.getViewModels().getDataset(
				"notice_contact_info");
		// ȡ������view��dataset
		Dataset visualUserInfoDs = view_comp.getViewModels().getDataset(
				"visualUserInfo");
		Dataset cp_userpsnDs = view_comp.getViewModels().getDataset(
				"cp_userpsn");
		StringBuffer errorMessage = new StringBuffer();
		if (report_body != null
				&& ("1".equals(report_body) || "3".equals(report_body))) {
			Row[] selRows_r = cp_userpsnDs.getAllSelectedRows();
			// ȡ���Ѿ�������ϵ����ϸ��Ϣ�ӱ�dataset���Ѿ����ڵ��ˣ�����ȥ�ش���
			Map<String, List<String>> mapExisted_r = getUserExistedMapByType_r(senderOrReceiver);
			// ��д ������ϵ����ϸ��Ϣ�ӱ���ds
			for (int i = 0; i < selRows_r.length; i++) {
				// �����ж�����grid��治���ڣ�
				String pk_user = (String) selRows_r[i]
						.getValue(cp_userpsnDs
								.nameToIndex(V_userpsnVO.CUSERID));
				String pk_org = (String) selRows_r[i].getValue(cp_userpsnDs
						.nameToIndex(V_userpsnVO.PK_ORG));
				if (mapExisted_r.containsKey(pk_org)) {
					if (mapExisted_r.get(pk_org).contains(pk_user)) {
						errorMessage.append(NoticeUtil.getNameByPkUser(pk_user)).append(",");
						continue;// ����grid���Ѿ����ڸ���ҵ�ĸ��û�
					}
				}
				Row row = notice_contact_infods.getEmptyRow();
				String pk_primarykey = generatePk();
				// ����
				row.setValue(
						notice_contact_infods
								.nameToIndex(notice_contact_info.PK_NOTICE_CONTACT_INFO),
						pk_primarykey);
				// ��ϵ������������������ã�
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.PK_CONTACT_TYPE),
						pk_contact_type.toString());
				String user_name = (String) selRows_r[i].getValue(cp_userpsnDs
						.nameToIndex(V_userpsnVO.USER_NAME));
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.CONTACTS_NAME), user_name);
				// ������
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.PK_USER), pk_user);
				// ������ҵ
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.PK_ORG), pk_org);
				 // �Ա�
				 row.setValue(notice_contact_infods.nameToIndex(Receive_man.SEX),
						 NoticeUtil.getSexByPkUser(pk_user));

				// �绰����
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.PHONE_NO),
						(String) selRows_r[i].getValue(cp_userpsnDs
								.nameToIndex(V_userpsnVO.MOBILE)));
				// Email
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.EMAIL), (String) selRows_r[i]
						.getValue(cp_userpsnDs
								.nameToIndex(V_userpsnVO.EMAIL)));
				// ����ѡ�񱣴�Ϊ���շ����ͷ�
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.CONTACTS_TYPE), senderOrReceiver);
				notice_contact_infods.addRow(row);
			}
		}
		if (report_body != null
				&& ("2".equals(report_body) || "3".equals(report_body))) {
			Row[] selRows_v = visualUserInfoDs.getAllSelectedRows();
			// ȡ���Ѿ�������ϵ����ϸ��Ϣ�ӱ�dataset���Ѿ����ڵ��ˣ�����ȥ�ش���
			Map<String, List<String>> mapExisted_v = getUserExistedMapByType_v(senderOrReceiver);
			// ��д ������ϵ����ϸ��Ϣ�ӱ���ds
			for (int i = 0; i < selRows_v.length; i++) {
				// �����ж�����grid��治���ڣ�
				String pk_user = (String) selRows_v[i]
						.getValue(visualUserInfoDs
								.nameToIndex(visualUserInfo.PK_USER));

				String pk_visualorg = (String) selRows_v[i].getValue(visualUserInfoDs
						.nameToIndex(visualUserInfo.PK_VISUALORG));
				if (mapExisted_v.containsKey(pk_visualorg)) {
					if (mapExisted_v.get(pk_visualorg).contains(pk_user)) {
						errorMessage.append(NoticeUtil.getNameByPkUser(pk_user)).append(",");
						continue;// ����grid���Ѿ����ڸ���֯�ĸ��û�
					}
				}
				Row row = notice_contact_infods.getEmptyRow();
				String pk_primarykey = generatePk();
				// ����
				row.setValue(
						notice_contact_infods
								.nameToIndex(notice_contact_info.PK_NOTICE_CONTACT_INFO),
						pk_primarykey);
				// ��ϵ������������������ã�
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.PK_CONTACT_TYPE),
						pk_contact_type.toString());
				// ��ϵ������
				String user_name = (String) selRows_v[i].getValue(visualUserInfoDs
						.nameToIndex(visualUserInfo.USER_NAME));
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.CONTACTS_NAME), user_name);
				// ������
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.PK_USER), pk_user);

				// ������ҵ
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.PK_VISUALORG), pk_visualorg);
				 // �Ա�
				 row.setValue(notice_contact_infods.nameToIndex(Receive_man.SEX),
						 NoticeUtil.getSexByPkUser(pk_user));

				// �绰����
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.PHONE_NO),
						(String) selRows_v[i].getValue(visualUserInfoDs
								.nameToIndex(visualUserInfo.PHONE_NO)));
				// Email
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.EMAIL), (String) selRows_v[i]
						.getValue(visualUserInfoDs
								.nameToIndex(visualUserInfo.EMAIL)));
				// Ĭ�Ͻ��շ�
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.CONTACTS_TYPE), senderOrReceiver);
				notice_contact_infods.addRow(row);
			}
		}
		if (StringUtils.isNotBlank(errorMessage.toString())) {
			if (IGlobalConstants.CONTENT_TYPE_RECEIVER.equals(senderOrReceiver)) {
				AppInteractionUtil.showMessageDialog("�Ѿ�����������Ա�Ľ��շ���" + errorMessage.substring(0, errorMessage.length()-1),true);
			}
			if (IGlobalConstants.CONTENT_TYPE_SENDER.equals(senderOrReceiver)) {
				AppInteractionUtil.showMessageDialog("�Ѿ�����������Ա�ķ��ͷ���" + errorMessage.substring(0, errorMessage.length()-1),true);
			}
		}
  }
  private Map<String, List<String>> getUserExistedMapByType_r(String senderOrReceiver){
    LfwView view = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset notice_contact_infods = view.getViewModels().getDataset(
				"notice_contact_info");
		// ��ϵ����ϸ��Ϣ�ӱ�ȥ�ش���
		Map<String, List<String>> mapExisted = new HashMap<String, List<String>>();
		Row[] rowExisted = notice_contact_infods.getAllRow();
		for (int i = 0; i < rowExisted.length; i++) {
			String contacts_type = (String) rowExisted[i]
					.getValue(notice_contact_infods
							.nameToIndex(notice_contact_info.CONTACTS_TYPE));
			// ��ϵ�����ͣ� ���շ���1�����ͷ���2��
			if (!contacts_type.equals(senderOrReceiver)) {
				continue;
			}
			String pkOrg = (String) rowExisted[i]
					.getValue(notice_contact_infods
							.nameToIndex(notice_contact_info.PK_ORG));
			String pkUser = (String) rowExisted[i]
					.getValue(notice_contact_infods
							.nameToIndex(notice_contact_info.PK_USER));
			if (mapExisted.containsKey(pkOrg)) {
				mapExisted.get(pkOrg).add(pkUser);
			} else {
				List<String> pkUserListTemp = new ArrayList<String>();
				pkUserListTemp.add(pkUser);
				mapExisted.put(pkOrg, pkUserListTemp);
			}
		}
		return mapExisted;
  }
  private Map<String, List<String>> getUserExistedMapByType_v(String senderOrReceiver){
	    LfwView view = AppLifeCycleContext.current().getWindowContext()
					.getViewContext("main").getView();
			Dataset notice_contact_infods = view.getViewModels().getDataset(
					"notice_contact_info");
			// ��ϵ����ϸ��Ϣ�ӱ�ȥ�ش���
			Map<String, List<String>> mapExisted = new HashMap<String, List<String>>();
			Row[] rowExisted = notice_contact_infods.getAllRow();
			for (int i = 0; i < rowExisted.length; i++) {
				String contacts_type = (String) rowExisted[i]
						.getValue(notice_contact_infods
								.nameToIndex(notice_contact_info.CONTACTS_TYPE));
				// ��ϵ�����ͣ� ���շ���1�����ͷ���2��
				if (!contacts_type.equals(senderOrReceiver)) {
					continue;
				}
				String pkVisualorg = (String) rowExisted[i]
						.getValue(notice_contact_infods
								.nameToIndex(notice_contact_info.PK_VISUALORG));
				String pkUser = (String) rowExisted[i]
						.getValue(notice_contact_infods
								.nameToIndex(notice_contact_info.PK_USER));
				if (mapExisted.containsKey(pkVisualorg)) {
					mapExisted.get(pkVisualorg).add(pkUser);
				} else {
					List<String> pkUserListTemp = new ArrayList<String>();
					pkUserListTemp.add(pkUser);
					mapExisted.put(pkVisualorg, pkUserListTemp);
				}
			}
			return mapExisted;
	  }
  public void onAfterDataChange(  DatasetCellEvent datasetCellEvent){
    Dataset ds = datasetCellEvent.getSource();
		LfwView view = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		FormComp form = (FormComp) view
				.getComponentsByDataset(ds.getId())[0];
		Row row = ds.getSelectedRow();
		if (ds.nameToIndex(notice_contact_type.NOTICE_STYLE) == datasetCellEvent
				.getColIndex()) {
			FormElement notice_style = form.getElementById("notice_style"); // ��ȡ֪ͨ����
			if (notice_style.getValue().equals(IGlobalConstants.NOTICE_STYLE_REPORT)) { // �֪ͨ
				// ҵ������
				FormElement business_type_name = form
						.getElementById("ye_type_name"); // ҵ������
				ds.setValue(
						ds.nameToIndex(notice_contact_type.YE_TYPE), "");
				business_type_name.setVisible(true);
//				business_type_name.setNullAble(false);
				// ��������
				FormElement report_type_report_type = form
						.getElementById("report_type_report_type"); // ��������
				ds.setValue(
						ds.nameToIndex(notice_contact_type.REPORT_TYPE), "");
				report_type_report_type.setVisible(true);
//				report_type_report_type.setNullAble(false);
				// ��������
				FormElement data_type_name = form
						.getElementById("data_type_name"); // ��������
				ds.setValue(
						ds.nameToIndex(notice_contact_type.DATA_TYPE), "");
				data_type_name.setVisible(false);
//				data_type_name.setNullAble(true);
				// ��Ϣ����
				FormElement message_type = form
						.getElementById("vdef1"); // ��Ϣ����
				ds.setValue(
						ds.nameToIndex(notice_contact_type.VDEF1), "");
				message_type.setVisible(false);
			}
			if (notice_style.getValue().equals(IGlobalConstants.NOTICE_STYLE_MESSAGE)) { // ������Ϣ֪ͨ
				// ҵ������
				FormElement business_type_name = form
						.getElementById("ye_type_name"); // ҵ������
				ds.setValue(
						ds.nameToIndex(notice_contact_type.YE_TYPE), "");
				business_type_name.setVisible(false);
//				business_type_name.setNullAble(true);
				// ��������
				FormElement report_type_report_type = form
						.getElementById("report_type_report_type"); // ��������
				ds.setValue(
						ds.nameToIndex(notice_contact_type.REPORT_TYPE), "");
				report_type_report_type.setVisible(false);
//				report_type_report_type.setNullAble(true);
				// ��������
				FormElement data_type_name = form
						.getElementById("data_type_name"); // ��������
				ds.setValue(
						ds.nameToIndex(notice_contact_type.DATA_TYPE), "");
				data_type_name.setVisible(true);
//				data_type_name.setNullAble(false);
				// ��Ϣ����
				FormElement message_type = form
						.getElementById("vdef1"); // ��Ϣ����
				ds.setValue(
						ds.nameToIndex(notice_contact_type.VDEF1), "");
				message_type.setVisible(false);
			}
			if (notice_style.getValue().equals(IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE)) { // ��Ϣ֪ͨ
				// ҵ������
				FormElement business_type_name = form
						.getElementById("ye_type_name"); // ҵ������
				ds.setValue(
						ds.nameToIndex(notice_contact_type.YE_TYPE), "");
				business_type_name.setVisible(false);
//				business_type_name.setNullAble(true);
				// ��������
				FormElement report_type_report_type = form
						.getElementById("report_type_report_type"); // ��������
				ds.setValue(
						ds.nameToIndex(notice_contact_type.REPORT_TYPE), "");
				report_type_report_type.setVisible(false);
//				report_type_report_type.setNullAble(true);
				// ��������
				FormElement data_type_name = form
						.getElementById("data_type_name"); // ��������
				ds.setValue(
						ds.nameToIndex(notice_contact_type.DATA_TYPE), "");
				data_type_name.setVisible(false);
//				data_type_name.setNullAble(false);
				// ��Ϣ����
				FormElement message_type = form
						.getElementById("vdef1"); // ��Ϣ����
				ds.setValue(
						ds.nameToIndex(notice_contact_type.VDEF1), IGlobalConstants.STANDARD_MESSAGE_TYPE_CODE);
				message_type.setVisible(true);
			}
		}
		if (ds.nameToIndex(notice_contact_type.YE_TYPE) == datasetCellEvent
				.getColIndex()) {
			String pk_business_type = row.getString(ds
					.nameToIndex(notice_contact_type.YE_TYPE));
			uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE,
					pk_business_type);
		}
		if (ds.nameToIndex(notice_contact_type.REPORT_TYPE) == datasetCellEvent
				.getColIndex()) {
			String report_type = row.getString(ds
					.nameToIndex(notice_contact_type.REPORT_TYPE));
			uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_TYPE,
					report_type);
		}
		if (ds.nameToIndex(notice_contact_type.DATA_TYPE) == datasetCellEvent
				.getColIndex()) {
			String data_type = row.getString(ds
					.nameToIndex(notice_contact_type.DATA_TYPE));
			uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_DATA_TYPE,
					data_type);
		}
		if (ds.nameToIndex(notice_contact_type.VDEF1) == datasetCellEvent
				.getColIndex()) {
			String message_type = row.getString(ds
					.nameToIndex(notice_contact_type.VDEF1));
			uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_MESSAGE_TYPE,
					message_type);
		}
  }
  private void check(){
    FormComp formcomp = (FormComp) this.getCurrentView()
				.getViewComponents().getComponent("notice_contact_type_form");
		Dataset masterDs = getMasterDs();
		Row rowSelect = masterDs.getSelectedRow();
		String pk_contact_type = (String)rowSelect.getValue(
				masterDs.nameToIndex(masterDs.getPrimaryKeyField()));
		String notice_style = (String) rowSelect.getValue(masterDs
				.nameToIndex(notice_contact_type.NOTICE_STYLE));
		String ye_type = (String) rowSelect.getValue(masterDs
				.nameToIndex(notice_contact_type.YE_TYPE));
		String report_type = (String) rowSelect.getValue(masterDs
				.nameToIndex(notice_contact_type.REPORT_TYPE));
		String data_type = (String) rowSelect.getValue(masterDs
				.nameToIndex(notice_contact_type.DATA_TYPE));
		String message_type = (String) rowSelect.getValue(masterDs
				.nameToIndex(notice_contact_type.VDEF1));
		notice_contact_type notice_contact_type= (notice_contact_type)ScapDAO.retrieveByPK(notice_contact_type.class, pk_contact_type);
		if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
			if (StringUtils.isEmpty(ye_type) || StringUtils.isEmpty(report_type)) {
				throw new LfwRuntimeException("ҵ�����ͺͱ������ͱ������룡");
			}
			if (StringUtils.isNotEmpty(ye_type)
					&& StringUtils.isNotEmpty(report_type)) {
				// ��ѯ��ϵ�����ͱ����治������Ӧ�����ʹ��ڣ�����Ѿ����ˣ���message�����ñ��棡
				String sql = "select t.* from scapco_notice_contact_type t where t.ye_type = '"
						+ ye_type
						+ "' "
						+ " and t.report_type ='"
						+ report_type
						+ "' "
						+ " and t.dr = '0'  and t.enablestate = '2'  ";
				notice_contact_type notice_contact_type_db = new notice_contact_type();
				notice_contact_type_db = (notice_contact_type) ScapDAO
						.executeQuery(
								sql,
								new BeanProcessor(
										notice_contact_type.class));
				String oper = getOperator();
				if (AppConsts.OPE_ADD.equals(oper)) {
					if (notice_contact_type_db != null) {
						throw new LfwRuntimeException("��Ӧ�ģ�ҵ�����ͣ��������ͣ��Ѿ����ڲ����ٴ�¼�룡");
					}
				} else if (AppConsts.OPE_EDIT.equals(oper)) {
					if (notice_contact_type_db != null) {
						// ��db���Ѿ����ڵĳ�����ϵ�������е�ҵ�����ͺͱ������������ֶΣ�
						// �͵�ǰ��ϵ������pk�����������Ǹ���ϵ������(Ҳ�����޸�֮ǰ���Ǹ�)�е�ҵ�����ͺͱ������������ֶν��бȽ�
						// �����ͬ��˵�������ֶ�û�иı䣬������������
						// �������ͬ��˵���༭���������ֶ� �����ݿ����Ѿ������������͵������ˣ����ܱ���
						
						if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_contact_type.getNotice_style()) ||IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_contact_type.getNotice_style())) {
							// ���ԭ����������Ϣ֪ͨ����Ϣ֪ͨ����ô�϶�����¼��
							throw new LfwRuntimeException("��Ӧ�ģ�ҵ�����ͣ��������ͣ��Ѿ����ڲ����ٴ�¼�룡");
						} else if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_contact_type.getNotice_style())) {
							if (notice_contact_type.getYe_type().equals(notice_contact_type_db.getYe_type())
									&& notice_contact_type.getReport_type().equals(notice_contact_type_db.getReport_type())) {
								// �������

							} else {
								throw new LfwRuntimeException("��Ӧ�ģ�ҵ�����ͣ��������ͣ��Ѿ����ڲ����ٴ�¼�룡");
							}
						}
					}
				}

			}
		} else if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)) { // ��Ϣ֪ͨ
			if (StringUtils.isEmpty(data_type)) {
				throw new LfwRuntimeException("�������ͱ������룡");
			}
			if (StringUtils.isNotEmpty(data_type)) {
				// ��ѯ��ϵ�����ͱ����治������Ӧ�����ʹ��ڣ�����Ѿ����ˣ���message�����ñ��棡
				String sql = "select t.* from scapco_notice_contact_type t where t.data_type = '"
						+ data_type
						+ "' "
						+ " and t.dr = '0'  and t.enablestate = '2' ";
				notice_contact_type notice_contact_type_db = new notice_contact_type();
				notice_contact_type_db = (notice_contact_type) ScapDAO
						.executeQuery(
								sql,
								new BeanProcessor(
										notice_contact_type.class));
				String oper = getOperator();
				if (AppConsts.OPE_ADD.equals(oper)) {
					if (notice_contact_type_db != null) {
						throw new LfwRuntimeException("��Ӧ�ģ��������ͣ��Ѿ����ڲ����ٴ�¼�룡");
					}
				} else if (AppConsts.OPE_EDIT.equals(oper)) {
					if (notice_contact_type_db != null) {
						// ��db���Ѿ����ڵĳ�����ϵ�������е�������������ֶΣ�
						// �͵�ǰ��ϵ������pk�����������Ǹ���ϵ������(Ҳ�����޸�֮ǰ���Ǹ�)�е�������������ֶν��бȽ�
						// �����ͬ��˵�������ֶ�û�иı䣬������������
						// �������ͬ��˵���༭���������ֶ� �����ݿ����Ѿ������������͵������ˣ����ܱ���
						
						if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_contact_type.getNotice_style())||IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_contact_type.getNotice_style())) {
							// ���ԭ�����֪ͨ����Ϣ֪ͨ����ô�϶�����¼��
							throw new LfwRuntimeException("��Ӧ�ģ��������ͣ��Ѿ����ڲ����ٴ�¼�룡");
						} else if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_contact_type.getNotice_style())) {
							if (notice_contact_type.getData_type().equals(notice_contact_type_db.getData_type())) {
								// �������

							} else {
								throw new LfwRuntimeException("��Ӧ�ģ��������ͣ��Ѿ����ڲ����ٴ�¼�룡");
							}
						}
					}
				}
			}
		} else if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) { // ��Ϣ֪ͨ
					if (StringUtils.isEmpty(message_type)) {
						throw new LfwRuntimeException("��Ϣ���ͱ������룡");
					}
					if (StringUtils.isNotEmpty(message_type)) {
						// ��ѯ��ϵ�����ͱ����治������Ӧ�����ʹ��ڣ�����Ѿ����ˣ���message�����ñ��棡
						String sql = "select t.* from scapco_notice_contact_type t where t.vdef1 = '"
								+ message_type
								+ "' "
								+ " and t.dr = '0'  and t.enablestate = '2' ";
						notice_contact_type notice_contact_type_db = new notice_contact_type();
						notice_contact_type_db = (notice_contact_type) ScapDAO
								.executeQuery(
										sql,
										new BeanProcessor(
												notice_contact_type.class));
						String oper = getOperator();
						if (AppConsts.OPE_ADD.equals(oper)) {
							if (notice_contact_type_db != null) {
								throw new LfwRuntimeException("��Ӧ�ģ���Ϣ���ͣ��Ѿ����ڲ����ٴ�¼�룡");
							}
						} else if (AppConsts.OPE_EDIT.equals(oper)) {
							if (notice_contact_type_db != null) {
								// ��db���Ѿ����ڵĳ�����ϵ�������е���Ϣ��������ֶΣ�
								// �͵�ǰ��ϵ������pk�����������Ǹ���ϵ������(Ҳ�����޸�֮ǰ���Ǹ�)�е���Ϣ��������ֶν��бȽ�
								// �����ͬ��˵�������ֶ�û�иı䣬������������
								// �������ͬ��˵���༭���������ֶ� �����ݿ����Ѿ������������͵������ˣ����ܱ���
								if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_contact_type.getNotice_style()) ||IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_contact_type.getNotice_style())) {
									// ���ԭ�����֪ͨ����ô�϶�����¼��
									throw new LfwRuntimeException("��Ӧ�ģ���Ϣ���ͣ��Ѿ����ڲ����ٴ�¼�룡");
								} else if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_contact_type.getNotice_style())) {
									if (notice_contact_type.getVdef1().equals(notice_contact_type_db.getVdef1())) {
										// �������

									} else {
										throw new LfwRuntimeException("��Ӧ�ģ���Ϣ���ͣ��Ѿ����ڲ����ٴ�¼�룡");
									}
								}
							}
						}
					}
				}
		// һ���˲����������Ƿ��ͷ�����շ����ظ����ݣ�
		// ȡ����ϵ����ϸ��Ϣ�ӱ���dataset
		LfwView view = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset notice_contact_infods = view.getViewModels().getDataset(
				"notice_contact_info");
		Row[] rowExisted = notice_contact_infods.getAllRow();
		HashSet set = new HashSet<String>();
		for (int i = 0; i < rowExisted.length; i++) {
			String pkOrg_i = (String) rowExisted[i]
					.getValue(notice_contact_infods
							.nameToIndex(notice_contact_info.PK_ORG));
			String pkVisualorg_i = (String) rowExisted[i]
					.getValue(notice_contact_infods
							.nameToIndex(notice_contact_info.PK_VISUALORG));
			String pkUser_i = (String) rowExisted[i]
					.getValue(notice_contact_infods
							.nameToIndex(notice_contact_info.PK_USER));
			String contacts_type_i = (String) rowExisted[i]
					.getValue(notice_contact_infods
							.nameToIndex(notice_contact_info.CONTACTS_TYPE));
			for (int j = i + 1; j < rowExisted.length; j++) {

				String pkOrg_j = (String) rowExisted[j]
						.getValue(notice_contact_infods
								.nameToIndex(notice_contact_info.PK_ORG));
				String pkVisualorg_j = (String) rowExisted[j]
						.getValue(notice_contact_infods
								.nameToIndex(notice_contact_info.PK_VISUALORG));
				String pkUser_j = (String) rowExisted[j]
						.getValue(notice_contact_infods
								.nameToIndex(notice_contact_info.PK_USER));
				String contacts_type_j = (String) rowExisted[j]
						.getValue(notice_contact_infods
								.nameToIndex(notice_contact_info.CONTACTS_TYPE));
				if (StringUtils.isNotBlank(pkOrg_i) && StringUtils.isNotBlank(pkOrg_j)) {
					if (pkOrg_i.equals(pkOrg_j) && pkUser_i.equals(pkUser_j) && contacts_type_i.equals(contacts_type_j)) {
						set.add(pkUser_i);
					}
				}
				if (StringUtils.isNotBlank(pkVisualorg_i) && StringUtils.isNotBlank(pkVisualorg_j)) {
					if (pkVisualorg_i.equals(pkVisualorg_j) && pkUser_i.equals(pkUser_j) && contacts_type_i.equals(contacts_type_j)) {
						set.add(pkUser_i);
					}
				}
			}
		}
		StringBuffer errorMessage = new StringBuffer();
		Iterator iter = set.iterator();
		while(iter.hasNext()) { 
			String pkUser = (String)iter.next();
			errorMessage.append(NoticeUtil.getNameByPkUser(pkUser) + ",");
		}
		if (StringUtils.isNotBlank(errorMessage.toString())) {
			throw new LfwRuntimeException("�����û����ظ����ݣ�" + errorMessage.substring(0, errorMessage.length()-1) + "! ���ܱ��档" );
		}
  }
  public void onDataLoad_user(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
	    LfwRuntimeEnvironment.getWebContext().getWebSession().addOriginalParameter("openBillId",null);
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
}