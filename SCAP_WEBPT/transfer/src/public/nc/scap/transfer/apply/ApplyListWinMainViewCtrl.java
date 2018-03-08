package nc.scap.transfer.apply;

import nc.scap.ptpub.method.ScapPtMethod;
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.ScriptEvent;
import nc.uap.wfm.exception.WfmServiceException;
import nc.uap.wfm.facility.WfmServiceFacility;
import nc.scap.pt.vos.ScapptApplyBVO;
import nc.scap.pt.vos.ScapptApplyHVO;
import nc.scap.ptpub.PtConstants;
import nc.scap.pub.util.CpOrgUtil;
import nc.uap.wfm.model.ProDef;
import nc.uap.wfm.model.HumAct;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.bs.dao.DAOException;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.scap.pub.itf.IGlobalConstants;
import nc.uap.lfw.core.model.plug.TranslatedRow;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.file.LfwFileConstants;
import java.util.List;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.wfm.itf.IWfmTaskQry;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.wfm.vo.WfmProdefVO;
import nc.uap.wfm.vo.WfmTaskVO;
import java.util.Iterator;
import nc.scap.pub.util.CntUtil;
import nc.uap.portal.log.ScapLogger;
import nc.uap.lfw.core.cmd.CmdInvoker;
import java.util.Map;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.bs.dao.BaseDAO;
import java.util.HashMap;
import nc.uap.lfw.core.page.LfwView;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.uap.wfm.utils.WfmCPUtilFacade;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.data.Dataset;
import java.util.Map.Entry;
import java.util.ArrayList;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.vo.pub.SuperVO;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.vo.pub.BusinessException;
import nc.scap.pub.util.CpPubMethod;
import nc.uap.wfm.utils.WfmTaskUtil;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.lfw.core.constants.AppConsts;
import uap.web.bd.pub.AppUtil;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.scap.pub.util.CpWinUtil;
import nc.jdbc.framework.processor.ArrayListProcessor;
import uap.lfw.core.locator.ServiceLocator;
import nc.uap.lfw.core.event.MouseEvent;
import nc.scap.pub.util.CpUIctrl;

/**
 * 信息列表默认逻辑
 */
public class ApplyListWinMainViewCtrl<T extends WebElement> extends
		AbstractMasterSlaveViewController {
	private static final String MAIN_VIEW_ID = "main";
	private static final String CARD_WIN_ID = "nc.scap.transfer.applyComps.apply_cardwin";
	private static final String CARD_WIN_TITLE = "编辑";

	/**
	 * 主数据加载逻辑
	 * 
	 * @param dataLoadEvent
	 */
	public void onDataLoad(DataLoadEvent dataLoadEvent) {
		Dataset ds = dataLoadEvent.getSource();
		String curkey = ds.getCurrentKey();
		if (curkey != null) {
			AppUtil.addAppAttr("curkey", curkey);
		}
		ds.setLastCondition(getWheresql(false));
		ds.setExtendAttribute(
				Dataset.ORDER_PART,
				"order by pk_org desc,ts desc,cchangetype desc,ctradetype desc,cgreattype desc ");
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()) {
			protected void changeCurrentKey() {
				if (getDs().getCurrentKey() == null) {
					getDs().setCurrentKey((String) AppUtil.getAppAttr("curkey"));
				}
				return;
			}
		});
	}

	/**
	 * 获取SQL flag 为true时　表示查询模板　false为默认加载
	 * 
	 * @param operationcmd
	 * @return
	 */
	public String getWheresql(boolean flag) {
		String node_type = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter(PtConstants.NODE_TYPE);
		if (node_type == null || "".equals(node_type)) {
			node_type = (String) AppUtil.getAppAttr(PtConstants.NODE_TYPE);
		}
		String pk_Org = AppLifeCycleContext.current().getApplicationContext()
				.getAppEnvironment().getPk_org();
		if (pk_Org == null) {
			AppInteractionUtil.showShortMessage("当前组织未分配职责!");
			return " dr=2";
		}
		String where = " 1=1 " + getFilteByNodeType(node_type, flag);
		if (CpOrgUtil.isCompanyOrg(pk_Org)) {
			where += " and pk_org='" + pk_Org + "'" + "and dr=0 ";
		}

		return where;
	}
	
	

	public String getFilteByNodeType(String node_type, boolean flag) {
		String filtStr = " ";
		String type2 = ScapPtMethod.getNodeType();
		if(type2==null){
			return filtStr;
		}else{
			filtStr+=" and vdef1='"+type2+"' ";
		}
		
		if (PtConstants.PAGECODE_MANAGER.equals(node_type)) {

		} else if (PtConstants.PAGECODE_APPROVE.equals(node_type)) {
			if (flag) {
				filtStr += " and  pk_apply_h in (select pk_frmins from wfm_task where pk_owner='"
						+ CntUtil.getCntUserPk() + "')";
			} else {
				filtStr += " and  pk_apply_h in (select pk_frmins from wfm_task where pk_owner='"
						+ CntUtil.getCntUserPk() + "' and state='State_Run')";
			}
		} else if (PtConstants.PAGECODE_BROWSE.equals(node_type)) {

		}
		return filtStr;
	}

	/**
	 * 主数据选中逻辑
	 * 
	 * @param datasetEvent
	 */
	public void onAfterRowSelect(DatasetEvent datasetEvent) {
		Dataset ds = datasetEvent.getSource();

		Row row = ds.getSelectedRow();
		if (row == null)
			return;
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));
		String formstate = (String) row.getValue(ds
				.nameToIndex(ScapptApplyHVO.FORMSTATE));
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
		setBtnState(pkValue, formstate);
		ScapptApplyHVO[] mainvo = (ScapptApplyHVO[]) new Dataset2SuperVOSerializer<SuperVO>()
				.serialize(ds, row);
		CpPubMethod.resetWfmParameter();
		AppUtil.addAppAttr(WfmConstants.WfmAppAttr_FolwTypePk,
				ScapPtMethod.getNodeCode(mainvo[0]));
		try {
			WfmProdefVO[] proDef = WfmServiceFacility.getProDefQry()
					.getProDefByByWhere(
							"flwtype='"
									+ CpPubMethod.getFlwTypePk(ScapPtMethod
											.getNodeCode(mainvo[0]))
									+ "' and isnotstartup='Y'");
			if (proDef != null && proDef.length > 0) {
				AppUtil.addAppAttr(WfmConstants.WfmAppAttr_ProdefPk,
						proDef[0].getPk_prodef());
			}
		} catch (WfmServiceException e) {
			LfwLogger.error("获取流程定义出错！");
		}
		WfmTaskVO taskvo = null;
		try {
			taskvo = CpPubMethod.getLasterTaskByBillIdAndUser(pkValue,
					CntUtil.getCntUserPk(), "wf");
		} catch (WfmServiceException e) {
			AppInteractionUtil.showShortMessage("亲,当前单据无流程信息哦!");
		}
		if (taskvo != null) {
			Dataset[] approveds = this
					.getDetailDs(new String[] { "approveds" });
			if (approveds != null) {
				approveds[0].clear();
			}
			queryApproveInfo(taskvo.getPk_task(), pkValue);
		}
	}

	/**
	 * 通用审批信息汇总
	 * 
	 * @param taskPK
	 * @param pkValue
	 */
	public void queryApproveInfo(String taskPK, String pkValue) {
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
			sb1.append(" else '' end 信息  ,CU.USER_NAME 审核人 ,WT.ts 日期, WT.SYSEXT8 结果,WT.OPINION 意建  from wfm_task wt left join CP_USER cu on CU.CUSERID = WT.PK_OWNER where 1=1 and nvl(WT.DR,0) = 0 and nvl(CU.DR,0) = 0  and  WT.PK_FRMINS = '"
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
			abits = humacts.entrySet().iterator();// 重置
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
					emptyRow.setValue(dsCh.nameToIndex("info"), objArray[i][0]);// 信息
					emptyRow.setValue(dsCh.nameToIndex("pk_user_name"),
							objArray[i][1]);// 审核人
					emptyRow.setValue(dsCh.nameToIndex("use_date"),
							objArray[i][2]);// 日期
					emptyRow.setValue(dsCh.nameToIndex("result"),
							objArray[i][3]);// 结果
					emptyRow.setValue(dsCh.nameToIndex("suggest"),
							objArray[i][4]);// 意见
					dsCh.addRow(emptyRow);
					// dsCh.setRowSelectIndex(ds.getRowIndex(emptyRow));
				}
			}

		} catch (DAOException e) {
			ScapLogger.error(sb.toString());
			throw new LfwRuntimeException("查询审批信息时出错!");
		}
	}

	/**
	 * 外部触发刷新
	 * 
	 * @param keys
	 */
	public void doRefresh(Map<?, ?> keys) {
		clearDetailDs();
		Dataset ds = this.getMasterDs();
		LfwRuntimeEnvironment.getWebContext().getWebSession()
				.addOriginalParameter("openBillId", null);
		onDataLoad(new DataLoadEvent(ds));
		// TranslatedRow selRow = (TranslatedRow) keys.get(OPERATE_ROW);
		// if (selRow != null) {
		// String sign = this.getOperator();
		// if (AppConsts.OPE_EDIT.equals(sign)) {
		// Row savedRow = ds.getSelectedRow();
		// copyTranslateRow2Row(selRow, savedRow, ds);
		// // }
		// } else if (AppConsts.OPE_ADD.equals(sign)) {
		// Row savedRow = ds.getEmptyRow();
		// savedRow = copyTranslateRow2Row(selRow, savedRow, ds);
		// ds.addRow(savedRow);
		// }
		// }
	}

	private Row copyTranslateRow2Row(TranslatedRow translatedRow, Row row,
			Dataset ds) {
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
	 * 新增
	 * 
	 * @param mouseEvent
	 */
	public void onAdd(MouseEvent<?> mouseEvent) {
		OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_ADD);
		paramMap.put("model", "nc.scap.transfer.model.ApplyEditPageModel");
		CpPubMethod.resetWfmParameter();
		// CpPubMethod
		// .setWfmInfo(paramMap, null, PtConstants.NODECODE_JYSQ, "add");
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
	}

	/**
	 * 新增-流程业务
	 */
	private void onAdd_wfm() {
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_BillID,
				null);
		this.resetWfmParameter();
		this.getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_FolwTypePk, this.getFlwTypePk());
	}

	/**
	 * 打开页面与当前页面属于同一app范围，每次需要清除app中的变量
	 */
	private void resetWfmParameter() {
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
	 * 获得单据类型PK
	 * 
	 * @return
	 */
	private String getFlwTypePk() {
		return "0001ZG100000000EPTAX";
	}

	/**
	 * 编辑
	 * 
	 * @param scriptEvent
	 */
	public void onEdit(MouseEvent mouseEvent) {
		Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待编辑数据");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		paramMap.put(WfmConstants.WfmUrlConst_billID, pkValue);
		paramMap.put("model", "nc.scap.transfer.model.ApplyEditPageModel");
		ScapptApplyHVO[] mainvo = (ScapptApplyHVO[]) new Dataset2SuperVOSerializer<SuperVO>()
				.serialize(ds, row);
		CpPubMethod.setWfmInfo(paramMap, pkValue,
				ScapPtMethod.getCodeByNodetype(), "edit");
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
	}

	/**
	 * 编辑-流程业务
	 * 
	 * @param pkValue
	 */
	private void onEdit_wfm(String pkValue) {
		this.resetWfmParameter();
		try {
			String pk_user = LfwRuntimeEnvironment.getLfwSessionBean()
					.getPk_user();
			IWfmTaskQry taskQry = ServiceLocator.getService(IWfmTaskQry.class);
			WfmTaskVO task = taskQry.getLastTaskVOByFormPkAndUserPk(pkValue,
					pk_user);
			if (task != null) {
				this.getCurrentAppCtx().addAppAttribute(
						WfmConstants.WfmAppAttr_TaskPk, task.getPk_task());
			}
		} catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
	}

	/**
	 * 删除
	 * 
	 * @param scriptEvent
	 */
	public void onDelete(MouseEvent mouseEvent) {
		Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待删除数据");
		}

		Row row = ds.getSelectedRow();
		String pk_form = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));
		if (pk_form != null && !pk_form.equals("")) {
			boolean isCanDel = WfmCPUtilFacade.isCanDelBill(pk_form);
			if (isCanDel) {
				WfmCPUtilFacade.delWfmInfo(pk_form);
				CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
			} else {
				throw new LfwRuntimeException("流程已启动，无法删除单据");
			}

		} else {
			throw new LfwRuntimeException("未获取到流程单据主键值");
		}
	}

	/**
	 * 流程进度
	 * 
	 * @param mouseEvent
	 */
	public void onFlow(MouseEvent<?> mouseEvent) {
		Dataset masterDs = this.getMasterDs();
		Row row = masterDs.getSelectedRow();
		String pk_form = (String) row.getValue(masterDs.nameToIndex(masterDs
				.getPrimaryKeyField()));
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_BillID,
				pk_form);
		WfmUtilFacade.openFlowImage();
	}

	/**
	 * 启用
	 * 
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onStart(MouseEvent<?> mouseEvent) throws BusinessException {
		CmdInvoker.invoke(new UifEnableCmd(getMasterDsId(), true));
	}

	/**
	 * 停用
	 * 
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onStop(MouseEvent<?> mouseEvent) throws BusinessException {
		CmdInvoker.invoke(new UifEnableCmd(getMasterDsId(), false));
	}

	/**
	 * 附件
	 * 
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onAttchFile(MouseEvent<?> mouseEvent) {
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
	public void onPrint(MouseEvent<?> mouseEvent) {
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
	 * 主组织变化
	 */
	public void doOrgChange(Map keys) {
		// CmdInvoker.invoke(new UifDatasetLoadCmd(getMasterDsId()) {
		// protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
		// // String where = AppUtil.getGroupOrgFielterSqlForDesign(false,
		// // "pk_org");
		// // ds.setLastCondition(where);
		// String where = null;
		// return where;
		// }
		//
		// // 避免翻页时重走缓存
		// protected void changeCurrentKey() {
		// setChangeCurrentKey(true);
		// }
		// });
		// clearDetailDs();
	}

	private void clearDetailDs() {
		Dataset[] detailDss = this.getDetailDs(this.getDetailDsIds());
		int len = detailDss != null ? detailDss.length : 0;
		for (int i = 0; i < len; i++) {
			if (detailDss[i] != null) {
				detailDss[i].clear();
			}
		}
	}

	/**
	 * 查询pluguin
	 * 
	 * @param keys
	 */
	public void doQueryChange(Map keys) {
		FromWhereSQL whereSql = (FromWhereSQL) keys
				.get(FromWhereSQL.WHERE_SQL_CONST);
		String where = whereSql.getWhere();
		where += " and " + getWheresql(true);
		CmdInvoker.invoke(new QueryCmd(MAIN_VIEW_ID, getMasterDsId(), where));
	}

	@Override
	protected String getMasterDsId() {
		return "scappt_apply_h";
	}

	/**
	 * 设置按钮状态
	 * 
	 * @param pk
	 * @param formstate
	 */
	public void setBtnState(String pk, String formstate) {
		LfwView view = CpWinUtil.getView();
		if (formstate == null) {
			return;
		}
		if (formstate.equals(IGlobalConstants.SCAPPM_BILLSTATE_RUN)) {
			CpUIctrl.showMenuBar(view, "menubar",
					new String[] { "edit", "del" });
		} else if (formstate.equals(IGlobalConstants.SCAPPM_BILLSTATE_END)) {
			CpUIctrl.showMenuBar(view, "menubar", new String[] { "edit", "del",
					"approve" });
		} else if (formstate
				.equals(IGlobalConstants.SCAPPM_BILLSTATE_NOTTSTART)) {
			CpUIctrl.showMenuBar(view, "menubar", null);
			boolean flag = false;
			try {
				flag = WfmCPUtilFacade.isCanDelBill(pk);
			} catch (Exception e) {
				flag = false;
			}
			if (!flag) {
				CpUIctrl.showMenuBar(view, "menubar", new String[] { "del" });
			}
		} else if (formstate
				.equals(IGlobalConstants.SCAPPM_BILLSTATE_CANCELLATION)) {
			CpUIctrl.hideMenuBar(view, "menubar", new String[] { "add", "look",
					"wf" });
		} else {
			CpUIctrl.showMenuBar(view, "menubar", null);
		}
	}

	public void onLook(MouseEvent mouseEvent) {
		Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待查看数据");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(CARD_WIN_ID, "查看");
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, "look");
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		paramMap.put(WfmConstants.WfmUrlConst_billID, pkValue);
		paramMap.put("model", "nc.scap.transfer.model.ApplyEditPageModel");
		props.setParamMap(paramMap);

		this.onEdit_wfm(pkValue);

		this.getCurrentAppCtx().navgateTo(props);
	}

	public void onApprove(MouseEvent mouseEvent) {
		Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待审批数据");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(CARD_WIN_ID, "审批");
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, "approve");
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		paramMap.put(WfmConstants.WfmUrlConst_billID, pkValue);
		paramMap.put("model", "nc.scap.transfer.model.ApplyEditPageModel");
		ScapptApplyHVO[] mainvo = (ScapptApplyHVO[]) new Dataset2SuperVOSerializer<SuperVO>()
				.serialize(ds, row);
		CpPubMethod.setWfmInfo(paramMap, pkValue,
				ScapPtMethod.getCodeByNodetype(), "edit");
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
	}

	public void onAttach_id(MouseEvent mouseEvent) {
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
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

		String taskPk = WfmUtilFacade.getTaskPkPkByPkFormIns(primaryKeyValue);
		// 如果当前人不是当前单据的参与者,此时取不到任务.附件只能查看,需要设为浏览态.
		if (taskPk == null || taskPk.equals("")) {
			this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE,
					WfmConstants.BILLSTATE_BROWSE);
		}
		AppUtil.addAppAttr(LfwFileConstants.SYSID,
				LfwFileConstants.SYSID_BAFILE);
		// 流程附件参数
		Map<String, String> wfmParam = WfmUtilFacade
				.getFileMgrParamsByTask(taskPk);

		// 附件参数
		Map<String, String> param = UploadFileHelper.BuildDefaultPamater(
				LfwFileConstants.SYSID_BAFILE, primaryKeyValue,
				CommonObjectConstants.AttachFileType, "");
		param.put("usescanable", "true");
		param.put("state", String.valueOf(31));

		// pos 指定跳转到哪个类　2014-6-16
		param.put(LfwFileConstants.Filemgr_Para_OperateClazz,
				"nc.scap.transfer.fileback.TraFileBackImp");

		String title = "附件";
		// if (wfmParam != null && !wfmParam.isEmpty()) {
		// param.putAll(wfmParam);
		// }

		CmdInvoker.invoke(new UifAttachCmd(title, param));
	}

	public void onDetail(ScriptEvent mouseEvent) {
		String oper = "look";
		String title = "查看";
		Dataset ds = this.getCurrentView().getViewModels()
				.getDataset("scappt_apply_b");
		Dataset mainds = this.getCurrentView().getViewModels()
				.getDataset("scappt_apply_h");
		String mainpk = (String) mainds.getValue(mainds.getPrimaryKeyField());
		// 产权树　转让方PK
		String pk_rorg = (String) mainds.getValue(ScapptApplyHVO.PK_RORG);
		Row row = ds.getSelectedRow();
		// 产权树　标地企业PK
		String pk = (String) row.getValue(ds
				.nameToIndex(ScapptApplyBVO.PK_BORG));
		// 根据条件　查询是否发生产权交易
		String billpk = "" ;
		String node_type = (String) AppUtil.getAppAttr(PtConstants.NODE_TYPE2);
//		billpk = ScapPtMethod.getBillPk(mainpk, pk);
		

		//土地资产处置查询是否发生交易
		if (PtConstants.TDCZ.equals(node_type)){
			billpk = ScapPtMethod.getBillPkland(mainpk, pk);
		}else {
			// 根据条件　查询是否发生产权交易
			billpk = ScapPtMethod.getBillPk(mainpk, pk);
		}
		
		if(billpk==null){
			AppInteractionUtil.showShortMessage("请完善交易申请！");
			return;
		}
		
		// 获取转让方产权登记信息
		String rorg = ScapPtMethod.getFirmBaseHVOByTreePk(pk_rorg);
		if (rorg == null) {
			AppInteractionUtil.showShortMessage("转让方未登记产权信息!");
			return;
		}
		ScapPtMethod.onLink(ScapPtMethod.getWinByType(), title, oper, billpk,
				pk, rorg, mainpk);
	}
}
