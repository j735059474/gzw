package nc.scap.library.transfer.export;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sz.common.exception.ExcelException;

import nc.jdbc.framework.processor.BeanListProcessor;
import nc.scap.pt.vos.ScapptApplyHVO;
import nc.scap.pt.vos.ScapptExportTransHVO;
import nc.scap.ptpub.PtConstants;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpPubMethod;
import nc.scap.pub.util.CpWfmUtilFacade;
import nc.scap.pub.util.CpWinUtil;
import nc.scap.pub.util.ScapDAO;
import nc.scap.pub.util.UifExcelImportCmd;
import nc.scap.transfer.land.utils.ILandConstants;
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
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import uap.web.bd.pub.AppUtil;
/** 
 * 信息列表默认逻辑
 */
public class ExportListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="dataimpComps.export_cardwin";
  private static final String CARD_WIN_TITLE="编辑";
  private static final String CARD_WIN_TITLE_VIEW="查看";
  /** 
 * 主数据加载逻辑
 * @param dataLoadEvent
 */
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
    			String wheresql = filterSql();
    			ds.setLastCondition(wheresql);
//    			ds.setExtendAttribute(Dataset.ORDER_PART, "order by pk_org,ts desc ");
				String iisAudit = (String) AppUtil.getAppAttr(ILandConstants.IS_AUDIT);
//				String whpart = "";
//				if("compare".equals(iisAudit)){
//					whpart = " union (select * from scappt_export_trans  where def5 is null )";
//				}
    			ds.setExtendAttribute(Dataset.ORDER_PART, "order by ts desc ");
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
    //	    this.onAdd_wfm();
				OpenProperties props = new OpenProperties(CARD_WIN_ID, CARD_WIN_TITLE);
				props.setButtonZone(false);
				Map<String, String> paramMap = new HashMap<String, String>(2);
				paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_ADD);
				paramMap.put("model", "nc.scap.transfer.model.LandPopPageModel");
				props.setParamMap(paramMap);
				CpPubMethod.resetWfmParameter();
				this.getCurrentAppCtx().navgateTo(props);

				AppUtil.addAppAttr(ILandConstants.SAVE_OPE,ILandConstants.SAVE_SIGN_ADD);
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
				Map<String, String> paramMap = new HashMap<String, String>(2);
				paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
				paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
//				paramMap.put(WfmConstants.WfmUrlConst_billID, pkValue);
				paramMap.put("model", "nc.scap.transfer.model.LandPopPageModel");
				props.setParamMap(paramMap);

//				this.onEdit_wfm(pkValue);
//				CpPubMethod.setWfmInfo(paramMap, pkValue,"E9AI020225", "edit");

				this.getCurrentAppCtx().navgateTo(props);
				AppUtil.addAppAttr(ILandConstants.SAVE_OPE,ILandConstants.SAVE_SIGN_EDIT);
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
    String isAudit = (String) LfwRuntimeEnvironment.getWebContext().getOriginalParameter(ILandConstants.IS_AUDIT);
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
//			if (taskPk == null || taskPk.equals("")) {
//				this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE, WfmConstants.BILLSTATE_BROWSE);
//			}
			CpWfmUtilFacade.setWfmBillState(primaryKeyValue, taskPk);
			// 附件只能查看,需要设为浏览态.
			if (isAudit != null&& ILandConstants.AUDIT_VIEW.equals(isAudit)) {
				this.getCurrentAppCtx().addAppAttribute(WfmConstants.BILLSTATE, WfmConstants.BILLSTATE_BROWSE);
			}
			// 流程附件参数
			Map<String, String> wfmParam = WfmUtilFacade.getFileMgrParamsByTask(taskPk);

			// 附件参数
			Map<String, String> param = UploadFileHelper.BuildDefaultPamater(LfwFileConstants.SYSID_BAFILE, primaryKeyValue, CommonObjectConstants.AttachFileType, "");
			param.put("usescanable", "true");
			param.put("state", String.valueOf(31));

			String title = "附件";
			wfmParam.put("billitem", primaryKeyValue);
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
    Dataset ds = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset(getMasterDsId());
			LfwRuntimeEnvironment.getWebContext().getWebSession()
					.addOriginalParameter("openBillId", null);
			// ds.setCurrentKey(ds.getSelectedIndex() + "1");
			onDataLoad(new DataLoadEvent(ds));
	  
//	  TranslatedRow selRow = (TranslatedRow) keys.get(OPERATE_ROW);
//		if (selRow != null) {
//			Dataset ds = this.getMasterDs();
//			String sign = this.getOperator();
//			if (AppConsts.OPE_EDIT.equals(sign)) {
//				Row savedRow = ds.getSelectedRow();
//				copyTranslateRow2Row(selRow,savedRow,ds);
////				}
//			} else if (AppConsts.OPE_ADD.equals(sign)) {
//				Row savedRow = ds.getEmptyRow();
//				savedRow = copyTranslateRow2Row(selRow,savedRow,ds);
//				ds.addRow(savedRow);
//			}
//		}
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
    CmdInvoker.invoke(new UifDatasetLoadCmd(getMasterDsId()) {
						protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
							// String where = AppUtil.getGroupOrgFielterSqlForDesign(false,
							// PK_ORG);
							String where = filterSql();
							ds.setLastCondition(where);
							ds.setExtendAttribute("ORDER_PART", " order by ts desc");
							return where;
						}

						// 避免翻页时重走缓存
						protected void changeCurrentKey() {
							setChangeCurrentKey(true);
						}
					});
						clearDetailDs();
  }
  /** 
 * 界面渲染条件过滤
 * @param
 * @return where
 */
  public String filterSql(){
    String where = "1=1";
	  					String isAudit = LfwRuntimeEnvironment.getWebContext().getOriginalParameter(ILandConstants.IS_AUDIT);
	  					String saveope = (String) AppUtil.getAppAttr(ILandConstants.SAVE_OPE);
	  					String iisAudit = (String) AppUtil.getAppAttr(ILandConstants.IS_AUDIT);
	  					isAudit = isAudit == null ? saveope : isAudit;
	  					isAudit = isAudit == null ? iisAudit : isAudit;
	  					if (ILandConstants.AUDIT_TRUE.equals(isAudit)||ILandConstants.SAVE_SIGN_AUDIT.equals(isAudit)) {// 国资委审核and formstate = 'Run'
//	  						where += " and dr = 0 and "+LandVO.FORMSTATE+"='"+IGlobalConstants.SCAPPM_BILLSTATE_RUN+"'"+LandUtil.getAllWfmTaskByUserPK(null, Task.State_Run, LandVO.PK_LAND,getFlwTypePk());
	  					} else if (ILandConstants.AUDIT_FALSE.equals(isAudit)
	  							||ILandConstants.SAVE_SIGN_ADD.equals(isAudit)
	  							||ILandConstants.SAVE_SIGN_EDIT.equals(isAudit)) {// 企业申报
//	  						where += " and dr = 0 and pk_org=" + "'" + CntUtil.getCntOrgPk()+ "'";
	  						where += " and dr = 0 and def5 is null ";
	  					} else if (ILandConstants.AUDIT_VIEW.equals(isAudit)
	  							||"compare".equals(isAudit)
	  							) {// 查看（org条件）
	  						where += " and dr = 0 and def4 is not null ";
	  					}
	  					return where;
  }
  /** 
 * 查询plugin
 * @param keys
 */
  public void doQueryChange(  Map<?,?> keys){
    FromWhereSQL whereSql = (FromWhereSQL) keys
					.get(FromWhereSQL.WHERE_SQL_CONST);
			String sql = whereSql.getWhere() + " and " + this.filterSql();
			CmdInvoker.invoke(new QueryCmd(MAIN_VIEW_ID, getMasterDsId(), sql));
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
    return "scappt_export_trans";
  }
  public void onAfterRowUnSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
			CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
  }
  public void onExport(  MouseEvent mouseEvent){
    UifExcelImportCmd cmd = new UifExcelImportCmd(
				  "nc.scap.library.transfer.export.util.ExportImportUtil","dataimpComps.export_listwin");
		  CmdInvoker.invoke(cmd);
  }
  public void onView(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
			if (ds.getSelectedIndex() < 0) {
				throw new LfwRuntimeException("请选中待查看数据");
			}

			Row row = ds.getSelectedRow();
			String pkValue = (String) row.getValue(ds.nameToIndex(ds
					.getPrimaryKeyField()));

			OpenProperties props = new OpenProperties(CARD_WIN_ID,
					CARD_WIN_TITLE_VIEW);
			props.setButtonZone(false);
			Map<String, String> paramMap = new HashMap<String, String>(3);
			paramMap.put(AppConsts.OPE_SIGN, ILandConstants.SAVE_SIGN_VIEW);
			paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
			paramMap.put("model", "nc.scap.transfer.model.LandPopPageModel");
			props.setParamMap(paramMap);

//			this.onEdit_wfm(pkValue);
//			CpPubMethod.setWfmInfo(paramMap, pkValue,
//				"E9AI020225", ILandConstants.BTN_WF);

			this.getCurrentAppCtx().navgateTo(props);
			AppUtil.addAppAttr(ILandConstants.SAVE_OPE,
					ILandConstants.SAVE_SIGN_VIEW);
  }
  public void onRefresh(  MouseEvent mouseEvent){
    Dataset ds = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset(getMasterDsId());
				LfwRuntimeEnvironment.getWebContext().getWebSession()
						.addOriginalParameter("openBillId", null);
				// ds.setCurrentKey(ds.getSelectedIndex() + "1");
				onDataLoad(new DataLoadEvent(ds));
  }
//  public void onCompare(  MouseEvent mouseEvent){
//    Dataset main_ds = CpWinUtil.getDataset(getMasterDsId());
//    Row[] rows = main_ds.getCurrentRowData().getRows();
//    
//    Map<String, String> paramMap = new HashMap<String, String>();
//    paramMap.put("model", "nc.scap.transfer.model.LandPageModel");
//    paramMap.put(PtConstants.NODE_TYPE, "compare");
//    AppUtil.addAppAttr(PtConstants.NODE_TYPE, "compare");
//    OpenProperties props = new OpenProperties(
//			"dataimpComps.export_listwin_compare", "对比查看", "-1", "-1",
//			paramMap);
//	props.setButtonZone(false);
//	AppLifeCycleContext.current().getApplicationContext()
//	.navgateTo(props);
//  }
  
  public void onCompare(  MouseEvent mouseEvent){
	  try{
		  //删除DEF5=1的数据（删除进场交易过来的数据）***
		  ScapDAO.executeUpdate("delete from scappt_export_trans t where t.def5 = '1' ");
		  //交易中心导入无数据;进场交易无数据给出提示，终止比较
		  String sqle = " select * from scappt_export_trans e where e.dr = 0 ";
//		  String sqla = " select * from scappt_apply_h a where a.dr = 0 and a.vdef1 = 'gpzr' ";
		  List<ScapptExportTransHVO> listmNullE = (List<ScapptExportTransHVO>) ScapDAO.executeQuery(sqle, new BeanListProcessor(ScapptExportTransHVO.class));
//		  List<ScapptApplyHVO> listmNUllA = (List<ScapptApplyHVO>) ScapDAO.executeQuery(sqla, new BeanListProcessor(ScapptApplyHVO.class));
		  if(listmNullE.size()==0){
			  throw new LfwRuntimeException("请完善产权交易中心导入数据");
		  }
//		  if(listmNUllA.size()==0){
//			  throw new LfwRuntimeException("请完善进场交易数据");
//		  }
		  //重置DEF4=1的数据（重置对比数据）
		  ScapDAO.executeUpdate("UPDATE scappt_export_trans e SET def4 = NULL WHERE def4 = '1'");
		  //进场交易记录插入
		  List<ScapptExportTransHVO> listm1 = new ArrayList<ScapptExportTransHVO>();
	  
		 StringBuffer stb = new StringBuffer();
		 stb.append(" SELECT a.pk_rorg pk_sgroup, ");//委托单位
		 stb.append(" a.pk_group pk_org, ");//省属企业集团
		 stb.append(" b.vcassessname def1, ");//标的物
		 stb.append(" t.vbaddress vwperiod, ");//地址
		 stb.append(" b.vcdetail atrdata, ");//面积/数量
		 stb.append(" t.vacceptno vacceptno, ");//批准文件
		 stb.append(" t.vpnumber billno, ");//备案审批单位(含文号)
		 stb.append(" a.vdef5 dbpercent, ");//业务性质
		 stb.append(" t.vwtranstype vwtranstype, ");//交易方式
		 stb.append(" t.vwperiod ibinpost, ");//公告次数
//		 stb.append(" 'jinmai' ibretired, ");//竞买人数
		 stb.append(" t.cwtransflag if_sus, ");//是否交易成功
//		 stb.append(" 'pgjia' vbdetail, ");//评估价/原租价(万元)
		 stb.append(" t.dwprice dwprice, ");//挂牌价/交易低价(万元)
		 stb.append(" t.dwtransprice dwtransprice, ");//成交价(万元)
		 stb.append(" t.cgreportdate cwctdate, ");//交易时间
		 stb.append(" t.vsname vsname, ");//名称
//		 stb.append(" 'zhuzhi' address, ");//住址
//		 stb.append(" 'lxdianhua' dscapital, ");//联系电话
//		 stb.append(" 'memo' memo ");//备注
		 stb.append(" '1' def5 ");
		 stb.append(" FROM scappt_apply_h a LEFT JOIN scappt_apply_b b ON a.pk_apply_h = b.pk_apply_h LEFT JOIN scappt_transfer_h t ON a.pk_apply_h = t.def9 WHERE a.vdef1 = 'gpzr' AND t.vrname IS NOT NULL ");
		 
		 listm1 = (List<ScapptExportTransHVO>) ScapDAO.executeQuery(stb.toString(), new BeanListProcessor(ScapptExportTransHVO.class));
		 if(listm1.size()==0){
			  throw new LfwRuntimeException("请完善进场交易数据");
		  }
		 //***
		 ScapDAO.insertOrUpdateVOs(listm1.toArray(new ScapptExportTransHVO[listm1.size()]));
		 //按照字段分别查询出导入数据与进场交易数据的交集选项
		 StringBuffer strcom = new StringBuffer();
		 strcom.append(" select pk_sgroup, pk_org, def1, atrdata, dbpercent, vwtranstype, dwprice, dwtransprice, vsname from scappt_export_trans WHERE def5 IS NULL ");
		 strcom.append(" INTERSECT ");
		 strcom.append(" select pk_sgroup, pk_org, def1, atrdata, dbpercent, vwtranstype, dwprice, dwtransprice, vsname from scappt_export_trans WHERE def5 IS NOT NULL ");
		 List<ScapptExportTransHVO> listcom = new ArrayList<ScapptExportTransHVO>();
		 listcom = (List<ScapptExportTransHVO>) ScapDAO.executeQuery(strcom.toString(), new BeanListProcessor(ScapptExportTransHVO.class));
		 
		 if(listcom.size() > 0){
			 StringBuffer strin = new StringBuffer();
//			 for (ScapptExportTransHVO comExpertHVO : listcom) {
			 for(int i = 0 ; i < listcom.size() ; i++){
				 String pk_sgroup = listcom.get(i).getPk_sgroup();
				 pk_sgroup = null == pk_sgroup ? " is null " : " = '"+pk_sgroup+"' ";
				 String pk_org = listcom.get(i).getPk_org();
				 pk_org = null == pk_org ? " is null " : " = '"+pk_org+"' ";
				 String def1 = listcom.get(i).getDef1();
				 def1 = null == def1 ? " is null " : " = '"+def1+"' ";
				 String atrdata = listcom.get(i).getAtrdata();
				 atrdata = null == atrdata ? " is null " : " = '"+atrdata+"' ";
				 String dbpercent = listcom.get(i).getDbpercent();
				 dbpercent = null == dbpercent ? " is null " : " = '"+dbpercent+"' ";
				 String vwtranstype = listcom.get(i).getVwtranstype();
				 vwtranstype = null == vwtranstype ? " is null " : " = '"+vwtranstype+"' ";
				 UFDouble dwprice = listcom.get(i).getDwprice();
				 String dwprice_S = null == dwprice ? " is null " : " = "+dwprice.toString()+" ";
				 UFDouble dwtransprice = listcom.get(i).getDwtransprice();
				 String dwtransprice_S = null == dwtransprice ? " is null " : " = "+dwtransprice.toString()+" ";
				 String vsname = listcom.get(i).getVsname();
				 vsname = null == vsname ? " is null " : " = '"+vsname+"' ";
				 if(i == listcom.size() - 1){
					 strin.append(" SELECT se.pk_main FROM scappt_export_trans se where pk_sgroup "+pk_sgroup+" and pk_org "+pk_org+" and def1 "+def1+" and atrdata "+atrdata+" and dbpercent "+dbpercent+" and vwtranstype "+vwtranstype+" and dwprice "+dwprice_S+" and dwtransprice "+dwtransprice_S+" and vsname "+vsname+" ");
				 }else{
					 strin.append(" SELECT se.pk_main FROM scappt_export_trans se where pk_sgroup "+pk_sgroup+" and pk_org "+pk_org+" and def1 "+def1+" and atrdata "+atrdata+" and dbpercent "+dbpercent+" and vwtranstype "+vwtranstype+" and dwprice "+dwprice_S+" and dwtransprice "+dwtransprice_S+" and vsname "+vsname+" ");
					 strin.append(" UNION ");
				 }
				 
			 }
			 //not in PK
			 StringBuffer strfin = new StringBuffer();
			 strfin.append(" select * from scappt_export_trans se where se.pk_main not in ("+strin+")");
			 //表急为最终数据
			 ScapDAO.executeUpdate("UPDATE ("+strfin+") e SET def4 = '1' ");
			 
//			 List<ScapptExportTransHVO> listfin = new ArrayList<ScapptExportTransHVO>();
//			 listfin = (List<ScapptExportTransHVO>) ScapDAO.executeQuery(strfin.toString(), new BeanListProcessor(ScapptExportTransHVO.class));
	//
//			 //def4 = 1 update
//			 for (ScapptExportTransHVO comExpertHVO : listfin) {
//				 comExpertHVO.setDef4("1");
//			 }
//			 ScapDAO.insertOrUpdateVOs(listfin.toArray(new ScapptExportTransHVO[listfin.size()]));

		 }else{
			 ScapDAO.executeUpdate("UPDATE scappt_export_trans e SET def4 = '1' ");
		 }
		  
	  }catch (ExcelException e1) {
			throw new ExcelException(e1.getMessage());
		}
	  //对比结果集插入数据库；对比
    Dataset main_ds = CpWinUtil.getDataset(getMasterDsId());
    Row[] rows = main_ds.getCurrentRowData().getRows();
    
    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put("model", "nc.scap.transfer.model.LandPageModel");
    paramMap.put(PtConstants.NODE_TYPE, "compare");
    AppUtil.addAppAttr(PtConstants.NODE_TYPE, "compare");
    AppUtil.addAppAttr(ILandConstants.IS_AUDIT, "compare");
    OpenProperties props = new OpenProperties(
			"dataimpComps.export_listwin_compare", "对比查看", "-1", "-1",
			paramMap);
	props.setButtonZone(false);
	AppLifeCycleContext.current().getApplicationContext()
	.navgateTo(props);
	  //报表展示
  }
  
  private String sqlstr(String value){
	  String sqlstr = "";
	  sqlstr = null == sqlstr ? " is null " : " = '"+sqlstr+"' ";
	return value;
  }
}
