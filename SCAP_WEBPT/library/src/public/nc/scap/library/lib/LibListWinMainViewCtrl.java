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
 * ��Ϣ�б�Ĭ���߼�
 */
public class LibListWinMainViewCtrl<T extends WebElement> extends
		AbstractMasterSlaveViewController {
	private static final long serialVersionUID = -1;
	private static final String MAIN_VIEW_ID = "main";
	private static final String CARD_WIN_ID = "libraryComps.lib_cardwin";
	private static final String CARD_WIN_TITLE = "�༭";

	/**
	 * ������ѡ���߼�
	 * 
	 * @param datasetEvent
	 */
	public void onAfterRowSelect(DatasetEvent datasetEvent) {
		Dataset ds = datasetEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
	}

	/**
	 * ����
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
	 * �༭
	 * 
	 * @param scriptEvent
	 */
	public void onEdit(MouseEvent mouseEvent) {
		Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("��ѡ�д��༭����");
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
	 * ɾ��
	 * 
	 * @param scriptEvent
	 */
	public void onDelete(MouseEvent mouseEvent) {
		Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("��ѡ�д�ɾ������");
		}
		if(AppInteractionUtil.showConfirmDialog("ȷ�Ͽ�", "�Ƿ�ɾ����")){
			Row[]rows = ds.getCurrentRowSet().getCurrentRowData().getSelectedRows();
			SuperVO[]supers = new Dataset2SuperVOSerializer<SuperVO>().serialize(ds, rows);
			try {
				ScapDAO.getBaseDAO().deleteVOArray(supers);
				onDataLoad(new DataLoadEvent(this.getMasterDs()));
			} catch (DAOException e) {
				ScapLogger.error("ɾ��������ʱ����!");
			}
		}
		
//		CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
	}

	/**
	 * ����
	 * 
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onStart(MouseEvent<?> mouseEvent) throws BusinessException {
		Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("��ѡ�д�ɾ������");
		}
		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));
		if (isReretive(pkValue)) {// �жϻ������Ƿ����
			AppInteractionUtil.showShortMessage("�������ѹ���,����ɾ��!");
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
		// throw new LfwRuntimeException("��ѯ����������Ϣ����!");
		// }
		return flag;
	}

	/**
	 * ͣ��
	 * 
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onStop(MouseEvent<?> mouseEvent) throws BusinessException {
		CmdInvoker.invoke(new UifEnableCmd(this.getMasterDsId(), false));
	}

	/**
	 * ����
	 * 
	 * @param mouseEvent
	 * @throws BusinessException
	 */
	public void onAttchFile(MouseEvent<?> mouseEvent) throws BusinessException {
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
		// �����ǰ�˲��ǵ�ǰ���ݵĲ�����,��ʱȡ��������.����ֻ�ܲ鿴,��Ҫ��Ϊ���̬.
		if (taskPk == null || taskPk.equals("")) {
			this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE,
					WfmConstants.BILLSTATE_BROWSE);
		}

		// ���̸�������
		Map<String, String> wfmParam = WfmUtilFacade
				.getFileMgrParamsByTask(taskPk);

		// ��������
		Map<String, String> param = UploadFileHelper.BuildDefaultPamater("",
				primaryKeyValue, CommonObjectConstants.AttachFileType, "");
		param.put("usescanable", "true");
		param.put("state", String.valueOf(31));

		String title = "����";
		if (wfmParam != null && !wfmParam.isEmpty()) {
			param.putAll(wfmParam);
		}

		CmdInvoker.invoke(new UifAttachCmd(title, param));
	}

	/**
	 * ��ӡ
	 */
	public void onPrint(MouseEvent mouseEvent) {
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
	 * �����ݼ����߼�
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
	 * �ⲿ����ˢ��
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
	 * ����֯�仯
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
//			// ���ⷭҳʱ���߻���
//			protected void changeCurrentKey() {
//				setChangeCurrentKey(true);
//			}
//		});
//		this.clearDetailDs();
	}

	/**
	 * ��ѯplugin
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
	 * ����ģ��
	 * 
	 * @param scriptEvent
	 */
	public void onExport(MouseEvent mouseEvent) {
		StringBuffer str = new StringBuffer();
		str.append("/portal/ReportServer?reportlet=scappt%2Fscappt_lib.cpt&op=view");
		StringBuffer js = new StringBuffer().append("window.open ('")
				.append(str.toString())
				.append("'," + IGlobalConstants.OPEN_REPORT_CONDITION + ");");
		// ����js���򿪱���
		AppLifeCycleContext.current().getWindowContext()
				.addExecScript(js.toString());
//		LfwView edit = AppLifeCycleContext.current().getWindowContext()
//				.getCurrentViewContext().getView();
//		Dataset ds = edit.getViewModels().getDataset(getMasterDsId());
//		if (ds == null) {
//			AppInteractionUtil.showShortMessage("��ѡ��Ҫ����������!");
//			return;
//		}
//		if (ds.getCurrentRowSet().getCurrentRowData() == null) {
//			AppInteractionUtil.showShortMessage("��ѡ��Ҫ����������!");
//			return;
//		}
//		Row[] rows = ds.getAllSelectedRows();
//		if (rows == null || rows.length <= 0) {
//			AppInteractionUtil.showShortMessage("��ѡ��Ҫ����������!");
//			return;
//		}
//		LibraryVO[] vos = new Dataset2SuperVOSerializer<LibraryVO>().serialize(
//				ds, rows);
//		realExport(Arrays.asList(vos));
	}

	public void realExport(List<LibraryVO> vos) {
//		String tmp = new UFDateTime().toString().replaceAll("[-: ]", "");
//		File outFile = new File(RuntimeEnv.getInstance().getNCHome()
//				+ IScapFileConstants.PortalFileStoreSCAPPTOUTPUT + tmp + ".xlsx");// ���
//		OutputStream outStream = null;
//		try {
//			outStream = new FileOutputStream(outFile);
//		} catch (FileNotFoundException e) {
//		}
//		ExcelSheetConfig sconfig = new ExcelSheetConfig();
//		sconfig.setBeginRow(1);
//		Map<String, String[]> maps1 = new LinkedHashMap<String, String[]>() {
//			{
//				put(LibraryVO.PK_ORG, new String[] { "��֯", "STRING" });
//				put(LibraryVO.VNAME, new String[] { "��������", "STRING" });
//				put(LibraryVO.VADDRESS, new String[] { "�칫��ַ", "STRING" });
//				put(LibraryVO.VHEADER, new String[] { "������", "STRING" });
//				put(LibraryVO.VPHONE, new String[] { "��ϵ�绰", "STRING" });
//				put(LibraryVO.VWEBSITE, new String[] { "��ַ", "STRING" });
//				put(LibraryVO.VCONTROLPHONE, new String[] { "�ල�绰", "STRING" });
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
//		sconfig.setSheetName("��Ȩ��");
//		sconfig.setExportDataSource(vos);
//		String srcDirPath = RuntimeEnv.getInstance().getNCHome()
//				+ IScapFileConstants.PortalFileStoreSCAPPT;
//		ExcelSheetConfig[] sconfigs = new ExcelSheetConfig[] { sconfig, sconfig };
//		ExcelUtils.templateListCardExport(sconfigs, srcDirPath + "ptcq.xlsx",
//				outStream);// ��ģ��
//		
		
//		try { 
//			String downfile;
//			downfile = "sysDownloadFile('"+ LfwRuntimeEnvironment.getRootPath()+URLDecoder.decode(srcDirPath + "ptcq.xls", "UTF-8")+"')";
//			AppLifeCycleContext ctx = AppLifeCycleContext.current();
//			ctx.getWindowContext().addExecScript(downfile);
//		} catch (UnsupportedEncodingException e) {
//			// TODO �Զ����ɵ� catch ��
//			e.printStackTrace();
//		}	
//		CmdInvoker.invoke(new UifZipDownloadCmd(RuntimeEnv.getInstance()
//				.getNCHome() + IScapFileConstants.PortalFileStoreSCAPPTOUTPUT,
//				"c:/", tmp + ".zip"));
		// AppInteractionUtil.showMessageDialog("���ݵ����ɹ�!<br>" + "����Ŀ¼  c��  �ļ���:"
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
