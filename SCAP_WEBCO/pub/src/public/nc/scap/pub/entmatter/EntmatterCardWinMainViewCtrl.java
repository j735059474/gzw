package nc.scap.pub.entmatter;

import nc.scap.pub.entmatter.wfm.WfmFlwFormVO;
import nc.uap.lfw.core.cmd.UifAddCmd;
import nc.vo.pub.lang.UFDateTime;
import nc.uap.lfw.core.cmd.UifUpdateUIDataCmdRV;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifLineDelCmd;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifSaveCmdRV;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.file.LfwFileConstants;
import java.util.List;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.file.FillFileInfoHelper;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.scap.pub.util.CpCtrlUtil;
import nc.uap.wfm.exe.WfmCmd;
import org.apache.commons.lang.StringUtils;
import nc.uap.lfw.core.uif.delegator.DefaultDataValidator;
import nc.scap.pub.util.CommonOperate;
import java.util.Map;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.cmd.UifCopyCmd;
import java.util.HashMap;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.scap.pub.entmatter.vos.EntmattersVO;
import nc.uap.wfm.utils.WfmCPUtilFacade;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.Dataset;
import java.util.ArrayList;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.cmd.base.CommandStatus;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.vo.pub.SuperVO;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.vo.pub.BusinessException;
import nc.scap.pub.entmatter.util.MatterUtil;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.wfm.utils.WfmTaskUtil;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.lfw.core.constants.AppConsts;
import uap.web.bd.pub.AppUtil;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.scap.pub.util.CpWinUtil;
import uap.lfw.core.locator.ServiceLocator;
import nc.uap.wfm.vo.WfmFormInfoCtx;
import nc.uap.lfw.core.event.MouseEvent;

/**
 * 卡片窗口默认逻辑 reserve1 事项备注
 */
public class EntmatterCardWinMainViewCtrl<T extends WebElement> extends
		AbstractMasterSlaveViewController {
	public static final String OPEN_BILL_ID = "openBillId";

	/**
	 * 页面显示事件
	 *
	 * @param dialogEvent
	 */
	public void beforeShow(DialogEvent dialogEvent) {
		Dataset masterDs = getMasterDs();
		masterDs.clear();

		String oper = getOperator();

		AppUtil.addAppAttr(LfwFileConstants.SYSID,
				LfwFileConstants.SYSID_Default);
		String pro_action = LfwRuntimeEnvironment.getWebContext()
				.getOriginalParameter("pro_action");
		AppUtil.addAppAttr("pro_action", pro_action);
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
					row.setValue(masterDs.nameToIndex("attach"), pk_primarykey);
					FillFileInfoHelper.resetItem(pk_primarykey);
					FillFileInfoHelper.fillFileInfo(masterDs, row);
				}
			});
		} else if (AppConsts.OPE_EDIT.equals(oper)) {
			String currentValue = LfwRuntimeEnvironment.getWebContext()
					.getWebSession().getOriginalParameter("openBillId");
			if (currentValue == null) {
				String value = LfwRuntimeEnvironment.getWebContext()
						.getWebSession().getOriginalParameter("pk_matter");
				LfwRuntimeEnvironment.getWebContext().getWebSession()
						.addOriginalParameter("openBillId", value);
			}
			CmdInvoker.invoke(new UifDatasetLoadCmd(masterDs) {
				@Override
				protected void onAfterDatasetLoad() {
					setDSEnabledByTask(getDs());

					String primaryKey = getDs().getPrimaryKeyField();
					if (primaryKey == null) {
						throw new LfwRuntimeException("当前Dataset没有设置主键!");
					}
					String primaryKeyValue = (String) getDs()
							.getSelectedRow()
							.getValue(getDs().nameToIndex(primaryKey));
					FillFileInfoHelper.resetItem(primaryKeyValue);
				}
			});
		}
		masterDs.getSelectedIndex();
		// masterDs.setRowSelectIndex(masterDs.getRowIndex(masterDs.getSelectedRow()));
		if ("approve".equals(pro_action)) {
			FormComp form = (FormComp) getCurrentView()
					.getViewComponents().getComponent("ent_matters_form");
			// 显示"评价内容"字段
			CpCtrlUtil.visiableFormEles(form, true, new String[] { "def1" });
		} else if ("edit".equals(oper)) {
			FormComp form = (FormComp) getCurrentView()
					.getViewComponents().getComponent("ent_matters_form");
			// 显示"评价内容"字段
			CpCtrlUtil.visiableFormEles(form, false, new String[] { "def1" });

			// report_user:qy这个是企业用户填报事项。
			// 如果没有值得话，就是其他处室填报的（其他处室的节点）。
			String report_user = MatterUtil.getAddressParameter("report_user");
			if (StringUtils.isNotBlank(report_user) && "qy".equals(report_user)) {
				// 隐藏处室字段
				CpCtrlUtil.visiableFormEles(form, false, new String[] {
						"office", "office_name" });
				CpCtrlUtil.setFormNullAble(form, new String[] { "office",
						"office_name" }, true);

				// 让企业名称字段enabled=false
				CpCtrlUtil.setFormEditCol(form, false, new String[] { "pk_org",
						"pk_org_name" });
			}
		}
	}

	/**
	 * 获取任务PK
	 *
	 * @return String
	 */
	private String getPkTask() {
		String pk = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(
				WfmConstants.WfmUrlConst_TaskPk);
		if (pk == null) {
			pk = (String) getCurrentAppCtx().getAppAttribute(
					WfmConstants.WfmUrlConst_TaskPk);
		}
		return pk;
	}

	/**
	 * 根据流程任务设置数据集使用状态
	 *
	 * @param ds
	 */
	private void setDSEnabledByTask(Dataset ds) {
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
	 * 设置PK_ORG字段值
	 *
	 * @param row
	 */
	private void setAutoFillValue(Row row) {
		if (row != null) {
			Dataset ds = getCurrentView().getViewModels()
					.getDataset(this.getMasterDsId());

			/*
			 * String pkOrg =
			 * this.getCurrentAppCtx().getAppEnvironment().getPk_org(); if(pkOrg
			 * != null){ int pkOrgIndex = ds.nameToIndex(PK_ORG); if(pkOrgIndex
			 * >= 0){ row.setValue(pkOrgIndex, pkOrg); } }
			 */
			String pkGroup = getCurrentAppCtx().getAppEnvironment()
					.getPk_group();
			if (pkGroup != null) {
				int pkGroupIndex = ds.nameToIndex(PK_GROUP);
				if (pkGroupIndex >= 0) {
					row.setValue(pkGroupIndex, pkGroup);
				}
			}

			// 填报人
			String pk_user = LfwRuntimeEnvironment.getLfwSessionBean()
					.getPk_user();
			row.setValue(ds.nameToIndex("fill_user"), pk_user);
			// report_user:qy这个是企业用户填报事项。
			// 如果没有值得话，就是其他处室填报的（其他处室的节点）。
			String report_user = MatterUtil.getAddressParameter("report_user");
			if (StringUtils.isNotBlank(report_user) && "qy".equals(report_user)) {
				// 企业名称
				String pk_org = getCurrentAppCtx().getAppEnvironment()
						.getPk_org();
				row.setValue(ds.nameToIndex("pk_org"), pk_org);
				// 隐藏处室字段

				FormComp form = (FormComp) getCurrentView()
						.getViewComponents().getComponent("ent_matters_form");
				CpCtrlUtil.visiableFormEles(form, false, new String[] {
						"office", "office_name" });
				CpCtrlUtil.setFormNullAble(form, new String[] { "office",
						"office_name" }, true);

				// 让企业名称字段enabled=false
				CpCtrlUtil.setFormEditCol(form, false, new String[] { "pk_org",
						"pk_org_name" });
			} else {
				// 填报处室
				String dept = MatterUtil.getCntDept(pk_user);
				row.setValue(ds.nameToIndex("office"), dept);
				// 隐藏处室字段

				FormComp form = (FormComp) getCurrentView()
						.getViewComponents().getComponent("ent_matters_form");
				CpCtrlUtil.setFormNullAble(form, new String[] { "office",
						"office_name" }, false);
			}

			// //显示"评价内容"字段
			FormComp form = (FormComp) getCurrentView()
					.getViewComponents().getComponent("ent_matters_form");
			CpCtrlUtil.visiableFormEles(form, false, new String[] { "def1" });

			// 上报日期
			row.setValue(ds.nameToIndex("rep_date"), new UFDateTime());

			// 功能类型
			row.setValue(ds.nameToIndex("func_type"),
					MatterUtil.getMatterFuncType());

			// 初始化发布状态  0:未发布 1：已发布
			row.setValue(ds.nameToIndex("def2"), "0");
		}
	}

	/**
	 * 主数据选中逻辑
	 *
	 * @param datasetEvent
	 */
	public void onAfterRowSelect(DatasetEvent dsEvent) {
		Dataset ds = dsEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
		ds.setEnabled(true);
		String pk = (String) ds.getValue(ds.getPrimaryKeyField());
		String pro_action = MatterUtil.getAddressParameter("pro_action");
		if ("add".equals(pro_action) || "edit".equals(pro_action)) {
			CpWinUtil.refshAttachList("main", "plugout_winpopattchlist", pk,
					true, true, true);
		} else {
			CpWinUtil.refshAttachList("main", "plugout_winpopattchlist", pk,
					false, true, false);
		}

		if ("detail".equals(pro_action)) {
			ds.setEnabled(false);
		}
		AppLifeCycleContext.current().getApplicationContext()
				.addExecScript("setTimeout(function(){testlu();}, 1);");
	}

	/**
	 * 新增
	 */
	public void onAdd(MouseEvent<?> mouseEvent) throws BusinessException {
		getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_BillID,
				null);
		this.resetWfmParameter();
		getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_FolwTypePk, this.getFlwTypePk());

		CmdInvoker.invoke(new UifAddCmd(this.getMasterDsId()) {
			@Override
			protected void onBeforeRowAdd(Row row) {
				setAutoFillValue(row);
			}
		});
	}

	/**
	 * 打印
	 *
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onPrint(MouseEvent<?> mouseEvent) throws BusinessException {
		Dataset masterDs = getMasterDs();
		Row row = masterDs.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("请选中数据!");
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

	private String getNodeCode() {
		return "配置了打印模板的功能节点的nodecode";
	}

	private String getFlwTypePk() {
		return "0001ZZ100000000JE3Y1";
	}

	private void resetWfmParameter() {
		getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_TaskPk,
				null);
		getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_ScratchPad, null);
		getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE, null);
		getCurrentAppCtx().addAppAttribute(
				WfmConstants.AttachFileList_Temp_Billitem, null);
		getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_ProInsPk, null);
		getCurrentAppCtx().addAppAttribute(WfmConstants.RETURN_PK_TASK,
				null);
		getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_FormInFoCtx_Billitem, null);
	}

	/**
	 * 删除
	 */
	public void onDelete(MouseEvent<?> mouseEvent) throws BusinessException {
		String pk_form = LfwRuntimeEnvironment.getWebContext().getWebSession()
				.getOriginalParameter("openBillId");
		if (pk_form == null) {
			pk_form = LfwRuntimeEnvironment.getWebContext()
					.getWebSession().getOriginalParameter("pk_matter");
			LfwRuntimeEnvironment.getWebContext().getWebSession()
					.addOriginalParameter("openBillId", pk_form);
		}
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
	 * 返回
	 */
	public void onBack(MouseEvent<?> mouseEvent) throws BusinessException {
		getCurrentAppCtx().closeWinDialog();
	}

	/**
	 * 复制
	 */
	public void onCopy(MouseEvent<?> mouseEvent) throws BusinessException {
		getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_BillID,
				null);
		this.resetWfmParameter();
		CmdInvoker.invoke(new UifCopyCmd(this.getMasterDsId()));
	}

	public void doTaskExecute(Map<?, ?> keys) {
		// 平台默认校验
		new DefaultDataValidator().validate(getMasterDs(),
				getCurrentView());
		WfmFormInfoCtx formCtx = this.getWfmFormInfoCtx();
		// 设置流程form
		getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_FormInFoCtx, formCtx);
		// 设置流程类型pk
		getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_FolwTypePk, this.getFlwTypePk());
		// 设置任务pk
		getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_TaskPk,
				this.getPkTask());
		// 调用流程
		CmdInvoker.invoke(new WfmCmd());
		if (CommandStatus.SUCCESS.equals(CommandStatus.getCommandStatus())) {
			CmdInvoker.invoke(new UifUpdateUIDataCmdRV((SuperVO) formCtx,
					getMasterDsId()));

			MatterUtil.forceCloseWindow();

			// 禁止再次修改提交保存
			CommonOperate.afterApprove(getCurrentView());

			// 刷新列表
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put(OPERATE_ROW, null);
			UifPlugoutCmd plugcmd = new UifPlugoutCmd("main",
					"afterSavePlugout", paramMap);
			plugcmd.execute();
		}
	}

	/**
	 * 子表新增
	 */
	public void onGridAddClick(MouseEvent<?> mouseEvent) {
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = getCurrentView().getViewModels().getDataset(dsId);
		Row emptyRow = ds.getEmptyRow();
		ds.addRow(emptyRow);
		ds.setRowSelectIndex(ds.getRowIndex(emptyRow));
		ds.setEnabled(true);
	}

	/**
	 * 子表编辑
	 */
	public void onGridEditClick(MouseEvent<?> mouseEvent) {

	}

	/**
	 * 子表删除
	 */
	public void onGridDeleteClick(MouseEvent<?> mouseEvent) {
		GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		CmdInvoker.invoke(new UifLineDelCmd(dsId));
	}

	@Override
	protected String getMasterDsId() {
		return "ent_matters";
	}

	protected WfmFormInfoCtx getWfmFormInfoCtx() {
		Dataset masterDs = getMasterDs();
		Dataset[] detailDss = getDetailDs(getDetailDsIds());
		SuperVO richVO = getDs2RichVOSerializer().serialize(masterDs,
				detailDss, this.getRichVoClazz());
		return (WfmFormInfoCtx) richVO;
	}

	protected String getRichVoClazz() {
		return WfmFlwFormVO.class.getName();
	}

	public void onSaveClick(MouseEvent<?> mouseEvent) {
		Dataset masterDs = getMasterDs();
		Row savedRow = masterDs.getSelectedRow();
		String riskContent = (String) savedRow.getValue(masterDs
				.nameToIndex(EntmattersVO.MATTER_CONTENT));

		// String riskContent_u;
		// try {
		// riskContent_u = URLDecoder.decode(riskContent,"UTF-8");
		// savedRow.setValue(
		// masterDs.nameToIndex(EntmattersVO.MATTER_CONTENT),
		// riskContent_u);
		// } catch (UnsupportedEncodingException e) {
		// // TODO 自动生成的 catch 块
		// e.printStackTrace();
		// }

		CmdInvoker.invoke(new UifSaveCmdRV(this.getMasterDsId(), getDetailDsIds(), false));

		masterDs.setEnabled(true);

		MatterUtil.forceCloseWindow();

		AppInteractionUtil.showShortMessage("保存成功!");

		Map<String, Object> paramMap = new HashMap<String, Object>(2);

		paramMap.put(OPERATE_ROW, savedRow);
		CmdInvoker.invoke(new UifPlugoutCmd(getCurrentView().getId(),
				"afterSavePlugout", paramMap));
	}
}
