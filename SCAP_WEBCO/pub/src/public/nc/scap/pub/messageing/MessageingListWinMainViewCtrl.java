package nc.scap.pub.messageing;
import nc.uap.lfw.core.cmd.CmdInvoker;
import java.util.Map;
import java.net.MalformedURLException;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import nc.uap.lfw.core.cmd.UifEnableCmd;
import nc.uap.lfw.core.comp.WebElement;
import nc.uap.cpb.org.querycmd.QueryCmd;
import net.extsoft.webservice.service.Generic;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.cmd.base.FromWhereSQL;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpExtUtil;
import nc.uap.lfw.core.data.Dataset;
import java.util.ArrayList;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.sysinit.util.ScapSysinitUtil;

import java.util.List;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.lfw.file.UploadFileHelper;
import nc.vo.pub.BusinessException;
import nc.uap.lfw.core.cmd.UifAttachCmd;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.lfw.core.event.DatasetEvent;
import uap.lfw.core.locator.ServiceLocator;
import com.caucho.hessian.client.HessianProxyFactory;
import nc.uap.lfw.core.event.MouseEvent;
/** 
 * 信息列表默认逻辑
 */
public class MessageingListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final long serialVersionUID=-1;
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="即时通讯.messageing_cardwin";
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
    //	    Row savedRow = (Row) keys.get(OPERATE_ROW);
//			if (savedRow != null) {
//				Dataset ds = this.getMasterDs();
//				savedRow.setRowId(ds.getEmptyRow().getRowId());
//				String sign = this.getOperator();
//				if (AppConsts.OPE_EDIT.equals(sign)) {
//					int index = ds.getRowIndex(ds.getSelectedRow());
//					if (index >= 0) {
//						ds.removeRow(index);
//						ds.insertRow(index, savedRow);
//					}
//				} else if (AppConsts.OPE_ADD.equals(sign)) {
//					ds.addRow(savedRow);
//				}
//			}
  }
  /** 
 * 主组织变化
 * @param keys
 */
  public void doOrgChange(  Map<?,?> keys){
    //	    CmdInvoker.invoke(new UifDatasetLoadCmd(this.getMasterDsId()) {
//				protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
//					String where = AppUtil.getGroupOrgFielterSqlForDesign(false, PK_ORG);
//					ds.setLastCondition(where);
//					return where;
//				}
//	
//				// 避免翻页时重走缓存
//				protected void changeCurrentKey() {
//					setChangeCurrentKey(true);
//				}
//			});
//			this.clearDetailDs();
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
    return "dataSourceName";
  }
  public void onTbjg(  MouseEvent mouseEvent){
    // 服务地址
	String url = ScapSysinitUtil.getSysinitStrByCode("elinkpath");
	Generic service = CpExtUtil.getGeneric(url);
	if (service != null) {
		try {
//			service.delDomain("国资集团");
			//获得默认域名
			String findDefaultDomain = service.findDefaultDomain();
			if(findDefaultDomain==null){
				service.addDomain(IGlobalConstants.DOMAIN_NAME);
				findDefaultDomain = IGlobalConstants.DOMAIN_NAME;
			}
			//同步公司
			CpExtUtil.syncOneLevelDept(findDefaultDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// 调用服务提供的接口，这里是查询服务器当前在线人员
		String[] loginUsers = service.getAllLoginUser();
		System.out.println("当前在线" + loginUsers.length + "人");
		for (String username : loginUsers) {
			// 打印在线人员登录名
			System.out.println(username);
		}
	}
  }
  public void onTbUser(  MouseEvent mouseEvent){
    try {
		  CpExtUtil.syncPerson(IGlobalConstants.DOMAIN_NAME);
	} catch (Exception e) {
		e.printStackTrace();
	}
  }
}
