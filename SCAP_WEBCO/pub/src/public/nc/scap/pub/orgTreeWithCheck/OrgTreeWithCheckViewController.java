package nc.scap.pub.orgTreeWithCheck;
import nc.uap.lfw.core.event.TextEvent;
import nc.uap.lfw.core.event.KeyEvent;
import nc.uap.lfw.core.cmd.CmdInvoker;
import java.util.Map;
import nc.vo.pub.lang.UFDateTime;
import nc.uap.lfw.core.event.DataLoadEvent;
import nc.uap.lfw.core.comp.text.TextComp;
import nc.uap.lfw.core.cmd.UifPlugoutCmd;
import nc.uap.lfw.core.cmd.UifDatasetAfterSelectCmd;
import nc.uap.lfw.core.data.FieldSet;
import nc.uap.lfw.core.cmd.UifDatasetLoadCmd;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.comp.TreeViewComp;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import java.util.List;
import nc.scap.pub.util.ScapDAO;
import nc.uap.lfw.core.data.Field;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.uap.lfw.core.comp.ButtonComp;
import nc.uap.lfw.core.event.DialogEvent;
import nc.vo.pub.lang.UFBoolean;
import java.util.Iterator;
import nc.uap.lfw.core.comp.CheckBoxComp;
import uap.web.bd.pub.AppUtil;
import nc.scap.pub.util.CntUtil;
import nc.uap.lfw.core.event.DatasetEvent;
import nc.vo.pub.lang.UFDate;
import nc.uap.lfw.core.event.MouseEvent;
public class OrgTreeWithCheckViewController {
  private static final long serialVersionUID=1L;
  private static final long ID=5L;
  private static String PLUGOUT_ID="afterOkPlugout";
  @SuppressWarnings("unchecked") public void onDataLoad_org_ds(  DataLoadEvent dataLoadEvent){
    Dataset ds = dataLoadEvent.getSource();
		ds.setOrderByPart(" order by code asc");

		String sql = "select distinct o.*, c.orglevel, s.pk_organization, s.pk_fatherorganization,s1.mdmcode,s.mdm_version_max from org_orgs o " +
				" inner join cp_orgs c on o.pk_org = c.pk_org " +
				" left join scapmdm_organization s on o.pk_org = s.pk_org and s.dr=0 " +
				" left join scapmdm_organization s1 on o.pk_org = s1.pk_org " +
				" left join (select max(ts) ts, pk_org from scapmdm_organization group by pk_org) max on max.ts=s1.ts and max.pk_org=s1.pk_org " +
				" where c.dr=0  and c.orglevel ='2' START WITH  c.PK_ORG = '"
				+ CntUtil.getCntOrgPk()
				+ "' CONNECT BY PRIOR c.pk_org=c.pk_fatherorg order by o.code asc";
		List<Map<String, Object>> list = (List<Map<String, Object>>) ScapDAO
				.executeQuery(sql, new MapListProcessor());
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			Iterator<String> keyItor = map.keySet().iterator();
			Row emptyRow = ds.getEmptyRow();

			while (keyItor.hasNext()) {
				String keyName = keyItor.next();
				FieldSet fset = ds.getFieldSet();
				Field field = fset.getField(keyName);
				if (field != null) {
					if ("UFBoolean".equalsIgnoreCase(field.getDataType())) {
						emptyRow.setValue(ds.nameToIndex(keyName),
								UFBoolean.valueOf((String) map.get(keyName)));
					} else if ("UFDate".equalsIgnoreCase(field.getDataType())) {
						emptyRow.setValue(ds.nameToIndex(keyName), new UFDate(
								(String) map.get(keyName)));
					} else if ("UFDateTime".equalsIgnoreCase(field
							.getDataType())) {
						emptyRow.setValue(ds.nameToIndex(keyName),
								new UFDateTime((String) map.get(keyName)));
					} else if ("Integer".equalsIgnoreCase(field.getDataType())) {
						emptyRow.setValue(ds.nameToIndex(keyName),
								(Integer) (map.get(keyName)));
					} else {
						emptyRow.setValue(ds.nameToIndex(keyName),
								(String) map.get(keyName));
					}
				}
			}
			String pk_organization = (String) emptyRow.getValue(ds
					.nameToIndex("pk_organization"));
			if (!"null".equals(pk_organization) && pk_organization != null && pk_organization.trim().length() > 0) {
				emptyRow.setValue(ds.nameToIndex("loadstate"), "（已装载）");
			} else {
				emptyRow.setValue(ds.nameToIndex("loadstate"), "");
			}
			emptyRow.setState(Row.STATE_NORMAL);
			emptyRow.setRowChanged(false);
			ds.addRow(emptyRow);
		}
//		Row[] rows = ds.getAllRow();
//		for(Row row : rows) {
//			row.setState(Row.STATE_NORMAL);
//			row.setRowChanged(false);
//		}
  }
  @SuppressWarnings("unchecked") public void onQueryEnter(  KeyEvent keyEvent){
    TextComp txt = keyEvent.getSource();
		Dataset ds = txt.getView().getViewModels().getDataset("org_ds");
		if (ds == null) {
			return;
		}

		ds.clear();
//		ds.clearSelectedRowData();
//		Row[] rows = ds.getAllRow();
//		for(Row row : rows) {
//			ds.clearSelectedRowData()removeRow(row);
//		}
		final String queryStr = txt.getValue();
		ds.setOrderByPart(" order by code asc");
		String tmpStr = "";
		if (queryStr != null && queryStr.trim().length() > 0) {
			tmpStr = " and ( c.name like '%" + queryStr
					+ "%' or c.code like '%" + queryStr + "%' ) ";
		}
		String sql = "select distinct o.*, c.orglevel, s.pk_organization, s.pk_fatherorganization,s1.mdmcode ,s.mdm_version_max from org_orgs o " +
				" join cp_orgs c on o.pk_org = c.pk_org "
//				+ tmpStr
				+ " left join scapmdm_organization s on o.pk_org = s.pk_org and s.dr=0 "
				+ " left join scapmdm_organization s1 on o.pk_org = s1.pk_org "+
				" left join (select max(ts) ts, pk_org from scapmdm_organization group by pk_org) max on max.ts=s1.ts and max.pk_org=s1.pk_org "
				+ " where c.dr=0  and c.orglevel ='2' and o.pk_org in (select pk_org from (select * from cp_orgs start with pk_org = '" + CntUtil.getCntOrgPk() + "' connect by prior pk_org=pk_fatherorg)) START WITH  1=1 "
				+ tmpStr
				+ " CONNECT BY PRIOR c.pk_fatherorg=c.pk_org order by o.code asc";
		List<Map<String, Object>> list = (List<Map<String, Object>>) ScapDAO
				.executeQuery(sql, new MapListProcessor());
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			Iterator<String> keyItor = map.keySet().iterator();
			Row emptyRow = ds.getEmptyRow();

			while (keyItor.hasNext()) {
				String keyName = keyItor.next();
				FieldSet fset = ds.getFieldSet();
				Field field = fset.getField(keyName);
				if (field != null) {
					if ("UFBoolean".equalsIgnoreCase(field.getDataType())) {
						emptyRow.setValue(ds.nameToIndex(keyName),
								UFBoolean.valueOf((String) map.get(keyName)));
					} else if ("UFDate".equalsIgnoreCase(field.getDataType())) {
						emptyRow.setValue(ds.nameToIndex(keyName), new UFDate(
								(String) map.get(keyName)));
					} else if ("UFDateTime".equalsIgnoreCase(field
							.getDataType())) {
						emptyRow.setValue(ds.nameToIndex(keyName),
								new UFDateTime((String) map.get(keyName)));
					} else if ("Integer".equalsIgnoreCase(field.getDataType())) {
						emptyRow.setValue(ds.nameToIndex(keyName),
								(Integer) (map.get(keyName)));
					} else {
						emptyRow.setValue(ds.nameToIndex(keyName),
								(String) map.get(keyName));
					}
				}
			}
			String pk_organization = (String) emptyRow.getValue(ds
					.nameToIndex("pk_organization"));
			if (!"null".equals(pk_organization) && pk_organization != null && pk_organization.trim().length() > 0) {
				emptyRow.setValue(ds.nameToIndex("loadstate"), "（已装载）");
			} else {
				emptyRow.setValue(ds.nameToIndex("loadstate"), "");
			}
			emptyRow.setState(Row.STATE_NORMAL);
			emptyRow.setRowChanged(false);
			ds.addRow(emptyRow);
		}
//		Row[] rows = ds.getAllRow();
//		for(Row row : rows) {
//			row.setState(Row.STATE_NORMAL);
//			row.setRowChanged(false);
//		}
  }
  public void onOk(  MouseEvent<?> mouseEvent){
	  
    
//    AppLifeCycleContext.current().getApplicationContext()
//	.getCurrentWindowContext().getWindow().setHasChanged(false);
		
		ButtonComp button = (ButtonComp)mouseEvent.getSource();
		Dataset ds = button.getView().getViewModels().getDataset("org_ds");
		Row[] rows = ds.getAllRow();
//		for(Row row : rows) {
//			row.setState(Row.STATE_NORMAL);
//			row.setRowChanged(false);
//		}
//		ds.setRowSelectIndex(0);
		closeView();
		CmdInvoker.invoke(new UifPlugoutCmd(AppUtil.getCntView().getId(),
				PLUGOUT_ID));
//		
//		onDataLoad_org_ds(new DataLoadEvent(ds));
  }
  public void onCancel(  MouseEvent<?> mouseEvent){
    closeView();
  }
  public void closeView(){
//    AppLifeCycleContext.current().getApplicationContext()
//				.getCurrentWindowContext().getWindow().setHasChanged(true);
		AppLifeCycleContext.current().getApplicationContext()
		.getCurrentWindowContext().getWindow().setCtxChanged(false);
//		AppLifeCycleContext.current().getViewContext().getView().setCtxChanged(false);
//    AppLifeCycleContext.current().getViewContext().getView().getPagemeta()
//	.setHasChanged(false);
		AppLifeCycleContext
				.current()
				.getApplicationContext()
				.getCurrentWindowContext()
				.closeView(
						AppLifeCycleContext.current().getViewContext().getId());
		
//		AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
  }
  public void ifWithSubValueChanged(  TextEvent textEvent){
    CheckBoxComp comp = (CheckBoxComp) textEvent.getSource();
		boolean checked = comp.isChecked();
		TreeViewComp orgTree = (TreeViewComp) comp.getView()
				.getViewComponents().getComponent("org_tree");
		if (checked) {
			orgTree.setCheckBoxModel(2);
		} else {
			orgTree.setCheckBoxModel(0);
		}
  }
  public void onclose(  DialogEvent dialogEvent){
//    closeView();
  }
  public void onAfterRowSelect(  DatasetEvent datasetEvent){
    Dataset ds = datasetEvent.getSource();
//	  	CmdInvoker.invoke(new UifDatasetAfterSelectCmd(ds.getId()));
    Row[] rows = ds.getSelectedRows();
    for(Row row : rows) {
    	row.setState(Row.STATE_NORMAL);
    }
  }
}
