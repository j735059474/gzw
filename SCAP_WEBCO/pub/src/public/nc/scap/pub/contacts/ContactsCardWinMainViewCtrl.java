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
 * 卡片窗口默认逻辑
 */
public class ContactsCardWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
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

					Dataset masterDs = getMasterDs();
					String pk_primarykey = generatePk();
					row.setValue(
							masterDs.nameToIndex(masterDs.getPrimaryKeyField()),
							pk_primarykey);
					// 设置默认通知类型
					row.setValue(masterDs
							.nameToIndex(notice_contact_type.NOTICE_STYLE),
							"1");
					// 设置默认填报时间
					row.setValue(masterDs
							.nameToIndex(notice_contact_type.REPORT_DATE),
							new UFDate());
					// 设置默认填报人
					row.setValue(masterDs
							.nameToIndex(notice_contact_type.REPORT_PSN),
							CntUtil.getCntUserPk());
					// 设置默认填报企业
					row.setValue(masterDs
							.nameToIndex(notice_contact_type.REPORT_ORG),
							CntUtil.getCntOrgPk());
					// 设置启用状态
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
						throw new LfwRuntimeException("当前Dataset没有设置主键!");
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
		// 如果是企业进来，主表enabled是false，不能编辑
		// 判断是否企业用户
		if (CpOrgUtil.isCompanyOrg(CntUtil.getCntOrgPk())) {
			masterDs.setEnabled(false);
		}
  }
  /** 
 * 主数据选中逻辑
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
		if (notice_style != null && "1".equals(notice_style)) {// 填报通知
			formcomp.getElementById("ye_type_name").setVisible(
					true);		
			formcomp.getElementById("report_type_report_type").setVisible(
					true);			
			formcomp.getElementById("data_type_name").setVisible(
					false);
			formcomp.getElementById("vdef1").setVisible(
					false);
		} else if (notice_style != null && "2".equals(notice_style)) { // 资料消息通知
			formcomp.getElementById("ye_type_name").setVisible(
					false);		
			formcomp.getElementById("report_type_report_type").setVisible(
					false);	
			formcomp.getElementById("data_type_name").setVisible(
					true);
			formcomp.getElementById("vdef1").setVisible(
					false);
		} else if (notice_style != null && "3".equals(notice_style)) { // 消息通知
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
					// 根据报告类型取得报告主体
					String report_body = "";
					String notice_style = (String)masteDs.getSelectedRow().getValue(masteDs.nameToIndex(notice_contact_type.NOTICE_STYLE));
					String report_type = (String)masteDs.getSelectedRow().getValue(masteDs.nameToIndex(notice_contact_type.REPORT_TYPE));
					// 如果是填报通知，那个根据报告类型取得报告主体
					if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_style)) {
						Work_report_type WorkReportType = (Work_report_type) ScapDAO
								.retrieveByPK(Work_report_type.class, report_type);
						report_body = WorkReportType.getReport_body();
					} else if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)) {
						// 如果是资料消息通知，则默认报告主体是个人
						report_body = IGlobalConstants.REPORT_BODY_MAN;
					} else if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
						// 如果是消息通知，则默认报告主体是个人
						report_body = IGlobalConstants.REPORT_BODY_MAN;
					}
					// 根据报告类型 取得报告主体，从而决定过滤条件是企业还是组织	
					List<String> list_r = NoticeUtil
							.getAllChildPk_OrgByCurrentPk(CntUtil.getCntOrgPk());
					List<String> list_v = NoticeUtil
							.getAllChildPKVisualorgByPkUser(CntUtil.getCntUserPk());
					StringBuffer condition = new StringBuffer("pk_contact_type = '"
							+ vo.getAttributeValue("pk_contact_type") + "' and dr = '0' ");
					String whereSql = "";
					// 报告主体是企业
					if (report_body != null && "1".equals(report_body)) {
						if (list_r != null && list_r.size() > 0) {
							condition
									.append(" and (")
									.append(NoticeUtil.getSqlStrByList(list_r, 1000,
											"pk_org")).append(")");
						}
					}
					// 报告主体是组织
					if (report_body != null && "2".equals(report_body)) {
						if (list_v != null && list_v.size() > 0) {
							condition
							.append(" and (")
							.append(NoticeUtil.getSqlStrByList(list_v, 1000,
									"pk_visualorg")).append(")");
						}
					}
					// 报告主体是个人
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
	
				// 重写此方法，避免选中第一行
				protected void postProcessChildRowSelect(Dataset ds) {
					return;
				}
			});
		}
  }
  /** 
 * 保存
 * @param datasetEvent
 */
  public void onSave(  MouseEvent<?> mouseEvent) throws BusinessException {
    Dataset masterDs = this.getMasterDs();
		// 保存时check ：每种（业务类型，报告类型），资料类型,消息类型应该只有一条数据
		check();
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
 * 子表新增
 */
  public void onGridAddClick(  MouseEvent<?> mouseEvent){
	  	// 取得主表报告类型字段：如果没有设置，那么不能选择联系人
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
				throw new LfwRuntimeException("没有选择需要的报告类型，不能选择联系人！");
			}
			uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_TYPE, report_type);
		}
		if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)) {
			if (data_type == null && StringUtils.isEmpty(data_type)) {
				throw new LfwRuntimeException("没有选择需要的资料类型，不能选择联系人！");
			}
			uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_DATA_TYPE, data_type);
		}
		if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) {
			if (message_type == null && StringUtils.isEmpty(message_type)) {
				throw new LfwRuntimeException("没有选择需要的消息类型，不能选择联系人！");
			}
			uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_MESSAGE_TYPE, message_type);
		}
	  	ScapLogger.console("doSelectUser");
	  	RadioGroupItem radioGroupItem = new RadioGroupItem(IGlobalConstants.APPATTR_SENDER_OR_RECEIVER,"请选择",true);
//	  	radioGroupItem.setValue(value)
	  	ComboData comboData = new StaticComboData();
	  	CombItem sender = new CombItem(IGlobalConstants.CONTENT_TYPE_SENDER,"发送方");
	  	comboData.addCombItem(sender);	  	
	  	CombItem receiver = new CombItem(IGlobalConstants.CONTENT_TYPE_RECEIVER,"接收方");
	  	comboData.addCombItem(receiver);
	  	radioGroupItem.setComboData(comboData);
	  	radioGroupItem.setValue(IGlobalConstants.CONTENT_TYPE_RECEIVER);
	  	AppInteractionUtil.showInputDialog("请选择接收方还是发送方", new InputItem[]{radioGroupItem});
	  	
	  	Map<String, String> rs = AppInteractionUtil.getInputDialogResult();
	  	AppUtil.addAppAttr(IGlobalConstants.APPATTR_SENDER_OR_RECEIVER, rs.get(IGlobalConstants.APPATTR_SENDER_OR_RECEIVER));
		OpenProperties props = new OpenProperties("selectUserByVisualOrg",
				"联系人选择", "800", "600");
		props.setButtonZone(false);
		this.getCurrentAppCtx().getCurrentWindowContext().popView(props);
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
		// 取出联系人详细信息子表的dataset
		Dataset notice_contact_infods = view.getViewModels().getDataset(
				"notice_contact_info");
		// 取出公共view的dataset
		Dataset visualUserInfoDs = view_comp.getViewModels().getDataset(
				"visualUserInfo");
		Dataset cp_userpsnDs = view_comp.getViewModels().getDataset(
				"cp_userpsn");
		StringBuffer errorMessage = new StringBuffer();
		if (report_body != null
				&& ("1".equals(report_body) || "3".equals(report_body))) {
			Row[] selRows_r = cp_userpsnDs.getAllSelectedRows();
			// 取出已经常用联系人详细信息子表dataset中已经存在的人，用于去重处理
			Map<String, List<String>> mapExisted_r = getUserExistedMapByType_r(senderOrReceiver);
			// 回写 常用联系人详细信息子表的ds
			for (int i = 0; i < selRows_r.length; i++) {
				// 首先判断现在grid里存不存在；
				String pk_user = (String) selRows_r[i]
						.getValue(cp_userpsnDs
								.nameToIndex(V_userpsnVO.CUSERID));
				String pk_org = (String) selRows_r[i].getValue(cp_userpsnDs
						.nameToIndex(V_userpsnVO.PK_ORG));
				if (mapExisted_r.containsKey(pk_org)) {
					if (mapExisted_r.get(pk_org).contains(pk_user)) {
						errorMessage.append(NoticeUtil.getNameByPkUser(pk_user)).append(",");
						continue;// 现在grid里已经存在该企业的该用户
					}
				}
				Row row = notice_contact_infods.getEmptyRow();
				String pk_primarykey = generatePk();
				// 主键
				row.setValue(
						notice_contact_infods
								.nameToIndex(notice_contact_info.PK_NOTICE_CONTACT_INFO),
						pk_primarykey);
				// 联系人类型主键（外键设置）
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.PK_CONTACT_TYPE),
						pk_contact_type.toString());
				String user_name = (String) selRows_r[i].getValue(cp_userpsnDs
						.nameToIndex(V_userpsnVO.USER_NAME));
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.CONTACTS_NAME), user_name);
				// 接收人
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.PK_USER), pk_user);
				// 接收企业
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.PK_ORG), pk_org);
				 // 性别
				 row.setValue(notice_contact_infods.nameToIndex(Receive_man.SEX),
						 NoticeUtil.getSexByPkUser(pk_user));

				// 电话号码
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.PHONE_NO),
						(String) selRows_r[i].getValue(cp_userpsnDs
								.nameToIndex(V_userpsnVO.MOBILE)));
				// Email
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.EMAIL), (String) selRows_r[i]
						.getValue(cp_userpsnDs
								.nameToIndex(V_userpsnVO.EMAIL)));
				// 根据选择保存为接收方或发送方
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.CONTACTS_TYPE), senderOrReceiver);
				notice_contact_infods.addRow(row);
			}
		}
		if (report_body != null
				&& ("2".equals(report_body) || "3".equals(report_body))) {
			Row[] selRows_v = visualUserInfoDs.getAllSelectedRows();
			// 取出已经常用联系人详细信息子表dataset中已经存在的人，用于去重处理
			Map<String, List<String>> mapExisted_v = getUserExistedMapByType_v(senderOrReceiver);
			// 回写 常用联系人详细信息子表的ds
			for (int i = 0; i < selRows_v.length; i++) {
				// 首先判断现在grid里存不存在；
				String pk_user = (String) selRows_v[i]
						.getValue(visualUserInfoDs
								.nameToIndex(visualUserInfo.PK_USER));

				String pk_visualorg = (String) selRows_v[i].getValue(visualUserInfoDs
						.nameToIndex(visualUserInfo.PK_VISUALORG));
				if (mapExisted_v.containsKey(pk_visualorg)) {
					if (mapExisted_v.get(pk_visualorg).contains(pk_user)) {
						errorMessage.append(NoticeUtil.getNameByPkUser(pk_user)).append(",");
						continue;// 现在grid里已经存在该组织的该用户
					}
				}
				Row row = notice_contact_infods.getEmptyRow();
				String pk_primarykey = generatePk();
				// 主键
				row.setValue(
						notice_contact_infods
								.nameToIndex(notice_contact_info.PK_NOTICE_CONTACT_INFO),
						pk_primarykey);
				// 联系人类型主键（外键设置）
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.PK_CONTACT_TYPE),
						pk_contact_type.toString());
				// 联系人姓名
				String user_name = (String) selRows_v[i].getValue(visualUserInfoDs
						.nameToIndex(visualUserInfo.USER_NAME));
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.CONTACTS_NAME), user_name);
				// 接收人
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.PK_USER), pk_user);

				// 接收企业
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.PK_VISUALORG), pk_visualorg);
				 // 性别
				 row.setValue(notice_contact_infods.nameToIndex(Receive_man.SEX),
						 NoticeUtil.getSexByPkUser(pk_user));

				// 电话号码
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.PHONE_NO),
						(String) selRows_v[i].getValue(visualUserInfoDs
								.nameToIndex(visualUserInfo.PHONE_NO)));
				// Email
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.EMAIL), (String) selRows_v[i]
						.getValue(visualUserInfoDs
								.nameToIndex(visualUserInfo.EMAIL)));
				// 默认接收方
				row.setValue(notice_contact_infods
						.nameToIndex(notice_contact_info.CONTACTS_TYPE), senderOrReceiver);
				notice_contact_infods.addRow(row);
			}
		}
		if (StringUtils.isNotBlank(errorMessage.toString())) {
			if (IGlobalConstants.CONTENT_TYPE_RECEIVER.equals(senderOrReceiver)) {
				AppInteractionUtil.showMessageDialog("已经存在下列人员的接收方：" + errorMessage.substring(0, errorMessage.length()-1),true);
			}
			if (IGlobalConstants.CONTENT_TYPE_SENDER.equals(senderOrReceiver)) {
				AppInteractionUtil.showMessageDialog("已经存在下列人员的发送方：" + errorMessage.substring(0, errorMessage.length()-1),true);
			}
		}
  }
  private Map<String, List<String>> getUserExistedMapByType_r(String senderOrReceiver){
    LfwView view = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		Dataset notice_contact_infods = view.getViewModels().getDataset(
				"notice_contact_info");
		// 联系人详细信息子表去重处理
		Map<String, List<String>> mapExisted = new HashMap<String, List<String>>();
		Row[] rowExisted = notice_contact_infods.getAllRow();
		for (int i = 0; i < rowExisted.length; i++) {
			String contacts_type = (String) rowExisted[i]
					.getValue(notice_contact_infods
							.nameToIndex(notice_contact_info.CONTACTS_TYPE));
			// 联系人类型： 接收方：1；发送方：2；
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
			// 联系人详细信息子表去重处理
			Map<String, List<String>> mapExisted = new HashMap<String, List<String>>();
			Row[] rowExisted = notice_contact_infods.getAllRow();
			for (int i = 0; i < rowExisted.length; i++) {
				String contacts_type = (String) rowExisted[i]
						.getValue(notice_contact_infods
								.nameToIndex(notice_contact_info.CONTACTS_TYPE));
				// 联系人类型： 接收方：1；发送方：2；
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
			FormElement notice_style = form.getElementById("notice_style"); // 获取通知类型
			if (notice_style.getValue().equals(IGlobalConstants.NOTICE_STYLE_REPORT)) { // 填报通知
				// 业务类型
				FormElement business_type_name = form
						.getElementById("ye_type_name"); // 业务类型
				ds.setValue(
						ds.nameToIndex(notice_contact_type.YE_TYPE), "");
				business_type_name.setVisible(true);
//				business_type_name.setNullAble(false);
				// 报告类型
				FormElement report_type_report_type = form
						.getElementById("report_type_report_type"); // 报告类型
				ds.setValue(
						ds.nameToIndex(notice_contact_type.REPORT_TYPE), "");
				report_type_report_type.setVisible(true);
//				report_type_report_type.setNullAble(false);
				// 资料类型
				FormElement data_type_name = form
						.getElementById("data_type_name"); // 资料类型
				ds.setValue(
						ds.nameToIndex(notice_contact_type.DATA_TYPE), "");
				data_type_name.setVisible(false);
//				data_type_name.setNullAble(true);
				// 消息类型
				FormElement message_type = form
						.getElementById("vdef1"); // 消息类型
				ds.setValue(
						ds.nameToIndex(notice_contact_type.VDEF1), "");
				message_type.setVisible(false);
			}
			if (notice_style.getValue().equals(IGlobalConstants.NOTICE_STYLE_MESSAGE)) { // 资料消息通知
				// 业务类型
				FormElement business_type_name = form
						.getElementById("ye_type_name"); // 业务类型
				ds.setValue(
						ds.nameToIndex(notice_contact_type.YE_TYPE), "");
				business_type_name.setVisible(false);
//				business_type_name.setNullAble(true);
				// 报告类型
				FormElement report_type_report_type = form
						.getElementById("report_type_report_type"); // 报告类型
				ds.setValue(
						ds.nameToIndex(notice_contact_type.REPORT_TYPE), "");
				report_type_report_type.setVisible(false);
//				report_type_report_type.setNullAble(true);
				// 资料类型
				FormElement data_type_name = form
						.getElementById("data_type_name"); // 资料类型
				ds.setValue(
						ds.nameToIndex(notice_contact_type.DATA_TYPE), "");
				data_type_name.setVisible(true);
//				data_type_name.setNullAble(false);
				// 消息类型
				FormElement message_type = form
						.getElementById("vdef1"); // 消息类型
				ds.setValue(
						ds.nameToIndex(notice_contact_type.VDEF1), "");
				message_type.setVisible(false);
			}
			if (notice_style.getValue().equals(IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE)) { // 消息通知
				// 业务类型
				FormElement business_type_name = form
						.getElementById("ye_type_name"); // 业务类型
				ds.setValue(
						ds.nameToIndex(notice_contact_type.YE_TYPE), "");
				business_type_name.setVisible(false);
//				business_type_name.setNullAble(true);
				// 报告类型
				FormElement report_type_report_type = form
						.getElementById("report_type_report_type"); // 报告类型
				ds.setValue(
						ds.nameToIndex(notice_contact_type.REPORT_TYPE), "");
				report_type_report_type.setVisible(false);
//				report_type_report_type.setNullAble(true);
				// 资料类型
				FormElement data_type_name = form
						.getElementById("data_type_name"); // 资料类型
				ds.setValue(
						ds.nameToIndex(notice_contact_type.DATA_TYPE), "");
				data_type_name.setVisible(false);
//				data_type_name.setNullAble(false);
				// 消息类型
				FormElement message_type = form
						.getElementById("vdef1"); // 消息类型
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
				throw new LfwRuntimeException("业务类型和报告类型必须填入！");
			}
			if (StringUtils.isNotEmpty(ye_type)
					&& StringUtils.isNotEmpty(report_type)) {
				// 查询联系人类型表里面不能有相应的类型存在，如果已经有了，抛message，不让保存！
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
						throw new LfwRuntimeException("相应的（业务类型，报告类型）已经存在不能再次录入！");
					}
				} else if (AppConsts.OPE_EDIT.equals(oper)) {
					if (notice_contact_type_db != null) {
						// 把db中已经存在的常用联系人类型中的业务类型和报告类型两个字段，
						// 和当前联系人类型pk检索出来的那个联系人类型(也就是修改之前的那个)中的业务类型和报告类型两个字段进行比较
						// 如果相同，说明两个字段没有改变，可以正常保存
						// 如果不相同，说明编辑了这两个字段 且数据库中已经存在这种类型的数据了，不能保存
						
						if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_contact_type.getNotice_style()) ||IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_contact_type.getNotice_style())) {
							// 如果原来是资料消息通知或消息通知，那么肯定不能录入
							throw new LfwRuntimeException("相应的（业务类型，报告类型）已经存在不能再次录入！");
						} else if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_contact_type.getNotice_style())) {
							if (notice_contact_type.getYe_type().equals(notice_contact_type_db.getYe_type())
									&& notice_contact_type.getReport_type().equals(notice_contact_type_db.getReport_type())) {
								// 正常情况

							} else {
								throw new LfwRuntimeException("相应的（业务类型，报告类型）已经存在不能再次录入！");
							}
						}
					}
				}

			}
		} else if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_style)) { // 消息通知
			if (StringUtils.isEmpty(data_type)) {
				throw new LfwRuntimeException("资料类型必须填入！");
			}
			if (StringUtils.isNotEmpty(data_type)) {
				// 查询联系人类型表里面不能有相应的类型存在，如果已经有了，抛message，不让保存！
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
						throw new LfwRuntimeException("相应的（资料类型）已经存在不能再次录入！");
					}
				} else if (AppConsts.OPE_EDIT.equals(oper)) {
					if (notice_contact_type_db != null) {
						// 把db中已经存在的常用联系人类型中的资料类型这个字段，
						// 和当前联系人类型pk检索出来的那个联系人类型(也就是修改之前的那个)中的资料类型这个字段进行比较
						// 如果相同，说明两个字段没有改变，可以正常保存
						// 如果不相同，说明编辑了这两个字段 且数据库中已经存在这种类型的数据了，不能保存
						
						if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_contact_type.getNotice_style())||IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_contact_type.getNotice_style())) {
							// 如果原来是填报通知或消息通知，那么肯定不能录入
							throw new LfwRuntimeException("相应的（资料类型）已经存在不能再次录入！");
						} else if (IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_contact_type.getNotice_style())) {
							if (notice_contact_type.getData_type().equals(notice_contact_type_db.getData_type())) {
								// 正常情况

							} else {
								throw new LfwRuntimeException("相应的（资料类型）已经存在不能再次录入！");
							}
						}
					}
				}
			}
		} else if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_style)) { // 消息通知
					if (StringUtils.isEmpty(message_type)) {
						throw new LfwRuntimeException("消息类型必须填入！");
					}
					if (StringUtils.isNotEmpty(message_type)) {
						// 查询联系人类型表里面不能有相应的类型存在，如果已经有了，抛message，不让保存！
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
								throw new LfwRuntimeException("相应的（消息类型）已经存在不能再次录入！");
							}
						} else if (AppConsts.OPE_EDIT.equals(oper)) {
							if (notice_contact_type_db != null) {
								// 把db中已经存在的常用联系人类型中的消息类型这个字段，
								// 和当前联系人类型pk检索出来的那个联系人类型(也就是修改之前的那个)中的消息类型这个字段进行比较
								// 如果相同，说明两个字段没有改变，可以正常保存
								// 如果不相同，说明编辑了这两个字段 且数据库中已经存在这种类型的数据了，不能保存
								if (IGlobalConstants.NOTICE_STYLE_REPORT.equals(notice_contact_type.getNotice_style()) ||IGlobalConstants.NOTICE_STYLE_MESSAGE.equals(notice_contact_type.getNotice_style())) {
									// 如果原来是填报通知，那么肯定不能录入
									throw new LfwRuntimeException("相应的（消息类型）已经存在不能再次录入！");
								} else if (IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(notice_contact_type.getNotice_style())) {
									if (notice_contact_type.getVdef1().equals(notice_contact_type_db.getVdef1())) {
										// 正常情况

									} else {
										throw new LfwRuntimeException("相应的（消息类型）已经存在不能再次录入！");
									}
								}
							}
						}
					}
				}
		// 一个人不能两条都是发送方或接收方的重复数据；
		// 取出联系人详细信息子表的dataset
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
			throw new LfwRuntimeException("以下用户有重复数据：" + errorMessage.substring(0, errorMessage.length()-1) + "! 不能保存。" );
		}
  }
  public void onDataLoad_user(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
	    LfwRuntimeEnvironment.getWebContext().getWebSession().addOriginalParameter("openBillId",null);
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
}
