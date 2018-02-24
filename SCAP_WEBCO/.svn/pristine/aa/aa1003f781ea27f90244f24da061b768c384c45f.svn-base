package nc.scap.dsm.material;
import nc.vo.scapjj.dsm.Datetype_HVO;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.lfw.core.cmd.UifUpdateUIDataCmdRV;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.jsp.uimeta.UITabItem;
import nc.uap.cpb.persist.dao.PtBaseDAO;
import nc.uap.lfw.core.event.TabEvent;
import nc.vo.scapjj.material.Otheruser_BVO;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.bs.dao.DAOException;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import nc.uap.lfw.core.ctx.OpenProperties;
import nc.uap.lfw.core.comp.WebElement;
import nc.vo.util.BDPKLockUtil;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.cpb.org.util.CpbUtil;
import nc.uap.lfw.util.LfwClassUtil;
import nc.scap.jjpub.util.JjUtil;
import nc.uap.cpb.org.vos.CpUserVO;
import nc.scap.pub.itf.IGlobalConstants;
import nc.uap.lfw.core.model.plug.TranslatedRow;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import java.util.List;
import nc.uap.lfw.core.log.LfwLogger;
import nc.scap.scapjj.pub.IConst4scapjj;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.wfm.itf.IWfmTaskQry;
import nc.vo.scapjj.material.Material_HVO;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.wfm.vo.WfmTaskVO;
import nc.uap.cpb.log.CpLogger;
import nc.scap.pub.util.CntUtil;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import java.text.DecimalFormat;
import nc.uap.wfm.exe.WfmCmd;
import org.apache.commons.lang.StringUtils;
import nc.uap.lfw.core.cmd.CmdInvoker;
import java.util.Map;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.uap.ctrl.filemgr.LfwFileDsVO;
import nc.bs.dao.BaseDAO;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import java.util.HashMap;
import nc.scap.dsm.material.wfm.WfmFlwFormVO;
import nc.uap.cpb.org.itf.ICpUserQry;
import java.util.regex.Pattern;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import nc.uap.wfm.utils.WfmCPUtilFacade;
import nc.jdbc.framework.SQLParameter;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.exception.LfwBusinessException;
import java.util.Collection;
import nc.jdbc.framework.processor.ColumnListProcessor;
import java.util.ArrayList;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.file.FileManager;
import nc.uap.lfw.core.cmd.base.CommandStatus;
import nc.bs.framework.common.NCLocator;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.vo.pub.SuperVO;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.scap.jjpub.checkutil.CheckFormState;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.lfw.file.vo.LfwFileVO;
import nc.uap.lfw.core.constants.AppConsts;
import uap.web.bd.pub.AppUtil;
import nc.uap.lfw.jsp.uimeta.UIElement;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.model.plug.TranslatedRows;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import uap.lfw.core.locator.ServiceLocator;
import nc.uap.wfm.vo.WfmFormInfoCtx;
import nc.uap.lfw.core.event.MouseEvent;
/** 
 * 信息列表默认逻辑
 */
public class MaterialListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="materialuiComps.material_cardwin";
  private static final String CARD_WIN_TITLE="编辑";
  /** 
 * 主数据加载逻辑
 * @param dataLoadEvent
 */
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
    final Dataset ds = dataLoadEvent.getSource();
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()) {

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
  /** 
 * 主数据选中逻辑
 * @param datasetEvent
 */
  public void onAfterRowSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
		Dataset otheruser_b = this.getCurrentView().getViewModels()
				.getDataset("otheruser_b");
		otheruser_b.setOrderByPart("order by def2 asc");
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
		String pk = JjUtil.getDSvalue(ds, ds.getSelectedRow(),
				ds.getPrimaryKeyField());
		refshData(pk, null);
  }
  public String getSysId(){
    return LfwFileConstants.SYSID_BAFILE;
  }
  /** 
 * 获取文件管理器
 * @author taoye 2013-7-22
 */
  private FileManager getFileManager(  String filemanager,  String sysid){
    FileManager fileManager = null;
		if (StringUtils.isNotBlank(filemanager)) {
			fileManager = (FileManager) LfwClassUtil.newInstance(filemanager);
		} else if (StringUtils.isNotBlank(sysid))
			fileManager = FileManager.getSystemFileManager(sysid);
		return fileManager;
  }
  /** 
 * 获取不带文件类型的文件名
 * @author taoye 2013-7-22
 */
  private String getSimpleName(  String filename,  String type){
    if (null == filename || filename.equals(""))
			return "";
		if (null == type || type.equals(""))
			return filename;
		if (type.equals("NaN")) {
			return filename;
		}
		String simplename = "";
		Pattern pattern = Pattern.compile("\\." + type + "$");
		simplename = pattern.matcher(filename).replaceAll("");

		return simplename;
  }
  /** 
 * 获取文件大小字符串
 * @author taoye 2013-7-22
 */
  private String getSizeStr(  long size){
    String sizestr = size + "B";
		if (size <= 0)
			sizestr = size + "B";
		else {
			sizestr = size + "";
			int sizelen = sizestr.length();
			if (sizelen <= 3)
				sizestr = size + "B";
			else if (sizelen > 3 && sizelen <= 6) {
				double newsize = size / Math.pow(10.0, 3);
				DecimalFormat dicf = new DecimalFormat("0.00");
				sizestr = dicf.format(newsize) + "K";
			} else if (sizelen > 6 && sizelen <= 9) {
				double newsize = size / Math.pow(10.0, 6);
				DecimalFormat dicf = new DecimalFormat("0.00");
				sizestr = dicf.format(newsize) + "M";
			} else {
				double newsize = size / Math.pow(10.0, 9);
				DecimalFormat dicf = new DecimalFormat("0.00");
				sizestr = dicf.format(newsize) + "G";
			}
		}
		return sizestr;
  }
  /** 
 * 将文件信息更新至文件列表数据集
 * @author taoye 2013-7-22
 */
  public void bindFiletoDS(  LfwFileVO[] files,  Dataset ds) throws CpbBusinessException {
    ds.clear();
		List<LfwFileDsVO> list = new ArrayList<LfwFileDsVO>();
		if (files != null) {
			for (LfwFileVO file : files) {
				LfwFileDsVO vo = new LfwFileDsVO();
				vo.setId(file.getPk_lfwfile());
				vo.setName(getSimpleName(file.getDisplayname(),
						file.getFiletypo()));
				vo.setType(file.getFiletypo());
				vo.setSize(getSizeStr(file.getFilesize()));
				vo.setLastmodified(file.getLastmodifytime());
				vo.setFilemanager(file.getFilemgr());
				String pk_user = file.getLastmodifyer();
				if (null != pk_user && !pk_user.equals("")) {
					CpUserVO user = NCLocator.getInstance()
							.lookup(ICpUserQry.class).getUserByPk(pk_user);
					if (user != null) {
						vo.setLastmodifier(user.getUser_code());
					} else {
						vo.setLastmodifier(pk_user);
					}
				}
				list.add(vo);

			}
		}
		new SuperVO2DatasetSerializer().serialize(
				(SuperVO[]) list.toArray(new LfwFileDsVO[0]), ds);
		ds.getCurrentRowData().getRows();
  }
  public void refshData(  String billitem,  String billtype){
    String sysid = getSysId();
		String filemgr = (String) LfwRuntimeEnvironment.getWebContext()
				.getWebSession()
				.getOriginalParameter(LfwFileConstants.Par_FILEMANAGER);
		FileManager filemanager = getFileManager(filemgr, sysid);
		try {
			LfwFileVO[] files = filemanager.getAttachFileByItemID(billitem,
					billtype);
			Dataset ds = AppLifeCycleContext.current().getWindowContext()
					.getViewContext("main").getView().getViewModels()
					.getDataset("ds_attach");
			ds.clear();
			bindFiletoDS(files, ds);
		} catch (LfwBusinessException e) {
			CpLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
  }
  /** 
 * 外部触发刷新
 * @param keys
 */
  public void doRefresh(  Map<?,?> keys){
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
		// onRefresh(false);
		// }
		Dataset ds = this.getMasterDs();
		String pk = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(
				AppConsts.OPEN_BILL_ID);
		LfwRuntimeEnvironment.getWebContext().getWebSession()
				.getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, null);
		this.onDataLoad(new DataLoadEvent(ds));
		this.clearDetailDs();
		LfwRuntimeEnvironment.getWebContext().getWebSession()
				.getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, pk);
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
 * 新增
 * @param mouseEvent
 */
  public void onAdd(  MouseEvent<?> mouseEvent){
    String[] currzllx = (String[]) AppUtil.getAppAttr(currZllx);
		if (currzllx == null || currzllx.length == 0) {
			throw new LfwRuntimeException("必须选择一个资料类型!");
		}
		String cuserid = CntUtil.getCntUserPk();
		String sql = "select count(1) nnum from ("
				+ "select dh.pk_datetype_h from scapjj_poweruser_b ub "
				+ "join scapjj_datetype_h dh on dh.pk_datetype_h=ub.pk_datetype_h "
				+ "where ub.dr=0 and dh.dr=0 and ub.qxlx = '"
				+ IConst4scapjj.QXLX_REP
				+ "' "
				+ /** 维护权限的用户 */
				"and ub.yh = '"
				+ cuserid
				+ "' "
				+ "union all "
				+ "select dh.pk_datetype_h from scapjj_powerrole_b rb "
				+ "join scapjj_datetype_h dh on dh.pk_datetype_h=rb.pk_datetype_h "
				+ "join cp_userrole ur on ur.pk_role=rb.jsmx "
				+ "where rb.dr=0 and dh.dr=0 and rb.qxlx = '"
				+ IConst4scapjj.QXLX_REP + "' " + /** 维护权限的角色 */
				"and ur.pk_user = '" + cuserid
				+ "' )cc where cc.pk_datetype_h ='" + currzllx[0] + "' ";
		Object nnum = null;
		try {
			nnum = getBaseDAO().executeQuery(sql, new ColumnProcessor());
		} catch (DAOException e) {
			e.printStackTrace();
			throw new LfwRuntimeException(e.getMessage());
		}
		if (nnum == null || Integer.valueOf(nnum.toString()) == 0) {
			throw new LfwRuntimeException("当前登录用户没有资料类型 : " + currzllx[1]
					+ " 的维护权限!");
		}

		this.onAdd_wfm();
		OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(3);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_ADD);
		paramMap.put("model", "nc.scap.dsm.material.MaterialPageModel");
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * 新增-流程业务
 */
  private void onAdd_wfm(){
    this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_BillID,
				null);
		this.resetWfmParameter();
		this.getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_FolwTypePk, this.getFlwTypePk());
  }
  /** 
 * 打开页面与当前页面属于同一app范围，每次需要清除app中的变量
 */
  private void resetWfmParameter(){
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
 * @return
 */
  public String getFlwTypePk(){
    // return "0001ZZ100000000HGRSX";
		return "0001ZZ100000000HGQ9D";
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
		new CheckFormState().execute(ds, "当前单据已审核通过,不能修改!");
		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(4);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		String node_type = JjUtil.getStr(AppUtil
				.getAppAttr(IGlobalConstants.NODE_TYPE));
		if ("power".equals(node_type)) {
			paramMap.put("model", "nc.scap.dsm.material.MaterialPowerPageModel");
		} else {
			paramMap.put("model", "nc.scap.dsm.material.MaterialPageModel");
		}
		paramMap.put(WfmConstants.WfmUrlConst_billID, pkValue);

		props.setParamMap(paramMap);

		this.onEdit_wfm(pkValue);

		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * 编辑-流程业务
 * @param pkValue
 */
  private void onEdit_wfm(  String pkValue){
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
				this.getCurrentAppCtx().addAppAttribute(
						WfmConstants.WfmUrlConst_TaskPk, task.getPk_task());
			}
		} catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
  }
  /** 
 * 删除
 * @param scriptEvent
 */
  public void onDelete(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待删除数据");
		}
		new CheckFormState().execute(ds, null);
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
 * @param mouseEvent
 */
  public void onFlow(  MouseEvent<?> mouseEvent){
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
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onStart(  MouseEvent<?> mouseEvent) throws BusinessException {
    CmdInvoker.invoke(new UifEnableCmd(getMasterDsId(), true));
  }
  /** 
 * 停用
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onStop(  MouseEvent<?> mouseEvent) throws BusinessException {
    CmdInvoker.invoke(new UifEnableCmd(getMasterDsId(), false));
  }
  /** 
 * 附件
 * @param mouseEvent
 * @throws BusinessException
 */
  public void onAttchFile(  MouseEvent<?> mouseEvent){
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
  public void onPrint(  MouseEvent<?> mouseEvent){
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
  public void doOrgChange(  Map keys){
    CmdInvoker.invoke(new UifDatasetLoadCmd(getMasterDsId()) {
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
		clearDetailDs();
		hiddenTab4Yh();
  }
  /** 
 * 填报和查询是 隐藏其他人员页签 隐藏其他人员页签
 */
  private void hiddenTab4Yh(){
    String node_type = (String) AppUtil
				.getAppAttr(IGlobalConstants.NODE_TYPE);
		if ("add".equals(node_type) || "approve".equals(node_type)
				|| "query".equals(node_type)) {
			UIMeta uimeta = (UIMeta) LfwRuntimeEnvironment.getWebContext()
					.getUIMeta();
			UITabItem split = (UITabItem) UIElementFinder.findElementById(
					uimeta, "tabitem_otheruser_b");
			split.setVisible(false);
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

		Dataset ds = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView().getViewModels()
				.getDataset("ds_attach");
		ds.clear();
  }
  /** 
 * 查询pluguin
 * @param keys
 */
  public void doQueryChange(  Map keys){
	//---------------start------modify by yhl 2015-10-10--------------------------------------------
	//解决 提交到流程中心时提示   “业务类型[资料维护流程]下没有您可以发起的流程！” 问题
	AppUtil.addAppAttr("$$$$$$$$WfmCurOrg", CntUtil.getCntOrgPk());
	//---------------end--------------------------------------------------
    FromWhereSQL whereSql = (FromWhereSQL) keys
				.get(FromWhereSQL.WHERE_SQL_CONST);
		CmdInvoker.invoke(new QueryCmd(MAIN_VIEW_ID, getMasterDsId(), whereSql
				.getWhere() + " and " + getWhereSql4TreeNode(null)));
  }
  @Override protected String getMasterDsId(){
    return "material_h";
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
			if (isshow)
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
    onRefresh(false);
		Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待查看数据");
		}
		addDjcs();
		onRefresh(false);
		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(CARD_WIN_ID, "查看");
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(4);
		paramMap.put(AppConsts.OPE_SIGN, "SCAN");
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		String node_type = JjUtil.getStr(AppUtil
				.getAppAttr(IGlobalConstants.NODE_TYPE));
		if ("power".equals(node_type)) {
			paramMap.put("model", "nc.scap.dsm.material.MaterialPowerPageModel");
		} else {
			paramMap.put("model", "nc.scap.dsm.material.MaterialPageModel");
		}
		paramMap.put(WfmConstants.WfmUrlConst_billID, pkValue);
		props.setParamMap(paramMap);
		onEdit_wfm(pkValue);
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * 增加查看次数
 */
  private void addDjcs(){
    Dataset ds = this.getMasterDs();
		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));
		// 未发布时 点击不增加次数
		String fbzt = (String) row.getValue(ds.nameToIndex("fbzt"));
		if ("2".equals(fbzt)) {
			return;
		}
		// 加技术锁（主键锁）
		try {
			BDPKLockUtil.lockString(pkValue);
		} catch (BusinessException e1) {
			e1.printStackTrace();
			throw new LfwRuntimeException(e1.getMessage());
		}
		String sql = "update " + ds.getTableName()
				+ " set djcs=nvl(djcs,0)+1 where " + ds.getPrimaryKeyField()
				+ "=?";
		SQLParameter par = new SQLParameter();
		par.addParam(pkValue);
		try {
			getBaseDAO().executeUpdate(sql, par);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new LfwRuntimeException(e.getMessage());
		}
  }
  /** 
 * 当前选中资料类型 String[]{主键,名称}
 */
  final public static String currZllx="currZllx";
  public void doSelectDataTypeTreeNode(  Map keys){
    TranslatedRow selRow = (TranslatedRow) keys.get(OPERATE_ROW);
		String zllx = JjUtil.getStr(selRow.getValue("pk_datetype_h"));
		String zllx_name = JjUtil.getStr(selRow.getValue("name"));
		if (!zllx.equals(""))
			AppUtil.addAppAttr(currZllx, new String[] { zllx, zllx_name });

		String wheresql = getWhereSql4TreeNode(null);
		CmdInvoker
				.invoke(new QueryCmd(MAIN_VIEW_ID, getMasterDsId(), wheresql));
		hiddenTab4Yh();

		clearDetailDs();
  }
  /** 
 * 点击树后组装sql, 考虑当前打开节点的因素, 考虑资料类型的授权,
 * @param selRow
 * @return
 */
  private String getWhereSql4TreeNode(  TranslatedRow selRow){
    String dataTypeCode = LfwRuntimeEnvironment.getWebContext().getOriginalParameter("data_type");
		String[] dyPks = JjUtil.getDataTypePksByUseridAndNodeTypeOrDataType(dataTypeCode);//获取当前登陆用户可以看到的本级资料类型pk数组
		String allDataTypeIds = JjUtil.getAllSameLevelDataTypeIds(dyPks);//根据 资料类型主键数组 通过递归的方式 获取该资料类型主键数组里所有资料类型主键及其所有上级资料类型的pk
		
		String wheresql = " dr=0 ";
		String node_type = JjUtil.getStr(AppUtil
				.getAppAttr(IGlobalConstants.NODE_TYPE));
		String pk_datetype_h = (String) AppUtil.getAppAttr("pk_datetype_h");
		if (selRow != null) {
			if (selRow.getKeys().length == 0) {// onDataLoad 加载初始化数据时,不加载任何数据
				return " 1 = 2 ";
			}
			String zllx = JjUtil.getStr(selRow.getValue("pk_datetype_h"));
			wheresql += "and zllx = '" + zllx + "' ";
		} else if (pk_datetype_h != null
				&& !IGlobalConstants.ORG_TREE_ROOT.equals(pk_datetype_h)) {
			wheresql += "and zllx = '" + pk_datetype_h + "' ";
		}
		
		String statussql = "and formstate = '"
				+ IGlobalConstants.SCAPPM_BILLSTATE_END + "' " + /** *  * 审批状态为审批结束 */" and fbzt = '" + IConst4scapjj.FBZT[0] + "' ";/** 发布状态为 发布 */
		// 下面是打开节点类型不同的限定
		String pk_org = CntUtil.getCntOrgPk();
		String cuserid = CntUtil.getCntUserPk();
		if ("add".equals(node_type)) {// 资料维护 填报
			if (CntUtil.CtnUserIsCompanyUser()) {
				wheresql += "and pk_org = '" + pk_org + "'";/** 只能看到制单人等于当前登陆人的资料 */ 
				/** 当前登陆人组织 */ 
			}
			wheresql += " and billmaker = '"+cuserid+"'";
			
//			if(fatherAndSameLevelDataTypeIds!=null)
			wheresql+=" and zllx in ("+allDataTypeIds+")";
//			else
			wheresql += "and zllx in ("
					+ "select dh.pk_datetype_h from scapjj_poweruser_b ub "
					+ "join scapjj_datetype_h dh on dh.pk_datetype_h=ub.pk_datetype_h "
					+ "where ub.dr=0 and dh.dr=0 and dh.enablestate='Y' and ub.qxlx = '"    /**查询scapjj_datetype_h表数据时加 是否启用 等于 是 的过滤条件dh.enablestate='Y'  */
					+ IConst4scapjj.QXLX_REP
					+ "' "
					+ /** 维护权限的用户 */
					"and ub.yh = '"
					+ cuserid
					+ "' "
					+ "union all "
					+ "select dh.pk_datetype_h from scapjj_powerrole_b rb "
					+ "join scapjj_datetype_h dh on dh.pk_datetype_h=rb.pk_datetype_h "
					+ "join cp_userrole ur on ur.pk_role=rb.jsmx "
					+ "where rb.dr=0 and dh.dr=0 and dh.enablestate='Y' and rb.qxlx = '"                             /**查询scapjj_datetype_h表数据时加 是否启用 等于 是 的过滤条件dh.enablestate='Y'  */
					+ IConst4scapjj.QXLX_REP + "' " + /** 维护权限的角色 */
					"and ur.pk_user = '" + cuserid + "' " + ") ";

		} else if ("approve".equals(node_type)) {// 资料维护审核
			/** 此段代码暂时注释掉，不管当前登录用户有没有某个资料类型的管理、维护、查阅权限，只要在审批流程引擎里配置了当前登录用户可以审批，
			 * 那么打开审核节点后即使看不到资料类型（原因，通过功能节点传入的资料类型与待审批单据对应的资料类型不匹配），也可以看到待办数据并允许审批。目前和资料类型权限没有关系。
			 * 
			 * 如果想控制某个用户必须能看到某个资料类型后才可以审批，那么取消注释此行代码。想要正常使用审批功能需要进行如下配置： 用户待办单据对应的资料类型与通过功能节点传入的资料类型要对应。
			 * 或者 不通过功能节点传入资料类型参数（如果不通过功能节点传入资料类型参数，加与不加此行代码都可以 正常使用）
			 * 
			 * */
//			wheresql+=" and zllx in ("+allDataTypeIds+")"; //暂时注释掉
			wheresql += "and formstate = '"
					+ IGlobalConstants.SCAPPM_BILLSTATE_RUN + "' ";
			String pk_user = LfwRuntimeEnvironment.getLfwSessionBean()
					.getPk_user();
			String pkFieldName = this.getMasterDs().getPrimaryKeyField();
			wheresql += "and " + pkFieldName + " in "
					+ "(select pk_frmins from wfm_task " + "where pk_owner='"
					+ pk_user + "' and state='State_Run' "
					+ "and pk_flowtype='" + this.getFlwTypePk() + "')";
			/** 审批状态为审批结束 */
		} else if ("power".equals(node_type)) {// 资料权限维护
//			wheresql += "and formstate = '"
//					+ IGlobalConstants.SCAPPM_BILLSTATE_END + "' ";
			wheresql+=statussql; /* 审批状态为审批结束 /** 发布状态为 发布 */
			
//			if(fatherAndSameLevelDataTypeIds!=null)
				wheresql+=" and zllx in ("+allDataTypeIds+")";
//			else
			wheresql += "and zllx in ("
					+ "select dh.pk_datetype_h from scapjj_poweruser_b ub "
					+ "join scapjj_datetype_h dh on dh.pk_datetype_h=ub.pk_datetype_h "
					+ "where ub.dr=0 and dh.dr=0 and dh.enablestate='Y' and ub.qxlx = '"     /**查询scapjj_datetype_h表数据时加 是否启用 等于 是 的过滤条件dh.enablestate='Y'  */
					+ IConst4scapjj.QXLX_MAN
					+ "' "
					+ /** 管理权限的用户 */
					"and ub.yh = '"
					+ cuserid
					+ "' "
					+ "union all "
					+ "select dh.pk_datetype_h from scapjj_powerrole_b rb "
					+ "join scapjj_datetype_h dh on dh.pk_datetype_h=rb.pk_datetype_h "
					+ "join cp_userrole ur on ur.pk_role=rb.jsmx "
					+ "where rb.dr=0 and dh.dr=0 and dh.enablestate='Y' and rb.qxlx = '"      /**查询scapjj_datetype_h表数据时加 是否启用 等于 是 的过滤条件dh.enablestate='Y'  */
					+ IConst4scapjj.QXLX_MAN + "' " + /** 管理权限的角色 */
					"and ur.pk_user = '" + cuserid + "' " + ") ";

			// wheresql += "and " +
			// pkFieldName+" in (select h.pk_datetype_h from scapjj_datetype_h h join scapjj_poweruser_b b on h.pk_datetype_h = b.pk_datetype_h where b.yh = '"
			// + pk_user + "' and h.fbzt = '1' and h.formstate = '" +
			// IGlobalConstants.SCAPPM_BILLSTATE_END + "' ) ";
			/** 审批状态为审批结束 */
		} else if ("query".equals(node_type)) {// 资料查询
			wheresql+=statussql; /* 审批状态为审批结束 /** 发布状态为 发布 */
			
//			if(fatherAndSameLevelDataTypeIds!=null)
				wheresql+=" and zllx in ("+allDataTypeIds+")";
			// else
			//
			 wheresql += "and (zllx in ("
			 + "select dh.pk_datetype_h from scapjj_poweruser_b ub "
			 +
			 "join scapjj_datetype_h dh on dh.pk_datetype_h=ub.pk_datetype_h "
			 + "where ub.dr=0 and dh.dr=0 and dh.enablestate='Y' and ub.qxlx = '"    /**查询scapjj_datetype_h表数据时加 是否启用 等于 是 的过滤条件dh.enablestate='Y'  */
			 + IConst4scapjj.QXLX_BRO
			 + "' "
			 + /** 浏览权限的用户 */
			 "and ub.yh = '"
			 + cuserid
			 + "' "
			 + "union all "
			 + "select dh.pk_datetype_h from scapjj_powerrole_b rb "
			 +
			 "join scapjj_datetype_h dh on dh.pk_datetype_h=rb.pk_datetype_h "
			 + "join cp_userrole ur on ur.pk_role=rb.jsmx "
			 + "where rb.dr=0 and dh.dr=0 and dh.enablestate='Y' and rb.qxlx = '"     /**查询scapjj_datetype_h表数据时加 是否启用 等于 是 的过滤条件dh.enablestate='Y'  */
			 + IConst4scapjj.QXLX_BRO
			 + "' "
			 + /** 浏览权限的角色 */
			 "and ur.pk_user = '"
			 + cuserid
			 + "' "
			 + ")  or pk_material_h in ("
			 + "select distinct h.pk_material_h from scapjj_otheruser_b b "
			 + "join scapjj_material_h h on h.pk_material_h=b.pk_material_h "
			 + "where h.dr=0 and b.dr=0 and  b.yh = '" + cuserid + "' " + /**
			 *
			 *
			 * 资料的其他人员
			 */
			 ")" +
			" or pk_material_h in ("         /**当前登陆用户默认可以查看  制单人等于当前登录人并且审批通过的资料*/
			+ "select distinct mh.pk_material_h from scapjj_material_h mh "
			+ "where mh.dr=0 and  mh.billmaker = '" + cuserid + "' " +
			") " +
			")";
		}

		// 下面是资料类型的授权限定
		// 判断 资料类型的角色 用户 资料的其他人员

//		if ("add".equals(node_type)) {// 资料维护 填报
//			wheresql += "and zllx in ("
//					+ "select dh.pk_datetype_h from scapjj_poweruser_b ub "
//					+ "join scapjj_datetype_h dh on dh.pk_datetype_h=ub.pk_datetype_h "
//					+ "where ub.dr=0 and dh.dr=0 and ub.qxlx = '"
//					+ IConst4scapjj.QXLX_REP
//					+ "' "
//					+ /** 维护权限的用户 */
//					"and ub.yh = '"
//					+ cuserid
//					+ "' "
//					+ "union all "
//					+ "select dh.pk_datetype_h from scapjj_powerrole_b rb "
//					+ "join scapjj_datetype_h dh on dh.pk_datetype_h=rb.pk_datetype_h "
//					+ "join cp_userrole ur on ur.pk_role=rb.jsmx "
//					+ "where rb.dr=0 and dh.dr=0 and rb.qxlx = '"
//					+ IConst4scapjj.QXLX_REP + "' " + /** 维护权限的角色 */
//					"and ur.pk_user = '" + cuserid + "' " + ") ";
//
//		} else if ("power".equals(node_type)) {// 资料权限维护
//			wheresql += "and zllx in ("
//					+ "select dh.pk_datetype_h from scapjj_poweruser_b ub "
//					+ "join scapjj_datetype_h dh on dh.pk_datetype_h=ub.pk_datetype_h "
//					+ "where ub.dr=0 and dh.dr=0 and ub.qxlx = '"
//					+ IConst4scapjj.QXLX_MAN
//					+ "' "
//					+ /** 管理权限的用户 */
//					"and ub.yh = '"
//					+ cuserid
//					+ "' "
//					+ "union all "
//					+ "select dh.pk_datetype_h from scapjj_powerrole_b rb "
//					+ "join scapjj_datetype_h dh on dh.pk_datetype_h=rb.pk_datetype_h "
//					+ "join cp_userrole ur on ur.pk_role=rb.jsmx "
//					+ "where rb.dr=0 and dh.dr=0 and rb.qxlx = '"
//					+ IConst4scapjj.QXLX_MAN + "' " + /** 管理权限的角色 */
//					"and ur.pk_user = '" + cuserid + "' " + ") ";
//		} else if ("query".equals(node_type)) {// 资料查询
//			wheresql += "and (zllx in ("
//					+ "select dh.pk_datetype_h from scapjj_poweruser_b ub "
//					+ "join scapjj_datetype_h dh on dh.pk_datetype_h=ub.pk_datetype_h "
//					+ "where ub.dr=0 and dh.dr=0 and ub.qxlx = '"
//					+ IConst4scapjj.QXLX_BRO
//					+ "' "
//					+ /** 浏览权限的用户 */
//					"and ub.yh = '"
//					+ cuserid
//					+ "' "
//					+ "union all "
//					+ "select dh.pk_datetype_h from scapjj_powerrole_b rb "
//					+ "join scapjj_datetype_h dh on dh.pk_datetype_h=rb.pk_datetype_h "
//					+ "join cp_userrole ur on ur.pk_role=rb.jsmx "
//					+ "where rb.dr=0 and dh.dr=0 and rb.qxlx = '"
//					+ IConst4scapjj.QXLX_BRO
//					+ "' "
//					+ /** 浏览权限的角色 */
//					"and ur.pk_user = '"
//					+ cuserid
//					+ "' "
//					+ ")  or pk_material_h in ("
//					+ "select distinct h.pk_material_h from scapjj_otheruser_b b "
//					+ "join scapjj_material_h h on h.pk_material_h=b.pk_material_h "
//					+ "where h.dr=0 and b.dr=0 and  b.yh = '" + cuserid + "' " + /**
//					 * 
//					 * 
//					 * 资料的其他人员
//					 */
//					"))";
//		}

		return wheresql;
  }
  public void onAdd_OtherUser(  MouseEvent mouseEvent){
    Dataset ds = getMasterDs();
		String pk = JjUtil.getDSvalue(ds, ds.getSelectedRow(),
				ds.getPrimaryKeyField());
		if (pk == null || pk.trim().length() == 0) {
			throw new LfwRuntimeException("未选中资料!");
		}
		OpenProperties props = new OpenProperties(
				IConst4scapjj.PUBLIC_VIEW_SELECTUSERBYORG, "选择用户", "800", "600");
		props.setButtonZone(false);
		this.getCurrentAppCtx().getCurrentWindowContext().popView(props);
  }
  public void onDelOtherUser(  MouseEvent mouseEvent){
    Dataset ouser_ds = getCurrentView().getViewModels().getDataset(
				"otheruser_b");
		String pk = JjUtil.getDSvalue(ouser_ds, ouser_ds.getSelectedRow(),
				ouser_ds.getPrimaryKeyField());
		if (pk.equals("")) {
			throw new LfwRuntimeException("未选中人员!");
		}
		// 加技术锁（主键锁）
		try {
			BDPKLockUtil.lockString(pk);
		} catch (BusinessException e1) {
			e1.printStackTrace();
			throw new LfwRuntimeException(e1.getMessage());
		}
		String sql = "update scapjj_otheruser_b set dr = 1 where pk_otheruser = ?";
		SQLParameter par = new SQLParameter();
		par.addParam(pk);
		try {
			getBaseDAO().executeUpdate(sql, par);
			onRefresh(false);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new LfwRuntimeException(e.getMessage());
		}
  }
  public void doAddSelectUser(  Map keys){
    TranslatedRows selRows = (TranslatedRows) keys.get(OPERATE_ROW);
		if (selRows != null) {
			Dataset ds = getMasterDs();
			String pk = JjUtil.getDSvalue(ds, ds.getSelectedRow(),
					ds.getPrimaryKeyField());
			if (pk == null || pk.trim().length() == 0) {
				throw new LfwRuntimeException("未选中资料!");
			}
			// 加技术锁（主键锁）
			try {
				BDPKLockUtil.lockString(pk);
			} catch (BusinessException e1) {
				e1.printStackTrace();
				throw new LfwRuntimeException(e1.getMessage());
			}

			String[] users = selRows.getValue("cuserid").toArray(new String[0]);
			String[] userCodes = selRows.getValue("user_code").toArray(
					new String[0]);
			String sql = "select yh from scapjj_otheruser_b where dr = 0 and pk_material_h = '"
					+ pk + "' ";
			ArrayList<Otheruser_BVO> bvos = new ArrayList<Otheruser_BVO>();
			try {
				List<String> list = (List<String>) getBaseDAO().executeQuery(
						sql, new ColumnListProcessor());
				if (list != null && list.size() > 0) {
					for (int i = 0; i < users.length; i++) {
						boolean add = true;
						for (int j = 0; j < list.size(); j++) {
							if (list.get(j).equals(users[i])) {
								add = false;
							}
						}
						if (add) {
							Otheruser_BVO bvo = new Otheruser_BVO();
							bvo.setDef2(userCodes[i]);
							bvo.setYh(users[i]);
							bvo.setPk_material_h(pk);
							bvos.add(bvo);
						}
					}
				} else {
					for (int i = 0; i < users.length; i++) {
						Otheruser_BVO bvo = new Otheruser_BVO();
						bvo.setDef2(userCodes[i]);
						bvo.setYh(users[i]);
						bvo.setPk_material_h(pk);
						bvos.add(bvo);
					}
				}
				if (bvos.size() > 0) {
					getBaseDAO().insertVOArrayWithPK(
							bvos.toArray(new Otheruser_BVO[0]));
					onRefresh(false);
				}
			} catch (DAOException e) {
				e.printStackTrace();
				throw new LfwRuntimeException(e.getMessage());
			}
		}
  }
  public void onAfterTabChange(  TabEvent tabEvent){
    UITabComp tabComp = tabEvent.getSource();
		String cntTabItemId = tabComp.getCurrentItem();
		UIMeta uimeta = (UIMeta) LfwRuntimeEnvironment.getWebContext()
				.getUIMeta();
		UIElement menubar_listbody = UIElementFinder.findElementById(uimeta,
				"menubar_listbody");
		menubar_listbody.setVisible(false);
		if ("0".equals(cntTabItemId)) {
			String node_type = (String) AppUtil
					.getAppAttr(IGlobalConstants.NODE_TYPE);
			if ("power".equals(node_type))
				menubar_listbody.setVisible(true);
		}
  }
  public void onSend(  MouseEvent mouseEvent){
    updateFbzt(IConst4scapjj.FBZT[0]);
		onRefresh(false);
		AppInteractionUtil.showShortMessage("发布成功!");
  }
  public void onUnSend(  MouseEvent mouseEvent){
    updateFbzt(IConst4scapjj.FBZT[1]);
		onRefresh(false);
		AppInteractionUtil.showShortMessage("取消发布成功!");
  }
  /** 
 * 更新 发布状态 IConst4scapjj.FBZT
 * @param fbzt
 */
  private void updateFbzt(  String fbzt){
    Dataset ds = getMasterDs();
		String pk = JjUtil.getDSvalue(ds, ds.getSelectedRow(),
				ds.getPrimaryKeyField());
		if (pk == null || pk.trim().length() == 0) {
			throw new LfwRuntimeException("未选中资料!");
		}
		// 加技术锁（主键锁）
		try {
			BDPKLockUtil.lockString(pk);
		} catch (BusinessException e1) {
			e1.printStackTrace();
			throw new LfwRuntimeException(e1.getMessage());
		}
		String sql = "update " + ds.getTableName() + " set fbzt='" + fbzt
				+ "' where " + ds.getPrimaryKeyField() + "=?";
		SQLParameter par = new SQLParameter();
		par.addParam(pk);
		try {
			getBaseDAO().executeUpdate(sql, par);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new LfwRuntimeException(e.getMessage());
		}
  }
  public void onAppr(  MouseEvent mouseEvent){
    onRefresh(false);
		Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待查看数据");
		}
		addDjcs();
		onRefresh(false);
		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(CARD_WIN_ID, "审核");
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(4);
		paramMap.put(AppConsts.OPE_SIGN, "APPR");
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		paramMap.put("model", "nc.scap.dsm.material.MaterialPageModel");
		paramMap.put(WfmConstants.WfmUrlConst_billID, pkValue);
		props.setParamMap(paramMap);
		onEdit_wfm(pkValue);
		this.getCurrentAppCtx().navgateTo(props);
  }
  public void onSubmit(  MouseEvent mouseEvent){
    doTask("agree");
  }
  public void onRecycle(  MouseEvent mouseEvent){
    doTask("recallBack");
  }
  public void onSendback(  MouseEvent mouseEvent){
    doTask("reject");
  }
  protected String getRichVoClazz(){
    return WfmFlwFormVO.class.getName();
  }
  protected WfmFormInfoCtx getWfmFormInfoCtx(){
    Dataset masterDs = this.getMasterDs();
		Dataset[] detailDss = this.getDetailDs(this.getDetailDsIds());
		SuperVO richVO = this.getDs2RichVOSerializer().serialize(masterDs,
				detailDss, this.getRichVoClazz());
		return (WfmFormInfoCtx) richVO;
  }
  /** 
 * 获取任务PK
 * @return String
 */
  private String getPkTask(){
    String pk_task = null;
		Dataset ds = this.getMasterDs();
		String pkValue = (String) ds.getValue(ds.getPrimaryKeyField());
		try {
			String pk_user = LfwRuntimeEnvironment.getLfwSessionBean()
					.getPk_user();
			IWfmTaskQry taskQry = ServiceLocator.getService(IWfmTaskQry.class);
			WfmTaskVO task = taskQry.getLastTaskVOByFormPkAndUserPk(pkValue,
					pk_user);
			if (task != null) {
				pk_task = task.getPk_task();
			}
		} catch (Exception e) {
			LfwLogger.error(e);
			throw new LfwRuntimeException(e.getMessage());
		}
		return pk_task;
  }
  public void doTaskExecute(  Map keys){
    String operation = "";

		doTask(operation);
  }
  public void doTask(  String operation){
    this.resetWfmParameter();
		WfmFormInfoCtx formCtx = this.getWfmFormInfoCtx();
		// 设置流程form
		this.getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_FormInFoCtx, formCtx);
		// 设置流程类型pk
		this.getCurrentAppCtx().addAppAttribute(
				WfmConstants.WfmAppAttr_FolwTypePk, this.getFlwTypePk());
		// 设置任务pk
		if (this.getPkTask() != null) {
			this.getCurrentAppCtx().addAppAttribute(
					WfmConstants.WfmAppAttr_TaskPk, this.getPkTask());
			this.getCurrentAppCtx().addAppAttribute(
					WfmConstants.WfmUrlConst_TaskPk, this.getPkTask());
		}
		// 调用流程
		WfmCmd wfmCmd = new WfmCmd();
		if (operation != null && operation.length() > 0) {
			wfmCmd.wfmParams.setOperator(operation);
		}
		CmdInvoker.invoke(wfmCmd);
		if (CommandStatus.SUCCESS.equals(CommandStatus.getCommandStatus())) {
			CmdInvoker.invoke(new UifUpdateUIDataCmdRV((SuperVO) formCtx,
					getMasterDsId()));
			this.getCurrentAppCtx().closeWindow();
			Dataset ds = this.getMasterDs();
			String pk = LfwRuntimeEnvironment.getWebContext()
					.getOriginalParameter(AppConsts.OPEN_BILL_ID);
			LfwRuntimeEnvironment.getWebContext().getWebSession()
					.getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, null);
			this.onDataLoad(new DataLoadEvent(ds));
			this.clearDetailDs();
			LfwRuntimeEnvironment.getWebContext().getWebSession()
					.getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, pk);
			// onRefresh(false);
		}
  }
  public void onDataLoad_ds_attach(  DataLoadEvent dataLoadEvent){
    // Dataset ds = dataLoadEvent.getSource();
		// CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
  public void onDataLoad_otheruser_b(  DataLoadEvent dataLoadEvent){
    // Dataset ds = dataLoadEvent.getSource();
		// CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
		final Dataset ds = dataLoadEvent.getSource();
		Dataset dsh = AppLifeCycleContext.current().getViewContext().getView()
				.getViewModels().getDataset(getMasterDsId());
		if (dsh.getSelectedIndex() < 0)
			throw new LfwRuntimeException("请选中待查看数据");
		Row row = dsh.getSelectedRow();
		String pkValue = (String) row.getValue(dsh.nameToIndex(dsh
				.getPrimaryKeyField()));
		ds.setLastCondition(Material_HVO.PK_MATERIAL_H + "='" + pkValue + "'");
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()) {

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
}
