package nc.scap.pub.visualOrganization;
import java.util.Map;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.event.TreeNodeEvent;
import nc.uap.lfw.core.event.DataLoadEvent;
import java.util.HashMap;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.data.Dataset;
import java.util.ArrayList;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.model.plug.TranslatedRow;
import java.util.List;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.vo.pub.SuperVO;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.lfw.file.UploadFileHelper;
import nc.vo.pub.BusinessException;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.lfw.core.constants.AppConsts;
import uap.web.bd.pub.AppUtil;
import nc.uap.lfw.core.event.DatasetEvent;
import uap.lfw.core.locator.ServiceLocator;
import nc.uap.lfw.core.event.MouseEvent;
/** 
 * ��Ϣ�б�Ĭ���߼�
 */
public class VisualOrganizationListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="visualOrganization.visualOrganization_cardwin";
  private static final String CARD_WIN_TITLE="�༭";
  /** 
 * �����ݼ����߼�
 * @param dataLoadEvent
 */
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
	String lastCondition = ds.getLastCondition();
	CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()){
	});
  }
  /** 
 * ������ѡ���߼�
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
  }
  /** 
 * ����
 * @param mouseEvent
 */
  public void onAdd(  MouseEvent mouseEvent){
    OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_ADD);
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
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
		String pkValue = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * ɾ��
 * @param scriptEvent
 */
  public void onDelete(  MouseEvent mouseEvent){
    CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
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
  public void onPrint(  MouseEvent mouseEvent){
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
					copyTranslateRow2Row(selRow,savedRow,ds);
//					}
				} else if (AppConsts.OPE_ADD.equals(sign)) {
					Row savedRow = ds.getEmptyRow();
					savedRow = copyTranslateRow2Row(selRow,savedRow,ds);
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
 * ����֯�仯
 * @param keys
 */
  public void doOrgChange(  Map<?,?> keys){
    CmdInvoker.invoke(new UifDatasetLoadCmd(this.getMasterDsId()) {
				protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
					String where = AppUtil.getGroupOrgFielterSqlForDesign(false, PK_ORG);
					ds.setLastCondition(where);
					return where;
				}
	
				// ���ⷭҳʱ���߻���
				protected void changeCurrentKey() {
					setChangeCurrentKey(true);
				}
			});
			this.clearDetailDs();
  }
  /** 
 * ��ѯplugin
 * @param keys
 */
  public void doQueryChange(  Map<?,?> keys){
    FromWhereSQL whereSql = (FromWhereSQL) keys.get(FromWhereSQL.WHERE_SQL_CONST);
			CmdInvoker.invoke(new QueryCmd(MAIN_VIEW_ID, this.getMasterDsId(), whereSql.getWhere()));
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
    return "pub_visualOrganization";
  }
  public void onVisOrgTreeClick(  TreeNodeEvent treeNodeEvent){
    TreeViewComp trc = treeNodeEvent.getSource();

        String dsId = trc.getDataset();
        Dataset ds = AppUtil.getCntViewCtx().getView().getViewModels()
                        .getDataset(dsId);
	
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("��ѡ�д��༭����");
		}
		Row row = ds.getSelectedRow();
		String pk_visualOrganization = (String)row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));
		uap.web.bd.pub.AppUtil.addAppAttr("pk_visualOrganization", pk_visualOrganization);
  }
}