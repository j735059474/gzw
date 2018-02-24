package nc.scap.pub.notice_manager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.model.ScapCoConstants;
import nc.scap.pub.notice.util.NoticeUtil;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpCtrlUtil;
import nc.scap.pub.util.CpWinUtil;
import nc.scap.pub.util.ScapDAO;
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
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetCellEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.file.FillFileInfoHelper;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.portal.log.ScapLogger;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.scap.work_report_type.Work_report_type;
import nc.vo.scapco.pub_visualorganization.visualOrganization;
import nc.vo.scapco.pub_visualorganization.visualUserInfo;
import nc.vo.scapco.work_notice_contacts.notice_contact_info;
import nc.vo.scapco.work_notice_manager.Notice_manager;
import nc.vo.scapco.work_notice_manager.Receive_man;
import nc.vo.scapco.work_notice_manager.Receive_notice;
import nc.vo.scapjj.userpsn.V_userpsnVO;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.core.locator.ServiceLocator;
import uap.web.bd.pub.AppUtil;
/** 
 * ��Ƭ����Ĭ���߼�
 */
public class Notice_managerCardWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
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
					String tzxf_notice_style = (String) AppUtil.getAppAttr(IGlobalConstants.TZXF_NOTICE_STYLE);
					Dataset masterDs = getMasterDs();
					String pk_primarykey = generatePk();
					row.setValue(
							masterDs.nameToIndex(masterDs.getPrimaryKeyField()),
							pk_primarykey);
					if (StringUtils.isNotBlank(tzxf_notice_style)) {
						// ֪ͨ����
						row.setValue(
								masterDs.nameToIndex(Notice_manager.NOTICE_STYLE),
								tzxf_notice_style);
						String tzxf_message_type_code = (String) AppUtil.getAppAttr(IGlobalConstants.TZXF_MESSAGE_TYPE_CODE);

						// ��Ϣ����
						if (StringUtils.isNotBlank(tzxf_message_type_code)) {
							String[] messageTypeCode = tzxf_message_type_code.substring(1,
									tzxf_message_type_code.length() - 1).split(",");
							if (messageTypeCode != null && messageTypeCode.length > 0) {
								row.setValue(
										masterDs.nameToIndex(Notice_manager.DEF4),
										messageTypeCode[0]);
							} else {
								row.setValue(
										masterDs.nameToIndex(Notice_manager.DEF4),
										IGlobalConstants.STANDARD_MESSAGE_TYPE_CODE);
							}
						} else {
								row.setValue(
										masterDs.nameToIndex(Notice_manager.DEF4),
										IGlobalConstants.STANDARD_MESSAGE_TYPE_CODE);
							}
					} else {
						// ֪ͨ����
						row.setValue(
								masterDs.nameToIndex(Notice_manager.NOTICE_STYLE),
								"1");

						// ֪ͨ����
						row.setValue(
								masterDs.nameToIndex(Notice_manager.NOTICE_CONTENT),
								IGlobalConstants.CONTENT_NOTICE_CONTENT_REPORT);
						// �Ƿ�������
						row.setValue(
								masterDs.nameToIndex(Notice_manager.IS_REMIND), "0");
						// ���ѷ�ʽ
						row.setValue(
								masterDs.nameToIndex(Notice_manager.REMIND_WAY),
								"1");
						// ��������
						row.setValue(
								masterDs.nameToIndex(Notice_manager.REMIND_INFO),
								IGlobalConstants.CONTENT_REMIND_INFO_REPORT);
						// �Ƿ��Զ��߱�
						row.setValue(
								masterDs.nameToIndex(Notice_manager.IS_AUTO_URGE),
								"0");
						// �߱���Ϣ���ͷ�ʽ
						row.setValue(masterDs
								.nameToIndex(Notice_manager.URGE_INFO_TRANS_WAY),
								"1");
						// �߱�Ƶ��
						row.setValue(
								masterDs.nameToIndex(Notice_manager.URGE_FREQUENCY),
								"1");
						// ����ͳ�����ݸ�ʽ������ί�û���
						row.setValue(masterDs
								.nameToIndex(Notice_manager.URGE_CONTENT_GZW),
								IGlobalConstants.CONTENT_URGE_CONTENT_GZW);
						// �߱���Ϣ���ݸ�ʽ����ҵ�û���
						row.setValue(masterDs
								.nameToIndex(Notice_manager.URGE_CONTENT_QY),
								IGlobalConstants.CONTENT_URGE_CONTENT_QY);

						// �ϱ���ֹ����
						row.setValue(masterDs
								.nameToIndex(Notice_manager.EXPIRATION_DATE),
								new UFDate());
					}
					// ֪ͨ�·�״̬
					row.setValue(
							masterDs.nameToIndex(Notice_manager.NOTICE_STATUS),
							"1");
					// Ĭ��֪ͨ����
					row.setValue(
							masterDs.nameToIndex(Notice_manager.NOTICE_DATE),
							new UFDate());
					// ����֪ͨ��
					row.setValue(
							masterDs.nameToIndex(Notice_manager.NOTICE_PSN),
							CntUtil.getCntUserPk());
					// ����֪ͨ��ҵ
					row.setValue(masterDs
							.nameToIndex(Notice_manager.NOTICE_SEND_ORG),
							CntUtil.getCntOrgPk());
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
					FillFileInfoHelper.resetItem(primaryKeyValue);
					// �鿴ʱ���ݲ����޸�
					String pro_action = LfwRuntimeEnvironment.getWebContext()
							.getOriginalParameter("pro_action");
					if ((ScapCoConstants.DETAIL).equals(pro_action)) {
						String pkUser = CntUtil.getCntUserPk();
						String name = NoticeUtil.getNameByPkUser(pkUser);
						Dataset ds = this.getDs();
						// �û������滻
						String notice_content = (String) ds.getValue(ds
								.nameToIndex(Notice_manager.NOTICE_CONTENT));
						Row mrow = ds.getSelectedRow();
						if (notice_content != null && StringUtils.isNotEmpty(notice_content)) {
							mrow.setValue(
									ds.nameToIndex(Notice_manager.NOTICE_CONTENT),
									notice_content.replace(IGlobalConstants.CONTENT_REPLACE_USERNAME,
											name));
						}

						String remind_info = (String) ds.getValue(ds
								.nameToIndex(Notice_manager.REMIND_INFO));
						if (remind_info != null && StringUtils.isNotEmpty(remind_info)) {
							mrow.setValue(
									ds.nameToIndex(Notice_manager.REMIND_INFO),
									remind_info.replace(IGlobalConstants.CONTENT_REPLACE_USERNAME,
											name));
						}

						String urge_content_qy = (String) ds.getValue(ds
								.nameToIndex(Notice_manager.URGE_CONTENT_QY));
						if (urge_content_qy != null && StringUtils.isNotEmpty(urge_content_qy)) {
							mrow.setValue(
									ds.nameToIndex(Notice_manager.URGE_CONTENT_QY),
									urge_content_qy.replace(IGlobalConstants.CONTENT_REPLACE_USERNAME,
											name));
						}
					}
				}
			});
		}
		// �鿴ʱ���ݲ����޸�
		String method_type = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter(IGlobalConstants.METHOD_TYPE);
		if ((IGlobalConstants.BTN_LOOK).equals(method_type)) {
			masterDs.setEnabled(false);
			LfwView view = AppLifeCycleContext.current().getWindowContext()
					.getViewContext("main").getView();
			Dataset receive_noticeds = view.getViewModels().getDataset(
					"receive_notice");
			receive_noticeds.setEnabled(false);
		  	Dataset receive_mands = view.getViewModels().getDataset("receive_man");
		  	receive_mands.setEnabled(false);
		}
  }
  /** 
 * ������ѡ���߼�
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent dsEvent){
    Dataset ds = dsEvent.getSource();
		String oper = getOperator();
		LfwView view = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		FormComp form = (FormComp) view
				.getComponentsByDataset("notice_manager")[0];
		String notice_style = (String) ds.getValue(ds
				.nameToIndex(Notice_manager.NOTICE_STYLE));// ��ȡ֪ͨ����
		if (AppConsts.OPE_ADD.equals(oper)) {
			// �������ͳ��ڻ�ʱ��Ϊ����
			// ��������
			FormElement data_type_name = form.getElementById("data_type_name"); // ��������
			data_type_name.setVisible(false);

		} else if (AppConsts.OPE_EDIT.equals(oper)) {
			if (notice_style != null && "2".equals(notice_style)) { // ��Ϣ֪ͨ
				// ҵ������
				FormElement business_type_name = form
						.getElementById("business_type_name");
				business_type_name.setVisible(false);
				// ����ʵ������
				FormElement notice_type_report_type = form
						.getElementById("notice_type_report_type");
				notice_type_report_type.setVisible(false);
				// �Ƿ��Զ��߱�
				FormElement is_auto_urge = form.getElementById("is_auto_urge"); // �Ƿ��Զ��߱�
				is_auto_urge.setVisible(false);
				// �߱���Ϣ���ͷ�ʽ
				FormElement urge_info_trans_way = form
						.getElementById("urge_info_trans_way"); // �߱���Ϣ���ͷ�ʽ
				urge_info_trans_way.setVisible(false);
				// ��ǰ�߱�����
				FormElement day_num = form.getElementById("day_num"); // ��ǰ�߱�����
				day_num.setVisible(false);
				// �߱�Ƶ��
				FormElement urge_frequency = form
						.getElementById("urge_frequency"); // �߱�Ƶ��
				urge_frequency.setVisible(false);

				// ����ͳ�����ݸ�ʽ������ί�û���
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // ����ͳ�����ݸ�ʽ������ί�û���
				urge_content_gzw.setVisible(false);

				// �߱���Ϣ���ݸ�ʽ����ҵ�û���
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // �߱���Ϣ���ݸ�ʽ����ҵ�û���
				urge_content_qy.setVisible(false);

			} else if (notice_style != null && "1".equals(notice_style)) {// �֪ͨ
				// ��������
				FormElement data_type_name = form.getElementById("data_type_name"); 
				data_type_name.setVisible(false);
			}
			//�޸Ľ����������session����
			addSessionAppAttr();
		}
		if (AppConsts.OPE_EDIT.equals(oper)) {
	
			CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()) {
				protected String postProcessRowSelectVO(SuperVO vo, Dataset ds) {
					
					List<String> listOrg = NoticeUtil
							.getAllChildPk_OrgByCurrentPk(CntUtil.getCntOrgPk());
					List<String> listVisualOrg = NoticeUtil
							.getAllChildPKVisualorgByPkUser(CntUtil.getCntUserPk());
					StringBuffer condition = new StringBuffer("pk_notice = '"
							+ vo.getAttributeValue("pk_notice") + "' and DR = '0' ");
					String reportBody = NoticeUtil.getReportBodyByNoticePk((String)vo.getAttributeValue("pk_notice"));
					String id = ds.getId();
					if (id != null) {
						//��������ҵ�ӱ��wheresql
						if (id.equals("receive_notice")) {
								if (IGlobalConstants.REPORT_BODY_QY.equals(reportBody)) {
									if (listOrg != null && listOrg.size() > 0) {
										condition.append(" and (")
										.append(NoticeUtil.getSqlStrByList(listOrg, 1000,"NOTICE_ORG"))
										.append(")");
									}
								} else if (IGlobalConstants.REPORT_BODY_VISORG.equals(reportBody)) {
									if (listVisualOrg != null && listVisualOrg.size() > 0) {
										condition.append(" and (")
										.append(NoticeUtil.getSqlStrByList(listVisualOrg, 1000,"NOTICE_VISUAL_ORG"))
										.append(")");
									}
								} else if (IGlobalConstants.REPORT_BODY_MAN.equals(reportBody)) {
									if (listOrg != null && listOrg.size() > 0) {
										condition.append(" and ((")
										.append(NoticeUtil.getSqlStrByList(listOrg, 1000,"NOTICE_ORG"))
										.append(")");
									}
									if (listVisualOrg != null && listVisualOrg.size() > 0) {
										condition.append(" or (")
										.append(NoticeUtil.getSqlStrByList(listVisualOrg, 1000,"NOTICE_VISUAL_ORG"))
										.append("))");
									} else {
										condition.append(")");
									}
								}
						//���������ӱ��wheresql
						} else if (id.equals("receive_man")) {
							
								if (IGlobalConstants.REPORT_BODY_QY.equals(reportBody)) {
									if (listOrg != null && listOrg.size() > 0) {
										condition.append(" and (")
										.append(NoticeUtil.getSqlStrByList(listOrg, 1000,"RECEIVE_ORG"))
										.append(")");
									}
								} else if (IGlobalConstants.REPORT_BODY_VISORG.equals(reportBody)) {
									if (listVisualOrg != null && listVisualOrg.size() > 0) {
										condition.append(" and (")
										.append(NoticeUtil.getSqlStrByList(listVisualOrg, 1000,"RECEIVE_VISUAL_ORG"))
										.append(")");
									}
								} else if (IGlobalConstants.REPORT_BODY_MAN.equals(reportBody)) {
									if (listOrg != null && listOrg.size() > 0) {
										condition.append(" and ((")
										.append(NoticeUtil.getSqlStrByList(listOrg, 1000,"RECEIVE_ORG"))
										.append(")");
									}
									if (listVisualOrg != null && listVisualOrg.size() > 0) {
										condition.append(" or (")
										.append(NoticeUtil.getSqlStrByList(listVisualOrg, 1000,"RECEIVE_VISUAL_ORG"))
										.append("))");
									} else {
										condition.append(")");
									}
	
								}
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
        CpWinUtil
        .refshAttachList("main", "attachout",
                        ds.getValue(ds.getPrimaryKeyField())
                                        .toString(), true, true, true);
  }
  /** 
 * ����
 * @param datasetEvent
 */
  public void onSave(  MouseEvent<?> mouseEvent) throws BusinessException {
    Dataset masterDs = this.getMasterDs();
	  	// ����ҵ�����ͣ��������ͣ��ϱ���ֹ���������ֶ�ȷ��һ��֪ͨ������������ֶβ��õĻ�������Ϊ������֪ͨ��
	  	// ����Ļ������ܱ���
	  	Row row = masterDs.getSelectedRow();
	  	String notice_style = (String)row.getValue(masterDs.nameToIndex(Notice_manager.NOTICE_STYLE));
	  	String business_type = (String)row.getValue(masterDs.nameToIndex(Notice_manager.BUSINESS_TYPE));
	  	String notice_type = (String)row.getValue(masterDs.nameToIndex(Notice_manager.NOTICE_TYPE));
	  	UFDate expiration_date = (UFDate)row.getValue(masterDs.nameToIndex(Notice_manager.EXPIRATION_DATE));
	  	if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
		  	if (expiration_date == null) {
		  		throw new LfwRuntimeException("�ϱ���ֹ����Ϊ�����");
		  	}
		  	String oper = getOperator();
		  	Boolean isUnique = NoticeUtil.checkIsUnique(business_type,notice_type,expiration_date,oper);
		  	if (Boolean.FALSE.equals(isUnique)) {
		           throw new LfwRuntimeException("���֪ͨ�Ѵ��ڣ���֪ͨ���ܱ��棡");
		  	}
	  	}
	  	// ��������
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
				int pkOrgIndex = ds.nameToIndex("pk_org");
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
  @Override protected String getMasterDsId(){
    return "notice_manager";
  }
  /** 
 * @param datasetCellEvent
 */
  public void onAfterDataChange(  DatasetCellEvent datasetCellEvent){
    Dataset masterDs = getMasterDs();
		int colIndex = datasetCellEvent.getColIndex();
		LfwView view = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		FormComp form = (FormComp) view
				.getComponentsByDataset("notice_manager")[0];
		GridComp receiveNoticeGrid = (GridComp)view.getViewComponents().getComponent("receive_notice_grid");
		GridComp receiveManGrid = (GridComp)view.getViewComponents().getComponent("receive_man_grid");
		// ���֪ͨ���͸ı�,�����ֶ�
		if (colIndex == masterDs.nameToIndex(Notice_manager.NOTICE_STYLE)) {
			FormElement notice_style = form.getElementById("notice_style"); // ��ȡ֪ͨ����
			// �֪ͨ����ʾ��Ҫ��ʾ���ֶΣ��߱���أ�
			if (notice_style.getValue().equals("1")) {
				// ֪ͨ����
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_CONTENT), IGlobalConstants.CONTENT_NOTICE_CONTENT_REPORT);
				// ����ҵ������
				FormElement business_type_name = form
						.getElementById("business_type_name"); // ����ҵ������
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.BUSINESS_TYPE), "");
				business_type_name.setVisible(true);
				business_type_name.setNullAble(false);
				// ��������
				FormElement notice_type_report_type = form
						.getElementById("notice_type_report_type"); // ��������
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_TYPE), "");
				notice_type_report_type.setVisible(true);
				notice_type_report_type.setNullAble(false);
				// ��������
				FormElement data_type_name = form
						.getElementById("data_type_name"); // ��������
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.DATA_TYPE), "");
				data_type_name.setVisible(false);
				data_type_name.setNullAble(true);
				// ���߱���ء�
				FormElement urge_related = form.getElementById("urge_related"); // ���߱���ء�
				urge_related.setVisible(true);
				// �Ƿ��Զ��߱�
				FormElement is_auto_urge = form.getElementById("is_auto_urge"); // �Ƿ��Զ��߱�
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.IS_AUTO_URGE), "0");
				is_auto_urge.setVisible(true);
				// �߱���Ϣ���ͷ�ʽ
				FormElement urge_info_trans_way = form
						.getElementById("urge_info_trans_way"); // �߱���Ϣ���ͷ�ʽ
				masterDs.setValue(masterDs
						.nameToIndex(Notice_manager.URGE_INFO_TRANS_WAY), "1");
				urge_info_trans_way.setVisible(true);
				// ��ǰ�߱�����
				FormElement day_num = form.getElementById("day_num"); // ��ǰ�߱�����
				masterDs.setValue(masterDs.nameToIndex(Notice_manager.DAY_NUM),
						"");
				day_num.setVisible(true);
				// �߱�Ƶ��
				FormElement urge_frequency = form
						.getElementById("urge_frequency"); // �߱�Ƶ��
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.URGE_FREQUENCY),
						"1");
				urge_frequency.setVisible(true);

				// ����ͳ�����ݸ�ʽ������ί�û���
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // ����ͳ�����ݸ�ʽ������ί�û���
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.URGE_CONTENT_GZW),
						IGlobalConstants.CONTENT_URGE_CONTENT_GZW);
				urge_content_gzw.setVisible(true);

				// �߱���Ϣ���ݸ�ʽ����ҵ�û���
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // �߱���Ϣ���ݸ�ʽ����ҵ�û���
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.URGE_CONTENT_QY),
						IGlobalConstants.CONTENT_URGE_CONTENT_QY);
				urge_content_qy.setVisible(true);
				// ��������
				FormElement remind_info = form.getElementById("remind_info"); // ��������
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.REMIND_INFO),
						IGlobalConstants.CONTENT_REMIND_INFO_REPORT);
				remind_info.setVisible(true);
				String[] elesReceiveNoticeGrid = new String[] { "report_status","report_time"};
				CpCtrlUtil.gridColmVisiable(receiveNoticeGrid,elesReceiveNoticeGrid,true);
				String[] elesReceiveManGrid = new String[] { "report_status","report_time"};
				CpCtrlUtil.gridColmVisiable(receiveManGrid,elesReceiveManGrid,true);
			} else if (notice_style.getValue().equals("2")) { // ������Ϣ֪ͨʱ
																// ������Ӧ�ֶ�����,�������;
				// ֪ͨ����
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_CONTENT), IGlobalConstants.CONTENT_NOTICE_CONTENT_MESSAGE);
				// ����ҵ������
				FormElement business_type_name = form
						.getElementById("business_type_name"); // ����ҵ������
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.BUSINESS_TYPE), "");
				business_type_name.setVisible(false);

				// ��������
				FormElement notice_type_report_type = form
						.getElementById("notice_type_report_type"); // ��������
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_TYPE), "");
				notice_type_report_type.setVisible(false);

				// ��������
				FormElement data_type_name = form
						.getElementById("data_type_name"); // ��������
				// ����list������ڻ�ʱ�鵽�ĸ��û��ڳ�����ϵ��ά��ʱ�������������� ����һ��Ĭ�ϵ��������ͣ�������Ϣ֪ͨ��
		  		masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.DATA_TYPE), "");
			  	data_type_name.setVisible(true);

				// ���߱���ء�
				FormElement urge_related = form.getElementById("urge_related"); // ���߱���ء�
				urge_related.setVisible(false);
				
				// �Ƿ��Զ��߱�
				FormElement is_auto_urge = form.getElementById("is_auto_urge"); // �Ƿ��Զ��߱�
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.IS_AUTO_URGE), "");
				is_auto_urge.setVisible(false);
				// �߱���Ϣ���ͷ�ʽ
				FormElement urge_info_trans_way = form
						.getElementById("urge_info_trans_way"); // �߱���Ϣ���ͷ�ʽ
				masterDs.setValue(masterDs
						.nameToIndex(Notice_manager.URGE_INFO_TRANS_WAY), "");
				urge_info_trans_way.setVisible(false);
				// ��ǰ�߱�����
				FormElement day_num = form.getElementById("day_num"); // ��ǰ�߱�����
				masterDs.setValue(masterDs.nameToIndex(Notice_manager.DAY_NUM),
						"");
				day_num.setVisible(false);
				// �߱�Ƶ��
				FormElement urge_frequency = form
						.getElementById("urge_frequency"); // �߱�Ƶ��
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.URGE_FREQUENCY), "");
				urge_frequency.setVisible(false);

				// ����ͳ�����ݸ�ʽ������ί�û���
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // ����ͳ�����ݸ�ʽ������ί�û���
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.URGE_CONTENT_GZW),
						"");
				urge_content_gzw.setVisible(false);

				// �߱���Ϣ���ݸ�ʽ����ҵ�û���
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // �߱���Ϣ���ݸ�ʽ����ҵ�û���
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.URGE_CONTENT_QY),
						"");
				urge_content_qy.setVisible(false);
				// ��������
				FormElement remind_info = form.getElementById("remind_info"); // ��������
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.REMIND_INFO),
						IGlobalConstants.CONTENT_REMIND_INFO_MESSAGE);
				remind_info.setVisible(true);
				String[] elesReceiveNoticeGrid = new String[] { "report_status","report_time"};
				CpCtrlUtil.gridColmVisiable(receiveNoticeGrid,elesReceiveNoticeGrid,false);
				String[] elesReceiveManGrid = new String[] { "report_status","report_time"};
				CpCtrlUtil.gridColmVisiable(receiveManGrid,elesReceiveManGrid,false);
			} else if (notice_style.getValue().equals("3"))   { // ��Ϣ֪ͨ
				//�������ͨ��Ϣ֪ͨ�Ļ�������Ӧ�ֶε�����
				String[] elesNoticeManagerForm = new String[] { "business_type_name","notice_type_report_type","data_type_name",
						"urge_related","is_auto_urge","urge_info_trans_way", 
						"day_num", "urge_frequency", "urge_content_gzw","urge_content_qy",
						"remind_related","is_remind","remind_way","remind_info","expiration_date"};
				CpCtrlUtil.visiableFormEles(form, false, elesNoticeManagerForm);
				CpCtrlUtil.visiableFormEles(form, true, new String[]{"def4"});
				CpCtrlUtil.editableFormEles(form, false, new String[] {"notice_style"});
				CpCtrlUtil.nullableFormEles(form, true, new String[] {"expiration_date"});
				String[] elesReceiveNoticeGrid = new String[] { "report_status","report_time"};
				CpCtrlUtil.gridColmVisiable(receiveNoticeGrid,elesReceiveNoticeGrid,false);
				String[] elesReceiveManGrid = new String[] { "report_status","report_time"};
				CpCtrlUtil.gridColmVisiable(receiveManGrid,elesReceiveManGrid,false);
			}
		} else if (colIndex == masterDs
				.nameToIndex(Notice_manager.BUSINESS_TYPE)) {

			if ((String) masterDs.getValue(masterDs
					.nameToIndex(Notice_manager.BUSINESS_TYPE)) != null) {
				String business_type = form.getElementById("business_type").getValue();

				AppUtil.addAppAttr("pk_business_type",
						business_type);
				String business_type_name = form.getElementById(
						"business_type_name").getValue();
				// �����һ��ҵ������
				String def1 = (String) masterDs.getValue(masterDs
						.nameToIndex(Notice_manager.DEF1));
				// ֪ͨ����
				FormElement notice_content = form
						.getElementById("notice_content"); // ֪ͨ����
				String notice_contentS = (String) masterDs.getValue(masterDs
						.nameToIndex(Notice_manager.NOTICE_CONTENT));
				String notice_contentNew = "";
				if (def1 != null) {
					if (StringUtils.isNotBlank(notice_contentS) && notice_contentS.contains(def1)) {
						notice_contentNew = notice_contentS.replace(def1,
								business_type_name);
					} else {
						notice_contentNew = notice_contentS;
					}

				} else {
					if (StringUtils.isNotBlank(notice_contentS) && notice_contentS.contains(IGlobalConstants.CONTENT_REPLACE_BUSINESS_TYPE)) {
						notice_contentNew = notice_contentS.replace(IGlobalConstants.CONTENT_REPLACE_BUSINESS_TYPE,
								business_type_name);
					} else {
						notice_contentNew = notice_contentS;
					}
				}
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_CONTENT),
						notice_contentNew);
				notice_content.setVisible(true);
				// ����ͳ�����ݸ�ʽ������ί�û���
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // ����ͳ�����ݸ�ʽ������ί�û���
				String urge_content_gzwS = (String) masterDs.getValue(masterDs
						.nameToIndex(Notice_manager.URGE_CONTENT_GZW));
				String urge_content_gzwNew = "";
				if (def1 != null) {
					if (StringUtils.isNotBlank(urge_content_gzwS) && urge_content_gzwS.contains(def1)) {
						urge_content_gzwNew = urge_content_gzwS.replace(def1,
								business_type_name);
					} else {
						urge_content_gzwNew = urge_content_gzwS;
					}
				} else {
					if (StringUtils.isNotBlank(urge_content_gzwS) && urge_content_gzwS.contains(IGlobalConstants.CONTENT_REPLACE_BUSINESS_TYPE)) {
						urge_content_gzwNew = urge_content_gzwS.replace(IGlobalConstants.CONTENT_REPLACE_BUSINESS_TYPE,
								business_type_name);	
					} else {
						urge_content_gzwNew = urge_content_gzwS;
					}
				}
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.URGE_CONTENT_GZW),
						urge_content_gzwNew);
				urge_content_gzw.setVisible(true);

				// �߱���Ϣ���ݸ�ʽ����ҵ�û���
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // �߱���Ϣ���ݸ�ʽ����ҵ�û���
				String urge_content_qyS = (String) masterDs.getValue(masterDs
						.nameToIndex(Notice_manager.URGE_CONTENT_QY));
				String urge_content_qyNew = "";
				if (def1 != null) {
					if (StringUtils.isNotBlank(urge_content_qyS) && urge_content_qyS.contains(def1)) {
						urge_content_qyNew = urge_content_qyS.replace(def1,
								business_type_name);
					} else {
						urge_content_qyNew = urge_content_qyS;
					}
				} else {
					if (StringUtils.isNotBlank(urge_content_qyS) && urge_content_qyS.contains(IGlobalConstants.CONTENT_REPLACE_BUSINESS_TYPE)) {
						urge_content_qyNew = urge_content_qyS.replace(IGlobalConstants.CONTENT_REPLACE_BUSINESS_TYPE,
								business_type_name);
					} else {
						urge_content_qyNew = urge_content_qyS;
					}
				}
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.URGE_CONTENT_QY),
						urge_content_qyNew);
				urge_content_qy.setVisible(true);
				// ��������
				FormElement remind_info = form.getElementById("remind_info"); // ��������
				String remind_infoS = (String) masterDs.getValue(masterDs
						.nameToIndex(Notice_manager.REMIND_INFO));
				String remind_infoNew = "";
				if (def1 != null) {
					if (StringUtils.isNotBlank(remind_infoS) && remind_infoS.contains(def1)) {
						remind_infoNew = remind_infoS.replace(def1,
								business_type_name);
					} else {
						remind_infoNew = remind_infoS;
					}

				} else {
					if (StringUtils.isNotBlank(remind_infoS) && remind_infoS.contains(IGlobalConstants.CONTENT_REPLACE_BUSINESS_TYPE)) {
						remind_infoNew = remind_infoS.replace(IGlobalConstants.CONTENT_REPLACE_BUSINESS_TYPE,
								business_type_name);
					} else {
						remind_infoNew = remind_infoS;
					}
				}
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.REMIND_INFO),
						remind_infoNew);
				remind_info.setVisible(true);
				// �����µ�ҵ�����͵�def1��
				masterDs.setValue(masterDs.nameToIndex(Notice_manager.DEF1),
						business_type_name);
			}

		} else if (colIndex == masterDs.nameToIndex(Notice_manager.NOTICE_TYPE)) {
			if ((String) masterDs.getValue(masterDs
					.nameToIndex(Notice_manager.NOTICE_TYPE)) != null) {
				String notice_type_report_type = form.getElementById(
						"notice_type_report_type").getValue();
				String notice_type = form.getElementById(
						"notice_type").getValue();
				// �����һ����������
				String def2 = (String) masterDs.getValue(masterDs
						.nameToIndex(Notice_manager.DEF2));
				// ֪ͨ����
				FormElement notice_content = form
						.getElementById("notice_content"); // ֪ͨ����
				String notice_contentS = (String) masterDs.getValue(masterDs
						.nameToIndex(Notice_manager.NOTICE_CONTENT));
				String notice_contentNew = "";
				if (def2 != null) {
					if (StringUtils.isNotBlank(notice_contentS) && notice_contentS.contains(def2)) {
						notice_contentNew = notice_contentS.replace(def2,
								notice_type_report_type);
					} else {
						notice_contentNew = notice_contentS;
					}
				} else {
					if (StringUtils.isNotBlank(notice_contentS) && notice_contentS.contains(IGlobalConstants.CONTENT_REPLACE_REPORT_TYPE)) {
						notice_contentNew = notice_contentS.replace(IGlobalConstants.CONTENT_REPLACE_REPORT_TYPE,
								notice_type_report_type);
					} else {
						notice_contentNew = notice_contentS;
					}
				}
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_CONTENT),
						notice_contentNew);
				notice_content.setVisible(true);
				// ����ͳ�����ݸ�ʽ������ί�û���
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // ����ͳ�����ݸ�ʽ������ί�û���
				String urge_content_gzwS = (String) masterDs.getValue(masterDs
						.nameToIndex(Notice_manager.URGE_CONTENT_GZW));
				String urge_content_gzwNew = "";
				if (def2 != null) {
					if (StringUtils.isNotBlank(urge_content_gzwS) && urge_content_gzwS.contains(def2)) {
						urge_content_gzwNew = urge_content_gzwS.replace(def2,
								notice_type_report_type);
					} else {
						urge_content_gzwNew = urge_content_gzwS;
					}
				} else {
					if (StringUtils.isNotBlank(urge_content_gzwS) && urge_content_gzwS.contains(IGlobalConstants.CONTENT_REPLACE_REPORT_TYPE)) {
						urge_content_gzwNew = urge_content_gzwS.replace(IGlobalConstants.CONTENT_REPLACE_REPORT_TYPE,
								notice_type_report_type);
					} else {
						urge_content_gzwNew = urge_content_gzwS;
					}
				}
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.URGE_CONTENT_GZW),
						urge_content_gzwNew);
				urge_content_gzw.setVisible(true);

				// �߱���Ϣ���ݸ�ʽ����ҵ�û���
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // �߱���Ϣ���ݸ�ʽ����ҵ�û���
				String urge_content_qyS = (String) masterDs.getValue(masterDs
						.nameToIndex(Notice_manager.URGE_CONTENT_QY));
				String urge_content_qyNew = "";
				if (def2 != null) {
					if (StringUtils.isNotBlank(urge_content_qyS) && urge_content_qyS.contains(def2)) {
						urge_content_qyNew = urge_content_qyS.replace(def2,
								notice_type_report_type);
					} else {
						urge_content_qyNew = urge_content_qyS;
					}
				} else {
					if (StringUtils.isNotBlank(urge_content_qyS) && urge_content_qyS.contains(IGlobalConstants.CONTENT_REPLACE_REPORT_TYPE)) {
						urge_content_qyNew = urge_content_qyS.replace(IGlobalConstants.CONTENT_REPLACE_REPORT_TYPE,
								notice_type_report_type);
					} else {
						urge_content_qyNew = urge_content_qyS;
					}
				}
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.URGE_CONTENT_QY),
						urge_content_qyNew);
				urge_content_qy.setVisible(true);
				// ��������
				FormElement remind_info = form.getElementById("remind_info"); // ��������
				String remind_infoS = (String) masterDs.getValue(masterDs
						.nameToIndex(Notice_manager.REMIND_INFO));
				String remind_infoNew = "";
				if (def2 != null) {
					if (StringUtils.isNotBlank(remind_infoS) && remind_infoS.contains(def2)) {
						remind_infoNew = remind_infoS.replace(def2,
								notice_type_report_type);
					} else {
						remind_infoNew = remind_infoS;
					}

				} else {
					if (StringUtils.isNotBlank(remind_infoS) && remind_infoS.contains(IGlobalConstants.CONTENT_REPLACE_REPORT_TYPE)) {
						remind_infoNew = remind_infoS.replace(IGlobalConstants.CONTENT_REPLACE_REPORT_TYPE,
								notice_type_report_type);
					} else {
						remind_infoNew = remind_infoS;
					}
				}
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.REMIND_INFO),
						remind_infoNew);
				remind_info.setVisible(true);
				// �����µı������͵�def2��
				masterDs.setValue(masterDs.nameToIndex(Notice_manager.DEF2),
						notice_type_report_type);
			}
		} else if (colIndex == masterDs.nameToIndex(Notice_manager.DATA_TYPE)) {
			if ((String) masterDs.getValue(masterDs
					.nameToIndex(Notice_manager.DATA_TYPE)) != null) {
				String data_type_name = form.getElementById("data_type_name")
						.getValue();
				// �����һ����������
				String def3 = (String) masterDs.getValue(masterDs
						.nameToIndex(Notice_manager.DEF3));
				// test
				String DATA_TYPE = (String) masterDs.getValue(masterDs
						.nameToIndex(Notice_manager.DATA_TYPE));
				// ֪ͨ����
				FormElement notice_content = form
						.getElementById("notice_content"); // ֪ͨ����
				String notice_contentS = (String) masterDs.getValue(masterDs
						.nameToIndex(Notice_manager.NOTICE_CONTENT));
				String notice_contentNew = "";
				if (def3 != null) {
					if (StringUtils.isNotBlank(notice_contentS) && notice_contentS.contains(def3)) {
						notice_contentNew = notice_contentS.replace(def3,
								data_type_name);
					} else {
						notice_contentNew = notice_contentS;
					}

				} else {
					if (StringUtils.isNotBlank(notice_contentS) && notice_contentS.contains(IGlobalConstants.CONTENT_REPLACE_DATA_TYPE)) {
						notice_contentNew = notice_contentS.replace(IGlobalConstants.CONTENT_REPLACE_DATA_TYPE,
								data_type_name);
					} else {
						notice_contentNew = notice_contentS;
					}

				}
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_CONTENT),
						notice_contentNew);
				notice_content.setVisible(true);
				
				// ��������
				FormElement remind_info = form.getElementById("remind_info"); // ��������
				String remind_infoS = (String) masterDs.getValue(masterDs
						.nameToIndex(Notice_manager.REMIND_INFO));
				String remind_infoNew = "";
				if (def3 != null) {
					if (StringUtils.isNotBlank(remind_infoS) && remind_infoS.contains(def3)) {
						remind_infoNew = remind_infoS.replace(def3, data_type_name);
					} else {
						remind_infoNew = remind_infoS;
					}
					
				} else {
					if (StringUtils.isNotBlank(remind_infoS) && remind_infoS.contains(IGlobalConstants.CONTENT_REPLACE_DATA_TYPE)) {
						remind_infoNew = remind_infoS.replace(IGlobalConstants.CONTENT_REPLACE_DATA_TYPE,
								data_type_name);
					} else {
						remind_infoNew = remind_infoS;
					}
				}
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.REMIND_INFO),
						remind_infoNew);
				remind_info.setVisible(true);
				// �����µ��������͵�def3��
				masterDs.setValue(masterDs.nameToIndex(Notice_manager.DEF3),
						data_type_name);
			}
		}
		// ���"�Ƿ�������"�ı�
		if (colIndex == masterDs.nameToIndex(Notice_manager.IS_REMIND)) {
			FormElement is_remind = form.getElementById("is_remind"); // ��ȡ�Ƿ�������
			// �Ƿ������ѣ� 0���ǣ�1����
			if ("0".equals(is_remind.getValue())) {
				// ���ѷ�ʽ
				FormElement remind_way = form.getElementById("remind_way"); // ���ѷ�ʽ
				remind_way.setEnabled(true);
				// ��������
				FormElement remind_info = form.getElementById("remind_info"); // ��������
				remind_info.setEnabled(true);
			}
			if ("1".equals(is_remind.getValue())) {
				// ���ѷ�ʽ
				FormElement remind_way = form.getElementById("remind_way"); // ���ѷ�ʽ
				remind_way.setEnabled(false);
				// ��������
				FormElement remind_info = form.getElementById("remind_info"); // ��������
				remind_info.setEnabled(false);
			}
		}
		// ���"�Ƿ��Զ��߱�"�ı�
		if (colIndex == masterDs.nameToIndex(Notice_manager.IS_AUTO_URGE)) {
			FormElement is_auto_urge = form.getElementById("is_auto_urge"); // ��ȡ�Ƿ��Զ��߱�
			// �Ƿ��Զ��߱��� 0���ǣ�1����
			if ("0".equals(is_auto_urge.getValue())) {
				// �߱���Ϣ���ͷ�ʽ
				FormElement urge_info_trans_way = form
						.getElementById("urge_info_trans_way"); // �߱���Ϣ���ͷ�ʽ
				urge_info_trans_way.setEnabled(true);
				// ��ǰ�߱�����
				FormElement day_num = form.getElementById("day_num"); // ��ǰ�߱�����
				day_num.setEnabled(true);
				// �߱�Ƶ��
				FormElement urge_frequency = form
						.getElementById("urge_frequency"); // �߱�Ƶ��
				urge_frequency.setEnabled(true);
				// ����ͳ�����ݸ�ʽ������ί�û���
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // ����ͳ�����ݸ�ʽ������ί�û���
				urge_content_gzw.setEnabled(true);
				// �߱���Ϣ���ݸ�ʽ����ҵ�û���
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // �߱���Ϣ���ݸ�ʽ����ҵ�û���
				urge_content_qy.setEnabled(true);
			}
			if ("1".equals(is_auto_urge.getValue())) {
				// �߱���Ϣ���ͷ�ʽ
				FormElement urge_info_trans_way = form
						.getElementById("urge_info_trans_way"); // �߱���Ϣ���ͷ�ʽ
				urge_info_trans_way.setEnabled(false);
				// ��ǰ�߱�����
				FormElement day_num = form.getElementById("day_num"); // ��ǰ�߱�����
				day_num.setEnabled(false);
				// �߱�Ƶ��
				FormElement urge_frequency = form
						.getElementById("urge_frequency"); // �߱�Ƶ��
				urge_frequency.setEnabled(false);
				// ����ͳ�����ݸ�ʽ������ί�û���
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // ����ͳ�����ݸ�ʽ������ί�û���
				urge_content_gzw.setEnabled(false);
				// �߱���Ϣ���ݸ�ʽ����ҵ�û���
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // �߱���Ϣ���ݸ�ʽ����ҵ�û���
				urge_content_qy.setEnabled(false);
			}

		}
		// �����Ӧ��������Ѿ�����ֵ����ô�Ϳ���ѡ����ҵ�ˡ�����������������session����������ݣ�
		canSelectComp();
  }
  public void onNoticeIssueClick(  MouseEvent mouseEvent){
    Dataset masterDs = getMasterDs();
		LfwView view = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset receive_noticeds = view.getViewModels().getDataset(
				"receive_notice");
	  	Dataset receive_mands = view.getViewModels().getDataset("receive_man");
		Row selectedRow = masterDs.getSelectedRow();
		String notice_style = selectedRow.getValue(masterDs
				.nameToIndex(Notice_manager.NOTICE_STYLE)) == null ? ""
				: selectedRow.getValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_STYLE))
						.toString();
		String business_type = selectedRow.getValue(masterDs
				.nameToIndex(Notice_manager.BUSINESS_TYPE)) == null ? ""
				: selectedRow.getValue(
						masterDs.nameToIndex(Notice_manager.BUSINESS_TYPE))
						.toString();
		String notice_type = selectedRow.getValue(masterDs
				.nameToIndex(Notice_manager.NOTICE_TYPE)) == null ? ""
				: selectedRow.getValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_TYPE))
						.toString();
		String data_type = selectedRow.getValue(masterDs
				.nameToIndex(Notice_manager.DATA_TYPE)) == null ? ""
				: selectedRow.getValue(
						masterDs.nameToIndex(Notice_manager.DATA_TYPE))
						.toString();

	  	// ���û�н��շ�Χ���ݻ��߽��������ݣ����ܱ���
		if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
			if (StringUtils.isBlank(business_type)) {
				throw new LfwRuntimeException("ҵ�����Ͳ���Ϊ�գ�");
			}
			if (StringUtils.isBlank(notice_type)) {
				throw new LfwRuntimeException("�������Ͳ���Ϊ�գ�");
			}
			String report_body = NoticeUtil.getReportBodyByNoticeType(notice_type);
			if (IGlobalConstants.REPORT_BODY_QY.equals(report_body)) {
				if (receive_noticeds.getAllRow().length == 0) {
					throw new LfwRuntimeException("û��ѡ�������ҵ�����ܱ���֪ͨ��");
				}
			}
			if (IGlobalConstants.REPORT_BODY_VISORG.equals(report_body)) {
				if (receive_noticeds.getAllRow().length == 0) {
					throw new LfwRuntimeException("û��ѡ�������֯�����ܱ���֪ͨ��");
				}
			}
			if (IGlobalConstants.REPORT_BODY_MAN.equals(report_body)) {
				if (receive_mands.getAllRow().length == 0) {
					throw new LfwRuntimeException("û��ѡ������ˣ����ܱ���֪ͨ��");
				}
			}
			// Ψһ�Լ��
			UFDate expiration_date = (UFDate)selectedRow.getValue(
							masterDs.nameToIndex(Notice_manager.EXPIRATION_DATE));
		  	if (expiration_date == null) {
		  		throw new LfwRuntimeException("�ϱ���ֹ����Ϊ�����");
		  	}
		  	String oper = getOperator();
			Boolean isUnique = NoticeUtil.checkIsUnique(business_type, notice_type, expiration_date,oper);
		  	if (Boolean.FALSE.equals(isUnique)) {
		           throw new LfwRuntimeException("���֪ͨ�Ѵ��ڣ���֪ͨ���ܱ��棡");
		  	}
		}
		if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)) {
			if (StringUtils.isBlank(data_type)) {
				throw new LfwRuntimeException("�������Ͳ���Ϊ�գ�");
			}
			if (receive_mands.getAllRow().length == 0) {
				throw new LfwRuntimeException("û��ѡ������ˣ����ܱ���֪ͨ��");
			}
		}
		if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
			// �������ͨ��Ϣ֪ͨ����ôĬ�ϵı��������Ǹ��ˣ����֪ͨ�·����ܽڵ������˱�������Ĳ�������ô�����趨�Ĳ��ս�������
			String report_body = IGlobalConstants.REPORT_BODY_MAN;
			String tzxf_report_body = (String)AppUtil.getAppAttr(IGlobalConstants.TZXF_REPORTBODY);
			if (StringUtils.isNotBlank(tzxf_report_body)) {
				report_body = tzxf_report_body;
			}
			if (IGlobalConstants.REPORT_BODY_QY.equals(report_body)) {
				if (receive_noticeds.getAllRow().length == 0) {
					throw new LfwRuntimeException("û��ѡ�������ҵ�����ܱ���֪ͨ��");
				}
			}
			if (IGlobalConstants.REPORT_BODY_VISORG.equals(report_body)) {
				if (receive_noticeds.getAllRow().length == 0) {
					throw new LfwRuntimeException("û��ѡ�������֯�����ܱ���֪ͨ��");
				}
			}
			if (IGlobalConstants.REPORT_BODY_MAN.equals(report_body)) {
				if (receive_mands.getAllRow().length == 0) {
					throw new LfwRuntimeException("û��ѡ������ˣ����ܱ���֪ͨ��");
				}
			}
		}
		// ��������
		CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this
				.getDetailDsIds(), false));

		String pk_notice = selectedRow.getValue(masterDs
				.nameToIndex(Notice_manager.PK_NOTICE)) == null ? ""
				: selectedRow.getValue(
						masterDs.nameToIndex(Notice_manager.PK_NOTICE))
						.toString();
		StringBuffer errorMessage = new StringBuffer();
		// check�Ƿ�����·�֪ͨ
		String report_body = "";
		if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
			report_body = NoticeUtil.getReportBodyByNoticeType(notice_type);
		}
		if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
			// �������ͨ��Ϣ֪ͨ����ôĬ�ϵı��������Ǹ��ˣ����֪ͨ�·����ܽڵ������˱�������Ĳ�������ô�����趨�Ĳ��ս�������
			report_body = IGlobalConstants.REPORT_BODY_MAN;
			String tzxf_report_body = (String)AppUtil.getAppAttr(IGlobalConstants.TZXF_REPORTBODY);
			if (StringUtils.isNotBlank(tzxf_report_body)) {
				report_body = tzxf_report_body;
			}
		}

		Row[] selRowsComp = receive_noticeds.getAllRow();
		Row[] selRowsMan = receive_mands.getAllRow();
		
		if (IGlobalConstants.REPORT_BODY_QY.equals(report_body)) {
			HashSet<String> pkOrgSet = new HashSet<String>();
			for (Row rowMan : selRowsMan) {
				String receive_org = (String)rowMan.getValue(receive_mands.nameToIndex(Receive_man.RECEIVE_ORG));
				if (StringUtils.isNotBlank(receive_org) && (!"~".equals(receive_org))){
					pkOrgSet.add(receive_org);
				}
			}
			for (Row rowComp : selRowsComp) {
				String notice_org = (String)rowComp.getValue(receive_noticeds.nameToIndex(Receive_notice.NOTICE_ORG));
				if (StringUtils.isNotBlank(notice_org) && (!"~".equals(notice_org))) {
					if (pkOrgSet.contains(notice_org)) {
						continue;
					} else {
						errorMessage.append(NoticeUtil.getOrgNameByPK(notice_org)).append(",");
					}
				}
			}
		} else if (IGlobalConstants.REPORT_BODY_VISORG.equals(report_body)) {
			HashSet<String> pkVisualOrgSet = new HashSet<String>();
			for (Row rowMan : selRowsMan) {
				String receive_visual_org = (String)rowMan.getValue(receive_mands.nameToIndex(Receive_man.RECEIVE_VISUAL_ORG));
				if (StringUtils.isNotBlank(receive_visual_org) && (!"~".equals(receive_visual_org))){
					pkVisualOrgSet.add(receive_visual_org);
				}
			}
			for (Row rowComp : selRowsComp) {
				String notice_visual_org = (String)rowComp.getValue(receive_noticeds.nameToIndex(Receive_notice.NOTICE_VISUAL_ORG));
				if (StringUtils.isNotBlank(notice_visual_org) && (!"~".equals(notice_visual_org))) {
					if (pkVisualOrgSet.contains(notice_visual_org)) {
						continue;
					} else {
						errorMessage.append(NoticeUtil.getOrgNameByPK(notice_visual_org)).append(",");
					}
				}
			}
		} 

		// �����ҵû����ϵ�ˣ����׳���ص�message��֪ͨ״̬������Ϊ"���·�".
		if (errorMessage != null && errorMessage.length() > 0) {
			throw new LfwRuntimeException("������ҵ����֯��û����ص���ϵ�ˣ�" + errorMessage.substring(0,errorMessage.length() - 1)
					+ "����֪ͨ����ֻ�Ǳ��沢û���·�!");
		} else { // ���� ����֪ͨ״̬��Ϊ"���·�"
			StringBuffer sql = new StringBuffer();
			sql.append(
					" update scapco_notice_manager t set t.notice_status = '2' ")
					.append(" where t.pk_notice = '").append(pk_notice)
					.append("'");
			try {
				ScapDAO.getBaseDAO().executeUpdate(sql.toString());
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}
		
		// ���ѹ������
		NoticeUtil.remindByNoticePK(pk_notice);
		
		this.getCurrentAppCtx().closeWinDialog();
		
		// ����listҳ��
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		Row savedRow = masterDs.getSelectedRow();
		paramMap.put(OPERATE_ROW, savedRow);
		CmdInvoker.invoke(new UifPlugoutCmd(getCurrentView().getId(),
				PLUGOUT_ID, paramMap));
  }
  public void onConfirmLookClick(  MouseEvent mouseEvent){
    Dataset masterDs = this.getMasterDs();
		Row rowm = masterDs.getSelectedRow();
		String pk_notice = rowm.getString(masterDs
				.nameToIndex(Notice_manager.PK_NOTICE));
		String notice_style = rowm.getString(masterDs
				.nameToIndex(Notice_manager.NOTICE_STYLE));
		if (pk_notice == null) {
			throw new LfwRuntimeException("��ѡ����Ӧ��֪ͨ!");
		}
		String notice_type = rowm.getString(masterDs
				.nameToIndex(Notice_manager.NOTICE_TYPE));
		String reportBody = "";
		if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
			// ���ݱ�������ȡ�ñ�������
			Work_report_type WorkReportType = (Work_report_type) ScapDAO
					.retrieveByPK(Work_report_type.class, notice_type);
			reportBody = WorkReportType.getReport_body();
		} else if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)) {
			// ������Ϣ֪ͨĬ�ϱ���������Ǹ���
			reportBody = IGlobalConstants.REPORT_BODY_MAN;
		} else if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
			// ��ͨ��Ϣ֪ͨĬ�ϱ���������Ǹ���
			reportBody = IGlobalConstants.REPORT_BODY_MAN;
			// ����й��ܽڵ�����Ļ��������óɹ��ܽڵ������õı������壨��Ҫ������Ϣ�Ĳ鿴����ҵ����֯��ʱ��Ҫ��д���շ�Χ�ӱ���
			String tzxf_report_body = (String)AppUtil.getAppAttr(IGlobalConstants.TZXF_REPORTBODY);
			if (StringUtils.isNotBlank(tzxf_report_body)) {
				reportBody = tzxf_report_body;
			}
		}
		// ���ݱ������������Ӧ����
		if (reportBody != null && ("1".equals(reportBody))) {//���������ҵ
			String pkUser = CntUtil.getCntUserPk();
			BaseDAO baseDAO = new BaseDAO();
			// �����Ҫ�޸�״̬�Ľ�������Ϣ
			String conditionMan = " pk_notice = '" + pk_notice
					+ "' and receive_man = '" + pkUser + "'";
			try {
				List<Receive_man> receive_manList = (List<Receive_man>) baseDAO
						.retrieveByClause(Receive_man.class, conditionMan);
				if (receive_manList != null) {
					for (Receive_man vo : receive_manList) {
						vo.setReceive_status("2");
						vo.setReceive_time(new UFDate());
						vo.setReceive_man(CntUtil.getCntUserPk());
						ScapDAO.getBaseDAO().updateVO(vo);// ���²���
					}
				}
			} catch (DAOException e) {
				ScapLogger.error("���²������ִ��󣡴����쳣��" + e.getMessage());
			}
			// ȷ����ҵ�Ƿ��Ѳ鿴
			// �����ҵ��Ϣ
			List<Receive_notice> receive_noticeList = new ArrayList<Receive_notice>();
			String pkOrg = CntUtil.getCntOrgPk();
			String conditionComp = " pk_notice = '" + pk_notice
					+ "' and notice_org = '" + pkOrg + "'";
			Boolean isCompReaded = true;
			try {
				receive_noticeList = (List<Receive_notice>) baseDAO
						.retrieveByClause(Receive_notice.class, conditionComp);
				if (receive_noticeList != null) {
					for (Receive_notice vo : receive_noticeList) {
						if ("1".equals(vo.getReceive_status())) { // ��ҵδ�鿴
							isCompReaded = false;
						}
					}
				}
			} catch (DAOException e) {
				ScapLogger.error("���Ҳ������ִ��󣡴����쳣��" + e.getMessage());
			}
			// �����ҵ�Ѳ鿴��ֱ�ӷ���
			if (isCompReaded.equals(true)) {
				return;
			}
			// �����ҵδ�鿴
			// ȷ��ͬһ����ҵ���������Ƿ�ȫ���鿴������ǵĻ�����Ҫ�޸���ҵ�鿴״̬Ϊ�Ѳ鿴;�����Ļ��������û��Ƿ�ȷ����ҵ�Ѳ鿴
			Boolean isAllManReaded = true;
			// ���ͬһ����ҵ�Ŀ��Բ鿴��֪ͨ����
			String conditionManAll = " pk_notice = '" + pk_notice
					+ "' and receive_org = '" + pkOrg + "'";
			try {
				List<Receive_man> receive_manAllList = (List<Receive_man>) baseDAO
						.retrieveByClause(Receive_man.class, conditionManAll);
				if (receive_manAllList != null) {
					for (Receive_man vo : receive_manAllList) {
						if ("1".equals(vo.getStatus())) {
							isAllManReaded = false;
							break;
						}
					}
				}
			} catch (DAOException e) {
				ScapLogger.error("���Ҳ������ִ��󣡴����쳣��" + e.getMessage());
			}
			if (isAllManReaded.equals(false)) {
				AppInteractionUtil.showConfirmDialog("ȷ�϶Ի���", "�Ƿ�ȷ����ҵ�Ѳ鿴��");
				if (!AppInteractionUtil.getConfirmDialogResult().booleanValue())
					return;
			}
			// ���û�ȷ����ҵ�Ѳ鿴���޸���ҵ�����ҵ�鿴״̬
			try {
				if (receive_noticeList != null) {
					for (Receive_notice vo : receive_noticeList) {
						vo.setReceive_status("2");// �Ѳ鿴
						vo.setReceive_time(new UFDate());
						vo.setReceive_man(CntUtil.getCntUserPk());
						ScapDAO.getBaseDAO().updateVO(vo);// ���²���
					}
				}
			} catch (DAOException e) {
				ScapLogger.error("���²������ִ��󣡴����쳣��" + e.getMessage());
			}
		}
		// ���ݱ������������Ӧ����
		if (reportBody != null && ("2".equals(reportBody))) {//���������֯
			String pkUser = CntUtil.getCntUserPk();
			BaseDAO baseDAO = new BaseDAO();
			// �����Ҫ�޸�״̬�Ľ�������Ϣ
			String conditionMan = " pk_notice = '" + pk_notice
					+ "' and receive_man = '" + pkUser + "'";
			try {
				List<Receive_man> receive_manList = (List<Receive_man>) baseDAO
						.retrieveByClause(Receive_man.class, conditionMan);
				if (receive_manList != null) {
					for (Receive_man vo : receive_manList) {
						vo.setReceive_status("2");
						vo.setReceive_time(new UFDate());
						vo.setReceive_man(CntUtil.getCntUserPk());
						ScapDAO.getBaseDAO().updateVO(vo);// ���²���
					}
				}
			} catch (DAOException e) {
				ScapLogger.error("���²������ִ��󣡴����쳣��" + e.getMessage());
			}
			// ȷ����֯�Ƿ��Ѳ鿴
			// �����֯��Ϣ
			List<Receive_notice> receive_noticeList = new ArrayList<Receive_notice>();
			String pkOrg = CntUtil.getCntOrgPk();
			List<String> result = NoticeUtil.getAllPKVisualorgByPkUser(pkUser);
			String[] pkVisualOrgArray = (String[])result.toArray(new String[result.size()]);
			String conditionComp = " pk_notice = '" + pk_notice + "' "
					+ " and (" + NoticeUtil.getSqlStrByList(result,1000,"notice_visual_org") + ")";
			Boolean isVisCompReaded = true;
			String beingPkVisualOrg = "";
			try {
				receive_noticeList = (List<Receive_notice>) baseDAO
						.retrieveByClause(Receive_notice.class, conditionComp);
				if (receive_noticeList != null) {
					for (Receive_notice vo : receive_noticeList) {
						beingPkVisualOrg = vo.getNotice_visual_org();
						if ("1".equals(vo.getReceive_status())) { // ��ҵδ�鿴
							isVisCompReaded = false;
						}
					}
				}
			} catch (DAOException e) {
				ScapLogger.error("���Ҳ������ִ��󣡴����쳣��" + e.getMessage());
			}
			// �����֯�Ѳ鿴��ֱ�ӷ���
			if (isVisCompReaded.equals(true)) {
				return;
			}
			// �����֯δ�鿴
			// ȷ��ͬһ����֯���������Ƿ�ȫ���鿴������ǵĻ�����Ҫ�޸���֯�鿴״̬Ϊ�Ѳ鿴;�����Ļ��������û��Ƿ�ȷ����֯�Ѳ鿴
			Boolean isAllManReaded = true;
			// ���ͬһ����֯�Ŀ��Բ鿴��֪ͨ����
			String conditionManAll = " pk_notice = '" + pk_notice
					+ "' and RECEIVE_VISUAL_ORG = '" + beingPkVisualOrg + "'";
			try {
				List<Receive_man> receive_manAllList = (List<Receive_man>) baseDAO
						.retrieveByClause(Receive_man.class, conditionManAll);
				if (receive_manAllList != null) {
					for (Receive_man vo : receive_manAllList) {
						if ("1".equals(vo.getStatus())) {
							isAllManReaded = false;
							break;
						}
					}
				}
			} catch (DAOException e) {
				ScapLogger.error("���Ҳ������ִ��󣡴����쳣��" + e.getMessage());
			}
			if (isAllManReaded.equals(false)) {
				AppInteractionUtil.showConfirmDialog("ȷ�϶Ի���", "�Ƿ�ȷ����֯�Ѳ鿴��");
				if (!AppInteractionUtil.getConfirmDialogResult().booleanValue())
					return;
			}
			// ���û�ȷ����֯�Ѳ鿴���޸Ľ��շ�Χ�����֯�鿴״̬
			try {
				if (receive_noticeList != null) {
					for (Receive_notice vo : receive_noticeList) {
						vo.setReceive_status("2");// �Ѳ鿴��
						vo.setReceive_time(new UFDate());
						vo.setReceive_man(CntUtil.getCntUserPk());
						ScapDAO.getBaseDAO().updateVO(vo);// ���²���
					}
				}
			} catch (DAOException e) {
				ScapLogger.error("���²������ִ��󣡴����쳣��" + e.getMessage());
			}
		}
		// ���ݱ������������Ӧ���� ; 
		// �����������Ǹ���ʱ���鿴״ֻ̬��Ҫ���½������ӱ����շ�Χ�ӱ��и��û���������֯����ҵ�Ĳ鿴״̬����Ҫ����
		if (reportBody != null && ("3".equals(reportBody))) {//������Ǹ���
			String pkUser = CntUtil.getCntUserPk();
			BaseDAO baseDAO = new BaseDAO();
			// �����Ҫ�޸�״̬�Ľ�������Ϣ
			String conditionMan = " pk_notice = '" + pk_notice
					+ "' and receive_man = '" + pkUser + "'";
			try {
				List<Receive_man> receive_manList = (List<Receive_man>) baseDAO
						.retrieveByClause(Receive_man.class, conditionMan);
				if (receive_manList != null) {
					for (Receive_man vo : receive_manList) {
						vo.setReceive_status("2");
						vo.setReceive_time(new UFDate());
						vo.setReceive_man(CntUtil.getCntUserPk());
						ScapDAO.getBaseDAO().updateVO(vo);// ���²���
					}
				}
			} catch (DAOException e) {
				ScapLogger.error("���²������ִ��󣡴����쳣��" + e.getMessage());
			}
		}
		// ȷ�ϲ鿴�ɹ���ʾ��Ϣ
		AppInteractionUtil.showShortMessage("ȷ�ϲ鿴�ɹ�");
		this.getCurrentAppCtx().closeWinDialog();
		// ����listҳ��
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		Row savedRow = masterDs.getSelectedRow();
		paramMap.put(OPERATE_ROW, savedRow);
		CmdInvoker.invoke(new UifPlugoutCmd(getCurrentView().getId(),
				PLUGOUT_ID, paramMap));
  }
  public void onReceiveManGridAddClick(  MouseEvent mouseEvent){
    // �ж��Ƿ���ѡ���û���
		if (!canSelectUser()) {
			throw new LfwRuntimeException("û�н�����ҵ�����ܽ��н����û�ѡ��");
		}
	    String business_type = (String) AppUtil.getAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE);
	    String report_type = (String) AppUtil.getAppAttr(IGlobalConstants.APPATTR_REPORT_TYPE);
	    String data_type = (String) AppUtil.getAppAttr(IGlobalConstants.APPATTR_DATA_TYPE);
	    String message_type = (String) AppUtil.getAppAttr(IGlobalConstants.APPATTR_MESSAGE_TYPE);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put(IGlobalConstants.PARAMMAP_KEY_BUSINESS_TYPE, business_type);
		paramMap.put(IGlobalConstants.PARAMMAP_KEY_REPORT_TYPE, report_type);
		paramMap.put(IGlobalConstants.PARAMMAP_KEY_DATA_TYPE, data_type);
		paramMap.put(IGlobalConstants.PARAMMAP_KEY_MESSAGE_TYPE, message_type);
		List<notice_contact_info> list = NoticeUtil.getContactInfoByParam(paramMap);
		String pkOrglist = "";
		String pkVisualOrglist = "";
		if (list != null && list.size() > 0) {
			for (notice_contact_info vo : list) {
				if (IGlobalConstants.CONTENT_TYPE_RECEIVER.equals(vo.getContacts_type())) {
					if (StringUtils.isNotBlank(vo.getPk_org()) && !"~".equals(vo.getPk_org())) {
						pkOrglist = pkOrglist + "'" + vo.getPk_org() + "'" + ",";
					}
					if (StringUtils.isNotBlank(vo.getPk_visualorg()) && !"~".equals(vo.getPk_visualorg())) {
						pkVisualOrglist = pkVisualOrglist + "'" + vo.getPk_visualorg() + "'" + ",";
					}
				}
			}
			if (StringUtils.isNotBlank(pkOrglist)) {
				pkOrglist = pkOrglist.substring(0, pkOrglist.length()-1);
			}
			if (StringUtils.isNotBlank(pkVisualOrglist)) {
				pkVisualOrglist = pkVisualOrglist.substring(0, pkVisualOrglist.length()-1);
			}
		}
		String pk_org_all = (String)AppUtil.getAppAttr("pk_org_all");
		String pk_visual_org_all = (String)AppUtil.getAppAttr("pk_visual_org_all");
		if (StringUtils.isNotBlank(pk_org_all)) {
			pk_org_all = pk_org_all + "," + pkOrglist;
		} else {
			pk_org_all = pkOrglist;
		}
		if (StringUtils.isNotBlank(pk_visual_org_all)) {
			pk_visual_org_all = pk_visual_org_all + pkVisualOrglist;
		} else {
			pk_visual_org_all = pkVisualOrglist;
		}
		AppUtil.addAppAttr("pk_org_all", pk_org_all);
		AppUtil.addAppAttr("pk_visual_org_all",pk_visual_org_all);
		String tzxf_notice_style = (String)AppUtil.getAppAttr(IGlobalConstants.TZXF_NOTICE_STYLE);
		if (StringUtils.isNotBlank(tzxf_notice_style) && !(NoticeUtil.ifExitedMessageType(message_type))) {
			OpenProperties props = new OpenProperties(
					"selectUserByVisualOrg", "��Ϣ������ѡ��", "800",
					"600");
			props.setButtonZone(false);
			this.getCurrentAppCtx().getCurrentWindowContext().popView(props);
		} else {
			OpenProperties props = new OpenProperties(
					"multiUserSelect", "��ϵ��ѡ��", "800",
					"600");
			props.setButtonZone(false);
			this.getCurrentAppCtx().getCurrentWindowContext().popView(props);
		}
  }
  public void onReceiveManGridEditClick(  MouseEvent mouseEvent){
    GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		ds.setEnabled(true);
  }
  public void onReceiveManGridDeleteClick(  MouseEvent mouseEvent){
    GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		CmdInvoker.invoke(new UifLineDelCmd(dsId));
  }
  public void onReceiveCompGridAddClick(  MouseEvent mouseEvent){
    // ���ж�������֪ͨ���ͣ���ҵ�����ͣ��������ͣ����������ͻ���Ϣ���͵��ֶ��Ƿ��Ѿ���д��
		// ֻ���Ѿ���д�� �����ܽ�����ҵ��ѡ�񡣷�����ѡ�������ҵ��
		if (!canSelectComp()) {
			throw new LfwRuntimeException("��ҵ�����ͣ��������ͣ����������ͻ���Ϣ����û��ѡ�����Բ���ѡ�������ҵ��");
		}
		OpenProperties props = new OpenProperties(
				"visualOrganization", "ѡ����ҵ����֯", "300",
				"600");
		props.setButtonZone(false);
		this.getCurrentAppCtx().getCurrentWindowContext().popView(props);
  }
  public void onReceiveCompGridEditClick(  MouseEvent mouseEvent){
    GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
		ds.setEnabled(true);
  }
  public void onReceiveCompGridDeleteClick(  MouseEvent mouseEvent){
    GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		LfwView view = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset receive_notice = view.getViewModels().getDataset(dsId);
		Row selectedRow = receive_notice.getSelectedRow();
		if (selectedRow == null) {
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pub", "UifLineDelCmd-000000")/*��ѡ��Ҫɾ������!*/);
		}
		// ������ҵ�ӱ�ɾ��ʱ����Ҫ�ȼ�¼��Ҫɾ������ҵ��pk_org��Ȼ��ɾ����Ӧ�������ӱ�Ľ�����
		String notice_org = selectedRow.getValue(receive_notice
				.nameToIndex(Receive_notice.NOTICE_ORG)) == null ? ""
				: selectedRow.getValue(
						receive_notice.nameToIndex(Receive_notice.NOTICE_ORG))
						.toString();
		String notice_visual_org = selectedRow.getValue(receive_notice
				.nameToIndex(Receive_notice.NOTICE_VISUAL_ORG)) == null ? ""
				: selectedRow.getValue(
						receive_notice.nameToIndex(Receive_notice.NOTICE_VISUAL_ORG))
						.toString();
		// ɾ��������ҵ�ӱ������
		CmdInvoker.invoke(new UifLineDelCmd(dsId));

		Dataset receive_mands = view.getViewModels().getDataset("receive_man");
		Row[] rows_man = receive_mands.getAllRow();
		for (int i = 0; i < rows_man.length; i++) {
			String receive_org = (String) rows_man[i].getValue(receive_mands
					.nameToIndex(Receive_man.RECEIVE_ORG));
			if (receive_org != null && notice_org.equals(receive_org)) {
				// ��remove����Ӧ�Ľ���������
				int rowIndex = receive_mands.getRowIndex(rows_man[i]);
				receive_mands.setRowSelectIndex(rowIndex);
				CmdInvoker.invoke(new UifLineDelCmd(receive_mands.getId()));
			}
			String receive_visual_org = (String) rows_man[i].getValue(receive_mands
					.nameToIndex(Receive_man.RECEIVE_VISUAL_ORG));
			if (receive_visual_org != null && notice_visual_org.equals(receive_visual_org)) {
				// ��remove����Ӧ�Ľ���������
				int rowIndex = receive_mands.getRowIndex(rows_man[i]);
//				receive_mands.setFocusIndex(rowIndex);
				receive_mands.setRowSelectIndex(rowIndex);
				CmdInvoker.invoke(new UifLineDelCmd(receive_mands.getId()));
//				receive_mands.removeRow(rowIndex);
			}
		}
  }
  private Boolean canSelectComp(){
    return addSessionAppAttr();
  }
  private Boolean addSessionAppAttr(){
    // ���session�����õ�����
		AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_BODY, null);
		AppUtil.addAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE, null);
		AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_TYPE, null);
		AppUtil.addAppAttr(IGlobalConstants.APPATTR_DATA_TYPE, null);
		AppUtil.addAppAttr(IGlobalConstants.APPATTR_MESSAGE_TYPE, null);
		
	  	Boolean result = false;
		Dataset masterDs = this.getMasterDs();
		Row selectedRow = masterDs.getSelectedRow();
		// ���֪ͨ����(������) 1=�֪ͨ 2=��Ϣ֪ͨ
		String notice_style = selectedRow.getValue(masterDs
				.nameToIndex(Notice_manager.NOTICE_STYLE)) == null ? ""
				: selectedRow.getValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_STYLE))
						.toString();
		// ��ñ���ҵ������
		String besinss_type = selectedRow.getValue(masterDs
				.nameToIndex(Notice_manager.BUSINESS_TYPE)) == null ? ""
				: selectedRow.getValue(
						masterDs.nameToIndex(Notice_manager.BUSINESS_TYPE))
						.toString();
		// ��ñ���ʵ������
		String notice_type = selectedRow.getValue(masterDs
				.nameToIndex(Notice_manager.NOTICE_TYPE)) == null ? ""
				: selectedRow.getValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_TYPE))
						.toString();
		// �����������
		String data_type = selectedRow.getValue(masterDs
				.nameToIndex(Notice_manager.DATA_TYPE)) == null ? ""
				: selectedRow.getValue(
						masterDs.nameToIndex(Notice_manager.DATA_TYPE))
						.toString();
		// �����Ϣ����
		String message_type = selectedRow.getValue(masterDs
				.nameToIndex(Notice_manager.DEF4)) == null ? ""
				: selectedRow.getValue(
						masterDs.nameToIndex(Notice_manager.DEF4))
						.toString();
		// ������ѡ������Ҫ�õ���Щ����
		AppUtil.addAppAttr("notice_style", notice_style);
		// �֪ͨʱ
		if (notice_style.equals("1")) {
			if (!besinss_type.isEmpty()) {
				AppUtil.addAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE, besinss_type);
			}
			if (!notice_type.isEmpty()) {
				AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_TYPE, notice_type);
			}
			if ((!besinss_type.isEmpty()) && (!notice_type.isEmpty())) {
				result = true;
				String report_body = NoticeUtil.getReportBodyByNoticeType(notice_type);
				AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_BODY, report_body);
				AppUtil.addAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE, besinss_type);
				AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_TYPE, notice_type);
			}
			// ������Ϣ֪ͨʱ
		} else if (notice_style.equals("2")) {
			if (!data_type.isEmpty()) {
				result = true;
				// ������Ϣ֪ͨĬ��Ϊ���ˣ����������ں�ߵĴ���
				AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_BODY, IGlobalConstants.REPORT_BODY_MAN);
				AppUtil.addAppAttr(IGlobalConstants.APPATTR_DATA_TYPE, data_type);
			}
			// ��ͨ��Ϣ֪ͨʱ
		} else if (notice_style.equals("3")) {
			if (!message_type.isEmpty()) {
				result = true;
				// ��ͨ��Ϣ֪ͨĬ��Ϊ���ˣ����������ں�ߵĴ���
				AppUtil.addAppAttr(IGlobalConstants.APPATTR_MESSAGE_TYPE, message_type);
				AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_BODY, IGlobalConstants.REPORT_BODY_MAN);
				// ����й��ܽڵ�����Ļ��������óɹ��ܽڵ������õı������壨��Ҫ������Ϣ�Ĳ鿴����ҵ����֯��ʱ��Ҫ��д���շ�Χ�ӱ���
				String tzxf_report_body = (String)AppUtil.getAppAttr(IGlobalConstants.TZXF_REPORTBODY);
				if (StringUtils.isNotBlank(tzxf_report_body)) {
					AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_BODY, tzxf_report_body);
				}
			}	
		}
		
		return result;
  }
  private Boolean canSelectUser(){
    Boolean result = false;
		if (!addSessionAppAttr()) {
			throw new LfwRuntimeException("û��ѡ��(ҵ������,��������),��(��������),���ܽ�����Աѡ��");
		}
		// �鿴������ҵ�ӱ��Ƿ������ݣ������ݵĻ�����ѡ���û�
		LfwView view = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset receive_noticeds = view.getViewModels().getDataset(
				"receive_notice");
		Row[] selRows = receive_noticeds.getAllRow();
		String reportBody = (String)AppUtil.getAppAttr(IGlobalConstants.APPATTR_REPORT_BODY);
		String message_type = (String)AppUtil.getAppAttr(IGlobalConstants.APPATTR_MESSAGE_TYPE);
		if (StringUtils.isBlank(message_type)) {
			if (IGlobalConstants.REPORT_BODY_QY.equals(reportBody) || IGlobalConstants.REPORT_BODY_VISORG.equals(reportBody)) {
				if (selRows != null && selRows.length == 0) {
					result = false;
					return result;
				}
			}	
		} else if (StringUtils.isNotBlank(message_type) && !IGlobalConstants.STANDARD_MESSAGE_TYPE_CODE.equals(message_type)) {
			if (IGlobalConstants.REPORT_BODY_QY.equals(reportBody) || IGlobalConstants.REPORT_BODY_VISORG.equals(reportBody)) {
				if (selRows != null && selRows.length == 0) {
					result = false;
					return result;
				}
			} else if (IGlobalConstants.REPORT_BODY_MAN.equals(reportBody)) {
				result = true;
			}	
		} else if (StringUtils.isNotBlank(message_type) && IGlobalConstants.STANDARD_MESSAGE_TYPE_CODE.equals(message_type)) {
			result = true;
		}


		// ��ѡ�����ҵ������Խ����û�ѡ��
		if (selRows != null && selRows.length > 0) {
			result = true;
			StringBuffer pk_org_all = new StringBuffer();
			StringBuffer pk_visual_org_all = new StringBuffer();
			for (int i = 0; i < selRows.length; i++) {
				String pk_org = (String) selRows[i].getValue(receive_noticeds
						.nameToIndex(Receive_notice.NOTICE_ORG));
				if ((pk_org != null) && (!"~".equals(pk_org)) ) {
					pk_org_all.append("'").append(pk_org).append("',");
				}
				String notice_visual_org = (String) selRows[i].getValue(receive_noticeds
						.nameToIndex(Receive_notice.NOTICE_VISUAL_ORG));
				if ((notice_visual_org != null) && (!"~".equals(notice_visual_org)) ) {
					pk_visual_org_all.append("'").append(notice_visual_org).append("',");
				}
			}
			if (pk_org_all.length() > 0) {
				// ���շ�Χ�еĽ�����ҵ��pk_org���û�ѡ���������֯���˵�ʱ����Ҫ�õ�
				AppUtil.addAppAttr("pk_org_all",
						pk_org_all.substring(0, pk_org_all.length() - 1));
			} else {
				AppUtil.addAppAttr("pk_org_all",
						"");
			}
			if (pk_visual_org_all.length() > 0) {
				// ���շ�Χ�еĽ�����֯��pk_org���û�ѡ���������֯���˵�ʱ����Ҫ�õ�
				AppUtil.addAppAttr("pk_visual_org_all",
						pk_visual_org_all.substring(0, pk_visual_org_all.length() - 1));
			} else {
				AppUtil.addAppAttr("pk_visual_org_all",
						"");
			}
		}
		return result;
  }
  public void doSelectComp(  Map keys){
    AppUtil.addAppAttr(IGlobalConstants.NOTICE_ERROR_MESSAGE, null);
	  		LfwView view = AppLifeCycleContext.current().getWindowContext()
					.getViewContext("main").getView();
			LfwView view_comp = AppLifeCycleContext.current().getWindowContext()
					.getViewContext("visualOrganization").getView();
			Dataset masterDs = this.getMasterDs();
			Row selectedRow = masterDs.getSelectedRow();
			Object pk_notice = selectedRow.getValue(masterDs.nameToIndex(masterDs
					.getPrimaryKeyField()));
			// ���֪ͨ����(������) 1=�֪ͨ 2=��Ϣ֪ͨ
			String notice_style = selectedRow.getValue(masterDs
					.nameToIndex(Notice_manager.NOTICE_STYLE)) == null ? ""
					: selectedRow.getValue(
							masterDs.nameToIndex(Notice_manager.NOTICE_STYLE))
							.toString();
			// ��ñ���ҵ������
			String besinss_type = selectedRow.getValue(masterDs
					.nameToIndex(Notice_manager.BUSINESS_TYPE)) == null ? ""
					: selectedRow.getValue(
							masterDs.nameToIndex(Notice_manager.BUSINESS_TYPE))
							.toString();
			// ��ñ���ʵ������
			String notice_type = selectedRow.getValue(masterDs
					.nameToIndex(Notice_manager.NOTICE_TYPE)) == null ? ""
					: selectedRow.getValue(
							masterDs.nameToIndex(Notice_manager.NOTICE_TYPE))
							.toString();
			// �����������
			String data_type = selectedRow.getValue(masterDs
					.nameToIndex(Notice_manager.DATA_TYPE)) == null ? ""
					: selectedRow.getValue(
							masterDs.nameToIndex(Notice_manager.DATA_TYPE))
							.toString();
			// �����Ϣ����
			String message_type = selectedRow.getValue(masterDs
					.nameToIndex(Notice_manager.DEF4)) == null ? ""
					: selectedRow.getValue(
							masterDs.nameToIndex(Notice_manager.DEF4))
							.toString();
			// ȡ�������ӱ��dataset
			Dataset receive_noticeds = view.getViewModels().getDataset(
					"receive_notice");
			receive_noticeds.setOrderByPart(" order by NOTICE_ORG, NOTICE_VISUAL_ORG asc ");
			Dataset receive_mands = view.getViewModels().getDataset("receive_man");
			receive_mands.setOrderByPart(" order by RECEIVE_ORG,RECEIVE_VISUAL_ORG asc ");
			
			// ���ݱ�������ȡ�ñ�������
			String reportBody = "";
			// ������֪ͨ���Ǹ����ݱ�������ȡ�ñ�������
			if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
				Work_report_type WorkReportType = (Work_report_type) ScapDAO
						.retrieveByPK(Work_report_type.class, notice_type);
				reportBody = WorkReportType.getReport_body();
			} else if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)) {
				// �����������Ϣ֪ͨ����Ĭ�ϱ��������Ǹ���
				reportBody = IGlobalConstants.REPORT_BODY_MAN;
			} else if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
				// �������ͨ��Ϣ֪ͨ����Ĭ�ϱ��������Ǹ���
				reportBody = IGlobalConstants.REPORT_BODY_MAN;
				// ����й��ܽڵ�����Ļ��������óɹ��ܽڵ������õı������壨��Ҫ������Ϣ�Ĳ鿴����ҵ����֯��ʱ��Ҫ��д���շ�Χ�ӱ���
				String tzxf_report_body = (String)AppUtil.getAppAttr(IGlobalConstants.TZXF_REPORTBODY);
				if (StringUtils.isNotBlank(tzxf_report_body)) {
					reportBody = tzxf_report_body;
				}
			}
			// ��ϵ�˼�������map
			Map<String, String> paramMap = new HashMap<String, String>();
	    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_REPORTBODY, reportBody);
			// ���ݱ������������Ӧ����
			if (reportBody != null && (IGlobalConstants.REPORT_BODY_QY.equals(reportBody) || IGlobalConstants.REPORT_BODY_MAN.equals(reportBody))) {//���������ҵ�����

				Dataset cp_orgsDs = view_comp.getViewModels().getDataset(
						"cp_orgsDs");
				Row[] selRows = cp_orgsDs.getAllSelectedRows();
				String[] pk_orgArray = new String[selRows.length];
				// ѭ��������ҵlist�����ݽ�����ҵ��
				for (int i = 0; i < selRows.length; i++) {
					String pk_org = (String) selRows[i].getValue(cp_orgsDs
							.nameToIndex(CpOrgVO.PK_ORG));
					pk_orgArray[i] = pk_org;
				}
				// �ĳ��ӱ�ʵ�ַ�ʽ��������ֻ��Ҫ������Ľ�����ҵ�ֶκ�������grid����Ӧ�ֶλ�д�ϼ��ɡ�
				Map<String, String> mapExistedComp = isCompExisted();
				// ��д ������ҵ�ӱ��ds
				for (int i = 0; i < pk_orgArray.length; i++) {
					// ȥ�ش���
					if (mapExistedComp.containsKey(pk_orgArray[i])) {
						continue;
					}
					Row row = receive_noticeds.getEmptyRow();
					String pk_org = pk_orgArray[i];
					String pk_primarykey = generatePk();
					// ����
					row.setValue(
							receive_noticeds.nameToIndex(Receive_notice.PK_NOTICE_ORG),
							pk_primarykey);

					// ֪ͨ���� ����pk
					row.setValue(
							receive_noticeds.nameToIndex(Receive_notice.PK_NOTICE),
							pk_notice.toString());

					// ������ҵ
					row.setValue(
							receive_noticeds.nameToIndex(Receive_notice.NOTICE_ORG),
							pk_org);

					if (IGlobalConstants.REPORT_BODY_QY.equals(reportBody) ) {
						// �鿴״̬��δ�鿴��
						row.setValue(
								receive_noticeds.nameToIndex(Receive_notice.RECEIVE_STATUS),
								"1");
						// �ύ״̬��δ�ύ��
						row.setValue(
								receive_noticeds.nameToIndex(Receive_notice.REPORT_STATUS),
								"1");
					}

					receive_noticeds.addRow(row);
				}
				List<notice_contact_info> receiceManList = new ArrayList<notice_contact_info>();
				// �֪ͨʱ����ҵ�����ͺͱ�������ȥѰ����ϵ��
				if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
			    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_BUSINESS_TYPE, besinss_type);
			    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_REPORT_TYPE, notice_type);
					receiceManList = NoticeUtil.getContactsList(paramMap,
							pk_orgArray,Boolean.FALSE);
					// ������Ϣ֪ͨʱ������������ȥѰ����ϵ��
				} else if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)) {
			    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_DATA_TYPE, data_type);
					receiceManList = NoticeUtil.getContactsList(paramMap, pk_orgArray,Boolean.FALSE);
					// ��ͨ��Ϣ֪ͨʱ��ʹ����ϵ�˽ڵ㣬����Ҫ���������ӱ�����д����
				} else if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
					paramMap.put(IGlobalConstants.PARAMMAP_KEY_MESSAGE_TYPE, message_type);
					//��ϵ���д�����Ӧ��Ϣ���ͣ��������Ӧ���ˣ����򲻴��ڵĻ����ô���
					if (NoticeUtil.ifExitedMessageType(message_type)) {
						receiceManList = NoticeUtil.getContactsList(paramMap, pk_orgArray,Boolean.FALSE);
					}
				}

				Receive_man receiceManArray[] = new Receive_man[receiceManList.size()];
				Map<String, List<String>> mapExistedUser = getUserExistedMap_r();
				for (int i = 0; i < receiceManArray.length; i++) {
					// �����ж�����grid��治���ڣ�
					String pk_user = receiceManList.get(i).getPk_user();
					String pk_org = receiceManList.get(i).getPk_org();
					if (mapExistedUser.containsKey(pk_org)) {
						if (mapExistedUser.get(pk_org).contains(pk_user)) {
							continue;// ����grid���Ѿ����ڸ���ҵ�ĸ��û�
						}
					}
					// �ж���ϵ�������Ƿ��ǽ�����,���ܷŷ�����
					String contacts_type = receiceManList.get(i).getContacts_type();
					if (contacts_type != null && "2".equals(contacts_type)) {
						continue;
					}
					Row row = receive_mands.getEmptyRow();
					String pk_primarykey = generatePk();
					// ����
					row.setValue(receive_mands.nameToIndex(Receive_man.PK_RECEIVE_MAN),
							pk_primarykey);
					// ֪ͨ����
					row.setValue(receive_mands.nameToIndex(Receive_man.PK_NOTICE),
							pk_notice.toString());
					// ������
					row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_MAN),
							receiceManList.get(i).getPk_user());

					// ���յ�λ
					row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_ORG),
							receiceManList.get(i).getPk_org());

					 // �Ա�
					 row.setValue(receive_mands.nameToIndex(Receive_man.SEX),
					 NoticeUtil.getSexByPkUser(pk_user));

					// �绰����
					row.setValue(receive_mands.nameToIndex(Receive_man.TELEPHONE),
							receiceManList.get(i).getPhone_no());

					// Email
					row.setValue(receive_mands.nameToIndex(Receive_man.EMAIL),
							receiceManList.get(i).getEmail());

					// �鿴״̬(δ�鿴)
					row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_STATUS),
							"1");
					// �ύ״̬��δ�ύ��
					row.setValue(
							receive_mands.nameToIndex(Receive_man.REPORT_STATUS),
							"1");
					// ��ע
					row.setValue(receive_mands.nameToIndex(Receive_man.DEF1),
							receiceManList.get(i).getRemark());
					// �鿴ʱ��
					// row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_TIME),
					// new UFDate());

					receive_mands.addRow(row);
				}
			}
			// ���ݱ������������Ӧ����
			if (reportBody != null && (IGlobalConstants.REPORT_BODY_VISORG.equals(reportBody) || IGlobalConstants.REPORT_BODY_MAN.equals(reportBody))) {//���������֯�����

				Dataset visualOrganizationDs = view_comp.getViewModels().getDataset(
						"visualOrganizationDs");
				Row[] selRows = visualOrganizationDs.getAllSelectedRows();
				String[] pk_visualOrgArray = new String[selRows.length];
				// ѭ��������֯list�����ݽ�����֯
				for (int i = 0; i < selRows.length; i++) {
					String pk_visualorg = (String) selRows[i].getValue(visualOrganizationDs
							.nameToIndex(visualOrganization.PK_VISUALORG));
					pk_visualOrgArray[i] = pk_visualorg;
				}
				// �ĳ��ӱ�ʵ�ַ�ʽ��������ֻ��Ҫ��������grid����Ӧ�ֶλ�д�ϼ��ɡ�
				Map<String, String> mapExistedComp = isVisualCompExisted();
				// ��д ������ҵ�ӱ��ds
				for (int i = 0; i < pk_visualOrgArray.length; i++) {
					// ȥ�ش���
					if (mapExistedComp.containsKey(pk_visualOrgArray[i])) {
						continue;
					}
					Row row = receive_noticeds.getEmptyRow();
					String pk_visualorg = pk_visualOrgArray[i];
					String pk_primarykey = generatePk();
					// ����
					row.setValue(
							receive_noticeds.nameToIndex(Receive_notice.PK_NOTICE_ORG),
							pk_primarykey);

					// ֪ͨ���� ����pk
					row.setValue(
							receive_noticeds.nameToIndex(Receive_notice.PK_NOTICE),
							pk_notice.toString());

					// ������ҵ
					row.setValue(
							receive_noticeds.nameToIndex(Receive_notice.NOTICE_VISUAL_ORG),
							pk_visualorg);

					if (IGlobalConstants.REPORT_BODY_VISORG.equals(reportBody)) {
						// �鿴״̬��δ�鿴��
						row.setValue(
								receive_noticeds.nameToIndex(Receive_notice.RECEIVE_STATUS),
								"1");
						// �ύ״̬��δ�ύ��
						row.setValue(
								receive_noticeds.nameToIndex(Receive_notice.REPORT_STATUS),
								"1");
					}

					receive_noticeds.addRow(row);
				}
				List<notice_contact_info> receiceManList = new ArrayList<notice_contact_info>();
				// �֪ͨʱ����ҵ�����ͺͱ�������ȥѰ����ϵ��
				if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
			    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_BUSINESS_TYPE, besinss_type);
			    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_REPORT_TYPE, notice_type);
					receiceManList = NoticeUtil.getContactsList(paramMap,
							pk_visualOrgArray,Boolean.FALSE);
					// ������Ϣ֪ͨʱ������������ȥѰ����ϵ��
				} else if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)) {
			    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_DATA_TYPE, data_type);
					receiceManList = NoticeUtil.getContactsList(paramMap, pk_visualOrgArray,Boolean.FALSE);
					// ��ͨ��Ϣ֪ͨʱ
				} else if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
					paramMap.put(IGlobalConstants.PARAMMAP_KEY_MESSAGE_TYPE, message_type);
					//��ϵ���д�����Ӧ��Ϣ���ͣ��������Ӧ���ˣ����򲻴��ڵĻ����ô���
					if (NoticeUtil.ifExitedMessageType(message_type)) {
						receiceManList = NoticeUtil.getContactsList(paramMap, pk_visualOrgArray,Boolean.FALSE);
					}
				}
				Receive_man receiceManArray[] = new Receive_man[receiceManList.size()];
				Map<String, List<String>> mapExistedUser = getUserExistedMap_v();
				for (int i = 0; i < receiceManArray.length; i++) {
					// �����ж�����grid��治���ڣ�
					String pk_user = receiceManList.get(i).getPk_user();
					String pk_visualorg = receiceManList.get(i).getPk_visualorg();
					if (mapExistedUser.containsKey(pk_visualorg)) {
						if (mapExistedUser.get(pk_visualorg).contains(pk_user)) {
							continue;// ����grid���Ѿ����ڸ���ҵ�ĸ��û�
						}
					}
					// �ж���ϵ�������Ƿ��ǽ�����,���ܷŷ�����
					String contacts_type = receiceManList.get(i).getContacts_type();
					if (contacts_type != null && "2".equals(contacts_type)) {
						continue;
					}
					Row row = receive_mands.getEmptyRow();
					String pk_primarykey = generatePk();
					// ����
					row.setValue(receive_mands.nameToIndex(Receive_man.PK_RECEIVE_MAN),
							pk_primarykey);
					// ֪ͨ����
					row.setValue(receive_mands.nameToIndex(Receive_man.PK_NOTICE),
							pk_notice.toString());
					// ������
					row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_MAN),
							receiceManList.get(i).getPk_user());

					// ���յ�λ
					row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_ORG),
							receiceManList.get(i).getPk_org());
					// ������֯
					row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_VISUAL_ORG),
							receiceManList.get(i).getPk_visualorg());
					 // �Ա�
					 row.setValue(receive_mands.nameToIndex(Receive_man.SEX),
							 NoticeUtil.getSexByPkUser(pk_user));

					// �绰����
					row.setValue(receive_mands.nameToIndex(Receive_man.TELEPHONE),
							receiceManList.get(i).getPhone_no());

					// Email
					row.setValue(receive_mands.nameToIndex(Receive_man.EMAIL),
							receiceManList.get(i).getEmail());

					// �鿴״̬(δ�鿴)
					row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_STATUS),
							"1");
					// �ύ״̬��δ�ύ��
					row.setValue(
							receive_mands.nameToIndex(Receive_man.REPORT_STATUS),
							"1");
					// ��ע
					row.setValue(receive_mands.nameToIndex(Receive_man.DEF1),
							receiceManList.get(i).getRemark());
					// �鿴ʱ��
					// row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_TIME),
					// new UFDate());

					receive_mands.addRow(row);
				}
			}


			// �����ҵû����ϵ�ˣ����׳���ص�message��
			String errorMessage = (String) AppUtil.getAppAttr(IGlobalConstants.NOTICE_ERROR_MESSAGE);
			if (errorMessage != null && errorMessage.length() > 0) {
				AppUtil.addAppAttr(IGlobalConstants.NOTICE_ERROR_MESSAGE,null);
				AppInteractionUtil.showConfirmDialog("ȱ����ϵ��", "������ҵ(��֯)û����ص���ϵ�ˣ�" + errorMessage
						+ "��");
			}
  }
  public void doSelectUser(  Map keys){
    LfwView view = AppLifeCycleContext.current().getWindowContext()
					.getViewContext("main").getView();
			Dataset masterDs = this.getMasterDs();
			Row selectedRow = masterDs.getSelectedRow();
			Object pk_notice = selectedRow.getValue(masterDs.nameToIndex(masterDs
					.getPrimaryKeyField()));
			// ���֪ͨ����(������) 1=�֪ͨ 2=��Ϣ֪ͨ
			String notice_style = selectedRow.getValue(masterDs
					.nameToIndex(Notice_manager.NOTICE_STYLE)) == null ? ""
					: selectedRow.getValue(
							masterDs.nameToIndex(Notice_manager.NOTICE_STYLE))
							.toString();
			// ��ñ���ʵ������
			String notice_type = selectedRow.getValue(masterDs
					.nameToIndex(Notice_manager.NOTICE_TYPE)) == null ? ""
					: selectedRow.getValue(
							masterDs.nameToIndex(Notice_manager.NOTICE_TYPE))
							.toString();
			// �����Ϣ����
			String message_type = selectedRow.getValue(masterDs
					.nameToIndex(Notice_manager.DEF4)) == null ? ""
					: selectedRow.getValue(
							masterDs.nameToIndex(Notice_manager.DEF4))
							.toString();
			// ���ݱ�������ȡ�ñ�������
			String reportBody = "";
			// ������֪ͨ���Ǹ����ݱ�������ȡ�ñ�������
			if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
				Work_report_type WorkReportType = (Work_report_type) ScapDAO
						.retrieveByPK(Work_report_type.class, notice_type);
				reportBody = WorkReportType.getReport_body();
			} else if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)) {
				// �����������Ϣ֪ͨ����Ĭ�ϱ��������Ǹ���
				reportBody = IGlobalConstants.REPORT_BODY_MAN;
			} else if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
				// �������ͨ��Ϣ֪ͨ����Ĭ�ϱ��������Ǹ���
				reportBody = IGlobalConstants.REPORT_BODY_MAN;
				// ����й��ܽڵ�����Ļ��������óɹ��ܽڵ������õı������壨��Ҫ������Ϣ�Ĳ鿴����ҵ����֯��ʱ��Ҫ��д���շ�Χ�ӱ���
				String tzxf_report_body = (String)AppUtil.getAppAttr(IGlobalConstants.TZXF_REPORTBODY);
				if (StringUtils.isNotBlank(tzxf_report_body)) {
					reportBody = tzxf_report_body;
				}
			}
			Dataset receive_mands = view.getViewModels().getDataset("receive_man");
			receive_mands.setOrderByPart(" order by  receive_org asc ");
			if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style) || IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)
					|| (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style) && NoticeUtil.ifExitedMessageType(message_type))) {
				LfwView view_comp = AppLifeCycleContext.current().getWindowContext()
						.getViewContext("multiUserSelect").getView();
				if (reportBody != null && (IGlobalConstants.REPORT_BODY_QY.equals(reportBody) || IGlobalConstants.REPORT_BODY_MAN.equals(reportBody))) {

					Dataset cp_userpsnds = view_comp.getViewModels().getDataset(
							"cp_userpsn");
					Row[] selRows = cp_userpsnds.getAllSelectedRows();
					Map<String, List<String>> mapExisted = getUserExistedMap_r();
					// ��д �������ӱ��ds
					for (int i = 0; i < selRows.length; i++) {
						// �����ж�����grid��治���ڣ�
						String pk_user = (String) selRows[i]
								.getValue(cp_userpsnds
										.nameToIndex(notice_contact_info.PK_USER));
						String pk_org = (String) selRows[i].getValue(cp_userpsnds
								.nameToIndex(notice_contact_info.PK_ORG));
						if (mapExisted.containsKey(pk_org)) {
							if (mapExisted.get(pk_org).contains(pk_user)) {
								continue;// ����grid���Ѿ����ڸ���ҵ�ĸ��û�
							}
						}
						Row row = receive_mands.getEmptyRow();
						String pk_primarykey = generatePk();
						// ����
						row.setValue(receive_mands.nameToIndex(Receive_man.PK_RECEIVE_MAN),
								pk_primarykey);
						// ֪ͨ����
						row.setValue(receive_mands.nameToIndex(Receive_man.PK_NOTICE),
								pk_notice.toString());
						// ������
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_MAN),
								pk_user);
						// ���յ�λ
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_ORG),
								pk_org);
						// // �Ա�
						// row.setValue(receive_mands.nameToIndex(Receive_man.SEX),
						// receiceManList.get(i).get);

						// �绰����
						row.setValue(receive_mands.nameToIndex(Receive_man.TELEPHONE),
								(String) selRows[i].getValue(cp_userpsnds
										.nameToIndex(notice_contact_info.PHONE_NO)));
						// Email
						row.setValue(receive_mands.nameToIndex(Receive_man.EMAIL),
								(String) selRows[i].getValue(cp_userpsnds
										.nameToIndex(notice_contact_info.EMAIL)));
						// �鿴״̬(δ�鿴)
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_STATUS),
								"1");
						// �ύ״̬��δ�ύ��
						row.setValue(
								receive_mands.nameToIndex(Receive_man.REPORT_STATUS),
								"1");
						receive_mands.addRow(row);

					}
				}
				if (reportBody != null && (IGlobalConstants.REPORT_BODY_VISORG.equals(reportBody) || IGlobalConstants.REPORT_BODY_MAN.equals(reportBody))) {

					Dataset visualUserInfods = view_comp.getViewModels().getDataset(
							"visualUserInfo");
					Row[] selRows = visualUserInfods.getAllSelectedRows();
					Map<String, List<String>> mapExisted = getUserExistedMap_v();
					// ��д �������ӱ��ds
					for (int i = 0; i < selRows.length; i++) {
						// �����ж�����grid��治���ڣ�
						String pk_user = (String) selRows[i]
								.getValue(visualUserInfods
										.nameToIndex(notice_contact_info.PK_USER));
						String pk_visualorg = (String) selRows[i].getValue(visualUserInfods
								.nameToIndex(notice_contact_info.PK_VISUALORG));
						if (mapExisted.containsKey(pk_visualorg)) {
							if (mapExisted.get(pk_visualorg).contains(pk_user)) {
								continue;// ����grid���Ѿ����ڸ���ҵ�ĸ��û�
							}
						}
						Row row = receive_mands.getEmptyRow();
						String pk_primarykey = generatePk();
						// ����
						row.setValue(receive_mands.nameToIndex(Receive_man.PK_RECEIVE_MAN),
								pk_primarykey);
						// ֪ͨ����
						row.setValue(receive_mands.nameToIndex(Receive_man.PK_NOTICE),
								pk_notice.toString());
						// ������
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_MAN),
								pk_user);
//						// ���յ�λ
//						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_ORG),
//								pk_org);
						// ������֯
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_VISUAL_ORG),
								pk_visualorg);
						// // �Ա�
						// row.setValue(receive_mands.nameToIndex(Receive_man.SEX),
						// receiceManList.get(i).get);

						// �绰����
						row.setValue(receive_mands.nameToIndex(Receive_man.TELEPHONE),
								(String) selRows[i].getValue(visualUserInfods
										.nameToIndex(notice_contact_info.PHONE_NO)));
						// Email
						row.setValue(receive_mands.nameToIndex(Receive_man.EMAIL),
								(String) selRows[i].getValue(visualUserInfods
										.nameToIndex(notice_contact_info.EMAIL)));
						// �鿴״̬(δ�鿴)
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_STATUS),
								"1");
						// �ύ״̬��δ�ύ��
						row.setValue(
								receive_mands.nameToIndex(Receive_man.REPORT_STATUS),
								"1");
						receive_mands.addRow(row);
					}
				}
			} else if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style) && !(NoticeUtil.ifExitedMessageType(message_type))) {
				LfwView view_comp = AppLifeCycleContext.current().getWindowContext()
						.getViewContext("selectUserByVisualOrg").getView();
				if (reportBody != null && (IGlobalConstants.REPORT_BODY_QY.equals(reportBody) || IGlobalConstants.REPORT_BODY_MAN.equals(reportBody))) {

					Dataset cp_userpsnds = view_comp.getViewModels().getDataset(
							"cp_userpsn");
					Row[] selRows = cp_userpsnds.getAllSelectedRows();
					Map<String, List<String>> mapExisted = getUserExistedMap_r();
					// ��д �������ӱ��ds
					for (int i = 0; i < selRows.length; i++) {
						// �����ж�����grid��治���ڣ�
						String pk_user = (String) selRows[i]
								.getValue(cp_userpsnds
										.nameToIndex(V_userpsnVO.CUSERID));
						String pk_org = (String) selRows[i].getValue(cp_userpsnds
								.nameToIndex(V_userpsnVO.PK_ORG));
						if (mapExisted.containsKey(pk_org)) {
							if (mapExisted.get(pk_org).contains(pk_user)) {
								continue;// ����grid���Ѿ����ڸ���ҵ�ĸ��û�
							}
						}
						Row row = receive_mands.getEmptyRow();
						String pk_primarykey = generatePk();
						// ����
						row.setValue(receive_mands.nameToIndex(Receive_man.PK_RECEIVE_MAN),
								pk_primarykey);
						// ֪ͨ����
						row.setValue(receive_mands.nameToIndex(Receive_man.PK_NOTICE),
								pk_notice.toString());
						// ������
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_MAN),
								pk_user);
						// ���յ�λ
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_ORG),
								pk_org);
						// // �Ա�
						// row.setValue(receive_mands.nameToIndex(Receive_man.SEX),
						// receiceManList.get(i).get);

						// �绰����
						row.setValue(receive_mands.nameToIndex(Receive_man.TELEPHONE),
								(String) selRows[i].getValue(cp_userpsnds
										.nameToIndex(V_userpsnVO.MOBILE)));
						// Email
						row.setValue(receive_mands.nameToIndex(Receive_man.EMAIL),
								(String) selRows[i].getValue(cp_userpsnds
										.nameToIndex(V_userpsnVO.EMAIL)));
						// �鿴״̬(δ�鿴)
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_STATUS),
								"1");
						// �ύ״̬��δ�ύ��
						row.setValue(
								receive_mands.nameToIndex(Receive_man.REPORT_STATUS),
								"1");
						receive_mands.addRow(row);

					}
				}
				if (reportBody != null && (IGlobalConstants.REPORT_BODY_VISORG.equals(reportBody) || IGlobalConstants.REPORT_BODY_MAN.equals(reportBody))) {

					Dataset visualUserInfods = view_comp.getViewModels().getDataset(
							"visualUserInfo");
					Row[] selRows = visualUserInfods.getAllSelectedRows();
					Map<String, List<String>> mapExisted = getUserExistedMap_v();
					// ��д �������ӱ��ds
					for (int i = 0; i < selRows.length; i++) {
						// �����ж�����grid��治���ڣ�
						String pk_user = (String) selRows[i]
								.getValue(visualUserInfods
										.nameToIndex(visualUserInfo.PK_USER));
						String pk_visualorg = (String) selRows[i].getValue(visualUserInfods
								.nameToIndex(visualUserInfo.PK_VISUALORG));
						if (mapExisted.containsKey(pk_visualorg)) {
							if (mapExisted.get(pk_visualorg).contains(pk_user)) {
								continue;// ����grid���Ѿ����ڸ���֯�ĸ��û�
							}
						}
						Row row = receive_mands.getEmptyRow();
						String pk_primarykey = generatePk();
						// ����
						row.setValue(receive_mands.nameToIndex(Receive_man.PK_RECEIVE_MAN),
								pk_primarykey);
						// ֪ͨ����
						row.setValue(receive_mands.nameToIndex(Receive_man.PK_NOTICE),
								pk_notice.toString());
						// ������
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_MAN),
								pk_user);
//						// ���յ�λ
//						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_ORG),
//								pk_org);
						// ������֯
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_VISUAL_ORG),
								pk_visualorg);
						// // �Ա�
						// row.setValue(receive_mands.nameToIndex(Receive_man.SEX),
						// receiceManList.get(i).get);

						// �绰����
						row.setValue(receive_mands.nameToIndex(Receive_man.TELEPHONE),
								(String) selRows[i].getValue(visualUserInfods
										.nameToIndex(notice_contact_info.PHONE_NO)));
						// Email
						row.setValue(receive_mands.nameToIndex(Receive_man.EMAIL),
								(String) selRows[i].getValue(visualUserInfods
										.nameToIndex(notice_contact_info.EMAIL)));
						// �鿴״̬(δ�鿴)
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_STATUS),
								"1");
						// �ύ״̬��δ�ύ��
						row.setValue(
								receive_mands.nameToIndex(Receive_man.REPORT_STATUS),
								"1");
						receive_mands.addRow(row);
					}
				}
			}
  }
  private Map<String,String> isCompExisted(){
    LfwView view = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset receive_noticeds = view.getViewModels().getDataset(
				"receive_notice");
		// ������ҵ�ӱ�ȥ�ش���
		Map<String, String> mapExisted = new HashMap<String, String>();
		Row[] rowExisted = receive_noticeds.getAllRow();
		for (int i = 0; i < rowExisted.length; i++) {
			String pkOrg = (String) rowExisted[i].getValue(receive_noticeds
					.nameToIndex(Receive_notice.NOTICE_ORG));
			if (mapExisted.containsKey(pkOrg)) {
				continue;
			} else {
				mapExisted.put(pkOrg, "1");
			}
		}
		return mapExisted;
  }
  private Map<String,String> isVisualCompExisted(){
    LfwView view = AppLifeCycleContext.current().getWindowContext()
					.getViewContext("main").getView();
			Dataset receive_noticeds = view.getViewModels().getDataset(
					"receive_notice");
			// ������ҵ�ӱ�ȥ�ش���
			Map<String, String> mapExisted = new HashMap<String, String>();
			Row[] rowExisted = receive_noticeds.getAllRow();
			for (int i = 0; i < rowExisted.length; i++) {
				String notice_visual_org = (String) rowExisted[i].getValue(receive_noticeds
						.nameToIndex(Receive_notice.NOTICE_VISUAL_ORG));
				if (mapExisted.containsKey(notice_visual_org)) {
					continue;
				} else {
					mapExisted.put(notice_visual_org, "1");
				}
			}
			return mapExisted;
  }
  private Map<String,List<String>> getUserExistedMap_r(){
    LfwView view = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset receive_mands = view.getViewModels().getDataset("receive_man");
		// �������ӱ�ȥ�ش���
		Map<String, List<String>> mapExisted = new HashMap<String, List<String>>();
		Row[] rowExisted = receive_mands.getAllRow();
		for (int i = 0; i < rowExisted.length; i++) {
			String pkUser = (String) rowExisted[i].getValue(receive_mands
					.nameToIndex(Receive_man.RECEIVE_MAN));
			String pkOrg = (String) rowExisted[i].getValue(receive_mands
					.nameToIndex(Receive_man.RECEIVE_ORG));
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
  private Map<String,List<String>> getUserExistedMap_v(){
    LfwView view = AppLifeCycleContext.current().getWindowContext()
					.getViewContext("main").getView();
			Dataset receive_mands = view.getViewModels().getDataset("receive_man");
			// �������ӱ�ȥ�ش���
			Map<String, List<String>> mapExisted = new HashMap<String, List<String>>();
			Row[] rowExisted = receive_mands.getAllRow();
			for (int i = 0; i < rowExisted.length; i++) {
				String pkUser = (String) rowExisted[i].getValue(receive_mands
						.nameToIndex(Receive_man.RECEIVE_MAN));
				String receive_visual_org = (String) rowExisted[i].getValue(receive_mands
						.nameToIndex(Receive_man.RECEIVE_VISUAL_ORG));
				if (mapExisted.containsKey(receive_visual_org)) {
					mapExisted.get(receive_visual_org).add(pkUser);
				} else {
					List<String> pkUserListTemp = new ArrayList<String>();
					pkUserListTemp.add(pkUser);
					mapExisted.put(receive_visual_org, pkUserListTemp);
				}
			}
			return mapExisted;
  }
  /** 
 * ���շ�Χ�ӱ�ҳʹ��
 * @param dataLoadEvent
 */
  public void onDataLoad_org(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
	    LfwRuntimeEnvironment.getWebContext().getWebSession().addOriginalParameter("openBillId",null);
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
  /** 
 * �������ӱ�ҳʹ��
 * @param dataLoadEvent
 */
  public void onDataLoad_user(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
	    LfwRuntimeEnvironment.getWebContext().getWebSession().addOriginalParameter("openBillId",null);
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
}
