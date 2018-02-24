package nc.uap.cpb.org.user;
import java.util.ArrayList;
import java.util.List;

import nc.uap.cpb.log.CpLogger;
import nc.uap.cpb.org.exception.CpbBusinessException;
import nc.uap.cpb.org.util.CpbServiceFacility;
import nc.uap.cpb.org.vos.CpRoleGroupVO;
import nc.uap.cpb.org.vos.CpRoleVO;
import nc.uap.cpb.org.vos.ManageTypeVO;
import nc.uap.lfw.core.cmd.CmdInvoker;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.comp.ReferenceComp;
import nc.uap.lfw.core.ctrl.IController;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.ctx.WindowContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.RowData;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.uap.lfw.core.event.DialogEvent;
import nc.uap.lfw.core.event.MouseEvent;
import nc.uap.lfw.core.event.TabEvent;
import nc.uap.lfw.core.event.TextEvent;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.page.LfwView;
import nc.uap.lfw.core.serializer.impl.SuperVO2DatasetSerializer;
import nc.uap.lfw.jsp.uimeta.UITabComp;
import uap.web.bd.pub.AppUtil;
/** 
 */
public class UserRelateRoleController implements IController {
  private static final long serialVersionUID=1L;
  public static final String PUBLIC_VIEW_ROLE="role";
  public static final String ORG_TEXT="org_text";
  public static final String CUR_ROLEGROUP_TYPE="cur_group_type";
  
  private static final String DS_ROLE = "ds_role";
  private static final String DS_SELECTED_ROLE = "ds_selected_role";
  private WindowContext getCurrentWinCtx(){
    return AppLifeCycleContext.current().getApplicationContext().getCurrentWindowContext();
  }
  public void onCancelClick(  MouseEvent<?> mouseEvent){
    AppLifeCycleContext.current().getApplicationContext().getCurrentWindowContext().closeView(PUBLIC_VIEW_ROLE);
  }
  public void onBeforeShow(  DialogEvent dialogEvent){
    
  }
  public void onAfterRowSelect_rolegroup(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
	CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()){
		protected void postProcessChildRowSelect(Dataset ds) {
			
		}
	});
  }
  public void onOrgvalueChanged(  TextEvent textEvent){
    LfwView role = AppLifeCycleContext.current().getWindowContext().getViewContext(PUBLIC_VIEW_ROLE).getView();	
	  Dataset ds = role.getViewModels().getDataset("ds_rolegroup");
	  ReferenceComp org_text = (ReferenceComp) role.getViewComponents().getComponent("org_text");
	  String pk_org = org_text.getValue();
	  String where = " pk_org = '"+pk_org+"' and type = '"+AppUtil.getAppAttr(CUR_ROLEGROUP_TYPE)+"'";
	  CpRoleGroupVO[] vos = null;
		try {
			vos = CpbServiceFacility.getCpbQryService().queryVOs(CpRoleGroupVO.class, where);
			new SuperVO2DatasetSerializer().serialize(vos, ds, Row.STATE_NORMAL);
			if(ds.getCurrentRowCount() > 0){
				ds.setRowSelectIndex(0);
			}
			else{
				//如果当前集团下没有用户有权限的角色组,则清空右侧的角色表 
				Dataset roleds = role.getViewModels().getDataset("ds_role");
				roleds.setCurrentKey("roleds_clear_key");
			}
		} catch (CpbBusinessException e) {
			CpLogger.error(e.getMessage(), e);
			throw new LfwRuntimeException(e.getMessage());
		}
  }
  public void onRoleDsLoad(  DataLoadEvent dataLoadEvent){
	    CmdInvoker.invoke(new UifDatasetAfterSelectCmd("ds_rolegroup"){
	    	protected int getPageIndex(){
	    		Dataset ds = AppLifeCycleContext.current().getViewContext().getView().getViewModels().getDataset("ds_role");
	    		return ds.getCurrentRowSet().getPaginationInfo().getPageIndex();
	    	}
	    });
	    //解决翻页出现"-1"问题.
//	    ds.setRowSelectIndex(-1);
  }
  public void onAfterTabChange(  TabEvent tabEvent){
    UITabComp tabComp = tabEvent.getSource();
	  String cntTabItemId = tabComp.getCurrentItem();
	  if("0".equals(cntTabItemId))
		  AppUtil.addAppAttr(CUR_ROLEGROUP_TYPE,ManageTypeVO.ADMIN_TYPE_VALUE);
	  else if("1".equals(cntTabItemId))
		  AppUtil.addAppAttr(CUR_ROLEGROUP_TYPE, ManageTypeVO.BUSINESS_TYPE_VALUE);
	  else 
		  AppUtil.addAppAttr(CUR_ROLEGROUP_TYPE, null);
	  LfwView role = getCurrentWinCtx().getViewContext("role").getView();		  
	  ReferenceComp org_text = (ReferenceComp) role.getViewComponents().getComponent("org_text");
	  org_text.setValue("");
//	  org_text.setText("");
	  org_text.setShowValue("");
	  Dataset rolegroupds = role.getViewModels().getDataset("ds_rolegroup");
	  rolegroupds.clear();
	  Dataset roleds = role.getViewModels().getDataset("ds_role");
	  roleds.setCurrentKey("roleds_clear_key");
  }
  public void ondelclick(  MouseEvent mouseEvent){
	  Dataset selectedDs = getDatasetByDsId(DS_SELECTED_ROLE);
	  Row[] selectedRows = selectedDs.getSelectedRows();
	  if(selectedRows != null && selectedRows.length > 0)
		  for(Row row : selectedRows)
			  selectedDs.removeRow(row);
  }
  public void ondelallclick(  MouseEvent mouseEvent){
	  Dataset selectedDs = getDatasetByDsId(DS_SELECTED_ROLE);
	  selectedDs.clear();
  }
  public void onaddallclick(  MouseEvent mouseEvent){
	  RowData rowData = getDatasetByDsId(DS_ROLE).getCurrentRowData();
	  if(rowData != null){
		  Row[] rows = rowData.getRows();
		  addRows(rows);
	  }
  }
  public void onaddclick(  MouseEvent mouseEvent){
	  Row[] rows = getDatasetByDsId(DS_ROLE).getSelectedRows();
	  addRows(rows);
  }
  
  private Dataset getDatasetByDsId(String dsId){
	  return getCurrentWinCtx().getViewContext("role").getView().getViewModels().getDataset(dsId);
  }
  
  private boolean isSelected(Row row,List<String> selectedList){
	  boolean isSelected = false;
	  String pk_role = row.getString(getDatasetByDsId(DS_SELECTED_ROLE).nameToIndex(CpRoleVO.PK_ROLE));
	  if(pk_role != null){
		 if(selectedList != null && selectedList.contains(pk_role))
			 isSelected = true;
	  }
	  return isSelected;
  }

  private void addRows(Row[] rows){
	  if(rows != null && rows.length > 0){
		  List<String> selectedList = null;
		  Row[] selectedrows = null;
		  RowData selectedRowData = getDatasetByDsId(DS_SELECTED_ROLE).getCurrentRowData();
		  if(selectedRowData != null)
			  selectedrows = selectedRowData.getRows();
		  if(selectedrows != null && selectedrows.length > 0){
			  selectedList = new ArrayList<String>(selectedrows.length);
			  for(Row row : selectedrows)
				  selectedList.add(row.getString(getDatasetByDsId(DS_SELECTED_ROLE).nameToIndex(CpRoleVO.PK_ROLE)));
		  }
		  Dataset seletedDs = getDatasetByDsId(DS_SELECTED_ROLE);
		  for(Row row : rows){
			  if(!isSelected(row, selectedList))
			  	seletedDs.addRow(row);
		  }
	  }
	  
  }
}
