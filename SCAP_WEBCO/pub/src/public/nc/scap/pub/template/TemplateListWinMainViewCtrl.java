package nc.scap.pub.template;
import nc.uap.lfw.core.cmd.UifSaveCmd;
import nc.uap.lfw.core.cmd.CmdInvoker;
import java.util.Map;
import nc.uap.lfw.core.event.DataLoadEvent;
import java.util.HashMap;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.bm.ButtonStateManager;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.cmd.UifSaveCmdRV;
import java.util.ArrayList;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import java.util.List;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.vo.pub.SuperVO;
import nc.scap.pub.vos.TemplateVO;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.vo.pub.BusinessException;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.lfw.core.constants.AppConsts;
import uap.web.bd.pub.AppUtil;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.scap.pub.util.CpWinUtil;
import uap.lfw.core.locator.ServiceLocator;
import nc.uap.lfw.core.event.MouseEvent;
/** 
 * ��Ϣ�б�Ĭ���߼�
 */
public class TemplateListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="nc.scap.pub.pub_uiComps_template.template_cardwin";
  private static final String CARD_WIN_TITLE="�༭";
  /** 
 * ������ѡ���߼�
 * @param datasetEvent
 */
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
  			  	CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
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
   Dataset dataset = CpWinUtil.getDataset(this.getMasterDsId());
	Row row = dataset.getSelectedRow();
	row.setValue(dataset.nameToIndex(TemplateVO.ENABLESTATE), 2);
	CmdInvoker.invoke(new UifSaveCmdRV(getMasterDsId(), getDetailDsIds(),false));
	// CmdInvoker.invoke(new UifEnableCmd(this.getMasterDsId(), true));
  }
  /** 
 * ͣ��
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onStop(  MouseEvent<?> mouseEvent) throws BusinessException {
    Dataset dataset = CpWinUtil.getDataset(this.getMasterDsId());
	Row row = dataset.getSelectedRow();
	row.setValue(dataset.nameToIndex(TemplateVO.ENABLESTATE), 3);
	CmdInvoker.invoke(new UifSaveCmdRV(getMasterDsId(), getDetailDsIds(),false));
    // CmdInvoker.invoke(new UifEnableCmd(this.getMasterDsId(), false));
  }
  private static final String PARAM_BILLITEM="billitem";
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
		Map<String, String> paramMap = null;
		if (primaryKeyValue != null){
			paramMap = new HashMap<String, String>(2);
			paramMap.put(PARAM_BILLITEM, primaryKeyValue);
		}
		CmdInvoker.invoke(new UifAttachCmd("����", paramMap));
		/**
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
		*/
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
    Row savedRow = (Row) keys.get(OPERATE_ROW);
			if (savedRow != null) {
				Dataset ds = this.getMasterDs();
				savedRow.setRowId(ds.getEmptyRow().getRowId());
				String sign = this.getOperator();
				if (AppConsts.OPE_EDIT.equals(sign)) {
					int index = ds.getRowIndex(ds.getSelectedRow());
					if (index >= 0) {
						ds.removeRow(index);
						ds.insertRow(index, savedRow);
					}
				} else if (AppConsts.OPE_ADD.equals(sign)) {
					ds.addRow(savedRow);
				}
			}
  }
  /** 
 * ����֯�仯
 * @param keys
 */
  public void doOrgChange(  Map<?,?> keys){
    CmdInvoker.invoke(new UifDatasetLoadCmd(this.getMasterDsId()) {
    			/**
				protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
					String where = AppUtil.getGroupOrgFielterSqlForDesign(false, PK_ORG);
					ds.setLastCondition(where);
					return where;
				}
				*/
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
    return "scapco_template";
  }
}
