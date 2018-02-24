package nc.scap.pub.urge_notice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.ScapDAO;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifDelCmdRV;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
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
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.refnode.IRefNode;
import nc.uap.lfw.core.refnode.NCRefNode;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.portal.log.ScapLogger;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.scapco.work_notice_contacts.notice_contact_type;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import uap.web.bd.pub.AppUtil;
/** 
 * 信息列表默认逻辑
 */
public class Urge_noticeListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="nc.scap.pub.urge.urge_notice_cardwin";
  private static final String CARD_WIN_TITLE="编辑";
  /** 
 * 主数据加载逻辑
 * @param dataLoadEvent
 */
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
	// 因为乜有pagemodle所以写在了这里；
	  // 配置的查询模板业务类型和报告类型字段的过滤
	LfwView searchView = LfwRuntimeEnvironment.getWebContext().getPageMeta().getView("simplequery");
//		FormComp formcomp = (FormComp) searchView.getViewComponents()
//		.getComponent("mainform");
	if (searchView != null) {
		IRefNode[] IRefNodeArray = searchView.getViewModels().getRefNodes();
		NCRefNode refNode1 = (NCRefNode)searchView.getViewModels().getRefNode("E9AA0119_refnode_mainds_notice_type");
		if (refNode1 != null) {
			refNode1.setDataListener("nc.scap.pub.notice_manager.RepportTypeGridRefCtrl");	
		}
		NCRefNode refNode2 = (NCRefNode)searchView.getViewModels().getRefNode("E9AA0119_refnode_mainds_business_type");
		if (refNode1 != null) {
			refNode2.setDataListener("nc.scap.pub.notice_manager.BusinessTypeGridRefCtrl");
		}
	}
		
    Dataset ds = dataLoadEvent.getSource();
    String lastCondition = ds.getLastCondition();
    if (lastCondition == null) {
    	ds.setLastCondition(getWheresql());
    	ds.setOrderByPart(" order by urge_time desc ");
    }
  		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
  }
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
 * 删除
 * @param scriptEvent
 */
  public void onDelete(  MouseEvent mouseEvent){
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
 * 主组织变化
 * @param keys
 */
  public void doOrgChange(  Map<?,?> keys){
    CmdInvoker.invoke(new UifDatasetLoadCmd(this.getMasterDsId()) {
				protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
					String where = AppUtil.getGroupOrgFielterSqlForDesign(false, PK_ORG);
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
    FromWhereSQL whereSql = (FromWhereSQL) keys.get(FromWhereSQL.WHERE_SQL_CONST);
			CmdInvoker.invoke(new QueryCmd(MAIN_VIEW_ID, this.getMasterDsId(), whereSql.getWhere() + " and " + getWheresql()));
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
    return "urge_notice";
  }
  public void onLookClick(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("请选中待查看数据");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(CARD_WIN_ID, "查看", "1200",
				"400");
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		paramMap.put("method_type", "look");
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * 获取SQL
 * @param
 * @return
 */
  public String getWheresql(){
    StringBuffer whereSql = new StringBuffer();
		String pkUser = CntUtil.getCntUserPk();
		String pkOrg = CntUtil.getCntOrgPk();
		String businessType = "";
		String noticeType = "";
		String data_type = "";
		try {
			List<notice_contact_type>  contact_typeList = new ArrayList<notice_contact_type>();
			String sql = " select a.* from scapco_notice_contact_type a inner join scapco_notice_contact_info b " +
					  " on a.pk_contact_type = b.pk_contact_type " +
					  " where  b.pk_user ='" +pkUser+ "' " +
					  " and a.dr = '0'  and a.enablestate = '2' ";
			contact_typeList = (List<notice_contact_type>) ScapDAO.getBaseDAO().executeQuery(sql, new BeanListProcessor(notice_contact_type.class));
			for (int i = 0; i < contact_typeList.size(); i++) {
				notice_contact_type contact_type = contact_typeList.get(i);
				// 拼接 业务类型
				if(contact_type.getYe_type() != null) {
					businessType = businessType + "'" + contact_type.getYe_type() + "'";
					businessType = businessType + ",";
				}
				// 拼接报告类型
				if(contact_type.getReport_type() != null) {
					noticeType = noticeType + "'" + contact_type.getReport_type() + "'";
					noticeType = noticeType + ",";
				}
				// 拼接资料类型
				if(contact_type.getData_type() != null) {
					data_type = data_type + "'" + contact_type.getData_type() + "'";
					data_type = data_type + ",";
				}
			}
		} catch (DAOException e) {
			ScapLogger.error("查询操作出现错误！错误异常：" + e.getMessage());
		}
		
		whereSql.append(" 1=1 ");
//		// 根据业务类型和报告类型 查询相关的填报通知
//		if (businessType != "" && noticeType != "") {
//			whereSql.append(" and (BUSINESS_TYPE in(").append(businessType).append(")")
//			.append(" and NOTICE_TYPE in(").append(noticeType).append("))");
//		}
		if (data_type != "") {
			data_type = data_type.substring(0, data_type.length() - 1);
			if (businessType != "" && noticeType != "") {
				businessType = businessType.substring(0, businessType.length() - 1);
				noticeType = noticeType.substring(0, noticeType.length() - 1);
				whereSql.append(" and ((BUSINESS_TYPE in(").append(businessType)
						.append(")").append(" and NOTICE_TYPE in(")
						.append(noticeType).append("))");
				whereSql.append(" or (DATA_TYPE in(").append(data_type).append(")))");
			}
		} else {
			if (businessType != "" && noticeType != "") {
				businessType = businessType.substring(0, businessType.length() - 1);
				noticeType = noticeType.substring(0, noticeType.length() - 1);
				whereSql.append(" and (BUSINESS_TYPE in(").append(businessType)
						.append(")").append(" and NOTICE_TYPE in(")
						.append(noticeType).append("))");
			}
		}
		uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE_LIANXIREN, businessType);
		uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_REPORT_TYPE_LIANXIREN, noticeType);
		uap.web.bd.pub.AppUtil.addAppAttr(IGlobalConstants.APPATTR_DATA_TYPE_LIANXIREN, data_type);
		// 当前用户不管是通知发送方还是接收方，都只能看当前用户自己接受的催报消息
//		whereSql.append(" and URGE_NOTICE_ORG ='").append(pkOrg).append("' ");
		whereSql.append(" and URGE_RECEPT_MAN ='").append(pkUser).append("' ");

		// 企业只能看到已下发通知的催报数据
		whereSql.append(" AND  EXISTS (SELECT ''  FROM SCAPCO_NOTICE_MANAGER t  WHERE  t.PK_NOTICE = SCAPCO_URGE_NOTICE.PK_NOTICE and t.NOTICE_STATUS = '2')");
		return whereSql.toString();
  }
}
