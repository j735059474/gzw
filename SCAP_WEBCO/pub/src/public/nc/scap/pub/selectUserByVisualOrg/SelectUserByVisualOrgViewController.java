package nc.scap.pub.selectUserByVisualOrg;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import nc.scap.jjpub.util.JjUtil;
import nc.scap.jjpub.util.SqlUtil;
import nc.scap.pub.itf.IGlobalConstants;
import nc.scap.pub.notice.util.NoticeUtil;
import nc.scap.pub.util.CntUtil;
import nc.scap.pub.util.CpOrgUtil;
import nc.scap.pub.util.CpWinUtil;
import nc.scap.sysinit.util.ScapSysinitUtil;
import nc.uap.cpb.org.orgs.CpOrgVO;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.constants.AppConsts;
import nc.uap.lfw.core.crud.CRUDHelper;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.WindowContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowSet;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.KeyEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.exception.LfwBusinessException;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.jsp.uimeta.UIElementFinder;
import nc.uap.lfw.jsp.uimeta.UILayoutPanel;
import nc.uap.lfw.jsp.uimeta.UIMeta;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import nc.uap.lfw.jsp.uimeta.UITabItem;
import nc.vo.pub.SuperVO;
import nc.vo.scapco.pub_visualorganization.visualOrganization;
import uap.web.bd.pub.AppUtil;
public class SelectUserByVisualOrgViewController {
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
						String pk_visual_org_all = (String)AppUtil.getAppAttr("pk_visual_org_all");
					    String report_body = "";
					    String tzxf_notice_style = (String) AppUtil.getAppAttr(IGlobalConstants.TZXF_NOTICE_STYLE);
					    if (StringUtils.isNotBlank(tzxf_notice_style)) {
					    	// 普通消息通知默认是个人
					    	report_body = "3";
							// 如果有功能节点参数的话，就设置成功能节点中设置的报告主体（主要用于消息的查看（企业和组织的时候要回写接收范围子表））
							String tzxf_report_body = (String)AppUtil.getAppAttr(IGlobalConstants.TZXF_REPORTBODY);
							if (StringUtils.isNotBlank(tzxf_report_body)) {
								report_body = tzxf_report_body;
							}
					    }
						String sql="";
						sql = " SELECT * FROM scapco_visualOrganization t where t.dr = 0 and t.enablestate = 2 ";
						if (StringUtils.isNotBlank(pk_visual_org_all)) {
							sql = sql + " and PK_VISUALORG in (" + pk_visual_org_all + ")";
						}
						if(orderPart!=null){
							sql += orderPart;
						}
						try {
							vos = CRUDHelper.getCRUDService().queryVOs(sql,visualOrganization.class, null, null);
							pinfo.setPageSize(vos.length);
						} catch (LfwBusinessException e1) {
							e1.printStackTrace();
						}
						return vos;
					}
				});
//	    Dataset ds = dataLoadEvent.getSource();
//			
//			
//			Dataset orgds = AppUtil.getCntView().getViewModels()
//					.getDataset("multiUserSelect_COMP");
//			orgds.getLastCondition();
//			orgds.setLastCondition(" orglevel = '2' and enablestate=2 and dr=0 ");
//			
//			//移除参数OPEN_BILL_ID
//			String pk= LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPEN_BILL_ID);
//			LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().remove(AppConsts.OPEN_BILL_ID);
//			
//			CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
//			//还原参数 OPEN_BILL_ID
//			LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, pk);
  }
  public void onAfterRowSelect_visualOrgs(  DatasetEvent datasetEvent){
	  		Dataset orgds = AppUtil.getCntView().getViewModels()
					.getDataset("visualOrganization");
			String pk_visualorg = orgds.getSelectedRow().getString(
					orgds.nameToIndex("pk_visualorg"));

			Dataset ds_user = AppLifeCycleContext.current().getViewContext()
					.getView().getViewModels().getDataset("visualUserInfo");
			// 按编码排序
			ds_user.setExtendAttribute("ORDER_PART", "PK_USER");

			String condition = " PK_VISUALORG='"
					+ pk_visualorg + "'";
			condition = condition + "  and dr = '0' ";
			ds_user.getLastCondition();
			ds_user.setLastCondition(condition);
			Dataset ds = datasetEvent.getSource();

			String oldKey = (String) AppLifeCycleContext.current()
					.getApplicationContext()
					.getAppAttribute("treeeindex:" + pk_visualorg);
			RowSet rowSet = ds_user.getRowSet(oldKey);
			// 如果未加载过此页数据
			if (rowSet == null) {
				CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()) {
					protected String postProcessRowSelectVO(SuperVO vo, Dataset ds) {
						return ds.getLastCondition();
					}

					// 重写此方法，避免选中第一行
					protected void postProcessChildRowSelect(Dataset ds) {
						return;
					}
				});
				Dataset userDs = AppLifeCycleContext.current().getViewContext()
						.getView().getViewModels().getDataset("visualUserInfo");
				AppLifeCycleContext
						.current()
						.getApplicationContext()
						.addAppAttribute("treeeindex:" + pk_visualorg,
								userDs.getCurrentKey());
			} else {
				// 已经加载过则走缓存
				ds_user.setCurrentKey(oldKey);
			}
			
			Dataset userDs = AppLifeCycleContext.current().getViewContext()
					.getView().getViewModels().getDataset("visualUserInfo");
			Row[] rows = userDs.getAllRow();
			AppUtil.addAppAttr(ISQUERY, "N");
  }
  final static String ISQUERY="isquery";

  public void onEnterQuery(  KeyEvent keyEvent){
    TextComp text = (TextComp) AppLifeCycleContext.current()
					.getViewContext().getView().getViewComponents()
					.getComponent("queryInput");
			String str = text.getValue().trim();
//			// where条件 enablestate =2 表示启用 user_type =0和2表示集团管理员和公司管理员
//			String where = " user_type != 0 and user_type != 2 and enablestate = 2 and dr=0 and";
		    UIMeta uimeta = (UIMeta) CpWinUtil.getWinCtx().getViewContext("selectUserByVisualOrg").getUIMeta();
		    UITabComp tabComp = (UITabComp) uimeta.findChildById("tabLayout");
			List<UILayoutPanel> tabItemList = tabComp.getPanelList();
			String currentIndex = tabComp.getCurrentItem();
			if ("0".equals(currentIndex)) {
				Dataset userDs = AppLifeCycleContext.current().getViewContext()
						.getView().getViewModels().getDataset("cp_userpsn");

				String condition = SqlUtil.sqlLike(new String[] { "user_code", "user_name",
						"szdw", "deptname" }, str, "or");
				userDs.setLastCondition(condition);
				//移除参数OPEN_BILL_ID
				String pk= LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPEN_BILL_ID);
				LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, null);
				CmdInvoker.invoke(new UifDatasetLoadCmd(userDs.getId()));
				//还原参数 OPEN_BILL_ID
				LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, pk);
				AppUtil.addAppAttr(ISQUERY, "Y");
			} else if ("1".equals(currentIndex)) {
				Dataset userDsVisual = AppLifeCycleContext.current().getViewContext()
						.getView().getViewModels().getDataset("visualUserInfo");

				String condition = SqlUtil.sqlLike(new String[] { "pk_user", "user_name",
						"post" }, str, "or");
				userDsVisual.setLastCondition(condition);
				//移除参数OPEN_BILL_ID
				String pk= LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPEN_BILL_ID);
				LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, null);
				CmdInvoker.invoke(new UifDatasetLoadCmd(userDsVisual.getId()));
				//还原参数 OPEN_BILL_ID
				LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, pk);
				AppUtil.addAppAttr(ISQUERY, "Y");
			}

  }
  public void onDataLoad_visualUser(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
			String flag=JjUtil.getStr(AppUtil.getAppAttr(ISQUERY));
			String pk= LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPEN_BILL_ID);
			if("Y".equals(flag)){
			//移除参数OPEN_BILL_ID
				LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, null);
			}
			CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
			if("Y".equals(flag)){
			//还原参数 OPEN_BILL_ID
				LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, pk);
			}
  }
  public void onDataLoad_cporgs(  DataLoadEvent dataLoadEvent){
	    Dataset ds = dataLoadEvent.getSource();
						ds.setOrderByPart(" order by code asc");
						CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()) {

							protected SuperVO[] queryVOs(PaginationInfo pinfo, SuperVO vo,
									String wherePart, String orderPart)
									throws LfwBusinessException {
								CpOrgVO[] vos = null;
								String sql="";
								boolean isFilterOrg = ScapSysinitUtil.isFilterOrg();
								String pk_org_all = (String)AppUtil.getAppAttr("pk_org_all");
							    String report_body = "";
							    String tzxf_notice_style = (String) AppUtil.getAppAttr(IGlobalConstants.TZXF_NOTICE_STYLE);
							    if (StringUtils.isNotBlank(tzxf_notice_style)) {
							    	// 普通消息通知默认是个人
							    	report_body = "3";
									// 如果有功能节点参数的话，就设置成功能节点中设置的报告主体（主要用于消息的查看（企业和组织的时候要回写接收范围子表））
									String tzxf_report_body = (String)AppUtil.getAppAttr(IGlobalConstants.TZXF_REPORTBODY);
									if (StringUtils.isNotBlank(tzxf_report_body)) {
										report_body = tzxf_report_body;
									}
							    }
								if(isFilterOrg) {
									sql = "SELECT * FROM CP_ORGS where "+CpOrgUtil.getFilterOrgWhereSql();
									if (StringUtils.isNotBlank(pk_org_all)) {
										sql = sql + " and PK_ORG in (" + pk_org_all + ")";
									}
								} else {
									if (StringUtils.isNotBlank(pk_org_all)) {
										sql = "SELECT * FROM CP_ORGS where dr=0  and orglevel ='2'" + " and PK_ORG in (" + pk_org_all + ")" + " START WITH  PK_ORG = '"
												+ CntUtil.getCntOrgPk()
												+ "' CONNECT BY PRIOR pk_org=pk_fatherorg ";	
									} else {
										sql = "SELECT * FROM CP_ORGS where dr=0  and orglevel ='2' START WITH  PK_ORG = '"
												+ CntUtil.getCntOrgPk()
												+ "' CONNECT BY PRIOR pk_org=pk_fatherorg ";
									}
								}
								if(orderPart!=null){
									sql += orderPart;
								}
								try {
									vos = CRUDHelper.getCRUDService().queryVOs(sql,CpOrgVO.class, null, null);
									pinfo.setPageSize(vos.length);
									if(ScapSysinitUtil.isShowShortName()){
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
//			    Dataset ds = dataLoadEvent.getSource();
//					
//					
//					Dataset orgds = AppUtil.getCntView().getViewModels()
//							.getDataset("multiUserSelect_COMP");
//					orgds.getLastCondition();
//					orgds.setLastCondition(" orglevel = '2' and enablestate=2 and dr=0 ");
//					
//					//移除参数OPEN_BILL_ID
//					String pk= LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPEN_BILL_ID);
//					LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().remove(AppConsts.OPEN_BILL_ID);
//					
//					CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
//					//还原参数 OPEN_BILL_ID
//					LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, pk);
	  }
		public void onAfterRowSelect_cporgs(DatasetEvent datasetEvent) {
			
			Dataset orgds = AppUtil.getCntView().getViewModels()
					.getDataset("cp_orgs");
			String pk_org = orgds.getSelectedRow().getString(
					orgds.nameToIndex("pk_org"));

			Dataset ds_user = AppLifeCycleContext.current().getViewContext()
					.getView().getViewModels().getDataset("cp_userpsn");
			// 按编码排序
			ds_user.setExtendAttribute("ORDER_PART", "user_code");
			String condition = " user_type != 0 and user_type != 2 and enablestate = 2 and dr=0 and pk_org='"
					+ pk_org + "'";
			ds_user.getLastCondition();
			ds_user.setLastCondition(condition);
			Dataset ds = datasetEvent.getSource();

			String oldKey = (String) AppLifeCycleContext.current()
					.getApplicationContext()
					.getAppAttribute("treeeindex:" + pk_org);
			RowSet rowSet = ds_user.getRowSet(oldKey);
			String pk_user = LfwRuntimeEnvironment.getLfwSessionBean().getPk_user();
			// 如果未加载过此页数据
			if (rowSet == null) {
				CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()) {
					protected String postProcessRowSelectVO(SuperVO vo, Dataset ds) {
						return ds.getLastCondition();
					}

					// 重写此方法，避免选中第一行
					protected void postProcessChildRowSelect(Dataset ds) {
						return;
					}
				});
				Dataset userDs = AppLifeCycleContext.current().getViewContext()
						.getView().getViewModels().getDataset("cp_userpsn");
				AppLifeCycleContext
						.current()
						.getApplicationContext()
						.addAppAttribute("treeeindex:" + pk_org,
								userDs.getCurrentKey());
			} else {
				// 已经加载过则走缓存
				ds_user.setCurrentKey(oldKey);
			}
			
			AppUtil.addAppAttr(ISQUERY, "N");
		}

	  public void onDataLoad_userpsn(  DataLoadEvent dataLoadEvent){
	    Dataset ds = dataLoadEvent.getSource();
					String flag=JjUtil.getStr(AppUtil.getAppAttr(ISQUERY));
					String pk= LfwRuntimeEnvironment.getWebContext().getOriginalParameter(AppConsts.OPEN_BILL_ID);
					if("Y".equals(flag)){
					//移除参数OPEN_BILL_ID
						LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, null);
					}
					CmdInvoker.invoke(new UifDatasetLoadCmd(ds.getId()));
					if("Y".equals(flag)){
					//还原参数 OPEN_BILL_ID
						LfwRuntimeEnvironment.getWebContext().getWebSession().getOriginalParamMap().put(AppConsts.OPEN_BILL_ID, pk);
					}
	  }
	  private UIMeta getUIMeta(){
		    UIMeta uimeta = (UIMeta) CpWinUtil.getWinCtx().getViewContext("selectUserByVisualOrg").getUIMeta();
			return uimeta;
	  }
	  public void beforeShow(  DialogEvent dialogEvent){
			LfwRuntimeEnvironment.getWebContext().getWebSession().addOriginalParameter("openBillId",null);
		    String report_type = (String) AppUtil.getAppAttr(IGlobalConstants.APPATTR_REPORT_TYPE);
		    String data_type = (String) AppUtil.getAppAttr(IGlobalConstants.APPATTR_DATA_TYPE);
		    String message_type = (String) AppUtil.getAppAttr(IGlobalConstants.APPATTR_MESSAGE_TYPE);
		    String report_body = "";
		    if (report_type != null) {
		    	report_body = NoticeUtil.getReportBodyByNoticeType(report_type);
		    } else if (data_type != null) {
		    	report_body = "3";
		    } else if (message_type != null) {
		    	report_body = "3";
		    }
		    String tzxf_notice_style = (String) AppUtil.getAppAttr(IGlobalConstants.TZXF_NOTICE_STYLE);
		    if (StringUtils.isNotBlank(tzxf_notice_style)) {
		    	// 普通消息通知默认是个人
		    	report_body = "3";
				// 如果有功能节点参数的话，就设置成功能节点中设置的报告主体（主要用于消息的查看（企业和组织的时候要回写接收范围子表））
				String tzxf_report_body = (String)AppUtil.getAppAttr(IGlobalConstants.TZXF_REPORTBODY);
				if (StringUtils.isNotBlank(tzxf_report_body)) {
					report_body = tzxf_report_body;
				}
		    }
			if (StringUtils.isNotBlank(report_body)) {
				LfwView view = AppLifeCycleContext.current().getWindowContext()
						.getViewContext("selectUserByVisualOrg").getView();
				if ("1".equals(report_body)) {
					UIMeta uimeta = getUIMeta();
					UITabItem split = (UITabItem) UIElementFinder.findElementById(
							uimeta, "tabitem_visualOrganization");
					split.setVisible(false);
					Dataset ds = view.getViewModels().getDataset("cp_orgs");
					DataLoadEvent dataLoadEvent = new DataLoadEvent(ds);
					onDataLoad_cporgs(dataLoadEvent);
				}
				if ("2".equals(report_body)) {
					//FIXME
					//onDataLoad_visualOrgs里使用的scapco_visualOrganization表没有数据，并不知道数据在哪维护，暂时弃用     jiangqf6
					/*UIMeta uimeta = getUIMeta();
					UITabItem split = (UITabItem) UIElementFinder.findElementById(
							uimeta, "tabitem_Org");
					split.setVisible(false);
					Dataset ds = view.getViewModels().getDataset("visualOrganization");
					DataLoadEvent dataLoadEvent = new DataLoadEvent(ds);
					onDataLoad_visualOrgs(dataLoadEvent)*/;
					UIMeta uimeta = getUIMeta();
					UITabItem split = (UITabItem) UIElementFinder.findElementById(
							uimeta, "tabitem_visualOrganization");
					split.setVisible(false);
					Dataset ds = view.getViewModels().getDataset("cp_orgs");
					DataLoadEvent dataLoadEvent = new DataLoadEvent(ds);
					onDataLoad_cporgs(dataLoadEvent);
				}
				if ("3".equals(report_body)) {
					Dataset ds1 = view.getViewModels().getDataset("cp_orgs");
					DataLoadEvent dataLoadEvent1 = new DataLoadEvent(ds1);
					onDataLoad_cporgs(dataLoadEvent1);
					
					Dataset ds2 = view.getViewModels().getDataset("visualOrganization");
					DataLoadEvent dataLoadEvent2 = new DataLoadEvent(ds2);
					onDataLoad_visualOrgs(dataLoadEvent2);
				}
			}
	  }
}
