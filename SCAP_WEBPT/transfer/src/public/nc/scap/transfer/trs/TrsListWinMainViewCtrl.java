package nc.scap.transfer.trs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.scap.ptpub.PtConstants;
import nc.scap.pub.alert.IAlertDeal;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.pub.util.CpPubMethod;
import nc.scap.pub.util.CpUIctrl;
import nc.scap.pub.util.CpWinUtil;
import nc.scap.pub.util.ScapDAO;
import nc.uap.cpb.org.querycmd.QueryCmd;
import nc.uap.ctrl.tpl.print.ICpPrintTemplateService;
import nc.uap.ctrl.tpl.print.init.DefaultPrintService;
import nc.uap.lfw.app.plugin.AppControlPlugin;
import nc.uap.lfw.core.AppSession;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
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
import nc.uap.lfw.file.LfwFileConstants;
import nc.uap.lfw.file.UploadFileHelper;
import nc.uap.portal.log.ScapLogger;
import nc.uap.wfm.constant.WfmConstants;
import nc.uap.wfm.utils.WfmCPUtilFacade;
import nc.uap.wfm.utils.WfmUtilFacade;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import uap.lfw.core.itf.ctrl.AbstractMasterSlaveViewController;
import uap.lfw.core.locator.ServiceLocator;
import uap.lfw.dbl.uiengine.CommonObjectConstants;
import uap.web.bd.pub.AppUtil;
/** 
 * ��Ϣ�б�Ĭ���߼�
 */
public class TrsListWinMainViewCtrl<T extends WebElement> extends AbstractMasterSlaveViewController {
  private static final String MAIN_VIEW_ID="main";
  private static final String CARD_WIN_ID="transferComps.trs_cardwin";
  private static final String CARD_WIN_TITLE="�༭";
  /** 
 * �����ݼ����߼�
 * @param dataLoadEvent
 */
  public void onDataLoad(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
		ds.setLastCondition(getWheresql(false));
		String curkey = ds.getCurrentKey();
		if (curkey != null) {
			AppUtil.addAppAttr("curkey", curkey);
		}
		ds.setExtendAttribute(
				Dataset.ORDER_PART,
				"order by pk_org desc,cchangetype desc,ctradetype desc,billmakedate desc,ts desc ");
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()) {
			protected void changeCurrentKey() {
				if (getDs().getCurrentKey() == null) {
					getDs().setCurrentKey((String) AppUtil.getAppAttr("curkey"));
				}
				return;
			}
		});
  }
  /** 
 * ���ݽڵ����͹���SQL
 * @param node_type
 * @param flag
 * @return
 */
  public String getFilteByNodeType(  String node_type,  boolean flag){
    String filtStr = " ";
		if (PtConstants.GPZR.equals(node_type)) {
			filtStr = getSQL(flag, PtConstants.GPZR);
		} else if (PtConstants.XYZR.equals(node_type)) {
			filtStr = getSQL(flag, PtConstants.XYZR);
		} else if (PtConstants.WCZR.equals(node_type)) {
			filtStr = getSQL(flag, PtConstants.WCZR);
		} else if (PtConstants.ZDZCGPZR.equals(node_type)) {
			// pos �ش��ʲ�����
			filtStr = getSQL(flag, PtConstants.ZDZCGPZR);
		} else if (PtConstants.ZDZCXYZR.equals(node_type)) {
			// pos �ش��ʲ�Э��
			filtStr = getSQL(flag, PtConstants.ZDZCXYZR);
		} else if (PtConstants.ZFZCZL.equals(node_type)) {
			// pos �ش��ʲ�����
			filtStr = getSQL(flag, PtConstants.ZFZCZL);
		} else if (PtConstants.BTBLZZ.equals(node_type)) {
			// pos ����
			filtStr = getSQL(flag, PtConstants.BTBLZZ);
		} else if (PtConstants.TBLZZ.equals(node_type)) {
			// pos ͬ��������
			filtStr = getSQL(flag, PtConstants.TBLZZ);
		} else if (PtConstants.BTBLJZ.equals(node_type)) {
			// pos ����
			filtStr = getSQL(flag, PtConstants.BTBLJZ);
		} else if (PtConstants.QYBG.equals(node_type)) {
			// pos ����
			filtStr = getSQL(flag, PtConstants.QYBG);
		}
		return filtStr;
  }
  /** 
 * ���Ĳ�ѯ�ܹ�
 * @param flag
 * @param type
 * @return
 */
  public String getSQL(  boolean flag,  String type){
    String sql = "";
		String pk_org = CntUtil.getCntOrgPk();
		if (CpOrgUtil.isCompanyOrg(pk_org)) {
			sql = getTypeSQl(type);
		} else {
			String query = CpPubMethod.getWebParter(PtConstants.QUERYOPER);
			if (query != null && !"".equals(query)) {
				sql = getTypeSQl(type);
			} else {
				sql = getTypeSQl(type);
//				if (flag) {
//					sql = getTypeSQl(type)
//							+ " and  pk_transfer_h in (select pk_frmins from wfm_task where pk_owner='"
//							+ CntUtil.getCntUserPk() + "')";
//					return sql;
//				}
//				sql = getTypeSQl(type)
//						+ " and  pk_transfer_h in (select pk_frmins from wfm_task where pk_owner='"
//						+ CntUtil.getCntUserPk() + "' and state='State_Run')";

			}
		}
		return sql;
  }
  /** 
 * ���ݽڵ����ͻ�ȡ��ӦSQL
 * @param node_type
 * @return
 */
  public String getTypeSQl(  String node_type){
    String sql = "";
		if (PtConstants.GPZR.equals(node_type)) {
			sql = " and cchangetype='"
					+ getDefdocByListCodeAndCode(PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_CQZR).getPk_defdoc()
					+ "' and ctradetype='"
					+ getDefdocByListCodeAndCode(PtConstants.SCAPPT_CQJY,
							PtConstants.CTRADETTYPE_GPZR).getPk_defdoc() + "'";
		} else if (PtConstants.XYZR.equals(node_type)) {
			sql = " and cchangetype='"
					+ getDefdocByListCodeAndCode(PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_CQZR).getPk_defdoc()
					+ "' and ctradetype='"
					+ getDefdocByListCodeAndCode(PtConstants.SCAPPT_CQJY,
							PtConstants.CTRADETTYPE_XYZR).getPk_defdoc() + "'";

		} else if (PtConstants.WCZR.equals(node_type)) {
			sql = " and cchangetype='"
					+ getDefdocByListCodeAndCode(PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_CQZR).getPk_defdoc()
					+ "' and ctradetype='"
					+ getDefdocByListCodeAndCode(PtConstants.SCAPPT_CQJY,
							PtConstants.CTRADETTYPE_WCZR).getPk_defdoc() + "'";
		} else if (PtConstants.ZDZCGPZR.equals(node_type)) {
			// pos
			sql = " and cchangetype='"
					+ getDefdocByListCodeAndCode(PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_MAJOR).getPk_defdoc()
					+ "' and cgreattype='"
					+ getDefdocByListCodeAndCode(PtConstants.SCAPPT_ZDZC,
							PtConstants.MAJORLISTMeTHODS).getPk_defdoc() + "'";
		} else if (PtConstants.ZDZCXYZR.equals(node_type)) {
			// pos
			sql = " and cchangetype='"
					+ getDefdocByListCodeAndCode(PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_MAJOR).getPk_defdoc()
					+ "' and cgreattype='"
					+ getDefdocByListCodeAndCode(PtConstants.SCAPPT_ZDZC,
							PtConstants.MAJORAGREEMeTHODS).getPk_defdoc() + "'";
		} else if (PtConstants.ZFZCZL.equals(node_type)) {
			// pos
			sql = " and cchangetype='"
					+ getDefdocByListCodeAndCode(PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_MAJOR).getPk_defdoc()
					+ "' and cgreattype='"
					+ getDefdocByListCodeAndCode(PtConstants.SCAPPT_ZDZC,
							PtConstants.MAJORLEASEMeTHODS).getPk_defdoc() + "'";
		} else if (PtConstants.BTBLZZ.equals(node_type)) {
			// pos ��ͬ��������
			sql = " and cchangetype='"
					+ getDefdocByListCodeAndCode(PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_BLZZ).getPk_defdoc()+"'";
		} else if (PtConstants.TBLZZ.equals(node_type)) {
			// pos ��ͬ��������
			sql = " and cchangetype='"
					+ getDefdocByListCodeAndCode(PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_TBLJZ).getPk_defdoc()+"'";
		} else if (PtConstants.BTBLJZ.equals(node_type)) {
			// pos ��ͬ��������
			sql = " and cchangetype='"
					+ getDefdocByListCodeAndCode(PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_BLJZ).getPk_defdoc()+"'";
		} else if (PtConstants.QYBG.equals(node_type)) {
			// pos ��ҵ����
			sql = " and cchangetype='"
					+ getDefdocByListCodeAndCode(PtConstants.SCAPPT_CQBD,
							PtConstants.CCHANGE_TYPE_QYBG).getPk_defdoc()+"'";
		}
		return sql;
  }
  /** 
 * ����list code ��doc code ����
 * @param docListCode
 * @param docCode
 * @return
 */
  public DefdocVO getDefdocByListCodeAndCode(  String docListCode,  String docCode){
    DefdocVO defdocvo = null;
		String sql = "select * from bd_defdoc bd where BD.PK_DEFDOCLIST =(select B.PK_DEFDOCLIST from bd_defdoclist b where B.CODE='"
				+ docListCode
				+ "' and nvl(dr,0) = 0) and BD.CODE='"
				+ docCode
				+ "' and nvl(dr,0) = 0 ";
		defdocvo = (DefdocVO) ScapDAO.executeQuery(sql, new BeanProcessor(
				DefdocVO.class));
		return defdocvo;
  }
  /** 
 * ��ȡSQL flag Ϊtrueʱ����ʾ��ѯģ�塡falseΪĬ�ϼ���
 * @param operationcmd
 * @return
 */
  public String getWheresql(  boolean flag){
    String node_type = CpPubMethod.getAppParter(PtConstants.NODE_TYPE);
		String pk_org = (String) AppUtil.getAppAttr("pk_org");
		String pk_group = AppLifeCycleContext.current().getApplicationContext()
				.getAppEnvironment().getPk_group();
		String pk_Org = AppLifeCycleContext.current().getApplicationContext()
				.getAppEnvironment().getPk_org();
		String where = " isleadin is null and 1=1 " + getFilteByNodeType(node_type, flag);
		if (CpOrgUtil.isCompanyOrg(pk_Org)) {
			where += " and pk_org='" + pk_Org 
					+ "' and dr=0 ";
		} else {
			if (pk_org == null || "".equals(pk_org)) {
				where += " and dr=0 ";
			} else {
				if (!CpOrgUtil.isCompanyOrg(pk_org)) {
					where += " and dr=0  ";
				} else {
					where += " and pk_org='" + pk_org + "' and dr=0 ";
				}
			}
		}
		ScapLogger.error(where);
		return where;
  }
  /** 
 * ��֯��plugin
 * @param keys
 */
  public void pluginorgtree_plugin(  Map keys){
    LfwView main = AppLifeCycleContext.current().getWindowContext()
				.getViewContext(MAIN_VIEW_ID).getView();
		Dataset ds = main.getViewModels().getDataset(getMasterDsId());
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()) {
			protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
				String where = getWheresql(false);
				ds.setLastCondition(where);
				ds.setExtendAttribute(Dataset.ORDER_PART,
						"order by pk_org,cchangetype,ctradetype,billmakedate,ts desc ");
				return where;
			}

			// ���ⷭҳʱ���߻���
			protected void changeCurrentKey() {
				setChangeCurrentKey(true);
			}
		});

		clearDetailDs();
  }
  /** 
 * ���ð�ť״̬
 * @param pk
 * @param formstate
 */
  public void setBtnState(  String pk,  String formstate){
    LfwView view = CpWinUtil.getView();
		if (formstate.equals(IGlobalConstants.SCAPPM_BILLSTATE_RUN)) {
			CpUIctrl.showMenuBar(view, "menubar",
					new String[] { "edit", "del","endwrite" });
		} else if (formstate.equals(IGlobalConstants.SCAPPM_BILLSTATE_END)) {
			CpUIctrl.showMenuBar(view, "menubar",
					new String[] { "edit", "del" });
		} else if (formstate
				.equals(IGlobalConstants.SCAPPM_BILLSTATE_NOTTSTART)) {
			CpUIctrl.showMenuBar(view, "menubar", null);
			boolean flag = false;
			try {
				flag = WfmCPUtilFacade.isCanDelBill(pk);
			} catch (Exception e) {
				flag = false;
			}
			if (!flag) {
				CpUIctrl.showMenuBar(view, "menubar", new String[] { "del","endwrite" });
			}
		} else {
			CpUIctrl.showMenuBar(view, "menubar", null);
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
			if (AppConsts.OPE_EDIT.equals(sign) || "approve".equals(sign)) {
				Row savedRow = ds.getSelectedRow();
				LfwRuntimeEnvironment.getWebContext().getWebSession()
						.addOriginalParameter("openBillId", null);
				onDataLoad(new DataLoadEvent(ds));
				Dataset[] childs = this.getDetailDs(this.getDetailDsIds());
				for (Dataset cds : childs) {
					cds.clear();
				}
				// copyTranslateRow2Row(selRow, savedRow, ds);
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
	  	OpenProperties props = new OpenProperties(getWinByType(),
				CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_ADD);
		paramMap.put("model", "nc.scap.transfer.model.TransferCardPageModel");
//		CpPubMethod.setWfmInfo(paramMap, null, getNodeCodeByType(), "add");
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * ��õ�������PK
 * @return
 */
  private String getFlwTypePk(){
    return "0001ZZ100000000DWU3D";
  }
  /** 
 * ���ݲ������ͻ�ȡ�ڵ����
 * @return
 */
  public String getNodeCodeByType(){
    String node_code = "";
		String node_type = (String) AppUtil.getAppAttr(PtConstants.NODE_TYPE);
		if (PtConstants.GPZR.equals(node_type)) {
			node_code = PtConstants.NODECODE_GPZR;
		} else if (PtConstants.XYZR.equals(node_type)) {
			node_code = PtConstants.NODECODE_XYZR;
		} else if (PtConstants.WCZR.equals(node_type)) {
			node_code = PtConstants.NODECODE_WCZR;
		} else if (PtConstants.ZDZCGPZR.equals(node_type)) {
			node_code = PtConstants.NODECODE_ZDZCGPZR;
		} else if (PtConstants.ZDZCXYZR.equals(node_type)) {
			node_code = PtConstants.NODECODE_ZDZCXYZR;
		} else if (PtConstants.ZFZCZL.equals(node_type)) {
			node_code = PtConstants.NODECODE_ZFZCZL;
		} else if (PtConstants.BTBLZZ.equals(node_type)) {
			node_code = PtConstants.NODECODE_BTBLZZ;
		} else if (PtConstants.TBLZZ.equals(node_type)) {
			node_code = PtConstants.NODECODE_TBLZZ;
		} else if (PtConstants.BTBLJZ.equals(node_type)) {
			node_code = PtConstants.NODECODE_BTBLJZ;
		} else if (PtConstants.QYBG.equals(node_type)) {
			node_code = PtConstants.NODECODE_QYBG;
		}
		return node_code;
  }
  /** 
 * ��ȡ����ID
 * @return
 */
  public String getWinByType(){
    String winid = CARD_WIN_ID;
		String node_type = (String) AppUtil.getAppAttr(PtConstants.NODE_TYPE);
		if (PtConstants.GPZR.equals(node_type)) {
			winid = PtConstants.WIN_GPZR;
		} else if (PtConstants.XYZR.equals(node_type)) {
			winid = PtConstants.WIN_XYZR;
		} else if (PtConstants.WCZR.equals(node_type)) {
			winid = PtConstants.WIN_WCZR;
		} else if (PtConstants.ZDZCGPZR.equals(node_type)) {
			// pos 2014-05-26 �ش��ʲ�����ת��
			winid = "transferComps.trsmajorlist_cardwin";
		} else if (PtConstants.ZDZCXYZR.equals(node_type)) {
			// pos 2014-05-26 �ش��ʲ�Э��ת��
			winid = "transferComps.trsmajoragree_cardwin";
		} else if (PtConstants.ZFZCZL.equals(node_type)) {
			// pos 2014-05-26 �ش��ʲ�����
			winid = "transferComps.trsmajorlease_cardwin";
		} else if (PtConstants.BTBLZZ.equals(node_type)||PtConstants.TBLZZ.equals(node_type)) {
			// pos 2014-05-30 ��ͬ��������
			winid = "transferComps.capitalIncrease_cardwin";

		} else if (PtConstants.BTBLJZ.equals(node_type)) {
			// pos 2014-05-30 ��ͬ��������
			winid = "transferComps.capitalreduce_cardwin";
		} else if (PtConstants.QYBG.equals(node_type)) {
			// pos 2014-05-30 �ϲ�
			winid = "transferComps.commerger_cardwin";
		}
		return winid;
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
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(getWinByType(),
				CARD_WIN_TITLE);
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
//		paramMap.put(WfmConstants.WfmUrlConst_billID, pkValue);
		paramMap.put("model", "nc.scap.transfer.model.TransferCardPageModel");
//		CpPubMethod.setWfmInfo(paramMap, pkValue, getNodeCodeByType(), "edit");
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * ����
 * @param scriptEvent
 */
  public void onApprove(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("��ѡ�д���������");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(getWinByType(), "����");
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, "approve");
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		paramMap.put(WfmConstants.WfmUrlConst_billID, pkValue);
		
		//chenmeng1  ���ڵ��ƴ��url���������������õ�ģ�岻��Ч   2014-06-27
		AppSession appSes = LfwRuntimeEnvironment.getWebContext().getAppSession();
		String nodeid = LfwRuntimeEnvironment.getWebContext().getParameter(AppControlPlugin.NODECODE );
		if( nodeid == null){
		    nodeid = appSes.getOriginalParameter(AppControlPlugin.NODECODE );
		}
		if( nodeid ==null)
		nodeid = (String) appSes.getAttribute(AppControlPlugin.NODECODE );
		paramMap.put("nodecode", nodeid);
		
		paramMap.put("model", "nc.scap.transfer.model.TransferCardPageModel");
		CpPubMethod.setWfmInfo(paramMap, pkValue, getNodeCodeByType(),
				"approve");
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  /** 
 * @param scriptEvent
 */
  public void onLook(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("��ѡ�д��鿴����");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties(getWinByType(), "�鿴");
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, "look");
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
//		paramMap.put(WfmConstants.WfmUrlConst_billID, pkValue);
		paramMap.put("model", "nc.scap.transfer.model.TransferCardPageModel");
//		CpPubMethod.setWfmInfo(paramMap, pkValue, getNodeCodeByType(), "wf");
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
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
		String pk_form = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));
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
  /** 
 * ���̽���
 * @param mouseEvent
 */
  public void onFlow(  MouseEvent<?> mouseEvent){
    Dataset masterDs = this.getMasterDs();
		Row row = masterDs.getSelectedRow();
		String pk_form = (String) row.getValue(masterDs.nameToIndex(masterDs
				.getPrimaryKeyField()));
		this.getCurrentAppCtx().addAppAttribute(WfmConstants.WfmAppAttr_BillID,
				pk_form);
		CpPubMethod.onFlow(masterDs.getId(), getNodeCodeByType());
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
			ICpPrintTemplateService service = ServiceLocator
					.getService(ICpPrintTemplateService.class);
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
    // CmdInvoker.invoke(new UifDatasetLoadCmd(getMasterDsId()) {
		// protected String postProcessQueryVO(SuperVO vo, Dataset ds) {
		// String where = AppUtil.getGroupOrgFielterSqlForDesign(false,
		// "pk_org");
		// ds.setLastCondition(where);
		// return where;
		// }
		//
		// // ���ⷭҳʱ���߻���
		// protected void changeCurrentKey() {
		// setChangeCurrentKey(true);
		// }
		// });
		// clearDetailDs();
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
    FromWhereSQL whereSql = (FromWhereSQL) keys
				.get(FromWhereSQL.WHERE_SQL_CONST);
		String where = whereSql.getWhere();
		where += " and " + getWheresql(true);
		CmdInvoker.invoke(new QueryCmd(MAIN_VIEW_ID, getMasterDsId(), where));
  }
  @Override protected String getMasterDsId(){
    return "scappt_transfer_h";
  }
  public void onAfterRowSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
		Row row = ds.getSelectedRow();
		if (row == null)
			return;
//		String pkValue = (String) row.getValue(ds.nameToIndex(ds
//				.getPrimaryKeyField()));
//		String formstate = (String) row.getValue(ds
//				.nameToIndex(ScapptTransferHVO.FORMSTATE));
		CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
//		setBtnState(pkValue, formstate);
  }
  /** 
 * �ӱ���
 */
  public void onAttachChild(  MouseEvent<?> mouseEvent){
    GridComp grid = (GridComp) mouseEvent.getSource();
		String dsId = grid.getDataset();
		Dataset ds = this.getCurrentView().getViewModels().getDataset(dsId);
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
		// ��������
		Map<String, String> param = UploadFileHelper.BuildDefaultPamater("",
				primaryKeyValue, CommonObjectConstants.AttachFileType, "");
		param.put("usescanable", "true");
		param.put("state", String.valueOf(31));
		String title = "����";
		CmdInvoker.invoke(new UifAttachCmd(title, param));
  }
  public void onAttach(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("��ѡ������!");
		}
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
		AppUtil.addAppAttr(LfwFileConstants.SYSID,
				LfwFileConstants.SYSID_BAFILE);
		// ���̸�������
		Map<String, String> wfmParam = WfmUtilFacade
				.getFileMgrParamsByTask(taskPk);

		// ��������
		Map<String, String> param = UploadFileHelper.BuildDefaultPamater(
				LfwFileConstants.SYSID_BAFILE, primaryKeyValue,
				CommonObjectConstants.AttachFileType, "");
		param.put("usescanable", "true");
		param.put("state", String.valueOf(31));

		String title = "����";
		CmdInvoker.invoke(new UifAttachCmd(title, param));
  }
  public void onReport(  MouseEvent mouseEvent){
    Dataset ds = CpWinUtil.getDataset(CpWinUtil.getView(),
				"scappt_transfer_h");
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LfwRuntimeException("��ѡ��һ����¼��");
		}
		// ���ѡ����pk
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));
		StringBuffer str = new StringBuffer();

		str.append("/portal/ReportServer?reportlet=scappt_zdzcczbab.cpt&op=write")
				.append("&pk_transfer_h=").append(pkValue);
		StringBuffer js = new StringBuffer().append("window.open ('")
				.append(str.toString())
				.append("'," + IGlobalConstants.OPEN_REPORT_CONDITION + ");");
		// ����js���򿪱���
		AppLifeCycleContext.current().getWindowContext()
				.addExecScript(js.toString());
  }
  public void onAlert(  MouseEvent mouseEvent){
    IAlertDeal al = NCLocator.getInstance().lookup(IAlertDeal.class);
		al.priceDisAert(null);
  }
  public void onEndwrite(  MouseEvent mouseEvent){
    Dataset ds = this.getMasterDs();
		if (ds.getSelectedIndex() < 0) {
			throw new LfwRuntimeException("��ѡ������");
		}

		Row row = ds.getSelectedRow();
		String pkValue = (String) row.getValue(ds.nameToIndex(ds
				.getPrimaryKeyField()));

		OpenProperties props = new OpenProperties("transferComps.result_cardwin",
				"���۽��¼��");
		props.setButtonZone(false);
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put(AppConsts.OPE_SIGN, AppConsts.OPE_EDIT);
		paramMap.put(AppConsts.OPEN_BILL_ID, pkValue);
		props.setParamMap(paramMap);
		this.getCurrentAppCtx().navgateTo(props);
  }
  
  
//  /**
//	 * ������Ϣ
//	 * 
//	 * @param mouseEvent
//	 */
//	public void onDetail(ScriptEvent mouseEvent) {
//		String oper = "look";
//		String title = "�鿴";
//		Dataset ds = this.getCurrentView().getViewModels()
//				.getDataset("scappt_apply_b");
//		Dataset mainds = this.getCurrentView().getViewModels()
//				.getDataset("scappt_apply_h");
//		String mainpk = (String) mainds.getValue(mainds.getPrimaryKeyField());
//		// ��Ȩ����ת�÷�PK
//		String pk_rorg = (String) mainds.getValue(ScapptApplyHVO.PK_RORG);
//		Row row = ds.getSelectedRow();
//		// ��Ȩ���������ҵPK
//		String pk = (String) row.getValue(ds
//				.nameToIndex(ScapptApplyBVO.PK_BORG));
//		// ������������ѯ�Ƿ�����Ȩ����
//		String billpk = ScapPtMethod.getBillPk(mainpk, pk);
//		// ��ȡת�÷���Ȩ�Ǽ���Ϣ
//		String rorg = ScapPtMethod.getFirmBaseHVOByTreePk(pk_rorg);
//		if (rorg == null) {
//			AppInteractionUtil.showShortMessage("ת�÷�δ�Ǽǲ�Ȩ��Ϣ!");
//			return;
//		}
//		ScapPtMethod.onLink(ScapPtMethod.getWinByType(), title, oper, billpk,
//				pk, rorg, mainpk);
//	}
}
