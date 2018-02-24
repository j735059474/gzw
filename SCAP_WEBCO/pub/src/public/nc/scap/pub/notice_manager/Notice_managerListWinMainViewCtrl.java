package nc.scap.pub.notice_manager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.model.ScapCoConstants;
import nc.scap.pub.notice.util.NoticeUtil;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.ScapDAO;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifDelCmdRVNoValidator;
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
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.portal.log.ScapLogger;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.scapco.work_notice_contacts.notice_contact_type;
import nc.vo.scapco.work_notice_manager.Notice_manager;
import nc.vo.scapco.work_notice_manager.Receive_man;
import nc.vo.scapco.work_notice_manager.Receive_notice;

import org.apache.commons.lang.StringUtils;

import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import uap.web.bd.pub.AppUtil;
/** 
 * 信息列表默认逻辑
 */
public class Notice_managerListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="nc.scap.pub.notice.notice_manager_cardwin";
  private static final String CARD_WIN_TITLE="编辑";
  private static final String PLUGOUT_ID="afterSavePlugout";
  /** 
 * 主数据加载逻辑
 * @param dataLoadEvent
 */
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
		//国资委用户和企业用户都需要过滤
		String lastCondition = ds.getLastCondition();
		String tzxf_notice_style = (String) AppUtil.getAppAttr(IGlobalConstants.TZXF_NOTICE_STYLE);
		if (lastCondition == null) {
			String whereSql = "";
			if (StringUtils.isNotBlank(tzxf_notice_style) && IGlobalConstants.NOTICE_STYLE_NORMAL_MESSAGE.equals(tzxf_notice_style)) {
				whereSql = getWheresqlByMessageType();
			} else {
				whereSql = getWheresql();
			}
			ds.setLastCondition(whereSql);
			ds.setOrderByPart(" order by notice_date desc ");
		}
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()) {
			 @Override
			 protected void onAfterDatasetLoad() {
				 String pageCode = (String) LfwRuntimeEnvironment.getWebContext()
					.getOriginalParameter("pagecode");
				 Dataset ds = this.getDs();
				 Row[] mrows = ds.getAllRow(); 
				 if (ScapCoConstants.PAGECODE_BROWSE.equals(pageCode)) {// 查看页面
						for (Row mrow : mrows) {
							String pkUser = CntUtil.getCntUserPk();
							String name = NoticeUtil.getNameByPkUser(pkUser);
							// 用户名字替换
							String notice_content = (String) mrow.getValue(ds
									.nameToIndex(Notice_manager.NOTICE_CONTENT));
							if (notice_content != null && StringUtils.isNotEmpty(notice_content)) {
								mrow.setValue(
										ds.nameToIndex(Notice_manager.NOTICE_CONTENT),
										notice_content.replace(IGlobalConstants.CONTENT_REPLACE_USERNAME,
												name));
							}

							String remind_info = (String) mrow.getValue(ds
									.nameToIndex(Notice_manager.REMIND_INFO));
							if (remind_info != null && StringUtils.isNotEmpty(remind_info)) {
								mrow.setValue(
										ds.nameToIndex(Notice_manager.REMIND_INFO),
										remind_info.replace(IGlobalConstants.CONTENT_REPLACE_USERNAME,
												name));
							}

							String urge_content_qy = (String) mrow.getValue(ds
									.nameToIndex(Notice_manager.URGE_CONTENT_QY));
							if (urge_content_qy != null && StringUtils.isNotEmpty(urge_content_qy)) {
								mrow.setValue(
										ds.nameToIndex(Notice_manager.URGE_CONTENT_QY),
										urge_content_qy.replace(IGlobalConstants.CONTENT_REPLACE_USERNAME,
												name));
							}

						} 
				 }

			 }
		});
  }
  /** 
 * 主数据选中逻辑
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
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
  /** 
 * 新增
 * @param mouseEvent
 */
  public void onAdd(  MouseEvent mouseEvent){
    //	  // test autoplugin start
//	  AutoNoticeTimerPlugin plugin = new AutoNoticeTimerPlugin();
//	  CurrEnvVO context = new CurrEnvVO();
//	  try {
//		  plugin.executeTask(context);
//	  } catch (BusinessException e) {
//		  
//	  }
//	  // test autoplugin end
    OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_ADD);
		paramMap.put("pro_action", ScapCoConstants.ADD);
		paramMap.put("model", "nc.scap.pub.model.NoticeCardPageModel");
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * 编辑
 * @param scriptEvent
 */
  public void onEdit(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待编辑数据!");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));
		// 已下发通知不得修改
		// 取得通知下发状态
		String notice_status = (String) row.getValue(ds
				.nameToIndex(Notice_manager.NOTICE_STATUS));
		if ("2".equals(notice_status)) {
			throw new LfwRuntimeException("已下发通知不能修改!");
		}

		OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		paramMap.put("pro_action", ScapCoConstants.EDIT);
		paramMap.put("model", "nc.scap.pub.model.NoticeCardPageModel");
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * 删除
 * @param scriptEvent
 */
  public void onDelete(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待删除数据!");
		}

		Row row = ds.getSelectedRow();
		// 已下发通知不得删除
		// 取得通知下发状态
		String notice_status = (String) row.getValue(ds
				.nameToIndex(Notice_manager.NOTICE_STATUS));
		if ("2".equals(notice_status)) {
			throw new LfwRuntimeException("已下发通知不能删除!");
		}
		CmdInvoker.invoke(new UifDelCmdRVNoValidator(this.getMasterDsId()));
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
		uap.web.bd.pub.AppUtil.addAppAttr(LfwFileConstants.SYSID,
				LfwFileConstants.SYSID_BAFILE);
		String primaryKey = ds.getPrimaryKeyField();
		if (primaryKey == null) {
			throw new LfwRuntimeException("当前Dataset没有设置主键!");
		}
		String primaryKeyValue = (String) row.getValue(ds
				.nameToIndex(primaryKey));

		String taskPk = WfmUtilFacade.getTaskPkPkByPkFormIns(primaryKeyValue);
		// 如果当前人不是当前单据的参与者,此时取不到任务.附件只能查看,需要设为浏览态.
		if (taskPk == null || taskPk.equals("")) {
			this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE,
					WfmConstants.BILLSTATE_BROWSE);
		}

		// 流程附件参数
		Map<String, String> wfmParam = WfmUtilFacade
				.getFileMgrParamsByTask(taskPk);

		// 附件参数
		Map<String, String> param = UploadFileHelper.BuildDefaultPamater("",
				primaryKeyValue, CommonObjectConstants.AttachFileType, "");
		param.put("usescanable", "true");
		param.put("state", String.valueOf(31));

		String title = "附件";
		if (wfmParam != null && !wfmParam.isEmpty()) {
			param.putAll(wfmParam);
		}

		CmdInvoker.invoke(new UifAttachCmd(title, param));
  }
  /** 
 * 打印
 */
  public void onPrint(  MouseEvent mouseEvent){
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
 * 外部触发刷新
 * @param keys
 */
  public void doRefresh(  Map<?,?> keys){
    Row savedRow = (Row) keys.get(OPERATE_ROW);
		if (savedRow != null) {
			Dataset ds = getMasterDs();
			savedRow.setRowId(ds.getEmptyRow().getRowId());
			// 标识是否编辑
			boolean isEdit = false;
			// 当前页所有选中行
			Row[] selRows = ds.getSelectedRows();
			int len = selRows != null ? selRows.length : 0;
			if (len > 0) {
				int pkIndex = ds.nameToIndex(ds.getPrimaryKeyField());
				for (int i = 0; i < len; i++) {
					if (selRows[i] == null
							|| selRows[i].getValue(pkIndex) == null) {
						continue;
					} // PK值相同,父页面更新数据.
					if (selRows[i].getValue(pkIndex).equals(
							savedRow.getValue(pkIndex))) {
						isEdit = true;
						int index = ds.getRowIndex(selRows[i]);
						if (index >= 0) {
							ds.removeRow(index);
							ds.insertRow(index, savedRow);
							ds.setRowSelectIndex(index);
						}
						break;
					}
				}
			}
			if (!isEdit) {
				int pageSize = ds.getPageSize();
				if (pageSize <= 0) {
					if (ds.getCurrentRowSet() != null
							&& ds.getCurrentRowSet().getPaginationInfo() != null) {
						pageSize = ds.getCurrentRowSet().getPaginationInfo()
								.getPageSize();
					}
				}
				if (pageSize > 0 && ds.getCurrentRowData() != null
						&& ds.getCurrentRowData().getRowCount() >= pageSize) {
					ds.removeRow(ds.getCurrentRowData().getRowCount() - 1);
					ds.insertRow(0, savedRow);
					ds.setRowSelectIndex(0);
				} else {
					ds.insertRow(0, savedRow);
					ds.setRowSelectIndex(0);
				}
			}
		}
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
 * 主组织变化
 * @param keys
 */
  public void doOrgChange(  Map<?,?> keys){
    CmdInvoker.invoke(new UifDatasetLoadCmd(this.getMasterDsId()) {
			protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
				String where = AppUtil.getGroupOrgFielterSqlForDesign(false,
						"pk_org");
				ds.setLastCondition(where);
				return where;
			}

			// 避免翻页时重走缓存
			protected void changeCurrentKey() {
				setChangeCurrentKey(true);
			}
		});
		this.clearDetailDs();
  }
  /** 
 * 查询plugin
 * @param keys
 */
  public void doQueryChange(  Map<?,?> keys){
    FromWhereSQL whereSql = (FromWhereSQL) keys
				.get(FromWhereSQL.WHERE_SQL_CONST);
		CmdInvoker.invoke(new QueryCmd(MAIN_VIEW_ID, this.getMasterDsId(),
				whereSql.getWhere() + " and " + getWheresql()));
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
  @Override protected String getMasterDsId(){
    return "notice_manager";
  }
  public void onLookClick(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待查看数据");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		String node_type = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter(ScapCoConstants.NODE_TYPE);
		if (node_type == null)
			node_type = (String) AppLifeCycleContext.current()
					.getApplicationContext()
					.getAppAttribute(ScapCoConstants.NODE_TYPE);

		OpenProperties props = new OpenProperties(CARD_WIN_ID, "查看");
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		paramMap.put("method_type", "look");
		 String pageCode = (String) LfwRuntimeEnvironment.getWebContext()
			.getOriginalParameter("pagecode");
		 if (ScapCoConstants.PAGECODE_MANAGER.equals(pageCode)) {// 管理页面(查看按钮)
			paramMap.put("pro_action", "detail_gzw");
		 } else if (ScapCoConstants.PAGECODE_BROWSE.equals(pageCode)) {// 查看页面（查看画面）
			paramMap.put("pro_action", ScapCoConstants.DETAIL);
		 }
		paramMap.put("node_type", node_type);
		paramMap.put("model", "nc.scap.pub.model.NoticeCardPageModel");
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  public void onNoticeIssueclick(  MouseEvent mouseEvent){
    Dataset masterDs = getMasterDs();
	LfwView view = AppLifeCycleContext.current().getWindowContext()
			.getViewContext("main").getView();
	Dataset receive_noticeds = view.getViewModels().getDataset(
			"receive_notice");
  	Dataset receive_mands = view.getViewModels().getDataset("receive_man");
		Row row = masterDs.getSelectedRow();
		String pk_notice = row.getString(masterDs
				.nameToIndex(Notice_manager.PK_NOTICE));
		String notice_style = row.getValue(masterDs
				.nameToIndex(Notice_manager.NOTICE_STYLE)) == null ? ""
				: row.getValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_STYLE))
						.toString();
		String business_type = row.getValue(masterDs
				.nameToIndex(Notice_manager.BUSINESS_TYPE)) == null ? ""
				: row.getValue(
						masterDs.nameToIndex(Notice_manager.BUSINESS_TYPE))
						.toString();
		String notice_type = row.getValue(masterDs
				.nameToIndex(Notice_manager.NOTICE_TYPE)) == null ? ""
				: row.getValue(
						masterDs.nameToIndex(Notice_manager.NOTICE_TYPE))
						.toString();
		if (pk_notice == null) {
			throw new LfwRuntimeException("请选择相应的通知!");
		}
		// 已下发通知不得修改
		// 取得通知下发状态
		String notice_status = (String) row.getValue(masterDs
				.nameToIndex(Notice_manager.NOTICE_STATUS));
		if ("2".equals(notice_status)) {
			throw new LfwRuntimeException("已下发通知不能再次下发!");
		}
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
			try {
				ScapDAO.getBaseDAO().executeUpdate(
						"update scapco_notice_manager set NOTICE_STATUS = '2' where PK_NOTICE = '"
								+ pk_notice + "'");
				AppInteractionUtil.showShortMessage("更新成功！");
			} catch (DAOException e) {
				ScapLogger.error("更新操作出现错误！错误异常：" + e.getMessage());
			}
		}
		// 提醒功能相关
		NoticeUtil.remindByNoticePK(pk_notice);
		
		// 更新list页面
		onDataLoad(new DataLoadEvent(masterDs));
  }
  public void onUrgeClick(  MouseEvent mouseEvent){
    Dataset masterDs = this.getMasterDs();
		Row selectedRow = masterDs.getSelectedRow();
		if (masterDs.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待催报的通知!");
		}
		String pk_notice = selectedRow.getString(masterDs
				.nameToIndex(Notice_manager.PK_NOTICE));
		if (pk_notice == null) {
			throw new LfwRuntimeException("请选择相应的通知!");
		}
		// 调用共同的函数 进行具体催报工作
		NoticeUtil.urgeByNoticePK(pk_notice);
		// 催报消息发送成功提示消息
		AppInteractionUtil.showShortMessage("催报消息发送成功");
  }
  /** 
 * 获取SQL
 * @param
 * @return
 */
  public String getWheresql(){
    StringBuffer whereSql = new StringBuffer();

		String pkUser = CntUtil.getCntUserPk();
		String pkOrg = CntUtil.getCntOrgPk();
		List<String> pkVisOrgList = NoticeUtil.getAllPKVisualorgByPkUser(pkUser);
		String orgSql = " b.pk_org = '" + pkOrg + "'";
		if (pkVisOrgList != null && pkVisOrgList.size() > 0) {
			String visOrgSql = NoticeUtil.getSqlStrByList(pkVisOrgList,1000,"b.PK_VISUALORG");
			orgSql = orgSql + " or (" + visOrgSql + ")";
		}
		String businessType = "";
		String noticeType = "";
		String data_type = "";
		try {
			List<notice_contact_type> contact_typeList = new ArrayList<notice_contact_type>();
			String sql = " select a.* from scapco_notice_contact_type a inner join scapco_notice_contact_info b "
					+ " on a.pk_contact_type = b.pk_contact_type "
					+ " where  b.pk_user ='"
					+ pkUser
					+ "' and b.dr = '0' "
					+ " and a.dr = '0'  and a.enablestate = '2' "
					+ " and (" + orgSql + ")";
			contact_typeList = (List<notice_contact_type>) ScapDAO.getBaseDAO()
					.executeQuery(sql,
							new BeanListProcessor(notice_contact_type.class));
			for (int i = 0; i < contact_typeList.size(); i++) {
				notice_contact_type contact_type = contact_typeList.get(i);
				// 拼接 业务类型
				if (contact_type.getYe_type() != null) {
					businessType = businessType + "'"
							+ contact_type.getYe_type() + "'";
					businessType = businessType + ",";
				}
				// 拼接报告类型
				if (contact_type.getReport_type() != null) {
					noticeType = noticeType + "'"
							+ contact_type.getReport_type() + "'";
					noticeType = noticeType + ",";
				}
				// 拼接资料类型
				if (contact_type.getData_type() != null) {
					data_type = data_type + "'" + contact_type.getData_type()
							+ "'";
					data_type = data_type + ",";
				}
			}
		} catch (DAOException e) {
			ScapLogger.error("查询操作出现错误！错误异常：" + e.getMessage());
		}
//		// 根据节点注册的参数 重置业务类型和报告类型(只会有一个业务类型和一个报告类型)
//		String businessTypeFun = (String) LfwRuntimeEnvironment.getWebContext()
//				.getOriginalParameter("business_type");
//		String reportTypeFun = (String) LfwRuntimeEnvironment.getWebContext()
//				.getOriginalParameter("report_type");
//		String dataTypeFun = (String) LfwRuntimeEnvironment.getWebContext()
//				.getOriginalParameter("data_type");
//		if (StringUtils.isNotBlank(businessTypeFun)) {
//			businessType = "";
//			String businessTypeCode = businessTypeFun.substring(1,
//					businessTypeFun.length() - 1);
//			uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE_FUNCNODE, businessTypeCode);
//			businessType = businessType + "'"
//					+ NoticeUtil.getBusinessTypePKByCode() + "'";
//			businessType = businessType + ",";
//		} else {
//			
//		}
//		if (StringUtils.isNotBlank(reportTypeFun)) {
//			noticeType = "";
//			String reportTypeCode = reportTypeFun.substring(1,
//					reportTypeFun.length() - 1);
//			if (StringUtils.isNotBlank(businessTypeFun)) {
//				String businessTypeCode = businessTypeFun.substring(1,
//						businessTypeFun.length() - 1);
//				uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE_FUNCNODE, businessTypeCode);
//				String noticeTypePK = NoticeUtil.getNoticeTypePKByBusinessTypeAndCode(NoticeUtil.getBusinessTypePKByCode(), reportTypeCode);
//				if (StringUtils.isNotBlank(noticeTypePK)) {
//					noticeType = noticeType + "'"
//							+ noticeTypePK + "'";
//					noticeType = noticeTypePK + ",";
//				}
//			}
//		}
//		if (StringUtils.isNotBlank(dataTypeFun)) {
//			data_type = "";
//			String dataTypeFunCode = dataTypeFun.substring(1,
//					dataTypeFun.length() - 1);
//			String data_typePK = NoticeUtil.getDataTypePKByCode(dataTypeFunCode);
//			if (StringUtils.isNotBlank(data_typePK)) {
//				data_type = data_type + "'"
//						+ data_typePK + "'";
//				data_type = data_type + ",";
//			}
//		}
		// 根据业务类型和报告类型 查询相关的填报通知
		whereSql.append(" 1=1 ");
		if (data_type != "") {
			data_type = data_type.substring(0, data_type.length() - 1);
			if (businessType != "" && noticeType != "") {
				businessType = businessType.substring(0, businessType.length() - 1);
				noticeType = noticeType.substring(0, noticeType.length() - 1);
				whereSql.append(" and ((BUSINESS_TYPE in(").append(businessType)
						.append(")").append(" and NOTICE_TYPE in(")
						.append(noticeType).append("))");
				whereSql.append(" or (DATA_TYPE in(").append(data_type).append(")))");
			}
		} else {
			if (businessType != "" && noticeType != "") {
				businessType = businessType.substring(0, businessType.length() - 1);
				noticeType = noticeType.substring(0, noticeType.length() - 1);
				whereSql.append(" and (BUSINESS_TYPE in(").append(businessType)
						.append(")").append(" and NOTICE_TYPE in(")
						.append(noticeType).append("))");
			}
		}
		uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE_LIANXIREN, businessType);
		uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_TYPE_LIANXIREN, noticeType);
		uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_DATA_TYPE_LIANXIREN, data_type);
		String pageCode = getPageCode();
		// 企业或组织中的通知发送用户能够看见自己下发的通知
		if (ScapCoConstants.PAGECODE_MANAGER.equals(pageCode)) {// 管理页面
			whereSql.append(" and (NOTICE_PSN = '").append(pkUser).append("' or NOTICE_PSN = '~') ");
			// 企业或组织中的通知接收用户能够看到自己所属的所有企业和组织的应该接受的通知。	
		} else if (ScapCoConstants.PAGECODE_BROWSE.equals(pageCode)) {// 查看页面
			String orgSqlTemp = " b.NOTICE_ORG = '" + pkOrg + "'";
			if (pkVisOrgList != null && pkVisOrgList.size() > 0) {
				String visOrgSql = NoticeUtil.getSqlStrByList(pkVisOrgList,1000,"b.NOTICE_VISUAL_ORG");
				orgSqlTemp = orgSqlTemp + " or (" + visOrgSql + ")";
			}
//			// 当前用户所在的企业必须已经接收到这个通知，才能检索出来
//			whereSql.append(" and exists (select * from scapco_receive_notice b where b.pk_notice = scapco_notice_manager.pk_notice ").append(" and (" + orgSqlTemp + ")").append(")");
			// 当前用户必须已经接收到这个通知，才能检索出来
			whereSql.append(" and exists (select * from scapco_receive_man t where t.pk_notice = scapco_notice_manager.pk_notice and t.receive_man = '").append(pkUser).append("')");
			// 企业只能看到已下发的通知的数据
			whereSql.append(" and NOTICE_STATUS = '2'");
		}
		return whereSql.toString();
  }
  /** 
 * 获取消息通知的sql句
 * @param
 * @return
 */
  public String getWheresqlByMessageType(){
    StringBuffer whereSql = new StringBuffer();

		String pkUser = CntUtil.getCntUserPk();
		String pkOrg = CntUtil.getCntOrgPk();

		// 根据业务类型和报告类型 查询相关的填报通知
		whereSql.append(" 1=1 ");
		String pageCode = getPageCode();
		String tzxf_message_type_code = (String) AppUtil.getAppAttr(IGlobalConstants.TZXF_MESSAGE_TYPE_CODE);
		if (StringUtils.isNotBlank(tzxf_message_type_code)) {
			tzxf_message_type_code = tzxf_message_type_code.replace("{", "'").replace("}", "'").replace(",", "','");
		} else {
			tzxf_message_type_code = "'" + IGlobalConstants.STANDARD_MESSAGE_TYPE_CODE + "'";
		}
		// 需要合并上已经维护联系人的消息类型，即使没有设置这个消息类型的参数
		List<String> existedMessageType = NoticeUtil.selectMessageTypeByPkUser(pkUser);
		if (existedMessageType != null && existedMessageType.size() > 0) {
			for (String type : existedMessageType) {
				tzxf_message_type_code = tzxf_message_type_code + ",'" + type + "'";
			}
		}
		whereSql.append(" and def4 in (").append(tzxf_message_type_code).append(") ");
		// 企业或组织中的通知发送用户能够看见自己下发的通知
		if (ScapCoConstants.PAGECODE_MANAGER.equals(pageCode)) {// 管理页面
			whereSql.append(" and NOTICE_PSN = '").append(pkUser).append("' ");
			// 企业或组织中的通知接收用户能够看到自己所属的所有企业和组织的应该接受的通知。	
		} else if (ScapCoConstants.PAGECODE_BROWSE.equals(pageCode)) {// 查看页面
			// 当前用户必须已经接收到这个通知，才能检索出来
			whereSql.append(" and exists (select * from scapco_receive_man t where t.pk_notice = scapco_notice_manager.pk_notice and t.receive_man = '").append(pkUser).append("')");
			// 企业只能看到已下发的通知的数据
			whereSql.append(" and NOTICE_STATUS = '2'");
		}
		return whereSql.toString();
  }
  /** 
 * 获取页面编码
 * @return
 */
  protected String getPageCode(){
    return (String) LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("pagecode");
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
