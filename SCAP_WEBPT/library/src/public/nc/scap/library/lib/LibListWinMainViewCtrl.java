package nc.scap.library.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.scap.library.vos.LibraryVO;
import nc.scap.pub.itf.IGlobalConstants;
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
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.portal.log.ScapLogger;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.dbl.uiengine.CommonObjectConstants;

/**
 * 信息列表默认逻辑
 */
public class LibListWinMainViewCtrl<T extends WebElement> extends
		AbstractMasterSlaveViewController {
	private static final long serialVersionUID = -1;
	private static final String MAIN_VIEW_ID = "main";
	private static final String CARD_WIN_ID = "libraryComps.lib_cardwin";
	private static final String CARD_WIN_TITLE = "编辑";

	/**
	 * 主数据选中逻辑
	 * 
	 * @param datasetEvent
	 */
	public void onAfterRowSelect(DatasetEvent datasetEvent) {
		Dataset ds = datasetEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
	}

	/**
	 * 新增
	 * 
	 * @param mouseEvent
	 */
	public void onAdd(MouseEvent mouseEvent) {
		OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_ADD);
		props.setParamMap(paramMap);
		props.setHeight("400");
		props.setWidth("1000");
		this.getCurrentAppCtx().navgateTo(props);
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
		props.setHeight("400");
		props.setWidth("1000");
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
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
		if(AppInteractionUtil.showConfirmDialog("确认框", "是否删除？")){
			Row[]rows = ds.getCurrentRowSet().getCurrentRowData().getSelectedRows();
			SuperVO[]supers = new Dataset2SuperVOSerializer<SuperVO>().serialize(ds, rows);
			try {
				ScapDAO.getBaseDAO().deleteVOArray(supers);
				onDataLoad(new DataLoadEvent(this.getMasterDs()));
			} catch (DAOException e) {
				ScapLogger.error("删除机构库时出错!");
			}
		}
		
//		CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
	}

	/**
	 * 启用
	 * 
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onStart(MouseEvent<?> mouseEvent) throws BusinessException {
		Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待删除数据");
		}
		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));
		if (isReretive(pkValue)) {// 判断机构库是否关联
			AppInteractionUtil.showShortMessage("机构库已关联,不能删除!");
			return;
		}
		CmdInvoker.invoke(new UifEnableCmd(this.getMasterDsId(), true));
	}

	private boolean isReretive(String pkValue) {
		boolean flag = false;
		// String sql =
		// "select * from scappt_attachent a  left join scappt_attach b on (a.pk_attchfile=b.attach_name) where a.dr=0 and a.id_entity='"
		// + pkValue + "'";
		// try {
		// ScapLogger.debug(sql);
		// List<> vos = (List<AttachEntVO>) ScapDAO
		// .getBaseDAO()
		// .executeQuery(sql, new BeanListProcessor(AttachEntVO.class));
		// if (vos != null && vos.size() > 0) {
		// flag = true;
		// }
		// } catch (DAOException e) {
		// ScapLogger.error(sql);
		// throw new LfwRuntimeException("查询附件关联信息出错!");
		// }
		return flag;
	}

	/**
	 * 停用
	 * 
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onStop(MouseEvent<?> mouseEvent) throws BusinessException {
		CmdInvoker.invoke(new UifEnableCmd(this.getMasterDsId(), false));
	}

	/**
	 * 附件
	 * 
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onAttchFile(MouseEvent<?> mouseEvent) throws BusinessException {
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
	public void onPrint(MouseEvent mouseEvent) {
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
	 * 主数据加载逻辑
	 * 
	 * @param dataLoadEvent
	 */
	public void onDataLoad(DataLoadEvent dataLoadEvent) {
		Dataset ds = dataLoadEvent.getSource();
		ds.setExtendAttribute(
				Dataset.ORDER_PART,
				"order by pk_org desc,ts desc ");
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
	}

	/**
	 * 外部触发刷新
	 * 
	 * @param keys
	 */
	public void doRefresh(Map<?, ?> keys) {
		LfwRuntimeEnvironment.getWebContext().getWebSession()
				.addOriginalParameter("openBillId", null);
		onDataLoad(new DataLoadEvent(this.getMasterDs()));
		// TranslatedRow selRow = (TranslatedRow) keys.get(OPERATE_ROW);
		// if (selRow != null) {
		// Dataset ds = this.getMasterDs();
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
	 * 主组织变化
	 * 
	 * @param keys
	 */
	public void doOrgChange(Map<?, ?> keys) {
//		CmdInvoker.invoke(new UifDatasetLoadCmd(this.getMasterDsId()) {
//			protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
//				String where = AppUtil.getGroupOrgFielterSqlForDesign(false,
//						"pk_org");
//				ds.setLastCondition(where);
//				return where;
//			}
//
//			// 避免翻页时重走缓存
//			protected void changeCurrentKey() {
//				setChangeCurrentKey(true);
//			}
//		});
//		this.clearDetailDs();
	}

	/**
	 * 查询plugin
	 * 
	 * @param keys
	 */
	public void doQueryChange(Map<?, ?> keys) {
		FromWhereSQL whereSql = (FromWhereSQL) keys
				.get(FromWhereSQL.WHERE_SQL_CONST);
		CmdInvoker.invoke(new QueryCmd(MAIN_VIEW_ID, this.getMasterDsId(),
				whereSql.getWhere()));
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

	@Override
	protected String getMasterDsId() {
		return "scappt_library";
	}

	/**
	 * 导出模板
	 * 
	 * @param scriptEvent
	 */
	public void onExport(MouseEvent mouseEvent) {
		StringBuffer str = new StringBuffer();
		str.append("/portal/ReportServer?reportlet=scappt%2Fscappt_lib.cpt&op=view");
		StringBuffer js = new StringBuffer().append("window.open ('")
				.append(str.toString())
				.append("'," + IGlobalConstants.OPEN_REPORT_CONDITION + ");");
		// 调用js语句打开报表
		AppLifeCycleContext.current().getWindowContext()
				.addExecScript(js.toString());
//		LfwView edit = AppLifeCycleContext.current().getWindowContext()
//				.getCurrentViewContext().getView();
//		Dataset ds = edit.getViewModels().getDataset(getMasterDsId());
//		if (ds == null) {
//			AppInteractionUtil.showShortMessage("请选中要导出的数据!");
//			return;
//		}
//		if (ds.getCurrentRowSet().getCurrentRowData() == null) {
//			AppInteractionUtil.showShortMessage("请选中要导出的数据!");
//			return;
//		}
//		Row[] rows = ds.getAllSelectedRows();
//		if (rows == null || rows.length <= 0) {
//			AppInteractionUtil.showShortMessage("请选中要导出的数据!");
//			return;
//		}
//		LibraryVO[] vos = new Dataset2SuperVOSerializer<LibraryVO>().serialize(
//				ds, rows);
//		realExport(Arrays.asList(vos));
	}

	public void realExport(List<LibraryVO> vos) {
//		String tmp = new UFDateTime().toString().replaceAll("[-: ]", "");
//		File outFile = new File(RuntimeEnv.getInstance().getNCHome()
//				+ IScapFileConstants.PortalFileStoreSCAPPTOUTPUT + tmp + ".xlsx");// 输出
//		OutputStream outStream = null;
//		try {
//			outStream = new FileOutputStream(outFile);
//		} catch (FileNotFoundException e) {
//		}
//		ExcelSheetConfig sconfig = new ExcelSheetConfig();
//		sconfig.setBeginRow(1);
//		Map<String, String[]> maps1 = new LinkedHashMap<String, String[]>() {
//			{
//				put(LibraryVO.PK_ORG, new String[] { "组织", "STRING" });
//				put(LibraryVO.VNAME, new String[] { "机构名称", "STRING" });
//				put(LibraryVO.VADDRESS, new String[] { "办公地址", "STRING" });
//				put(LibraryVO.VHEADER, new String[] { "负责人", "STRING" });
//				put(LibraryVO.VPHONE, new String[] { "联系电话", "STRING" });
//				put(LibraryVO.VWEBSITE, new String[] { "网址", "STRING" });
//				put(LibraryVO.VCONTROLPHONE, new String[] { "监督电话", "STRING" });
//			}
//		};
//		int j = 0;
//		for (Map.Entry<String, String[]> entry : maps1.entrySet()) {
//			String[] strs = entry.getValue();
//			Property prop = new Property(j, entry.getKey(), strs[0],
//					Type.valueOf(strs[1].trim()));
//			sconfig.addProp(prop);
//			j++;
//		}
//		sconfig.setSheetName("产权库");
//		sconfig.setExportDataSource(vos);
//		String srcDirPath = RuntimeEnv.getInstance().getNCHome()
//				+ IScapFileConstants.PortalFileStoreSCAPPT;
//		ExcelSheetConfig[] sconfigs = new ExcelSheetConfig[] { sconfig, sconfig };
//		ExcelUtils.templateListCardExport(sconfigs, srcDirPath + "ptcq.xlsx",
//				outStream);// 读模板
//		
		
//		try { 
//			String downfile;
//			downfile = "sysDownloadFile('"+ LfwRuntimeEnvironment.getRootPath()+URLDecoder.decode(srcDirPath + "ptcq.xls", "UTF-8")+"')";
//			AppLifeCycleContext ctx = AppLifeCycleContext.current();
//			ctx.getWindowContext().addExecScript(downfile);
//		} catch (UnsupportedEncodingException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}	
//		CmdInvoker.invoke(new UifZipDownloadCmd(RuntimeEnv.getInstance()
//				.getNCHome() + IScapFileConstants.PortalFileStoreSCAPPTOUTPUT,
//				"c:/", tmp + ".zip"));
		// AppInteractionUtil.showMessageDialog("数据导出成功!<br>" + "导出目录  c盘  文件名:"
		// + tmp + ".zip");
//		File delist = new File(RuntimeEnv.getInstance().getNCHome()
//				+ IScapFileConstants.PortalFileStoreSCAPPTOUTPUT);
//		if (delist.exists()) {
//			for (File tmpfile : delist.listFiles()) {
//				tmpfile.delete();
//			}
//		}
	}
}
