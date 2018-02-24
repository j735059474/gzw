package nc.scap.dsm.dtp.ctrl;
import nc.vo.scapjj.dsm.Datetype_HVO;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.event.TreeNodeEvent;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.scap.jjpub.checkutil.CheckDelTreeNode;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.bs.dao.DAOException;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.util.LfwClassUtil;
import nc.scap.jjpub.util.JjUtil;
import nc.uap.lfw.core.model.plug.TranslatedRow;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.file.LfwFileConstants;
import java.util.List;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import java.util.Map;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.bs.dao.BaseDAO;
import java.util.HashMap;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.jdbc.framework.SQLParameter;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.exception.LfwBusinessException;
import java.util.ArrayList;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.scap.jjpub.checkutil.CheckDocIsRef;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.vo.pub.SuperVO;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.lfw.file.UploadFileHelper;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.lfw.core.constants.AppConsts;
import uap.web.bd.pub.AppUtil;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import uap.lfw.core.locator.ServiceLocator;
import nc.uap.lfw.core.event.MouseEvent;
/** 
 * 信息列表默认逻辑
 */
public class DtpListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="dtpuiComps.dtp_cardwin";
  private static final String CARD_WIN_TITLE="编辑";
  /** 
 * 主数据选中逻辑
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
    Dataset poweruser = this.getCurrentView().getViewModels().getDataset("poweruser");
    poweruser.setOrderByPart("order by qxlx asc, def1 asc");
    Dataset powerrole = this.getCurrentView().getViewModels().getDataset("powerrole");
    powerrole.setOrderByPart("order by qxlx asc");
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
  }
  /** 
 * 新增
 * @param mouseEvent
 */
  public void onAdd(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		String pk = JjUtil.getDSvalue(ds, ds.getSelectedRow(),
				ds.getPrimaryKeyField());
		OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(4);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_ADD);
		paramMap.put("model", "nc.scap.dsm.dtp.ctrl.DtpPageModel");
		AppUtil.addAppAttr("pk_father", pk);
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
			throw new LfwRuntimeException("请选中待编辑数据");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(3);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		paramMap.put("model", "nc.scap.dsm.dtp.ctrl.DtpPageModel");
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * 删除
 * @param scriptEvent
 */
  public void onDelete(  MouseEvent mouseEvent){
    new CheckDocIsRef().execute(getMasterDs());
		new CheckDelTreeNode().execute(getMasterDs(), null);

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
    TranslatedRow selRow = (TranslatedRow) keys.get(OPERATE_ROW);
		if (selRow != null) {
			Dataset ds = this.getMasterDs();
			String sign = this.getOperator();
			if (AppConsts.OPE_EDIT.equals(sign)) {
				Row savedRow = ds.getSelectedRow();
				copyTranslateRow2Row(selRow, savedRow, ds);
				onRefresh();
				// }
			} else if (AppConsts.OPE_ADD.equals(sign)) {
				Row savedRow = ds.getEmptyRow();
				savedRow = copyTranslateRow2Row(selRow, savedRow, ds);
				ds.addRow(savedRow);
			}
//			onRefresh();
		}
  }
  /** 
 * 刷新
 */
  public void onRefresh(){
    onRefresh(false);
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
				" 1=1"));
		clearDetailDs();
		try {
			Dataset ds = getMasterDs();
			String sql = "select " + ds.getPrimaryKeyField() + " from "
					+ ds.getTableName() + " where " + whereSql.getWhere();
			Object obj = getBaseDAO().executeQuery(sql, new ColumnProcessor());
			if (obj == null || obj.toString().trim().length() == 0) {
			} else {
				String pk = obj.toString();
				Row[] rows = ds.getAllRow();
				if (rows != null && rows.length > 0) {
					for (int i = 0; i < rows.length; i++) {
						Row row = rows[i];
						String temppk = JjUtil.getDSvalue(ds, row,
								ds.getPrimaryKeyField());
						if (pk.equals(temppk)) {
							ds.setRowSelectIndex(i);
							return;
						}
					}
				}

			}
		} catch (Exception e) {
		}
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
    return "datetype_h";
  }
  BaseDAO dao;
  BaseDAO getBaseDAO(){
    if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
  }
  public void onRefresh(  MouseEvent mouseEvent){
    onRefresh(true);
  }
  /** 
 * 是否提示刷新成功
 * @param isshow
 */
  public void onRefresh(  boolean isshow){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			AppInteractionUtil.showShortMessage("未选中刷新数据!");
			return;
		}
		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		SuperVO svo = (SuperVO) LfwClassUtil.newInstance(ds.getVoMeta());
		String sql = "select * from " + svo.getTableName() + " where "
				+ svo.getPKFieldName() + " = ? and dr = 0 ";
		SQLParameter par = new SQLParameter();
		par.addParam(pkValue);
		try {
			SuperVO hvo = (SuperVO) getBaseDAO().executeQuery(sql, par,
					new BeanProcessor(svo.getClass()));
			if (hvo == null) {
				ds.removeRow(row);
				clearDetailDs();
			} else {
				new SuperVO2DatasetSerializer().vo2DataSet(hvo, ds, row);
				CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
			}
		} catch (DAOException e) {
			e.printStackTrace();
			LfwLogger.error(e.getMessage());
			throw new LfwRuntimeException(e);
		}
		if (isshow)
			AppInteractionUtil.showShortMessage("刷新操作结束!");
  }
  public void onScan(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待查看数据");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(CARD_WIN_ID, "查看");
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(4);
		paramMap.put(AppConsts.OPE_SIGN, "SCAN");
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		paramMap.put("model", "nc.scap.dsm.dtp.ctrl.DtpPageModel");
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  public void doSelectDataTypeRow(  Map keys){
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
  public void onDataLoad_datatype(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
    ds.setOrderByPart(" order by code asc ");
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
  public void onDataLoad_powerrole(  DataLoadEvent dataLoadEvent){
    //    Dataset ds = dataLoadEvent.getSource();
//	  	CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
      final Dataset ds = dataLoadEvent.getSource();
      Dataset dsh = AppLifeCycleContext.current().getViewContext().getView()
                          .getViewModels().getDataset(getMasterDsId());
          if (dsh.getSelectedIndex() < 0)
                  throw new LfwRuntimeException("请选中待查看数据");
          Row row = dsh.getSelectedRow();
          String pkValue = (String) row.getValue(dsh.nameToIndex(dsh
                          .getPrimaryKeyField()));
      ds.setLastCondition(Datetype_HVO.PK_DATETYPE_H + "='"+pkValue+"'");
                  CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()){

                      int currentPageIndex = 0;

                      @Override
                      protected void changeCurrentKey() {
                          setChangeCurrentKey(true);
                          currentPageIndex = ds.getCurrentRowSet().getPaginationInfo()
                                  .getPageIndex();
                      }

                      @Override
                      protected SuperVO[] queryVOs(PaginationInfo pinfo, SuperVO vo,
                              String wherePart, String orderPart)
                              throws LfwBusinessException {
                          pinfo.setPageIndex(currentPageIndex);
                          return super.queryVOs(pinfo, vo, wherePart, orderPart);
                      }

                  });
  }
  public void onDataLoad_poweruser(  DataLoadEvent dataLoadEvent){
    //    Dataset ds = dataLoadEvent.getSource();
//	  	CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
      final Dataset ds = dataLoadEvent.getSource();
      Dataset dsh = AppLifeCycleContext.current().getViewContext().getView()
                          .getViewModels().getDataset(getMasterDsId());
          if (dsh.getSelectedIndex() < 0)
                  throw new LfwRuntimeException("请选中待查看数据");
          Row row = dsh.getSelectedRow();
          String pkValue = (String) row.getValue(dsh.nameToIndex(dsh
                          .getPrimaryKeyField()));
      ds.setLastCondition(Datetype_HVO.PK_DATETYPE_H + "='"+pkValue+"'");
                  CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()){

                      int currentPageIndex = 0;

                      @Override
                      protected void changeCurrentKey() {
                          setChangeCurrentKey(true);
                          currentPageIndex = ds.getCurrentRowSet().getPaginationInfo()
                                  .getPageIndex();
                      }

                      @Override
                      protected SuperVO[] queryVOs(PaginationInfo pinfo, SuperVO vo,
                              String wherePart, String orderPart)
                              throws LfwBusinessException {
                          pinfo.setPageIndex(currentPageIndex);
                          return super.queryVOs(pinfo, vo, wherePart, orderPart);
                      }

                  });
  }
  public void onAfterRowUnSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
	  Integer[] selectedIndices = ds.getSelectedIndices();
	  if(selectedIndices == null || selectedIndices.length == 0) {
		  this.clearDetailDs();
	  }
  }
}
