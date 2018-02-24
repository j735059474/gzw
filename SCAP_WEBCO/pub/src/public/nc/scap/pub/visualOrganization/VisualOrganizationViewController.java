package nc.scap.pub.visualOrganization;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.notice.util.NoticeUtil;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.pub.util.CpWinUtil;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.comp.CheckBoxComp;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.WindowContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.data.RowData;
import nc.uap.lfw.core.data.RowSet;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.TextEvent;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UITabItem;
import nc.vo.pub.SuperVO;
import nc.vo.scapco.pub_visualorganization.visualOrganization;
import nc.vo.scapco.work_notice_contacts.notice_contact_info;

import org.apache.commons.lang.StringUtils;

import uap.web.bd.pub.AppUtil;
public class VisualOrganizationViewController {
  private static final long serialVersionUID=1L;
  private static final long ID=5L;
  private static String PLUGOUT_ID="afterOkplugout";
  /** 
 * 获取当前WindowContext
 * @return
 */
  private WindowContext getCurrentWinCtx(){
    return AppLifeCycleContext.current().getApplicationContext()
				.getCurrentWindowContext();
  }
  public void onCancel(  MouseEvent mouseEvent){
    closeView();
  }
  public void closeView(){
    AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
  }
  public void onOk(  MouseEvent mouseEvent){
    closeView();
		CmdInvoker.invoke(new UifPlugoutCmd(AppUtil.getCntView().getId(),
				PLUGOUT_ID));
  }
  public void onDataLoad_visualOrgs(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
		ds.setOrderByPart(" order by visualorg_code asc");
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()) {

			protected SuperVO[] queryVOs(PaginationInfo pinfo, SuperVO vo,
					String wherePart, String orderPart)
					throws LfwBusinessException {

				visualOrganization[] vos = null;
				String sql = "";
				sql = " SELECT * FROM scapco_visualOrganization t where t.dr = 0 and t.enablestate = 2 ";
				String notice_pkVisualOrglistStr = (String)AppUtil.getAppAttr("notice_pkVisualOrglistStr");
				if (StringUtils.isNotBlank(notice_pkVisualOrglistStr) && notice_pkVisualOrglistStr.length() > 0) {
					sql = sql + " and t.PK_VISUALORG in (" 
							+ notice_pkVisualOrglistStr.substring(0, notice_pkVisualOrglistStr.length() - 1)
							+ ") ";
				}
				if (orderPart != null) {
					sql += orderPart;
				}
				try {
					vos = CRUDHelper.getCRUDService().queryVOs(sql,
							visualOrganization.class, null, null);
					pinfo.setPageSize(vos.length);
				} catch (LfwBusinessException e1) {
					e1.printStackTrace();
				}
				return vos;
			}
		});
  }
  public void onDataLoad_cporgs(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
		ds.setOrderByPart(" order by code asc");
		CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()) {
			protected SuperVO[] queryVOs(PaginationInfo pinfo, SuperVO vo,
					String wherePart, String orderPart)
					throws LfwBusinessException {
				CpOrgVO[] vos = null;
				String sql = "";
				String sqlOrg = "";
				String notice_pkOrglistStr = (String)AppUtil.getAppAttr("notice_pkOrglistStr");
				if (StringUtils.isNotBlank(notice_pkOrglistStr) && notice_pkOrglistStr.length() > 0) {
					sqlOrg = " PK_ORG in (" 
							+ notice_pkOrglistStr.substring(0, notice_pkOrglistStr.length() - 1)
							+ ") ";
				}
				boolean isFilterOrg = ScapSysinitUtil.isFilterOrg();
				if (isFilterOrg) {
					if (StringUtils.isNotBlank(sqlOrg)) {
						sql = "SELECT * FROM CP_ORGS where " + sqlOrg + " and " +CpOrgUtil.getFilterOrgWhereSql();
					} else {
						sql = "SELECT * FROM CP_ORGS where " + CpOrgUtil.getFilterOrgWhereSql();	
					}
				}
				else {
					if (StringUtils.isNotBlank(sqlOrg)) {
						sql = "SELECT * FROM CP_ORGS where " + sqlOrg + " and dr=0  and orglevel ='2' START WITH  PK_ORG = '"
								+ CntUtil.getCntOrgPk()
								+ "' CONNECT BY PRIOR pk_org=pk_fatherorg ";
					} else {
						sql = "SELECT * FROM CP_ORGS where dr=0  and orglevel ='2' START WITH  PK_ORG = '"
								+ CntUtil.getCntOrgPk()
								+ "' CONNECT BY PRIOR pk_org=pk_fatherorg ";
					}


				}
					if (orderPart != null) {
					sql += orderPart;
				}
				try {
					vos = CRUDHelper.getCRUDService().queryVOs(sql,
							CpOrgVO.class, null, null);
					pinfo.setPageSize(vos.length);
					if (ScapSysinitUtil.isShowShortName()) {
						for (CpOrgVO cpOrgVO : vos) {
							cpOrgVO.setName(cpOrgVO.getDef11());
						}
					}
				} catch (LfwBusinessException e1) {
					e1.printStackTrace();
				}
				return vos;
			}
		});
  }
  private UIMeta getUIMeta(){
    UIMeta uimeta = (UIMeta) CpWinUtil.getWinCtx()
				.getViewContext("visualOrganization").getUIMeta();
		return uimeta;
  }
  public void beforeShow(  DialogEvent dialogEvent){
    LfwRuntimeEnvironment.getWebContext().getWebSession().addOriginalParameter("openBillId",null);
    String business_type = (String) AppUtil.getAppAttr(IGlobalConstants.APPATTR_BUSINESS_TYPE);
    String report_type = (String) AppUtil.getAppAttr(IGlobalConstants.APPATTR_REPORT_TYPE);
    String data_type = (String) AppUtil.getAppAttr(IGlobalConstants.APPATTR_DATA_TYPE);
    String message_type = (String) AppUtil.getAppAttr(IGlobalConstants.APPATTR_MESSAGE_TYPE);
    String report_body = (String) AppUtil.getAppAttr(IGlobalConstants.APPATTR_REPORT_BODY);
	Map<String, String> paramMap = new HashMap<String, String>();
	paramMap.put(IGlobalConstants.PARAMMAP_KEY_BUSINESS_TYPE, business_type);
	paramMap.put(IGlobalConstants.PARAMMAP_KEY_REPORT_TYPE, report_type);
	paramMap.put(IGlobalConstants.PARAMMAP_KEY_DATA_TYPE, data_type);
	paramMap.put(IGlobalConstants.PARAMMAP_KEY_MESSAGE_TYPE, message_type);
	List<notice_contact_info> list = NoticeUtil.getContactInfoByParam(paramMap);
	String pkOrglist = "";
	String pkVisualOrglist = "";
	if (list != null && list.size() > 0) {
		for (notice_contact_info vo : list) {
			if (IGlobalConstants.CONTENT_TYPE_RECEIVER.equals(vo.getContacts_type())) {
				if (StringUtils.isNotBlank(vo.getPk_org()) && !"~".equals(vo.getPk_org())) {
					pkOrglist = pkOrglist + "'" + vo.getPk_org() + "'" + ",";
				}
				if (StringUtils.isNotBlank(vo.getPk_visualorg()) && !"~".equals(vo.getPk_visualorg())) {
					pkVisualOrglist = pkVisualOrglist + "'" + vo.getPk_visualorg() + "'" + ",";
				}
			}

		}
	}

    if (StringUtils.isNotEmpty(report_type)) {
    	report_body = NoticeUtil.getReportBodyByNoticeType(report_type);
    } else if (data_type != null) {
    	report_body = "3";
    } else if (message_type != null) {
    	// 如果在联系人中存在相应消息类型的联系人，不用过滤企业树
    	if (!(NoticeUtil.ifExitedMessageType(message_type))) {
    		pkOrglist = "";
    		pkVisualOrglist = "";
    	}
    }
	AppUtil.addAppAttr("notice_pkOrglistStr", pkOrglist);
	AppUtil.addAppAttr("notice_pkVisualOrglistStr", pkVisualOrglist);
		if (report_body != null) {
			LfwView view = AppLifeCycleContext.current().getWindowContext()
					.getViewContext("visualOrganization").getView();
			if ("1".equals(report_body)) {
				UIMeta uimeta = getUIMeta();
				UITabItem split = (UITabItem) UIElementFinder.findElementById(
						uimeta, "tabitem_visualOrganization");
				split.setVisible(false);
				Dataset ds = view.getViewModels().getDataset("cp_orgsDs");
				DataLoadEvent dataLoadEvent = new DataLoadEvent(ds);
				onDataLoad_cporgs(dataLoadEvent);
			}
			if ("2".equals(report_body)) {
				UIMeta uimeta = getUIMeta();
				UITabItem split = (UITabItem) UIElementFinder.findElementById(
						uimeta, "tabitem_Org");
				split.setVisible(false);
				Dataset ds = view.getViewModels().getDataset("visualOrganizationDs");
				DataLoadEvent dataLoadEvent = new DataLoadEvent(ds);
				onDataLoad_visualOrgs(dataLoadEvent);
			}
			if ("3".equals(report_body)) {
				Dataset ds1 = view.getViewModels().getDataset("cp_orgsDs");
				DataLoadEvent dataLoadEvent1 = new DataLoadEvent(ds1);
				onDataLoad_cporgs(dataLoadEvent1);
				
				Dataset ds2 = view.getViewModels().getDataset("visualOrganizationDs");
				DataLoadEvent dataLoadEvent2 = new DataLoadEvent(ds2);
				onDataLoad_visualOrgs(dataLoadEvent2);
			}
		}
  }
  public void SelectSubCompChanged(  TextEvent textEvent){
	  CheckBoxComp comp = (CheckBoxComp)textEvent.getSource(); 
	  boolean checked = comp.isChecked();
	  TreeViewComp orgTree = (TreeViewComp)comp.getView().getViewComponents().getComponent("cp_orgs_tree");
	  if (checked) {
		  orgTree.setCheckBoxModel(1);
	  } else {
		  orgTree.setCheckBoxModel(0);
	  }
  }
  public void SelectAllCompChanged(  TextEvent textEvent){
	  CheckBoxComp comp = (CheckBoxComp)textEvent.getSource(); 
	  boolean checked = comp.isChecked();
	  Dataset orgTree = (Dataset)comp.getView().getViewModels().getDataset("cp_orgsDs");
	  if (checked) {
			RowSet[] rowSets = orgTree.getRowSets();
			for (int i = 0; i < rowSets.length; i++) {
				RowSet rowSet = rowSets[i];
				RowData[] rowDatas = rowSet.getRowDatas();
				for (int j = 0; j < rowDatas.length; j++) {
					RowData rowData = rowDatas[j];
					Integer[] selectIndices = new Integer[rowData.getRows().length];
					for (int q = 0; q < selectIndices.length; q++) {
						selectIndices[q] = q;
					}
					rowData.setRowSelectIndices(selectIndices);
				}
			}
	  } else {
		  orgTree.setAllRowUnSelect();
	  }
  }
  public void SelectSubVisualCompChanged(  TextEvent textEvent){
	  CheckBoxComp comp = (CheckBoxComp)textEvent.getSource(); 
	  boolean checked = comp.isChecked();
	  TreeViewComp visOrgTree = (TreeViewComp)comp.getView().getViewComponents().getComponent("visualOrganization_tree");
	  if (checked) {
		  visOrgTree.setCheckBoxModel(1);
	  } else {
		  visOrgTree.setCheckBoxModel(0);
	  }
  }
  public void SelectAllVisualCompChanged(  TextEvent textEvent){
	  CheckBoxComp comp = (CheckBoxComp)textEvent.getSource(); 
	  boolean checked = comp.isChecked();
	  Dataset visOrgTree = (Dataset)comp.getView().getViewModels().getDataset("visualOrganizationDs");
	  
	  if (checked) {
			RowSet[] rowSets = visOrgTree.getRowSets();
			for (int i = 0; i < rowSets.length; i++) {
				RowSet rowSet = rowSets[i];
				RowData[] rowDatas = rowSet.getRowDatas();
				for (int j = 0; j < rowDatas.length; j++) {
					RowData rowData = rowDatas[j];
					rowData.setRowSelectIndex(j);
					//rowData.setRowSelectIndices(null);
				}
			}
	  } else {
		  visOrgTree.setAllRowUnSelect();
	  }
  }
}
