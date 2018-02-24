package nc.scap.pub.news;
import nc.uap.lfw.core.serializer.impl.Dataset2SuperVOSerializer;
import nc.scap.pub.vos.NewsVO;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.wfm.exception.WfmServiceException;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import nc.bs.dao.DAOException;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.scap.pub.itf.IGlobalConstants;
import nc.uap.lfw.core.model.plug.TranslatedRow;
import nc.uap.lfw.file.LfwFileConstants;
import java.util.List;
import nc.scap.pub.util.ScapDAO;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.wfm.itf.IWfmTaskQry;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.wfm.vo.WfmTaskVO;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpCtrlUtil;
import nc.uap.portal.log.ScapLogger;
import nc.uap.lfw.core.cmd.CmdInvoker;
import java.util.Map;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import java.util.HashMap;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.uap.wfm.utils.WfmCPUtilFacade;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.data.Dataset;
import java.util.ArrayList;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.vo.pub.SuperVO;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.lfw.file.UploadFileHelper;
import nc.vo.pub.BusinessException;
import nc.scap.pub.util.CpPubMethod;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.scap.pub.util.CpWinUtil;
import uap.lfw.core.locator.ServiceLocator;
import nc.uap.lfw.core.event.MouseEvent;
/** 
 * ��Ϣ�б�Ĭ���߼�
 */
public class NewsListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="nc.scap.pub.news.uiComps.news_cardwin";
  private static final String CARD_WIN_TITLE="�༭";
  /** 
 * �����ݼ����߼�
 * @param dataLoadEvent
 */
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
		String type = getFixedNewsType();
		if(type == null || type.equals("") || type.equals("0")) {
			CpWinUtil.setMenuItemsVisible(CpWinUtil.getView(), "menubar", new String[] {"add", "edit", "del"}, false);
		}
		String where = "";
		if(type == null || type.equals("") || type.equals("0")) {
			where += "pk_org = '" + CntUtil.getCntOrgPk() + "'";
		}else {
			where += "pk_org = '" + CntUtil.getCntOrgPk() + "' and type = '" + type + "'";
		}
		ds.setLastCondition(where);
		ds.setOrderByPart("type");
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
  /** 
 * ������ѡ���߼�
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
		Row row = ds.getSelectedRow();
		String state = row.getString(ds.nameToIndex(NewsVO.FORMSTATE));
		if(state.equals("NottStart")) {
			CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[] {"audit"}, false);
		}else if(state.equals("Run")) {
			CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[] {"edit", "del"}, false);
			try {
				WfmTaskVO wfmtaskVO = CpPubMethod.getLasterTaskByBillIdAndUser((String) CpCtrlUtil.getNametoValue(ds, ds.getSelectedRow(), ds.getPrimaryKeyField()), CntUtil.getCntUserPk(), IGlobalConstants.BTN_WF);
				if (wfmtaskVO != null && !"State_End".equals(wfmtaskVO.getState())) {
					CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[] {"audit"}, true);
				}
			} catch (WfmServiceException e) {
			}
		}else if(state.equals("End")) {
			CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[] {"add", "view"}, true);
			CpWinUtil.setMenuItemsEnable(CpWinUtil.getView(), "menubar", new String[] {"edit", "del", "audit"}, false);
		}
  }
  /** 
 * �ⲿ����ˢ��
 * @param keys
 */
  public void doRefresh(  Map<?,?> keys){
    TranslatedRow selRow = (TranslatedRow) keys.get(OPERATE_ROW);
		if (selRow != null) {
			Dataset ds = this.getMasterDs();
			String sign = this.getOperator();
			if (AppConsts.OPE_EDIT.equals(sign)) {
				Row savedRow = ds.getSelectedRow();
				copyTranslateRow2Row(selRow, savedRow, ds);
				// }
			} else if (AppConsts.OPE_ADD.equals(sign)) {
				Row savedRow = ds.getEmptyRow();
				savedRow = copyTranslateRow2Row(selRow, savedRow, ds);
				ds.addRow(savedRow);
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
 * ����
 * @param mouseEvent
 */
  public void onAdd(  MouseEvent<?> mouseEvent){
    String type = getFixedNewsType();
		this.onAdd_wfm();
		OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		if (type != null) {
			paramMap.put("type", type);
		}
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_ADD);

		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * ����-����ҵ��
 */
  private void onAdd_wfm(){
    this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_BillID, null);
		this.resetWfmParameter();
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_FolwTypePk, NewsUtil.getFlwTypePk());
  }
  /** 
 * ��ҳ���뵱ǰҳ������ͬһapp��Χ��ÿ����Ҫ���app�еı���
 */
  private void resetWfmParameter(){
    this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_TaskPk, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_ScratchPad, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.AttachFileList_Temp_Billitem, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_ProInsPk, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.RETURN_PK_TASK, null);
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_FormInFoCtx_Billitem, null);
  }
  /** 
 * �༭
 * @param scriptEvent
 */
  public void onEdit(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("��ѡ�д��༭����");
		}

		Row row = ds.getSelectedRow();
		
//		String type = (String) row.getValue(ds.nameToIndex("type"));
//		if ("4".equals(type)) {
//			throw new LfwRuntimeException("�ص�Ԥ�����Ų���ֱ�ӱ༭");
//		}

		String pkValue = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		paramMap.put(WfmConstants.WfmUrlConst_billID, pkValue);

		props.setParamMap(paramMap);

		this.onEdit_wfm(pkValue);

		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * �༭-����ҵ��
 * @param pkValue
 */
  private void onEdit_wfm(  String pkValue){
    this.resetWfmParameter();
		try {
			String pk_user = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
			IWfmTaskQry taskQry = ServiceLocator.getService(IWfmTaskQry.class);
			WfmTaskVO task = taskQry.getLastTaskVOByFormPkAndUserPk(pkValue, pk_user);
			if (task != null) {
				this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_TaskPk, task.getPk_task());
			}
		} catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
  }
  /** 
 * ɾ��
 * @param scriptEvent
 */
  public void onDelete(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("��ѡ�д�ɾ������");
		}

		Row row = ds.getSelectedRow();
		String pk_form = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));
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
  public void onView(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("��ѡ�д��༭����");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));
		this.onEdit_wfm(pkValue);

		OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, "view");
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		paramMap.put(WfmConstants.WfmUrlConst_billID, pkValue);
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  public void onAudit(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
			if (ds.getSelectedIndex() < 0) {
				throw new LfwRuntimeException("��ѡ�д��༭����");
			}

			Row row = ds.getSelectedRow();
			String pkValue = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));
			this.onEdit_wfm(pkValue);

			OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
			props.setButtonZone(false);
			Map<String, String> paramMap = new HashMap<String, String>(2);
			paramMap.put(AppConsts.OPE_SIGN, "audit");
			paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
			paramMap.put(WfmConstants.WfmUrlConst_billID, pkValue);
			props.setParamMap(paramMap);
			this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * ���̽���
 * @param mouseEvent
 */
  public void onFlow(  MouseEvent<?> mouseEvent){
    Dataset masterDs = this.getMasterDs();
		Row row = masterDs.getSelectedRow();
		String pk_form = (String) row.getValue(masterDs.nameToIndex(masterDs.getPrimaryKeyField()));
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_BillID, pk_form);
		WfmUtilFacade.openFlowImage();
  }
  /** 
 * ����
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onStart(  MouseEvent<?> mouseEvent) throws BusinessException {
    CmdInvoker.invoke(new UifEnableCmd(getMasterDsId(), true));
  }
  /** 
 * ͣ��
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onStop(  MouseEvent<?> mouseEvent) throws BusinessException {
    CmdInvoker.invoke(new UifEnableCmd(getMasterDsId(), false));
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
		uap.web.bd.pub.AppUtil.addAppAttr(LfwFileConstants.SYSID, LfwFileConstants.SYSID_BAFILE);
		String primaryKey = ds.getPrimaryKeyField();
		if (primaryKey == null) {
			throw new LfwRuntimeException("��ǰDatasetû����������!");
		}
		String primaryKeyValue = (String) row.getValue(ds.nameToIndex(primaryKey));

		String taskPk = WfmUtilFacade.getTaskPkPkByPkFormIns(primaryKeyValue);
		// �����ǰ�˲��ǵ�ǰ���ݵĲ�����,��ʱȡ��������.����ֻ�ܲ鿴,��Ҫ��Ϊ���̬.
		if (taskPk == null || taskPk.equals("")) {
			this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE, WfmConstants.BILLSTATE_BROWSE);
		}

		// ���̸�������
		Map<String, String> wfmParam = WfmUtilFacade.getFileMgrParamsByTask(taskPk);

		// ��������
		Map<String, String> param = UploadFileHelper.BuildDefaultPamater("", primaryKeyValue, CommonObjectConstants.AttachFileType, "");
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
  public void onPrint(  MouseEvent<?> mouseEvent){
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
			ICpPrintTemplateService service = ServiceLocator.getService(ICpPrintTemplateService.class);
			service.print(printService);
		} catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
  }
  /** 
 * ����֯�仯
 */
  public void doOrgChange(  Map keys){
    CmdInvoker.invoke(new UifDatasetLoadCmd(getMasterDsId()) {
			protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
//				String type = getFixedNewsType();
				String type = LfwRuntimeEnvironment.getWebContext().getOriginalParameter("type");
				String where = "";
				if(type.equals("1")) {
//					where += "pk_org = '" + CntUtil.getCntOrgPk() + "' and type = '1'";
				}else if(type.equals("2")) {
					where += "pk_org = '" + CntUtil.getCntOrgPk() + "' and type = '2'";
				}else if(type.equals("3")) {
					where += "pk_org = '" + CntUtil.getCntOrgPk() + "' and type = '3'";
				}else if(type.equals("4")) {
					where += "pk_org = '" + CntUtil.getCntOrgPk() + "' and type = '4'";
				}

//				where = AppUtil.getGroupOrgFielterSqlForDesign(false, "pk_org");
//				if (type != null) {
//					where += " and type = '" + type + "'";
//				}
				ds.setLastCondition(where);
				return where;
			}

			// ���ⷭҳʱ���߻���
			protected void changeCurrentKey() {
				setChangeCurrentKey(true);
			}
		});
		clearDetailDs();
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
  /** 
 * ��ѯpluguin
 * @param keys
 */
  public void doQueryChange(  Map keys){
    FromWhereSQL whereSql = (FromWhereSQL) keys.get(FromWhereSQL.WHERE_SQL_CONST);
		String whereCondition = whereSql.getWhere();
		String type = getFixedNewsType();
		if(type != null) {
			if(type.equals("1")) {
			}else if(type.equals("2")) {
				whereCondition += "pk_org = '" + CntUtil.getCntOrgPk() + "' and type = '2'";
			}else if(type.equals("3")) {
				whereCondition += "pk_org = '" + CntUtil.getCntOrgPk() + "' and type = '3'";
			}
		}
//		if (type != null) {
//			whereCondition += " and type = '" + type + "'";
//		}
		CmdInvoker.invoke(new QueryCmd(MAIN_VIEW_ID, getMasterDsId(), whereCondition));
  }
  @Override protected String getMasterDsId(){
    return "NewsVO";
  }
  protected String getFixedNewsType(){
    String typeId = LfwRuntimeEnvironment.getWebContext().getOriginalParameter("type");
		return typeId;
  }
  public void onXiajia(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
    Row[] rows = ds.getSelectedRows();
    for(int i=0;i<rows.length;i++){
    	String xiajia = (String)rows[i].getValue(ds.nameToIndex(NewsVO.XIAJIA));
    	if("2".equals(xiajia)){
    		throw new LfwRuntimeException("��ѡ�����е�"+i+"�������Ѿ��¼ܣ�����ִ���¼ܲ�����");
    	}
    	rows[i].setValue(ds.nameToIndex(NewsVO.XIAJIA),"2");
    }
    NewsVO[] vos = new Dataset2SuperVOSerializer<NewsVO>()
            .serialize(ds, rows);
    try {
    	if (AppInteractionUtil.showConfirmDialog("��ʾ", "�Ƿ�ȷ���¼ܣ�")) {
	        ScapDAO.getBaseDAO().updateVOArray(vos);
	        onDataLoad(new DataLoadEvent(ds));
	        AppInteractionUtil.showShortMessage("�¼ܳɹ�!");
    	}
    } catch (DAOException e) {
        ScapLogger.error("���µ���״̬ʱ����,�������¼�");
    }
  }
  public void onXianshi(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
	    Row[] rows = ds.getSelectedRows();
	    for(int i=0;i<rows.length;i++){
	    	String xiajia = (String)rows[i].getValue(ds.nameToIndex(NewsVO.XIAJIA));
	    	if("1".equals(xiajia)){
	    		throw new LfwRuntimeException("��ѡ�����е�"+i+"������������ҳ��ʾ������ִ����ʾ������");
	    	}
	    	rows[i].setValue(ds.nameToIndex(NewsVO.XIAJIA),"1");
	    }
	    NewsVO[] vos = new Dataset2SuperVOSerializer<NewsVO>()
	            .serialize(ds, rows);
	    try {
	    	if (AppInteractionUtil.showConfirmDialog("��ʾ", "�Ƿ�ȷ����ҳ��ʾ��")) {
		        ScapDAO.getBaseDAO().updateVOArray(vos);
		        onDataLoad(new DataLoadEvent(ds));
		        AppInteractionUtil.showShortMessage("��ʾ�ɹ�!");
	    	}
	    } catch (DAOException e) {
	        ScapLogger.error("���µ���״̬ʱ����,�������¼�");
	    }
  }
}
