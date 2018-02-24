package nc.scap.pub.qryorgs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.jdbc.framework.processor.BeanListProcessor;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.vos.QryOrgsVO;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.model.plug.TranslatedRow;
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import uap.web.bd.pub.AppUtil;
/** 
 * 信息列表默认逻辑
 */
public class Qry_orgsListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="nc.scap.pub.qryorgsComps.qry_orgs_cardwin";
  private static final String CARD_WIN_TITLE="编辑";
  /** 
 * 主数据选中逻辑
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
  }
  /** 
 * 新增
 * @param mouseEvent
 */
  public void onAdd(  MouseEvent mouseEvent){
	  OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_ADD);
		
		props.setParamMap(paramMap);
		props.setWidth("600");
		props.setHeight("400");
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * 设置PK_ORG字段值
 * @param row
 */
  private void setAutoFillValue(  Row row,  String pk_type,  String type_name){
    if(row != null){
			Dataset ds = this.getCurrentView().getViewModels().getDataset(this.getMasterDsId());
			
			String pkOrg = this.getCurrentAppCtx().getAppEnvironment().getPk_org();
			if(pkOrg != null){
				int pkOrgIndex = ds.nameToIndex("pk_org");
				if(pkOrgIndex >= 0){
					row.setValue(pkOrgIndex, pkOrg);		
				}
			}
			String pkGroup = this.getCurrentAppCtx().getAppEnvironment().getPk_group();
			if(pkGroup != null){
				int pkGroupIndex = ds.nameToIndex(PK_GROUP);
				if(pkGroupIndex >= 0){
					row.setValue(pkGroupIndex, pkGroup);		
				}
			}
			
			row.setValue(ds.nameToIndex(QryOrgsVO.PK_TYPE), pk_type);
			
			//非快报让月份不能编辑
			GridComp gc = (GridComp)AppUtil.getCntWindowCtx().getCurrentViewContext().getView().getViewComponents().getComponent( "qry_orgs_grid");
			if("快报".equals(type_name)){
				gc.getElementById(QryOrgsVO.VMONTH).setEditable(true);
			}else {
				gc.getElementById(QryOrgsVO.VMONTH).setEditable(false);
			}

		}
  }
  /** 
 * 编辑
 * @param scriptEvent
 */
  public void onEdit(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待编辑数据");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		props.setParamMap(paramMap);
		props.setWidth("600");
		props.setHeight("400");
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * 删除
 * @param scriptEvent
 */
  public void onDelete(  MouseEvent mouseEvent){
	  ScapDAO dao = new ScapDAO();
		Dataset ds = this.getMasterDs();
		Row row = ds.getSelectedRow();
		String pk_qry_orgs = (String) row.getValue(ds.nameToIndex(QryOrgsVO.PK_QRY_ORGS));
		StringBuffer sql = new StringBuffer();
		sql.append("select * from scap_qryorgs t inner join (");
		sql.append(" select a.* from scap_qryorgs a");
		sql.append(" inner join bd_defdoc b on a.pk_type = b.pk_defdoc");
		sql.append(" where b.name = '快报' and (a.vmonth is null or a.vmonth = '~') and a.pk_qry_orgs = '"+ pk_qry_orgs +"' and nvl(a.dr,0) = 0 ) c");
		sql.append(" on t.vyear = c.vyear and t.pk_type = c.pk_type where t.pk_qry_orgs <> c.pk_qry_orgs and nvl(t.dr,0) = 0");
		List<QryOrgsVO> list = (List<QryOrgsVO>) dao.executeQuery(sql.toString(), new BeanListProcessor(QryOrgsVO.class));
		if(list != null && list.size() > 0){
			throw new LfwRuntimeException("快报数据存在该年份其他月份的数据，不能删除全年数据");
		}
    CmdInvoker.invoke(new UifDelCmdRV(this.getMasterDsId()));
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
		uap.web.bd.pub.AppUtil.addAppAttr(LfwFileConstants.SYSID,LfwFileConstants.SYSID_BAFILE);
		String primaryKey = ds.getPrimaryKeyField();
		if (primaryKey == null) {
			throw new LfwRuntimeException("当前Dataset没有设置主键!");
		}
		String primaryKeyValue = (String) row.getValue(ds.nameToIndex(primaryKey));

		String taskPk = WfmUtilFacade.getTaskPkPkByPkFormIns(primaryKeyValue);
		// 如果当前人不是当前单据的参与者,此时取不到任务.附件只能查看,需要设为浏览态.
		if (taskPk == null || taskPk.equals("")) {
			this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE, WfmConstants.BILLSTATE_BROWSE);
		}

		// 流程附件参数
		Map<String, String> wfmParam = WfmUtilFacade.getFileMgrParamsByTask(taskPk);

		// 附件参数
		Map<String, String> param = UploadFileHelper.BuildDefaultPamater("", primaryKeyValue, CommonObjectConstants.AttachFileType, "");
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
			ICpPrintTemplateService service = ServiceLocator.getService(ICpPrintTemplateService.class);
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
	String fresh_type = (String) AppUtil.getAppAttr("fresh_type"); //区分是点击左边类型查询数据,还是保持数据后的刷新数据
	if(fresh_type != null){
		this.onDataLoad(null);
	}else {
		Row savedRow = (Row) keys.get("OPERATE_ROW");
		if (savedRow != null) {
			Dataset ds = this.getMasterDs();
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
	
	AppUtil.addAppAttr("fresh_type", null);
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
					String pk_type = (String) AppUtil.getAppAttr("pk_type");
					String where = " pk_type ='" + pk_type + "' and nvl(dr,0) = 0 ";
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
 * 主数据加载逻辑
 * @param dataLoadEvent
 */
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
    Dataset ds = this.getMasterDs();
		String pk_type = (String) AppUtil.getAppAttr("pk_type");
		ds.setLastCondition(" pk_type ='" + pk_type + "' ");
		ds.setExtendAttribute(Dataset.ORDER_PART," vyear desc,vmonth desc ");
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
  /** 
 * 查询plugin
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
    return "qry_orgs";
  }
}
