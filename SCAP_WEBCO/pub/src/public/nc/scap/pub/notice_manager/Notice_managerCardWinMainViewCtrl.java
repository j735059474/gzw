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
 * 卡片窗口默认逻辑
 */
public class Notice_managerCardWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
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
						// 通知类型
						row.setValue(
								masterDs.nameToIndex(Notice_manager.NOTICE_STYLE),
								tzxf_notice_style);
						String tzxf_message_type_code = (String) AppUtil.getAppAttr(IGlobalConstants.TZXF_MESSAGE_TYPE_CODE);

						// 消息类型
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
						// 通知类型
						row.setValue(
								masterDs.nameToIndex(Notice_manager.NOTICE_STYLE),
								"1");

						// 通知内容
						row.setValue(
								masterDs.nameToIndex(Notice_manager.NOTICE_CONTENT),
								IGlobalConstants.CONTENT_NOTICE_CONTENT_REPORT);
						// 是否发送提醒
						row.setValue(
								masterDs.nameToIndex(Notice_manager.IS_REMIND), "0");
						// 提醒方式
						row.setValue(
								masterDs.nameToIndex(Notice_manager.REMIND_WAY),
								"1");
						// 提醒内容
						row.setValue(
								masterDs.nameToIndex(Notice_manager.REMIND_INFO),
								IGlobalConstants.CONTENT_REMIND_INFO_REPORT);
						// 是否自动催报
						row.setValue(
								masterDs.nameToIndex(Notice_manager.IS_AUTO_URGE),
								"0");
						// 催报信息发送方式
						row.setValue(masterDs
								.nameToIndex(Notice_manager.URGE_INFO_TRANS_WAY),
								"1");
						// 催报频率
						row.setValue(
								masterDs.nameToIndex(Notice_manager.URGE_FREQUENCY),
								"1");
						// 报送统计内容格式（国资委用户）
						row.setValue(masterDs
								.nameToIndex(Notice_manager.URGE_CONTENT_GZW),
								IGlobalConstants.CONTENT_URGE_CONTENT_GZW);
						// 催报消息内容格式（企业用户）
						row.setValue(masterDs
								.nameToIndex(Notice_manager.URGE_CONTENT_QY),
								IGlobalConstants.CONTENT_URGE_CONTENT_QY);

						// 上报截止日期
						row.setValue(masterDs
								.nameToIndex(Notice_manager.EXPIRATION_DATE),
								new UFDate());
					}
					// 通知下发状态
					row.setValue(
							masterDs.nameToIndex(Notice_manager.NOTICE_STATUS),
							"1");
					// 默认通知日期
					row.setValue(
							masterDs.nameToIndex(Notice_manager.NOTICE_DATE),
							new UFDate());
					// 发送通知人
					row.setValue(
							masterDs.nameToIndex(Notice_manager.NOTICE_PSN),
							CntUtil.getCntUserPk());
					// 发送通知企业
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
						throw new LfwRuntimeException("当前Dataset没有设置主键!");
					}
					String primaryKeyValue = (String) this.getDs()
							.getSelectedRow()
							.getValue(this.getDs().nameToIndex(primaryKey));
					FillFileInfoHelper.resetItem(primaryKeyValue);
					// 查看时数据不可修改
					String pro_action = LfwRuntimeEnvironment.getWebContext()
							.getOriginalParameter("pro_action");
					if ((ScapCoConstants.DETAIL).equals(pro_action)) {
						String pkUser = CntUtil.getCntUserPk();
						String name = NoticeUtil.getNameByPkUser(pkUser);
						Dataset ds = this.getDs();
						// 用户名字替换
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
		// 查看时数据不可修改
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
 * 主数据选中逻辑
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
				.nameToIndex(Notice_manager.NOTICE_STYLE));// 获取通知类型
		if (AppConsts.OPE_ADD.equals(oper)) {
			// 资料类型初期化时设为隐藏
			// 资料类型
			FormElement data_type_name = form.getElementById("data_type_name"); // 资料类型
			data_type_name.setVisible(false);

		} else if (AppConsts.OPE_EDIT.equals(oper)) {
			if (notice_style != null && "2".equals(notice_style)) { // 消息通知
				// 业务类型
				FormElement business_type_name = form
						.getElementById("business_type_name");
				business_type_name.setVisible(false);
				// 报告实例类型
				FormElement notice_type_report_type = form
						.getElementById("notice_type_report_type");
				notice_type_report_type.setVisible(false);
				// 是否自动催报
				FormElement is_auto_urge = form.getElementById("is_auto_urge"); // 是否自动催报
				is_auto_urge.setVisible(false);
				// 催报信息发送方式
				FormElement urge_info_trans_way = form
						.getElementById("urge_info_trans_way"); // 催报信息发送方式
				urge_info_trans_way.setVisible(false);
				// 提前催报天数
				FormElement day_num = form.getElementById("day_num"); // 提前催报天数
				day_num.setVisible(false);
				// 催报频率
				FormElement urge_frequency = form
						.getElementById("urge_frequency"); // 催报频率
				urge_frequency.setVisible(false);

				// 报送统计内容格式（国资委用户）
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // 报送统计内容格式（国资委用户）
				urge_content_gzw.setVisible(false);

				// 催报消息内容格式（企业用户）
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // 催报消息内容格式（企业用户）
				urge_content_qy.setVisible(false);

			} else if (notice_style != null && "1".equals(notice_style)) {// 填报通知
				// 资料类型
				FormElement data_type_name = form.getElementById("data_type_name"); 
				data_type_name.setVisible(false);
			}
			//修改进来马上添加session参数
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
						//给接受企业子表加wheresql
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
						//给接受人子表加wheresql
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
	
				// 重写此方法，避免选中第一行
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
 * 保存
 * @param datasetEvent
 */
  public void onSave(  MouseEvent<?> mouseEvent) throws BusinessException {
    Dataset masterDs = this.getMasterDs();
	  	// 按照业务类型，报告类型，上报截止日期三个字段确定一个通知。如果这三个字段不用的话，就认为是两个通知。
	  	// 否则的话，不能保存
	  	Row row = masterDs.getSelectedRow();
	  	String notice_style = (String)row.getValue(masterDs.nameToIndex(Notice_manager.NOTICE_STYLE));
	  	String business_type = (String)row.getValue(masterDs.nameToIndex(Notice_manager.BUSINESS_TYPE));
	  	String notice_type = (String)row.getValue(masterDs.nameToIndex(Notice_manager.NOTICE_TYPE));
	  	UFDate expiration_date = (UFDate)row.getValue(masterDs.nameToIndex(Notice_manager.EXPIRATION_DATE));
	  	if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
		  	if (expiration_date == null) {
		  		throw new LfwRuntimeException("上报截止日期为必填项！");
		  	}
		  	String oper = getOperator();
		  	Boolean isUnique = NoticeUtil.checkIsUnique(business_type,notice_type,expiration_date,oper);
		  	if (Boolean.FALSE.equals(isUnique)) {
		           throw new LfwRuntimeException("相关通知已存在，该通知不能保存！");
		  	}
	  	}
	  	// 保存数据
		CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this
				.getDetailDsIds(), false));
		masterDs.setEnabled(true);

		this.getCurrentAppCtx().closeWinDialog();
		// 更新list页面
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		Row savedRow = masterDs.getSelectedRow();
		paramMap.put(OPERATE_ROW, savedRow);
		CmdInvoker.invoke(new UifPlugoutCmd(getCurrentView().getId(),
				PLUGOUT_ID, paramMap));
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
		if (primaryKey == null) {
			throw new LfwRuntimeException("当前Dataset没有设置主键!");
		}
		String primaryKeyValue = (String) row.getValue(ds
				.nameToIndex(primaryKey));
		Map<String, String> paramMap = null;
		if (primaryKeyValue != null) {
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
 * 设置PK_ORG字段值
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
		// 如果通知类型改变,隐藏字段
		if (colIndex == masterDs.nameToIndex(Notice_manager.NOTICE_STYLE)) {
			FormElement notice_style = form.getElementById("notice_style"); // 获取通知类型
			// 填报通知，显示需要显示的字段（催报相关）
			if (notice_style.getValue().equals("1")) {
				// 通知内容
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_CONTENT), IGlobalConstants.CONTENT_NOTICE_CONTENT_REPORT);
				// 报告业务类型
				FormElement business_type_name = form
						.getElementById("business_type_name"); // 报告业务类型
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.BUSINESS_TYPE), "");
				business_type_name.setVisible(true);
				business_type_name.setNullAble(false);
				// 报告类型
				FormElement notice_type_report_type = form
						.getElementById("notice_type_report_type"); // 报告类型
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_TYPE), "");
				notice_type_report_type.setVisible(true);
				notice_type_report_type.setNullAble(false);
				// 资料类型
				FormElement data_type_name = form
						.getElementById("data_type_name"); // 资料类型
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.DATA_TYPE), "");
				data_type_name.setVisible(false);
				data_type_name.setNullAble(true);
				// 【催报相关】
				FormElement urge_related = form.getElementById("urge_related"); // 【催报相关】
				urge_related.setVisible(true);
				// 是否自动催报
				FormElement is_auto_urge = form.getElementById("is_auto_urge"); // 是否自动催报
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.IS_AUTO_URGE), "0");
				is_auto_urge.setVisible(true);
				// 催报信息发送方式
				FormElement urge_info_trans_way = form
						.getElementById("urge_info_trans_way"); // 催报信息发送方式
				masterDs.setValue(masterDs
						.nameToIndex(Notice_manager.URGE_INFO_TRANS_WAY), "1");
				urge_info_trans_way.setVisible(true);
				// 提前催报天数
				FormElement day_num = form.getElementById("day_num"); // 提前催报天数
				masterDs.setValue(masterDs.nameToIndex(Notice_manager.DAY_NUM),
						"");
				day_num.setVisible(true);
				// 催报频率
				FormElement urge_frequency = form
						.getElementById("urge_frequency"); // 催报频率
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.URGE_FREQUENCY),
						"1");
				urge_frequency.setVisible(true);

				// 报送统计内容格式（国资委用户）
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // 报送统计内容格式（国资委用户）
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.URGE_CONTENT_GZW),
						IGlobalConstants.CONTENT_URGE_CONTENT_GZW);
				urge_content_gzw.setVisible(true);

				// 催报消息内容格式（企业用户）
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // 催报消息内容格式（企业用户）
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.URGE_CONTENT_QY),
						IGlobalConstants.CONTENT_URGE_CONTENT_QY);
				urge_content_qy.setVisible(true);
				// 提醒内容
				FormElement remind_info = form.getElementById("remind_info"); // 提醒内容
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.REMIND_INFO),
						IGlobalConstants.CONTENT_REMIND_INFO_REPORT);
				remind_info.setVisible(true);
				String[] elesReceiveNoticeGrid = new String[] { "report_status","report_time"};
				CpCtrlUtil.gridColmVisiable(receiveNoticeGrid,elesReceiveNoticeGrid,true);
				String[] elesReceiveManGrid = new String[] { "report_status","report_time"};
				CpCtrlUtil.gridColmVisiable(receiveManGrid,elesReceiveManGrid,true);
			} else if (notice_style.getValue().equals("2")) { // 资料消息通知时
																// 设置相应字段隐藏,内容清空;
				// 通知内容
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_CONTENT), IGlobalConstants.CONTENT_NOTICE_CONTENT_MESSAGE);
				// 报告业务类型
				FormElement business_type_name = form
						.getElementById("business_type_name"); // 报告业务类型
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.BUSINESS_TYPE), "");
				business_type_name.setVisible(false);

				// 报告类型
				FormElement notice_type_report_type = form
						.getElementById("notice_type_report_type"); // 报告类型
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_TYPE), "");
				notice_type_report_type.setVisible(false);

				// 资料类型
				FormElement data_type_name = form
						.getElementById("data_type_name"); // 资料类型
				// 根据list画面初期化时查到的该用户在常用联系人维护时所属的资料类型 设置一个默认的资料类型（方便消息通知）
		  		masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.DATA_TYPE), "");
			  	data_type_name.setVisible(true);

				// 【催报相关】
				FormElement urge_related = form.getElementById("urge_related"); // 【催报相关】
				urge_related.setVisible(false);
				
				// 是否自动催报
				FormElement is_auto_urge = form.getElementById("is_auto_urge"); // 是否自动催报
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.IS_AUTO_URGE), "");
				is_auto_urge.setVisible(false);
				// 催报信息发送方式
				FormElement urge_info_trans_way = form
						.getElementById("urge_info_trans_way"); // 催报信息发送方式
				masterDs.setValue(masterDs
						.nameToIndex(Notice_manager.URGE_INFO_TRANS_WAY), "");
				urge_info_trans_way.setVisible(false);
				// 提前催报天数
				FormElement day_num = form.getElementById("day_num"); // 提前催报天数
				masterDs.setValue(masterDs.nameToIndex(Notice_manager.DAY_NUM),
						"");
				day_num.setVisible(false);
				// 催报频率
				FormElement urge_frequency = form
						.getElementById("urge_frequency"); // 催报频率
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.URGE_FREQUENCY), "");
				urge_frequency.setVisible(false);

				// 报送统计内容格式（国资委用户）
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // 报送统计内容格式（国资委用户）
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.URGE_CONTENT_GZW),
						"");
				urge_content_gzw.setVisible(false);

				// 催报消息内容格式（企业用户）
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // 催报消息内容格式（企业用户）
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.URGE_CONTENT_QY),
						"");
				urge_content_qy.setVisible(false);
				// 提醒内容
				FormElement remind_info = form.getElementById("remind_info"); // 提醒内容
				masterDs.setValue(
						masterDs.nameToIndex(Notice_manager.REMIND_INFO),
						IGlobalConstants.CONTENT_REMIND_INFO_MESSAGE);
				remind_info.setVisible(true);
				String[] elesReceiveNoticeGrid = new String[] { "report_status","report_time"};
				CpCtrlUtil.gridColmVisiable(receiveNoticeGrid,elesReceiveNoticeGrid,false);
				String[] elesReceiveManGrid = new String[] { "report_status","report_time"};
				CpCtrlUtil.gridColmVisiable(receiveManGrid,elesReceiveManGrid,false);
			} else if (notice_style.getValue().equals("3"))   { // 消息通知
				//如果是普通消息通知的话进行相应字段的隐藏
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
				// 保存的一个业务类型
				String def1 = (String) masterDs.getValue(masterDs
						.nameToIndex(Notice_manager.DEF1));
				// 通知内容
				FormElement notice_content = form
						.getElementById("notice_content"); // 通知内容
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
				// 报送统计内容格式（国资委用户）
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // 报送统计内容格式（国资委用户）
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

				// 催报消息内容格式（企业用户）
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // 催报消息内容格式（企业用户）
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
				// 提醒内容
				FormElement remind_info = form.getElementById("remind_info"); // 提醒内容
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
				// 保存新的业务类型到def1中
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
				// 保存的一个报告类型
				String def2 = (String) masterDs.getValue(masterDs
						.nameToIndex(Notice_manager.DEF2));
				// 通知内容
				FormElement notice_content = form
						.getElementById("notice_content"); // 通知内容
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
				// 报送统计内容格式（国资委用户）
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // 报送统计内容格式（国资委用户）
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

				// 催报消息内容格式（企业用户）
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // 催报消息内容格式（企业用户）
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
				// 提醒内容
				FormElement remind_info = form.getElementById("remind_info"); // 提醒内容
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
				// 保存新的报告类型到def2中
				masterDs.setValue(masterDs.nameToIndex(Notice_manager.DEF2),
						notice_type_report_type);
			}
		} else if (colIndex == masterDs.nameToIndex(Notice_manager.DATA_TYPE)) {
			if ((String) masterDs.getValue(masterDs
					.nameToIndex(Notice_manager.DATA_TYPE)) != null) {
				String data_type_name = form.getElementById("data_type_name")
						.getValue();
				// 保存的一个资料类型
				String def3 = (String) masterDs.getValue(masterDs
						.nameToIndex(Notice_manager.DEF3));
				// test
				String DATA_TYPE = (String) masterDs.getValue(masterDs
						.nameToIndex(Notice_manager.DATA_TYPE));
				// 通知内容
				FormElement notice_content = form
						.getElementById("notice_content"); // 通知内容
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
				
				// 提醒内容
				FormElement remind_info = form.getElementById("remind_info"); // 提醒内容
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
				// 保存新的资料类型到def3中
				masterDs.setValue(masterDs.nameToIndex(Notice_manager.DEF3),
						data_type_name);
			}
		}
		// 如果"是否发送提醒"改变
		if (colIndex == masterDs.nameToIndex(Notice_manager.IS_REMIND)) {
			FormElement is_remind = form.getElementById("is_remind"); // 获取是否发送提醒
			// 是否发送提醒： 0：是；1：否。
			if ("0".equals(is_remind.getValue())) {
				// 提醒方式
				FormElement remind_way = form.getElementById("remind_way"); // 提醒方式
				remind_way.setEnabled(true);
				// 提醒内容
				FormElement remind_info = form.getElementById("remind_info"); // 提醒内容
				remind_info.setEnabled(true);
			}
			if ("1".equals(is_remind.getValue())) {
				// 提醒方式
				FormElement remind_way = form.getElementById("remind_way"); // 提醒方式
				remind_way.setEnabled(false);
				// 提醒内容
				FormElement remind_info = form.getElementById("remind_info"); // 提醒内容
				remind_info.setEnabled(false);
			}
		}
		// 如果"是否自动催报"改变
		if (colIndex == masterDs.nameToIndex(Notice_manager.IS_AUTO_URGE)) {
			FormElement is_auto_urge = form.getElementById("is_auto_urge"); // 获取是否自动催报
			// 是否自动催报： 0：是；1：否。
			if ("0".equals(is_auto_urge.getValue())) {
				// 催报信息发送方式
				FormElement urge_info_trans_way = form
						.getElementById("urge_info_trans_way"); // 催报信息发送方式
				urge_info_trans_way.setEnabled(true);
				// 提前催报天数
				FormElement day_num = form.getElementById("day_num"); // 提前催报天数
				day_num.setEnabled(true);
				// 催报频率
				FormElement urge_frequency = form
						.getElementById("urge_frequency"); // 催报频率
				urge_frequency.setEnabled(true);
				// 报送统计内容格式（国资委用户）
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // 报送统计内容格式（国资委用户）
				urge_content_gzw.setEnabled(true);
				// 催报消息内容格式（企业用户）
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // 催报消息内容格式（企业用户）
				urge_content_qy.setEnabled(true);
			}
			if ("1".equals(is_auto_urge.getValue())) {
				// 催报信息发送方式
				FormElement urge_info_trans_way = form
						.getElementById("urge_info_trans_way"); // 催报信息发送方式
				urge_info_trans_way.setEnabled(false);
				// 提前催报天数
				FormElement day_num = form.getElementById("day_num"); // 提前催报天数
				day_num.setEnabled(false);
				// 催报频率
				FormElement urge_frequency = form
						.getElementById("urge_frequency"); // 催报频率
				urge_frequency.setEnabled(false);
				// 报送统计内容格式（国资委用户）
				FormElement urge_content_gzw = form
						.getElementById("urge_content_gzw"); // 报送统计内容格式（国资委用户）
				urge_content_gzw.setEnabled(false);
				// 催报消息内容格式（企业用户）
				FormElement urge_content_qy = form
						.getElementById("urge_content_qy"); // 催报消息内容格式（企业用户）
				urge_content_qy.setEnabled(false);
			}

		}
		// 如果相应的数据项都已经有了值，那么就可以选择企业了。（在这个方法里会向session里面添加数据）
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

	  	// 如果没有接收范围数据或者接收人数据，不能保存
		if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
			if (StringUtils.isBlank(business_type)) {
				throw new LfwRuntimeException("业务类型不能为空！");
			}
			if (StringUtils.isBlank(notice_type)) {
				throw new LfwRuntimeException("报告类型不能为空！");
			}
			String report_body = NoticeUtil.getReportBodyByNoticeType(notice_type);
			if (IGlobalConstants.REPORT_BODY_QY.equals(report_body)) {
				if (receive_noticeds.getAllRow().length == 0) {
					throw new LfwRuntimeException("没有选择接收企业，不能保存通知！");
				}
			}
			if (IGlobalConstants.REPORT_BODY_VISORG.equals(report_body)) {
				if (receive_noticeds.getAllRow().length == 0) {
					throw new LfwRuntimeException("没有选择接收组织，不能保存通知！");
				}
			}
			if (IGlobalConstants.REPORT_BODY_MAN.equals(report_body)) {
				if (receive_mands.getAllRow().length == 0) {
					throw new LfwRuntimeException("没有选择接收人，不能保存通知！");
				}
			}
			// 唯一性检查
			UFDate expiration_date = (UFDate)selectedRow.getValue(
							masterDs.nameToIndex(Notice_manager.EXPIRATION_DATE));
		  	if (expiration_date == null) {
		  		throw new LfwRuntimeException("上报截止日期为必填项！");
		  	}
		  	String oper = getOperator();
			Boolean isUnique = NoticeUtil.checkIsUnique(business_type, notice_type, expiration_date,oper);
		  	if (Boolean.FALSE.equals(isUnique)) {
		           throw new LfwRuntimeException("相关通知已存在，该通知不能保存！");
		  	}
		}
		if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)) {
			if (StringUtils.isBlank(data_type)) {
				throw new LfwRuntimeException("资料类型不能为空！");
			}
			if (receive_mands.getAllRow().length == 0) {
				throw new LfwRuntimeException("没有选择接收人，不能保存通知！");
			}
		}
		if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
			// 如果是普通消息通知，那么默认的报告主体是个人；如果通知下发功能节点设置了报告主体的参数，那么按照设定的参照进行设置
			String report_body = IGlobalConstants.REPORT_BODY_MAN;
			String tzxf_report_body = (String)AppUtil.getAppAttr(IGlobalConstants.TZXF_REPORTBODY);
			if (StringUtils.isNotBlank(tzxf_report_body)) {
				report_body = tzxf_report_body;
			}
			if (IGlobalConstants.REPORT_BODY_QY.equals(report_body)) {
				if (receive_noticeds.getAllRow().length == 0) {
					throw new LfwRuntimeException("没有选择接收企业，不能保存通知！");
				}
			}
			if (IGlobalConstants.REPORT_BODY_VISORG.equals(report_body)) {
				if (receive_noticeds.getAllRow().length == 0) {
					throw new LfwRuntimeException("没有选择接收组织，不能保存通知！");
				}
			}
			if (IGlobalConstants.REPORT_BODY_MAN.equals(report_body)) {
				if (receive_mands.getAllRow().length == 0) {
					throw new LfwRuntimeException("没有选择接收人，不能保存通知！");
				}
			}
		}
		// 保存数据
		CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), this
				.getDetailDsIds(), false));

		String pk_notice = selectedRow.getValue(masterDs
				.nameToIndex(Notice_manager.PK_NOTICE)) == null ? ""
				: selectedRow.getValue(
						masterDs.nameToIndex(Notice_manager.PK_NOTICE))
						.toString();
		StringBuffer errorMessage = new StringBuffer();
		// check是否可以下发通知
		String report_body = "";
		if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
			report_body = NoticeUtil.getReportBodyByNoticeType(notice_type);
		}
		if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
			// 如果是普通消息通知，那么默认的报告主体是个人；如果通知下发功能节点设置了报告主体的参数，那么按照设定的参照进行设置
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

		// 如果企业没有联系人，则抛出相关的message。通知状态不设置为"已下发".
		if (errorMessage != null && errorMessage.length() > 0) {
			throw new LfwRuntimeException("以下企业（组织）没有相关的联系人：" + errorMessage.substring(0,errorMessage.length() - 1)
					+ "。此通知现在只是保存并没有下发!");
		} else { // 否则 ，把通知状态设为"已下发"
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
		
		// 提醒功能相关
		NoticeUtil.remindByNoticePK(pk_notice);
		
		this.getCurrentAppCtx().closeWinDialog();
		
		// 更新list页面
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
			throw new LfwRuntimeException("请选择相应的通知!");
		}
		String notice_type = rowm.getString(masterDs
				.nameToIndex(Notice_manager.NOTICE_TYPE));
		String reportBody = "";
		if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
			// 根据报告类型取得报告主体
			Work_report_type WorkReportType = (Work_report_type) ScapDAO
					.retrieveByPK(Work_report_type.class, notice_type);
			reportBody = WorkReportType.getReport_body();
		} else if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)) {
			// 资料消息通知默认报告主体就是个人
			reportBody = IGlobalConstants.REPORT_BODY_MAN;
		} else if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
			// 普通消息通知默认报告主体就是个人
			reportBody = IGlobalConstants.REPORT_BODY_MAN;
			// 如果有功能节点参数的话，就设置成功能节点中设置的报告主体（主要用于消息的查看（企业和组织的时候要回写接收范围子表））
			String tzxf_report_body = (String)AppUtil.getAppAttr(IGlobalConstants.TZXF_REPORTBODY);
			if (StringUtils.isNotBlank(tzxf_report_body)) {
				reportBody = tzxf_report_body;
			}
		}
		// 根据报告主体进行相应处理
		if (reportBody != null && ("1".equals(reportBody))) {//填报主体是企业
			String pkUser = CntUtil.getCntUserPk();
			BaseDAO baseDAO = new BaseDAO();
			// 获得需要修改状态的接收人信息
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
						ScapDAO.getBaseDAO().updateVO(vo);// 更新操作
					}
				}
			} catch (DAOException e) {
				ScapLogger.error("更新操作出现错误！错误异常：" + e.getMessage());
			}
			// 确认企业是否已查看
			// 获得企业信息
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
						if ("1".equals(vo.getReceive_status())) { // 企业未查看
							isCompReaded = false;
						}
					}
				}
			} catch (DAOException e) {
				ScapLogger.error("查找操作出现错误！错误异常：" + e.getMessage());
			}
			// 如果企业已查看，直接返回
			if (isCompReaded.equals(true)) {
				return;
			}
			// 如果企业未查看
			// 确认同一个企业的所有人是否全部查看，如果是的话，需要修改企业查看状态为已查看;如果否的话，提醒用户是否确认企业已查看
			Boolean isAllManReaded = true;
			// 获得同一个企业的可以查看该通知的人
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
				ScapLogger.error("查找操作出现错误！错误异常：" + e.getMessage());
			}
			if (isAllManReaded.equals(false)) {
				AppInteractionUtil.showConfirmDialog("确认对话框", "是否确认企业已查看？");
				if (!AppInteractionUtil.getConfirmDialogResult().booleanValue())
					return;
			}
			// 该用户确认企业已查看，修改企业表的企业查看状态
			try {
				if (receive_noticeList != null) {
					for (Receive_notice vo : receive_noticeList) {
						vo.setReceive_status("2");// 已查看
						vo.setReceive_time(new UFDate());
						vo.setReceive_man(CntUtil.getCntUserPk());
						ScapDAO.getBaseDAO().updateVO(vo);// 更新操作
					}
				}
			} catch (DAOException e) {
				ScapLogger.error("更新操作出现错误！错误异常：" + e.getMessage());
			}
		}
		// 根据报告主体进行相应处理
		if (reportBody != null && ("2".equals(reportBody))) {//填报主体是组织
			String pkUser = CntUtil.getCntUserPk();
			BaseDAO baseDAO = new BaseDAO();
			// 获得需要修改状态的接收人信息
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
						ScapDAO.getBaseDAO().updateVO(vo);// 更新操作
					}
				}
			} catch (DAOException e) {
				ScapLogger.error("更新操作出现错误！错误异常：" + e.getMessage());
			}
			// 确认组织是否已查看
			// 获得组织信息
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
						if ("1".equals(vo.getReceive_status())) { // 企业未查看
							isVisCompReaded = false;
						}
					}
				}
			} catch (DAOException e) {
				ScapLogger.error("查找操作出现错误！错误异常：" + e.getMessage());
			}
			// 如果组织已查看，直接返回
			if (isVisCompReaded.equals(true)) {
				return;
			}
			// 如果组织未查看
			// 确认同一个组织的所有人是否全部查看，如果是的话，需要修改组织查看状态为已查看;如果否的话，提醒用户是否确认组织已查看
			Boolean isAllManReaded = true;
			// 获得同一个组织的可以查看该通知的人
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
				ScapLogger.error("查找操作出现错误！错误异常：" + e.getMessage());
			}
			if (isAllManReaded.equals(false)) {
				AppInteractionUtil.showConfirmDialog("确认对话框", "是否确认组织已查看？");
				if (!AppInteractionUtil.getConfirmDialogResult().booleanValue())
					return;
			}
			// 该用户确认组织已查看，修改接收范围表的组织查看状态
			try {
				if (receive_noticeList != null) {
					for (Receive_notice vo : receive_noticeList) {
						vo.setReceive_status("2");// 已查看、
						vo.setReceive_time(new UFDate());
						vo.setReceive_man(CntUtil.getCntUserPk());
						ScapDAO.getBaseDAO().updateVO(vo);// 更新操作
					}
				}
			} catch (DAOException e) {
				ScapLogger.error("更新操作出现错误！错误异常：" + e.getMessage());
			}
		}
		// 根据报告主体进行相应处理 ; 
		// 当报告主体是个人时，查看状态只需要更新接收人子表，接收范围子表中该用户所属的组织或企业的查看状态不需要更新
		if (reportBody != null && ("3".equals(reportBody))) {//填报主体是个人
			String pkUser = CntUtil.getCntUserPk();
			BaseDAO baseDAO = new BaseDAO();
			// 获得需要修改状态的接收人信息
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
						ScapDAO.getBaseDAO().updateVO(vo);// 更新操作
					}
				}
			} catch (DAOException e) {
				ScapLogger.error("更新操作出现错误！错误异常：" + e.getMessage());
			}
		}
		// 确认查看成功提示消息
		AppInteractionUtil.showShortMessage("确认查看成功");
		this.getCurrentAppCtx().closeWinDialog();
		// 更新list页面
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		Row savedRow = masterDs.getSelectedRow();
		paramMap.put(OPERATE_ROW, savedRow);
		CmdInvoker.invoke(new UifPlugoutCmd(getCurrentView().getId(),
				PLUGOUT_ID, paramMap));
  }
  public void onReceiveManGridAddClick(  MouseEvent mouseEvent){
    // 判断是否能选择用户。
		if (!canSelectUser()) {
			throw new LfwRuntimeException("没有接受企业，不能进行接收用户选择！");
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
					"selectUserByVisualOrg", "消息接收人选择", "800",
					"600");
			props.setButtonZone(false);
			this.getCurrentAppCtx().getCurrentWindowContext().popView(props);
		} else {
			OpenProperties props = new OpenProperties(
					"multiUserSelect", "联系人选择", "800",
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
    // 先判断主表中通知类型，（业务类型，报告类型）或资料类型或消息类型等字段是否已经填写。
		// 只有已经填写了 ，才能进行企业的选择。否则不能选择接受企业；
		if (!canSelectComp()) {
			throw new LfwRuntimeException("（业务类型，报告类型）或资料类型或消息类型没有选择，所以不能选择接受企业！");
		}
		OpenProperties props = new OpenProperties(
				"visualOrganization", "选择企业或组织", "300",
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
			throw new LfwRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pub", "UifLineDelCmd-000000")/*请选择要删除的行!*/);
		}
		// 接收企业子表删除时，需要先记录下要删除的企业的pk_org。然后删除相应接收人子表的接收人
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
		// 删除接收企业子表的数据
		CmdInvoker.invoke(new UifLineDelCmd(dsId));

		Dataset receive_mands = view.getViewModels().getDataset("receive_man");
		Row[] rows_man = receive_mands.getAllRow();
		for (int i = 0; i < rows_man.length; i++) {
			String receive_org = (String) rows_man[i].getValue(receive_mands
					.nameToIndex(Receive_man.RECEIVE_ORG));
			if (receive_org != null && notice_org.equals(receive_org)) {
				// 则remove掉相应的接收人数据
				int rowIndex = receive_mands.getRowIndex(rows_man[i]);
				receive_mands.setRowSelectIndex(rowIndex);
				CmdInvoker.invoke(new UifLineDelCmd(receive_mands.getId()));
			}
			String receive_visual_org = (String) rows_man[i].getValue(receive_mands
					.nameToIndex(Receive_man.RECEIVE_VISUAL_ORG));
			if (receive_visual_org != null && notice_visual_org.equals(receive_visual_org)) {
				// 则remove掉相应的接收人数据
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
    // 清空session中设置的属性
		AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_BODY, null);
		AppUtil.addAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE, null);
		AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_TYPE, null);
		AppUtil.addAppAttr(IGlobalConstants.APPATTR_DATA_TYPE, null);
		AppUtil.addAppAttr(IGlobalConstants.APPATTR_MESSAGE_TYPE, null);
		
	  	Boolean result = false;
		Dataset masterDs = this.getMasterDs();
		Row selectedRow = masterDs.getSelectedRow();
		// 获得通知类型(必填项) 1=填报通知 2=消息通知
		String notice_style = selectedRow.getValue(masterDs
				.nameToIndex(Notice_manager.NOTICE_STYLE)) == null ? ""
				: selectedRow.getValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_STYLE))
						.toString();
		// 获得报告业务类型
		String besinss_type = selectedRow.getValue(masterDs
				.nameToIndex(Notice_manager.BUSINESS_TYPE)) == null ? ""
				: selectedRow.getValue(
						masterDs.nameToIndex(Notice_manager.BUSINESS_TYPE))
						.toString();
		// 获得报告实例类型
		String notice_type = selectedRow.getValue(masterDs
				.nameToIndex(Notice_manager.NOTICE_TYPE)) == null ? ""
				: selectedRow.getValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_TYPE))
						.toString();
		// 获得资料类型
		String data_type = selectedRow.getValue(masterDs
				.nameToIndex(Notice_manager.DATA_TYPE)) == null ? ""
				: selectedRow.getValue(
						masterDs.nameToIndex(Notice_manager.DATA_TYPE))
						.toString();
		// 获得消息类型
		String message_type = selectedRow.getValue(masterDs
				.nameToIndex(Notice_manager.DEF4)) == null ? ""
				: selectedRow.getValue(
						masterDs.nameToIndex(Notice_manager.DEF4))
						.toString();
		// 接收人选择画面需要用到这些东西
		AppUtil.addAppAttr("notice_style", notice_style);
		// 填报通知时
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
			// 资料消息通知时
		} else if (notice_style.equals("2")) {
			if (!data_type.isEmpty()) {
				result = true;
				// 资料消息通知默认为个人，这样做便于后边的处理
				AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_BODY, IGlobalConstants.REPORT_BODY_MAN);
				AppUtil.addAppAttr(IGlobalConstants.APPATTR_DATA_TYPE, data_type);
			}
			// 普通消息通知时
		} else if (notice_style.equals("3")) {
			if (!message_type.isEmpty()) {
				result = true;
				// 普通消息通知默认为个人，这样做便于后边的处理
				AppUtil.addAppAttr(IGlobalConstants.APPATTR_MESSAGE_TYPE, message_type);
				AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_BODY, IGlobalConstants.REPORT_BODY_MAN);
				// 如果有功能节点参数的话，就设置成功能节点中设置的报告主体（主要用于消息的查看（企业和组织的时候要回写接收范围子表））
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
			throw new LfwRuntimeException("没有选择(业务类型,报告类型),或(资料类型),不能进行人员选择");
		}
		// 查看接收企业子表是否有数据，有数据的话可以选择用户
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


		// 有选择的企业，则可以进行用户选择
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
				// 接收范围中的接受企业的pk_org在用户选择画面左侧组织过滤的时候需要用到
				AppUtil.addAppAttr("pk_org_all",
						pk_org_all.substring(0, pk_org_all.length() - 1));
			} else {
				AppUtil.addAppAttr("pk_org_all",
						"");
			}
			if (pk_visual_org_all.length() > 0) {
				// 接收范围中的接受组织的pk_org在用户选择画面左侧组织过滤的时候需要用到
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
			// 获得通知类型(必填项) 1=填报通知 2=消息通知
			String notice_style = selectedRow.getValue(masterDs
					.nameToIndex(Notice_manager.NOTICE_STYLE)) == null ? ""
					: selectedRow.getValue(
							masterDs.nameToIndex(Notice_manager.NOTICE_STYLE))
							.toString();
			// 获得报告业务类型
			String besinss_type = selectedRow.getValue(masterDs
					.nameToIndex(Notice_manager.BUSINESS_TYPE)) == null ? ""
					: selectedRow.getValue(
							masterDs.nameToIndex(Notice_manager.BUSINESS_TYPE))
							.toString();
			// 获得报告实例类型
			String notice_type = selectedRow.getValue(masterDs
					.nameToIndex(Notice_manager.NOTICE_TYPE)) == null ? ""
					: selectedRow.getValue(
							masterDs.nameToIndex(Notice_manager.NOTICE_TYPE))
							.toString();
			// 获得资料类型
			String data_type = selectedRow.getValue(masterDs
					.nameToIndex(Notice_manager.DATA_TYPE)) == null ? ""
					: selectedRow.getValue(
							masterDs.nameToIndex(Notice_manager.DATA_TYPE))
							.toString();
			// 获得消息类型
			String message_type = selectedRow.getValue(masterDs
					.nameToIndex(Notice_manager.DEF4)) == null ? ""
					: selectedRow.getValue(
							masterDs.nameToIndex(Notice_manager.DEF4))
							.toString();
			// 取出各个子表的dataset
			Dataset receive_noticeds = view.getViewModels().getDataset(
					"receive_notice");
			receive_noticeds.setOrderByPart(" order by NOTICE_ORG, NOTICE_VISUAL_ORG asc ");
			Dataset receive_mands = view.getViewModels().getDataset("receive_man");
			receive_mands.setOrderByPart(" order by RECEIVE_ORG,RECEIVE_VISUAL_ORG asc ");
			
			// 根据报告类型取得报告主体
			String reportBody = "";
			// 如果是填报通知，那个根据报告类型取得报告主体
			if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
				Work_report_type WorkReportType = (Work_report_type) ScapDAO
						.retrieveByPK(Work_report_type.class, notice_type);
				reportBody = WorkReportType.getReport_body();
			} else if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)) {
				// 如果是资料消息通知，则默认报告主体是个人
				reportBody = IGlobalConstants.REPORT_BODY_MAN;
			} else if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
				// 如果是普通消息通知，则默认报告主体是个人
				reportBody = IGlobalConstants.REPORT_BODY_MAN;
				// 如果有功能节点参数的话，就设置成功能节点中设置的报告主体（主要用于消息的查看（企业和组织的时候要回写接收范围子表））
				String tzxf_report_body = (String)AppUtil.getAppAttr(IGlobalConstants.TZXF_REPORTBODY);
				if (StringUtils.isNotBlank(tzxf_report_body)) {
					reportBody = tzxf_report_body;
				}
			}
			// 联系人检索参数map
			Map<String, String> paramMap = new HashMap<String, String>();
	    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_REPORTBODY, reportBody);
			// 根据报告主体进行相应处理
			if (reportBody != null && (IGlobalConstants.REPORT_BODY_QY.equals(reportBody) || IGlobalConstants.REPORT_BODY_MAN.equals(reportBody))) {//填报主体是企业或个人

				Dataset cp_orgsDs = view_comp.getViewModels().getDataset(
						"cp_orgsDs");
				Row[] selRows = cp_orgsDs.getAllSelectedRows();
				String[] pk_orgArray = new String[selRows.length];
				// 循环接受企业list，根据接受企业，
				for (int i = 0; i < selRows.length; i++) {
					String pk_org = (String) selRows[i].getValue(cp_orgsDs
							.nameToIndex(CpOrgVO.PK_ORG));
					pk_orgArray[i] = pk_org;
				}
				// 改成子表实现方式后，在这里只需要把主表的接受企业字段和两个子grid的相应字段回写上即可。
				Map<String, String> mapExistedComp = isCompExisted();
				// 回写 接受企业子表的ds
				for (int i = 0; i < pk_orgArray.length; i++) {
					// 去重处理
					if (mapExistedComp.containsKey(pk_orgArray[i])) {
						continue;
					}
					Row row = receive_noticeds.getEmptyRow();
					String pk_org = pk_orgArray[i];
					String pk_primarykey = generatePk();
					// 主键
					row.setValue(
							receive_noticeds.nameToIndex(Receive_notice.PK_NOTICE_ORG),
							pk_primarykey);

					// 通知主键 主表pk
					row.setValue(
							receive_noticeds.nameToIndex(Receive_notice.PK_NOTICE),
							pk_notice.toString());

					// 接收企业
					row.setValue(
							receive_noticeds.nameToIndex(Receive_notice.NOTICE_ORG),
							pk_org);

					if (IGlobalConstants.REPORT_BODY_QY.equals(reportBody) ) {
						// 查看状态（未查看）
						row.setValue(
								receive_noticeds.nameToIndex(Receive_notice.RECEIVE_STATUS),
								"1");
						// 提交状态（未提交）
						row.setValue(
								receive_noticeds.nameToIndex(Receive_notice.REPORT_STATUS),
								"1");
					}

					receive_noticeds.addRow(row);
				}
				List<notice_contact_info> receiceManList = new ArrayList<notice_contact_info>();
				// 填报通知时按照业务类型和报告类型去寻找联系人
				if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
			    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_BUSINESS_TYPE, besinss_type);
			    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_REPORT_TYPE, notice_type);
					receiceManList = NoticeUtil.getContactsList(paramMap,
							pk_orgArray,Boolean.FALSE);
					// 资料消息通知时按照资料类型去寻找联系人
				} else if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)) {
			    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_DATA_TYPE, data_type);
					receiceManList = NoticeUtil.getContactsList(paramMap, pk_orgArray,Boolean.FALSE);
					// 普通消息通知时不使用联系人节点，不需要往接收人子表里面写数据
				} else if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
					paramMap.put(IGlobalConstants.PARAMMAP_KEY_MESSAGE_TYPE, message_type);
					//联系人中存在相应消息类型，则带出相应的人；否则不存在的话不用带出
					if (NoticeUtil.ifExitedMessageType(message_type)) {
						receiceManList = NoticeUtil.getContactsList(paramMap, pk_orgArray,Boolean.FALSE);
					}
				}

				Receive_man receiceManArray[] = new Receive_man[receiceManList.size()];
				Map<String, List<String>> mapExistedUser = getUserExistedMap_r();
				for (int i = 0; i < receiceManArray.length; i++) {
					// 首先判断现在grid里存不存在；
					String pk_user = receiceManList.get(i).getPk_user();
					String pk_org = receiceManList.get(i).getPk_org();
					if (mapExistedUser.containsKey(pk_org)) {
						if (mapExistedUser.get(pk_org).contains(pk_user)) {
							continue;// 现在grid里已经存在该企业的该用户
						}
					}
					// 判断联系人类型是否是接收人,不能放发送人
					String contacts_type = receiceManList.get(i).getContacts_type();
					if (contacts_type != null && "2".equals(contacts_type)) {
						continue;
					}
					Row row = receive_mands.getEmptyRow();
					String pk_primarykey = generatePk();
					// 主键
					row.setValue(receive_mands.nameToIndex(Receive_man.PK_RECEIVE_MAN),
							pk_primarykey);
					// 通知主键
					row.setValue(receive_mands.nameToIndex(Receive_man.PK_NOTICE),
							pk_notice.toString());
					// 接收人
					row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_MAN),
							receiceManList.get(i).getPk_user());

					// 接收单位
					row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_ORG),
							receiceManList.get(i).getPk_org());

					 // 性别
					 row.setValue(receive_mands.nameToIndex(Receive_man.SEX),
					 NoticeUtil.getSexByPkUser(pk_user));

					// 电话号码
					row.setValue(receive_mands.nameToIndex(Receive_man.TELEPHONE),
							receiceManList.get(i).getPhone_no());

					// Email
					row.setValue(receive_mands.nameToIndex(Receive_man.EMAIL),
							receiceManList.get(i).getEmail());

					// 查看状态(未查看)
					row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_STATUS),
							"1");
					// 提交状态（未提交）
					row.setValue(
							receive_mands.nameToIndex(Receive_man.REPORT_STATUS),
							"1");
					// 备注
					row.setValue(receive_mands.nameToIndex(Receive_man.DEF1),
							receiceManList.get(i).getRemark());
					// 查看时间
					// row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_TIME),
					// new UFDate());

					receive_mands.addRow(row);
				}
			}
			// 根据报告主体进行相应处理
			if (reportBody != null && (IGlobalConstants.REPORT_BODY_VISORG.equals(reportBody) || IGlobalConstants.REPORT_BODY_MAN.equals(reportBody))) {//填报主体是组织或个人

				Dataset visualOrganizationDs = view_comp.getViewModels().getDataset(
						"visualOrganizationDs");
				Row[] selRows = visualOrganizationDs.getAllSelectedRows();
				String[] pk_visualOrgArray = new String[selRows.length];
				// 循环接受组织list，根据接受组织
				for (int i = 0; i < selRows.length; i++) {
					String pk_visualorg = (String) selRows[i].getValue(visualOrganizationDs
							.nameToIndex(visualOrganization.PK_VISUALORG));
					pk_visualOrgArray[i] = pk_visualorg;
				}
				// 改成子表实现方式后，在这里只需要把两个子grid的相应字段回写上即可。
				Map<String, String> mapExistedComp = isVisualCompExisted();
				// 回写 接受企业子表的ds
				for (int i = 0; i < pk_visualOrgArray.length; i++) {
					// 去重处理
					if (mapExistedComp.containsKey(pk_visualOrgArray[i])) {
						continue;
					}
					Row row = receive_noticeds.getEmptyRow();
					String pk_visualorg = pk_visualOrgArray[i];
					String pk_primarykey = generatePk();
					// 主键
					row.setValue(
							receive_noticeds.nameToIndex(Receive_notice.PK_NOTICE_ORG),
							pk_primarykey);

					// 通知主键 主表pk
					row.setValue(
							receive_noticeds.nameToIndex(Receive_notice.PK_NOTICE),
							pk_notice.toString());

					// 接收企业
					row.setValue(
							receive_noticeds.nameToIndex(Receive_notice.NOTICE_VISUAL_ORG),
							pk_visualorg);

					if (IGlobalConstants.REPORT_BODY_VISORG.equals(reportBody)) {
						// 查看状态（未查看）
						row.setValue(
								receive_noticeds.nameToIndex(Receive_notice.RECEIVE_STATUS),
								"1");
						// 提交状态（未提交）
						row.setValue(
								receive_noticeds.nameToIndex(Receive_notice.REPORT_STATUS),
								"1");
					}

					receive_noticeds.addRow(row);
				}
				List<notice_contact_info> receiceManList = new ArrayList<notice_contact_info>();
				// 填报通知时按照业务类型和报告类型去寻找联系人
				if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
			    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_BUSINESS_TYPE, besinss_type);
			    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_REPORT_TYPE, notice_type);
					receiceManList = NoticeUtil.getContactsList(paramMap,
							pk_visualOrgArray,Boolean.FALSE);
					// 资料消息通知时按照资料类型去寻找联系人
				} else if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)) {
			    	paramMap.put(IGlobalConstants.PARAMMAP_KEY_DATA_TYPE, data_type);
					receiceManList = NoticeUtil.getContactsList(paramMap, pk_visualOrgArray,Boolean.FALSE);
					// 普通消息通知时
				} else if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
					paramMap.put(IGlobalConstants.PARAMMAP_KEY_MESSAGE_TYPE, message_type);
					//联系人中存在相应消息类型，则带出相应的人；否则不存在的话不用带出
					if (NoticeUtil.ifExitedMessageType(message_type)) {
						receiceManList = NoticeUtil.getContactsList(paramMap, pk_visualOrgArray,Boolean.FALSE);
					}
				}
				Receive_man receiceManArray[] = new Receive_man[receiceManList.size()];
				Map<String, List<String>> mapExistedUser = getUserExistedMap_v();
				for (int i = 0; i < receiceManArray.length; i++) {
					// 首先判断现在grid里存不存在；
					String pk_user = receiceManList.get(i).getPk_user();
					String pk_visualorg = receiceManList.get(i).getPk_visualorg();
					if (mapExistedUser.containsKey(pk_visualorg)) {
						if (mapExistedUser.get(pk_visualorg).contains(pk_user)) {
							continue;// 现在grid里已经存在该企业的该用户
						}
					}
					// 判断联系人类型是否是接收人,不能放发送人
					String contacts_type = receiceManList.get(i).getContacts_type();
					if (contacts_type != null && "2".equals(contacts_type)) {
						continue;
					}
					Row row = receive_mands.getEmptyRow();
					String pk_primarykey = generatePk();
					// 主键
					row.setValue(receive_mands.nameToIndex(Receive_man.PK_RECEIVE_MAN),
							pk_primarykey);
					// 通知主键
					row.setValue(receive_mands.nameToIndex(Receive_man.PK_NOTICE),
							pk_notice.toString());
					// 接收人
					row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_MAN),
							receiceManList.get(i).getPk_user());

					// 接收单位
					row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_ORG),
							receiceManList.get(i).getPk_org());
					// 接收组织
					row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_VISUAL_ORG),
							receiceManList.get(i).getPk_visualorg());
					 // 性别
					 row.setValue(receive_mands.nameToIndex(Receive_man.SEX),
							 NoticeUtil.getSexByPkUser(pk_user));

					// 电话号码
					row.setValue(receive_mands.nameToIndex(Receive_man.TELEPHONE),
							receiceManList.get(i).getPhone_no());

					// Email
					row.setValue(receive_mands.nameToIndex(Receive_man.EMAIL),
							receiceManList.get(i).getEmail());

					// 查看状态(未查看)
					row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_STATUS),
							"1");
					// 提交状态（未提交）
					row.setValue(
							receive_mands.nameToIndex(Receive_man.REPORT_STATUS),
							"1");
					// 备注
					row.setValue(receive_mands.nameToIndex(Receive_man.DEF1),
							receiceManList.get(i).getRemark());
					// 查看时间
					// row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_TIME),
					// new UFDate());

					receive_mands.addRow(row);
				}
			}


			// 如果企业没有联系人，则抛出相关的message。
			String errorMessage = (String) AppUtil.getAppAttr(IGlobalConstants.NOTICE_ERROR_MESSAGE);
			if (errorMessage != null && errorMessage.length() > 0) {
				AppUtil.addAppAttr(IGlobalConstants.NOTICE_ERROR_MESSAGE,null);
				AppInteractionUtil.showConfirmDialog("缺少联系人", "以下企业(组织)没有相关的联系人：" + errorMessage
						+ "。");
			}
  }
  public void doSelectUser(  Map keys){
    LfwView view = AppLifeCycleContext.current().getWindowContext()
					.getViewContext("main").getView();
			Dataset masterDs = this.getMasterDs();
			Row selectedRow = masterDs.getSelectedRow();
			Object pk_notice = selectedRow.getValue(masterDs.nameToIndex(masterDs
					.getPrimaryKeyField()));
			// 获得通知类型(必填项) 1=填报通知 2=消息通知
			String notice_style = selectedRow.getValue(masterDs
					.nameToIndex(Notice_manager.NOTICE_STYLE)) == null ? ""
					: selectedRow.getValue(
							masterDs.nameToIndex(Notice_manager.NOTICE_STYLE))
							.toString();
			// 获得报告实例类型
			String notice_type = selectedRow.getValue(masterDs
					.nameToIndex(Notice_manager.NOTICE_TYPE)) == null ? ""
					: selectedRow.getValue(
							masterDs.nameToIndex(Notice_manager.NOTICE_TYPE))
							.toString();
			// 获得消息类型
			String message_type = selectedRow.getValue(masterDs
					.nameToIndex(Notice_manager.DEF4)) == null ? ""
					: selectedRow.getValue(
							masterDs.nameToIndex(Notice_manager.DEF4))
							.toString();
			// 根据报告类型取得报告主体
			String reportBody = "";
			// 如果是填报通知，那个根据报告类型取得报告主体
			if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
				Work_report_type WorkReportType = (Work_report_type) ScapDAO
						.retrieveByPK(Work_report_type.class, notice_type);
				reportBody = WorkReportType.getReport_body();
			} else if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)) {
				// 如果是资料消息通知，则默认报告主体是个人
				reportBody = IGlobalConstants.REPORT_BODY_MAN;
			} else if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
				// 如果是普通消息通知，则默认报告主体是个人
				reportBody = IGlobalConstants.REPORT_BODY_MAN;
				// 如果有功能节点参数的话，就设置成功能节点中设置的报告主体（主要用于消息的查看（企业和组织的时候要回写接收范围子表））
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
					// 回写 接收人子表的ds
					for (int i = 0; i < selRows.length; i++) {
						// 首先判断现在grid里存不存在；
						String pk_user = (String) selRows[i]
								.getValue(cp_userpsnds
										.nameToIndex(notice_contact_info.PK_USER));
						String pk_org = (String) selRows[i].getValue(cp_userpsnds
								.nameToIndex(notice_contact_info.PK_ORG));
						if (mapExisted.containsKey(pk_org)) {
							if (mapExisted.get(pk_org).contains(pk_user)) {
								continue;// 现在grid里已经存在该企业的该用户
							}
						}
						Row row = receive_mands.getEmptyRow();
						String pk_primarykey = generatePk();
						// 主键
						row.setValue(receive_mands.nameToIndex(Receive_man.PK_RECEIVE_MAN),
								pk_primarykey);
						// 通知主键
						row.setValue(receive_mands.nameToIndex(Receive_man.PK_NOTICE),
								pk_notice.toString());
						// 接收人
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_MAN),
								pk_user);
						// 接收单位
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_ORG),
								pk_org);
						// // 性别
						// row.setValue(receive_mands.nameToIndex(Receive_man.SEX),
						// receiceManList.get(i).get);

						// 电话号码
						row.setValue(receive_mands.nameToIndex(Receive_man.TELEPHONE),
								(String) selRows[i].getValue(cp_userpsnds
										.nameToIndex(notice_contact_info.PHONE_NO)));
						// Email
						row.setValue(receive_mands.nameToIndex(Receive_man.EMAIL),
								(String) selRows[i].getValue(cp_userpsnds
										.nameToIndex(notice_contact_info.EMAIL)));
						// 查看状态(未查看)
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_STATUS),
								"1");
						// 提交状态（未提交）
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
					// 回写 接收人子表的ds
					for (int i = 0; i < selRows.length; i++) {
						// 首先判断现在grid里存不存在；
						String pk_user = (String) selRows[i]
								.getValue(visualUserInfods
										.nameToIndex(notice_contact_info.PK_USER));
						String pk_visualorg = (String) selRows[i].getValue(visualUserInfods
								.nameToIndex(notice_contact_info.PK_VISUALORG));
						if (mapExisted.containsKey(pk_visualorg)) {
							if (mapExisted.get(pk_visualorg).contains(pk_user)) {
								continue;// 现在grid里已经存在该企业的该用户
							}
						}
						Row row = receive_mands.getEmptyRow();
						String pk_primarykey = generatePk();
						// 主键
						row.setValue(receive_mands.nameToIndex(Receive_man.PK_RECEIVE_MAN),
								pk_primarykey);
						// 通知主键
						row.setValue(receive_mands.nameToIndex(Receive_man.PK_NOTICE),
								pk_notice.toString());
						// 接收人
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_MAN),
								pk_user);
//						// 接收单位
//						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_ORG),
//								pk_org);
						// 接收组织
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_VISUAL_ORG),
								pk_visualorg);
						// // 性别
						// row.setValue(receive_mands.nameToIndex(Receive_man.SEX),
						// receiceManList.get(i).get);

						// 电话号码
						row.setValue(receive_mands.nameToIndex(Receive_man.TELEPHONE),
								(String) selRows[i].getValue(visualUserInfods
										.nameToIndex(notice_contact_info.PHONE_NO)));
						// Email
						row.setValue(receive_mands.nameToIndex(Receive_man.EMAIL),
								(String) selRows[i].getValue(visualUserInfods
										.nameToIndex(notice_contact_info.EMAIL)));
						// 查看状态(未查看)
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_STATUS),
								"1");
						// 提交状态（未提交）
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
					// 回写 接收人子表的ds
					for (int i = 0; i < selRows.length; i++) {
						// 首先判断现在grid里存不存在；
						String pk_user = (String) selRows[i]
								.getValue(cp_userpsnds
										.nameToIndex(V_userpsnVO.CUSERID));
						String pk_org = (String) selRows[i].getValue(cp_userpsnds
								.nameToIndex(V_userpsnVO.PK_ORG));
						if (mapExisted.containsKey(pk_org)) {
							if (mapExisted.get(pk_org).contains(pk_user)) {
								continue;// 现在grid里已经存在该企业的该用户
							}
						}
						Row row = receive_mands.getEmptyRow();
						String pk_primarykey = generatePk();
						// 主键
						row.setValue(receive_mands.nameToIndex(Receive_man.PK_RECEIVE_MAN),
								pk_primarykey);
						// 通知主键
						row.setValue(receive_mands.nameToIndex(Receive_man.PK_NOTICE),
								pk_notice.toString());
						// 接收人
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_MAN),
								pk_user);
						// 接收单位
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_ORG),
								pk_org);
						// // 性别
						// row.setValue(receive_mands.nameToIndex(Receive_man.SEX),
						// receiceManList.get(i).get);

						// 电话号码
						row.setValue(receive_mands.nameToIndex(Receive_man.TELEPHONE),
								(String) selRows[i].getValue(cp_userpsnds
										.nameToIndex(V_userpsnVO.MOBILE)));
						// Email
						row.setValue(receive_mands.nameToIndex(Receive_man.EMAIL),
								(String) selRows[i].getValue(cp_userpsnds
										.nameToIndex(V_userpsnVO.EMAIL)));
						// 查看状态(未查看)
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_STATUS),
								"1");
						// 提交状态（未提交）
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
					// 回写 接收人子表的ds
					for (int i = 0; i < selRows.length; i++) {
						// 首先判断现在grid里存不存在；
						String pk_user = (String) selRows[i]
								.getValue(visualUserInfods
										.nameToIndex(visualUserInfo.PK_USER));
						String pk_visualorg = (String) selRows[i].getValue(visualUserInfods
								.nameToIndex(visualUserInfo.PK_VISUALORG));
						if (mapExisted.containsKey(pk_visualorg)) {
							if (mapExisted.get(pk_visualorg).contains(pk_user)) {
								continue;// 现在grid里已经存在该组织的该用户
							}
						}
						Row row = receive_mands.getEmptyRow();
						String pk_primarykey = generatePk();
						// 主键
						row.setValue(receive_mands.nameToIndex(Receive_man.PK_RECEIVE_MAN),
								pk_primarykey);
						// 通知主键
						row.setValue(receive_mands.nameToIndex(Receive_man.PK_NOTICE),
								pk_notice.toString());
						// 接收人
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_MAN),
								pk_user);
//						// 接收单位
//						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_ORG),
//								pk_org);
						// 接收组织
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_VISUAL_ORG),
								pk_visualorg);
						// // 性别
						// row.setValue(receive_mands.nameToIndex(Receive_man.SEX),
						// receiceManList.get(i).get);

						// 电话号码
						row.setValue(receive_mands.nameToIndex(Receive_man.TELEPHONE),
								(String) selRows[i].getValue(visualUserInfods
										.nameToIndex(notice_contact_info.PHONE_NO)));
						// Email
						row.setValue(receive_mands.nameToIndex(Receive_man.EMAIL),
								(String) selRows[i].getValue(visualUserInfods
										.nameToIndex(notice_contact_info.EMAIL)));
						// 查看状态(未查看)
						row.setValue(receive_mands.nameToIndex(Receive_man.RECEIVE_STATUS),
								"1");
						// 提交状态（未提交）
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
		// 接收企业子表去重处理
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
			// 接收企业子表去重处理
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
		// 接收人子表去重处理
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
			// 接收人子表去重处理
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
 * 接收范围子表翻页使用
 * @param dataLoadEvent
 */
  public void onDataLoad_org(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
	    LfwRuntimeEnvironment.getWebContext().getWebSession().addOriginalParameter("openBillId",null);
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
  /** 
 * 接收人子表翻页使用
 * @param dataLoadEvent
 */
  public void onDataLoad_user(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
	    LfwRuntimeEnvironment.getWebContext().getWebSession().addOriginalParameter("openBillId",null);
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
}
